package com.hjw.webService.client.yichang.bean.cdr.client.requestForm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.util.DateTimeUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registRequestReq")  
@XmlType(propOrder = {})
public class RegistRequestReq {
	
	@XmlElement
	private String applyNo = "";//applyNo>
	@XmlElement
	private String applyType = "";//applyType>
	@XmlElement
	private String diagnosisNo = "";//diagnosisNo>
	@XmlElement
	private String diagnosisSerialNo = "";//diagnosisSerialNo>
	@XmlElement
	private String diagnosisType = "04";//诊疗类型名称：04	体检
	@XmlElement
	private String equipmentTypeCode = "";//
	@XmlElement
	private String equipmentTypeName = "";//equipmentTypeName>
	@XmlElement
	private String documentTypeCode = "";//documentTypeCode>
	@XmlElement
	private String documentTypeName = "";//documentTypeName>
	@XmlElement
	private String localId = "";//localId>
	@XmlElement
	private String documentTitle = "";//documentTitle>
	@XmlElement
	private String patientName = "";//patientName>
	@XmlElement
	private String patientLocalId = "";//patientLocalId>
	@XmlElement
	private String patientPhonenumber = "";//patientPhonenumber>
	@XmlElement
	private String sexCode = "";//sexCode>
	@XmlElement
	private String sexName = "";//sexName>
	@XmlElement
	private String age = "";//age>
	@XmlElement
	private String birthdate = "";//birthdate>
	@XmlElement
	private String specimenCollectDatetime = "";//specimenCollectDatetime>
	@XmlElement
	private String patientSource = "";//patientSource>
	@XmlElement
	private String applicationStartTime = DateTimeUtil.getDateTime().replace(" ", "T");//applicationStartTime>
	@XmlElement
	private String applicationDeptName = "";//applicationDeptName>
	@XmlElement
	private String applicationDeptCode = "";//applicationDeptCode>
	@XmlElement
	private String applicationDoctorName = "";//applicationDoctorName>
	@XmlElement
	private String applicationDoctorCode = "";//applicationDoctorCode>
	@XmlElement
	private String executiveDepartmentName = "";//executiveDepartmentName>
	@XmlElement
	private String executiveDepartmentCode = "";//executiveDepartmentCode>
	@XmlElement
	private String nursingStationName = "";//nursingStationName>
	@XmlElement
	private String nursingStationCode = "";//nursingStationCode>
	@XmlElement
	private String applyReason = "";//applyReason>
	@XmlElement
	private String applyContent = "";//applyContent>
	@XmlElement
	private String historySummary = "";//historySummary>
	@XmlElement
	private String clinicalDiagnosis = "";//clinicalDiagnosis>
	@XmlElement
	private String specimenType = "";//specimenType>
	@XmlElement
	private String specimenTypeName = "";//specimenTypeName>
	@XmlElement
	private String specimenCollecCode = "";//specimenCollecCode>
	@XmlElement
	private String specimenCollecName = "";//specimenCollecName>
	@XmlElement
	private String organz = "";//organz>
	@XmlElement
	private String organizationCode = "";//organizationCode>
	@XmlElement
	private String bedNo = "";//bedNo>
	private String urgentFlag = "";//urgentFlag>
	@XmlElement
	private String applyExecutiveDatetime = "";//applyExecutiveDatetime>
	@XmlElement
	private List<ItemCDRYC> itemList = new ArrayList<>();//applyExecutiveDatetime>
	@XmlElement
	private String xmlDocument = "";//xmlDocument>
	@XmlElement
	private String fileBase64Content = "";//fileBase64Content>
	@XmlElement
	private String fileType = "";//fileType>
	@XmlElement
	private String orderRemarks = "";//orderRemarks>
	@XmlElement
	private String codeExpand = "";//codeExpand>
	
