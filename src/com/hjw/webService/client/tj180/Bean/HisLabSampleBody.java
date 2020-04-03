package com.hjw.webService.client.tj180.Bean;

public class HisLabSampleBody {
	private String reserveId;
	private String breserveId;//火箭蛙添加，与平台无关
	
	public String getBreserveId() {
		return breserveId;
	}

	public void setBreserveId(String breserveId) {
		this.breserveId = breserveId;
	}

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
		this.breserveId=reserveId;
		if(reserveId.indexOf("B")==0){			
			reserveId=reserveId.substring(1,reserveId.length());
		}
	}
}
