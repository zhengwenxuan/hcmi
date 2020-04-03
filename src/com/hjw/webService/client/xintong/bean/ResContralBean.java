package com.hjw.webService.client.xintong.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.hjw.webService.client.body.ResultHeader;

public class ResContralBean {
   
	/**
	 * 响应返回结果处理
	 * @param xmlmessage
	 * @return
	 */
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
		// 获取某一根节点属性值;
		rh.setTypeCode(document.selectSingleNode("abc:PRPA_IN201306UV02/abc:acknowledgement/@typeCode").getText());
		// 获取某一根节点内容值;
		rh.setText(document.selectSingleNode("abc:PRPA_IN201306UV02/abc:acknowledgement/abc:acknowledgementDetail/abc:text").getStringValue());
		}catch(Exception ex){
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
        return rh;
	}
	
	//模拟测试方法
	public static void main(String[] args) throws Exception {  
		ResContralBean r = new ResContralBean();
		String  dm =r.dd();
		ResultHeader rh =ResContralBean.getRes(dm);
		System.out.println(rh.getTypeCode()+"===="+rh.getText());
    } 
	
	public String dd(){
		StringBuffer sb= new StringBuffer();
		
		sb.append("<PRPA_IN201306UV02 ITSVersion=\"XML_1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201306UV02.xsd\">  ");
		sb.append("	<id extension=\"040CD76A-ED0E-400B-9FD3-60387BCDE0EB\" /> ");
		sb.append("	<creationTime value=\"2012110911900\"/>   ");
		sb.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201306UV02\"/> ");
		sb.append("	<processingCode code=\"T\"/>  ");
		sb.append("	<processingModeCode code=\"I\"/>  ");
		sb.append("	<acceptAckCode code=\"AA\"/>  ");
		sb.append("	<receiver typeCode=\"RCV\">   ");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		sb.append("			<id root=\"1.2.840.114350.1.13.999.567\"/>  ");
		sb.append("		</device> ");
		sb.append("	</receiver> ");
		sb.append("	<sender typeCode=\"SND\"> ");
		sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		sb.append("			<id root=\"1.2.840.114350.1.13.999.234\"/>  ");
		sb.append("		</device> ");
		sb.append("	</sender> ");
		sb.append("	<!--typeCode为处理结果，AA表示成功 AE表示失败-->  ");
		sb.append("	<acknowledgement typeCode=\"AE\"> ");
		sb.append("		<targetMessage>   ");
		sb.append("			<!--请求消息ID-->   ");
		sb.append("			<id extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>");
		sb.append("		</targetMessage>  ");
		sb.append("		<acknowledgementDetail> ");
		sb.append("			<!--处理结果说明--> ");
		sb.append("			<text>由于XXX原因，查询失败</text>  ");
		sb.append("		</acknowledgementDetail>");
		sb.append("	</acknowledgement>");
		sb.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">   ");
		sb.append("		<code code=\"PRPA_TE201306UV02\" codeSystem=\"2.16.840.1.113883.1.6\"/> ");
		sb.append("		<queryAck>");
		sb.append("			<queryId extension=\"040CD76A-ED0E-400B-9FD3-60387BCD\" />  ");
		sb.append("			<queryResponseCode/>");
		sb.append("		</queryAck>   ");
		sb.append("	</controlActProcess>  ");
		sb.append("</PRPA_IN201306UV02>   ");
		
		
	/*	sb.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb.append("<id extension=\"@12122\"/>");
		sb.append("<creationTime value=\"20170106151903\"/>  ");
		sb.append("<interactionId extension=\"MCCI_IN000002UV01\" root=\"2.16.840.1.113883.1.6\"/> ");
		sb.append("<processingCode code=\"P\"/>  ");
		sb.append("<processingModeCode/> ");
		sb.append("<acceptAckCode code=\"AL\"/>  ");
		sb.append("<receiver typeCode=\"RCV\">   ");
		sb.append("	<device classCode=\"DEV\" determinerCode=\"INSTANCE\">   ");
		sb.append("		<id> ");
		sb.append("			<item extension=\"@111\"/> ");
		sb.append("		</id>");
		sb.append("	</device>");
		sb.append("</receiver> ");
		sb.append("<sender typeCode=\"SND\"> ");
		sb.append("	<device classCode=\"DEV\" determinerCode=\"INSTANCE\">   ");
		sb.append("		<id> ");
		sb.append("			<item extension=\"@222\"/> ");
		sb.append("		</id>");
		sb.append("	</device>");
		sb.append("</sender> ");
		sb.append("<!--AA成功，AE失败--> ");
		sb.append("<acknowledgement typeCode=\"AA\"> ");
		sb.append("	<!--请求消息ID-->");
		sb.append("	<targetMessage>  ");
		sb.append("		<id extension=\"请求的消息ID\"/> ");
		sb.append("	</targetMessage> ");
		sb.append("	<acknowledgementDetail>");
		sb.append("		<text value=\"成功或失败处理结果说明\"/> ");
		sb.append("	</acknowledgementDetail>   ");
		sb.append("</acknowledgement>");
	    sb.append("</MCCI_IN000002UV01>  ");
     */
	    return sb.toString();
	}
}
