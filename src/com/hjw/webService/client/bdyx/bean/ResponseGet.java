package com.hjw.webService.client.bdyx.bean;

public class ResponseGet {

	private int status;//1：成功，0：失败
	private DataGet data;//对应消息中间件中的消息ID
	private String errMsg;//失败时的错误描述
	private String stack;//失败时后台的异常栈信息
	public int getStatus() {
		return status;
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
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public void setStack(String stack) {
		this.stack = stack;
	}
	public DataGet getData() {
		return data;
	}
	public void setData(DataGet data) {
		this.data = data;
	}
	
}
