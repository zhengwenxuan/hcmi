package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "AuthHeader")  
@XmlType(propOrder = {})  
public class AuthHeader {
	@XmlElement 
	private String msgType;//	消息类型
	@XmlElement 
	private String msgId;//	消息ID,本次消息发送的唯一标识
	@XmlElement 
	private String createTime;//	消息创建时间
	@XmlElement 
	private String sourceId;//	消息源系统域标识
	@XmlElement 
	private String targetId;//消息目标系统域标识
	@XmlElement 
	private String sysPassword;//系统密码，平台分配给第三方系统一个唯一的sysPassword
	@XmlElement 
	private String version;//字典版本号
	
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getSysPassword() {
		return sysPassword;
	}
	public void setSysPassword(String sysPassword) {
		this.sysPassword = sysPassword;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
