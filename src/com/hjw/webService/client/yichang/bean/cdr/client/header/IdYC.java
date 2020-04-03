package com.hjw.webService.client.yichang.bean.cdr.client.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "telecom")  
@XmlType(propOrder = {})
public class IdYC {
	
	@XmlAttribute
	private String root = "2.999.1.96";//
	@XmlAttribute
	private String extension = "";
//	private String extension = UUID.randomUUID().toString();//唯一码：每次调用时生成唯一码，如e92acb6f-87cf-4084-8b14-3f04d2bef3d2调用系统自动生成的字符串，方便核对日志
	
	public IdYC() {
	}
	
	public IdYC(String arch_num) {
		this.extension = "tj"+arch_num;
	}
	
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
