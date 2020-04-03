package com.hjw.webService.client.donghua.bean.lis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "OrgResultMsg")  
@XmlType(propOrder = {})
public class OrgResultMsg {

	@XmlElement
	private String BugsCode = "";//细菌代码
	@XmlElement
	private String AntiCode = "";//抗生素代码
	@XmlElement
	private String AntiName = "";//名称
	@XmlElement
	private String AntiEngName = "";//缩写
	@XmlElement
	private String MICRes = "";//MIC结果
	@XmlElement
	private String KBRes = "";//KB结果
	@XmlElement
	private String Result = "";//药敏结果
	@XmlElement
	private String MICRanges = "";//MIC判定范围
	@XmlElement
	private String KBRanges = "";//KB判定范围
	@XmlElement
	private String Order = "";//显示顺序
	
	public String getBugsCode() {
		return BugsCode;
	}
	public String getAntiCode() {
		return AntiCode;
	}
	public String getAntiName() {
		return AntiName;
	}
	public String getAntiEngName() {
		return AntiEngName;
	}
	public String getMICRes() {
		return MICRes;
	}
	public String getKBRes() {
		return KBRes;
	}
	public String getResult() {
		return Result;
	}
	public String getMICRanges() {
		return MICRanges;
	}
	public String getKBRanges() {
		return KBRanges;
	}
	public String getOrder() {
		return Order;
	}
	public void setBugsCode(String bugsCode) {
		BugsCode = bugsCode;
	}
	public void setAntiCode(String antiCode) {
		AntiCode = antiCode;
	}
	public void setAntiName(String antiName) {
		AntiName = antiName;
	}
	public void setAntiEngName(String antiEngName) {
		AntiEngName = antiEngName;
	}
	public void setMICRes(String mICRes) {
		MICRes = mICRes;
	}
	public void setKBRes(String kBRes) {
		KBRes = kBRes;
	}
	public void setResult(String result) {
		Result = result;
	}
	public void setMICRanges(String mICRanges) {
		MICRanges = mICRanges;
	}
	public void setKBRanges(String kBRanges) {
		KBRanges = kBRanges;
	}
	public void setOrder(String order) {
		Order = order;
	}
}
