package com.hjw.webService.client.liubaxian.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ItemList")  
@XmlType(propOrder = {})
public class ItemList_Apply {

	@XmlElement
	private List<Item_Apply> Item = new ArrayList<>();//

	public List<Item_Apply> getItem() {
		return Item;
	}

	public void setItem(List<Item_Apply> item) {
		Item = item;
	}
}
