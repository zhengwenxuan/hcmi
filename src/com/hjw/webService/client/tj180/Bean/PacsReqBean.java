package com.hjw.webService.client.tj180.Bean;

public class PacsReqBean {
	private String unionProjectId="";//	体检项目编码
	private String itemCode="";	//诊疗项目编码
	private String examClass="";//	检验标本类型 	如：尿液、血清、粪便等
	private String performedBy="";//	执行科室代码
	private String examSubClass="";//检查子类型
	private String examNo="";//检查流水号
	
	public String getExamClass() {
		return examClass;
	}
	public void setExamClass(String examClass) {
		this.examClass = examClass;
	}
	public String getExamSubClass() {
		return examSubClass;
	}
	public void setExamSubClass(String examSubClass) {
		this.examSubClass = examSubClass;
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
	
	public String getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
	public String getExamNo() {
		return examNo;
	}
	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

}
