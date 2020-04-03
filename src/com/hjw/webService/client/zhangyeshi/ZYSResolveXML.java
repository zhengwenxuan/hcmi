package com.hjw.webService.client.zhangyeshi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.dom4j.Element;

public class ZYSResolveXML {
	
	private static Map<String, String> xmlMap = new HashMap<>();
	
	/**
	 * 张掖市 解析XML文件
	 * @param xmlMsg
	 * @param flags
	 * @return
	 */
	public Map<String, String> resolveXML(String xmlMsg,boolean flags){
		Document document = null;
		InputStream is = null;
		SAXReader sax = new SAXReader();
		try {
			is = new ByteArrayInputStream(xmlMsg.getBytes("utf-8"));
			document = sax.read(is);
            Element xmlRoot = document.getRootElement();
            this.getNodes(xmlRoot);//递归
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return xmlMap;
	
	}
	
	
	/**
	 * 遍历所有属性名和值
	 * @param node
	 */
	public void getNodes(Element node){
		//节点名称   节点内容
		xmlMap.put(node.getName(), node.getTextTrim().trim());
		//当前节点的名称、文本内容和属性
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		/*for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			System.out.println("属性名称："+name+"属性值："+value);
		}*/
		List<Element> listElement=node.elements();
		for(Element e:listElement){
			this.getNodes(e);//递归
		}
	}
	
	public static void main(String[] args) {
		//String xmlMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Result> <resultCode> 1 </resultCode> <resultMsg> <VAJ1> <Ie> <收费项目ID> 5191 </收费项目ID> <子单据ID> 105728 </子单据ID> </Ie> </VAJ1> <Ie> <单据ID> 19598 </单据ID> <单据号> I0000019598 </单据号> </Ie> </resultMsg> <interfaceID> 1 </interfaceID> </Result>";
		String xmlMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <Result> <resultCode> 1 </resultCode> <resultMsg> 该病人费用已经提交 </resultMsg> <interfaceID> 1 </interfaceID> </Result>";
		ZYSResolveXML ssl = new ZYSResolveXML();
		ssl.resolveXML(xmlMsg, true);
		System.out.println(xmlMap.get("resultMsg"));
	}
	

}
