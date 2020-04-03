package com.hjw.webService.client.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ResultHisBody")  
@XmlType(propOrder = {})  
public class ResultHisBody {
	@XmlElement  
	private ResultHeader ResultHeader= new ResultHeader();

	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}
}
