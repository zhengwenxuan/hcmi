package com.hjw.webService.client.acmeway.bean;

import com.google.gson.Gson;

public class PatientReq {

	private String	id	 = "";//受试者唯一id(档案号)	Id123456
	private String	testOrder	 = "";//体检号	223456
	private String	cardNo	 = "";//卡号,可以与体检号testOrder或受试者唯一id一样	CardNo123
	private String	name	 = "";//姓名	张三
	private String	sex	 = "";//性别： M 男性 F 女性	M
	private String	birthday	 = "";//出生日期格式为短日期 yyyy-MM-dd	1988/2/24
	private String	idCardNo	 = "";//身份证号	
	private String	nationCode	 = "";//民族代码，详见附录 A 民族代码表	
	private String	education	 = "";//文化程度内容为：初中、未上过学、扫盲班、小学、初中、高中或中专、大专及以上	
	private String	profession	 = "";//职业代码，详见附录 B职业代码表	
	private String	trdCode	 = "";//行业代码，详见附录 C行业代码表	
	private String	cpyName	 = "";//工作单位名称	
	private String	deptName	 = "";//部门名称	
	private int	isMarried;//婚姻状况:   1:未婚、2:已婚、3:离异、4:丧偶 	
	private String	areaCode	 = "";//所在地区，详见附录 D 地区代码表	
	private String	nativePlaceCode	 = "";//籍贯，详见附录 D 地区代码表	
	private String	Mobile	 = "";//手机号	18518731702
	private String	RegisterList[];//登记项目,详见备注A	为空时全登记
	
	public String getId() {
		return id;
	}
	public String getTestOrder() {
		return testOrder;
	}
	public String getCardNo() {
		return cardNo;
	}
	public String getName() {
		return name;
	}
	public String getSex() {
		return sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public String getNationCode() {
		return nationCode;
	}
	public String getEducation() {
		return education;
	}
	public String getProfession() {
		return profession;
	}
	public String getTrdCode() {
		return trdCode;
	}
	public String getCpyName() {
		return cpyName;
	}
	public String getDeptName() {
		return deptName;
	}
	public int getIsMarried() {
		return isMarried;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public String getNativePlaceCode() {
		return nativePlaceCode;
	}
	public String getMobile() {
		return Mobile;
	}
	public String[] getRegisterList() {
		return RegisterList;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTestOrder(String testOrder) {
		this.testOrder = testOrder;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public void setTrdCode(String trdCode) {
		this.trdCode = trdCode;
	}
	public void setCpyName(String cpyName) {
		this.cpyName = cpyName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setIsMarried(int isMarried) {
		this.isMarried = isMarried;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public void setNativePlaceCode(String nativePlaceCode) {
		this.nativePlaceCode = nativePlaceCode;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public void setRegisterList(String[] registerList) {
		RegisterList = registerList;
	}
	
	public static void main(String[] args) {
		PatientReq patientReq = new PatientReq();
		String req = new Gson().toJson(patientReq, PatientReq.class);
		System.out.println(req);
	}
}
