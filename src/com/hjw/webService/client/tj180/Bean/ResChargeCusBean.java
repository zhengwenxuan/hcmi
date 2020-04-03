package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResChargeCusBean{
	private String status = "";
	private String errorinfo = "";
	private List<ChargeCusItemInfo> chargeTypeInfo = new ArrayList<ChargeCusItemInfo>();

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

	public List<ChargeCusItemInfo> getChargeTypeInfo() {
		return chargeTypeInfo;
	}

	public void setChargeTypeInfo(List<ChargeCusItemInfo> chargeTypeInfo) {
		this.chargeTypeInfo = chargeTypeInfo;
	}

		
}
