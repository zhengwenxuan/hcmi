package com.hjw.webService.client.yichang.bean.cdr.client.list;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "patientAddressList")  
@XmlType(propOrder = {})
public class PatientAddress {
	
	@XmlElement
	private String addressCityCode = "";//
	@XmlElement
	private String addressCityName = "";//
	@XmlElement
	private String addressDistrictCode = "";//
	@XmlElement
	private String addressDistrictName = "";//
	@XmlElement
	private String addressName = "";//
	@XmlElement
	private String addressProvinceCode = "";//
	@XmlElement
	private String addressProvinceName = "";//
	@XmlElement
	private String addressType = "";//
	@XmlElement
	private String addressVillage = "";//
	@XmlElement
	private String codeExpand = "";//
	@XmlElement
	private String fullAddress = "";//
	
	public String getAddressCityCode() {
		return addressCityCode;
	}
	public void setAddressCityCode(String addressCityCode) {
		this.addressCityCode = addressCityCode;
	}
	public String getAddressCityName() {
		return addressCityName;
	}
	public void setAddressCityName(String addressCityName) {
		this.addressCityName = addressCityName;
	}
	public String getAddressDistrictCode() {
		return addressDistrictCode;
	}
	public void setAddressDistrictCode(String addressDistrictCode) {
		this.addressDistrictCode = addressDistrictCode;
	}
	public String getAddressDistrictName() {
		return addressDistrictName;
	}
	public void setAddressDistrictName(String addressDistrictName) {
		this.addressDistrictName = addressDistrictName;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getAddressProvinceCode() {
		return addressProvinceCode;
	}
	public void setAddressProvinceCode(String addressProvinceCode) {
		this.addressProvinceCode = addressProvinceCode;
	}
	public String getAddressProvinceName() {
		return addressProvinceName;
	}
	public void setAddressProvinceName(String addressProvinceName) {
		this.addressProvinceName = addressProvinceName;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public String getAddressVillage() {
		return addressVillage;
	}
	public void setAddressVillage(String addressVillage) {
		this.addressVillage = addressVillage;
	}
	public String getCodeExpand() {
		return codeExpand;
	}
	public void setCodeExpand(String codeExpand) {
		this.codeExpand = codeExpand;
	}
	public String getFullAddress() {
		return fullAddress;
	}
	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
}
