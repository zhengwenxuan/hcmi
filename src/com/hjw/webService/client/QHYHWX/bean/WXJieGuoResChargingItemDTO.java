package com.hjw.webService.client.QHYHWX.bean;

import java.util.List;

public class WXJieGuoResChargingItemDTO {

	
	private String packageName;//套餐名称   收费项目名称
	private String masterDoctor;//主检医生  检查医生
	private String examineTime;//检查时间
	private String fee;//套餐检查费用      	     收费项目金额
	private String summary;//套餐检查小结	收费项目检查结论
	
	private List<WXJieGuoResChargingItem_examitemDTO>  items;//检查套餐项目信息

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getMasterDoctor() {
		return masterDoctor;
	}

	public void setMasterDoctor(String masterDoctor) {
		this.masterDoctor = masterDoctor;
	}

	public String getExamineTime() {
		return examineTime;
	}

	public void setExamineTime(String examineTime) {
		this.examineTime = examineTime;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<WXJieGuoResChargingItem_examitemDTO> getItems() {
		return items;
	}

	public void setItems(List<WXJieGuoResChargingItem_examitemDTO> items) {
		this.items = items;
	}

	

	
	
	
	
	
}
