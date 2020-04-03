package com.hjw.webService.client.bdyx.bean.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;

public class ReportBDYX {

	private static String HOSPITAL_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
	}
	
	private String versionNumber = "0";//文档的操作版本 新增:0 修改:1 删除-1
	private String reportNo = "";//报告号
	private String effectiveTime = "";//文档生效日期
	private String documentName = "";//文档标题
	private String patientDomain = "02";//域ID
	private String patientLid = "";//患者ID
	private String medicalNo = "";//就诊号
	private String physicalExaNo = "";//体检号码
	private String visitTimes = "1";//就诊次数
	private String visitOrdNo = "";//就诊流水号
	private String visitTypeCode = "0401";//就诊类别编码
	private String visitTypeName = "体检";//就诊类别名称
	private String physicalExaDate = "";//体检日期
	private String patientName = "";//患者名称
	private String identityCard = "";//身份证号码
	private String genderCode = "";//性别编码
	private String genderName = "";//性别名称
	private String birthDate = "";//出生日期
	private String age = "";//年龄
	private String telephoneNo = "";//电话
	private String company = "";//单位
	private String reportDate = "";//报告日期
	private String reporterId = "";//报告医生编码
	private String reporterName = "";//报告医生名称
	private String orgCode = HOSPITAL_ID;//医疗机构编码
	private String orgName = "武威医学科学院";//医疗机构名称
	private String docImageContent = "";//整张图像编码
	private String docImageFormat = "";//图像格式
	private String summary = "";//总检结论及健康建议
	private String summaryDate = "";//总检日期
	private String summaryDocId = "";//主检医生编码
	private String summaryDocName = "";//主检医生名称
	private List<PhysicalExamInfo> physicalExamInfo = new ArrayList<>();//0..n 体格检查信息
	private List<ExamInfo> examInfo = new ArrayList<>();//0..n 医技检查项目信息
	private List<LabInfo> labInfo = new ArrayList<>();//0..n 实验室检验项目信息
	
	public String getVersionNumber() {
		return versionNumber;
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
	public String getPhysicalExaNo() {
		return physicalExaNo;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getVisitTypeCode() {
		return visitTypeCode;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getPhysicalExaDate() {
		return physicalExaDate;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getIdentityCard() {
		return identityCard;
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
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public String getCompany() {
		return company;
	}
	public String getReportDate() {
		return reportDate;
	}
	public String getReporterId() {
		return reporterId;
	}
	public String getReporterName() {
		return reporterName;
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
	public String getSummary() {
		return summary;
	}
	public String getSummaryDate() {
		return summaryDate;
	}
	public String getSummaryDocId() {
		return summaryDocId;
	}
	public String getSummaryDocName() {
		return summaryDocName;
	}
	public List<PhysicalExamInfo> getPhysicalExamInfo() {
		return physicalExamInfo;
	}
	public List<ExamInfo> getExamInfo() {
		return examInfo;
	}
	public List<LabInfo> getLabInfo() {
		return labInfo;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
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
	public void setPhysicalExaNo(String physicalExaNo) {
		this.physicalExaNo = physicalExaNo;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setPhysicalExaDate(String physicalExaDate) {
		this.physicalExaDate = physicalExaDate;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
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
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo = telephoneNo;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
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
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public void setSummaryDate(String summaryDate) {
		this.summaryDate = summaryDate;
	}
	public void setSummaryDocId(String summaryDocId) {
		this.summaryDocId = summaryDocId;
	}
	public void setSummaryDocName(String summaryDocName) {
		this.summaryDocName = summaryDocName;
	}
	public void setPhysicalExamInfo(List<PhysicalExamInfo> physicalExamInfo) {
		this.physicalExamInfo = physicalExamInfo;
	}
	public void setExamInfo(List<ExamInfo> examInfo) {
		examInfo = examInfo;
	}
	public void setLabInfo(List<LabInfo> labInfo) {
		this.labInfo = labInfo;
	}
}
