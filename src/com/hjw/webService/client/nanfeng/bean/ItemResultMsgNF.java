package com.hjw.webService.client.nanfeng.bean;

import javax.xml.bind.annotation.XmlElement;

import com.hjw.webService.client.donghua.bean.lis.OrgResultMsgs;

public class ItemResultMsgNF {

	private String LabNo = "";//检验号
	
	private String reportNo;  //报告单号
	
	private String TestCode = "";//项目代码
	
	private String TestName = "";//项目名称
	
	private String TestEngName = "";//缩写
	
	private String Result = "";//结果 如果细菌标志为Y则该项目为细菌代码
	
	private String Units = "";//单位
	
	private String Notes = "";//备注
	
	private String ResultFlag = "";//结果异常标示 (高H,低L,异常A,警告 W, 荒诞 U)
	
	private String Ranges = "";//范围
	
	private String Sequence = "";//显示顺序
	
	private String MICFlag = "";//细菌标志 Y/N
	
	private String MICName = "";//细菌名称 名称
	
	private String WarnDesc = "";//危急值说明 W@危急
	
	private String exam_status = "";//体检状态
	
	private OrgResultMsgs OrgResultMsgs = new OrgResultMsgs();//药敏结果（list）
	
	public String getLabNo() {
		return LabNo;
	}
	public String getTestCode() {
		return TestCode;
	}
	public String getTestName() {
		return TestName;
	}
	public String getTestEngName() {
		return TestEngName;
	}
	public String getResult() {
		return Result;
	}
	public String getUnits() {
		return Units;
	}
	public String getNotes() {
		return Notes;
	}
	public String getResultFlag() {
		return ResultFlag;
	}
	public String getRanges() {
		return Ranges;
	}
	public String getSequence() {
		return Sequence;
	}
	public String getMICFlag() {
		return MICFlag;
	}
	public String getMICName() {
		return MICName;
	}
	public String getWarnDesc() {
		return WarnDesc;
	}
	public OrgResultMsgs getOrgResultMsgs() {
		return OrgResultMsgs;
	}
	public void setLabNo(String labNo) {
		LabNo = labNo;
	}
	public void setTestCode(String testCode) {
		TestCode = testCode;
	}
	public void setTestName(String testName) {
		TestName = testName;
	}
	public void setTestEngName(String testEngName) {
		TestEngName = testEngName;
	}
	public void setResult(String result) {
		Result = result;
	}
	public void setUnits(String units) {
		Units = units;
	}
	public void setNotes(String notes) {
		Notes = notes;
	}
	public void setResultFlag(String resultFlag) {
		ResultFlag = resultFlag;
	}
	public void setRanges(String ranges) {
		Ranges = ranges;
	}
	public void setSequence(String sequence) {
		Sequence = sequence;
	}
	public void setMICFlag(String mICFlag) {
		MICFlag = mICFlag;
	}
	public void setMICName(String mICName) {
		MICName = mICName;
	}
	public void setWarnDesc(String warnDesc) {
		WarnDesc = warnDesc;
	}
	public void setOrgResultMsgs(OrgResultMsgs orgResultMsgs) {
		OrgResultMsgs = orgResultMsgs;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getExam_status() {
		return exam_status;
	}
	public void setExam_status(String exam_status) {
		this.exam_status = exam_status;
	}
	
}
