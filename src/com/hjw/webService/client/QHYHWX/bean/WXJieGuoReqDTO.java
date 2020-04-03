package com.hjw.webService.client.QHYHWX.bean;

public class WXJieGuoReqDTO {

	
	private String identityCard;//身份证号
	private String teamId;//团队ID
	private String date;//检查日期(不传日期默认返回所有体检)
	
	
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}
