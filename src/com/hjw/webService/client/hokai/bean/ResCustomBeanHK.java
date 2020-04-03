package com.hjw.webService.client.hokai.bean;

public class ResCustomBeanHK {
	private String code="AE";
	private String examinfo_id="";
	private String persionid="";
	private String codetext="";
	private Boolean faly=false;
	
	private String username;
	private String sex;
	private String age;
	private String addr;
	private String identityCode;
	private String id_no;
	
	
	
	public String getId_no() {
		return id_no;
	}
	public void setId_no(String id_no) {
		this.id_no = id_no;
	}
	public String getIdentityCode() {
		return identityCode;
	}
	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public Boolean getFaly() {
		return faly;
	}
	public void setFaly(Boolean faly) {
		this.faly = faly;
	}
	public String getExaminfo_id() {
		return examinfo_id;
	}
	public void setExaminfo_id(String examinfo_id) {
		this.examinfo_id = examinfo_id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPersionid() {
		return persionid;
	}
	public void setPersionid(String persionid) {
		this.persionid = persionid;
	}
	public String getCodetext() {
		return codetext;
	}
	public void setCodetext(String codetext) {
		this.codetext = codetext;
	}
   
}
