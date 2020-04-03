package com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "QueryRegularApplic")  
@XmlType(propOrder = {})
public class QueryRegularApplic {

	@XmlElement
	private String applyCode  = "";// 申请单编码 
	@XmlElement
	private String applyTypeCode  = "";// 申请单类型编码 是
	@XmlElement
	private String visitNo  = "";// 诊疗号
	@XmlElement
	private String visitSqNo  = "";// 诊疗流水号
	@XmlElement
	private String visitTypeCode  = "";// 诊疗类型编码 是
	@XmlElement
	private String deviceTypeCode  = "";// 设备类型编码 是
	@XmlElement
	private String deviceTypeName  = "";// 设备类型名称 是
	@XmlElement
	private String documentTypeCode  = "";// 文档类型编码 是
	@XmlElement
	private String documentTypeName  = "";// 文档类型名称 是
	@XmlElement
	private String localId  = "";// 文档本地ID
	@XmlElement
	private String documentTitle  = "";// 文档标题
	@XmlElement
	private String patientName  = "";// 患者姓名
	@XmlElement
	private String patientCode  = "";// 患者编码
	@XmlElement
	private String patientPhoneNumber  = "";// 患者电话号码
	@XmlElement
	private String sexCode  = "";// 性别编码 是
	@XmlElement
	private String sexName  = "";// 性别名称 是
	@XmlElement
	private String age  = "";// 年龄
	@XmlElement
	private String birthDate  = "";// 出生日期
	@XmlElement
	private String simpleTime  = "";// 采样时间 
	@XmlElement
	private String patientSourceCode  = "";// 患者来源编码 是
	@XmlElement
	private String applicationStartTime  = "";// 开立时间
	@XmlElement
	private String applicationDeptCode  = "";// 申请科室编码 是
	@XmlElement
	private String applicationDeptName  = "";// 申请科室名称 是
	@XmlElement
	private String applicationDoctorCode  = "";// 申请医生编码 是
	@XmlElement
	private String applicationDoctorName  = "";// 申请医生名称 是
	@XmlElement
	private String updateDoctorCode  = "";// 更新医生编码 是
	@XmlElement
	private String updateDocotorName  = "";// 更新医生名称 是
	@XmlElement
	private String executDeptCode  = "";// 执行科室编码 是
	@XmlElement
	private String executDeptName  = "";// 执行科室名称 是
	@XmlElement
	private String nursingStationCode  = "";// 护士站编码 是
	@XmlElement
	private String nursingStationName  = "";// 护士站名称 是
	@XmlElement
	private String applyReason  = "";// 申请目的.
	@XmlElement
	private String applyContent  = "";// 申请内容
	@XmlElement
	private String historySummary  = "";// 病史摘要
	@XmlElement
	private String clinicalDiagnosisName  = "";// 临床诊断名称 是
	@XmlElement
	private String specimenTypeCode  = "";// 样本类型编码 是
	@XmlElement
	private String specimenTypeName  = "";// 样本类型名称 是
	@XmlElement
	private String specimenCollecCode  = "";// 标本采集部位编码 是
	@XmlElement
	private String specimenCollecName  = "";// 标本采集部位名称 是
	@XmlElement
	private String organizationCode  = "";// 医疗机构编码
	@XmlElement
	private String organizationName  = "";// 医疗机构名称
	@XmlElement
	private String bedNo  = "";// 病床号 是
	@XmlElement
	private String urgentFlag  = "";// 是否加急 是
	@XmlElement
	private String applyExecutiveDatetime  = "";// 申请执行时间 
	@XmlElement
	private ItemList itemList = new ItemList();// itemList
	@XmlElement
	private String xmlDocument  = "";// xml文档
	@XmlElement
	private String fileType  = "";// 文件类型 是
	@XmlElement
	private String pdfBase64  = "";// PDF或JPG
	@XmlElement
	private String doctorMark  = "";// 医嘱备注
	@XmlElement
	private String applyStatus  = "";// 申请单状态
	@XmlElement
	private String codeExpand1  = "";// 扩展字段1
	@XmlElement
	private String codeExpand2  = "";// 扩展字段2
	@XmlElement
	private String codeExpand3  = "";// 扩展字段3
	@XmlElement
	private String codeExpand4  = "";// 扩展字段4
	@XmlElement
	private String codeExpand5  = "";// 扩展字段5
	@XmlElement
	private String idCard = "";//身份证号
	
