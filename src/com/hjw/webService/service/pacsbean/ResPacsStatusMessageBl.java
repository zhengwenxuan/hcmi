package com.hjw.webService.service.pacsbean;

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

public class ResPacsStatusMessageBl {
	public RetPacsStatusCustome rc = new RetPacsStatusCustome();
	private Document document;
	public ResPacsStatusMessageBl(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=checkxml(xmlmessage);
		}else{
			xmlmess= getXml_test1();
		}
		 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
		 Map<String, String> xmlMap = new HashMap<>();
	 	 xmlMap.put("abc", "urn:hl7-org:v3");
	 	 SAXReader sax = new SAXReader();
	 	 sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		 this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 this.getCustom();// 获取检查人员信息		
		 this.getdoctor_effectiveTime();
		 this.getdoctor_Item();// 关联检查项目
	}
	
	private String checkxml(String xmlmess){
		/*if(xmlmess.indexOf("urn:hl7-org:v3")>0){
			xmlmess=xmlmess.substring(0,16)+ xmlmess.substring(187,xmlmess.length());
		}
		xmlmess=xmlmess.replaceAll("xsi:", "");*/
		return xmlmess;
	}
	
	private String getXml_test1() {
		// TODO Auto-generated method stub
		String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		
str=str+"<POOR_IN200901UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3  ../../Schemas/POOR_IN200901UV23.xsd\">";
str=str+"  <id extension=\"TJ013\"/>";
str=str+"  <creationTime value=\"20180522153613\"/>";
str=str+"  <interactionId extension=\"POOR_IN200901UV23\"/>";
str=str+"  <processingCode code=\"P\"/>";
str=str+"  <processingModeCode code=\"T\"/>";
str=str+"  <acceptAckCode code=\"NE\"/>";
str=str+"  <receiver typeCode=\"RCV\">";
str=str+"    <device classCode=\"DEV\" determinerCode=\"INSTANCE\">";
str=str+"      <id>";
str=str+"        <item extension=\"\" root=\"\"/>";
str=str+"      </id>";
str=str+"    </device>";
str=str+"  </receiver>";
str=str+"  <sender typeCode=\"SND\">";
str=str+"    <device classCode=\"DEV\" determinerCode=\"INSTANCE\">";
str=str+"      <id>";
str=str+"        <item extension=\"\" root=\"\"/>";
str=str+"      </id>";
str=str+"    </device>";
str=str+"  </sender>";
str=str+"  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">";
str=str+"    <code code=\"new\"/>";
str=str+"    <effectiveTime value=\"2018-05-22\"/>";
str=str+"    <subject typeCode=\"SUBJ\" xsi:nil=\"false\">";
str=str+"      <placerGroup>";
str=str+"        <subject typeCode=\"SBJ\">";
str=str+"          <patient classCode=\"PAT\">";
str=str+"            <id>";
str=str+"              <item extension=\"2\" root=\"1.2.156.112606.1.2.1.2\"/>";
str=str+"              <item extension=\"T185220056\" root=\"1.2.156.112606.1.2.1.3\"/>";
str=str+"              <item extension=\"0\" root=\"1.2.156.112606.1.2.1.7\"/>";
str=str+"              <item extension=\"\" root=\"1.2.156.112606.1.2.1.12\"/>";
str=str+"            </id>";
str=str+"          </patient>";
str=str+"        </subject>";
str=str+"        <performer typeCode=\"PRF\">";
str=str+"          <assignedEntity classCode=\"ASSIGNED\">";
str=str+"            <id>";
str=str+"              <item extension=\"\" root=\"1.2.156.112606.1.1.2\"/>";
str=str+"            </id>";
str=str+"            <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">";
str=str+"              <name xsi:type=\"BAG_EN\">";
str=str+"                <item use=\"ABC\">";
str=str+"                  <part value=\"李京\"/>";
str=str+"                </item>";
str=str+"              </name>";
str=str+"            </assignedPerson>";
str=str+"          </assignedEntity>";
str=str+"        </performer>";
str=str+"        <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">";
str=str+"          <id>";
str=str+"            <item extension=\"\" root=\"1.2.156.112606.1.1.1\"/>";
str=str+"          </id>";
str=str+"          <name xsi:type=\"BAG_EN\">";
str=str+"            <item>";
str=str+"              <part value=\"健康管理中心\"/>";
str=str+"            </item>";
str=str+"          </name>";
str=str+"        </representedOrganization>";
str=str+"        <custodian>";
str=str+"          <assignedCustodian>";
str=str+"            <representedCustodianOrganization>";
str=str+"              <id extension=\"44643245-7\" root=\"1.2.156.112649\"/>";
str=str+"              <name>东北国际医院</name>";
str=str+"            </representedCustodianOrganization>";
str=str+"          </assignedCustodian>";
str=str+"        </custodian>";
str=str+"        <location typeCode=\"LOC\" xsi:nil=\"false\">";
str=str+"          <serviceDeliveryLocation classCode=\"SDLOC\">";
str=str+"            <serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">";
str=str+"              <id>";
str=str+"                <item extension=\"04010801\" root=\"1.21.156.112606.1.1.1\"/>";
str=str+"              </id>";
str=str+"              <name xsi:type=\"BAG_EN\">";
str=str+"                <item>";
str=str+"                  <part value=\"病理科\"/>";
str=str+"                </item>";
str=str+"              </name>";
str=str+"            </serviceProviderOrganization>";
str=str+"          </serviceDeliveryLocation>";
str=str+"        </location>";
str=str+"        <component2>";
str=str+"          <observationRequest classCode=\"OBS\">";
str=str+"            <id>";
str=str+"              <item extension=\"18052200337\"/>";
str=str+"            </id>";
str=str+"            <code code=\"\" codeSystem=\"\" codeSystemName=\"\">";
str=str+"              <displayName value=\"\"/>";
str=str+"            </code>";
str=str+"            <methodCode>";
str=str+"              <item code=\"\" codeSystem=\"\" codeSystemName=\"\">";
str=str+"                <displayName value=\"\"/>";
str=str+"              </item>";
str=str+"            </methodCode>";
str=str+"            <targetSiteCode>";
str=str+"              <displayName value=\"\"/>";
str=str+"              <displayName value=\"\"/>";
str=str+"              <displayName value=\"\"/>";
str=str+"            </targetSiteCode>";
str=str+"          </observationRequest>";
str=str+"        </component2>";
str=str+"        <component1 contextConductionInd=\"true\">";
str=str+"          <processStep classCode=\"PROC\">";
str=str+"            <code code=\"2\" codeSystem=\"1.2.156.112606.1.1.93\">";
str=str+"              <displayName value=\"已执行\"/>";
str=str+"            </code>";
str=str+"          </processStep>";
str=str+"        </component1>";
str=str+"      </placerGroup>";
str=str+"    </subject>";
str=str+"  </controlActProcess>";
str=str+"</POOR_IN200901UV>";
		return str;
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
		Node root = document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:subject/abc:patient/abc:id/abc:item");  
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
					if ("1.2.156.112606.1.2.1.12".equals(attr.getValue().trim())){
						for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
							if("extension".equals(attrone.getName().trim().toLowerCase())){
								this.rc.setCoustom_jzh(attrone.getValue());
								break;
							}
						}
					}
					
					if ("1.2.156.112606.1.2.1.3".equals(attr.getValue().trim())){
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
	     * @Title: getdoctor_orderid   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @return
	     * @param: @throws Exception      
	     * @return: String      
	     * @throws
	 */
	public void getdoctor_effectiveTime() throws Exception {
		String time = document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:effectiveTime/@value").getText();// 获取根节点
        this.rc.setEffectiveTime(time);
        
        String status = document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:component1/abc:processStep/abc:code/@code").getText();// 获取根节点
        this.rc.setStatus(status);
	}
	
	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("abc:POOR_IN200901UV/abc:controlActProcess/abc:subject/abc:placerGroup/abc:component2/abc:observationRequest");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_coms(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_coms(List<Element> listElement)  throws Exception {	
		List<String> rtlischarge=new ArrayList<String>();// 收费项目 
		for(Element e:listElement){
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("abc:observationRequest/abc:id/abc:item/@extension").getText();
			rtlischarge.add(Items_code);
		}
		this.rc.setPacs_summary_id(rtlischarge);
	}
	

	public static void main(String[] args) {
		try {
			ResPacsStatusMessage rpm = new ResPacsStatusMessage("",false);
			System.out.println(rpm.rc.getExam_num());
			System.out.println(rpm.rc.getStatus());
			System.out.println(rpm.rc.getPacs_summary_id().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
