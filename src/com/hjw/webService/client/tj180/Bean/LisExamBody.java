package com.hjw.webService.client.tj180.Bean;

public class LisExamBody {
	private String patientId;//客户HISID号
	private String startDate;//申请的开始日期 Yyyy-mm-dd
	private String endDate;//申请的结束日期 Yyyy-mm-dd
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
