package com.hjw.webService.client.yichang.bean.cdr.client.header.sender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "device")  
@XmlType(propOrder = {})
public class DeviceSender {
	
	@XmlAttribute
	private String classCode = "DEV";//
	@XmlAttribute
	private String determinerCode = "INSTANCE";//
	
	@XmlElement
	private IdSender id = new IdSender();//
	@XmlElement
	private TelecomSender telecom = new TelecomSender();//
	@XmlElement
	private SoftwareNameSender softwareName = new SoftwareNameSender();//
	
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
	public IdSender getId() {
		return id;
	}
	public void setId(IdSender id) {
		this.id = id;
	}
	public TelecomSender getTelecom() {
		return telecom;
	}
	public void setTelecom(TelecomSender telecom) {
		this.telecom = telecom;
	}
	public SoftwareNameSender getSoftwareName() {
		return softwareName;
	}
	public void setSoftwareName(SoftwareNameSender softwareName) {
		this.softwareName = softwareName;
	}
	
}
