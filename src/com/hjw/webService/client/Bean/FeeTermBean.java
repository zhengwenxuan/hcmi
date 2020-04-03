package com.hjw.webService.client.Bean;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description:  医生和科室
     * @author: yangm     
     * @date:   2016年10月7日 上午11:36:01   
     * @version V2.0.0.0
 */
public class FeeTermBean {
	public long charging_item_id;
	public int item_num;
	public double acc_charge;
	public double item_amount;
	public String item_code="";
	public String his_num="";
	private String dep_category;
	public long getCharging_item_id() {
		return charging_item_id;
	}
	public void setCharging_item_id(long charging_item_id) {
		this.charging_item_id = charging_item_id;
	}
	public int getItem_num() {
		return item_num;
	}
	public void setItem_num(int item_num) {
		this.item_num = item_num;
	}
	public double getAcc_charge() {
		return acc_charge;
	}
	public void setAcc_charge(double acc_charge) {
		this.acc_charge = acc_charge;
	}
	public double getItem_amount() {
		return item_amount;
	}
	public void setItem_amount(double item_amount) {
		this.item_amount = item_amount;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getHis_num() {
		return his_num;
	}
	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}
	public String getDep_category() {
		return dep_category;
	}
	public void setDep_category(String dep_category) {
		this.dep_category = dep_category;
	}

}
