package com.hjw.webService.service.lisbean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.service.bean.RetCustome;

public class RetLisCustome extends RetCustome {

	private String sample_barcode;// 申请单号// 条码号
	
	private String dept_code;//执行科室	
	
	private String dept_name;//执行科室名称
	
	private String effectiveTime;//报告时间	
	
	private String patient_id; //患者id

	
	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	private List<RetLisChargeItem> listRetLisChargeItem = new ArrayList<RetLisChargeItem>();// 收费项目	

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

	public List<RetLisChargeItem> getListRetLisChargeItem() {
		return listRetLisChargeItem;
	}

	public void setListRetLisChargeItem(List<RetLisChargeItem> listRetLisChargeItem) {
		this.listRetLisChargeItem = listRetLisChargeItem;
	}
}
