package com.hjw.webService.client.hghis.SMS.bean;

//6.2.4.短信发送bean
public class SendDSMS {

	//手机号码数组，允许群发信息，该字符串数组中的每个字符串代表一个手机号码。群发短信单批最大号码数组为每批5000条。
	private String[] mobiles;
	
	//发送短信内容
	private String  smsContent;
	
	//扩展码，根据向移动公司申请的通道填写，如果申请的精确匹配通道，则填写空字符串("")，否则添加移动公司允许的扩展码
	private String  addSerial;
	
	//短信优先级，取值1-5，填其余值，系统默认选择1, 1最低，5最高
	private long  smsPriority;
	
	//网关签名编码，必填，签名编码在中国移动集团开通帐号后分配，可以在云MAS网页端管理子系统-SMS接口管理功能中下载。
	private String  sign;
	
	//发送数据批次号，32位世界上唯一编码，由字母和数字组成。用户可以采用自定义的数据批次产生算法，标定每次下发的数据的批号。
	//如果不填写该参数，SDK为满足发送服务器的管理需要，会自动生成一个批次号，但是客户端取状态报告时无法分辨短信的状态报告批次。 建议填写
	private String  msgGroup;
	
	//是否需要上行，True代表需要；false代表不需要。 
	//目前云MAS平台默认推送上行
	private boolean isMo;
	
	public String[] getMobiles() {
		return mobiles;
	}
	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public String getAddSerial() {
		return addSerial;
	}
	public void setAddSerial(String addSerial) {
		this.addSerial = addSerial;
	}
	public long getSmsPriority() {
		return smsPriority;
	}
	public void setSmsPriority(long smsPriority) {
		this.smsPriority = smsPriority;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getMsgGroup() {
		return msgGroup;
	}
	public void setMsgGroup(String msgGroup) {
		this.msgGroup = msgGroup;
	}
	public boolean isMo() {
		return isMo;
	}
	public void setMo(boolean isMo) {
		this.isMo = isMo;
	}
	
	
	
}
