package com.hjw.webService.job.bean;
/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.wst.DTO   
     * @Description:  科室检查细项结论
     * @author: zr     
     * @date:   2016年11月29日 下午8:12:00   
     * @version V2.0.0.0
 */

public class CommonExamDetailDTO  implements java.io.Serializable{

	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 9165522337204355286L;
	private long id;
	private long exam_info_id;
	private long exam_item_id;
	private String item_num="";
	private String item_name="";
	private String exam_doctor="";
	private String center_num="";
	private String health_level="";
	private String exam_result="";
	private String exam_date="";
	private long creater;
	private String create_time="";
	private long updater;
	private long charging_item_id;	
	private String charging_item_code="";
	private String charging_item_name="";
	private String join_date="";
	private String suggestion="";
	private String dep_num="";
	private long dep_id;
	private String dep_name="";
	private long examination_item_id;//
	private long seq_code;
	private double ref_Mmax;
	private double ref_Mmin;
	private double ref_Fmin;
	private double ref_Fmax;
	private String item_unit="";
	private String item_category="";
	
	public long getExamination_item_id() {
		return examination_item_id;
	}
	public void setExamination_item_id(long examination_item_id) {
		this.examination_item_id = examination_item_id;
	}
	public String getDep_num() {
		return dep_num;
	}
	public void setDep_num(String dep_num) {
		this.dep_num = dep_num;
	}
	public String getCharging_item_code() {
		return charging_item_code;
	}
	public void setCharging_item_code(String charging_item_code) {
		this.charging_item_code = charging_item_code;
	}
	public String getCharging_item_name() {
		return charging_item_name;
	}
	public void setCharging_item_name(String charging_item_name) {
		this.charging_item_name = charging_item_name;
	}
	public long getCharging_item_id() {
		return charging_item_id;
	}
	public void setCharging_item_id(long charging_item_id) {
		this.charging_item_id = charging_item_id;
	}
	public String getItem_category() {
		return item_category;
	}
	public void setItem_category(String item_category) {
		this.item_category = item_category;
	}
	public String getItem_unit() {
		return item_unit;
	}
	public void setItem_unit(String item_unit) {
		this.item_unit = item_unit;
	}
	public long getDep_id() {
		return dep_id;
	}
	public void setDep_id(long dep_id) {
		this.dep_id = dep_id;
	}
	public String getDep_name() {
		return dep_name;
	}
	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}
	public String getItem_num() {
		return item_num;
	}
	public void setItem_num(String item_num) {
		this.item_num = item_num;
	}
	public long getSeq_code() {
		return seq_code;
	}
	public void setSeq_code(long seq_code) {
		this.seq_code = seq_code;
	}
	public double getRef_Mmax() {
		return ref_Mmax;
	}
	public void setRef_Mmax(double ref_Mmax) {
		this.ref_Mmax = ref_Mmax;
	}
	public double getRef_Mmin() {
		return ref_Mmin;
	}
	public void setRef_Mmin(double ref_Mmin) {
		this.ref_Mmin = ref_Mmin;
	}
	public double getRef_Fmin() {
		return ref_Fmin;
	}
	public void setRef_Fmin(double ref_Fmin) {
		this.ref_Fmin = ref_Fmin;
	}
	public double getRef_Fmax() {
		return ref_Fmax;
	}
	public void setRef_Fmax(double ref_Fmax) {
		this.ref_Fmax = ref_Fmax;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public String getJoin_date() {
		return join_date;
	}
	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExam_info_id() {
		return exam_info_id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public long getExam_item_id() {
		return exam_item_id;
	}
	public void setExam_item_id(long exam_item_id) {
		this.exam_item_id = exam_item_id;
	}
	public String getExam_doctor() {
		return exam_doctor;
	}
	public void setExam_doctor(String exam_doctor) {
		this.exam_doctor = exam_doctor;
	}
	public String getCenter_num() {
		return center_num;
	}
	public void setCenter_num(String center_num) {
		this.center_num = center_num;
	}
	public String getHealth_level() {
		return health_level;
	}
	public void setHealth_level(String health_level) {
		this.health_level = health_level;
	}
	public String getExam_result() {
		return exam_result;
	}
	public void setExam_result(String exam_result) {
		this.exam_result = exam_result;
	}
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
	public long getCreater() {
		return creater;
	}
	public void setCreater(long creater) {
		this.creater = creater;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public long getUpdater() {
		return updater;
	}
	public void setUpdater(long updater) {
		this.updater = updater;
	}
	
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
}
