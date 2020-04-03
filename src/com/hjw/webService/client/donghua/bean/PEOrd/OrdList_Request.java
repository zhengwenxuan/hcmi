package com.hjw.webService.client.donghua.bean.PEOrd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "OrdInfo")  
@XmlType(propOrder = {})
public class OrdList_Request {

	private List<OrdInfo_Request> OrdInfo = new ArrayList<>();//医嘱信息节点

	public List<OrdInfo_Request> getOrdInfo() {
		return OrdInfo;
	}

	public void setOrdInfo(List<OrdInfo_Request> ordInfo) {
		OrdInfo = ordInfo;
	}
}
