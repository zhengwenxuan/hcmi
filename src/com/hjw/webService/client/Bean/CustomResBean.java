package com.hjw.webService.client.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "LIST")  
@XmlType(propOrder = {})  
public class CustomResBean {
	
	@XmlElement  
	private String CLINIC_NO;//门诊号
	@XmlElement  
	private String PATIENT_ID;//病人id，处理成功后返回
	@XmlElement  
	private String VISIT_DATE;//就诊日期
	@XmlElement  
	private String VISIT_NO;//就诊序号
	public String getCLINIC_NO() {
		return CLINIC_NO;
	}
	public void setCLINIC_NO(String cLINIC_NO) {
		CLINIC_NO = cLINIC_NO;
	}
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getVISIT_DATE() {
		return VISIT_DATE;
	}
	public void setVISIT_DATE(String vISIT_DATE) {
		VISIT_DATE = vISIT_DATE;
	}
	public String getVISIT_NO() {
		return VISIT_NO;
	}
	public void setVISIT_NO(String vISIT_NO) {
		VISIT_NO = vISIT_NO;
	}
	
	
}
