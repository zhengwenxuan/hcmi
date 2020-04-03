package com.hjw.webService.client.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "feeitem")  
@XmlType(propOrder = {})  
public class FeeRes {
	@XmlElement
	  private String PATIENT_ID;//病案ID号
	@XmlElement
	  private String EXAM_NUM;//体检号
	@XmlElement
	  private String FEE_CODE;//his缴费系统对应的缴费项目编码		
	@XmlElement
	  private String CHARGES;//                      	实收费用	
	@XmlElement
	private String exam_chargeItem_code;//对应体检系统编码
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getEXAM_NUM() {
		return EXAM_NUM;
	}
	public void setEXAM_NUM(String eXAM_NUM) {
		EXAM_NUM = eXAM_NUM;
	}
	public String getFEE_CODE() {
		return FEE_CODE;
	}
	public void setFEE_CODE(String fEE_CODE) {
		FEE_CODE = fEE_CODE;
	}
	public String getCHARGES() {
		return CHARGES;
	}
	public void setCHARGES(String cHARGES) {
		CHARGES = cHARGES;
	}
	public String getExam_chargeItem_code() {
		return exam_chargeItem_code;
	}
	public void setExam_chargeItem_code(String exam_chargeItem_code) {
		this.exam_chargeItem_code = exam_chargeItem_code;
	}	

}
