package com.hjw.webService.client.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.Bean.Fees;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class FeeMessage {
	@XmlElement
	private String MSG_TYPE="TJ602";//	服务编码		TJ602（固定）
	@XmlElement
	private String REQ_NO;//申请单号
	@XmlElement
	private Fees PROJECTS= new Fees();//
	
	public String getMSG_TYPE() {
		return MSG_TYPE;
	}
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public String getREQ_NO() {
		return REQ_NO;
	}
	public void setREQ_NO(String rEQ_NO) {
		REQ_NO = rEQ_NO;
	}
	public Fees getPROJECTS() {
		return PROJECTS;
	}
	public void setPROJECTS(Fees pROJECTS) {
		PROJECTS = pROJECTS;
	}	
	
}
