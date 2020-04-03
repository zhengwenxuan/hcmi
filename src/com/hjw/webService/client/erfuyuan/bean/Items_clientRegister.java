package com.hjw.webService.client.erfuyuan.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "items")
@XmlType(propOrder = {})
public class Items_clientRegister {
	
	private List<Item_clientRegister> item = new ArrayList<>();//

	public List<Item_clientRegister> getItem() {
		return item;
	}

	public void setItem(List<Item_clientRegister> item) {
		this.item = item;
	}

}
