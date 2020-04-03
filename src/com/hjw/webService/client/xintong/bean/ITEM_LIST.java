package com.hjw.webService.client.xintong.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ITEM_LIST")  
@XmlType(propOrder = {})
public class ITEM_LIST {

	@XmlElement(name = "ITEM_INFO") 
	private List<ITEM_INFO> ITEM_INFO;

	public List<ITEM_INFO> getITEM_INFO() {
		return ITEM_INFO;
	}

	public void setITEM_INFO(List<ITEM_INFO> iTEM_INFO) {
		ITEM_INFO = iTEM_INFO;
	}
	
	
}
