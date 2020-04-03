package com.hjw.webService.client.jsjg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.jsjg.bean.in.MsgBody_RecReport;
import com.hjw.webService.client.jsjg.bean.in.MsgBody_Report;
import com.hjw.webService.client.jsjg.bean.in.MsgBody_RequestFrom;
import com.hjw.webService.client.jsjg.bean.in.MsgBody_Status;
import com.hjw.webService.client.jsjg.bean.in.ReportJSJG;
import com.hjw.webService.client.jsjg.bean.in.Result;
import com.hjw.webService.client.jsjg.bean.out.ApplyOrderInfo;
import com.hjw.webService.client.jsjg.bean.out.ApplyOrderInfos;
import com.hjw.webService.client.jsjg.bean.out.ItemJSJG;
import com.hjw.webService.client.jsjg.bean.out.RegInfo;
import com.hjw.webService.client.jsjg.bean.out.RetMsg;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.JobDTO;
import com.hjw.wst.DTO.LisSendDTO;
import com.hjw.wst.service.CompanyService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 江苏省省级机关医院-柯林布瑞-HSB平台-LIS接口
 * @author zwx
 */
public class Service4JSJG extends ServletEndpointSupport {
	private ConfigService configService;
	private CompanyService companyService;
	private JdbcQueryManager jdbcQueryManager;
	private ThridInterfaceLog til;

	protected void onInit() {
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
		this.companyService = (CompanyService) getWebApplicationContext().getBean("companyService");
		this.jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----参数为：" + xmlmessage;
	}
	
