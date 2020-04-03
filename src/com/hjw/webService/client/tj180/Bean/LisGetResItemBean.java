package com.hjw.webService.client.tj180.Bean;

public class LisGetResItemBean {
	private String testNo="";//	检验申请流水号
	private String reportItemCode="";//Lis检验子项目编码
	private String reportItemName="";//Lis检验子项目名称
	private String result="";//	结果值
	private String units="";//;	单位
	private String abnormalIndicator="";//	是否异常 N：正常，L：偏低，H：偏高
	private String resultDateTime="";//	结果时间 	yyyymmddhhmmss
	private String printContext="";//	结果值区间
	private String transcriptionist="";//	检验人
	private String verifiedBy="";//	审核人
	private String inspectionId="";//
	
	public String getVerifiedBy() {
		return verifiedBy;
	}
	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}
	public String getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(String inspectionId) {
		this.inspectionId = inspectionId;
	}
	public String getReportItemCode() {
		return reportItemCode;
	}
	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}
	public String getTestNo() {
		return testNo;
	}
	public void setTestNo(String testNo) {
		this.testNo = testNo;
	}
	public String getReportItemName() {
		return reportItemName;
	}
	public void setReportItemName(String reportItemName) {
		this.reportItemName = reportItemName;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getAbnormalIndicator() {
		return abnormalIndicator;
	}
	public void setAbnormalIndicator(String abnormalIndicator) {
		this.abnormalIndicator = abnormalIndicator;
	}
	public String getResultDateTime() {
		return resultDateTime;
	}
	public void setResultDateTime(String resultDateTime) {
		this.resultDateTime = resultDateTime;
	}
	public String getPrintContext() {
		return printContext;
	}
	public void setPrintContext(String printContext) {
		this.printContext = printContext;
	}
	public String getTranscriptionist() {
		return transcriptionist;
	}
	public void setTranscriptionist(String transcriptionist) {
		this.transcriptionist = transcriptionist;
	}

}
