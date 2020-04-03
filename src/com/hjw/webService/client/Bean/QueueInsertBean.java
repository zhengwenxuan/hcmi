package com.hjw.webService.client.Bean;

public class QueueInsertBean {
	private String ipAddress;
	private String currentNo="";
	private String nextNo="";
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getCurrentNo() {
		return currentNo;
	}
	public void setCurrentNo(String currentNo) {
		this.currentNo = currentNo;
	}
	public String getNextNo() {
		return nextNo;
	}
	public void setNextNo(String nextNo) {
		this.nextNo = nextNo;
	}
	
}
