package com.hjw.webService.client.donghua.bean.pacs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ReturnReports")  
@XmlType(propOrder = {})
public class ReturnReports {

	@XmlElement
	private List<ReturnReport> ReturnReport = new ArrayList<>();

	public List<ReturnReport> getReturnReport() {
		return ReturnReport;
	}

	public void setReturnReport(List<ReturnReport> returnReport) {
		ReturnReport = returnReport;
	}
}
