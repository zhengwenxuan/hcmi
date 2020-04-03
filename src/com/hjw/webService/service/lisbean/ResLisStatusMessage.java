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

public class ResLisStatusMessage {
	public RetLisStatusCustome rc = new RetLisStatusCustome();
	private Document document;
	public ResLisStatusMessage(String xmlmessage,boolean flags) throws Exception{
		String xmlmess="";
		if(flags){
			xmlmess=xmlmessage;
		}else{
			xmlmess= getXml_test();
		}
		 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
         SAXReader sax = new SAXReader();
		 this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 this.getCustom();// 获取检查人员信息
		 this.getdoctor_effectiveTime();
		 this.getdoctor_Item();// 关联医嘱号或者清单号
	}
	

	private String getXml_test() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<POOR_IN200901UV>");
		sb.append("<!-- 消息ID -->");
		sb.append("<id extension=\"TJ023\" />");
		sb.append("<!-- 消息创建时间 -->");
		sb.append("<creationTime value=\"20120106110000\" />");
		sb.append("<!-- 交互ID -->");
		sb.append("<interactionId extension=\"POOR_IN200901UV23\" />");
		sb.append("<!--消息用途: P(Production); D(Debugging); T(Training) -->");
		sb.append("<processingCode code=\"P\" />");
		sb.append("<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) -->");
		sb.append("<processingModeCode code=\"R\" />");
		sb.append("<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->");
		sb.append("<acceptAckCode code=\"NE\" />");
		sb.append("<!-- 接受者 -->");
		sb.append("<receiver typeCode=\"RCV\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		sb.append("<!-- 接受者ID -->");
		sb.append("<id>");
		sb.append("<item root=\"\" extension=\"\"></item>");
		sb.append("</id>");
		sb.append("</device>");
		sb.append("</receiver>");
		sb.append("<!-- 发送者 -->");
		sb.append("<sender typeCode=\"SND\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		sb.append("<!-- 发送者ID -->");
		sb.append("<id>");
		sb.append("<item root=\"\" extension=\"\"></item>");
		sb.append("</id>");
		sb.append("</device>");
		sb.append("</sender>");
		sb.append("<!-- 封装的消息内容(按Excel填写) -->");
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">");
		sb.append("<!-- 消息交互类型 @code: 新增 :new 修改:update -->");
		sb.append("<code code=\"update\"></code>");
		sb.append("<!-- 检查确认时间 -->");
		sb.append("<effectiveTime value=\"20060110112211\" />");
		sb.append("<subject typeCode=\"SUBJ\" nil=\"false\">");
		sb.append("<placerGroup>");
		sb.append("<!-- 患者信息 -->");
		sb.append("<subject typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\">");
		sb.append("<id>");
		sb.append("<!--域ID -->");
		sb.append("<item root=\"1.2.156.112606.1.2.1.2\" extension=\"01\" />");
		sb.append("<!-- 患者ID -->");
		sb.append("<item root=\"1.2.156.112606.1.2.1.3\" extension=\"001407878200\" />");
		sb.append("<!-- 就诊次数 -->");
		sb.append("<item root=\"1.2.156.112606.1.2.1.7\" extension=\"2\" />");
		sb.append("<!-- 就诊号 -->");
		sb.append("<item root=\"1.2.156.112606.1.2.1.12\" extension=\"14327643\" />");
		sb.append("<!-- 体检编号 -->");
		sb.append("<item root=\"exam_num\" extension=\"14327643\" />");
		sb.append("</id>");
		sb.append("</patient>");
		sb.append("</subject>");
		sb.append("<!-- 操作人 -->");
		sb.append("<performer typeCode=\"PRF\">");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		sb.append("<!-- 操作人编码 -->");
		sb.append("<id>");
		sb.append("<item extension=\"01003\" root=\"1.2.156.112606.1.1.2\"></item>");
		sb.append("</id>");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\"	classCode=\"PSN\">");
		sb.append("<!-- 操作人姓名 必须项已使用 -->");
		sb.append("<name type=\"BAG_EN\">");
		sb.append("<item use=\"ABC\">");
		sb.append("<part value=\"积显\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</performer>");
		sb.append("<!-- 病人科室信息 -->");
		sb.append("<representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		sb.append("<!--病人科室编码 必须项已使用 -->");
		sb.append("<id>");
		sb.append("<item extension=\"023984\" root=\"1.2.156.112606.1.1.1\" />");
		sb.append("</id>");
		sb.append("<!--病人科室名称 -->");
		sb.append("<name type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"骨科\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</representedOrganization>");
		sb.append("<!--医疗机构 -->");
		sb.append("<custodian>");
		sb.append("<assignedCustodian>");
		sb.append("<representedCustodianOrganization>");
		sb.append("<id root=\"1.2.156.112649\" extension=\"44643245-7\"/>");
		sb.append("<name>东北国际医院</name>");
		sb.append("</representedCustodianOrganization>");
		sb.append("</assignedCustodian>");
		sb.append(" </custodian>");
		sb.append("<!--执行科室 -->");
		sb.append("<location typeCode=\"LOC\" nil=\"false\">");
		sb.append("<!--就诊机构/科室 -->");
		sb.append("<serviceDeliveryLocation classCode=\"SDLOC\">");
		sb.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		sb.append("<!--执行科室编码 -->");
		sb.append("<id>");
		sb.append("<item extension=\"1010700\" root=\"1.21.156.112606.1.1.1\" />");
		sb.append("</id>");
		sb.append("<!--执行科室名称 -->");
		sb.append("<name type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"检验室\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</serviceProviderOrganization>");
		sb.append("</serviceDeliveryLocation>");
		sb.append("</location>");
		sb.append("<!-- 检验执行状态 -->");
		sb.append("<component1 contextConductionInd=\"true\">");
		sb.append("<processStep classCode=\"PROC\">");
		sb.append("<code code=\"01\" codeSystem=\"1.2.156.112606.1.1.93\">");
		sb.append("<!--检验执行状态名称 -->");
		sb.append("<displayName value=\"退检\" />");
		sb.append("</code>");
		sb.append("</processStep>");
		sb.append("</component1>");
		sb.append("<!-- 一个申请单可以包含多个检验，每个项目对应一个component2,多个项目重复component2 -->");
		sb.append("<component2>");
		sb.append("<observationRequest classCode=\"OBS\">");
		sb.append("<!-- 检验申请单号 -->");
		sb.append("<id>");
		sb.append("<item extension=\"293847547\" />");
		sb.append("</id>");
		sb.append("</observationRequest>");
		sb.append("</component2>");
		sb.append("</placerGroup>");
		sb.append("</subject>");
		sb.append("</controlActProcess>");
		sb.append("</POOR_IN200901UV>");
		return sb.toString();
	}

	/**
	 * 
	 * @Title: getCustom @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getCustom() throws Exception {
		//System.out.println(document.asXML());
		Node root = document.selectSingleNode("/POOR_IN200901UV/controlActProcess/subject/placerGroup/subject/patient/id/item");  
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
	     * @Title: getdoctor_orderid   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @return
	     * @param: @throws Exception      
	     * @return: String      
	     * @throws
	 */
	public void getdoctor_effectiveTime() throws Exception {
		String time = document.selectSingleNode("/POOR_IN200901UV/controlActProcess/effectiveTime/@value").getText();// 获取根节点
        this.rc.setEffectiveTime(time);
        
        String status = document.selectSingleNode("/POOR_IN200901UV/controlActProcess/subject/placerGroup/component1/processStep/code/@code").getText();// 获取根节点
        this.rc.setStatus(status);
	}

	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("/POOR_IN200901UV/controlActProcess/subject/placerGroup/component2/observationRequest");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_coms(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_coms(List<Element> listElement)  throws Exception {	
		List<String> rtlischarge=new ArrayList<String>();// 收费项目 
		for(Element e:listElement){
			//System.out.println(e.asXML());
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("/observationRequest/id/item/@extension").getText();
			rtlischarge.add(Items_code);
		}
		this.rc.setSample_barcode(rtlischarge);
	}

	public static void main(String[] args) throws Exception {
		ResLisStatusMessage rpm = new ResLisStatusMessage("",false);
		  System.out.print("12");
		
	}

}
