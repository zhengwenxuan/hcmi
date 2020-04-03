package com.hjw.webService.client.hghis.SMS.bean;
//6.2.5.发送模板短信
public class SendTSMS {
	//手机号码数组，允许群发信息，该字符串数组中的每个字符串代表一个手机号码。群发短信单批最大号码数组为每批5000条。
	private String[] mobiles;
	
	//模版ID，模板由用户在中国移动集团提供的客户业务平台上，由客户自己增加短信模版的信息。
	private String  tempID;
	
	//模版参数，字符串数组。 模板采用模板和参数合成的方式产生短信， 短信内容由发送服务器自动拼接。模板参数必须和模板中定义的动态填写的参数的个数一致。
	private String[] params;
	
	//扩展码，根据向移动公司申请的通道填写，如果申请的精确匹配通道，则填写空字符串("")，否则添加移动公司允许的扩展码
	private String  addSerial;
	
	//短信优先级，取值1-5，填其余值，系统默认选择1
	//优先级1为最低，5为最高
	private long  smsPriority;
	
	//网关签名编码，必填，签名编码由企业在中国移动集团开通帐号分配
	private String  sign;
	
	
	//发送数据批次号，32位世界上唯一编码，由字母和数字组成。用户可以采用自定义的数据批次产生算法，标定每次下发的数据的批号。
	//如果不填写该参数，SDK为满足发送服务器的管理需要，会自动生成一个批次号，但是客户端取状态报告时无法分辨短信的状态报告批次。 建议填写
	private String  msgGroup;
	
	
	public String[] getMobiles() {
		return mobiles;
	}
	public void setMobiles(String[] mobiles) {
		this.mobiles = mobiles;
	}
	public String getTempID() {
		return tempID;
	}
	public void setTempID(String tempID) {
		this.tempID = tempID;
	}
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
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
	
	
}
