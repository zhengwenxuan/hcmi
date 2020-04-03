package com.hjw.webService.client.liubaxian.bean;

public class PatInfoBean {
	private String patID="";//体检编号或者结算号
	private String vipid="";//档案号 没有档案号填体检号
	private String patName="";//姓名/团体名称 
	private String sex="";//性	别 
	private String birth="";//出生日期 个人/团体标识 
	private String ptFlag="P" ;//P:个人；T:团体
	private String regDate="";//申请时间
	public String getPatID() {
		return patID;
	}
	public void setPatID(String patID) {
		this.patID = patID;
	}
	public String getVipid() {
		return vipid;
	}
	public void setVipid(String vipid) {
		this.vipid = vipid;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getPtFlag() {
		return ptFlag;
	}
	public void setPtFlag(String ptFlag) {
		this.ptFlag = ptFlag;
	}
	
}
