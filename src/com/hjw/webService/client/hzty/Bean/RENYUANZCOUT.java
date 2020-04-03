package com.hjw.webService.client.hzty.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "RENYUANZC_OUT")  
@XmlType(propOrder = {})  
public class RENYUANZCOUT {
	@XmlElement 
	private OUTMSG OUTMSG= new OUTMSG();
	
	@XmlElement 
	private String JIUZHENKH="";// 就诊卡号   
	
	@XmlElement 
	private String CHONGFUJYMX="";// 重复交易信息   
	
	@XmlElement 
	private String XUNIZH="";// 虚拟账户   
	
	@XmlElement 
	private String BINGRENID="";// 病人 ID   
	
	@XmlElement 
	private String BINGANH="";// 病案号 	

	public OUTMSG getOUTMSG() {
		return OUTMSG;
	}

	public void setOUTMSG(OUTMSG oUTMSG) {
		OUTMSG = oUTMSG;
	}

	public String getJIUZHENKH() {
		return JIUZHENKH;
	}

	public void setJIUZHENKH(String jIUZHENKH) {
		JIUZHENKH = jIUZHENKH;
	}

	public String getCHONGFUJYMX() {
		return CHONGFUJYMX;
	}

	public void setCHONGFUJYMX(String cHONGFUJYMX) {
		CHONGFUJYMX = cHONGFUJYMX;
	}

	public String getXUNIZH() {
		return XUNIZH;
	}

	public void setXUNIZH(String xUNIZH) {
		XUNIZH = xUNIZH;
	}

	public String getBINGRENID() {
		return BINGRENID;
	}

	public void setBINGRENID(String bINGRENID) {
		BINGRENID = bINGRENID;
	}

	public String getBINGANH() {
		return BINGANH;
	}

	public void setBINGANH(String bINGANH) {
		BINGANH = bINGANH;
	}

}
