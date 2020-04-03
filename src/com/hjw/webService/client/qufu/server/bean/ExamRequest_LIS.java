package com.hjw.webService.client.qufu.server.bean;//

import javax.xml.bind.annotation.XmlAccessType;//
import javax.xml.bind.annotation.XmlAccessorType;//
import javax.xml.bind.annotation.XmlElement;//
import javax.xml.bind.annotation.XmlRootElement;//
import javax.xml.bind.annotation.XmlType;//

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ExamRequest")  
@XmlType(propOrder = {})
public class ExamRequest_LIS {
	
	@XmlElement
	private String PAT_TYPE = "";//来源 – 体检(由平台增加来源字典定义)
	@XmlElement
	private String BARCODE = "";//申请单号/条码号/医嘱单号
	@XmlElement
	private String PAT_LIS_NO = "";//Lis唯一值（条码号+项目编码）
	@XmlElement
	private String EXAM_NO = "";//体检编号
	@XmlElement
	private String PAT_ID = "";//体检客户ID
	@XmlElement
	private String PAT_NAME = "";//体检客户姓名
	@XmlElement
	private String PAT_SEX = "";//体检客户性别
	@XmlElement
	private String PAT_BIRTH = "";//体检客户出生日期
	@XmlElement
	private String REQ_DEPTNO = "";//申请科室编号
	@XmlElement
	private String REQ_DOCNO = "";//申请医生编码
	@XmlElement
	private String KSBM = "";//执行科室编号
	@XmlElement
	private String APPLICATION_DATE = "";//申请时间
	@XmlElement
	private String SPECIMEN_NAME = "";//标本类型 如：血、尿等
	@XmlElement
	private String REQ_ITEMCODE = "";//项目编码
	@XmlElement
	private String REQ_ITEMNAME = "";//项目名称
	@XmlElement
	private String SFZH = "";//身份证号
	
	public String getPAT_TYPE() {
		return PAT_TYPE;
	}
	public void setPAT_TYPE(String pAT_TYPE) {
		PAT_TYPE = pAT_TYPE;
	}
	public String getBARCODE() {
		return BARCODE;
	}
	public void setBARCODE(String bARCODE) {
		BARCODE = bARCODE;
	}
	public String getPAT_LIS_NO() {
		return PAT_LIS_NO;
	}
	public void setPAT_LIS_NO(String pAT_LIS_NO) {
		PAT_LIS_NO = pAT_LIS_NO;
	}
	public String getEXAM_NO() {
		return EXAM_NO;
	}
	public void setEXAM_NO(String eXAM_NO) {
		EXAM_NO = eXAM_NO;
	}
	public String getPAT_ID() {
		return PAT_ID;
	}
	public void setPAT_ID(String pAT_ID) {
		PAT_ID = pAT_ID;
	}
	public String getPAT_NAME() {
		return PAT_NAME;
	}
	public void setPAT_NAME(String pAT_NAME) {
		PAT_NAME = pAT_NAME;
	}
	public String getPAT_SEX() {
		return PAT_SEX;
	}
	public void setPAT_SEX(String pAT_SEX) {
		PAT_SEX = pAT_SEX;
	}
	public String getPAT_BIRTH() {
		return PAT_BIRTH;
	}
	public void setPAT_BIRTH(String pAT_BIRTH) {
		PAT_BIRTH = pAT_BIRTH;
	}
	public String getREQ_DEPTNO() {
		return REQ_DEPTNO;
	}
	public void setREQ_DEPTNO(String rEQ_DEPTNO) {
		REQ_DEPTNO = rEQ_DEPTNO;
	}
	public String getREQ_DOCNO() {
		return REQ_DOCNO;
	}
	public void setREQ_DOCNO(String rEQ_DOCNO) {
		REQ_DOCNO = rEQ_DOCNO;
	}
	public String getKSBM() {
		return KSBM;
	}
	public void setKSBM(String kSBM) {
		KSBM = kSBM;
	}
	public String getAPPLICATION_DATE() {
		return APPLICATION_DATE;
	}
	public void setAPPLICATION_DATE(String aPPLICATION_DATE) {
		APPLICATION_DATE = aPPLICATION_DATE;
	}
	public String getSPECIMEN_NAME() {
		return SPECIMEN_NAME;
	}
	public void setSPECIMEN_NAME(String sPECIMEN_NAME) {
		SPECIMEN_NAME = sPECIMEN_NAME;
	}
	public String getREQ_ITEMCODE() {
		return REQ_ITEMCODE;
	}
	public void setREQ_ITEMCODE(String rEQ_ITEMCODE) {
		REQ_ITEMCODE = rEQ_ITEMCODE;
	}
	public String getREQ_ITEMNAME() {
		return REQ_ITEMNAME;
	}
	public void setREQ_ITEMNAME(String rEQ_ITEMNAME) {
		REQ_ITEMNAME = rEQ_ITEMNAME;
	}
	public String getSFZH() {
		return SFZH;
	}
	public void setSFZH(String sFZH) {
		SFZH = sFZH;
	}
	
}
