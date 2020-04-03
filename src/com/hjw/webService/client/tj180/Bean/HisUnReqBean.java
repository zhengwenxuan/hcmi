package com.hjw.webService.client.tj180.Bean;

public class HisUnReqBean{
	private String unionProjectId="";//	体检项目编码
	private String itemClass="";//	收费项目类型
	private String itemCode="";//	收费项目编码
	private String itemSpec="";//	收费项目规格 codeClass=2时，””
	private String units="";//	收费项目单位 	codeClass=2时，””
	private String codeClass="";//	收费项目编码类别 1：价表项目，2：诊疗项目	
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public String getItemClass() {
		return itemClass;
	}
	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemSpec() {
		return itemSpec;
	}
	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getCodeClass() {
		return codeClass;
	}
	public void setCodeClass(String codeClass) {
		this.codeClass = codeClass;
	}
    
}
