package com.hjw.webService.client.jsjg.bean.out;

public class OutResult {

	private String Status = "0";//接口调用状态	0失败，1：成功	PS:当返回结果集为空时，按失败处理
	private String Msg = "";//描述信息	对成功失败的描述
	
	public String getStatus() {
		return Status;
	}
	public String getMsg() {
		return Msg;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	
}
