package com.hjw.webService.client.donghua.bean.pacs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ReturnReport")  
@XmlType(propOrder = {})
public class ReturnReport {

	@XmlElement
	private String OrdRowID = "";//医嘱号
	@XmlElement
	private String StudyNo  = "";//检查号（第三方号）
	@XmlElement
	private String GetDocCode = "";//取材医生代码
	@XmlElement
	private String GetDoc = "";//取材医生
	@XmlElement
	private String ReportStatusCode = "";//报告状态代码 默认为“V”
	@XmlElement
	private String ReportStatus  = "";//报告状态描述
	@XmlElement
	private String UnsendCause  = "";//未发报告原因
	@XmlElement
	private String ReportDocCode = "";//报告医生代码
	@XmlElement
	private String ReportDoc  = "";//报告医生
	@XmlElement
	private String AuditDocCode = "";//审核医生代码
	@XmlElement
	private String AuditDoc  = "";//审核医生
	@XmlElement
	private String ReportDate  = "";//报告日期 YYYY-MM-DD
	@XmlElement
	private String AuditDate  = "";//审核日期 YYYY-MM-DD
	@XmlElement
	private String ReportTime  = "";//报告时间 HH:MM:SS
	@XmlElement
	private String AuditTime  = "";//审核时间 HH:MM:SS
	@XmlElement
	private String Memo  = "";//备注
	@XmlElement
	private String ImageFile  = "";//图像的路径（多幅图使用@分割）
	@XmlElement
	private String HisArchiveTag  = "";//HIS归档标记
	@XmlElement
	private String EyeSee  = "";//肉眼所见(用于病理取材)
	@XmlElement
	private String ExamSee  = "";//检查所见
	@XmlElement
	private String Diagnose  = "";//诊断结果
	
	public String getOrdRowID() {
		return OrdRowID;
	}
	public String getStudyNo() {
		return StudyNo;
	}
	public String getGetDocCode() {
		return GetDocCode;
	}
	public String getGetDoc() {
		return GetDoc;
	}
	public String getReportStatusCode() {
		return ReportStatusCode;
	}
	public String getReportStatus() {
		return ReportStatus;
	}
	public String getUnsendCause() {
		return UnsendCause;
	}
	public String getReportDocCode() {
		return ReportDocCode;
	}
	public String getReportDoc() {
		return ReportDoc;
	}
	public String getAuditDocCode() {
		return AuditDocCode;
	}
	public String getAuditDoc() {
		return AuditDoc;
	}
	public String getReportDate() {
		return ReportDate;
	}
	public String getAuditDate() {
		return AuditDate;
	}
	public String getReportTime() {
		return ReportTime;
	}
	public String getAuditTime() {
		return AuditTime;
	}
	public String getMemo() {
		return Memo;
	}
	public String getImageFile() {
		return ImageFile;
	}
	public String getHisArchiveTag() {
		return HisArchiveTag;
	}
	public String getEyeSee() {
		return EyeSee;
	}
	public String getExamSee() {
		return ExamSee;
	}
	public String getDiagnose() {
		return Diagnose;
	}
	public void setOrdRowID(String ordRowID) {
		OrdRowID = ordRowID;
	}
	public void setStudyNo(String studyNo) {
		StudyNo = studyNo;
	}
	public void setGetDocCode(String getDocCode) {
		GetDocCode = getDocCode;
	}
	public void setGetDoc(String getDoc) {
		GetDoc = getDoc;
	}
	public void setReportStatusCode(String reportStatusCode) {
		ReportStatusCode = reportStatusCode;
	}
	public void setReportStatus(String reportStatus) {
		ReportStatus = reportStatus;
	}
	public void setUnsendCause(String unsendCause) {
		UnsendCause = unsendCause;
	}
	public void setReportDocCode(String reportDocCode) {
		ReportDocCode = reportDocCode;
	}
	public void setReportDoc(String reportDoc) {
		ReportDoc = reportDoc;
	}
	public void setAuditDocCode(String auditDocCode) {
		AuditDocCode = auditDocCode;
	}
	public void setAuditDoc(String auditDoc) {
		AuditDoc = auditDoc;
	}
	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}
	public void setAuditDate(String auditDate) {
		AuditDate = auditDate;
	}
	public void setReportTime(String reportTime) {
		ReportTime = reportTime;
	}
	public void setAuditTime(String auditTime) {
		AuditTime = auditTime;
	}
	public void setMemo(String memo) {
		Memo = memo;
	}
	public void setImageFile(String imageFile) {
		ImageFile = imageFile;
	}
	public void setHisArchiveTag(String hisArchiveTag) {
		HisArchiveTag = hisArchiveTag;
	}
	public void setEyeSee(String eyeSee) {
		EyeSee = eyeSee;
	}
	public void setExamSee(String examSee) {
		ExamSee = examSee;
	}
	public void setDiagnose(String diagnose) {
		Diagnose = diagnose;
	}
}
