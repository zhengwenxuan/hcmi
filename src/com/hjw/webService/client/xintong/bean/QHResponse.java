package com.hjw.webService.client.xintong.bean;

public class QHResponse {
	
	String RETURN_CODE; // 1 成功   0 失败
	String ERROR_MESSAGE;  //返回信息
	
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
	
	

}
