package com.hjw.webService.client.hokai305.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;


public class ResPacsMessageHK305 {
	private RetPacsCustome rc= new RetPacsCustome();
	private Document document;
	public ResPacsMessageHK305(String xmlmessage,boolean flags) throws Exception{
		 String xmlmessagess="";
		 if(flags){
			 xmlmessagess=xmlmessage;
		}
		InputStream is = new ByteArrayInputStream(xmlmessagess.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
			this.getCustom();//获取检查人员信息
		
			this.getdoctor_bg();//获取报告医生
//			this.getdoctor_effectiveTime();//获取报告时间
			this.getdoctor_sh();//获取审核医生
			this.getdoctor_orderid();//关联医嘱号或者清单号				
			//this.getdoctor_deptcode();//关联科室编码对应类别编码
			this.getNodes_doctor_Item();//获取结果
	
	}

	public RetPacsCustome getRetPacsCustome(){
		return this.rc;
	}
	
	
	
	
    private String getMessNO(){
    	String tjno= document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:checkAppId/@extension").getText();// 获取根节点
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
		String exam_num= document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:id/abc:item/@extension").getText();// 获取根节点
		this.rc.setExam_num(exam_num);
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
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		
		
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:author/abc:author/@displayName").getText();// 获取根节点
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
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:authenticator/abc:author/@displayName").getText();// 获取根节点
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
	
	/*
	 * 
	 */
	public void getdoctor_orderid() throws Exception {
		String time = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:id/@extension").getText();// 获取根节点
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
		String dept_code = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:participant/abc:associatedEntity/abc:scopingOrganization/abc:id/@extension").getText();// 获取根节点
		String dept_name = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:participant/abc:associatedEntity/abc:scopingOrganization/abc:id/abc:name").getText();// 获取根节点
		this.rc.setDept_code(dept_code);
        this.rc.setDept_name(dept_name);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_Item() throws Exception {		
		List<RetPacsItem> rpi=new ArrayList<RetPacsItem>();
		RetPacsItem reti=new RetPacsItem();
		
		
			String  Items_code = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation/@classCode").getText();// 获取根节点
			reti.setChargingItem_num(Items_code);
		
			
			//描述
		//	String Itemsitem =   document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation/abc:suggestionText").getText();// 获取根节点
			String Itemsitem = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation/abc:value[@code='01']/abc:originalText").getText();// 获取根节点
			reti.setChargingItem_ms(Itemsitem);
			
			//结论
			//String Itemsitemjl = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation/abc:value[@code='01']/abc:originalText").getText();// 获取根节点
			String Itemsitemjl = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation/abc:value[@code='02']/abc:originalText").getText();// 获取根节点
			reti.setChargingItem_jl(Itemsitemjl);
			
		//	String  imagetype = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observationMedia/abc:value/@mediaType").getText();// 获取根节点
		//	reti.setImagetype(imagetype);
		//	String  Items_base = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observationMedia/abc:value").getText();// 获取根节点
			
			
			//此处用于放置  pacs图片路径url
			String  Items_base = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observation/abc:entryRelationship/abc:observationMedia/abc:value").getText();// 获取根节点
			reti.setBase64_bg(Items_base);
			rpi.add(reti);
		this.rc.setList(rpi);
	}

	public static void main(String[] args)throws Exception  {
		String str = "<clinicalDocument><id extension=\"TJ012\"/><creationTime value=\"20180515160550\"/>"
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
				+ "<originalText>sssssssssssssssssssssssssssssddddddd</originalText></value><methodCode code=\"01\" "
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
				+ "</entryRelationship></observation></component></clinicalDocument>";
		
		try{
			new ResPacsMessageHK305(str,true);
			System.out.print("12");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
}
