package com.hjw.webService.client.sxwn.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "NewDataSet")  
@XmlType(propOrder = {})  
public class LisItemNewDataSet {
	@XmlElement
 private List<TableItemBean> Table= new ArrayList<TableItemBean>();

	public List<TableItemBean> getTable() {
		return Table;
	}

	public void setTable(List<TableItemBean> table) {
		Table = table;
	}
	
	
}
