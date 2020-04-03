package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ITEMS")  
@XmlType(propOrder = {})  
public class YbControlActProcessItems {

	@XmlElement  
	private List<YbCustom> ITEM=new ArrayList<YbCustom>();

	public List<YbCustom> getITEM() {
		return ITEM;
	}

	public void setITEM(List<YbCustom> iTEM) {
		ITEM = iTEM;
	}
    
	
}
