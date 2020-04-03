package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResClinicItemBean{
	private String status = "";
	private String errorinfo = "";
	private List<ClinicItemInfo> clinicItemInfo = new ArrayList<ClinicItemInfo>();

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

	public List<ClinicItemInfo> getClinicItemInfo() {
		return clinicItemInfo;
	}

	public void setClinicItemInfo(List<ClinicItemInfo> clinicItemInfo) {
		this.clinicItemInfo = clinicItemInfo;
	}
	
}
