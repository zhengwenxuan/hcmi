package com.hjw.webService.client.fangzheng;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.PdfToJpg;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.job.fangzheng.ResPacsMessageFZ;
import com.hjw.webService.service.pacsbean.ResPacsMessage;
import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcPacsResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PacsResMessageFZ{
	private static CommService commService;
	private static JdbcQueryManager jdbcQueryManager;
    
	  static{
	    	init();
	    	}
	public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			commService = (CommService) wac.getBean("commService");
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}

	/**
	 * 
	 * @Title: accetpMessagePacs @Description: pacs
	 * 结果返回处理 @param: @return @return: String @throws
	 */
	public ResultHeader accetpMessagePacs(String xmlmessage) {
		String filetpe = "respacs";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResPacsMessageFZ rpm = new ResPacsMessageFZ(xmlmessage, true);
			RetPacsCustome rc = new RetPacsCustome();
			rc = rpm.getRetPacsCustome();
			try {
				int pic_num = 0;
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				ei = getExamInfoForNum(rc.getCustome_id(),rc.getCoustom_jzh());
					if ((ei == null) || (ei.getId() <= 0)) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 查无此人，入库错误" + rc.getCustome_id()+","+rc.getCoustom_jzh());
					} else if ("Z".equals(ei.getStatus())) {
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("pacs信息 已经总检，入账错误" + rc.getCustome_id()+","+rc.getCoustom_jzh());
					} else {
						String pacsid = getreqIn_pacs_item(rc.getPacs_summary_id(),filetpe);
						rc.setPacs_summary_id(pacsid);
					if (pacsid.trim().length() > 0) {
						ProcPacsResult plr = new ProcPacsResult();
						plr.setExam_num(ei.getExam_num());
						plr.setPacs_req_code(rc.getPacs_summary_id());
						//plr.setCheck_date(rc.getDoctor_time_bg());
						plr.setCheck_doct(rc.getDoctor_name_bg());
						//plr.setAudit_date(rc.getDoctor_time_sh());
						plr.setAudit_doct(rc.getDoctor_name_sh());
						
						  Date parse5 = parse5(rc.getDoctor_time_bg());
                          plr.setCheck_date(shortFmt2(parse5));                          
                          parse5 = parse5(rc.getDoctor_time_sh());
                          plr.setAudit_date(shortFmt2(parse5));
						
						String datatime = DateTimeUtil.getDate();//(DateTimeUtil.parse(rc.getEffectiveTime()));
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

								picpath = picpath + "\\" + ei.getExam_num();
								picname = picname + "/" + ei.getExam_num();
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
								String jpgfile="";
								if("image/jpeg".equals(rpi.getImagetype().trim().toLowerCase())){
									filepath = rc.getPacs_summary_id() + "-" + Timeutils.getFileData() + "_"
											+ pic_num + ".jpg";
									jpgfile=picname+"/"+filepath;
								}
								String jpgpath = rc.getPacs_summary_id() + "_" + pic_num;
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
								if("image/jpeg".equals(rpi.getImagetype().trim().toLowerCase())){
									plr.setImg_file(jpgfile);
								} else {
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
					}else{
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("查询申请单号错误");
					}
					}
			} catch (Exception ex) {
				ex.printStackTrace();
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("pacs信息调用webservice错误"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("信息 xml解析错误"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		frb.setResultHeader(ResultHeader);
		String reqxml = JaxbUtil.convertToXml(frb, true);
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + orderid + ":" + reqxml);
		return ResultHeader;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String patient_id,String clinic_no) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id,a.exam_num from exam_info a where a.patient_id='"+patient_id+"' and a.clinic_no='"+clinic_no+"' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	public String getreqIn_pacs_item(String id,String logname){
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		String lisid="";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();						
				String insertsql = "select req_id from zl_req_pacs_item where id='"+id+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +insertsql);				
				preparedStatement = tjtmpconnect.prepareStatement(insertsql);
				
				ResultSet rs = null;
				rs =preparedStatement.executeQuery();
				if (rs.next())
					lisid = rs.getString("req_id");				
				rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}
	
	public static Date parse5(String param) {
		Date date = new Date();
		if ((param != null) && (param.trim().length() == 14)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			try {
				date = sdf.parse(param);
			} catch (ParseException ex) {
			}
		}
		return date;
	}
	
	public static String shortFmt2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
