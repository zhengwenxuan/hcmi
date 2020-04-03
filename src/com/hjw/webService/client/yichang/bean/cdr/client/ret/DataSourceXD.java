package com.hjw.webService.client.yichang.bean.cdr.client.ret;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class DataSourceXD {

	private Document document;
	private String message;
	private String status;

	public DataSourceXD(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=xmlmessage;
		}else{
			xmlmess=getXml_test();
		}
		InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
        SAXReader sax = new SAXReader();
		
		is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		Node node = document.selectSingleNode("/DataSource/HIPMessageServerResult/Response/Body/ResultContent");
		if(node != null) {
			this.message = node.getText();
		}
		node = document.selectSingleNode("/DataSource/HIPMessageServerResult/Response/Body/ResultCode");
		if(node != null) {
			this.status = node.getText();
        }
	}
	
	private String getXml_test() {
		String xml = "<DataSource><return><message>SUCCESS</message><responseId>a3738511-e423-4954-9f40-afaf16c4f9a4</responseId><status>AA</status></return></DataSource>";
		return xml;
	}
	
	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(new DataSourceXD("", false).getStatus());
	}
}
