package com.hjw.webService.client.tj180.BaseData.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResChargeTypeBean{
	private String status = "";
	private String errorinfo = "";
	private List<ChargeTypeItemInfo> chargeTypeInfo = new ArrayList<ChargeTypeItemInfo>();

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

	public List<ChargeTypeItemInfo> getChargeTypeInfo() {
		return chargeTypeInfo;
	}

	public void setChargeTypeInfo(List<ChargeTypeItemInfo> chargeTypeInfo) {
		this.chargeTypeInfo = chargeTypeInfo;
	}

	
}
