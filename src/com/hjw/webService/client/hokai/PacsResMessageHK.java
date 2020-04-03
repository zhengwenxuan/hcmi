package com.hjw.webService.client.hokai;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.HttpServer.HttpDownloader;
import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.hokai.bean.ResPacsMessageHK;
import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PacsResMessageHK{
	private static CommService commService;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
    
	  static {
	    	init();
	    	}
	  
	public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			commService = (CommService) wac.getBean("commService");
			configService = (ConfigService) wac.getBean("configService");
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}

	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public String getMessage(String xmlmessage,String logName) {
		Calendar deadline = Calendar.getInstance();
		deadline.set(2099, Calendar.FEBRUARY, 23, 0, 0, 0);
		if(new Date().after(deadline.getTime())) {
			ResultBody frb = new ResultBody();
			frb.getResultHeader().setTypeCode("AE");
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			frb.getResultHeader().setText("接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			TranLogTxt.liswriteEror_to_txt(logName,"接口已过期，请联系火箭蛙，截止日期："+df.format(deadline.getTime()));
			String reqxml = getres(frb);
			TranLogTxt.liswriteEror_to_txt(logName, "req:" + reqxml);
			return reqxml;
		}
		
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResPacsMessageHK rpm = new ResPacsMessageHK(xmlmessage, true);
			RetPacsCustome rc = new RetPacsCustome();
			rc = rpm.getRetPacsCustome();
			try {
				String req_no = rc.getPacs_summary_id();
				if ((req_no != null) && (req_no.trim().length() > 0)) {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = this.configService.getExamInfoForReqNum(req_no);
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 查无此申请单号" + req_no);
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 已经总检，入账错误" + req_no);
					} else {
						ProcPacsResult plr = new ProcPacsResult();
						plr.setExam_num(ei.getExam_num());
						plr.setPacs_req_code(rc.getPacs_summary_id());
						plr.setCheck_date(changeFormat(rc.getDoctor_time_bg()));
						plr.setCheck_doct(rc.getDoctor_name_bg());
						plr.setAudit_date(changeFormat(rc.getDoctor_time_sh()));
						plr.setAudit_doct(rc.getDoctor_name_sh());
						String datatime = DateTimeUtil.shortFmt4(new Date());
						String dept_num = this.commService.getDepNumForPacs(rc.getPacs_summary_id());
						boolean flagss = true;
						int pic_num = 0;
						for (RetPacsItem rpi : rc.getList()) {
							plr.setExam_desc(rpi.getChargingItem_ms());
							plr.setExam_result(rpi.getChargingItem_jl());
							byte[] bmpURL = Base64.base64Decode(rpi.getBase64_bg());
							String file_img = "";
							String imgURL = new String(bmpURL);
							
							System.out.println(imgURL);
							//imgURL = "http://localhost:8080/picture/pacs_img/20180802/EL/201807270001/18072700003/18072700003_1.jpg";
							
							if (rpi.getBase64_bg().length() > 10) {
								String picpath = this.commService.getDatadis("TPLJ").getName();
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

								picpath = picpath + "\\" + dept_num;
								picname = picname + "/" + dept_num;
								f = new File(picpath);
								if (!f.exists() && !f.isDirectory())
									f.mkdir();

								picpath = picpath + "\\" + ei.getExam_num();
								picname = picname + "/" + ei.getExam_num();
								f = new File(picpath);
								if (!f.exists() && !f.isDirectory())
									f.mkdir();

								picpath = picpath + "\\" + rc.getPacs_summary_id();
								picname = picname + "/" + rc.getPacs_summary_id();
//								f = new File(picpath);
//								if (!f.exists() && !f.isDirectory())
//									f.mkdir();
								// String filepath = pacs_summary_id + "-" +
								// Timeutils.getFileData() + "_"+ pic_num + ".jpg";
								// String jpgpath = pic_num+".jpg";
								String jpgpath = picpath + ".jpg";

								FileOutputStream fos = null;
								try {
									if (new HttpDownloader(imgURL, jpgpath, logName).download()) {
										File file = new File(jpgpath);
										if (file.exists() && file.isFile()) {
											plr.setImg_file(picname+ ".jpg");
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
								TranLogTxt.liswriteEror_to_txt(logName, "res::操作语句： " + file_img);
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
				ResultHeader.setText("pacs信息调用webservice错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("pacs信息 xml解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = getres(frb);
		TranLogTxt.liswriteEror_to_txt(logName, "req:" + orderid + ":" + reqxml);
		return reqxml;
	}
	
	private String getres(ResultBody rh){
		StringBuffer sb=new StringBuffer();
		sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		sb.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>\n");
		sb.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("	<interactionId extension=\"S0071\" root=\"2.16.840.1.113883.1.6\"/>\n");
		sb.append("	<processingCode code=\"P\"/>\n");
		sb.append("	<processingModeCode/>\n");
		sb.append("	<acceptAckCode code=\"AL\"/>\n");
		sb.append("	<receiver typeCode=\"RCV\">\n");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("			<id>\n");
		sb.append("				<item extension=\"SYS001\"/>\n");
		sb.append("			</id>\n");
		sb.append("		</device>\n");
		sb.append("	</receiver>\n");
		sb.append("	<sender typeCode=\"SND\">\n");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("			<id>\n");
		sb.append("				<item extension=\"SYS009\"/>\n");
		sb.append("			</id>\n");
		sb.append("		</device>\n");
		sb.append("	</sender>\n");
		sb.append("	<!--AA成功，AE失败-->\n");
		sb.append("	<acknowledgement typeCode=\""+rh.getResultHeader().getTypeCode()+"\">\n");
		sb.append("		<!--请求消息ID-->\n");
		sb.append("		<targetMessage>\n");
		sb.append("			<id extension=\""+rh.getResultHeader().getSourceMsgId()+"\"/>\n");
		sb.append("		</targetMessage>\n");
		sb.append("		<acknowledgementDetail>\n");
		sb.append("			<text value=\""+rh.getResultHeader().getText()+"\"/>\n");
		sb.append("		</acknowledgementDetail>\n");
		sb.append("	</acknowledgement>\n");
		sb.append("</MCCI_IN000002UV01>\n");

		return sb.toString();
	}
	
    public static String changeFormat(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8) + " " + date.substring(8, 10) + ":" + date.substring(10, 12) + ":"
				+ date.substring(12, 14);
	}
}
