package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class LisPacsReqBean{
	private String reserveId="";//	体检预约号 
	private List<LisPacsReqItemBean> itemsInfo= new ArrayList<LisPacsReqItemBean>();
	
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public List<LisPacsReqItemBean> getItemsInfo() {
		return itemsInfo;
	}
	public void setItemsInfo(List<LisPacsReqItemBean> itemsInfo) {
		this.itemsInfo = itemsInfo;
	}
		
}
