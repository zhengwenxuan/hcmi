package com.hjw.webService.client.bdyx.bean.lis.req;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;

public class LisApplyReq {

	private static String HOSPITAL_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
	}
	
	private String newUpFlg = "new";//消息交互类型
	private String patientDomain = "02";//域ID
	private String patientLid = "";//患者ID
	private String visitNo = "";//就诊号
	private String visitOrdNo = "";//就诊流水号
	private String visitTimes = "1";//就诊次数
	private String visitType = "0401";//就诊类型编码
	private String visitTypeName = "体检";//就诊类型名称
	private String admissionWards = "";//病区编码
	private String admissionWardsName = "";//病区名
	private String admissionBedNo = "";//床号
	private String patientName = "";//患者姓名
	private String telephone = "";//联系电话
	private String mobile = "";//移动电话 
	private String genderCode = "";//性别代码
	private String birthTime = "";//出生日期
	private String age = "";//年龄
	private String addrCs = "";//家庭住址
	private String addrZs = "";//邮政编码
	private String workPlaceId = "";//工作单位代码
	private String workPlace = "";//工作单位名称
	private String visitDept = "";//病人科室编码
	private String visitDeptName = "";//病人科室名称
	private String orgCode = HOSPITAL_ID;//医疗机构代码
	private String orgName = "武威医学科学院";//医疗机构名称
	private String orderTime = DateTimeUtil.getDateTimes();//开单时间
	private String requestDoctor = "1";//开单医生编码
	private String requestDoctorName = "--";//开单医生姓名
	private String requestDept = "";//申请科室编码
	private String requestDeptName = "";//申请科室名称
	private String confirmTime = "";//确认时间
	private String confirmPerson = "";//确认人编码
	private String confirmPersonName = "";//确认人姓名
	private String transcriberFromDate = "";//录入日期 开始时间
	private String transcriberToDate = "";//录入日期 结束时间
	private String transcriberId = "";//录入人
	private String transcriberName = "";//录入人姓名
	private List<LisReq> requestList = new ArrayList<>();//1..n申请单信息
	private String visitHospitalCode = "";//就诊院区编码
	private String visitHospitalName = "";//就诊院区名称
	private String diagnosisType = "";//诊断类别
	private String diagnosisTypeName = "";//诊断类别名称
	private String diagnosisNo = "";//疾病代码
	private String diagnosisName = "";//疾病名称
	
	public String getNewUpFlg() {
		return newUpFlg;
	}
	public String getPatientDomain() {
		return patientDomain;
	}
	public String getPatientLid() {
		return patientLid;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getVisitType() {
		return visitType;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getAdmissionWards() {
		return admissionWards;
	}
	public String getAdmissionWardsName() {
		return admissionWardsName;
	}
	public String getAdmissionBedNo() {
		return admissionBedNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getTelephone() {
		return telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public String getBirthTime() {
		return birthTime;
	}
	public String getAge() {
		return age;
	}
	public String getAddrCs() {
		return addrCs;
	}
	public String getAddrZs() {
		return addrZs;
	}
	public String getWorkPlaceId() {
		return workPlaceId;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public String getVisitDept() {
		return visitDept;
	}
	public String getVisitDeptName() {
		return visitDeptName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public String getRequestDoctor() {
		return requestDoctor;
	}
	public String getRequestDoctorName() {
		return requestDoctorName;
	}
	public String getRequestDept() {
		return requestDept;
	}
	public String getRequestDeptName() {
		return requestDeptName;
	}
	public String getConfirmTime() {
		return confirmTime;
	}
	public String getConfirmPerson() {
		return confirmPerson;
	}
	public String getConfirmPersonName() {
		return confirmPersonName;
	}
	public String getTranscriberFromDate() {
		return transcriberFromDate;
	}
	public String getTranscriberToDate() {
		return transcriberToDate;
	}
	public String getTranscriberId() {
		return transcriberId;
	}
	public String getTranscriberName() {
		return transcriberName;
	}
	public List<LisReq> getRequestList() {
		return requestList;
	}
	public String getVisitHospitalCode() {
		return visitHospitalCode;
	}
	public String getVisitHospitalName() {
		return visitHospitalName;
	}
	public String getDiagnosisType() {
		return diagnosisType;
	}
	public String getDiagnosisTypeName() {
		return diagnosisTypeName;
	}
	public String getDiagnosisNo() {
		return diagnosisNo;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setNewUpFlg(String newUpFlg) {
		this.newUpFlg = newUpFlg;
	}
	public void setPatientDomain(String patientDomain) {
		this.patientDomain = patientDomain;
	}
	public void setPatientLid(String patientLid) {
		this.patientLid = patientLid;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setAdmissionWards(String admissionWards) {
		this.admissionWards = admissionWards;
	}
	public void setAdmissionWardsName(String admissionWardsName) {
		this.admissionWardsName = admissionWardsName;
	}
	public void setAdmissionBedNo(String admissionBedNo) {
		this.admissionBedNo = admissionBedNo;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public void setBirthTime(String birthTime) {
		this.birthTime = birthTime;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setAddrCs(String addrCs) {
		this.addrCs = addrCs;
	}
	public void setAddrZs(String addrZs) {
		this.addrZs = addrZs;
	}
	public void setWorkPlaceId(String workPlaceId) {
		this.workPlaceId = workPlaceId;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public void setVisitDept(String visitDept) {
		this.visitDept = visitDept;
	}
	public void setVisitDeptName(String visitDeptName) {
		this.visitDeptName = visitDeptName;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public void setRequestDoctor(String requestDoctor) {
		this.requestDoctor = requestDoctor;
	}
	public void setRequestDoctorName(String requestDoctorName) {
		this.requestDoctorName = requestDoctorName;
	}
	public void setRequestDept(String requestDept) {
		this.requestDept = requestDept;
	}
	public void setRequestDeptName(String requestDeptName) {
		this.requestDeptName = requestDeptName;
	}
	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}
	public void setConfirmPerson(String confirmPerson) {
		this.confirmPerson = confirmPerson;
	}
	public void setConfirmPersonName(String confirmPersonName) {
		this.confirmPersonName = confirmPersonName;
	}
	public void setTranscriberFromDate(String transcriberFromDate) {
		this.transcriberFromDate = transcriberFromDate;
	}
	public void setTranscriberToDate(String transcriberToDate) {
		this.transcriberToDate = transcriberToDate;
	}
	public void setTranscriberId(String transcriberId) {
		this.transcriberId = transcriberId;
	}
	public void setTranscriberName(String transcriberName) {
		this.transcriberName = transcriberName;
	}
	public void setRequestList(List<LisReq> requestList) {
		this.requestList = requestList;
	}
	public void setVisitHospitalCode(String visitHospitalCode) {
		this.visitHospitalCode = visitHospitalCode;
	}
	public void setVisitHospitalName(String visitHospitalName) {
		this.visitHospitalName = visitHospitalName;
	}
	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}
	public void setDiagnosisTypeName(String diagnosisTypeName) {
		this.diagnosisTypeName = diagnosisTypeName;
	}
	public void setDiagnosisNo(String diagnosisNo) {
		this.diagnosisNo = diagnosisNo;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
}
