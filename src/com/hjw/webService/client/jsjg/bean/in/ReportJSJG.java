package com.hjw.webService.client.jsjg.bean.in;

public class ReportJSJG {

	private String ReportNo = "";//报告单号
	private String HospitalCode = "";//医院代码
	private String RptunitID = "";//报告单元ID
	private String ApplyOrderNo = "";//申请单号
	private String BarCode = "";//条码号
	private String PatientNo = "";//患者编号
	private String VisitType = "";//就诊类型
	private String VisitNo = "";//就诊编号
	private String SampleCode = "";//标本类型代码
	private String SampleName = "";//标本类型名称
	private String SampleDesc = "";//标本备注描述
	private String SampleNo = "";//样本号
	private String ReportClass = "";//检验分类代码
	private String ReportClassName = "";//检验分类名称
	private String ItemCode = "";//检验项目代码
	private String ItemName = "";//检验项目名称
	private String TestItemId = "";//小项目关联码
	private String EnglishName = "";//英文简写
	private String ChineseName = "";//简写符号
	private String ApplyDatetime = "";//申请时间
	private String ApplyDoctorNo = "";//申请医生代码
	private String ApplyDoctorName = "";//申请医生名称
	private String DrawDocCode = "";//采样医生工号
	private String DrawDocName = "";//采样医生姓名
	private String SampleTime = "";//采样时间
	private String ReciveDoc = "";//签收医生工号
	private String ReciveDocName = "";//签收医生姓名
	private String ReciveTime = "";//签收时间
	private String ReportDoc = "";//报告医生工号
	private String ReportDocName = "";//报告医生姓名
	private String ReportTime = "";//报告时间
	private String BodySite = "";//采样部位
	private String ExamDept = "";//检验科室代码
	private String ExamDeptName = "";//检验科室名称
	private String VerifiDoc = "";//审核医生
	private String VerifiDocName = "";//审核医生姓名
	private String VerifiTime = "";//审核时间
	private String VerifiTime2 = "";//二审时间
	private String VerifiDoc2 = "";//二审医生代码
	private String VerifiDocName2 = "";//二审医生名称
	private String LastUpdateTime = "";//最后修改时间
	private String BeforeReportTime = "";//预出报告时间
	private String LastPrintTime = "";//最后打印时间
	private String PrintNum = "";//总打印次数
	private String ReleaseTime = "";//发布时间
	private String ReleasePerson = "";//发布人
	private String ChargeFlag = "";//计价标志
	private String ApplyReason = "";//送检目的
	private String IsUpdate = "";//是否有过改动
	private String IsCheckUpdate = "";//审核后是否有过改动
	private String DeleteReason = "";//删除原因
	private String UpTime = "";//上传时间
	private String UpInfo = "";//上级信息
	private String OutPrintNum = "";//外部打印次数
	private String Status = "";//报告状态
	private Results Results = new Results();//报告结果集合
	
