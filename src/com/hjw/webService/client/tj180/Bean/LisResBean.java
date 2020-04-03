package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class LisResBean{
	private String status = "";
	private String errorinfo = "";
	private List<LisResItemBean> labItemsInfo = new ArrayList<LisResItemBean>();
    private String labItemsNum="";
    
	public String getLabItemsNum() {
		return labItemsNum;
	}

	public void setLabItemsNum(String labItemsNum) {
		this.labItemsNum = labItemsNum;
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

	public List<LisResItemBean> getLabItemsInfo() {
		return labItemsInfo;
	}

	public void setLabItemsInfo(List<LisResItemBean> labItemsInfo) {
		this.labItemsInfo = labItemsInfo;
	}

}
