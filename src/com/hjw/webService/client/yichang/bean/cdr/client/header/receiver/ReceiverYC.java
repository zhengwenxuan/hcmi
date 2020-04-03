package com.hjw.webService.client.yichang.bean.cdr.client.header.receiver;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "receiver")  
@XmlType(propOrder = {})
public class ReceiverYC {
	
	@XmlAttribute
	private String typeCode = "RCV";//
	
	@XmlElement
	private TelecomReceiver telecom = new TelecomReceiver();//
	@XmlElement
	private DeviceReceiver device = new DeviceReceiver();//
	
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public TelecomReceiver getTelecom() {
		return telecom;
	}
	public void setTelecom(TelecomReceiver telecom) {
		this.telecom = telecom;
	}
	public DeviceReceiver getDevice() {
		return device;
	}
	public void setDevice(DeviceReceiver device) {
		this.device = device;
	}
	
}
