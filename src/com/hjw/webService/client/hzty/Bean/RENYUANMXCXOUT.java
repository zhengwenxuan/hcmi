package com.hjw.webService.client.hzty.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "RENYUANMXCX_OUT")  
@XmlType(propOrder = {})  
public class RENYUANMXCXOUT {

	@XmlElement 
	private OUTMSG OUTMSG= new OUTMSG();
	
	@XmlElement 
	private BINGRENMX BINGRENMX=new BINGRENMX();

	public OUTMSG getOUTMSG() {
		return OUTMSG;
	}

	public void setOUTMSG(OUTMSG oUTMSG) {
		OUTMSG = oUTMSG;
	}

	public BINGRENMX getBINGRENMX() {
		return BINGRENMX;
	}

	public void setBINGRENMX(BINGRENMX bINGRENMX) {
		BINGRENMX = bINGRENMX;
	}

}
