package com.hjw.webService.client.yichang.bean.cdr.client.list;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "medicalInsuranceInfoList")  
@XmlType(propOrder = {})
public class PatientMainList {
	
	@XmlElement
	private String contactAddressl = "";//
	@XmlElement
	private String contactName = "";//
	@XmlElement
	private String contactRelationship = "";//
	@XmlElement
	private String contactsPhonenumber = "";//
	
	public String getContactAddressl() {
		return contactAddressl;
	}
	public void setContactAddressl(String contactAddressl) {
		this.contactAddressl = contactAddressl;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactRelationship() {
		return contactRelationship;
	}
	public void setContactRelationship(String contactRelationship) {
		this.contactRelationship = contactRelationship;
	}
	public String getContactsPhonenumber() {
		return contactsPhonenumber;
	}
	public void setContactsPhonenumber(String contactsPhonenumber) {
		this.contactsPhonenumber = contactsPhonenumber;
	}
	
}
