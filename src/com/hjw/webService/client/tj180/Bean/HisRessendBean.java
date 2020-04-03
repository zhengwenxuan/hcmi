package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class HisRessendBean{
	private String reserveId="";//	体检预约号 
	private boolean status = false;
	private String errorinfo = "";
	private List<HisRessendItemBean> itemList= new ArrayList<HisRessendItemBean>();
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public List<HisRessendItemBean> getItemList() {
		return itemList;
	}
	public void setItemList(List<HisRessendItemBean> itemList) {
		this.itemList = itemList;
	}
	
	
}
