package com.hjw.webService.client.bdyx.useCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.Bean.ZJ.SummaryMsg;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.bean.RequestPost;
import com.hjw.webService.client.bdyx.bean.ResponsePost;
import com.hjw.webService.client.bdyx.bean.report.ExamInfo;
import com.hjw.webService.client.bdyx.bean.report.LabInfo;
import com.hjw.webService.client.bdyx.bean.report.LabSubItemInfo_SC;
import com.hjw.webService.client.bdyx.bean.report.LabSubItemInfo_ST;
import com.hjw.webService.client.bdyx.bean.report.PhyExamInfo_SC;
import com.hjw.webService.client.bdyx.bean.report.PhyExamInfo_ST;
import com.hjw.webService.client.bdyx.bean.report.PhysicalExamInfo;
import com.hjw.webService.client.bdyx.bean.report.ReportBDYX;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.job.bean.ExamSummaryDTO;
import com.hjw.wst.DTO.CommonExamDetailDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExamResultDetailDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.hjw.wst.DTO.ViewExamDetailDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class PutReportControlBDYX_UseCode {
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public ResultHeader getMessage(String url, int days, String logName) {
		String datetime = DateTimeUtil.DateDiff2(days);
		ResultHeader rh = new ResultHeader();

		String sb1 = "SELECT a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest,"
				+ "a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time,"
				+ "a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time,"
				+ "x.chi_name as examdocname,y.chi_name as checkdocname,b.exam_num,b.join_date" 
				+ "  FROM exam_summary a "
				+ "  left join user_usr x on x.id=a.exam_doctor_id"
				+ "  left join user_usr y on y.id=a.check_doc ,exam_info b "
				+ "where a.approve_status='A' and read_status='0' and a.exam_info_id=b.id "
				+ " and (CONVERT(varchar(50),a.create_time,23)>= '" + datetime
				+ "' or CONVERT(varchar(50),a.update_time,23)>= '" + datetime + "')";
		TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
		List<ExamSummaryDTO> userList = this.jdbcQueryManager.getList(sb1, ExamSummaryDTO.class);
		for (ExamSummaryDTO es : userList) {
			String logNameDetail = es.getExam_num()+"-"+logName;
			TranLogTxt.liswriteEror_to_txt(logName, "明细日志：" + logNameDetail);
			sendreportone(url,es,logNameDetail);
		}
		rh.setTypeCode("AA");
		rh.setText("");
		return rh;
	}
	
	public ResultHeader sendMessage(String url, String exam_num, String logName) {
		ResultHeader rh = new ResultHeader();
		if (exam_num != null && exam_num.trim().length() > 0) {
			String sb1 = "SELECT a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest,"
					+ "a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time,"
					+ "a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time,"
					+ "x.chi_name as examdocname,y.chi_name as checkdocname,b.exam_num,b.join_date" + "  FROM exam_summary a "
					+ "  left join user_usr x on x.id=a.exam_doctor_id"
					+ "  left join user_usr y on y.id=a.check_doc ,exam_info b "
					+ "where a.approve_status='A' and a.exam_info_id=b.id " + " and b.exam_num='" + exam_num + "' ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			List<ExamSummaryDTO> userList = this.jdbcQueryManager.getList(sb1, ExamSummaryDTO.class);
			if (userList != null && userList.size() == 1) {
				rh = sendreportone(url, userList.get(0), logName);
			} else {
				rh.setTypeCode("AE");
				rh.setText("不满足上传条件，可能未审核");
			}
		} else {
			rh.setTypeCode("AE");
			rh.setText("无效体检编号");
		}
		return rh;
	}

	private ResultHeader sendreportone(String url,ExamSummaryDTO es,String logName){
	     ResultHeader rh = new ResultHeader();		
			try {
				ReportBDYX report = new ReportBDYX();
				ExamInfoUserDTO ei = this.configService.getExamInfoForExam_id(es.getExam_info_id());
				
				String sexcode = "0";
				if("男".equals(ei.getSex()) || "男性".equals(ei.getSex())) {
					sexcode = "1";
				} else if("女".equals(ei.getSex()) || "女性".equals(ei.getSex())) {
					sexcode = "2";
				}
				String birthDate = ei.getBirthday().replaceAll("-", "").replaceAll(" ", "").replaceAll("-", "");
				if(!StringUtil.isEmpty(birthDate) && birthDate.length()==8) {
					birthDate = birthDate + "000000";
				}
				
				report.setReportNo(ei.getExam_num());
				report.setDocumentName(ei.getExam_num());
				report.setEffectiveTime(es.getUpdate_time());
				report.setPatientLid(ei.getPatient_id());
				report.setMedicalNo(ei.getExam_num());
				report.setPhysicalExaNo(ei.getExam_num());
				report.setVisitOrdNo(ei.getExam_num());
				report.setPhysicalExaDate(ei.getJoin_date());
				report.setPatientName(ei.getUser_name());
				report.setIdentityCard(ei.getId_num());
				report.setGenderCode(sexcode);
				report.setGenderName(ei.getSex());
				report.setBirthDate(ei.getBirthday());
				report.setAge(""+ei.getAge());
				report.setTelephoneNo(ei.getPhone());
				report.setCompany(ei.getCompany());
				report.setReportDate(es.getCreate_time());
				
				UserInfoDTO user = getUser(es.getCreater(), logName);
				report.setReporterId(user.getWork_num());
				report.setReporterName(user.getLog_Name());
				report.setSummary(es.getSuggest());
				report.setSummaryDate(es.getCreate_time());
				report.setSummaryDocId(user.getWork_num());
				report.setSummaryDocName(user.getLog_Name());
				
				// 普通科室小结
				StringBuffer ksxjstr = new StringBuffer();
				
				ksxjstr.append(" select n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.center_num,n.health_level,");
				ksxjstr.append("n.exam_result,n.exam_date,n.creater,n.create_time,n.updater,n.update_time,a.dep_id,b.charging_item_id,a.item_name as charging_item_name,dep.dep_name,x.item_num,");
				ksxjstr.append("x.item_name,x.seq_code,x.ref_Mmax,x.ref_Mmin,x.ref_Fmin,x.ref_Fmax,a.item_code as charging_item_code");
				ksxjstr.append(",x.item_unit,x.item_category");
				ksxjstr.append(" from common_exam_detail n");
				ksxjstr.append(",charging_item a,charging_item_exam_item b,examination_item x,department_dep dep,examinfo_charging_item y");
				ksxjstr.append(" where b.charging_item_id=a.id and x.id=b.exam_item_id and a.isActive='Y' and x.is_Active='Y' ");
				ksxjstr.append(" and dep.isActive='Y' and y.isActive='Y' and dep.id=a.dep_id ");
				ksxjstr.append(" and y.charge_item_id=a.id and y.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
				ksxjstr.append(" and n.exam_info_id='" + es.getExam_info_id() + "'  order by dep.seq_code,a.item_seq,x.seq_code  ");
				TranLogTxt.liswriteEror_to_txt(logName, "普通科室小结： " + ksxjstr.toString());
				List<CommonExamDetailDTO> ksmxList = this.jdbcQueryManager.getList(ksxjstr.toString(),
						CommonExamDetailDTO.class);

				if ((ksmxList != null) && (ksmxList.size() > 0)) {
					Map<String, PhysicalExamInfo> map = new HashMap<>();
					for (CommonExamDetailDTO dd : ksmxList) {
						PhysicalExamInfo physicalExamInfo = null;
						if(map.containsKey(dd.getCharging_item_code())) {
							physicalExamInfo = (PhysicalExamInfo)map.get(dd.getCharging_item_code());
						} else {
							physicalExamInfo = new PhysicalExamInfo();
							map.put(dd.getCharging_item_code(), physicalExamInfo);
							physicalExamInfo.setItemCode(dd.getCharging_item_code());
							physicalExamInfo.setItemName(dd.getCharging_item_name());
//							physicalExamInfo.setExamDocId();
							physicalExamInfo.setExamDocName(dd.getExam_doctor());
							physicalExamInfo.setExamDate(dd.getExam_date());
//							physicalExamInfo.setBriefSummary();
						}
						
						if ("长文本型".equals(dd.getItem_category().trim()) || "短文本型".equals(dd.getItem_category().trim())) {
							PhyExamInfo_ST phyExamInfo_ST = new PhyExamInfo_ST();
							phyExamInfo_ST.setPhyExamCode(dd.getItem_num());
							phyExamInfo_ST.setPhyExamName(dd.getItem_name());
							phyExamInfo_ST.setPhyExamTextValue(dd.getExam_result());
							physicalExamInfo.getPhyExamInfo_ST().add(phyExamInfo_ST);
						} else if ("数字型".equals(dd.getItem_category().trim()) || "数值型".equals(dd.getItem_category().trim())) {
							PhyExamInfo_SC phyExamInfo_SC = new PhyExamInfo_SC();
							phyExamInfo_SC.setPhyExamCode(dd.getItem_num());
							phyExamInfo_SC.setPhyExamName(dd.getItem_name());
							phyExamInfo_SC.setPhyExamValue(dd.getExam_result());
							phyExamInfo_SC.setPhyExamQueValueUnit(dd.getItem_unit());
							physicalExamInfo.getPhyExamInfo_SC().add(phyExamInfo_SC);
						}
					}
					report.setPhysicalExamInfo(new ArrayList(map.values()));
				}

				// 检验科室检查结果明细表
				StringBuffer jykstr = new StringBuffer();

				jykstr.append("select n.id,n.exam_info_id,n.exam_item_id,n.exam_category,n.exam_item_category,");
				jykstr.append("n.exam_doctor,n.exam_date,n.exam_result,n.dang_min,n.dang_max,n.ref_min,n.ref_max,");
				jykstr.append("n.ref_indicator,n.center_num,n.approver,n.approve_date,n.creater,n.create_time,");
				jykstr.append("n.updater,n.update_time,n.item_unit,n.ref_value,a.dep_id,dep.dep_name,x.item_num,x.item_name");
				jykstr.append(",x.seq_code,x.item_category,a.item_name as charging_item_name,b.charging_item_id,a.item_code as charging_item_code");
				jykstr.append(" from exam_result_detail n,");
				jykstr.append("charging_item a,examinfo_charging_item g,charging_item_exam_item b,examination_item x,department_dep dep");
				jykstr.append(" where b.charging_item_id=a.id and a.isActive='Y' and g.isActive='Y' and x.is_Active='Y' ");
				jykstr.append(" and x.is_Active='Y' and dep.isActive='Y' and a.id=g.charge_item_id and x.id=b.exam_item_id ");
				jykstr.append(" and dep.id=a.dep_id and g.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
				jykstr.append(" and n.exam_info_id='" + es.getExam_info_id() + "' order by dep.seq_code,a.item_seq,x.seq_code ");
				TranLogTxt.liswriteEror_to_txt(logName, "检验科室检查结果明细表： " + jykstr.toString());
				List<ExamResultDetailDTO> jyksList = this.jdbcQueryManager.getList(jykstr.toString(), ExamResultDetailDTO.class);

				if ((jyksList != null) && (jyksList.size() > 0)) {
					Map<String, LabInfo> map = new HashMap<>();
					for (ExamResultDetailDTO dd : jyksList) {
						LabInfo labInfo = null;
						if(map.containsKey(dd.getCharging_item_code())) {
							labInfo = (LabInfo)map.get(dd.getCharging_item_code());
						} else {
							labInfo = new LabInfo();
							map.put(dd.getCharging_item_code(), labInfo);
							labInfo.setItemCode(dd.getCharging_item_code());
							labInfo.setItemName(dd.getCharging_item_name());
//							labInfo.setLabDocCode();
							labInfo.setLabDocName(dd.getExam_doctor());
//							labInfo.setBriefSummary();
						}
						if ("长文本型".equals(dd.getItem_category().trim()) || "短文本型".equals(dd.getItem_category().trim())) {
							LabSubItemInfo_ST labSubItemInfo_ST = new LabSubItemInfo_ST();
							labSubItemInfo_ST.setDisplayOrder(""+dd.getSeq_code());
							labSubItemInfo_ST.setLabSubItemCode(dd.getItem_num());
							labSubItemInfo_ST.setLabSubItemName(dd.getItem_name());
							labSubItemInfo_ST.setLabSubTextValue(dd.getExam_result());
							labSubItemInfo_ST.setReferenceRange(dd.getRef_value().trim());
//							LabSubItemInfo_ST.setUnusualDesc();
							labInfo.getLabSubItemInfo_ST().add(labSubItemInfo_ST);
						} else if ("数字型".equals(dd.getItem_category().trim()) || "数值型".equals(dd.getItem_category().trim())) {
							LabSubItemInfo_SC labSubItemInfo_SC = new LabSubItemInfo_SC();
							labSubItemInfo_SC.setDisplayOrder(""+dd.getSeq_code());
							labSubItemInfo_SC.setLabSubItemCode(dd.getItem_num());
							labSubItemInfo_SC.setLabSubItemName(dd.getItem_name());
							labSubItemInfo_SC.setLabSubValue(dd.getExam_result());
							labSubItemInfo_SC.setLabSubQueValueUnit(dd.getItem_unit());
//							labSubItemInfo_SC.setUnusualDesc();
							labSubItemInfo_SC.setReferenceRange(dd.getRef_value().trim());
							labSubItemInfo_SC.setReferenceRangeUnit(dd.getItem_unit());
							labInfo.getLabSubItemInfo_SC().add(labSubItemInfo_SC);
						}
					}
					report.setLabInfo(new ArrayList(map.values()));
				}
				
				// 影像科室检查结果明细表
				StringBuffer jckstr = new StringBuffer();
				jckstr.append(
						"select n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.report_picture_path,n.exam_desc,"
								+ "n.exam_result,n.exam_date,n.center_num,n.approver,n.approve_date,n.creater,n.create_time,"
								+ "n.updater,n.update_time,n.pacs_id,m.dep_id,m.dep_num,m.dep_name,m.charging_item_name,m.charging_item_id,"
								+ "x.item_num,x.item_name,m.charging_item_code"
								+ " from view_exam_detail n left join ("
								+ "select dep_id,b.item_name as charging_item_name,b.id as charging_item_id,b.item_code as charging_item_code,a.id,"
								+ "dep.dep_name,dep.dep_num from pacs_summary a,pacs_detail g,charging_item b,department_dep dep"
								+ "   where a.id=g.summary_id and g.chargingItem_num=b.item_code and dep.id=b.dep_id) m "
								+ "on m.id=n.pacs_id ,examination_item x,department_dep dep where n.exam_info_id='" + es.getExam_info_id() + "' "
								+ " and x.id=n.exam_item_id and m.dep_id=dep.id order by dep.seq_code,x.seq_code");
				TranLogTxt.liswriteEror_to_txt(logName, "影像科室检查结果明细表： " + jckstr.toString());
				List<ViewExamDetailDTO> jcksList = this.jdbcQueryManager.getList(jckstr.toString(), ViewExamDetailDTO.class);
				List<SummaryMsg> summarys = new ArrayList<SummaryMsg>();
				if ((jcksList != null) && (jcksList.size() > 0)) {
					for (ViewExamDetailDTO dd : jcksList) {
						ExamInfo examInfo = new ExamInfo();
						examInfo.setDeptCode(dd.getDep_id() + "");//科室编码
						examInfo.setExamDate(dd.getExam_date());//检查日期
//						examInfo.setExamDocId();//检查医生编码
						examInfo.setExamDocName(dd.getExam_doctor());//检查医生名称
						examInfo.setReportDate(dd.getApprove_date());//报告日期
//						examInfo.setReviewerId();//审核医生编码
						examInfo.setReviewerName(dd.getApprover());//审核医生名称
						examInfo.setExamItemCode(dd.getCharging_item_code());//项目编码
						examInfo.setExamItemName(dd.getCharging_item_name());//项目名称
						examInfo.setExamDescription(dd.getExam_desc());//检查描述
						examInfo.setDeptName(dd.getExam_result());//检查提示
						report.getExamInfo().add(examInfo);
					}
				}
				
				String reportStr = JSONObject.fromObject(report).toString();
				TranLogTxt.liswriteEror_to_txt(logName,"req:"+reportStr);
				
				RequestPost requestBDYX = new RequestPost();
				requestBDYX.setService_id("BS347");
				requestBDYX.setExec_uint_id("");
				requestBDYX.setOrder_exec_id("029");
				requestBDYX.setExtend_sub_id("");
				requestBDYX.setBody(reportStr);
				String requestStr = JSONObject.fromObject(requestBDYX).toString();
				TranLogTxt.liswriteEror_to_txt(logName,"req:"+requestStr);
				String responseStr = HttpUtil.doPost(url,requestStr,"utf-8");
				TranLogTxt.liswriteEror_to_txt(logName,"res:"+responseStr);
				ResponsePost responseBDYX = new Gson().fromJson(responseStr, ResponsePost.class);
				if(1 == responseBDYX.getStatus()) {
					String sql = "update exam_summary set read_status='1',read_time='" + DateTimeUtil.getDateTime()
					+ "' where id ='" + es.getId() + "' ";
					this.jdbcPersistenceManager.executeSql(sql);
					rh.setTypeCode("AA");
					rh.setText("");
				}else{
					rh.setTypeCode("AE");
					rh.setText("报告上传不完整");
					TranLogTxt.liswriteEror_to_txt(logName, "LIS返回错误:" + responseBDYX.getErrMsg());
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rh.setTypeCode("AE");
				rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		return rh;
	}

	private UserInfoDTO getUser(long id, String logname) throws ServiceException {
		String sql = "select u.work_num,u.chi_name from user_usr u where  u.id = "+id+" and u.is_active = 'Y'";
		TranLogTxt.liswriteEror_to_txt(logname, "sql： " +sql);
		List<UserInfoDTO> list = this.jdbcQueryManager.getList(sql, UserInfoDTO.class);
		return list.get(0);
	}
}
