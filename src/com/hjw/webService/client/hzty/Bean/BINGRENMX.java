package com.hjw.webService.client.hzty.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "BINGRENMX")  
@XmlType(propOrder = {})  
public class BINGRENMX {		
	@XmlElement 
	private List<BINGRENXX> BINGRENXX=new ArrayList<BINGRENXX>();

	public List<BINGRENXX> getBINGRENXX() {
		return BINGRENXX;
	}

	public void setBINGRENXX(List<BINGRENXX> bINGRENXX) {
		BINGRENXX = bINGRENXX;
	}
	
}
