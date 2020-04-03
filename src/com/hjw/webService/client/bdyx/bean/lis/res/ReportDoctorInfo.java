package com.hjw.webService.client.bdyx.bean.lis.res;

public class ReportDoctorInfo {

	private String reportDate = "";//报告日期
	private String reporterId = "";//报告医生编码
	private String reporterName = "";//报告医生名称
	
	public String getReportDate() {
		return reportDate;
	}
	public String getReporterId() {
		return reporterId;
	}
	public String getReporterName() {
		return reporterName;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}
}
