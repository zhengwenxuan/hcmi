package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "LIST")  
@XmlType(propOrder = {})  
public class YbControlActProcessList {

	@XmlElement  
	private YbControlActProcessItems ITEMS=new YbControlActProcessItems();

	public YbControlActProcessItems getITEMS() {
		return ITEMS;
	}

	public void setITEMS(YbControlActProcessItems iTEMS) {
		ITEMS = iTEMS;
	}
   
	
}
