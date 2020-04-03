package com.hjw.webService.client.huojianwa.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Report")  
@XmlType(propOrder = {})
public class Report {

	@XmlElement
	private String ReqNo = "";//申请单号
	@XmlElement
	private String PacsCheckno = "";//PACS 报告 ID
	@XmlElement
	private String ExamNum = "";//体检号
	@XmlElement
	private List<ItemList_Report> ItemList = new ArrayList<>();//体检号
	@XmlElement
	private String ClinicDiagnose = "";//检查结论
	@XmlElement
	private String ClinicSymptom = "";//检查描述
	@XmlElement
	private String ClinicAdvice = "";//医师意见
	@XmlElement
	private String IsAbnormal = "";//阳性状态：N-正常 Y-阳性报告 C-危急
	@XmlElement
	private RptPic RptPic = new RptPic();//报告图片(BASE64 加密)
	@XmlElement
	private String StudyState = "";//检查状态，此处固定为 5
	@XmlElement
	private String RegDoc = "";//记录医生姓名
	@XmlElement
	private String CheckDoc = "";//检查医生姓名
	@XmlElement
	private String CheckDate = "";//检查时间 yyyy-MM-dd
	@XmlElement
	private String ReportDoc = "";//报告医生
	@XmlElement
	private String ReportDate = "";//报告时间 yyyy-MM-dd
	@XmlElement
	private String AuditDoc = "";//审核医师
	@XmlElement
	private String AuditDate = "";//审核时间 yyyy-MM-dd
	
	public String getReqNo() {
		return ReqNo;
	}
	public String getPacsCheckno() {
		return PacsCheckno;
	}
	public String getExamNum() {
		return ExamNum;
	}
	public String getClinicDiagnose() {
		return ClinicDiagnose;
	}
	public String getClinicSymptom() {
		return ClinicSymptom;
	}
	public String getClinicAdvice() {
		return ClinicAdvice;
	}
	public String getIsAbnormal() {
		return IsAbnormal;
	}
	public String getStudyState() {
		return StudyState;
	}
	public String getRegDoc() {
		return RegDoc;
	}
	public String getCheckDoc() {
		return CheckDoc;
	}
	public String getCheckDate() {
		return CheckDate;
	}
	public String getReportDoc() {
		return ReportDoc;
	}
	public String getReportDate() {
		return ReportDate;
	}
	public String getAuditDoc() {
		return AuditDoc;
	}
	public String getAuditDate() {
		return AuditDate;
	}
	public void setReqNo(String reqNo) {
		ReqNo = reqNo;
	}
	public void setPacsCheckno(String pacsCheckno) {
		PacsCheckno = pacsCheckno;
	}
	public void setExamNum(String examNum) {
		ExamNum = examNum;
	}
	public void setClinicDiagnose(String clinicDiagnose) {
		ClinicDiagnose = clinicDiagnose;
	}
	public void setClinicSymptom(String clinicSymptom) {
		ClinicSymptom = clinicSymptom;
	}
	public void setClinicAdvice(String clinicAdvice) {
		ClinicAdvice = clinicAdvice;
	}
	public void setIsAbnormal(String isAbnormal) {
		IsAbnormal = isAbnormal;
	}
	public void setStudyState(String studyState) {
		StudyState = studyState;
	}
	public void setRegDoc(String regDoc) {
		RegDoc = regDoc;
	}
	public void setCheckDoc(String checkDoc) {
		CheckDoc = checkDoc;
	}
	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}
	public void setReportDoc(String reportDoc) {
		ReportDoc = reportDoc;
	}
	public void setReportDate(String reportDate) {
		ReportDate = reportDate;
	}
	public void setAuditDoc(String auditDoc) {
		AuditDoc = auditDoc;
	}
	public void setAuditDate(String auditDate) {
		AuditDate = auditDate;
	}
	public List<ItemList_Report> getItemList() {
		return ItemList;
	}
	public RptPic getRptPic() {
		return RptPic;
	}
	public void setItemList(List<ItemList_Report> itemList) {
		ItemList = itemList;
	}
	public void setRptPic(RptPic rptPic) {
		RptPic = rptPic;
	}
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+ "<Report>"
	+ "<ReqNo>申请单号</ReqNo>"
	+ "<PacsCheckno>PACS 报告 ID</PacsCheckno>"
	+ "<ExamNum>体检号</ExamNum>"
	+ "<ItemList>"
		+ "<ItemName>项目名称</ItemName>"
		+ "<PacsItemCode>PACS 检查项目代码</PacsItemCode>"
		+ "<!-- 检查类别：US(超声)/MR(核磁)/CT(CT 检查)/XX(射线检查) -->"
		+ "<StudyType>检查类别</StudyType>"
		+ "<StudyBodyPart>检查部位</StudyBodyPart>"
	+ "</ItemList>"
	+ "<ClinicDiagnose>检查结论</ClinicDiagnose>"
	+ "<ClinicSymptom>检查描述</ClinicSymptom>"
	+ "<ClinicAdvice>医师意见</ClinicAdvice>"
	+ "<!-- 阳性状态：N-正常 Y-阳性报告 C-危急 -->"
	+ "<IsAbnormal>阳性状态</IsAbnormal>"
	+ "<RptPic>"
	+ "<pic>报告图片(BASE64 加密)</pic>"
	+ "</RptPic>"
	+ "<!-- 检查状态，此处固定为 5 -->"
	+ "<StudyState>5</StudyState>"
	+ "<RegDoc>记录医生姓名</RegDoc>"
	+ "<CheckDoc>检查医生姓名</CheckDoc>"
	+ "<!-- 时间格式：yyyy-MM-dd -->"
	+ "<CheckDate>检查时间</CheckDate>"
	+ "<ReportDoc>报告医生</ReportDoc>"
	+ "<!-- 时间格式：yyyy-MM-dd -->"
	+ "<ReportDate>报告时间</ReportDate>"
	+ "<AuditDoc>审核医师</AuditDoc>"
	+ "<!-- 时间格式：yyyy-MM-dd -->"
	+ "<AuditDate>审核时间</AuditDate>"
+ "</Report>";
		Report report = JaxbUtil.converyToJavaBean(xml, Report.class);
		System.out.println(report.AuditDate);
		System.out.println(report.RptPic.getPic());
		System.out.println(report.ItemList.get(0).getItemName());
	}
}
