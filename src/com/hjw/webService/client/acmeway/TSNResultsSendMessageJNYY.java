package com.hjw.webService.client.acmeway;

import java.sql.SQLException;

import javax.sql.RowSet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.acmeway.bean.PatientReq;
import com.hjw.webService.client.acmeway.bean.Result;
import com.hjw.webService.client.acmeway.bean.ResultsReq;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

public class TSNResultsSendMessageJNYY {
	
//	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public ResultPacsBody getMessage(String exam_num, String url,String logname,boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				ExamInfoUserDTO eu=configService.getExamInfoForNum(exam_num);
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					String sexCodeMF = "M";
					if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
						sexCodeMF = "M";
					} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
						sexCodeMF = "F";
					}
					if(!StringUtil.isEmpty(eu.getBirthday()) && eu.getBirthday().length()>10) {
						eu.setBirthday(eu.getBirthday().substring(0, 10));
					}
					
					PatientReq patientReq = new PatientReq();
					patientReq.setId(eu.getArch_num());
					patientReq.setTestOrder(eu.getExam_num());
					patientReq.setCardNo(eu.getExam_num());
					patientReq.setName(eu.getUser_name());
					patientReq.setSex(sexCodeMF);
					patientReq.setBirthday(eu.getBirthday());
					patientReq.setIdCardNo(eu.getId_num());
//					patientReq.setNationCode("");
//					patientReq.setEducation("");
//					patientReq.setProfession("");
//					patientReq.setTrdCode("");
//					patientReq.setCpyName("");
//					patientReq.setDeptName("");
//					patientReq.setIsMarried("");
//					patientReq.setAreaCode("");
//					patientReq.setNativePlaceCode("");
					patientReq.setMobile(eu.getPhone());
