package com.hjw.webService.client.jinyu.lisbean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "LisItems")  
@XmlType(propOrder = {})
public class LisItems {
	
	@XmlElement
	private String lis_item_code = "";//送检项目代码
	
	@XmlElement
	private String lis_item_name = "";//送检项目名称
	
	@XmlElement
	private List<SubItems> SubItems = new ArrayList<>();//送检项目名称

	public String getLis_item_code() {
		return lis_item_code;
	}

	public void setLis_item_code(String lis_item_code) {
		this.lis_item_code = lis_item_code;
	}

	public String getLis_item_name() {
		return lis_item_name;
	}

	public void setLis_item_name(String lis_item_name) {
		this.lis_item_name = lis_item_name;
	}

	public List<SubItems> getSubItems() {
		return SubItems;
	}

	public void setSubItems(List<SubItems> subItems) {
		SubItems = subItems;
	}
	
}
