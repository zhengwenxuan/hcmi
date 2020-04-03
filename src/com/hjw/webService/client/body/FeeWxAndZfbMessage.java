package com.hjw.webService.client.body;

public class FeeWxAndZfbMessage {

	
	private String  exam_num;
	private long  exam_id;
	private String  req_nums;
	private Double  amount1;//原价
	private Double  amount2;//实收价
	private String  peis_trade_code;//支付流水号
	private String  chargeType;//支付类型 微信 支付宝
	private long userid; //操作员id
	private String original_trade_no;
	private String original_voucher_no;
	private String acctype;// 收费 SF 退费TF 对账DZ
	
	
	
	public String getAcctype() {
		return acctype;
	}
	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}
	public String getOriginal_trade_no() {
		return original_trade_no;
	}
	public void setOriginal_trade_no(String original_trade_no) {
		this.original_trade_no = original_trade_no;
	}
	public String getOriginal_voucher_no() {
		return original_voucher_no;
	}
	public void setOriginal_voucher_no(String original_voucher_no) {
		this.original_voucher_no = original_voucher_no;
	}
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	
	public long getExam_id() {
		return exam_id;
	}
	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}
	public String getReq_nums() {
		return req_nums;
	}
	public void setReq_nums(String req_nums) {
		this.req_nums = req_nums;
	}
	
	public Double getAmount1() {
		return amount1;
	}
	public void setAmount1(Double amount1) {
		this.amount1 = amount1;
	}
	public Double getAmount2() {
		return amount2;
	}
	public void setAmount2(Double amount2) {
		this.amount2 = amount2;
	}
	public String getPeis_trade_code() {
		return peis_trade_code;
	}
	public void setPeis_trade_code(String peis_trade_code) {
		this.peis_trade_code = peis_trade_code;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	
	
	
}
