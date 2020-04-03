package com.hjw.webService.client.yichang.bean.cdr.server.registExamReportCDA;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registExamReportCDA")  
@XmlType(propOrder = {})
public class RegistExamReportCDA {

	@XmlElement
	private XmlIn_registExamReportCDA xmlIn_registExamReportCDA = new XmlIn_registExamReportCDA();//
	
	public XmlIn_registExamReportCDA getXmlIn_registExamReportCDA() {
		return xmlIn_registExamReportCDA;
	}

	public void setXmlIn_registExamReportCDA(XmlIn_registExamReportCDA xmlIn_registExamReportCDA) {
		this.xmlIn_registExamReportCDA = xmlIn_registExamReportCDA;
	}

}
