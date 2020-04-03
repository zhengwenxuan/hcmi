package com.hjw.webService.service.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "REQ_NOS")  
@XmlType(propOrder = {})  
public class ReqItem {
	@XmlElement 
	private List<RetReqNo> ITEM= new ArrayList<RetReqNo>();//申请单号

	public List<RetReqNo> getITEM() {
		return ITEM;
	}

	public void setITEM(List<RetReqNo> iTEM) {
		ITEM = iTEM;
	}
	
	
}
