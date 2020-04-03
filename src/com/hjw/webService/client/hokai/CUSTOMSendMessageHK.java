package com.hjw.webService.client.hokai;

import com.hjw.util.DateTimeUtil;

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
import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.synjones.framework.persistence.JdbcQueryManager;

public class CUSTOMSendMessageHK {
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
	
	public CUSTOMSendMessageHK(Custom custom){
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
	public ResultBody getMessage(String url,String configvalue,String logname) {
		if(StringUtils.isEmpty(this.custom.getID_NO())) {
			this.custom.setID_NO(this.custom.getEXAM_NUM());
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		long examid=examIdForExamNum(this.custom.getEXAM_NUM(),logname);
		ResultBody rb= new ResultBody();
		if(examid<=0){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("无效体检编号");
		}else{
			try {				
				ResCustomBeanHK rcb= new ResCustomBeanHK();
				rcb=this.searchPatid(configvalue, logname);
				if("AA".equals(rcb.getCode())){
						setSearchString(url,"active",logname);
						insert_search(examid,custom.getEXAM_NUM(),rcb.getPersionid(),logname);
						//插入表
						CustomResBean cus= new CustomResBean();
						cus.setCLINIC_NO(rcb.getPersionid());
						cus.setPATIENT_ID(rcb.getPersionid());
						cus.setVISIT_NO(rcb.getPersionid());
						cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
						List<CustomResBean> LIST=new ArrayList<CustomResBean>();
						LIST.add(cus);
						ControlActProcess ControlActProcess=new ControlActProcess();
						ControlActProcess.setLIST(LIST);
					    rb.setControlActProcess(ControlActProcess);
					    rb.getResultHeader().setTypeCode("AA");
					    rb.getResultHeader().setText(rcb.getCodetext());
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
		sb0.append("	<id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/> ");
		sb0.append("	<!-- 消息创建时间 -->");
		sb0.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
		sb0.append("	<!-- 交互ID(HL7交互类型代码系统) 用来区分消息类型，S0001代表患者注册请求-->");
		sb0.append("	<interactionId extension=\"S0068\" root=\"2.16.840.1.113883.1.6\"/>  ");
		sb0.append("	<!-- 消息发送系统的状态: P(Production); D(Debugging); T(Training) -->  ");
		sb0.append("	<processingCode code=\"P\"/> ");
		sb0.append("	<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) --> ");
		sb0.append("	<processingModeCode code=\"R\"/> ");
		sb0.append("	<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->  ");
		sb0.append("	<acceptAckCode/> ");
		sb0.append("	<!-- 接受者 -->  ");
		sb0.append("	<receiver typeCode=\"RCV\">  ");
		sb0.append("		<!-- 接收设备/应用 --> ");
		sb0.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\"> ");
		sb0.append("			<!-- 接收者ID(接收设备/应用ID) --> ");
		sb0.append("			<!-- item root=\"接收者ID\" extension=\"接收机构\" -->   ");
		sb0.append("			<id> ");
		sb0.append("				<item extension=\"SYS001\" root=\"1\"/>");
		sb0.append("			</id>");
		sb0.append("		</device>");
		sb0.append("	</receiver>");
		sb0.append("	<!-- 发送者 -->  ");
		sb0.append("	<sender typeCode=\"SND\">");
		sb0.append("		<!-- 发送设备/应用 --> ");
		sb0.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\"> ");
		sb0.append("			<!-- 发送者ID(发送设备/应用ID) --> ");
		sb0.append("			<!-- item root=\"发送者ID\" extension=\"发送机构\" -->   ");
		sb0.append("			<id> ");
		sb0.append("				<item extension=\"SYS009\" root=\"2\"/>");
		sb0.append("			</id>");
		sb0.append("		</device>");
		sb0.append("	</sender>");
		sb0.append("	<!-- 封装的消息内容 - Trigger Event Control Act wrapper -->  ");
		sb0.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">  ");
		sb0.append("		<subject typeCode=\"SUBJ\">  ");
		sb0.append("			<registrationRequest classCode=\"REG\" moodCode=\"RQO\"> ");
		sb0.append("				<statusCode code=\"active\"/>");
		sb0.append("				<subject1 typeCode=\"SBJ\">  ");
		sb0.append("					<patient classCode=\"PAT\">  ");
		sb0.append("						<!-- 注册，更新时为active，作废时为disable  -->");
		sb0.append("						<statusCode code=\""+statusCode+"\"/>  ");
		sb0.append("						<!-- 必须项已使用 -->  ");
		sb0.append("						<!-- 各种标识符 患者在医院中的ID(1..*)-->");
		sb0.append("						<id>   ");
		sb0.append("							<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.1\"/>");//患者EMPI标识
		sb0.append("							<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.13\"/>");//住院号
		sb0.append("							<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.12\"/>");//就诊卡号
		sb0.append("							<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.11\"/>");//医保号
		sb0.append("							<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.2\"/>");//域ID(门诊、住院)
		sb0.append("							<item extension=\""+rb1.getPersionid()+"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.3\"/>");//患者ID
		sb0.append("						</id>  ");
		sb0.append("						<!-- 患者有效期(0..1) -->");
		sb0.append("						<effectiveTime>20111101</effectiveTime>  ");
		sb0.append("						<!-- 优先级(0..1) -->  ");
		sb0.append("						<priorityNumber>1</priorityNumber> ");
		sb0.append("						<!-- 重要程度，比如老干部，VIP等，优先级可以根据重要程度进行设置(0..1) -->   ");
		sb0.append("						<veryImportantPersonCode>VIP</veryImportantPersonCode> ");
		sb0.append("						<!-- 患者基本信息(1..1) -->  ");
		sb0.append("						<patientPerson>");
		sb0.append("							<!-- 身份证号及各种证件号(身份证号必须有)(1..*)");
		sb0.append("							@extension: 证件号码 ");
		sb0.append("							@root: 证件OID ");
		sb0.append("							@controlInformationExtension: 证件名称 --> ");
		sb0.append("							<id>   ");
		sb0.append("								<item extension=\""+custom.getID_NO()+"\" root=\"2.16.156.10011.1.1.2.2.1.9\" controlInformationExtension=\"居民身份证\"/>");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.14\" controlInformationExtension=\"居民户口簿\"/>   ");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.15\" controlInformationExtension=\"护照\"/> ");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.16\" controlInformationExtension=\"军官证\"/> ");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.17\" controlInformationExtension=\"驾驶证\"/> ");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.18\" controlInformationExtension=\"港澳居民来往内地通行证\"/> ");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.19\" controlInformationExtension=\"台湾居民来往内地通行证\"/> ");
		sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.20\" controlInformationExtension=\"其他法定有效证件\"/> ");
		sb0.append("							</id>  ");
		sb0.append("							<!--姓名 (1..1)--> ");
		sb0.append("							<patientName xsi:type=\"DSET_EN\">");
		sb0.append("								<item> ");
		sb0.append("									<part value=\""+custom.getNAME()+"\"/> ");
		sb0.append("								</item>");
		sb0.append("							</patientName>");
		sb0.append("							<!-- 家庭电话，电子邮件等联系方式");
		sb0.append("							@use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->   ");
		sb0.append("							<!-- 患者电话或电子邮件(1..*) -->");
		sb0.append("							<telecom use=\"H\" value=\""+custom.NEXT_OF_KIN_PHONE+"\"/>");
		sb0.append("							<telecom use=\"PUB\" value=\""+custom.getPHONE_NUMBER_BUSINESS()+"\"/>");
		sb0.append("							<telecom use=\"EMA\" value=\""+custom.getE_NAME()+"\"/>   ");
		sb0.append("							<!--性别(1..1)-->");
		String sexcode="1";
		if("男".endsWith(custom.getSEX())){
			sexcode="1";
		}else if("女".endsWith(custom.getSEX())){
			sexcode="2";
		}
		sb0.append("							<administrativeGenderCode code=\""+sexcode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\" codeSystemName=\"性别\" displayName=\""+custom.getSEX()+"\"/>");
		sb0.append("							<!--婚姻状况(0..1)-->");
		sb0.append("							<maritalStatusCode code=\"\" codeSystem=\"2.16.156.10011.2.3.3.5\" codeSystemName=\"婚姻状况\" displayName=\""+custom.getMARITAL_STATUS()+"\"/>  ");
		sb0.append("							<!-- 文化程度代码(0..1) -->  ");
		sb0.append("							<educationLevelCode code=\"\" codeSystem=\"1.2.156.112635.1.1.8\" codeSystemName=\"文化程度\" displayName=\""+custom.getDEGREE()+"\"/>   ");
		sb0.append("							<!--民族代码(1..*) -->   ");
		sb0.append("							<ethnicGroupCode code=\"\" codeSystem=\"1.2.156.112635.1.1.5\" codeSystemName=\"民族\" displayName=\""+remark.getNATION()+"\"/>   ");
		sb0.append("							<!--出生日期(1..1)-->");
		sb0.append("							<birthTime value=\""+custom.getDATE_OF_BIRTH().toString().replaceAll("-", "")+"\"/>");
		sb0.append("							<!-- 出生地(0..1)  -->   ");
		sb0.append("							<birthPlace classCode=\"BIRTHPL\"> ");
		sb0.append("								<!-- 出生地(0..1)  -->   ");
		sb0.append("								<birthAddr use=\"H\"> ");
		sb0.append("									<!-- value=\"出生地\" -->");
		sb0.append("									<part type=\"SAL\" value=\""+custom.getBIRTH_PLACE()+"\"/>");
		sb0.append("									<!--邮政编码 --> ");
		sb0.append("									<part type=\"ZIP\" value=\""+custom.getZIP_CODE()+"\"/>");
		sb0.append("								</birthAddr>");
		sb0.append("							</birthPlace>  ");
		sb0.append("							<!--户籍地址 @use: 地址类别，比如H代表家庭住址，TMP代表现住址，PUB代表户籍地址，WP代表工作地址 (1..*)-->;  ");
		sb0.append("							<houseAddr use=\"PUB\">   ");
		sb0.append("								<item> ");
		sb0.append("									<!--非结构化地址（完整地址描述） -->   ");
		sb0.append("									<part type=\"SAL\" value=\""+custom.getBIRTH_PLACE()+"\"/>");
		sb0.append("									<!--地址-省（自治区、直辖市） -->");
		sb0.append("									<part type=\"STA\" value=\"\"/>");
		sb0.append("									<!--地址-市（地区） -->");
		sb0.append("									<part type=\"CTY\" value=\"\"/>");
		sb0.append("									<!--地址-县（区） -->");
		sb0.append("									<part type=\"CNT\" value=\"\"/>");
		sb0.append("									<!-- 地址-乡（镇、街道办事处） --> ");
		sb0.append("									<part type=\"STB\" value=\"\"/>");
		sb0.append("									<!-- 地址-村（街、路、弄等） --> ");
		sb0.append("									<part type=\"STR\" value=\"\"/>");
		sb0.append("									<!-- 地址-门牌号码 --> ");
		sb0.append("									<part type=\"BNR\" value=\"\"/> ");
		sb0.append("									<!-- 邮政编码--> ");
		sb0.append("									<part type=\"ZIP\" value=\""+custom.getZIP_CODE()+"\"/>");
		sb0.append("								</item>");
		sb0.append("							</houseAddr>");
		sb0.append("							<!--职业类别代码(0..*)-->");
		sb0.append("							<asEmployee classCode=\"EMP\"> ");
		sb0.append("								<!--职业编码系统名称/职业代码(0..*)--> ");
		sb0.append("								<occupationCode code=\"\" codeSystem=\"2.16.156.10011.2.3.3.7\" codeSystemName=\"职业代码\" displayName=\"\" />");
		sb0.append("								<employerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		sb0.append("									<!--机构识别号(0..*)-->");
		sb0.append("									<id>1289348683278175</id>");
		sb0.append("									<!--工作单位名称(0..*)-->");
		sb0.append("									<workName xsi:type=\"LIST_EN\">");
		sb0.append("										<item> ");
		sb0.append("											<part value=\"\"/> ");
		sb0.append("										</item>");
		sb0.append("									</workName>");
		sb0.append("									<!-- 工作单位联系方式(0..*) -->  ");
		sb0.append("									<telecom use=\"WP\" value=\"\"/> ");
		sb0.append("									<!-- 工作单位地址(0..*) -->  ");
		sb0.append("									<workAddr use=\"WP\">  ");
		sb0.append("										<item> ");
		sb0.append("											<!--非结构化地址（完整地址描述） --> ");
		sb0.append("											<part type=\"SAL\" value=\"\"/>  ");
		sb0.append("											<!--地址-省（自治区、直辖市） -->  ");
		sb0.append("											<part type=\"STA\" value=\"\"/>  ");
		sb0.append("											<!--地址-市（地区） -->  ");
		sb0.append("											<part type=\"CTY\" value=\"\"/>  ");
		sb0.append("											<!--地址-县（区） -->");
		sb0.append("											<part type=\"CNT\" value=\"\"/>  ");
		sb0.append("											<!-- 地址-乡（镇、街道办事处） --> ");
		sb0.append("											<part type=\"STB\" value=\"\"/>  ");
		sb0.append("											<!-- 地址-村（街、路、弄等） -->   ");
		sb0.append("											<part type=\"STR\" value=\"\"/>");
		sb0.append("											<!-- 地址-门牌号码 -->   ");
		sb0.append("											<part type=\"BNR\" value=\"\"/>   ");
		sb0.append("											<!-- 邮政编码--> ");
		sb0.append("											<part type=\"ZIP\" value=\"\"/>  ");
		sb0.append("										</item>");
		sb0.append("									</workAddr>");
		sb0.append("								</employerOrganization>  ");
		sb0.append("							</asEmployee>  ");
		sb0.append("							<!--公民身份(1..*) -->   ");
		sb0.append("							<asCitizen classCode=\"CIT\">  ");
		sb0.append("								<!--所属国家 必须项已使用--> ");
		sb0.append("								<politicalNational classCode=\"NAT\" determinerCode=\"INSTANCE\">");
		sb0.append("									<code code=\"\" codeSystem=\"1.2.156.112635.1.1.6\" codeSystemName=\"国籍\" displayName=\"\"/>   ");
		sb0.append("								</politicalNational> ");
		sb0.append("							</asCitizen>   ");
		sb0.append("							<!--联系人(0..*)-->");
		sb0.append("							<personalRelationship>   ");
		sb0.append("								<!-- 与患者关系 -->  ");
		sb0.append("								<code code=\"01\" displayName=\"\"/> ");
		sb0.append("								<!--联系人基本信息(0..1)-->");
		sb0.append("								<contactPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">  ");
		sb0.append("									<!-- 联系人姓名(0..*) -->");
		sb0.append("									<connectName xsi:type=\"LIST_EN\">");
		sb0.append("										<item> ");
		sb0.append("											<part value=\"\"/> ");
		sb0.append("										</item>");
		sb0.append("									</connectName>");
		sb0.append("									<!-- 联系人电话或电子邮件(0..*) -->");
		sb0.append("									<telecom use=\"H\" value=\"\"/>  ");
		sb0.append("									<!-- 联系人地址(0..*) -->");
		sb0.append("									<connectAddr use=\"WP\">  ");
		sb0.append("										<item> ");
		sb0.append("											<!--非结构化地址（完整地址描述） --> ");
		sb0.append("											<part type=\"SAL\" value=\"\"/>  ");
		sb0.append("											<!--地址-省（自治区、直辖市） -->  ");
		sb0.append("											<part type=\"STA\" value=\"\"/>  ");
		sb0.append("											<!--地址-市（地区） -->  ");
		sb0.append("											<part type=\"CTY\" value=\"\"/>  ");
		sb0.append("											<!--地址-县（区） -->");
		sb0.append("											<part type=\"CNT\" value=\"\"/>  ");
		sb0.append("											<!-- 地址-乡（镇、街道办事处） --> ");
		sb0.append("											<part type=\"STB\" value=\"\"/>  ");
		sb0.append("											<!-- 地址-村（街、路、弄等） -->   ");
		sb0.append("											<part type=\"STR\" value=\"\"/>");
		sb0.append("											<!-- 地址-门牌号码 -->   ");
		sb0.append("											<part type=\"BNR\" value=\"\"/>   ");
		sb0.append("											<!-- 邮政编码--> ");
		sb0.append("											<part type=\"ZIP\" value=\"\"/>  ");
		sb0.append("										</item>");
		sb0.append("									</connectAddr>");
		sb0.append("								</contactPerson> ");
		sb0.append("							</personalRelationship>  ");
		sb0.append("						</patientPerson>   ");
		sb0.append("						<!-- 建卡机构、医疗机构信息(1..1)  -->   ");
		sb0.append("						<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">   ");
		sb0.append("							<!--医疗机构编码-->");
		sb0.append("							<id extension=\"46600083-8\" root=\"2.16.156.10011.1.5\"/> ");
		sb0.append("							<!--医疗机构名称 --> ");
		sb0.append("							<buildName xsi:type=\"LIST_EN\">");
		sb0.append("								<item> ");
		sb0.append("									<part value=\"\"/>   ");
		sb0.append("								</item>");
		sb0.append("							</buildName>");
		sb0.append("							<contactParty classCode=\"CON\"> ");
		sb0.append("								<!-- 联系人电话或电子邮件 -->");
		sb0.append("								<telecom use=\"H\" value=\"\"/>  ");
		sb0.append("								<!-- 联系人地址 -->  ");
		sb0.append("								<buildAddr use=\"WP\">");
		sb0.append("									<item> ");
		sb0.append("										<!--非结构化地址（完整地址描述） -->   ");
		sb0.append("										<part type=\"SAL\" value=\"\"/>");
		sb0.append("										<!--地址-省（自治区、直辖市） -->  ");
		sb0.append("										<part type=\"STA\" value=\"\"/>  ");
		sb0.append("										<!--地址-市（地区） -->  ");
		sb0.append("										<part type=\"CTY\" value=\"\"/>  ");
		sb0.append("										<!--地址-县（区） -->");
		sb0.append("										<part type=\"CNT\" value=\"\"/>  ");
		sb0.append("										<!-- 地址-乡（镇、街道办事处） --> ");
		sb0.append("										<part type=\"STB\" value=\"\"/>  ");
		sb0.append("										<!-- 地址-村（街、路、弄等） -->   ");
		sb0.append("										<part type=\"STR\" value=\"\"/>");
		sb0.append("										<!-- 地址-门牌号码 -->   ");
		sb0.append("										<part type=\"BNR\" value=\"\"/>   ");
		sb0.append("										<!-- 邮政编码--> ");
		sb0.append("										<part type=\"ZIP\" value=\"\"/>  ");
		sb0.append("									</item>");
		sb0.append("								</buildAddr>");
		sb0.append("							</contactParty>");
		sb0.append("						</providerOrganization>");
		sb0.append("						<!--医疗保险信息(1..*)-->");
		sb0.append("						<coveredPartyOf typeCode=\"COV\">  ");
		sb0.append("							<coverageRecord classCode=\"COV\" moodCode=\"EVN\">");
		sb0.append("								<!-- 保险生效日期(0..1) -->");
		sb0.append("								<effectiveTime>20110101</effectiveTime>");
		sb0.append("								<!--受益人(1..*)-->  ");
		sb0.append("								<beneficiary typeCode=\"BEN\"> ");
		sb0.append("									<!-- 优先顺序(0..1) -->");
		sb0.append("									<sequenceNumber/>");
		sb0.append("									<!--受益人角色信息(1..1)-->  ");
		sb0.append("									<beneficiaryRole classCode=\"MBR\">");
		sb0.append("										<!-- 医保、商保号(0..1) -->  ");
		sb0.append("										<id>124322452435</id>");
		sb0.append("										<!-- 医保，商保类型编码(1..1) -->  ");
		sb0.append("										<code code=\"\" displayName=\"\"/> ");
		sb0.append("										<!--保障机构(0..1)-->");
		sb0.append("										<ensuranceEntity classCode=\"ORG\" determinerCode=\"INSTANCE\">  ");
		sb0.append("											<!--保险机构编号(0..*)-->");
		sb0.append("											<id>123414</id>");
		sb0.append("											<!--保险机构名称(0..*)-->");
		sb0.append("											<name xsi:type=\"LIST_EN\">");
		sb0.append("												<item>   ");
		sb0.append("													<part value=\"\"/>  ");
		sb0.append("												</item>  ");
		sb0.append("											</name>  ");
		sb0.append("											<!-- 保险机构联系方式(0..*) -->");
		sb0.append("											<telecom use=\"H\" value=\"\"/>  ");
		sb0.append("											<!-- 保险机构地址(0..*) -->");
		sb0.append("											<addr use=\"WP\"/> ");
		sb0.append("										</ensuranceEntity> ");
		sb0.append("									</beneficiaryRole>   ");
		sb0.append("								</beneficiary> ");
		sb0.append("							</coverageRecord>  ");
		sb0.append("						</coveredPartyOf>  ");
		sb0.append("						<!-- 描述和患者相关的一些Observation信息，比如血型，过敏信息，身高，体重，年龄等 (0..*) -->");
		sb0.append("						<subjectOf typeCode=\"OBS\">   ");
		sb0.append("							<!-- Observation详细信息(1..1) -->   ");
		sb0.append("							<administrationObservation classCode=\"OBS\" moodCode=\"EVN\"> ");
		sb0.append("								<!--该次obs的唯一，可以为空(0..1)--> ");
		sb0.append("								<id>23123123</id>");
		sb0.append("								<!--observation类型编码(1..1)--> ");
		sb0.append("								<code code=\"01\" displayName=\"血型\"/> ");
		sb0.append("								<!--该Observation的详细描述(0..1)--> ");
		sb0.append("								<text/>");
		sb0.append("								<!--observation的值(0..1)--> ");
		sb0.append("								<value code=\"\" displayName=\"\"/> ");
		sb0.append("							</administrationObservation> ");
		sb0.append("						</subjectOf> ");
		sb0.append("					</patient> ");
		sb0.append("				</subject1>");
		sb0.append("				<!--操作者信息(0..1)-->");
		sb0.append("				<author typeCode=\"AUT\">");
		sb0.append("					<assignedEntity classCode=\"ASSIGNED\">");
		sb0.append("						<id extension=\"admin\" identifierName=\"admin\" root=\"1.2.156.112635.1.1.2\"/>  ");
		sb0.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
		sb0.append("							<!--工号-->");
		sb0.append("							<id>234234</id>");
		sb0.append("							<name xsi:type=\"LIST_EN\">");
		sb0.append("								<item> ");
		sb0.append("									<part value=\"admin\"/>   ");
		sb0.append("								</item>");
		sb0.append("							</name>");
		sb0.append("						</assignedPerson>  ");
		sb0.append("					</assignedEntity>");
		sb0.append("				</author>");
		sb0.append("			</registrationRequest> ");
		sb0.append("		</subject>   ");
		sb0.append("	</controlActProcess> ");
		sb0.append("</PRPA_IN201311UV02>"); 
		TranLogTxt.liswriteEror_to_txt(logname,"request:"+sb0.toString());
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
		sb0.append("<PRPA_IN201305UV02 xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\">");  
		sb0.append("  <id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/> "); 
		sb0.append("  <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/> "); 
		sb0.append("  <interactionId root=\"2.16.840.1.113883.1.6\" extension=\"S0004\"/>"); 
		sb0.append("  <processingCode code=\"P\"/> "); 
		sb0.append("  <processingModeCode/>  "); 
		sb0.append("  <acceptAckCode code=\"AL\"/> "); 
		sb0.append("  <receiver typeCode=\"RCV\">  "); 
		sb0.append("    <telecom/> "); 
		sb0.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\"> "); 
		sb0.append("      <id> "); 
		sb0.append("        <item root=\"2.16.156.10011.0.1.1\" extension=\"SYS001\"/>   "); 
		sb0.append("      </id>"); 
		sb0.append("    </device>  "); 
		sb0.append("  </receiver>  "); 
		sb0.append("  <sender typeCode=\"SND\">"); 
		sb0.append("    <telecom/> "); 
		sb0.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\"> "); 
		sb0.append("      <id> "); 
		sb0.append("        <item root=\"2.16.156.10011.0.1.2\" extension=\"SYS009\"/>   "); 
		sb0.append("      </id>"); 
		sb0.append("    </device>  "); 
		sb0.append("  </sender>"); 
		sb0.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">"); 
		sb0.append("    <code code=\"SYS009\" codeSystem=\"2.16.840.1.113883.1.6\"/>  "); 
		sb0.append("    <queryByParameter>   "); 
		sb0.append("      <queryId root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>  "); 
		sb0.append("      <statusCode code=\"new\"/> "); 
		sb0.append("      <parameterList>"); 
		sb0.append("        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->"); 
		sb0.append("        <code code=\"03\" codeSystem=\"2.16.156.10011.2.3.1.271\"> "); 
		sb0.append("          <!-- 就诊类别名称(1..1) -->"); 
		sb0.append("          <displayName value=\"体检\"/>"); 
		sb0.append("        </code>"); 
		sb0.append("        <!--查询类型 患者id=PATIENTID,身份证号码=NAMEICCID,门诊号=EMERGENCY，住院号=ADNO , "); 
		sb0.append("        医保号, 社保号, 居民健康卡号 , 驾驶证, 军官证, 港澳台居住证 就诊卡 -->   "); 
		sb0.append("        <query code=\"NAMEICCID\"   value=\""+this.custom.getID_NO()+"\" />  "); 
		/*sb0.append("        <id>   "); 
		sb0.append("          <!--PatientID (1..1) -->   "); 
		sb0.append("          <patientId extension=\"\" root=\"1.2.156.112635.1.2.1.3\"/>  "); 
		sb0.append("          <!--住院号标识 (0..1)-->   "); 
		sb0.append("          <adId extension=\"\" root=\"2.16.156.10011.1.12\"/>"); 
		sb0.append("          <!--门急诊号标识 (0..1)--> "); 
		sb0.append("          <emergencyId extension=\"\" root=\"2.16.156.10011.1.11\"/>   "); 
		sb0.append("        </id>  "); 
		sb0.append("        <!--患者性别(0..1)-->"); 
		String sexcode="1";
		if("男".endsWith(custom.getSEX())){
			sexcode="1";
		}else if("女".endsWith(custom.getSEX())){
			sexcode="2";
		}
		sb0.append("        <livingSubjectAdministrativeGender>"); 
		sb0.append("          <value code=\""+sexcode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\"> "); 
		sb0.append("            <displayName value=\""+custom.getSEX()+"性\"/>  "); 
		sb0.append("          </value> "); 
		sb0.append("          <semanticsText value=\"患者性别\"/>"); 
		sb0.append("        </livingSubjectAdministrativeGender>   "); 
		sb0.append("        <!--患者身份证号(1..1)-->"); 
		sb0.append("        <livingSubjectId>"); 
		sb0.append("          <value>  "); 
		sb0.append("            <item root=\"2.16.156.10011.1.1.2.2.1.9\" extension=\""+this.custom.getID_NO()+"\"/> "); 
		sb0.append("          </value> "); 
		sb0.append("          <semanticsText value=\"患者身份证号\"/>"); 
		sb0.append("        </livingSubjectId>   "); 
		sb0.append("        <!--患者姓名(1..1)-->"); 
		sb0.append("        <livingSubjectName>  "); 
		sb0.append("          <value>  "); 
		sb0.append("            <item> "); 
		sb0.append("              <part value=\""+this.custom.getNAME()+"\"/> "); 
		sb0.append("            </item>"); 
		sb0.append("          </value> "); 
		sb0.append("          <semanticsText value=\"患者姓名\"/>"); 
		sb0.append("        </livingSubjectName> "); 
		*/
		sb0.append("      </parameterList>   "); 
		sb0.append("    </queryByParameter>  "); 
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
					rb1.setCode(document.selectSingleNode("abc:PRPA_IN201306UV02/abc:acknowledgement/@typeCode").getText());// 获取根节点;
					rb1.setPersionid(document.selectSingleNode("abc:PRPA_IN201306UV02/abc:controlActProcess/abc:subject/abc:registrationRequest/abc:subject1/abc:patient/abc:patientId/@value").getText());
					}catch(Exception ex){
						try{
							InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
							Map<String, String> xmlMap = new HashMap<>();
							xmlMap.put("abc", "urn:hl7-org:v3");
							SAXReader sax = new SAXReader();
							sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
							Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
							rb1.setCode(document.selectSingleNode("abc:PRPA_IN201304UV02/abc:acknowledgement/@typeCode").getText());// 获取根节点;
							rb1.setCodetext(document.selectSingleNode("abc:PRPA_IN201304UV02/abc:acknowledgement/abc:acknowledgementDetail/abc:description/@value").getText());
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