	public String SendMsg(String SysCode,String PassWord, String MsgType, String MsgBody) {
		til = new ThridInterfaceLog();
		til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//		til.setReq_no();
//		til.setExam_no();
//		til.setMessage_id();
		til.setMessage_name(MsgType);
		til.setMessage_type("webservice");
		til.setSender("LIS");
		til.setReceiver("PEIS");
		til.setMessage_request(SysCode+"-"+PassWord+"-"+MsgType+"-"+MsgBody);
		til.setFlag(2);
		til.setXtgnb_id("0");
    	til.setMessage_inout(1);
    	configService.insert_log(til);
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "SysCode:"+SysCode+"-PassWord:"+PassWord+"-MsgType:"+MsgType+"-MsgBody:"+MsgBody);
		RetMsg retMsg = new RetMsg();
		if("LIS".equals(SysCode)) {//实验室系统
			if("GetPEISLisApplyInfo".equals(MsgType)) {
				retMsg = GetPEISLisApplyInfo(MsgBody);
			} else if("CallBackLisInfo2".equals(MsgType)) {
				retMsg = CallBackLisInfo2(MsgBody);
			} else if("PushLisReport".equals(MsgType)) {
				retMsg = PushLisReport(MsgBody);
			} else if("RecLisReport".equals(MsgType)) {
				retMsg = RecLisReport(MsgBody);
			} else if("SaveLisMicReport2".equals(MsgType)) {
				retMsg = SaveLisMicReport2(MsgBody);
			} else {
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("不支持的消息类型-MsgType:"+MsgType);
			}
		} else {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("不支持的HSB系统代码-SysCode:"+SysCode);
		}
		String json = new Gson().toJson(retMsg, RetMsg.class);
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:"+json);
		til.setMessage_response(json);
		configService.update_log(til);
		String xml = "<RetMsg><![CDATA[" + json + "]]></RetMsg>";
		return xml;
	}
	
	//3.2.4获取体检检验申请单
	private RetMsg GetPEISLisApplyInfo(String jsonStr) {
		RetMsg retMsg = new RetMsg();
		MsgBody_RequestFrom msgBody = null;
		try {
			msgBody = new Gson().fromJson(jsonStr, MsgBody_RequestFrom.class);
		} catch (Exception e) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(e));
			return retMsg;
		}
		String exam_num = msgBody.getInParam().getPatientCode();
		til.setExam_no(exam_num);
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "patientCode:" + exam_num + "-patientCodeType:" + msgBody.getInParam().getPatientCodeType());
		if (StringUtil.isEmpty(exam_num)) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("患者编号为空-patientCode:"+exam_num);
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "患者编号为空-patientCode:"+exam_num);
		} else {
			ExamInfoUserDTO eu=this.configService.getExamInfoForNum(exam_num);
			if ((eu==null)||(eu.getId() <= 0)) {
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("根据患者编号查不到体检者-patientCode:"+exam_num);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据患者编号查不到体检者-patientCode:"+exam_num);
			} else if ("Z".equals(eu.getStatus())) {
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("此人已经总检-patientCode:"+exam_num);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检-patientCode:"+exam_num);
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append(" select c.Id as chargingitemId,c.item_name,c.exam_num,d.remark,d.data_name,ec.amount,c.his_num,c.item_code,ec.item_amount,"
						+ " hd.dept_code+'',hd.dept_name,ec.id,ec.pay_status,s.sample_barcode ");
				sb.append("from examinfo_charging_item ec,sample_exam_detail s,examResult_chargingItem er,sample_demo sd,data_dictionary d,charging_item  c "
						+" left join his_dict_dept hd on c.perform_dept = hd.dept_code"
						+" where ec.examinfo_id = s.exam_info_id and ec.charge_item_id = c.id and s.sample_id = sd.id"
						+" and sd.demo_category = d.id "
//						+ "and c.sam_demo_id = sd.id "
						+ "and ec.isActive = 'Y'  and ec.exam_status in ('N','D') " //and c.interface_flag = '2' 金域的维护的都是0
						+" and s.id = er.exam_id and er.charging_id = ec.charge_item_id and er.result_type = 'sample'"
						+" and ec.change_item != 'C' and ec.pay_status != 'M' "
						+" and ec.examinfo_id = '"+eu.getId()+"'");
				
				boolean isBufa = false;
				if(!isBufa){
					sb.append(" and ec.is_application = 'N'");
				}
				
				if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
					sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and s.is_binding = 1))");
				}
				
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "sql: " + sb.toString());
				
				List<LisSendDTO> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTO.class);
				
				String noPayItems = "";
				if("N".equals(eu.getIs_after_pay())){
					for(int i=sendList.size()-1;i>=0;i--){
						String IS_HIS_LIS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
						if(("N".equals(sendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_LIS_CHECK))){
							if(i == 0){
								noPayItems += sendList.get(i).getItem_name();
							}else{
								noPayItems += sendList.get(i).getItem_name()+",";
							}
							sendList.remove(i);
						}
					}
				}else{
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人为后付费");
				}
				
				if(sendList.size() == 0 && "".equals(noPayItems)){
					retMsg.getOutParam().getOutResult().setStatus("0");
					retMsg.getOutParam().getOutResult().setMsg("error-没有需要发送申请的检验科室项目!");
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "error-没有需要发送申请的检验科室项目!");
					return retMsg;
				}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
					retMsg.getOutParam().getOutResult().setStatus("0");
					retMsg.getOutParam().getOutResult().setMsg("error-项目("+noPayItems+")未付费,不能发送申请!");
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "error-项目("+noPayItems+")未付费,不能发送申请!");
					return retMsg;
				}
				
				List<JobDTO> dataList = this.companyService.getDatadis("SQKS");
			    if (dataList.size() == 0) {
			    	configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "error-请在数据字典中维护申请科室(data_code = SQKS)");
			    }
				
				RegInfo regInfo = new RegInfo();
				regInfo.setPatientNo(eu.getExam_num());
				regInfo.setPatientName(eu.getUser_name());
				regInfo.setSex(eu.getSex());
				regInfo.setMarriage(eu.getIs_marriage());
				regInfo.setBirth(eu.getBirthday());
				regInfo.setAge(""+eu.getAge());
//				regInfo.setPatHeight();
//				regInfo.setPatWeight();
//				regInfo.setNatioin();
//				regInfo.setVisitType();
				regInfo.setVisitNo(eu.getExam_num());
//				regInfo.setInPatientId();
//				regInfo.setCardNo();
				regInfo.setRegDeptCode(((JobDTO)dataList.get(0)).getRemark());
				regInfo.setRegDeptName(((JobDTO)dataList.get(0)).getName());
				regInfo.setPhone(eu.getPhone());
				regInfo.setAddress(eu.getAddress());
//				regInfo.setIDType();
				regInfo.setIDNumber(eu.getId_num());
//				regInfo.setBloodTypeABO();
//				regInfo.setBloodTypeRH();
				regInfo.setOutPatientTime(eu.getExam_times());
//				regInfo.setClinicDiagnosisCode();
//				regInfo.setClinicDiagnosisName();
//				regInfo.setRegistrationType();
//				regInfo.setSourceOfNum();
//				regInfo.setSortNo();
//				regInfo.setRegistrationDay();
				regInfo.setApplyDay(eu.getExam_times());
//				regInfo.setNumberType();
				regInfo.setAppointmentTime(eu.getRegister_date());
