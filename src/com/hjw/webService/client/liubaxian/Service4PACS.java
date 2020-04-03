package com.hjw.webService.client.liubaxian;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.remoting.jaxrpc.ServletEndpointSupport;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.PacsPictureDecodeBase64Util;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.huojianwa.bean.ApplyList;
import com.hjw.webService.client.huojianwa.bean.Apply;
import com.hjw.webService.client.huojianwa.bean.ItemList_Report;
import com.hjw.webService.client.huojianwa.bean.Item_Apply;
import com.hjw.webService.client.huojianwa.bean.Report;
import com.hjw.webService.client.huojianwa.bean.ResponseTC;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.PacsSendDTO;
import com.hjw.wst.service.CompanyService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * pacs接口火箭蛙方案
 * @author zwx
 * 目前正在使用的医院有：天长、咸宁（报告结果推送和报告回收）
 */
public class Service4PACS extends ServletEndpointSupport {
	private ThridInterfaceLog til;
	private CompanyService companyService;
	private ConfigService configService;
	private JdbcQueryManager jdbcQueryManager;

	protected void onInit() {
		this.companyService = (CompanyService) getWebApplicationContext().getBean("companyService");
		this.configService = (ConfigService) getWebApplicationContext().getBean("configService");
		this.jdbcQueryManager = (JdbcQueryManager) getWebApplicationContext().getBean("jdbcQueryManager");
	}

	public String acceptMessageTest(String xmlmessage) {
		return "返回ok----参数为：" + xmlmessage;
	}

	//天长专用
	public String GetRequest(String ExamNo) {
		return GetRequest(ExamNo, "");
	}
	
