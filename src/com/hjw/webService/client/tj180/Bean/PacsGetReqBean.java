package com.hjw.webService.client.tj180.Bean;

public class PacsGetReqBean{
	private String examNo="";
	private String patientId="";
	private String examineDate="";
	private String examClass="";
	private String examSubClass="";
	public String getExamNo() {
		return examNo;
	}
	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getExamineDate() {
		return examineDate;
	}
	public void setExamineDate(String examineDate) {
		this.examineDate = examineDate;
	}
	public String getExamClass() {
		return examClass;
	}
	public void setExamClass(String examClass) {
		this.examClass = examClass;
	}
	public String getExamSubClass() {
		return examSubClass;
	}
	public void setExamSubClass(String examSubClass) {
		this.examSubClass = examSubClass;
	}
    
}
