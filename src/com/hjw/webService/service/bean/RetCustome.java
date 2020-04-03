package com.hjw.webService.service.bean;

public class RetCustome {
	private String custome_id="";//患者id	
	private String coustom_jzh="";//就诊号	
	private String exam_num="";
	private String doctor_name_bg="";//报告医生名称
	private String doctor_time_bg="";//报告时间	
	private String doctor_name_sh="";//审核医生姓名
	private String doctor_time_sh="";//医生审核时间			
	
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getDoctor_name_bg() {
		return doctor_name_bg;
	}
	public void setDoctor_name_bg(String doctor_name_bg) {
		this.doctor_name_bg = doctor_name_bg;
	}
	public String getDoctor_time_bg() {
		return doctor_time_bg;
	}
	public void setDoctor_time_bg(String doctor_time_bg) {
		this.doctor_time_bg = doctor_time_bg;
	}
	public String getDoctor_name_sh() {
		return doctor_name_sh;
	}
	public void setDoctor_name_sh(String doctor_name_sh) {
		this.doctor_name_sh = doctor_name_sh;
	}
	public String getDoctor_time_sh() {
		return doctor_time_sh;
	}
	public void setDoctor_time_sh(String doctor_time_sh) {
		this.doctor_time_sh = doctor_time_sh;
	}
	public String getCustome_id() {
		return custome_id;
	}
	public void setCustome_id(String custome_id) {
		this.custome_id = custome_id;
	}
	public String getCoustom_jzh() {
		return coustom_jzh;
	}
	public void setCoustom_jzh(String coustom_jzh) {
		this.coustom_jzh = coustom_jzh;
	}

}
