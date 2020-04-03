package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.synjones.framework.persistence.JdbcQueryManager;

public class CUSTOMEditSendMessageHK305 {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private Custom custom=new Custom();
	private ResCustomBeanHK rb1= new ResCustomBeanHK();
	
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public CUSTOMEditSendMessageHK305(Custom custom){
		this.custom=custom;
	}
	
	/* 
	 *根据数据字典id查询民族
	 */
	public Custom dataidFromRemark(Custom custom){
		
		Connection sjzd = null;
		try {
			sjzd = jdbcQueryManager.getConnection();
			String sb = "select remark from  data_dictionary where id='" + custom.getNATION() + "'";
			ResultSet minzu = sjzd.createStatement().executeQuery(sb);
			if(minzu.next()){
				String mzhz = minzu.getString("remark");
				custom.setNATION(mzhz);
				
			}
			sjzd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return custom;
		
	}
	/**
	 * 
	 * @param url
	 * @param logname
	 * @return
	 */
	public FeeResultBody getMessage(String url,String configvalue,String logname) {
		if(StringUtils.isEmpty(this.custom.getID_NO())) {
			this.custom.setID_NO(this.custom.getEXAM_NUM());
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		long examid=examIdForExamNum(this.custom.getEXAM_NUM(),logname);
		FeeResultBody rb= new FeeResultBody();
		if(examid<=0){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("无效体检编号");
		}else{
			try {				
				ResCustomBeanHK rcb= new ResCustomBeanHK();
				rcb=this.searchPatid(configvalue, logname);
				if("AA".equals(rcb.getCode())){
					if(rcb.getFaly()==true){//  没有 controlActProcess 节点 没有档案信息 需要调用新增接口
						setSearchString(url,"active",logname);//新增接口
						insert_search(examid,custom.getEXAM_NUM(),rcb.getPersionid(),logname);
						//插入表
						/*CustomResBean cus= new CustomResBean();
						cus.setCLINIC_NO(rcb.getPersionid());
						cus.setPATIENT_ID(rcb.getPersionid());
						cus.setVISIT_NO(rcb.getPersionid());
						cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
						List<CustomResBean> LIST=new ArrayList<CustomResBean>();
						LIST.add(cus);
						ControlActProcess ControlActProcess=new ControlActProcess();
						ControlActProcess.setLIST(LIST);
					    rb.setControlActProcess(ControlActProcess);*/
					    rb.getResultHeader().setTypeCode("AA");
					    rb.getResultHeader().setText(rcb.getCodetext());
					}else{
						//setSearchString(url,"active",logname);
						insert_search(examid,custom.getEXAM_NUM(),rcb.getPersionid(),logname);
						//插入表
						/*CustomResBean cus= new CustomResBean();
						cus.setCLINIC_NO(rcb.getPersionid());
						cus.setPATIENT_ID(rcb.getPersionid());
						cus.setVISIT_NO(rcb.getPersionid());
						cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
						List<CustomResBean> LIST=new ArrayList<CustomResBean>();
						LIST.add(cus);
						ControlActProcess ControlActProcess=new ControlActProcess();
						ControlActProcess.setLIST(LIST);
					    rb.setControlActProcess(ControlActProcess);*/
					    rb.getResultHeader().setTypeCode("AA");
					    rb.getResultHeader().setText(rcb.getCodetext());
					}
						
					}else{
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("查询接口返回错误");
					}
			} catch (Exception ex){
			    rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("发送病人信息-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
				TranLogTxt.liswriteEror_to_txt(logname,"发送病人信息-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		
		return rb;
	}
	
	
	
			/**
			 * 新增
			 * @return
			 */
	private void setSearchString(String url,String statusCode,String logname){
		Custom remark = dataidFromRemark(custom);
		StringBuffer sb0=new StringBuffer();	                                                                                                                 
		sb0.append("<PRPA_IN201311UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb0.append("	<!-- 消息ID -->  ");
		sb0.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>  ");
		sb0.append("	<!-- 消息创建时间 -->   ");
		sb0.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>");
		sb0.append("	<!-- 交互ID(HL7交互类型代码系统) 用来区分消息类型，S0001代表患者注册     -->  ");
		sb0.append("	<interactionId extension=\"S0001\"/> ");
		
		sb0.append("	<!-- 接收者 --> ");
		sb0.append("  <receiver  code=\"SYS001\"/> "); 
		sb0.append("	<!-- 发送者 --> ");
		sb0.append("  <sender    code=\"SYS009\"/> ");
		sb0.append("	<!-- 封装的消息内容 - Trigger Event Control Act wrapper --> ");
		sb0.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">   ");
		sb0.append("		<code  value=\"create\"/> ");
		sb0.append("		<subject typeCode=\"SUBJ\">   ");
		sb0.append("			<registrationRequest classCode=\"REG\" moodCode=\"RQO\">");
		sb0.append("				<subject1 typeCode=\"SBJ\"> ");
		sb0.append("					<patient classCode=\"PAT\"> ");
		sb0.append("	<!-- 注册，更新时为active，作废时为disable  --> ");
		sb0.append("	<statusCode code=\"active\"/>   ");
		sb0.append("	<!-- 患者信息来源，1.门诊 2.住院 3.体检(1..1) -->  ");
		sb0.append("	<source  code=\"3\" displayName=\"体检中心\"/>   ");
		sb0.append("	<!-- 必须项已使用 --> ");
		sb0.append("	<!-- 各种标识符 患者在医院中的ID(1..*)-->   ");
		sb0.append("	<id>   ");
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.1\"/>");//患者EMPI标识
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.13\"/>");//住院号
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.12\"/>");//就诊卡号
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.11\"/>");//医保号
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.2\"/>");//域ID(门诊、住院)
		sb0.append("		<item extension=\""+rb1.getPersionid()+"\" identifierName=\"\" root=\"2.16.156.10011.0.2.2\"/>");//患者ID
		sb0.append("	</id> ");
		sb0.append("	<!-- 患者建卡日期(0..1) -->   ");
		sb0.append("	<effectiveDate value=\"20120606\"/> ");
		sb0.append("	<!-- 上次就诊日期(0..1) -->  ");
		sb0.append("	<lastVisitDate value=\"20120606\"/> ");
		sb0.append("	<!-- 保密等级(2:内部级，3:秘密级，4:机密级，5:绝密级)(0..1) -->");
		sb0.append("	<confidentialityCode code=\"2\" displayName=\"内部级\"/> ");
		sb0.append("	<!-- 费别,见费别字典(0..1) -->");
		sb0.append("	<chargeType code=\"13\" displayName=\"收费\"/> ");
		sb0.append("	<!-- 患者基本信息(1..1) --> ");
		sb0.append("	<patientPerson>");
		sb0.append("		<!-- 身份证号及各种证件号(身份证号必须有)(1..*)");
		sb0.append("		@extension: 证件号码 ");
		sb0.append("	@root: 证件OID ");
		sb0.append("	@controlInformationExtension: 证件名称 --> ");
		sb0.append("	<id>   ");
		sb0.append("		<item extension=\""+custom.getID_NO()+"\" root=\"2.16.156.10011.1.3\" controlInformationExtension=\"居民身份证\"/>");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.14\" controlInformationExtension=\"居民户口簿\"/>   ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.15\" controlInformationExtension=\"护照\"/> ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.16\" controlInformationExtension=\"军官证\"/> ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.17\" controlInformationExtension=\"驾驶证\"/> ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.18\" controlInformationExtension=\"港澳居民来往内地通行证\"/> ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.19\" controlInformationExtension=\"台湾居民来往内地通行证\"/> ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.20\" controlInformationExtension=\"其他法定有效证件\"/> ");
		sb0.append("	</id> ");
		sb0.append("	<!--姓名 (1..1)-->");
		sb0.append("	<name value=\""+custom.getNAME()+"\"/>   ");
		sb0.append("	<!-- 家庭电话，电子邮件等联系方式 ");
		sb0.append("	@use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->  ");
		sb0.append("	<!-- 患者电话或电子邮件(1..*) --> ");
		sb0.append("	<telecom use=\"H\" value=\""+custom.getNEXT_OF_KIN_PHONE()+"\"/>   ");
		sb0.append("	<telecom use=\"PUB\" value=\""+custom.getPHONE_NUMBER_BUSINESS()+"\"/> ");
		sb0.append("							<telecom use=\"EMA\" value=\""+custom.getE_NAME()+"\"/>  ");
		sb0.append("							<!--性别(1..1)-->");
		String sexcode="1";
		if("男".endsWith(custom.getSEX())){
			sexcode="1";
		}else if("女".endsWith(custom.getSEX())){
			sexcode="2";
		}
		sb0.append("<administrativeGenderCode code=\""+sexcode+"\" displayName=\""+custom.getSEX()+"\"/>");
		sb0.append("	<!--婚姻状况(0..1)-->");
		sb0.append("	<maritalStatusCode code=\"\"  displayName=\""+custom.getMARITAL_STATUS()+"\"/>  ");
		sb0.append("	<!-- 文化程度代码(0..1) -->  ");
		sb0.append("	<educationLevelCode code=\"\" displayName=\""+custom.getDEGREE()+"\"/>   ");
		sb0.append("	<!--民族代码(1..*) -->   ");
		sb0.append("	<ethnicGroupCode code=\"\" displayName=\""+remark.getNATION()+"\"/>   ");
		sb0.append("	<!--国籍代码,见国家及地区字典(0..1) -->   ");
		sb0.append("	<citizenCode code=\"53\"   displayName=\"中国\"/> ");
		sb0.append("	<!--身份代码,见身份字典(0..1) -->   ");
		sb0.append("	<identityCode code=\"I\" displayName=\"其他\"/> ");
		sb0.append("	<!--出生日期(1..1)-->");
		sb0.append("	<birthTime value=\""+custom.getDATE_OF_BIRTH().toString().replaceAll("-", "")+"\"/>");
		sb0.append("	<!-- 出生地(0..1)  -->   ");
		
		sb0.append("	<addr use=\"PUB\">   ");
		sb0.append("		<item> ");
		sb0.append("			<!--非结构化地址（完整地址描述） --> ");
		sb0.append("			<part type=\"SAL\" value=\""+custom.getBIRTH_PLACE()+"\"/>");
		sb0.append("			<!--地址-省（自治区、直辖市） -->");
		sb0.append("			<part type=\"STA\" value=\"\"/>   ");
		sb0.append("			<!--地址-市（地区） --> ");
		sb0.append("			<part type=\"CTY\" value=\"\"/>   ");
		sb0.append("			<!--地址-县（区） -->   ");
		sb0.append("			<part type=\"CNT\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-乡（镇、街道办事处） -->  ");
		sb0.append("			<part type=\"STB\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-村（街、路、弄等） -->");
		sb0.append("			<part type=\"STR\" value=\"\"/> ");
		sb0.append("			<!-- 地址-门牌号码 -->  ");
		sb0.append("			<part type=\"BNR\" value=\"\"/> ");
		sb0.append("			<!-- 邮政编码--> ");
		sb0.append("			<part type=\"ZIP\" value=\""+custom.getZIP_CODE()+"\"/>");
		sb0.append("		</item>  ");
		sb0.append("	</addr>   ");
		
		sb0.append("	<!--职业类别代码(0..*)--> ");
		sb0.append("	<asEmployee classCode=\"EMP\">");
		sb0.append("		<!--职业编码系统名称/职业代码(0..*)-->");
		sb0.append("		<occupationCode code=\"\" displayName=\"\" />");
		sb0.append("		<employerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		sb0.append("			<!--机构识别号(0..*)-->");
		sb0.append("			<id>1289348683278175</id>  ");
		sb0.append("			<!--工作单位名称(0..*)-->  ");
		sb0.append("			<name value=\"\"/>  ");
		
		sb0.append("			<!-- 工作单位联系方式(0..*) -->");
		sb0.append("			<telecom use=\"WP\" value=\"\"/> ");
		sb0.append("			<!-- 工作单位地址(0..*) -->");
		sb0.append("			<addr use=\"WP\">  ");
		sb0.append("					<!--非结构化地址（完整地址描述） -->");
		sb0.append("	<part type=\"SAL\" value=\"\"/> ");
		sb0.append("	<!--地址-省（自治区、直辖市） -->   ");
		sb0.append("	<part type=\"STA\" value=\"\"/>   ");
		sb0.append("	<!--地址-市（地区） --> ");
		sb0.append("	<part type=\"CTY\" value=\"\"/>   ");
		sb0.append("	<!--地址-县（区） -->   ");
		sb0.append("	<part type=\"CNT\" value=\"\"/>   ");
		sb0.append("	<!-- 地址-乡（镇、街道办事处） -->  ");
		sb0.append("	<part type=\"STB\" value=\"\"/>   ");
		sb0.append("	<!-- 地址-村（街、路、弄等） -->");
		sb0.append("	<part type=\"STR\" value=\"\"/> ");
		sb0.append("	<!-- 地址-门牌号码 -->  ");
		sb0.append("	<part type=\"BNR\" value=\"\"/>");
		sb0.append("	<!-- 邮政编码-->");
		sb0.append("					<part type=\"ZIP\" value=\"\"/>   ");
		sb0.append("			</addr>   ");
		sb0.append("		</employerOrganization> ");
		sb0.append("	</asEmployee>   ");
		
		sb0.append("	<!--联系人(0..*)--> ");
		sb0.append("	<personalRelationship>  ");
		sb0.append("		<!-- 与患者关系 -->");
		sb0.append("		<code code=\"01\" displayName=\"\"/>  ");
		sb0.append("		<!--联系人基本信息(0..1)--> ");
		sb0.append("		<contactPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">   ");
		sb0.append("			<!-- 联系人姓名(0..*) -->  ");
		sb0.append("	<name value=\"LIST_EN\"/>  ");
		sb0.append("	<!-- 联系人电话或电子邮件(0..*) -->  ");
		sb0.append("	<telecom use=\"H\" value=\"\"/>");
		sb0.append("	<!-- 联系人地址(0..*) -->  ");
		sb0.append("	<addr use=\"WP\">");
		sb0.append("		<item> ");
		sb0.append("			<!--非结构化地址（完整地址描述） -->  ");
		sb0.append("			<part type=\"SAL\" value=\"\"/> ");
		sb0.append("	<!--地址-省（自治区、直辖市） --> ");
		sb0.append("	<part type=\"STA\" value=\"\"/> ");
		sb0.append("	<!--地址-市（地区） -->   ");
		sb0.append("	<part type=\"CTY\" value=\"\"/> ");
		sb0.append("	<!--地址-县（区） --> ");
		sb0.append("	<part type=\"CNT\" value=\"\"/> ");
		sb0.append("	<!-- 地址-乡（镇、街道办事处） -->");
		sb0.append("	<part type=\"STB\" value=\"\"/> ");
		sb0.append("			<!-- 地址-村（街、路、弄等） -->  ");
		sb0.append("			<part type=\"STR\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-门牌号码 -->");
		sb0.append("			<part type=\"BNR\" value=\"\"/>  ");
		sb0.append("			<!-- 邮政编码-->");
		sb0.append("			<part type=\"ZIP\" value=\"\"/> ");
		sb0.append("		</item> ");
		sb0.append("							</addr>   ");
		sb0.append("			</contactPerson>");
		sb0.append("		</personalRelationship>   ");
		sb0.append("	</patientPerson> ");
		sb0.append("	<!-- 建卡机构、医疗机构信息(1..1)  -->   ");
		sb0.append("			<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">   ");
		sb0.append("				<!--医疗机构编码-->");
		sb0.append("				<id extension=\"46600083-8\" root=\"2.16.156.10011.1.5\"/> ");
		sb0.append("	<!--医疗机构名称 --> ");
		sb0.append("	<name value=\"LIST_EN\"/>");
		sb0.append("	<!--医疗机构联系信息 -->   ");
		sb0.append("	<contactParty classCode=\"CON\"> ");
		sb0.append("		<!-- 联系人电话或电子邮件 -->  ");
		sb0.append("		<telecom use=\"H\" value=\"\"/>");
		sb0.append("		<!-- 联系人地址 -->");
		sb0.append("		<addr use=\"WP\"> ");
		sb0.append("		<item>");
		sb0.append("			<!--非结构化地址（完整地址描述） -->");
		sb0.append("			<part type=\"SAL\" value=\"\"/> ");
		sb0.append("			<!--地址-省（自治区、直辖市） -->   ");
		sb0.append("			<part type=\"STA\" value=\"\"/> ");
		sb0.append("			<!--地址-市（地区） -->   ");
		sb0.append("			<part type=\"CTY\" value=\"\"/> ");
		sb0.append("			<!--地址-县（区） -->");
		sb0.append("			<part type=\"CNT\" value=\"\"/>");
		sb0.append("			<!-- 地址-乡（镇、街道办事处） -->  ");
		sb0.append("			<part type=\"STB\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-村（街、路、弄等） -->");
		sb0.append("			<part type=\"STR\" value=\"\"/> ");
		sb0.append("			<!-- 地址-门牌号码 -->  ");
		sb0.append("			<part type=\"BNR\" value=\"\"/>");
		sb0.append("			<!-- 邮政编码-->  ");
		sb0.append("			<part type=\"ZIP\" value=\"\"/>   ");
		sb0.append("		</item>   ");
		sb0.append("	</addr> ");
		sb0.append("				</contactParty>");
		sb0.append("			</providerOrganization>");
		
		sb0.append("<!-- 描述和患者相关的一些Observation信息，比如血型，过敏信息，身高，体重，年龄等 (0..*) -->");
		sb0.append("<subjectOf typeCode=\"OBS\">   ");
		sb0.append("	<!-- Observation详细信息(1..1) -->");
		sb0.append("	<observation classCode=\"OBS\" moodCode=\"EVN\"> ");
		sb0.append("		<!--observation类型编码(1..1)--> ");
		sb0.append("		<code code=\"01\" displayName=\"血型\"/>   ");
		sb0.append("					<!--observation的值(0..1)--> ");
		sb0.append("					<value code=\"\" displayName=\"\"/> ");
		sb0.append("					<!--该Observation的详细描述(0..1)-->   ");
		sb0.append("					<text/>");
		sb0.append("				</observation> ");
		sb0.append("			</subjectOf>   ");
		sb0.append("		</patient>");
		sb0.append("	</subject1>");
		sb0.append("				<!--操作者信息(0..1)-->");
		sb0.append("				<author code=\"\" displayName=\"\"/>");
		sb0.append("			</registrationRequest> ");
		sb0.append("		</subject>   ");
		sb0.append("	</controlActProcess> ");
		sb0.append("</PRPA_IN201311UV02>"); 
		TranLogTxt.liswriteEror_to_txt(logname,"request:"+sb0.toString());
		System.err.println(sb0.toString());
		String result = HttpUtil.doPost_Str(url,sb0.toString(), "utf-8");
		
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
	}
	
	/**
	 * 查询
	 * @return
	 */
	private ResCustomBeanHK searchPatid(String url,String logname){	
		 //ResCustomBeanHK rb= new ResCustomBeanHK();
		try{
		StringBuffer sb0=new StringBuffer();
		sb0.append("<PRPA_IN201305UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");  
		sb0.append("  <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/> "); 
		sb0.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/> "); 
		sb0.append("  <interactionId  extension=\"S0004\"/>"); 
		sb0.append("  <receiver  code=\"SYS001\"/>"); 
		sb0.append("  <sender  code=\"SYS009\"/>");
		sb0.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">  ");
		sb0.append("  <queryByParameter> "); 
		sb0.append("  <id>");
		sb0.append("  <item extension=\""+this.custom.getID_NO()+"\" root=\"2.16.156.10011.1.3\"/> ");
		sb0.append("  </id>   ");
		/*
		if(this.custom.getID_NO().length()<=0 || this.custom.getID_NO().equals("")){
			
		sb0.append("  <name value=\""+this.custom.getNAME()+"\" /> ");
		sb0.append("  <administrativeGenderCode code=\""+"1"+"\" displayName=\""+this.custom.getSEX()+"\"/> ");
		sb0.append("  <birthTime value=\""+19910907+"\" /> ");
		*/
		sb0.append("  <identityCode code=\"I\" displayName=\"其他\"/> ");
			
		//}
		
		sb0.append("  </queryByParameter>  "); 
		sb0.append("  </controlActProcess>   "); 
		sb0.append("</PRPA_IN201305UV02>	 "); 
		TranLogTxt.liswriteEror_to_txt(logname,"request:"+sb0.toString());
		 String result = HttpUtil.doPost_Str(url,sb0.toString(), "utf-8");
		  TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
		 if(result.trim().length()>0) {
			 try{
					InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
					Map<String, String> xmlMap = new HashMap<>();
					xmlMap.put("abc", "urn:hl7-org:v3");
					SAXReader sax = new SAXReader();
					
					sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
					Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
					rb1.setCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
					rb1.setPersionid(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText());
					rb1.setFaly(document.selectNodes("abc:MCCI_IN000002UV01/abc:controlActProcess").size()==0);
					
					}catch(Exception ex){
						try{
							InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
							Map<String, String> xmlMap = new HashMap<>();
							xmlMap.put("abc", "urn:hl7-org:v3");
							SAXReader sax = new SAXReader();
							sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
							Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
							rb1.setCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
							rb1.setCodetext(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:acknowledgementDetail/abc:text/@value").getText());
							}catch(Exception ext){
								rb1.setCode("AE");
								rb1.setCodetext(ext.toString());
							}
					}
		  }else{
			  rb1.setCode("AE");
			  rb1.setCodetext("");
		  }
		}catch(Exception ex){
			 rb1.setCode("AE");
		}
		return rb1;
	}
	
	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private ResCustomBeanHK res_search(long exam_id,String logname){
		ResCustomBeanHK rcb= new ResCustomBeanHK();
		rcb.setCode("AE");
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,flag from zl_req_patInfo where exam_info_id='"
					+ exam_id + "'";
			 TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb1 + "\r\n");
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				rcb.setCode("AA");
				rcb.setPersionid(rs1.getString("zl_pat_id"));
			}
			rs1.close();
        }catch(Exception ex){
			
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rcb;
	}
	
	/**
	 * 
	 * @param xmlmessage
	 * @return
	 */
	private void insert_search(long examid,String exam_num,String persion,String logname){
		long exam_id=examid;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			
			String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,zl_djh,flag) values('" + exam_id + "','" + persion
					+ "','" + exam_num + "','" + persion + "','" + persion
					+ "','"+persion+"','0')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql + "\r\n");
			tjtmpconnect.createStatement().executeUpdate(insertsql);
        }catch(Exception ex){
			
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param exam_num
	 * @return
	 */
	public long examIdForExamNum(String exam_num,String logname){
		long exam_id=0;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb = "select id from exam_info where exam_num='" + exam_num + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + sb + "\r\n");
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb);
			if (rs.next()) {
				exam_id = rs.getLong("id");
			}
			rs.close();
		}catch(Exception ex){
			
		}finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return exam_id;
	}
	
}
