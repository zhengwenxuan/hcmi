package com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class ResponseXD {

	@XmlElement
	private FindOrd FindOrd = new FindOrd();//
	@XmlElement
	private String ResultCode = "0";//
	@XmlElement
	private String ErrorMsg = "成功";//

	public FindOrd getFindOrd() {
		return FindOrd;
	}

	public void setFindOrd(FindOrd findOrd) {
		FindOrd = findOrd;
	}

	public String getResultCode() {
		return ResultCode;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}
}
