package com.hjw.webService.client.hokai.bean;

public class TeamItemBeanHK {

    private double price;
	private double acc_charge;
	private String his_num;
	private String item_name="";
	private int counts=1;
	
	
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getAcc_charge() {
		return acc_charge;
	}
	public void setAcc_charge(double acc_charge) {
		this.acc_charge = acc_charge;
	}
	public String getHis_num() {
		return his_num;
	}
	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	
	
	   
}
