package com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "HIPMessageServer")  
@XmlType(propOrder = {})
public class HIPMessageServer {

	@XmlElement
	private String action = "RegistryOrderInfo";//
	
	@XmlElement
	private String message = "";//

	public String getAction() {
		return action;
	}

	public String getMessage() {
		return message;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
