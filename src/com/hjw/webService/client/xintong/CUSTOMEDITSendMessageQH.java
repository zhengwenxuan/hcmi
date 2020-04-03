package com.hjw.webService.client.xintong;

import java.rmi.RemoteException;
import java.util.UUID;

import javax.xml.rpc.ServiceException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class CUSTOMEDITSendMessageQH {
	
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private Custom custom=new Custom();
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public CUSTOMEDITSendMessageQH(Custom custom){
		this.custom=custom;
	}
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url,String logname) {
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		FeeResultBody rb = new FeeResultBody();
		//发送请求信息
		HIPMessageServiceService damQuery = new HIPMessageServiceServiceLocator(url);
		IHIPMessageService damQuerys;
		
		String gettokens = Gettoken.Gettokens(url,logname);
		TranLogTxt.liswriteEror_to_txt(logname, "统一登录的tokens====" + gettokens);
		if(gettokens.equals("AE")){
			
			TranLogTxt.liswriteEror_to_txt(logname, "统一登录失败===");
			rb.getResultHeader().setTypeCode("AE");
			return rb;
		}
		
		
		StringBuffer sbgettokens = new  StringBuffer();

		sbgettokens.append(" <REQUEST>                                                             ");
		sbgettokens.append(" <TOKENID>"+gettokens+"</TOKENID>                                                     ");
		sbgettokens.append(" <!--消息发送方在平台上的注册标识-->                                   ");
		sbgettokens.append(" <SENDER>HJW</SENDER>                                                     ");
		sbgettokens.append(" <!--请求消息ID,由消息发送方生成单次请求唯一的标识，建议用uuid 32位--> ");
		sbgettokens.append(" <REQ_MSGID>"+UUID.randomUUID().toString().toLowerCase()+"</REQ_MSGID>   ");
		sbgettokens.append(" 	<REQ_PARAMS>                                                         ");
		sbgettokens.append(" <ACCESS_TOKEN>"+gettokens+"</ACCESS_TOKEN>                                         ");
		sbgettokens.append(" </REQ_PARAMS>                                                         ");
		sbgettokens.append(" </REQUEST>                                                            ");
		
		try {
			damQuerys = damQuery.getHIPMessageServicePort();
			
			String queryMsg = queryUserRegister(url, logname);
			
			TranLogTxt.liswriteEror_to_txt(logname, "==拼接查询 queryMsg 人员信息XML===" + queryMsg);
			//查询人员信息
			String resultQuery = damQuerys.HIPMessageServer2016Ext("XT-PatientRegistryFindCandidatesQuery", queryMsg,sbgettokens.toString());
			//解析消xml结果
			TranLogTxt.liswriteEror_to_txt(logname, "==解析resultQuery返回结果==" + QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","typeCode")+"==="+QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","text"));
			
			if ((resultQuery != null) && (resultQuery.trim().length() > 0)) {
				
				TranLogTxt.liswriteEror_to_txt(logname, "==解结果比较==" + "AA".equals(QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","typeCode")));
				
				if("AA".equals(QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","typeCode"))){
					
					TranLogTxt.liswriteEror_to_txt(logname, "==更新个人信息==");
					
					String updateMsg = updatePersonal(url, logname);
					
					TranLogTxt.liswriteEror_to_txt(logname, "==拼接 更新 updateMsg 人员信息 XML===" + updateMsg);
					
					HIPMessageServiceService damUpdate = new HIPMessageServiceServiceLocator(url);
					
					IHIPMessageService damUpdates = damUpdate.getHIPMessageServicePort();
					//更新个人信息 
					//String resultUpdate = damUpdates.HIPMessageServer("PatientRegistryReviseRequest", updateMsg);
					
					String resultUpdate = damQuerys.HIPMessageServer2016Ext("XT-PatientRegistryReviseRequest", updateMsg,sbgettokens.toString());
					TranLogTxt.liswriteEror_to_txt(logname, "==解析resultUpdate返回结果==" + QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","typeCode")+"==="+QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","text"));
					
					if((resultUpdate != null) && (resultUpdate.trim().length() > 0)){
						if("AA".equals(QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201315UV02","typeCode"))){
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","text"));
						}else{
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","text"));
						}
						
					}
					
				}else{ //注册个人信息
					
					TranLogTxt.liswriteEror_to_txt(logname, "==注册个人信息==");
					
					String regMsg = registerPersonal(url, logname);
					
					TranLogTxt.liswriteEror_to_txt(logname, "==拼接 注册 regMsg 人员信息 XML===" + regMsg);
					
					HIPMessageServiceService damAdd = new HIPMessageServiceServiceLocator(url);
					IHIPMessageService damAdds = damAdd.getHIPMessageServicePort();
					
					//String resultAdd = damAdds.HIPMessageServer("PatientRegistryAddRequest", regMsg);
					String resultAdd = damQuerys.HIPMessageServer2016Ext("XT-PatientRegistryAddRequest", regMsg,sbgettokens.toString());
					TranLogTxt.liswriteEror_to_txt(logname, "==解析resultAdd返回结果==" + QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201315UV02","typeCode")+"==="+QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201315UV02","text"));
					
					if((resultAdd != null) && (resultAdd.trim().length() > 0)){
						if("AA".equals(QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201312UV02","typeCode"))){
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201315UV02","text"));
						}else{
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201315UV02","text"));
						}
						
					}
				}
			}else{
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("查询接口返回错误");
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return rb;
	}
	
	
	/**
	 * 查询平台系统是否此人已经注册
	 * @param url
	 * @param logname
	 * @return
	 */
	private String queryUserRegister(String url , String logname){
		
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("	<PRPA_IN201305UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201305UV02.xsd\"> ");
		bufferXml.append("		<id root=\"2.16.156.10011.0\" extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>                 ");
		bufferXml.append("		<creationTime value=\"20070803130624\"/>                                                         ");
		bufferXml.append("		<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201305UV02\"/>                    ");
		bufferXml.append("		<processingCode code=\"P\"/>                                                                     ");
		bufferXml.append("		<processingModeCode code=\"R\"/>                                                                 ");
		bufferXml.append("		<acceptAckCode code=\"AL\"/>                                                                     ");
		bufferXml.append("		<receiver typeCode=\"RCV\">                                                                      ");
		bufferXml.append("			<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                         ");
		bufferXml.append("				<id root=\"2.16.156.10011.0.1.1\" extension=\"2.16.156.10011.0.1.1\"/>                     ");
		bufferXml.append("			</device>                                                                                  ");
		bufferXml.append("		</receiver>                                                                                    ");
		bufferXml.append("		<sender typeCode=\"SND\">                                                                        ");
		bufferXml.append("			<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                         ");
		bufferXml.append("				<id root=\"2.16.156.10011.0.1.2\" extension=\"2.16.156.10011.0.1.2\"/>                     ");
		bufferXml.append("			</device>                                                                                  ");
		bufferXml.append("		</sender>                                                                                      ");
		bufferXml.append("		<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                            ");
		bufferXml.append("			<code code=\"PRPA_TE201305UV02\" codeSystem=\"2.16.840.1.113883.1.6\"/>                        ");
		bufferXml.append("			<queryByParameter>                                                                         ");
		bufferXml.append("				<queryId root=\"2.16.156.10011.0\" extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>    ");
		bufferXml.append("				<statusCode code=\"new\"/>                                                               ");
		bufferXml.append("				<initialQuantity value=\"2\"/>                                                           ");
		bufferXml.append("				<matchCriterionList>                                                                   ");
		bufferXml.append("					<minimumDegreeMatch>                                                               ");
		bufferXml.append("						<value xsi:type=\"INT\" value=\"100\"/>                                            ");
		bufferXml.append("						<semanticsText></semanticsText>                                        ");
		bufferXml.append("					</minimumDegreeMatch>                                                              ");
		bufferXml.append("				</matchCriterionList>                                                                  ");
		bufferXml.append("				<parameterList>                                                                        ");
		bufferXml.append("					<livingSubjectId>                                                                  ");
		bufferXml.append("						<value root=\"2.16.156.10011.0.2.2\" extension=\""+this.custom.getPATIENT_ID()+"\"></value>                 ");
		bufferXml.append("						<semanticsText>"+this.custom.getPATIENT_ID()+"</semanticsText>                                ");
		bufferXml.append("					</livingSubjectId>                                                                 ");
		bufferXml.append("				</parameterList>                                                                       ");
		bufferXml.append("			</queryByParameter>                                                                        ");
		bufferXml.append("		</controlActProcess>                                                                           ");
		bufferXml.append("	</PRPA_IN201305UV02>                                                                               ");
		                                                                                                                     
		return bufferXml.toString();                                                                                                         
		                                                                                                                      
	}   
	
	/**
	 * 个人信息注册
	 * @param url
	 * @param logname
	 * @return
	 */
	private String registerPersonal(String url , String logname) {
		
		StringBuffer bufferXml = new StringBuffer();
		bufferXml.append("	<PRPA_IN201311UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201311UV02.xsd\" xmlns=\"urn:hl7-org:v3\"> ");
		bufferXml.append("	<id root=\"2.16.156.10011.0\" extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/> ");
		bufferXml.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
		bufferXml.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201311UV02\"/> ");
		bufferXml.append("	<processingCode code=\"P\"/> ");
		bufferXml.append("	<processingModeCode code=\"R\"/> ");
		bufferXml.append("	<acceptAckCode code=\"AL\"/> ");
		bufferXml.append("	<receiver typeCode=\"RCV\"> ");
		bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("			<id root=\"2.16.156.10011.0.1.1\" extension=\"HIP\" /> ");
		bufferXml.append("		</device> ");
		bufferXml.append("	</receiver> ");
		bufferXml.append("	<sender typeCode=\"SND\"> ");
		bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("			<id root=\"2.16.156.10011.0.1.2\" extension=\"HJW\" /> ");
		bufferXml.append("		</device> ");
		bufferXml.append("	</sender> ");
		bufferXml.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\"> ");
		bufferXml.append("		<subject typeCode=\"SUBJ\"> ");
		bufferXml.append("			<registrationRequest classCode=\"REG\" moodCode=\"RQO\"> ");
		bufferXml.append("				<statusCode code=\"active\"/> ");
		bufferXml.append("				<subject1 typeCode=\"SBJ\"> ");
		bufferXml.append("					<patient classCode=\"PAT\"> ");
		bufferXml.append("						<!--本地系统的患者ID --> ");
		bufferXml.append("						<id root=\"2.16.156.10011.0.2.2\" extension=\""+this.custom.getPATIENT_ID()+"\"/> ");
		bufferXml.append("						<statusCode code=\"active\"/> ");
		
		bufferXml.append("						<effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
		
		bufferXml.append("						<patientPerson> ");
		bufferXml.append("							<!--身份证号--> ");
		
		if("".equals(this.custom.getID_NO()) || this.custom.getID_NO() == null){
			bufferXml.append("							<id root=\"2.16.156.10011.1.3\"  /> ");
		}else{
			bufferXml.append("							<id root=\"2.16.156.10011.1.3\" extension=\""+this.custom.getID_NO()+"\"/> ");
		}
		bufferXml.append("							<!--姓名--> ");
		bufferXml.append("							<name use=\"L\">"+this.custom.getNAME()+"</name> ");
		bufferXml.append("							<!--联系电话--> ");
		if("".equals(this.custom.getPHONE_NUMBER_HOME()) || this.custom.getPHONE_NUMBER_HOME() == null){
			bufferXml.append("							<telecom value=\"\"  use=\"H\" /> ");
		}else{
			bufferXml.append("							<telecom value=\""+this.custom.getPHONE_NUMBER_HOME()+"\" use=\"H\" /> ");
		}
		
		bufferXml.append("							<!--性别--> ");
		bufferXml.append("							<administrativeGenderCode code=\"1\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""+this.custom.getSEX()+"\"/> ");
		bufferXml.append("							<!--出生时间--> ");
		bufferXml.append("							<birthTime value=\""+this.custom.getDATE_OF_BIRTH().replace("-", "")+"\"/> ");
		bufferXml.append("							<!--联系地址--> ");
		bufferXml.append("							<addr use=\"PUB\"> ");
		bufferXml.append("								<!--非结构化地址（完整地址描述） --> ");
		bufferXml.append("								<streetAddressLine partType=\"SAL\">"+this.custom.getMAILING_ADDRESS()+"</streetAddressLine> ");
		bufferXml.append("								<!--地址-省（自治区、直辖市）   --> ");
		bufferXml.append("								<state language=\"CH\"></state> ");
		bufferXml.append("								<!--地址-市（地区）   --> ");
		bufferXml.append("								<city></city> ");
		bufferXml.append("								<!--地址-县（区）   --> ");
		bufferXml.append("								<county></county> ");
		bufferXml.append("								<!-- 地址-乡（镇、街道办事处）   --> ");
		bufferXml.append("								<streetNameBase></streetNameBase> ");
		bufferXml.append("								<!-- 地址-村（街、路、弄等）   --> ");
		bufferXml.append("								<streetName></streetName> ");
		bufferXml.append("								<!-- 地址-门牌号码 --> ");
		bufferXml.append("								<houseNumber></houseNumber> ");
		bufferXml.append("								<!-- 邮政编码--> ");
		bufferXml.append("								<postalCode>"+this.custom.getZIP_CODE()+"</postalCode> ");
		bufferXml.append("							</addr> ");
		
		bufferXml.append("							<!--婚姻状况--> ");
		if("".equals(this.custom.getMARITAL_STATUS()) || this.custom.getMARITAL_STATUS() == null){
			bufferXml.append("							<maritalStatusCode  codeSystem=\"2.16.156.10011.2.3.3.5\" /> ");
		}else{
			bufferXml.append("							<maritalStatusCode  codeSystem=\"2.16.156.10011.2.3.3.5\"  displayName=\""+this.custom.getMARITAL_STATUS()+"\" /> ");
		}
			
		
		bufferXml.append("							<!--民族--> ");
		if("".equals(this.custom.getNATION()) || this.custom.getNATION() == null){
			bufferXml.append("							<ethnicGroupCode  codeSystem=\"2.16.156.10011.2.3.3.3\" /> ");
		}else{
			bufferXml.append("							<ethnicGroupCode  codeSystem=\"2.16.156.10011.2.3.3.3\"  displayName=\""+this.custom.getNATION()+"\" /> ");
		}
		
		bufferXml.append("							<!--职业类别代码--> ");
		bufferXml.append("							<asEmployee classCode=\"EMP\"> ");
		bufferXml.append("								<occupationCode codeSystem=\"2.16.156.10011.2.3.3.7\" /> ");
		bufferXml.append("								<employerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--工作单位名称--> ");
		bufferXml.append("									<name>"+this.custom.getUNIT_IN_CONTRACT()+"</name> ");
		bufferXml.append("									<contactParty classCode=\"CON\"> ");
		
		bufferXml.append("										<!--工作联系电话--> ");
		if("".equals(this.custom.getPHONE_NUMBER_BUSINESS()) || this.custom.getPHONE_NUMBER_BUSINESS() == null){
			bufferXml.append("										<telecom value=\"\" use=\"WP\"/> ");
		}else{
			bufferXml.append("										<telecom value=\""+this.custom.getPHONE_NUMBER_BUSINESS()+"\" use=\"WP\"/> ");
		}
		bufferXml.append("									</contactParty> ");
		bufferXml.append("								</employerOrganization> ");
		bufferXml.append("							</asEmployee> ");
		
		bufferXml.append("							<asOtherIDs classCode=\"PAT\"> ");
		bufferXml.append("								<!--体检号--> ");
		bufferXml.append("								<id root=\"2.16.156.10011.1.13\" extension=\""+this.custom.getEXAM_NUM()+"\"/> ");
		bufferXml.append("								<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--体检卡发放机构代码--> ");
		bufferXml.append("									<id root=\"2.16.156.10011.1.5\" extension=\"青海省人民医院\"/> ");
		bufferXml.append("								</scopingOrganization> ");
		bufferXml.append("							</asOtherIDs> ");
		
		bufferXml.append("							<asOtherIDs classCode=\"PAT\"> ");
		bufferXml.append("								<!--健康卡号--> ");
		bufferXml.append("								<id root=\"2.16.156.10011.1.19\" extension=\""+this.custom.getCARD_NO()+"\"/> ");
		bufferXml.append("								<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--健康卡发放机构代码--> ");
		bufferXml.append("									<id root=\"2.16.156.10011.1.5\" extension=\"青海省人民医院\"/> ");
		bufferXml.append("								</scopingOrganization> ");
		bufferXml.append("							</asOtherIDs> ");
		bufferXml.append("							<asOtherIDs classCode=\"PAT\"> ");
		bufferXml.append("								<!--城乡居民健康档案编号--> ");
		bufferXml.append("								<id root=\"2.16.156.10011.1.2\"/> ");
		bufferXml.append("								<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--建档医疗机构组织机构代码--> ");
		bufferXml.append("									<id root=\"2.16.156.10011.1.5\" /> ");
		bufferXml.append("								</scopingOrganization> ");
		bufferXml.append("							</asOtherIDs> ");
		bufferXml.append("							<!--联系人--> ");
		bufferXml.append("							<personalRelationship> ");
		bufferXml.append("								<code/> ");
		
		bufferXml.append("								<!--联系人电话--> ");
		if("".equals(this.custom.getNEXT_OF_KIN_PHONE()) || this.custom.getNEXT_OF_KIN_PHONE() == null){
			bufferXml.append("								<telecom use=\"H\" value=\"\"/> ");
		}else{
			bufferXml.append("								<telecom use=\"H\" value=\""+this.custom.getNEXT_OF_KIN_PHONE()+"\"/> ");
			
		}
			
		
		bufferXml.append("								<relationshipHolder1 classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--联系人姓名--> ");
		bufferXml.append("									<name>"+this.custom.getNEXT_OF_KIN()+"</name> ");
		bufferXml.append("								</relationshipHolder1> ");
		bufferXml.append("							</personalRelationship> ");
		bufferXml.append("						</patientPerson> ");
		bufferXml.append("						<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("							<id root=\"2.16.156.10011.1.5\" extension=\"44000115-4\"/> ");
		bufferXml.append("							<name use=\"L\"></name> ");
		bufferXml.append("							<contactParty classCode=\"CON\"/> ");
		bufferXml.append("						</providerOrganization> ");
		bufferXml.append("						<!--医疗保险信息--> ");
		bufferXml.append("						<coveredPartyOf typeCode=\"COV\"> ");
		bufferXml.append("							<coverageRecord classCode=\"COV\" moodCode=\"EVN\"> ");
		bufferXml.append("								<beneficiary typeCode=\"BEN\"> ");
		bufferXml.append("									<beneficiary classCode=\"MBR\"> ");
		bufferXml.append("										<code codeSystem=\"2.16.156.10011.2.3.1.248\" codeSystemName=\"医疗保险类别代码\" /> ");
		bufferXml.append("									</beneficiary> ");
		bufferXml.append("								</beneficiary> ");
		bufferXml.append("							</coverageRecord> ");
		bufferXml.append("						</coveredPartyOf> ");
		bufferXml.append("					</patient> ");
		bufferXml.append("				</subject1> ");
		bufferXml.append("				<author typeCode=\"AUT\"> ");
		bufferXml.append("					<assignedEntity classCode=\"ASSIGNED\"> ");
		// 录入人员ID  固定 
		bufferXml.append("						<id root=\"2.16.156.10011.0.3.2\"  extension=\"\"/> ");
		bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("							<name use=\"L\"></name> ");
		bufferXml.append("						</assignedPerson> ");
		bufferXml.append("					</assignedEntity> ");
		bufferXml.append("				</author> ");
		bufferXml.append("			</registrationRequest> ");
		bufferXml.append("		</subject> ");
		bufferXml.append("	</controlActProcess> ");
		bufferXml.append("</PRPA_IN201311UV02> ");
		return bufferXml.toString();
	}
	
	
	
	/**
	 *  个人基本信息更新
	 * @param url
	 * @param logname
	 * @return
	 */
	private String updatePersonal(String url , String logname) {
		
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("<PRPA_IN201314UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201314UV02.xsd\" xmlns=\"urn:hl7-org:v3\"> ");
		bufferXml.append("	<id root=\"2.16.156.10011.0\" extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/> ");
		bufferXml.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
		bufferXml.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201314UV02\"/> ");
		bufferXml.append("	<processingCode code=\"P\"/> ");
		bufferXml.append("	<processingModeCode code=\"R\"/> ");
		bufferXml.append("	<acceptAckCode code=\"AL\"/> ");
		bufferXml.append("	<receiver typeCode=\"RCV\"> ");
		bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("			<id root=\"2.16.156.10011.0.1.1\" extension=\"HIP\" /> ");
		bufferXml.append("		</device> ");
		bufferXml.append("	</receiver> ");
		bufferXml.append("	<sender typeCode=\"SND\"> ");
		bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("			<id root=\"2.16.156.10011.0.1.2\" extension=\"HJW\" /> ");
		bufferXml.append("		</device> ");
		bufferXml.append("	</sender> ");
		bufferXml.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\"> ");
		bufferXml.append("		<subject typeCode=\"SUBJ\"> ");
		bufferXml.append("			<registrationRequest classCode=\"REG\" moodCode=\"RQO\"> ");
		bufferXml.append("				<statusCode code=\"active\"/> ");
		bufferXml.append("				<subject1 typeCode=\"SBJ\"> ");
		bufferXml.append("					<patient classCode=\"PAT\"> ");
		bufferXml.append("						<!--本地系统的患者ID--> ");
		bufferXml.append("						<id root=\"2.16.156.10011.0.2.2\" extension=\""+this.custom.getPATIENT_ID()+"\"/> ");
		bufferXml.append("						<statusCode code=\"active\"/> ");
		bufferXml.append("						<effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
		bufferXml.append("						<patientPerson> ");
		bufferXml.append("							<!--身份证号--> ");
		
		if("".equals(this.custom.getID_NO()) || this.custom.getID_NO() == null){
			bufferXml.append("							<id root=\"2.16.156.10011.1.3\"  /> ");
		}else{
			bufferXml.append("							<id root=\"2.16.156.10011.1.3\" extension=\""+this.custom.getID_NO()+"\"/> ");
		}
		
		bufferXml.append("							<!--姓名--> ");
		bufferXml.append("							<name use=\"L\">"+this.custom.getNAME()+"</name> ");
		bufferXml.append("							<!--联系电话--> ");
		if("".equals(this.custom.getPHONE_NUMBER_HOME()) || this.custom.getPHONE_NUMBER_HOME() == null){
			bufferXml.append("							<telecom value=\"\" use=\"H\"/> ");
		}else{
			bufferXml.append("							<telecom value=\""+this.custom.getPHONE_NUMBER_HOME()+"\" use=\"H\"/> ");
		}
		bufferXml.append("							<!--性别--> ");
		bufferXml.append("							<administrativeGenderCode code=\"1\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""+this.custom.getSEX()+"\"/> ");
		bufferXml.append("							<!--出生时间--> ");
		bufferXml.append("							<birthTime value=\""+this.custom.getDATE_OF_BIRTH().replace("-", "")+"\"/> ");
		bufferXml.append("							<!--联系地址--> ");
		bufferXml.append("							<addr use=\"PUB\"> ");
		bufferXml.append("								<!--非结构化地址（完整地址描述） --> ");
		bufferXml.append("								<streetAddressLine partType=\"SAL\">"+this.custom.getMAILING_ADDRESS()+"</streetAddressLine> ");
		bufferXml.append("								<!--地址-省（自治区、直辖市）   --> ");
		bufferXml.append("								<state language=\"CH\"></state> ");
		bufferXml.append("								<!--地址-市（地区）   --> ");
		bufferXml.append("								<city></city> ");
		bufferXml.append("								<!--地址-县（区）   --> ");
		bufferXml.append("								<county></county> ");
		bufferXml.append("								<!-- 地址-乡（镇、街道办事处）   --> ");
		bufferXml.append("								<streetNameBase></streetNameBase> ");
		bufferXml.append("								<!-- 地址-村（街、路、弄等）   --> ");
		bufferXml.append("								<streetName></streetName> ");
		bufferXml.append("								<!-- 地址-门牌号码 --> ");
		bufferXml.append("								<houseNumber></houseNumber> ");
		bufferXml.append("								<!-- 邮政编码--> ");
		bufferXml.append("								<postalCode>"+this.custom.getZIP_CODE()+"</postalCode> ");
		bufferXml.append("							</addr> ");
		bufferXml.append("							<!--婚姻状况--> ");
		bufferXml.append("							<maritalStatusCode  codeSystem=\"2.16.156.10011.2.3.3.5\" /> ");
		bufferXml.append("							<!--民族--> ");
		bufferXml.append("							<ethnicGroupCode  codeSystem=\"2.16.156.10011.2.3.3.3\" /> ");
		bufferXml.append("							<!--职业类别代码--> ");
		bufferXml.append("							<asEmployee classCode=\"EMP\"> ");
		bufferXml.append("								<occupationCode codeSystem=\"2.16.156.10011.2.3.3.7\" /> ");
		bufferXml.append("								<employerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--工作单位名称--> ");
		bufferXml.append("									<name>"+this.custom.getUNIT_IN_CONTRACT()+"</name> ");
		bufferXml.append("									<contactParty classCode=\"CON\"> ");
		bufferXml.append("										<!--工作联系电话--> ");
		if("".equals(this.custom.getPHONE_NUMBER_BUSINESS()) || this.custom.getPHONE_NUMBER_BUSINESS() == null){
			bufferXml.append("										<telecom value=\"\" use=\"WP\"/> ");
		}else{
			bufferXml.append("										<telecom value=\""+this.custom.getPHONE_NUMBER_BUSINESS()+"\" use=\"WP\"/> ");
		}
		bufferXml.append("									</contactParty> ");
		bufferXml.append("								</employerOrganization> ");
		bufferXml.append("							</asEmployee> ");
		
		bufferXml.append("							<asOtherIDs classCode=\"PAT\"> ");
		bufferXml.append("								<!--体检号--> ");
		bufferXml.append("								<id root=\"2.16.156.10011.1.13\" extension=\""+this.custom.getEXAM_NUM()+"\"/> ");
		bufferXml.append("								<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--体检卡发放机构代码--> ");
		bufferXml.append("									<id root=\"2.16.156.10011.1.5\" extension=\"青海省人民医院\"/> ");
		bufferXml.append("								</scopingOrganization> ");
		bufferXml.append("							</asOtherIDs> ");
		
		bufferXml.append("							<asOtherIDs classCode=\"PAT\"> ");
		bufferXml.append("								<!--健康卡号--> ");
		bufferXml.append("								<id root=\"2.16.156.10011.1.19\" extension=\""+this.custom.getCARD_NO()+"\"/> ");
		bufferXml.append("								<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<id root=\"2.16.156.10011.1.5\" extension=\"青海省人民医院\"/> ");
		bufferXml.append("								</scopingOrganization> ");
		bufferXml.append("							</asOtherIDs> ");
		bufferXml.append("							<asOtherIDs classCode=\"PAT\"> ");
		bufferXml.append("								<!--城乡居民健康档案编号--> ");
		bufferXml.append("								<id root=\"2.16.156.10011.1.2\" extension=\"38273N237\"/> ");
		bufferXml.append("								<scopingOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--建档医疗机构组织机构代码--> ");
		bufferXml.append("									<id root=\"2.16.156.10011.1.5\" /> ");
		bufferXml.append("								</scopingOrganization> ");
		bufferXml.append("							</asOtherIDs> ");
		bufferXml.append("							<!--联系人--> ");
		bufferXml.append("							<personalRelationship> ");
		bufferXml.append("								<code/> ");
		bufferXml.append("								<!--联系人电话--> ");
		if("".equals(this.custom.getNEXT_OF_KIN_PHONE()) || this.custom.getNEXT_OF_KIN_PHONE() == null){
			bufferXml.append("								<telecom use=\"H\" value=\"\"/> ");
		}else{
			bufferXml.append("								<telecom use=\"H\" value=\""+this.custom.getNEXT_OF_KIN_PHONE()+"\"/> ");
			
		}
		bufferXml.append("								<relationshipHolder1 classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("									<!--联系人姓名--> ");
		bufferXml.append("									<name>"+this.custom.getNEXT_OF_KIN()+"</name> ");
		bufferXml.append("								</relationshipHolder1> ");
		bufferXml.append("							</personalRelationship> ");
		bufferXml.append("						</patientPerson> ");
		bufferXml.append("						<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("							<id root=\"2.16.156.10011.1.5\" extension=\"44000115-4\" /> ");
		bufferXml.append("							<name use=\"L\"></name> ");
		bufferXml.append("							<contactParty classCode=\"CON\"/> ");
		bufferXml.append("						</providerOrganization> ");
		bufferXml.append("						<!--医疗保险信息--> ");
		bufferXml.append("						<coveredPartyOf typeCode=\"COV\"> ");
		bufferXml.append("							<coverageRecord classCode=\"COV\" moodCode=\"EVN\"> ");
		bufferXml.append("								<beneficiary typeCode=\"BEN\"> ");
		bufferXml.append("									<beneficiary classCode=\"MBR\"> ");
		bufferXml.append("										<code code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.248\" codeSystemName=\"医疗保险类别代码\" displayName=\"城镇职工基本医疗保险\"/> ");
		bufferXml.append("									</beneficiary> ");
		bufferXml.append("								</beneficiary> ");
		bufferXml.append("							</coverageRecord> ");
		bufferXml.append("						</coveredPartyOf> ");
		bufferXml.append("					</patient> ");
		bufferXml.append("				</subject1> ");
		bufferXml.append("				<author typeCode=\"AUT\"> ");
		bufferXml.append("					<assignedEntity classCode=\"ASSIGNED\"> ");
		// 录入人 固定 
		bufferXml.append("						<id root=\"2.16.156.10011.0.3.2\" extension=\"\"/> ");
		bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
		bufferXml.append("							<name use=\"L\"></name> ");
		bufferXml.append("						</assignedPerson> ");
		bufferXml.append("					</assignedEntity> ");
		bufferXml.append("				</author> ");
		bufferXml.append("			</registrationRequest> ");
		bufferXml.append("		</subject> ");
		bufferXml.append("	</controlActProcess> ");
		bufferXml.append("</PRPA_IN201314UV02> ");
		
		return bufferXml.toString();
	}
	
		

}
