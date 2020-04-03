package com.hjw.webService.client.yichang.bean.cdr.client.requestForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "itemList")  
@XmlType(propOrder = {})
public class ItemCDRYC {
	
	@XmlElement
	private String examinationSiteCode = "";//
	@XmlElement
	private String examinationSiteName = "";//
	@XmlElement
	private String itemCode = "";//
	@XmlElement
	private String itemName = "";//
	@XmlElement
	private String itemPrice = "";//
	@XmlElement
	private String orderNo = "";//
	@XmlElement
	private String pacsTestMethodCode = "";//
	@XmlElement
	private String pacsTestMethodName = "无";//
	@XmlElement
	private String specimenTypeCode = "";//
	@XmlElement
	private String specimenTypeName = "";//
	
	public String getExaminationSiteCode() {
		return examinationSiteCode;
	}
	public String getExaminationSiteName() {
		return examinationSiteName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public String getPacsTestMethodCode() {
		return pacsTestMethodCode;
	}
	public String getPacsTestMethodName() {
		return pacsTestMethodName;
	}
	public String getSpecimenTypeCode() {
		return specimenTypeCode;
	}
	public String getSpecimenTypeName() {
		return specimenTypeName;
	}
	public void setExaminationSiteCode(String examinationSiteCode) {
		this.examinationSiteCode = examinationSiteCode;
	}
	public void setExaminationSiteName(String examinationSiteName) {
		this.examinationSiteName = examinationSiteName;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public void setPacsTestMethodCode(String pacsTestMethodCode) {
		this.pacsTestMethodCode = pacsTestMethodCode;
	}
	public void setPacsTestMethodName(String pacsTestMethodName) {
		this.pacsTestMethodName = pacsTestMethodName;
	}
	public void setSpecimenTypeCode(String specimenTypeCode) {
		this.specimenTypeCode = specimenTypeCode;
	}
	public void setSpecimenTypeName(String specimenTypeName) {
		this.specimenTypeName = specimenTypeName;
	}
}
