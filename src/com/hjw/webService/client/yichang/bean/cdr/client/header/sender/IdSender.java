package com.hjw.webService.client.yichang.bean.cdr.client.header.sender;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "telecom")  
@XmlType(propOrder = {})
public class IdSender {
	
	@XmlAttribute
	private String root = "2.999.1.98";//
	@XmlAttribute
	private String extension = "LCFW-TJGL";//消息发送方系统域代码
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}

}
