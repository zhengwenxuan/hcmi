package com.hjw.webService.client.QHYHWX.bean;

public class WeiXinResUpdateCustomer {

	
	private String status;
	private String errorMsg;
	
	private WXJieGuoResCustomerDTO data;

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

	public WXJieGuoResCustomerDTO getData() {
		return data;
	}

	public void setData(WXJieGuoResCustomerDTO data) {
		this.data = data;
	}
	
	
	
	
}
