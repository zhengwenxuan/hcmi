package com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "itemList")  
@XmlType(propOrder = {})
public class ItemList {

	@XmlElement
	private List<ItemYC> Item = new ArrayList<ItemYC>();//itemList

	public List<ItemYC> getItem() {
		return Item;
	}

	public void setItem(List<ItemYC> item) {
		this.Item = item;
	}
}
