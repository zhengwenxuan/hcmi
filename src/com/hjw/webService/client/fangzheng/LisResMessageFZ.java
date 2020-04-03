package com.hjw.webService.client.fangzheng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.job.fangzheng.ResLisMessageFZ;
import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ProcListResult;
import com.hjw.wst.service.CommService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class LisResMessageFZ{
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
	 * @Title: accetpMessageLis @Description: Lis
	 *         结果返回处理 @param: @return @return: String @throws
	 */
	public ResultHeader accetpMessageLis(String xmlmessage) {
		String filetpe = "reslis";
		TranLogTxt.liswriteEror_to_txt(filetpe, "req:" + xmlmessage);
		ResultHeader ResultHeader = new ResultHeader();
		ResultHeader.setSourceMsgId("");// 消息源id需要赋值
		ResultBody frb = new ResultBody();
		String orderid = "";
		try {
			ResLisMessageFZ rpm = new ResLisMessageFZ(xmlmessage, true);
			RetLisCustome rc = new RetLisCustome();
			rc = rpm.rc;
			if ((rc == null) || (rc.getListRetLisChargeItem() == null) || (rc.getListRetLisChargeItem().size() <= 0)) {
				ResultHeader.setTypeCode("AE");
				ResultHeader.setText("lis信息解析为空");
			} else {
					ExamInfoUserDTO ei = new ExamInfoUserDTO();
					ei = getExamInfoForNum(rc.getCustome_id(),rc.getCoustom_jzh());
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
								if (plr.getRef_indicator().indexOf("偏高")>=0) {
									plr.setRef_indicator("1");
								} else if (plr.getRef_indicator().indexOf("偏低")>=0) {
									plr.setRef_indicator("2");
								} else if (plr.getRef_indicator().indexOf("正常")>=0) {
									plr.setRef_indicator("0");
						        } else if (plr.getRef_indicator().indexOf("阳性")>=0) {
									plr.setRef_indicator("3");
								} else {
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
				
			}
		} catch (Exception ex) {
			ResultHeader.setTypeCode("AE");
			ResultHeader.setText("lis信息 xml解析错误");
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
