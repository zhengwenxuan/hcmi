package com.hjw.webService.client.tj180.Bean;

public class ExamJoinResBean{
	private boolean status = false;
	private String errorinfo = "";
	
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

}
