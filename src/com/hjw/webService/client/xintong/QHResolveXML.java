package com.hjw.webService.client.xintong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hjw.webService.client.xintong.lis.RetLisItemQH;


/**
 * 青海解析XML文件 取某一节点内容值/或属性值
 * @author Administrator
 *
 */
public class QHResolveXML {
	
	
	private static Map<String, String> xmlMap = new HashMap<>();
	
	
	/**
	 * 
	 * @param xmlMsg xml信息
	 * @param node  需要某一节点
	 * @param attval 是要属性/value
	 * @return
	 */
	public static String  getNodeAttVal(String xmlMsg,String node,String attval){
		String xmlNode = "";
		try {
			InputStream is = new ByteArrayInputStream(xmlMsg.getBytes("utf-8"));
			Map<String, String> xmlMap = new HashMap<>();
			xmlMap.put("abc", "urn:hl7-org:v3");
			SAXReader sax = new SAXReader();
			sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			if("att".equalsIgnoreCase(attval)) {
				xmlNode = document.selectSingleNode(node).getText();// 获取某一根节点属性值;
			}else if("val".equalsIgnoreCase(attval)){
				xmlNode = document.selectSingleNode(node).getStringValue();// 获取某一根节点内容值;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return xmlNode;
	}
	
	/**
	 * 解析个人注册  LIS 申请  撤销
	 * @param xmlResult
	 * @param type
	 * @param node
	 * @return
	 */
	public static String getXmlResult(String xmlResult , String type, String node) {
		String result = "";
		if("typeCode".equals(node)) {
			result = QHResolveXML.getNodeAttVal(xmlResult, "abc:"+type+"/abc:acknowledgement/@typeCode","att");
		}else if("text".equals(node)) {
			result = QHResolveXML.getNodeAttVal(xmlResult, "abc:"+type+"/abc:acknowledgement/abc:acknowledgementDetail/abc:text","val");
		}else if("patientid".equals(node)){
			result = QHResolveXML.getNodeAttVal(xmlResult, "abc:"+type+"/abc:controlActProcess/abc:subject/abc:registrationEvent/abc:subject1/abc:patient/abc:id/@extension","val");
		}
		return result;
	}
	
	public Map<String, String> resolveXMLQH(String xmlMsg,boolean flags){
		Document document = null;
		InputStream is = null;
		SAXReader sax = new SAXReader();
		try {
			is = new ByteArrayInputStream(xmlMsg.getBytes("utf-8"));
			document = sax.read(is);
            Element xmlRoot = document.getRootElement();
            this.getNodesQH(xmlRoot);//递归
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
	public void getNodesQH(Element node){
		//节点名称   节点内容
		xmlMap.put(node.getName(), node.getTextTrim().trim());
		//当前节点的名称、文本内容和属性
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		List<Element> listElement=node.elements();
		for(Element e:listElement){
			this.getNodesQH(e);//递归
		}
	}
	

	/**
	 * 解析PACS报告
	 * @param xmlMsg
	 */
	public static String pacsRportInfoNode(String xmlMsg, String type) { 
		String reusltXml = "";
		try {
			InputStream is = new ByteArrayInputStream(xmlMsg.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			Element root = document.getRootElement(); 
			Element nameElem = root.element("component").element("structuredBody");
			List<Element> contactList = nameElem.elements();
			for(int i=0;i<contactList.size();i++) { 
				String codeValue = contactList.get(i).element("section").element("code").attributeValue("code");
				//System.out.println(contactList.get(i).element("section").element("code").attributeValue("code"));
				if(!"".equals(codeValue) && codeValue!=null){
					//体检信息
					if("29545-1".equals(codeValue.trim())){
						List<Element> entryList = contactList.get(i).element("section").elements();
						for(int w=0;w<entryList.size();w++) {
							//System.out.println(entryList.get(w).element("observation"));
							if(!"".equals(entryList.get(w).element("observation")) && entryList.get(w).element("observation")!=null){
								String  checkFun = entryList.get(w).element("observation").element("code").attributeValue("code");
								if("DE02.10.027.00".equals(checkFun.trim()) && "JCFF".equals(type)){
									//检查方法 
									reusltXml = entryList.get(w).element("observation").element("value").getText();
								}else if("DE04.30.018.00".equals(checkFun.trim()) && "LBMS".equals(type)){
									//详细描述
									reusltXml = entryList.get(w).element("observation").element("value").getText();
								}
							}else{
								//物品名称
								//System.out.println(entryList.get(w).element("observation"));
								if(!"".equals(entryList.get(w).element("organizer")) && entryList.get(w).element("organizer")!=null){
									//项目代码
									if("BBDM".equals(type)){
										reusltXml = entryList.get(w).element("organizer").element("component").element("observation").element("value").getText();
									}
									List<Element> entryRelationshipList = entryList.get(w).element("organizer").element("component").element("observation").elements();
									for(int n=0;n<entryRelationshipList.size();n++) {
										if(!"".equals(entryRelationshipList.get(n).element("observation")) && entryRelationshipList.get(n).element("observation")!=null){
											String  codeItem = entryRelationshipList.get(n).element("observation").element("code").attributeValue("code");
											if("DE04.50.134.00".equals(codeItem.trim()) && "BBLB".equals(type)){
												//标本类别
												reusltXml = entryRelationshipList.get(n).element("observation").element("value").getText();
											}else if("DE04.50.135.00".equals(codeItem.trim()) && "BBZT".equals(type)){
												//标本状态
												reusltXml = entryRelationshipList.get(n).element("observation").element("value").getText().trim();
											}else if("DE08.50.027.00".equals(codeItem.trim()) && "BBMC".equals(type)){
												//标本名称
												reusltXml = entryRelationshipList.get(n).element("observation").element("value").getText().trim();
											}
										}
									}
								}
							}
						}
					}
				}else{
					//最后两个属性  报告的属性
					String displayName = contactList.get(i).element("section").element("code").attributeValue("displayName");
					//String sectiontext = contactList.get(i).element("section").element("text").getText();
					/*if("".equals(sectiontext.trim()) && (sectiontext.trim()) != null){
						if("PACSIMG".equals(type)){
							reusltXml =  sectiontext;
						}

					}*/
					if("检查报告".equals(displayName.trim())){
						String sectiontext = contactList.get(i).element("section").element("text").getText();
						//System.err.println(sectiontext+"==========sectiontext========");
						if(!"".equals(sectiontext) && sectiontext != null){
							/*System.err.println(type+"==========PACSIMG========");
							System.err.println(sectiontext+"==========sectiontext========");
							System.err.println(reusltXml+"==========reusltXml========");*/
							
							if("PACSIMG".equals(type)){
								reusltXml =  sectiontext;
								//System.err.println(reusltXml+"==========11111111111111111111111111========");
							}

						}else{}
						List<Element> entryList = contactList.get(i).element("section").elements();
						 
						for(int j=0;j<entryList.size();j++) {
							if(!"".equals(entryList.get(j).element("observation")) && entryList.get(j).element("observation")!=null){
								String reportCode = entryList.get(j).element("observation").element("code").attributeValue("code");
								if("DE04.50.131.00".equals(reportCode.trim()) && "BGMS".equals(type)){
									// 描述  
									reusltXml = entryList.get(j).element("observation").element("value").getText();
									//System.err.println(reusltXml+"==========描述=======");
								}else if("DE04.50.132.00".equals(reportCode.trim()) && "BGJG".equals(type)){
									//结论
									reusltXml = entryList.get(j).element("observation").element("value").getText();
									//System.err.println(reusltXml+"==========结论=======");
								}
							}
						}
						
						
					}
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return reusltXml ;
		
	} 
	
	
	
	
	
	/**
	 * 解析LIS 报告
	 * @param xmlMsg
	 * @param type
	 * @return
	 */
	public static String lisRportInfoNode(String xmlMsg, String type) { 
		String reusltXml = "";
		try {
			InputStream is = new ByteArrayInputStream(xmlMsg.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			Element root = document.getRootElement(); 
			
			if(root.element("recordTarget")!=null){
				Element nameNum = root.element("recordTarget").element("patientRole");
				List<Element> idNum = nameNum.elements();
				for(int s=0;s<idNum.size();s++) {		
					String id = idNum.get(s).attributeValue("root");
					if("2.16.156.10011.1.24".equals(id) && "SQDH".equals(type)){
						reusltXml = idNum.get(s).attributeValue("extension");
					}
				}
			}
			
			if(root.element("component")!=null){
				Element nameElem = root.element("component").element("structuredBody");
				List<Element> contactList = nameElem.elements();
				for(int i=0;i<contactList.size();i++) { 
					String codeValue = contactList.get(i).element("section").element("code").attributeValue("code");
					//System.out.println(contactList.get(i).element("section").element("code").attributeValue("code"));
					if(!"".equals(codeValue) && codeValue!=null){
						//体检信息
						if("30954-2".equals(codeValue.trim())){
							List<Element> entryList = contactList.get(i).element("section").elements();
							for(int w=0;w<entryList.size();w++) {
								
								//System.out.println(entryList.get(w).element("observation"));
								if(!"".equals(entryList.get(w).element("observation")) && entryList.get(w).element("observation")!=null){
									String  checkFun = entryList.get(w).element("observation").element("code").attributeValue("code");
									if("DE02.10.027.00".equals(checkFun.trim()) && "JCFF".equals(type)){
										//检查方法 
										reusltXml = entryList.get(w).element("observation").element("value").getText();
									}else if("DE04.30.018.00".equals(checkFun.trim()) && "LBMS".equals(type)){
										//详细描述
										reusltXml = entryList.get(w).element("observation").element("value").getText();
									}
								}else{
									//物品名称
									
									if(!"".equals(entryList.get(w).element("organizer")) && entryList.get(w).element("organizer")!=null){
										//项目代码
										
										List<Element> organizer = entryList.get(w).element("organizer").elements();
										for(int t=0;t<organizer.size();t++) {
											if(!"".equals(organizer.get(t).element("observation")) && organizer.get(t).element("observation")!=null){
												String code = organizer.get(t).element("observation").element("code").attributeValue("code");
												if("DE04.30.019.00".equals(code)){
													//标本编码
													//System.out.println(organizer.get(t).element("observation").element("value").getText());
													
													List<Element> entryRelationshipList = organizer.get(t).element("observation").elements();
													for(int n=0;n<entryRelationshipList.size();n++) {
														if(!"".equals(entryRelationshipList.get(n).element("observation")) && entryRelationshipList.get(n).element("observation")!=null){
															String  codeItem = entryRelationshipList.get(n).element("observation").element("code").attributeValue("code");
															if("DE04.50.134.00".equals(codeItem.trim())){
																//标本类别
																//System.out.println(entryRelationshipList.get(n).element("observation").element("value").getText());
															}else if("DE04.50.135.00".equals(codeItem.trim())){
																//标本状态
																//System.out.println(entryRelationshipList.get(n).element("observation").element("value").getText().trim());
															}
														}
														
													}
												}else if("DE04.30.017.00".equals(code)){
													//标本结果
													//System.out.println(organizer.get(t).element("observation").element("value").attributeValue("displayName"));
												}else if("DE04.30.015.00".equals(code)){
													//标本单位
													//System.out.println(organizer.get(t).element("observation").element("entryRelationship").element("observation").element("value").getText());
												}
											}
										}
										
									}
								}
							}
						}
					}else{
						//最后两个属性  报告的属性
						String displayName = contactList.get(i).element("section").element("code").attributeValue("displayName");
						if("检验报告".equals(displayName.trim())){
							List<Element> entryList = contactList.get(i).element("section").elements();
							for(int j=0;j<entryList.size();j++) {
								if(!"".equals(entryList.get(j).element("observation")) && entryList.get(j).element("observation")!=null){
									String reportCode = entryList.get(j).element("observation").element("code").attributeValue("code");
									if("DE08.10.026.00".equals(reportCode.trim()) && "BGKS".equals(type)){
										// 报告科室 
										reusltXml = entryList.get(j).element("observation").element("value").getText();
									}else if("DE04.50.130.00".equals(reportCode.trim()) && "BGJG".equals(type)){
										//报告结果
										reusltXml = entryList.get(j).element("observation").element("value").getText();
									}else if("DE06.00.179.00".equals(reportCode.trim()) && "BGBZ".equals(type)){
										//报告备注
										reusltXml = entryList.get(j).element("observation").element("value").getText();
									}
								}
							}
						}
					}
				}
			}
			
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return reusltXml ;
		
	} 
	
	
	/**
	 *  LIS 报告细项解析
	 * @param xmlMs
	 * @return
	 */
	public static List<RetLisItemQH> lisRportItemNodes(String xmlMsg) {
		
		List<RetLisItemQH> listRetLisItem =new ArrayList<RetLisItemQH>();
		 
		try {
			InputStream is = new ByteArrayInputStream(xmlMsg.getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			Element root = document.getRootElement(); 
			Element nameElem = root.element("component").element("structuredBody");
			List<Element> contactList = nameElem.elements();
			for(int i=0;i<contactList.size();i++) { 
				String codeValue = contactList.get(i).element("section").element("code").attributeValue("code");
				//System.out.println(contactList.get(i).element("section").element("code").attributeValue("code"));
				if(!"".equals(codeValue) && codeValue!=null){
					//体检信息
					if("30954-2".equals(codeValue.trim())){
						List<Element> entryList = contactList.get(i).element("section").elements();
						for(int w=0;w<entryList.size();w++) {
							//System.out.println(entryList.get(w).element("observation"));
							if(!"".equals(entryList.get(w).element("observation")) && entryList.get(w).element("observation")!=null){
								/*String  checkFun = entryList.get(w).element("observation").element("code").attributeValue("code");
								if("DE02.10.027.00".equals(checkFun.trim()) && "JCFF".equals(type)){
									//检查方法 
									reusltXml = entryList.get(w).element("observation").element("value").getText();
								}else if("DE04.30.018.00".equals(checkFun.trim()) && "LBMS".equals(type)){
									//详细描述
									reusltXml = entryList.get(w).element("observation").element("value").getText();
								}*/
							}else{
								//物品名称
								if(!"".equals(entryList.get(w).element("organizer")) && entryList.get(w).element("organizer")!=null){
									
									RetLisItemQH  retlisch = new RetLisItemQH();
									
									//项目代码
									List<Element> organizer = entryList.get(w).element("organizer").elements();
									for(int t=0;t<organizer.size();t++) {
										
										if(!"".equals(organizer.get(t).element("observation")) && organizer.get(t).element("observation")!=null){
											String code = organizer.get(t).element("observation").element("code").attributeValue("code");
											if("DE04.30.019.00".equals(code)){
												//标本编码
												String item_id = organizer.get(t).element("observation").element("value").getText();
												String[] str = item_id.split("\\|");
												// "1054| LDL-C| 低密度载脂蛋白| 1| 10| 1-10| H"
												// 小项目编码| 小项目英文名称| 中文名称| 低值| 高值| 参考范围| 高低值判断
												retlisch.setItem_id(str[0].trim());
												retlisch.setItem_name(str[2].trim());
												retlisch.setValue_ycbz(str[6].trim()); //异常标志
												retlisch.setValue_fw(str[5].trim());
												retlisch.setValues(str[str.length-1].trim());
												List<Element> entryRelationshipList = organizer.get(t).element("observation").elements();
												for(int n=0;n<entryRelationshipList.size();n++) {
													if(!"".equals(entryRelationshipList.get(n).element("observation")) && entryRelationshipList.get(n).element("observation")!=null){
														String  codeItem = entryRelationshipList.get(n).element("observation").element("code").attributeValue("code");
														if("DE04.50.134.00".equals(codeItem.trim())){
															//标本类别
															//System.out.println(entryRelationshipList.get(n).element("observation").element("value").getText());
														}else if("DE04.50.135.00".equals(codeItem.trim())){
															//标本状态
															//System.out.println(entryRelationshipList.get(n).element("observation").element("value").getText().trim());
														}
													}
													
												}
											}else if("DE04.30.017.00".equals(code)){
												//标本结果
												/*String values = organizer.get(t).element("observation").element("value").attributeValue("displayName");
												retlisch.setValues(values);*/
											}else if("DE04.30.015.00".equals(code)){
												
												//标本结果
												//String values = organizer.get(t).element("observation").element("value").attributeValue("value");
												//retlisch.setValues(values);
												
												//标本单位
												String values_dw = organizer.get(t).element("observation").element("entryRelationship").element("observation").element("value").getText();
												retlisch.setValues_dw(values_dw);
											}/*else if ("DE04.30.019.00".equals(code)){
												//标本结果
												String values = organizer.get(t).element("observation").element("value").getText();
												System.err.println("lis结果========"+values);
												String[] split = values.split("\\|");
												retlisch.setValues(split[split.length-1].trim());
											}*/
										}
									}
									
									listRetLisItem.add(retlisch);	
									
								}
							}
						}
					}
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return listRetLisItem; 
		
		
	}
	
	
	
	/**
	 * 获取节点类型
	 * @param xmlMsg
	 * @return
	 */
	public static String getReportType(String xmlMsg,String logname){
		
		String result = QHResolveXML.getNodeAttVal(xmlMsg, "abc:CUST_OUT00004/abc:controlActProcess/abc:subject/abc:message_contents","val");
		
		String result2 = result.replaceAll("&lt;", "<");
		String resultXML = result2.replaceAll("&gt;", ">");
		
		String resultBase64 = QHResolveXML.getNodeAttVal(resultXML, "abc:ProvideAndRegisterDocumentSetRequest/abc:Document/abc:Content","val");
		
		String xml  = LisResMessageQH.getFromBase64(resultBase64.trim());
		
		//String xml = pacsReportMsgBase64(); //本地测试
		
		//TranLogTxt.liswriteEror_to_txt(logname,"===解析xml结果===："+xml);
		
		String type = QHResolveXML.getNodeAttVal(xml,"abc:ClinicalDocument/abc:code/@code","att");
		
		//获取节点类型
		return type;
	}
}
