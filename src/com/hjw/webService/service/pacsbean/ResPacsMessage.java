package com.hjw.webService.service.pacsbean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.util.DateTimeUtil;


public class ResPacsMessage {
	private RetPacsCustome rc= new RetPacsCustome();
	private Document document;
	public ResPacsMessage(String xmlmessage,boolean flags) throws Exception{
		 String xmlmessagess="";
		 if(flags){
			 xmlmessagess=xmlmessage;
		}
		InputStream is = new ByteArrayInputStream(xmlmessagess.getBytes("utf-8"));
		SAXReader sax = new SAXReader();// 创建一个SAXReader对象
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		this.getCustom();//获取检查人员信息
		if("TJ012".equals(getMessNO())){
			this.getdoctor_bg_bl();
			this.getdoctor_sh();//获取审核医生
			this.getdoctor_orderid();//关联医嘱号或者清单号				
			this.getdoctor_deptcode();//关联科室编码对应类别编码
			this.getdoctor_Diagnosis();//获取阴阳性
			this.getdoctor_Item_bl();//关联检查项目	
			this.rc.setEffectiveTime(DateTimeUtil.getDateTime());
		}else{
			this.getdoctor_bg();//获取报告医生
			this.getdoctor_effectiveTime();//获取报告时间
			this.getdoctor_sh();//获取审核医生
			this.getdoctor_orderid();//关联医嘱号或者清单号				
			this.getdoctor_deptcode();//关联科室编码对应类别编码
			this.getdoctor_Diagnosis();//获取阴阳性
			this.getdoctor_Item();//关联检查项目	
		}
		
		
	}

	public RetPacsCustome getRetPacsCustome(){
		return this.rc;
	}
	
	
	
	
    private String getMessNO(){
    	String tjno= document.selectSingleNode("/ClinicalDocument/id/@extension").getText();// 获取根节点
    	 return  tjno;
	}
	
