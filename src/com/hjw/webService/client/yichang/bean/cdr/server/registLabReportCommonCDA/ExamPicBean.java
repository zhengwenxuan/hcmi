package com.hjw.webService.client.yichang.bean.cdr.server.registLabReportCommonCDA;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "examPicList")  
@XmlType(propOrder = {})
public class ExamPicBean {
	@XmlElement
	private String	picNo	 = "";//	图像编号
	@XmlElement
	private String	picType	 = "";//	图像格式
	@XmlElement
	private String	picContent	 = "";//	图像内容 Base64
	
	public String getPicNo() {
		return picNo;
	}
	public String getPicType() {
		return picType;
	}
	public String getPicContent() {
		return picContent;
	}
	public void setPicNo(String picNo) {
		this.picNo = picNo;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
	public void setPicContent(String picContent) {
		this.picContent = picContent;
	}
}
