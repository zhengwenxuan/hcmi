package com.hjw.webService.client.nanhua.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class NHRequest{
	
	@XmlElement(name = "Head")
	private Head Head = new Head();
	
	@XmlElement(name = "Data") 
	private Data Data = new Data();

	public Head getHead() {
		return Head;
	}

	public void setHead(Head head) {
		Head = head;
	}

	public Data getData() {
		return Data;
	}

	public void setData(Data data) {
		Data = data;
	}

//	public static void main(String[] args) {
//		Data data = new Data();
//		data.setAge("111");
//		data.setName("郑文轩");
//		Request request = new Request();
//		request.setData(data);
//		System.out.println(JaxbUtil.convertToXml(request, true));
//	}
}
