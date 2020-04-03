package com.hjw.webService.client.hghis.SMS.bean;

public class LoginMsg {

	//private String url;//身份认证地址，请向中国移动集团获得短信发送平台数据URL信息
	private String userAccount;//用户登录帐号
	private String password;//用户登录密码
	private String ecname;//用户企业名称
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEcname() {
		return ecname;
	}
	public void setEcname(String ecname) {
		this.ecname = ecname;
	}
	
	
	
	
	
}
