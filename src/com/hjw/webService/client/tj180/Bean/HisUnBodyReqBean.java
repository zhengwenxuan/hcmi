package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class HisUnBodyReqBean {
	private String reserveId="";//	体检预约号
	private String itemsNum="";//	开单项目数
	private List<HisUnReqBean> itemsInfo=new ArrayList<HisUnReqBean>();
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	
	public String getItemsNum() {
		return itemsNum;
	}
	public void setItemsNum(String itemsNum) {
		this.itemsNum = itemsNum;
	}
	public List<HisUnReqBean> getItemsInfo() {
		return itemsInfo;
	}
	public void setItemsInfo(List<HisUnReqBean> itemsInfo) {
		this.itemsInfo = itemsInfo;
	}

	
}
