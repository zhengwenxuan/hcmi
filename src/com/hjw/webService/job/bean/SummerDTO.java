package com.hjw.webService.job.bean;

public class SummerDTO implements java.io.Serializable{

	private static final long serialVersionUID = -97502163798576023L;

	private String final_exam_result="";
	private String exam_doctor="";
	private String create_time="";
	private String check_doc="";
	private String check_time="";
	private String suggest="";
	
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getFinal_exam_result() {
		return final_exam_result;
	}
	public void setFinal_exam_result(String final_exam_result) {
		this.final_exam_result = final_exam_result;
	}
	public String getExam_doctor() {
		return exam_doctor;
	}
	public void setExam_doctor(String exam_doctor) {
		this.exam_doctor = exam_doctor;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCheck_doc() {
		return check_doc;
	}
	public void setCheck_doc(String check_doc) {
		this.check_doc = check_doc;
	}
	public String getCheck_time() {
		return check_time;
	}
	public void setCheck_time(String check_time) {
		this.check_time = check_time;
	}
    
	
}
