package com.hjw.webService.client.tiantan.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {}) 
public class ResLisBodyTiantan {
	private String success_flag="1";  //0 表示成功
	private String returnMessage="";
	
	public String getSuccess_flag() {
		return success_flag;
	}
	public void setSuccess_flag(String success_flag) {
		this.success_flag = success_flag;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	
	/** 
     * 测试方法 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
       String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><success_flag>0</success_flag><returnMessage>成功</returnMessage></root>";
       ResLisBodyTiantan rbcomm = JaxbUtil.converyToJavaBean(str, ResLisBodyTiantan.class);
       System.out.println(rbcomm.returnMessage);
    }  
}
