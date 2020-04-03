package com.hjw.webService.client.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ResultHeader")  
@XmlType(propOrder = {})  
public class ResultHeader {
	@XmlElement  
	private String typeCode;//AA:处理成功；AE：处理失败
	
	@XmlElement  
	private String text;//处理结果说明
	
	@XmlElement  
    private String sourceMsgId;//源消息ID

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSourceMsgId() {
		return sourceMsgId;
	}

	public void setSourceMsgId(String sourceMsgId) {
		this.sourceMsgId = sourceMsgId;
	}
	
	
}
