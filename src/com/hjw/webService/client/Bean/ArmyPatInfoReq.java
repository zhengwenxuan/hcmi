package com.hjw.webService.client.Bean;

public class ArmyPatInfoReq {
	private String organizationOutpId="";//	集体ID号 	院方提供，只要录入一次保存可以了一个单位一个ID号 客户合同单位代码
	private String organizationId="";//	所属团队编码	单位编号 //对应批次编号
	private String organizationName="";//	所属团队名称
	
	public String getOrganizationOutpId() {
		return organizationOutpId;
	}
	public void setOrganizationOutpId(String organizationOutpId) {
		this.organizationOutpId = organizationOutpId;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

}
