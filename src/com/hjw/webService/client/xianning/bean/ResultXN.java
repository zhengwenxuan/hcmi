package com.hjw.webService.client.xianning.bean;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

public class ResultXN {

	private String Data = "";//数据内容

	private String Success = "";//ture or false
	
	private String Msg = "";//异常消息

	public String getData() {
		return Data;
	}

	public String getSuccess() {
		return Success;
	}

	public String getMsg() {
		return Msg;
	}

	public void setData(String data) {
		Data = data;
	}

	public void setSuccess(String success) {
		Success = success;
	}

	public void setMsg(String msg) {
		Msg = msg;
	}
	
	public static void main(String[] args) {
		String str = "{\"Date\": null,\"Success\": false,\"Msg\": \"申请科室代码-ORDERING_DEPT不能为空\"}";
		ResultXN result = new Gson().fromJson(str, ResultXN.class);
		System.out.println(result.getData());
		System.out.println(result.getSuccess());
		System.out.println(result.getMsg());
	}
}
