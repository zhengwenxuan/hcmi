package com.hjw.webService.client.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "List")
@XmlType(propOrder = {})
public class ReqNo {
	@XmlElement
	private String REQ_NO;// 申请单号，处理成功后返回

	public String getREQ_NO() {
		return REQ_NO;
	}

	public void setREQ_NO(String rEQ_NO) {
		REQ_NO = rEQ_NO;
	}

}