//				regInfo.setSerialNumber();
				
				String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生id
				String doctorname = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
				ApplyOrderInfos applyOrderInfos = new ApplyOrderInfos();
				Map<String, ApplyOrderInfo> map = new HashMap<>();
				for(LisSendDTO lissend : sendList){
					ApplyOrderInfo applyOrderInfo = new ApplyOrderInfo();
					if(map.containsKey(lissend.getSample_barcode())) {
						applyOrderInfo = map.get(lissend.getSample_barcode());
					} else {
						map.put(lissend.getSample_barcode(), applyOrderInfo);
					}
					
					applyOrderInfo.setApplyOrderNo(lissend.getSample_barcode());
					applyOrderInfo.setApplyDeptCode(lissend.getDept_code());
					applyOrderInfo.setApplyDeptName(lissend.getDept_name());
					applyOrderInfo.setDocCode(doctorid);
					applyOrderInfo.setDocName(doctorname);
					applyOrderInfo.setApplyTime(DateTimeUtil.getDateTime());
//					applyOrderInfo.setApplyOrderType();
//					applyOrderInfo.setDiagnosis();
//					applyOrderInfo.setDiagnosisCode();
//					applyOrderInfo.setDiagnosisName();
//					applyOrderInfo.setEspecialCondition();
//					applyOrderInfo.setInspectedInfo();
//					applyOrderInfo.setInfection();
					
					ItemJSJG item = new ItemJSJG();
//					item.setRecipeNo();
					item.setRecipeDetailNo(lissend.getSample_barcode());
//					item.setChargeCode();
//					item.setApplyItemCode(examinationItem.getExam_num());
					item.setApplyItemCode(lissend.getItem_code());
//					item.setGroupCode();
//					item.setGroupName();
					item.setApplyItemName(lissend.getItem_name());
//					item.setUnit();
					item.setBasePrice(""+lissend.getItem_amount());
					item.setUnitPrice(""+lissend.getAmount());
					item.setTotalPrice(""+lissend.getAmount());
//					item.setSpecimenCode();
					item.setSpecimenName(lissend.getData_name());
//					item.setSpecimenMeno();
					item.setBarCode(lissend.getSample_barcode());
//					item.setBarCodeReqState();
					item.setSubmintDoctor(doctorid);
					item.setExecDeptCode(lissend.getDept_code());
					item.setExecDeptName(lissend.getDept_name());
//					item.setSecrecy();
//					item.setUrgentFlag();
//					item.setExamGoal();
//					item.setAcquisitionpartCode();
//					item.setAcquisitionpartName();
					applyOrderInfo.getItems().getItem().add(item);
				}
				for (String key : map.keySet()) {
					ApplyOrderInfo applyOrderInfo = map.get(key);
					applyOrderInfos.getApplyOrderInfo().add(applyOrderInfo);
				}
				retMsg.getOutParam().setRegInfo(regInfo);
				retMsg.getOutParam().setApplyOrderInfos(applyOrderInfos);
				til.setFlag(0);
				retMsg.getOutParam().getOutResult().setStatus("1");
				retMsg.getOutParam().getOutResult().setMsg("获取申请单成功");
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "获取申请单成功");
			}
		}
		return retMsg;
	}

	/**
	 * 3.3.3 回写LIS申请单状态信息
	 * 2: 打印条码(采样)，3: 标本接收，4: 报告审核（发布报告），
	 * 7: 取消审核(报告召回)如果已总检，不可取消审核
	 * @return
	 */
	
	private RetMsg CallBackLisInfo2(String jsonStr) {
		RetMsg retMsg = new RetMsg();
		MsgBody_Status msgBody = null;
		try{
			msgBody = new Gson().fromJson(jsonStr, MsgBody_Status.class);
		} catch(Exception ex) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			return retMsg;
		}
