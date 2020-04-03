package com.hjw.webService.client.zhaotong.bean;

import java.util.Date;

public class ExamReportBean {

	private String arch_num;
	private String user_name;
	private String id_num;
	private String sex;
	private String sex_code;
	private String exam_num;
	private int age;
	private Date join_date;
	private Date create_time;
	private String final_doctor;
	private String final_exam_result;
	private String approve_status;
	private String phone;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getArch_num() {
		return arch_num;
	}
	public void setArch_num(String arch_num) {
		this.arch_num = arch_num;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getId_num() {
		return id_num;
	}
	public void setId_num(String id_num) {
		this.id_num = id_num;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public Date getJoin_date() {
		return join_date;
	}
	public void setJoin_date(Date join_date) {
		this.join_date = join_date;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
		switch(sex) {
		case "男" :
			this.sex_code = "M"; break;
		case "女" :
			this.sex_code = "F"; break;
		default:
			this.sex_code = sex;
		}
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFinal_doctor() {
		return final_doctor;
	}
	public void setFinal_doctor(String final_doctor) {
		this.final_doctor = final_doctor;
	}
	public String getFinal_exam_result() {
		return final_exam_result;
	}
	public void setFinal_exam_result(String final_exam_result) {
		this.final_exam_result = final_exam_result;
	}
	public String getSex_code() {
		return sex_code;
	}
	public void setSex_code(String sex_code) {
		this.sex_code = sex_code;
	}
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}
	
	
}
