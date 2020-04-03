package com.hjw.webService.client.sxwn.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "NewDataSet")  
@XmlType(propOrder = {})  
public class LisResNewDataSet {
	@XmlElement
 private List<LISReport> LISReport= new ArrayList<LISReport>();

	public List<LISReport> getLISReport() {
		return LISReport;
	}

	public void setLISReport(List<LISReport> lISReport) {
		LISReport = lISReport;
	}

	
}
