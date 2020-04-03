package com.hjw.webService.client.yichang.bean.cdr.client.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.util.DateTimeUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "creationTime")  
@XmlType(propOrder = {})
public class CreationTime {
	
	@XmlAttribute
	private String value = DateTimeUtil.getDateTimes();//消息发送时间： 是调用接口的操作时间，如20130501130624

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
