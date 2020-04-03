package com.hjw.webService.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.haijie.pacsbean.ResPacsMessageHJ;
import com.hjw.webService.client.haijie.pacsbean.RetPacsCustomeHJ;
import com.hjw.webService.client.haijie.pacsbean.RetPacsPicHJ;
import com.hjw.webService.service.pacsbean.ResPacsMessage;
import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;
import com.synjones.framework.persistence.JdbcQueryManager;

public class AcceptPacsMessage extends ServletEndpointSupport {
	private CommService commService;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	protected void onInit() {
		this.commService = (CommService) getWebApplicationContext().getBean("commService");
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----pacs结果回传" + xmlmessage;
	}

	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String accetpMessagePacs(String xmlmessage) {
		String filetpe = "respacs";
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
				String exam_num = rc.getExam_num();
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
						plr.setCheck_date(rc.getDoctor_time_bg());
						plr.setCheck_doct(rc.getDoctor_name_bg());
						plr.setAudit_date(rc.getDoctor_time_sh());
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
								
								
								String IS_PACS_PDF_DBGJ_VERSION="1";								
								try{
								   IS_PACS_PDF_DBGJ_VERSION = configService.getCenterconfigByKey("IS_PACS_PDF_DBGJ_VERSION").getConfig_value().trim();//开单医生id
								}catch(Exception ex){}
								   
								   if("2".equals(IS_PACS_PDF_DBGJ_VERSION)) {
									try {
										f = new File(pdfpath);
										if (f.exists() && f.isFile())
											f.delete();
										fos = new FileOutputStream(pdfpath);
										// 用FileOutputStream 的write方法写入字节数组
										String IS_PACS_PDF_DBGJ_URL="http://192.168.111.46:1501/ReportShow";
										try{
											IS_PACS_PDF_DBGJ_URL = configService.getCenterconfigByKey("IS_PACS_PDF_DBGJ_URL").getConfig_value().trim();//开单医生id
										}catch(Exception ex){}
											if (new HttpDownloader(IS_PACS_PDF_DBGJ_URL+"/"+rpi.getBase64_bg(), pdfpath, filetpe).download()) {
												File file = new File(pdfpath);
												if (file.exists() && file.isFile()) {
													String fileName = file.getName();
													TranLogTxt.liswriteEror_to_txt(filetpe,
															"res:文件----" + fileName + "的大小是：" + file.length());
													boolean writeflag = false;
													if (file.length() / 1024 < 5) {
														TranLogTxt.liswriteEror_to_txt(filetpe,
																"res:小文件----" + fileName + "的大小是：" + file.length());
														writeflag = checkPDF(pdfpath, filetpe);
														TranLogTxt.liswriteEror_to_txt(filetpe, "判断文件是否是pdf：" + writeflag);
													} else {
													 	writeflag = true;
													}
													if (writeflag) {
														PdfToJpg pjpg = new PdfToJpg();
														TranLogTxt.liswriteEror_to_txt(filetpe, "res:pdf文件生产jpg开始");
														TranLogTxt.liswriteEror_to_txt(filetpe, "res:pdf文件" + pdfpath);
														TranLogTxt.liswriteEror_to_txt(filetpe, "res:jpg路径" + picpath);
														int picsize = 200;
														int picnum = 0;														
														picnum = pjpg.pdf2jpg1(pdfpath, picpath,100);
														TranLogTxt.liswriteEror_to_txt(filetpe, "res:pdf文件生产jpg完成");
														String file_img = "";
														for (int j = 1; j <= picnum; j++) {
															file_img = file_img + ";" + picname + "_" + j + ".jpg";
														}
														plr.setImg_file(file_img);
														try{
														    file.delete();
														}catch(Exception e){
															TranLogTxt.liswriteEror_to_txt(filetpe, "res:pdf删除失败"+com.hjw.interfaces.util.StringUtil.formatException(e));
														}
													} else {
														file.delete();
													}
												}

											}
										} catch (Exception ex) {
											ex.printStackTrace();
										} finally {
											if (fos != null) {
												fos.close();
											}
										}
								}else{
									try {
										f = new File(pdfpath);
										if (f.exists() && f.isFile())
											f.delete();
										fos = new FileOutputStream(pdfpath);
										// 用FileOutputStream 的write方法写入字节数组
										byte[] bmpfiledata64 = Base64.base64Decode(rpi.getBase64_bg());
										fos.write(bmpfiledata64);
										System.out.println("写入成功");
									} catch (Exception ex) {
										ex.printStackTrace();
									} finally {
										if (fos != null) {
											fos.close();
										}
									}

									PdfToJpg pjpg = new PdfToJpg();
									int picnum = pjpg.pdf2jpg1(pdfpath, picpath,100);
									String file_img = "";
									for (int j = 1; j <= picnum; j++) {
										file_img = file_img + ";" + picname + "_" + j + ".jpg";
									}
									if (file_img.length() > 1) {
										file_img = file_img.substring(1, file_img.length());
									}
									plr.setImg_file(file_img);
								
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

/**  
 * 利用itext打开pdf文档  
 */  
private static boolean checkPDF(String file,String logname) { 
	TranLogTxt.liswriteEror_to_txt(logname, "res:" + file);
    boolean flag1 = false;  
    int n = 0;  
    try {  
        Document document = new Document(new PdfReader(file).getPageSize(1));  
        document.open();  
        PdfReader reader = new PdfReader(file);  
        n = reader.getNumberOfPages();  
        if (n != 0)  
            flag1 = true;  
        document.close();  
    } catch (IOException e) {  
         

    }  
    return flag1;    
}  
	
	/**
	 * 
	 * @Title: accetpMessagePacsHJ @Description: pacs海捷
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String accetpMessagePacsHJ(String xmlmessage) {
		String filetpe = "respacsHJ";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResPacsMessageHJ rpm = new ResPacsMessageHJ(xmlmessage, true);
			RetPacsCustomeHJ rc = new RetPacsCustomeHJ();
			rc = rpm.getRetPacsCustome();
			try {
				int pic_num = 0;
				String pacs_summary_id = rc.getPacs_summary_id();
				if ((pacs_summary_id != null) && (pacs_summary_id.trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.commService.getExamInfoForReqNum(pacs_summary_id);
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 查无此人，入库错误" + pacs_summary_id);
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 已经总检，入账错误" + pacs_summary_id);
					} else {
						ProcPacsResult plr = new ProcPacsResult();
						plr.setExam_num(ei.getExam_num());
						plr.setPacs_req_code(rc.getPacs_summary_id());
						plr.setCheck_date(rc.getDoctor_time_bg());
						plr.setCheck_doct(rc.getDoctor_name_bg());
						plr.setAudit_date(rc.getDoctor_time_sh());
						plr.setAudit_doct(rc.getDoctor_name_sh());
						plr.setExam_desc(rc.getPacsItem().getChargingItem_ms());
						plr.setExam_result(rc.getPacsItem().getChargingItem_jl());
						
						String url = "";
						for (RetPacsPicHJ rpi : rc.getPacsItem().getListRetPacsPic()) {
							if (rpi.getPicURL().length() > 10) {
								url += (rpi.getPicURL()+";");
							}
						}
						plr.setImg_file(url.substring(0,url.length()-1));
						int falgint = commService.proc_pacs_report_dbgj(plr);
						if (falgint == 0) {
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
				TranLogTxt.liswriteEror_to_txt(filetpe, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("pacs信息调用webservice错误");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(filetpe, "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("信息 xml解析错误");
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}

}
