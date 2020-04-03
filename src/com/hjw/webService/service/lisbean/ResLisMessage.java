package com.hjw.webService.service.lisbean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class ResLisMessage {
	public RetLisCustome rc = new RetLisCustome();
	private Document document;
	public ResLisMessage(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=checkxml(xmlmessage);
		}else{
			xmlmess= getXml_test();
		}
		 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
         SAXReader sax = new SAXReader();
		 this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
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
	     * @Title: checkxml   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @param xmlmess
	     * @param: @return      
	     * @return: String      
	     * @throws
	 */
	private String checkxml(String xmlmess){
		if(xmlmess.indexOf("urn:hl7-org:v3")>0){
			xmlmess=xmlmess.substring(0,17)+ xmlmess.substring(153,xmlmess.length());
		}
		xmlmess=xmlmess.replaceAll("xsi:", "");
		String[] flagstr=new String[10];
		flagstr[0]="value";
		//xmlmess = toxml(xmlmess,flagstr);
		return xmlmess;
	}

	private String getXml_test() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");
		sb.append("<ClinicalDocument>");
		sb.append("<!-- 消息ID -->");
		sb.append("<id extension=\"TJ022\"/>");
		sb.append("<!-- 消息创建时间 -->");
		sb.append("<creationTime value=\"2016-10-26 04:13:47\"/>");
		sb.append("<!-- 交互ID -->");
		sb.append("<interactionId extension=\"POOR_IN200901UV21\" root=\"\"/>");
		sb.append("<!-- 消息用途: P(Production); D(Debugging); T(Training) -->");
		sb.append("<processingCode code=\"P\"/>");
		sb.append("<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current 	processing) -->");
		sb.append("<processingModeCode code=\"R\"/>");
		sb.append("<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->");
		sb.append("<acceptAckCode code=\"NE\"/>");
		sb.append("<!-- 接受者 -->");
		sb.append("<receiver typeCode=\"RCV\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		sb.append("<!-- 接受者ID -->");
		sb.append("<id>");
		sb.append("<item extension=\"\" root=\"\"/>");
		sb.append("</id>");
		sb.append("</device>");
		sb.append("</receiver>");
		sb.append("<!-- 发送者 -->");
		sb.append("<sender typeCode=\"SND\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		sb.append("<!-- 发送者ID -->");
		sb.append("<id>");
		sb.append("<item extension=\"\" root=\"\"/>");
		sb.append("</id>");
		sb.append("</device>");
		sb.append("</sender>");
		sb.append("<title>检验报告</title>");
		sb.append("<effectiveTime value=\"20160701\"/>");
		sb.append("<!-- 记录目标 -->");
		sb.append("<recordTarget typeCode=\"RCT\">");
		sb.append("<!-- 病人角色  -->");
		sb.append("<patientRole classCode=\"PAT\">");
		sb.append("<id extension=\"01\" root=\"1.2.156.112649.1.2.1.2\"/>");
		sb.append("<!--患者ID -->");
		sb.append("<id extension=\"0000000059\" root=\"1.2.156.112649.1.2.1.3\"/>");
		sb.append("<!--就诊号 -->");
		sb.append("<id extension=\"0000000059\" root=\"1.2.156.112649.1.2.1.12\"/>");
		sb.append("<!-- 就诊次数  -->");
		sb.append("<id extension=\"1\" root=\"1.2.156.112649.1.2.1.7\"/>");
		sb.append("<patient classCode=\"PSN\" determinerCode=\"INSTANCE\">");
		sb.append("<name>卡卡</name>");
		sb.append("<administrativeGenderCode code=\"1 \" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"\"/>");
		sb.append("<birthTime value=\"\"/>");
		sb.append("</patient>");
		sb.append("</patientRole>");
		sb.append("</recordTarget>");
		sb.append("<!--执行科室 -->");
		sb.append("<location typeCode=\"LOC\">");
		sb.append("<serviceDeliveryLocation classCode=\"SDLOC\">");
		sb.append("<serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
		sb.append("<!--执行科室编码 -->");
		sb.append("<id>");
		sb.append("<item extension=\"0128384\" root=\"1.2.156.112606.1.1.1\"/>");
		sb.append("</id>");
		sb.append("<!-- 执行科室名称 -->");
		sb.append("<name type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"检验科 \"/>");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</serviceProviderOrganization>");
		sb.append("</serviceDeliveryLocation>");
		sb.append("</location>");
		sb.append("<!-- 报告医生 -->");
		sb.append("<author typeCode=\"AUT\">");
		sb.append("<time value=\"2016-10-26 04:13:47\"/>");
		sb.append("<assignedAuthor classCode=\"ASSIGNED\">");
		sb.append("<id extension=\"ZZ\" root=\"1.2.156.112649.1.1.2\"/>");
		sb.append("<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">");
		sb.append("<name>赵赵</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedAuthor>");
		sb.append("</author>");
		sb.append("<!-- 医疗机构 -->");
		sb.append("<custodian>");
		sb.append("<assignedCustodian>");
		sb.append("<representedCustodianOrganization>");
		sb.append("<id extension=\"44643245-7\" root=\"1.2.156.112649\"/>");
		sb.append("<name>东北国际医院</name>");
		sb.append("</representedCustodianOrganization>");
		sb.append("</assignedCustodian>");
		sb.append("</custodian>");
		sb.append("<!-- 审核医生 -->");
		sb.append("<authenticator>");
		sb.append("<time value=\"2016-10-26 04:13:47\"/>");
		sb.append("<signatureCode code=\"S\"/>");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		sb.append("<id extension=\"LIS\" root=\"1.2.156.112649.1.1.2\"/>");
		sb.append("<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">");
		sb.append("<name>LIS</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</authenticator>");
		sb.append("<!-- 申请单号-->");
		sb.append("<order>");
		sb.append("<id extension=\"1610268755\"/>");
		sb.append("</order>");
		sb.append("<entry typeCode=\"DRIV\">");
		sb.append("<act classCode=\"ACT\" moodCode=\"EVN\">");
		sb.append("<entryRelationship displayName=\"报告备注\" typeCode=\"01\">");
		sb.append("<observationText/>");
		sb.append("</entryRelationship>");
		sb.append("<entryRelationship displayName=\"技术备注\" typeCode=\"02\">");
		sb.append("<observationText/>");
		sb.append("</entryRelationship>");
		sb.append("<entryRelationship displayName=\"表现现象\" typeCode=\"03\">");
		sb.append("<observationText/>");
		sb.append("</entryRelationship>");
		sb.append("</act>");
		sb.append("</entry>");
		sb.append("<!--结构体 -->");
		sb.append("<component>");
		sb.append("<structuredBody>");
		sb.append("<!--项目信息 -->");
		sb.append("<entry typeCode=\"DRIV\">");
		sb.append("<!--主项目信息 -->");
		sb.append("<act classCode=\"ACT\" moodCode=\"EVN\">");
		sb.append("<code code=\"G001\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"生化\"/>");
		sb.append("<statusCode code=\"completed\"/>");
		sb.append("<entryRelationship typeCode=\"COMP\">");
		sb.append("<organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb.append("<!--子项目信息 -->");
		sb.append("<component>");
		sb.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("<id value=\"1\"/>");
		sb.append("<printid value=\"1\"/>");
		sb.append("<code code=\"GA\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"糖化白蛋白\"/>");
		sb.append("<value type=\"PQ\" unit=\"%\" value=\"79\"/>");
		sb.append("<referenceRange>");
		sb.append("<observationRange>");
		sb.append("<value type=\"IVL_PQ\">");
		sb.append("<low value=\"11.000\"/>");
		sb.append("<high value=\"17.000\"/>");
		sb.append("</value>");
		sb.append("</observationRange>");
		sb.append("</referenceRange>");
		sb.append("<ABNORMALINDICATOR>");
		sb.append("<code value=\"H\"/>");
		sb.append("</ABNORMALINDICATOR>");
		sb.append("<notetext value=\"11--17\"/>");
		sb.append("</observation>");
		sb.append("</component>");
		sb.append("<component>");
		sb.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("<id value=\"2\"/>");
		sb.append("<printid value=\"2\"/>");
		sb.append("<code code=\"HB\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总血红蛋白\"/>");
		sb.append("<value type=\"PQ\" unit=\"g/dL\" value=\"20\"/>");
		sb.append("<referenceRange>");
		sb.append("<observationRange>");
		sb.append("<value type=\"IVL_PQ\">");
		sb.append("<low value=\"0.000\"/>");
		sb.append("<high value=\"23.000\"/>");
		sb.append("</value>");
		sb.append("</observationRange>");
		sb.append("</referenceRange>");
		sb.append("<ABNORMALINDICATOR>");
		sb.append("<code value=\"M\"/>");
		sb.append("</ABNORMALINDICATOR>");
		sb.append("<notetext value=\"0--23\"/>");
		sb.append("</observation>");
		sb.append("</component>");
		sb.append("<component>");
		sb.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("<id value=\"3\"/>");
		sb.append("<printid value=\"3\"/>");
		sb.append("<code code=\"LIPA\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"脂肪酶\"/>");
		sb.append("<value type=\"PQ\" unit=\"U/L\" value=\"3\"/>");
		sb.append("<referenceRange>");
		sb.append("<observationRange>");
		sb.append("<value type=\"IVL_PQ\">");
		sb.append("<low value=\"\"/>");
		sb.append("<high value=\"\"/>");
		sb.append("</value>");
		sb.append("</observationRange>");
		sb.append("</referenceRange>");
		sb.append("<ABNORMALINDICATOR>");
		sb.append("<code value=\"M\"/>");
		sb.append("</ABNORMALINDICATOR>");
		sb.append("<notetext value=\"\"/>");
		sb.append("</observation>");
		sb.append("</component>");
		sb.append("<component>");
		sb.append("<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("<id value=\"4\"/>");
		sb.append("<printid value=\"4\"/>");
		sb.append("<code code=\"TBIL\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆红素\"/>");
		sb.append("<value type=\"PQ\" unit=\"umol/L\" value=\"4\"/>");
		sb.append("<referenceRange>");
		sb.append("<observationRange>");
		sb.append("<value type=\"IVL_PQ\">");
		sb.append("<low value=\"\"/>");
		sb.append("<high value=\"\"/>");
		sb.append("</value>");
		sb.append("</observationRange>");
		sb.append("</referenceRange>");
		sb.append("<ABNORMALINDICATOR>");
		sb.append("<code value=\"M\"/>");
		sb.append("</ABNORMALINDICATOR>");
		sb.append("<notetext value=\"\"/>");
		sb.append("</observation>");
		sb.append("</component>");
		sb.append("</organizer>");
		sb.append("</entryRelationship>");
		sb.append("<!--标本采集信息 -->");
		sb.append("<entryRelationship typeCode=\"COMP\">");
		sb.append("<act classCode=\"ACT\" moodCode=\"EVN\">");
		sb.append("<templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/>");
		sb.append("<code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/>");
		sb.append("<!--采集人信息 -->");
		sb.append("<specimen typeCode=\"SPC\">");
		sb.append("<effectiveTime value=\"20090415000000.0000-0500\"/>");
		sb.append("<specimenRole classCode=\"SPEC\">");
		sb.append("<id extension=\"5555\" root=\"1.3.6.1.4.1.19376.1.3.4\"/>");
		sb.append("<name extension=\"马龙\"/>");
		sb.append("</specimenRole>");
		sb.append("<!-- 采集地点 -->");
		sb.append("<addr type=\"BAG_AD\">");
		sb.append("<item use=\"WP\">");
		sb.append("<part type=\"BNR\" value=\"护士站\"/>");
		sb.append("</item>");
		sb.append("</addr>");
		sb.append("</specimen>");
		sb.append("<!--接收人信息 -->");
		sb.append("<recimen typeCode=\"REC\">");
		sb.append("<effectiveTime value=\"20090415000000.0000-0500\"/>");
		sb.append("<recimenRole classCode=\"SPEC\">");
		sb.append("<id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/>");
		sb.append("<name extension=\"\"/>");
		sb.append("</recimenRole>");
		sb.append("</recimen>");
		sb.append("<subject contextControlCode=\"OP\" typeCode=\"SBJ\">");
		sb.append("<specimenInContainer classCode=\"CONT\">");
		sb.append("<container classCode=\"CONT\" determinerCode=\"INSTANCE\">");
		sb.append("<!--测试项目容器类型 -->");
		sb.append("<code code=\"0237\"/>");
		sb.append("<!-- 测试项目容器 -->");
		sb.append("<desc value=\"试管\"/>");
		sb.append("</container>");
		sb.append("</specimenInContainer>");
		sb.append("</subject>");
		sb.append("</act>");
		sb.append("</entryRelationship>");
		sb.append("</act>");
		sb.append("</entry>");
		sb.append("</structuredBody>");
		sb.append("</component>");
		sb.append("</ClinicalDocument>");


		return sb.toString();
	}

	/**
	 * 
	 * @Title: getdoctor_bg @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_bg() throws Exception {
		
		String time = document.selectSingleNode("/ClinicalDocument/author/time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("/ClinicalDocument/author/assignedAuthor/assignedPerson/name").getText();// 获取根节点
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
		Node root = document.selectSingleNode("/ClinicalDocument/recordTarget/patientRole/id");  
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
		String time = document.selectSingleNode("/ClinicalDocument/authenticator/time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("/ClinicalDocument/authenticator/assignedEntity/assignedPerson/name").getText();// 获取根节点
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
		String time = document.selectSingleNode("/ClinicalDocument/effectiveTime/@value").getText();// 获取根节点
        this.rc.setEffectiveTime(time);
	}
	
	public void getdoctor_orderid() throws Exception {
		String time = document.selectSingleNode("/ClinicalDocument/order/id/@extension").getText();// 获取根节点
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
		/*String dept_code = document.selectSingleNode("/ClinicalDocument/location/serviceDeliveryLocation/serviceProviderOrganization/id/item/@extension").getText();// 获取根节点
		String dept_name = document.selectSingleNode("/ClinicalDocument/location/serviceDeliveryLocation/serviceProviderOrganization/name/part/@value").getText();// 获取根节点
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
		Node Items = document.selectSingleNode("/ClinicalDocument/component");// 获取根节点
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
			String  Items_code = documentItem.selectSingleNode("/component/structuredBody/entry/act/code/@code").getText();
			retlisch.setChargingItem_num(Items_code);
			
			Node Items = documentItem.selectSingleNode("/component/structuredBody/entry/act/entryRelationship/organizer/component");// 获取根节点
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
			String  Items_code = documentItem.selectSingleNode("/component/observation/code/@code").getText();
			retlisch.setItem_id(Items_code);
			retlisch.setValues(documentItem.selectSingleNode("/component/observation/result").getText());
			retlisch.setValues_dw(documentItem.selectSingleNode("/component/observation/value/@unit").getText());
			/*
			try{
			String fwvaluel = documentItem.selectSingleNode("/component/observation/referenceRange/observationRange/value/low/@value").getText();
			String fwdwl = documentItem.selectSingleNode("/component/observation/referenceRange/observationRange/value/low/@unit").getText();
			
			String fwvalueh = documentItem.selectSingleNode("/component/observation/referenceRange/observationRange/value/high/@value").getText();
			String fwdwh = documentItem.selectSingleNode("/component/observation/referenceRange/observationRange/value/high/@unit").getText();
			retlisch.setValue_fw(fwvaluel+fwdwl+"-"+fwvalueh+fwdwh);
			}catch(Exception ex){}*/
			String fwdwh = documentItem.selectSingleNode("/component/notetext").getText();
			retlisch.setValue_fw(fwdwh);
			retlisch.setValue_ycbz(documentItem.selectSingleNode("/component/ABNORMALINDICATOR/code/@value").getText());
			listRetLisItem.add(retlisch);	
		}
		return listRetLisItem;
	}

	public static void main(String[] args) throws Exception {
		
		StringBuffer xmlmessage=new StringBuffer("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><id extension=\"TI022\"/><creationTime value=\"2016-12-17 14:22:21\"/><interactionId extension=\"POOR_IN200901UV21\" root=\"\"/><processingCode code=\"P\"/><acceptAckCode code=\"NE\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></sender><title>检验报告</title><effectiveTime value=\"20160701\"/><recordTarget typeCode=\"RCT\"><patientRole classCode=\"PAT\"><id extension=\"01\" root=\"1.2.156.112649.1.2.1.2\"/><id extension=\"\" root=\"1.2.156.112649.1.2.1.3\"/><id extension=\"719\" root=\"1.2.156.112649.1.2.1.12\"/><id extension=\"1\" root=\"1.2.156.112649.1.2.1.7\"/><id extension=\"T16C170003\" root=\"exam_num\"/><patient classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>李德军</name><administrativeGenderCode code=\"1\" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"男\"/><birthTime value=\"19651217\"/></patient></patientRole></recordTarget><location typeCode=\"LOC\"><serviceDeliveryLocation classCode=\"SDLOC\"><serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"><id><item extension=\"1070101\" root=\"1.2.156.112606.1.1.1\"/></id><name xsi:type=\"BAG_EN\"><item><part value=\"检验科\"/></item></name></serviceProviderOrganization></serviceDeliveryLocation></location><author typeCode=\"AUT\"><time value=\"2016-12-17 14:21:57\"/><assignedAuthor classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson><name>回晓娇</name></assignedPerson></assignedAuthor></author><custodian><assignedCustodian><representedCustodianOrganization><id extension=\"44643245-7\" root=\"1.2.156.112649\"/><name>东北国际医院</name></representedCustodianOrganization></assignedCustodian></custodian><authenticator><time value=\"2016-12-17 14:21:57\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"910000004895\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250302001\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血糖测定\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301009\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"间接胆红素\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[8.24]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[2--15.22]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301046\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"葡萄糖\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.81]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[3.89--6.11]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250307005B\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血清尿酸测定\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301030\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿酸\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[373]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[210--430]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ009\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"肝功四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301007\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆红素\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[12.22]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[5.1--28]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301008\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"直接胆红素\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.98]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--10]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301013\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"丙氨酸氨基转移酶\"/><value unit=\"U/L\" xsi:type=\"PQ\"/><result><![CDATA[66]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[9--50]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ020\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"肾功二项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301028\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿素\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[6.1]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[2.86--8.2]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301029\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"肌酐\"/><value unit=\"umol/L\" xsi:type=\"PQ\"/><result><![CDATA[76]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[53--123]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ024\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血脂四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301020\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"甘油三脂\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.26]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--2.3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301021\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.30]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5.17]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301022\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"高密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[0.93]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0.78--1.81]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0301023\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"低密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.41]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3.37]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component></ClinicalDocument>");
