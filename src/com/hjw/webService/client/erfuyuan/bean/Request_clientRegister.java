package com.hjw.webService.client.erfuyuan.bean;

import com.hjw.interfaces.util.JaxbUtil;

public class Request_clientRegister {
	
	private String xmlString = "";//
	

	public String getXmlString() {
		return xmlString;
	}

	public void setXmlString(String xmlString) {
		this.xmlString = xmlString;
	}
	
	public static void main(String[] args) {
		ClientInfo_clientRegister message = new ClientInfo_clientRegister();
		for(int i=0;i<10;i++) {
			message.setName("tcz");
			Item_clientRegister item = new Item_clientRegister();
			item.setValue("==="+i);
			message.getItems().getItem().add(item);
		}
		System.out.println(JaxbUtil.convertToXml(message, true));
	}

}
