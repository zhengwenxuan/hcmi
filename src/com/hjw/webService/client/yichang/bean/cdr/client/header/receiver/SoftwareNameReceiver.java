package com.hjw.webService.client.yichang.bean.cdr.client.header.receiver;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "softwareName")  
@XmlType(propOrder = {})
public class SoftwareNameReceiver {
	
	@XmlAttribute
	private String code = "HIP-HSB";//消息接受方代码
	@XmlAttribute
	private String displayName = "医疗服务总线";//消息接受方名称
	@XmlAttribute
	private String codeSystem = "2.999.2.3.2.84";//
	@XmlAttribute
	private String codeSystemName = "医院信息平台系统域代码表";//
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}
	public String getCodeSystemName() {
		return codeSystemName;
	}
	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}
	
}
