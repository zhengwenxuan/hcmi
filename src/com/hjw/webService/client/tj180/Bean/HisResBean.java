package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class HisResBean{
	private String status = "";
	private String errorinfo = "";
	private List<HisResItemBean> billItemsInfo = new ArrayList<HisResItemBean>();
    private String billItemsNum="";    

	public List<HisResItemBean> getBillItemsInfo() {
		return billItemsInfo;
	}

	public void setBillItemsInfo(List<HisResItemBean> billItemsInfo) {
		this.billItemsInfo = billItemsInfo;
	}

	public String getBillItemsNum() {
		return billItemsNum;
	}

	public void setBillItemsNum(String billItemsNum) {
		this.billItemsNum = billItemsNum;
	}

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

}
