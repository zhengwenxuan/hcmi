package com.hjw.webService.client.yichang.bean.cdr.client.registration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.yichang.bean.cdr.client.list.Certificate;
import com.hjw.webService.client.yichang.bean.cdr.client.list.MedicalInsuranceInfo;
import com.hjw.webService.client.yichang.bean.cdr.client.list.PatientAddress;
import com.hjw.webService.client.yichang.bean.cdr.client.list.PatientMainList;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registPatientRegistrationInformationReq")  
@XmlType(propOrder = {})
public class RegistPatientRegistrationInformationReq {

	@XmlElement
	private String empiId = "";//
	@XmlElement
	private String createFiledate = "";//
	@XmlElement
	private String diagnosisNO = "";//
	@XmlElement
	private String diagnosisType = "04";//04	体检
	@XmlElement
	private String diagnosisSerialNO = "";//
	@XmlElement
	private String visitTimes = "";//
	@XmlElement
	private String clinicDatetime = "";//
	@XmlElement
	private String majorDiagName = "";//majorDiagName>
	@XmlElement
	private String patientName = "";//patientName>
	@XmlElement
	private String sexCode = "";//sexCode>
	@XmlElement
	private String sex = "";//sex>
	@XmlElement
	private String birthdate = "";//birthdate>
	@XmlElement
	private String age = "";//age>
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
	private String postCode = "";//postCode>
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
	private String workHomeNameTel = "";//workHomeNameTel>
	@XmlElement
	private String homePhone = "";//homePhone>
	@XmlElement
	private String mobilePhone = "";//mobilePhone>
	@XmlElement
	private String patientEmailAddress = "";//patientEmailAddress>
	@XmlElement
	private String registeredDoctorCode = "";//registeredDoctorCode>
	@XmlElement
	private String registeredDoctorName = "";//registeredDoctorName>
	@XmlElement
	private String deptCode = "0377";//0377	体检中心
	@XmlElement
	private String deptName = "体检中心";//
	@XmlElement
	private String organizationCode = "";//organizationCode>
	@XmlElement
	private String organz = "";//organz>
	@XmlElement
	private String certificateTypeCode = "01";//01	居民身份证
	@XmlElement
	private String certificateType = "居民身份证";//
	@XmlElement
	private String idNumber = "";//idNumber>
	@XmlElement
	private String workTelecom = "";//workTelecom>
	@XmlElement
	private String photo = "";//
	@XmlElement
	private List<Certificate> certificateList = new ArrayList<>();
	@XmlElement
	private List<MedicalInsuranceInfo> medicalInsuranceInfoList = new ArrayList<>();
	@XmlElement
	private List<PatientAddress> patientAddressList = new ArrayList<>();
	@XmlElement
	private List<PatientMainList> patientMainListList = new ArrayList<>();
	@XmlElement
	private String patientID = "";//patientID>
	@XmlElement
	private String codeExpand = "";//codeExpand>
	@XmlElement
	private String visitDeptCode = "";//visitDeptCode>
	@XmlElement
	private String visitDeptName = "";//visitDeptName>
	@XmlElement
	private String attendingDoctorDeptCode = "";//attendingDoctorDeptCode>
	@XmlElement
	private String attendingDoctorDeptName = "";//attendingDoctorDeptName>
	@XmlElement
	private String directorDeptCode = "";//directorDeptCode>
	@XmlElement
	private String directorDeptName = "";//directorDeptName>
	@XmlElement
	private String inHospitalDocDeptCode = "";//inHospitalDocDeptCode>
	@XmlElement
	private String inHospitalDocDeptName = "";//inHospitalDocDeptName>
	@XmlElement
	private String isNewborn = "";//isNewborn>
	@XmlElement
	private String healthCardNo = "";//healthCardNo>
	@XmlElement
	private String medicalCardNo = "";//medicalCardNo>
	@XmlElement
	private String householdAddressProvinceName = "";//householdAddressProvinceName>
	@XmlElement
	private String householdAddressCityName = "";//householdAddressCityName>
	@XmlElement
	private String householdAddressDistrictName = "";//householdAddressDistrictName>
	@XmlElement
	private String householdAddressVillage = "";//householdAddressVillage>
	@XmlElement
	private String householdAddressHamlet = "";//householdAddressHamlet>
	@XmlElement
	private String householdAddress = "";//householdAddress>
	@XmlElement
	private String presentAddress = "";//presentAddress>
	@XmlElement
	private String operatorName = "";//operatorName>
	@XmlElement
	private String operatorCode = "";//operatorCode>
	@XmlElement
	private String operatorTime = "";//operatorTime>
	@XmlElement
	private String registerLevelCode = "";//registerLevelCode>
	@XmlElement
	private String registerWayCode = "";//registerWayCode>
	@XmlElement
	private String registerCost = "";//registerCost>
	@XmlElement
	private String orderChannelCode = "";//orderChannelCode>
	@XmlElement
	private String cancelFlag = "";//cancelFlag>
	@XmlElement
	private String firstVisitFlag = "";//firstVisitFlag>
	@XmlElement
	private String visitFlag = "";//visitFlag>
	@XmlElement
	private String payModeCode = "";//payModeCode>
	@XmlElement
	private String outDate = "";//outDate>
	
