package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResPriceAndClinicBean{
	private String status = "";
	private String errorinfo = "";
	private List<PriceAndClinicInfo> clinicPriceInfo = new ArrayList<PriceAndClinicInfo>();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorinfo() {
		return errorinfo;
	}

	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

	public List<PriceAndClinicInfo> getClinicPriceInfo() {
		return clinicPriceInfo;
	}

	public void setClinicPriceInfo(List<PriceAndClinicInfo> clinicPriceInfo) {
		this.clinicPriceInfo = clinicPriceInfo;
	}
	
}
