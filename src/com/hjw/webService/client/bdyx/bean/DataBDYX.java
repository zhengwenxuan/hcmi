package com.hjw.webService.client.bdyx.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "data")  
@XmlType(propOrder = {})
public class DataBDYX {

	@XmlElement
	private String name_pkgu_med = "";//计量单位
	@XmlElement
    private String wbcode = "";//五笔码
	@XmlElement
    private String name = "";//名称
	@XmlElement
    private double price_std = 0.0;//价格
	@XmlElement
    private String code = "";//编码
	@XmlElement
    private String pycode = "";//拼音码
	@XmlElement
    private String fg_active = "";//启用标识
    
	public String getName_pkgu_med() {
		return name_pkgu_med;
	}
	public String getWbcode() {
		return wbcode;
	}
	public String getName() {
		return name;
	}
	public double getPrice_std() {
		return price_std;
	}
	public String getCode() {
		return code;
	}
	public String getPycode() {
		return pycode;
	}
	public String getFg_active() {
		return fg_active;
	}
	public void setName_pkgu_med(String name_pkgu_med) {
		this.name_pkgu_med = name_pkgu_med;
	}
	public void setWbcode(String wbcode) {
		this.wbcode = wbcode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice_std(double price_std) {
		this.price_std = price_std;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setPycode(String pycode) {
		this.pycode = pycode;
	}
	public void setFg_active(String fg_active) {
		this.fg_active = fg_active;
	}
	
}
