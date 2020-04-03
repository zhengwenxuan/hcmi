package com.hjw.webService.client.changan.Bean;

public class MSGSureTempalteSendBean {

	private String apiAccount="";  //智语平台分配的开发者帐号
	private String appId=""; //应用Id
	private String sign="";  //签名：MD5(apiAccount+apikey+timeStamp) apiKey：由智语平台分配，与apiAccount配对 
	private String timeStamp=""; //当前时间戳(精度ms)
	private String templateId=""; //短信模板ID，事先需在智语平台创建并审核通过
	private String singerid=""; //短信签名ID，事先需在智语平台创建并审核通过
	private String mobile=""; //接收的手机号码，只支持一个
	private String param=""; //变量值，与模板中的变量对应，多个已‘,’号分隔。(如：张三,zhangsan,5469) 
	private String userDate=""; //该条短信在您业务系统内的ID
	private String extNumber=""; //拓展号
	private String statusPushAddr=""; //短信发送后将向这个地址推送(运营商返回的)发送报告
	
	public String getApiAccount() {
		return apiAccount;
	}
	public void setApiAccount(String apiAccount) {
		this.apiAccount = apiAccount;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getSingerid() {
		return singerid;
	}
	public void setSingerid(String singerid) {
		this.singerid = singerid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getUserDate() {
		return userDate;
	}
	public void setUserDate(String userDate) {
		this.userDate = userDate;
	}
	public String getExtNumber() {
		return extNumber;
	}
	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}
	public String getStatusPushAddr() {
		return statusPushAddr;
	}
	public void setStatusPushAddr(String statusPushAddr) {
		this.statusPushAddr = statusPushAddr;
	}
	
	
	
	
}
