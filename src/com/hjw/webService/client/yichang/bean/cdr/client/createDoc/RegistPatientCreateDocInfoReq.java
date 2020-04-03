package com.hjw.webService.client.yichang.bean.cdr.client.createDoc;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.yichang.bean.cdr.client.list.Certificate;
import com.hjw.webService.client.yichang.bean.cdr.client.list.MedicalInsuranceInfo;
import com.hjw.webService.client.yichang.bean.cdr.client.list.PatientAddress;
import com.hjw.webService.client.yichang.bean.cdr.client.list.PatientMainList;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registPatientCreateDocInfoReq")  
@XmlType(propOrder = {})
public class RegistPatientCreateDocInfoReq {
	
	@XmlElement
	private String createFiledate = DateTimeUtil.getDateTime().replace(" ", "T");//
	@XmlElement
	private String patientID = "";//patientID>
	@XmlElement
	private String empiId = "";//empiId>
	@XmlElement
	private String codeExpand = "";//codeExpand>
	@XmlElement
	private String patientName = "";//patientName>
	@XmlElement
	private String age = "";//age>
	@XmlElement
	private String sexCode = "";//sexCode>
	@XmlElement
	private String sex = "";//sex>
	@XmlElement
	private String birthdate = "";//birthdate>
	@XmlElement
	private String maritalStatusCode = "";//maritalStatusCode>
	@XmlElement
	private String maritalStatusName = "";//maritalStatusName>
	@XmlElement
	private String nationCode = "";//nationCode>
	@XmlElement
	private String nation = "";//nation>
	@XmlElement
	private String nationalityCode = "";//nationalityCode>
	@XmlElement
	private String nationalityName = "";//nationalityName>
	@XmlElement
	private String aboBloodTypeCode = "";//aboBloodTypeCode>
	@XmlElement
	private String aboBloodTypeName = "";//aboBloodTypeName>
	@XmlElement
	private String rhBloodTypeCode = "";//rhBloodTypeCode>
	@XmlElement
	private String rhBloodTypeName = "";//rhBloodTypeName>
	@XmlElement
	private String birthPlace = "";//birthPlace>
	@XmlElement
	private String occupationalCode = "";//occupationalCode>
	@XmlElement
	private String occupationalName = "";//occupationalName>
	@XmlElement
	private String culturalLevelCode = "";//culturalLevelCode>
	@XmlElement
	private String culturalLevelName = "";//culturalLevelName>
	@XmlElement
	private String workHomeName = "";//workHomeName>
	@XmlElement
	private String homePhone = "";//homePhone>
	@XmlElement
	private String mobilePhone = "";//mobilePhone>
	@XmlElement
	private String patientEmailAddress = "";//patientEmailAddress>
	@XmlElement
	private String organzCode = "";//organzCode>
	@XmlElement
	private String organz = "";//organz>
	@XmlElement
	private String certificateTypeCode = "01";//01	居民身份证
	@XmlElement
	private String certificateType = "居民身份证";//
	@XmlElement
	private String idNumber = "";//idNumber>
	@XmlElement
	private List<Certificate> certificateList = new ArrayList<>();
	@XmlElement
	private String photo = "";//photo>
	@XmlElement
	private String workTelecom = "";//workTelecom>
	@XmlElement
	private String postCode = "";//postCode>
	@XmlElement
	private List<MedicalInsuranceInfo> medicalInsuranceInfoList = new ArrayList<>();
	@XmlElement
	private List<PatientAddress> patientAddressList = new ArrayList<>();
	@XmlElement
	private List<PatientMainList> patientMainListList = new ArrayList<>();
	@XmlElement
	private String operatorName = "";//operatorName>
	@XmlElement
	private String operatorCode = "";//operatorCode>
	@XmlElement
	private String operatorTime = "";//operatorTime>
	
