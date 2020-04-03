package com.hjw.webService.client.yichang.bean.cdr.client.header.receiver;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "device")  
@XmlType(propOrder = {})
public class DeviceReceiver {
	
	@XmlAttribute
	private String classCode = "DEV";//
	@XmlAttribute
	private String determinerCode = "INSTANCE";//
	
	@XmlElement
	private IdReceiver id = new IdReceiver();//
	@XmlElement
	private TelecomReceiver telecom = new TelecomReceiver();//
	@XmlElement
	private SoftwareNameReceiver softwareName = new SoftwareNameReceiver();//
	
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getDeterminerCode() {
		return determinerCode;
	}
	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}
	public IdReceiver getId() {
		return id;
	}
	public void setId(IdReceiver id) {
		this.id = id;
	}
	public TelecomReceiver getTelecom() {
		return telecom;
	}
	public void setTelecom(TelecomReceiver telecom) {
		this.telecom = telecom;
	}
	public SoftwareNameReceiver getSoftwareName() {
		return softwareName;
	}
	public void setSoftwareName(SoftwareNameReceiver softwareName) {
		this.softwareName = softwareName;
	}
	
}
