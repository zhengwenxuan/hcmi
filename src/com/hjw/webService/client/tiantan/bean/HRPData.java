package com.hjw.webService.client.tiantan.bean;

import java.util.Date;

public class HRPData {

	private String YWLSH;//体检数据的唯一标识
	private Date Date;//出报告时间
	private String Order_date;//开立时间
	private String Order_dept;//开立科室编码
	private String Order_doc;//开立医生工号
	private String undrug_code;//诊疗项目编码
	private String undrug_name;//诊疗项目名称
	private String itemcode;//价表项目编码
	private String itenname;//价表项目名称
	private String quantity;//数量
	private String price;//单价
	private String amount;//金额
	private String patno;//病人号
	private String patname;//病人姓名
	private String Oper_date;//执行时间
	private String Oper_dept;//Oper_dept
	private String Oper_doc;//Oper_doc
	private String Create_Time;
	
	
	
	public String getCreate_Time() {
		return Create_Time;
	}
	public void setCreate_Time(String create_Time) {
		Create_Time = create_Time;
	}
	public String getYWLSH() {
		return YWLSH;
	}
	public void setYWLSH(String yWLSH) {
		YWLSH = yWLSH;
	}
	
	public Date getDate() {
		return Date;
	}
	public void setDate(Date date) {
		Date = date;
	}
	public String getOrder_date() {
		return Order_date;
	}
	public void setOrder_date(String order_date) {
		Order_date = order_date;
	}
	public String getOrder_dept() {
		return Order_dept;
	}
	public void setOrder_dept(String order_dept) {
		Order_dept = order_dept;
	}
	public String getOrder_doc() {
		return Order_doc;
	}
	public void setOrder_doc(String order_doc) {
		Order_doc = order_doc;
	}
	public String getUndrug_code() {
		return undrug_code;
	}
	public void setUndrug_code(String undrug_code) {
		this.undrug_code = undrug_code;
	}
	public String getUndrug_name() {
		return undrug_name;
	}
	public void setUndrug_name(String undrug_name) {
		this.undrug_name = undrug_name;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItenname() {
		return itenname;
	}
	public void setItenname(String itenname) {
		this.itenname = itenname;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPatno() {
		return patno;
	}
	public void setPatno(String patno) {
		this.patno = patno;
	}
	public String getPatname() {
		return patname;
	}
	public void setPatname(String patname) {
		this.patname = patname;
	}
	public String getOper_date() {
		return Oper_date;
	}
	public void setOper_date(String oper_date) {
		Oper_date = oper_date;
	}
	public String getOper_dept() {
		return Oper_dept;
	}
	public void setOper_dept(String oper_dept) {
		Oper_dept = oper_dept;
	}
	public String getOper_doc() {
		return Oper_doc;
	}
	public void setOper_doc(String oper_doc) {
		Oper_doc = oper_doc;
	}
	
	
	
	
	
}
