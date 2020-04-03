package com.hjw.webService.client.erfuyuan.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "DataContent")  
@XmlType(propOrder = {})
public class DataContent {

	@XmlAttribute(name = "content")
	private String content = "";
	@XmlElement(name = "client")
	private client client;
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+ "<DataContent content='client_update'>"
+ "<client categoryid='309超声16' name='张文' card='1736211' > "
+ "</client>"
+ "</DataContent>";
		DataContent dataContent = JaxbUtil.converyToJavaBean(xml, DataContent.class);
		System.out.println(dataContent.getClient().getCategoryid());
	}
	
	public String getContent() {
		return content;
	}
	public client getClient() {
		return client;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setClient(client client) {
		this.client = client;
	}
}
