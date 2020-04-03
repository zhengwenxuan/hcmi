package com.hjw.webService.client.dashiqiao.FeeReqBean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.Bean.Fee;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "PROJECTS")  
@XmlType(propOrder = {})  
public class FeesDSQ {
	@XmlElement
	  private List<FeeDSQ> PROJECT = new ArrayList<FeeDSQ>();

	public List<FeeDSQ> getPROJECT() {
		return PROJECT;
	}

	public void setPROJECT(List<FeeDSQ> pROJECT) {
		PROJECT = pROJECT;
	}

	
}