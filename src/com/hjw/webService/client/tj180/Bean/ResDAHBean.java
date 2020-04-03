package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResDAHBean{
	private String status = "";
	private String errorinfo = "";
	private List<DAHBean> customerInfo = new ArrayList<DAHBean>();

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

	public List<DAHBean> getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(List<DAHBean> customerInfo) {
		this.customerInfo = customerInfo;
	}

}
