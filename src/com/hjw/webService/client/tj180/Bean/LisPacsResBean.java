package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class LisPacsResBean{
	private boolean status = false;
	private String errorinfo = "";
	private List<LisPacsResItemBean> itemsInfo= new ArrayList<LisPacsResItemBean>();
	
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public List<LisPacsResItemBean> getItemsInfo() {
		return itemsInfo;
	}
	public void setItemsInfo(List<LisPacsResItemBean> itemsInfo) {
		this.itemsInfo = itemsInfo;
	}	
}
