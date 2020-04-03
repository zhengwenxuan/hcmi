package com.hjw.webService.client.tj180.Bean;

public class CustDAHBean {
	private String customerName="";//	客户姓名
	private String customerSex="";//	客户性别  	中文：男/女
	private String customerIdentityNo="";//	客户身份证号
	private String customerBirthday="";//	客户出生日期
	private String customerBirthPlace="";//	客户出生地代码 身份证号前6位
	private String customerNation="";//	客户民族 	中文：如汉族、蒙古族等
	private String phone="";//	客户手机号
	private String operator="WYY";//	操作员登录用户名(需建与HIS一致的登录用户) 写拼音字头：如WYY
	private String customerPatientId="";//客户HISID号 新客户：””，老客户：HISID号
	private String customerSSid="";//	客户社保卡号
	private String customerIdentity="地方";//	客户身份 	地方(默认)
	private String customerChargeType="自费";//	客户费别 	自费(默认)
	private String unitInContract="";//合同单位
	private String address="";//	客户地址
	private String infoSource="0";//	获取客户信息来源	手工输入(默认)：0，身份证：1，社保卡：2
	private String webbed="";//	婚否 0-未婚，1-已婚，2-未知
	private String marriedAge="";//	婚龄
	private String vocation="";//	职业
	private String education="";//	学历
	private String organizationId="";//单位编号
	private String subOrganization="";//子单位名称
	private String groupName="";//部门名称
	
	public String getSubOrganization() {
		return subOrganization;
	}
	public void setSubOrganization(String subOrganization) {
		this.subOrganization = subOrganization;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getWebbed() {
		return webbed;
	}
	public void setWebbed(String webbed) {
		this.webbed = webbed;
	}
	public String getMarriedAge() {
		return marriedAge;
	}
	public void setMarriedAge(String marriedAge) {
		this.marriedAge = marriedAge;
	}
	public String getVocation() {
		return vocation;
	}
	public void setVocation(String vocation) {
		this.vocation = vocation;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getUnitInContract() {
		return unitInContract;
	}
	public void setUnitInContract(String unitInContract) {
		this.unitInContract = unitInContract;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerSex() {
		return customerSex;
	}
	public void setCustomerSex(String customerSex) {
		this.customerSex = customerSex;
	}
	public String getCustomerIdentityNo() {
		return customerIdentityNo;
	}
	public void setCustomerIdentityNo(String customerIdentityNo) {
		this.customerIdentityNo = customerIdentityNo;
	}
	public String getCustomerBirthday() {
		return customerBirthday;
	}
	public void setCustomerBirthday(String customerBirthday) {
		this.customerBirthday = customerBirthday;
	}
	public String getCustomerBirthPlace() {
		return customerBirthPlace;
	}
	public void setCustomerBirthPlace(String customerBirthPlace) {
		this.customerBirthPlace = customerBirthPlace;
	}
	public String getCustomerNation() {
		return customerNation;
	}
	public void setCustomerNation(String customerNation) {
		this.customerNation = customerNation;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
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
	public String getCustomerIdentity() {
		return customerIdentity;
	}
	public void setCustomerIdentity(String customerIdentity) {
		this.customerIdentity = customerIdentity;
	}
	public String getCustomerChargeType() {
		return customerChargeType;
	}
	public void setCustomerChargeType(String customerChargeType) {
		this.customerChargeType = customerChargeType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getInfoSource() {
		return infoSource;
	}
	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}

}
