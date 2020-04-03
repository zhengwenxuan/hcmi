package com.hjw.webService.client.tj180;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ExamItemUpdateMessage;
import com.hjw.webService.client.ExamUpdateMessage;
import com.hjw.webService.client.body.ExamItemUpdateMessageBody;
import com.hjw.webService.client.body.ExamUpdateMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.DeptBriefBean;
import com.hjw.webService.client.tj180.Bean.DeptBriefBody;
import com.hjw.webService.client.tj180.Bean.PutBean;
import com.hjw.webService.client.tj180.Bean.SummerBody;
import com.hjw.webService.client.tj180.Bean.UpdateDiagnosisBean;
import com.hjw.webService.client.tj180.Bean.UpdateDiagnosisBody;
import com.hjw.webService.client.tj180.Bean.UpdateReserveResultBean;
import com.hjw.webService.client.tj180.Bean.UpdateReserveResultBody;
import com.hjw.webService.job.bean.CommonExamDetailDTO;
import com.hjw.webService.job.bean.ExamDepResultDTO;
import com.hjw.webService.job.bean.ExamResultDetailDTO;
import com.hjw.webService.job.bean.ExamSummaryDTO;
import com.hjw.webService.job.bean.ExaminfoDiseaseDTO;
import com.hjw.webService.job.bean.SummerDTO;
import com.hjw.webService.job.bean.ViewExamDetailDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class PutReportControlTJ180 {
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
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
			sendreportone(url,es,logName);
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
				//es.setExam_info_id(68);
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				ei = this.getExamInfoForNum(es.getExam_info_id(), logName);
				ExamUpdateMessageBody body = new ExamUpdateMessageBody();
				body.setExam_id(ei.getId());
				ExamUpdateMessage msg = new ExamUpdateMessage(body);
				msg.send();			
				ExamItemUpdateMessageBody itembody = new ExamItemUpdateMessageBody();
				itembody.setExam_id(ei.getId());
				ExamItemUpdateMessage itemmsg = new ExamItemUpdateMessage(itembody);
				itemmsg.send();
//				if("G".equals(ei.getExam_type())){
//					ExamUpdateMessageBody eupbody = new ExamUpdateMessageBody();
//					eupbody.setExam_id(ei.getId());
//					ExamUpdateMessage eupmsg = new ExamUpdateMessage(eupbody);
//					eupmsg.send();
//					
//					ExamItemUpdateMessageBody eibody = new ExamItemUpdateMessageBody();
//					eibody.setExam_id(ei.getId());
//					ExamItemUpdateMessage eimsg = new ExamItemUpdateMessage(eibody);
//					eimsg.send();
//				}
				// 普通科室小结
				UpdateReserveResultBody ur = new UpdateReserveResultBody();
				ur.setReserveId(es.getExam_num());
				List<UpdateReserveResultBean> resultInfo = new ArrayList<UpdateReserveResultBean>();
				StringBuffer ksxjstr = new StringBuffer();
				ksxjstr.append(" select DISTINCT n.exam_item_id,n.exam_info_id,n.exam_doctor,n.center_num,n.health_level,");
				ksxjstr.append(
						"n.exam_result,n.creater,CONVERT(varchar(50),n.exam_date,20) as exam_date,n.updater,n.update_time,a.dep_id,b.charging_item_id,a.item_name as charging_item_name,dep.dep_name,x.item_num,");
				ksxjstr.append(
						"x.item_name,x.seq_code,x.ref_Mmax,x.ref_Mmin,x.ref_Fmin,x.ref_Fmax,a.item_code as charging_item_code");
				ksxjstr.append(",x.item_unit,x.item_category,dep.dep_num");
				ksxjstr.append(" from common_exam_detail n");
				ksxjstr.append(
						",charging_item a,charging_item_exam_item b,examination_item x,department_dep dep,examinfo_charging_item y");
				ksxjstr.append(
						" where b.charging_item_id=a.id and x.id=b.exam_item_id and a.isActive='Y' and x.is_Active='Y' ");
				ksxjstr.append(" and dep.isActive='Y' and y.isActive='Y' and dep.id=a.dep_id ");
				ksxjstr.append(
						" and y.charge_item_id=a.id and y.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
				ksxjstr.append(" and n.exam_info_id='" + es.getExam_info_id() + "' and x.is_print='Y' ");
				TranLogTxt.liswriteEror_to_txt(logName, "res: :普通科室小结： " + ksxjstr.toString());
				List<CommonExamDetailDTO> ksmxList = this.jdbcQueryManager.getList(ksxjstr.toString(),
						CommonExamDetailDTO.class);

				for (CommonExamDetailDTO dd : ksmxList) {
					UpdateReserveResultBean om = new UpdateReserveResultBean();
					om.setHint("");// 提示 如：偏高、偏低
					om.setUnionProjectId(dd.getCharging_item_code());
					om.setExamineDoctor(dd.getExam_doctor());
					om.setExamineDate(dd.getExam_date());
					om.setUnits("");// 单位
					if ("男".equals(ei.getSex())) {
						om.setPrintContext(dd.getRef_Fmin() + "-" + dd.getRef_Fmax());
					} else if ("女".equals(ei.getSex())) {
						om.setPrintContext(dd.getRef_Mmin() + "-" + dd.getRef_Mmax());
					}
					if ("Z".equals(dd.getHealth_level())) {
						om.setIfAbnormity("0");
					} else if ("Y".equals(dd.getHealth_level())) {
						om.setIfAbnormity("1");
					} else if ("W".equals(dd.getHealth_level())) {
						om.setIfAbnormity("1");
					} else {
						om.setIfAbnormity("0");
					}
					om.setProjectId(dd.getItem_num());
					om.setProjectName(dd.getItem_name());
					om.setDeptId(dd.getDep_num());
					om.setResult(dd.getExam_result());
					resultInfo.add(om);
				}

				StringBuffer jykstr = new StringBuffer();

				jykstr.append("select DISTINCT n.exam_item_id,n.exam_info_id,n.exam_category,n.exam_item_category,");
				jykstr.append("n.exam_doctor,n.exam_result,n.ref_value,");
				jykstr.append(
						"n.ref_indicator,n.center_num,n.approver,n.approve_date,n.creater,CONVERT(varchar(50),n.exam_date,20) as exam_date,");
				jykstr.append(
						"n.updater,n.update_time,n.item_unit,n.ref_value,a.dep_id,dep.dep_name,x.item_num,x.item_name");
				jykstr.append(
						",x.seq_code,x.item_category,a.item_name as charging_item_name,b.charging_item_id,a.item_code as charging_item_code,dep.dep_num,a.dep_id,a.item_seq");
				jykstr.append(" from exam_result_detail n,");
				jykstr.append(
						"charging_item a,examinfo_charging_item g,charging_item_exam_item b,examination_item x,department_dep dep");
				jykstr.append(
						" where b.charging_item_id=a.id and a.isActive='Y' and g.isActive='Y' and x.is_Active='Y' ");
				jykstr.append(
						" and x.is_Active='Y' and dep.isActive='Y' and a.id=g.charge_item_id and x.id=b.exam_item_id ");
				jykstr.append(
						" and dep.id=a.dep_id and g.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
				jykstr.append(" and n.exam_info_id='" + es.getExam_info_id() + "' and x.is_print='Y'");
				TranLogTxt.liswriteEror_to_txt(logName, "res: :lis检查： " + jykstr.toString());
				List<ExamResultDetailDTO> jyksList = this.jdbcQueryManager.getList(jykstr.toString(),
						ExamResultDetailDTO.class);

				for (ExamResultDetailDTO dd : jyksList) {
					UpdateReserveResultBean om = new UpdateReserveResultBean();
					om.setUnionProjectId(dd.getCharging_item_code());
					om.setExamineDoctor(dd.getExam_doctor());
					om.setExamineDate(dd.getExam_date());
					om.setUnits(dd.getItem_unit());// 单位
					om.setPrintContext(dd.getRef_value());
					if ("0".equals(dd.getRef_indicator().trim())) {
						om.setIfAbnormity("0");
					} else if ("1".equals(dd.getRef_indicator().trim())) {
						om.setIfAbnormity("1");
						om.setHint("偏高");// 提示 如：偏高、偏低
					} else if ("2".equals(dd.getRef_indicator().trim())) {
						om.setIfAbnormity("1");
						om.setHint("偏低");// 提示 如：偏高、偏低
					} else if ("3".equals(dd.getRef_indicator().trim())) {
						om.setIfAbnormity("1");
						om.setHint("阳性");// 提示 如：偏高、偏低
					} else if ("4".equals(dd.getRef_indicator().trim())) {
						om.setIfAbnormity("1");
						om.setHint("危机");// 提示 如：偏高、偏低
					} else {
						om.setIfAbnormity("1");
					}
					
					om.setProjectId(dd.getItem_num());
					om.setProjectName(dd.getItem_name());
					om.setDeptId(dd.getDep_num());
					om.setResult(dd.getExam_result());
					om.setPrintContext(dd.getRef_value());
					resultInfo.add(om);
				}

				ur.setResultInfo(resultInfo);
				String web_url = url;
				TranLogTxt.liswriteEror_to_txt(logName, "res: :请求地址： " + web_url);
				ResultHeader rb1 = new ResultHeader();
				rb1 = this.putMessage(web_url, ur, logName);

				// 科室结论
				DeptBriefBody db = new DeptBriefBody();
				db.setReserveId(es.getExam_num());
				StringBuffer xjstr = new StringBuffer();
				xjstr.append("SELECT n.id,n.exam_info_id,n.dep_id,n.exam_result_summary,");
				xjstr.append(
						"n.suggestion,n.center_num,n.approver,CONVERT(varchar(50),n.approve_date,20) as approve_date,y.work_num as exam_doctor,CONVERT(varchar(50),n.create_time,20) as create_time,");
				xjstr.append(
						"n.updater,n.update_time,n.Special_setup,dep.dep_num,dep.dep_num,dep.dep_name,x.work_num as approvername");
				xjstr.append(" FROM exam_dep_result n");
				xjstr.append(" left join department_dep dep on dep.id=n.dep_id");
				xjstr.append(" left join user_usr x on x.id=n.approver");
				xjstr.append(" left join user_usr y on y.id=n.creater");
				xjstr.append(" where n.exam_info_id='" + es.getExam_info_id() + "'  order by n.dep_id,dep.seq_code ");
				TranLogTxt.liswriteEror_to_txt(logName, "res: :科室结论语句： " + xjstr.toString());
				List<ExamDepResultDTO> ksxjList = this.jdbcQueryManager.getList(xjstr.toString(),
						ExamDepResultDTO.class);
				String zslr="";
				String editor="";
				String ExamineDate="";
				String dept_num="";
				String dept_name="";
				List<DeptBriefBean> briefInfo = new ArrayList<DeptBriefBean>();
				long  dept_id=0;
				String zslrnew="";					
					for (ExamDepResultDTO dd : ksxjList) {										
						long new_dept_id=dd.getDep_id();
						
						if(dept_id!=new_dept_id){
							if(zslrnew.trim().length()>0){
								DeptBriefBean om = new DeptBriefBean();		
								om.setDeptId(dept_num);
								om.setBrief(dept_name+"检查结果:"+zslrnew+"\r\n");								
								om.setIfAbnormity("0");
								om.setEditor(editor);
								om.setEditDate(ExamineDate);
								om.setExamineDoctor(editor);
								om.setExamineDate(ExamineDate);
								briefInfo.add(om);
								dept_id=0;
								zslrnew="";
								ExamineDate="";
								editor="";
								dept_num="";
								dept_name="";
							}
								if (StringUtil.isEmpty(dd.getSuggestion())) {
									zslrnew=zslrnew+"\r\n"+dd.getExam_result_summary();
									zslr=zslr+"\r\n"+dd.getExam_result_summary();
								} else {
									zslrnew=zslrnew+"\r\n"+dd.getExam_result_summary() + "   医生建议：" + dd.getSuggestion();
									zslr=zslr+"\r\n"+dd.getExam_result_summary() + "   医生建议：" + dd.getSuggestion();
								}
								dept_num=dd.getDep_num();
								dept_name=dd.getDep_name();
								dept_id=new_dept_id;
								editor=dd.getExam_doctor();
								ExamineDate=dd.getCreate_time();
						}else{
							if (StringUtil.isEmpty(dd.getSuggestion())) {
								zslrnew=zslrnew+"\r\n"+dd.getExam_result_summary();
								zslr=zslr+"\r\n"+dd.getExam_result_summary();
							} else {
								zslrnew=zslrnew+"\r\n"+dd.getExam_result_summary() + "   医生建议：" + dd.getSuggestion();
								zslr=zslr+"\r\n"+dd.getExam_result_summary() + "   医生建议：" + dd.getSuggestion();
							}
							dept_num=dd.getDep_num();
							dept_name=dd.getDep_name();
							dept_id=new_dept_id;
							editor=dd.getExam_doctor();
							ExamineDate=dd.getCreate_time();
						}						
					}
					if(zslrnew.trim().length()>0){
						DeptBriefBean om = new DeptBriefBean();		
						om.setDeptId(dept_num);
						om.setBrief(dept_name+"检查结果:"+zslrnew);								
						om.setIfAbnormity("0");
						om.setEditor(editor);
						om.setEditDate(ExamineDate);
						om.setExamineDoctor(editor);
						om.setExamineDate(ExamineDate);
						briefInfo.add(om);
						dept_id=0;
						zslrnew="";
						ExamineDate="";
						editor="";
						dept_num="";
					}

				// lis 异常值
				jykstr = new StringBuffer();
				jykstr.append("select DISTINCT n.exam_item_id, n.id,n.exam_info_id,n.exam_category,n.exam_item_category,");
				jykstr.append("n.exam_doctor,n.exam_result,n.ref_value,");
				jykstr.append(
						"n.ref_indicator,n.center_num,n.approver,n.approve_date,n.creater,CONVERT(varchar(50),n.exam_date,20) as exam_date,");
				jykstr.append(
						"n.updater,n.update_time,n.item_unit,n.ref_value,a.dep_id,dep.dep_name,x.item_num,x.item_name");
				jykstr.append(
						",x.seq_code,x.item_category,a.item_name as charging_item_name,b.charging_item_id,a.item_code as charging_item_code,dep.dep_num,a.dep_id,a.item_seq");
				jykstr.append(" from exam_result_detail n,");
				jykstr.append(
						"charging_item a,examinfo_charging_item g,charging_item_exam_item b,examination_item x,department_dep dep");
				jykstr.append(
						" where b.charging_item_id=a.id and a.isActive='Y' and g.isActive='Y' and x.is_Active='Y' ");
				jykstr.append(
						" and x.is_Active='Y' and dep.isActive='Y' and a.id=g.charge_item_id and x.id=b.exam_item_id ");
				jykstr.append(
						" and dep.id=a.dep_id and g.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
				jykstr.append(" and n.exam_info_id='" + es.getExam_info_id() + "' and x.is_print='Y' order by a.dep_id,a.item_seq");
				TranLogTxt.liswriteEror_to_txt(logName, "res: :lis检查： " + jykstr.toString());
				jyksList = this.jdbcQueryManager.getList(jykstr.toString(), ExamResultDetailDTO.class);
				 dept_id=0;
				 editor="";
				 ExamineDate="";
				 String aeditor="";
				 String aExamineDate="";
				 zslrnew = "";
				 dept_num="";
				 dept_name="";
				 List<DeptBriefBean> commlist= new ArrayList<DeptBriefBean>();
				for (ExamResultDetailDTO dd : jyksList) {						
					if ("0".equals(dd.getRef_indicator().trim())) {
						boolean exsitflag = geterdFordeptNum(es.getExam_info_id(),dd.getDep_num(),logName);
						if(!exsitflag){
							commlist=setDeptBriefList(dd,commlist);
						}
					}else{
						//1高 2 低 3 阳性
						String gd="";
						if("1".equals(dd.getRef_indicator().trim())){
							gd="偏高";
						}else if("2".equals(dd.getRef_indicator().trim())){
							gd="偏低";
						}else if("3".equals(dd.getRef_indicator().trim())){
							gd="阳性";
						}
						String jieguo=dd.getItem_name()+": "+gd+" "+dd.getExam_result()+" "+dd.getItem_unit();
						
						long new_dept_id = dd.getDep_id();
						if (dept_id != new_dept_id) {
							if (zslrnew.trim().length() > 0) {
								DeptBriefBean om = new DeptBriefBean();
								om.setDeptId(dept_num);
								om.setBrief(dept_name+"检查结果:"+zslrnew);
								om.setIfAbnormity("1");
								om.setEditor(editor);
								om.setEditDate(ExamineDate);
								om.setExamineDoctor(aeditor);
								om.setExamineDate(aExamineDate);
								briefInfo.add(om);
								dept_id = 0;
								zslrnew = "";
								ExamineDate="";
								editor="";
								aExamineDate="";
								aeditor="";		
								dept_num="";
								dept_name="";
							}
								dept_id = new_dept_id;
								dept_num=dd.getDep_num();
								dept_name=dd.getDep_name();
								zslrnew = zslrnew + "\r\n" + jieguo;
								zslr = zslr + "\r\n" + jieguo;
								ExamineDate=dd.getCreate_time();
								editor=dd.getExam_doctor();
								aExamineDate=dd.getApprove_date();
								aeditor=dd.getExam_doctor();
						} else {
							dept_id = new_dept_id;
							dept_num=dd.getDep_num();
							dept_name=dd.getDep_name();
							zslrnew = zslrnew + "\r\n" + jieguo;
							zslr = zslr + "\r\n" + jieguo;
							ExamineDate=dd.getCreate_time();
							editor=dd.getExam_doctor();
							aExamineDate=dd.getApprove_date();
							aeditor=dd.getExam_doctor();
						}
					}
				}
				if (zslrnew.trim().length() > 0) {
					DeptBriefBean om = new DeptBriefBean();
					om.setDeptId(dept_num);
					om.setBrief(dept_name+"检查结果:"+zslrnew);
					om.setIfAbnormity("1");
					om.setEditor(editor);
					om.setEditDate(ExamineDate);
					om.setExamineDoctor(aeditor);
					om.setExamineDate(aExamineDate);
					briefInfo.add(om);
					dept_id = 0;
					zslrnew = "";
					dept_name="";
					ExamineDate="";
					editor="";
					aExamineDate="";
					aeditor="";							
				}
				
				for(DeptBriefBean om:commlist){
					briefInfo.add(om);
				}

				// 影像科室检查结果明细表
				StringBuffer jckstr = new StringBuffer();
				/*jckstr.append("select distinct n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.report_picture_path,"
						+ "n.exam_desc,n.exam_result,n.dept_num as dep_num,CONVERT(varchar(50),n.exam_date,20) as exam_date,"
						+ "n.center_num,n.approver,n.approve_date,n.creater,n.create_time,n.updater,n.update_time,n.pacs_id,dep.dep_name"
						+ " from view_exam_detail n "
						+ " left join department_dep dep on dep.dep_num=n.dept_num ,pacs_detail g,exam_info ex where n.exam_info_id='"
						+ es.getExam_info_id() + "' "
						+ " and n.exam_info_id=ex.id and g.dep_num=n.dept_num and g.examinfo_num=ex.exam_num order by n.dept_num");*/
				jckstr.append("select distinct n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.report_picture_path,n.exam_desc,n.exam_result,n.dept_num as dep_num,CONVERT(varchar(50),n.exam_date,20) as exam_date,");
				jckstr.append("n.center_num,n.approver,n.approve_date,n.creater,n.create_time,n.updater,n.update_time,");
				jckstr.append("n.pacs_id,dep.dep_name ");
				jckstr.append(" from view_exam_detail n " );
				jckstr.append(" left join department_dep dep on dep.dep_num=n.dept_num ");
				jckstr.append(",pacs_detail g,");
				jckstr.append(" exam_info ex where n.exam_info_id='"+es.getExam_info_id()+"' "); 
				jckstr.append(" and n.exam_info_id=ex.id "); 
				jckstr.append(" and g.dep_num=n.dept_num and n.dept_num is not null"); 
				jckstr.append(" and g.examinfo_num=ex.exam_num");  
				jckstr.append(" union all "); 
				jckstr.append(" select distinct n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.report_picture_path,n.exam_desc,n.exam_result,dd.dep_num as dep_num,CONVERT(varchar(50),n.exam_date,20) as exam_date,"); 
				jckstr.append(" n.center_num,n.approver,n.approve_date,n.creater,n.create_time,n.updater,n.update_time,"); 
				jckstr.append(" n.pacs_id,dd.dep_name "); 
				jckstr.append(" from view_exam_detail n,pacs_summary ps,pacs_detail pd,charging_item ci,department_dep dd"); 
				jckstr.append(" where n.exam_info_id='"+es.getExam_info_id()+"' and ps.id=pd.summary_id and pd.chargingItem_num=ci.item_code and ci.dep_id =dd.id  and n.pacs_id=ps.id"); 
				jckstr.append(" and n.dept_num is null"); 
				jckstr.append(" order by n.dept_num ");
				TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " + jckstr.toString());
				List<ViewExamDetailDTO> jcksList = this.jdbcQueryManager.getList(jckstr.toString(),ViewExamDetailDTO.class);
				dept_num="";
				editor="";
				ExamineDate="";
				zslrnew = "";
				dept_name="";
				for (ViewExamDetailDTO dd : jcksList) {
					String new_dept_num = dd.getDep_num();
					if (!dept_num.equals(new_dept_num)) {
						if (zslrnew.trim().length() > 0) {
							DeptBriefBean om = new DeptBriefBean();
							om.setDeptId(dept_num);
							om.setBrief(dept_name+"检查结果:"+zslrnew);
							om.setIfAbnormity("0");
							om.setEditor("0000");
							om.setEditDate(DateTimeUtil.getDateTime());
							om.setExamineDoctor("0000");
							om.setExamineDate(DateTimeUtil.getDateTime());

							briefInfo.add(om);
							dept_num = "";
							zslrnew = "";
							ExamineDate = "";
							editor = "";
							dept_name="";
						}
						dept_num = new_dept_num;
						dept_name=dd.getDep_name();
						zslrnew = zslrnew + "\r\n" + dd.getExam_result();
						zslr = zslr + "\r\n" + dd.getExam_result();
						editor = dd.getExam_doctor();
						ExamineDate = dd.getCreate_time();

					} else {
						dept_num = new_dept_num;
						dept_name=dd.getDep_name();
						zslrnew = zslrnew + "\r\n" + dd.getExam_result();
						zslr = zslr + "\r\n" + dd.getExam_result();
						editor = dd.getExam_doctor();
						ExamineDate = dd.getCreate_time();
					}
				}
					if (zslrnew.trim().length() > 0) {
						DeptBriefBean om = new DeptBriefBean();
						om.setDeptId(dept_num);
						om.setBrief(dept_name+"检查结果:"+zslrnew);						
						om.setIfAbnormity("0");
						om.setEditor("0000");
						om.setEditDate(DateTimeUtil.getDateTime());
						om.setExamineDoctor("0000");
						om.setExamineDate(DateTimeUtil.getDateTime());														
						briefInfo.add(om);
						dept_num = "";
						zslrnew="";
						ExamineDate="";
						editor="";
						dept_name="";
				}
				db.setBriefInfo(briefInfo);

				WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
				wcd = webserviceConfigurationService.getWebServiceConfig("REPORT_SEND_KSJL");
				web_url = wcd.getConfig_url().trim();
				TranLogTxt.liswriteEror_to_txt(logName, "res: :请求地址： " + web_url);
				ResultHeader rb2 = new ResultHeader();
				rb2 = this.putMessage(web_url, db, logName);

				// 总检
				UpdateDiagnosisBody udb = new UpdateDiagnosisBody();
				udb.setReserveId(es.getExam_num());
				List<UpdateDiagnosisBean> diagnosisInfo = new ArrayList<UpdateDiagnosisBean>();
				String zjjlStr = "select a.exam_info_id,c.disease_num,c.disease_name,a.disease_name as disease_key,CONVERT(varchar(50),a.create_time,20) as create_time,"
						+ "a.update_time,a.final_doc_num,a.suggest,b.work_num,c.disease_num from examinfo_disease a "
						+ " left join user_usr b on b.id=a.creater "
						+ " left join disease_knowloedge_lib c on c.id=a.disease_id where exam_info_id='"
						+ es.getExam_info_id() + "' and a.isActive='Y' order by a.disease_index";
				TranLogTxt.liswriteEror_to_txt(logName, "res: :总检语句： " + zjjlStr);
				List<ExaminfoDiseaseDTO> zjjlList = this.jdbcQueryManager.getList(zjjlStr, ExaminfoDiseaseDTO.class);
				String jylr="";
				String zzzd="";
				if ((zjjlList != null) && (zjjlList.size() > 0)) {
					for (ExaminfoDiseaseDTO dd : zjjlList) {
						UpdateDiagnosisBean amsg = new UpdateDiagnosisBean();
						if((dd.getDisease_num()!=null)&&(dd.getDisease_num().trim().length()>0)){
						amsg.setDiagnosisName(dd.getDisease_name());
						
						amsg.setDiagnosisId(dd.getDisease_num());
						amsg.setDiagnosisKey(dd.getDisease_name());
						TranLogTxt.liswriteEror_to_txt(logName, "+++++++++++++++++++++++ " + jylr);
						if(jylr.trim().length()<=0){
							jylr=dd.getDisease_name();
						}else{
							jylr=jylr+"\r\n"+dd.getDisease_name();
						}
						
						if ((dd.getSuggest() != null) && (dd.getSuggest().length() >= 0)) {
							String res = dd.getSuggest();
							res = res.replaceAll("\n", "\r\n");
							amsg.setAdviceDesc(res);
							if(zzzd.trim().length()<=0){
								zzzd=res;
							}else{
								zzzd=zzzd+"\r\n"+res;
							}
							
							if(jylr.trim().length()<=0){
								jylr=res+"\r\n";
							}else{
								jylr=jylr+"\r\n"+res+"\r\n";
							}
							
						}
						//amsg.setDeptId(dd.getDep_num());
						amsg.setDeptId("NEW");
						amsg.setEditor(dd.getWork_num());
						amsg.setEditDate(dd.getCreate_time());
						diagnosisInfo.add(amsg);
						}
					}
				}
				udb.setDiagnosisInfo(diagnosisInfo);

				wcd = webserviceConfigurationService.getWebServiceConfig("REPORT_SEND_ZJ");
				web_url = wcd.getConfig_url().trim();
				TranLogTxt.liswriteEror_to_txt(logName, "res: :请求地址： " + web_url);
				ResultHeader rb3 = new ResultHeader();
				rb3 = this.putMessage(web_url, udb, logName);

				// 总检结论
				SummerBody sb = new SummerBody();
				sb.setReserveId(es.getExam_num());
				zjjlStr = "select a.final_exam_result,b.work_num as exam_doctor,c.work_num as check_doc,"
						+ "CONVERT(varchar(50),a.create_time,20) as create_time,"
						+ "CONVERT(varchar(50),a.check_time,20) as check_time,a.suggest from exam_summary a "
						+ "left join user_usr b on b.id=a.exam_doctor_id " + "left join user_usr c on c.id=a.check_doc "
						+ "where a.exam_info_id='" + es.getExam_info_id() + "'";
				TranLogTxt.liswriteEror_to_txt(logName, "res: :总检结论： " + zjjlStr);
				List<SummerDTO> jlList = this.jdbcQueryManager.getList(zjjlStr, SummerDTO.class);
				if ((jlList != null) && (jlList.size() > 0)) {
					SummerDTO dd = (SummerDTO) jlList.get(0);
					sb.setExamineDoctor(dd.getExam_doctor());
					sb.setExamineDate(dd.getCreate_time());
					if((dd.getFinal_exam_result()!=null)&&(dd.getFinal_exam_result().trim().length()>0)){
						dd.setFinal_exam_result(dd.getFinal_exam_result().replaceAll("\n", "\r\n"));
					}
					sb.setDiagnosisDesc(dd.getFinal_exam_result());	
					sb.setAdviceDesc(jylr);
					sb.setLastDiagnosis(zzzd);// lastDiagnosis 最终诊断
					sb.setEditor(dd.getCheck_doc());
					sb.setEditDate(dd.getCheck_time());
					sb.setJoinDate(es.getJoin_date());
				}
				
				wcd = webserviceConfigurationService.getWebServiceConfig("REPORT_SEND_JL");
				web_url = wcd.getConfig_url().trim();
				TranLogTxt.liswriteEror_to_txt(logName, "res: :请求地址： " + web_url);
				ResultHeader rb4 = new ResultHeader();
				rb4 = this.putMessage(web_url, sb, logName);

				if (("AA".equals(rb1.getTypeCode())) && ("AA".equals(rb2.getTypeCode()))
						&& ("AA".equals(rb3.getTypeCode())) && ("AA".equals(rb4.getTypeCode()))) {
					String sql = "update exam_summary set read_status='1',read_time='" + DateTimeUtil.getDateTime()
							+ "' where id ='" + es.getId() + "' ";
					this.jdbcPersistenceManager.executeSql(sql);
					rh.setTypeCode("AA");
					rh.setText("");
				}else{
					rh.setTypeCode("AE");
					rh.setText("报告上传不完整");
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
				rh.setTypeCode("AE");
				rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		
		return rh;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(long id,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,"
				+ "c.register_date,c.join_date,c.exam_times,a.arch_num ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.id = '" + id + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	private List<DeptBriefBean> setDeptBriefList(ExamResultDetailDTO dd,List<DeptBriefBean> oldlist)throws ServiceException{
		boolean falgs=false;
		for(DeptBriefBean db: oldlist){
			if(dd.getDep_num().equals(db.getDeptId())){
				falgs=true;
				break;
			}
		}
		if(!falgs){
			DeptBriefBean om = new DeptBriefBean();
			om.setDeptId(dd.getDep_num());
			om.setBrief("未见异常");
			om.setIfAbnormity("0");
			om.setEditor("0000");
			om.setEditDate(DateTimeUtil.getDateTime());
			om.setExamineDoctor("0000");
			om.setExamineDate(DateTimeUtil.getDateTime());
			oldlist.add(om);
		}
		return oldlist;
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public boolean geterdFordeptNum(long id,String dept_num,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		boolean flags=false;
		sb.append("select DISTINCT dep.dep_num ");
		sb.append(" from exam_result_detail n,");
		sb.append("charging_item a,examinfo_charging_item g,charging_item_exam_item b,examination_item x,department_dep dep");
		sb.append(" where b.charging_item_id=a.id and a.isActive='Y' and g.isActive='Y' and x.is_Active='Y' ");
		sb.append(" and x.is_Active='Y' and dep.isActive='Y' and a.id=g.charge_item_id and x.id=b.exam_item_id ");
		sb.append(" and dep.id=a.dep_id and g.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
		sb.append(" and n.exam_info_id='"+id+"' and dep.dep_num='"+dept_num+"' ");
		sb.append(" and n.ref_indicator!=0 " );
		sb.append("  and x.is_print='Y' order by dep.dep_num ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamResultDetailDTO.class);
		if((map!=null)&&(map.getList().size()>0)){
			flags=true;			
		}
		return flags;
	} 
	
	

	/**
	 * 
	 * @param url
	 * @param ob
	 * @param logname
	 * @return
	 */
	private ResultHeader putMessage(String url, Object ob, String logname) {
		ResultHeader rb = new ResultHeader();
		JSONObject json = JSONObject.fromObject(ob);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		try {
			String result = HttpUtil.doPost(url, ob, "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				JSONObject jsonobject = JSONObject.fromObject(result);
				PutBean resdah = new PutBean();
				resdah = (PutBean) JSONObject.toBean(jsonobject, PutBean.class);
				if ("200".equals(resdah.getStatus())) {
					rb.setTypeCode("AA");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setTypeCode("AE");
			rb.setText("pacs调用webservice错误");
		}
		return rb;
	}
}
