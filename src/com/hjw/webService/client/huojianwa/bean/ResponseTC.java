package com.hjw.webService.client.huojianwa.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class ResponseTC {

	@XmlElement
	private int Code;//0成功，1失败
	
	@XmlElement
	private String Message = "";//成功：申请单信息 XML；失败：失败原因

	public int getCode() {
		return Code;
	}

	public String getMessage() {
		return Message;
	}

	public void setCode(int code) {
		Code = code;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
	public static void main(String[] args) {
		ResponseTC response = new ResponseTC();
		Apply apply = new Apply();
		apply.getItemList().getItem().add(new Item_Apply());
		apply.getItemList().getItem().add(new Item_Apply());
		
		response.setMessage(JaxbUtil.convertToXml(apply, true));
		System.out.println(JaxbUtil.convertToXmlWithCDATA(response, "^Message"));
	}
}
