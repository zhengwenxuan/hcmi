package com.hjw.webService.client.yichang.bean.cdr.client.ret;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class DataSourceBGSC {

	private Document document;
	private String UpReportCollectionDataResult;

	public DataSourceBGSC(String xmlmessage,boolean flags) throws Exception{
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
		Node node = document.selectSingleNode("/DataSource/UpReportCollectionDataResult");
		if(node != null) {
			this.UpReportCollectionDataResult = node.getText();
		}
	}
	
	private String getXml_test() {
		String xml = "<DataSource><return><message>SUCCESS</message><responseId>a3738511-e423-4954-9f40-afaf16c4f9a4</responseId><status>AA</status></return></DataSource>";
		return xml;
	}

	

	public String getUpReportCollectionDataResult() {
		return UpReportCollectionDataResult;
	}

	public void setUpReportCollectionDataResult(String upReportCollectionDataResult) {
		UpReportCollectionDataResult = upReportCollectionDataResult;
	}
	
	


}