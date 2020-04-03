package com.hjw.webService.client.QHYHWX.bean;

public class WXJieGuoResChargingItem_examitemDTO {

	private String itemName;//检查项目名称
	private String itemPrice;//检查项目价格
	private String itemResult;//项目检查结果
	private String reference;//检查项目参考值
	private String doctorAdvice;//医生建议，无则空
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemResult() {
		return itemResult;
	}
	public void setItemResult(String itemResult) {
		this.itemResult = itemResult;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDoctorAdvice() {
		return doctorAdvice;
	}
	public void setDoctorAdvice(String doctorAdvice) {
		this.doctorAdvice = doctorAdvice;
	}
	
	
	
}
