package com.hjw.webService.client.hzzbmz.Bean;

public class DAHResBean {
	private String success="1";//	返回值（0成功；1失败）
	private String brid="";//	成功返回病人档案id
	private String message="";//	返回提示信息
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getBrid() {
		return brid;
	}
	public void setBrid(String brid) {
		this.brid = brid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}


}
