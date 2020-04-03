package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResUpdatePriceItemBean{
	private String status = "";
	private String errorinfo = "";
	private List<PriceUpdateItemInfo> priceItemInfo = new ArrayList<PriceUpdateItemInfo>();

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

	public List<PriceUpdateItemInfo> getPriceItemInfo() {
		return priceItemInfo;
	}

	public void setPriceItemInfo(List<PriceUpdateItemInfo> priceItemInfo) {
		this.priceItemInfo = priceItemInfo;
	}
	
}
