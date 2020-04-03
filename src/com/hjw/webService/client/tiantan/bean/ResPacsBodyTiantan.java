package com.hjw.webService.client.tiantan.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "RESPONSE")  
@XmlType(propOrder = {}) 
public class ResPacsBodyTiantan {
	private String ReturnCode="1";  //0 表示成功
	private String StudyID="";//字符串	检查号
	private String ImageNO="";//字符串	影像号
	private String CheckSerialNum="";//字符串	消息编号
	private String ReturnErrorText="";//字符串	错误描述
	
	public String getReturnCode() {
		return ReturnCode;
	}
	
	public void setReturnCode(String returnCode) {
		ReturnCode = returnCode;
	}

	public String getStudyID() {
		return StudyID;
	}

	public void setStudyID(String studyID) {
		StudyID = studyID;
	}

	public String getImageNO() {
		return ImageNO;
	}

	public void setImageNO(String imageNO) {
		ImageNO = imageNO;
	}

	public String getCheckSerialNum() {
		return CheckSerialNum;
	}

	public void setCheckSerialNum(String checkSerialNum) {
		CheckSerialNum = checkSerialNum;
	}

	public String getReturnErrorText() {
		return ReturnErrorText;
	}

	public void setReturnErrorText(String returnErrorText) {
		ReturnErrorText = returnErrorText;
	}

	/** 
     * 测试方法 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
       String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><RESPONSE><ReturnCode>0</ReturnCode><ReturnErrorText></ReturnErrorText></RESPONSE>";
       ResPacsBodyTiantan rbcomm = JaxbUtil.converyToJavaBean(str, ResPacsBodyTiantan.class);
       System.out.println(rbcomm.ReturnCode);
    }  
}
