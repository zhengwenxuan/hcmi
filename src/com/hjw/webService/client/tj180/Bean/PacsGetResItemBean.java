package com.hjw.webService.client.tj180.Bean;

public class PacsGetResItemBean {
	private String examNo="";//	检查申请流水号
	private String description="";//	检查描述
	private String impression="";//	印象(诊断)
	private String abnormalIndicator="";//	是否异常 	0：正常，1：偏低
	private String resultDateTime="";//	结果时间	yyyymmddhhmmss
	private String operator="";//	检查人
	private String checkPerson="";//	审核人
	private String examClass="";
	private String examSubClass="";
	private String reportDateTime="";
	private String examItemName="";//	检查项目名称
	private String examPdfPath="";//	检查结果PDF下载路径

	public String getExamItemName() {
		return examItemName;
	}
	public void setExamItemName(String examItemName) {
		this.examItemName = examItemName;
	}
	public String getExamPdfPath() {
		return examPdfPath;
	}
	public void setExamPdfPath(String examPdfPath) {
		this.examPdfPath = examPdfPath;
	}
	public String getExamNo() {
		return examNo;
	}
	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImpression() {
		return impression;
	}
	public void setImpression(String impression) {
		this.impression = impression;
	}
	public String getAbnormalIndicator() {
		return abnormalIndicator;
	}
	public void setAbnormalIndicator(String abnormalIndicator) {
		this.abnormalIndicator = abnormalIndicator;
	}
	public String getResultDateTime() {
		return resultDateTime;
	}
	public void setResultDateTime(String resultDateTime) {
		this.resultDateTime = resultDateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCheckPerson() {
		return checkPerson;
	}
	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
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
	public String getReportDateTime() {
		return reportDateTime;
	}
	public void setReportDateTime(String reportDateTime) {
		this.reportDateTime = reportDateTime;
	}
}
