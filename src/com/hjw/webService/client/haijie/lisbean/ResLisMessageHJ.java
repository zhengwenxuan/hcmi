package com.hjw.webService.client.haijie.lisbean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;

public class ResLisMessageHJ {
	public RetLisCustomeHJ rc = new RetLisCustomeHJ();
	private Document document;
	public ResLisMessageHJ(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=checkxml(xmlmessage);
		}else{
			xmlmess= getXml_test();
		}
		 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
         SAXReader sax = new SAXReader();
		 this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 this.getdoctor_bg();// 获取报告医生
		 this.getdoctor_sh();// 获取审核医生
		 this.getdoctor_orderid();// 关联医嘱号或者清单号
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
		
		String name = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/operatorCode").getText();// 获取根节点
		String time = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/testDate").getText();// 获取根节点
        this.rc.setDoctor_name_bg(name);
        this.rc.setDoctor_time_bg(time);
	}

	/**
	 * 
	 * @Title: getdoctor_sh @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_sh() throws Exception {
		String name = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/auditDoctorCode").getText();// 获取根节点
		String time = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/auditDate").getText();// 获取根节点
        this.rc.setDoctor_name_sh(name);
        this.rc.setDoctor_time_sh(time);
	}

	public void getdoctor_orderid() throws Exception {
		String barcode = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/barcode").getText();// 获取根节点
        this.rc.setSample_barcode(barcode);
	}

	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		List<RetLisItemHJ> listRetLisItem = getNodes_doctor_items(listElement);
		this.rc.setListRetLisItem(listRetLisItem);
	}

	@SuppressWarnings("unchecked")
	private List<RetLisItemHJ> getNodes_doctor_items(List<Element> listElement)  throws Exception {	
		List<RetLisItemHJ> listRetLisItem =new ArrayList<RetLisItemHJ>();
		for(Element e:listElement){
			RetLisItemHJ  retlisch = new RetLisItemHJ();
			//System.out.println(e.asXML());
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			retlisch.setItem_id(documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/testItemCode").getText());
			retlisch.setValues(documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/resultValue").getText());
			retlisch.setValues_dw(documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/resultUnit").getText());
			retlisch.setValue_fw(documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/resultRange").getText());
			retlisch.setValue_jd(documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/acuracy").getText());
			retlisch.setValue_zt(documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/resultState").getText());
			listRetLisItem.add(retlisch);	
		}
		return listRetLisItem;
	}

}
