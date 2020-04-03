package com.hjw.webService.client.yichang.bean.cdr.client.generalReport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registGeneralReportReq")  
@XmlType(propOrder = {})
public class RegistGeneralReportReq {
	
	@XmlElement
	private String localId = "";//localId>
	@XmlElement
	private String clinicType = "04";//04	体检
	@XmlElement
	private String diagnosisNo = "";//diagnosisNo>
	@XmlElement
	private String clinicFlowNo = "";//clinicFlowNo>
	@XmlElement
	private String roomName = "";//roomName>
	@XmlElement
	private String bedNO = "";//bedNO>
	@XmlElement
	private String patientName = "";//patientName>
	@XmlElement
	private String sexCode = "";//sexCode>
	@XmlElement
	private String sexName = "";//sexName>
	@XmlElement
	private String birthDate = "";//birthDate>
	@XmlElement
	private String applyNO = "";//applyNO>
	@XmlElement
	private String applyType = "";//applyType>
	@XmlElement
	private String reportTitle = "";//reportTitle>
	@XmlElement
	private String reportTypeCode = "H00001";//HIP019
	@XmlElement
	private String reportType = "体检报告";//HIP019
	@XmlElement
	private String openTime = "";//openTime>
	@XmlElement
	private String applyDeptNo = "0377";
	@XmlElement
	private String applyDeptName = "体检中心";
	@XmlElement
	private String applicationDoctorName = "";//applicationDoctorName>
	@XmlElement
	private String applicationDoctorNo = "";//applicationDoctorNo>
	@XmlElement
	private String emergencyStatus = "";//emergencyStatus>
	@XmlElement
	private String hospitalArea = "本院";//
	@XmlElement
	private String examinationPurpose = "";//examinationPurpose>
	@XmlElement
	private String reportView = "";//reportView>
	@XmlElement
	private String diagnosePrompt = "";//diagnosePrompt>
	@XmlElement
	private String reportWriterDortor = "";//reportWriterDortor>
	@XmlElement
	private String reportWriterDortorCode = "";//reportWriterDortorCode>
	@XmlElement
	private String reviewDoctorDeptCode = "";//reviewDoctorDeptCode>
	@XmlElement
	private String reviewDoctorDeptName = "";//reviewDoctorDeptName>
	@XmlElement
	private String reportAuditDoctor = "";//reportAuditDoctor>
	@XmlElement
	private String reportAuditDoctorCode = "";//reportAuditDoctorCode>
	@XmlElement
	private String reportDate = "";//reportDate>
	@XmlElement
	private List<ExaminationFile> examinationFileList = new ArrayList<>();
	@XmlElement
	private String pdfBase64 = "";//pdfBase64>
	@XmlElement
	private String xmlDocument = "";//xmlDocument>
	@XmlElement
	private String reportNO = "";//reportNO>
	@XmlElement
	private String patientSource = "";//patientSource>
	@XmlElement
	private String simpleTime = "";//simpleTime>
	@XmlElement
	private String reportContent = "";//reportContent>
	@XmlElement
	private String reportDeptCode = "0377";//reportDeptCode>
	@XmlElement
	private String reportDeptName = "体检中心";//reportDeptName>
	@XmlElement
	private String orgCode = "";//orgCode>
	@XmlElement
	private String orgName = "武威医学科学院";//orgName>
	@XmlElement
	private String age = "";//age>
	@XmlElement
	private String codeExpand = "";//codeExpand>
	@XmlElement
	private String fileType = "0";//0	PDF
	
