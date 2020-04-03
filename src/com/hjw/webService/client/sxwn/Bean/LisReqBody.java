package com.hjw.webService.client.sxwn.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "string")  
@XmlType(propOrder = {})  
public class LisReqBody {
	@XmlElement
  private LisReqNewDataSet NewDataSet= new LisReqNewDataSet();

	public LisReqNewDataSet getNewDataSet() {
		return NewDataSet;
	}

	public void setNewDataSet(LisReqNewDataSet newDataSet) {
		NewDataSet = newDataSet;
	}	
}
