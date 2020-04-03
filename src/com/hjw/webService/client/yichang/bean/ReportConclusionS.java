package com.hjw.webService.client.yichang.bean;

public class ReportConclusionS {

	private String RecordNo;//体检系统中的唯一编号
	private String SectionName;//科室名称
	private String Conclusion;//科室小结
	private String CheckUser;//检查人
	private String CheckTime;//检查时间
	public String getRecordNo() {
		return RecordNo;
	}
	public void setRecordNo(String recordNo) {
		RecordNo = recordNo;
	}
	public String getSectionName() {
		return SectionName;
	}
	public void setSectionName(String sectionName) {
		SectionName = sectionName;
	}
	public String getConclusion() {
		return Conclusion;
	}
	public void setConclusion(String conclusion) {
		Conclusion = conclusion;
	}
	public String getCheckUser() {
		return CheckUser;
	}
	public void setCheckUser(String checkUser) {
		CheckUser = checkUser;
	}
	public String getCheckTime() {
		return CheckTime;
	}
	public void setCheckTime(String checkTime) {
		CheckTime = checkTime;
	}
	
	
}
