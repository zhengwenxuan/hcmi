package com.hjw.webService.client.qufu.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.client.qufu.server.bean.ExamRequest_PACS;
import com.hjw.webService.client.qufu.server.bean.ResponseBody_PACS;
import com.hjw.webService.client.qufu.server.bean.RequestBody;
import com.hjw.webService.client.qufu.server.gencode.Response;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.webService.client.Bean.ThridInterfaceLog;


import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class ExamRequest_EL_ByTime {
	
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private ThridInterfaceLog til;
	static {
		init();
	}

	public ExamRequest_EL_ByTime(ThridInterfaceLog til) {
		this.til = til;
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public Response checkInfoQuery(RequestBody requestBody) {
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "====================================EL_ByTime====================================");
		Response response = new Response();
		List<ExamInfoUserDTO> list = getExamInfoForNum(requestBody.getSTARTTIME(), requestBody.getENDTIME());
		if(list == null || list.isEmpty()) {
			response.getResponseHeader().setErrCode("5000");
			response.getResponseHeader().setErrMessage("时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无体检者或时间格式错误。");
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无体检者或时间格式错误。");
		} else {
			String IS_LIS_PACS_DOCTOR_NAME = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value();
			ResponseBody_PACS responseBody = new ResponseBody_PACS();
			for(ExamInfoUserDTO eu : list) {
				StringBuilder sb = new StringBuilder();
				sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,dd.dep_name as dept_name,c.item_name,dd.dep_num,ec.id,ec.pay_status,ec.amount,c.id as itemId "
						+" from examinfo_charging_item ec,pacs_summary p,pacs_detail d,department_dep dd,charging_item c"
						+" where ec.charge_item_id = c.id and p.id = d.summary_id and d.chargingItem_num = c.item_code" 
						+" and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' and ec.change_item != 'C'" 
						+" and ec.pay_status != 'M' and ec.exam_status in ('N','D')"
						+" and ec.examinfo_id = "+eu.getId()+" and p.examinfo_num = '"+eu.getExam_num()+"'");
				sb.append(" and d.dep_num = 'EL' ");
				sb.append(" and dd.dep_inter_num in ('"+requestBody.getEXECDEPTID().replaceAll(",", "','")+"') ");
				
				boolean isBufa = false;
				if(!isBufa){
					sb.append(" and ec.is_application = 'N'");
				}
				
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "查项目sql:"+sb.toString());
				List<PacsSendDTO> pacsSendList = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
				
				String noPayItems = "";
				if("N".equals(eu.getIs_after_pay())){
					for(int i=0;i<pacsSendList.size();i++){
						String IS_HIS_PACS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_PACS_CHECK").getConfig_value().trim();
						if(("N".equals(pacsSendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_PACS_CHECK))){
							if(i == pacsSendList.size() - 1){
								noPayItems += pacsSendList.get(i).getItem_name();
							}else{
								noPayItems += pacsSendList.get(i).getItem_name()+",";
							}
							pacsSendList.remove(i);
						}
					}
				}
				
				if(pacsSendList.size() == 0 && "".equals(noPayItems)){
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检号【"+eu.getExam_num()+"】没有需要发送申请的心电科室项目!");
				}else if(pacsSendList.size() == 0 && (!"".equals(noPayItems))){
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检号【"+eu.getExam_num()+"】项目("+noPayItems+")未付费!");
				}
				
				if("男".equals(eu.getSex())) {
					eu.setSex("1");
				} else if("女".equals(eu.getSex())) {
					eu.setSex("0");
				} else {
					eu.setSex("%");
				}
				if(eu.getBirthday() != null && eu.getBirthday().length()>=10) {
					eu.setBirthday(eu.getBirthday().substring(0, 4) + eu.getBirthday().substring(5, 7) + eu.getBirthday().substring(8, 10));
				}
				for(PacsSendDTO pacssend : pacsSendList){
					ExamRequest_PACS examRequest = new ExamRequest_PACS();
					examRequest.setPAT_TYPE("体检");
					examRequest.setEXAM_NO(eu.getExam_num());
					examRequest.setXM(eu.getUser_name());
					examRequest.setXB(eu.getSex());
					examRequest.setCSNYR(eu.getBirthday());
					examRequest.setNL(""+eu.getAge());
					examRequest.setAPPLICATION_DATE(DateTimeUtil.getDateTimes());
					examRequest.setDWBH(eu.getRemark1());
					examRequest.setSFZH(eu.getId_num());
					examRequest.setHYZK(eu.getIs_marriage());
					examRequest.setADDRESS(eu.getAddress());
					examRequest.setPHONE(eu.getPhone());
//					examRequest.setMOBILE(eu.get);//家属联系电话
					examRequest.setDJLSH(pacssend.getPacs_req_code());
					examRequest.setTJLSH(pacssend.getPacs_req_code());
					examRequest.setXMBH(pacssend.getItem_code());
					examRequest.setXMMC(pacssend.getItem_name());
					examRequest.setKSBM(requestBody.getEXECDEPTID());
					examRequest.setKSMC(pacssend.getDept_name());
					examRequest.setKDYSXM(IS_LIS_PACS_DOCTOR_NAME);
					examRequest.setKSLB("查体科");
					responseBody.getExamRequest().add(examRequest);
				}
			}
			if(responseBody.getExamRequest().isEmpty()) {
				response.getResponseHeader().setErrCode("5000");
				response.getResponseHeader().setErrMessage("时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无心电项目数据。");
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无心电项目数据。");
			} else {
				String responseBodyStr = JaxbUtil.convertToXmlWithOutHead(responseBody, false);
				response.setResponseBody(responseBodyStr);
				response.getResponseHeader().setErrCode("0");
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"responseBody:"+responseBodyStr);
			}
		}
		return response;
	}

	private List<ExamInfoUserDTO> getExamInfoForNum(String STARTTIME,String ENDTIME) throws ServiceException {
		List<ExamInfoUserDTO> list = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			STARTTIME = DateTimeUtil.shortFmt2(sdf.parse(STARTTIME));
			ENDTIME = DateTimeUtil.shortFmt2(sdf.parse(ENDTIME));
		} catch (Exception ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			return list;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,a.sex,c.status,c.exam_type,c.is_marriage,c.is_after_pay"
				+ ",c.register_date,c.join_date,c.exam_times,c.phone,c.company,c.address,a.arch_num,a.birthday,a.id_num,ci.com_num as remark1 ");
		sb.append(" from customer_info a,exam_info c ");
		sb.append(" left join company_info ci on ci.id = c.company_id ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' and c.status != 'Z' ");		
		sb.append(" and c.join_date >= '"+STARTTIME+"' and c.join_date <= '"+ENDTIME+"'");	
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "req:" +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		if((map!=null)&&(map.getList().size()>0)){
			list= (List<ExamInfoUserDTO>)map.getList();			
		}
		return list;
	}
}
