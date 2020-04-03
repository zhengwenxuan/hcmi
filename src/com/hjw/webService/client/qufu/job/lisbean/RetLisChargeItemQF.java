package com.hjw.webService.client.qufu.job.lisbean;

import java.util.ArrayList;
import java.util.List;
 
public class RetLisChargeItemQF {

	private String chargingItem_num="";//收费项目编码
	
	private String chargingItem_name="";//收费项目编码
   
    private List<RetLisItemQF> listRetLisItem =new ArrayList<RetLisItemQF>();//检查项目

	public String getChargingItem_num() {
		return chargingItem_num;
	}

	public void setChargingItem_num(String chargingItem_num) {
		this.chargingItem_num = chargingItem_num;
	}

	public List<RetLisItemQF> getListRetLisItem() {
		return listRetLisItem;
	}

	public void setListRetLisItem(List<RetLisItemQF> listRetLisItem) {
		this.listRetLisItem = listRetLisItem;
	}

	public String getChargingItem_name() {
		return chargingItem_name;
	}

	public void setChargingItem_name(String chargingItem_name) {
		this.chargingItem_name = chargingItem_name;
	}
    
}
