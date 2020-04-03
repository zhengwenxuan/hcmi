package com.hjw.webService.client.Bean;

public class CustAccBody {
	private String customerName="";	//客户姓名
	private String customerIdentityNo="";//	客户身份证号
	private String phone="";//	客户手机号
	private String customerPatientId="";//	客户HISID号
	private String customerSSid="";//	客户社保卡号
	private String customerCardNo="";//	客户卡号
	private String customerSex="";
	private String customerBirthday="";//出生日期
	
	
	
	public String getCustomerSex() {
		return customerSex;
	}
	public void setCustomerSex(String customerSex) {
		this.customerSex = customerSex;
	}
	public String getCustomerBirthday() {
		return customerBirthday;
	}
	public void setCustomerBirthday(String customerBirthday) {
		this.customerBirthday = customerBirthday;
	}
	public String getCustomerCardNo() {
		return customerCardNo;
	}
	public void setCustomerCardNo(String customerCardNo) {
		this.customerCardNo = customerCardNo;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerIdentityNo() {
		return customerIdentityNo;
	}
	public void setCustomerIdentityNo(String customerIdentityNo) {
		this.customerIdentityNo = customerIdentityNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCustomerPatientId() {
		return customerPatientId;
	}
	public void setCustomerPatientId(String customerPatientId) {
		this.customerPatientId = customerPatientId;
	}
	public String getCustomerSSid() {
		return customerSSid;
	}
	public void setCustomerSSid(String customerSSid) {
		this.customerSSid = customerSSid;
	}
    
}
