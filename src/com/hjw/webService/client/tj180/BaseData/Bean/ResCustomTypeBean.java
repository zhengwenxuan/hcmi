package com.hjw.webService.client.tj180.BaseData.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResCustomTypeBean{
	private String status = "";
	private String errorinfo = "";
	private List<CustTypeItemInfo> identityInfo = new ArrayList<CustTypeItemInfo>();

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

	public List<CustTypeItemInfo> getIdentityInfo() {
		return identityInfo;
	}

	public void setIdentityInfo(List<CustTypeItemInfo> identityInfo) {
		this.identityInfo = identityInfo;
	}

	
	
}
