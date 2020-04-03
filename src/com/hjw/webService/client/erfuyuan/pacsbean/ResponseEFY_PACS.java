package com.hjw.webService.client.erfuyuan.pacsbean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Response")  
@XmlType(propOrder = {})
public class ResponseEFY_PACS {

	@XmlElement
    private String Result = "";//成功返回：0
	@XmlElement
    private String Content = "";//失败返回：详细错误信息。
	
	public String getResult() {
		return Result;
	}
	public String getContent() {
		return Content;
	}
	public void setResult(String result) {
		Result = result;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	public static void main(String[] args) throws Exception {
		//String message = "<Response><Result>0</Result><Content>accno</Content></Response>";
		String message = "<Response><Result>0</Result><Content>取消申请检查号为 TJ1809250001 的申请记录成功</Content></Response>";
		ResponseEFY_PACS response = JaxbUtil.converyToJavaBean(message, ResponseEFY_PACS.class);
		System.out.println(response.getResult());
		System.out.println(response.getContent());
	}
}
