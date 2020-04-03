package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class DataMessage {
	@XmlElement 
	private AuthHeader AuthHeader= new AuthHeader();
	
	public AuthHeader getAuthHeader() {
		return AuthHeader;
	}
	public void setAuthHeader(AuthHeader authHeader) {
		AuthHeader = authHeader;
	}
}
