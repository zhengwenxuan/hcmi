package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "REQ_NOS")
@XmlType(propOrder = {})
public class ReqUnNo {
	@XmlElement
	private List<String> REQ_NO= new ArrayList<String>();// 申请单号，处理成功后返回

	public List<String> getREQ_NO() {
		return REQ_NO;
	}

	public void setREQ_NO(List<String> rEQ_NO) {
		REQ_NO = rEQ_NO;
	}

	
}
