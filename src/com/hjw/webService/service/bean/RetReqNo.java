package com.hjw.webService.service.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ITEM")  
@XmlType(propOrder = {})  
public class RetReqNo {
	@XmlElement 
	private String REQ_NO="";//申请单号
	@XmlElement 
	private double COST=0;	

	public double getCOST() {
		return COST;
	}

	public void setCOST(double cOST) {
		COST = cOST;
	}

	public String getREQ_NO() {
		return REQ_NO;
	}

	public void setREQ_NO(String rEQ_NO) {
		REQ_NO = rEQ_NO;
	}
   
	
}
