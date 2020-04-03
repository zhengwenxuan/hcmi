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
public class LisReqNewDataSet {
	@XmlElement
 private List<ReqTableBean> Table2= new ArrayList<ReqTableBean>();

	public List<ReqTableBean> getTable2() {
		return Table2;
	}

	public void setTable2(List<ReqTableBean> table2) {
		Table2 = table2;
	}	
}
