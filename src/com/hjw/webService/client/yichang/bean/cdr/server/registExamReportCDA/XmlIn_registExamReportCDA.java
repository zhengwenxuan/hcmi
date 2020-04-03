package com.hjw.webService.client.yichang.bean.cdr.server.registExamReportCDA;

import javax.xml.bind.annotation.*;

import com.hjw.webService.client.yichang.bean.cdr.server.DiagnosisBean;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "xmlIn_registExamReportCDA")  
@XmlType(propOrder = {})
public class XmlIn_registExamReportCDA {

	@XmlElement
	private String	localId	 = "";//	本地Id	是	
	@XmlElement
	private String	clinicType	 = "";//	诊疗类型	是	
	@XmlElement
	private String	diagnosisNo	 = "";//	诊疗号	是	
	@XmlElement
	private String	clinicFlowNo	 = "";//	诊疗流水号	是	
	@XmlElement
	private String	roomName	 = "";//	病房名称		
	@XmlElement
	private String	bedNO	 = "";//	床号		
	@XmlElement
	private String	patientName	 = "";//	患者姓名	是	
	@XmlElement
	private String	sexCode	 = "";//	患者性别编码	是	GB/T 2261.1-2003
	@XmlElement
	private String	sexName	 = "";//	患者性别	是	
	@XmlElement
	private String	birthDate	 = "";//	出生日期	是	
	@XmlElement
	private String	applyNO	 = "";//	申请单号		
	@XmlElement
	private String	applyType	 = "";//	申请单类型		HIP059
	@XmlElement
	private String	reportTitle	 = "";//	报告名称		
	@XmlElement
	private String	reportTypeCode	 = "";//	文档类型编码	是	HIP019
	@XmlElement
	private String	reportType	 = "";//	文档类型名称	是	
	@XmlElement
	private String	openTime	 = "";//	开立时间		
	@XmlElement
	private String	applyDeptNo	 = "";//	申请科室编号		
	@XmlElement
	private String	applyDeptName	 = "";//	申请科室名称		
	@XmlElement
	private String	applicationDoctorName	 = "";//	申请医生名称		
	@XmlElement
	private String	applicationDoctorNo	 = "";//	申请医生编号		
	@XmlElement
	private String	emergencyStatus	 = "";//	紧急状态		HIP027
	@XmlElement
	private String	hospitalArea	 = "";//	院区		
	@XmlElement
	private String	examinationPurpose	 = "";//	检查目的		
	@XmlElement
	private String	reportView	 = "";//	所见		
	@XmlElement
	private String	diagnosePrompt	 = "";//	诊断及提示		
	@XmlElement
	private String	reportWriterDortor	 = "";//	报告书写者	是	
	@XmlElement
	private String	reportWriterDortorCode	 = "";//	报告书写者编码		
	@XmlElement
	private String	reportAuditDoctor	 = "";//	报告审核者		
	@XmlElement
	private String	reportAuditDoctorCode	 = "";//	报告审核者编码		
	@XmlElement
	private String	reportDate	 = "";//	报告日期	是	
	@XmlElement
	private java.util.List<ReportFileBean>	examinationFileList	 = new java.util.ArrayList<ReportFileBean>();//	体检用文件		
	@XmlElement
	private String	attachmentType	 = "";//	附件类型/base64加密串对应文件类型		
	@XmlElement
	private String	pdfBase64	 = "";//	pdf文件base64加密串		
	@XmlElement
	private String	xmlDocument	 = "";//	文档xml		
	@XmlElement
	private String	reportNO	 = "";//	报告编号		
	@XmlElement
	private String	patientSource	 = "";//	患者来源	是	HIP056
	@XmlElement
	private String	simpleTime	 = "";//	采样时间		
	@XmlElement
	private String	reportContent	 = "";//	报告内容		
	@XmlElement
	private String	reportDeptCode	 = "";//	报告科室编码	是	
	@XmlElement
	private String	reportDeptName	 = "";//	报告科室名称	是	
	@XmlElement
	private String	orgCode	 = "";//	医疗机构编码		
	@XmlElement
	private String	orgName	 = "";//	医疗机构名称		
	@XmlElement
	private String	age	 = "";//	年龄		
	@XmlElement
	private String	eventTypeCode	 = "";//	事件类型编码		
	@XmlElement
	private String	codeExpand	 = "";//	扩展码，M C 0号		
	@XmlElement
	private String	imageNo	 = "";//	影像号		
	@XmlElement
	private String	examSerialNo	 = "";//	检查流水号		
	@XmlElement
	private String	dicomImgSerialNo	 = "";//	DICOM图像流水号		
	@XmlElement
	private String	phoneNumbe	 = "";//	电话号码		
	@XmlElement
	private String	deptName	 = "";//	科室名称		
	@XmlElement
	private String	sickroomNo	 = "";//	病房号		
	@XmlElement
	private String	complaint	 = "";//	主诉		
	@XmlElement
	private String	symptomStartDate	 = "";//	症状开始日期时间	是	
	@XmlElement
	private String	symptomStopDate	 = "";//	症状停止日期时间	是	
	@XmlElement
	private String	symptomDescription	 = "";//	症状描述		
	@XmlElement
	private String	specialInspectionSign	 = "";//	特殊检查标志		
	@XmlElement
	private String	operationCode	 = "";//	操作编码		
	@XmlElement
	private String	operationName	 = "";//	操作名称		
	@XmlElement
	private String	intervenorName	 = "";//	介入物名称		
	@XmlElement
	private String	operatingDescription	 = "";//	操作方法描述		
	@XmlElement
	private String	operationNum	 = "";//	操作次数		
	@XmlElement
	private String	operationDate	 = "";//	操作日期时间	是	
	@XmlElement
	private String	anesthesiaCode	 = "";//	麻醉方法代码		
	@XmlElement
	private String	anesthesiaResult	 = "";//	麻醉观察结果		
	@XmlElement
	private String	anesthesiaSignCode	 = "";//	麻醉中西医标识代码		
	@XmlElement
	private String	anesthesiologistSign	 = "";//	麻醉医师签名		
	@XmlElement
	private String	specimenClass	 = "";//	标本类别		
	@XmlElement
	private String	checkSpecimenNumber	 = "";//	检查标本号		
	@XmlElement
	private String	specimenState	 = "";//	标本状态		
	@XmlElement
	private String	specimenFixativeName	 = "";//	标本固定液名称		
	@XmlElement
	private String	specimenSamplingDate	 = "";//	标本采样日期时间	是	
	@XmlElement
	private String	receivingSpecimenDate	 = "";//	接收标本日期时间	是	
	@XmlElement
	private String	examMethodName	 = "";//	检查方法名称		
	@XmlElement
	private String	inspectionCategory	 = "";//	检查类别		
	@XmlElement
	private String	itemCode	 = "";//	检查项目代码		
	@XmlElement
	private String	checkResultCode	 = "";//	检查结果代码		
	@XmlElement
	private String	checkQuantitativeResults	 = "";//	检查定量结果		
	@XmlElement
	private String	checkResultsUnit	 = "";//	检查定量结果计量单位		
	@XmlElement
	private String	checkTechnicianSign	 = "";//	检查技师签名		
	@XmlElement
	private String	checkDoctorSign	 = "";//	检查医师签名		
	@XmlElement
	private String	examinationDate	 = "";//	检查日期	是	
	@XmlElement
	private String	checkReportNumber	 = "";//	检查报告单编号		
	@XmlElement
	private String	checkReportOrgName	 = "";//	检查报告机构名称		
	@XmlElement
	private String	checkResultsSubjectiveCue	 = "";//	检查报告结果-主观提示		
	@XmlElement
	private String	reportMemo	 = "";//	检查报告备注		
	@XmlElement
	private String	operationAreaCode	 = "";//	操作部位编码		
	@XmlElement
	private String	wardName	 = "";//	病区名称		
	@XmlElement
	private String	documentSubmitDate	 = "";//	文档提交时间		
	@XmlElement
	private String	createRecordName	 = "";//	建档者姓名		
	@XmlElement
	private String	createRecordDate	 = "";//	建档日期		
	@XmlElement
	private String	medicalOrgCode	 = "";//	医疗组织机构代码		
	@XmlElement
	private String	checkResult	 = "";//	检查结果		
	@XmlElement
	private String	operationAreaName	 = "";//	操作部位名称		
	@XmlElement
	private String	anesthesiaName	 = "";//	麻醉方法名称		
	@XmlElement
	private String	anesthesiaSignName	 = "";//	麻醉中西医标识名称		
	@XmlElement
	private String	diagnosisProcessDescription	 = "";//	诊疗过程描述		
	@XmlElement
	private String	diagnosticCode	 = "";//	疾病诊断编码		
	@XmlElement
	private String	diagnosticName	 = "";//	疾病诊断名称		
	@XmlElement
	private String	diagnosticInstitutionName	 = "";//	诊断机构名称		
	@XmlElement
	private String	diagnosisDate	 = "";//	诊断日期	是	
	@XmlElement
	private java.util.List<DiagnosisBean>	diseaseDiagnosisList	 = new java.util.ArrayList<DiagnosisBean>();//	疾病诊断list		
	
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
	public String getReportAuditDoctor() {
		return reportAuditDoctor;
	}
	public String getReportAuditDoctorCode() {
		return reportAuditDoctorCode;
	}
	public String getReportDate() {
		return reportDate;
	}
	public java.util.List<ReportFileBean> getExaminationFileList() {
		return examinationFileList;
	}
	public String getAttachmentType() {
		return attachmentType;
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
	public String getEventTypeCode() {
		return eventTypeCode;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public String getImageNo() {
		return imageNo;
	}
	public String getExamSerialNo() {
		return examSerialNo;
	}
	public String getDicomImgSerialNo() {
		return dicomImgSerialNo;
	}
	public String getPhoneNumbe() {
		return phoneNumbe;
	}
	public String getDeptName() {
		return deptName;
	}
	public String getSickroomNo() {
		return sickroomNo;
	}
	public String getComplaint() {
		return complaint;
	}
	public String getSymptomStartDate() {
		return symptomStartDate;
	}
	public String getSymptomStopDate() {
		return symptomStopDate;
	}
	public String getSymptomDescription() {
		return symptomDescription;
	}
	public String getSpecialInspectionSign() {
		return specialInspectionSign;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public String getOperationName() {
		return operationName;
	}
	public String getIntervenorName() {
		return intervenorName;
	}
	public String getOperatingDescription() {
		return operatingDescription;
	}
	public String getOperationNum() {
		return operationNum;
	}
	public String getOperationDate() {
		return operationDate;
	}
	public String getAnesthesiaCode() {
		return anesthesiaCode;
	}
	public String getAnesthesiaResult() {
		return anesthesiaResult;
	}
	public String getAnesthesiaSignCode() {
		return anesthesiaSignCode;
	}
	public String getAnesthesiologistSign() {
		return anesthesiologistSign;
	}
	public String getSpecimenClass() {
		return specimenClass;
	}
	public String getCheckSpecimenNumber() {
		return checkSpecimenNumber;
	}
	public String getSpecimenState() {
		return specimenState;
	}
	public String getSpecimenFixativeName() {
		return specimenFixativeName;
	}
	public String getSpecimenSamplingDate() {
		return specimenSamplingDate;
	}
	public String getReceivingSpecimenDate() {
		return receivingSpecimenDate;
	}
	public String getExamMethodName() {
		return examMethodName;
	}
	public String getInspectionCategory() {
		return inspectionCategory;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getCheckResultCode() {
		return checkResultCode;
	}
	public String getCheckQuantitativeResults() {
		return checkQuantitativeResults;
	}
	public String getCheckResultsUnit() {
		return checkResultsUnit;
	}
	public String getCheckTechnicianSign() {
		return checkTechnicianSign;
	}
	public String getCheckDoctorSign() {
		return checkDoctorSign;
	}
	public String getExaminationDate() {
		return examinationDate;
	}
	public String getCheckReportNumber() {
		return checkReportNumber;
	}
	public String getCheckReportOrgName() {
		return checkReportOrgName;
	}
	public String getCheckResultsSubjectiveCue() {
		return checkResultsSubjectiveCue;
	}
	public String getReportMemo() {
		return reportMemo;
	}
	public String getOperationAreaCode() {
		return operationAreaCode;
	}
	public String getWardName() {
		return wardName;
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
	public String getCheckResult() {
		return checkResult;
	}
	public String getOperationAreaName() {
		return operationAreaName;
	}
	public String getAnesthesiaName() {
		return anesthesiaName;
	}
	public String getAnesthesiaSignName() {
		return anesthesiaSignName;
	}
	public String getDiagnosisProcessDescription() {
		return diagnosisProcessDescription;
	}
	public String getDiagnosticCode() {
		return diagnosticCode;
	}
	public String getDiagnosticName() {
		return diagnosticName;
	}
	public String getDiagnosticInstitutionName() {
		return diagnosticInstitutionName;
	}
	public String getDiagnosisDate() {
		return diagnosisDate;
	}
	public java.util.List<DiagnosisBean> getDiseaseDiagnosisList() {
		return diseaseDiagnosisList;
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
	public void setReportAuditDoctor(String reportAuditDoctor) {
		this.reportAuditDoctor = reportAuditDoctor;
	}
	public void setReportAuditDoctorCode(String reportAuditDoctorCode) {
		this.reportAuditDoctorCode = reportAuditDoctorCode;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public void setExaminationFileList(java.util.List<ReportFileBean> examinationFileList) {
		this.examinationFileList = examinationFileList;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
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
	public void setEventTypeCode(String eventTypeCode) {
		this.eventTypeCode = eventTypeCode;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
	public void setImageNo(String imageNo) {
		this.imageNo = imageNo;
	}
	public void setExamSerialNo(String examSerialNo) {
		this.examSerialNo = examSerialNo;
	}
	public void setDicomImgSerialNo(String dicomImgSerialNo) {
		this.dicomImgSerialNo = dicomImgSerialNo;
	}
	public void setPhoneNumbe(String phoneNumbe) {
		this.phoneNumbe = phoneNumbe;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setSickroomNo(String sickroomNo) {
		this.sickroomNo = sickroomNo;
	}
	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}
	public void setSymptomStartDate(String symptomStartDate) {
		this.symptomStartDate = symptomStartDate;
	}
	public void setSymptomStopDate(String symptomStopDate) {
		this.symptomStopDate = symptomStopDate;
	}
	public void setSymptomDescription(String symptomDescription) {
		this.symptomDescription = symptomDescription;
	}
	public void setSpecialInspectionSign(String specialInspectionSign) {
		this.specialInspectionSign = specialInspectionSign;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public void setIntervenorName(String intervenorName) {
		this.intervenorName = intervenorName;
	}
	public void setOperatingDescription(String operatingDescription) {
		this.operatingDescription = operatingDescription;
	}
	public void setOperationNum(String operationNum) {
		this.operationNum = operationNum;
	}
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}
	public void setAnesthesiaCode(String anesthesiaCode) {
		this.anesthesiaCode = anesthesiaCode;
	}
	public void setAnesthesiaResult(String anesthesiaResult) {
		this.anesthesiaResult = anesthesiaResult;
	}
	public void setAnesthesiaSignCode(String anesthesiaSignCode) {
		this.anesthesiaSignCode = anesthesiaSignCode;
	}
	public void setAnesthesiologistSign(String anesthesiologistSign) {
		this.anesthesiologistSign = anesthesiologistSign;
	}
	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}
	public void setCheckSpecimenNumber(String checkSpecimenNumber) {
		this.checkSpecimenNumber = checkSpecimenNumber;
	}
	public void setSpecimenState(String specimenState) {
		this.specimenState = specimenState;
	}
	public void setSpecimenFixativeName(String specimenFixativeName) {
		this.specimenFixativeName = specimenFixativeName;
	}
	public void setSpecimenSamplingDate(String specimenSamplingDate) {
		this.specimenSamplingDate = specimenSamplingDate;
	}
	public void setReceivingSpecimenDate(String receivingSpecimenDate) {
		this.receivingSpecimenDate = receivingSpecimenDate;
	}
	public void setExamMethodName(String examMethodName) {
		this.examMethodName = examMethodName;
	}
	public void setInspectionCategory(String inspectionCategory) {
		this.inspectionCategory = inspectionCategory;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setCheckResultCode(String checkResultCode) {
		this.checkResultCode = checkResultCode;
	}
	public void setCheckQuantitativeResults(String checkQuantitativeResults) {
		this.checkQuantitativeResults = checkQuantitativeResults;
	}
	public void setCheckResultsUnit(String checkResultsUnit) {
		this.checkResultsUnit = checkResultsUnit;
	}
	public void setCheckTechnicianSign(String checkTechnicianSign) {
		this.checkTechnicianSign = checkTechnicianSign;
	}
	public void setCheckDoctorSign(String checkDoctorSign) {
		this.checkDoctorSign = checkDoctorSign;
	}
	public void setExaminationDate(String examinationDate) {
		this.examinationDate = examinationDate;
	}
	public void setCheckReportNumber(String checkReportNumber) {
		this.checkReportNumber = checkReportNumber;
	}
	public void setCheckReportOrgName(String checkReportOrgName) {
		this.checkReportOrgName = checkReportOrgName;
	}
	public void setCheckResultsSubjectiveCue(String checkResultsSubjectiveCue) {
		this.checkResultsSubjectiveCue = checkResultsSubjectiveCue;
	}
	public void setReportMemo(String reportMemo) {
		this.reportMemo = reportMemo;
	}
	public void setOperationAreaCode(String operationAreaCode) {
		this.operationAreaCode = operationAreaCode;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
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
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public void setOperationAreaName(String operationAreaName) {
		this.operationAreaName = operationAreaName;
	}
	public void setAnesthesiaName(String anesthesiaName) {
		this.anesthesiaName = anesthesiaName;
	}
	public void setAnesthesiaSignName(String anesthesiaSignName) {
		this.anesthesiaSignName = anesthesiaSignName;
	}
	public void setDiagnosisProcessDescription(String diagnosisProcessDescription) {
		this.diagnosisProcessDescription = diagnosisProcessDescription;
	}
	public void setDiagnosticCode(String diagnosticCode) {
		this.diagnosticCode = diagnosticCode;
	}
	public void setDiagnosticName(String diagnosticName) {
		this.diagnosticName = diagnosticName;
	}
	public void setDiagnosticInstitutionName(String diagnosticInstitutionName) {
		this.diagnosticInstitutionName = diagnosticInstitutionName;
	}
	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}
	public void setDiseaseDiagnosisList(java.util.List<DiagnosisBean> diseaseDiagnosisList) {
		this.diseaseDiagnosisList = diseaseDiagnosisList;
	}
}
