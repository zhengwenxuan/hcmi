package com.hjw.webService.client.tj180.Bean;

public class HisGetResItemBean {
	private String unionProjectId="";//	已收费体检项目编码
	private double amount;//缴费金额   小数点后2位
	
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
 
	
}