	public String getApplyNo() {
		return applyNo;
	}
	public String getApplyType() {
		return applyType;
	}
	public String getDiagnosisNo() {
		return diagnosisNo;
	}
	public String getDiagnosisSerialNo() {
		return diagnosisSerialNo;
	}
	public String getDiagnosisType() {
		return diagnosisType;
	}
	public String getEquipmentTypeCode() {
		return equipmentTypeCode;
	}
	public String getEquipmentTypeName() {
		return equipmentTypeName;
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
	public String getPatientLocalId() {
		return patientLocalId;
	}
	public String getPatientPhonenumber() {
		return patientPhonenumber;
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
	public String getBirthdate() {
		return birthdate;
	}
	public String getSpecimenCollectDatetime() {
		return specimenCollectDatetime;
	}
	public String getPatientSource() {
		return patientSource;
	}
	public String getApplicationStartTime() {
		return applicationStartTime;
	}
	public String getApplicationDeptName() {
		return applicationDeptName;
	}
	public String getApplicationDeptCode() {
		return applicationDeptCode;
	}
	public String getApplicationDoctorName() {
		return applicationDoctorName;
	}
	public String getApplicationDoctorCode() {
		return applicationDoctorCode;
	}
	public String getExecutiveDepartmentName() {
		return executiveDepartmentName;
	}
	public String getExecutiveDepartmentCode() {
		return executiveDepartmentCode;
	}
	public String getNursingStationName() {
		return nursingStationName;
	}
	public String getNursingStationCode() {
		return nursingStationCode;
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
	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}
	public String getSpecimenType() {
		return specimenType;
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
	public String getOrganz() {
		return organz;
	}
	public String getOrganizationCode() {
		return organizationCode;
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
	public List<ItemCDRYC> getItemList() {
		return itemList;
	}
	public String getXmlDocument() {
		return xmlDocument;
	}
	public String getFileBase64Content() {
		return fileBase64Content;
	}
	public String getFileType() {
		return fileType;
	}
	public String getOrderRemarks() {
		return orderRemarks;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public void setDiagnosisNo(String diagnosisNo) {
		this.diagnosisNo = diagnosisNo;
	}
	public void setDiagnosisSerialNo(String diagnosisSerialNo) {
		this.diagnosisSerialNo = diagnosisSerialNo;
	}
	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}
	public void setEquipmentTypeCode(String equipmentTypeCode) {
		this.equipmentTypeCode = equipmentTypeCode;
	}
	public void setEquipmentTypeName(String equipmentTypeName) {
		this.equipmentTypeName = equipmentTypeName;
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
	public void setPatientLocalId(String patientLocalId) {
		this.patientLocalId = patientLocalId;
	}
	public void setPatientPhonenumber(String patientPhonenumber) {
		this.patientPhonenumber = patientPhonenumber;
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
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public void setSpecimenCollectDatetime(String specimenCollectDatetime) {
		this.specimenCollectDatetime = specimenCollectDatetime;
	}
	public void setPatientSource(String patientSource) {
		this.patientSource = patientSource;
	}
	public void setApplicationStartTime(String applicationStartTime) {
		this.applicationStartTime = applicationStartTime;
	}
	public void setApplicationDeptName(String applicationDeptName) {
		this.applicationDeptName = applicationDeptName;
	}
	public void setApplicationDeptCode(String applicationDeptCode) {
		this.applicationDeptCode = applicationDeptCode;
	}
	public void setApplicationDoctorName(String applicationDoctorName) {
		this.applicationDoctorName = applicationDoctorName;
	}
	public void setApplicationDoctorCode(String applicationDoctorCode) {
		this.applicationDoctorCode = applicationDoctorCode;
	}
	public void setExecutiveDepartmentName(String executiveDepartmentName) {
		this.executiveDepartmentName = executiveDepartmentName;
	}
	public void setExecutiveDepartmentCode(String executiveDepartmentCode) {
		this.executiveDepartmentCode = executiveDepartmentCode;
	}
	public void setNursingStationName(String nursingStationName) {
		this.nursingStationName = nursingStationName;
	}
	public void setNursingStationCode(String nursingStationCode) {
		this.nursingStationCode = nursingStationCode;
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
	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
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
	public void setOrganz(String organz) {
		this.organz = organz;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
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
	public void setItemList(List<ItemCDRYC> itemList) {
		this.itemList = itemList;
	}
	public void setXmlDocument(String xmlDocument) {
		this.xmlDocument = xmlDocument;
	}
	public void setFileBase64Content(String fileBase64Content) {
		this.fileBase64Content = fileBase64Content;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
}
