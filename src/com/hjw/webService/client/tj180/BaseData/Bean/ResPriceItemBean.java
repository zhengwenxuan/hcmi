package com.hjw.webService.client.tj180.BaseData.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResPriceItemBean{
	private String status = "";
	private String errorinfo = "";
	private List<PriceItemInfo> priceItemInfo = new ArrayList<PriceItemInfo>();

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

	public List<PriceItemInfo> getPriceItemInfo() {
		return priceItemInfo;
	}

	public void setPriceItemInfo(List<PriceItemInfo> priceItemInfo) {
		this.priceItemInfo = priceItemInfo;
	}
	
}
