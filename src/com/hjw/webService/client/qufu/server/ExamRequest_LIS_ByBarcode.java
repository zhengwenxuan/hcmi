package com.hjw.webService.client.qufu.server;

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

public class ExamRequest_LIS_ByBarcode {
	
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private ThridInterfaceLog til;
	static {
		init();
	}

	public ExamRequest_LIS_ByBarcode(ThridInterfaceLog til) {
		this.til = til;
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public Response checkInfoQuery(RequestBody requestBody) {
		Response response = new Response();
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "====================================LISByBarcode===================================");
		
		ExamInfoUserDTO eu = getExamInfoForBarcode(requestBody.getEXAM_NO());
		if ((eu == null) || (eu.getId() <= 0)) {
			response.getResponseHeader().setErrCode("5000");
			response.getResponseHeader().setErrMessage("根据条码号【"+requestBody.getEXAM_NO()+"】查无此人。");
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据条码号【"+requestBody.getEXAM_NO()+"】查无此人。");
		} else if ("Z".equals(eu.getStatus())) {
			response.getResponseHeader().setErrCode("5000");
			response.getResponseHeader().setErrMessage("条码号【"+requestBody.getEXAM_NO()+"】的体检者已经总检。");
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "条码号【"+requestBody.getEXAM_NO()+"】的体检者已经总检。");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(" select ci.Id as chargingitemId,ci.item_name,ci.exam_num,d.remark,d.data_name,eci.amount,ci.item_code,eci.item_amount,"
					+ " dd.dep_name as dept_name,eci.id,eci.pay_status,sed.sample_barcode ");
			sb.append(" from examinfo_charging_item eci,sample_exam_detail sed,examResult_chargingItem er,sample_demo sd,data_dictionary d,department_dep dd,charging_item  ci"
					+ " where eci.examinfo_id = sed.exam_info_id and eci.charge_item_id = ci.id and sed.sample_id = sd.id"
					+ " and sd.demo_category = d.id "
					+ " and ci.sam_demo_id = sd.id "
					+ " and ci.dep_id = dd.id and eci.isActive = 'Y' and ci.interface_flag = '2' and eci.exam_status in ('N','D') "
					+ " and sed.id = er.exam_id and er.charging_id = eci.charge_item_id and er.result_type = 'sample'"
					+ " and eci.change_item != 'C' and eci.pay_status != 'M' and sed.sample_barcode ='"+ requestBody.getEXAM_NO()+"'");
//			sb.append(" and dd.dep_inter_num in ('"+requestBody.getEXECDEPTID().replaceAll(",", "','")+"') ");
			
			boolean isBufa = false;
			if(!isBufa){
				sb.append(" and eci.is_application = 'N'");
			}
			
			if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
				sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and sed.is_binding = 1))");
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
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "条码号【"+requestBody.getEXAM_NO()+"】没有需要发送申请的检验科室项目!");
				response.getResponseHeader().setErrCode("5000");
				response.getResponseHeader().setErrMessage("条码号【"+requestBody.getEXAM_NO()+"】没有需要发送申请的检验科室项目!");
			}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "条码号【"+requestBody.getEXAM_NO()+"】项目("+noPayItems+")未付费!");
				response.getResponseHeader().setErrCode("5000");
				response.getResponseHeader().setErrMessage("条码号【"+requestBody.getEXAM_NO()+"】项目("+noPayItems+")未付费!");
			}else{
				String IS_LIS_PACS_DOCTOR_ID = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value();
				ResponseBody_LIS responseBody = new ResponseBody_LIS();
				for(LisSendDTO lissend : sendList){
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
				String responseBodyStr = JaxbUtil.convertToXmlWithOutHead(responseBody, false);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"responseBody:"+responseBodyStr);
				response.setResponseBody(responseBodyStr);
				response.getResponseHeader().setErrCode("0");
			}
		}
		
		til.setExam_no(eu.getExam_num());
		til.setReq_no(requestBody.getEXAM_NO());
		return response;
	}

	private ExamInfoUserDTO getExamInfoForBarcode(String sample_barcode) throws ServiceException{
		String sql = " select ci.user_name,ci.id_num,ci.sex,ci.birthday,ci.phone, "
				+ " ei.id,ei.age,ei.exam_num,ei.status,ei.exam_type,ei.register_date,ei.join_date,ei.exam_times,ei.is_marriage,ei.is_after_pay,ei.address, "
				+ " com.com_num as remark1 "
				+ " from sample_exam_detail sed, customer_info ci, exam_info ei "
				+ " left join company_info com on com.id = ei.company_id "
				+ " where sed.sample_barcode='" + sample_barcode+ "' and sed.exam_info_id=ei.id and ei.customer_id = ci.id and ei.is_Active='Y' ";
		
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
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
		return eu;
	}
}
