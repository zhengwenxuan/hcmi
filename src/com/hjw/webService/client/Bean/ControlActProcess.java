package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ControlActProcess")  
@XmlType(propOrder = {})  
public class ControlActProcess {
 
	@XmlElement  
	private List<CustomResBean> LIST=new ArrayList<CustomResBean>();

	public List<CustomResBean> getLIST() {
		return LIST;
	}

	public void setLIST(List<CustomResBean> lIST) {
		LIST = lIST;
	}
	
}
