package com.hjw.webService.client.hokai305.bean;

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
import com.hjw.wst.DTO.sysSurveyQuestionRelationDTO;

public class ResLisMessageHK305 {
	public RetLisCustome rc = new RetLisCustome();
	private Document document;
	public ResLisMessageHK305(String xmlmessage,boolean flags) throws Exception{
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
		 this.getdoctor_orderid();// 关联医嘱号或者清单号
//		 this.getdoctor_deptcode();// 关联科室编码对应类别编码
		 this.getdoctor_Item();// 关联检查项目
		 this.getpatient_id();
	}

	/**
	 * 
	 * @Title: getdoctor_bg @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_bg() throws Exception {
		
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		//String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name/abc:item/abc:part/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:author/@displayName").getText();// 获取根节点
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
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:author/@displayName").getText();// 获取根节点
		/*String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:assignedEntity/abc:assignedPerson/abc:name").getText();// 获取根节点
*/        this.rc.setDoctor_name_sh(name);
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
	public void getpatient_id() throws Exception {
		String patient_id = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText();// 获取根节点
		this.rc.setPatient_id(patient_id);
	}
	
	public void getdoctor_orderid() throws Exception {
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:observation/abc:entryRelationship/abc:procedure/abc:specimen/abc:id/@extension").getText();// 获取根节点
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
		Node Items = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:observation/abc:entryRelationship/abc:organizer");// 获取根节点///abc:section/abc:entry/abc:observation/abc:entryRelationship/abc:organizer
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
			
			//System.err.println("organize===="+Items.asXML());
			//System.err.println("Items.getParent()==="+Items.getParent().asXML());
			List<Element> listItemElement = Items.getParent().elements();
			//List<Element> listItemElement = Items.getParent().elements();// 所有一级子节点的list
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
			//System.out.println("当前节点"+e.asXML());
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Map<String, String> xmlMap = new HashMap<>();
	 		xmlMap.put("abc", "urn:hl7-org:v3");
	 		saxitem.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		//	String  Items_codexx = documentItem.selectSingleNode("abc:component/abc:sequenceNumber/@value").getText();
		//	String  Items_codess = documentItem.selectSingleNode("abc:component/abc:observation/abc:code/@displayName").getText();
			String asXML = documentItem.asXML();
			//System.err.println("=========================="+asXML);
			
			if(asXML.contains("component")){
				
				String  Items_code = documentItem.selectSingleNode("abc:component/abc:observation/abc:code/@code").getText();
				String  item_name = documentItem.selectSingleNode("abc:component/abc:observation/abc:code/@displayName").getText();
				
				retlisch.setItem_id(Items_code);
				retlisch.setItem_name(item_name);
				try{
				retlisch.setValues(documentItem.selectSingleNode("abc:component/abc:observation/abc:value/@value").getText());
				}catch(Exception ex){
					try{
						retlisch.setValues(documentItem.selectSingleNode("abc:component/abc:observation/abc:value").getText());
						}catch(Exception ex1){
						}
				}
				retlisch.setValues_dw(documentItem.selectSingleNode("abc:component/abc:observation/abc:value/@unit").getText());
				try{			//[@code='2.16.840.1.113883.7.1']/abc:translation
					String bz = documentItem.selectSingleNode("abc:component/abc:observation/abc:interpretationCode/@code").getText();
					if(bz!=null && bz.trim().length()>0){
						retlisch.setValue_ycbz("0");
						retlisch.setValue_ycbz(documentItem.selectSingleNode("abc:component/abc:observation/abc:interpretationCode/@code").getText());
					}else{
						retlisch.setValue_ycbz("0");
					}
				}catch(Exception ex){}
				try{//[@code='2.16.840.1.113883.7.2']/abc:translation/
					String value_ycbz=documentItem.selectSingleNode("abc:component/abc:observation/abc:interpretationCode@code").getText();
					System.err.println(retlisch.getValue_ycbz());
					if(value_ycbz!=null && value_ycbz.trim().length()>0){
						retlisch.setValue_ycbz(value_ycbz);
						System.err.println(retlisch.getValue_ycbz());
					}else{
						retlisch.setValue_ycbz("0");
					}
					
				}catch(Exception ex){}
				try{///abc:observationRange/abc:value   ///abc:observationRange/abc:value
				String fwtext = documentItem.selectSingleNode("abc:component/abc:observation/abc:text").getText();
				
				/*boolean contains = fwtext.contains("<");
				String fwvaluel="";
				String fwdwl="";
				String fwvalueh="";
				String fwdwh="";*/
				/*if(contains){
					String[] splitFW = fwtext.split("<");
					fwvaluel="0";
					fwvalueh=splitFW[1];
					
					
					 fwdwl = documentItem.selectSingleNode("abc:component/abc:observation/abc:value/@unit").getText();///abc:observationRange/abc:value
					 fwdwh = documentItem.selectSingleNode("abc:component/abc:observation/abc:value/@unit").getText();///abc:observationRange/abc:value
					 System.err.println(fwvaluel);
				}else{
					String[] splitfw = fwtext.split("--");
					fwvaluel=splitfw[0];
					fwvalueh=splitfw[1];
				    fwdwl = documentItem.selectSingleNode("abc:component/abc:observation/abc:referenceRange/@unit").getText();///abc:observationRange/abc:value
				    fwdwh = documentItem.selectSingleNode("abc:component/abc:observation/abc:referenceRange/@unit").getText();///abc:observationRange/abc:value
				}*/
				
				//retlisch.setValue_fw(fwvaluel+fwdwl+"-"+fwvalueh+fwdwl);
				retlisch.setValue_fw(fwtext);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				  //retlisch.setValue_ycbz(documentItem.selectSingleNode("/component/ABNORMALINDICATOR/code/@value").getText());
				listRetLisItem.add(retlisch);	
			}
			
			
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return listRetLisItem;
	}

	/*public static void main(String[] args) throws Exception {
		
		StringBuffer sb=new StringBuffer();
		sb.append(" <RCMR_IN000002UV02 xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\">  ");
		sb.append(" 	<id extension=\"96FE15B1-EE98-47AB-BCFA-800E29E44654\"/>                                                                 ");
		sb.append(" 	<creationTime value=\"20191022172722\"/>                                                                                 ");
		sb.append(" 	<interactionId extension=\"S0065\"/>                                                                                     ");
		sb.append(" 	<receiver code=\"SYS009\"/>                                                                                              ");
		sb.append(" 	<sender code=\"SYS001\"/>                                                                                                ");
		sb.append(" 	<controlActProcess classCode=\"STC\" moodCode=\"APT\">                                                                   ");
		sb.append(" 		<code value=\"create\"/>                                                                                             ");
		sb.append(" 		<subject typeCode=\"SUBJ\">                                                                                          ");
		sb.append(" 			<clinicalDocument classCode=\"DOCCLIN\" moodCode=\"EVN\">                                                        ");
		sb.append(" 				<id>                                                                                                         ");
		sb.append(" 					<item extension=\"87\" root=\"2.16.156.10011.1.1\"/>                                                     ");
		sb.append(" 				</id>                                                                                                        ");
		sb.append(" 				<title>检验报告</title>                                                                                      ");
		sb.append(" 				<effectiveTime value=\"20191022172721\"/>                                                                    ");
		sb.append(" 				<recordTarget typeCode=\"RCT\">                                                                              ");
		sb.append(" 					<patient classCode=\"PAT\">                                                                              ");
		sb.append(" 						<id>                                                                                                 ");
		sb.append(" 							<item extension=\"D100631\" root=\"2.16.156.10011.0.2.2\"/>                                      ");
		sb.append(" 						</id>                                                                                                ");
		sb.append(" 						<patientType code=\"1\" displayName=\"门诊\"/>                                                       ");
		sb.append(" 					</patient>                                                                                               ");
		sb.append(" 				</recordTarget>                                                                                              ");
		sb.append(" 				<author>                                                                                                     ");
		sb.append(" 					<time value=\"20191022172721\"/>                                                                         ");
		sb.append(" 					<author code=\"\" displayName=\"Admin\"/>                                                                ");
		sb.append(" 				</author>                                                                                                    ");
		sb.append(" 				<custodian>                                                                                                  ");
		sb.append(" 					<id>                                                                                                     ");
		sb.append(" 						<item extension=\"PLA305\" root=\"2.16.156.10011.1.5\"/>                                             ");
		sb.append(" 					</id>                                                                                                    ");
		sb.append(" 					<name>解放军第305医院</name>                                                                             ");
		sb.append(" 				</custodian>                                                                                                 ");
		sb.append(" 				<legalAuthenticator>                                                                                         ");
		sb.append(" 					<time value=\"20191022172721\"/>                                                                         ");
		sb.append(" 					<author code=\"\" displayName=\"Admin\"/>                                                                ");
		sb.append(" 				</legalAuthenticator>                                                                                        ");
		sb.append(" 				<authenticator>                                                                                              ");
		sb.append(" 					<time value=\"20191022\"/>                                                                               ");
		sb.append(" 					<author code=\"\" displayName=\"Admin\"/>                                                                ");
		sb.append(" 				</authenticator>                                                                                             ");
		sb.append(" 				<participant>                                                                                                ");
		sb.append(" 					<time value=\"20191021\"/>                                                                               ");
		sb.append(" 					<location code=\"4115\" displayName=\"体检中心\">                                                        ");
		sb.append(" 						<organization code=\"PLA305\" displayName=\"解放军第305医院\"/>                                      ");
		sb.append(" 					</location>                                                                                              ");
		sb.append(" 				</participant>                                                                                               ");
		sb.append(" 				<component>                                                                                                  ");
		sb.append(" 					<structuredBody classCode=\"DOCBODY\" moodCode=\"EVN\">                                                  ");
		sb.append(" 						<component code=\"30952-2\">                                                                         ");
		sb.append(" 							<section>                                                                                        ");
		sb.append(" 								<title>实验室检查</title>                                                                    ");
		sb.append(" 								<text/>                                                                                      ");
		sb.append(" 								<entry typeCode=\"DRIV\">                                                                    ");
		sb.append(" 									<id extension=\"1910213381\"/>                                                           ");
		sb.append(" 									<effectiveTime value=\"20191022172131\"/>                                                ");
		sb.append(" 									<typeCode code=\"185\" displayName=\"检验类别名称\"/>                                    ");
		sb.append(" 									<methodCode code=\"002\" displayName=\"患者接受医学检查项目所采用的检验方法名称\"/>      ");
		sb.append(" 									<observation classCode=\"OBS\" moodCode=\"EVN\">                                         ");
		sb.append(" 										<entryRelationship typeCode=\"COMP\">                                                ");
		sb.append(" 											<organizer>                                                                      ");
		sb.append(" 												<text/>                                                                      ");
		sb.append(" 												<code code=\"1\" displayName=\"1\"/>                                         ");
		sb.append(" 												<statusCode code=\"9\" displayName=\"报告完成\"/>                            ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"1\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"PT-RP\" displayName=\"凝血酶原时间\">                   ");
		sb.append(" 															<originalText>凝血酶原时间</originalText>                        ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"sec\" value=\"5\"/>                                    ");
		sb.append(" 														<text>8.5--14.5</text>                                               ");
		sb.append(" 														<referenceRange unit=\"sec\">                                        ");
		sb.append(" 															<low value=\"8.5\"/>                                             ");
		sb.append(" 															<high value=\"14.5\"/>                                           ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"L\" displayName=\"L\"/>                   ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"2\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"PTa\" displayName=\"凝血酶原活动度\">                   ");
		sb.append(" 															<originalText>凝血酶原活动度</originalText>                      ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"%\" value=\"160\"/>                                    ");
		sb.append(" 														<text>70--150</text>                                                 ");
		sb.append(" 														<referenceRange unit=\"%\">                                          ");
		sb.append(" 															<low value=\"70\"/>                                              ");
		sb.append(" 															<high value=\"150\"/>                                            ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"H\" displayName=\"H\"/>                   ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"3\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"INR\" displayName=\"凝血酶原国际标准比值\">             ");
		sb.append(" 															<originalText>凝血酶原国际标准比值</originalText>                ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"\" value=\"2.3\"/>                                     ");
		sb.append(" 														<text>0.75--1.25</text>                                              ");
		sb.append(" 														<referenceRange unit=\"\">                                           ");
		sb.append(" 															<low value=\"0.75\"/>                                            ");
		sb.append(" 															<high value=\"1.25\"/>                                           ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"H\" displayName=\"H\"/>                   ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"4\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"APTT-SS\" displayName=\"活化部分凝血活酶时间\">         ");
		sb.append(" 															<originalText>活化部分凝血活酶时间</originalText>                ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"s\" value=\"35\"/>                                     ");
		sb.append(" 														<text>25--45</text>                                                  ");
		sb.append(" 														<referenceRange unit=\"s\">                                          ");
		sb.append(" 															<low value=\"25\"/>                                              ");
		sb.append(" 															<high value=\"45\"/>                                             ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"\" displayName=\"\"/>                     ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"5\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"TT\" displayName=\"凝血酶时间\">                        ");
		sb.append(" 															<originalText>凝血酶时间</originalText>                          ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"s\" value=\"15\"/>                                     ");
		sb.append(" 														<text>12--18</text>                                                  ");
		sb.append(" 														<referenceRange unit=\"s\">                                          ");
		sb.append(" 															<low value=\"12\"/>                                              ");
		sb.append(" 															<high value=\"18\"/>                                             ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"\" displayName=\"\"/>                     ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"6\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"FIB-C\" displayName=\"血浆纤维蛋白原\">                 ");
		sb.append(" 															<originalText>血浆纤维蛋白原</originalText>                      ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"mg/dL\" value=\"100\"/>                                ");
		sb.append(" 														<text>150--450</text>                                                ");
		sb.append(" 														<referenceRange unit=\"mg/dL\">                                      ");
		sb.append(" 															<low value=\"150\"/>                                             ");
		sb.append(" 															<high value=\"450\"/>                                            ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"L\" displayName=\"L\"/>                   ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"7\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"FDP\" displayName=\"纤维蛋白降解产物\">                 ");
		sb.append(" 															<originalText>纤维蛋白降解产物</originalText>                    ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"mg/L\" value=\"6\"/>                                   ");
		sb.append(" 														<text>&lt;5</text>                                                   ");
		sb.append(" 														<interpretationCode code=\"H\" displayName=\"H\"/>                   ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"8\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"D-POLY\" displayName=\"血浆D-二聚体定量\">              ");
		sb.append(" 															<originalText>血浆D-二聚体定量</originalText>                    ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"ng/mL\" value=\"400\"/>                                ");
		sb.append(" 														<text>0--500</text>                                                  ");
		sb.append(" 														<referenceRange unit=\"ng/mL\">                                      ");
		sb.append(" 															<low value=\"0\"/>                                               ");
		sb.append(" 															<high value=\"500\"/>                                            ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"\" displayName=\"\"/>                     ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 												<component>                                                                  ");
		sb.append(" 													<sequenceNumber value=\"9\"/>                                            ");
		sb.append(" 													<observation>                                                            ");
		sb.append(" 														<code code=\"ATIII\" displayName=\"抗凝血酶III\">                    ");
		sb.append(" 															<originalText>抗凝血酶III</originalText>                         ");
		sb.append(" 														</code>                                                              ");
		sb.append(" 														<value unit=\"%\" value=\"70.0\"/>                                   ");
		sb.append(" 														<text>83--128</text>                                                 ");
		sb.append(" 														<referenceRange unit=\"%\">                                          ");
		sb.append(" 															<low value=\"83\"/>                                              ");
		sb.append(" 															<high value=\"128\"/>                                            ");
		sb.append(" 														</referenceRange>                                                    ");
		sb.append(" 														<interpretationCode code=\"L\" displayName=\"L\"/>                   ");
		sb.append(" 														<deviceCode code=\"1\" displayName=\"1号机器\"/>                     ");
		sb.append(" 													</observation>                                                           ");
		sb.append(" 												</component>                                                                 ");
		sb.append(" 											</organizer>                                                                     ");
		sb.append(" 											<procedure>                                                                      ");
		sb.append(" 												<performer>                                                                  ");
		sb.append(" 													<time value=\"\"/>                                                       ");
		sb.append(" 												</performer>                                                                 ");
		sb.append(" 												<specimen>                                                                   ");
		sb.append(" 													<id extension=\"8191021002\"/>                                           ");
		sb.append(" 													<code code=\"\" displayName=\"静脉血\"/>                                 ");
		sb.append(" 												</specimen>                                                                  ");
		sb.append(" 											</procedure>                                                                     ");
		sb.append(" 										</entryRelationship>                                                                 ");
		sb.append(" 									</observation>                                                                           ");
		sb.append(" 								</entry>                                                                                     ");
		sb.append(" 							</section>                                                                                       ");
		sb.append(" 						</component>                                                                                         ");
		sb.append(" 					</structuredBody>                                                                                        ");
		sb.append(" 				</component>                                                                                                 ");
		sb.append(" 			</clinicalDocument>                                                                                              ");
		sb.append(" 		</subject>                                                                                                           ");
		sb.append(" 	</controlActProcess>                                                                                                     ");
		sb.append(" </RCMR_IN000002UV02>                                                                                                         ");
		ResLisMessageHK305 rpm = new ResLisMessageHK305(sb.toString(),true);
		
	}*/
}
