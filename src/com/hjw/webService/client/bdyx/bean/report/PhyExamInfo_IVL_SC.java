package com.hjw.webService.client.bdyx.bean.report;

public class PhyExamInfo_IVL_SC {

	private String phyExamCode = "";//检查项目编码（VL_SC）
	private String phyExamName = "";//检查项目名称（VL_SC）
	private String phyExamLowValue = "";//检查结果定量低值（VL_SC）
	private String phyExamHighValue = "";//检查结果定量高值（VL_SC）
	
	public String getPhyExamCode() {
		return phyExamCode;
	}
	public String getPhyExamName() {
		return phyExamName;
	}
	public String getPhyExamLowValue() {
		return phyExamLowValue;
	}
	public String getPhyExamHighValue() {
		return phyExamHighValue;
	}
	public void setPhyExamCode(String phyExamCode) {
		this.phyExamCode = phyExamCode;
	}
	public void setPhyExamName(String phyExamName) {
		this.phyExamName = phyExamName;
	}
	public void setPhyExamLowValue(String phyExamLowValue) {
		this.phyExamLowValue = phyExamLowValue;
	}
	public void setPhyExamHighValue(String phyExamHighValue) {
		this.phyExamHighValue = phyExamHighValue;
	}
}
