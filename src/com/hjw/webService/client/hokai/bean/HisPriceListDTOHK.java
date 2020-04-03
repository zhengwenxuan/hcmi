package com.hjw.webService.client.hokai.bean;



public class HisPriceListDTOHK implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	private String item_code="";

	private String item_class="";

	private String item_name="";

	private String item_spec="";

	private String units="";

	private double price;

	private double prefer_price;

	private String performed_by="";

	private String input_code="";

	private String class_on_inp_rcpt="";

	private String clss_on_outp_rcpt="";

	private String class_on_reckoning="";

	private String subj_code="";

	private String memo="";

	private String start_date;

	private String stop_date;
	
   private String update_date="";
	
	private String create_date="";
	
	private String amount;
	

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public String getItem_class() {
		return item_class;
	}

	public void setItem_class(String item_class) {
		this.item_class = item_class;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_spec() {
		return item_spec;
	}

	public void setItem_spec(String item_spec) {
		this.item_spec = item_spec;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrefer_price() {
		return prefer_price;
	}

	public void setPrefer_price(double prefer_price) {
		this.prefer_price = prefer_price;
	}

	public String getPerformed_by() {
		return performed_by;
	}

	public void setPerformed_by(String performed_by) {
		this.performed_by = performed_by;
	}

	public String getInput_code() {
		return input_code;
	}

	public void setInput_code(String input_code) {
		this.input_code = input_code;
	}

	public String getClass_on_inp_rcpt() {
		return class_on_inp_rcpt;
	}

	public void setClass_on_inp_rcpt(String class_on_inp_rcpt) {
		this.class_on_inp_rcpt = class_on_inp_rcpt;
	}

	public String getClss_on_outp_rcpt() {
		return clss_on_outp_rcpt;
	}

	public void setClss_on_outp_rcpt(String clss_on_outp_rcpt) {
		this.clss_on_outp_rcpt = clss_on_outp_rcpt;
	}

	public String getClass_on_reckoning() {
		return class_on_reckoning;
	}

	public void setClass_on_reckoning(String class_on_reckoning) {
		this.class_on_reckoning = class_on_reckoning;
	}

	public String getSubj_code() {
		return subj_code;
	}

	public void setSubj_code(String subj_code) {
		this.subj_code = subj_code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getStop_date() {
		return stop_date;
	}

	public void setStop_date(String stop_date) {
		this.stop_date = stop_date;
	}

}
