package com.hjw.webService.client.bjxy.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "PATIENT")
@XmlType(propOrder = {})
public class PATIENT{
	
	@XmlElement
	private String patient_id = "";

	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	
	public static void main(String[] args) throws Exception {
		String xml = "<PATIENT><patient_id>000950661100</patient_id></PATIENT>";
		PATIENT PATIENT = JaxbUtil.converyToJavaBean(xml, PATIENT.class);
		System.out.println(PATIENT.getPatient_id());
	}
}
