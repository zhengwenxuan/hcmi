package com.hjw.webService.client.erfuyuan.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "client")  
@XmlType(propOrder = {})
public class client {

	@XmlAttribute(name = "categoryid")
	private String categoryid = "";//科室名称
	@XmlAttribute(name = "name")
	private String name = "";//客户姓名
	@XmlAttribute(name = "card")
	private String card = "";//体检编号
	
	public String getCategoryid() {
		return categoryid;
	}
	public String getName() {
		return name;
	}
	public String getCard() {
		return card;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCard(String card) {
		this.card = card;
	}
}
