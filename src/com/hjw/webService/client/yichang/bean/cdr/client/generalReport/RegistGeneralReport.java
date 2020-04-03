package com.hjw.webService.client.yichang.bean.cdr.client.generalReport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registGeneralReport")  
@XmlType(propOrder = {})
public class RegistGeneralReport {
	
	@XmlElement
	private RegistGeneralReportReq registGeneralReportReq = new RegistGeneralReportReq();//

	public RegistGeneralReportReq getRegistGeneralReportReq() {
		return registGeneralReportReq;
	}

	public void setRegistGeneralReportReq(RegistGeneralReportReq registGeneralReportReq) {
		this.registGeneralReportReq = registGeneralReportReq;
	}
}
