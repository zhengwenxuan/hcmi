package com.hjw.webService.service.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class ResultSerBody {
	@XmlElement
	private String MSG_TYPE;
	@XmlElement
	private String EXAM_NUM;//返回
	@XmlElement
	private String PATIENT_ID;
		//<!-- 姓名 -->
	@XmlElement
	private String NAME;
		//<!-- 收费明细 -->
	@XmlElement
	private RetCharges CHARGES= new RetCharges();
	public String getMSG_TYPE() {
		return MSG_TYPE;
	}
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public String getEXAM_NUM() {
		return EXAM_NUM;
	}
	public void setEXAM_NUM(String eXAM_NUM) {
		EXAM_NUM = eXAM_NUM;
	}
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public RetCharges getCHARGES() {
		return CHARGES;
	}
	public void setCHARGES(RetCharges cHARGES) {
		CHARGES = cHARGES;
	}
	
	
}
