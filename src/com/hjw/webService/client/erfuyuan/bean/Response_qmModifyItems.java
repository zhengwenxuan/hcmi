package com.hjw.webService.client.erfuyuan.bean;

public class Response_qmModifyItems {
	
	private String status="";//	返回状态码	正常：10000，体检者ID不正确或不存在：12001，返回失败： 12002
	private String result;
	
	public String getStatus() {
		return status;
	}
	public String getResult() {
		return result;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
