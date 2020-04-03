package com.hjw.webService.client.yichang.bean.caixueguan.out;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "job")  
@XmlType(propOrder = {})
public class LabelsReqForm {

	@XmlElement
	private List<ItemReqForm> item = new ArrayList<>();

	public List<ItemReqForm> getItem() {
		return item;
	}

	public void setItem(List<ItemReqForm> item) {
		this.item = item;
	}

}
