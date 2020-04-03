package com.hjw.webService.client.yichang.bean.cdr.client.update;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "registUpdatePatienInfo")  
@XmlType(propOrder = {})
public class RegistUpdatePatienInfo {
	
	@XmlElement
	private RegistUpdatePatienInfoReq registUpdatePatienInfoReq = new RegistUpdatePatienInfoReq();//

	public RegistUpdatePatienInfoReq getRegistUpdatePatienInfoReq() {
		return registUpdatePatienInfoReq;
	}

	public void setRegistUpdatePatienInfoReq(RegistUpdatePatienInfoReq registUpdatePatienInfoReq) {
		this.registUpdatePatienInfoReq = registUpdatePatienInfoReq;
	}

}
