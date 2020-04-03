package com.hjw.webService.client.jsjg.bean.out;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.google.gson.Gson;
import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ReturnValue")  
@XmlType(propOrder = {})
public class ReturnValue {

//	@XmlElement
//	private String IsSucceed = "True";//表示提供方是否正确的返回	True表示正确的返回，False表示非正常返回
//	@XmlElement
//	private String RetMsg = "";//返回的消息	Xml或josn格式业务字符串，请使用<![CDATA[……]]>将业务字符串包裹。（如果业务数据本身使用了CDATA请注意CDATA嵌套）
//	
//	public String getIsSucceed() {
//		return IsSucceed;
//	}
//	public String getRetMsg() {
//		return RetMsg;
//	}
//	public void setIsSucceed(String isSucceed) {
//		IsSucceed = isSucceed;
//	}
//	public void setRetMsg(String retMsg) {
//		RetMsg = retMsg;
//	}
//	
//	public static void main(String[] args) {
//		PatientInfo patientInfo = new PatientInfo();
//		patientInfo.getSpecimens().getSpecimen().add(new Specimen());
//		patientInfo.getSpecimens().getSpecimen().add(new Specimen());
//		
//		RetMsg RetMsg = new RetMsg();
//		RetMsg.getOutParam().setPatientInfo(patientInfo);
//		
//		ReturnValue returnValue = new ReturnValue();
//		returnValue.setRetMsg(new Gson().toJson(RetMsg, RetMsg.class));
//		String returnValueStr = JaxbUtil.convertToXmlWithCDATA(returnValue, "^RetMsg");
//		
//		System.out.println(returnValueStr);
//	}
	
}
