package com.hjw.webService.service.lisbean;

import java.util.ArrayList;
import java.util.List;
 
public class RetLisChargeItem {

	private String chargingItem_num;//收费项目编码
   
    private List<RetLisItem> listRetLisItem =new ArrayList<RetLisItem>();//检查项目

	public String getChargingItem_num() {
		return chargingItem_num;
	}

	public void setChargingItem_num(String chargingItem_num) {
		this.chargingItem_num = chargingItem_num;
	}

	public List<RetLisItem> getListRetLisItem() {
		return listRetLisItem;
	}

	public void setListRetLisItem(List<RetLisItem> listRetLisItem) {
		this.listRetLisItem = listRetLisItem;
	}
    
	
}
