package com.hjw.webService.client.erfuyuan.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ClientInfo")
@XmlType(propOrder = {})
public class ClientInfo_clientRegister {
	
	@XmlAttribute
	private String name;//
	
	@XmlAttribute
	private String sex;//
	
	@XmlAttribute
	private String age;//
	
	@XmlAttribute
	private String clientid;//
	
	@XmlAttribute
	private String idcard;//
	
	@XmlAttribute
	private String phone;//
	
	@XmlAttribute
	private String regno;//
	
	@XmlAttribute
	private String viplevel;//
	
	@XmlAttribute
	private String regdate;// 
	
	@XmlElement
	private Items_clientRegister items = new Items_clientRegister();//

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
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegno() {
		return regno;
	}

	public void setRegno(String regno) {
		this.regno = regno;
	}

	public String getViplevel() {
		return viplevel;
	}

	public void setViplevel(String viplevel) {
		this.viplevel = viplevel;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public Items_clientRegister getItems() {
		return items;
	}

	public void setItems(Items_clientRegister items) {
		this.items = items;
	}

	
	
}
