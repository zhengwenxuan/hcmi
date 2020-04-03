package com.hjw.webService.client.longkou.bean;

public class ExamSummaryLKBean {

    private long id;

	private long exam_doctor_id;

	private long exam_info_id;

	private String final_exam_result="";

	private String result_Y="";

	private String result_D="";

	private String suggest="";

	private String center_num="";

	private long check_doc;

	private String check_time="";

	private String approve_status="";;

	private long creater;

	private String create_time="";;

	private long updater;

	private String update_time="";;

	private long acceptance_check;

	private long acceptance_doctor;

	private String acceptance_date="";

	private long read_status;

	private String read_time="";	
	
	private String examdocname="";
	
	private String checkdocname="";	
	
	private String exam_num="";
	private String pdf_file="";
	
	

	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getPdf_file() {
		return pdf_file;
	}
	public void setPdf_file(String pdf_file) {
		this.pdf_file = pdf_file;
	}
	public String getExamdocname() {
		return examdocname;
	}
	public void setExamdocname(String examdocname) {
		this.examdocname = examdocname;
	}
	public String getCheckdocname() {
		return checkdocname;
	}
	public void setCheckdocname(String checkdocname) {
		this.checkdocname = checkdocname;
	}
	public long getExam_doctor_id() {
		return exam_doctor_id;
	}
	public void setExam_doctor_id(long exam_doctor_id) {
		this.exam_doctor_id = exam_doctor_id;
	}
	public String getResult_Y() {
		return result_Y;
	}
	public void setResult_Y(String result_Y) {
		this.result_Y = result_Y;
	}
	public String getResult_D() {
		return result_D;
	}
	public void setResult_D(String result_D) {
		this.result_D = result_D;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getCenter_num() {
		return center_num;
	}
	public void setCenter_num(String center_num) {
		this.center_num = center_num;
	}
	public long getCheck_doc() {
		return check_doc;
	}
	public void setCheck_doc(long check_doc) {
		this.check_doc = check_doc;
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
	public long getAcceptance_doctor() {
		return acceptance_doctor;
	}
	public void setAcceptance_doctor(long acceptance_doctor) {
		this.acceptance_doctor = acceptance_doctor;
	}
	public long getRead_status() {
		return read_status;
	}
	public void setRead_status(long read_status) {
		this.read_status = read_status;
	}
	public String getRead_time() {
		return read_time;
	}
	public void setRead_time(String read_time) {
		this.read_time = read_time;
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
	public String getFinal_exam_result() {
		return final_exam_result;
	}
	public void setFinal_exam_result(String final_exam_result) {
		this.final_exam_result = final_exam_result;
	}
	public long getAcceptance_check() {
		return acceptance_check;
	}
	public void setAcceptance_check(long acceptance_check) {
		this.acceptance_check = acceptance_check;
	}
	
	public String getAcceptance_date() {
		return acceptance_date;
	}
	public void setAcceptance_date(String acceptance_date) {
		this.acceptance_date = acceptance_date;
	}
	
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}
	public String getCheck_time() {
		return check_time;
	}
	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}
}
