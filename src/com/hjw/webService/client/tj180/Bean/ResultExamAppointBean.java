package com.hjw.webService.client.tj180.Bean;

public class ResultExamAppointBean{
	private String status="";//	返回状态码 正常：200，参数异常400，	未知错误: 500
	private String errorInfo="";//	错误信息提示 	正常：””，异常：中文提示
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
}
