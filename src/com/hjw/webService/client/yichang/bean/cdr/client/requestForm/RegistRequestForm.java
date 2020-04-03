package com.hjw.webService.client.yichang.bean.cdr.client.requestForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registRequestForm")  
@XmlType(propOrder = {})
public class RegistRequestForm {
	
	@XmlElement
	private RegistRequestReq registRequestReq = new RegistRequestReq();//

	public RegistRequestReq getRegistRequestReq() {
		return registRequestReq;
	}

	public void setRegistRequestReq(RegistRequestReq registRequestReq) {
		this.registRequestReq = registRequestReq;
	}
}
