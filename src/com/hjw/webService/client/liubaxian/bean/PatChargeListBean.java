package com.hjw.webService.client.liubaxian.bean;

public class PatChargeListBean {
	 private long listNo;//流水号
	 private String patID="";//自增体检号 ; 
	 private String itemCode="";//体检项目代码   
	 private String hisItemCode="";//收费项目名称  C
	 private String hisItemName="";//HIS诊疗项目代码 临床项目代码、套餐代码。非价表代码 
	 private int itemNum=1;//收费项目计数 
	 private String oderDeptNo="";//开单科室代码  	
	 private String deptNo="";//执行科室代码  
	 private double price;//原金额 原项目总额  
	 private double charge;//收费金额  实收项目 
	 private float rate;//总额折扣率 
	 private String chargeFlag="0";//收/退费标识0：未收费；1：已收费；2：待退费；3：已退费  
	 private String chargeOperator="";//收费员姓名 
	 private String chargeOperNo="";//收费员代码 
	 private String chargeDate="";//收费日期
	 private String rcpt_NO="";//收据号 
	 private String return_RCPT_NO;// 退费收据号 
	 private int readFlag;//体检系统是否已经读取标识 0：未读取；1：已读
	 
	public long getListNo() {
		return listNo;
	}
	public void setListNo(long listNo) {
		this.listNo = listNo;
	}
	public String getPatID() {
		return patID;
	}
	public void setPatID(String patID) {
		this.patID = patID;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getHisItemCode() {
		return hisItemCode;
	}
	public void setHisItemCode(String hisItemCode) {
		this.hisItemCode = hisItemCode;
	}
	public String getHisItemName() {
		return hisItemName;
	}
	public void setHisItemName(String hisItemName) {
		this.hisItemName = hisItemName;
	}
	public int getItemNum() {
		return itemNum;
	}
	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}
	public String getOderDeptNo() {
		return oderDeptNo;
	}
	public void setOderDeptNo(String oderDeptNo) {
		this.oderDeptNo = oderDeptNo;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getCharge() {
		return charge;
	}
	public void setCharge(double charge) {
		this.charge = charge;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(String chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public String getChargeOperator() {
		return chargeOperator;
	}
	public void setChargeOperator(String chargeOperator) {
		this.chargeOperator = chargeOperator;
	}
	public String getChargeOperNo() {
		return chargeOperNo;
	}
	public void setChargeOperNo(String chargeOperNo) {
		this.chargeOperNo = chargeOperNo;
	}
	public String getChargeDate() {
		return chargeDate;
	}
	public void setChargeDate(String chargeDate) {
		this.chargeDate = chargeDate;
	}
	public String getRcpt_NO() {
		return rcpt_NO;
	}
	public void setRcpt_NO(String rcpt_NO) {
		this.rcpt_NO = rcpt_NO;
	}
	public String getReturn_RCPT_NO() {
		return return_RCPT_NO;
	}
	public void setReturn_RCPT_NO(String return_RCPT_NO) {
		this.return_RCPT_NO = return_RCPT_NO;
	}
	public int getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}
}
