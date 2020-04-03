package com.hjw.webService.client.qufu.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.client.qufu.server.bean.ExamRequest_LIS;
import com.hjw.webService.client.qufu.server.bean.ResponseBody_LIS;
import com.hjw.webService.client.qufu.server.bean.RequestBody;
import com.hjw.webService.client.qufu.server.gencode.Response;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.LisSendDTO;
import com.hjw.webService.client.Bean.ThridInterfaceLog;


import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class ExamRequest_LIS_ByTime {
	
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private ThridInterfaceLog til;
	static {
		init();
	}

	public ExamRequest_LIS_ByTime(ThridInterfaceLog til) {
		this.til = til;
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public Response checkInfoQuery(RequestBody requestBody) {
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "====================================LISByTime===================================");
		Response response = new Response();
		
		List<ExamInfoUserDTO> list = getExamInfoForNum(requestBody.getSTARTTIME(), requestBody.getENDTIME());
		if(list == null || list.isEmpty()) {
			response.getResponseHeader().setErrCode("5000");
			response.getResponseHeader().setErrMessage("时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无体检者或时间格式错误。");
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无体检者或时间格式错误。");
		} else {
			ResponseBody_LIS responseBody = new ResponseBody_LIS();
			for(ExamInfoUserDTO eu : list) {
				StringBuilder sb = new StringBuilder();
				sb.append(" select c.Id as chargingitemId,c.item_name,c.exam_num,d.remark,d.data_name,ec.amount,c.his_num,c.item_code,ec.item_amount,"
						+ " dd.dep_name as dept_name,ec.id,ec.pay_status,s.sample_barcode ");
				sb.append(" from examinfo_charging_item ec,sample_exam_detail s,examResult_chargingItem er,sample_demo sd,data_dictionary d,department_dep dd,charging_item  c"
						+" where ec.examinfo_id = s.exam_info_id and ec.charge_item_id = c.id and s.sample_id = sd.id "
						+" and sd.demo_category = d.id "
						+" and c.sam_demo_id = sd.id "
						+" and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' and ec.exam_status in ('N','D') " 
						+" and s.id = er.exam_id and er.charging_id = ec.charge_item_id and er.result_type = 'sample' "
						+" and ec.change_item != 'C' and ec.pay_status != 'M' and ec.examinfo_id ="+ eu.getId());
				//sb.append(" and dd.dep_inter_num in ('"+requestBody.getEXECDEPTID().replaceAll(",", "','")+"') ");
				
				boolean isBufa = false;
				if(!isBufa){
					sb.append(" and ec.is_application = 'N'");
				}
				
				if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
					sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))");
				}
				
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "查项目sql:"+sb.toString());
				List<LisSendDTO> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTO.class);
				
				String noPayItems = "";
				if("N".equals(eu.getIs_after_pay())){
					String IS_HIS_LIS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
					for(int i=0;i<sendList.size();i++){
						if(("N".equals(sendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_LIS_CHECK))){
							if(i == sendList.size() - 1){
								noPayItems += sendList.get(i).getItem_name();
							}else{
								noPayItems += sendList.get(i).getItem_name()+",";
							}
							sendList.remove(i);
						}
					}
				}
				
				if(sendList.size() == 0 && "".equals(noPayItems)){
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检号【"+eu.getExam_num()+"】没有需要发送申请的检验科室项目!");
				}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检号【"+eu.getExam_num()+"】项目("+noPayItems+")未付费,未发送申请!");
				}else{
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
					for(LisSendDTO lissend : sendList){
						String IS_LIS_PACS_DOCTOR_ID = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value();
						ExamRequest_LIS examRequest = new ExamRequest_LIS();
						examRequest.setPAT_TYPE("体检");
						examRequest.setBARCODE(lissend.getSample_barcode());
						examRequest.setPAT_LIS_NO(lissend.getSample_barcode()+lissend.getExam_num());
						examRequest.setEXAM_NO(eu.getExam_num());
						examRequest.setPAT_ID(eu.getExam_num());
						examRequest.setPAT_NAME(eu.getUser_name());
						examRequest.setSFZH(eu.getId_num());
						examRequest.setPAT_SEX(eu.getSex());
						examRequest.setPAT_BIRTH(eu.getBirthday());
						examRequest.setREQ_DEPTNO(requestBody.getEXECDEPTID());
						examRequest.setREQ_DOCNO(IS_LIS_PACS_DOCTOR_ID);
						examRequest.setKSBM(requestBody.getEXECDEPTID());
						examRequest.setAPPLICATION_DATE(DateTimeUtil.getDateTimes());
						examRequest.setSPECIMEN_NAME(lissend.getData_name());
						examRequest.setREQ_ITEMCODE(lissend.getExam_num());
						examRequest.setREQ_ITEMNAME(lissend.getItem_name());
						responseBody.getExamRequest().add(examRequest);
					}
				}
			}
			if(responseBody.getExamRequest().isEmpty()) {
				response.getResponseHeader().setErrCode("5000");
				response.getResponseHeader().setErrMessage("时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无检验项目数据。");
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "时间段【"+requestBody.getSTARTTIME()+"-"+requestBody.getENDTIME()+"】内无检验项目数据。");
			} else {
				String responseBodyStr = JaxbUtil.convertToXmlWithOutHead(responseBody, false);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"responseBody:"+responseBodyStr);
				response.setResponseBody(responseBodyStr);
				response.getResponseHeader().setErrCode("0");
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
