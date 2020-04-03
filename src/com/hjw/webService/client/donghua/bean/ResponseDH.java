package com.hjw.webService.client.donghua.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class ResponseDH {

	@XmlElement
	private String ResultCode = "";//0：成功 非0：失败
	@XmlElement
	private String ResultContent = "";//失败原因
	
	public static void main(String[] args) {
		ResponseDH response = new ResponseDH();
		String xml = JaxbUtil.convertToXmlWithOutHead(response, true);
		System.out.println(xml);
	}
	
	public String getResultCode() {
		return ResultCode;
	}
	public String getResultContent() {
		return ResultContent;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public void setResultContent(String resultContent) {
		ResultContent = resultContent;
	}
}
