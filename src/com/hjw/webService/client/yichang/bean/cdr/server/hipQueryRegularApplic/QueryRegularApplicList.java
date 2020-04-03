package com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "queryRegularApplicList")  
@XmlType(propOrder = {})
public class QueryRegularApplicList {

	@XmlElement
	private List<QueryRegularApplic> QueryRegularApplic = new ArrayList<QueryRegularApplic>();

	public List<QueryRegularApplic> getQueryRegularApplic() {
		return QueryRegularApplic;
	}

	public void setQueryRegularApplic(List<QueryRegularApplic> queryRegularApplic) {
		this.QueryRegularApplic = queryRegularApplic;
	}
}
