package com.hjw.webService.client.jinyu.lisbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "SubItems")  
@XmlType(propOrder = {})
public class SubItems {
	
	@XmlElement
	private String lis_subitem_code = "";//子项项目代码
	
	@XmlElement
	private String lis_subitem_name = "";//子项项目名称

	public String getLis_subitem_code() {
		return lis_subitem_code;
	}

	public void setLis_subitem_code(String lis_subitem_code) {
		this.lis_subitem_code = lis_subitem_code;
	}

	public String getLis_subitem_name() {
		return lis_subitem_name;
	}

	public void setLis_subitem_name(String lis_subitem_name) {
		this.lis_subitem_name = lis_subitem_name;
	}
	
}
