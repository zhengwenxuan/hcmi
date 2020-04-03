package com.hjw.webService.client.Bean;

public class DAHCustomerBean {
	private String persionId="";//病案号
	private String id_num="";
	private String name="";
	private String sex="";
	private String sexcode="";
	private String brid="";
    private String address="";
    private String phone="";
    private String nation="";
    private String customerIdentity="地方";//	客户身份 	地方(默认)
	private String customerChargeType="自费";//	客户费别 	自费(默认)
	private String unitInContract="";//合同单位
	private String webbed="";//	婚否 0-未婚，1-已婚，2-未知
	private String marriedAge="";//	婚龄
	private String vocation="";//	职业
	private String education="";//	学历
	private String organizationId;//单位编号
	private long userId;
	private String customerSSid;//社保卡号
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPersionId() {
		return persionId;
	}

	public void setPersionId(String persionId) {
		this.persionId = persionId;
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

	public String getUnitInContract() {
		return unitInContract;
	}

	public void setUnitInContract(String unitInContract) {
		this.unitInContract = unitInContract;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
		if("男".equals(sex)){
			this.setSexcode("1");
		}else if("女".equals(sex)){
			this.setSexcode("2");
		}else{
			this.setSexcode("1");
		}
	}

	public String getSexcode() {
		return sexcode;
	}

	public void setSexcode(String sexcode) {
		this.sexcode = sexcode;
	}

	public String getBrid() {
		return brid;
	}

	public void setBrid(String brid) {
		this.brid = brid;
	}

	public String getCustomerSSid() {
		return customerSSid;
	}

	public void setCustomerSSid(String customerSSid) {
		this.customerSSid = customerSSid;
	}

}