	public String getReportNo() {
		return ReportNo;
	}
	public String getHospitalCode() {
		return HospitalCode;
	}
	public String getRptunitID() {
		return RptunitID;
	}
	public String getApplyOrderNo() {
		return ApplyOrderNo;
	}
	public String getBarCode() {
		return BarCode;
	}
	public String getPatientNo() {
		return PatientNo;
	}
	public String getVisitType() {
		return VisitType;
	}
	public String getVisitNo() {
		return VisitNo;
	}
	public String getSampleCode() {
		return SampleCode;
	}
	public String getSampleName() {
		return SampleName;
	}
	public String getSampleDesc() {
		return SampleDesc;
	}
	public String getSampleNo() {
		return SampleNo;
	}
	public String getReportClass() {
		return ReportClass;
	}
	public String getReportClassName() {
		return ReportClassName;
	}
	public String getItemCode() {
		return ItemCode;
	}
	public String getItemName() {
		return ItemName;
	}
	public String getTestItemId() {
		return TestItemId;
	}
	public String getEnglishName() {
		return EnglishName;
	}
	public String getChineseName() {
		return ChineseName;
	}
	public String getApplyDatetime() {
		return ApplyDatetime;
	}
	public String getApplyDoctorNo() {
		return ApplyDoctorNo;
	}
	public String getApplyDoctorName() {
		return ApplyDoctorName;
	}
	public String getDrawDocCode() {
		return DrawDocCode;
	}
	public String getDrawDocName() {
		return DrawDocName;
	}
	public String getSampleTime() {
		return SampleTime;
	}
	public String getReciveDoc() {
		return ReciveDoc;
	}
	public String getReciveDocName() {
		return ReciveDocName;
	}
	public String getReciveTime() {
		return ReciveTime;
	}
	public String getReportDoc() {
		return ReportDoc;
	}
	public String getReportDocName() {
		return ReportDocName;
	}
	public String getReportTime() {
		return ReportTime;
	}
	public String getBodySite() {
		return BodySite;
	}
	public String getExamDept() {
		return ExamDept;
	}
	public String getExamDeptName() {
		return ExamDeptName;
	}
	public String getVerifiDoc() {
		return VerifiDoc;
	}
	public String getVerifiDocName() {
		return VerifiDocName;
	}
	public String getVerifiTime() {
		return VerifiTime;
	}
	public String getVerifiTime2() {
		return VerifiTime2;
	}
	public String getVerifiDoc2() {
		return VerifiDoc2;
	}
	public String getVerifiDocName2() {
		return VerifiDocName2;
	}
	public String getLastUpdateTime() {
		return LastUpdateTime;
	}
	public String getBeforeReportTime() {
		return BeforeReportTime;
	}
	public String getLastPrintTime() {
		return LastPrintTime;
	}
	public String getPrintNum() {
		return PrintNum;
	}
	public String getReleaseTime() {
		return ReleaseTime;
	}
	public String getReleasePerson() {
		return ReleasePerson;
	}
	public String getChargeFlag() {
		return ChargeFlag;
	}
	public String getApplyReason() {
		return ApplyReason;
	}
	public String getIsUpdate() {
		return IsUpdate;
	}
	public String getIsCheckUpdate() {
		return IsCheckUpdate;
	}
	public String getDeleteReason() {
		return DeleteReason;
	}
	public String getUpTime() {
		return UpTime;
	}
	public String getUpInfo() {
		return UpInfo;
	}
	public String getOutPrintNum() {
		return OutPrintNum;
	}
	public String getStatus() {
		return Status;
	}
	public Results getResults() {
		return Results;
	}
	public void setReportNo(String reportNo) {
		ReportNo = reportNo;
	}
	public void setHospitalCode(String hospitalCode) {
		HospitalCode = hospitalCode;
	}
	public void setRptunitID(String rptunitID) {
		RptunitID = rptunitID;
	}
	public void setApplyOrderNo(String applyOrderNo) {
		ApplyOrderNo = applyOrderNo;
	}
	public void setBarCode(String barCode) {
		BarCode = barCode;
	}
	public void setPatientNo(String patientNo) {
		PatientNo = patientNo;
	}
	public void setVisitType(String visitType) {
		VisitType = visitType;
	}
	public void setVisitNo(String visitNo) {
		VisitNo = visitNo;
	}
	public void setSampleCode(String sampleCode) {
		SampleCode = sampleCode;
	}
	public void setSampleName(String sampleName) {
		SampleName = sampleName;
	}
	public void setSampleDesc(String sampleDesc) {
		SampleDesc = sampleDesc;
	}
	public void setSampleNo(String sampleNo) {
		SampleNo = sampleNo;
	}
	public void setReportClass(String reportClass) {
		ReportClass = reportClass;
	}
	public void setReportClassName(String reportClassName) {
		ReportClassName = reportClassName;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public void setTestItemId(String testItemId) {
		TestItemId = testItemId;
	}
	public void setEnglishName(String englishName) {
		EnglishName = englishName;
	}
	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}
	public void setApplyDatetime(String applyDatetime) {
		ApplyDatetime = applyDatetime;
	}
	public void setApplyDoctorNo(String applyDoctorNo) {
		ApplyDoctorNo = applyDoctorNo;
	}
	public void setApplyDoctorName(String applyDoctorName) {
		ApplyDoctorName = applyDoctorName;
	}
	public void setDrawDocCode(String drawDocCode) {
		DrawDocCode = drawDocCode;
	}
	public void setDrawDocName(String drawDocName) {
		DrawDocName = drawDocName;
	}
	public void setSampleTime(String sampleTime) {
		SampleTime = sampleTime;
	}
	public void setReciveDoc(String reciveDoc) {
		ReciveDoc = reciveDoc;
	}
	public void setReciveDocName(String reciveDocName) {
		ReciveDocName = reciveDocName;
	}
	public void setReciveTime(String reciveTime) {
		ReciveTime = reciveTime;
	}
	public void setReportDoc(String reportDoc) {
		ReportDoc = reportDoc;
	}
	public void setReportDocName(String reportDocName) {
		ReportDocName = reportDocName;
	}
	public void setReportTime(String reportTime) {
		ReportTime = reportTime;
	}
	public void setBodySite(String bodySite) {
		BodySite = bodySite;
	}
	public void setExamDept(String examDept) {
		ExamDept = examDept;
	}
	public void setExamDeptName(String examDeptName) {
		ExamDeptName = examDeptName;
	}
	public void setVerifiDoc(String verifiDoc) {
		VerifiDoc = verifiDoc;
	}
	public void setVerifiDocName(String verifiDocName) {
		VerifiDocName = verifiDocName;
	}
	public void setVerifiTime(String verifiTime) {
		VerifiTime = verifiTime;
	}
	public void setVerifiTime2(String verifiTime2) {
		VerifiTime2 = verifiTime2;
	}
	public void setVerifiDoc2(String verifiDoc2) {
		VerifiDoc2 = verifiDoc2;
	}
	public void setVerifiDocName2(String verifiDocName2) {
		VerifiDocName2 = verifiDocName2;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		LastUpdateTime = lastUpdateTime;
	}
	public void setBeforeReportTime(String beforeReportTime) {
		BeforeReportTime = beforeReportTime;
	}
	public void setLastPrintTime(String lastPrintTime) {
		LastPrintTime = lastPrintTime;
	}
	public void setPrintNum(String printNum) {
		PrintNum = printNum;
	}
	public void setReleaseTime(String releaseTime) {
		ReleaseTime = releaseTime;
	}
	public void setReleasePerson(String releasePerson) {
		ReleasePerson = releasePerson;
	}
	public void setChargeFlag(String chargeFlag) {
		ChargeFlag = chargeFlag;
	}
	public void setApplyReason(String applyReason) {
		ApplyReason = applyReason;
	}
	public void setIsUpdate(String isUpdate) {
		IsUpdate = isUpdate;
	}
	public void setIsCheckUpdate(String isCheckUpdate) {
		IsCheckUpdate = isCheckUpdate;
	}
	public void setDeleteReason(String deleteReason) {
		DeleteReason = deleteReason;
	}
	public void setUpTime(String upTime) {
		UpTime = upTime;
	}
	public void setUpInfo(String upInfo) {
		UpInfo = upInfo;
	}
	public void setOutPrintNum(String outPrintNum) {
		OutPrintNum = outPrintNum;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public void setResults(Results results) {
		Results = results;
	}
}
