package com.hjw.webService.client.bdyx.bean;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;

public class RegisterReq {

	private static String HOSPITAL_ID = "";
	private static String REGISTREED_DEPT = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
		REGISTREED_DEPT = configService.getCenterconfigByKey("REGISTREED_DEPT").getConfig_value();
	}
	
	private String patientDomain = "02";//
	private String registeredDoctorName = "";//"徐星",
	private String registeredDate = DateTimeUtil.getDateTimes();//"20190529144716",
	private String registeredDept = REGISTREED_DEPT;//
	private String registeredTimeIntervalName = "";//"全天",
	private String registeredWayName = "体检";//
	private String visitTypeName = "体检";//
	private String visitDate = DateTimeUtil.getDateTimes();//"20190529000000",
	private String visitTimes = "1";//就诊次数,
	private String insuranceTypeName = "";//"其他",
	private String patientName = "";//"门诊测试",
	private String registeredSequence = "";//"2",
	private String registrationClass = "";//"123123",
	private String orgCode = HOSPITAL_ID;//"sdlanling1",
//	private static String orgCode = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();//医疗机构编码
	private String age = "";//"",
	private String birthDate = "";//"20100216",
	private String registrationClassName = "";//"肝胆测试",
	private String visitNo = "";//
	private String registeredTimeIntervalCode = "";//"AM",
	private String patientLid = "";//"100039695900",
	private String registeredDeptName = "体检中心";//
	private String orgName = "武威医学科学院";//"兰陵县人民医院",
	private String registeredDoctor = "";//"xuxing",
	private String visitType = "0401";//
	private String registeredWay = "";//"01",
	private String visitOrdNo = "";//"1900007873",
	private String insuranceType = "";//"05",
	private String genderCode = "";//"1",
	private String triggerEventCode = "new";//新增:new	撤销或退号:cancel
	
	public String getPatientDomain() {
		return patientDomain;
	}
	public String getRegisteredDoctorName() {
		return registeredDoctorName;
	}
	public String getRegisteredDate() {
		return registeredDate;
	}
	public String getRegisteredDept() {
		return registeredDept;
	}
	public String getRegisteredTimeIntervalName() {
		return registeredTimeIntervalName;
	}
	public String getRegisteredWayName() {
		return registeredWayName;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getInsuranceTypeName() {
		return insuranceTypeName;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getRegisteredSequence() {
		return registeredSequence;
	}
	public String getRegistrationClass() {
		return registrationClass;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getAge() {
		return age;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getRegistrationClassName() {
		return registrationClassName;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public String getRegisteredTimeIntervalCode() {
		return registeredTimeIntervalCode;
	}
	public String getPatientLid() {
		return patientLid;
	}
	public String getRegisteredDeptName() {
		return registeredDeptName;
	}
	public String getOrgName() {
		return orgName;
	}
	public String getRegisteredDoctor() {
		return registeredDoctor;
	}
	public String getVisitType() {
		return visitType;
	}
	public String getRegisteredWay() {
		return registeredWay;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getInsuranceType() {
		return insuranceType;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public String getTriggerEventCode() {
		return triggerEventCode;
	}
	public void setPatientDomain(String patientDomain) {
		this.patientDomain = patientDomain;
	}
	public void setRegisteredDoctorName(String registeredDoctorName) {
		this.registeredDoctorName = registeredDoctorName;
	}
	public void setRegisteredDate(String registeredDate) {
		this.registeredDate = registeredDate;
	}
	public void setRegisteredDept(String registeredDept) {
		this.registeredDept = registeredDept;
	}
	public void setRegisteredTimeIntervalName(String registeredTimeIntervalName) {
		this.registeredTimeIntervalName = registeredTimeIntervalName;
	}
	public void setRegisteredWayName(String registeredWayName) {
		this.registeredWayName = registeredWayName;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setInsuranceTypeName(String insuranceTypeName) {
		this.insuranceTypeName = insuranceTypeName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setRegisteredSequence(String registeredSequence) {
		this.registeredSequence = registeredSequence;
	}
	public void setRegistrationClass(String registrationClass) {
		this.registrationClass = registrationClass;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setRegistrationClassName(String registrationClassName) {
		this.registrationClassName = registrationClassName;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public void setRegisteredTimeIntervalCode(String registeredTimeIntervalCode) {
		this.registeredTimeIntervalCode = registeredTimeIntervalCode;
	}
	public void setPatientLid(String patientLid) {
		this.patientLid = patientLid;
	}
	public void setRegisteredDeptName(String registeredDeptName) {
		this.registeredDeptName = registeredDeptName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setRegisteredDoctor(String registeredDoctor) {
		this.registeredDoctor = registeredDoctor;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public void setRegisteredWay(String registeredWay) {
		this.registeredWay = registeredWay;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public void setTriggerEventCode(String triggerEventCode) {
		this.triggerEventCode = triggerEventCode;
	}
	
	public static void main(String[] args) {
		RegisterReq req = new RegisterReq();
		String str = new Gson().toJson(req, RegisterReq.class);
		System.out.println(str);
	}
}
