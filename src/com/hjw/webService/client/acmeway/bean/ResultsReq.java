package com.hjw.webService.client.acmeway.bean;

public class ResultsReq {

	private String member_id = "";//档案号或体检号或卡号
	private String recordno = "";//会员号（奥美方的会员号）
	private String tstOrderNo = "";//本次测试流水号
	private String member_name = "";//姓名
	private int gender = 1;//性别 1男 2女
	private long age = 0;//年龄
	private String test_count = "";//测评次数
	private String test_time = "";//本次测试完成时间（YYYY-MM-DD hh:mm:ss）
	private String cmp = "";//工作单位
	private String dept = "";//部门
	private String Heart_rate = "";//脉搏
	private String M_sbp = "";//收缩压
	private String M_dbp = "";//舒张压
	private String M_sbp_pg = "";//收缩压评价
	private String M_dbp_pg = "";//舒张压评价
	private String M_tc = "";//总胆固醇(Tch)
	private String M_tg = "";//甘油三酯（TG）
	private String M_tc_pg = "";//总胆固醇(Tch)评价
	private String M_tg_pg = "";//甘油三酯（TG）评价
	private String M_hdl = "";//高密度脂蛋白胆固醇(HDL-C)
	private String M_ldl = "";//低密度脂蛋白胆固醇(LDL-C)
	private String M_hdl_pg = "";//高密度脂蛋白胆固醇(HDL-C)评价
	private String M_ldl_pg = "";//低密度脂蛋白胆固醇(LDL-C)评价
	private String M_f = "";//空腹血糖（GLU）
	private String M_2hf = "";//餐后2h血糖
	private String M_fpg = "";//空腹血糖（GLU）评价
	private String M_2hfpg = "";//餐后2h血糖评价
	private String M_ua = "";//血尿酸测定(UA)
	private String M_ecg = "";//心电图检查
	private String M_cca = "";//颈总动脉
	private String Mwj_fgh_xgyc = "";//家族遗传史-心血管疾病遗传史    
	private String Mwj_fgh_bea = "";//家族遗传史-哮喘
	private String Mwj_fgh_copd = "";//家族遗传史-慢阻肺(慢性支气管炎,肺气肿)
	private String Mwj_fgh_stroke = "";//家族遗传史-脑卒中(脑中风)
	private String Mwj_mh_tf = "";//个人疾病史-痛风
	private String Mwj_mh_wao = "";//个人疾病史-骨质疏松
	private String Mwj_mh_bea = "";//个人疾病史-哮喘
	private String Mwj_mh_copd = "";//个人疾病史-慢阻肺
	private String Mwj_mh_stroke = "";//个人疾病史-脑卒中
	private String Mwj_mh_chd_igr = "";//个人疾病史-冠心病或心肌梗死
	private String Mwj_mh_cld = "";//个人疾病史-慢性肝病
	private String Mwj_mh_bnp = "";//个人疾病史-慢性肾病
	private String Mwj_medication_jyy = "";//用药史-降压药
	private String Mwj_medication_lld = "";//用药史-降脂药
	private String Mwj_medication_jty = "";//用药史-降糖药
	private String Mwj_medication_jnsy = "";//用药史-降尿酸药
	private String Mwj_medication_kxlscy = "";//用药史-抗心律失常药
	private String Mwj_medication_hjxcyw = "";//用药史-缓解哮喘药物
	private String Mwj_smoke = "";//吸烟
	private String Mwj_sleep = "";//睡眠质量
	private String Mwj_drink = "";//饮酒情况
	private String Injury_phusical = "";//体力活动水平
	private String Injury_wxfc = "";//危险分层
	private String Injury_hypertension_dose = "";//高血压服药
	private String Injury_lipid_lowering_dose = "";//服用降脂药
	private String Injury_diabetes_dose = "";//糖尿病I型/糖尿病II型服药
	private String Injury_shoulder  = "";//肩关节
	private String Injury_elbow  = "";//肘关节
	private String Injury_wrist = "";//腕部
	private String Injury_back = "";//背部
	private String Injury_spine = "";//脊柱
	private String Injury_waist = "";//腰部
	private String Injury_thigh = "";//大腿
	private String Injury_knee = "";//膝盖
	private String Injury_ankle = "";//脚踝
	private String Injury_crus = "";//小腿
	private String Injury_achilles_tendon = "";//跟腱
	private String Injury_option = "";//骨关节或软组织疾病
	private String Pf_height = "";//身高
	private String Pf_weight = "";//体重
	private String Pf_total_score = "";//综合得分
	private String Pf_total_pg = "";//综合评价
	private String Pf_tsn_zj = "";//体适能主要问题
	private String Pf_sport_cve = "";//运动中心血管事件的发生风险
	private String Pf_sport_sr = "";//运动损伤发生风险
	private String Pf_sport_mt = "";//运动的禁忌项目
	private String Pf_sport_ma = "";//运动其他注意事项
	private String Pf_vo2_max_xd = "";//心肺耐力-最大摄氧量
	private String Pf_vo2_max_xd_pg = "";//心肺耐力-最大摄氧量评价
	private String Pf_vo2_max_xd_zj = "";//心肺耐力-最大摄氧量总结
	private String Pf_fat = "";//身体成分
	private String Pf_fat_pg = "";//身体成分
	private String Pf_fat_zj = "";//身体成分
	private String Pf_grip_strength = "";//握力 
	private String Pf_grip_strength_pg = "";//握力 
	private String Pf_grip_strength_zj = "";//握力 
	private String Pf_sitandreach = "";//坐位体前屈
	private String Pf_sitandreach_pg = "";//坐位体前屈
	private String Pf_sitandreach_zj = "";//坐位体前屈
	private String Pf_close_eye_stand_one_leg = "";//闭眼单脚站
	private String Pf_close_eye_stand_one_leg_pg = "";//闭眼单脚站
	private String Pf_close_eye_stand_one_leg_zj = "";//闭眼单脚站
	private String Pf_choice_rt = "";//选择反应
	private String Pf_choice_rt_pg = "";//选择反应
	private String Pf_choice_rt_zj = "";//选择反应
	private String Pf_kip = "";//重点改善项目
	private String Pf_foitp = "";//重点改善目的
	private String Pf_ns = "";//营养建议
	private String Pf_la = "";//生活方式建议
	private String Pf_dpaa = "";//日常体力活动建议
	private String Pf_rs = "";//复测建议
	private String Pf_yy_one_des = "";//有氧运动第1阶段描述
	private String Pf_yy_one_timerange = "";//有氧运动第1阶段运动时间范围
	private String Pf_yy_two_des = "";//有氧运动第2阶段描述
	private String Pf_yy_two_timerange = "";//有氧运动第2阶段运动时间范围
	private String Pf_yy_three_des = "";//有氧运动第3阶段描述
	private String Pf_yy_three_timerange = "";//有氧运动第3阶段运动时间范围
	private String Pf_yy_four_des = "";//有氧运动第4阶段描述
	private String Pf_yy_four_timerange = "";//有氧运动第4阶段运动时间范围
	private String Pf_yy_fifth_des = "";//有氧运动第5阶段描述
	private String Pf_yy_fifth_timerange = "";//有氧运动第5阶段运动时间范围
	private String Pf_yy_six_des = "";//有氧运动第6阶段描述
	private String Pf_yy_six_timerange = "";//有氧运动第6阶段运动时间范围
	private String Pf_kz_one_des = "";//抗阻运动第1阶段描述
	private String Pf_kz_one_num  = "";//抗阻运动第1阶段运动次数
	private String Pf_kz_two_des = "";//抗阻运动第1阶段描述
	private String Pf_kz_two_num  = "";//抗阻运动第1阶段运动次数
	private String Pf_kz_three_des = "";//抗阻运动第1阶段描述
	private String Pf_kz_three_num  = "";//抗阻运动第1阶段运动次数
	private String Pf_kz_four_des = "";//抗阻运动第1阶段描述
	private String Pf_kz_four_num  = "";//抗阻运动第1阶段运动次数
	private String Pf_kz_fifth_des = "";//抗阻运动第1阶段描述
	private String Pf_kz_fifth_num  = "";//抗阻运动第1阶段运动次数
	private String Pf_kz_six_des = "";//抗阻运动第1阶段描述
	private String Pf_kz_six_num  = "";//抗阻运动第1阶段运动次数
	private String Pf_fc = "";//心脏功能能力
	private String Pf_fc_pg = "";//心脏功能能力评价
	private String Pf_hr1 = "";//HR1
	private String Pf_hr2 = "";//HR2
	private String Pf_met1 = "";//MET1
	private String Pf_met2 = "";//MET2
	private String Pf_cpf_hr1 = "";//第1级心率
	private String Pf_cpf_hr2 = "";//第2级心率
	private String Pf_cpf_hr3 = "";//第3级心率
	private String Pf_cpf_hr4 = "";//第4级心率
	private String Pf_cpf_hr5 = "";//第5级心率
	private String Pf_cpf_hr6 = "";//第6级心率
	private String Pf_cpf_power1 = "";//第1级功率
	private String Pf_cpf_power2 = "";//第2级功率
	private String Pf_cpf_power3 = "";//第3级功率
	private String Pf_cpf_power4 = "";//第4级功率
	private String Pf_cpf_power5 = "";//第5级功率
	private String Pf_cpf_power6 = "";//第6级功率
	private String Pf_cpf_level = "";//心肺测试等级
	private String Pf_cpf_project = "";//心肺测试方案
	private String Pf_hr0 = "";//安静心率
	private String Pf_VitalCapacity = "";//肺活量
	private String Pf_VitalCapacity_df = "";//肺活量得分
	private String Pf_VitalCapacity_pg = "";//肺活量评价
	private String Pf_StepIndex = "";//台阶指数
	private String Pf_StepIndex_df = "";//台阶指数得分
	private String Pf_StepIndex_pg = "";//台阶指数评价
	private String Pf_StandingVerticalJump = "";//纵跳
	private String Pf_StandingVerticalJump_df = "";//纵跳得分
	private String Pf_StandingVerticalJump_pg = "";//纵跳评价
	private String Pf_Push_Up = "";//俯卧撑
	private String Pf_Push_Up_df = "";//俯卧撑得分
	private String Pf_Push_Up_pg = "";//俯卧撑评价
	private String Pf_Curl_Up = "";//仰卧起坐
	private String Pf_Curl_Up_df = "";//仰卧起坐得分
	private String Pf_Curl_Up_pg = "";//仰卧起坐评价
	private String FDataSource = "";//数据来源
	