//		String BarCode = msgBody.getInParam().getBarCode();
		String BarCode = msgBody.getInParam().getApplyID();
		til.setReq_no(BarCode);
		til.setExam_no(msgBody.getInParam().getPatientNo());
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "PatientNo:"+msgBody.getInParam().getPatientNo()+"-ApplyID:"+msgBody.getInParam().getApplyID()+"-BarCode:" + msgBody.getInParam().getBarCode() + "-ExecStatus:" + msgBody.getInParam().getExecStatus());
		if (StringUtil.isEmpty(msgBody.getInParam().getBarCode())) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("条码号为空-ApplyID:"+BarCode);
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "条码号为空-ApplyID:"+BarCode);
		} else {
			ExamInfoUserDTO eu=this.configService.getExamInfoForBarcode(BarCode);
			if ((eu==null)||(eu.getId() <= 0)) {
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("根据条码号查不到体检者-ApplyID:"+BarCode);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据条码号查不到体检者-ApplyID:"+BarCode);
			} else if ("Z".equals(eu.getStatus())) {
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("此人已经总检-ApplyID:"+BarCode);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检-ApplyID:"+BarCode);
			} else {
				List<String> req_nums = new ArrayList<>();
				req_nums.add(BarCode);
				if("1".equals(msgBody.getInParam().getExecStatus())) {//生成条码
					this.configService.setExamInfoChargeItemLisStatus(req_nums, eu.getExam_num(), "C","H");
				} else if("2".equals(msgBody.getInParam().getExecStatus())) {//2:采样确认
				} else if("3".equals(msgBody.getInParam().getExecStatus())) {//3、标本接收
//					this.configService.setExamInfoChargeItemLisStatus(req_nums, eu.getExam_num(), "C","E");
				} else if("4".equals(msgBody.getInParam().getExecStatus())) {//4、报告审核
					this.configService.setExamInfoChargeItemLisStatus(req_nums, eu.getExam_num(), "C","H");
				} else if("5".equals(msgBody.getInParam().getExecStatus())) {//5: 作废条码
					
				} else if("6".equals(msgBody.getInParam().getExecStatus())) {//6: 取消签收
					
				} else if("7".equals(msgBody.getInParam().getExecStatus())) {//7、取消审核
					
				} else {
					retMsg.getOutParam().getOutResult().setStatus("0");
					retMsg.getOutParam().getOutResult().setMsg("不支持的执行状态代码-ExecStatus:"+msgBody.getInParam().getExecStatus());
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "不支持的执行状态代码-ExecStatus:"+msgBody.getInParam().getExecStatus());
					return retMsg;
				}
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"LIS申请单状态信息通知成功");
				til.setFlag(0);
				retMsg.getOutParam().getOutResult().setStatus("1");
				retMsg.getOutParam().getOutResult().setMsg("LIS申请单状态信息通知成功");
			}
		}
		return retMsg;
	}

	//3.3.4 LIS报告推送
	private RetMsg PushLisReport(String jsonStr) {
		RetMsg retMsg = new RetMsg();
		MsgBody_Report msgBody = null;
		try{
			msgBody = new Gson().fromJson(jsonStr, MsgBody_Report.class);
		} catch(Exception ex) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			return retMsg;
		}
		List<ReportJSJG> reportList = msgBody.getInParam().getReport();
		for(ReportJSJG report : reportList) {
			til.setReq_no(report.getBarCode());
			til.setExam_no(report.getVisitNo());
			
			LisResult lisResult = new LisResult();
			lisResult.setExam_num(report.getVisitNo());
			lisResult.setTil_id(til.getId());
			lisResult.setSample_barcode(report.getBarCode());
			lisResult.setExam_date(report.getReportTime());
			lisResult.setDoctor(report.getReportDocName());
			lisResult.setSh_doctor(report.getVerifiDocName());
//			lisResult.setLis_item_code(report.getItemCode());
//			lisResult.setLis_item_name(report.getItemName());
			int seq_code = 0;
			boolean flagss = true;
			for(Result result : report.getResults().getResult()) {
				lisResult.setReport_item_code(result.getReportItemCode());
				lisResult.setReport_item_name(result.getReportItemName());
				lisResult.setItem_result(result.getItemResult());
				lisResult.setItem_unit(result.getUnit());
				if ("Q".equals(lisResult.getFlag())) {
					lisResult.setFlag("P");
				}
				lisResult.setFlag(result.getAbnormalFlag());
				lisResult.setRef(result.getReference());
				lisResult.setSeq_code(seq_code++);
				boolean succ = this.configService.insert_lis_result(lisResult);
				if (!succ) {
					flagss = false;
				}
			}
			if (flagss) {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis报告 入库成功");
				til.setFlag(0);
				retMsg.getOutParam().getOutResult().setStatus("1");
				retMsg.getOutParam().getOutResult().setMsg("lis报告 入体检库成功");
			} else {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis报告 入库失败");
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("lis报告 入体检库失败");
			}
		}
		return retMsg;
	}
	
	//3.3.8 报告撤回(推送体检)(RecLisReport)
	private RetMsg RecLisReport(String jsonStr) {
		RetMsg retMsg = new RetMsg();
		MsgBody_RecReport msgBody = null;
		try{
			msgBody = new Gson().fromJson(jsonStr, MsgBody_RecReport.class);
		} catch(Exception ex) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			return retMsg;
		}
		String ReportNo = msgBody.getInParam().getReportNo();
		til.setReq_no(ReportNo);
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "ReportNo:" + ReportNo + "-OperatorCode:" + msgBody.getInParam().getOperatorCode()
				 + "-OperatorName:" + msgBody.getInParam().getOperatorName() + "-OperatorTime:" + msgBody.getInParam().getOperatorTime());

		//和lis沟通决定，此接口直接返回成功
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"报告撤回成功");
		til.setFlag(0);
		retMsg.getOutParam().getOutResult().setStatus("1");
		retMsg.getOutParam().getOutResult().setMsg("报告撤回成功");
		
