package com.hjw.webService.client.Bean;

public class Person {
	private String personid="";// 患者ID
	private String personno="";// 就诊号
	private String personidnum="";// 身份证号
	private String persioncode="";/// 医保卡号
	private String arch_num="";	//档案号
	private String mc_no="";//就诊卡号
	private String exam_num="";
	private String name="";// 姓名
	private String tel="";// 联系电话
	private String sexcode="";// 性别代码
	private String sexname="";//性别名称
	private String birthtime="";// 出生日期
	private int old;// 年龄
	private String address="";// 住址
	private String meritalcode="";// 婚姻状况类别编码
	private String ethnicGroupCode="";// 民族编码
	private String comname="";// 工作单位名称
	private String contact_tel="";// 联系人电话
	private String contact_name="";// 联系人姓名
	private String vipflag="0";//是否vip 1 是，0 否	
	
	public String getArch_num() {
		return arch_num;
	}

	public void setArch_num(String arch_num) {
		this.arch_num = arch_num;
	}

	public String getMc_no() {
		return mc_no;
	}

	public void setMc_no(String mc_no) {
		this.mc_no = mc_no;
	}

	public String getSexname() {
		return sexname;
	}

	public void setSexname(String sexname) {
		this.sexname = sexname;
	}

	public String getVipflag() {
		return vipflag;
	}

	public void setVipflag(String vipflag) {
		this.vipflag = vipflag;
	}

	public String getExam_num() {
		return exam_num;
	}

	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid;
	}

	public String getPersonno() {
		return personno;
	}

	public void setPersonno(String personno) {
		this.personno = personno;
	}

	public String getPersonidnum() {
		return personidnum;
	}

	public void setPersonidnum(String personidnum) {
		this.personidnum = personidnum;
	}

	public String getPersioncode() {
		return persioncode;
	}

	public void setPersioncode(String persioncode) {
		this.persioncode = persioncode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSexcode() {
		return sexcode;
	}

	public void setSexcode(String sexcode) {
		this.sexcode = sexcode;
	}

	public String getBirthtime() {
		return birthtime;
	}

	public void setBirthtime(String birthtime) {
		this.birthtime = birthtime;
	}

	public int getOld() {
		return old;
	}

	public void setOld(int old) {
		this.old = old;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMeritalcode() {
		return meritalcode;
	}

	public void setMeritalcode(String meritalcode) {
		this.meritalcode = meritalcode;
	}

	public String getEthnicGroupCode() {
		return ethnicGroupCode;
	}

	public void setEthnicGroupCode(String ethnicGroupCode) {
		this.ethnicGroupCode = ethnicGroupCode;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getContact_tel() {
		return contact_tel;
	}

	public void setContact_tel(String contact_tel) {
		this.contact_tel = contact_tel;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

}
