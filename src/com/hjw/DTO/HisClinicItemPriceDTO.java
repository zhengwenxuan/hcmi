package com.hjw.DTO;

public class HisClinicItemPriceDTO {
	private String clinic_item_code="";
	
	private String clinic_item_class="";
	
	private int charge_item_no = 1;
	
	private String charge_item_classs="";
	
	private String charge_item_code="";
	
	private String charge_item_spec="";
	
	private long amount = 1;
	
	private String units="";
	
	private String backbill_rule="";
	
	private String body_part="";
	
	private String method="";
	
	public String getClinic_item_code() {
		return clinic_item_code;
	}

	public void setClinic_item_code(String clinic_item_code) {
		this.clinic_item_code = clinic_item_code;
	}

	public String getClinic_item_class() {
		return clinic_item_class;
	}

	public void setClinic_item_class(String clinic_item_class) {
		this.clinic_item_class = clinic_item_class;
	}

	public int getCharge_item_no() {
		return charge_item_no;
	}

	public void setCharge_item_no(int charge_item_no) {
		this.charge_item_no = charge_item_no;
	}

	public String getCharge_item_classs() {
		return charge_item_classs;
	}

	public void setCharge_item_classs(String charge_item_classs) {
		this.charge_item_classs = charge_item_classs;
	}

	public String getCharge_item_code() {
		return charge_item_code;
	}

	public void setCharge_item_code(String charge_item_code) {
		this.charge_item_code = charge_item_code;
	}

	public String getCharge_item_spec() {
		return charge_item_spec;
	}

	public void setCharge_item_spec(String charge_item_spec) {
		this.charge_item_spec = charge_item_spec;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getBody_part() {
		return body_part;
	}

	public String getMethod() {
		return method;
	}

	public void setBody_part(String body_part) {
		this.body_part = body_part;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUnits() {
		return units;
	}

	public String getBackbill_rule() {
		return backbill_rule;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public void setBackbill_rule(String backbill_rule) {
		this.backbill_rule = backbill_rule;
	}
	
	
}
