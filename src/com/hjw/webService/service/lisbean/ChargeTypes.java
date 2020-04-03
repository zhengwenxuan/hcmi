package com.hjw.webService.service.lisbean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "CHARGE_TYPES")  
@XmlType(propOrder = {})  
public class ChargeTypes {
	
	@XmlElement 
	private List<ChargeType> CHARGE_TYPE= new ArrayList<ChargeType>();

	public List<ChargeType> getCHARGE_TYPE() {
		return CHARGE_TYPE;
	}

	public void setCHARGE_TYPE(List<ChargeType> cHARGE_TYPE) {
		CHARGE_TYPE = cHARGE_TYPE;
	}
}
