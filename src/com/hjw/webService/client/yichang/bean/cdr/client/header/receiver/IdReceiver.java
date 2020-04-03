package com.hjw.webService.client.yichang.bean.cdr.client.header.receiver;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "telecom")  
@XmlType(propOrder = {})
public class IdReceiver {
	
	@XmlAttribute
	private String root = "2.999.1.97";//
	@XmlAttribute
	private String extension = "HIP-HSB";//消息接受方代码
	
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
