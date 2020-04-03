package com.hjw.webService.client.hokai305;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai305.bean.ResLisMessageHK305;
import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class LisResMessageHK305_1222{
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

	public String getMessage(String xmlmessage,String logName) {
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResLisMessageHK305 rpm = new ResLisMessageHK305(xmlmessage, true);
			RetLisCustome rc = new RetLisCustome();
			rc = rpm.rc;
			if ((rc == null) || (rc.getListRetLisChargeItem() == null) || (rc.getListRetLisChargeItem().size() <= 0)) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("lis信息解析为空");
			} else {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					if(!"".equals(rc.getExam_num())) {
						ArrayList<ZlReqItemDTO> zlreqitem = getcharingitemid(rc.getSample_barcode(),logName);
						
						if(zlreqitem.size()>0 && zlreqitem !=null){
							TranLogTxt.liswriteEror_to_txt(logName, "req:" + xmlmessage);
							ei = getExamInfoForNum(rc.getExam_num());
							if ((ei == null) || (ei.getId() <= 0)) {
								ResultHeader.setTypeCode("AE");
								ResultHeader.setText("lis信息 查无此人，入库错误" + rc.getCustome_id()+","+rc.getCoustom_jzh());
							} else if ("Z".equals(ei.getStatus())) {
								ResultHeader.setTypeCode("AE");
								ResultHeader.setText("lis信息 已经总检，入账错误" + rc.getCustome_id()+","+rc.getCoustom_jzh());
							} else {
								//ProcListResult plr = new ProcListResult();
								//plr.setExam_num(ei.getExam_num());
								//plr.setBar_code(rc.getSample_barcode());
								//plr.setExam_doctor(rc.getDoctor_name_bg());
								//plr.setExam_date(rc.getDoctor_time_bg());
								//plr.setApprover(rc.getDoctor_name_sh());
								//plr.setApprove_date(rc.getDoctor_time_sh());
								
								LisResult lisResult = new LisResult();
								lisResult.setTil_id("");
								lisResult.setExam_num(ei.getExam_num());
								lisResult.setSample_barcode(rc.getSample_barcode());
								lisResult.setDoctor(rc.getDoctor_name_bg());
								lisResult.setExam_date(rc.getDoctor_time_bg());
								lisResult.setSh_doctor(rc.getDoctor_name_sh());
								boolean flagss = true;
								int seq_code = 0;
								
							//	ArrayList<ChargingItem> itemList = gethisnum(zlreqitem,logName);
								//for (ChargingItem chargingItem : itemList) {
									for (RetLisChargeItem rlcharg : rc.getListRetLisChargeItem()) {
									
										//plr.setLis_item_code(chargingItem.getHis_num());
										//lisResult.setLis_item_code(chargingItem.getHis_num());
										
										//lisResult.setLis_item_name(chargingItem.getItem_name());
										
										for (RetLisItem rlis : rlcharg.getListRetLisItem()) {
											//plr.setLis_rep_item_code(rlis.getItem_id());
											//plr.setExam_result(rlis.getValues());
											//plr.setRef_value(rlis.getValue_fw());
											//plr.setItem_unit(rlis.getValues_dw());
											//plr.setRef_indicator(rlis.getValue_ycbz());
											
											
											lisResult.setItem_result(rlis.getValues()); //检查结果
											
											lisResult.setSeq_code(seq_code++);
											lisResult.setReport_item_code(rlis.getItem_id());
											lisResult.setReport_item_name(rlis.getItem_name());
											lisResult.setItem_result(rlis.getValues());
											lisResult.setRef(rlis.getValue_fw());
											lisResult.setItem_unit(rlis.getValues_dw());
											lisResult.setFlag(rlis.getValue_ycbz());
											
																				
											// 火箭蛙 高低标识（0：正常；1：高；2：低；3：阳性；4：危急）
											if (lisResult.getFlag().indexOf("NEG")>=0 ) {
												lisResult.setFlag("0");
											} /*else if (plr.getRef_indicator().indexOf("NEG")>=0 ) {
												plr.setRef_indicator("0");
											}*/ else if (lisResult.getFlag().indexOf("0")>=0) {
												lisResult.setFlag("0");
											} else if (lisResult.getFlag().indexOf("POS")>=0) {
												lisResult.setFlag("3");
											} else if(lisResult.getFlag().indexOf("H")>=0 || lisResult.getFlag().indexOf("h")>=0){
												lisResult.setFlag("1");
											}else if(lisResult.getFlag().indexOf("L")>=0 || lisResult.getFlag().indexOf("l")>=0){
												lisResult.setFlag("2");
											}
											else {
												lisResult.setFlag("0");
											}
											if(lisResult.getRef()==null) lisResult.setRef("");
											if(lisResult.getSample_barcode()==null) lisResult.setSample_barcode("");
											
											Date parse5 = parse5(rc.getDoctor_time_sh());
											//lisResult.setApprove_date(shortFmt2(parse5));
											
											parse5 = parse5(rc.getDoctor_time_bg());
											lisResult.setExam_date(shortFmt2(parse5));
											
										//	int resflag = this.commService.doproc_Lis_result(plr);
											boolean succ = this.configService.insert_lis_result(lisResult);
											if (!succ) {
												flagss = false;
											}
										}
										
									}
							//	}
								
								if (flagss) {
									ResultHeader.setTypeCode("AA");
									
									ResultHeader.setText("lis信息 入库成功");
								} else {
									ResultHeader.setTypeCode("AE");
									ResultHeader.setText("lis信息 入库错误");
								}
							}
						}else{
							ResultHeader.setTypeCode("AA");
							ResultHeader.setText("不是体检的申请单,正常返回");

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
	
	
	private ArrayList<ChargingItem> gethisnum(ArrayList<ZlReqItemDTO> zlreqitem, String logname) {
		
		 ArrayList<ChargingItem> Charginglist = new ArrayList<>();
		for (ZlReqItemDTO zlReqItemDTO : zlreqitem) {
			StringBuffer sb = new StringBuffer();
			sb.append("select his_num,item_name from charging_item where id='"+zlReqItemDTO.getCharging_item_id()+"' and isActive='Y' ");
			
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ChargingItem.class);
			ChargingItem eu = new ChargingItem();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ChargingItem)map.getList().get(0);		
				Charginglist.add(eu);
			}
			
		}
		
		
		return Charginglist;
	}

	private ArrayList<ZlReqItemDTO> getcharingitemid(String sample_barcode,String logname) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select  * from zl_req_item where lis_req_code='"+sample_barcode+"' ");
		
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		List<ZlReqItemDTO> cilist = this.jdbcQueryManager.getList(sb.toString(), ZlReqItemDTO.class);
		
		return (ArrayList<ZlReqItemDTO>) cilist;
	}

	private String getres(ResultBody rh){
		RetLisCustome rc = new RetLisCustome();
		StringBuffer sb=new StringBuffer();

		sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		sb.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>\n");
		sb.append("	<creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/>\n");
		sb.append("	<interactionId extension=\"S0001\"/>\n");
		
		sb.append("	<receiver  code=\"SYS009\"/>\n");
		
		sb.append("	<sender    code=\"SYS001\"/>\n");
	
		sb.append("	<!--AA成功，AE失败-->\n");
		sb.append("	<acknowledgement typeCode=\""+rh.getResultHeader().getTypeCode()+"\">\n");
		sb.append("	<!--患者id-->\n");
		sb.append("	<id  extension=\""+rc.getPatient_id()+"\"/>\n");
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
