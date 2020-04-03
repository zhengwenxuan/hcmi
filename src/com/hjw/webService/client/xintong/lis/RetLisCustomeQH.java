package com.hjw.webService.client.xintong.lis;

import com.hjw.webService.client.qufu.job.bean.RetCustomeQF;

public class RetLisCustomeQH extends RetCustomeQF {

	private String sample_barcode="";// 申请单号// 条码号
	
	private String dept_code="";//执行科室	
	
	private String dept_name="";//执行科室名称
	
	private String effectiveTime="";//报告时间	

	private RetLisChargeItemQH retLisChargeItem = new RetLisChargeItemQH();// 收费项目	

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}

	public String getSample_barcode() {
		return sample_barcode;
	}

	public void setSample_barcode(String sample_barcode) {
		this.sample_barcode = sample_barcode;
	}

	public RetLisChargeItemQH getRetLisChargeItem() {
		return retLisChargeItem;
	}

	public void setRetLisChargeItem(RetLisChargeItemQH retLisChargeItem) {
		this.retLisChargeItem = retLisChargeItem;
	}


}
