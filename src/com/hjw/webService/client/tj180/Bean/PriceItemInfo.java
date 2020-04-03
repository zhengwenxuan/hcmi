package com.hjw.webService.client.tj180.Bean;

public class PriceItemInfo {
	private String priceClass="";//	价表项目类别
	private String priceCode="";//	价表项目代码
	private String priceName="";//	价表项目名称
	private String priceSpec="";//	价表项目规格
	private String priceUnits="";//	价表项目单位
	private String performedBy="";//
	private String performedByName="";//
	private double price;//	价格
	
	public String getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
	public String getPerformedByName() {
		return performedByName;
	}
	public void setPerformedByName(String performedByName) {
		this.performedByName = performedByName;
	}
	public String getPriceClass() {
		return priceClass;
	}
	public void setPriceClass(String priceClass) {
		this.priceClass = priceClass;
	}
	public String getPriceCode() {
		return priceCode;
	}
	public void setPriceCode(String priceCode) {
		this.priceCode = priceCode;
	}
	public String getPriceName() {
		return priceName;
	}
	public void setPriceName(String priceName) {
		this.priceName = priceName;
	}
	public String getPriceSpec() {
		return priceSpec;
	}
	public void setPriceSpec(String priceSpec) {
		this.priceSpec = priceSpec;
	}
	public String getPriceUnits() {
		return priceUnits;
	}
	public void setPriceUnits(String priceUnits) {
		this.priceUnits = priceUnits;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

}
