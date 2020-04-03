package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

public class ArmyPatInfoBody {
	private String status;
	private int patNum;
	private List<ArmyReserveBean> customerInfo = new ArrayList<ArmyReserveBean>();
	private String errorInfo = "";

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPatNum() {
		return patNum;
	}

	public void setPatNum(int patNum) {
		this.patNum = patNum;
	}

	public List<ArmyReserveBean> getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(List<ArmyReserveBean> customerInfo) {
		this.customerInfo = customerInfo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

}
