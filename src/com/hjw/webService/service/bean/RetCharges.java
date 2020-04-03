package com.hjw.webService.service.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.service.lisbean.ChargeTypes;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "CHARGES")  
@XmlType(propOrder = {}) 
public class RetCharges{
	//<!-- 收据号号 -->
	@XmlElement
	private String RCPT_NO;
		//<!-- 收费员编号 -->
	@XmlElement
	private String OPERATOR_NO;
		//<!-- 收费员姓名 -->
	@XmlElement
	private String OPERATOR_NAME;
		///<!-- 收费日期 -->
	@XmlElement
	private String CHARGE_DATE;
		//<!-- 收费总金额 -->
	@XmlElement
	private String CHARGE;
		//<!-- 发票号 -->
	@XmlElement
	private String INVOICE_NO;

	@XmlElement
	private ReqItem REQ_NOS=new ReqItem();//申请单号
	
	@XmlElement
	private ChargeTypes  CHARGE_TYPES=new ChargeTypes();//	

	public ReqItem getREQ_NOS() {
		return REQ_NOS;
	}

	public void setREQ_NOS(ReqItem rEQ_NOS) {
		REQ_NOS = rEQ_NOS;
	}

	public String getRCPT_NO() {
		return RCPT_NO;
	}

	public void setRCPT_NO(String rCPT_NO) {
		RCPT_NO = rCPT_NO;
	}

	public String getOPERATOR_NO() {
		return OPERATOR_NO;
	}

	public void setOPERATOR_NO(String oPERATOR_NO) {
		OPERATOR_NO = oPERATOR_NO;
	}

	public String getOPERATOR_NAME() {
		return OPERATOR_NAME;
	}

	public void setOPERATOR_NAME(String oPERATOR_NAME) {
		OPERATOR_NAME = oPERATOR_NAME;
	}

	public String getCHARGE_DATE() {
		return CHARGE_DATE;
	}

	public void setCHARGE_DATE(String cHARGE_DATE) {
		CHARGE_DATE = cHARGE_DATE;
	}

	public String getCHARGE() {
		return CHARGE;
	}

	public void setCHARGE(String cHARGE) {
		CHARGE = cHARGE;
	}

	public String getINVOICE_NO() {
		return INVOICE_NO;
	}

	public void setINVOICE_NO(String iNVOICE_NO) {
		INVOICE_NO = iNVOICE_NO;
	}

	public ChargeTypes getCHARGE_TYPES() {
		return CHARGE_TYPES;
	}

	public void setCHARGE_TYPES(ChargeTypes cHARGE_TYPES) {
		CHARGE_TYPES = cHARGE_TYPES;
	}

	
	
}
