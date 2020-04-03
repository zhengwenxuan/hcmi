package com.hjw.webService.client.QHYHWX.bean;

public class WeiXinExaminePay {

	private String status;//0 1
	private String errorMsg;// 成功 失败
	private WeiXinDaTa data = new WeiXinDaTa();
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
	public WeiXinDaTa getData() {
		return data;
	}
	public void setData(WeiXinDaTa data) {
		this.data = data;
	}


	
	
	
}
