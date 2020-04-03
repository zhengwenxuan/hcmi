package com.hjw.webService.client.QHYHWX.bean;

import java.util.List;

public class WXJieGuoResDataDTO {

	
	private WXJieGuoResCustomerDTO patInfo;
	
	
	private List<WXJieGuoResChargingItemDTO> packages;
	
	
	public WXJieGuoResCustomerDTO getPatInfo() {
		return patInfo;
	}
	public void setPatInfo(WXJieGuoResCustomerDTO patInfo) {
		this.patInfo = patInfo;
	}
	public List<WXJieGuoResChargingItemDTO> getPackages() {
		return packages;
	}
	public void setPackages(List<WXJieGuoResChargingItemDTO> packages) {
		this.packages = packages;
	}
	
	
	
	
}