	public String getMember_id() {
		return member_id;
	}
	public String getRecordno() {
		return recordno;
	}
	public String getTstOrderNo() {
		return tstOrderNo;
	}
	public String getMember_name() {
		return member_name;
	}
	public int getGender() {
		return gender;
	}
	public long getAge() {
		return age;
	}
	public String getTest_count() {
		return test_count;
	}
	public String getTest_time() {
		return test_time;
	}
	public String getCmp() {
		return cmp;
	}
	public String getDept() {
		return dept;
	}
	public String getHeart_rate() {
		return Heart_rate;
	}
	public String getM_sbp() {
		return M_sbp;
	}
	public String getM_dbp() {
		return M_dbp;
	}
	public String getM_sbp_pg() {
		return M_sbp_pg;
	}
	public String getM_dbp_pg() {
		return M_dbp_pg;
	}
	public String getM_tc() {
		return M_tc;
	}
	public String getM_tg() {
		return M_tg;
	}
	public String getM_tc_pg() {
		return M_tc_pg;
	}
	public String getM_tg_pg() {
		return M_tg_pg;
	}
	public String getM_hdl() {
		return M_hdl;
	}
	public String getM_ldl() {
		return M_ldl;
	}
	public String getM_hdl_pg() {
		return M_hdl_pg;
	}
	public String getM_ldl_pg() {
		return M_ldl_pg;
	}
	public String getM_f() {
		return M_f;
	}
	public String getM_2hf() {
		return M_2hf;
	}
	public String getM_fpg() {
		return M_fpg;
	}
	public String getM_2hfpg() {
		return M_2hfpg;
	}
	public String getM_ua() {
		return M_ua;
	}
	public String getM_ecg() {
		return M_ecg;
	}
	public String getM_cca() {
		return M_cca;
	}
	public String getMwj_fgh_xgyc() {
		return Mwj_fgh_xgyc;
	}
	public String getMwj_fgh_bea() {
		return Mwj_fgh_bea;
	}
	public String getMwj_fgh_copd() {
		return Mwj_fgh_copd;
	}
	public String getMwj_fgh_stroke() {
		return Mwj_fgh_stroke;
	}
	public String getMwj_mh_tf() {
		return Mwj_mh_tf;
	}
	public String getMwj_mh_wao() {
		return Mwj_mh_wao;
	}
	public String getMwj_mh_bea() {
		return Mwj_mh_bea;
	}
	public String getMwj_mh_copd() {
		return Mwj_mh_copd;
	}
	public String getMwj_mh_stroke() {
		return Mwj_mh_stroke;
	}
	public String getMwj_mh_chd_igr() {
		return Mwj_mh_chd_igr;
	}
	public String getMwj_mh_cld() {
		return Mwj_mh_cld;
	}
	public String getMwj_mh_bnp() {
		return Mwj_mh_bnp;
	}
	public String getMwj_medication_jyy() {
		return Mwj_medication_jyy;
	}
	public String getMwj_medication_lld() {
		return Mwj_medication_lld;
	}
	public String getMwj_medication_jty() {
		return Mwj_medication_jty;
	}
	public String getMwj_medication_jnsy() {
		return Mwj_medication_jnsy;
	}
	public String getMwj_medication_kxlscy() {
		return Mwj_medication_kxlscy;
	}
	public String getMwj_medication_hjxcyw() {
		return Mwj_medication_hjxcyw;
	}
	public String getMwj_smoke() {
		return Mwj_smoke;
	}
	public String getMwj_sleep() {
		return Mwj_sleep;
	}
	public String getMwj_drink() {
		return Mwj_drink;
	}
	public String getInjury_phusical() {
		return Injury_phusical;
	}
	public String getInjury_wxfc() {
		return Injury_wxfc;
	}
	public String getInjury_hypertension_dose() {
		return Injury_hypertension_dose;
	}
	public String getInjury_lipid_lowering_dose() {
		return Injury_lipid_lowering_dose;
	}
	public String getInjury_diabetes_dose() {
		return Injury_diabetes_dose;
	}
	public String getInjury_shoulder() {
		return Injury_shoulder;
	}
	public String getInjury_elbow() {
		return Injury_elbow;
	}
	public String getInjury_wrist() {
		return Injury_wrist;
	}
	public String getInjury_back() {
		return Injury_back;
	}
	public String getInjury_spine() {
		return Injury_spine;
	}
	public String getInjury_waist() {
		return Injury_waist;
	}
	public String getInjury_thigh() {
		return Injury_thigh;
	}
	public String getInjury_knee() {
		return Injury_knee;
	}
	public String getInjury_ankle() {
		return Injury_ankle;
	}
	public String getInjury_crus() {
		return Injury_crus;
	}
	public String getInjury_achilles_tendon() {
		return Injury_achilles_tendon;
	}
	public String getInjury_option() {
		return Injury_option;
	}
	public String getPf_height() {
		return Pf_height;
	}
	public String getPf_weight() {
		return Pf_weight;
	}
	public String getPf_total_score() {
		return Pf_total_score;
	}
	public String getPf_total_pg() {
		return Pf_total_pg;
	}
	public String getPf_tsn_zj() {
		return Pf_tsn_zj;
	}
	public String getPf_sport_cve() {
		return Pf_sport_cve;
	}
	public String getPf_sport_sr() {
		return Pf_sport_sr;
	}
	public String getPf_sport_mt() {
		return Pf_sport_mt;
	}
	public String getPf_sport_ma() {
		return Pf_sport_ma;
	}
	public String getPf_vo2_max_xd() {
		return Pf_vo2_max_xd;
	}
	public String getPf_vo2_max_xd_pg() {
		return Pf_vo2_max_xd_pg;
	}
	public String getPf_vo2_max_xd_zj() {
		return Pf_vo2_max_xd_zj;
	}
	public String getPf_fat() {
		return Pf_fat;
	}
	public String getPf_fat_pg() {
		return Pf_fat_pg;
	}
	public String getPf_fat_zj() {
		return Pf_fat_zj;
	}
	public String getPf_grip_strength() {
		return Pf_grip_strength;
	}
	public String getPf_grip_strength_pg() {
		return Pf_grip_strength_pg;
	}
	public String getPf_grip_strength_zj() {
		return Pf_grip_strength_zj;
	}
	public String getPf_sitandreach() {
		return Pf_sitandreach;
	}
	public String getPf_sitandreach_pg() {
		return Pf_sitandreach_pg;
	}
	public String getPf_sitandreach_zj() {
		return Pf_sitandreach_zj;
	}
	public String getPf_close_eye_stand_one_leg() {
		return Pf_close_eye_stand_one_leg;
	}
	public String getPf_close_eye_stand_one_leg_pg() {
		return Pf_close_eye_stand_one_leg_pg;
	}
	public String getPf_close_eye_stand_one_leg_zj() {
		return Pf_close_eye_stand_one_leg_zj;
	}
	public String getPf_choice_rt() {
		return Pf_choice_rt;
	}
	public String getPf_choice_rt_pg() {
		return Pf_choice_rt_pg;
	}
	public String getPf_choice_rt_zj() {
		return Pf_choice_rt_zj;
	}
	public String getPf_kip() {
		return Pf_kip;
	}
	public String getPf_foitp() {
		return Pf_foitp;
	}
	public String getPf_ns() {
		return Pf_ns;
	}
	public String getPf_la() {
		return Pf_la;
	}
	public String getPf_dpaa() {
		return Pf_dpaa;
	}
	public String getPf_rs() {
		return Pf_rs;
	}
	public String getPf_yy_one_des() {
		return Pf_yy_one_des;
	}
	public String getPf_yy_one_timerange() {
		return Pf_yy_one_timerange;
	}
	public String getPf_yy_two_des() {
		return Pf_yy_two_des;
	}
	public String getPf_yy_two_timerange() {
		return Pf_yy_two_timerange;
	}
	public String getPf_yy_three_des() {
		return Pf_yy_three_des;
	}
	public String getPf_yy_three_timerange() {
		return Pf_yy_three_timerange;
	}
	public String getPf_yy_four_des() {
		return Pf_yy_four_des;
	}
	public String getPf_yy_four_timerange() {
		return Pf_yy_four_timerange;
	}
	public String getPf_yy_fifth_des() {
		return Pf_yy_fifth_des;
	}
	public String getPf_yy_fifth_timerange() {
		return Pf_yy_fifth_timerange;
	}
	public String getPf_yy_six_des() {
		return Pf_yy_six_des;
	}
	public String getPf_yy_six_timerange() {
		return Pf_yy_six_timerange;
	}
	public String getPf_kz_one_des() {
		return Pf_kz_one_des;
	}
	public String getPf_kz_one_num() {
		return Pf_kz_one_num;
	}
	public String getPf_kz_two_des() {
		return Pf_kz_two_des;
	}
	public String getPf_kz_two_num() {
		return Pf_kz_two_num;
	}
	public String getPf_kz_three_des() {
		return Pf_kz_three_des;
	}
	public String getPf_kz_three_num() {
		return Pf_kz_three_num;
	}
	public String getPf_kz_four_des() {
		return Pf_kz_four_des;
	}
	public String getPf_kz_four_num() {
		return Pf_kz_four_num;
	}
	public String getPf_kz_fifth_des() {
		return Pf_kz_fifth_des;
	}
	public String getPf_kz_fifth_num() {
		return Pf_kz_fifth_num;
	}
	public String getPf_kz_six_des() {
		return Pf_kz_six_des;
	}
	public String getPf_kz_six_num() {
		return Pf_kz_six_num;
	}
	public String getPf_fc() {
		return Pf_fc;
	}
	public String getPf_fc_pg() {
		return Pf_fc_pg;
	}
	public String getPf_hr1() {
		return Pf_hr1;
	}
	public String getPf_hr2() {
		return Pf_hr2;
	}
	public String getPf_met1() {
		return Pf_met1;
	}
	public String getPf_met2() {
		return Pf_met2;
	}
	public String getPf_cpf_hr1() {
		return Pf_cpf_hr1;
	}
	public String getPf_cpf_hr2() {
		return Pf_cpf_hr2;
	}
	public String getPf_cpf_hr3() {
		return Pf_cpf_hr3;
	}
	public String getPf_cpf_hr4() {
		return Pf_cpf_hr4;
	}
	public String getPf_cpf_hr5() {
		return Pf_cpf_hr5;
	}
	public String getPf_cpf_hr6() {
		return Pf_cpf_hr6;
	}
	public String getPf_cpf_power1() {
		return Pf_cpf_power1;
	}
	public String getPf_cpf_power2() {
		return Pf_cpf_power2;
	}
	public String getPf_cpf_power3() {
		return Pf_cpf_power3;
	}
	public String getPf_cpf_power4() {
		return Pf_cpf_power4;
	}
	public String getPf_cpf_power5() {
		return Pf_cpf_power5;
	}
	public String getPf_cpf_power6() {
		return Pf_cpf_power6;
	}
	public String getPf_cpf_level() {
		return Pf_cpf_level;
	}
	public String getPf_cpf_project() {
		return Pf_cpf_project;
	}
	public String getPf_hr0() {
		return Pf_hr0;
	}
	public String getPf_VitalCapacity() {
		return Pf_VitalCapacity;
	}
	public String getPf_VitalCapacity_df() {
		return Pf_VitalCapacity_df;
	}
	public String getPf_VitalCapacity_pg() {
		return Pf_VitalCapacity_pg;
	}
	public String getPf_StepIndex() {
		return Pf_StepIndex;
	}
	public String getPf_StepIndex_df() {
		return Pf_StepIndex_df;
	}
	public String getPf_StepIndex_pg() {
		return Pf_StepIndex_pg;
	}
	public String getPf_StandingVerticalJump() {
		return Pf_StandingVerticalJump;
	}
	public String getPf_StandingVerticalJump_df() {
		return Pf_StandingVerticalJump_df;
	}
	public String getPf_StandingVerticalJump_pg() {
		return Pf_StandingVerticalJump_pg;
	}
	public String getPf_Push_Up() {
		return Pf_Push_Up;
	}
	public String getPf_Push_Up_df() {
		return Pf_Push_Up_df;
	}
	public String getPf_Push_Up_pg() {
		return Pf_Push_Up_pg;
	}
	public String getPf_Curl_Up() {
		return Pf_Curl_Up;
	}
	public String getPf_Curl_Up_df() {
		return Pf_Curl_Up_df;
	}
	public String getPf_Curl_Up_pg() {
		return Pf_Curl_Up_pg;
	}
	public String getFDataSource() {
		return FDataSource;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public void setRecordno(String recordno) {
		this.recordno = recordno;
	}
	public void setTstOrderNo(String tstOrderNo) {
		this.tstOrderNo = tstOrderNo;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public void setAge(long age) {
		this.age = age;
	}
	public void setTest_count(String test_count) {
		this.test_count = test_count;
	}
	public void setTest_time(String test_time) {
		this.test_time = test_time;
	}
	public void setCmp(String cmp) {
		this.cmp = cmp;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public void setHeart_rate(String heart_rate) {
		Heart_rate = heart_rate;
	}
	public void setM_sbp(String m_sbp) {
		M_sbp = m_sbp;
	}
	public void setM_dbp(String m_dbp) {
		M_dbp = m_dbp;
	}
	public void setM_sbp_pg(String m_sbp_pg) {
		M_sbp_pg = m_sbp_pg;
	}
	public void setM_dbp_pg(String m_dbp_pg) {
		M_dbp_pg = m_dbp_pg;
	}
	public void setM_tc(String m_tc) {
		M_tc = m_tc;
	}
	public void setM_tg(String m_tg) {
		M_tg = m_tg;
	}
	public void setM_tc_pg(String m_tc_pg) {
		M_tc_pg = m_tc_pg;
	}
	public void setM_tg_pg(String m_tg_pg) {
		M_tg_pg = m_tg_pg;
	}
	public void setM_hdl(String m_hdl) {
		M_hdl = m_hdl;
	}
	public void setM_ldl(String m_ldl) {
		M_ldl = m_ldl;
	}
	public void setM_hdl_pg(String m_hdl_pg) {
		M_hdl_pg = m_hdl_pg;
	}
	public void setM_ldl_pg(String m_ldl_pg) {
		M_ldl_pg = m_ldl_pg;
	}
	public void setM_f(String m_f) {
		M_f = m_f;
	}
	public void setM_2hf(String m_2hf) {
		M_2hf = m_2hf;
	}
	public void setM_fpg(String m_fpg) {
		M_fpg = m_fpg;
	}
	public void setM_2hfpg(String m_2hfpg) {
		M_2hfpg = m_2hfpg;
	}
	public void setM_ua(String m_ua) {
		M_ua = m_ua;
	}
	public void setM_ecg(String m_ecg) {
		M_ecg = m_ecg;
	}
	public void setM_cca(String m_cca) {
		M_cca = m_cca;
	}
	public void setMwj_fgh_xgyc(String mwj_fgh_xgyc) {
		Mwj_fgh_xgyc = mwj_fgh_xgyc;
	}
	public void setMwj_fgh_bea(String mwj_fgh_bea) {
		Mwj_fgh_bea = mwj_fgh_bea;
	}
	public void setMwj_fgh_copd(String mwj_fgh_copd) {
		Mwj_fgh_copd = mwj_fgh_copd;
	}
	public void setMwj_fgh_stroke(String mwj_fgh_stroke) {
		Mwj_fgh_stroke = mwj_fgh_stroke;
	}
	public void setMwj_mh_tf(String mwj_mh_tf) {
		Mwj_mh_tf = mwj_mh_tf;
	}
	public void setMwj_mh_wao(String mwj_mh_wao) {
		Mwj_mh_wao = mwj_mh_wao;
	}
	public void setMwj_mh_bea(String mwj_mh_bea) {
		Mwj_mh_bea = mwj_mh_bea;
	}
	public void setMwj_mh_copd(String mwj_mh_copd) {
		Mwj_mh_copd = mwj_mh_copd;
	}
	public void setMwj_mh_stroke(String mwj_mh_stroke) {
		Mwj_mh_stroke = mwj_mh_stroke;
	}
	public void setMwj_mh_chd_igr(String mwj_mh_chd_igr) {
		Mwj_mh_chd_igr = mwj_mh_chd_igr;
	}
	public void setMwj_mh_cld(String mwj_mh_cld) {
		Mwj_mh_cld = mwj_mh_cld;
	}
	public void setMwj_mh_bnp(String mwj_mh_bnp) {
		Mwj_mh_bnp = mwj_mh_bnp;
	}
	public void setMwj_medication_jyy(String mwj_medication_jyy) {
		Mwj_medication_jyy = mwj_medication_jyy;
	}
	public void setMwj_medication_lld(String mwj_medication_lld) {
		Mwj_medication_lld = mwj_medication_lld;
	}
	public void setMwj_medication_jty(String mwj_medication_jty) {
		Mwj_medication_jty = mwj_medication_jty;
	}
	public void setMwj_medication_jnsy(String mwj_medication_jnsy) {
		Mwj_medication_jnsy = mwj_medication_jnsy;
	}
	public void setMwj_medication_kxlscy(String mwj_medication_kxlscy) {
		Mwj_medication_kxlscy = mwj_medication_kxlscy;
	}
	public void setMwj_medication_hjxcyw(String mwj_medication_hjxcyw) {
		Mwj_medication_hjxcyw = mwj_medication_hjxcyw;
	}
	public void setMwj_smoke(String mwj_smoke) {
		Mwj_smoke = mwj_smoke;
	}
	public void setMwj_sleep(String mwj_sleep) {
		Mwj_sleep = mwj_sleep;
	}
	public void setMwj_drink(String mwj_drink) {
		Mwj_drink = mwj_drink;
	}
	public void setInjury_phusical(String injury_phusical) {
		Injury_phusical = injury_phusical;
	}
	public void setInjury_wxfc(String injury_wxfc) {
		Injury_wxfc = injury_wxfc;
	}
	public void setInjury_hypertension_dose(String injury_hypertension_dose) {
		Injury_hypertension_dose = injury_hypertension_dose;
	}
	public void setInjury_lipid_lowering_dose(String injury_lipid_lowering_dose) {
		Injury_lipid_lowering_dose = injury_lipid_lowering_dose;
	}
	public void setInjury_diabetes_dose(String injury_diabetes_dose) {
		Injury_diabetes_dose = injury_diabetes_dose;
	}
	public void setInjury_shoulder(String injury_shoulder) {
		Injury_shoulder = injury_shoulder;
	}
	public void setInjury_elbow(String injury_elbow) {
		Injury_elbow = injury_elbow;
	}
	public void setInjury_wrist(String injury_wrist) {
		Injury_wrist = injury_wrist;
	}
	public void setInjury_back(String injury_back) {
		Injury_back = injury_back;
	}
	public void setInjury_spine(String injury_spine) {
		Injury_spine = injury_spine;
	}
	public void setInjury_waist(String injury_waist) {
		Injury_waist = injury_waist;
	}
	public void setInjury_thigh(String injury_thigh) {
		Injury_thigh = injury_thigh;
	}
	public void setInjury_knee(String injury_knee) {
		Injury_knee = injury_knee;
	}
	public void setInjury_ankle(String injury_ankle) {
		Injury_ankle = injury_ankle;
	}
	public void setInjury_crus(String injury_crus) {
		Injury_crus = injury_crus;
	}
	public void setInjury_achilles_tendon(String injury_achilles_tendon) {
		Injury_achilles_tendon = injury_achilles_tendon;
	}
	public void setInjury_option(String injury_option) {
		Injury_option = injury_option;
	}
	public void setPf_height(String pf_height) {
		Pf_height = pf_height;
	}
	public void setPf_weight(String pf_weight) {
		Pf_weight = pf_weight;
	}
	public void setPf_total_score(String pf_total_score) {
		Pf_total_score = pf_total_score;
	}
	public void setPf_total_pg(String pf_total_pg) {
		Pf_total_pg = pf_total_pg;
	}
	public void setPf_tsn_zj(String pf_tsn_zj) {
		Pf_tsn_zj = pf_tsn_zj;
	}
	public void setPf_sport_cve(String pf_sport_cve) {
		Pf_sport_cve = pf_sport_cve;
	}
	public void setPf_sport_sr(String pf_sport_sr) {
		Pf_sport_sr = pf_sport_sr;
	}
	public void setPf_sport_mt(String pf_sport_mt) {
		Pf_sport_mt = pf_sport_mt;
	}
	public void setPf_sport_ma(String pf_sport_ma) {
		Pf_sport_ma = pf_sport_ma;
	}
	public void setPf_vo2_max_xd(String pf_vo2_max_xd) {
		Pf_vo2_max_xd = pf_vo2_max_xd;
	}
	public void setPf_vo2_max_xd_pg(String pf_vo2_max_xd_pg) {
		Pf_vo2_max_xd_pg = pf_vo2_max_xd_pg;
	}
	public void setPf_vo2_max_xd_zj(String pf_vo2_max_xd_zj) {
		Pf_vo2_max_xd_zj = pf_vo2_max_xd_zj;
	}
	public void setPf_fat(String pf_fat) {
		Pf_fat = pf_fat;
	}
	public void setPf_fat_pg(String pf_fat_pg) {
		Pf_fat_pg = pf_fat_pg;
	}
	public void setPf_fat_zj(String pf_fat_zj) {
		Pf_fat_zj = pf_fat_zj;
	}
	public void setPf_grip_strength(String pf_grip_strength) {
		Pf_grip_strength = pf_grip_strength;
	}
	public void setPf_grip_strength_pg(String pf_grip_strength_pg) {
		Pf_grip_strength_pg = pf_grip_strength_pg;
	}
	public void setPf_grip_strength_zj(String pf_grip_strength_zj) {
		Pf_grip_strength_zj = pf_grip_strength_zj;
	}
	public void setPf_sitandreach(String pf_sitandreach) {
		Pf_sitandreach = pf_sitandreach;
	}
	public void setPf_sitandreach_pg(String pf_sitandreach_pg) {
		Pf_sitandreach_pg = pf_sitandreach_pg;
	}
	public void setPf_sitandreach_zj(String pf_sitandreach_zj) {
		Pf_sitandreach_zj = pf_sitandreach_zj;
	}
	public void setPf_close_eye_stand_one_leg(String pf_close_eye_stand_one_leg) {
		Pf_close_eye_stand_one_leg = pf_close_eye_stand_one_leg;
	}
	public void setPf_close_eye_stand_one_leg_pg(String pf_close_eye_stand_one_leg_pg) {
		Pf_close_eye_stand_one_leg_pg = pf_close_eye_stand_one_leg_pg;
	}
	public void setPf_close_eye_stand_one_leg_zj(String pf_close_eye_stand_one_leg_zj) {
		Pf_close_eye_stand_one_leg_zj = pf_close_eye_stand_one_leg_zj;
	}
	public void setPf_choice_rt(String pf_choice_rt) {
		Pf_choice_rt = pf_choice_rt;
	}
	public void setPf_choice_rt_pg(String pf_choice_rt_pg) {
		Pf_choice_rt_pg = pf_choice_rt_pg;
	}
	public void setPf_choice_rt_zj(String pf_choice_rt_zj) {
		Pf_choice_rt_zj = pf_choice_rt_zj;
	}
	public void setPf_kip(String pf_kip) {
		Pf_kip = pf_kip;
	}
	public void setPf_foitp(String pf_foitp) {
		Pf_foitp = pf_foitp;
	}
	public void setPf_ns(String pf_ns) {
		Pf_ns = pf_ns;
	}
	public void setPf_la(String pf_la) {
		Pf_la = pf_la;
	}
	public void setPf_dpaa(String pf_dpaa) {
		Pf_dpaa = pf_dpaa;
	}
	public void setPf_rs(String pf_rs) {
		Pf_rs = pf_rs;
	}
	public void setPf_yy_one_des(String pf_yy_one_des) {
		Pf_yy_one_des = pf_yy_one_des;
	}
	public void setPf_yy_one_timerange(String pf_yy_one_timerange) {
		Pf_yy_one_timerange = pf_yy_one_timerange;
	}
	public void setPf_yy_two_des(String pf_yy_two_des) {
		Pf_yy_two_des = pf_yy_two_des;
	}
	public void setPf_yy_two_timerange(String pf_yy_two_timerange) {
		Pf_yy_two_timerange = pf_yy_two_timerange;
	}
	public void setPf_yy_three_des(String pf_yy_three_des) {
		Pf_yy_three_des = pf_yy_three_des;
	}
	public void setPf_yy_three_timerange(String pf_yy_three_timerange) {
		Pf_yy_three_timerange = pf_yy_three_timerange;
	}
	public void setPf_yy_four_des(String pf_yy_four_des) {
		Pf_yy_four_des = pf_yy_four_des;
	}
	public void setPf_yy_four_timerange(String pf_yy_four_timerange) {
		Pf_yy_four_timerange = pf_yy_four_timerange;
	}
	public void setPf_yy_fifth_des(String pf_yy_fifth_des) {
		Pf_yy_fifth_des = pf_yy_fifth_des;
	}
	public void setPf_yy_fifth_timerange(String pf_yy_fifth_timerange) {
		Pf_yy_fifth_timerange = pf_yy_fifth_timerange;
	}
	public void setPf_yy_six_des(String pf_yy_six_des) {
		Pf_yy_six_des = pf_yy_six_des;
	}
	public void setPf_yy_six_timerange(String pf_yy_six_timerange) {
		Pf_yy_six_timerange = pf_yy_six_timerange;
	}
	public void setPf_kz_one_des(String pf_kz_one_des) {
		Pf_kz_one_des = pf_kz_one_des;
	}
	public void setPf_kz_one_num(String pf_kz_one_num) {
		Pf_kz_one_num = pf_kz_one_num;
	}
	public void setPf_kz_two_des(String pf_kz_two_des) {
		Pf_kz_two_des = pf_kz_two_des;
	}
	public void setPf_kz_two_num(String pf_kz_two_num) {
		Pf_kz_two_num = pf_kz_two_num;
	}
	public void setPf_kz_three_des(String pf_kz_three_des) {
		Pf_kz_three_des = pf_kz_three_des;
	}
	public void setPf_kz_three_num(String pf_kz_three_num) {
		Pf_kz_three_num = pf_kz_three_num;
	}
	public void setPf_kz_four_des(String pf_kz_four_des) {
		Pf_kz_four_des = pf_kz_four_des;
	}
	public void setPf_kz_four_num(String pf_kz_four_num) {
		Pf_kz_four_num = pf_kz_four_num;
	}
	public void setPf_kz_fifth_des(String pf_kz_fifth_des) {
		Pf_kz_fifth_des = pf_kz_fifth_des;
	}
	public void setPf_kz_fifth_num(String pf_kz_fifth_num) {
		Pf_kz_fifth_num = pf_kz_fifth_num;
	}
	public void setPf_kz_six_des(String pf_kz_six_des) {
		Pf_kz_six_des = pf_kz_six_des;
	}
	public void setPf_kz_six_num(String pf_kz_six_num) {
		Pf_kz_six_num = pf_kz_six_num;
	}
	public void setPf_fc(String pf_fc) {
		Pf_fc = pf_fc;
	}
	public void setPf_fc_pg(String pf_fc_pg) {
		Pf_fc_pg = pf_fc_pg;
	}
	public void setPf_hr1(String pf_hr1) {
		Pf_hr1 = pf_hr1;
	}
	public void setPf_hr2(String pf_hr2) {
		Pf_hr2 = pf_hr2;
	}
	public void setPf_met1(String pf_met1) {
		Pf_met1 = pf_met1;
	}
	public void setPf_met2(String pf_met2) {
		Pf_met2 = pf_met2;
	}
	public void setPf_cpf_hr1(String pf_cpf_hr1) {
		Pf_cpf_hr1 = pf_cpf_hr1;
	}
	public void setPf_cpf_hr2(String pf_cpf_hr2) {
		Pf_cpf_hr2 = pf_cpf_hr2;
	}
	public void setPf_cpf_hr3(String pf_cpf_hr3) {
		Pf_cpf_hr3 = pf_cpf_hr3;
	}
	public void setPf_cpf_hr4(String pf_cpf_hr4) {
		Pf_cpf_hr4 = pf_cpf_hr4;
	}
	public void setPf_cpf_hr5(String pf_cpf_hr5) {
		Pf_cpf_hr5 = pf_cpf_hr5;
	}
	public void setPf_cpf_hr6(String pf_cpf_hr6) {
		Pf_cpf_hr6 = pf_cpf_hr6;
	}
	public void setPf_cpf_power1(String pf_cpf_power1) {
		Pf_cpf_power1 = pf_cpf_power1;
	}
	public void setPf_cpf_power2(String pf_cpf_power2) {
		Pf_cpf_power2 = pf_cpf_power2;
	}
	public void setPf_cpf_power3(String pf_cpf_power3) {
		Pf_cpf_power3 = pf_cpf_power3;
	}
	public void setPf_cpf_power4(String pf_cpf_power4) {
		Pf_cpf_power4 = pf_cpf_power4;
	}
	public void setPf_cpf_power5(String pf_cpf_power5) {
		Pf_cpf_power5 = pf_cpf_power5;
	}
	public void setPf_cpf_power6(String pf_cpf_power6) {
		Pf_cpf_power6 = pf_cpf_power6;
	}
	public void setPf_cpf_level(String pf_cpf_level) {
		Pf_cpf_level = pf_cpf_level;
	}
	public void setPf_cpf_project(String pf_cpf_project) {
		Pf_cpf_project = pf_cpf_project;
	}
	public void setPf_hr0(String pf_hr0) {
		Pf_hr0 = pf_hr0;
	}
	public void setPf_VitalCapacity(String pf_VitalCapacity) {
		Pf_VitalCapacity = pf_VitalCapacity;
	}
	public void setPf_VitalCapacity_df(String pf_VitalCapacity_df) {
		Pf_VitalCapacity_df = pf_VitalCapacity_df;
	}
	public void setPf_VitalCapacity_pg(String pf_VitalCapacity_pg) {
		Pf_VitalCapacity_pg = pf_VitalCapacity_pg;
	}
	public void setPf_StepIndex(String pf_StepIndex) {
		Pf_StepIndex = pf_StepIndex;
	}
	public void setPf_StepIndex_df(String pf_StepIndex_df) {
		Pf_StepIndex_df = pf_StepIndex_df;
	}
	public void setPf_StepIndex_pg(String pf_StepIndex_pg) {
		Pf_StepIndex_pg = pf_StepIndex_pg;
	}
	public void setPf_StandingVerticalJump(String pf_StandingVerticalJump) {
		Pf_StandingVerticalJump = pf_StandingVerticalJump;
	}
	public void setPf_StandingVerticalJump_df(String pf_StandingVerticalJump_df) {
		Pf_StandingVerticalJump_df = pf_StandingVerticalJump_df;
	}
	public void setPf_StandingVerticalJump_pg(String pf_StandingVerticalJump_pg) {
		Pf_StandingVerticalJump_pg = pf_StandingVerticalJump_pg;
	}
	public void setPf_Push_Up(String pf_Push_Up) {
		Pf_Push_Up = pf_Push_Up;
	}
	public void setPf_Push_Up_df(String pf_Push_Up_df) {
		Pf_Push_Up_df = pf_Push_Up_df;
	}
	public void setPf_Push_Up_pg(String pf_Push_Up_pg) {
		Pf_Push_Up_pg = pf_Push_Up_pg;
	}
	public void setPf_Curl_Up(String pf_Curl_Up) {
		Pf_Curl_Up = pf_Curl_Up;
	}
	public void setPf_Curl_Up_df(String pf_Curl_Up_df) {
		Pf_Curl_Up_df = pf_Curl_Up_df;
	}
	public void setPf_Curl_Up_pg(String pf_Curl_Up_pg) {
		Pf_Curl_Up_pg = pf_Curl_Up_pg;
	}
	public void setFDataSource(String fDataSource) {
		FDataSource = fDataSource;
	}
}
