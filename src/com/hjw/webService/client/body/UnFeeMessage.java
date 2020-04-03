package com.hjw.webService.client.body;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.ReqUnNo;
import com.hjw.webService.service.bean.ReqItem;
import com.hjw.webService.service.bean.RetReqNo;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class UnFeeMessage {
	@XmlElement
	private String MSG_TYPE="TJ604";
	@XmlElement
	private ReqUnNo REQ_NOS=new ReqUnNo();//申请单号
	@XmlElement
	private String EXAM_NUM;//体检编号
	@XmlElement
	private String RCPT_NO;//收据号
	
	private List<String> itemCodeList = new ArrayList<String>();	
	
	public List<String> getItemCodeList() {
		return itemCodeList;
	}
	public void setItemCodeList(List<String> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
	public String getEXAM_NUM() {
		return EXAM_NUM;
	}
	public void setEXAM_NUM(String eXAM_NUM) {
		EXAM_NUM = eXAM_NUM;
	}	
	public ReqUnNo getREQ_NOS() {
		return REQ_NOS;
	}
	public void setREQ_NOS(ReqUnNo rEQ_NOS) {
		REQ_NOS = rEQ_NOS;
	}
	public String getMSG_TYPE() {
		return MSG_TYPE;
	}
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public String getRCPT_NO() {
		return RCPT_NO;
	}
	public void setRCPT_NO(String rCPT_NO) {
		RCPT_NO = rCPT_NO;
	}

	
}
