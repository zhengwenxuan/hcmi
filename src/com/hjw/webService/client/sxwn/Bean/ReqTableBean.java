package com.hjw.webService.client.sxwn.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Table2")  
@XmlType(propOrder = {})  
public class ReqTableBean {
	@XmlElement
  private int iRequestId;
	@XmlElement
  private String cRequestCode="";
	public int getiRequestId() {
		return iRequestId;
	}
	public void setiRequestId(int iRequestId) {
		this.iRequestId = iRequestId;
	}
	public String getcRequestCode() {
		return cRequestCode;
	}
	public void setcRequestCode(String cRequestCode) {
		this.cRequestCode = cRequestCode;
	}
	
	
}
