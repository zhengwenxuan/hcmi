package com.hjw.webService.service.Databean;

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
public class ControlActDiagnosisPriceProcess{

	@XmlElement 
  private List<DiagnosisPrice> List= new ArrayList<DiagnosisPrice>();

	public List<DiagnosisPrice> getList() {
		return List;
	}

	public void setList(List<DiagnosisPrice> list) {
		List = list;
	}

  
	
}