	public String getEmpiId() {
		return empiId;
	}
	public void setEmpiId(String empiId) {
		this.empiId = empiId;
	}
	public String getCreateFiledate() {
		return createFiledate;
	}
	public void setCreateFiledate(String createFiledate) {
		this.createFiledate = createFiledate;
	}
	public String getDiagnosisNO() {
		return diagnosisNO;
	}
	public void setDiagnosisNO(String diagnosisNO) {
		this.diagnosisNO = diagnosisNO;
	}
	public String getDiagnosisType() {
		return diagnosisType;
	}
	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}
	public String getDiagnosisSerialNO() {
		return diagnosisSerialNO;
	}
	public void setDiagnosisSerialNO(String diagnosisSerialNO) {
		this.diagnosisSerialNO = diagnosisSerialNO;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public String getClinicDatetime() {
		return clinicDatetime;
	}
	public void setClinicDatetime(String clinicDatetime) {
		this.clinicDatetime = clinicDatetime;
	}
	public String getMajorDiagName() {
		return majorDiagName;
	}
	public void setMajorDiagName(String majorDiagName) {
		this.majorDiagName = majorDiagName;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getSexCode() {
		return sexCode;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getMaritalStatusCode() {
		return maritalStatusCode;
	}
	public void setMaritalStatusCode(String maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}
	public String getMaritalStatusName() {
		return maritalStatusName;
	}
	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}
	public String getNationCode() {
		return nationCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getNationalityCode() {
		return nationalityCode;
	}
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	public String getNationalityName() {
		return nationalityName;
	}
	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}
	public String getAboBloodTypeCode() {
		return aboBloodTypeCode;
	}
	public void setAboBloodTypeCode(String aboBloodTypeCode) {
		this.aboBloodTypeCode = aboBloodTypeCode;
	}
	public String getAboBloodTypeName() {
		return aboBloodTypeName;
	}
	public void setAboBloodTypeName(String aboBloodTypeName) {
		this.aboBloodTypeName = aboBloodTypeName;
	}
	public String getRhBloodTypeCode() {
		return rhBloodTypeCode;
	}
	public void setRhBloodTypeCode(String rhBloodTypeCode) {
		this.rhBloodTypeCode = rhBloodTypeCode;
	}
	public String getRhBloodTypeName() {
		return rhBloodTypeName;
	}
	public void setRhBloodTypeName(String rhBloodTypeName) {
		this.rhBloodTypeName = rhBloodTypeName;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getBirthPlace() {
		return birthPlace;
	}
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}
	public String getOccupationalCode() {
		return occupationalCode;
	}
	public void setOccupationalCode(String occupationalCode) {
		this.occupationalCode = occupationalCode;
	}
	public String getOccupationalName() {
		return occupationalName;
	}
	public void setOccupationalName(String occupationalName) {
		this.occupationalName = occupationalName;
	}
	public String getCulturalLevelCode() {
		return culturalLevelCode;
	}
	public void setCulturalLevelCode(String culturalLevelCode) {
		this.culturalLevelCode = culturalLevelCode;
	}
	public String getCulturalLevelName() {
		return culturalLevelName;
	}
	public void setCulturalLevelName(String culturalLevelName) {
		this.culturalLevelName = culturalLevelName;
	}
	public String getWorkHomeName() {
		return workHomeName;
	}
	public void setWorkHomeName(String workHomeName) {
		this.workHomeName = workHomeName;
	}
	public String getWorkHomeNameTel() {
		return workHomeNameTel;
	}
	public void setWorkHomeNameTel(String workHomeNameTel) {
		this.workHomeNameTel = workHomeNameTel;
	}
	public String getHomePhone() {
		return homePhone;
	}
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPatientEmailAddress() {
		return patientEmailAddress;
	}
	public void setPatientEmailAddress(String patientEmailAddress) {
		this.patientEmailAddress = patientEmailAddress;
	}
	public String getRegisteredDoctorCode() {
		return registeredDoctorCode;
	}
	public void setRegisteredDoctorCode(String registeredDoctorCode) {
		this.registeredDoctorCode = registeredDoctorCode;
	}
	public String getRegisteredDoctorName() {
		return registeredDoctorName;
	}
	public void setRegisteredDoctorName(String registeredDoctorName) {
		this.registeredDoctorName = registeredDoctorName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getOrganizationCode() {
		return organizationCode;
	}
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}
	public String getOrganz() {
		return organz;
	}
	public void setOrganz(String organz) {
		this.organz = organz;
	}
	public String getCertificateTypeCode() {
		return certificateTypeCode;
	}
	public void setCertificateTypeCode(String certificateTypeCode) {
		this.certificateTypeCode = certificateTypeCode;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public String getWorkTelecom() {
		return workTelecom;
	}
	public void setWorkTelecom(String workTelecom) {
		this.workTelecom = workTelecom;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public List<Certificate> getCertificateList() {
		return certificateList;
	}
	public void setCertificateList(List<Certificate> certificateList) {
		this.certificateList = certificateList;
	}
	public List<MedicalInsuranceInfo> getMedicalInsuranceInfoList() {
		return medicalInsuranceInfoList;
	}
	public void setMedicalInsuranceInfoList(List<MedicalInsuranceInfo> medicalInsuranceInfoList) {
		this.medicalInsuranceInfoList = medicalInsuranceInfoList;
	}
	public List<PatientAddress> getPatientAddressList() {
		return patientAddressList;
	}
	public void setPatientAddressList(List<PatientAddress> patientAddressList) {
		this.patientAddressList = patientAddressList;
	}
	public List<PatientMainList> getPatientMainListList() {
		return patientMainListList;
	}
	public void setPatientMainListList(List<PatientMainList> patientMainListList) {
		this.patientMainListList = patientMainListList;
	}
	public String getPatientID() {
		return patientID;
	}
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
	public String getVisitDeptCode() {
		return visitDeptCode;
	}
	public void setVisitDeptCode(String visitDeptCode) {
		this.visitDeptCode = visitDeptCode;
	}
	public String getVisitDeptName() {
		return visitDeptName;
	}
	public void setVisitDeptName(String visitDeptName) {
		this.visitDeptName = visitDeptName;
	}
	public String getAttendingDoctorDeptCode() {
		return attendingDoctorDeptCode;
	}
	public void setAttendingDoctorDeptCode(String attendingDoctorDeptCode) {
		this.attendingDoctorDeptCode = attendingDoctorDeptCode;
	}
	public String getAttendingDoctorDeptName() {
		return attendingDoctorDeptName;
	}
	public void setAttendingDoctorDeptName(String attendingDoctorDeptName) {
		this.attendingDoctorDeptName = attendingDoctorDeptName;
	}
	public String getDirectorDeptCode() {
		return directorDeptCode;
	}
	public void setDirectorDeptCode(String directorDeptCode) {
		this.directorDeptCode = directorDeptCode;
	}
	public String getDirectorDeptName() {
		return directorDeptName;
	}
	public void setDirectorDeptName(String directorDeptName) {
		this.directorDeptName = directorDeptName;
	}
	public String getInHospitalDocDeptCode() {
		return inHospitalDocDeptCode;
	}
	public void setInHospitalDocDeptCode(String inHospitalDocDeptCode) {
		this.inHospitalDocDeptCode = inHospitalDocDeptCode;
	}
	public String getInHospitalDocDeptName() {
		return inHospitalDocDeptName;
	}
	public void setInHospitalDocDeptName(String inHospitalDocDeptName) {
		this.inHospitalDocDeptName = inHospitalDocDeptName;
	}
	public String getIsNewborn() {
		return isNewborn;
	}
	public void setIsNewborn(String isNewborn) {
		this.isNewborn = isNewborn;
	}
	public String getHealthCardNo() {
		return healthCardNo;
	}
	public void setHealthCardNo(String healthCardNo) {
		this.healthCardNo = healthCardNo;
	}
	public String getMedicalCardNo() {
		return medicalCardNo;
	}
	public void setMedicalCardNo(String medicalCardNo) {
		this.medicalCardNo = medicalCardNo;
	}
	public String getHouseholdAddressProvinceName() {
		return householdAddressProvinceName;
	}
	public void setHouseholdAddressProvinceName(String householdAddressProvinceName) {
		this.householdAddressProvinceName = householdAddressProvinceName;
	}
	public String getHouseholdAddressCityName() {
		return householdAddressCityName;
	}
	public void setHouseholdAddressCityName(String householdAddressCityName) {
		this.householdAddressCityName = householdAddressCityName;
	}
	public String getHouseholdAddressDistrictName() {
		return householdAddressDistrictName;
	}
	public void setHouseholdAddressDistrictName(String householdAddressDistrictName) {
		this.householdAddressDistrictName = householdAddressDistrictName;
	}
	public String getHouseholdAddressVillage() {
		return householdAddressVillage;
	}
	public void setHouseholdAddressVillage(String householdAddressVillage) {
		this.householdAddressVillage = householdAddressVillage;
	}
	public String getHouseholdAddressHamlet() {
		return householdAddressHamlet;
	}
	public void setHouseholdAddressHamlet(String householdAddressHamlet) {
		this.householdAddressHamlet = householdAddressHamlet;
	}
	public String getHouseholdAddress() {
		return householdAddress;
	}
	public void setHouseholdAddress(String householdAddress) {
		this.householdAddress = householdAddress;
	}
	public String getPresentAddress() {
		return presentAddress;
	}
	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(String operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getRegisterLevelCode() {
		return registerLevelCode;
	}
	public void setRegisterLevelCode(String registerLevelCode) {
		this.registerLevelCode = registerLevelCode;
	}
	public String getRegisterWayCode() {
		return registerWayCode;
	}
	public void setRegisterWayCode(String registerWayCode) {
		this.registerWayCode = registerWayCode;
	}
	public String getRegisterCost() {
		return registerCost;
	}
	public void setRegisterCost(String registerCost) {
		this.registerCost = registerCost;
	}
	public String getOrderChannelCode() {
		return orderChannelCode;
	}
	public void setOrderChannelCode(String orderChannelCode) {
		this.orderChannelCode = orderChannelCode;
	}
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getFirstVisitFlag() {
		return firstVisitFlag;
	}
	public void setFirstVisitFlag(String firstVisitFlag) {
		this.firstVisitFlag = firstVisitFlag;
	}
	public String getVisitFlag() {
		return visitFlag;
	}
	public void setVisitFlag(String visitFlag) {
		this.visitFlag = visitFlag;
	}
	public String getPayModeCode() {
		return payModeCode;
	}
	public void setPayModeCode(String payModeCode) {
		this.payModeCode = payModeCode;
	}
	public String getOutDate() {
		return outDate;
	}
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	
}
