package com.hjw.webService.client.Bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})  
public class ApplyNOBean {

	@XmlElement  
    private String ApplyNO;//申请单号
	
	private String lis_id;//
	private String barcode;//申请号(即条码)	
	private List<String> itemCodeList;

	public String getLis_id() {
		return lis_id;
	}

	public void setLis_id(String lis_id) {
		this.lis_id = lis_id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getApplyNO() {
		return ApplyNO;
	}

	public void setApplyNO(String applyNO) {
		ApplyNO = applyNO;
	}

	public List<String> getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List<String> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
	
	
}
