package com.hjw.webService.client.donghua.bean.lis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ResultMsgs")  
@XmlType(propOrder = {})
public class ResultMsgs {

	@XmlElement
	private List<ResultMsg> ResultMsg = new ArrayList<>();

	public List<ResultMsg> getResultMsg() {
		return ResultMsg;
	}

	public void setResultMsg(List<ResultMsg> resultMsg) {
		ResultMsg = resultMsg;
	}
}
