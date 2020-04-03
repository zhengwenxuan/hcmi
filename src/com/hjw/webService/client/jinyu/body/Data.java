package com.hjw.webService.client.jinyu.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.jinyu.lisbean.PatientInfoJY;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Data")  
@XmlType(propOrder = {})
public class Data{
	@XmlElement(name = "Data_Row") 
	private PatientInfoJY patientInfo;

	public PatientInfoJY getPatientInfo() {
		return patientInfo;
	}

	public void setPatientInfo(PatientInfoJY patientInfo) {
		this.patientInfo = patientInfo;
	}
}
