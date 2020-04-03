package com.hjw.webService.client.bdyx.bean.pacs.req;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;

public class PacsApplyReq {

	private static String HOSPITAL_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
	}
	
	private String triggerEventCode = "new";//消息交互类型
	private String patientDomain = "02";//域ID
	private String patientLid = "";//患者ID
	private String visitNo = "";//就诊号
	private String visitTimes = "1";//就诊次数
	private String visitTypeCode = "0401";//就诊类别编码
	private String visitTypeName = "体检";//就诊类别名称
	private String visitOrdNo = "";//就诊流水号
	private String wardsName = "";//病区名
	private String wardsId = "";//病区编码
	private String bedNo = "";//床号
	private String identityCard = "";//身份证号
	private String medicareCard = "";//医保卡号
	private String patientName = "";//姓名
	private String telNum = "";//联系电话
	private String genderCode = "";//性别编码
	private String birthDate = "";//出生日期
	private String age = "";//年龄
	private String address = "";//住址
	private String marriageStatusCode = "";//婚姻状况类别编码
	private String marriageStatusName = "";//民族编码
	private String occupationCode = "";//职业编码
	private String occupationName = "";//职业
	private String workPlace = "";//工作单位名称
	private String nationalityCode = "";//国籍编码
	private String nationalityName = "";//国家
	private List<PatientContact> patientContact = new ArrayList<>();//0..n 联系人
	private String visitDept = "";//病人科室编码
	private String visitDeptName = "";//病人科室名称
	private String orgCode = HOSPITAL_ID;//医疗机构代码
	private String orgName = "武威医学科学院";//医疗机构名称
	private String orderTime = DateTimeUtil.getDateTimes();//开单时间
	private String orderPerson = "1";//开单医生编码
	private String orderPersonName = "--";//开单医生姓名
	private String orderDept = "";//申请科室编码
	private String orderDeptName = "";//申请科室名称
	private String confirmTime = "";//确认时间
	private String confirmPerson = "";//确认人编码
	private String confirmPersonName = "";//确认人姓名
	private List<ExaminationApplication> examinationApplications = new ArrayList<>();//申请单信息
	private List<Diagnose> diagnoses = new ArrayList(){{add(new Diagnose());}};//诊断信息
	private String pastDiseaseName = "";//既往史
	private String principleActionName = "";//主诉
	private String presentIllnessName = "";//现病史
	
	public String getTriggerEventCode() {
		return triggerEventCode;
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
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getVisitTypeCode() {
		return visitTypeCode;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getWardsName() {
		return wardsName;
	}
	public String getWardsId() {
		return wardsId;
	}
	public String getBedNo() {
		return bedNo;
	}
	public String getIdentityCard() {
		return identityCard;
	}
	public String getMedicareCard() {
		return medicareCard;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getTelNum() {
		return telNum;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getAge() {
		return age;
	}
	public String getAddress() {
		return address;
	}
	public String getMarriageStatusCode() {
		return marriageStatusCode;
	}
	public String getMarriageStatusName() {
		return marriageStatusName;
	}
	public String getOccupationCode() {
		return occupationCode;
	}
	public String getOccupationName() {
		return occupationName;
	}
	public String getWorkPlace() {
		return workPlace;
	}
	public String getNationalityCode() {
		return nationalityCode;
	}
	public String getNationalityName() {
		return nationalityName;
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
	public String getOrderPerson() {
		return orderPerson;
	}
	public String getOrderPersonName() {
		return orderPersonName;
	}
	public String getOrderDept() {
		return orderDept;
	}
	public String getOrderDeptName() {
		return orderDeptName;
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
	public void setTriggerEventCode(String triggerEventCode) {
		this.triggerEventCode = triggerEventCode;
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
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setWardsName(String wardsName) {
		this.wardsName = wardsName;
	}
	public void setWardsId(String wardsId) {
		this.wardsId = wardsId;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public void setMedicareCard(String medicareCard) {
		this.medicareCard = medicareCard;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setMarriageStatusCode(String marriageStatusCode) {
		this.marriageStatusCode = marriageStatusCode;
	}
	public void setMarriageStatusName(String marriageStatusName) {
		this.marriageStatusName = marriageStatusName;
	}
	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
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
	public void setOrderPerson(String orderPerson) {
		this.orderPerson = orderPerson;
	}
	public void setOrderPersonName(String orderPersonName) {
		this.orderPersonName = orderPersonName;
	}
	public void setOrderDept(String orderDept) {
		this.orderDept = orderDept;
	}
	public void setOrderDeptName(String orderDeptName) {
		this.orderDeptName = orderDeptName;
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
	public List<ExaminationApplication> getExaminationApplications() {
		return examinationApplications;
	}
	public void setExaminationApplications(List<ExaminationApplication> examinationApplications) {
		this.examinationApplications = examinationApplications;
	}
	public List<PatientContact> getPatientContact() {
		return patientContact;
	}
	public List<Diagnose> getDiagnoses() {
		return diagnoses;
	}
	public String getPastDiseaseName() {
		return pastDiseaseName;
	}
	public String getPrincipleActionName() {
		return principleActionName;
	}
	public String getPresentIllnessName() {
		return presentIllnessName;
	}
	public void setPatientContact(List<PatientContact> patientContact) {
		this.patientContact = patientContact;
	}
	public void setDiagnoses(List<Diagnose> diagnoses) {
		this.diagnoses = diagnoses;
	}
	public void setPastDiseaseName(String pastDiseaseName) {
		this.pastDiseaseName = pastDiseaseName;
	}
	public void setPrincipleActionName(String principleActionName) {
		this.principleActionName = principleActionName;
	}
	public void setPresentIllnessName(String presentIllnessName) {
		this.presentIllnessName = presentIllnessName;
	}
	
	public static void main(String[] args) {
		PacsApplyReq pacsApplyReq = new PacsApplyReq();
		pacsApplyReq.getExaminationApplications().add(new ExaminationApplication());
		pacsApplyReq.getExaminationApplications().add(new ExaminationApplication());
		String json = new Gson().toJson(pacsApplyReq, PacsApplyReq.class);
		System.out.println(json);
	}
}
