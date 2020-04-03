package com.hjw.webService.client.donghua.bean.LinkLabNoWithOrdRowId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "LinkLabNoWithOrdRowIdReturn")  
@XmlType(propOrder = {})
public class Response_LinkLabNoWithOrdRowId {

	@XmlElement
	private String ResultCode = "";//0：成功 非0：失败
	@XmlElement
	private String ResultContent = "";//失败原因
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+"<LinkLabNoWithOrdRowIdReturn>"
+"<ResultContent>成功</ResultContent>"
+"<ResultCode>0</ResultCode>"
+"</LinkLabNoWithOrdRowIdReturn>";
		Response_LinkLabNoWithOrdRowId response = JaxbUtil.converyToJavaBean(xml, Response_LinkLabNoWithOrdRowId.class);
		System.out.println(response.getResultContent());
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
