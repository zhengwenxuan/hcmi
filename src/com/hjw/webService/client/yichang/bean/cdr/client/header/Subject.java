package com.hjw.webService.client.yichang.bean.cdr.client.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "subject")  
@XmlType(propOrder = {})
public class Subject {
	
	@XmlAttribute
	private String typeCode = "SUBJ";//
	
	@XmlElement
	private RequestYC request = new RequestYC();//

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public RequestYC getRequest() {
		return request;
	}

	public void setRequest(RequestYC request) {
		this.request = request;
	}

}
