package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "PROJECTS")  
@XmlType(propOrder = {})  
public class Fees {
	@XmlElement
	  private List<Fee> PROJECT = new ArrayList<Fee>();

	public List<Fee> getPROJECT() {
		return PROJECT;
	}

	public void setPROJECT(List<Fee> pROJECT) {
		PROJECT = pROJECT;
	}

}
