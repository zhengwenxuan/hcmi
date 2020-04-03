package com.hjw.webService.client.hzty.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "RENYUANMXCX_IN")  
@XmlType(propOrder = {})  
public class RENYUANMXCXIN {
	@XmlElement 
	private BASEINFO BASEINFO= new BASEINFO();
	
	@XmlElement 
	private String CHAXUNLX="1"; //查询类型  1、身份证号 2、就诊卡号 3、病案号
	
	@XmlElement 
	private String CHAXUNHM="";// 查询号码   
	
	@XmlElement 
	private String BINGRENLB="0";// 病人类别  0 全部，其它值详见附 录 	

	public BASEINFO getBASEINFO() {
		return BASEINFO;
	}

	public void setBASEINFO(BASEINFO bASEINFO) {
		BASEINFO = bASEINFO;
	}

	public String getCHAXUNLX() {
		return CHAXUNLX;
	}

	public void setCHAXUNLX(String cHAXUNLX) {
		CHAXUNLX = cHAXUNLX;
	}

	public String getCHAXUNHM() {
		return CHAXUNHM;
	}

	public void setCHAXUNHM(String cHAXUNHM) {
		CHAXUNHM = cHAXUNHM;
	}

	public String getBINGRENLB() {
		return BINGRENLB;
	}

	public void setBINGRENLB(String bINGRENLB) {
		BINGRENLB = bINGRENLB;
	}
	
}
