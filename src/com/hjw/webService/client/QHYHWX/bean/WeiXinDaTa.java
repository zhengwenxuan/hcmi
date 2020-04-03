package com.hjw.webService.client.QHYHWX.bean;

public class WeiXinDaTa {
    
    private String identityCard;//身份证号
	private String patName;//缴费人
	private String fee;//缴费金额
	private String payTime;//缴费时间
	
	
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	
}
