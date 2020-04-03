package com.hjw.webService.client.tj180.Bean;

public class PriceAndClinicInfo {
	private String clinicClass="";//	诊疗项目类别
	private String clinicCode="";//	诊疗项目代码
	private String priceClass="";//	价表项目类别
	private String priceCode="";//	价表项目代码
	private String priceSpec="";//	价表项目规格
	private String priceUnits="";//	价表项目单位
	private int amount;//	价格

	public String getClinicClass() {
		return clinicClass;
	}
	public void setClinicClass(String clinicClass) {
		this.clinicClass = clinicClass;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
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
	
}
