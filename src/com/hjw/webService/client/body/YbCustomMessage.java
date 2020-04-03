package com.hjw.webService.client.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class YbCustomMessage {
	@XmlElement
	private String MSG_TYPE="TJ607";//	服务编码
	@XmlElement
    private String VISIT_DEPT;//就诊科室
	@XmlElement
    private String VISIT_DATE;//就诊日期
	@XmlElement
	private String MC_NO;//医保卡号
	@XmlElement
	private String CARD_CLASS;//医保卡和就诊卡类别，医保卡为1，就诊卡为0	
	
	public String getVISIT_DEPT() {
		return VISIT_DEPT;
	}
	public void setVISIT_DEPT(String vISIT_DEPT) {
		VISIT_DEPT = vISIT_DEPT;
	}
	public String getVISIT_DATE() {
		return VISIT_DATE;
	}
	public void setVISIT_DATE(String vISIT_DATE) {
		VISIT_DATE = vISIT_DATE;
	}
	public String getMSG_TYPE() {
		return MSG_TYPE;
	}
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public String getMC_NO() {
		return MC_NO;
	}
	public void setMC_NO(String mC_NO) {
		MC_NO = mC_NO;
	}
	public String getCARD_CLASS() {
		return CARD_CLASS;
	}
	public void setCARD_CLASS(String cARD_CLASS) {
		CARD_CLASS = cARD_CLASS;
	}

}
