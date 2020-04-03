package com.hjw.webService.client.hokai.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.webService.service.lisbean.RetLisChargeItem;
import com.hjw.webService.service.lisbean.RetLisCustome;
import com.hjw.webService.service.lisbean.RetLisItem;

public class ResLisMessageHK {
	public RetLisCustome rc = new RetLisCustome();
	private Document document;
	public ResLisMessageHK(String xmlmessage,boolean flags) throws Exception{
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
//		 this.getdoctor_effectiveTime();
		 //this.getdoctor_orderid();// 关联医嘱号或者清单号
//		 this.getdoctor_deptcode();// 关联科室编码对应类别编码
		 this.getdoctor_Item();// 关联检查项目
	}

	/**
	 * 
	 * @Title: getdoctor_bg @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_bg() throws Exception {
		
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name/abc:item/abc:part/@value").getText();// 获取根节点
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
		String exam_num= document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:id/abc:item/@extension").getText();// 获取根节点
		this.rc.setExam_num(exam_num);
	}

	/**
	 * 
	 * @Title: getdoctor_sh @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_sh() throws Exception {
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:assignedEntity/abc:assignedPerson/abc:name").getText();// 获取根节点
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
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:componentOf/abc:encompassingEncounter/abc:effectiveTime/@value").getText();// 获取根节点
        this.rc.setEffectiveTime(time);
	}
	
	public void getdoctor_orderid() throws Exception {
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:checkAppId/@extension").getText();// 获取根节点
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
		String dept_code = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:participant/abc:associatedEntity/abc:scopingOrganization/abc:id/@extension").getText();// 获取根节点
		String dept_name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:participant/abc:associatedEntity/abc:scopingOrganization/abc:id/abc:name").getText();// 获取根节点
		this.rc.setDept_code(dept_code);
        this.rc.setDept_name(dept_name);
	}

	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component1/abc:section/abc:entry/abc:observation/abc:entryRelationship/abc:organizer");// 获取根节点
		//System.out.println("当前节点的内容：" + Items.asXML());// 当前节点名称
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_coms(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_coms(List<Element> listElement)  throws Exception {	
		List<RetLisChargeItem> rtlischarge=new ArrayList<RetLisChargeItem>();// 收费项目 
		for(Element e:listElement){
			String componentxml=e.asXML();
			//System.out.println("当前节点的内容：" + e.asXML());// 当前节点名称
			if(componentxml.indexOf("<organizer")>=0){
				try{
			//System.out.println("当前节点名称：" + e.getName());// 当前节点名称

			RetLisChargeItem  retlisch = new RetLisChargeItem();
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			 Map<String, String> xmlMap = new HashMap<>();
		 		xmlMap.put("abc", "urn:hl7-org:v3");
		 		SAXReader sax = new SAXReader();
		 		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document documentItem = sax.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("/abc:organizer/abc:code/@code").getText();//组合项编码
			retlisch.setChargingItem_num(Items_code);
			
			Node Items = documentItem.selectSingleNode("/abc:organizer/abc:component");// 获取根节点
			List<Element> listItemElement = Items.getParent().elements();// 所有一级子节点的list
			List<RetLisItem> listRetLisItem =new ArrayList<RetLisItem>();
			listRetLisItem = getNodes_doctor_items(listItemElement);
			retlisch.setListRetLisItem(listRetLisItem);
			rtlischarge.add(retlisch);
				}catch(Exception ex){
					
				}
			}
		}
		this.rc.setListRetLisChargeItem(rtlischarge);
	}

	@SuppressWarnings("unchecked")
	private List<RetLisItem> getNodes_doctor_items(List<Element> listElement)  throws Exception {	
		List<RetLisItem> listRetLisItem =new ArrayList<RetLisItem>();
		for(Element e:listElement){
			try{
			RetLisItem  retlisch = new RetLisItem();
			System.out.println(e.asXML());
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Map<String, String> xmlMap = new HashMap<>();
	 		xmlMap.put("abc", "urn:hl7-org:v3");
	 		saxitem.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("/abc:component/abc:observation/abc:code/@code").getText();
			retlisch.setItem_id(Items_code);
			try{
			retlisch.setValues(documentItem.selectSingleNode("/abc:component/abc:observation/abc:value/@value").getText());
			}catch(Exception ex){
				try{
					retlisch.setValues(documentItem.selectSingleNode("/abc:component/abc:observation/abc:value").getText());
					}catch(Exception ex1){
					}
			}
			retlisch.setValues_dw(documentItem.selectSingleNode("/abc:component/abc:observation/abc:value/@unit").getText());
			try{			
				retlisch.setValue_ycbz(documentItem.selectSingleNode("/abc:component/abc:observation/abc:interpretationCode[@code='2.16.840.1.113883.7.1']/abc:translation/@code").getText());
			}catch(Exception ex){}
			try{
				String value_ycbz=documentItem.selectSingleNode("/abc:component/abc:observation/abc:interpretationCode[@code='2.16.840.1.113883.7.2']/abc:translation/@code").getText();
				
				if(value_ycbz!=null && value_ycbz.trim().length()>0){
					retlisch.setValue_ycbz(value_ycbz);
				}
				
			}catch(Exception ex){}
			try{
			String fwvaluel = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/abc:low/@value").getText();
			String fwdwl = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/@unit").getText();
			String fwvalueh = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/abc:high/@value").getText();
			String fwdwh = documentItem.selectSingleNode("/abc:component/abc:observation/abc:referenceRange/abc:observationRange/abc:value/@unit").getText();
			//retlisch.setValue_fw(fwvaluel+fwdwl+"-"+fwvalueh+fwdwh);
			retlisch.setValue_fw(fwvaluel+"-"+fwvalueh);
			System.out.println(retlisch.getValue_fw());
			}catch(Exception ex){}
			  //retlisch.setValue_ycbz(documentItem.selectSingleNode("/component/ABNORMALINDICATOR/code/@value").getText());
			listRetLisItem.add(retlisch);	
			}catch(Exception ex){}
		}
		return listRetLisItem;
	}

	public static void main(String[] args) throws Exception {
		
		StringBuffer xmlmessage=new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>  <clinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\">   <realmCode code=\"CN\"></realmCode>   <typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_HD000040\"></typeId>   <id root=\"S008\" extension=\"994880_28_24_20180319\"></id>   <code code=\"04\" codeSystem=\"1.2.156.112685.1.1.60\" displayName=\"检查检验记录\"></code>   <title>检验报告</title>   <effectiveTime value=\"20180319145658\"></effectiveTime>   <confidentialityCode code=\"文档保密程度\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" displayName=\"normal\"></confidentialityCode>   <languageCode code=\"zh-CN\"></languageCode>   <setId extension=\"BS319\"></setId>   <versionNumber value=\"0\"></versionNumber>   <recordTarget>    <patientRole>     <id root=\"1.2.156.112685.1.2.1.2\" extension=\"01\"></id>     <id root=\"1.2.156.112685.1.2.1.3\" extension=\"4554326\"></id>     <id root=\"1.2.156.112685.1.2.1.12\" extension=\"1803190879\"></id>     <patient>      <name>白叙含</name>      <administrativeGenderCode code=\"1\" codeSystem=\"1.2.156.112685.1.1.3\" displayName=\"男\"></administrativeGenderCode>      <birthTime value=\"20161011\"></birthTime>     </patient>    </patientRole>   </recordTarget>   <author>    <time value=\"20180319145658\"></time>    <assignedAuthor>     <id root=\"1.2.156.112685.1.1.2\" extension=\"48\"></id>     <assignedPerson>      <name>吴小龙</name>     </assignedPerson>    </assignedAuthor>   </author>   <custodian>    <assignedCustodian>     <representedCustodianOrganization>      <id root=\"1.2.156.112685\" extension=\"46014326-4\"></id>      <name>包头市中心医院</name>     </representedCustodianOrganization>    </assignedCustodian>   </custodian>   <legalAuthenticator>    <time></time>    <signatureCode code=\"S\"></signatureCode>    <assignedEntity>     <id extension=\"\"></id>    </assignedEntity>   </legalAuthenticator>   <authenticator>    <time value=\"20180319145658\"></time>    <signatureCode code=\"S\"></signatureCode>    <assignedEntity>     <id root=\"1.2.156.112685.1.1.2\" extension=\"20\"></id>     <assignedPerson>      <name>郭静</name>     </assignedPerson>    </assignedEntity>   </authenticator>   <participant typeCode=\"DIST\">    <associatedEntity classCode=\"ASSIGNED\">     <id root=\"1.2.156.112685.1.1.2\" extension=\"张艳梅\"></id>     <associatedPerson>      <name>张艳梅</name>     </associatedPerson>    </associatedEntity>   </participant>   <participant typeCode=\"PRF\">    <associatedEntity classCode=\"ASSIGNED\">     <associatedPerson></associatedPerson>     <scopingOrganization>      <id root=\"1.2.156.112685.1.1.1\" extension=\"0301\"></id>      <name>化验室</name>     </scopingOrganization>    </associatedEntity>   </participant>   <participant typeCode=\"AUT\">    <time value=\"20180319132300\"></time>    <associatedEntity classCode=\"ASSIGNED\">     <scopingOrganization>      <id root=\"1.2.156.112685.1.1.1\" extension=\"022831\"></id>      <name>儿科门诊</name>     </scopingOrganization>    </associatedEntity>   </participant>   <inFulfillmentOf>    <order>     <id extension=\"69883365\"></id>    </order>   </inFulfillmentOf>   <componentOf>    <encompassingEncounter>     <id root=\"1.2.156.112685.1.2.1.7\" extension=\"180142102\"></id>     <id root=\"1.2.156.112685.1.2.1.6\" extension=\"45543260142102\"></id>     <code code=\"02\" codeSystem=\"1.2.156.112685.1.1.80\" displayName=\"急诊\"></code>     <effectiveTime></effectiveTime>     <location>      <healthCareFacility>       <serviceProviderOrganization>        <asOrganizationPartOf classCode=\"PART\">         <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">          <id extension=\"\"></id>          <asOrganizationPartOf classCode=\"PART\">           <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">            <id extension=\"\"></id>            <asOrganizationPartOf classCode=\"PART\">             <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">              <id root=\"1.2.156.112685.1.1.1\" extension=\"022831\"></id>              <name>儿科门诊</name>              <asOrganizationPartOf classCode=\"PART\">               <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                <id root=\"1.2.156.112685.1.1.33\" extension=\"\"></id>                <name></name>               </wholeOrganization>              </asOrganizationPartOf>             </wholeOrganization>            </asOrganizationPartOf>           </wholeOrganization>          </asOrganizationPartOf>         </wholeOrganization>        </asOrganizationPartOf>       </serviceProviderOrganization>      </healthCareFacility>     </location>    </encompassingEncounter>   </componentOf>   <component>    <structuredBody>     <component>      <section>       <code code=\"34076-0\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Information for patients section\"></code>       <title>文档中患者相关信息</title>       <entry>        <observation classCode=\"OBS\" moodCode=\"EVN\">         <code code=\"397669002\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"age\"></code>         <value xsi:type=\"ST\">17月</value>        </observation>       </entry>      </section>     </component>     <component>      <section>       <code code=\"11502-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Laboratory report\"></code>       <title>检验</title>       <text>        <content ID=\"a1\"></content>        <content ID=\"a2\"></content>        <content ID=\"a3\"></content>        <content ID=\"a4\"></content>       </text>       <entry>        <observation classCode=\"OBS\" moodCode=\"EVN\">         <code></code>         <statusCode code=\"completed\"></statusCode>         <priorityCode code=\"\"></priorityCode>         <methodCode displayName=\"\"></methodCode>         <entryRelationship typeCode=\"COMP\">          <organizer classCode=\"BATTERY\" moodCode=\"EVN\">           <code code=\"310388008\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"relative information status\"></code>           <statusCode code=\"completed\"></statusCode>           <component>            <observationMedia classCode=\"OBS\" moodCode=\"EVN\">             <value xsi:type=\"ED\" mediaType=\"\"></value>             <entryRelationship typeCode=\"XCRPT\">              <act classCode=\"INFRM\" moodCode=\"PRP\">               <code></code>               <text></text>              </act>             </entryRelationship>            </observationMedia>           </component>          </organizer>         </entryRelationship>         <entryRelationship typeCode=\"COMP\">                    <organizer classCode=\"BATTERY\" moodCode=\"EVN\">           <code code=\"1T022\" codeSystem=\"1.2.156.112685.1.1.46\" displayName=\"便常规\"></code>           <statusCode code=\"completed\"></statusCode>           <component>                        <sequenceNumber value=\"1\"></sequenceNumber>            <observation classCode=\"OBS\" moodCode=\"EVN\">             <text>阴性</text>             <statusCode code=\"completed\"></statusCode>             <value xsi:type=\"ST\">阳性</value>             <code code=\"0103055\" codeSystem=\"1.2.156.112685.1.1.108\" displayName=\"\">              <originalText>隐血试验</originalText>             </code>             <interpretationCode code=\"04\" displayName=\"↑\">              <originalText>阳性</originalText>              <translation code=\"数值标识\"></translation>             </interpretationCode>             <interpretationCode code=\"04\" displayName=\"非危急值\">              <translation code=\"危险标识\"></translation>             </interpretationCode>             <referenceRange>              <observationRange>               <code code=\"01\" displayName=\"参考范围值\"></code>               <text></text>               <value xsi:type=\"IVL_PQ\" unit=\"\">                <low value=\"\"></low>                <high value=\"\"></high>               </value>              </observationRange>             </referenceRange>            </observation>           </component>           <component>                        <sequenceNumber value=\"2\"></sequenceNumber>            <observation classCode=\"OBS\" moodCode=\"EVN\">             <text></text>             <statusCode code=\"completed\"></statusCode>             <value xsi:type=\"ST\">黄色粘便带血</value>             <code code=\"0103001\" codeSystem=\"1.2.156.112685.1.1.108\" displayName=\"\">              <originalText>便外观</originalText>             </code>             <interpretationCode code=\"01\" displayName=\"-\">              <originalText>正常</originalText>              <translation code=\"数值标识\"></translation>             </interpretationCode>             <interpretationCode code=\"04\" displayName=\"非危急值\">              <translation code=\"危险标识\"></translation>             </interpretationCode>             <referenceRange>              <observationRange>               <code code=\"01\" displayName=\"参考范围值\"></code>               <text></text>               <value xsi:type=\"IVL_PQ\" unit=\"\">                <low value=\"\"></low>                <high value=\"\"></high>               </value>              </observationRange>             </referenceRange>            </observation>           </component>           <component>                        <sequenceNumber value=\"3\"></sequenceNumber>            <observation classCode=\"OBS\" moodCode=\"EVN\">             <text>无</text>             <statusCode code=\"completed\"></statusCode>             <value xsi:type=\"ST\">++</value>             <code code=\"0100093\" codeSystem=\"1.2.156.112685.1.1.108\" displayName=\"\">              <originalText>便镜检红细胞</originalText>             </code>             <interpretationCode code=\"04\" displayName=\"↑\">              <originalText>阳性</originalText>              <translation code=\"数值标识\"></translation>             </interpretationCode>             <interpretationCode code=\"04\" displayName=\"非危急值\">              <translation code=\"危险标识\"></translation>             </interpretationCode>             <referenceRange>              <observationRange>               <code code=\"01\" displayName=\"参考范围值\"></code>               <text></text>               <value xsi:type=\"IVL_PQ\" unit=\"/HP\">                <low value=\"123\"></low>                <high value=\"456\"></high>               </value>              </observationRange>             </referenceRange>            </observation>           </component>           <component>                        <sequenceNumber value=\"4\"></sequenceNumber>            <observation classCode=\"OBS\" moodCode=\"EVN\">             <text>不见或偶见</text>             <statusCode code=\"completed\"></statusCode>             <value xsi:type=\"ST\">++</value>             <code code=\"0100092\" codeSystem=\"1.2.156.112685.1.1.108\" displayName=\"\">              <originalText>便镜检白细胞</originalText>             </code>             <interpretationCode code=\"04\" displayName=\"↑\">              <originalText>阳性</originalText>              <translation code=\"数值标识\"></translation>             </interpretationCode>             <interpretationCode code=\"04\" displayName=\"非危急值\">              <translation code=\"危险标识\"></translation>             </interpretationCode>             <referenceRange>              <observationRange>               <code code=\"01\" displayName=\"参考范围值\"></code>               <text></text>               <value xsi:type=\"IVL_PQ\" unit=\"/HP\">                <low value=\"123\"></low>                <high value=\"456\"></high>               </value>              </observationRange>             </referenceRange>            </observation>           </component>          </organizer>         </entryRelationship>         <entryRelationship typeCode=\"SAS\" inversionInd=\"true\">          <procedure classCode=\"PROC\" moodCode=\"EVN\">           <code></code>           <statusCode code=\"completed\"></statusCode>           <effectiveTime value=\"20180319140532\"></effectiveTime>           <specimen>            <specimenRole>             <id extension=\"4803196659\"></id>             <specimenPlayingEntity>              <code code=\"14\" codeSystem=\"1.2.156.112685.1.1.45\" displayName=\"粪便\"></code>             </specimenPlayingEntity>            </specimenRole>           </specimen>           <performer>            <assignedEntity>             <id root=\"1.2.156.112685.1.1.2\" extension=\"u9999\"></id>             <assignedPerson>              <name>任中婷</name>             </assignedPerson>             <representedOrganization>              <id root=\"1.2.156.112685.1.1.1\" extension=\"\"></id>              <name></name>             </representedOrganization>            </assignedEntity>           </performer>           <participant typeCode=\"RCV\">            <time value=\"20180319140554\"></time>            <participantRole>             <id root=\"1.2.156.112685.1.1.2\" extension=\"u9999\"></id>             <playingEntity>              <name>任中婷</name>             </playingEntity>            </participantRole>           </participant>           <participant typeCode=\"SBJ\">            <participantRole>             <playingDevice>              <code code=\"82\" displayName=\"\"></code>             </playingDevice>            </participantRole>           </participant>           <entryRelationship typeCode=\"SPRT\">            <observationMedia classCode=\"OBS\" moodCode=\"EVN\">             <value xsi:type=\"ED\" mediaType=\"\"></value>             <entryRelationship typeCode=\"XCRPT\">              <act classCode=\"INFRM\" moodCode=\"PRP\">               <code></code>               <text>提示信息</text>              </act>             </entryRelationship>            </observationMedia>           </entryRelationship>          </procedure>         </entryRelationship>        </observation>       </entry>      </section>     </component>     <component>      <section>       <code code=\"29308-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Diagnosis\"></code>       <title>诊断</title>       <entry typeCode=\"DRIV\">        <act classCode=\"ACT\" moodCode=\"EVN\">         <code nullFlavor=\"NA\"></code>         <entryRelationship typeCode=\"SUBJ\">          <observation classCode=\"OBS\" moodCode=\"EVN\">           <code code=\"\" codeSystem=\"1.2.156.112685.1.1.29\" displayName=\"(055002)小儿腹泻病\"></code>           <statusCode code=\"completed\"></statusCode>           <value xsi:type=\"CD\" code=\"\" codeSystem=\"1.2.156.112685.1.1.30\" displayName=\"\"></value>          </observation>         </entryRelationship>        </act>       </entry>      </section>     </component>     <component>      <section>       <entry>        <observation classCode=\"OBS\" moodCode=\"EVN\">         <code code=\"\" displayName=\"\"></code>        </observation>       </entry>      </section>     </component>    </structuredBody>   </component>  </clinicalDocument>");
///			StringBuffer xmlmessage=new StringBuffer("<clinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><id extension=\"TI022\"/><creationTime value=\"2016-12-9 09:41:41\"/><interactionId extension=\"POOR_IN200901UV21\" root=\"\"/><processingCode code=\"P\"/><acceptAckCode code=\"NE\"/><receiver typeCode=\"RCV\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/></id></device></sender><title>检验报告</title><effectiveTime value=\"20160701\"/><recordTarget typeCode=\"RCT\"><patientRole classCode=\"PAT\"><id extension=\"01\" root=\"1.2.156.112649.1.2.1.2\"/><id extension=\"\" root=\"1.2.156.112649.1.2.1.3\"/><id extension=\"98576\" root=\"1.2.156.112649.1.2.1.12\"/><id extension=\"1\" root=\"1.2.156.112649.1.2.1.7\"/><id extension=\"T16C090025\" root=\"exam_num\"/><patient classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>董秀丹</name><administrativeGenderCode code=\"2\" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"女\"/><birthTime value=\"19811209\"/></patient></patientRole></recordTarget><location typeCode=\"LOC\"><serviceDeliveryLocation classCode=\"SDLOC\"><serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"><id><item extension=\"1070101\" root=\"1.2.156.112606.1.1.1\"/></id><name xsi:type=\"BAG_EN\"><item><part value=\"检验科\"/></item></name></serviceProviderOrganization></serviceDeliveryLocation></location><author typeCode=\"AUT\"><time value=\"2016-12-9 09:41:36\"/><assignedAuthor classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson><name>李杰英</name></assignedPerson></assignedAuthor></author><custodian><assignedCustodian><representedCustodianOrganization><id extension=\"44643245-7\" root=\"1.2.156.112649\"/><name>东北国际医院</name></representedCustodianOrganization></assignedCustodian></custodian><authenticator><time value=\"2016-12-9 09:41:36\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"980000003464\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"250102035\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"尿常规(最后验)\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0102003\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿胆原\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[正常]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[正常]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0102004\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿胆红素\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0102005\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"酮体\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0102006\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"隐血\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"5\"/><printid value=\"5\"/><code code=\"0102007\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿蛋白\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"6\"/><printid value=\"6\"/><code code=\"0102008\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"亚硝酸盐\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"7\"/><printid value=\"7\"/><code code=\"0102009\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"白细胞\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"8\"/><printid value=\"8\"/><code code=\"0102011\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"尿比重\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[1.030]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[1.01--1.025]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"9\"/><printid value=\"9\"/><code code=\"0102012\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"酸碱度\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[5.0]]></result></observation><ABNORMALINDICATOR><code value=\"L\"/></ABNORMALINDICATOR><notetext><![CDATA[5.5--7]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"10\"/><printid value=\"10\"/><code code=\"0102013\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"维生素C\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"11\"/><printid value=\"11\"/><code code=\"0302019\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"糖\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[－]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[阴性]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"12\"/><printid value=\"12\"/><code code=\"0102401\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检红细胞\"/><value unit=\"/HP\" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"13\"/><printid value=\"13\"/><code code=\"0102402\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检白细胞\"/><value unit=\"/HP\" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"14\"/><printid value=\"14\"/><code code=\"0102403\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"镜检管型\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"\"/></ABNORMALINDICATOR><notetext><![CDATA[]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"15\"/><printid value=\"15\"/><code code=\"0102038\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"其它\"/><value unit=\" \" xsi:type=\"PQ\"/><result><![CDATA[-]]></result></observation><ABNORMALINDICATOR><code value=\"\"/></ABNORMALINDICATOR><notetext><![CDATA[]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-9 09:07:33\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><code code=\"TJ024\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"血脂四项\"/><statusCode code=\"completed\"/><entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" moodCode=\"EVN\"><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"1\"/><printid value=\"1\"/><code code=\"0301020\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"甘油三脂\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.26]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--2.3]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"2\"/><printid value=\"2\"/><code code=\"0301021\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"总胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[5.30]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--5.17]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"3\"/><printid value=\"3\"/><code code=\"0301022\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"高密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[0.93]]></result></observation><ABNORMALINDICATOR><code value=\"M\"/></ABNORMALINDICATOR><notetext><![CDATA[0.78--1.81]]></notetext></component><component><observation classCode=\"OBS\" moodCode=\"EVN\"><id value=\"4\"/><printid value=\"4\"/><code code=\"0301023\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"低密度脂蛋白胆固醇\"/><value unit=\"mmol/L\" xsi:type=\"PQ\"/><result><![CDATA[3.41]]></result></observation><ABNORMALINDICATOR><code value=\"H\"/></ABNORMALINDICATOR><notetext><![CDATA[0--3.37]]></notetext></component></organizer></entryRelationship><entryRelationship typeCode=\"COMP\"><act classCode=\"ACT\" moodCode=\"EVN\"><templateId root=\"1.3.6.1.4.1.19376.1.3.1.2\"/><code code=\"33882-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"标本采集\"/><specimen typeCode=\"SPC\"><effectiveTime value=\" \"/><specimenRole classCode=\"SPEC\"><id extension=\" \" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name/></specimenRole><addr xsi:type=\"BAG_AD\"><item use=\"WP\"><part type=\"BNR\" value=\"护士站\"/></item></addr></specimen><recimen typeCode=\"REC\"><effectiveTime value=\"2016-12-17 12:36:27\"/><recimenRole classCode=\"SPEC\"><id extension=\"\" root=\"1.3.6.1.4.1.19376.1.3.4\"/><name extension=\" \"/></recimenRole></recimen><subject contextControlCode=\"OP\" typeCode=\"SBJ\"><specimenInContainer classCode=\"CONT\"><container classCode=\"CONT\" determinerCode=\"INSTANCE\"><code code=\"0237\"/><desc value=\"试管\"/></container></specimenInContainer></subject></act></entryRelationship></act></entry></structuredBody></component></clinicalDocument>");
        //StringBuffer xmlmessage=new StringBuffer("<clinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"><authenticator><time value=\"2016-12-17 14:21:57\"/><signatureCode code=\"S\"/><assignedEntity classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>张昊</name></assignedPerson></assignedEntity></authenticator><order><id extension=\"910000004895\"/></order><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"><entryRelationship displayName=\"报告备注\" typeCode=\"01\"><observationText/></entryRelationship><entryRelationship displayName=\"技术备注\" typeCode=\"02\"><observationText/></entryRelationship><entryRelationship displayName=\"表现现象\" typeCode=\"03\"><observationText/></entryRelationship></act></entry><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component><component><structuredBody><entry typeCode=\"DRIV\"><act classCode=\"ACT\" moodCode=\"EVN\"></act></entry></structuredBody></component></clinicalDocument>");
		ResLisMessageHK rpm = new ResLisMessageHK(xmlmessage.toString(),true);
		
	}
}
