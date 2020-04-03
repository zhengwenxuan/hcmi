package com.hjw.webService.client.xintong.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "RESPONSE")  
@XmlType(propOrder = {})
public class RESPONSE {
	
	@XmlElement(name = "REQ_MSGID")
	private String REQ_MSGID;//请求消息ID
	@XmlElement(name = "RESP_MSGID")
	private String RESP_MSGID;//响应消息ID
	@XmlElement(name = "RETURN_CODE")
	private String RETURN_CODE;//成功 失败
	@XmlElement(name = "ERROR_MESSAGE")
	private String ERROR_MESSAGE;//错误描述
	
	@XmlElement(name = "ITEM_LIST") 
	private ITEM_LIST ITEM_LIST;

	public String getREQ_MSGID() {
		return REQ_MSGID;
	}

	public void setREQ_MSGID(String rEQ_MSGID) {
		REQ_MSGID = rEQ_MSGID;
	}

	public String getRESP_MSGID() {
		return RESP_MSGID;
	}

	public void setRESP_MSGID(String rESP_MSGID) {
		RESP_MSGID = rESP_MSGID;
	}

	public String getRETURN_CODE() {
		return RETURN_CODE;
	}

	public void setRETURN_CODE(String rETURN_CODE) {
		RETURN_CODE = rETURN_CODE;
	}

	public String getERROR_MESSAGE() {
		return ERROR_MESSAGE;
	}

	public void setERROR_MESSAGE(String eRROR_MESSAGE) {
		ERROR_MESSAGE = eRROR_MESSAGE;
	}

	public ITEM_LIST getITEM_LIST() {
		return ITEM_LIST;
	}

	public void setITEM_LIST(ITEM_LIST iTEM_LIST) {
		ITEM_LIST = iTEM_LIST;
	}
	
	
	
	
}
