package com.hjw.webService.client.bdyx.bean.lis.res;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;

public class LisResultBDYX {

	private static String HOSPITAL_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
	}
	private String versionNumber = "";//文档的操作版本
	private String sourceSystemId = "";//报告号root
	private String signatureId = "";//电子签章号
	private String reportNo = "";//报告号
	private String effectiveTime = "";//文档生效日期
	private String documentName = "";//文档标题
	private String patientDomain = "";//域ID
	private String patientLid = "";//患者ID
	private String medicalNo = "";//就诊号
	private String visitTimes = "1";//就诊次数
	private String visitOrdNo = "";//就诊流水号
	private String visitType = "";//就诊类别编码
	private String visitTypeName = "";//就诊类别名称
	private String patientName = "";//患者名称
	private String genderCode = "";//性别编码
	private String genderName = "";//性别名称
	private String birthDate = "";//出生日期
	private String age = "";//年龄
	private String requestTime = "";//申请日期
	private String visitDept = "";//申请科室编码
	private String visitDeptName = "";//申请科室名称
	private String wardsNo = "";//病区编码
	private String wardsName = "";//病区名称
	private String roomNo = "";//病房号
	private String bedNo = "";//床位号
	private List<ReviewDoctorInfo> reviewDoctorInfo = new ArrayList<>();//0..n 审核医生信息
	private String testerId = "";//检验医师编码
	private String testerName = "";//检验医师名称
	private String labDept = "";//检验科室编码
	private String labDeptName = "";//检验科室名称
	private String applyPerson = "";//送检医生编码
	private String applyPersonName = "";//送检医生名称
	private List<ReportDoctorInfo> reportDoctorInfo = new ArrayList<>();//1..n 报告医生信息
	
	private String orgCode = HOSPITAL_ID;//医疗机构编码
	private String orgName = "武威医学科学院";//医疗机构名称
	private String docImageContent = "";//整张图像编码
	private String docImageFormat = "";//图像格式
	private String reportMemo = "";//报告备注
	private String techMemo = "";//技术备注
	private String surface = "";//表现现象
	private String HISMemo = "";//HIS相关备注
	private String medObCode = "";//药观编码
	private String medObName = "";//药观名称
	private String labTpyeCode = "";//检验类别编码
	private String labTypeName = "";//检验类别名称
	private String priorityCode = "";//优先级别
	private String method = "";//方法
	private String promptMsg = "";//提示信息
	private List<String> orderNo = new ArrayList<>();//1..n 关联医嘱号
	private List<ReportLisBDYX> report = new ArrayList<>();//1..n 检验项信息
	private Sample sample = new Sample();//1..1 标本及图像信息
	
	public static void main(String[] args) {
		String jsonStr = "";
		LisResultBDYX lisResultBDYX = new Gson().fromJson(jsonStr, LisResultBDYX.class);
		System.out.println(lisResultBDYX);
	}
	
	public String getVersionNumber() {
		return versionNumber;
	}
	public String getSourceSystemId() {
		return sourceSystemId;
	}
	public String getSignatureId() {
		return signatureId;
	}
	public String getReportNo() {
		return reportNo;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public String getDocumentName() {
		return documentName;
	}
	public String getPatientDomain() {
		return patientDomain;
	}
	public String getPatientLid() {
		return patientLid;
	}
	public String getMedicalNo() {
		return medicalNo;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getVisitType() {
		return visitType;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public String getGenderName() {
		return genderName;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getAge() {
		return age;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public String getVisitDept() {
		return visitDept;
	}
	public String getVisitDeptName() {
		return visitDeptName;
	}
	public String getWardsNo() {
		return wardsNo;
	}
	public String getWardsName() {
		return wardsName;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public String getBedNo() {
		return bedNo;
	}
	public String getTesterId() {
		return testerId;
	}
	public String getTesterName() {
		return testerName;
	}
	public String getLabDept() {
		return labDept;
	}
	public String getLabDeptName() {
		return labDeptName;
	}
	public String getApplyPerson() {
		return applyPerson;
	}
	public String getApplyPersonName() {
		return applyPersonName;
	}
	public List<ReportDoctorInfo> getReportDoctorInfo() {
		return reportDoctorInfo;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public String getDocImageContent() {
		return docImageContent;
	}
	public String getDocImageFormat() {
		return docImageFormat;
	}
	public String getReportMemo() {
		return reportMemo;
	}
	public String getTechMemo() {
		return techMemo;
	}
	public String getSurface() {
		return surface;
	}
	public String getHISMemo() {
		return HISMemo;
	}
	public String getMedObCode() {
		return medObCode;
	}
	public String getMedObName() {
		return medObName;
	}
	public String getLabTpyeCode() {
		return labTpyeCode;
	}
	public String getLabTypeName() {
		return labTypeName;
	}
	public String getPriorityCode() {
		return priorityCode;
	}
	public String getMethod() {
		return method;
	}
	public String getPromptMsg() {
		return promptMsg;
	}
	public List<ReportLisBDYX> getReport() {
		return report;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}
	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public void setPatientDomain(String patientDomain) {
		this.patientDomain = patientDomain;
	}
	public void setPatientLid(String patientLid) {
		this.patientLid = patientLid;
	}
	public void setMedicalNo(String medicalNo) {
		this.medicalNo = medicalNo;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public void setVisitDept(String visitDept) {
		this.visitDept = visitDept;
	}
	public void setVisitDeptName(String visitDeptName) {
		this.visitDeptName = visitDeptName;
	}
	public void setWardsNo(String wardsNo) {
		this.wardsNo = wardsNo;
	}
	public void setWardsName(String wardsName) {
		this.wardsName = wardsName;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public void setTesterId(String testerId) {
		this.testerId = testerId;
	}
	public void setTesterName(String testerName) {
		this.testerName = testerName;
	}
	public void setLabDept(String labDept) {
		this.labDept = labDept;
	}
	public void setLabDeptName(String labDeptName) {
		this.labDeptName = labDeptName;
	}
	public void setApplyPerson(String applyPerson) {
		this.applyPerson = applyPerson;
	}
	public void setApplyPersonName(String applyPersonName) {
		this.applyPersonName = applyPersonName;
	}
	public void setReportDoctorInfo(List<ReportDoctorInfo> reportDoctorInfo) {
		this.reportDoctorInfo = reportDoctorInfo;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setDocImageContent(String docImageContent) {
		this.docImageContent = docImageContent;
	}
	public void setDocImageFormat(String docImageFormat) {
		this.docImageFormat = docImageFormat;
	}
	public void setReportMemo(String reportMemo) {
		this.reportMemo = reportMemo;
	}
	public void setTechMemo(String techMemo) {
		this.techMemo = techMemo;
	}
	public void setSurface(String surface) {
		this.surface = surface;
	}
	public void setHISMemo(String hISMemo) {
		HISMemo = hISMemo;
	}
	public void setMedObCode(String medObCode) {
		this.medObCode = medObCode;
	}
	public void setMedObName(String medObName) {
		this.medObName = medObName;
	}
	public void setLabTpyeCode(String labTpyeCode) {
		this.labTpyeCode = labTpyeCode;
	}
	public void setLabTypeName(String labTypeName) {
		this.labTypeName = labTypeName;
	}
	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public void setPromptMsg(String promptMsg) {
		this.promptMsg = promptMsg;
	}
	public void setReport(List<ReportLisBDYX> report) {
		this.report = report;
	}
	public List<ReviewDoctorInfo> getReviewDoctorInfo() {
		return reviewDoctorInfo;
	}
	public void setReviewDoctorInfo(List<ReviewDoctorInfo> reviewDoctorInfo) {
		this.reviewDoctorInfo = reviewDoctorInfo;
	}
	public List<String> getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(List<String> orderNo) {
		this.orderNo = orderNo;
	}
	public Sample getSample() {
		return sample;
	}
	public void setSample(Sample sample) {
		this.sample = sample;
	}
}
