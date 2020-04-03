package com.hjw.webService.client.jsjg.bean.in;

public class InParam_RecReport {

	private String ReportNo = "";//报告单号
	private String OperatorCode = "";//操作员工号
	private String OperatorName = "";//操作员姓名
	private String OperatorTime = "";//操作时间
	
	public String getReportNo() {
		return ReportNo;
	}
	public String getOperatorCode() {
		return OperatorCode;
	}
	public String getOperatorName() {
		return OperatorName;
	}
	public String getOperatorTime() {
		return OperatorTime;
	}
	public void setReportNo(String reportNo) {
		ReportNo = reportNo;
	}
	public void setOperatorCode(String operatorCode) {
		OperatorCode = operatorCode;
	}
	public void setOperatorName(String operatorName) {
		OperatorName = operatorName;
	}
	public void setOperatorTime(String operatorTime) {
		OperatorTime = operatorTime;
	}

}
