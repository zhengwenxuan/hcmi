package com.hjw.webService.client.tj180.Bean;

public class ExamJoinReqBean{
	private String reserveId="";//	体检预约号 
	private String joinDate="";//体检(Yyyy-mm-dd hh:mm:ss)
	
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}		
}
