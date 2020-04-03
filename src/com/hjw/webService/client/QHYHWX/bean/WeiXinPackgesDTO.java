package com.hjw.webService.client.QHYHWX.bean;

import java.util.ArrayList;
import java.util.List;

public class WeiXinPackgesDTO {
	
	private String packageName;//套餐名称
	private String price;//套餐价格
	private String description;//套餐描述
	private String pictures;//套餐配图
	private String notice;//注意事项
	
	private List<WeiXinItemm> items = new ArrayList<>();

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getNotice() {
		return notice;
	}
	
	public void setNotice(String notice) {
		this.notice = notice;
	}

	public List<WeiXinItemm> getItems() {
		return items;
	}

	public void setItems(List<WeiXinItemm> items) {
		this.items = items;
	}
	
	
	
	
}
