package com.hjw.webService.client.tj180.Bean;

public class CustModifyPSWResBody {

	private String status="AE";//	返回状态码 正常：200，参数异常400，	未知错误: 500
	private String password;//	密码
	private String errorinfo="";//	错误信息提示 	正常：””，异常：中文提示
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
}