	/**
	 * @Title: GetRequest
	 * @Description: 获取个人信息及检查项目
	 * @param: String 申请单号
	 */
	public String GetRequest(String ExamNo, String reqNo) {
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(reqNo);
    	til.setExam_no(ExamNo);
//    	til.setMessage_id();
    	til.setMessage_name("GetRequest");
    	til.setMessage_type("webservice");
    	til.setSender("PACS");
    	til.setReceiver("PEIS");
    	til.setMessage_request(reqNo);
    	til.setFlag(2);
    	til.setXtgnb_id("0");
    	til.setMessage_inout(1);
		configService.insert_log(til);
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "ExamNo:" + ExamNo + "-reqNo:" + reqNo);
		ResponseTC response = new ResponseTC();
		try {
			if (StringUtil.isEmpty(ExamNo) && StringUtil.isEmpty(reqNo)) {
				response.setCode(1);
				response.setMessage("pacs传入参数为空 ExamNo:" + ExamNo + "-reqNo:" + reqNo);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "pacs传入参数为空 ExamNo:" + ExamNo + "-reqNo:" + reqNo);
			} else {
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				if(StringUtil.isEmpty(ExamNo)) {
					ei = this.configService.getExamInfoForReqNum(reqNo);
					ExamNo = ei.getExam_num();
				}else{
					ei = this.configService.getExamInfoForNum(ExamNo);
				}
				if ((ei == null) || (ei.getId() <= 0)) {
					response.setCode(1);
					response.setMessage("查无此人，获取pacs信息错误。 ExamNo:" + ExamNo + "-reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "查无此人，获取pacs信息错误。 ExamNo:" + ExamNo + "-reqNo:" + reqNo);
				} else if ("Z".equals(ei.getStatus())) {
					response.setCode(1);
					response.setMessage("此人已经总检，获取pacs信息错误。 ExamNo:" + ExamNo + "-reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检，获取pacs信息错误。 ExamNo:" + ExamNo + "-reqNo:" + reqNo);
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,dd.dep_name as dept_name,c.item_name,dd.dep_inter_num as dept_code,dd.dep_num,ec.id,ec.pay_status,ec.amount,c.id as itemId "
							+" from examinfo_charging_item ec,pacs_summary p,pacs_detail d,department_dep dd,charging_item c"
							+" where ec.charge_item_id = c.id and p.id = d.summary_id and d.chargingItem_num = c.item_code" 
							+" and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' and ec.change_item != 'C'" 
							+" and ec.pay_status != 'M' and ec.exam_status in ('N','D')"
							+" and ec.examinfo_id = "+ei.getId());
					if(!StringUtil.isEmpty(reqNo)) {
						sb.append(" and p.pacs_req_code = '"+reqNo+"'");
					} else {
						sb.append(" and p.examinfo_num = '"+ExamNo+"'");
					}
					
					boolean isBufa = false;
					if(!isBufa){
						sb.append(" and ec.is_application = 'N'");
					}
					
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "查项目sql:"+sb.toString());
					List<PacsSendDTO> pacsSendList = this.jdbcQueryManager.getList(sb.toString(), PacsSendDTO.class);
					
					String noPayItems = "";
					if("N".equals(ei.getIs_after_pay())){
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
						configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检号【"+ei.getExam_num()+"】没有需要发送申请的影像科室项目!");
						response.setCode(1);
						response.setMessage("体检号【"+ei.getExam_num()+"】没有需要发送申请的影像科室项目!");
					}else if(pacsSendList.size() == 0 && (!"".equals(noPayItems))){
						configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "体检号【"+ei.getExam_num()+"】项目("+noPayItems+")未付费!");
						response.setCode(1);
						response.setMessage("体检号【"+ei.getExam_num()+"】项目("+noPayItems+")未付费!");
					}else{
						String doctorid = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value().trim();//开单医生姓名
						Map<String, Apply> map = new HashMap<>();
						String SQKS = companyService.getDatadis("SQKS").get(0).getRemark();
						for(PacsSendDTO pacssend : pacsSendList){
							Apply apply = new Apply();
							if(map.containsKey(pacssend.getPacs_req_code())) {
								apply = map.get(pacssend.getPacs_req_code());
							} else {
								map.put(pacssend.getPacs_req_code(), apply);
							}
							apply.setReqNo(pacssend.getPacs_req_code());
							apply.setExamNum(ei.getExam_num());
							apply.setPatName(ei.getUser_name());
							apply.setSex(ei.getSex());
							apply.setBirthday(ei.getBirthday());
							apply.setAge(""+ei.getAge());
							apply.setStudyType(pacssend.getDep_num());
							apply.setReqDoctor(doctorid);
							apply.setReqDate(DateTimeUtil.getDateTimes());
							apply.setReqDept(SQKS);
							apply.setPerformedBy(pacssend.getDept_code());
							
							Item_Apply item = new Item_Apply();
							item.setSeqNo(apply.getItemList().getItem().size());
							item.setChargingId(pacssend.getItem_code());
							item.setItemName(pacssend.getItem_name());
							item.setPrice(""+pacssend.getAmount());
							item.setPacsItemCode(pacssend.getView_num());
							apply.getItemList().getItem().add(item);
						}
						
						ApplyList applyList = new ApplyList();
						for (String key : map.keySet()) {
							Apply apply = map.get(key);
							applyList.getApply().add(apply);
						}
						String message = JaxbUtil.convertToXml(applyList, true);
						configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "message:"+message);
						response.setCode(0);
						response.setMessage(message);
						til.setFlag(0);
					}
				}
			}
		} catch (Throwable ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			response.setCode(1);
			response.setMessage("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String resxml = JaxbUtil.convertToXmlWithCDATA(response, "^Message");
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:" + resxml);
		
		til.setReq_no(reqNo);
    	til.setExam_no(ExamNo);
		til.setMessage_response(resxml);
		configService.update_log(til);
		return resxml;
	}
	
	/**
	 * @Title: ConfirmRequest
	 * @Description: PACS系统登记确认后，调用此接口，表示申请单已登记， 此时体检系统不再做退项处理。
	 * @param: String 申请单号
	 * @param: String PACS检查项目代码
	 */
	public String ConfirmRequest(String reqNo, String PacsItemCode) {
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(reqNo);
//    	til.setMessage_id();
    	til.setMessage_name("ConfirmRequest");
    	til.setMessage_type("webservice");
    	til.setSender("PACS");
    	til.setReceiver("PEIS");
    	til.setMessage_request(reqNo+"-"+PacsItemCode);
    	til.setFlag(2);
    	til.setXtgnb_id("0");
    	til.setMessage_inout(1);
		configService.insert_log(til);
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "reqNo:" + reqNo);
		ResponseTC response = new ResponseTC();
		try {
			if (StringUtil.isEmpty(reqNo)) {
				response.setCode(1);
				response.setMessage("pacs传入参数为空 reqNo:" + reqNo);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "pacs传入参数为空 reqNo:" + reqNo);
			} else {
				ExamInfoUserDTO ei = this.configService.getExamInfoForReqNum(getMinOne(reqNo));
				if ((ei == null) || (ei.getId() <= 0)) {
					response.setCode(1);
					response.setMessage("根据申请单号查无此人，不能确认登记。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据申请单号查无此人，不能确认登记。reqNo:" + reqNo);
				} else if ("Z".equals(ei.getStatus())) {
					response.setCode(1);
					response.setMessage("此人已经总检，不能确认登记。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检，不能确认登记。reqNo:" + reqNo);
				} else {
					if(StringUtil.isEmpty(PacsItemCode)) {
						this.configService.setExamInfoChargeItemPacsStatus(reqNo, "C");
					} else {
						this.configService.setExamInfoChargeItemPacsStatus(reqNo, PacsItemCode, "C");
					}
					til.setFlag(0);
				}
				til.setExam_no(ei.getExam_num());
			}
		} catch (Throwable ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			response.setCode(1);
			response.setMessage("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}

		String resxml = JaxbUtil.convertToXmlWithCDATA(response, "^Message");
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:" + resxml);
		
		til.setMessage_response(resxml);
		configService.update_log(til);
		return resxml;
	}
	
	/**
	 * @Title: CancelRequest
	 * @Description: 当PACS系统登记后，需要取消登记，调用此接口，表示PACS系统已撤销该申请单，此时体检系统可作退项处理。
	 * @param: String 申请单号
	 * @param: String PACS检查项目代码
	 */
	public String CancelRequest(String reqNo, String PacsItemCode) {
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(reqNo);
//    	til.setMessage_id();
    	til.setMessage_name("CancelRequest");
    	til.setMessage_type("webservice");
    	til.setSender("PACS");
    	til.setReceiver("PEIS");
    	til.setMessage_request(reqNo+"-"+PacsItemCode);
    	til.setFlag(2);
    	til.setXtgnb_id("0");
    	til.setMessage_inout(1);
		configService.insert_log(til);
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "reqNo:" + reqNo);
		ResponseTC response = new ResponseTC();
		try {
			if (StringUtil.isEmpty(reqNo)) {
				response.setCode(1);
				response.setMessage("pacs传入参数为空 reqNo:" + reqNo);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "pacs传入参数为空 reqNo:" + reqNo);
			} else {
				ExamInfoUserDTO ei = this.configService.getExamInfoForReqNum(getMinOne(reqNo));
				if ((ei == null) || (ei.getId() <= 0)) {
					response.setCode(1);
					response.setMessage("根据申请单号查无此人，不能取消登记。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据申请单号查无此人，不能取消登记。reqNo:" + reqNo);
				} else if ("Z".equals(ei.getStatus())) {
					response.setCode(1);
					response.setMessage("此人已经总检，不能取消登记。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检，不能取消登记。reqNo:" + reqNo);
				} else {
					if(StringUtil.isEmpty(PacsItemCode)) {
						this.configService.setExamInfoChargeItemPacsStatus(reqNo, "N");
					} else {
						this.configService.setExamInfoChargeItemPacsStatus(reqNo, PacsItemCode, "N");
					}
					til.setFlag(0);
				}
				til.setExam_no(ei.getExam_num());
			}
		} catch (Throwable ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			response.setCode(1);
			response.setMessage("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String resxml = JaxbUtil.convertToXmlWithCDATA(response, "^Message");
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:" + resxml);
		
		til.setMessage_response(resxml);
		configService.update_log(til);
		return resxml;
	}
	
	/**
	 * @Title: PutReport
	 * @Description: PACS系统结果审核发布后，调用此接口，推送报告和报告图片(将报告存为JPG格式)至体检系统。
	 * @param: String XML格式字符串
	 */
	public String PutReport(String report) {
		String reportWithOutBase64 = report;
		if(report != null) {
			reportWithOutBase64 = report.replaceAll("<pic>.*</pic>", "<pic></pic>");
		}
		
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//    	til.setMessage_id();
    	til.setMessage_name("PutReport");
    	til.setMessage_type("webservice");
    	til.setSender("PACS");
    	til.setReceiver("PEIS");
    	til.setMessage_request(reportWithOutBase64);
    	til.setFlag(2);
    	til.setXtgnb_id("0");
    	til.setMessage_inout(1);
		configService.insert_log(til);
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "reportWithOutBase64:" + reportWithOutBase64);
		ResponseTC response = new ResponseTC();
		try {
			if (StringUtil.isEmpty(report)) {
				response.setCode(1);
				response.setMessage("pacs传入参数为空 report:" + report);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "pacs传入参数为空 report:" + report);
			} else {
				Report reportData = JaxbUtil.converyToJavaBean(report, Report.class);
				String reqNo = reportData.getReqNo();
				ExamInfoUserDTO ei = this.configService.getExamInfoForReqNum(getMinOne(reqNo));
				if ((ei == null) || (ei.getId() <= 0)) {
					response.setCode(1);
					response.setMessage("根据申请单号查无此人，报告推送失败。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据申请单号查无此人，报告推送失败。reqNo:" + reqNo);
				} else if ("Z".equals(ei.getStatus())) {
					response.setCode(1);
					response.setMessage("此人已经总检，报告推送失败。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检，报告推送失败。reqNo:" + reqNo);
				} else {
					PacsResult pacsResult = new PacsResult();
					pacsResult.setReq_no(reportData.getReqNo());
					pacsResult.setPacs_checkno(reportData.getPacsCheckno());
					pacsResult.setExam_num(reportData.getExamNum());
					pacsResult.setTil_id(til.getId());
					
					String itemNames = "";
					String pacsItemCodes = "";
					String studyBodyParts = "";
					for(ItemList_Report item : reportData.getItemList()) {
						itemNames += (item.getItemName() + ",");
						pacsItemCodes += (item.getPacsItemCode() + ",");
						studyBodyParts += (item.getStudyBodyPart() + ",");
					}
					itemNames = itemNames.substring(0, itemNames.length()-1);
					pacsItemCodes = pacsItemCodes.substring(0, pacsItemCodes.length()-1);
					studyBodyParts = studyBodyParts.substring(0, studyBodyParts.length()-1);
					
					pacsResult.setItem_name(itemNames);
					pacsResult.setPacs_item_code(pacsItemCodes);
					pacsResult.setStudy_type(reportData.getItemList().get(0).getStudyType());
					pacsResult.setStudy_body_part(studyBodyParts);
					pacsResult.setClinic_diagnose(reportData.getClinicDiagnose());
					pacsResult.setClinic_symptom(reportData.getClinicSymptom());
					pacsResult.setClinic_advice(reportData.getClinicAdvice());
					pacsResult.setIs_abnormal(reportData.getIsAbnormal());
					String base64 = reportData.getRptPic().getPic();
					TranLogTxt.liswriteEror_to_txt("PutReport", "长度:" +base64.length());
					TranLogTxt.liswriteEror_to_txt("PutReport", "内容:" +base64);
					if (reportData.getRptPic().getPic().length() > 10) {
						String picname = PacsPictureDecodeBase64Util.decodeBase64JPG(reportData.getExamNum(), getMinOne(reqNo), DateTimeUtil.getDate(), reportData.getRptPic().getPic());
						pacsResult.setIs_tran_image(0);
						pacsResult.setReport_img_path(picname);
					} else {
						pacsResult.setIs_tran_image(0);
					}
					pacsResult.setReg_doc(reportData.getRegDoc());
					pacsResult.setCheck_doc(reportData.getCheckDoc());
					pacsResult.setCheck_date(reportData.getCheckDate());
					pacsResult.setReport_doc(reportData.getRegDoc());
					pacsResult.setReport_date(reportData.getReportDate());
					pacsResult.setAudit_doc(reportData.getAuditDoc());
					pacsResult.setAudit_date(reportData.getAuditDate());
					boolean succ = this.configService.insert_pacs_result(pacsResult);
					if (succ) {
						til.setFlag(0);
					} else {
						response.setCode(1);
						response.setMessage("pacs 入中间库失败");
					}
				}
				til.setReq_no(reqNo);
				til.setExam_no(reportData.getExamNum());
			}
		} catch (Throwable ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			response.setCode(1);
			response.setMessage("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String resxml = JaxbUtil.convertToXmlWithCDATA(response, "^Message");
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:" + resxml);
		
		til.setMessage_response(resxml);
		configService.update_log(til);
		return resxml;
	}
	
	/**
	 * @Title: ReportRecovery
	 * @Description: 当PACS发现已发出报告需要回收时，调用此接口，由体检系统返回回收状态，对于成功回收的报告，PACS可修改报告，并重新发布；对于回收失败的报告，PACS不可修改报告及重新发布报告。
	 * @param: String XML格式字符串
	 */
	public String ReportRecovery(String reqNo, String PacsItemCode) {
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(reqNo);
//    	til.setMessage_id();
    	til.setMessage_name("ReportRecovery");
    	til.setMessage_type("webservice");
    	til.setSender("PACS");
    	til.setReceiver("PEIS");
    	til.setMessage_request(reqNo+"-"+PacsItemCode);
    	til.setFlag(2);
    	til.setXtgnb_id("0");
    	til.setMessage_inout(1);
		configService.insert_log(til);
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "reqNo:" + reqNo);
		ResponseTC response = new ResponseTC();
		try {
			if (StringUtil.isEmpty(reqNo)) {
				response.setCode(1);
				response.setMessage("pacs传入参数为空 reqNo:" + reqNo);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "pacs传入参数为空 reqNo:" + reqNo);
			} else {
				ExamInfoUserDTO ei = this.configService.getExamInfoForReqNum(getMinOne(reqNo));
				if ((ei == null) || (ei.getId() <= 0)) {
					response.setCode(1);
					response.setMessage("根据申请单号查无此人，报告不能回收。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "根据申请单号查无此人，报告不能回收。reqNo:" + reqNo);
				} else if ("Z".equals(ei.getStatus())) {
					response.setCode(1);
					response.setMessage("此人已经总检，报告不能回收。reqNo:" + reqNo);
					configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "此人已经总检，报告不能回收。reqNo:" + reqNo);
				} else {
					til.setFlag(0);
				}
				til.setExam_no(ei.getExam_num());
			}
		} catch (Throwable ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			response.setCode(1);
			response.setMessage("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String resxml = JaxbUtil.convertToXmlWithCDATA(response, "^Message");
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:" + resxml);
		
		til.setMessage_response(resxml);
		configService.update_log(til);
		return resxml;
	}
	
	private static String getMinOne(String reqNos) {
		String min = reqNos;
		for(String reqNo : reqNos.split(",")) {
			if(reqNo.compareTo(min) < 0) {
				min = reqNo;
			}
		}
		return min;
	}
	
	public static void main(String[] args) {
		String reqNos = "2018102300015,2018102300012,2018102300011,2018102300014,2018102300016";
		System.out.println(getMinOne(reqNos));
		
		String report = "<pic>123456789</pic>";
		String reportWithOutBase64 = report;
		if(report != null) {
			reportWithOutBase64 = report.replaceAll("<pic>.*</pic>", "<pic></pic>");
		}
		System.out.println(reportWithOutBase64);
	}
}