//		if (StringUtil.isEmpty(ReportNo)) {
//			retMsg.getOutParam().getOutResult().setStatus("0");
//			retMsg.getOutParam().getOutResult().setMsg("报告单号为空-ReportNo:"+ReportNo);
//			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "报告单号为空-ReportNo:"+ReportNo);
//		} else {
//			ExamInfoUserDTO eu=this.configService.getExamInfoForBarcode(ReportNo);
//			til.setExam_no(eu.getExam_num());
//			if ((eu==null)||(eu.getId() <= 0)) {
//				retMsg.getOutParam().getOutResult().setStatus("0");
//				retMsg.getOutParam().getOutResult().setMsg("根据报告单号查不到体检者-ReportNo:"+ReportNo);
//				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据报告单号查不到体检者-ReportNo:"+ReportNo);
//			} else if ("Z".equals(eu.getStatus())) {
//				retMsg.getOutParam().getOutResult().setStatus("0");
//				retMsg.getOutParam().getOutResult().setMsg("此人已经总检-ReportNo:"+ReportNo);
//				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检-ReportNo:"+ReportNo);
//			} else {
//				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"报告撤回成功");
//				til.setFlag(0);
//				retMsg.getOutParam().getOutResult().setStatus("1");
//				retMsg.getOutParam().getOutResult().setMsg("报告撤回成功");
//			}
//		}
		return retMsg;
	}

	//3.3.21 微生物报告推送(SaveLisMicReport2)推送体检
	private RetMsg SaveLisMicReport2(String jsonStr) {
		RetMsg retMsg = new RetMsg();
		MsgBody_Report msgBody = null;
		try{
			msgBody = new Gson().fromJson(jsonStr, MsgBody_Report.class);
		} catch(Exception ex) {
			retMsg.getOutParam().getOutResult().setStatus("0");
			retMsg.getOutParam().getOutResult().setMsg("json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "json解析失败:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			return retMsg;
		}
		List<ReportJSJG> reportList = msgBody.getInParam().getReport();
		for(ReportJSJG report : reportList) {
			til.setReq_no(report.getBarCode());
			til.setExam_no(report.getVisitNo());
			
			LisResult lisResult = new LisResult();
			lisResult.setExam_num(report.getVisitNo());
			lisResult.setTil_id(til.getId());
			lisResult.setSample_barcode(report.getBarCode());
			lisResult.setExam_date(report.getReportTime());
			lisResult.setDoctor(report.getReportDocName());
			lisResult.setSh_doctor(report.getVerifiDocName());
//			lisResult.setLis_item_code(report.getItemCode());
//			lisResult.setLis_item_name(report.getItemName());
			int seq_code = 0;
			boolean flagss = true;
			for(Result result : report.getResults().getResult()) {
				lisResult.setReport_item_code(result.getItemCode());
				lisResult.setReport_item_name(result.getItemName());
				lisResult.setItem_result(result.getItemResult());
//				lisResult.setItem_unit(result.getUnit());
//				if ("Q".equals(lisResult.getFlag())) {
//					lisResult.setFlag("P");
//				}
//				lisResult.setFlag(result.getAbnormalFlag());
//				lisResult.setRef(result.getReference());
				lisResult.setSeq_code(seq_code++);
				boolean succ = this.configService.insert_lis_result(lisResult);
				if (!succ) {
					flagss = false;
				}
			}
			if (flagss) {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis报告 入库成功");
				til.setFlag(0);
				retMsg.getOutParam().getOutResult().setStatus("1");
				retMsg.getOutParam().getOutResult().setMsg("lis报告 入体检库成功");
			} else {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis报告 入库失败");
				retMsg.getOutParam().getOutResult().setStatus("0");
				retMsg.getOutParam().getOutResult().setMsg("lis报告 入体检库失败");
			}
		}
		return retMsg;
	}
	
}
