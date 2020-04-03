package com.hjw.webService.client.qufu.server.bean;//

import javax.xml.bind.annotation.XmlAccessType;//
import javax.xml.bind.annotation.XmlAccessorType;//
import javax.xml.bind.annotation.XmlElement;//
import javax.xml.bind.annotation.XmlRootElement;//
import javax.xml.bind.annotation.XmlType;//

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ExamRequest")  
@XmlType(propOrder = {})
public class ExamRequest_PACS {
	
	@XmlElement
	private String PAT_TYPE = "";//来源 – 体检(由平台增加来源字典定义)
	@XmlElement
	private String DJLSH = "";//申请单号/条码号/登记流水号（唯一键）
	@XmlElement
	private String TJLSH  = "";//体检流水号,赋值为申请单号即可
	@XmlElement
	private String EXAM_NO = "";//体检编号
	@XmlElement
	private String XM = "";//姓名
	@XmlElement
	private String XB = "";//1男0女%未知
	@XmlElement
	private String CSNYR = "";//出生年月日
	@XmlElement
	private String NL = "";//年龄
	@XmlElement
	private String APPLICATION_DATE = "";//申请时间
	@XmlElement
	private String DWBH = "";//单位编号
	@XmlElement
	private String SFZH = "";//身份证号
	@XmlElement
	private String HYZK = "";//婚姻状况
	@XmlElement
	private String ADDRESS = "";//联系地址
	@XmlElement
	private String PHONE = "";//联系电话
	@XmlElement
	private String MOBILE = "";//家属联系电话
	@XmlElement
	private String XMBH = "";//项目编号
	@XmlElement
	private String XMMC = "";//项目名称
	@XmlElement
	private String KSMC = "";//执行科室名称
	@XmlElement
	private String KSBM = "";//执行科室编号
	@XmlElement
	private String KDYSXM = "";//体检科开单医生姓名
	@XmlElement
	private String KSLB = "体检科";//科室类别，默认是体检科
	
	public String getPAT_TYPE() {
		return PAT_TYPE;
	}
	public void setPAT_TYPE(String pAT_TYPE) {
		PAT_TYPE = pAT_TYPE;
	}
	public String getDJLSH() {
		return DJLSH;
	}
	public void setDJLSH(String dJLSH) {
		DJLSH = dJLSH;
	}
	public String getEXAM_NO() {
		return EXAM_NO;
	}
	public void setEXAM_NO(String eXAM_NO) {
		EXAM_NO = eXAM_NO;
	}
	public String getXM() {
		return XM;
	}
	public void setXM(String xM) {
		XM = xM;
	}
	public String getXB() {
		return XB;
	}
	public void setXB(String xB) {
		XB = xB;
	}
	public String getCSNYR() {
		return CSNYR;
	}
	public void setCSNYR(String cSNYR) {
		CSNYR = cSNYR;
	}
	public String getNL() {
		return NL;
	}
	public void setNL(String nL) {
		NL = nL;
	}
	public String getAPPLICATION_DATE() {
		return APPLICATION_DATE;
	}
	public void setAPPLICATION_DATE(String aPPLICATION_DATE) {
		APPLICATION_DATE = aPPLICATION_DATE;
	}
	public String getDWBH() {
		return DWBH;
	}
	public void setDWBH(String dWBH) {
		DWBH = dWBH;
	}
	public String getSFZH() {
		return SFZH;
	}
	public void setSFZH(String sFZH) {
		SFZH = sFZH;
	}
	public String getHYZK() {
		return HYZK;
	}
	public void setHYZK(String hYZK) {
		HYZK = hYZK;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getXMBH() {
		return XMBH;
	}
	public void setXMBH(String xMBH) {
		XMBH = xMBH;
	}
	public String getXMMC() {
		return XMMC;
	}
	public void setXMMC(String xMMC) {
		XMMC = xMMC;
	}
	public String getKSMC() {
		return KSMC;
	}
	public void setKSMC(String kSMC) {
		KSMC = kSMC;
	}
	public String getKSBM() {
		return KSBM;
	}
	public void setKSBM(String kSBM) {
		KSBM = kSBM;
	}
	public String getKDYSXM() {
		return KDYSXM;
	}
	public void setKDYSXM(String kDYSXM) {
		KDYSXM = kDYSXM;
	}
	public String getKSLB() {
		return KSLB;
	}
	public void setKSLB(String kSLB) {
		KSLB = kSLB;
	}
	public String getTJLSH() {
		return TJLSH;
	}
	public void setTJLSH(String tJLSH) {
		TJLSH = tJLSH;
	}
}