///			StringBuffer xmlmessage=new StringBuffer("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><id extension=\"TI022\"/><creationTime value=\"2016-12-9 09:41:41\"/><interactionId extension=\"POOR_IN200901UV21\" root=\"\"/><processingCode code=\"P\"/><acceptAckCode code=\"NE\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></sender><title>检验报告</title><effectiveTime value=\"20160701\"/><recordTarget typeCode=\"RCT\"><patientRole classCode=\"PAT\"><id extension=\"01\" root=\"1.2.156.112649.1.2.1.2\"/><id extension=\"\" root=\"1.2.156.112649.1.2.1.3\"/><id extension=\"98576\" root=\"1.2.156.112649.1.2.1.12\"/><id extension=\"1\" root=\"1.2.156.112649.1.2.1.7\"/><id extension=\"T16C090025\" root=\"exam_num\"/><patient classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>董秀丹</name><administrativeGenderCode code=\"2\" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"女\"/><birthTime value=\"19811209\"/></patient></patientRole></recordTarget><location typeCode=\"LOC\"><serviceDeliveryLocation classCode=\"SDLOC\"><serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"><id><item extension=\"1070101\" root=\"1.2.156.112606.1.1.1\"/></id><name xsi:type=\"BAG_EN\"><item><part value=\"检验科\"/></item></name></serviceProviderOrganization></serviceDeliveryLocation></location><author typeCode=\"AUT\"><time value=\"2016-12-9 09:41:36\"/><assignedAuthor classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson><name>李杰英</name></assignedPerson></assignedAuthor></author><custodian><assignedCustodian><representedCustodianOrganization><id extension=\"44643245-7\" root=\"1.2.156.112649\"/><name>东北国际医院</name></representedCustodianOrganization></assignedCustodian></custodian><authenticator><time value=\"2016-12-9 09:41:36\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"980000003464\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250102035\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"尿常规(最后验)\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0102003\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿胆原\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[正常]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[正常]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0102004\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿胆红素\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0102005\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"酮体\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0102006\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"隐血\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"5\"/><printid value=\"5\"/><code code=\"0102007\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿蛋白\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"6\"/><printid value=\"6\"/><code code=\"0102008\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"亚硝酸盐\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"7\"/><printid value=\"7\"/><code code=\"0102009\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"白细胞\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"8\"/><printid value=\"8\"/><code code=\"0102011\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿比重\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[1.030]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[1.01--1.025]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"9\"/><printid value=\"9\"/><code code=\"0102012\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"酸碱度\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[5.0]]></result></observation><ABNORMALINDICATOR><code value=\"L\"/></ABNORMALINDICATOR><notetext><![CDATA[5.5--7]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"10\"/><printid value=\"10\"/><code code=\"0102013\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"维生素C\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"11\"/><printid value=\"11\"/><code code=\"0302019\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"糖\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"12\"/><printid value=\"12\"/><code code=\"0102401\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检红细胞\"/><value unit=\"/HP\" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"13\"/><printid value=\"13\"/><code code=\"0102402\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检白细胞\"/><value unit=\"/HP\" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"14\"/><printid value=\"14\"/><code code=\"0102403\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检管型\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"\"/></ABNORMALINDICATOR><notetext><![CDATA[]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"15\"/><printid value=\"15\"/><code code=\"0102038\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"其它\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"\"/></ABNORMALINDICATOR><notetext><![CDATA[]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-9 09:07:33\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ024\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血脂四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301020\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"甘油三脂\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.26]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--2.3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301021\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.30]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5.17]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301022\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"高密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[0.93]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0.78--1.81]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0301023\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"低密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.41]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3.37]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component></ClinicalDocument>");
        //StringBuffer xmlmessage=new StringBuffer("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><authenticator><time value=\"2016-12-17 14:21:57\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"910000004895\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component></ClinicalDocument>");
		ResLisMessage rpm = new ResLisMessage(xmlmessage.toString(),true);
		
	}

}
