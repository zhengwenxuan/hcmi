package com.hjw.webService.client.yichang.bean.cdr.client.list;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "certificateList")  
@XmlType(propOrder = {})
public class Certificate {
	
	@XmlElement
	private String certificateType = "";//
	@XmlElement
	private String certificateTypeCode = "";//
	@XmlElement
	private String idNumber = "";//
	
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCertificateTypeCode() {
		return certificateTypeCode;
	}
	public void setCertificateTypeCode(String certificateTypeCode) {
		this.certificateTypeCode = certificateTypeCode;
	}
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	
}
