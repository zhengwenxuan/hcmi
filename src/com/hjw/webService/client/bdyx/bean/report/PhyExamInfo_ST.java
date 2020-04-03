package com.hjw.webService.client.bdyx.bean.report;

public class PhyExamInfo_ST {

	private String phyExamCode = "";//检查项目编码（ST）
	private String phyExamName = "";//检查项目名称（ST）
	private String phyExamTextValue = "";//检查结果文本值（ST）
	
	public String getPhyExamCode() {
		return phyExamCode;
	}
	public String getPhyExamName() {
		return phyExamName;
	}
	public String getPhyExamTextValue() {
		return phyExamTextValue;
	}
	public void setPhyExamCode(String phyExamCode) {
		this.phyExamCode = phyExamCode;
	}
	public void setPhyExamName(String phyExamName) {
		this.phyExamName = phyExamName;
	}
	public void setPhyExamTextValue(String phyExamTextValue) {
		this.phyExamTextValue = phyExamTextValue;
	}
}