	/**
	 * 
	     * @Title: getCustom   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @return
	     * @param: @throws Exception      
	     * @return: String      
	     * @throws
	 */
	public void getCustom() throws Exception {		
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
	     * @Title: getdoctor_bg   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @return
	     * @param: @throws Exception      
	     * @return: String      
	     * @throws
	 */
	public void getdoctor_bg() throws Exception {		
		String time = document.selectSingleNode("/ClinicalDocument/author/time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("/ClinicalDocument/author/assignedAuthor/assignedPerson/name").getText();// 获取根节点
        this.rc.setDoctor_name_bg(name);
        this.rc.setDoctor_time_bg(time);
		//getNodes_doctor_bg(root, "author",flags);// 从根节点开始遍历所有节点
	}
	
	public void getdoctor_bg_bl() throws Exception {		
		String time = document.selectSingleNode("/ClinicalDocument/author/time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("/ClinicalDocument/authenticator/assignedEntity/assignedPerson/name").getText();// 获取根节点
        this.rc.setDoctor_name_bg(name);
        this.rc.setDoctor_time_bg(time);
		//getNodes_doctor_bg(root, "author",flags);// 从根节点开始遍历所有节点
	}
	
	/**
	 * 
	     * @Title: getdoctor_sh   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @return
	     * @param: @throws Exception      
	     * @return: String      
	     * @throws
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
		String time = document.selectSingleNode("/ClinicalDocument/req/id/@extension").getText();// 获取根节点
        this.rc.setPacs_summary_id(time);
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
		String dept_code = document.selectSingleNode("/ClinicalDocument/component/structuredBody/component/section/entry/organizer/code/@code").getText();// 获取根节点
		String dept_name = document.selectSingleNode("/ClinicalDocument/component/structuredBody/component/section/entry/organizer/code/@displayName").getText();// 获取根节点
        this.rc.setDept_code(dept_code);
        this.rc.setDept_name(dept_name);
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
	public void getdoctor_Diagnosis() throws Exception {
		Node yyang = document.selectSingleNode("/ClinicalDocument/component/structuredBody/component/section/entry/act/entryRelationship/observation/value");// 获取根节点
		List<Element> listElement = yyang.getParent().elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点				
			List<Attribute> listAttr = e.attributes();// 当前节点的所有属性的list
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				String name = attr.getName();// 属性名称
				String value = attr.getValue();// 属性的值
				//System.out.println("属性名称：" + name + "属性值：" + value);
				if ("yyang".equals(value.trim().toLowerCase())) {		
					 for (Attribute attrnew : listAttr) {// 遍历当前节点的所有属性
							String namenew = attrnew.getName();// 属性名称
							String valuenew = attrnew.getValue();// 属性的值
							if ("displayname".equals(namenew.trim().toLowerCase())){
							  this.rc.setNpositive(valuenew);		
								break;
							}
					}
				}
			}
		}
	}
	
	public void getdoctor_Item_bl() throws Exception {
		Node Items = document.selectSingleNode("/ClinicalDocument/component/structuredBody/component/section/entry/organizer/component/observation");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_Item_bl(listElement);
	}
	
	@SuppressWarnings("unchecked")
	private void getNodes_doctor_Item_bl(List<Element> listElement) throws Exception {		
		List<RetPacsItem> rpi=new ArrayList<RetPacsItem>();
		for(Element e:listElement){
			RetPacsItem reti= new RetPacsItem();
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("/observation/code/@code").getText();
			reti.setChargingItem_num(Items_code);
			String Itemsitem = documentItem.selectSingleNode("/observation/value[@code='01']/originalText").getText();
			reti.setChargingItem_ms(Itemsitem);
			String Itemsitemjl = documentItem.selectSingleNode("/observation/value[@code='02']/originalText").getText();
			reti.setChargingItem_jl(Itemsitemjl);
			
			String  Items_base = documentItem.selectSingleNode("/observation/entryRelationship/observationMedia/value").getText();
			reti.setBase64_bg(Items_base);
			rpi.add(reti);
		}
		this.rc.setList(rpi);
	}


	/**
	 * 
	     * @Title: getdoctor_Item   
	     * @Description: TODO(这里用一句话描述这个方法的作用)   
	     * @param: @return
	     * @param: @throws Exception      
	     * @return: String      
	     * @throws
	 */
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("/ClinicalDocument/component/structuredBody/component/section/entry/organizer/component/observation");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_Item(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_Item(List<Element> listElement) throws Exception {		
		List<RetPacsItem> rpi=new ArrayList<RetPacsItem>();
		for(Element e:listElement){
			RetPacsItem reti= new RetPacsItem();
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("/observation/code/@code").getText();
			reti.setChargingItem_num(Items_code);
			String Itemsitem = documentItem.selectSingleNode("/observation/value[@code='01']/originalText").getText();
			reti.setChargingItem_ms(Itemsitem);
			String Itemsitemjl = documentItem.selectSingleNode("/observation/value[@code='02']/originalText").getText();
			reti.setChargingItem_jl(Itemsitemjl);
			
			String  Items_base = documentItem.selectSingleNode("/observation/entryRelationship[@typeCode='SPRT']/observationMedia/value").getText();
			reti.setBase64_bg(Items_base);
			rpi.add(reti);
		}
		this.rc.setList(rpi);
	}

	public static void main(String[] args)throws Exception  {
		String str = "<ClinicalDocument><id extension=\"TJ012\"/><creationTime value=\"20180515160550\"/>"
				+ "<interactionId extension=\"POOR_IN200901UV21\" root=\"\"/><processingCode code=\"P\"/>"
				+ "<processingModeCode code=\"T\"/><acceptAckCode code=\"NE\"/><receiver typeCode=\"RCV\">"
				+ "<device classCode=\"DEV\" determinerCode=\"INSTANCE\"><id><item extension=\"\" root=\"\"/>"
				+ "</id></device></receiver><sender typeCode=\"SND\"><device classCode=\"DEV\" determinerCode=\"INSTANCE\">"
				+ "<id><item extension=\"\" root=\"\"/></id></device></sender><title>病理科检查报告</title><recordTarget typeCode=\"RCT\">"
				+ "<patientRole classCode=\"PAT\"><id extension=\"1\" root=\"1.2.156.112649.1.2.1.2\"/>"
				+ "<id extension=\"T185150121\" root=\"1.2.156.112649.1.2.1.3\"/><id extension=\"0\" root=\"1.2.156.112649.1.2.1.12\"/>"
				+ "<id extension=\"P004313\" root=\"1.2.156.112649.1.2.1.5\"/><id extension=\"\" root=\"1.2.156.112649.1.2.1.7\"/>"
				+ "<item extension=\"\" root=\"exam_num\"/><patient classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>病理测试4</name>"
				+ "<administrativeGenderCode code=\"\" codeSystem=\"1.2.156.112649.1.1.3\" displayName=\"2\"/>"
				+ "<birthTime value=\"19840515\"/></patient></patientRole></recordTarget>"
				+ "<author typeCode=\"AUT\">"
				+ "<time value=\"20180515162948\">"
				+ "<assignedAuthor classCode=\"ASSIGNED\">"
				+ "<id extension=\"\" root=\"1.2.156.112649.1.1.2\"/>"
				+ "<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">"
				+ "<name></name>"
				+ "</assignedPerson>"
				+ "</assignedAuthor>"
				+ "</time>"
				+ "</author><custodian><assignedCustodian><representedCustodianOrganization><id extension=\"44643245-7\" "
				+ "root=\"1.2.156.112649\"/><name>东北国际医院</name></representedCustodianOrganization></assignedCustodian></custodian>"
				+ "<authenticator>"
				+ "<time value=\"20180515162948\"/>"
				+ "<assignedEntity classCode=\"ASSIGNED\">"
				+ "<id extension=\"\" root=\"1.2.156.112649.1.1.2\"/>"
				+ "  <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">"
				+ "<name>孟繁龄</name>"
				+ "</assignedPerson>"
				+ "</assignedEntity>"
				+ "</authenticator>"
				+ "<participant typeCode=\"AUT\"><associatedEntity "
				+ "classCode=\"ASSIGNED\"><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/><assignedPerson><name>admin</name>"
				+ "</assignedPerson></associatedEntity></participant><inFullfillmentOf><order><id extension=\"18051500548\"/>"
				+ "<Device code=\"\" extension=\"\"/></order></inFullfillmentOf><req><id extension=\"18051500548\"/>"
				+ "<Device extension=\"\"/></req><component><structuredBody><component><section><code code=\"29308-4\" "
				+ "codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Diagnosis\"/><title>诊断</title>"
				+ "<entry typeCode=\"DRIV\"><act classCode=\"ACT\" modeCode=\"EVN\"><entryRelationship typeCode=\"SUBJ\">"
				+ "<observation classCode=\"OBS\" modeCode=\"EVN\"><code code=\"\" codeSystem=\"1.2.156.112649.1.1.29\" displayName=\"\"/>"
				+ "<value code=\"\" codeSystem=\"1.2.156.112649.1.1.30\" displayName=\"急性阑尾炎。\"/><value code=\"YYANG\" displayName=\"\"/>"
				+ "</observation></entryRelationship></act></entry></section><section><code code=\"30954-2\" codeSystem=\"2.16.840.1.113883.6.1\" "
				+ "codeSystemName=\"LOINC\" displayName=\"STUDIES SUMMARY\"/><title>检查</title><entry typeCode=\"DRIV\">"
				+ "<organizer classCode=\"BATTERY\" modeCode=\"EVN\"><code code=\"2\" codeSystem=\"1.2.156.112649.1.1.41\" "
				+ "displayName=\"USCC\"/><component typeCode=\"COMP\"><observation classCode=\"OBS\" modeCode=\"EVN\">"
				+ "<code code=\"\" codeSystem=\"1.2.156.112649.1.1.88\" displayName=\"\"/><text/><value code=\"01\" "
				+ "codeSystem=\"1.2.156.112649.1.1.98\"><originalText>sdfsdfsdfsdfsdfsdfsdfsdsd</originalText></value>"
				+ "<value code=\"02\" codeSystem=\"1.2.156.112649.1.1.98\">"
				+ "<originalText>ddddddd</originalText></value><methodCode code=\"01\" "
				+ "codeSystem=\"1.2.156.112649.1.1.43\" displayName=\"\"/><targetSiteCode code=\"\" codeSystem=\"1.2.156.112649.1.1.42\" "
				+ "displayName=\"\"/><performer><time value=\"2018-05-15000000\"/><assignedEntity><id extension=\"\" root=\"1.2.156.112649.1.1.2\"/>"
				+ "<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"><name>测试1</name></assignedPerson>"
				+ "<represontedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"><id extension=\"\" "
				+ "root=\"1.2.156.112649.1.1.1\"/><name/></represontedOrganization></assignedEntity></performer>"
				+ "<entryRelationship typeCode=\"COMP\"><organizer classCode=\"BATTERY\" modeCode=\"EVN\"><component>"
				+ "<observation classCode=\"OBS\" modeCode=\"EVN\"><code code=\"200\" displayName=\"\"/><value/></observation>"
				+ "</component></organizer></entryRelationship><entryRelationship><observationMedia classCode=\"OBS\" modeCode=\"EVN\">"
				+ "<value>http://192.168.111.46:1501/ReportShow/P/PIS/T185150121/0/18051500548/T185150121_0_18051500548_P004313.pdf</value>"
				+ "</observationMedia></entryRelationship></observation></component></organizer></entry></section></component>"
				+ "</structuredBody></component><component typeCode=\"COMP\"><observation classCode=\"OBS\" modeCode=\"EVN\">"
				+ "<code code=\"01\" codeSystem=\"1.2.156.112649.1.1.88\" displayName=\"\"/><text/><value code=\"01\" "
				+ "codeSystem=\"1.2.156.112649.1.1.98\"><originalText>sdfsdfsdfsdfsdfsdfsdfsdsd</originalText></value>"
				+ "<value code=\"02\" codeSystem=\"1.2.156.112649.1.1.98\">"
				+ "<originalText>sssssssssssssssssssssssssssssddddddd</originalText></value><entryRelationship typeCode=\"SPRT\">"
				+ "<observationMedia classCode=\"OBS\" modeCode=\"EVN\"><value mediaType=\"application/pdf\"/></observationMedia>"
				+ "</entryRelationship></observation></component></ClinicalDocument>";
		
		try{
			new ResPacsMessage(str,true);
			System.out.print("12");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
}
