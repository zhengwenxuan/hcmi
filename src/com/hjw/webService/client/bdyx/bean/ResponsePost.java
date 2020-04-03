package com.hjw.webService.client.bdyx.bean;

public class ResponsePost {

	private int status;//1：成功，0：失败
	private String data;//对应消息中间件中的消息ID
	private String errMsg;//失败时的错误描述
	private String stack;//失败时后台的异常栈信息
	public int getStatus() {
		return status;
	}
	public String getData() {
		return data;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public String getStack() {
		return stack;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public void setStack(String stack) {
		this.stack = stack;
	}
}