//					patientReq.setRegisterList();
					
					
					String req = new Gson().toJson(patientReq, PatientReq.class);
					TranLogTxt.liswriteEror_to_txt(logname, "req:"+req);
					String res = HttpUtil.doPost_Str(url.split("\\^")[0], req, "utf-8");
					Result result = new Gson().fromJson(res, Result.class);
					
					if(result.getStatus() == 0) {
						ResultsReq resultsReq = getResultReq(logname, eu);
						
						req = new Gson().toJson(resultsReq, ResultsReq.class);
						TranLogTxt.liswriteEror_to_txt(logname, "req:"+req);
						res = HttpUtil.doPost_Str(url.split("\\^")[1], req, "utf-8");
						result = new Gson().fromJson(res, Result.class);
						if(result.getStatus() == 0) {
							ApplyNOBean an = new ApplyNOBean();
							an.setApplyNO(eu.getExam_num());	
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("pacs调用成功");
						} else {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("运动管理综合评估："+result.getMessage());
							TranLogTxt.liswriteEror_to_txt(logname,"运动管理综合评估："+result.getMessage());
						}
					} else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("登记受试者信息返回："+result.getMessage());
						TranLogTxt.liswriteEror_to_txt(logname,"登记受试者信息返回："+result.getMessage());
					}
				}
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + xml);
		return rb;
	}
	
	private ResultsReq getResultReq(String logname, ExamInfoUserDTO eu) {
		int sexCode12 = 1;
		if("男".equals(eu.getSex()) || "男性".equals(eu.getSex())) {
			sexCode12 = 1;
		} else if("女".equals(eu.getSex()) || "女性".equals(eu.getSex())) {
			sexCode12 = 2;
		}
		String exam_num = eu.getExam_num();
		
		ResultsReq resultsReq = new ResultsReq();
		resultsReq.setMember_id(eu.getArch_num());//档案号或体检号或卡号
//						resultsReq.setrecordno();//会员号（奥美方的会员号）
		resultsReq.setTstOrderNo(eu.getExam_num());//本次测试流水号
		resultsReq.setMember_name(eu.getUser_name());//姓名
		resultsReq.setGender(sexCode12);//性别 1男 2女
		resultsReq.setAge(eu.getAge());//年龄
//						resultsReq.settest_count();//测评次数
//						resultsReq.settest_time();//本次测试完成时间（YYYY-MM-DD hh:mm:ss）
		resultsReq.setCmp(eu.getCompany());//工作单位
//						resultsReq.setDept();//部门
		
		resultsReq.setHeart_rate(getCommonResult(exam_num, "WL006",logname));//脉搏
		
		resultsReq.setM_sbp(getCommonResult(exam_num, "WLOO4",logname));//收缩压
		resultsReq.setM_dbp(getCommonResult(exam_num, "WLOO5",logname));//舒张压
//						resultsReq.setM_sbp_pg();//收缩压评价
//						resultsReq.setM_dbp_pg();//舒张压评价
		resultsReq.setM_tc(getExamResult(exam_num, "WL0001386",logname));//总胆固醇(Tch)
		resultsReq.setM_tg(getExamResult(exam_num, "WL0001719",logname));//甘油三酯（TG）
//						resultsReq.setM_tc_pg();//总胆固醇(Tch)评价
//						resultsReq.setM_tg_pg();//甘油三酯（TG）评价
		resultsReq.setM_hdl(getExamResult(exam_num, "WL0001720",logname));//高密度脂蛋白胆固醇(HDL-C)
		resultsReq.setM_ldl(getExamResult(exam_num, "WL0001721",logname));//低密度脂蛋白胆固醇(LDL-C)
//						resultsReq.setM_hdl_pg();//高密度脂蛋白胆固醇(HDL-C)评价
//						resultsReq.setM_ldl_pg();//低密度脂蛋白胆固醇(LDL-C)评价
		resultsReq.setM_f(getExamResult(exam_num, "C0001138",logname));//空腹血糖（GLU）
		resultsReq.setM_2hf(getExamResult(exam_num, "C0000970",logname));//餐后2h血糖
//						resultsReq.setM_fpg();//空腹血糖（GLU）评价
//						resultsReq.setM_2hfpg();//餐后2h血糖评价
		resultsReq.setM_ua(getExamResult(exam_num, "C0001149",logname));//血尿酸测定(UA)
		resultsReq.setM_ecg(getViewResult(exam_num, "A000325", logname));//心电图检查
//						resultsReq.setM_cca();//颈总动脉
//						resultsReq.setMwj_fgh_xgyc();//家族遗传史-心血管疾病遗传史    
//						resultsReq.setMwj_fgh_bea();//家族遗传史-哮喘
//						resultsReq.setMwj_fgh_copd();//家族遗传史-慢阻肺(慢性支气管炎,肺气肿)
//						resultsReq.setMwj_fgh_stroke();//家族遗传史-脑卒中(脑中风)
//						resultsReq.setMwj_mh_tf();//个人疾病史-痛风
//						resultsReq.setMwj_mh_wao();//个人疾病史-骨质疏松
//						resultsReq.setMwj_mh_bea();//个人疾病史-哮喘
//						resultsReq.setMwj_mh_copd();//个人疾病史-慢阻肺
//						resultsReq.setMwj_mh_stroke();//个人疾病史-脑卒中
//						resultsReq.setMwj_mh_chd_igr();//个人疾病史-冠心病或心肌梗死
//						resultsReq.setMwj_mh_cld();//个人疾病史-慢性肝病
//						resultsReq.setMwj_mh_bnp();//个人疾病史-慢性肾病
//						resultsReq.setMwj_medication_jyy();//用药史-降压药
//						resultsReq.setMwj_medication_lld();//用药史-降脂药
//						resultsReq.setMwj_medication_jty();//用药史-降糖药
//						resultsReq.setMwj_medication_jnsy();//用药史-降尿酸药
//						resultsReq.setMwj_medication_kxlscy();//用药史-抗心律失常药
//						resultsReq.setMwj_medication_hjxcyw();//用药史-缓解哮喘药物
//						resultsReq.setMwj_smoke();//吸烟
//						resultsReq.setMwj_sleep();//睡眠质量
//						resultsReq.setMwj_drink();//饮酒情况
//						resultsReq.setInjury_phusical();//体力活动水平
//						resultsReq.setInjury_wxfc();//危险分层
//						resultsReq.setInjury_hypertension_dose();//高血压服药
//						resultsReq.setInjury_lipid_lowering_dose();//服用降脂药
//						resultsReq.setInjury_diabetes_dose();//糖尿病I型/糖尿病II型服药
//						resultsReq.setInjury_shoulder ();//肩关节
//						resultsReq.setInjury_elbow ();//肘关节
//						resultsReq.setInjury_wrist();//腕部
//						resultsReq.setInjury_back();//背部
//						resultsReq.setInjury_spine();//脊柱
//						resultsReq.setInjury_waist();//腰部
//						resultsReq.setInjury_thigh();//大腿
//						resultsReq.setInjury_knee();//膝盖
//						resultsReq.setInjury_ankle();//脚踝
//						resultsReq.setInjury_crus();//小腿
//						resultsReq.setInjury_achilles_tendon();//跟腱
//						resultsReq.setInjury_option();//骨关节或软组织疾病
		resultsReq.setPf_height(getCommonResult(exam_num, "WL001",logname));//身高
		resultsReq.setPf_weight(getCommonResult(exam_num, "WL002",logname));//体重
//						resultsReq.setPf_total_score();//综合得分
//						resultsReq.setPf_total_pg();//综合评价
//						resultsReq.setPf_tsn_zj();//体适能主要问题
//						resultsReq.setPf_sport_cve();//运动中心血管事件的发生风险
//						resultsReq.setPf_sport_sr();//运动损伤发生风险
//						resultsReq.setPf_sport_mt();//运动的禁忌项目
//						resultsReq.setPf_sport_ma();//运动其他注意事项
//						resultsReq.setPf_vo2_max_xd();//心肺耐力-最大摄氧量
//						resultsReq.setPf_vo2_max_xd_pg();//心肺耐力-最大摄氧量评价
//						resultsReq.setPf_vo2_max_xd_zj();//心肺耐力-最大摄氧量总结
//						resultsReq.setPf_fat();//身体成分
//						resultsReq.setPf_fat_pg();//身体成分
//						resultsReq.setPf_fat_zj();//身体成分
//						resultsReq.setPf_grip_strength();//握力 
//						resultsReq.setPf_grip_strength_pg();//握力 
//						resultsReq.setPf_grip_strength_zj();//握力 
//						resultsReq.setPf_sitandreach();//坐位体前屈
//						resultsReq.setPf_sitandreach_pg();//坐位体前屈
//						resultsReq.setPf_sitandreach_zj();//坐位体前屈
//						resultsReq.setPf_close_eye_stand_one_leg();//闭眼单脚站
//						resultsReq.setPf_close_eye_stand_one_leg_pg();//闭眼单脚站
//						resultsReq.setPf_close_eye_stand_one_leg_zj();//闭眼单脚站
//						resultsReq.setPf_choice_rt();//选择反应
//						resultsReq.setPf_choice_rt_pg();//选择反应
//						resultsReq.setPf_choice_rt_zj();//选择反应
//						resultsReq.setPf_kip();//重点改善项目
//						resultsReq.setPf_foitp();//重点改善目的
//						resultsReq.setPf_ns();//营养建议
//						resultsReq.setPf_la();//生活方式建议
//						resultsReq.setPf_dpaa();//日常体力活动建议
//						resultsReq.setPf_rs();//复测建议
//						resultsReq.setPf_yy_one_des();//有氧运动第1阶段描述
//						resultsReq.setPf_yy_one_timerange();//有氧运动第1阶段运动时间范围
//						resultsReq.setPf_yy_two_des();//有氧运动第2阶段描述
//						resultsReq.setPf_yy_two_timerange();//有氧运动第2阶段运动时间范围
//						resultsReq.setPf_yy_three_des();//有氧运动第3阶段描述
//						resultsReq.setPf_yy_three_timerange();//有氧运动第3阶段运动时间范围
//						resultsReq.setPf_yy_four_des();//有氧运动第4阶段描述
//						resultsReq.setPf_yy_four_timerange();//有氧运动第4阶段运动时间范围
//						resultsReq.setPf_yy_fifth_des();//有氧运动第5阶段描述
//						resultsReq.setPf_yy_fifth_timerange();//有氧运动第5阶段运动时间范围
//						resultsReq.setPf_yy_six_des();//有氧运动第6阶段描述
//						resultsReq.setPf_yy_six_timerange();//有氧运动第6阶段运动时间范围
//						resultsReq.setPf_kz_one_des();//抗阻运动第1阶段描述
//						resultsReq.setPf_kz_one_num ();//抗阻运动第1阶段运动次数
//						resultsReq.setPf_kz_two_des();//抗阻运动第1阶段描述
//						resultsReq.setPf_kz_two_num ();//抗阻运动第1阶段运动次数
//						resultsReq.setPf_kz_three_des();//抗阻运动第1阶段描述
//						resultsReq.setPf_kz_three_num ();//抗阻运动第1阶段运动次数
//						resultsReq.setPf_kz_four_des();//抗阻运动第1阶段描述
//						resultsReq.setPf_kz_four_num ();//抗阻运动第1阶段运动次数
//						resultsReq.setPf_kz_fifth_des();//抗阻运动第1阶段描述
//						resultsReq.setPf_kz_fifth_num ();//抗阻运动第1阶段运动次数
//						resultsReq.setPf_kz_six_des();//抗阻运动第1阶段描述
//						resultsReq.setPf_kz_six_num ();//抗阻运动第1阶段运动次数
//						resultsReq.setPf_fc();//心脏功能能力
//						resultsReq.setPf_fc_pg();//心脏功能能力评价
//						resultsReq.setPf_hr1();//HR1
//						resultsReq.setPf_hr2();//HR2
//						resultsReq.setPf_met1();//MET1
//						resultsReq.setPf_met2();//MET2
//						resultsReq.setPf_cpf_hr1();//第1级心率
//						resultsReq.setPf_cpf_hr2();//第2级心率
//						resultsReq.setPf_cpf_hr3();//第3级心率
//						resultsReq.setPf_cpf_hr4();//第4级心率
//						resultsReq.setPf_cpf_hr5();//第5级心率
//						resultsReq.setPf_cpf_hr6();//第6级心率
//						resultsReq.setPf_cpf_power1();//第1级功率
//						resultsReq.setPf_cpf_power2();//第2级功率
//						resultsReq.setPf_cpf_power3();//第3级功率
//						resultsReq.setPf_cpf_power4();//第4级功率
//						resultsReq.setPf_cpf_power5();//第5级功率
//						resultsReq.setPf_cpf_power6();//第6级功率
//						resultsReq.setPf_cpf_level();//心肺测试等级
//						resultsReq.setPf_cpf_project();//心肺测试方案
//						resultsReq.setPf_hr0();//安静心率
//						resultsReq.setPf_VitalCapacity();//肺活量
//						resultsReq.setPf_VitalCapacity_df();//肺活量得分
//						resultsReq.setPf_VitalCapacity_pg();//肺活量评价
//						resultsReq.setPf_StepIndex();//台阶指数
//						resultsReq.setPf_StepIndex_df();//台阶指数得分
//						resultsReq.setPf_StepIndex_pg();//台阶指数评价
//						resultsReq.setPf_StandingVerticalJump();//纵跳
//						resultsReq.setPf_StandingVerticalJump_df();//纵跳得分
//						resultsReq.setPf_StandingVerticalJump_pg();//纵跳评价
//						resultsReq.setPf_Push_Up();//俯卧撑
//						resultsReq.setPf_Push_Up_df();//俯卧撑得分
//						resultsReq.setPf_Push_Up_pg();//俯卧撑评价
//						resultsReq.setPf_Curl_Up();//仰卧起坐
//						resultsReq.setPf_Curl_Up_df();//仰卧起坐得分
//						resultsReq.setPf_Curl_Up_pg();//仰卧起坐评价
//						resultsReq.setFDataSource();//数据来源
		return resultsReq;
	}
	
	private String getCommonResult(String exam_num, String item_num, String logname) {
		String exam_result = "";
		String sql = "select ced.exam_result from common_exam_detail ced,exam_info e,examination_item ei "
				+ " where ced.exam_info_id = e.id and ced.exam_item_id = ei.id "
				+ " and e.is_Active = 'Y' and ei.is_Active = 'Y' "
				+ " and e.exam_num = '"+exam_num+"' and ei.item_num = '"+item_num+"'";
		TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
		RowSet rs = jdbcQueryManager.getRowSet(sql);
		try {
			exam_result = rs.getString("exam_result");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exam_result;
	}
	
	private String getExamResult(String exam_num, String item_num, String logname) {
		String exam_result = "";
		String sql = "select erd.* from exam_result_detail erd,exam_info e,examination_item ei "
				+ " where erd.exam_info_id = e.id and erd.exam_item_id = ei.id "
				+ " and e.exam_num = '"+exam_num+"' and ei.item_num = '"+item_num+"'";
		TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
		RowSet rs = jdbcQueryManager.getRowSet(sql);
		try {
			exam_result = rs.getString("exam_result");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exam_result;
	}
	
	private String getViewResult(String exam_num, String item_num, String logname) {
		String exam_result = "";
		String sql = "select ved.* from view_exam_detail ved,exam_info e,pacs_detail pd "
				+ " where ved.exam_info_id = e.id and ved.pacs_id = pd.summary_id "
				+ " and e.exam_num = '"+exam_num+"' "
				+ " and pd.chargingItem_num = '"+item_num+"'";
		TranLogTxt.liswriteEror_to_txt(logname, "sql:"+sql);
		RowSet rs = jdbcQueryManager.getRowSet(sql);
		try {
			exam_result = rs.getString("exam_result");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exam_result;
	}
}
