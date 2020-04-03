package com.hjw.webService.client.erfuyuan.bean;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class Request_qmModifyItems {
	
	private String clientId = "";//体检客户ID
	private String itemsRemove = "";//需要删除的检查项目
	private String itemsAdd = "";//需要新增的检查项目
	
	public static void main(String[] args) {
		Request_qmModifyItems request = new Request_qmModifyItems();
		System.out.println(new Gson().toJson(request, Request_qmModifyItems.class));
	}
	
	public String getClientId() {
		return clientId;
	}
	public String getItemsRemove() {
		return itemsRemove;
	}
	public String getItemsAdd() {
		return itemsAdd;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setItemsRemove(String itemsRemove) {
		this.itemsRemove = itemsRemove;
	}
	public void setItemsAdd(String itemsAdd) {
		this.itemsAdd = itemsAdd;
	}
	
	public Map<String, Object> covertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("clientId", clientId);
		map.put("itemsRemove", itemsRemove);
		map.put("itemsAdd", itemsAdd);
		return map;
	}
}
