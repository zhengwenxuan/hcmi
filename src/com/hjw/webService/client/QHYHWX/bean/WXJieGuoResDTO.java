package com.hjw.webService.client.QHYHWX.bean;

public class WXJieGuoResDTO {

	
	private String status;//0 1
	private String errorMsg;// 成功 失败
	
	private WXJieGuoResDataDTO data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public WXJieGuoResDataDTO getData() {
		return data;
	}

	public void setData(WXJieGuoResDataDTO data) {
		this.data = data;
	}
	
	
}
