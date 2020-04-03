package com.hjw.webService.client.tj180.Bean;

public class LisReqBean {
	private String unionProjectId="";//	体检项目编码
	private String itemCode="";	//诊疗项目编码
	private String sampleClass="";//	检验标本类型 	如：尿液、血清、粪便等
	private String performedBy="";//	执行科室代码
	private String testNo="";//检验流水号
	private String lisState="9";//收费状态 ‘9’未收费，‘0’已收费
	
	public String getLisState() {
		return lisState;
	}
	public void setLisState(String lisState) {
		this.lisState = lisState;
	}
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getSampleClass() {
		return sampleClass;
	}
	public void setSampleClass(String sampleClass) {
		this.sampleClass = sampleClass;
	}
	public String getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
	public String getTestNo() {
		return testNo;
	}
	public void setTestNo(String testNo) {
		this.testNo = testNo;
	}

}
