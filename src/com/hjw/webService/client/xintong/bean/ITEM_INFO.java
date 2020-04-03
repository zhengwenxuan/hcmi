package com.hjw.webService.client.xintong.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ITEM_INFO")  
@XmlType(propOrder = {})
public class ITEM_INFO {
	
	@XmlElement(name = "PACK_FLAG")
	private String PACK_FLAG;//包项目标识
	
	@XmlElement(name = "ITEM_ID")
	private String ITEM_ID;//项目ID
	
	@XmlElement(name = "ITEM_CODE")
	private String ITEM_CODE;//项目代码
	
	@XmlElement(name = "ITEM_NAME")
	private String ITEM_NAME;//项目名称   
	
	@XmlElement(name = "SPEC_NAME")
	private String SPEC_NAME;//规格  项/个 expand1  
	
	@XmlElement(name = "UNIT_PRICE")
	private String UNIT_PRICE;//收费标准
	
	@XmlElement(name = "CHARGE_UNIT")
	private String CHARGE_UNIT;//计价单位  expand2
	
	@XmlElement(name = "OP_CRG_TYPE")
	private String OP_CRG_TYPE;//门诊费别代码
	
	@XmlElement(name = "IP_CRG_TYPE")
	private String IP_CRG_TYPE;//住院费别代码
	
	@XmlElement(name = "FP_CRG_TYPE")
	private String FP_CRG_TYPE;//病案首页费别代码
	
	@XmlElement(name = "IS_VALID")
	private String IS_VALID;//是否停用 1 是， 2 否
	
	@XmlElement(name = "LAST_OP_DATE")
	private String LAST_OP_DATE;//最后修改时间
	
	public String getPACK_FLAG() {
		return PACK_FLAG;
	}
	public void setPACK_FLAG(String pACK_FLAG) {
		PACK_FLAG = pACK_FLAG;
	}
	public String getITEM_CODE() {
		return ITEM_CODE;
	}
	public void setITEM_CODE(String iTEM_CODE) {
		ITEM_CODE = iTEM_CODE;
	}
	public String getITEM_NAME() {
		return ITEM_NAME;
	}
	public void setITEM_NAME(String iTEM_NAME) {
		ITEM_NAME = iTEM_NAME;
	}
	public String getSPEC_NAME() {
		return SPEC_NAME;
	}
	public void setSPEC_NAME(String sPEC_NAME) {
		SPEC_NAME = sPEC_NAME;
	}
	public String getUNIT_PRICE() {
		return UNIT_PRICE;
	}
	public void setUNIT_PRICE(String uNIT_PRICE) {
		UNIT_PRICE = uNIT_PRICE;
	}
	public String getCHARGE_UNIT() {
		return CHARGE_UNIT;
	}
	public void setCHARGE_UNIT(String cHARGE_UNIT) {
		CHARGE_UNIT = cHARGE_UNIT;
	}
	public String getOP_CRG_TYPE() {
		return OP_CRG_TYPE;
	}
	public void setOP_CRG_TYPE(String oP_CRG_TYPE) {
		OP_CRG_TYPE = oP_CRG_TYPE;
	}
	public String getIP_CRG_TYPE() {
		return IP_CRG_TYPE;
	}
	public void setIP_CRG_TYPE(String iP_CRG_TYPE) {
		IP_CRG_TYPE = iP_CRG_TYPE;
	}
	public String getFP_CRG_TYPE() {
		return FP_CRG_TYPE;
	}
	public void setFP_CRG_TYPE(String fP_CRG_TYPE) {
		FP_CRG_TYPE = fP_CRG_TYPE;
	}
	public String getITEM_ID() {
		return ITEM_ID;
	}
	public void setITEM_ID(String iTEM_ID) {
		ITEM_ID = iTEM_ID;
	}
	public String getIS_VALID() {
		return IS_VALID;
	}
	public void setIS_VALID(String iS_VALID) {
		IS_VALID = iS_VALID;
	}
	public String getLAST_OP_DATE() {
		return LAST_OP_DATE;
	}
	public void setLAST_OP_DATE(String lAST_OP_DATE) {
		LAST_OP_DATE = lAST_OP_DATE;
	}
	
	
	
	
}
