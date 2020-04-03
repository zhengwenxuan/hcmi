package com.hjw.webService.client.yichang.bean.cdr.client.header.sender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "sender")  
@XmlType(propOrder = {})
public class SenderYC {
	
	@XmlAttribute
	private String typeCode = "SND";//
	
	@XmlElement
	private TelecomSender telecom = new TelecomSender();//
	@XmlElement
	private DeviceSender device = new DeviceSender();//
	
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public TelecomSender getTelecom() {
		return telecom;
	}
	public void setTelecom(TelecomSender telecom) {
		this.telecom = telecom;
	}
	public DeviceSender getDevice() {
		return device;
	}
	public void setDevice(DeviceSender device) {
		this.device = device;
	}
	
}
