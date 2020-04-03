package com.hjw.webService.client.hokai;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.ResLisMessageHK;
import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class LisResMessageHK{
	private static CommService commService;
	private static JdbcQueryManager jdbcQueryManager;
    
	  static {
	    	init();
	    	}
	public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			commService = (CommService) wac.getBean("commService");
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		}

	/**
	 * 
	 * @Title: accetpMessageLis @Description: Lis
	 *         结果返回处理 @param: @return @return: String @throws
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
			ResLisMessageHK rpm = new ResLisMessageHK(xmlmessage, true);
			RetLisCustome rc = new RetLisCustome();
			rc = rpm.rc;
			if ((rc == null) || (rc.getListRetLisChargeItem() == null) || (rc.getListRetLisChargeItem().size() <= 0)) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("lis信息解析为空");
			} else {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					if(!"".equals(rc.getExam_num())) {
						ei = getExamInfoForNum(rc.getExam_num());
						if ((ei == null) || (ei.getId() <= 0)) {
							ResultHeader.setTypeCode("AE");
							ResultHeader.setText("lis信息 查无此人，入库错误" + rc.getCustome_id()+","+rc.getCoustom_jzh());
						} else if ("Z".equals(ei.getStatus())) {
							ResultHeader.setTypeCode("AE");
							ResultHeader.setText("lis信息 已经总检，入账错误" + rc.getCustome_id()+","+rc.getCoustom_jzh());
						} else {
							ProcListResult plr = new ProcListResult();
							plr.setExam_num(ei.getExam_num());
							plr.setBar_code(rc.getSample_barcode());
							plr.setExam_doctor(rc.getDoctor_name_bg());
							plr.setExam_date(rc.getDoctor_time_bg());
							plr.setApprover(rc.getDoctor_name_sh());
							plr.setApprove_date(rc.getDoctor_time_sh());
							boolean flagss = true;
							for (RetLisChargeItem rlcharg : rc.getListRetLisChargeItem()) {
								plr.setLis_item_code(rlcharg.getChargingItem_num());
								for (RetLisItem rlis : rlcharg.getListRetLisItem()) {
									plr.setLis_rep_item_code(rlis.getItem_id());
									plr.setExam_result(rlis.getValues());
									plr.setRef_value(rlis.getValue_fw());
									plr.setItem_unit(rlis.getValues_dw());
									plr.setRef_indicator(rlis.getValue_ycbz());
																		
									// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
									if (plr.getRef_indicator().indexOf("↑")>=0 ) {
										plr.setRef_indicator("1");
									} else if (plr.getRef_indicator().indexOf("↓")>=0 ) {
										plr.setRef_indicator("2");
									} else if (plr.getRef_indicator().indexOf("")>=0) {
										plr.setRef_indicator("0");
									} else if (plr.getRef_indicator().indexOf("*")>=0) {
										plr.setRef_indicator("3");
									} else if(plr.getRef_indicator().indexOf("H")>=0 || plr.getRef_indicator().indexOf("h")>=0){
										plr.setRef_indicator("4");
									}else if(plr.getRef_indicator().indexOf("L")>=0 || plr.getRef_indicator().indexOf("l")>=0){
										plr.setRef_indicator("4");
									}
									else {
										plr.setRef_indicator("0");
									}
									if(plr.getRef_value()==null) plr.setRef_value("");
									if(plr.getBar_code()==null) plr.setBar_code("");
									
									Date parse5 = parse5(plr.getApprove_date());
									plr.setApprove_date(shortFmt2(parse5));
									
									parse5 = parse5(plr.getExam_date());
									plr.setExam_date(shortFmt2(parse5));
									
									int resflag = this.commService.doproc_Lis_result(plr);
									if (resflag != 0) {
										flagss = false;
									}
								}
								
							}
							if (flagss) {
								ResultHeader.setTypeCode("AA");
								ResultHeader.setText("lis信息 入库成功");
							} else {
								ResultHeader.setTypeCode("AE");
								ResultHeader.setText("lis信息 入库错误");
							}
						}
					} else {
						TranLogTxt.liswriteEror_to_txt(logName, "lis回传结果缺少医嘱id");
						ResultHeader.setTypeCode("AE");
						ResultHeader.setText("lis回传结果缺少医嘱id");
					}
				
			}
		} catch (Exception ex) {
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("lis信息 xml解析错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		sb.append("	<interactionId extension=\"S0075\" root=\"2.16.840.1.113883.1.6\"/>\n");
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String patient_id) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.id,a.exam_num from exam_info a where a.patient_id='"+patient_id+"' and a.is_Active='Y' and a.status != 'Z'"
				+ " order by a.create_time desc");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
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
