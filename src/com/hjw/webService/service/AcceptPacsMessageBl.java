package com.hjw.webService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.service.pacsbean.ResPacsMessage;
import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;

public class AcceptPacsMessageBl extends ServletEndpointSupport{

	private CommService commService;
	private static ConfigService configService;
	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
	}

//	public String acceptMessageTest(String xmlmessage) {
//		return "返回ok----pacs结果回传" + xmlmessage;
//	}
	
	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String accetpMessagePacs(String xmlmessage) {
		String filetpe = "respacsbl";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResPacsMessage rpm = new ResPacsMessage(xmlmessage, true);
			RetPacsCustome rc = new RetPacsCustome();
			rc = rpm.getRetPacsCustome();
			try {
				int pic_num = 0;
				String exam_num = rc.getCustome_id();
				if ((exam_num != null) && (exam_num.trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.commService.getExamInfoForNum(exam_num);
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 查无此人，入库错误" + exam_num);
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 已经总检，入账错误" + exam_num);
					} else {
						ProcPacsResult plr = new ProcPacsResult();
						plr.setExam_num(exam_num);
						plr.setPacs_req_code(rc.getPacs_summary_id());
						plr.setCheck_date(fomartDate(rc.getDoctor_time_bg()));
						plr.setCheck_doct(rc.getDoctor_name_bg());
						plr.setAudit_date(fomartDate(rc.getDoctor_time_sh()));
						plr.setAudit_doct(rc.getDoctor_name_sh());
						String datatime = DateTimeUtil.shortFmt4(DateTimeUtil.parse(rc.getEffectiveTime()));
						String depnum = this.commService.getDepNumForPacs(rc.getPacs_summary_id());
						String picpath = this.commService.getDatadis("TPLJ").getName();
						boolean flagss = true;
						for (RetPacsItem rpi : rc.getList()) {
							plr.setExam_desc(rpi.getChargingItem_ms());
							plr.setExam_result(rpi.getChargingItem_jl());
							if (rpi.getBase64_bg().length() > 10) {
								pic_num++;
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

								picpath = picpath + "\\" + rc.getPacs_summary_id();
								picname = picname + "/" + rc.getPacs_summary_id();
								f = new File(picpath);
								if (!f.exists() && !f.isDirectory())
									f.mkdir();
								String filepath = rc.getPacs_summary_id() + "-" + Timeutils.getFileData() + "_"
										+ pic_num + ".pdf";
								String jpgpath = rc.getPacs_summary_id() + "_" + pic_num;
								String pdfpath = picpath + "\\" + filepath;
								picpath = picpath + "\\" + jpgpath;
								picname = picname + "/" + jpgpath;
								FileOutputStream fos = null;
								try {
									f = new File(pdfpath);
									if (f.exists() && f.isFile())
										f.delete();
									new HttpDownloader(rpi.getBase64_bg(),pdfpath,filetpe).download();
//									String czyxm= configService.getCenterconfigByKey("IS_DBGJ_PACS_TYPE").getConfig_value().trim();//东北国际病例执行科室
//									if("2".equals(czyxm)){
//										new HttpDownloader(rpi.getBase64_bg(),pdfpath,filetpe).download();
//									} else {
//										fos = new FileOutputStream(pdfpath);
//										// 用FileOutputStream 的write方法写入字节数组
//										byte[] bmpfiledata64 = Base64.base64Decode(rpi.getBase64_bg());
//										fos.write(bmpfiledata64);
//									}
								} catch (Exception ex) {
									ex.printStackTrace();
								} finally {
									if (fos != null) {
										fos.close();
									}
								}
								PdfToJpg pjpg = new PdfToJpg();
								int picnum = pjpg.pdf2jpg(pdfpath, picpath);
								String file_img = "";
								for (int j = 1; j <= picnum; j++) {
									file_img = file_img + ";" + picname + "_" + j + ".jpg";
								}
								if (file_img.length() > 1) {
									file_img = file_img.substring(1, file_img.length());
								}
								plr.setImg_file(file_img);
								
								try{
								    File file = new File(pdfpath);
								    file.delete();
								}catch(Exception e){
									//TranLogTxt.liswriteEror_to_txt(logname, "res:pdf删除失败"+com.hjw.interfaces.util.StringUtil.formatException(e));
								}
							}
							int falgint = commService.proc_pacs_report_dbgj(plr);
							if (falgint != 0) {
								flagss = false;
							}
						}
						if (flagss) {
							ResultHeader.setTypeCode("AA");
							ResultHeader.setText("交易成功");
						} else {
							ResultHeader.setTypeCode("AE");
							ResultHeader.setText("pacs 入库失败");
						}
					}
				} else {
					ResultHeader.setTypeCode("AE");
					ResultHeader.setText("pacs信息 体检编号为空");
					frb.setResultHeader(ResultHeader);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("pacs信息调用webservice错误");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("信息 xml解析错误");
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}
	
	private String fomartDate(String datetime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = sdf.parse(datetime);
		SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datet = sdfs.format(date);
		return datet;
	}
}
