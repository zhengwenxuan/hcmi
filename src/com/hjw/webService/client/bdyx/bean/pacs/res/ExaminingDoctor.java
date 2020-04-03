package com.hjw.webService.client.bdyx.bean.pacs.res;

public class ExaminingDoctor {

	private String examinationDate = "";//检查日期
	private String examiningDoctor = "";//检查医生编码
	private String examiningDoctorName = "";//检查医生名称
	private String examinationDept = "";//检查科室编码
	private String examinationDeptName = "";//检查科室名称
	public String getExaminationDate() {
		return examinationDate;
	}
	public String getExaminingDoctor() {
		return examiningDoctor;
	}
	public String getExaminingDoctorName() {
		return examiningDoctorName;
	}
	public String getExaminationDept() {
		return examinationDept;
	}
	public String getExaminationDeptName() {
		return examinationDeptName;
	}
	public void setExaminationDate(String examinationDate) {
		this.examinationDate = examinationDate;
	}
	public void setExaminingDoctor(String examiningDoctor) {
		this.examiningDoctor = examiningDoctor;
	}
	public void setExaminingDoctorName(String examiningDoctorName) {
		this.examiningDoctorName = examiningDoctorName;
	}
	public void setExaminationDept(String examinationDept) {
		this.examinationDept = examinationDept;
	}
	public void setExaminationDeptName(String examinationDeptName) {
		this.examinationDeptName = examinationDeptName;
	}
}
