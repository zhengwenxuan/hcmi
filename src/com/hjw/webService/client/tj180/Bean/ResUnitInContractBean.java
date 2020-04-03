package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResUnitInContractBean{
	private String status = "";
	private String errorinfo = "";
	private List<UnitInContractItem> unitInfo = new ArrayList<UnitInContractItem>();

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

	public List<UnitInContractItem> getUnitInfo() {
		return unitInfo;
	}

	public void setUnitInfo(List<UnitInContractItem> unitInfo) {
		this.unitInfo = unitInfo;
	}

	
}
