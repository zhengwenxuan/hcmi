package com.hjw.webService.client.erfuyuan.bean;

public class Result_clientRegister {
	
	private String clientId="";//	体检编号
	private String name="";//	姓名
	private String sex="";//	性别
	private String firstQueue="";// 第一个项目
	private String msg="";
	
	public String getClientId() {
		return clientId;
	}
	public String getName() {
		return name;
	}
	public String getSex() {
		return sex;
	}
	public String getFirstQueue() {
		return firstQueue;
	}
	public String getMsg() {
		return msg;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setFirstQueue(String firstQueue) {
		this.firstQueue = firstQueue;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