	public String getCreateFiledate() {
		return createFiledate;
	}
	public String getPatientID() {
		return patientID;
	}
	public String getEmpiId() {
		return empiId;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getAge() {
		return age;
	}
	public String getSexCode() {
		return sexCode;
	}
	public String getSex() {
		return sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public String getMaritalStatusCode() {
		return maritalStatusCode;
	}
	public String getMaritalStatusName() {
		return maritalStatusName;
	}
	public String getNationCode() {
		return nationCode;
	}
	public String getNation() {
		return nation;
	}
	public String getNationalityCode() {
		return nationalityCode;
	}
	public String getNationalityName() {
		return nationalityName;
	}
	public String getAboBloodTypeCode() {
		return aboBloodTypeCode;
	}
	public String getAboBloodTypeName() {
		return aboBloodTypeName;
	}
	public String getRhBloodTypeCode() {
		return rhBloodTypeCode;
	}
	public String getRhBloodTypeName() {
		return rhBloodTypeName;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public String getOccupationalCode() {
		return occupationalCode;
	}
	public String getOccupationalName() {
		return occupationalName;
	}
	public String getCulturalLevelCode() {
		return culturalLevelCode;
	}
	public String getCulturalLevelName() {
		return culturalLevelName;
	}
	public String getWorkHomeName() {
		return workHomeName;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public String getPatientEmailAddress() {
		return patientEmailAddress;
	}
	public String getOrganzCode() {
		return organzCode;
	}
	public String getOrganz() {
		return organz;
	}
	public String getCertificateTypeCode() {
		return certificateTypeCode;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public List<Certificate> getCertificateList() {
		return certificateList;
	}
	public String getPhoto() {
		return photo;
	}
	public String getWorkTelecom() {
		return workTelecom;
	}
	public String getPostCode() {
		return postCode;
	}
	public List<MedicalInsuranceInfo> getMedicalInsuranceInfoList() {
		return medicalInsuranceInfoList;
	}
	public List<PatientAddress> getPatientAddressList() {
		return patientAddressList;
	}
	public List<PatientMainList> getPatientMainListList() {
		return patientMainListList;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setCreateFiledate(String createFiledate) {
		this.createFiledate = createFiledate;
	}
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	public void setEmpiId(String empiId) {
		this.empiId = empiId;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public void setMaritalStatusCode(String maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}
	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}
	public void setAboBloodTypeCode(String aboBloodTypeCode) {
		this.aboBloodTypeCode = aboBloodTypeCode;
	}
	public void setAboBloodTypeName(String aboBloodTypeName) {
		this.aboBloodTypeName = aboBloodTypeName;
	}
	public void setRhBloodTypeCode(String rhBloodTypeCode) {
		this.rhBloodTypeCode = rhBloodTypeCode;
	}
	public void setRhBloodTypeName(String rhBloodTypeName) {
		this.rhBloodTypeName = rhBloodTypeName;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public void setOccupationalCode(String occupationalCode) {
		this.occupationalCode = occupationalCode;
	}
	public void setOccupationalName(String occupationalName) {
		this.occupationalName = occupationalName;
	}
	public void setCulturalLevelCode(String culturalLevelCode) {
		this.culturalLevelCode = culturalLevelCode;
	}
	public void setCulturalLevelName(String culturalLevelName) {
		this.culturalLevelName = culturalLevelName;
	}
	public void setWorkHomeName(String workHomeName) {
		this.workHomeName = workHomeName;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public void setPatientEmailAddress(String patientEmailAddress) {
		this.patientEmailAddress = patientEmailAddress;
	}
	public void setOrganzCode(String organzCode) {
		this.organzCode = organzCode;
	}
	public void setOrganz(String organz) {
		this.organz = organz;
	}
	public void setCertificateTypeCode(String certificateTypeCode) {
		this.certificateTypeCode = certificateTypeCode;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public void setCertificateList(List<Certificate> certificateList) {
		this.certificateList = certificateList;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public void setWorkTelecom(String workTelecom) {
		this.workTelecom = workTelecom;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public void setMedicalInsuranceInfoList(List<MedicalInsuranceInfo> medicalInsuranceInfoList) {
		this.medicalInsuranceInfoList = medicalInsuranceInfoList;
	}
	public void setPatientAddressList(List<PatientAddress> patientAddressList) {
		this.patientAddressList = patientAddressList;
	}
	public void setPatientMainListList(List<PatientMainList> patientMainListList) {
		this.patientMainListList = patientMainListList;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
}
