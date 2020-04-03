package com.hjw.webService.client.xintong.lis;

import java.util.ArrayList;
import java.util.List;
 
public class RetLisChargeItemQH {

	private String chargingItem_num="";//收费项目编码
	
	private String chargingItem_name="";//收费项目名称
	
	private String item_result; // 检查项目结果
   
    private List<RetLisItemQH> listRetLisItem =new ArrayList<RetLisItemQH>();//检查项目

	public String getChargingItem_num() {
		return chargingItem_num;
	}

	public void setChargingItem_num(String chargingItem_num) {
		this.chargingItem_num = chargingItem_num;
	}

	public List<RetLisItemQH> getListRetLisItem() {
		return listRetLisItem;
	}

	public void setListRetLisItem(List<RetLisItemQH> listRetLisItem) {
		this.listRetLisItem = listRetLisItem;
	}

	public String getChargingItem_name() {
		return chargingItem_name;
	}

	public void setChargingItem_name(String chargingItem_name) {
		this.chargingItem_name = chargingItem_name;
	}

	public String getItem_result() {
		return item_result;
	}

	public void setItem_result(String item_result) {
		this.item_result = item_result;
	}
	
    
}
