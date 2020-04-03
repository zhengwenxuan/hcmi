package com.hjw.webService.client.ShanxiXXG.bean;

import java.util.List;

public class SendFeeXXG {
	
	private String patient_id;
	private int times;
	private String charge_no;
	private String apply_unit;
	private String apply_opera;
	private float charge_total;
	private List<FeeItemXXG> listcodes;
	
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public String getCharge_no() {
		return charge_no;
	}
	public void setCharge_no(String charge_no) {
		this.charge_no = charge_no;
	}
	public String getApply_unit() {
		return apply_unit;
	}
	public void setApply_unit(String apply_unit) {
		this.apply_unit = apply_unit;
	}
	public String getApply_opera() {
		return apply_opera;
	}
	public void setApply_opera(String apply_opera) {
		this.apply_opera = apply_opera;
	}
	public float getCharge_total() {
		return charge_total;
	}
	public void setCharge_total(float charge_total) {
		this.charge_total = charge_total;
	}
	public List<FeeItemXXG> getListcodes() {
		return listcodes;
	}
	public void setListcodes(List<FeeItemXXG> listcodes) {
		this.listcodes = listcodes;
	}
	
}
