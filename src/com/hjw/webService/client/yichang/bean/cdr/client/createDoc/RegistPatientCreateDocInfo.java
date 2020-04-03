package com.hjw.webService.client.yichang.bean.cdr.client.createDoc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registPatientCreateDocInfo")  
@XmlType(propOrder = {})
public class RegistPatientCreateDocInfo {
	
	@XmlElement
	private RegistPatientCreateDocInfoReq registPatientCreateDocInfoReq = new RegistPatientCreateDocInfoReq();//

	public RegistPatientCreateDocInfoReq getRegistPatientCreateDocInfoReq() {
		return registPatientCreateDocInfoReq;
	}

	public void setRegistPatientCreateDocInfoReq(RegistPatientCreateDocInfoReq registPatientCreateDocInfoReq) {
		this.registPatientCreateDocInfoReq = registPatientCreateDocInfoReq;
	}
	
}
