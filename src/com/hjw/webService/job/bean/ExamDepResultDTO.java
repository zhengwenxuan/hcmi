package com.hjw.webService.job.bean;

public class ExamDepResultDTO {

	private long id;
	private long exam_info_id;
	private String exam_doctor;
	private String exam_date;
	private String exam_result_summary;
	private String suggestion;
	
	private long dep_id;
	private String center_num="";
	private long approver;
	private String approvername="";
	private String approve_date="";
	private long creater;
	private String create_time="";
	private long updater;
	private String update_time="";
	private String Special_setup="";
	private String dep_name="";
	private String dep_num="";
	
	public String getDep_num() {
		return dep_num;
	}
	public void setDep_num(String dep_num) {
		this.dep_num = dep_num;
	}
	public String getApprovername() {
		return approvername;
	}
	public void setApprovername(String approvername) {
		this.approvername = approvername;
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
	public String getExam_doctor() {
		return exam_doctor;
	}
	public void setExam_doctor(String exam_doctor) {
		this.exam_doctor = exam_doctor;
	}
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
	public String getExam_result_summary() {
		return exam_result_summary;
	}
	public void setExam_result_summary(String exam_result_summary) {
		this.exam_result_summary = exam_result_summary;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public long getDep_id() {
		return dep_id;
	}
	public void setDep_id(long dep_id) {
		this.dep_id = dep_id;
	}
	public String getCenter_num() {
		return center_num;
	}
	public void setCenter_num(String center_num) {
		this.center_num = center_num;
	}
	public long getApprover() {
		return approver;
	}
	public void setApprover(long approver) {
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
	public String getSpecial_setup() {
		return Special_setup;
	}
	public void setSpecial_setup(String special_setup) {
		Special_setup = special_setup;
	}
	public String getDep_name() {
		return dep_name;
	}
	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}
	 
	
}
