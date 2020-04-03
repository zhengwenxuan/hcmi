package com.hjw.webService.client.bdyx.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "datalist")  
@XmlType(propOrder = {})
public class Datalist {
	private List<DataBDYX> data;

	public List<DataBDYX> getData() {
		return data;
	}

	public void setData(List<DataBDYX> data) {
		this.data = data;
	}
}