	public String getLocalId() {
		return localId;
	}
	public String getClinicType() {
		return clinicType;
	}
	public String getDiagnosisNo() {
		return diagnosisNo;
	}
	public String getClinicFlowNo() {
		return clinicFlowNo;
	}
	public String getRoomName() {
		return roomName;
	}
	public String getBedNO() {
		return bedNO;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getSexCode() {
		return sexCode;
	}
	public String getSexName() {
		return sexName;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getApplyNO() {
		return applyNO;
	}
	public String getApplyType() {
		return applyType;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public String getReportTypeCode() {
		return reportTypeCode;
	}
	public String getReportType() {
		return reportType;
	}
	public String getOpenTime() {
		return openTime;
	}
	public String getApplyDeptNo() {
		return applyDeptNo;
	}
	public String getApplyDeptName() {
		return applyDeptName;
	}
	public String getApplicationDoctorName() {
		return applicationDoctorName;
	}
	public String getApplicationDoctorNo() {
		return applicationDoctorNo;
	}
	public String getEmergencyStatus() {
		return emergencyStatus;
	}
	public String getHospitalArea() {
		return hospitalArea;
	}
	public String getExaminationPurpose() {
		return examinationPurpose;
	}
	public String getReportView() {
		return reportView;
	}
	public String getDiagnosePrompt() {
		return diagnosePrompt;
	}
	public String getReportWriterDortor() {
		return reportWriterDortor;
	}
	public String getReportWriterDortorCode() {
		return reportWriterDortorCode;
	}
	public String getReviewDoctorDeptCode() {
		return reviewDoctorDeptCode;
	}
	public String getReviewDoctorDeptName() {
		return reviewDoctorDeptName;
	}
	public String getReportAuditDoctor() {
		return reportAuditDoctor;
	}
	public String getReportAuditDoctorCode() {
		return reportAuditDoctorCode;
	}
	public String getReportDate() {
		return reportDate;
	}
	public String getPdfBase64() {
		return pdfBase64;
	}
	public String getXmlDocument() {
		return xmlDocument;
	}
	public String getReportNO() {
		return reportNO;
	}
	public String getPatientSource() {
		return patientSource;
	}
	public String getSimpleTime() {
		return simpleTime;
	}
	public String getReportContent() {
		return reportContent;
	}
	public String getReportDeptCode() {
		return reportDeptCode;
	}
	public String getReportDeptName() {
		return reportDeptName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public String getAge() {
		return age;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	public void setClinicType(String clinicType) {
		this.clinicType = clinicType;
	}
	public void setDiagnosisNo(String diagnosisNo) {
		this.diagnosisNo = diagnosisNo;
	}
	public void setClinicFlowNo(String clinicFlowNo) {
		this.clinicFlowNo = clinicFlowNo;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public void setBedNO(String bedNO) {
		this.bedNO = bedNO;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setApplyNO(String applyNO) {
		this.applyNO = applyNO;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	public void setReportTypeCode(String reportTypeCode) {
		this.reportTypeCode = reportTypeCode;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public void setApplyDeptNo(String applyDeptNo) {
		this.applyDeptNo = applyDeptNo;
	}
	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	public void setApplicationDoctorName(String applicationDoctorName) {
		this.applicationDoctorName = applicationDoctorName;
	}
	public void setApplicationDoctorNo(String applicationDoctorNo) {
		this.applicationDoctorNo = applicationDoctorNo;
	}
	public void setEmergencyStatus(String emergencyStatus) {
		this.emergencyStatus = emergencyStatus;
	}
	public void setHospitalArea(String hospitalArea) {
		this.hospitalArea = hospitalArea;
	}
	public void setExaminationPurpose(String examinationPurpose) {
		this.examinationPurpose = examinationPurpose;
	}
	public void setReportView(String reportView) {
		this.reportView = reportView;
	}
	public void setDiagnosePrompt(String diagnosePrompt) {
		this.diagnosePrompt = diagnosePrompt;
	}
	public void setReportWriterDortor(String reportWriterDortor) {
		this.reportWriterDortor = reportWriterDortor;
	}
	public void setReportWriterDortorCode(String reportWriterDortorCode) {
		this.reportWriterDortorCode = reportWriterDortorCode;
	}
	public void setReviewDoctorDeptCode(String reviewDoctorDeptCode) {
		this.reviewDoctorDeptCode = reviewDoctorDeptCode;
	}
	public void setReviewDoctorDeptName(String reviewDoctorDeptName) {
		this.reviewDoctorDeptName = reviewDoctorDeptName;
	}
	public void setReportAuditDoctor(String reportAuditDoctor) {
		this.reportAuditDoctor = reportAuditDoctor;
	}
	public void setReportAuditDoctorCode(String reportAuditDoctorCode) {
		this.reportAuditDoctorCode = reportAuditDoctorCode;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public void setPdfBase64(String pdfBase64) {
		this.pdfBase64 = pdfBase64;
	}
	public void setXmlDocument(String xmlDocument) {
		this.xmlDocument = xmlDocument;
	}
	public void setReportNO(String reportNO) {
		this.reportNO = reportNO;
	}
	public void setPatientSource(String patientSource) {
		this.patientSource = patientSource;
	}
	public void setSimpleTime(String simpleTime) {
		this.simpleTime = simpleTime;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public void setReportDeptCode(String reportDeptCode) {
		this.reportDeptCode = reportDeptCode;
	}
	public void setReportDeptName(String reportDeptName) {
		this.reportDeptName = reportDeptName;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
	public List<ExaminationFile> getExaminationFileList() {
		return examinationFileList;
	}
	public void setExaminationFileList(List<ExaminationFile> examinationFileList) {
		this.examinationFileList = examinationFileList;
	}
}
