package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hjw.util.TranLogTxt;

public class test {

	
	
	public static String searchPatid(String result,String logname){	
		 //ResCustomBeanHK rb= new ResCustomBeanHK();
		try{
		
		 if(result.trim().length()>0) {
			 try{
					InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
					Map<String, String> xmlMap = new HashMap<>();
					xmlMap.put("abc", "urn:hl7-org:v3");
					SAXReader sax = new SAXReader();
					
					sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
					Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
					String setCode = document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText();// 获取根节点;
					boolean setFaly = document.selectNodes("abc:MCCI_IN000002UV01/abc:controlActProcess").size()==0;
					
					String patid = document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText();
					
					//如果"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension" 为空  则去人员信息中拿患者id
					
					if(patid.equals("")){
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入1111");
						
						Document document2 = document.selectSingleNode("abc:MCCI_IN000002UV01/abc:controlActProcess").getDocument();
						
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入2222");
						Element rootElement = document2.getRootElement();
						
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入3333:"+rootElement.getName());
						
						Element element = rootElement.element("controlActProcess");
						List<Element> subject1List = element.elements("subject");
						
						
						TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入4444");
						
						for (int i = 0; i < subject1List.size(); i++) {
							TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入5555");
							String name = subject1List.get(0).getName();
							TranLogTxt.liswriteEror_to_txt(logname,  subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText());
							//TranLogTxt.liswriteEror_to_txt(logname,  subject1List.get(0).selectSingleNode("abc:subject/abc:registrationRequest/abc:subject1/abc:patient/abc:id/item[@root='2.16.156.10011.0.2.2']/@extension").getText());
							TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入6666");
							String setPersionid = subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText();
							//String setPersionid = subject1List.get(0).selectSingleNode("abc:subject/abc:registrationRequest/abc:subject1/abc:patient/abc:id/item[@root='2.16.156.10011.0.2.2']/@extension").getText();
							
							TranLogTxt.liswriteEror_to_txt(logname,  "患者id为空进入6666");
							TranLogTxt.liswriteEror_to_txt(logname, "获取人员信息节点的患者id:" + subject1List.get(0).selectSingleNode("abc:registrationRequest/abc:subject1/abc:patient/abc:id/item[@root='2.16.156.10011.0.2.2']/@extension").getText() + "\r\n");
						}
						
					}else{
						//如果"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension" 不为空  则直接拿此节点的 患者id
						String setPersionid = (document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText());
					}
					
					}catch(Exception ex){
						try{
							InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
							Map<String, String> xmlMap = new HashMap<>();
							xmlMap.put("abc", "urn:hl7-org:v3");
							SAXReader sax = new SAXReader();
							sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
							Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
							String setCode = (document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
							String setCodetext =(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:acknowledgementDetail/abc:text/@value").getText());
							}catch(Exception ext){
							}
					}
		  }else{
			  
		  }
		}catch(Exception ex){
			
		}
		return "";
	}
}
