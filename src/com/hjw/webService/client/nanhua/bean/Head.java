package com.hjw.webService.client.nanhua.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.util.DateTimeUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Head")  
@XmlType(propOrder = {})
public class Head{
	
	@XmlElement
	private String ResultCode = "";
	
	@XmlElement
	private String ErrorMsg = "";
	
	@XmlElement
	private String ExeDate = DateTimeUtil.shortFmt3(new Date());
	
	@XmlElement
	private String IDName = "1";
	
	@XmlElement
	private String IDCHECK = "1234";

	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public String getErrorMsg() {
		return ErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}

	public String getExeDate() {
		return ExeDate;
	}

	public void setExeDate(String exeDate) {
		ExeDate = exeDate;
	}

	public String getIDName() {
		return IDName;
	}

	public void setIDName(String iDName) {
		IDName = iDName;
	}

	public String getIDCHECK() {
		return IDCHECK;
	}

	public void setIDCHECK(String iDCHECK) {
		IDCHECK = iDCHECK;
	}
}
