package com.hjw.webService.client.fangzheng.Bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.webService.service.lisbean.ResLisMessage;
import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISMessageAnalyzeFZ{
		public RetLisCustome rc = new RetLisCustome();
		private Document document;
		public LISMessageAnalyzeFZ(String xmlmessage) throws Exception{
			String xmlmess=xmlmessage;
			 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
			 Map<String, String> xmlMap = new HashMap<>();
	    		xmlMap.put("abc", "urn:hl7-org:v3");
	    		SAXReader sax = new SAXReader();
	    		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
	    		document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			 this.getCustom();// 获取检查人员信息
			 this.getdoctor_bg();// 获取报告医生
			 this.getdoctor_sh();// 获取审核医生
			 this.getdoctor_effectiveTime();
			 this.getdoctor_orderid();// 关联医嘱号或者清单号
			 this.getdoctor_deptcode();// 关联科室编码对应类别编码
			 this.getdoctor_Item();// 关联检查项目
		}
		/**
		 * 
		 * @Title: getdoctor_bg @Description:
		 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
		 *         Exception @return: String @throws
		 */
		public void getdoctor_bg() throws Exception {
			
			String time = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
			String name = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name").getText();// 获取根节点
	        this.rc.setDoctor_name_bg(name);
	        this.rc.setDoctor_time_bg(time);
		}

		/**
		 * 
		 * @Title: getCustom @Description:
		 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
		 *         Exception @return: String @throws
		 */
		public void getCustom() throws Exception {
			//System.out.println(document.asXML());
			Node root = document.selectSingleNode("abc:ClinicalDocument/abc:recordTarget/abc:patientRole/abc:id");  
			getNodes_custom(root.getParent());
		}

		@SuppressWarnings("unchecked")
		private void getNodes_custom(Element node) {	
				// 递归遍历当前节点所有的子节点
				List<Element> listElement = node.elements();// 所有一级子节点的list
				for (Element e : listElement) {// 遍历所有一级子节点					
					// 当前节点的名称、文本内容和属性
					//System.out.println("当前节点名称：" + e.getName());// 当前节点名称
					//System.out.println("当前节点的内容：" + e.getTextTrim());// 当前节点名称
					List<Attribute> listAttr = e.attributes();// 当前节点的所有属性的list
					for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
						//String name = attr.getName();// 属性名称
						//String value = attr.getValue();// 属性的值
						//System.out.println("属性名称：" + name + "属性值：" + value);
						if ("1.2.156.112649.1.2.1.3".equals(attr.getValue().trim())){
							for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
								if("extension".equals(attrone.getName().trim().toLowerCase())){
									this.rc.setCustome_id(attrone.getValue());
									break;
								}
							}
						}
						if ("1.2.156.112649.1.2.1.12".equals(attr.getValue().trim())){
							for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
								if("extension".equals(attrone.getName().trim().toLowerCase())){
									this.rc.setCoustom_jzh(attrone.getValue());
									break;
								}
							}
						}
						
						if ("exam_num".equals(attr.getValue().trim())){
							for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
								if("extension".equals(attrone.getName().trim().toLowerCase())){
									this.rc.setExam_num(attrone.getValue());
									break;
								}
							}
						}
					}		
				}		
		}

		/**
		 * 
		 * @Title: getdoctor_sh @Description:
		 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
		 *         Exception @return: String @throws
		 */
		public void getdoctor_sh() throws Exception {
			String time = document.selectSingleNode("abc:ClinicalDocument/abc:authenticator/abc:time/@value").getText();// 获取根节点
			String name = document.selectSingleNode("abc:ClinicalDocument/abc:authenticator/abc:assignedEntity/abc:assignedPerson/abc:name").getText();// 获取根节点
	        this.rc.setDoctor_name_sh(name);
	        this.rc.setDoctor_time_sh(time);
		}

		/**
		 * 
		     * @Title: getdoctor_orderid   
		     * @Description: TODO(这里用一句话描述这个方法的作用)   
		     * @param: @return
		     * @param: @throws Exception      
		     * @return: String      
		     * @throws
		 */
		public void getdoctor_effectiveTime() throws Exception {
			String time = document.selectSingleNode("abc:ClinicalDocument/abc:effectiveTime/@value").getText();// 获取根节点
	        this.rc.setEffectiveTime(time);
		}
		
		public void getdoctor_orderid() throws Exception {
			String time = document.selectSingleNode("abc:ClinicalDocument/abc:order/abc:id/@extension").getText();// 获取根节点
	        this.rc.setSample_barcode(time);
		}

		/**
		 * 
		     * @Title: getdoctor_orderid   
		     * @Description: TODO(这里用一句话描述这个方法的作用)   
		     * @param: @return
		     * @param: @throws Exception      
		     * @return: String      
		     * @throws
		 */
		public void getdoctor_deptcode() throws Exception {
			/*String dept_code = document.selectSingleNode("abc:ClinicalDocument/abc:location/abc:serviceDeliveryLocation/abc:serviceProviderOrganization/abc:id/abc:item/@extension").getText();// 获取根节点
			String dept_name = document.selectSingleNode("abc:ClinicalDocument/abc:location/abc:serviceDeliveryLocation/abc:serviceProviderOrganization/abc:name/abc:part/@value").getText();// 获取根节点
	        this.rc.setDept_code(dept_code);
	        this.rc.setDept_name(dept_name);*/
		}

		/**
		 * 
		 * @Title: getdoctor_Item @Description:
		 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
		 *         Exception @return: String @throws
		 */
		public void getdoctor_Item() throws Exception {
			Node Items = document.selectSingleNode("abc:ClinicalDocument/abc:component");// 获取根节点
			//System.out.println("当前节点的内容：" + Items.asXML());// 当前节点名称
			List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
			getNodes_doctor_coms(listElement);
		}

		@SuppressWarnings("unchecked")
		private void getNodes_doctor_coms(List<Element> listElement)  throws Exception {	
			List<RetLisChargeItem> rtlischarge=new ArrayList<RetLisChargeItem>();// 收费项目 
			for(Element e:listElement){
				String componentxml=e.asXML();
				System.out.println(componentxml);
				if(componentxml.indexOf("<component>")==0){
					try{
				//System.out.println("当前节点名称：" + e.getName());// 当前节点名称
				//System.out.println("当前节点的内容：" + e.asXML());// 当前节点名称
				RetLisChargeItem  retlisch = new RetLisChargeItem();
				InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
				SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
				Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				String  Items_code = documentItem.selectSingleNode("/abc:component/abc:structuredBody/abc:entry/abc:act/abc:code/@code").getText();
				retlisch.setChargingItem_num(Items_code);
				
				Node Items = documentItem.selectSingleNode("/abc:component/abc:structuredBody/abc:entry/abc:act/abc:entryRelationship/abc:organizer/abc:component");// 获取根节点
				List<Element> listItemElement = Items.getParent().elements();// 所有一级子节点的list
				List<RetLisItem> listRetLisItem =new ArrayList<RetLisItem>();
				listRetLisItem = getNodes_doctor_items(listItemElement);
				retlisch.setListRetLisItem(listRetLisItem);
				rtlischarge.add(retlisch);
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
			this.rc.setListRetLisChargeItem(rtlischarge);
		}

		@SuppressWarnings("unchecked")
		private List<RetLisItem> getNodes_doctor_items(List<Element> listElement)  throws Exception {	
			List<RetLisItem> listRetLisItem =new ArrayList<RetLisItem>();
			for(Element e:listElement){
				RetLisItem  retlisch = new RetLisItem();
				//System.out.println(e.asXML());
				InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
				SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
				Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				String  Items_code = documentItem.selectSingleNode("/abc:component/abc:observation/abc:code/@code").getText();
				retlisch.setItem_id(Items_code);
				retlisch.setValues(documentItem.selectSingleNode("/abc:component/abc:observation/abc:result").getText());
				retlisch.setValues_dw(documentItem.selectSingleNode("/abc:component/abc:observation/abc:value/@unit").getText());
				/*
				try{
				String fwvaluel = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/abc:low/@value").getText();
				String fwdwl = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/abc:low/@unit").getText();
				
				String fwvalueh = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:valueabc:/abc:high/@value").getText();
				String fwdwh = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/abc:high/@unit").getText();
				retlisch.setValue_fw(fwvaluel+fwdwl+"-"+fwvalueh+fwdwh);
				}catch(Exception ex){}*/
				String fwdwh = documentItem.selectSingleNode("/abc:component/abc:notetext").getText();
				retlisch.setValue_fw(fwdwh);
				retlisch.setValue_ycbz(documentItem.selectSingleNode("/abc:component/abc:ABNORMALINDICATOR/abc:code/@value").getText());
				listRetLisItem.add(retlisch);	
			}
			return listRetLisItem;
		}

		public static void main(String[] args) throws Exception {
			
			StringBuffer xmlmessage=new StringBuffer("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><id extension=\"TI022\"/><creationTime value=\"2016-12-17 14:22:21\"/><interactionId extension=\"POOR_IN200901UV21\" root=\"\"/><processingCode code=\"P\"/><acceptAckCode code=\"NE\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></sender><title>检验报告</title><effectiveTime value=\"20160701\"/><recordTarget typeCode=\"RCT\"><patientRole classCode=\"PAT\"><id extension=\"01\" root=\"1.2.156.112649.1.2.1.2\"/><id extension=\"\" root=\"1.2.156.112649.1.2.1.3\"/><id extension=\"719\" root=\"1.2.156.112649.1.2.1.12\"/><id extension=\"1\" root=\"1.2.156.112649.1.2.1.7\"/><id extension=\"T16C170003\" root=\"exam_num\"/><patient classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>李德军</name><administrativeGenderCode code=\"1\" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"男\"/><birthTime value=\"19651217\"/></patient></patientRole></recordTarget><location typeCode=\"LOC\"><serviceDeliveryLocation classCode=\"SDLOC\"><serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"><id><item extension=\"1070101\" root=\"1.2.156.112606.1.1.1\"/></id><name xsi:type=\"BAG_EN\"><item><part value=\"检验科\"/></item></name></serviceProviderOrganization></serviceDeliveryLocation></location><author typeCode=\"AUT\"><time value=\"2016-12-17 14:21:57\"/><assignedAuthor classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson><name>回晓娇</name></assignedPerson></assignedAuthor></author><custodian><assignedCustodian><representedCustodianOrganization><id extension=\"44643245-7\" root=\"1.2.156.112649\"/><name>东北国际医院</name></representedCustodianOrganization></assignedCustodian></custodian><authenticator><time value=\"2016-12-17 14:21:57\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"910000004895\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250302001\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血糖测定\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301009\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"间接胆红素\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[8.24]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[2--15.22]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301046\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"葡萄糖\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.81]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[3.89--6.11]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250307005B\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血清尿酸测定\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301030\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿酸\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[373]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[210--430]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ009\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"肝功四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301007\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆红素\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[12.22]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[5.1--28]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301008\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"直接胆红素\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.98]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--10]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301013\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"丙氨酸氨基转移酶\"/><value unit=\"U/L\" xsi:type=\"PQ\"/><result><![CDATA[66]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[9--50]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ020\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"肾功二项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301028\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿素\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[6.1]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[2.86--8.2]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301029\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"肌酐\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[76]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[53--123]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ024\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血脂四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301020\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"甘油三脂\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.26]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--2.3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301021\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.30]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5.17]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301022\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"高密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[0.93]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0.78--1.81]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0301023\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"低密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.41]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3.37]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component></ClinicalDocument>");
	///			StringBuffer xmlmessage=new StringBuffer("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><id extension=\"TI022\"/><creationTime value=\"2016-12-9 09:41:41\"/><interactionId extension=\"POOR_IN200901UV21\" root=\"\"/><processingCode code=\"P\"/><acceptAckCode code=\"NE\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></sender><title>检验报告</title><effectiveTime value=\"20160701\"/><recordTarget typeCode=\"RCT\"><patientRole classCode=\"PAT\"><id extension=\"01\" root=\"1.2.156.112649.1.2.1.2\"/><id extension=\"\" root=\"1.2.156.112649.1.2.1.3\"/><id extension=\"98576\" root=\"1.2.156.112649.1.2.1.12\"/><id extension=\"1\" root=\"1.2.156.112649.1.2.1.7\"/><id extension=\"T16C090025\" root=\"exam_num\"/><patient classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>董秀丹</name><administrativeGenderCode code=\"2\" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"女\"/><birthTime value=\"19811209\"/></patient></patientRole></recordTarget><location typeCode=\"LOC\"><serviceDeliveryLocation classCode=\"SDLOC\"><serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"><id><item extension=\"1070101\" root=\"1.2.156.112606.1.1.1\"/></id><name xsi:type=\"BAG_EN\"><item><part value=\"检验科\"/></item></name></serviceProviderOrganization></serviceDeliveryLocation></location><author typeCode=\"AUT\"><time value=\"2016-12-9 09:41:36\"/><assignedAuthor classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson><name>李杰英</name></assignedPerson></assignedAuthor></author><custodian><assignedCustodian><representedCustodianOrganization><id extension=\"44643245-7\" root=\"1.2.156.112649\"/><name>东北国际医院</name></representedCustodianOrganization></assignedCustodian></custodian><authenticator><time value=\"2016-12-9 09:41:36\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"980000003464\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250102035\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"尿常规(最后验)\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0102003\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿胆原\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[正常]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[正常]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0102004\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿胆红素\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0102005\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"酮体\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0102006\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"隐血\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"5\"/><printid value=\"5\"/><code code=\"0102007\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿蛋白\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"6\"/><printid value=\"6\"/><code code=\"0102008\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"亚硝酸盐\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"7\"/><printid value=\"7\"/><code code=\"0102009\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"白细胞\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"8\"/><printid value=\"8\"/><code code=\"0102011\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿比重\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[1.030]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[1.01--1.025]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"9\"/><printid value=\"9\"/><code code=\"0102012\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"酸碱度\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[5.0]]></result></observation><ABNORMALINDICATOR><code value=\"L\"/></ABNORMALINDICATOR><notetext><![CDATA[5.5--7]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"10\"/><printid value=\"10\"/><code code=\"0102013\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"维生素C\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"11\"/><printid value=\"11\"/><code code=\"0302019\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"糖\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"12\"/><printid value=\"12\"/><code code=\"0102401\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检红细胞\"/><value unit=\"/HP\" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"13\"/><printid value=\"13\"/><code code=\"0102402\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检白细胞\"/><value unit=\"/HP\" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"14\"/><printid value=\"14\"/><code code=\"0102403\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检管型\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"\"/></ABNORMALINDICATOR><notetext><![CDATA[]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"15\"/><printid value=\"15\"/><code code=\"0102038\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"其它\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"\"/></ABNORMALINDICATOR><notetext><![CDATA[]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-9 09:07:33\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ024\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血脂四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301020\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"甘油三脂\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.26]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--2.3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301021\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.30]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5.17]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301022\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"高密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[0.93]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0.78--1.81]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0301023\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"低密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.41]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3.37]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component></ClinicalDocument>");
	        //StringBuffer xmlmessage=new StringBuffer("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><authenticator><time value=\"2016-12-17 14:21:57\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"910000004895\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><abc:ClinicalDocument>");
			ResLisMessage rpm = new ResLisMessage(xmlmessage.toString(),true);
			
		}
 }
