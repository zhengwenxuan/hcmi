package com.hjw.webService.client.donghua.bean.lis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ReportMsgs")  
@XmlType(propOrder = {})
public class ReportMsgs {

	@XmlElement
	private List<ReportMsg> ReportMsg = new ArrayList<>();

	public List<ReportMsg> getReportMsg() {
		return ReportMsg;
	}

	public void setReportMsg(List<ReportMsg> reportMsg) {
		ReportMsg = reportMsg;
	}
}
