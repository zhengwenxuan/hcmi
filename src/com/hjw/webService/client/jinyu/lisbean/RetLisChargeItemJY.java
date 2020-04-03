package com.hjw.webService.client.jinyu.lisbean;

import java.util.ArrayList;
import java.util.List;
 
public class RetLisChargeItemJY {

	private String chargingItem_num;//收费项目编码
   
    private List<RetLisItemJY> listRetLisItem =new ArrayList<RetLisItemJY>();//检查项目

	public String getChargingItem_num() {
		return chargingItem_num;
	}

	public void setChargingItem_num(String chargingItem_num) {
		this.chargingItem_num = chargingItem_num;
	}

	public List<RetLisItemJY> getListRetLisItem() {
		return listRetLisItem;
	}

	public void setListRetLisItem(List<RetLisItemJY> listRetLisItem) {
		this.listRetLisItem = listRetLisItem;
	}
    
	
}
