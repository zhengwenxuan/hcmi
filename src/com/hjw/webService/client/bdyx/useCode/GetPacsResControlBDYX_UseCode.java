package com.hjw.webService.client.bdyx.useCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.bdyx.bean.pacs.res.ExamItem;
import com.hjw.webService.client.bdyx.bean.pacs.res.PacsResultBDYX;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.service.CommService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetPacsResControlBDYX_UseCode {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CommService commService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		commService = (CommService) wac.getBean("commService");
	}
    public ResultHeader getMessage(String body, String logname){
    	ResultHeader rb = new ResultHeader();
    	PacsResult pacsResult = new PacsResult();
    	try {
    		PacsResultBDYX pacsResultBDYX = new Gson().fromJson(body, PacsResultBDYX.class);
    		logname = pacsResultBDYX.getVisitOrdNo()+"-"+logname;
        	TranLogTxt.liswriteEror_to_txt(logname, "body:"+body);
        	
    		String exam_num = pacsResultBDYX.getVisitOrdNo();
    		String req_code = pacsResultBDYX.getOrderLid().get(0);
    		
    		String reportDate = pacsResultBDYX.getReportDoctors().get(0).getReportDate();
    		if(!StringUtil.isEmpty(reportDate) && reportDate.length()==14) {
    			reportDate = reportDate.substring(0, 4)+"-"+reportDate.substring(4, 6)+"-"+reportDate.substring(6, 8)+" "+reportDate.substring(8, 10)+":"+reportDate.substring(10, 12)+":"+reportDate.substring(12, 14);
    		} else {
    			pacsResult.setNote("reportDate不符合规范");
    			pacsResult.setStatus(3);
    		}
    		String reviewDate = pacsResultBDYX.getReviewDoctors().get(0).getReviewDate();
    		if(!StringUtil.isEmpty(reviewDate) && reviewDate.length()==14) {
    			reviewDate = reviewDate.substring(0, 4)+"-"+reviewDate.substring(4, 6)+"-"+reviewDate.substring(6, 8)+" "+reviewDate.substring(8, 10)+":"+reviewDate.substring(10, 12)+":"+reviewDate.substring(12, 14);
    		} else {
    			pacsResult.setNote("reviewDate不符合规范");
    			pacsResult.setStatus(3);
    		}
    		
    		pacsResult.setTil_id(logname);
    		pacsResult.setExam_num(exam_num);
    		pacsResult.setReq_no(req_code);
    		pacsResult.setPacs_checkno(pacsResultBDYX.getExamReportLid());
    		pacsResult.setReg_doc(pacsResultBDYX.getReportDoctors().get(0).getReportDoctorName());
    		pacsResult.setCheck_doc(pacsResultBDYX.getReportDoctors().get(0).getReportDoctorName());
    		pacsResult.setCheck_date(reportDate);
    		pacsResult.setReport_doc(pacsResultBDYX.getReportDoctors().get(0).getReportDoctorName());
    		pacsResult.setReport_date(reportDate);
    		pacsResult.setAudit_doc(pacsResultBDYX.getReviewDoctors().get(0).getReviewDoctorName());
    		pacsResult.setAudit_date(reviewDate);
    		String datetime = pacsResultBDYX.getReportDoctors().get(0).getReportDate().substring(0,8);
    		
    		ExamItem examItem = pacsResultBDYX.getExamItems().get(0);
    		pacsResult.setClinic_symptom(examItem.getImagingFinding());
    		pacsResult.setClinic_diagnose(examItem.getImagingConclusion());
    		pacsResult.setStudy_body_part(examItem.getTargetSiteName());
    		pacsResult.setStudy_type(examItem.getExaminationMethodName());
    		pacsResult.setItem_name(examItem.getItemName());
    		pacsResult.setPacs_item_code(examItem.getItemCode());
    		
    		if (pacsResultBDYX.getDocImageContent().length() > 10) {
    			String picname = "";
    			//
    			TranLogTxt.liswriteEror_to_txt(logname, "req_code:"+req_code);
    			if(pacsResultBDYX.getDocImageFormat().contains("jpeg")) {
    				picname = PacsPictureDecodeBase64Util.decodeBase64JPG(exam_num, req_code, datetime, pacsResultBDYX.getDocImageContent());
    			} else if(pacsResultBDYX.getDocImageFormat().contains("pdf")) {
    				picname = decodeBase64PDF(exam_num, req_code, datetime, pacsResultBDYX.getDocImageContent());
    			} else {
    				pacsResult.setNote("docImageFormat不符合规范");
        			pacsResult.setStatus(3);
    			}
    			pacsResult.setIs_tran_image(0);
    			pacsResult.setReport_img_path(picname);
    		} else {
    			pacsResult.setIs_tran_image(0);
    			pacsResult.setNote("docImageContent不符合规范");
    			pacsResult.setStatus(3);
    		}
    	} catch (Exception e) {
			e.printStackTrace();
			rb.setTypeCode("AE");
			rb.setText("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			pacsResult.setStatus(3);
			pacsResult.setNote("Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		}
    	boolean succ = this.configService.insert_pacs_result(pacsResult);
    	if (succ) {
    		rb.setTypeCode("AA");
    		rb.setText("交易成功");
    	} else {
    		rb.setTypeCode("AE");
    		rb.setText("pacs 入库失败");
    	}
    	String xml = JaxbUtil.convertToXmlWithOutHead(rb, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"res:"+xml);
    	return rb;    	
    }
    
    private static String decodeBase64PDF(String exam_num, String req_code, String datetime, String base64Str) throws IOException {
		String depnum = commService.getDepNumForPacs(req_code);
		TranLogTxt.liswriteEror_to_txt("resLisPacs", "depnum:"+depnum);
		String picpath = commService.getDatadis("TPLJ").getName();;
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\pacs_img";
		String picname = "pacs_img";
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\" + datetime;
		picname = picname + "/" + datetime;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + depnum;
		picname = picname + "/" + depnum;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + exam_num;
		picname = picname + "/" + exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + req_code;
		picname = picname + "/" + req_code;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		String filepath = req_code + "-" + Timeutils.getFileData() + ".pdf";
		String jpgpath = req_code;
		String pdfpath = picpath + "\\" + filepath;
		picpath = picpath + "\\" + jpgpath;
		picname = picname + "/" + jpgpath;
		FileOutputStream fos = null;
		try {
			f = new File(pdfpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(pdfpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		TranLogTxt.liswriteEror_to_txt("resLisPacs", "pdfpath:"+pdfpath);
		TranLogTxt.liswriteEror_to_txt("resLisPacs", "picpath:"+picpath);
		PdfToJpg pjpg = new PdfToJpg();
		int picnum = pjpg.pdf2jpg1(pdfpath, picpath, 100);
		String file_img = "";
		for (int j = 1; j <= picnum; j++) {
			file_img = file_img + ";" + picname + "_" + j + ".jpg";
		}
		if (file_img.length() > 1) {
			file_img = file_img.substring(1, file_img.length());
		}
		if (f.exists() && f.isFile())
			f.delete();
		return file_img;
	}
}
