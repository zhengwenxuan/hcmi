package com.hjw.webService.client.bdyx.bean.pacs.res;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;

public class PacsResultBDYX {

	private static String HOSPITAL_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
	}
	
	private String versionNo = "";//文档的操作版本
	private String examReportRoot = "";//报告号root
	private String signatureId = "";//电子签章号
	private String examReportLid = "";//报告号
	private String effectiveTime = "";//文档生效日期
	private String documentName = "";//文档标题
	private String patientDomain = "";//域ID
	private String patientLid = "";//患者ID
	private String medicalVisitNo = "";//就诊号
	private String visitTimes = "1";//就诊次数
	private String visitTypeCode = "";//就诊类别编码
	private String visitTypeName = "";//就诊类别名称
	private String visitOrdNo = "";//就诊流水号
	private String patientName = "";//患者名称
	private String genderCode = "";//性别编码
	private String genderName = "";//性别名称
	private String birthDate = "";//出生日期
	private String age = "";//患者年龄
	private String ward = "";//病区编码
	private String wardName = "";//病区名称
	private String bedNo = "";//床位号
	private String deptCode = "";//病人科室编码
	private String deptName = "";//病人科室名称
	private List<ReportDoctor> reportDoctors = new ArrayList<>();//1..n 报告医生信息
	private String dataEntererId = "";//记录者编码
	private String dataEntererName = "";//记录者名称
	private String orgCode = HOSPITAL_ID;//医疗机构编码
	private String orgName = "武威医学科学院";//医疗机构名称
	private List<ReviewDoctor> reviewDoctors = new ArrayList<>();//0..n 审核医生信息
	private String docImageContent = "";//整张图像编码
	private String docImageFormat = "";//图像格式
	private String requestDoctor = "1";//申请医生编码
	private String requestDoctorName = "--";//申请医生名称
	private String requestDept = "";//申请科室编码
	private String requestDeptName = "";//申请科室名称
	private List<String> orderLid = new ArrayList<>();//1..n 关联医嘱号
	private String imageIndex = "";//图像索引号
	private String examNo = "";//检查号
	private String reportTypeCode = "";//报告类型标识编码
	private String reportTypeName = "";//报告类型名称
	private String itemClass = "";//检查类型编码
	private String itemClassName = "";//检查类型名称
	private List<ExamItem> examItems = new ArrayList<>();//1..n 检查项目条目信息
	private List<DiagnosisInfo> diagnosisInfo = new ArrayList<>();//0..n 诊断信息
	
	public static void main(String[] args) {
		String jsonStr = "";
		PacsResultBDYX pacsResultBDYX = new Gson().fromJson(jsonStr, PacsResultBDYX.class);
		System.out.println(pacsResultBDYX.getOrderLid().get(0));
	}
	
	public String getVersionNo() {
		return versionNo;
	}
	public String getExamReportRoot() {
		return examReportRoot;
	}
	public String getSignatureId() {
		return signatureId;
	}
	public String getExamReportLid() {
		return examReportLid;
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
	public String getMedicalVisitNo() {
		return medicalVisitNo;
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
	public String getWard() {
		return ward;
	}
	public String getWardName() {
		return wardName;
	}
	public String getBedNo() {
		return bedNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public List<ReportDoctor> getReportDoctors() {
		return reportDoctors;
	}
	public String getDataEntererId() {
		return dataEntererId;
	}
	public String getDataEntererName() {
		return dataEntererName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public List<ReviewDoctor> getReviewDoctors() {
		return reviewDoctors;
	}
	public String getDocImageContent() {
		return docImageContent;
	}
	public String getDocImageFormat() {
		return docImageFormat;
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
	public List<String> getOrderLid() {
		return orderLid;
	}
	public String getImageIndex() {
		return imageIndex;
	}
	public String getExamNo() {
		return examNo;
	}
	public String getReportTypeCode() {
		return reportTypeCode;
	}
	public String getReportTypeName() {
		return reportTypeName;
	}
	public String getItemClass() {
		return itemClass;
	}
	public String getItemClassName() {
		return itemClassName;
	}
	public List<ExamItem> getExamItems() {
		return examItems;
	}
	public List<DiagnosisInfo> getDiagnosisInfo() {
		return diagnosisInfo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public void setExamReportRoot(String examReportRoot) {
		this.examReportRoot = examReportRoot;
	}
	public void setSignatureId(String signatureId) {
		this.signatureId = signatureId;
	}
	public void setExamReportLid(String examReportLid) {
		this.examReportLid = examReportLid;
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
	public void setMedicalVisitNo(String medicalVisitNo) {
		this.medicalVisitNo = medicalVisitNo;
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
	public void setWard(String ward) {
		this.ward = ward;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setReportDoctors(List<ReportDoctor> reportDoctors) {
		this.reportDoctors = reportDoctors;
	}
	public void setDataEntererId(String dataEntererId) {
		this.dataEntererId = dataEntererId;
	}
	public void setDataEntererName(String dataEntererName) {
		this.dataEntererName = dataEntererName;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setReviewDoctors(List<ReviewDoctor> reviewDoctors) {
		this.reviewDoctors = reviewDoctors;
	}
	public void setDocImageContent(String docImageContent) {
		this.docImageContent = docImageContent;
	}
	public void setDocImageFormat(String docImageFormat) {
		this.docImageFormat = docImageFormat;
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
	public void setOrderLid(List<String> orderLid) {
		this.orderLid = orderLid;
	}
	public void setImageIndex(String imageIndex) {
		this.imageIndex = imageIndex;
	}
	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}
	public void setReportTypeCode(String reportTypeCode) {
		this.reportTypeCode = reportTypeCode;
	}
	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}
	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}
	public void setExamItems(List<ExamItem> examItems) {
		this.examItems = examItems;
	}
	public void setDiagnosisInfo(List<DiagnosisInfo> diagnosisInfo) {
		this.diagnosisInfo = diagnosisInfo;
	}
}
