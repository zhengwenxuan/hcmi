package com.hjw.webService.client.donghua.bean.PEStop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class Request_PEStop {
	
	@XmlElement
	private String OEORowid = "";//检验号 体检系统产生 多个医嘱用,分割
	
	public static void main(String[] args) throws Exception {
		Request_PEStop request = new Request_PEStop();
		String xml = JaxbUtil.convertToXmlWithOutHead(request, true);
		System.out.println(xml);
	}

	public String getOEORowid() {
		return OEORowid;
	}
	public void setOEORowid(String oEORowid) {
		OEORowid = oEORowid;
	}
}