	public String getApplyCode() {
		return applyCode;
	}
	public String getApplyTypeCode() {
		return applyTypeCode;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public String getVisitSqNo() {
		return visitSqNo;
	}
	public String getVisitTypeCode() {
		return visitTypeCode;
	}
	public String getDeviceTypeCode() {
		return deviceTypeCode;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public String getDocumentTypeCode() {
		return documentTypeCode;
	}
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public String getLocalId() {
		return localId;
	}
	public String getDocumentTitle() {
		return documentTitle;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getPatientCode() {
		return patientCode;
	}
	public String getPatientPhoneNumber() {
		return patientPhoneNumber;
	}
	public String getSexCode() {
		return sexCode;
	}
	public String getSexName() {
		return sexName;
	}
	public String getAge() {
		return age;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getSimpleTime() {
		return simpleTime;
	}
	public String getPatientSourceCode() {
		return patientSourceCode;
	}
	public String getApplicationStartTime() {
		return applicationStartTime;
	}
	public String getApplicationDeptCode() {
		return applicationDeptCode;
	}
	public String getApplicationDeptName() {
		return applicationDeptName;
	}
	public String getApplicationDoctorCode() {
		return applicationDoctorCode;
	}
	public String getApplicationDoctorName() {
		return applicationDoctorName;
	}
	public String getUpdateDoctorCode() {
		return updateDoctorCode;
	}
	public String getUpdateDocotorName() {
		return updateDocotorName;
	}
	public String getExecutDeptCode() {
		return executDeptCode;
	}
	public String getExecutDeptName() {
		return executDeptName;
	}
	public String getNursingStationCode() {
		return nursingStationCode;
	}
	public String getNursingStationName() {
		return nursingStationName;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public String getApplyContent() {
		return applyContent;
	}
	public String getHistorySummary() {
		return historySummary;
	}
	public String getClinicalDiagnosisName() {
		return clinicalDiagnosisName;
	}
	public String getSpecimenTypeCode() {
		return specimenTypeCode;
	}
	public String getSpecimenTypeName() {
		return specimenTypeName;
	}
	public String getSpecimenCollecCode() {
		return specimenCollecCode;
	}
	public String getSpecimenCollecName() {
		return specimenCollecName;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public String getUrgentFlag() {
		return urgentFlag;
	}
	public String getApplyExecutiveDatetime() {
		return applyExecutiveDatetime;
	}
	public String getXmlDocument() {
		return xmlDocument;
	}
	public String getFileType() {
		return fileType;
	}
	public String getPdfBase64() {
		return pdfBase64;
	}
	public String getDoctorMark() {
		return doctorMark;
	}
	public String getApplyStatus() {
		return applyStatus;
	}
	public String getCodeExpand1() {
		return codeExpand1;
	}
	public String getCodeExpand2() {
		return codeExpand2;
	}
	public String getCodeExpand3() {
		return codeExpand3;
	}
	public String getCodeExpand4() {
		return codeExpand4;
	}
	public String getCodeExpand5() {
		return codeExpand5;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public void setApplyTypeCode(String applyTypeCode) {
		this.applyTypeCode = applyTypeCode;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public void setVisitSqNo(String visitSqNo) {
		this.visitSqNo = visitSqNo;
	}
	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}
	public void setDeviceTypeCode(String deviceTypeCode) {
		this.deviceTypeCode = deviceTypeCode;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	public void setLocalId(String localId) {
		this.localId = localId;
	}
	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setPatientCode(String patientCode) {
		this.patientCode = patientCode;
	}
	public void setPatientPhoneNumber(String patientPhoneNumber) {
		this.patientPhoneNumber = patientPhoneNumber;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setSimpleTime(String simpleTime) {
		this.simpleTime = simpleTime;
	}
	public void setPatientSourceCode(String patientSourceCode) {
		this.patientSourceCode = patientSourceCode;
	}
	public void setApplicationStartTime(String applicationStartTime) {
		this.applicationStartTime = applicationStartTime;
	}
	public void setApplicationDeptCode(String applicationDeptCode) {
		this.applicationDeptCode = applicationDeptCode;
	}
	public void setApplicationDeptName(String applicationDeptName) {
		this.applicationDeptName = applicationDeptName;
	}
	public void setApplicationDoctorCode(String applicationDoctorCode) {
		this.applicationDoctorCode = applicationDoctorCode;
	}
	public void setApplicationDoctorName(String applicationDoctorName) {
		this.applicationDoctorName = applicationDoctorName;
	}
	public void setUpdateDoctorCode(String updateDoctorCode) {
		this.updateDoctorCode = updateDoctorCode;
	}
	public void setUpdateDocotorName(String updateDocotorName) {
		this.updateDocotorName = updateDocotorName;
	}
	public void setExecutDeptCode(String executDeptCode) {
		this.executDeptCode = executDeptCode;
	}
	public void setExecutDeptName(String executDeptName) {
		this.executDeptName = executDeptName;
	}
	public void setNursingStationCode(String nursingStationCode) {
		this.nursingStationCode = nursingStationCode;
	}
	public void setNursingStationName(String nursingStationName) {
		this.nursingStationName = nursingStationName;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}
	public void setHistorySummary(String historySummary) {
		this.historySummary = historySummary;
	}
	public void setClinicalDiagnosisName(String clinicalDiagnosisName) {
		this.clinicalDiagnosisName = clinicalDiagnosisName;
	}
	public void setSpecimenTypeCode(String specimenTypeCode) {
		this.specimenTypeCode = specimenTypeCode;
	}
	public void setSpecimenTypeName(String specimenTypeName) {
		this.specimenTypeName = specimenTypeName;
	}
	public void setSpecimenCollecCode(String specimenCollecCode) {
		this.specimenCollecCode = specimenCollecCode;
	}
	public void setSpecimenCollecName(String specimenCollecName) {
		this.specimenCollecName = specimenCollecName;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public void setUrgentFlag(String urgentFlag) {
		this.urgentFlag = urgentFlag;
	}
	public void setApplyExecutiveDatetime(String applyExecutiveDatetime) {
		this.applyExecutiveDatetime = applyExecutiveDatetime;
	}
	public void setXmlDocument(String xmlDocument) {
		this.xmlDocument = xmlDocument;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public void setPdfBase64(String pdfBase64) {
		this.pdfBase64 = pdfBase64;
	}
	public void setDoctorMark(String doctorMark) {
		this.doctorMark = doctorMark;
	}
	public void setApplyStatus(String applyStatus) {
		this.applyStatus = applyStatus;
	}
	public void setCodeExpand1(String codeExpand1) {
		this.codeExpand1 = codeExpand1;
	}
	public void setCodeExpand2(String codeExpand2) {
		this.codeExpand2 = codeExpand2;
	}
	public void setCodeExpand3(String codeExpand3) {
		this.codeExpand3 = codeExpand3;
	}
	public void setCodeExpand4(String codeExpand4) {
		this.codeExpand4 = codeExpand4;
	}
	public void setCodeExpand5(String codeExpand5) {
		this.codeExpand5 = codeExpand5;
	}
	public ItemList getItemList() {
		return itemList;
	}
	public void setItemList(ItemList itemList) {
		this.itemList = itemList;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
}
