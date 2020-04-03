package com.hjw.webService.client.xhhk.lisbean;

public class ItemsApplyLisXHHK {

	private String ItemCode = "";//检验项目编号
	private String ItemName = "";//检验项目名称
	//private String SampleNo = "";//样本编号
	private String SampleText = "";//样本说明
	private double Price = 0.0;//项目价格 115.2元
	
	public String getItemCode() {
		return ItemCode;
	}
	public String getItemName() {
		return ItemName;
	}
	/*public String getSampleNo() {
		return SampleNo;
	}*/
	public String getSampleText() {
		return SampleText;
	}
	public double getPrice() {
		return Price;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	/*public void setSampleNo(String sampleNo) {
		SampleNo = sampleNo;
	}*/
	public void setSampleText(String sampleText) {
		SampleText = sampleText;
	}
	public void setPrice(double price) {
		Price = price;
	}
}
