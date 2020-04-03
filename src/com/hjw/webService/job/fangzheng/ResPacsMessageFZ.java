package  com.hjw.webService.job.fangzheng;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
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

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.service.pacsbean.RetPacsCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;


public class ResPacsMessageFZ {
	private RetPacsCustome rc= new RetPacsCustome();
	private Document document;
	public ResPacsMessageFZ(String xmlmessage,boolean flags) throws Exception{
		 String xmlmessagess=xmlmessage;
		InputStream is = new ByteArrayInputStream(xmlmessagess.getBytes("utf-8"));
		SAXReader sax = new SAXReader();// 创建一个SAXReader对象
		Map<String, String> xmlMap = new HashMap<>();
	 	xmlMap.put("abc", "urn:hl7-org:v3");
	 	sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		this.getCustom();//获取检查人员信息
		this.getdoctor_bg();//获取报告医生
		this.getdoctor_sh();//获取审核医生
		this.getdoctor_orderid();//关联医嘱号或者清单号		 
		this.getdoctor_effectiveTime();//获取报告时间
		//this.getdoctor_deptcode();//关联科室编码对应类别编码
		//this.getdoctor_Diagnosis();//获取阴阳性
		this.getdoctor_Item();//关联检查项目	
	}

	public RetPacsCustome getRetPacsCustome(){
		return this.rc;
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
					if ("1.2.156.112685.1.2.1.3".equals(attr.getValue().trim())){
						for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
							if("extension".equals(attrone.getName().trim().toLowerCase())){
								this.rc.setCustome_id(attrone.getValue());
								break;
							}
						}
					}
					if ("1.2.156.112685.1.2.1.12".equals(attr.getValue().trim())){
						for (Attribute attrone : listAttr) {// 遍历当前节点的所有属性
							if("extension".equals(attrone.getName().trim().toLowerCase())){
								this.rc.setCoustom_jzh(attrone.getValue());
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
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		String name = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name").getText();// 获取根节点
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
		String time = DateTimeUtil.getDateTimes();//document.selectSingleNode("abc:ClinicalDocument/abc:effectiveTime/@value").getText();// 获取根节点
        this.rc.setEffectiveTime(time);
	}
	
	public void getdoctor_orderid() throws Exception {
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:inFulfillmentOf/abc:order/abc:id/@extension").getText();// 获取根节点
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
		String dept_code = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:code/@code").getText();// 获取根节点
		String dept_name = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:code/@displayName").getText();// 获取根节点
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
		Node yyang = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:entryRelationship/abc:observation/abc:value");// 获取根节点
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
		Node Items = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry[@typeCode='DRIV']/abc:organizer/abc:component/abc:observation");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		//System.out.println(listElement.size());
		getNodes_doctor_Item(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_Item(List<Element> listElement) throws Exception {	
		
		String imagebase = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observationMedia/abc:value").getText();// 获取根节点
		String imagetype = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:organizer/abc:component/abc:observationMedia/abc:value/@mediaType").getText();// 获取根节点

		List<RetPacsItem> rpi=new ArrayList<RetPacsItem>();
		for(Element e:listElement){
			try{
				//System.out.println(e.asXML());
			RetPacsItem reti= new RetPacsItem();
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			 Map<String, String> xmlMap = new HashMap<>();
		 		xmlMap.put("abc", "urn:hl7-org:v3");
		 		SAXReader sax = new SAXReader();
		 		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String  Items_code = documentItem.selectSingleNode("/abc:observation/abc:code/@code").getText();
			reti.setChargingItem_num(Items_code);
			String Itemsitem = documentItem.selectSingleNode("/abc:observation/abc:value[@code='01']/abc:originalText").getText();
			reti.setChargingItem_ms(Itemsitem);
			String Itemsitemjl = documentItem.selectSingleNode("/abc:observation/abc:value[@code='02']/abc:originalText").getText();
			reti.setChargingItem_jl(Itemsitemjl);
			
			//String  Items_base = documentItem.selectSingleNode("/abc:observation/abc:entryRelationship[@typeCode='SPRT']/abc:observationMedia/abc:value").getText();
			reti.setBase64_bg(imagebase);
			reti.setImagetype(imagetype);
			rpi.add(reti);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		this.rc.setList(rpi);
	}

	public static void main(String[] args)throws Exception  {
		StringBuffer sb= new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\">");
		sb.append("<!--===================================-->");
		sb.append("<!-- 检查报告                           -->");
		sb.append("<!--===================================-->");
		sb.append("<!--");
		sb.append("********************************************************");
		sb.append("  CDA Header");
		sb.append("********************************************************");
		sb.append("-->");
		sb.append("    <!-- 文档适用范围编码 -->");
		sb.append("	<realmCode code=\"CN\"/>");
		sb.append("");
		sb.append("	<!-- 文档信息模型类别-标识符 -->");
		sb.append("	<!-- 固定值 -->");
		sb.append("	<typeId root=\"2.16.840.1.112685.1.3\" extension=\"POCD_HD000040\"/>");
		sb.append("");
		sb.append("		<!-- 文档标识-报告号 root填写发送系统id，extension填报告唯一id-->");
		sb.append("	<id root=\"S003\" extension=\"DR18011752\"/>");
		sb.append("");
		sb.append("	<!-- 文档标识-名称 / 文档标识-类别编码 -->");
		sb.append("	<!-- 固定值 -->");
		sb.append("	<code code=\"04\" codeSystem=\"1.2.156.112685.1.1.60\" displayName=\"检查检验记录\"/>");
		sb.append("	");
		sb.append("	<!-- 文档标题文本 -->");
		sb.append("	<title>检查报告</title>");
		sb.append("");
		sb.append("	<!-- 文档生效日期 -->");
		sb.append("    <effectiveTime value=\"\"/>");
		sb.append("");
		sb.append("	<!-- 文档密级编码 -->");
		sb.append("	<confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.112685.5.25\" codeSystemName=\"Confidentiality\" displayName=\"normal\"/>");
		sb.append("");
		sb.append("	<!-- 文档语言编码 -->");
		sb.append("    <!-- 固定值 -->");
		sb.append("	<languageCode code=\"zh-CN\"/>");
		sb.append("	");
		sb.append("	<!--服务ID-->");
		sb.append("	<setId extension=\"BS320\"/>");
		sb.append("	");
		sb.append("    <!-- 文档的操作版本:");
		sb.append("    0 表示新增,");
		sb.append("    1 表示修改 ");
		sb.append("    2 表示替换（用于解决审核后的超声心动系统报告，修改后，会生成新的报告，新旧报告报告号不同问题） ");
		sb.append("     -->");
		sb.append("	<versionNumber value=\"0\"/>");
		sb.append("");
		sb.append("	<!-- 文档记录对象 -->");
		sb.append("	<recordTarget typeCode=\"RCT\">");
		sb.append("		<!-- 病人信息 -->");
		sb.append("		<patientRole classCode=\"PAT\">");
		sb.append("		    <!-- 域ID -->");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.2\" extension=\"01\"/> ");
		sb.append("			<!-- 患者ID -->");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.3\" extension=\"4284415\"/> ");
		sb.append("			<!-- 就诊号 -->");
		sb.append("            <id root=\"1.2.156.112685.1.2.1.12\" extension=\"1704020409\"/> ");
		sb.append("  ");
		sb.append("			<!-- 病区床号信息 -->");
		sb.append("			<addr use=\"TMP\">");
		sb.append("                <!-- 床位号 -->");
		sb.append("			    <careOf/>");
		sb.append("			</addr>");
		sb.append("			");
		sb.append("			<!-- 病人基本信息 -->");
		sb.append("			<patient classCode=\"PSN\" determinerCode=\"INSTANCE\">");
		sb.append("				<!-- 病人名称 -->");
		sb.append("				<name>张祥铁</name>");
		sb.append("				<!-- 性别编码/性别名称 -->");
		sb.append("				<administrativeGenderCode code=\"1\" codeSystem=\"1.2.156.112685.1.1.3\" displayName=\"男\"/>			");
		sb.append("				<!-- 出生日期 -->");
		sb.append("				<birthTime value=\"1950/11/25\"/>");
		sb.append("			</patient>");
		sb.append("		 ");
		sb.append("		</patientRole>");
		sb.append("	</recordTarget>");
		sb.append("	");
		sb.append("	<!-- 文档作者(检查报告医生, 可循环) -->");
		sb.append("	<author typeCode=\"AUT\">");
		sb.append("		<!-- 报告日期 -->");
		sb.append("	    <time value=\"20180320092551\"/>");
		sb.append("		<assignedAuthor classCode=\"ASSIGNED\">");
		sb.append("		    <!-- 报告医生编码 -->");
		sb.append("			<id root=\"1.2.156.112685.1.1.2\" extension=\"DR002\"/>");
		sb.append("			<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("			    <!-- 报告医生名称 -->");
		sb.append("				<name>李晓峰</name>");
		sb.append("			</assignedPerson>");
		sb.append("		</assignedAuthor>");
		sb.append("	</author>");
		sb.append("");
		sb.append("	<dataEnterer typeCode=\"ENT\">");
		sb.append("		<assignedEntity>");
		sb.append("		    <!-- 记录者编码 -->");
		sb.append("			<id root=\"1.2.156.112685.1.1.2\" extension=\"\"/>");
		sb.append("			<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("			    <!-- 记录者名称 -->");
		sb.append("				<name/>");
		sb.append("			</assignedPerson>");
		sb.append("		</assignedEntity>");
		sb.append("	</dataEnterer>");
		sb.append("	");
		sb.append("	<!-- 文档保管者(CDA中custodian为必填项) -->");
		sb.append("	<custodian>");
		sb.append("		<assignedCustodian>");
		sb.append("			<representedCustodianOrganization>");
		sb.append("			    <!-- 医疗机构编码 -->");
		sb.append("				<id root=\"1.2.156.112685\" extension=\"46014326-4\"/>");
		sb.append("				<!-- 医疗机构名称 -->");
		sb.append("				<name>包头市中心医院</name>");
		sb.append("			</representedCustodianOrganization>");
		sb.append("		</assignedCustodian>");
		sb.append("	</custodian>");
		sb.append("	");
		sb.append("	<!-- 电子签章信息 -->");
		sb.append("	<legalAuthenticator>");
		sb.append("	    <time/>");
		sb.append("		<signatureCode code=\"S\"/>");
		sb.append("		<assignedEntity>");
		sb.append("		    <!-- 电子签章号-->");
		sb.append("			<id extension=\"\"/>");
		sb.append("		</assignedEntity>");
		sb.append("	</legalAuthenticator>");
		sb.append("	");
		sb.append("    <!-- 文档审核者(检查报告审核医师, 可循环) -->");
		sb.append("	<authenticator>");
		sb.append("	    <!-- 审核日期 -->");
		sb.append("	    <time value=\"20180320093641\"/>");
		sb.append("	    <signatureCode code=\"S\"/>");
		sb.append("		<assignedEntity classCode=\"ASSIGNED\">");
		sb.append("	        <!-- 审核医生编码 -->");
		sb.append("			<id root=\"1.2.156.112685.1.1.2\" extension=\"DR007\"/>");
		sb.append("		    <assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("		        <!-- 审核医生名称 -->");
		sb.append("				<name>姚贵</name>");
		sb.append("			</assignedPerson>");
		sb.append("		</assignedEntity>");
		sb.append("	</authenticator>");
		sb.append("	<authenticator>");
		sb.append("	    <!-- 审核日期 -->");
		sb.append("	    <time value=\"20180320093641\"/>");
		sb.append("	    <signatureCode code=\"S\"/>");
		sb.append("		<assignedEntity classCode=\"ASSIGNED\">");
		sb.append("	        <!-- 审核医生编码 -->");
		sb.append("			<id root=\"1.2.156.112685.1.1.2\" extension=\"DR007\"/>");
		sb.append("		    <assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("		        <!-- 审核医生名称 -->");
		sb.append("				<name>姚贵</name>");
		sb.append("			</assignedPerson>");
		sb.append("		</assignedEntity>");
		sb.append("	</authenticator>	");
		sb.append("");
		sb.append("	<!-- 申请医生和申请科室信息 -->");
		sb.append("	<participant typeCode=\"AUT\">");
		sb.append("		<associatedEntity classCode=\"ASSIGNED\">");
		sb.append("			<!-- 申请医生编码 -->");
		sb.append("			<id root=\"1.2.156.112685.1.1.2\" extension=\"\"/>");
		sb.append("			<associatedPerson>");
		sb.append("				<!-- 申请医生名称 -->");
		sb.append("				<name/>");
		sb.append("			</associatedPerson>");
		sb.append("			");
		sb.append("			<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
		sb.append("			    <!-- 申请科室编码 -->");
		sb.append("			    <id root=\"1.2.156.112685.1.1.1\" extension=\"025021\"/>");
		sb.append("	            <!-- 申请科室名称 -->");
		sb.append("			        <name>普外三科门诊</name>");
		sb.append("			</scopingOrganization>");
		sb.append("			");
		sb.append("		</associatedEntity>");
		sb.append("	</participant>");
		sb.append("	");
		sb.append("");
		sb.append("	<!-- 关联医嘱信息 -->");
		sb.append("	<inFulfillmentOf>");
		sb.append("		<order>");
		sb.append("			<!-- 关联医嘱号(可多个) -->");
		sb.append("			<id extension=\"69903077\"/>");
		sb.append("			<!--可多个,当还有需要关联的医嘱号时参照上述格式添加-->");
		sb.append("		</order>");
		sb.append("	</inFulfillmentOf>");
		sb.append("	");
		sb.append("    <!-- 文档中医疗卫生事件的就诊场景 -->");
		sb.append("	<componentOf typeCode=\"COMP\">");
		sb.append("		<!-- 就诊信息 -->");
		sb.append("		<encompassingEncounter classCode=\"ENC\" moodCode=\"EVN\">");
		sb.append("		    <!-- 就诊次数 -->");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.7\" extension=\"180143784\"/>");
		sb.append("		    <!-- 就诊流水号 -->");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.6\" extension=\"42844150143784\"/>	");
		sb.append("			<!-- 就诊类别编码/就诊类别名称 -->");
		sb.append("			<code code=\"01\" codeSystem=\"1.2.156.112685.1.1.80\" displayName=\"门诊\"/>");
		sb.append("			<effectiveTime/>");
		sb.append("		</encompassingEncounter>");
		sb.append("	</componentOf>");
		sb.append("	");
		sb.append("<!--");
		sb.append("********************************************************");
		sb.append("CDA Body");
		sb.append("********************************************************");
		sb.append("-->");
		sb.append("	<component>");
		sb.append("		<structuredBody>");
		sb.append("		");
		sb.append("<!-- ");
		sb.append("********************************************************");
		sb.append("文档中患者相关信息");
		sb.append("********************************************************");
		sb.append("-->");
		sb.append("            <component>");
		sb.append("                <section>");
		sb.append("                    <code code=\"34076-0\" codeSystem=\"2.16.840.1.112685.6.1\" codeSystemName=\"LOINC\" displayName=\"Information for patients section\"/>");
		sb.append("                    <title>文档中患者相关信息</title>");
		sb.append("                    <!-- 患者年龄 -->");
		sb.append("                    <entry>");
		sb.append("                        <observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("                            <code code=\"397669002\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"age\"/>");
		sb.append("                            <value xsi:type=\"ST\"/>");
		sb.append("                        </observation>");
		sb.append("                    </entry>");
		sb.append("                    <!-- 病区 -->");
		sb.append("                    <entry>");
		sb.append("                        <observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("                            <code code=\"225746001\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"ward\"/>");
		sb.append("                            <!-- 病区编码 病区名称 -->");
		sb.append("                            <value xsi:type=\"SC\" code=\"\" codeSystem=\"1.2.156.112685.1.1.33\"/>");
		sb.append("                        </observation>");
		sb.append("                    </entry>  ");
		sb.append("                  </section>");
		sb.append("            </component>");
		sb.append("");
		sb.append("");
		sb.append("<!--");
		sb.append("********************************************************");
		sb.append("检查章节");
		sb.append("********************************************************");
		sb.append("-->");
		sb.append("			<component>");
		sb.append("				<section>");
		sb.append("					<code code=\"30954-2\" displayName=\"STUDIES SUMMARY\" codeSystem=\"2.16.840.1.112685.6.1\" codeSystemName=\"LOINC\"/>");
		sb.append("					<title>检查</title>");
		sb.append("					");
		sb.append("					<!-- 相关信息 -->");
		sb.append("				    <entry>");
		sb.append("				        <organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb.append("				            <code code=\"310388008\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"relative information status\"/>");
		sb.append("							<statusCode code=\"completed\"/>");
		sb.append("							");
		sb.append("							<!-- 定位图像信息 -->");
		sb.append("							<component>");
		sb.append("							    <supply classCode=\"SPLY\" moodCode=\"EVN\">");
		sb.append("							        <!-- 图像索引号(accessionNumber) -->");
		sb.append("							        <id extension=\"1.2.840.31314.14143234.201803200908350615755\"/>");
		sb.append("							    </supply>");
		sb.append("							</component>");
		sb.append("							<!-- 整张报告图片信息 -->");
		sb.append("							<component>");
		sb.append("								<observationMedia classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("									<!-- 图片信息(要求编码为BASE64), @mediaType: 图片格式(JPG格式: image/jpeg PDF格式为: application/pdf) -->");
		sb.append("								<value xsi:type=\"ED\" mediaType=\"application/pdf\">JVBERi0xLjQKJcOkw7</value>");
		sb.append("								</observationMedia>");
		sb.append("							</component>");
		sb.append("							<!-- 							");
		sb.append("								检查报告类型标识信息(下面是检查报告的展示, 实际标识值按报告内容填写)");
		sb.append("								1. @code=\"301\" @displayName=\"X线检查报告单\"");
		sb.append("								2. @code=\"302\" @displayName=\"CT检查报告单\"");
		sb.append("                                3. @code=\"303\" @displayName=\"MRI检查报告单\"");
		sb.append("                                4. @code=\"304\" @displayName=\"核医学科PET/CT中心\"");
		sb.append("							-->");
		sb.append("							");
		sb.append("							<component>");
		sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("									<!-- 检查报告类型标识编码/检查报告类型标识名称 -->");
		sb.append("									<code code=\"103\" codeSystem=\"1.2.156.112685.1.1.112\" displayName=\"\"/>");
		sb.append("								</observation>");
		sb.append("							</component>");
		sb.append("							");
		sb.append("													");
		sb.append("						</organizer>");
		sb.append("					</entry>	");
		sb.append("					");
		sb.append("	                <!--****************************************************************************-->");
		sb.append("					<!-- 检查报告条目 -->");
		sb.append("                    <entry typeCode=\"DRIV\">");
		sb.append("		                <organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb.append("");
		sb.append("                            <!-- 检查号--> ");
		sb.append("                            <id extension=\"\"/> ");
		sb.append("");
		sb.append("		                    <!-- 检查类型编码/检查类型名称 -->");
		sb.append("		                    <code code=\"DX\" codeSystem=\"1.2.156.112685.1.1.41\" displayName=\"CR\"/>	");
		sb.append("							");
		sb.append("		                    <!-- 必须固定项 -->");
		sb.append("			                <statusCode code=\"completed\"/>");
		sb.append("			  ");
		sb.append("			                <!-- study -->");
		sb.append("			                <component typeCode=\"COMP\">");
		sb.append("				                <observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("					                <!-- 检查项目编码/检查项目名称 -->");
		sb.append("					                <code code=\"1T406\" codeSystem=\"1.2.156.112685.1.1.88\" displayName=\"胸部肋骨双斜位DR\"/>");
		sb.append("									");
		sb.append("                                    <!-- 检查备注 -->");
		sb.append("					                <text/>");
		sb.append("					   ");
		sb.append("					                <!-- 必须固定项 -->");
		sb.append("					                <statusCode code=\"completed\"/>");
		sb.append("									");
		sb.append("					                <!-- 检查报告结果-客观所见/影像学表现(能够与项目对应时的填写处 - @code:01表示客观所见, 02表示主观意见) -->");
		sb.append("					                <value xsi:type=\"CD\" code=\"01\" codeSystem=\"1.2.156.112685.1.1.98\">");
		sb.append("										<originalText>    肋骨双斜位片，双侧所见肋骨骨质未见异常改变。右肺上可见陈旧灶。</originalText>");
		sb.append("					                </value>");
		sb.append("					  ");
		sb.append("                                    <!-- 检查报告结果-主观意见/影像学结论(能够与项目对应时的填写处 - @code:01表示客观所见, 02表示主观意见) -->");
		sb.append("					                <value xsi:type=\"CD\" code=\"02\" codeSystem=\"1.2.156.112685.1.1.98\">");
		sb.append("										<originalText>      双侧所见肋骨骨质未见异常改变。请结合临床，必要时行其他检查。");
		sb.append("</originalText>");
		sb.append("					                </value>");
		sb.append("					  ");
		sb.append("					                <!-- 检查方法编码/检查方法名称 -->");
		sb.append("					                <methodCode code=\"\" codeSystem=\"1.2.156.112685.1.1.43\" displayName=\"\"/>");
		sb.append("					  ");
		sb.append("                                    <!-- 检查部位编码/检查部位名称 -->");
		sb.append("					                <targetSiteCode code=\"\" codeSystem=\"1.2.156.112685.1.1.42\" displayName=\"\"/>");
		sb.append("					  ");
		sb.append("					                <!-- 检查医师信息(可循环) -->");
		sb.append("					                <performer>");
		sb.append("					                    <!-- 检查日期 -->");
		sb.append("	                                    <time value=\"20180320091750\"/>");
		sb.append("										<assignedEntity>");
		sb.append("											<!-- 检查医生编码 -->");
		sb.append("			                                <id root=\"1.2.156.112685.1.1.2\" extension=\"\"/>");
		sb.append("		                                    <assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("		                                        <!-- 检查医生名称 -->");
		sb.append("				                                <name></name>");
		sb.append("			                                </assignedPerson>");
		sb.append("			                                <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
		sb.append("			                                   <!-- 检查科室编码 -->");
		sb.append("			                                   <id root=\"1.2.156.112685.1.1.1\" extension=\"\"/>");
		sb.append("										       <!-- 检查科室名称 -->");
		sb.append("										       <name/>");
		sb.append("									        </representedOrganization>");
		sb.append("										</assignedEntity>");
		sb.append("									</performer>");
		sb.append("									");
		sb.append("									<participant typeCode=\"DEV\">");
		sb.append("									    <!--对应动态心电图-->");
		sb.append("			                            <time>");
		sb.append("			                                <!--记录开始时间-->");
		sb.append("			                                <low value=\"\"/>");
		sb.append("			                                <!--记录总时间-->");
		sb.append("			                                <width xsi:type=\"PQ\" value=\"\" unit=\"\"/>");
		sb.append("			                            </time>");
		sb.append("										<participantRole>");
		sb.append("											<playingDevice>");
		sb.append("												<!--仪器型号-->");
		sb.append("												<manufacturerModelName code=\"\"/>");
		sb.append("											</playingDevice>");
		sb.append("										</participantRole>");
		sb.append("									</participant>");
		sb.append("									");
		sb.append("																		");
		sb.append("");
		sb.append("									<!-- 仪器或医生客观所见信息(超声心动报告和动态心电图等结构化所见部分的信息) -->");
		sb.append("									<entryRelationship typeCode=\"COMP\">");
		sb.append("									    <organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb.append("									        <code code=\"365605003\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"body measurement finding\"/>");
		sb.append("											<statusCode code=\"completed\"/>");
		sb.append("											");
		sb.append("											<!-- 项目信息(可循环) -->");
		sb.append("											<!--");
		sb.append("											PQ: <value xsi:type=\"PQ\" value=\"19.1\" unit=\"10^9/L\" />  											数值类型的结果+单位)");
		sb.append("											SC: <value xsi:type=\"SC\">1mm</value>  文本类型结果或者数字结果和单位无法分开的");
		sb.append("											-->");
		sb.append("											");
		sb.append("											<!-- 当只有一级关系时使用 -->	");
		sb.append("											<component>");
		sb.append("												<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("													<code code=\"\" displayName=\"\"/>");
		sb.append("													<value xsi:type=\"SC\"/>");
		sb.append("												</observation>");
		sb.append("											</component>");
		sb.append("											");
		sb.append("											<!-- 存在多级关系时使用 -->												");
		sb.append("											<component>");
		sb.append("												<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("												    <!-- 项目编码/项目名称 -->");
		sb.append("													<code code=\"\" displayName=\"\"/>");
		sb.append("													<!-- 带单位的类型为PQ，不带单位为SC 项目结果值文本 数据类型为SC的去掉@Unit-->");
		sb.append("													<value xsi:type=\"SC\"/>");
		sb.append("													");
		sb.append("													<entryRelationship typeCode=\"COMP\">");
		sb.append("														<organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb.append("															<statusCode code=\"completed\"/>");
		sb.append("");
		sb.append("															<!-- 具体检查项信息(可循环) -->");
		sb.append("															<!--带单位的类型为PQ，不带单位为SC-->");
		sb.append("															");
		sb.append("															<component>");
		sb.append("																<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("																	<code code=\"\" displayName=\"\"/>");
		sb.append("																	<!--占总数等说明-->");
		sb.append("																	<text/>");
		sb.append("																	<value xsi:type=\"PQ\" value=\"\" unit=\"\"/>	");
		sb.append("																</observation>");
		sb.append("															</component>						");
		sb.append("															<!-- 其它信息按上面格式添加 -->");
		sb.append("");
		sb.append("														</organizer>");
		sb.append("													</entryRelationship>");
		sb.append("												</observation>");
		sb.append("											</component>");
		sb.append("											<!-- 其它信息按上面格式添加 -->");
		sb.append("");
		sb.append("										</organizer>");
		sb.append("									</entryRelationship>");
		sb.append("							");
		sb.append("							        <!-- 图像信息(能与项目对应的图像) -->");
		sb.append("							");
		sb.append("									<!-- 当有多个影像对应同一个study时,可以复用此entryRelationship -->");
		sb.append("									");
		sb.append("				                </observation>");
		sb.append("				            </component>");
		sb.append("				            <!-- 其他项目按上面结构和格式添加 -->");
		sb.append("				        </organizer>");
		sb.append("				    </entry>");
		sb.append("				</section>");
		sb.append("			</component>");
		sb.append("  		");
		sb.append("<!-- ");
		sb.append("********************************************************");
		sb.append("诊断");
		sb.append("********************************************************");
		sb.append("-->");
		sb.append("			<component>");
		sb.append("				<section>");
		sb.append("					<code code=\"29308-4\" codeSystem=\"2.16.840.1.112685.6.1\" codeSystemName=\"LOINC\" displayName=\"Diagnosis\"/> ");
		sb.append("					<title>诊断</title> ");
		sb.append("					<entry typeCode=\"DRIV\">");
		sb.append("						<act classCode=\"ACT\" moodCode=\"EVN\">");
		sb.append("							<code nullFlavor=\"NA\"/>");
		sb.append("							<entryRelationship typeCode=\"SUBJ\">");
		sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb.append("									<!-- 诊断类别编码/诊断类别名称 -->");
		sb.append("									<code code=\"\" codeSystem=\"1.2.156.112685.1.1.29\" displayName=\"\"/>");
		sb.append("										");
		sb.append("									<statusCode code=\"completed\"/>");
		sb.append("									 ");
		sb.append("                                    <!-- 疾病编码/疾病名称(没有编码去掉@code) -->");
		sb.append("									<value xsi:type=\"CD\" code=\"\" codeSystem=\"1.2.156.112685.1.1.30\" displayName=\"\"/>");
		sb.append("								</observation>");
		sb.append("							</entryRelationship>");
		sb.append("						</act>");
		sb.append("					</entry>");
		sb.append("				</section>");
		sb.append("		    </component>");
		sb.append(" ");
		sb.append("		</structuredBody>");
		sb.append("	</component>");
		sb.append("");
		sb.append("</ClinicalDocument>");
		
		StringBuffer sb1= new StringBuffer();
		sb1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb1.append("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\">");
		sb1.append("		<!--===================================-->");
		sb1.append("		<!-- 检查报告                           -->");
		sb1.append("		<!--===================================-->");
		sb1.append("		<!--");
		sb1.append("		********************************************************");
		sb1.append("		  CDA Header");
		sb1.append("		********************************************************");
		sb1.append("		-->");
		sb1.append("		    <!-- 文档适用范围编码 -->");
		sb1.append("			<realmCode code=\"CN\"/>");
		sb1.append("");
		sb1.append("			<!-- 文档信息模型类别-标识符 -->");
		sb1.append("			<!-- 固定值 -->");
		sb1.append("			<typeId root=\"2.16.840.1.112685.1.3\" extension=\"POCD_HD000040\"/>");
		sb1.append("");
		sb1.append("				<!-- 文档标识-报告号 root填写发送系统id，extension填报告唯一id-->");
		sb1.append("			<id root=\"S003\" extension=\"CT18000288\"/>");
		sb1.append("");
		sb1.append("			<!-- 文档标识-名称 / 文档标识-类别编码 -->");
		sb1.append("			<!-- 固定值 -->");
		sb1.append("			<code code=\"04\" codeSystem=\"1.2.156.112685.1.1.60\" displayName=\"检查检验记录\"/>");
		sb1.append("	");		
		sb1.append("			<!-- 文档标题文本 -->");
		sb1.append("			<title>检查报告</title>");
		sb1.append("");
		sb1.append("			<!-- 文档生效日期 -->");
		sb1.append("		    <effectiveTime value=\"\"/>");
		sb1.append("");
		sb1.append("			<!-- 文档密级编码 -->");
		sb1.append("			<confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.112685.5.25\" codeSystemName=\"Confidentiality\" displayName=\"normal\"/>");
		sb1.append("");
		sb1.append("			<!-- 文档语言编码 -->");
		sb1.append("		    <!-- 固定值 -->");
		sb1.append("			<languageCode code=\"zh-CN\"/>");
		sb1.append("");			
		sb1.append("			<!--服务ID-->");
		sb1.append("			<setId extension=\"BS320\"/>");
		sb1.append("");			
		sb1.append("		    <!-- 文档的操作版本:");
		sb1.append("		    0 表示新增,");
		sb1.append("		    1 表示修改 ");
		sb1.append("		    2 表示替换（用于解决审核后的超声心动系统报告，修改后，会生成新的报告，新旧报告报告号不同问题） ");
		sb1.append("		     -->");
		sb1.append("			<versionNumber value=\"0\"/>");
		sb1.append("");
		sb1.append("			<!-- 文档记录对象 -->");
		sb1.append("			<recordTarget typeCode=\"RCT\">");
		sb1.append("				<!-- 病人信息 -->");
		sb1.append("				<patientRole classCode=\"PAT\">");
		sb1.append("				    <!-- 域ID -->");
		sb1.append("					<id root=\"1.2.156.112685.1.2.1.2\" extension=\"01\"/>"); 
		sb1.append("					<!-- 患者ID -->");
		sb1.append("					<id root=\"1.2.156.112685.1.2.1.3\" extension=\"4166657\"/>"); 
		sb1.append("					<!-- 就诊号 -->");
		sb1.append("		            <id root=\"1.2.156.112685.1.2.1.12\" extension=\"1803300005\"/>"); 
		sb1.append("	");	            
		sb1.append("					<!-- 病区床号信息 -->");
		sb1.append("					<addr use=\"TMP\">");
		sb1.append("		                <!-- 床位号 -->");
		sb1.append("					    <careOf/>");
		sb1.append("					</addr>");
		sb1.append("					");
		sb1.append("					<!-- 病人基本信息 -->");
		sb1.append("					<patient classCode=\"PSN\" determinerCode=\"INSTANCE\">");
		sb1.append("						<!-- 病人名称 -->");
		sb1.append("						<name>p测试</name>");
		sb1.append("						<!-- 性别编码/性别名称 -->");
		sb1.append("						<administrativeGenderCode code=\"1\" codeSystem=\"1.2.156.112685.1.1.3\" displayName=\"男\"/>");			
		sb1.append("						<!-- 出生日期 -->");
		sb1.append("						<birthTime value=\"1996/03/30\"/>");
		sb1.append("					</patient>");
		sb1.append("				 ");
		sb1.append("				</patientRole>");
		sb1.append("			</recordTarget>");
		sb1.append("			");
		sb1.append("			<!-- 文档作者(检查报告医生, 可循环) -->");
		sb1.append("			<author typeCode=\"AUT\">");
		sb1.append("				<!-- 报告日期 -->");
		sb1.append("			    <time value=\"20180330093846\"/>");
		sb1.append("				<assignedAuthor classCode=\"ASSIGNED\">");
		sb1.append("				    <!-- 报告医生编码 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.1.2\" extension=\"USER001\"/>");
		sb1.append("					<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb1.append("					    <!-- 报告医生名称 -->");
		sb1.append("						<name>管理员</name>");
		sb1.append("					</assignedPerson>");
		sb1.append("				</assignedAuthor>");
		sb1.append("			</author>");
		sb1.append("");
		sb1.append("			<dataEnterer typeCode=\"ENT\">");
		sb1.append("				<assignedEntity>");
		sb1.append("				    <!-- 记录者编码 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.1.2\" extension=\"\"/>");
		sb1.append("					<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb1.append("					    <!-- 记录者名称 -->");
		sb1.append("						<name/>");
		sb1.append("					</assignedPerson>");
		sb1.append("				</assignedEntity>");
		sb1.append("			</dataEnterer>");
		sb1.append("			");
		sb1.append("			<!-- 文档保管者(CDA中custodian为必填项) -->");
		sb1.append("			<custodian>");
		sb1.append("				<assignedCustodian>");
		sb1.append("					<representedCustodianOrganization>");
		sb1.append("					    <!-- 医疗机构编码 -->");
		sb1.append("						<id root=\"1.2.156.112685\" extension=\"46014326-4\"/>");
		sb1.append("						<!-- 医疗机构名称 -->");
		sb1.append("						<name>包头市中心医院</name>");
		sb1.append("					</representedCustodianOrganization>");
		sb1.append("				</assignedCustodian>");
		sb1.append("			</custodian>");
		sb1.append("			");
		sb1.append("			<!-- 电子签章信息 -->");
		sb1.append("			<legalAuthenticator>");
		sb1.append("			    <time/>");
		sb1.append("				<signatureCode code=\"S\"/>");
		sb1.append("				<assignedEntity>");
		sb1.append("				    <!-- 电子签章号-->");
		sb1.append("					<id extension=\"\"/>");
		sb1.append("				</assignedEntity>");
		sb1.append("			</legalAuthenticator>");
		sb1.append("			");
		sb1.append("		    <!-- 文档审核者(检查报告审核医师, 可循环) -->");
		sb1.append("			<authenticator>");
		sb1.append("			    <!-- 审核日期 -->");
		sb1.append("			    <time value=\"20180330093910\"/>");
		sb1.append("			    <signatureCode code=\"S\"/>");
		sb1.append("				<assignedEntity classCode=\"ASSIGNED\">");
		sb1.append("			        <!-- 审核医生编码 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.1.2\" extension=\"USER001\"/>");
		sb1.append("				    <assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb1.append("				        <!-- 审核医生名称 -->");
		sb1.append("						<name>管理员</name>");
		sb1.append("					</assignedPerson>");
		sb1.append("				</assignedEntity>");
		sb1.append("			</authenticator>");
		sb1.append("			<authenticator>");
		sb1.append("			    <!-- 审核日期 -->");
		sb1.append("			    <time value=\"20180330093910\"/>");
		sb1.append("			    <signatureCode code=\"S\"/>");
		sb1.append("				<assignedEntity classCode=\"ASSIGNED\">");
		sb1.append("			        <!-- 审核医生编码 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.1.2\" extension=\"USER001\"/>");
		sb1.append("				    <assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb1.append("				        <!-- 审核医生名称 -->");
		sb1.append("						<name>管理员</name>");
		sb1.append("					</assignedPerson>");
		sb1.append("				</assignedEntity>");
		sb1.append("			</authenticator>");	
		sb1.append("");
		sb1.append("			<!-- 申请医生和申请科室信息 -->");
		sb1.append("			<participant typeCode=\"AUT\">");
		sb1.append("				<associatedEntity classCode=\"ASSIGNED\">");
		sb1.append("					<!-- 申请医生编码 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.1.2\" extension=\"\"/>");
		sb1.append("					<associatedPerson>");
		sb1.append("						<!-- 申请医生名称 -->");
		sb1.append("						<name/>");
		sb1.append("					</associatedPerson>");
		sb1.append("					");
		sb1.append("					<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
		sb1.append("					    <!-- 申请科室编码 -->");
		sb1.append("					    <id root=\"1.2.156.112685.1.1.1\" extension=\"428\"/>");
		sb1.append("			            <!-- 申请科室名称 -->");
		sb1.append("					        <name>体检中心</name>");
		sb1.append("					</scopingOrganization>");
		sb1.append("					");
		sb1.append("				</associatedEntity>");
		sb1.append("			</participant>");
		sb1.append("			");
		sb1.append("");
		sb1.append("			<!-- 关联医嘱信息 -->");
		sb1.append("			<inFulfillmentOf>");
		sb1.append("				<order>");
		sb1.append("					<!-- 关联医嘱号(可多个) -->");
		sb1.append("					<id extension=\"37\"/>");
		sb1.append("					<!--可多个,当还有需要关联的医嘱号时参照上述格式添加-->");
		sb1.append("				</order>");
		sb1.append("			</inFulfillmentOf>");
		sb1.append("			");
		sb1.append("		    <!-- 文档中医疗卫生事件的就诊场景 -->");
		sb1.append("			<componentOf typeCode=\"COMP\">");
		sb1.append("				<!-- 就诊信息 -->");
		sb1.append("				<encompassingEncounter classCode=\"ENC\" moodCode=\"EVN\">");
		sb1.append("				    <!-- 就诊次数 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.2.1.7\" extension=\"180000342\"/>");
		sb1.append("				    <!-- 就诊流水号 -->");
		sb1.append("					<id root=\"1.2.156.112685.1.2.1.6\" extension=\"41666570000342\"/>");	
		sb1.append("					<!-- 就诊类别编码/就诊类别名称 -->");
		sb1.append("					<code code=\"0401\" codeSystem=\"1.2.156.112685.1.1.80\" displayName=\"干保体检\"/>");
		sb1.append("					<effectiveTime/>");
		sb1.append("				</encompassingEncounter>");
		sb1.append("			</componentOf>");
		sb1.append("			");
		sb1.append("		<!--");
		sb1.append("		********************************************************");
		sb1.append("		CDA Body");
		sb1.append("		********************************************************");
		sb1.append("		-->");
		sb1.append("			<component>");
		sb1.append("				<structuredBody>");
		sb1.append("				");
		sb1.append("		<!-- ");
		sb1.append("		********************************************************");
		sb1.append("		文档中患者相关信息");
		sb1.append("		********************************************************");
		sb1.append("		-->");
		sb1.append("		            <component>");
		sb1.append("		                <section>");
		sb1.append("		                    <code code=\"34076-0\" codeSystem=\"2.16.840.1.112685.6.1\" codeSystemName=\"LOINC\" displayName=\"Information for patients section\"/>");
		sb1.append("		                    <title>文档中患者相关信息</title>");
		sb1.append("		                    <!-- 患者年龄 -->");
		sb1.append("		                    <entry>");
		sb1.append("		                        <observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("		                            <code code=\"397669002\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"age\"/>");
		sb1.append("		                            <value xsi:type=\"ST\"/>");
		sb1.append("		                        </observation>");
		sb1.append("		                    </entry>");
		sb1.append("		                    <!-- 病区 -->");
		sb1.append("		                    <entry>");
		sb1.append("		                        <observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("		                            <code code=\"225746001\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"ward\"/>");
		sb1.append("		                            <!-- 病区编码 病区名称 -->");
		sb1.append("		                            <value xsi:type=\"SC\" code=\"\" codeSystem=\"1.2.156.112685.1.1.33\"/>");
		sb1.append("		                        </observation>");
		sb1.append("		                    </entry>");                
		sb1.append("		                  </section>");
		sb1.append("		            </component>");
		sb1.append("		<!--");
		sb1.append("		********************************************************");
		sb1.append("		检查章节");
		sb1.append("		********************************************************");
		sb1.append("		-->");
		sb1.append("					<component>");
		sb1.append("						<section>");
		sb1.append("							<code code=\"30954-2\" displayName=\"STUDIES SUMMARY\" codeSystem=\"2.16.840.1.112685.6.1\" codeSystemName=\"LOINC\"/>");
		sb1.append("							<title>检查</title>");
		sb1.append("							");
		sb1.append("							<!-- 相关信息 -->");
		sb1.append("						    <entry>");
		sb1.append("						        <organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb1.append("						            <code code=\"310388008\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"relative information status\"/>");
		sb1.append("									<statusCode code=\"completed\"/>");
		sb1.append("									");
		sb1.append("									<!-- 定位图像信息 -->");
		sb1.append("									<component>");
		sb1.append("									    <supply classCode=\"SPLY\" moodCode=\"EVN\">");
		sb1.append("									        <!-- 图像索引号(accessionNumber) -->");
		sb1.append("									        <id extension=\"1.2.840.31314.14143234.201803300932530325254\"/>");
		sb1.append("									    </supply>");
		sb1.append("									</component>");
		sb1.append("									<!-- 整张报告图片信息 -->");
		sb1.append("									<component>");
		sb1.append("										<observationMedia classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("											<!-- 图片信息(要求编码为BASE64), @mediaType: 图片格式(JPG格式: image/jpeg PDF格式为: application/pdf) -->");
		sb1.append("											<value xsi:type=\"ED\" mediaType=\"application/pdf\">JVBERi0xLjQKJcOkw7zDtsOfCjIgMCBvYmoKPDwvTGVuZ3RoIDMgMCBSL0ZpbHRlci9GbGF0ZURlY29kZT4+CnN0cmVhbQp4nLVYS4/jNgy+51f4vMCm4kOUDAwCxI8U6G3aAD0seutugaIt0Ln075eyYsW2JM8YaGM4sUOR/ERKfMicofnn9HdjGqNPtrVnbDzD2TdvX08/f2r+OkETrrffTlbOrnFCDXs4tw14Hfr2tfn26UHhmYJm9T/CWWYKnHlFM6j/JnluSRPPqqBCE6uCEs2vaExLfWsaclUf+Va/HzRa85GDChcinW2FCw3rH2U+8CbNQEGtrALWJ5kYvpY0nV1CyUnf5EC9fvz+FB/UX939hM39TR3rSD/qZvAuGL0NzPdfm+9uoPib+7cvLwYMGtKb9bZ6y+Uzv+ivesj4yy/3H07j/fSa9DzEk1UETiRKQ0X0kHYBZb6AiqALSHjORJShsuhHoaLAtNpkBjqJfjGt4rnq3end6z1c7r8fkatGVMRq8a1gzARFfqGMASeGUbXH6wamwgy44cZZHR/EDRQWSQE4ANSgg2DGE7HPyBEoPXFhFu/4yObyIyYLVMGE07rfxyTpyVUtS24jJ1nWGntwHqTbAovz8BXtbMq65aBmhnx1Rc1tTTMZHfuf6ObcE1H3tabbbuyUdLujusUVNXcVzXHXGqthcbNooK8t4LOz2HL2O29MLEuEAUa4YXVD67KrIAkXwowmPH4YDbIvS0VCRVrdSryHZXuFmRk1+gFcOraCy6IEWXvuEh++N8BwyicHzROdVZCn0nzVPCGLVlAkJxm9j6KJziqhMVhHE521i2ZCpPkNrzmiZeTzG+tSlBST+AU15WoWt9Ml+sS17B29JDme1lyxw34nq+GSk2ZOqCZRoFAs5hxueVX92C55E86AcsCxihKhyIc3qnKw1Yh0ZF66C7SUKs5rd2twe6aS2Qmo7viH2Qu8BLcaD09GePIkiLXUptlXxxa0aAnJZEnIUY1Xw9Wa97k023lxXqvFpJ2sX1DcUp6RKvllrvAEnb5kC4Y6Wi7r1yaIY0M6NrQ+pOVw7FBi65OrenQRoa34s7G6quXx9kfzUyQGaTYR41siJs7nyIm2EJRzRpS19mxtRBe6A9GA3W5sONCITDcNB8D6wMyWRnIs7Nhzy1dk7mgwSCP3PPDINw0gEMZYtGTZWivWWa8YavWBblRRjGbrQNvZflLUad9CNNrBjvYWIYgRLVOFqBcWy3kB8I6jdTVmyclrkeHEZ45GCvVLdLTimBqyXUeTODXk7JH4lnwZpHEixrdETJzPkU9Hr8U+qUcc7UMZSBY3bp6cFxy3F360q8s2R6fdVFYcLMMCVDjtQDepVatgsaJvlfXkKt2udrR8SPsHekDQBPpcOFP5GWBAx3uJT6ZWPnE+O96Qa30IMZfPGuTEdBf38njptDSvxDxoUf1cFKj1lekf/G0QFl8GzSy1AMphK+TCFsY+2uAJFO3UyzW3U6VoChr266lNM0ZVnTLI+H9o1PZvd55HYxJIOLnarnfdYe8srnAWtuBNqVrj+VTTdfrbTzs173Dn9sSvZWC23w5MBlpaS/twtNCMUebcjRbo24q+o8jJuLKkdyMNSdF/+1FOu3h11lQ9PjzXTrDzw6mZwYVl8mSAyCC3YFktHsFpl+RCLahYQz5zjASds06YnXPete7qOtfr2+DGMHIa5ZymVRi98SC9qfbxbqM+x7vOg8+jXbM+jVyetW5IywPOLWlxvvkkvTb/AilmxJ0KZW5kc3RyZWFtCmVuZG9iagoKMyAwIG9iagoxMjYzCmVuZG9iagoKNSAwIG9iago8PC9MZW5ndGggNiAwIFIvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aDEgMjk0MDQ+PgpzdHJlYW0KeJzsvXeYHMl1J5iRkZkRkRkR6TPL+67q6mrfXV2NbnSj4c0Ag3HAAOMxAGaAseBwhkPO0HuJokZLURKlo0hJFCWR4pKUxJNEiSdDmZXoze59d99Jq5Ojk6Vb7benE+5FVrUBMKL2vvvj/rgrfFWdlZVVGeaZ3++9F4Gnn3rmosa112pY2zj/+Lkrrzza1eDxGU1DwfmXPV17td58Fo7/d03Tf+uhKw8//rV3/MwxTTN+Cq4RDz/2ioe+9bafgi8YH9W082cuXTx34a/e/PFE0y5R+HzpEpz4Cw3B4aUNeGldevzpl/+lMYXh/QX4Tf7Yk+fPLZR+Pta0y1fgvfX4uZdfec96Dr7wyMvh+toT5x6/+L6DP/w/w/t3aRq5cuXJlz7Ntb+7qmlPqd+vXXnq4pX/ePP/9s/wfhze/zI8UXY7DXoEv6fe69gwLUKZ7XAhXc8PwihO0ly+UCyVK9VavdFsjbU7492J3uTU9Mzs3PzCYn9psLxrZXX32vqejb379h84eOjwkaPHbjp+4uaTt9x62+13nDp955mzd919z7333f/AuQfPX7j40MOXLj/y6GOPP/HklZc89dKnn3nZsy9/xXPPv/JVr37Na1+n/b/8uPr/P/4//dB+UdO1K/A0rv7L1T+D95ZGNRv0Q2qeFmiRlmg5raCVtIo6869+Dlf8d9yrAv/+GISuBP/0q9/QTmkL2rL2u/B7G9pAW9Pmtd3an1z9U+1O7YTmaNXs+Q745Dlo4WPak1oN/hXgjqoN6ulq2kfHJ2+67czBA8V6/eyUEmis2Vf/UfuO9gW410Bb1bTBYiddGKQkSkm/GUcVtK4PFioolagxjfoDqZN2p5/MLw2aFumso0FqEXV5vNBfRe4ey6NI7vV3LwsDmchirLTer78D6baH04Db6KXlBi607Ih4T9jy7XtvLeUo42TSMZx5aSFkuc1ShAV8M/eDi4s+kZaB6cOTJVzeXeS4YMvJ6WnVZnn1L7RvQptN6PMEtLmCrHaHZK8DIpFqETQvew/vVDObDdJZWEf93U3f8YpxWzh+/nkb7Nmqn17QjYVpvbnur3dQb8W5WTjsioF+y/EK5XhK+rlqvEzqU3U/pXacdnrNeo0ZYqKNWf0EGFtd869+BVrzRRhjaNweVEEStcmSGsd+dltoTJyNUUQW1dh11MCmZGkNNePLeiQIsw4g5A4OwY1/w0seXSYBS0t+jFaQhwafw7br4vS32kdT1sTMS5o2J2dCKqmh89suBgEppPdfUaPSuPpV7a+0L8FRVWvCqMCcdKAVZHRzNaHqtnD3rHVltNB/05E7UY4eoj5D7hHko4MuNOTgv9u3z+pEDh3ngq443AqOC5bexGlwIGbWvn3goGAG/g/tH7Tfgz4XtYbWzeYAut0hcCcrhZEeLERJavVQe9BfJAsRfLY0GponcgkWPC7k4Z67HBOZpkPjY6leNw0vKv44rnBDWr8no5iPFccFfShCqFYo5g0PlW8r+LUKJ9UxvRhFrAVjn1z9W+0ftU+DbO+CNmwOdZN04LadKtqShS2JmEYda1siYDCydxM/5ka/8TNe9AcgcuhIZDF8VITrCO0W4b263mmZYTVOlvVSx+jBJJ3QqxLENPqEG/+mGzNu/YIMfNsIiQjzqQhtyuNKLuXjIZadPGb5NBfofg9GrQgz9E3tc6CTZW1dO6OkpZmooRkspAuLHRhD1VDLRaq5jeGfOBosgLptDicIzw6RVgOaCdXSIoj+aGLVdH+aPqhjKlzdQNNCD5k9SV3u3nPA8daFU3iEe3PSJxgZBrOCqodivdtCtbpuCmcy9JDwo7zjyKApEBIN6tK3J4KaehzafqA7nm1LnHDHh6kKjkhUAhSAXeSdXOoyzHvTuOeXUt8UJvx6nEQrf0hbH3QZEyZIiw268l3ty5oAW1gA+zStHYd5S0mSxhZYFpiMJTLqfKbVSmas9oAMZzCbsgHJpFhdOoCJXtgU8WacKfyPJLS05BkzQTSHZ4vHbO+ASzkOqa/ncGr5+j4m/aoIXwvzeE9gvRFUzMvpZZ5jdALlmOv4PS90hPNlbi5TAznx8lIFd8bAgBVRueg2llCtJFslc1ARIWb9uf2yZa2ut/K4u6HTYtMmU46fEj9X4WCdBNjWb4NGKn2c15bAZmf2ddjcUZebO3oD/UtBawdD47s1mXBq1P3noIkHZXRPc83XV1MLGt85oK+3dkFnVi9I5F4A/fXPUe/5C+/5ZTf+mMc+GngO8WQ0Z2Pdzs9NCJIrz+VxZR6zZ2u11l6XCE6KgxJhHDcal9wYK+2mMEvf0T4PPZiCdt+ubMnSAAQsUWKXmRNre8ZiZclGLc3cA3wEk5QM+tfOjuqINRTWNWSRN6Z+YMsc09H6AebaUpbLHs7HcQG64O1mkoLXEGbInECy37a6ZU7nUErBbndbehuBgsm6fvFSlCTjHdMqRLcw1xyLSiY2qJtLfGI105flU8akKauuJaxmNUkrKfLmdVrPO2Tc8aelEZ31iWCmqfPDngl207v6V9rfgyV3tVCb07R0awKGE9LfaVGHhkZJZH/TFbpoN7T9EvPAsj366BrKsykRntjnxv8TzM/lXTIy/MSIGC8VlgvM5nQsL2gUnFpbe6AUORYI5SyMP5uNPT2cCLgVgb5wsBhKX7BGwNe5YDnWhpZ9QQnF6G9naHzToXXPWjQSHtWqoQ0cOXQ42liSu14Fz6d1GSPHmQIhOiXoI0tq3I8TF0c05OMU+iB33eQl76rDo9OAx72Y+tKp/kRXkGZBp1VulZZLzKk68BUXvPhaaDlVLxliiq9o/wX88wSM4slMv0F/+9eO3I7h2zGImWAtDTpDhScjJ55pRxueQwPwqpgYRgvl7TIPVnM8WKqL8CkY4gM+vMyPOR7mfL/lup7jcIZJlYAF4CHTfZmv4rrr5h0GFzao/lHb5KIrfIf0ebDMgwNKpw9gtlyL9WI/SFYtq7R32ZIVm1t7q1SEod0J9UrNtsd7mPUC1Vdx9W+0PwOZmdBmtUXo62i4400dVrIOLe+nSndGgjLqXdY3pfCfTefSsxSwk1LgZhv1qk46799Za+G0ZycONe9g4VvrhsOi3IWfqvdcW1ht1weLbjWdasiwl9iGN1PoxdhfqXoGJ46bl76bRjft3q0QSu3qf9MOavdmeElbkmgaraPIaq+jA8j3URjEHgrS0LYjez5mTsg0QIvO1a9pf6N9FmQuD/ZrAvzrYcCZyhZcpxXKeqt+6emwP0NrAJ9W0XDKIyvDjEuAxSrg69QxDEJnCNey15OAOuaooEgsBDFByZzeTHRe5oIHuxBKKIpRTCgnadF+e0gQynHuBsyJByUsoL1FSzAxHj6dZ1RYp6PI+r63vcQhBw4XjNz8/tujKGhGuhsdSKRDT7lu7d0l1yYfd7jhghiht5/MtyTtOoahI/+w7Ud2RS67viwBXDZBkv9c+yewhBbg9jlA38e2EF6aaVZnqbMJ8kg6WLCiBABGdqLdVzqXGfpNbJS5diULm7CwjmVgCDNoYt8jnok4dVjHCtj7AdvR6Qpehfen9RxDDRFsYNv3debI5TZqNgnPcQaf7uJu/Xd0O/RY+Sc7uJCTvmXficG4kAe7aaHQPL6GWbUsSOrL6MJ7CYxoUX7IMaRPA8s4ghkPhvr6NdDXL2nj2oy2d6eP+h6aqsxdJ9mB6zojUKu+/F6lnDLaUxDhSkdGb1CgKYaXpa4A5fT0lNmnsOGIUK95UqfMASMEru37l8BbLcpoIMJDgLDYIcwGtUjPrYaOVSYV1zLTVFi2ZyXOm/yIlNO3+zmHaJmn/SpI6+egLyGwpN1qnpSVJNfJapoxl00ApYxJZ1MZ4SSI49JQYkGkN8U4UafXT4BfXcmM4noJAJ7VNapNvVzATYcuIZSCTI4zl5nCxcktILEEWU0L3JWeo/mPz80dirsJtQVpSDA49jOGfaAxE+BotqTTMGdbQsJn+1NT0ErzRMkmp7jfMk3zF+zYpv9jyjxLA/mLMnT/ea0OctgHbVzLZmqpv9V+nSyNPPQOgHi9svavcx3P6inR9cPQM4uJSK+ijkONo4Br0MQM68OMxTqSzxGXIPf5JbGsnodTzk0KGNBqlwWW3HCsSq3bpViMjWEAfIcSxemW3MB02H1j8Oi14AFWyBlhP6x4UsZORm3fIWabxKgxhA4Lm2j38Mpywih9vRvf05XRTXvd+CLQlVPQQPBfK+t2EuKg9uji4iVpgG2I52W06MZtX5CqrGA2HbN5N+/q4YIaSXb1rzNpT7Ux0OhDgEAf2JL5G7x7utMFVTORUCOp3g6hwLXgNc0MWjwYaTg0fsg5BkkdJSxFqQ38czLPvXu4w47ohs+EYYMaJFYs4QIkNigzQYDYaxNaGBdWS+aquGbrse2a5Q5856Ad2EngUSqLgZ68vieZNbHhMppz/Bogcla0uYyCYtwI9PqKfe96zMhqheQE90y6J7HJTZ/m1h6BEasv7M3rbQtLLyzePassgZ7POTLKlSR2YLZCsAl/Cn4tB7hVG5SQEh+Qp53DpF5HUyix8gCgUpuDtY7+E3LfSCUBLdgNqr2K0icSZhPwZHY6fmAF1fYsFs7KEpOOlzb02sQvRi6xOOWg4japzcSOtZDm7CmJObMD+LxqYCPpdV3Pi2t+LsTpkpYhtq9on9E+BUcUNGOo8xFgjUx/h7KDNxUApuiOc+fQg9PF8EE8ltejxAG/cz/AtTxqfHx+fjktzL3KQHxtfFpir9Y7uGE7kvxyAFRbQ5oL+vcd7Y9BZoY8cyi5FaTmXtn8nYGGpcxS/qAbn0b4oJ8+4C9XfavbNRhb7erdGQocjD2AgUr+hhsXJ/x0cdYykEiPFRfXPZysTYLEphisWnL161lkIQW+WIc796+xzdHQrQ61JXO+M2ik2ul1DOJjYI9dEc4kqFaUi3D3qMA95t3U7XaBNXQV9GsD/vihTkEQwKxvDqlLTEN3XgXeIwjTVry8e/e+jY1oVtIoprXXhsqaKaZggy79E+gSIFWtou3figOMsEDW2D0oHVI2F82gTbazOVRw3B+GkbY53FsCF3yCh3wP/DoIj83taAK56DCzE/gT13FSNpU7GdRcdzEQbhpwfyYHVvQj1JF26u9iXlLyZl9adcirsHTpIYc0FqRJ5+9NyOz+dgnn+wD05gxk6FwEyFzgQQJfKStP0gOJ+lPo0XDEu8CAAL8uXBdHyczU4nCEr0fjPXcDZH6N+ADn9oBf23CR3JB73g7PD7FcymjFAWsPqMibCFyS5JvSah3wJ+ExPwUPZaFoJmtf0hpaG5DXHrBRd/7bXnlEHklmWLOmJoptVVBnJAnDqRmiUzXgnaHovAta25X+kbLwVmak//2AKo6l8LLQhpex48eP3yVusjzQ46IfoeMSHceBYwjcblGftXDJlYZNnY/Gwia+CGMe5BWIzoMnwOyuKCqk6Tj4A+vWkAgQcmTn5kjl0Kt07kpW2gVSdLaO60XPovdi1XMBo/8t8HJ5GP9JbUU7qt0KPKw/FGMXgMYITM4vZfGOjursgrKvw9AJdAfYQ7odLtiicMNpywImcPlPIznFJKsEnifNNPIpDdPpl6/etapbRC9gu+5456iz5ukxMMgq914GQ1EEYwENBgiwSgMb24ZFCh/KJQ6TZK9XLYaGilYGreTwJ++Yn38I1AsDROcBHox5whQJHLFX5GfzDPq84JM0/lkhOQs9G2xXBzjEX2Z8W2q1rXjmJhYZjCazrSYzm7LVCnNpEABkomj3aRJQvYBqUTIQzufdIKkmf+G/UODkDT7/JNzsvTnqE0PXxXuwsphDbv8lkO6Za+3JZjxi5II3IxIzKAOx0dC+LQ7b9iaUslUR3l5d9a2FGINS1nfry4UW6KTnv1tGv41FDojk7bGQTLykHHBSEuGR0EBO88idYUSKwS3rU7i/isGfyaiwvtHC4ycGu6gX5t3Mqn9N+0dAc1ILMqxwYzwkzSINgyQLAY2wnIr7wcvvDMMg7nkmn7sQMSk9vYzGUnoPoBJm7GL0d0XwJnnWcX9zCrAkM6OpiNrcWFh4y6JHDBfwDBtQaQSAS0XYclywcgza8/cwZgUYtWPaLZlF2CF02diMYjiLnZEBzAzzcBBHLV/awv3RiCLCeM9kAa5O3z+MTFsPJF/n/gdxVNG7BUvQsZZr645h/3jNmjWAFVk9Caa6i5ow0M9L7xCaslVkjaROwZVO8Bs2EVynrsODwWBXEfduz3OpF6YB23FakD9p6OaZ+TKuTD9p1fbWiQcQ2/FjF7DFGRgI17rJj8NmqPJ+/Op3MikZV30FbfrvYQR7UluugkjucsO7p7zg5BEv+g/Qzsfb8HJ4LQ2wn/tdYdamY5vMu/G6Gx/xEsyAhKz3Ar2wDpiDX/02aP9nQA8aW1rwvQzeg8UksNODH3Djc9MyuuWgl/wB3OuxSUU3dgvnMzLKV/NLbrxXRsfUvY5itg+DlfGu/i3Mp2ILRbDvq4CkD48Q6XwW5xpmDnTSmR8FUK4F0eDB4NNki8qSHVT2+eOnc7MFx9XHpGsaE40KwOOoYEROKULyJuJSJMGG0jEAeXkBLJbyzi0NnTO/WKLCdm/a2JjFKPRxMY49ZmBiRuG8lFb3DukqVrfHsa00sFzxk0/kW67TGdLWh0peKAvxmvCiYgi9UvL6zcyeNDId/zdH0kVDjR8FJ7KevjHn+zScfBbgdlWEeyZl9CYY12N5eFmpCqeHvLxeahkJCxxO0OeEn5aSvoz2ifCE4m4n1Fi/f0qeyaW4dZMvrWpIyTleVVbeAVzz7SxjkmodsEPL2saOzMk2o87c1DUAex0oCskaCn+HJmnzxATmLnIxMxAFa92kLqvjhnl3RGsLrjGVNGZ0Ny3kPDZeRUmj5dvlqRQVFnu+fWgDzRz8A0Q9yUsfMOl9GX2+u4Q78n8V1qpj6HZh1/4YO7negqtXbcyreQ/FeYJ5JwrQnIPdac0GJPR1YDef1kpgwVvaOvjsm7XbtLu0B7VHNy3Ylo9SvPO6+K56ZtZ9+/RQ2DL+mY7QJUxkYxplMG/zXJbkGP7V1d83ITnjGmabB0/CTO1HTg5MhpK8c+DKj0hZUW8PW8DoZBmFKEE+q7g6y+8qM9OyuNM/cja0u4nu1aKALuX1wljg2b35FNXWPz7mWow4pnJl1DWtaMIF147juBiGP6Uju2eaYVEQ5lhxYNozkcmtnjBgxGjddoQHNHepUnmpp0+42B1f8NC8g4OJeV/vlCwsFrJoy5AhelqcZQeeUlpJrhVNUNOlwYKKJ3ZUPK2KMoKWxZXXN3M7W2m/9mayJ95CncOE5c6g6DDkmBG9NG6Q9h4X+cAvKmZMfdOZqmMYYWNq2p+VhOxn4Pi9qFDydH1yCRUXPawnU44e1WNDr9T0qGlXJeIOTnJWROiULe3I6mT8ecYgNgOddyKrHnlW+Y0/QiP3ZfkE1w/H3GgzwgCBCa9sFVyKb8N6MFk0BDEMq8THXcxbITOiGGMb2mzkAX8UAsvwK5zq+TGf6w5zdZ/Y3Co4JgN2LqTr6zTsUYOVRxHZ72YaN6MispsxrFFMdhsyxRUdTEUG0ncQmvR7kp1BOtCTADExhwpOmDqR4xTmZPDInhy2KkuAgIOZCTTWlSpL8RJdPy2DS/58OzTrU+B3F+dQa9HZDx9d1FH+DzATQpQqPcJs2q0mPqYAJy1furyaVoklxtMGB24GCPOkZQJZrRhAnMRk2KlTzOpNzOBKheKDq9/U/gH6W8myk4MsCr3tkIdWbzs1qzCPgpLp0ialH8KtLAhEXiziD68Po7Le17vYhrb3wL/ezYO6I8Ie6pX3IBSTYkz0FFUK4gzQ3xkZ3brPSz4B1z7cVXqZylq/jKtAvCd0QIkTIqxF7PGJidvTSY+SPQ/b5OEQAIuJEb9d/Kgb2CqL01VOrIuZ6qEEG/8d8JUqMlnWFrUT3ysymcXUr8vbZCH1JhlE80uJ1UONQb+ZxXE3czld7AkkLK+NvZi6FhJgVcdJYO+WQcjvOgsY2I3LkxFqFiZ2oY1CTEx2RwI2PucFaBIlTb3mIK8HNOhTui0FL/1kAwclIS16D1YY2JsMcm3L9szxYmpg0+b5BsVhpdrRZ23T1b35RjfA+bGSBKppaQSYwdcyRKLi7KpSYQ+wzcMqR7NpHTZ1Pc3ypmSozqqrW4HqYWIKbMgquJnrWBtwthKanHV9DvQyX7JiSg4z3ynEuaWStDxqIPdhX3ejOBzPlfAFXD06LSYfhmcu5y0zYnglGZcbEU7nI2natmu1/NDXMXZMbCx60nL5J/VSGJX3hUKXUZZUqcJjGH39O/DWvwkWbzbz1jcmPgaq4TCZgxebQpJ9Gp0iPmBd37WFX+7pvUSUcS7xaF84509Qj/E4V/GNvF+b05eSxKCCJJ7zEcVFKlE53ypMg7+bSFp6mu+0MFhrcny8bRs6S8vtGDea/Q2M13utgyozwK5+A2zJp7KclNQi7QD4uTPafaPoi3oO5W+Yu+0Mse9gi0Fk0tW8Ni21TW7ia5FVEm0hqxf27dt3LHDyBb21X69LLk+DI5O2voZKJT0fIfeSDA97lNlTGcoqgGtLRVDYVTW3Egb1w03OvFzZEsx9e7ncrFT60iyFuBnjOCg0xwvTOZry7vHdRVzde38BqAkDiwKDtE6JvS+ypAkMlu4xLz9eaEl7G3xFohiucT8sBsPaiK8Ae/mC5gN32TVENsMB2Q78p5uDMnpv7cwZDchwqPQsigLm9sC4vh7p3KlQZTmENzELPU8NVFKVKtLfsPicyjnKWwbUb7f0mQLRvYmQulG0LIyiMCPdvqICfG8oHKzQkgM2NrTLoFVrfkAffegYXeqWbBy3+p3TiVfOpT70YRXm+PPaH2bx9BRQ8otG0/uj+b7p0eeBlVymoY28R+FxYDC4q/W2KceR5J3goErRJxcWds3Pq7ERWa3Ml7QFGJlT2r0ZMlLxoWRE25PMeKkkynDE2h0XeNbmqxIXuHokEGpQB0uZsVsaxCOXNPxDRrj93fqMiE5ikoiAhWGQkzBhLNcMgrES6jUY7lJnFaGyxSYRSinqiWgNVSkVr4tokOtyEpBGUc4WKy53yo6Tsx2n3C012miqa75VpiYQ/GoQge8Lgrw9i1LQsDjlYCy8UrFqGtSSOd2guNl0SFwx3GABUzdMywI360YUCWO+qTODOHaMxwKdsFDvv8+wqCFcczMr892RhR9VFoE2pFuhm4WRdd8ZEhxGz9UnHZT4jnA6OiBho8Ash3UdaYMvolMiOI58tACdYv8B26BD1Q828JTrW+IuIISSsP2RQw+I8O7IJ1XvjPI3BLiZiht8GeZwOqtlWAEe/BDMn0Jig63k8qjC6Fp8oNBXeyg5nU0TkUWpRuU5SRaT2fzS1tnI2j77KNLnesh1p2RwAhDFPJkqoHLZGlbjoAAwLXIftTzy3GPq8Swwa2uSewuxyTCmvg1knuWeAFBqLHG3n2eW5eSIAAQfr3luNaGBZVdE2LBJJUpyBrZjD/Tep2Z1L6EAZ/UgyIVhPo6fdzAg3sBxTQYmJLIxazQ54pbjU5c6DiOupbtdYLLDHMOXAcNm+foom7Et7VlFo4oeicDxrqHvWdHy1mU8hdxp5tk2B/ujxEpYsf8UDINb1ituVrmSEOn4E7mABL9057EO7h6cLZZUiuZ9tldKXAMw078/+EhCJm+eqOGZgzptVhiZc/wi44WyyhAPZe2LgLz37fSpIzI42JGV2YkLd+T0h9hZTVj2dgF5KU7rOAbX49HzqsoGpmrOHU9x2LETlawHVDQlwgOy63umrAO45cF8rhY8XEhx+VZPGvWQkbd1wojUHIHZA2NLAQ72VTCr2KFDuiKcWLAM5myMXeLBZiT6m8C/LK0JnHL3KErfIGmGaHc2euR9BqNw+bo+mpdhjkPZ3tsf48Fll90WFnWbWDO7AK+0iQpjvEOEl0JWix9m/uM/8DuhpyPvbbakyPsBK8w9CbzogJN4XDgwRyGaDZSlFbgeYOm9u9H4Unl9bSGmMCtOSi1eWV8qZlz9b0CvPq3lQVY6Q6a4NdQ7ywebg7SRZZ2G8aW3gAarSsUnl+HlfTK696gbPz6IadnHhdeWuenYQSVJSDW/gtkdIMzRHW58i9fy9GYhDlzazKtZF1mOX+UXlke4I1Pk60MDL17Js7RZyfPkhax04aTyRntUZuOgcB7lrRKnszDHDg+6YzpYI1ROZQO9H/zODxWtImbHMduLT+hiXqeNPMwoD6akEZ6OKOcA6vkh38xs4Fez/LuKB/e3RycZukcw/u1hmzrKCqWNYYx+xOpgwsHqrKPPgfSNowM4qWLdWxahPLYX2vQuNON4wWAKFBe4Gerg0AEnFlQCEdFK/tTZPo723j7JJA912ecBn58/4ctxJqxeYLmDnl5eeyhmJs+qUb+ifVv7I8BCRVU1dH20MiVZcgynN6i9GtFRtNK/33HvzNuIxJPjerWTsJqcJ6ZuWsQEUxyAc/qV+332qmdZ9VSROW4oSKXfq7qGqEysXeFuIwc+3kp2u74gmg0y9VWwPn+cjdowi3FMu1V7RHsiY9QvUpo6Ah478HCGjZe2GN5WBeUOZWoPrFGp11aubVOVstTD5s89DOOauCAYLxd+P8kiDw+08ms5nufpBcrBcLIpagSE7ZN+j/NqoFtR1e02UWHMz9m2nfrc/pHUriDQL24BGy9TULm4gCuVyp3hJGdFBW6KzLY6AkbrTLFY72aP5znQJd/PEhMVyRwPB4J7GJtS0o7rmaZbGGBk76vM5PRisflokTFu/VJqSvKbMzN9ACwqUqVG8otZTGdcmwTPd8uWfdzBmq97UUVyW15uGHz7njWLTyMuccFLmOncIsLlxPHnYx4sVEX49PjwYeYtz+L3Kzua4pLMK5uft4njNwOPO05+Tl6KpN5eFSaLRTjpwEAGsyIMPzRp9Xpzs7Pvkl5YDv59VqzYyeuTe3VarHPSdfw8dfOq1kXlXr4Duvb7Wgl8wCGQGBW/AmkZ9F/EGMVZLcv11QUvGly8oeZAVBwTpKFN4GUP9zs2PLuo3aZ3IiRuV+BdtMrcX5kQwVvhkjuUtxiMjfHWKXim1NiP2Zqa8TUe7JGGH9L9td01NXUztcDi0PGuykABTe7Zl9TYTU/AAzyEB3P5dzCXY+CTD2g37bR4mxUgmxUUpDNYR/2hlmzpc39UlTAK+2ep74zlgE5MvC2L8bzEejPxKI9yegWNudx8qwIZhvvko8J5HLnjLhpXlYCOHjB2wI0/5NlxTa++Ro9L2PIvA9AIHZPmGDd7IeKOwVAxJNxkIXNSfHzMI7sHVtJRMQ6dSubGjquHIc5FmHDp5gGjjYMv+YusArwyyh0qrDaqtdiRtN+h8zsSmDvn6Ge67/M6KGVdlLM7WUHjO10kx0VXdN8Jr7ccOXIbG4sc0ul63OoAQ3p9SkKPzNxbWsse+4/AI8NBKu70JWjPxsh6b6ZJ0qEmLG3hPStN5sGODLVjlNCBj3Zmu0+C/IcNXMshW/AVl96uCmwsDK7tOS+J3EmX7kdO6Yik99u+zAtKJ0R4LEbFx88X7OJdOdCcpk3NqKjTUOV5iBtX36bzOZ0WPKusUzvUfdu0EhGuScaplrX/69q3sgre5WGubNskkk2buF3k969Eihb6yPY9lZU46eAUQOMZN351HLsojfTKZbDtbREempeRkvbDWXz9AcyNuIpZMeAWC9y4mzxJPXE85rgwSwJbwZ9bVJD9FoXNkVa6+l+0o4B5uKYtDRqqug9Agr60YDCKdR5YNAZQopJdzPDyqc6GdYvDHPM89Eyhpa2ASVYhf226YzvEsonRVYzFRZn1B2n5LTuO5a5mYLgrcaFonSYKD9zpoqCJxgYyclnq6UmZRoweoreafhr5d9z5S6SZ2rHy9DgPBNqgwppwPRK5VuouOY7hNUVa5pFthJNZRMWzinFafWp1VUUdRvkDFYFIYG6mgE8e244/dHbUvcL4J5s1XTviep3+4tAwZ5mPxR0lIsOKisHs7OzZ1zPHiEjCdrnx7wvndSgGDZ/M2SiPKkUPvbxsSCoV9ZAzYLR8PeJy9vl6vVOvH3dTR2LQzxi/9FjEyB5PkjcK+nSOugQYKL8rf7Pve+XwP1sl+85EMptbL/eogMFATgsslZPlEtT6ljPa09orhywLhnoz27kzhr0Zvs5KjUYYnCSZLetXQBK2Q+HXHyjR3GTOAEx20K8Rqx69e8Ew53XTNfC85RBueJY5DQ7YMXIyuJlQz8p70gY/XY4FsfPMBPPtobxwGUbBdFFvj7m67o0lyO8CrnOTCDy3rYR8SczUSvB7fsVNAAKUaETzXo/aAa0uycIEqrasDzOX+AucEY7HQmDVtsBjUoQm8YQb+YibuZqJHdao6JV2Ygpq6LqZi8KSg+NKxLGICCYlSUB6CLYDB7MV3TSIbUVyAqCVjxaLHHdzOs8LY/KsbjCDSTKsEBtWIWDAT8MKjP1D6dqjcE5brW7JDOkweTMMt8Y7MzlL2zgoq1ZQgS3lBm+9q+l8DIXUsbEBKnoXsKMKC6gjX9slHmve1V0GHnvWdm0UWIUCu+uu9VJpLOfbnLhcrIQMuMOKEyDi3ZsHm1tcr1arH2/sq1HbJXd5xMOmTvIzV/L5TIa+qv0lyJCrzQBXz1idKlgbzSk07FprRXai1sxqkXXcz5ziABwcCJXT0pHcfcjxawd34dnnQRtmhX9zV+m6fEBpwfgEMGCZ9gI9ofrU4v1TFWx6pYq/cnI+YibMJKbGvuP993suszwelKBXCwE5ecKqzES0hNkv0GKzKRwyPSmWuPCEH0rAss6Io6jaZV+LAdPOgLYPtMPa/TAnSVZ4Osy/ZnGR7eDCFmzdtmPbtTrX2red1MbaDtT1F19ZRjYVke2a/GbPTrHcuOmmmy6KVd/y7VyTSILEQwLxST79sDyqEIC8SaJ9CaWO32qhQxaXuQzqfN7z7by3yv2woNa3lfTcgUKhViy+Ayy8xCXALnPUN23LTAfmPnicojA4YyKBcbJyCz9hkwXHP+UYZSfA9CxmdU/5pubVq9o9YL0pMA9tKclW6K2jaGkaWdEpYrqRh1gUoiiYjULbC12yyFObq/WAlavf0L4KsqHqKxLAe2OZd7s279i/Eb9m9X6bBWdbFQyd06p62xYgBovwuAimj0k8LOh9ZY3eokgqcGQmaRrX9lRV7H9flXrOpU5namLiFa4fV8NPeJ7l/oLLrZ8wMfkFrOT8/YJbvqXizWqF4nezWgSFv1Xl6wlgM6eyKoztOc6mb1vddhL+bNrJ5iLBrDpDndihwFmp7Eg29rs5ggLb3g+SHUp/Xo/1NHLO64U2iss4A6Py/L5ACMqNAPCaKRYrb0byWSCSXPqfUu67JuqfYLFtG0pzwblXIirBuNNyvh5gUS1j1iDdrk5FXBCzapVW6O9ZX1/v1SkTZJdCr+vqkfHfr2fcsggyv7Jzhdt1kqoCHZmLW9xeSjR0DlU0DCLzDe63HcdrN9FG4MSJAQ0do8L5EZipSldfzqto8uOut0e1oR1w5ke/zt01x19xjaYITLYHzCce3FP0aC48v1TGu06p8EYuoW4hn4srcVaF/fWsRpJkqwbK2Zq8F09T61kgcLj8bggEr8nHRennQ9Yu6LIW+Wy+ppc7fmB35wLdnhr/4TGExvSBPoFznE76nD4mQkCg4QT6Y4m7QBG7Mx6e8nDc6wdorGxhs9p9/sCB363deWoCT53U6QFV2KPCm6EGNsUeMTwV5a6CXTmY1Zyd1R7UXqK9HKzmOmr3r7flIPabWONfw3zba4c2lelFR6IPDnoYwB6lCJZGZXq2YLrIKQsT6JHLCyKoFgqFZRSRyJFTwpazMF01lRBw1Ro9uJC3WkcC1sjpoux6rF3Ro4bw6PhcsTk4ElCLOCnKzWIOlKKlOwzbelrY9UrBgHV8FGiTCeK5kZ9PPj83N5ibmyoxSmPHT20PsxSzJ/26UIHQjkmtyVrtriyHb493fH3axbIHJr9XIVjOx7YJBNvCLDSBXucdnXGHxIBQa8BM/lz7bLZCZpMs9jdxWX+0XjPznzuPt/98XvdWD6oQvfujJKsp3r+iaouzumIKz/d2bOjEuUnpC2ssCEyPADdxA05WzhHH6nLJrf6+wHfYrVPcc7aj618Em7J6XXXlIN3B1lV79qBNzD9ckkkGOytS0GCYIR5SgthSyzX7asm0RZqNwV4gUZT7Y12kKlg7BihhkXAaEFVcCdCNs4oI9kQvsNystDpe2tb9kl0C1tWMPFEuA92xdQNhoIazOXQh79skz4OWMIJ2iXRBFyPsjBNhzcOQl1yrIMLlB8iAY90uzy6WsL/QeEc15mQyLYZ6JGux3nBEAUuUgI6qKNE/gl0VWc3KfvAlj2hXlI1pX1NOeYNXULW6URb+A10YSm3G2IYmpwEjsQdt1WSoutPmyHnAp8PkXynjE/owjTOYx1wPKY0c/7BwKEjzhKUca69XAaKA8p4IEmynFeQEKgR7hEqZj+ODRT8w0hxAyaABjUCiTrw/dt/txr8jUgz46VRMHeq8w3GwxYnj4yWZ2oSowANY7HIQnDCtKqcMIKMTmAR7ugkEycWMej6Y6vvmZb5JOTVMkq8svb5kVd8CF/NJIN3ttQNVvbR28z6L+7Gj/LCbjePnNS+rFd6xCmaIS0ZEsb3pkawdNfIfVW4yb7dKVu77cpTSHo3s8mJLnwZEQWnNoDwIPBzVX3K26JIw/46SdOgh7teaLxVmCPqJzqx4uHxiMZSO76V+0cWFUxkf/CftbrBlAGaVPcHWCohj3sFuI8CmyyMVtXSyNXpf0iJAuKcyhrHtNsgw0EduNGvbvJcsfA+NGXneXypO6MvVSRD1w7bHQrtYMK3cXSeFvzguAlVTu+ZS52aGIssym8LbGxdRQrnjtjtoHLSli7nbLTAO+mLBxW+oxnhsBas4XlGoXHd9XISHlbc8jC/UuW2IgAe78wEjRcdv24YcL4CKuJavOxMEnFwByLKZSf03tS+DtZ/P6ghuzVbRbAfltno1WjF0Y8VE5qayioohztxRCZoMo6APoQKn09CXZdD+UIQLSC6fTL1a38Mzpdll1O+Fns3oZeaeC8NIbiy/yT+JiIoNOHtl9HGAbS6OHHRrVNCpCxjD913b8kRYZhYzi2UZ6Qa2WVIkhl0OSzplbkGG8Xq5vGpbUujAH2S0e/9spDePn9Syuf6brFYkBxhK1dNevxZza3pHyfl4WAjSH65mObYCNnZShMc3ZPTzoIFngL2NIfdtbvwRlOawGz2Lc4lrPzTlOpwsivA+FZm4Dx+Sie3GN88Mcjha7CW6lxQqGUb4NthdVXuZA7sz0NY341PWjnqAUcHssCpOH8D4N6b1a3PoZOQonzs6E9vefLaFwVFV9+va+aOvodUz877eW2mnLLZcWVTFKx3i0zr2PV2Q++fnn+QG2EJ6LrVCz8yfy7Pq2NzcM44PDNEQhpPHATgNfnNWDHgHDKkraqBPQ6/xqa31uHtAz+7fijzsUQkl8BH96yLr18vQsJ5jm2y56DrQtnMk1G+/bmyshQRQirHHHhXeRjMLoo9h0ZgD5lquzqO5nODY4zZuKSY2VhDOCclxjVswXxuON8bsalYVUZFIVN5ZrY7VamPZ6y2OX7Udsl9aMp/PqYIVngJ/DvJerEd2kCOmvxZbVXwy4fr8hqoXbhZgRE66liiYxfvuuEMbeZLvgC8tAHZS2Pyy9jigp50+NevKZgAVTqajcbk2dXhj2njx2rTxzoUN2UYa0TDNs7nw4ddB3zzuTYqOUD3vgAoeRrKh67PSP2S3cr6Ry5um3amhfIPlFFTXUUGN2FEA+2eA231IlhW+CBNXoIoX6D44okql8uE8YyRwwOUkHgE8VGDMioARWowLEQYF03C8hqyWCWalImbSIWZu3iHA63CSlAqFuwE8kV+MLG4ZOttX6Ed6feUl+sxMX+1nQrPqPJVbn9d+QnuP9jPaz2kf1D6sdmB6UY6eOVnwqok13DjiWtquvPOQGyxes85mxytc27+2znRo+K6tPL2h4DsLMzVIMhgGCa+LGG8S75erMefBxVlLWkjO3cY8EWPxMcSCABvojmm1pcT0Bgz9OU909AkLWO0YKk4FS3Dq5gBeFioqL4B4XV1ZVb/CKZlE6l3TEhaA3ZCURbiWd/z+nIxeBuNvwj2akyHqzK5Ni7mX1KdaJycnJz9BPEYID2yiSiIti9pBqqeerfaxoLphu9y0MXsXZ4mHE1uYtukCLM4BCBCYcYACtmGaBgMQSW3TpgZW+WZCfYcQZtrEtJhhiZA7vpBAsH1Helw3TGbl3yvhsejAY4JzIIrAuN0sovRl0JDh2s3btIez3T+k3lYb7MSZE0miYVjPRcNUdHx9wY0CUFuaMMSiowUt22tybzj5WecjyHIoNEw3bkeWhfLW7Xbg2MJH8oKS/bMZd7/7QVu84u67LzwbMnvadgfwDYoNwxWJLuNLAbOX1N4hMGCWBbTXxkJcSD2GLQLE3iaS6o5n8AgTcdRJgRMLo3EmK92LGDWdki32lkoHqtVfE6a0PQq/wwPDC7Db8gVgMp9y2wLywKjuFbTMzv4joJPPbdnZFW0v4NMbdj4gw9Da9lhltnXTkLZJQjaZ24wqnlblM5k7SW90J5cnxcwleNY8b0ZMwrj4PWD+vlfZLx32EAu8jkGnVIgnQvl36opnRHb99FKEZlbHUjs2XTevElody6MvgMWotNTLMUSrU9bCrFW8B6gUxyBemLEQeIsFRvcRn0jj53QC0lJ8gfsgX6YwRA6ltnCdOzLPo6KRCVjXb2erTzazOndsZpl37uezY43atYOkggbDmsjhGucKGq6B7o92JMj4d8ZxnkAyD6KjKsbdFMU0T8VtOZ6lar1bAFXsv9mWzHYSEz50bcd3B9PTMzSsLKN5AADc3jsjnB/dExO2kVq5g4CzdxVpXHasOhUoWwmsXo7brnHEluTWquWBBuni7PyKa8R76wcSXF2mLj6crVPyr35d+ztA1QKYeXXTEo5ohyod0ocMIgtNPo+KTh7Vk2gmoaJawKLg39sumdIpJu+pBpx9NLW5MA35Gs9IeimW4x1SBXJR72VVEHLkvUxtYVhlBVLyb6/uXNjeyOGQHuVddxbpx934PJue9KzilEPZ6ixamCQHVcmvvqpqn6nEOe6LRRl9BjtJcXymUHLjwizHiMuZ7ljfNZI+gNSkBh5kjHtmwZNRVpuj5v9zMP8DFfHMJH5HlchWHG5wHXTdzljC6R+uykNANlQS6j2OS6kgNQ5iWA3iQ8jd5aIpZX4B540Z9OC9gcvS+O6sFuygrcJwb04CbiVeAo6tVqSBj1nqKWIPXEOOpHNCRZh3BJ923HuHB1H19WrLm2dRZ6++1lTD8pAq4jqOCvakjG7e58b3Nvb61u7IRPnJA7sreAxAxxzY4xYnAHzn3Xi/b4ixjbPFLIelKi0/C3qh6oEB1y5lZnHnOGyFmBpksHNjs1W0vUeVKsVQlkUcRrpaUQfDUHPjA7qP1shxtIeMCafmPkY8gtwn5FPE1A3DZv5rI/nESs63rXwKLibnxouBVZ1a83G6iP8HBv6kYZtOKlAQy8Rwgk5JbzRAxlQW+ltZJX4pqyk4vb131jb5sDL/PtxcYRQ22l4hco3S929YMLKVyQK1BdokgFrE5BY7AFTufBjZwvDNH/RpJLDNT99MQrEC/mZZrQAe1JQ3OAF8C8n6Mpj1yEg+7UhXL/Kq0xbMajkBKuiFQCKHF1tSDx3MoyOl0iMGtHBXZOZWdptRJyR5argVtTTEKjCT5Zm0Oq5aRA29j7J8sFqZxLWutpTtJzKK3mf2+HvWSZBt2qICbKN6dX2U1WhsLiGO0kUXyLxzOLBsD8sI+ucUcd4Os0I5YNjedMHn4gwcrzr+aSp/BY3pxSqOQeZEXKYeC6YXeVBDH3X8qBgSoctUr20s5K3SdKmsNyd1uthwyAHbc7wgqW88li465IOOz0R114k9SwkOW5O31qlhEzDpKzw47VSyef9aVvmqVjz1tEOqXv/fjh1uB12a11S4zg95+GD4Z5gS/BmUs8e4v6/IfbUvwpzKsfkwrQeYZEgePvzzB/kid6gs8MRmNhhmFkfjnASsNG3LThcde3dXCNLgwZRa3zOF2ceSFbX81eomAHa9k7t2OQZ2WYBlSzetYFfFMcerKPSEOfZ4qDNulDVD49lugsOVXW21Q8W1VavbSxKuC6FlMaXBQrLUH+mkNdgMqk1gVwrLnsJTptoihajKEY8+xirzrjXjVvp6pWlPV8tyxgHq5jBvV0kHBW5F3h/qFDxr+afHcV96hn82W3Zwylr3DOSMz2/UcXG19rrULYZIH3OcKohJ/nTk0LlCOcokVe1/8DnAHoXRnhsvtvPN0A1lPBncxIvMWx9m4LmsPu5V/Rw1kolZ1G9IiY45Rly7D/TtXsU35H33lUuTedBWApNhnTxx0KH1Xo4ZXmW2ZwRJGCVXonqsllaVbcfibDA+DsgoAFvyjVEeXKEktTZiz4vsD3VtzcdO+zFyd9tkQiH89WWx69XwPARsapBltXYpG6GswxRMhmvdLH4O2+AFDPR+5rvRUzV4tNWahskE3EIELmO/JKIrSdijZrI7JnfqBFSpn/Mc8KgcaOWwFvyvs0iMma1FPrHpe8mmZwUbYO1c3AvnsrV+WysjOt+zqOrf4ZhKUpsFsB7aM+tootmjzM6RGqNP8OBOn/l7QxZETd8JCxOL6I6sSDXViyJhdHy01VgsmWd/QeduUL4b8JtnHKwahRkDEFwrUst2OM4VXZyGUZS0o7ZntKRo0tU97SKeXtdpveyQSVVhJdNqtmbHHyENNV9cy4MV2F4zkQWURvg96/bWOpZkYaQo/aXBKISww+gPqcLpBx544A4/CpP7Ipu+VEbvqlRxqdKixs3QgTQdXxHmQqe/99BYIJMWmvAsD+TyPuYz5J394Vyuks8vJ1Kk75SGLaNGgJPJp4HKeEbqpQbiVrEC6CkfJqUYNzqYJr2sjJXXuWUprLZ09e/Asv1R1q90R5+u04eDzz777JsBWj6T7Y3w7K8fP37bnXe+YvX8mg084fvKOVoINY1llSn/kGXflVR3snXqu7T1LEq/s3z8xXZsGSWSNteEjnzL1q4gmf+8sVJxT1YuOFAbIcATO34VOf7jktbBCU4GrFjW3fxEzuG2e3xuDs23Qn9er+d1P7GRO6NwQVp6b6FQA9BfrR7LAzVIA/C9Occ/7/g6Pa/Tl3q4Boaz5gFVLITfPz+/XEoXXomRs7s56WJZn3xIbQhHPhxRofYacUAzVG1Od7NmqjncKWMTW924Zh/evBX5aILR1fZhtC8DVhcBUx2Q0T31vSFeKpoVHjRaoTUJLTpz85gxdTtmxRqnHRndHquF+acu8yDbH+Cr2dphVUe9lHnnzZqBLTQ8ckQKF2+Gi69ZOQwNfoqGQkSWy2MhOwXfiKsi7BX1QtfzAiDOYL69D0QBDutWTAV31U6GH+ouRdHKmLk0F3glinWbF/2kZBu8EedWwOfacrIc6cVDRWGWUmo9Ce63FA336FJ7kCitaoJnXdjUqs61WaQbvOwOGclWGpAbl0C/pbcMIKEHT58HMzkR/hAM66LK6PdCu1HVg+ZCzydO0X8OxR72ADSCtnGK3nbokFV2Q4dMtbh0LHCoh1SY+hBm93pGF1jvDMn7rhNW3YN176GSh6Pd065RDS3rKTuXrYP/K+1ftC+MUILawWNnrnfHjinDJTFbwZnhApoFK9mOVsdDNKiqFlXpblPk0aTB3akidGJ3WoyXHddP+jqICgJecgqTnPQB3aWAX3B0oFjsoomYGUs9fSpXfCGIdc5DrOu8H5BIl/OYTWMMqMAOdGN2OeYkbcpoISxXfCOo153J2SRtx2BF4nRlWuBwP4Ap9ntUCpmPFcvPARr8W5g5qoVaDfDCMDYylPms2GBrL7l0sFltl6UghomVG7eFyLydyqmktKQSK+iQbe0X4cU4wU0vDfIFFhRuQ+4pFSe+nXmPNWVrIAJSfjy0ya/l/fdJcF8fCbwvRI4I24E+tVf2ba9cjTeO+iQNLe+Uz2ypZ7WNOIt6qiqtXraHlDYY7gVxI3HsX7/bbJYGuz5rsDTMwCw2hxtsOHsdb0Vf4v4x2cu7RrFOTTYz0WnRhlo7oSehxL5nxKeDQ8iiemrSDcf7RUTKid7Ty8ThzVZEXcN5IQJjBsJbrRkY27Jbao/b2GvVgWE2SkLPd31eB5zKMHeZ468enPLx5HrAPezv9SJZjLUM030j81pzMDt7szo7i/xr/DgL3G5r0eZmCiTdWrS/Y/E+DMwnZoGLPiyCe9P5WFppT7T98THUarBVRk8arCuIN81c3pPPiUCvFZm9z6P9CVSb6njOvj6amV907ZXDE8t3vhsjIUSYizDGjFWTpAZGpy50aoW6FaAmdClvIW4DE0vluKe3HUM0pKf3qCFrzNcbvm5YHbC9w70rv6gdHFbdDUabmg6ynePUhmoDcs1qkht47s7SUHj9PtPaAPxHiZuiJhoPCd0PSpDt7DCbotJy6TRM6PlhzDNve8Kb4uWyT8vxHhGcD8LbZHCHCGxp1nOOLm2DeW3KCfAa6gLNEmNFzGAyazajecevF8GbVBMe+K4Is8jGN4BrflbLAUPeykzuCAqPCkSyKVILSsY39PpaqJaV/JYb3w8CwfGGx9PXPFDxSLt0Zm3MyB0FLu7GfmrbeCKX9rRsXzflr7+sVVX8ZPMu12512bwmhj1qhD4ytD8OCitBQ9OiETGb3CLC0yrbtCDCD6rF4xH2C9KIgJZJ0q4z16qEr2slen3DF2YMU74kwjkBCMyltfT+UqSn/XG1MWbIrCzL/13Q0T/I1tD1v8cK6cE1jczOrqFhPUKD1PVAYsd06tjzLWkiBhykAUpTepkXHqq44UbP9d8B58YS1pygzm+r9c4s/bGaniTSNcWZLJJ31I1vAWescmOXSpy85Jez/Y4aIG1/le1frdZZqh1Ed924gmRr0dzWXmbJ5j66SvG24M0lJNX+MHKCuHS51+uluSqHtwNFP3KNfLiKpyonV1bQ6tmCIEFEKveVKROWMTc3WFh4u9tq3z1tA3b/1QAYo2kie601I/9Tv79azC1omVf6ZhaTNbN43LCcajiPm6l/hc2zZF37XI0IO6ikrivbt+ZeopZOORHvHg/u3Uj0Yyuf8/ywmHyUJ8C1by1hxqPewZRSwxKO8ugcuNefa5+B+Ruuu5gaIb+sx1tBna2V6sP47ibpUQsS37p/vCD3A2nZR5o4lMh9QZUppnrl0obY/0MI7Xt1v/+cjsTCBie7Cv22Xt24EtgOpx/JE9/8xAAeaxNOf7iaV60a+EJWe7iY7ZC/XRW9Y3629pG8fsvaoeXP4H3T+mugUjKbEpce2vVjQPCstuVTmwM19ILyvD4XV0LQbxivLpF2mbNx0uSplSakYj8YhKosEL/61auO5ZUdTg5VbDeIeFviuCGM6ipm7TforHKgmlUlfF37G2i3BSO4uRZOpXRHJSwjw7a0bdjct4jwLdyZVdtWPiPCH8EvowHTaeyjBkpqNnoWxGgeYMwrC75jjYlwhgs6NggNYZvmaiuLVZvZKtFhVUFX262dylaFbyUldqzn3NzQ5JrPyDUWarjbx461rdZ1+91lBY+dPRHlc7a36FFuWlbMXB9H0SXmOUbok4g5i467IKjAlnUF5CGN9WLNDm3B7EsoJgn3F2zHLNWpAsxJqBKxpk2+wE3P9hh3PUG4C6M7FonQxmkjlYYPHwhXePSeY45Ni+7elON0KvVw6LAwlA5xeXCfYKYkhqFb92AWSl+QKFS1B87Vb2X7amNtQzugHclQwxZPuhbdtS1yjc38nqtrskI0eL5p/jWqFjPLri2mc7e4Li/W9rnhhbks+Tu/JIJTN3nRH6lKvwW1tGYOHmL+7mJxKjeZ0hSznI4MLGiAMC7IKIVZncgTXhRhRW09UQHKYB3OKldrhYKW9eirWTyvCBy+o01nO2kOGcB2cG5pKzY3rCUdbVU4pPhbiz76m1nfbPPcLWy0WeW+uQhksfNFmMaanu3fSMDWCV8WauAAPiVdYKQrocuZuxDSJMKxE4gWo/0y9PSoY8yoyk0k9MBmeR401c7fBR6spjzoh/kn7/Fi0r9977KL/bGpAz2w2IFh7hLhAvPifHjJcZNiYHGj6esF8L17dNrErAS2XIaq3IIHDoY7B01A+i7hNPt/c0zy4Q/+2c/++P3u7u9qzvA/0vm1d+177Y7/TebPAeV/WVP/v44+OqUu+5Orf6J0dseZnQ8DDU/a2muzvxIsUwNeE5gDWxOApT34PXVUU9WN2VEERww8jJftIG4DYqVwtpNdy+Bqnv0dXm1rAfyaUKsI4e9qdsTh2M7OqHcyu1bAd8bhiGklOJbZHRz41wSsMbyrDS1Q33HhCmf0HTv7JReeCbRbZr+mfinKWh3BvQWcX4JXZ/Q/p+SyFtrZHWzoKc/+NxUXXtXd1OMu+PfT2l+jIrof/ao+0D+Mbfxm/Hljwvgp41smN1+wdOu89R9Jn7yCcno/fSf9HKuyt7JP2YftT9r/1TnmvIM7/BB/jn9DrIiflYfkt91H3T/x7vB+18/7HwicYBB8NozCX4xwdCH6nehf4pPxT8ffSF5IzfSZ9Cu5mdwLub/I/XP+rvynCt9f+NviLcU/Lf630q2lz5fb5ddWFiqvrPyf1Z+ufqP6z7WX175VX67/WKPR+GTzV1o3tb44dnzsdWNfbNfa97V/vTPofGy8M/4D4/+5u9b91YnGxNGJT/Vo71zvU5Prkz855U+9dervp+vTT07/9kw8c2zmh2b+drYz+6tz1bkPjaTk9dqvjEZEvZtR0QN0bu03R3JTQU9syVJnS64Q+IfO6FgHRjY1Ola7pC2Ojg04Xhkdq7j9xujYgn/HR8dEm9Rug28hg8G7m7VXj44RXP+Z0THgUe1/GR1jOP+V0TEwDECpw2MT5vtfRseWxlEyOibaGdQYHQv9g+i50bHUbjNa6pgaagSMl42OkWYY/3V0DPc1/nl0jDXDtEfH8M+Uo2O4rxmMji04PzE6hvua06Njqq2bbx4dMzj+/dExjLmlj465Ov5Abf+TV17x1OWHLz1du+vIyZsPnz1aO/jYxfNPP/XkE5fPw4fTtfnZ2bnbLz9++zNP3Hbx4WceO/fU8M3w9fTFp156+cknav3p2YXhmTueOnfh4uPnnnq09uRD/8pPTtb2Xbz8yOUnHtY+kFVzPqld0V6hPaVd1h7WLmlPw7m7wOOchLk5rJ3VjsL7g9pj2kXtPHz2FFz9BFx5fvTNafg7D2h0Fjjf7Vk1z+3aM3DFbXD9w3D0mHYOvrPzk53Hp+Gqp7SXwhn1qzXA3dPwSwvXXHMHXHFOuwBXPp791qNw3ZPaQ/83WzkJR/vg08vaI/B8Atp2Fs4/A2fVr74C/j4D7bgIf5+GMbgMxzW4h/qVp7Nz6v9IupCdv5L1SX3jHHx6Af5eycZueOX50Xcujt6fy37pStbPx+Gqp7PP1LcezH5D3U3d9bGstepbm60YfmOzHU/tuPZK1r8L0OLz2T0uZ2P3bNbu8/D64n0YvlfXnoe7PZON6IVs/q4fCfWNx7Kjcbi+C3/V2D84aveL//YT/w/6vv3rF0Zzo/ZzuwjffjobWdXL/2uQFILNBzDbMd1ljRRHIJ9A/FICtq8AHKaJYPMhfk0BipSDfZ4PTm/4UkIiSqyngmMnH0pCfAVhlwJ5BWBSAezaMmgsw8wBqcwBqsCXhvTAeRJkjhWwpNYHuhEE9cAhmgxOCcXg/ABSCdKZC1RTAvQRyIfpYD8WAE2oBIrCfFFM5XwPrKujw/2DUwuS80oNg8X91vjppgn76Wawl5pGB/g/TMhPakjXT+ecFhCYE/wj+YL/bAOm6G7/YNQyAiQCFsVRJoDlaFUWoPJApmWAdYFKiUwcphXDzaNMNzO0hfcKVCNgAkcpRkFGAWBYa4DGPoDpGLTcGULzMPIyuAFpXiifD8iXgIhvdtMIYNzB+L+1l0F2g5F3UMSGBtnIHYxsrhlAQtllByMLiMUCYrGCWa4JQBaIywTnMoG4zCAuE4jLDOIywGUZQFxGEJcBxGVUdmHUhgAGBgDiDGAVCmVuZHN0cmVhbQplbmRvYmoKCjYgMCBvYmoKMTgxMjAKZW5kb2JqCgo3IDAgb2JqCjw8L1R5cGUvRm9udERlc2NyaXB0b3IvRm9udE5hbWUvQkFBQUFBK1NpbVN1bgovRmxhZ3MgNAovRm9udEJCb3hbLTcgLTE0NCAxMDAwIDg2MF0vSXRhbGljQW5nbGUgMAovQXNjZW50IDg1OQovRGVzY2VudCAtMTQwCi9DYXBIZWlnaHQgODU5Ci9TdGVtViA4MAovRm9udEZpbGUyIDUgMCBSCj4+CmVuZG9iagoKOCAwIG9iago8PC9MZW5ndGggODk3L0ZpbHRlci9GbGF0ZURlY29kZT4+CnN0cmVhbQp4nF2VS2/rNhCF9/4VWt4uLsz3kEBgQKIoIIs+0LQ/wLGZ1EAjG4qzyL8vz+Ht6y4SHEvD4ceZM9Q+P86P6+W+/2W7np7qfXi5rOetvl8/tlMdnuvrZd1pM5wvp/u3X/x/ejvedvu29unz/V7fHteX68PDbv9re/d+3z6HL+P5+lx/2O1/3s51u6yvw5ff81P7/fRxu/1Z3+p6H9TucBjO9aXl+fF4++n4Vvdc9fXx3F5f7p9f25J/A377vNXB8LfuKKfrub7fjqe6HdfXuntQ6jA8LMthV9fzd++0Un3N88vpj+PWYnWL9Vb5Q9OmaaWMgrZ4nqyDdtClvW7aN+2KmaEDni/ZQgvz2Ak6Np1CYHxqOsTMnCO0FO41QZuROmOtcyP0zDyezwsZZuZfGKOwr+7n04jX4A+zw74a/HGasVaDPygj0OQ3hjHgF2/5nPxFcEYN/rREavD7HDU0+aUdqemRPCZBk98vARr8kmbGzz0/mHUhz8K9wJ9UQR4D/uS7Zv1LxFmM4dkZbyzzZJzFOOqWoulef9bZgD/kiRr8bnGMidw3M2dijwRshvwqMwb8koVrWX+J1DPXTgW611+Rh/W3OTdtwR9KQk8t+FMU1MSCX0LBWkv+MmEv65hzBINl/aeEnJb8YsFpO79H3Wyvv4N/bOo94nP6xwRw2u4fRwbwx5SZn/xa+Bz8oRUdGvziFPrlFGtiwObon7BgL8f6Tx59cfS/GGpHvymc3fnuQ/jE0T/GwZMO/LJoxtP/40gN/jj3PKx/TOBx4LeKM+Uy/bAwJ/0TZsaDv5WHnKz/wpnyimdkfk//6AU19PT/EpDfk79wdjz9nzTO7n3fF7XygfH0gOf8lpExrL+lHzz5l0INfokBdfATGRI1/RPoN0/+3mtf6HnOqWf9vYOfA+ufFc4VOj/7G8hvIs4YOL9isDaQf0Gt2o327eYy6f8XWWBjikVRAo3lRzQmsDG9EKEPxsSkNJZjswMb06EDjRV7ntwHgHlm5uTlFWgs4UCGpYOiEKI4nBxa0TR0QuHE0Kwa+cX2AUazpQ82h018HzAcXtgYpcAsHIxCBol9SLhX6kPOnByMQKPLxHheiMKLydN8wos1sbhSek7GszGTQeMjLyadsVfkYPhJ/bfo+KDgi/f3h2o4fWxb+0jxs8ivE75Ll7X+8+W8XW9Yxb+/AGdSrukKZW5kc3RyZWFtCmVuZG9iagoKOSAwIG9iago8PC9UeXBlL0ZvbnQvU3VidHlwZS9UcnVlVHlwZS9CYXNlRm9udC9CQUFBQUErU2ltU3VuCi9GaXJzdENoYXIgMAovTGFzdENoYXIgMTI5Ci9XaWR0aHNbMTAwMCAxMDAwIDUwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwCjEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAKMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMAoxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwCjEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAKMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMAoxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwCjEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAgMTAwMCAxMDAwIDEwMDAKMTAwMCAxMDAwIF0KL0ZvbnREZXNjcmlwdG9yIDcgMCBSCi9Ub1VuaWNvZGUgOCAwIFIKPj4KZW5kb2JqCgoxMCAwIG9iago8PC9MZW5ndGggMTEgMCBSL0ZpbHRlci9GbGF0ZURlY29kZS9MZW5ndGgxIDE5MDE2Pj4Kc3RyZWFtCnic7Xt5YNTVtfC59/5mS2bLZJnJOr/MTCZhJiHJJCEJSzJZBWIwQMQEiWQ3gZCEJIC4BVQUg1ZarQpqxbovLZOJ4gAueWqrPhewttUqBVRa1wivRf1qIfOde2cI0PIevvf++P74Or+ce86959xzzz3n3OWXSYYG1nSAFjYAA2/bqpb+7Pg4EwC8CUBMbWuH5Gv+/OXNSB8GUDs7+y9f1dYpnwSIUAMo/nJ5z/rON9giM4DhBMKfuzpa2rN8K/UA1iOoY0YXNtRPrlcByElYd3StGrpip5Q8A+terBf19LW1FCSVJ2L9CqynrGq5ot/F1Khbvhvrcm/Lqo5D5D+akQwAxE3r7xscehkcQVTN9cv9Ax39D8y+xgFQKAFEl2EbwYd/tEgqeZ0ySaFUqTURkVod/H/4UfwIrIoLBSSxOwB9HfwIAf0X/GxyfvCEYiXYJ1cED7NodJYjBOFPGtwADvgM7oQXoQneoAyqyHRoAIlYIB4oKYYaYgQzKEgEZIAdaqAOYmE+/InoYCfkwhekGjaSNLgI7gUbLIA4KIMfww5yQfBz2Ajvkm54Ens/RryQDheSucFDsBDqgs/iGACz4C7YTvRgRU4EsQcPooZBuAn2wO8hCEvhbsUO1FIHi6A3+Cwsg3fIUnJpMAnmQS9cC3fDA/A8HCGbybikCDZDAbTCAFGRaJLBrgs+BkWK9zXPBF8J7gcjyj+AWr+ibqk6+DV44TOJBLtwNURDHj698HPYBQeIhRSwCtBDPo7VBFfDTpaBNs6Fm3Fue8hVZCfTBx/C2RRCGwzDYXIFGaepivcVx4JXggnnl4+WjsBD8G/wMnyJ2qpJPVs1WRpcgPmqBjdU4Ug3wI3wS/TcS/i8QgwklcxDzf9GDpKPWC/7M2p+FCbgW/g/JIN0k2tpKb1O4Tm5MfgMOHGGXtQxDy6BHniKOImXXIp976Xr6LV0mO1iB6QM6WiwKPgyKCEbZa+DJ3Beb8O78B7Gq5rUkt/Ta9mY4sbgVWhvNnThLG6Ah2E3fEMUREO0JIbIJI8U4syuIuPkI5pM7bSBtbKdiluC64O3QirmShN0YM8VcD1sgmdhH3wMX8IEScCe2dizlNSRW8lt5BW6j13ClrE7Ja90p/Sk9JJ0QhGleGnyncnD6HWuJwdq8WmCTrgSfR3A52X4gDCSSFJQ0xwyHzUtJ53karKV/JQ8SB4hu8irZD/5nBwlf6MWegu9g+6lv6L76H6WzFyskt3P3pRSpQ+kv6taTiZPvjh5NBgZdAfzgluD9wY/DE6IKCRhxpdCBWbXStwPb4Ct8FO4D33+NLwFv8O8OySeI3AMY/B3osRsikeLbMRO0kkmzu4S0kDWkRFyO3mI/Jp8RI6QExSoltrwcdEZdD5dRq+jX9ETLILZWRm7gt3FfsO+l9YrPPg8qXhGcUx5RJWmfvPEPScPTsJk9+Sdk/cECzAXlZh50bjm8qEcc24+RrkdVuMzAGthHfroSvT4vZg5O8EPe+E13L334fMhHBD28udzjMRxOAmThGI8FUSNT8j2HIxMBWZLM+nA2Iaeq8h15GZyNz73kJ+RB9C/75DfkHfJIfIJ+QbnBDSLltELcEZ19FLahM9y2kY30i30aXzepr+nH9KP6ffMyKKYlaWzKnY528xGmI89zX7Lfic5pTJprrRSelV6B2c+VzFPsVzRptiieEDxoOIlxb8rjiiCytuVP1cGlJ+pIlQzVHWqetXNqsdVe1UHVEF1OuZTLVo/7Yxt7nZyqZRNt5IgDeC8X6BD7A16B3nyrJ1wBC1oh+U0wJ6n9129lX3MnqLXAUiVgj0Hd7E34Tl4U/GuFKv4DF6lCfA17od3sBb6At1GLWQGmyVtkt7EXWc92vkgPURVdCdKfInRWA4Xk3j4i7QEjqL/9ylG0KfV9CB5kv6azsdMfh8eonthG+yADlKI1rXDM/A9/JjsZjLZhXk3DPvhKzh82lop+2Q5LVVa6FrlTIzQbrIw+CqdFvwSV/1HZBN8yL7H3F9CFpBseAQ+waj/juQTqzQpJcI7uPOlwD2YtZ/CGK7Bf5ccuIK+gd0sH5ZKhzHm2Sdfn6xUDLHrybe0DMNpFjv3RXw3xj34btyr+D6qh52YCbiLiBX9JbxFbOjFd5UfwHa4DfawWEhjD9MNNMhek2T4CRxmF+Ko1+D+lETyUdMq6MZ5yME/Tz6EGlZAERSRVrIUKpEzF1KCq9DyR3Av8gaXBbcpGhVueJtcSGLhRdy9LOjFOxWayQmUfBrX4Ycwl2yBscl2GMdzxULSiAezaUKxVrFV8YTiacULireUuXAFrtp7MIofw3E8NWTShr74Ar7DXC/H1ZOJ66cMrZiLZ1gPbWTPQwVJgH7cAzNw3y5HHyzFSA6iluvgFlxPD+MZ8jYcI0ayDF6A93HlmHGdt+H4atRTAxdj1AfhEdwdrydj2NIOKeBCP31P9KSIDuF4fJ+9E/fZcbTpAPwZd46gsCuTzCKVGL02+I6vZRxhBtSRUTyTd0ExnpSV7E34EzjwdC3HNfoQ9mvG3NBDMhQrPiEUMicXBItoN3uexOFpqMesqseTfQ5ZjVYYcB4nIZZcBAWTF6C2J3Evq1M8jKevG0+GWBorXaK4GO3+AE+yt2Eg2EC2q3AFeMsvrveWlsyZPWtmcVFhQX6eJzcne3pWpts1LSPdmeaw21Jla0pyUmJCvMUcFxsTbYoyGvQ6bWSERq1SKiRGCWRW2aubZZ+z2Sc57XPnZvG6vQUbWs5oaPbJ2FR9toxPbhZi8tmSXpTs/AdJb0jSOyVJjPJsmJ2VKVfZZd9blXY5QJYubED61kp7o+ybEHStoLcKWod0aip2kKssXZWyjzTLVb7qtV0jVc2VqG40MqLCXtERkZUJoxGRSEYi5TPb+0eJuYQIgpqrZo5SUOvQKF+CvbLKF2+v5Bb4WFpVS7uvbmFDVWViampjVqaPVLTZW31gL/cZ3EIEKsQwPmWFTyWGkbv5bGCLPJo5PnJLwAitzW5tu729ZVmDj7U08jGi3Dhupc985RHL6SoqN1U03HQmN5GNVFm6ZV4dGblJ9u1Y2HAmN5WXjY2oA/vStOrmkWoc+hZ0Ys1iGUejmxobfGQTDinzmfBZhebXYa/iLc0rZJ/GXm7vGlnRjKFJGPHBovWp/oQE7+7gYUiokkfqG+ypvtJEe2NLZdJoDIwsWj8W75Xjz+ZkZY4ao0KOHdUbwoRWdybRMcUTlBDnVM2iKc8SbpF9HiaET26T0ZIGO86piBcdRTDSVoRi+Gkk2MvXjhHp9mkqmkeMM3k77+9TpBnt8sg3gBlgn/jq7JaWcIsyzfgNcJLnyVSqIf8U7XO7fS4XTxFVBcYUbSwR9YKszLUBer+93ygjQvdBHfq2pXFmNro/NZUHeEvAC61Y8W1Y2BCqy9Ca6AdvtrvRR5s5Z/wUJ/ZiztlwijPVvdmOmfy0eOuJ9amdUz8GY1x0VddMH4n7L9gdIX7NYnvNwqUNctVIc9i3NfVn1UL8oilemPJFVzSwRBqmaCITXEzKZVPCvNKg9Ulp+KMUSd0eUKkxK0ULkat9xua5obIxIjX1B3YKBI/xXgKd7hY20zfTfXZ91ln1s8zTjjA0WHLSmvqlIyMRZ/GqcQcaGam2y9UjzSMtgeCGVrtstI/sxvtM+kh/VfOpiAaCe7Yk+qpvacRJdJGZmK0UykftZPPCUS/ZvHhpw24jvqturm/wU0IrmssbRx3Ia9gt46YrWulUK6/JvIZvVpjpfqoWrMTdXoANgiuJBlFvCxAQbepTbQTaAjTUZhRt+OELvaK+4cwQinXRmIUpQQm+lyuSFIB3TBWUP03Jy0pVgKm90aCQXmYQoZJeJhCvVipepuw5UgYaPHiXgMVt/Hb2ydkLjMdn156cDaVIG09gkZuTGpUalYYFSZLghMzGT3gV8HeQpXH+Zr4w+JHiEnzfdJDk3RAX3DCmichPCoSwMox1iL2NSGgTNIkzomsTbozbknBb4s1J6pVRK03ro9abbo56VPmY7mHzq+Y3EiOUceCsiCtL2hC3yXxj4g1Jz0p7UyKynV3Wdcq1urWJN0bvMagK9VEmRzIspcmEBEiMF8nUx6NMesWKZKZfEashy7OjSFRCv5M4TWm9u4kH57fAWNHg1RgirBE0ojY+/njt502JYyFqonGBsenbptojUDpROhFVXPzV8QlinDg+AcbXc3NqFq8f9agr1nsdcUlKndZpTlNrVBqqTHTq4iLSQJmERaRFnwaaBEUacbvxx+1yuzduJE2roWk1r7pJlN3ptNuUKmVsjCkuzzOjMFaptNsctCDf5MjzmEWT4pL0zGN3D/82t3TZK/du+N3age8e/sPkzmffII0v3Xb/sng5W6VYOekKvPKTtXft3jX5u239N69Zt/KXpDrwElk2XuLIzuMRmY/R1youBBme2g224PiYJSHfFgiOe2cZTfmyzWurs43bpBwkKPmjSnUCXWiRk402m0ZONtjs1j8mJJxISbaqEjJApkaDGvoJ97HLa1MbNFYN1ZTEGy1EttRZtlqYRTZaiWytsw5bt1ol6x7iAgv95VhqbwPPp+NNq2cbEU42zT7exHOKZxX69SswnkSXhIjcHLK6KeygdKfdHhUTF5eXF5takOcpnFE4oyDKblMpVfYohdYhL6h0Lu8wV8zMOjkza5YjwRDZenPJJWan4sLJHw/3pZr+/kVXfnpaWqzeZumU4mYuvJP04XqA9Mn5UgJ7GWyQTYa88aZstUGJSyPKqjSqjFHK6Gw7EK3DqgqQr72RNuKwKn9l1zmsUbyeaM+6wayKMilNUco0pzVSqdIbp5Fp3sQEUy461V9TwNHYrDn5HHtzomPz63L359KcXG9uXW5/rpRrMhArXjNdOpNXS3K0Xm2ddly7X6vQxucsWM291LQad8bdoEU18amlWq7OIgvsN1tLMXcauQMxQQeQFqK5IdHcsGjuGaLf1k40cUdPcGeXTmAT0xtn44c05eZgCsvOTEtKfJrbmexMT8u0TEsnzhQsXAlZ6SQjKS0dIJS/bp6/uF5mObylF+TbeTFsGU4Zdg5nSkMxw/H9yVfb+9OH3ZtibrHfGXOXZVvKNts9jkdiHrc94dgV85zDVBlLYHUTaUJ9jWkiphhSEc98Z3pBaiySeZ44vHTyZZDuTI/j4S7IxwywqchOc071yS9LUmKml5PNuXnzllz+eMOlv1hRW+EpXNI6w55f7PR2lC2ffGhuviUtjaaam9mHbYVS2lVz5ezr/nTDj768ypbw0JXF9V/9pXHWT/ia8OBmeK+iC5LgM69jM/0lfYqxdO1PGY2IjIgkoEg07Yh7Oo7GJVFqJhGR6qQAad5lyjb7zNQcIDY/Mam5ryN1+eoAczytVxAtC5Dj3kRQGBVUccD0riGJvJhEkhJSDIS8iAsmPnkPaSBbxbZzpGk1j3Lt8ZNNuMFgUDCI3mi1N05Xqvaa9VjEG7DQFXOvNxpPNokgm3FAlDDzgVFI4ESjwP6kqFIhewS3qihTMUFoiio2FWOVb1jo9abU1AIwFeRzd6bPCLtapSSpnhkzCvNY3YmPSd991122/eK0GQe2Xv5E8/yOyadIWk+Zy+aII8+Q6Vu7t2zXjQeaH5236ebdk8+Y3FV8Jc0PHmHPoB/xvYPM98ZrEpVWZZpmmlllSYyVY9Ms0zQqNVmnTg6QCL9JkY5oTKkzmQMswpsGXoczH7zu6VjkzcAC14wX32t24DmVkGUy2Kw2auOS+tt0ROfFhaSLz/zmP7gDv3UPYFpjNpptXkd6vo0rsXElNq6kz0ZWC2+goCBqJyrC/kNh4TeUFxi7cPwM9mo2h3uJLV+sjVbiklOtqVRp0Bv1VOmwp9mpMlIbodVo1VpJGRsXE0eV8ZYES6KFKSlhRCJM6XJPc1NlSpStFZwqLJKiza0kQ4FFqj65ldi16a1giUPKTZASRwEvXOHPRlhNVpMYlZ7abbj9pTsL8gtFuMxxCiOv891PGWU0x4kFUsieKbYN/mRJ68/mZKa6S/L2D619K6di8k0pwhlf5I5PS4gxFE33xLuU9JE3fD0jC9ubKldve/CPu7c9+MDmvQdI+6wtubLFPnry6OTh1gty5KI1PKqlGNVnRVQzYNybGMESmIuxuzWPaQKa17RSpVphtivUZms62Ut+ijcMNdk+lp4OVgyvV2tQgM78DsQb42k8j54pOsFlPxD5DuERJPHTQhE87q49PoHxqQ0vAVNx9oRxAtM2lLzC/Z4Ep8aUmqZzRqUlJiQlJCcw3HFlvb0VUozxrcSpQcqmtbaSBBMWjgh0J0y5U+xXgPuNuaCEFvI9BT0pHGmKjaESEVtLyJuxRn7alj7y6U2JJZfkbH+r7+2+db+99q3JFWRahMuSHZ/hSUovd89LT0py3vHBj+T4g/9246GrNk9OPvz7ySsm6Ob+i3fdd8m0OPesRya/7K7ku8tNuLu0of/McP1zoCe/IAXoood32Zar+lSUlOlEi4r8DewQRx4GA/kOX+ULII5Srx7PV4VapYXQIRFgGq9Rr68z9Bl2GpjRQAzxFv0LFF1Of40Hq5kcEve0I+jQpqbZtbhViFPVVPzNxAnyjZs0uXHxn3GAzhDbrdgD0ug9cdW11pMzHJfMx7NLzptnIn9VdP39yWuqMtPSMqo30Bcvy06VHUf4vXEuZkRA0YYzcoCHzPVmMK0UHaWNia7SdjnXO1VppNC8xLNOup7eEL9dd4/jCd0TjoB6V4x2VMkvpl6zhsUaXLm2RG2aBbT5eRxJ1unW0L4wHdGoUhe6a5nMPAPMxYn4ggSlkK9li7V1aW3aQe31oEjT6nQei8MBWoMlLdcGsYlpFi1ISpOHOBzcWbE6TwyKEAezeXS5Bp2DeCTlGcP4lbrE8M6dGGDgTY7NyT3gZXVsB2MsIT+06Rww5Li8Gl2+i0vob9MQDc9dTXwez12++Uy4j0804eYycfykW1gdMlrsvcU36ae7b9Jf8wqEpyIaTcV4T5h9k974yis8KRtzczgi4rwLpWahk5eYn9iimhE++fByGMfwjsgxvxEW5NM9t/V999vXDwzf8cCln77+0jurX05zFLnmV1zWnWXVxcg5jdnz2ulk9zNrHvrk17eteqjyqnsv37zv2Q3Nt6s9V8+/rqqgZe68+yZfSzLbb5x32XDRyqaX+GlI+uh6WsL3Xa+WfgiQoCDx0i9uFWeV8c+QXYv7IUktSKXrT+6mF5C+fTzLU4OfsBHMcjfs81pVBrOhy73evSl2U9w90T+Ne9z0SNye6MispNIkGqPG6+JPvRoAI24ukBpZpiHNoIZU+iY46duQAGqMiC4qX5yppljE9O1dXr0iQQcxARr9tEyIImIPbjWRJGFXSuiIxa3l2ah3YZpxGp3Gt5kog5mYE7IMKSSFByslPvOM89aN5+1qPDF41I6fjCrOjk+YmA2W0tIEvBAZTx4xHsH9p2mC7zv8qCS4ZZx5UvKI8EhBqi09FCRzOBpOkj3Q4F2/9JbWtLkfjdz67MWXrrlq8q3JyacuKi53pyYbX754/opx+pg9tXjN7MXr7tA9+thTgzVbCoofvfa3k+8VZ5ROL9Or71+z9OZP0TE7g5+SE+wliMRV5tkN8Xh7jDdF5yvngUo7zxRpYPM0mS/Gkth4y/v7xBbahBeI2eHTCk8Tz+lcij6DJkuqWlqqECqrW1o5Zi+JKsLJgdYQ1crjWYVrfAzfE2ykzjtdrySaiPiIDMhgUkxEbGJsEitSzlM+q2CRCpKQGJEkJRuxTJZIgsTw6rPMawRbDIANz20CNqM4ujVPm0AiUoAc3WWS2YuMoqBtjICUECDbvRGGaGs0jT6g1dEAfW2MvKOGvVSJl/Jk8o03wauuU+9QM3WCw/jObTZi40G1xdtPnR4DtUf4+sP0PM7f0ZrweiuuSd4Y5sXLEPPizYjxuxTjtypxrk+GrtUS5hhKSOHrkxS+TgmMohz7Y7Sii7txQtzFvSk2rtTGldq4UhtXavOimM1rigzJuhtvUkx3X2OcWvdmzCZ+4x3AN75UlqqS+G9UlZL9VA7hiW4OZZEjFa+4RfSqjpNf5JHGPdt+NDm5/ZHGkjJ3el3LnExr+qLByR2TxxNn4GvN5E26+69/+ZqjG0syi9zlcqXLqL2i3neA/+qHgyIvb9VbF6mXG2Z/o45Xi+8Yfv5JQfXpbxwm54u3cwKa8Hfoop8qdbIKLpkSInD2R68sJkmKV2Eh5UMsgfnSIKRLAB6E+QilCDchzKVPiLZUxDsR4xURZkAQgmQzvZllSRYpoEhVGpVu5buqaNVfwyPpIQ93Hv6huEFkwxI83bbQV0DBNwtYzL4ACPNXiJKJfimixkQvNWwO0wx6YGuYlvD2ciJMK8AivvPmtBIyyewwrYK1pCtMqyGHHAzTGriR6sO0jm6nd0z5pEAxPUwTMOCJSMKWS4qBMM1ghmJtmJYgRvFUmFaAVjEeppUQp3gzTKtgluLjMK0GizI5TGugQlkQpnWkVnkFaiYSw7H0ql+FaQmmq94VNPdWhJqEaQlc6khBK7FdqS4I0xI41bMEreJ+U7eEafSVulvQamzXqn8cpiXIVN8naA33v/r1MI3+1+SFadSjWRim0f+axjCNOjW/CtPof81EmEb/RyjCNPo/IjdMo/8jHg3T6P+I98I0+j+ySdARfO6678I0zl0f0hOJ7SZ9XpiWwKOvErSW26ZfF6bRHv2woPU80/SPhmkJcvTPCtoo9Hwaprmevwk6mvvQ4A7T6ENDyOYYbo/hkjCN9hg6BB2L7TGG28O0BPmG0FhxQv7DMM3lPxd0PJc3msM0yhtDYyXymBqXh2mMqTEUo2Ruj/H2MI32GLcJ2irkXwzTXD4ULwePqfGvYRpjagwK2sX9E+UO0+ifqJAPs/jKi7o0TEunaLXw/xSN9kcJe9RiXlF3hWne/jCntSH5d8M0b/9I0CIuJkOYxnFNZngcZHwrz4FcyEeqHrqgA3Et9EEvwhCsh37RUoG1AaR52YLt3UJiOnLKcO33IF6EbZdj/yEYFLUOxB3iW/QOaEfJeuSvEq0yLEC8Tkj1YVsLaipHqgfl/nHkmefpJ0/1nIk7GB9vMGybDAU4ai4+Mu5Itdjahtw+5PdBJ+qf9oM1n0sv99np/qHep/vWwWLIOmNO9ecdq1twWhCGhIfbUYbzBmAltnGL/yfR+e/2+Ge5+imqUkiuQ8lejLMMF6FNncKrnJuFsBj78dn0YMv68KgDYmZcaya2LBHyQ6JdhgvF/PhMe7FNxkgV45nkgUbkr8E6nz/Xs0ZkEvdMV9hPnULjkPAYr/eLCPHvxIfw4fkmQ6voOxT2QBVcjOOVhfsOnMHpF1nRjqO0CY3dIh7rxFhtWJ573FCdy7bhfNeIWbQL2T4s2wW/HzmhGXCvtIfH6g5raAvrCs2eryT5n2beJ7y5XmRwN2asLLKidWqsc9nV+0+6f7iXTmtvn4rzgMjaIWF521SmnHv2odH/2a5ZZ/iAzyQ0lyEx3qkc5PpDc23HlnVi5n1iJZx7piFPt5zl1Q4R2b5wGZpViF6DtX5RysLatVOZG9LDJXtQ4r+M0eOyJyc3X67v6pBr+3r7htb3d8gVfQP9fQMtQ919vdPlsp4eeVH35V1Dg/KijsGOgbUd7dPru1d1DMoLOtbJi/pWtfSW9/W0n+o88x94MmfOXNIxMIja5ILpublyRm1320DfYF/n0LRzCZ+W9eQIPrIFt25xlhip/h97dQ/KLfLQQEt7x6qWgZVyX+d/Pp3/jDHVVs+LyoGWdd29l8sXdXZ2t3XIWfLioZbeno712HWge7CvN1Ne0t021DcgX9gy0N7ROyTnFud5GvvWyKta1strBjvkoS60qbMPOS2Dcn/HwKruoaGOdrl1PXI65KqLLyxD7oCo9A/0ta9pG5K7e+V1Xd1tXWf0Rdzd29azph27DvXJ7d2D/T04QEtvO/bqRoE2lMLhp8vyqcH7envWyxnd0+SOVa2812ldvaekz2mSEG/ncx7oGBwawNmhU84YHrtP6ZolLMjoxlGGOlZxDw5046jtfet6e/pazhwUjW4JmdoxION8+3AoLNcM9a8Zkts71nLnokxXR0//P8xIHCiXi4XKN+DzHTVnSw/BGqLD9s/P2++0ZKdY7ueTD0lVi3GGzisdlmOb2fPsFXxpfZ6Nnq/PWbL/usj86yLzr4vMvy4y/7rI/ICLzNQZ0P3fOjFC0hci7kK8FiV4y5rz9v/nHhcITw2et+cpuWo8dXpwB/kWdXyObec/Tc6WP6VnMHwa9f3gkU/3WCKo8/ULSc3FWg+O3fmD+pwtXYcc7u01mMGhHD1/hM7V58yonX++Z0lLVqlEmiVVSDOkIskrzZFqpOLzafhP+vzQ28Vpyeof5LOQVA33HMnFlvPJn5asETtBP2bH+b1yhiyJgo+ZHTnn6TMl979ZY/+L2P2vxv3vrsup3xFDMBVehnN8RuvlgKQd0+o9HPujzZ6AFDmWIVsNZUbJBBsQKBiwLEVYjsBEScArmfxX5HkDiAZCqDeEVoRQfZ73ORScD3nBcck0ZrZ4ePNYhNazgWO1htej/EvzvGUaKQrXJZeLwrNdYH9dnmDXci1ROB/ROlZZFepVHmouCQvPzLOWObAuI3gR+hF2IhxDUKL1UZCNsBUhiCCJGpcbRrgNYQfCYS4rtKnzDGWJkhE5RjF3I1gRshEYNIvf8vpEaZDU6BU1XIRwP/9tsRThhx7rblTCxqqEpWzMPV1gf8Y0j2D4E5I8z0uMboN0sGID8cclCg74y8vDxIyiEDHmyvIcKouQAI4iUAkkgqea6DWWMd1z7EWsEzYJBkJ4KzsxZozB0djJMUO0x1tmZH+DOgQKPryAjyNQ6GPfwDACRfGd/qxcPhDbORah9xhR/ihe2o/CBgQGO7Akou5F4PJHx6LjuPpP/YYo0e+QPyc/RIwZLZ66shh2AO15nf0G7GBlHyNOQfwq4mTEv2avgU7Y+dCYwejZgOM9iOIPsvV4SFvZw+xKvJJZ2WPsWkgUYn/w60Pj/MGf4fKURbBH2dVCZJCtxrcIK+thK/0eq7yXPcTzkX01ponk9n3lN8Z6nmefs5UQg1JHUMpsNTzPeiEbgc8kMKbRebaWaVkApxlAt1jRRgL3i9LLfuNHRTje42wDxCFvH9sIsYifYNf5Y63je9l3QuxbrgXH+zlmDEdjOr1nvEzDfs4zhP0FPf4XMdrxMWeRB8qc7BbIQaDo1E+Q+oT/up19jdTXGKavMTRfY2i+Riu+xqQFNoGcCZTJZgehn30IWxHuR1pClev96MHdgnBkeHaza9jV6AnjXvQdwdZrxzR6btnVflO0ELuaL/DS59l7cBECRePf5yuyby/7kZjK1jFLIu/wW79Gi667KhQL7Hglj8HzbAO7Tnhio/CA7wWsYv6z60Xn4Jg2yjOM0a/Hah+WtyHsRziKIKFYPc6hHpYjMBSvG9MbPIa9bKnoPM+vz7M+z+bi1OcKb831x9qEzReECcngT0zxvMAJyCL86zW9pPRnWxfuZTWYPxexBf52K9q+0I96eccFY0UzPTl72QLhiwV+qz3U7I+OF0S1XxPKq4qxiChuSaUQdPvVetHsDi9J5hqLMXusmKczxWzz+LdOrBDDV4ihKcR1kieC4RkzmjD725lHzMgDzQg7EHwIEsYY3+EQ6hAOixYDm4HTnQFBBIaxnQHHEHCrYblQinAbwosIhxEUorUZgWJ7Do7QjOVWBIoas7FuxNKL0IywAWEHwjjCMQQV7GNZOA7/6iIHyw0IPoRDjH+B0ccy0Y5M/vUOk+GkGsAKw3SbdyYZhmEyTIfZsDSsGDYOR6m9BWmZHu8KXkznRQYWhc2afs0GDcvReDV1GmbUyBrKvyZWzczjf+NpUs7M+6D2i9rva5mpcKtyq4ruK9Pi6XsI4SgCg33EiDUj1ozem9i+kkMlR0vYvtpDtUdr2b6Dhw4ePcj2ZR3KOprFvLWJMz2Fy0kfGSa3EclKskkpuYhIy1kfG2a3McnKslkp5oLUHNkfuSGS5UR6I+simTFSjqRbI3dE+iLHI/dHKnzKceV+5WHlMaWiTtms7FduUG5V7lAqrapsVanKq5SOlVXQD9GpO7D0IVDYgOVWQRkFZxzL/aK+VdSbsewXdS+WdYKyY5nDKQQ76voA5TZguRWBy/G6HcscXkew4+7+B2zrx3IrAqV/8CbZchxeBzU6ZAcFBznmIPsdhx3U5xh30PGymfR9YeX7aOX7wsr3sef7Yuz3US9SCHa09j0h9x7KvSfk3kM5Tp2rrRnLfkF5sawTlB3LHE7R9/z2QkOZmd6DGpdjeT/CIQQG2ViWIvSJmpVL0Huw9NLtY+mZeODT7X4n7pGIbCGUEkJJAo3FJ3iWlxnodlS5HVVuRyW8ZkUo5bXgON3mr+Sy2/xzQmhm3qGyQjxFuSnbYCcChYuwvF9Q2ViWCmqnkDFM1X1YHhZUP5Y7pvotF5QVy1N9Gd2OzzakDPRKbL3SG0khLg7vTKYotSlA9/i7TdYAfdqfYUQ0FkJ+jsqiKUPf68jXovylKO8X5R2ivESUBm+kXfc3u+5Xdt2jdl1ZBJ0PDmw+JsrPRbnCq3foPnPofu3QPejQ/dyh20s+ARsyUr0JNt2fbLo/2nTP2nRP2HS323TLbLqFNt2FNq4qAy+BOprMS3KZKJO8Zll3QtZ9JOvekHWvyboHZF2jrJspozj5C56nOnKvKO8SZcGz+Tprvi45X7eH4s5ELvUbQLOXUnIp6FiE31ViDTCNQDTVX5uGKMlfW4Yo0V+7CFGCv3YAUbS/9nZrmYYayCheVqxUT0bVHGv9ro3Ijgwhtd91GSKF31VsDZBJv8uO6O/+zmRE3/s7UxB96+/MR/QNR8+Rv0InRTXkP/ydP0P15AvI4GrJp+CkTyIO+GtLUfrZ0OjkaSghadjsx1sfF3vK70LjyGN+VwaiR/0uB6JHQuhBv8uK6AF/J/87vJ/5O29HdJ+/8wii7f6MHq5vG2QIPXeDU+BBf20islf7a7mGfn9tNqI+f20BopX+krcQdftLjvCul5NRgplNOsElLG3xd7qQvTw8kSbIEOxlUCA0X+Cv5S6p5krKdKQqPJFKUsHvfKScjAotXr8rB8VK/C4nojkhz832d7oRFfkz0Mek0J/xM/TcjPAA03h8niMONIMrsvtdT6KQ1d85DVGKv7MKUSLviUZFh0c1QYkwKsrv4lJGv0u2vkAioVNojAAn2b7LehL1/r0kQJb4rd97A2rit36XgWiX9avaVuuXtQG88Vq/wCX85C7rIRQ9WIKkN9J6wHXE+mGnzfrvLpTwJlpfd023vuxcbw1k7LWO1aZYR9EwX2erdWen0PBLJ3bzWx/LCFCCvXd0Xmi92+W23uUMcBt+gsI38TFQ0SbXeut1zo3WNZgKQ7U3Wwddydb+jMusKzL4QGZrt2uRtQsncjn26ei83Nriut3aXCAsvsz1lnVxgZhDTaeY0bwSwZjbuchajRYgo5Qz0IJZmJce7Dq9YC/3Ed5UKsbesl5c+BzFU5hsQBjwTlc9r7pW1aqqV5XjeZOuSlOlqlJUMWqT2qjWq7XqCLVarVRLaqoGNdCYQPCw183/oCZGaeRIKfFSErSR8pKG/laIEjXFFy1fNKuhNYvLfYXumoAquMhX5K7xqesubRgl5EeNpMY33gY1rbLv28X2AIlYuNSnsJcTn6kGaurLLSjso5sDBOobAiTIe2xK5P9iuRsIydx0ayLH1ZtubWyEuLWlllJTSVRxdeU5iuZwWVXpPv2xuN1n1ZJ9d9YsbvA9kdzo83AimNxY45vG/w1zN+2hK6oqd9OVHDU27CZdtKdqEW8nXZWNKDZLiEEJXYliUMsRitFlUMLFsH3ZGWJkFJsrR0tKQkIXkVEuhIvmIiG0NCRUcaYQ20IqhFAF2yKEfhYa0IV24IBejlBM0QMuMaBL0SPELFxs1OlETZ1OLjLqcaLAqNMj2AtPszNC7F+E2L/g7AAhp/kFzpC1GeAUIzhpBsq4/x9+Osr/B53I2Jy1vQ3832eb7VUdCM2+LWu7LL4NrbI82rs2/H+1zubWti6OWzp8a+0dlb5ee6U8OqfhHOwGzp5jrxyFhqr6htEGb0elf453TpW9pbJxbMHGotVnjXXz1FhFG8+hbCNXVsTHWrD6HOzVnL2Aj7Waj7Waj7XAu0CMVbOonNTUNYyqobyxYlkIj9HICFwtzYmpjeVxxv4SsXRmpVquTdwjAXkMIt2NPq293KdD4KyssqwyzsIlzVl6/i/SYZbl2lmpiXvIY2GWEZuj7OUwZKnqrsSfQfwMDa3BD/p4cDDka0uIMeSuEnwUGEJqSHxQEmkOg6I1zB+CNac/bndIFgbdFQ2jtbVVlu5K8Qfh/N7tbhwEtzs0oNsNOCbOWlz048RFP1IZl/e72j/VflPLxsUNfz/CYXHDH8fb/X6Ew3jDT2HjJftLDpew8dr9tYdR9uD+g4cPsvGs/VmHs1hh2AI+VCNBC08/a9yDawbFvzCI2Yp5c0PQaCT4rE+5YVAwhoRj8BNqF13dqMg91d19mhgMMdeILqHWwdM5jAyufmiN+58/4Vbcgv8vQdJIBgplbmRzdHJlYW0KZW5kb2JqCgoxMSAwIG9iagoxMDk1OQplbmRvYmoKCjEyIDAgb2JqCjw8L1R5cGUvRm9udERlc2NyaXB0b3IvRm9udE5hbWUvQ0FBQUFBK1RpbWVzTmV3Um9tYW5QUy1Cb2xkTVQKL0ZsYWdzIDYKL0ZvbnRCQm94Wy01NTggLTMwNiAyMDAwIDEwMjZdL0l0YWxpY0FuZ2xlIDAKL0FzY2VudCA4OTEKL0Rlc2NlbnQgLTIxNgovQ2FwSGVpZ2h0IDEwMjUKL1N0ZW1WIDgwCi9Gb250RmlsZTIgMTAgMCBSCj4+CmVuZG9iagoKMTMgMCBvYmoKPDwvTGVuZ3RoIDI4MS9GaWx0ZXIvRmxhdGVEZWNvZGU+PgpzdHJlYW0KeJxdkctqxCAUhvc+hcvpYsi9MwMhkCYMZNELTecBjJ6kQmPEmEXevnqcttCF8p3Lr8ffqOnaTkkbvZmF92DpKJUwsC6b4UAHmKQiSUqF5PYe4c5npknktP2+Wpg7NS5lSaJ3V1ut2emhFssADyR6NQKMVBM93Jrexf2m9RfMoCyNSVVRAaM755npFzZDhKpjJ1xZ2v3oJH8NH7sGmmKchFH4ImDVjINhagJSxnFFy+u1IqDEv1qSBckw8k9mXGviWuM4zyrHKXIae86Qi9xzjnzCfIGcpZ4fA2P+FPji+Rw48XwJfPZch/Nbz08hj/c2gWvPbeACh79P6Z/hff6xh/LNGGcNfgZ64t2QCn7/Sy/aq3B9A7g7iXgKZW5kc3RyZWFtCmVuZG9iagoKMTQgMCBvYmoKPDwvVHlwZS9Gb250L1N1YnR5cGUvVHJ1ZVR5cGUvQmFzZUZvbnQvQ0FBQUFBK1RpbWVzTmV3Um9tYW5QUy1Cb2xkTVQKL0ZpcnN0Q2hhciAwCi9MYXN0Q2hhciAxMwovV2lkdGhzWzc3NyA3MjIgMjUwIDY2NiA1NTYgNTAwIDUwMCA1MDAgNTAwIDUwMCAzMzMgNTAwIDMzMyA1MDAgXQovRm9udERlc2NyaXB0b3IgMTIgMCBSCi9Ub1VuaWNvZGUgMTMgMCBSCj4+CmVuZG9iagoKMTUgMCBvYmoKPDwvTGVuZ3RoIDE2IDAgUi9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoMSAxNzI0OD4+CnN0cmVhbQp4nO17e2CUxbX4mfm+bx9JNrvZJJtNlmS/3c1uHptk8w4JkXx58UogCBETNOYNCQQS8gCDUOID0aBCFRFfEGvBB3jZJIIL2BK9pS3aXvBWbfW2glds1UpLLdpaIfs7M7u8LL2P3+/PX7/JOXPmnDMzZ87MnJkvu9vfO9AOYTAEAiitK5p7PLEmIwD8DIAYW1f3y6b3cyxInwZQv7+kZ+mKfvnpRQDaVwFUWUu7Bpc4Hon/EkB/DsA4r6O9ue1nO14PB7Ar2EZ+BzIWTg6qsXw7lhM7VvTf/gPpp5lYforJu7pbmx+RD1qx/DaWE1Y0396zSvyeCsvnsSyvbF7R/u57kgjgiAIwfNTT3dffBol+gJy/MnlPb3vPZ8c/9wDkmtGGSuQRTOwJQ1LFylQQJZVaow0JDdPB/4eP9BBCNVgRpgjbAOfS/yHCGYRPJuf4L0jLwTG5zH9aiETll4IA4ITtsAsS4RzJgtdhAubAHiiF+bANZsIJ2A/hMEjeBBEcUAHPg5NYgcIMiCESPA7vwa3QCx/DaUiGKviAGLGdSugBExT6P0VcBff5D6FWCJTDv8Bh0kUWggfpWTSNuLHnLf4JiIFk/8/9v8LS0/AxSfSPwiykfgsRkAQb4LtghGXwhv8CWpoILfAcWUc+BRs0wWYxVxz2L4dpcADeIVVIzYVB6VfaA9CFtZ4lMWTCf8r/O/ihSKAdW7oL7kOLx2CCZgjl0gjI4IIbYB40o/QOeI9EkixB8Sf5y/yPI/c5+IK66Y8FNdrhhtnQCA/CM+iNd+EMfElCSR55muzF9Bb5g/QrtK0KBmAt7q2n0XvPwT44RLJIFo2hMeitGEiBm1C2BXZj/+NwklSRejJBXhN2S5mTJf4of7T/d34/pEIdWrgLXsM+zpNM1MEeBLvQLyaI/VL2xTtxhG3wFJyEt9COD9DvX8JfSSqmD+l36Ab/zf7n/R+jLRqwwlS4ERZDN6yGNfA9nNXX4UfwJ/IN1aLmCfGYtFY6538YfeuCMrS9BrUXYtubcZbGwIfpXRxlBJFxFFPJPLKALCVbyHbiI++R96iK2ugq+pngFd4Ufi3mS5K/CFsyQQL264CboQNn4Dvo7YdxvM/DMThOoomLpOOI3sX6X9FptALTs/QE/UDYKGwRL0j3Tp6e/P3kN/5hUOMqm4l+GIAX0Qt/JCa0IYUsI33kI7R8K31ZCBcMgkPIE0qFWqFeuE/YJvxU+DexV9wrvi/NlpqlvermyZWTb/mr/PcAixIqtCsJ0iAXCnD9LMHVtBzt68HUC+vgThiGh3C9PAwjsBfHfRSOwzvwG/gcZwCIDW3uxN5X4KrbSB7C9DjZR14jx8hx8iH5iiVqx5RM82kJLacz6FK6EdM2epK+Sz8RpgitwgZhCNNO4aDwngiiKPqlbEyzpM3Sc6o31cnqWeoWzc8unL2YerH+4geTMBk3ecvk9snXJn/nX+QfRPudkA4ZaOkmtPJxXIO7Mb2IK/Eg/Bhj9y+5rV8QSiRc8WbiwNWQhrNWQmaS2Zjmkhsx3YTpZrIYUzNpIR2YNpAhche5m9xDHiSP8rQDx7abvEAOYnqFHMb0DjlFfks+I19QXMRUwNXspEnUQwtxpOV0Jq2hCzAtpd2YemgvXY0z9Bwdp4fou0Kk4BTShWZhlfC48C/C68LbwtciFdNEj1gsLhKXineLJ8S3xF+J30hWqVLqkHZKr6ssqlzVTaplqh2q/apPVBfUKvV8dYt6nfpttV/jxGj1Exz3gWtCnkd1gvRJUeLt9BTuC7PQI20iN6HHVLRW6BIeEv5dWkLOCTJ5nwwLncJy/7PCDPpXoZssokeJXbBKRcISeAD8ZC/9kJ6nvxOjSS39lCSL3yWv0G6hnKp4XP2FGC3eLX0CQH8JRXQ9maDHhLuFu/0/gCJpJzkl7aRvgSyeppFwCnf1JvoYVvo32kk3Q52YK30Dnej3F6Tb0d/T6X0kVXhb3AkfCw76Z3KObMeo8XMyR0ykt9FCshcj7kWSAGfJKughj4JCjpDfEB8Q8rzwHKmmYThbXqojBXj0/VywkbeFEKhnNhIXjSbz6Tl6k/Cq6qSQRwhGiX+HtUQgmbh2Lj2TsBJ3wDaahDGtEqPJL0g2mOExjPfnJ19lEVv6lbQZ19kzQhosgExooG9CEe6NjzHVwb2QDYdxDd4HmXQHrPMPkTaM+3MxflLwkWXgIaEYLWPQtg14XpioHWNhI/b6V4z/b2DUryJ/gDVExp01AckikzwgVmJkasL4uxlTGzRg6Sl4WHVA+gXUkBgAUZ7ciav813AbnjkfYf9xUIz2LYZnxDS0WsbIvAprPDU5CxRM98KbhMJ6tHk67vP54iyMvNv9y3CEnXhGVeOZeBw6/Y9BOc7dAv/d/s3Q6H/GfysshYX+5zH+rvaPQT5skurpIskt5mKMPU5+hOfRf5DNGLdnwfsYj5zEDJ9h+he0f7p0BIbFX2LsLPE/4H8HotEfdvRQC56iZ2AF/AH9NkuYgJzJeXTUP0PowRPqFNzof85vJSHQ4e/CyPsq7FZLGHuGIEHajWt3s7iEZqK9KWAiHuTeKu0CUMpuqlVKpt9QPK2ocGpBfl5uTnZWpicjPc2dmpKc5HImOuw22ZoQP8USF2uOMUVFGiMM+nBdWGiIVqNWSaJACaRVOmY0yV5Xk1d0OWbNSmdlRzMymq9iNHllZM24VscrN3E1+VpNBTWXfEtTCWgqlzWJQS6G4vQ0udIhe39e4ZB9ZPGNdUg/WOGol71nOT2X01s5rUPaZsMKcqW5o0L2kia50jtjdcdwZVMFNjcaGlLuKG8PSU+D0ZBQJEOR8sY4ekZJzHTCCRpTWTRKQaNDo7xxjopKb6yjglngFZyVzW3e+TfWVVZYbLb69DQvKW91tHjBUebVu7kKlPNuvKpyr5p3I3ey0cBmeTRtYvgBnwFamtxhbY625lvrvEJzPesjwo39Vnhj1p4xXyli48byuk1XSy3CcKW5U2bF4eFNsnfkxrqrpTaG6+uxDS91zmganoEdP4AurFooY190Y32dl2zEDmU2DjamwOjaHZWM07RM9modZY6O4WVNODFxw15YMGgbi4tTDvlPQ1ylPFxb57B5SyyO+uaKKaNRMLxgcDxWkWOvlaSnjRoiAm4dDdcHiTDd1UT7ZRmnuDqjqhZc9ithFjlm43Lwyq0yWlLnwDFNZah9Kgy3TkU1fOoJ1vK24Xx0erXlTcOGIuQbWH2v5DQ45OEvAeffcfbzaznNQY7KafgSGMlWyeWFhvJLtNft9qamsgWiLscZRRun83JeetpqH/U6egwyZug+mI++ba4v8qDzbTY2vZt9CrRgwTt0Y12gLEOLZQwUj7veS5uYZOKSJPomJhm6JLlcvcmB6/hl/p4S7dW4Lv/pDabIyo4iLzH9F+L2gLxqoaPqxsV1cuVwU9C3VbXXlALyqZdlQYoEBOhwr+hET8124NJbsLiOMfBPcs5wVHY2zcKthjZ6I8vrBAutD1DUIvCmcP3eerllVqgLY22JThVf/20+tQYXMOcQeYbX0DQrgOtDbLb/YSWf/xyrxbMr1YJj8ha5ry1Pu6Z8jXlhwwIaLLpoVe3i4eGQa2QzMFgND89wyDOGm4abff6hFodscAwfEuqEuuGeyqZL0+/zH95s8c54oB4H0UGKcGlTKBt1kPtuHFXIfQsX1x0y4KvofbV1Y5TQ8qay+tFElNUdkjE+cy5lXMZkBZkV8MzDXTFGNVzfckgBGOJSkTN4udVHgPM0l3gEWn00wDNc4lHkiQGewnnsYZGivLbu6jXAN1Z9OjvtKZmCt5cpEuAbvxrmjlJyhP4Q78NqenQMJNFHf/iyACFqRhwgEKtRSUdRTkEgKaAly8ltYHYbviq+WDzPcL547sViKEHacAFRVqYtwhbhRESmiHBBFiYuKBJ8g7egCTR+jv9DSY9vm4mEKmXaBA/xUI/gsW7XP57wrP5Z40H9K8ZQTQIxxZD1wh3Rt5seFIZNTwvb4/YJRwRtmBAu0vhZeLWXPBpDRKIFrxbSAWoh5DD4hKqD8hNS8hSB+OipAxg6DcTgE0oPbNHt0lGdT/Aonigt3Yf3JZJt2Lc/glgjSiJoRJziIi5tsWwmerPVTM1hOh29yTzb2dZqduPY3A29c8/OMzR81btq7tnzq85CycVV5xvO/7bk7OfnzxLD2fNnDcezMssHFTnaogpTO+NcoS6TU2XRpkNYNCJNrJROQmJ06HS3m7j5c+edZFUD9K5qIJEOl8thV9HoKKMpJzu/IEYlOuQkV16uMTEnOwZZBfkF4ltW6/TfPrPp/fWrz+64541B65LJc0cm9x8aPkhKfvDIllSjJSouVFo+mXPi4P2Tb5/yTX6xddXzUQee/9vhC2+S2iOzTJGWTPauMx1vTF72PwD4k1KE1326OH5xwnKynC6PX56g8dhKbDW2HdJjluelPRY1JfEJJqvFYLNrrRa9zaE2O8BKDXqNzUcnlEgtvqErMeElRj02Nx9fOETw0WQlTqNVRUbSm7Rhej3DzJVae4zJ6k7w+SeUcFYDEgwJjQkjCWLCYZoMJv/nSqjBQG8yxTFlE7Y+Lrc1sLXldp9vKK87BAn+ibHQPNbAWKg+1+2ud58xFF9ENxYzuaIFJTQP4ZLot7gS2VpEbxuOs7khDcAdnYSudkREoVdzom15zLV5EQ67WqVSOyLFZ/Su0Ejr0tqjFleN5+JrmYsSTc82JufOUbsMUvXk67WJRQXfnF9vTXU6c+VeMSw8sutWMp159RxuIJXUgW+4jylRirnJPGI+bRbBrJjparxy0vDSSHxLLMU9M4K3P4HTGqQdWPmvoCedeJMrRfoLJZyg07T4rqbVhFEBDpO/oPpsxRgerlci8jL1G/Rb9SN6UR8bc5gmkjPAl2eDu3iu4Sw6hI86wlhIIgrhy7MXyJdudxbe0XGhRTpzIqJMphgc9HSaF5HL/GBXnyNzbJHFt07SpqmmEFy2zjLxJ898s6l3agJ1Oml81lr6622pcoKVxYd1/k+EYRxjLLggh6xVDtcTos2x5qQmdeestQ+FDoUNxQ1Z7nIOuYZzXjDvjnvOOR72ctwrriNJx0KOhf5SZ1JDCFHpaJw2yaSLiXPqnOFV5AFyt25j+AsQPg2KSBXGwtnJjeSWpFtzlsEy0kmXupYldeTcQdYlrU5bl7NF3CINqYc0d0XcZdwStcW0Q9yu2Rax3fikaY/rpaSXcnziQc2noZ+FfRr+adKn2SlqnTapCArJ1GypQgNhcUkiR4YYvGCqxlRSOssidfGlWqLHmVE4ZCJtIEsUA+QpeVTJa8obyTudJ+Y5XkWBgLOdSvRKSGaMErM1RoiJzT1M/kA+Dc7CXBYHGlZ9dfbMeYwQZ0vORhhjCklMIY8M2W5Pgj3CJGqinTbJ0QJWdXwLSYtKbYEMY2YLsYtyC0nQIHKb0lvAE4EIWJggwWARCBcsYhAXhob8nGxTdJRKbYphmcPOwkW+Mx/v/SyUREeZYiJVLMN4koeTTe5/puFnL3z/p117vYXV74++1rVokGTdrqxesmQoLyt/4fwHV3Td5ZpJ994zsuieo2O91TuX3zdvyaotbw429y0efbdrfU3nmtU1uR2eyd/N2N1055Nrb55VuAzPgTm4Jl7BNWGARDg81qzhnpWkaJbpdHE+9JVRGwcuxUUVV5NrxHXaJboiGDu8EbrxJXALjIAEsc7DJAFfSC+5cR7z4tyAD7nzqkmiI9GeSFUUX1epSu2cYom3JFgEVaRL7wx1mWNjYqnKJkagW1VxLSQqHClTGFKJBD1qYW41GqJbIDYE0WWvpnJITb0zMtdYkM9CbUQU5b4sMMTwSJzPNkoS2ykqOueB/sVNT6178r5ftLx+54ofVRauyu9PyMhMLEwpqsiblUt3fkJqFpTuOja5//PJg49+/NpfJj8ZfbS5dx8p/OTJvkzbDQsnn8JIkYZbaS96LIF0KHepzaGFMeYpN+SaFUSxDOkTTKYUdbF6tvoFtUqRbxEXa26JWWxerumP6Dc+Ffp0+OMR+0L3hR+Xjsf81PxezHvm0/LX4tcx0dEkXoyVLNGxptiYeLNaGxNqDo3PjZ0Ze3/MFlltjqU0Ji42LFalE2KppDKzVaOOFHU+NEOrVaLCSoa0ROsTcpQwgxS3JZbsit0fS2MPCzm46B8cJzQswUceVHSg+s+ayMbI7sgNkWKkj6iVSHYXiQNZkYdkoUkekakce4R8jfFCRxQlqpF20w10Cz1KT9BT9I94gYm1HiYPXZnrM8WB2W6Ye77hrAGnvPjsxYZVxXi+jqrYxeWVLVpyVHtCS6FhFQZ2tqN4fDMWFlJDQOXl9bEPxqK8Prx4k0Fa/6PwH2G4X9XLAj5OMLiJYMsDCGwMtSM/sHPUKjVV27Lz8wuEvY0XTuMLgbxzZdsulzP2xJO7f5M5Z8/X00lL180z4og0+Y2TlJEdL9y5Z2DVoR+/vXXp0u8dmDw31ZCVjkPX4/1pP56nJQLeBbk1EaLabPEJ//myqqgg2YmEYnBF2CBDdMVOpS6KlygNlODDhmK4ePLkhOECIuLJyrQohsEIoovVuorI7bDGJhmtPv9pJVxf6DFEFRpKFbdSKpSyM3SL1ZG7Gm6PWGvvca9Nf8L+uGMP2WN4wfaC/QXHnvQXPEccR5xHXIenHiz5qeGY5Zj808KJ0neM78hfh54rnWL0GGSjXU50J2d4PDcYMo2Z8jRbflKmeybojFAql2aWniwVf5xO+tPXeTa67/eI5e76sHqboHXEOkzTS0qr4sqTVMaoDJKY0W7bbdudIQYmQrGLcaVKSoQrg0aALUO0OJkrLHGqOA1zhcU11UVxgg0Xjx07FsyYExr4bFqUqgzZQ9JtssdgjzDYjSVA0o0lKoPaooqTsZWk9GRLUmFJkaVQIqJFijWaLbEuO2vVU2CZmm43GOwkPYqQdE+J0eijx5XpsidKlj0ZtggQOSL2wqlTk5JcNC42VqWSNB0lpMSNN0E9kUkmuRVfEHuIl0yQ0+QcCcH7498UfYW8UG6TBTkb7CN2avfRfz2olG63LR82u91fNZxvwLV6poGNCVMAB2P+pvAM96bw9T/C3MwJMxjwpjjxP8d6fOrZ6Y33mzH0TEM9acCHrCqvO+ghKXbPdKGhnt99Om1t7m5PU6mIZTfpxSWP71SKfklS51SaajbpShwGXSH1+T9RIsMKHRhzMhAcs0yFrkwT408cNBXak02FIq62MVNhFGYHQwvNBiMTnlNCjYXpGmOhXTYWTsVGxvSFSYHM6PP/CjM5kLkD2XTMRvWFwUvulYeHXTcfBW5VQgJ3EQyx/HxKEgg/ri7xCgieckl5tmjGjYmMDGgFOOyyJlQQx+DA4ouHi6ZEW7TqzN9Onkk35ldPWnOc03tmEWXyyxU7Wmnf/GmZJ/+UGhmmz5hFPixMzF+8gP5xct7LjZLTSUK1zsiYmIiZ5NbJbUVJ0XKq4HRKhri6W8g2smlXK5aEjCnOmZPHSVZ+cnS0ITqCIEsfM6+T3fhqJ+fQddJDEAlFimN7xHMR9N6w+yNoyA5tBOwgkfjGHqJ9Ptw+X0VUQ1G1t7GrbMPZi8XF7I7GTzWMTg0kGg8XmmeAAhwnXv9jEihd91j71qdI9ld37Jxni5uzfrLbWb3ku2T4bZJP/CtTKz6f3H7s3f3Dzz2BNmSgDYu4DYVKYoqYqpklCdh5BBoRictaG4IGyKpMlaISVEPRdd//eyNIQ2Qe3iCM0QZQ5+XnG9HJGTRjR/uWpyZP/OWOXXNtsVXrpLbUqiUPT655Z/KNSbLSWfl7svzYO97hPcyChWSI1tEYjPYlikylofi2/A0SBncKXkEAaiDzcVNtJSPkJFERH8k9AENi7WJmx8UGZoXnLGJmhjvSFm1bSKWL39CYx5h/F+LNYhGek9mk+hCE4LoMK9SyyFccVliqrQyZEVplF09oSUrK1BQltyn3RO7p3L+EqCGXlGo3ONZmvJh4KPFwxvGMU45Tzv/I+Mz+qTNstibFRx4YT0424FvKmfGTmSTTJ+QeECSDiZh8ZNeBeMXtyY33kfJxgy4l+QjpgCjQ0o+U0Pl4ttGt/GzDE3LcG0bCfGQr8tOH0unW9JF0mo78A43qDXim+OjHSoiSS0ZyJ3JpLr6aTn9FiTwaSSNjc9gl55PLBx8/9c424HskojP4/oxz4j7bW3K24ayx0BO49+RneBJcIXpRZbc5bIk2p01USc5wlysELzQeMR1vjHqkbKFJLSREm6HCe6RVF89uOIbi4MUxld0c+RtAL6xyuyPz+T0Hzz8TPwRt9sBdMga346W7In9FYm8J7MRUdxSN3vPszWWH1w/1PDz5+/tbPbbYuIjbY5ypSx5zxFnd2+fJNbtm3dn0ZIc45/5Hl9Us3rYz6+Ad3jufr0iKT9NIJarQnV01VVPjk0sTQm67p2bphj1sdr9L5ws9uHIXwGfKg9G1G/W35B8KPxo3PuOVBSfi/nXGB3Fvz9AUSNPCp+qL4qa58gryZ+Qs0ETFG+yG4qjSqLKo8jRLWuUNlhsq51nmVTZaGisHzX1T+soHZ91nvnfKxvL7Z+0wb5/yaPmOWS+a90zZXb539huuNwrkG2eXF4rZ1bkz8sU4d1JifIxBtIboICk/Wwxxi9aSjHX2Yh95RXEYc2vWqQF26N6JzrW+k7Qj/52SErk6s/pE9clqsXrjwmVr+VbCI+zixfMX8dJy/vzF4jMkeDkpDOaXyOAbmJu4kq66uLMoFyT5LT5wjUeVpGuu9PkFTM4kWChgiakE2zGRP4hme1aiNSNPVZBWUWa3pZfdMzcnt2qwKj0hYfb0lBtodGKmxZkQnREmTUub7YybYs9MSbE0TZ+WX3VHfHp6gm3OSjGqsqLFiT1lpz9T4ciucaVbnUXxESZLuKksKSEtdXaWu7B8tTu5ID46I3NTTnZS5gKTIWNKbIExLEoXa9HHRds8lvTUDfxDJvZhWE7Oy6Ynmxr1xV9qYjX8Q6XvfRT/+pWP5Cbn4I3pISS0we9n8Hpq22Ql3HxZicC1j05VSKZIi2CO+BFMFwHOIaxDmIOQRlm3PwE9fRFqETIQFiJ/IXyX150GfyXP0mjBKvjEEXFE9ZTqA7VVXa2Zpk0K9hSOugLXpfgu44FFACqn+Ht8NWFNF9EfAgTlyzgWeL0EXhJ4rXC4L0gL0Avbg7SI93xNkJbATFxBWgV29o8DTqthNWkK0hp8/3w3SGvhXv7pIqN19An6wGWf5EmuII33T+mWIE1BLS0L0gIUSquCtIg6LwZpCcKkQ0FaBRHSsSCthmnSu0FaA2ZVRJDWQrkqJUjryFxVF7ZMRIzsEKY+xGnmIYP6GKdVnP8Op9Wc/yGnNZz+E6e1zIcaEqTRh5rlQRp9qLk/SKMPNY8FafShVhuk0Yc4ZwEafagtCdLoQ21zkEYfan8ZpNGHeCgEaPRhyIOcDmF26gLthDLbdDmcDuP8ck6Hc7qG0wZmm+4WTkcibdR1cjqK69zB6WjeziZOmzg/YH8sr/sspy1cZ5TT8VznNU5bOf1zTidy/fc4ncrp33E6na1E3ZeM1gTsD9C8r3DuzzDOD9dxmo8lPBZeABmyIROyIBepWuiAdszn4sv3SoR+GIQezinHUi/SDDcjv5NrZKCkFLowyRirO2Ep1u+HPl5qx7wdtVcjbkPNWpSv4FwZ5mG+hmt1I68ZW2L6S2EAW2rGOt/uv+i/qS1/q34R7lDWd1/QThny0IIsTDL/vLkTWlHajfJuWIK9pPw37V+vNea1K7UCda7UmI8RYy7K/zu7O7mkGaGfe7YNdVbwMSxHHrPu/2ZW/rc1/l6v9jJVwTXXoOZK9LAMNWjTEu5BJk1HWIj12Gi6kDMY7LWXj4y1moacRVy/n/NlqObjYyNdiTwZZ6UQctCf9SgfwDIbP2tngK8g5pmOoJ+W8Bb7ucdYuYfPywqU9mNi60yGFl63P+iBSrgJ+ysN1u29StLDV0Ab9tLKW+zk87GG99WK+Pr9BspMtxXHO8BH0cZ1uxG3cXkPX4WD3MqVXNrD/RFooTXYVmD0bAfJfzfybu7NQb5a2ef4Ml8VLZf7up5dK/+u7f+5l6603nZ5nnv5qu3nlrdeXinXH32g97+3a9pVPmAjCYyln/d3aQ2y9gNjbUPOGj7ybr4Trj/SgKebr/FqO5/Z7iAOjCpAD2Cph2OZW7v68soNtMM02Xe5/ss5ekHOzszKlWs72uW53Su7+wd72uXy7t6e7t7m/s7ulRlyaVeXvKBzaUd/n7ygva+9d3V7W0Zt54r2Pnle+xp5QfeK5pUL2pcOdDX3Xqpf9C2xHJQXLWrv7cM25byMrCw5eW5na293X/eS/pRv6V9Ry87kIpRwwfyFc2u/3XZnn9ws9/c2t7WvaO5dLncv+cdD+UeCy7xahip6m9d0rlwq1yxZ0tnaLqfLC/ubV3a1D2LV3s6+7pVp8qLO1v7uXrm6ubetfWW/nFWYk13fPSCvaB6UB/ra5f4OtGlJN0qa++Se9t4Vnf397W1yyyBK2uXKm6pLUdrLCz293W0Drf1y50p5TUdna8dVdTHvXNnaNdCGVfu75bbOvp4u7KB5ZRvW6kSFVtTC7jNk+VLn3Su7BuXkzhS5fUULq3WlrZWXtK9rEldvY2Pube/r78XRoVOu6h6rX25rGrcguRN76W9fwTzY24m9tnWvWdnV3Xx1p2h0c8DU9l4Zx9uNXSEe6O8Z6Jfb2lcz56JOR3tXz7dGhIdJN9/UzXy74HYmOlyuy3DBfspD+SXZpeDcFgi6whPCqPAD4SjCIeGwsO+fF4B/XgD+eQH45wXgnxeA/3KOro62V2hW6ryu7MNr9Ni+uDoOB1b/9dvsQp3Bq8tigpglVokzxRsQF17Tw0ps9x+1wn6WsJp7MbCvO4iXPCMAn9t/XOf6dPB/G+C3YXPXeQ5BrfD5uJBqLSmNFs5Ak/Ap7BI+hlMIIhiQY0CqBKEHaT+C5J8QPhyvrMxWfJi7M3g+lpySfYgJxuKmZP9A+JDugySwIuPUmMnCJR+MlZUFifypAWI8NT37VGmI8AH8EYEKHwincKHxWuPJGdnnSnXIIMJ3QE8IWGFE+A14ESgowvvjia7sXUeFn6H8DeE4Do1VOz6mi8jGBn8ivAJGsAoHhQNByYHx8IhsKO0THgQCE4hPIpxGOIcgQrfwHGxA2IKwH0EEPWIrggehhnGEvcJetHM3+78MYg9CN8IWBBFdyP4Ls5xh4XlhGdix7gPCNojGfLPwCM+/j3kc5t9DfgLmz2CZ5buC5ScxZ/IngvzHsWzCfEcwfwz5Fsy381/3WIVHg+XVwgCv1x/MR4S+sQSroTQB5TJCJoKA1DaktqHrtrEVgZgIdwtdvKdRzLMxXxHI0V3rx2wOPkfrx2Nis0fQpevR9evRc+vRc+tBRNG6SzrrAjrpwjrUWYc661BnHXolU+jD/vrYfzcQGxBkBAH93od+7+OfJPShfh/qM/49iLcijLCSsAb9mIJW3S8sG0u24iJbOl6oZJccEZagqxVhyXhsfPaWKyVtCFuImIcHcz3TbefS9nFtGOO2j8fFB3LUWl4aLrTCHQgUohAnIuQiVCCIQutYosd6WJgHKzSghFs30A3CBnGDJGZWEONRIRvmawCXpFFIh2JUSLE2FpOCJm2PdkgrGLSyNlOraOdrpW5hg7BFEKyCRygRaoRGQWJfclIX5bCPO2aqinK2ho6EekMnQk+GSl7VhOqk6rTqnEoKfLAzX9Wk6lENqbaqRlTaraqtatoU2hM6FCoYQuXQzFAldH6oZFWTkdKNQgv7Px5iA0IPwlYEEX3ciHxZuA2hEWejEV1xG/IBMWDJgHAS6dOYS1jSo54e9fTI1SNXD+zLg3oumY/QhNATlKouSy7VYfrnmAQhCaXhyGX/aTuN+ByjEOZgSYclHZZ0qHWSXkALDYhlhPkIAuedRsBVg/iSLDMob0JQcfk5rnNJprC69ILSnDSRQrwpZCSFbE0hSnFJabZiR2Q0Ghsdjc7G5MbdYrej29md3L1brHHUOGuSa3aLJY4SZ0lyyW7R4/A4Pcme3aLVYXVak627xS3V+6uPVp+oFhuru6s3VAsFOHXjY+7MbJ7bnSw/MBYbl12gL51G9+NwGhHvQjiFIIAVsQehBKEbQaT7EVvpS8h9CbkvQQ1CI4KENV5i4QWxNShj/F1cxigmp9fIBRz4vrGinJrSORhyGxF2IQjY9j6U7+PaAWo/53sRn+b8mqD+COdbEV+qI2CAW8zD3GLcfosx+C+GRoQeBAlOCDfj4XAzaxmxFaEHYT+CKCzGdLNwM30J0z66T0hTdFnRVjCZ8JwxRmgMpQYahmtAR57neAfH93NcwnGiEj5H99Uc3Q/n6O6do0tCgibj/U9HtnFsU0JLdS+X6mpKdSmlOmwtBmygo9Ecqxgmv+d4HsdpSpRN97VN92eb7k823dM23Sqb7gYbqzcF966ORnEcyjDZzvEcjl1KqFX3Y6vuZquuwKor1ZGdBHuHMo4TOLYwTL54WV+hB+0R8gVetHWUjBWnWH0UeEb8Y8WlmE2OFc/E7OJY8U7M/jZW/Ij1VfI14Uca+Wos8Yy1NJqcJ7NFVv5zMP8TmQ17MT+H+VLM90AxcWL+/bHiO5n+s1j/CSx/D+wapv8MzOf1dpHZnP90sN5TY2kt2OuTY2mD2OsTkMZ7fWws7QxyHxlLux+zh8fSujDbMuZkBi4bK061lkaQpZBImW4rOCmzpDrY4yxsuQvzmYHKlWNprFYF68BHysccWZglMStfJQ6Yz7uzjjn4IOPBwZuYAg5utAWcPA8nem68Duw814w57sRWVC87z1j/UnyEDRy+JPqxndaPXsXxLcLif5LZY3utbx1i7hqznkjzEedB6785jliPJfrIojHrRJpPg4KjaT5KDlhH0cle1KXkoHV/2lLrSw4u3e1AKU71ruJ065OOxdbHnVges96Z9iozA1bgiBehuD5turW6eK91htNHUKwUY2dKiLXI0WstRPZUH5k9vtealehjpmRiG3sPWlOxR5eDm3JTwWGaB2oyoKSp+9Ut6kXqG9XT1DnqdLWsjldPUUdpjBqDJlwTpgnRaDQqjaihGtCw72kobvYhUJTKwH/HKDIsctpAGaaBz7co0VDcO95IoYpWLSwjXmMVVNWWeQvcVT61f4F3qrvKq5l/S90oIQ/VY8lL7/MRqK3DBcpYGy3sdyqHgBDPxgctLF+38cH6elLlnWiFqhbZ+9VCHEfIjYu9kqPMDKbVJeYS4/SIwhkV10FNQXzVV0PM13xRxBzv3V61sM77Yny9N5sR/vj6Ku9M9guXQ3QV7a6sOER7WFZfd4ispasqFzA+WVtRf1kN7LQH1aCYZUxtHOxMDexknKtVczVcpvbKilG7PaD0OpnNlHD5vM6VlgbaSsQusK35LEM1mgCJvK1EmsDUcD0EGtNf3VgYED1vTB8GvLEpTGnU6USVNCdTGS1wosKos4CL914RO5wBc+rByftxknreDyFXdJIDOrgKgjpUgzrf/srN/9PTXva/UCbjzb9ua2W/M2pyVLYjNHk3r+4we4daZHm07dfBHyC5mlpaO1je3O79taO9wtvmqJBHm1uvI25l4mZHxSi0VtbWjbYq7RVjzUpzpaO5on58z4byqmv6uv9yX+UbrtPYBtZYOetrT9V1xFVMvIf1VcX6qmJ97VH28L6qFpSRqvl1oxooqy+/NZCP09AQ3A9NFlt9mcnQM51vjmk283csh0XAYyvUXe8Nc5R5dQhMlF6aXspEuDuZKJz9kiwoMn9nms1ymDwfFBmQHeEoAzeYKzsrLv/19fX1MxgYcCPuHzBzXj9uWtvCKu8M9ruXYm9xpVdpqqjn3/oYCD7ldYrhaPGJYtpdvKF4S/Gu4v3F0sBAPbKNR+0n7LTR3m3fYN9i32Xfb1cxwa11B5XiXfY/2oUBXE2kH5/KCt7nAOb4x4r9A33sAeygDyHQnXvAXV5XaodWgX0rUkAcieBAyEFYiCDBvyL+BcJHCH9GEOFuxI8gPIswzjhCupBeae6sYD3Wu1nQMQvZ45l52VN9mDcvCeQLFwfyynmBvLg024z5WElOSKkeL94EDiN+A+F9hM8Q/oYgCdlCNm98ILBq6/ugz03QfPbdtH6G+tz9hH1TjTB39/e53dAX+OYawRlg36j51tfagPQNALoCJwQzVOLcPlZtgOWXHibAUPx/ALoOzUAKZW5kc3RyZWFtCmVuZG9iagoKMTYgMCBvYmoKMTA2NzcKZW5kb2JqCgoxNyAwIG9iago8PC9UeXBlL0ZvbnREZXNjcmlwdG9yL0ZvbnROYW1lL0RBQUFBQStUaW1lc05ld1JvbWFuUFNNVAovRmxhZ3MgNgovRm9udEJCb3hbLTU2OCAtMzA2IDIwMDAgMTAwN10vSXRhbGljQW5nbGUgMAovQXNjZW50IDg5MQovRGVzY2VudCAtMjE2Ci9DYXBIZWlnaHQgMTAwNgovU3RlbVYgODAKL0ZvbnRGaWxlMiAxNSAwIFIKPj4KZW5kb2JqCgoxOCAwIG9iago8PC9MZW5ndGggMjgxL0ZpbHRlci9GbGF0ZURlY29kZT4+CnN0cmVhbQp4nF2Ry26EIBSG9zwFy+liAl6mnUmMidVM4qKX1PYBFI6WpCJBXPj2hcO0TbqAfOfynxx+WN02rVaOvdpFdODoqLS0sC6bFUAHmJQmSUqlEu4W4S3m3hDmtd2+OphbPS5FQdibr63O7vRQyWWAO8JerASr9EQPH3Xn424z5gtm0I5yUpZUwujnPPXmuZ+BoerYSl9Wbj96yV/D+26AphgncRWxSFhNL8D2egJScF7S4notCWj5r5ZkUTKM4rO3vjXxrZznWek5RT7lgTPkLAmcRz4HPkXmge8jp4EfkFPMn+Mc7L/EPHIV+RL4MXITuI5zcIcm5itc/rZleEbw+cceKjZrvTX4GehJcENp+P0vs5igwvMNuzyJfgplbmRzdHJlYW0KZW5kb2JqCgoxOSAwIG9iago8PC9UeXBlL0ZvbnQvU3VidHlwZS9UcnVlVHlwZS9CYXNlRm9udC9EQUFBQUErVGltZXNOZXdSb21hblBTTVQKL0ZpcnN0Q2hhciAwCi9MYXN0Q2hhciAxMwovV2lkdGhzWzc3NyA2NjYgNjEwIDUwMCA1MDAgNTAwIDUwMCAyNTAgNzIyIDMzMyAzMzMgMzMzIDUwMCA1MDAgXQovRm9udERlc2NyaXB0b3IgMTcgMCBSCi9Ub1VuaWNvZGUgMTggMCBSCj4+CmVuZG9iagoKMjAgMCBvYmoKPDwvRjEgOSAwIFIvRjIgMTQgMCBSL0YzIDE5IDAgUgo+PgplbmRvYmoKCjIxIDAgb2JqCjw8L0ZvbnQgMjAgMCBSCi9Qcm9jU2V0Wy9QREYvVGV4dF0KPj4KZW5kb2JqCgoxIDAgb2JqCjw8L1R5cGUvUGFnZS9QYXJlbnQgNCAwIFIvUmVzb3VyY2VzIDIxIDAgUi9NZWRpYUJveFswIDAgNTk1IDg0Ml0vR3JvdXA8PC9TL1RyYW5zcGFyZW5jeS9DUy9EZXZpY2VSR0IvSSB0cnVlPj4vQ29udGVudHMgMiAwIFI+PgplbmRvYmoKCjQgMCBvYmoKPDwvVHlwZS9QYWdlcwovUmVzb3VyY2VzIDIxIDAgUgovTWVkaWFCb3hbIDAgMCA1OTUgODQyIF0KL0tpZHNbIDEgMCBSIF0KL0NvdW50IDE+PgplbmRvYmoKCjIyIDAgb2JqCjw8L1R5cGUvQ2F0YWxvZy9QYWdlcyA0IDAgUgovT3BlbkFjdGlvblsxIDAgUiAvWFlaIG51bGwgbnVsbCAwXQovTGFuZyh6aC1DTikKPj4KZW5kb2JqCgoyMyAwIG9iago8PC9DcmVhdG9yPEZFRkYwMDU3MDA3MjAwNjkwMDc0MDA2NTAwNzI+Ci9Qcm9kdWNlcjxGRUZGMDA0RjAwNzAwMDY1MDA2RTAwNEYwMDY2MDA2NjAwNjkwMDYzMDA2NTAwMjAwMDM0MDAyRTAwMzAwMDJFMDAzMD4KL0NyZWF0aW9uRGF0ZShEOjIwMTgwMzMwMDkzOTA2KzA4JzAwJyk+PgplbmRvYmoKCnhyZWYKMCAyNAowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwNDUwNTkgMDAwMDAgbiAKMDAwMDAwMDAxOSAwMDAwMCBuIAowMDAwMDAxMzUzIDAwMDAwIG4gCjAwMDAwNDUyMDIgMDAwMDAgbiAKMDAwMDAwMTM3NCAwMDAwMCBuIAowMDAwMDE5NTc5IDAwMDAwIG4gCjAwMDAwMTk2MDEgMDAwMDAgbiAKMDAwMDAxOTc4NSAwMDAwMCBuIAowMDAwMDIwNzUxIDAwMDAwIG4gCjAwMDAwMjE1NDggMDAwMDAgbiAKMDAwMDAzMjU5NCAwMDAwMCBuIAowMDAwMDMyNjE3IDAwMDAwIG4gCjAwMDAwMzI4MjMgMDAwMDAgbiAKMDAwMDAzMzE3NCAwMDAwMCBuIAowMDAwMDMzMzk2IDAwMDAwIG4gCjAwMDAwNDQxNjAgMDAwMDAgbiAKMDAwMDA0NDE4MyAwMDAwMCBuIAowMDAwMDQ0Mzg0IDAwMDAwIG4gCjAwMDAwNDQ3MzUgMDAwMDAgbiAKMDAwMDA0NDk1MiAwMDAwMCBuIAowMDAwMDQ1MDA0IDAwMDAwIG4gCjAwMDAwNDUzMDEgMDAwMDAgbiAKMDAwMDA0NTM5OCAwMDAwMCBuIAp0cmFpbGVyCjw8L1NpemUgMjQvUm9vdCAyMiAwIFIKL0luZm8gMjMgMCBSCi9JRCBbIDwwMzhFQUZEMzYwQzRCOEUzNzFDRDE5OTk0ODIzQTU0MT4KPDAzOEVBRkQzNjBDNEI4RTM3MUNEMTk5OTQ4MjNBNTQxPiBdCi9Eb2NDaGVja3N1bSAvOTVBOTMyMTczMUQ0RTY4RTdFMzhBRkM3QjRDNzE5M0EKPj4Kc3RhcnR4cmVmCjQ1NTc3CiUlRU9GCg==</value>");
		sb1.append("										</observationMedia>");
		sb1.append("									</component>");
		sb1.append("									<!-- 	");						
		sb1.append("										检查报告类型标识信息(下面是检查报告的展示, 实际标识值按报告内容填写)");
		sb1.append("										1. @code=\"301\" @displayName=\"X线检查报告单\"");
		sb1.append("										2. @code=\"302\" @displayName=\"CT检查报告单\"");
		sb1.append("		                                3. @code=\"303\" @displayName=\"MRI检查报告单\"");
		sb1.append("		                                4. @code=\"304\" @displayName=\"核医学科PET/CT中心\"");
		sb1.append("									-->");
		sb1.append("									");
		sb1.append("									<component>");
		sb1.append("										<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("											<!-- 检查报告类型标识编码/检查报告类型标识名称 -->");
		sb1.append("											<code code=\"103\" codeSystem=\"1.2.156.112685.1.1.112\" displayName=\"\"/>");
		sb1.append("										</observation>");
		sb1.append("									</component>");
		sb1.append("									");
		sb1.append("													");		
		sb1.append("								</organizer>");
		sb1.append("							</entry>");	
		sb1.append("							");
		sb1.append("			                <!--****************************************************************************-->");
		sb1.append("							<!-- 检查报告条目 -->");
		sb1.append("		                    <entry typeCode=\"DRIV\">");
		sb1.append("				                <organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb1.append("");
		sb1.append("		                            <!-- 检查号-->"); 
		sb1.append("		                            <id extension=\"\"/>"); 
		sb1.append("");
		sb1.append("				                    <!-- 检查类型编码/检查类型名称 -->");
		sb1.append("				                    <code code=\"CT\" codeSystem=\"1.2.156.112685.1.1.41\" displayName=\"CT\"/>");	
		sb1.append("									");
		sb1.append("				                    <!-- 必须固定项 -->");
		sb1.append("					                <statusCode code=\"completed\"/>");
		sb1.append("					  ");
		sb1.append("					                <!-- study -->");
		sb1.append("					                <component typeCode=\"COMP\">");
		sb1.append("						                <observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("							                <!-- 检查项目编码/检查项目名称 -->");
		sb1.append("							                <code code=\"EBABP00100\" codeSystem=\"1.2.156.112685.1.1.88\" displayName=\"头部X线计算机体层(CT)平扫\"/>");
		sb1.append("											");
		sb1.append("		                                    <!-- 检查备注 -->");
		sb1.append("							                <text/>");
		sb1.append("							   ");
		sb1.append("							                <!-- 必须固定项 -->");
		sb1.append("							                <statusCode code=\"completed\"/>");
		sb1.append("											");
		sb1.append("							                <!-- 检查报告结果-客观所见/影像学表现(能够与项目对应时的填写处 - @code:01表示客观所见, 02表示主观意见) -->");
		sb1.append("							                <value xsi:type=\"CD\" code=\"01\" codeSystem=\"1.2.156.112685.1.1.98\">");
		sb1.append("												<originalText>;腰椎生理弯曲存在，各椎体前缘骨质增生。腰 椎间盘环形膨出椎体轮廓外、轻度压迫硬膜囊前缘。骨性椎管通畅，各椎小关节未见异常。</originalText>");
		sb1.append("							                </value>");
		sb1.append("							  ");
		sb1.append("		                                    <!-- 检查报告结果-主观意见/影像学结论(能够与项目对应时的填写处 - @code:01表示客观所见, 02表示主观意见) -->");
		sb1.append("							                <value xsi:type=\"CD\" code=\"02\" codeSystem=\"1.2.156.112685.1.1.98\">");
		sb1.append("												<originalText>椎间盘膨出</originalText>");
		sb1.append("							                </value>");
		sb1.append("							  ");
		sb1.append("							                <!-- 检查方法编码/检查方法名称 -->");
		sb1.append("							                <methodCode code=\"\" codeSystem=\"1.2.156.112685.1.1.43\" displayName=\"\"/>");
		sb1.append("							  ");
		sb1.append("		                                    <!-- 检查部位编码/检查部位名称 -->");
		sb1.append("							                <targetSiteCode code=\"\" codeSystem=\"1.2.156.112685.1.1.42\" displayName=\"\"/>");
		sb1.append("							  ");
		sb1.append("							                <!-- 检查医师信息(可循环) -->");
		sb1.append("							                <performer>");
		sb1.append("							                    <!-- 检查日期 -->");
		sb1.append("			                                    <time value=\"20180330093259\"/>");
		sb1.append("												<assignedEntity>");
		sb1.append("													<!-- 检查医生编码 -->");
		sb1.append("					                                <id root=\"1.2.156.112685.1.1.2\" extension=\"USER001\"/>");
		sb1.append("				                                    <assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb1.append("				                                        <!-- 检查医生名称 -->");
		sb1.append("						                                <name>管理员</name>");
		sb1.append("					                                </assignedPerson>");
		sb1.append("					                                <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
		sb1.append("					                                   <!-- 检查科室编码 -->");
		sb1.append("					                                   <id root=\"1.2.156.112685.1.1.1\" extension=\"\"/>");
		sb1.append("												       <!-- 检查科室名称 -->");
		sb1.append("												       <name/>");
		sb1.append("											        </representedOrganization>");
		sb1.append("												</assignedEntity>");
		sb1.append("											</performer>");
		sb1.append("											");
		sb1.append("											<participant typeCode=\"DEV\">");
		sb1.append("											    <!--对应动态心电图-->");
		sb1.append("					                            <time>");
		sb1.append("					                                <!--记录开始时间-->");
		sb1.append("					                                <low value=\"\"/>");
		sb1.append("					                                <!--记录总时间-->");
		sb1.append("					                                <width xsi:type=\"PQ\" value=\"\" unit=\"\"/>");
		sb1.append("					                            </time>");
		sb1.append("												<participantRole>");
		sb1.append("													<playingDevice>");
		sb1.append("														<!--仪器型号-->");
		sb1.append("														<manufacturerModelName code=\"\"/>");
		sb1.append("													</playingDevice>");
		sb1.append("												</participantRole>");
		sb1.append("											</participant>");
		sb1.append("											");
		sb1.append("												");								
		sb1.append("											<!-- 仪器或医生客观所见信息(超声心动报告和动态心电图等结构化所见部分的信息) -->");
		sb1.append("											<entryRelationship typeCode=\"COMP\">");
		sb1.append("											    <organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb1.append("											        <code code=\"365605003\" codeSystem=\"2.16.840.1.112685.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"body measurement finding\"/>");
		sb1.append("													<statusCode code=\"completed\"/>");
		sb1.append("													");
		sb1.append("													<!-- 项目信息(可循环) -->");
		sb1.append("													<!--");
		sb1.append("													PQ: <value xsi:type=\"PQ\" value=\"19.1\" unit=\"10^9/L\">数值类型的结果+单位)");
		sb1.append("													SC: <value xsi:type=\"SC\">1mm</value>  文本类型结果或者数字结果和单位无法分开的");
		sb1.append("													-->");
		sb1.append("													<!-- 当只有一级关系时使用 -->");	
		sb1.append("													<component>");
		sb1.append("														<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("															<code code=\"\" displayName=\"\"/>");
		sb1.append("															<value xsi:type=\"SC\"/>");
		sb1.append("														</observation>");
		sb1.append("													</component>");
		sb1.append("													<!-- 存在多级关系时使用 -->");												
		sb1.append("													<component>");
		sb1.append("														<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("														    <!-- 项目编码/项目名称 -->");
		sb1.append("															<code code=\"\" displayName=\"\"/>");
		sb1.append("															<!-- 带单位的类型为PQ，不带单位为SC 项目结果值文本 数据类型为SC的去掉@Unit-->");
		sb1.append("															<value xsi:type=\"SC\"/>");
		sb1.append("															<entryRelationship typeCode=\"COMP\">");
		sb1.append("																<organizer classCode=\"BATTERY\" moodCode=\"EVN\">");
		sb1.append("																	<statusCode code=\"completed\"/>");
		sb1.append("																	<!-- 具体检查项信息(可循环) -->");
		sb1.append("																	<!--带单位的类型为PQ，不带单位为SC-->");
		sb1.append("																	<component>");
		sb1.append("																		<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("																			<code code=\"\" displayName=\"\"/>");
		sb1.append("																			<!--占总数等说明-->");
		sb1.append("																			<text/>");
		sb1.append("																			<value xsi:type=\"PQ\" value=\"\" unit=\"\"/>");	
		sb1.append("																		</observation>");
		sb1.append("																	</component>");						
		sb1.append("																	<!-- 其它信息按上面格式添加 -->");
		sb1.append("																</organizer>");
		sb1.append("															</entryRelationship>");
		sb1.append("														</observation>");
		sb1.append("													</component>");
		sb1.append("													<!-- 其它信息按上面格式添加 -->");
		sb1.append("												</organizer>");
		sb1.append("											</entryRelationship>");
		sb1.append("									");
		sb1.append("									        <!-- 图像信息(能与项目对应的图像) -->");
		sb1.append("									");
		sb1.append("											<!-- 当有多个影像对应同一个study时,可以复用此entryRelationship -->");
		sb1.append("											");
		sb1.append("						                </observation>");
		sb1.append("						            </component>");
		sb1.append("						            <!-- 其他项目按上面结构和格式添加 -->");
		sb1.append("						        </organizer>");
		sb1.append("						    </entry>");
		sb1.append("						</section>");
		sb1.append("					</component>");
		sb1.append("		  		");
		sb1.append("		<!-- ");
		sb1.append("		********************************************************");
		sb1.append("		诊断");
		sb1.append("		********************************************************");
		sb1.append("		-->");
		sb1.append("					<component>");
		sb1.append("						<section>");
		sb1.append("							<code code=\"29308-4\" codeSystem=\"2.16.840.1.112685.6.1\" codeSystemName=\"LOINC\" displayName=\"Diagnosis\"/>"); 
		sb1.append("							<title>诊断</title>"); 
		sb1.append("							<entry typeCode=\"DRIV\">");
		sb1.append("								<act classCode=\"ACT\" moodCode=\"EVN\">");
		sb1.append("									<code nullFlavor=\"NA\"/>");
		sb1.append("									<entryRelationship typeCode=\"SUBJ\">");
		sb1.append("										<observation classCode=\"OBS\" moodCode=\"EVN\">");
		sb1.append("											<!-- 诊断类别编码/诊断类别名称 -->");
		sb1.append("											<code code=\"\" codeSystem=\"1.2.156.112685.1.1.29\" displayName=\"\"/>");
		sb1.append("											<statusCode code=\"completed\"/>");
		sb1.append("		                                    <!-- 疾病编码/疾病名称(没有编码去掉@code) -->");
		sb1.append("											<value xsi:type=\"CD\" code=\"\" codeSystem=\"1.2.156.112685.1.1.30\" displayName=\"\"/>");        
		sb1.append("										</observation>");
		sb1.append("									</entryRelationship>");
		sb1.append("								</act>");
		sb1.append("							</entry>");
		sb1.append("						</section>");
		sb1.append("				    </component>");
		sb1.append("				</structuredBody>");
		sb1.append("			</component>");
		sb1.append("		</ClinicalDocument>");
		
		FileWriter writer;
        try {
            writer = new FileWriter("E:/token.txt");
            writer.write(sb1.toString());
            writer.flush();
            writer.close();
		ResPacsMessageFZ rpm=new ResPacsMessageFZ(sb1.toString(),true);
	
			
		  System.out.print("12");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
}
