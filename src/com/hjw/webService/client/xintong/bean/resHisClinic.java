package com.hjw.webService.client.xintong.bean;

import java.util.List;

public class resHisClinic {

	private String REQ_MSGID;//请求消息ID
	private String RESP_MSGID;//响应消息ID
	private String RETURN_CODE;//成功 失败
	private String ERROR_MESSAGE;//错误描述
	
	private List<ITEM_INFO> ITEM_LIST;

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

	public List<ITEM_INFO> getITEM_LIST() {
		return ITEM_LIST;
	}

	public void setITEM_LIST(List<ITEM_INFO> iTEM_LIST) {
		ITEM_LIST = iTEM_LIST;
	}
	
	
	
	
}
