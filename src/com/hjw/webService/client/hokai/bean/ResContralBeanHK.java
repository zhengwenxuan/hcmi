package com.hjw.webService.client.hokai.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.body.ResultHeader;

public class ResContralBeanHK {
   
	public static ResultHeader  getRes(String xmlmessage){
		ResultHeader rh= new ResultHeader();
		rh.setTypeCode("AE");
		try{
		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		rh.setTypeCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
		rh.setText(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:acknowledgementDetail/abc:text/@value").getText());
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
        return rh;
	}
	
	
	public static ResultHeader  getResGET_REQNO(String xmlmessage){//305获取申请单响应结果
		ResultHeader rh= new ResultHeader();
		rh.setTypeCode("AE");
		try{
		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		rh.setTypeCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
		rh.setSourceMsgId(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText());
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
        return rh;
	}
	
	
	public static ResultHeader  getResGET_pacsshenqing(String xmlmessage){//305获取pacs申请响应结果
		ResultHeader rh= new ResultHeader();
		rh.setTypeCode("AE");
		try{
		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		rh.setTypeCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
		rh.setSourceMsgId(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:targetMessage/abc:id/@extension").getText());
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
        return rh;
	}
	
	public static QueueResBody  Queue(String xmlmessage){//305叫号系统响应
		QueueResBody rh= new QueueResBody();
		rh.setRescode("AE");
		try{
		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		rh.setRescode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
		rh.setRestext(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText());
		}catch(Exception ex){
			rh.setRescode("AE");
			rh.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
        return rh;
	}
	
	public static void main(String[] args) throws Exception {
		ResContralBeanHK r = new ResContralBeanHK();
		String  dm =r.dd();
		ResultHeader rh =ResContralBeanHK.getRes(dm);
		System.out.println(rh.getText());
    } 
	
	public String dd(){
		StringBuffer sb= new StringBuffer();
		sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb.append("	<!-- 消息ID(系统生成唯一UUID)(1..1) -->   ");
		sb.append("	<id extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>");
		sb.append("	<!-- 消息创建时间(1..1) -->   ");
		sb.append("	<creationTime value=\"20111129220000\"/>  ");
		sb.append("	<!-- 服务编码，S0001代表患者注册请求(1..1)--> ");
		sb.append("	<interactionId extension=\"S0001\"/>");
		sb.append("	<!-- 接受者(1..1) -->   ");
		sb.append("	<receiver code=\"接收系统编码\"/> ");
		sb.append("	<!-- 发送者(1..1) -->   ");
		sb.append("	<sender code=\"发送系统编码\"/>   ");
		sb.append("	<!--AA成功，AE失败(1..1)-->   ");
		sb.append("	<acknowledgement typeCode=\"AA\"> ");
		sb.append("		<!-- 注册唯一标识，如患者注册则为患者唯一标识、报告新增则为报告唯一标识(1..1) --> ");
		sb.append("		<id extension=\"D3827423\"/>  ");
		sb.append("		<!--请求消息ID(1..1)-->   ");
		sb.append("		<targetMessage> ");
		sb.append("			<id extension=\"请求的消息ID\"/>  ");
		sb.append("		</targetMessage>");
		sb.append("		<acknowledgementDetail>   ");
		sb.append("			<text value=\"成功或失败处理结果说明(1..1)\"/>  ");
		sb.append("		</acknowledgementDetail>  ");
		sb.append("	</acknowledgement>  ");
		sb.append("</MCCI_IN000002UV01> ");
      return sb.toString();
	}


	public static ResultHeader getLisReqStatus305(String xmlmessage) {
		ResultHeader rh= new ResultHeader();
		rh.setTypeCode("AE");
		try{

		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		rh.setTypeCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
		rh.setText(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:controlActProcess/abc:subject/abc:observationReport/abc:statusCode/@code").getText());
		}catch(Exception ex){
			ex.printStackTrace();
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
        return rh;
	}
}
