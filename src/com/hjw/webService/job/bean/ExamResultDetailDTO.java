package com.hjw.webService.job.bean;

public class ExamResultDetailDTO {

	private long id;
	private long exam_info_id;
	private long exam_item_id;
	private String item_name="";
	private String exam_result="";
	private String ref_indicator="";
	private String item_unit="";
	private String ref_value="";
	
	private String demo_name="";
	private String item_code="";
	private String exam_num="";
	private String exam_status="";
	private String exam_date="";
	private String exam_doctor="";
	private String item_category="";	
	private String exam_item_category="";
	private String dang_min="";
	private String dang_max="";
	private String ref_min="";
	private String ref_max="";
	private String center_num="";
	private String approver="";
	private String approve_date="";
	private long creater;
	private String create_time="";
	private long updater;
    private String update_time="";
	private long dep_id;
	private String dep_name="";
	private String item_num="";
	private long seq_code;	
	private long charging_item_id;
	private String charging_item_code;
	private String charging_item_name="";
	private String dep_num="";
	
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
	public long getCharging_item_id() {
		return charging_item_id;
	}
	public void setCharging_item_id(long charging_item_id) {
		this.charging_item_id = charging_item_id;
	}
	public String getCharging_item_name() {
		return charging_item_name;
	}
	public void setCharging_item_name(String charging_item_name) {
		this.charging_item_name = charging_item_name;
	}
	public String getExam_item_category() {
		return exam_item_category;
	}
	public void setExam_item_category(String exam_item_category) {
		this.exam_item_category = exam_item_category;
	}
	public String getDang_min() {
		return dang_min;
	}
	public void setDang_min(String dang_min) {
		this.dang_min = dang_min;
	}
	public String getDang_max() {
		return dang_max;
	}
	public void setDang_max(String dang_max) {
		this.dang_max = dang_max;
	}
	public String getRef_min() {
		return ref_min;
	}
	public void setRef_min(String ref_min) {
		this.ref_min = ref_min;
	}
	public String getRef_max() {
		return ref_max;
	}
	public void setRef_max(String ref_max) {
		this.ref_max = ref_max;
	}
	public String getCenter_num() {
		return center_num;
	}
	public void setCenter_num(String center_num) {
		this.center_num = center_num;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(String approve_date) {
		this.approve_date = approve_date;
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
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getExam_item_id() {
		return exam_item_id;
	}
	public void setExam_item_id(long exam_item_id) {
		this.exam_item_id = exam_item_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getExam_result() {
		return exam_result;
	}
	public void setExam_result(String exam_result) {
		this.exam_result = exam_result;
	}
	public String getRef_indicator() {
		return ref_indicator;
	}
	public void setRef_indicator(String ref_indicator) {
		this.ref_indicator = ref_indicator;
	}
	public String getItem_unit() {
		return item_unit;
	}
	public void setItem_unit(String item_unit) {
		this.item_unit = item_unit;
	}
	public String getRef_value() {
		return ref_value;
	}
	public void setRef_value(String ref_value) {
		this.ref_value = ref_value;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public long getExam_info_id() {
		return exam_info_id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public String getDemo_name() {
		return demo_name;
	}
	public void setDemo_name(String demo_name) {
		this.demo_name = demo_name;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getExam_status() {
		return exam_status;
	}
	public void setExam_status(String exam_status) {
		this.exam_status = exam_status;
	}
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
	public String getExam_doctor() {
		return exam_doctor;
	}
	public void setExam_doctor(String exam_doctor) {
		this.exam_doctor = exam_doctor;
	}
	public String getItem_category() {
		return item_category;
	}
	public void setItem_category(String item_category) {
		this.item_category = item_category;
	}
	

}
