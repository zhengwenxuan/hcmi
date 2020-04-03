package com.hjw.webService.client.bdyx.bean.report;

public class PhyExamInfo_SC {

	private String phyExamCode = "";//检查项目编码（PQ）
	private String phyExamName = "";//检查项目名称（PQ）
	private String phyExamValue = "";//检查结果定量值（PQ）
	private String phyExamQueValueUnit = "";//检查结果值单位（PQ）
	
	public String getPhyExamCode() {
		return phyExamCode;
	}
	public String getPhyExamName() {
		return phyExamName;
	}
	public String getPhyExamValue() {
		return phyExamValue;
	}
	public String getPhyExamQueValueUnit() {
		return phyExamQueValueUnit;
	}
	public void setPhyExamCode(String phyExamCode) {
		this.phyExamCode = phyExamCode;
	}
	public void setPhyExamName(String phyExamName) {
		this.phyExamName = phyExamName;
	}
	public void setPhyExamValue(String phyExamValue) {
		this.phyExamValue = phyExamValue;
	}
	public void setPhyExamQueValueUnit(String phyExamQueValueUnit) {
		this.phyExamQueValueUnit = phyExamQueValueUnit;
	}
}
