package com.hjw.webService.client.zhangyeshi.bean;

import com.hjw.interfaces.util.JaxbUtil;

import net.sf.json.JSONObject;

public class ZYSResponse {
	
	String resultCode; // 1 成功   0 失败
	String resultMsg;  //返回信息
	String interfaceID;  //接口信息ID
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	public String getInterfaceID() {
		return interfaceID;
	}
	public void setInterfaceID(String interfaceID) {
		this.interfaceID = interfaceID;
	}
	

}
