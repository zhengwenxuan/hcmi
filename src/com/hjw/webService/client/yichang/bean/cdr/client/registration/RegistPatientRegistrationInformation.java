package com.hjw.webService.client.yichang.bean.cdr.client.registration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registPatientRegistrationInformation")  
@XmlType(propOrder = {})
public class RegistPatientRegistrationInformation {
	
	@XmlElement
	private RegistPatientRegistrationInformationReq registPatientRegistrationInformationReq = new RegistPatientRegistrationInformationReq();//

	public RegistPatientRegistrationInformationReq getRegistPatientRegistrationInformationReq() {
		return registPatientRegistrationInformationReq;
	}

	public void setRegistPatientRegistrationInformationReq(
			RegistPatientRegistrationInformationReq registPatientRegistrationInformationReq) {
		this.registPatientRegistrationInformationReq = registPatientRegistrationInformationReq;
	}
	
}
