package com.hjw.webService.client.bdyx.bean.pacs.res;

public class ReportDoctor {

	private String reportDate = "";//报告日期
	private String reportDoctor = "";//报告医生编码
	private String reportDoctorName = "";//报告医生名称
	
	public String getReportDate() {
		return reportDate;
	}
	public String getReportDoctor() {
		return reportDoctor;
	}
	public String getReportDoctorName() {
		return reportDoctorName;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public void setReportDoctor(String reportDoctor) {
		this.reportDoctor = reportDoctor;
	}
	public void setReportDoctorName(String reportDoctorName) {
		this.reportDoctorName = reportDoctorName;
	}
}
