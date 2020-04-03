package com.hjw.webService.client.acmeway.bean;

public class Result {

	private int status;//错误码0成功
	
	/*
	 * 如成功,返回则为结果的JSON 串
	 * 没有查到数据测评结果，body内容为null
	 * 本接口中为string[],对应着报告图片的地址，因报告有可能有多张
	 */
	private Object body;
	private String message = "";//错误码对应的信息
	
	public int getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
}
