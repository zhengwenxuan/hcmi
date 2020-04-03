package com.hjw.webService.service.lisbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "CHARGE_TYPE")  
@XmlType(propOrder = {})  
public class ChargeType {
	
	@XmlElement 
	private String CHARGE_TYPE_CODE;
	//<!-- 收费方式名称-->
	@XmlElement 
	private String CHARGE_TYPE;
	//<!-- 收费金额 -->
	@XmlElement 
	private String AMOUNT;
	public String getCHARGE_TYPE_CODE() {
		return CHARGE_TYPE_CODE;
	}
	public void setCHARGE_TYPE_CODE(String cHARGE_TYPE_CODE) {
		CHARGE_TYPE_CODE = cHARGE_TYPE_CODE;
	}
	public String getCHARGE_TYPE() {
		return CHARGE_TYPE;
	}
	public void setCHARGE_TYPE(String cHARGE_TYPE) {
		CHARGE_TYPE = cHARGE_TYPE;
	}
	public String getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}

}
