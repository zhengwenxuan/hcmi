package com.hjw.webService.client.Carestream.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ReturnXml")  
@XmlType(propOrder = {})  
public class RKReturnXml {
	@XmlElement  
	private String ResultCode="";//0-失败 1-成功

	@XmlElement  
	private String ResultInfo="";//

	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public String getResultInfo() {
		return ResultInfo;
	}

	public void setResultInfo(String resultInfo) {
		ResultInfo = resultInfo;
	}

	
}
