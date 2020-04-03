package com.hjw.webService.client.yichang.bean.cdr.server.registLabReportCommonCDA;

import javax.xml.bind.annotation.*;

import com.hjw.webService.client.yichang.bean.cdr.server.DiagnosisBean;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "xmlIn_registLabReportCommonCDA")  
@XmlType(propOrder = {})
public class XmlIn_registLabReportCommonCDA {

	@XmlElement
	private String	documentTypeCode	 = "";//	文档类型编码	
	@XmlElement
	private String	documentTypeName	 = "";//	文档类型名称	
	@XmlElement
	private String	documentTitle	 = "";//	文档标题	
	@XmlElement
	private String	reportNo	 = "";//	报告单号	是
	@XmlElement
	private String	examineApplyNo	 = "";//	检验申请单号	
	@XmlElement
	private String	barNo	 = "";//	条码号	
	@XmlElement
	private String	deptName	 = "";//	所属科室名称	
	@XmlElement
	private String	deptCode	 = "";//	所属科室编码	
	@XmlElement
	private String	inpatientAreaName	 = "";//	病区名称	
	@XmlElement
	private String	patientId	 = "";//	患者ID	是
	@XmlElement
	private String	clinicType	 = "";//	诊疗类型	是
	@XmlElement
	private String	clinicSerialNo	 = "";//	诊疗流水号	是
	@XmlElement
	private String	diagnosisNo	 = "";//	诊疗号	是
	@XmlElement
	private String	patientName	 = "";//	患者姓名	是
	@XmlElement
	private String	sex_code	 = "";//	性别代码	是
	@XmlElement
	private String	sex_name	 = "";//	性别名称	是
	@XmlElement
	private String	age	 = "";//	年龄	是
	@XmlElement
	private String	diagnosis	 = "";//	诊断	
	@XmlElement
	private String	applicationDoctorCode	 = "";//	申请医生编码	
	@XmlElement
	private String	applicationDoctorName	 = "";//	申请医生名称	
	@XmlElement
	private String	specimenNurseCode	 = "";//	采样护士编码	
	@XmlElement
	private String	specimenNurseName	 = "";//	采样护士姓名	
	@XmlElement
	private String	specimenReceiveDate	 = "";//	样本接收时间	
	@XmlElement
	private String	examineApplyDate	 = "";//	检验申请时间	
	@XmlElement
	private String	examineReportDate	 = "";//	报告时间	
	@XmlElement
	private String	specimenCategories	 = "";//	标本类型	
	@XmlElement
	private String	specimenCollectCollectPosition	 = "";//	采集部位	
	@XmlElement
	private String	hisItemid	 = "";//	检验项目编码	
	@XmlElement
	private String	hisItemname	 = "";//	检验项目名称	
	@XmlElement
	private String	inspectorCode	 = "";//	检验者编码	是
	@XmlElement
	private String	inspectorName	 = "";//	检验者姓名	是
	@XmlElement
	private String	reviewerCode	 = "";//	审核者编码	是
	@XmlElement
	private String	reviewerName	 = "";//	审核者姓名	是
	@XmlElement
	private String	acceptanceDate	 = "";//	核收时间	
	@XmlElement
	private String	resultDescription	 = "";//	描述结果	
	@XmlElement
	private java.util.List<ExamResulBean>	examResultList	 = new java.util.ArrayList<ExamResulBean>();//	检验结果list	是
	@XmlElement
	private java.util.List<ExamPicBean>	examPicList	 = new java.util.ArrayList<ExamPicBean>();//	检验图像list	
	@XmlElement
	private String	documentPDFBase64Content	 = "";//	文档pdf base64	
	@XmlElement
	private String	xmlBase64Content	 = "";//	XML文档消息  暂时可以为空	
	@XmlElement
	private String	attachmentType	 = "";//	附件类型/base64加密串对应文件类型	
	@XmlElement
	private String	codeExpand	 = "";//	扩展码，M C 0号	
	@XmlElement
	private String	printStatus	 = "";//	打印状态编码  0 否 未打印  1 是 已打印	
	@XmlElement
	private String	printDateTime	 = "";//	打印时间	
	@XmlElement
	private String	patientClassCode	 = "";//	患者类型代码	
	@XmlElement
	private String	phoneNumbe	 = "";//	电话号码	
	@XmlElement
	private String	sickroomNo	 = "";//	病房号	
	@XmlElement
	private String	bedNo	 = "";//	病床号	
	@XmlElement
	private String	checkApplyDep	 = "";//	检验申请科室	
	@XmlElement
	private String	checkSampleNo	 = "";//	检验标本号	
	@XmlElement
	private String	specimenState	 = "";//	标本状态	
	@XmlElement
	private String	specimenSamplingDate	 = "";//	标本采样日期时间	是
	@XmlElement
	private String	checkClass	 = "";//	检验类别	
	@XmlElement
	private String	inspectExamItemResultCode	 = "";//	检验结果代码	
	@XmlElement
	private String	checkTechnicianSign	 = "";//	检验技师签名	
	@XmlElement
	private String	checkDoctorSign	 = "";//	检验医师签名	
	@XmlElement
	private String	checkReportDep	 = "";//	检验报告科室	
	@XmlElement
	private String	checkReportRemark	 = "";//	检验报告备注	
	@XmlElement
	private String	reportDoctorSign	 = "";//	报告医师签名	
	@XmlElement
	private String	examineDoctorSign	 = "";//	审核医师签名	
	@XmlElement
	private String	checkApplyOrgName	 = "";//	检验申请机构名称	
	@XmlElement
	private String	checkReportOrgName	 = "";//	检验报告机构名称	
	@XmlElement
	private String	eventTypeCode	 = "";//	事件类型编码	
	@XmlElement
	private String	documentSubmitDate	 = "";//	文档提交时间	
	@XmlElement
	private String	createRecordName	 = "";//	建档者姓名	
	@XmlElement
	private String	createRecordDate	 = "";//	建档日期	
	@XmlElement
	private String	medicalOrgCode	 = "";//	医疗组织机构代码	
	@XmlElement
	private String	examResultName	 = "";//	检验结果名称	
	@XmlElement
	private String	diagnosticCode	 = "";//	疾病诊断编码	
	@XmlElement
	private String	diagnosticName	 = "";//	疾病诊断名称	
	@XmlElement
	private String	diagnosisDate	 = "";//	诊断日期	是
	@XmlElement
	private String	diagnosticInstitutionName	 = "";//	诊断机构名称	
	@XmlElement
	private java.util.List<DiagnosisBean>	diseaseDiagnosisList	 = new java.util.ArrayList<DiagnosisBean>();//	疾病诊断list
	
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public String getDocumentTitle() {
		return documentTitle;
	}
	public String getReportNo() {
		return reportNo;
	}
	public String getExamineApplyNo() {
		return examineApplyNo;
	}
	public String getBarNo() {
		return barNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public String getInpatientAreaName() {
		return inpatientAreaName;
	}
	public String getPatientId() {
		return patientId;
	}
	public String getClinicType() {
		return clinicType;
	}
	public String getClinicSerialNo() {
		return clinicSerialNo;
	}
	public String getDiagnosisNo() {
		return diagnosisNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getSex_code() {
		return sex_code;
	}
	public String getSex_name() {
		return sex_name;
	}
	public String getAge() {
		return age;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public String getApplicationDoctorCode() {
		return applicationDoctorCode;
	}
	public String getApplicationDoctorName() {
		return applicationDoctorName;
	}
	public String getSpecimenNurseCode() {
		return specimenNurseCode;
	}
	public String getSpecimenNurseName() {
		return specimenNurseName;
	}
	public String getSpecimenReceiveDate() {
		return specimenReceiveDate;
	}
	public String getExamineApplyDate() {
		return examineApplyDate;
	}
	public String getExamineReportDate() {
		return examineReportDate;
	}
	public String getSpecimenCategories() {
		return specimenCategories;
	}
	public String getSpecimenCollectCollectPosition() {
		return specimenCollectCollectPosition;
	}
	public String getHisItemid() {
		return hisItemid;
	}
	public String getHisItemname() {
		return hisItemname;
	}
	public String getInspectorCode() {
		return inspectorCode;
	}
	public String getInspectorName() {
		return inspectorName;
	}
	public String getReviewerCode() {
		return reviewerCode;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public String getAcceptanceDate() {
		return acceptanceDate;
	}
	public String getResultDescription() {
		return resultDescription;
	}
	public java.util.List<ExamResulBean> getExamResultList() {
		return examResultList;
	}
	public java.util.List<ExamPicBean> getExamPicList() {
		return examPicList;
	}
	public String getDocumentPDFBase64Content() {
		return documentPDFBase64Content;
	}
	public String getXmlBase64Content() {
		return xmlBase64Content;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public String getPrintStatus() {
		return printStatus;
	}
	public String getPrintDateTime() {
		return printDateTime;
	}
	public String getPatientClassCode() {
		return patientClassCode;
	}
	public String getPhoneNumbe() {
		return phoneNumbe;
	}
	public String getSickroomNo() {
		return sickroomNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public String getCheckApplyDep() {
		return checkApplyDep;
	}
	public String getCheckSampleNo() {
		return checkSampleNo;
	}
	public String getSpecimenState() {
		return specimenState;
	}
	public String getSpecimenSamplingDate() {
		return specimenSamplingDate;
	}
	public String getCheckClass() {
		return checkClass;
	}
	public String getInspectExamItemResultCode() {
		return inspectExamItemResultCode;
	}
	public String getCheckTechnicianSign() {
		return checkTechnicianSign;
	}
	public String getCheckDoctorSign() {
		return checkDoctorSign;
	}
	public String getCheckReportDep() {
		return checkReportDep;
	}
	public String getCheckReportRemark() {
		return checkReportRemark;
	}
	public String getReportDoctorSign() {
		return reportDoctorSign;
	}
	public String getExamineDoctorSign() {
		return examineDoctorSign;
	}
	public String getCheckApplyOrgName() {
		return checkApplyOrgName;
	}
	public String getCheckReportOrgName() {
		return checkReportOrgName;
	}
	public String getEventTypeCode() {
		return eventTypeCode;
	}
	public String getDocumentSubmitDate() {
		return documentSubmitDate;
	}
	public String getCreateRecordName() {
		return createRecordName;
	}
	public String getCreateRecordDate() {
		return createRecordDate;
	}
	public String getMedicalOrgCode() {
		return medicalOrgCode;
	}
	public String getExamResultName() {
		return examResultName;
	}
	public String getDiagnosticCode() {
		return diagnosticCode;
	}
	public String getDiagnosticName() {
		return diagnosticName;
	}
	public String getDiagnosisDate() {
		return diagnosisDate;
	}
	public String getDiagnosticInstitutionName() {
		return diagnosticInstitutionName;
	}
	public java.util.List<DiagnosisBean> getDiseaseDiagnosisList() {
		return diseaseDiagnosisList;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public void setExamineApplyNo(String examineApplyNo) {
		this.examineApplyNo = examineApplyNo;
	}
	public void setBarNo(String barNo) {
		this.barNo = barNo;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public void setInpatientAreaName(String inpatientAreaName) {
		this.inpatientAreaName = inpatientAreaName;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public void setClinicType(String clinicType) {
		this.clinicType = clinicType;
	}
	public void setClinicSerialNo(String clinicSerialNo) {
		this.clinicSerialNo = clinicSerialNo;
	}
	public void setDiagnosisNo(String diagnosisNo) {
		this.diagnosisNo = diagnosisNo;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setSex_code(String sex_code) {
		this.sex_code = sex_code;
	}
	public void setSex_name(String sex_name) {
		this.sex_name = sex_name;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public void setApplicationDoctorCode(String applicationDoctorCode) {
		this.applicationDoctorCode = applicationDoctorCode;
	}
	public void setApplicationDoctorName(String applicationDoctorName) {
		this.applicationDoctorName = applicationDoctorName;
	}
	public void setSpecimenNurseCode(String specimenNurseCode) {
		this.specimenNurseCode = specimenNurseCode;
	}
	public void setSpecimenNurseName(String specimenNurseName) {
		this.specimenNurseName = specimenNurseName;
	}
	public void setSpecimenReceiveDate(String specimenReceiveDate) {
		this.specimenReceiveDate = specimenReceiveDate;
	}
	public void setExamineApplyDate(String examineApplyDate) {
		this.examineApplyDate = examineApplyDate;
	}
	public void setExamineReportDate(String examineReportDate) {
		this.examineReportDate = examineReportDate;
	}
	public void setSpecimenCategories(String specimenCategories) {
		this.specimenCategories = specimenCategories;
	}
	public void setSpecimenCollectCollectPosition(String specimenCollectCollectPosition) {
		this.specimenCollectCollectPosition = specimenCollectCollectPosition;
	}
	public void setHisItemid(String hisItemid) {
		this.hisItemid = hisItemid;
	}
	public void setHisItemname(String hisItemname) {
		this.hisItemname = hisItemname;
	}
	public void setInspectorCode(String inspectorCode) {
		this.inspectorCode = inspectorCode;
	}
	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}
	public void setReviewerCode(String reviewerCode) {
		this.reviewerCode = reviewerCode;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
	public void setAcceptanceDate(String acceptanceDate) {
		this.acceptanceDate = acceptanceDate;
	}
	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	public void setExamResultList(java.util.List<ExamResulBean> examResultList) {
		this.examResultList = examResultList;
	}
	public void setExamPicList(java.util.List<ExamPicBean> examPicList) {
		this.examPicList = examPicList;
	}
	public void setDocumentPDFBase64Content(String documentPDFBase64Content) {
		this.documentPDFBase64Content = documentPDFBase64Content;
	}
	public void setXmlBase64Content(String xmlBase64Content) {
		this.xmlBase64Content = xmlBase64Content;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}
	public void setPrintDateTime(String printDateTime) {
		this.printDateTime = printDateTime;
	}
	public void setPatientClassCode(String patientClassCode) {
		this.patientClassCode = patientClassCode;
	}
	public void setPhoneNumbe(String phoneNumbe) {
		this.phoneNumbe = phoneNumbe;
	}
	public void setSickroomNo(String sickroomNo) {
		this.sickroomNo = sickroomNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public void setCheckApplyDep(String checkApplyDep) {
		this.checkApplyDep = checkApplyDep;
	}
	public void setCheckSampleNo(String checkSampleNo) {
		this.checkSampleNo = checkSampleNo;
	}
	public void setSpecimenState(String specimenState) {
		this.specimenState = specimenState;
	}
	public void setSpecimenSamplingDate(String specimenSamplingDate) {
		this.specimenSamplingDate = specimenSamplingDate;
	}
	public void setCheckClass(String checkClass) {
		this.checkClass = checkClass;
	}
	public void setInspectExamItemResultCode(String inspectExamItemResultCode) {
		this.inspectExamItemResultCode = inspectExamItemResultCode;
	}
	public void setCheckTechnicianSign(String checkTechnicianSign) {
		this.checkTechnicianSign = checkTechnicianSign;
	}
	public void setCheckDoctorSign(String checkDoctorSign) {
		this.checkDoctorSign = checkDoctorSign;
	}
	public void setCheckReportDep(String checkReportDep) {
		this.checkReportDep = checkReportDep;
	}
	public void setCheckReportRemark(String checkReportRemark) {
		this.checkReportRemark = checkReportRemark;
	}
	public void setReportDoctorSign(String reportDoctorSign) {
		this.reportDoctorSign = reportDoctorSign;
	}
	public void setExamineDoctorSign(String examineDoctorSign) {
		this.examineDoctorSign = examineDoctorSign;
	}
	public void setCheckApplyOrgName(String checkApplyOrgName) {
		this.checkApplyOrgName = checkApplyOrgName;
	}
	public void setCheckReportOrgName(String checkReportOrgName) {
		this.checkReportOrgName = checkReportOrgName;
	}
	public void setEventTypeCode(String eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}
	public void setDocumentSubmitDate(String documentSubmitDate) {
		this.documentSubmitDate = documentSubmitDate;
	}
	public void setCreateRecordName(String createRecordName) {
		this.createRecordName = createRecordName;
	}
	public void setCreateRecordDate(String createRecordDate) {
		this.createRecordDate = createRecordDate;
	}
	public void setMedicalOrgCode(String medicalOrgCode) {
		this.medicalOrgCode = medicalOrgCode;
	}
	public void setExamResultName(String examResultName) {
		this.examResultName = examResultName;
	}
	public void setDiagnosticCode(String diagnosticCode) {
		this.diagnosticCode = diagnosticCode;
	}
	public void setDiagnosticName(String diagnosticName) {
		this.diagnosticName = diagnosticName;
	}
	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}
	public void setDiagnosticInstitutionName(String diagnosticInstitutionName) {
		this.diagnosticInstitutionName = diagnosticInstitutionName;
	}
	public void setDiseaseDiagnosisList(java.util.List<DiagnosisBean> diseaseDiagnosisList) {
		this.diseaseDiagnosisList = diseaseDiagnosisList;
	}
}
