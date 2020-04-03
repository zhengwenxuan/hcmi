package com.hjw.webService.client.bdyx.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "response")  
@XmlType(propOrder = {})
public class ResponsePatReg {

	@XmlElement
	private int responseCode = 0;//返回码（1发卡成功、0发卡失败、-1his返回出错）
	@XmlElement
	private String responseMsg = "";//返回消息说明
	@XmlElement
	private String patientId = "";//病历号
	@XmlElement
	private String eHealthCardNo = "";//电子健康卡号
	@XmlElement
	private String balance = "";//余额
	@XmlElement
	private String serialNum = "";//交易流水号
	
	public static void main(String[] args) throws Exception {
		String responseStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response><responseCode>0</responseCode><responseMsg>身份证号为空</responseMsg></response>";
		ResponsePatReg response = JaxbUtil.converyToJavaBean(responseStr, ResponsePatReg.class);
		System.out.println(response.getResponseMsg());
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public String getPatientId() {
		return patientId;
	}

	public String geteHealthCardNo() {
		return eHealthCardNo;
	}

	public String getBalance() {
		return balance;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public void seteHealthCardNo(String eHealthCardNo) {
		this.eHealthCardNo = eHealthCardNo;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
}
