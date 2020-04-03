package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ControlActProcess")  
@XmlType(propOrder = {})  
public class FeeReqControlActProcess {

	@XmlElement  
	private List<ReqNo> List=new ArrayList<ReqNo>();
	
	public List<ReqNo> getList() {
		return List;
	}

	public void setList(List<ReqNo> list) {
		List = list;
	}

	
}
