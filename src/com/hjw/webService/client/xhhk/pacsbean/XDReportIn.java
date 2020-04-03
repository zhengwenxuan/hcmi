package com.hjw.webService.client.xhhk.pacsbean;

public class XDReportIn {

	private String FKey = "";//体检号
	private String CheckDoc = "";//检查医生
	private String CheckProjectCode = "";//检验项目编号
	private String CheckTime = "";//检查日期
	private String ReportDoc = "";//报告医生
	private String ReportDes = "";//报告描述
	private String ReportResult = "";//报告结论
	private String ReportID = "";//报告ID RepGUID
	private String Image = "";//图片 采用二进制（备用字段）
	private String ImagePath = "";//图片路径
	
	public String getFKey() {
		return FKey;
	}
	public String getCheckDoc() {
		return CheckDoc;
	}
	public String getCheckTime() {
		return CheckTime;
	}
	public String getReportDoc() {
		return ReportDoc;
	}
	public String getReportResult() {
		return ReportResult;
	}
	public String getReportID() {
		return ReportID;
	}
	public String getImage() {
		return Image;
	}
	public String getImagePath() {
		return ImagePath;
	}
	public void setFKey(String fKey) {
		FKey = fKey;
	}
	public void setCheckDoc(String checkDoc) {
		CheckDoc = checkDoc;
	}
	public void setCheckTime(String checkTime) {
		CheckTime = checkTime;
	}
	public void setReportDoc(String reportDoc) {
		ReportDoc = reportDoc;
	}
	public void setReportResult(String reportResult) {
		ReportResult = reportResult;
	}
	public void setReportID(String reportID) {
		ReportID = reportID;
	}
	public void setImage(String image) {
		Image = image;
	}
	public void setImagePath(String imagePath) {
		ImagePath = imagePath;
	}
	public String getCheckProjectCode() {
		return CheckProjectCode;
	}
	public String getReportDes() {
		return ReportDes;
	}
	public void setCheckProjectCode(String checkProjectCode) {
		CheckProjectCode = checkProjectCode;
	}
	public void setReportDes(String reportDes) {
		ReportDes = reportDes;
	}
}
