package com.hjw.webService.client.hanshou;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.remoting.jaxrpc.ServletEndpointSupport;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.hanshou.bean.ExamInfoUserDTOHS;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PacsResMessageHS_bak extends ServletEndpointSupport{
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static CommService commService;
    
	static {
    	init();
    }
	  
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		commService = (CommService) wac.getBean("commService");
		
	}

	public String getMessage(String strbody) {
		String logName="PacsRes";
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + strbody);
		
		String ResMessage="";
		PacsResult pacsResult =  getPacsResXML(strbody,logName);
		try {
			if((pacsResult.getStatus()+"").equals("0")){
				if ((pacsResult.getReq_no() != null) && (pacsResult.getReq_no().trim().length() > 0)) {
					ExamInfoUserDTOHS ei = new ExamInfoUserDTOHS();
					ei = this.getExamInfoForReqNum(pacsResult.getReq_no());
					if ((ei == null) || (ei.getId() <= 0)) {
						
						
						ResMessage="error-pacs信息 查无此申请单号" + pacsResult.getReq_no();
					} else if ("Z".equals(ei.getStatus())) {
				
						ResMessage="error-pacs信息 已经总检，入库错误" + pacsResult.getReq_no();
					} else {
						
						boolean succ = this.configService.insert_pacs_result(pacsResult);
						if (succ) {
							
							ResMessage="success-入库成功" ;
						} else {
							ResMessage="error-入库失败" ;
						}
					}
				} else {
					ResMessage="error-pacs信息 体检编号为空" ;
				}
			}else{
				ResMessage="error-pacs信息 xml解析错误" ;
				
			}
			
		} catch (Exception ex) {
			
			ResMessage="error-pacs信息 xml解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex) ;
		}
		TranLogTxt.liswriteEror_to_txt(logName, "res:" + ResMessage);
		return ResMessage;
	}
	public ExamInfoUserDTOHS getExamInfoForReqNum(String req_nums) throws ServiceException{
		String sql = " select m.id,m.age,m.exam_num,m.status,m.exam_type "
				+ " ,m.register_date,m.join_date,m.exam_times "
				+ " ,n.user_name,n.id_num,n.sex,n.birthday,n.phone "
				+ " from examinfo_charging_item a,exam_info m,customer_info n "
				+ " ,pacs_summary b,pacs_detail c,charging_item d " + " where b.pacs_req_code='" + req_nums
				+ "' and c.summary_id=b.id and c.chargingItem_num=d.item_code "
				+ " and d.id=a.charge_item_id and a.examinfo_id=m.id "
				+ " and m.exam_num=b.examinfo_num and a.isActive='Y' "
				+ " and m.customer_id = n.id";
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTOHS.class);
		ExamInfoUserDTOHS eu = new ExamInfoUserDTOHS();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTOHS)map.getList().get(0);			
		}
		return eu;
	}
	private PacsResult getPacsResXML(String xmlstr, String logName) {
		PacsResult pacsResult = new PacsResult();
		try {
			
			InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			
			
			pacsResult.setTil_id(logName);
			pacsResult.setExam_num("");
			pacsResult.setReq_no(document.selectSingleNode("root/applyNos/applyNo").getText());//
			pacsResult.setPacs_checkno(document.selectSingleNode("root/pacsNo").getText());//报告id
			pacsResult.setReg_doc(document.selectSingleNode("root/examOperator").getText());//记录医生姓名	
			pacsResult.setCheck_doc(document.selectSingleNode("root/examOperator").getText());//检查医生姓名	
			pacsResult.setCheck_date(document.selectSingleNode("root/examTime").getText());//检查时间	
			pacsResult.setReport_doc(document.selectSingleNode("root/reportOperator").getText());//报告医生	
			pacsResult.setReport_date(document.selectSingleNode("root/reportTime").getText());;//报告时间	
			pacsResult.setAudit_doc(document.selectSingleNode("root/auditOperator").getText());//审核医师	pdfPath
			pacsResult.setAudit_date(document.selectSingleNode("root/auditTime").getText());//审核时间	
			String pdfurl = document.selectSingleNode("root/pdfPath").getText();//pdf  路径
			String datatime = DateTimeUtil.shortFmt4(new Date());
			ExamInfoUserDTOHS ei = getExamInfoForReqNum(pacsResult.getReq_no());
			
			if(pdfurl.length()>0){
				
				String picpath = this.commService.getDatadis("TPLJ").getName();
				File f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				picpath = picpath + "\\pacs_img";
				String picname = "/pacs_img";
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();
				picpath = picpath + "\\" + datatime;
				picname = picname + "/" + datatime;
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				picpath = picpath + "\\" + ei.getDep_num();
				picname = picname + "/" + ei.getDep_num();
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				picpath = picpath + "\\" + ei.getExam_num();
				picname = picname + "/" + ei.getExam_num();
				f = new File(picpath);
				if (!f.exists() && !f.isDirectory())
					f.mkdir();

				
				String pdfpath = picpath + ".pdf";
				String jpgpath = picpath + ".jpg";
			
				FileOutputStream fos = null;
				try {
					if (new HttpDownloader(pdfurl, pdfpath, logName).download()) {
						File file = new File(pdfpath);
						if (file.exists() && file.isFile()) {
							pacsResult.setImg_path(picname+ ".jpg");
							pacsResult.setReport_img_path(picname+ ".jpg");
							String fileName = file.getName();
							TranLogTxt.liswriteEror_to_txt(logName,
									"res:文件----" + fileName + "的大小是：" + file.length());
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (fos != null) {
						fos.close();
					}
				}
			
			
				PdfToJpg pjpg = new PdfToJpg();
				int picnum = pjpg.pdf2jpg(pdfpath, picpath);
			
			}
				
			pacsResult.setClinic_diagnose(document.selectSingleNode("root/examResult").getText());//结论
			pacsResult.setClinic_symptom("");
			pacsResult.setStudy_body_part(document.selectSingleNode("root/examPart").getText());//部位
			
			pacsResult.setPacs_item_code(document.selectSingleNode("root/itemCode").getText());//检查项目编码 his_num item_code
			pacsResult.setIs_tran_image(0);
			pacsResult.setStatus(0);
		
			}catch(Exception ex){
				pacsResult.setStatus(1);
				ex.printStackTrace();
			}
		return pacsResult;
	}

}
