package com.hjw.webService.client.xintong;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.xml.rpc.ServiceException;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.interfaces.util.LockCenterDateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.Bean.Custom;
import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 新增信息 接口
 * @author Administrator
 *
 */
public class CUSTOMSendMessageQH {
	
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
	
	public CUSTOMSendMessageQH(Custom custom){
		this.custom=custom;
	}
	
	
	String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//his开单医生id
	String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//his开单医生名称
	
	String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室名称
	String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
	
	/**
	 * 青海信通 建档信息
	 * @param url
	 * @param logname
	 * @return
	 */
	public ResultBody getMessage(String url,String logname) {
		@SuppressWarnings("unused")
	//	String charging_summary_num = GetNumContral.getInstance().getParamNum("rcpt_num");//就诊流水号
		ResultBody rb = new ResultBody();
		long examid=examIdForExamNum(this.custom.getEXAM_NUM(),logname);
		ExamInfoUserDTO ei = configService.getExamInfoForExam_id(examid);
		String patientid="";
		String xml = JaxbUtil.convertToXmlWithOutHead(this.custom, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"req:"+xml);
		
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
		
		//发送请求信息
		HIPMessageServiceService damQuery = new HIPMessageServiceServiceLocator(url);
		IHIPMessageService damQuerys;
		try {
			damQuerys = damQuery.getHIPMessageServicePort();
			
			String queryMsg = queryUserRegister(url, logname,ei);
			
			TranLogTxt.liswriteEror_to_txt(logname, "== url===" + url);
			
			TranLogTxt.liswriteEror_to_txt(logname, "==拼接查询 queryMsg 人员信息XML===" + queryMsg);
			
			ResHdMeessage rhd = LockCenterDateUtil.SetEaminatioinCenterDate(2020, Calendar.FEBRUARY, 15, logname);
			System.err.println(rhd.getStatus()+"===日期");
			if(rhd.getStatus().equals("AE")){
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText(rhd.getMessage());
				return rb;
			}else{
				String resultQuery = damQuerys.HIPMessageServer2016Ext("XT-PatientRegistryFindCandidatesQuery", queryMsg,sbgettokens.toString());
				//查询人员信息
				//解析消xml结果
				TranLogTxt.liswriteEror_to_txt(logname, "==查询人员信息 返回的XML===" + resultQuery);
				TranLogTxt.liswriteEror_to_txt(logname, "==解析resultQuery返回结果==" + QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","typeCode")+"==="+QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","text"));
				
				if ((resultQuery != null) && (resultQuery.trim().length() > 0)) {
					
					TranLogTxt.liswriteEror_to_txt(logname, "==解结果比较==" + "AA".equals(QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","typeCode")));
					
					if("AA".equals(QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","typeCode"))){
						
						//1.通过身份证号查询 患者信心信息是否存在
						patientid=QHResolveXML.getXmlResult(resultQuery,"PRPA_IN201306UV02","patientid");
						
						TranLogTxt.liswriteEror_to_txt(logname, "==更新个人信息==ULR"+url);
						
						String updateMsg = updatePersonal(url, logname,patientid,ei);
						
						TranLogTxt.liswriteEror_to_txt(logname, "==拼接 更新 updateMsg 人员信息 XML===" + updateMsg);
						
						damQuerys = damQuery.getHIPMessageServicePort();
						
						//2.更新个人信息 
						String resultUpdate = damQuerys.HIPMessageServer2016Ext("XT-PatientRegistryReviseRequest", updateMsg,sbgettokens.toString());
						
						TranLogTxt.liswriteEror_to_txt(logname, "==更新个人信息返回结果=="+ resultUpdate);
						
						
						if((resultUpdate != null) && (resultUpdate.trim().length() > 0)){
							if("AA".equals(QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","typeCode"))){
								TranLogTxt.liswriteEror_to_txt(logname, "==解析resultUpdate返回结果==" + QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","typeCode")+"==="+QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","text"));
								
								TranLogTxt.liswriteEror_to_txt(logname, "更新个人信息时候的患者id===="+ patientid);
								
								ControlActProcess process = updateExam_info(patientid,rb,"",logname);
								rb.setControlActProcess(process);
								rb.getResultHeader().setTypeCode("AA");
								rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201315UV02","text"));
								//更新zl_req_patInfo表内的  门诊号(mzh)
								update_zl_req_patInfo(patientid,examid,"",logname);
								
								
							}else{
								
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultUpdate,"PRPA_IN201316UV02","text"));
							}
							
						}
						
					}else{ //注册个人信息
						
						
						String regMsg = registerPersonal(url, logname,ei);
						
						TranLogTxt.liswriteEror_to_txt(logname, "==拼接 注册 regMsg 人员信息 XML===" + regMsg);
						
						damQuerys = damQuery.getHIPMessageServicePort();
						
						String resultAdd = "";
						
						try {
							
							resultAdd = damQuerys.HIPMessageServer2016Ext("XT-PatientRegistryAddRequest", regMsg,sbgettokens.toString());
							TranLogTxt.liswriteEror_to_txt(logname, "===resultAdd返回结果==="+resultAdd );
							
							
							if((resultAdd != null) && (resultAdd.trim().length() > 0)){
								if("AA".equals(QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201312UV02","typeCode"))){
									TranLogTxt.liswriteEror_to_txt(logname, "==解析resultAdd返回结果==" + QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201312UV02","typeCode")+"==="+QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201312UV02","text"));
									
									patientid = QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201312UV02","patientid");
									
									ControlActProcess process = updateExam_info(patientid,rb,"",logname);
									rb.setControlActProcess(process);
									rb.getResultHeader().setTypeCode("AA");
									rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201312UV02","text"));
									
									//4.插入zl_req_patInfo信息
									insert_zl_req_patInfo(examid,this.custom.getEXAM_NUM(),"",patientid,logname);
								
									
								}else{
									rb.getResultHeader().setTypeCode("AE");
									rb.getResultHeader().setText(QHResolveXML.getXmlResult(resultAdd,"PRPA_IN201313UV02","text"));
								}
								
							}
						} catch (Exception e) {
							
							TranLogTxt.liswriteEror_to_txt(logname, "==异常错误=="+com.hjw.interfaces.util.StringUtil.formatException(e));
						}
						
						
					}
				}else{
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("查询接口返回错误");
				}
			}
			
		} catch (Exception e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("查询接口返回错误");
			e.printStackTrace();
		} 
		
		return rb;
	}

	

	private ControlActProcess updateExam_info(String patientid, ResultBody rb, String jzh, String logname) {
				//插入表
			TranLogTxt.liswriteEror_to_txt(logname, "人员新增更新examinfo的患者id=="+patientid);
				CustomResBean cus= new CustomResBean();
				cus.setCLINIC_NO(patientid);
				cus.setPATIENT_ID(patientid);
				//cus.setVISIT_NO(jzh);
				cus.setVISIT_DATE(DateTimeUtil.getDateTimes());
				List<CustomResBean> LIST=new ArrayList<CustomResBean>();
				LIST.add(cus);
				ControlActProcess controlActProcess=new ControlActProcess();
				controlActProcess.setLIST(LIST);
				
				rb.setControlActProcess(controlActProcess);
				rb.getResultHeader().setTypeCode("AA");
				
				return controlActProcess;
	}

	

	private void update_zl_req_patInfo(String patientid, long examid, String jzh, String logname) {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			
			String updatesql = " update zl_req_patInfo set zl_mzh='"+jzh+"' where  exam_info_id = '"+examid+"' and zl_pat_id='"+patientid+"' ";
			TranLogTxt.liswriteEror_to_txt("update_zl_req_patInfo", "res:" + updatesql + "\r\n");
			tjtmpconnect.createStatement().executeUpdate(updatesql);
			TranLogTxt.liswriteEror_to_txt(logname, "更新zl_req_patInfo=="+updatesql);
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
	 * 查询平台系统是否此人已经注册
	 * @param url
	 * @param logname
	 * @param ei 
	 * @return
	 */
	private String queryUserRegister(String url , String logname, ExamInfoUserDTO ei){
		
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("	<PRPA_IN201305UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201305UV02.xsd\"> ");
		bufferXml.append("		<id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                 ");
		bufferXml.append("		<creationTime value=\"20070803130624\"/>                                                         ");
		bufferXml.append("		<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201305UV02\"/>                    ");
		bufferXml.append("		<processingCode code=\"P\"/>                                                                     ");
		bufferXml.append("		<processingModeCode code=\"R\"/>                                                                 ");
		bufferXml.append("		<acceptAckCode code=\"AL\"/>                                                                     ");
		bufferXml.append("		<receiver typeCode=\"RCV\">                                                                      ");
		bufferXml.append("			<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                         ");
		bufferXml.append("				<id root=\"2.16.156.10011.0.1.1\" extension=\"HIP\"/>                     ");
		bufferXml.append("			</device>                                                                                  ");
		bufferXml.append("		</receiver>                                                                                    ");
		bufferXml.append("		<sender typeCode=\"SND\">                                                                        ");
		bufferXml.append("			<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                         ");
		bufferXml.append("				<id root=\"2.16.156.10011.0.1.2\" extension=\"HJW\"/>                     ");
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
		bufferXml.append("						<value root=\"2.16.156.10011.1.3\" extension=\""+this.custom.getID_NO()+"\"></value>                 ");
		bufferXml.append("						<semanticsText>患者身份证号</semanticsText>                                ");
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
	 * @param ei 
	 * @return
	 */
	private String registerPersonal(String url , String logname, ExamInfoUserDTO ei) {
		
		StringBuffer bufferXml = new StringBuffer();
		bufferXml.append("	<PRPA_IN201311UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201311UV02.xsd\" xmlns=\"urn:hl7-org:v3\"> ");
		bufferXml.append("	<id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/> ");
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
		bufferXml.append("						<id root=\"2.16.156.10011.0.2.2\" extension=\""+this.custom.getID_NO()+"\"/> ");
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
		if("".equals(this.custom.getNEXT_OF_KIN_PHONE()) || this.custom.getNEXT_OF_KIN_PHONE() == null){
			bufferXml.append("							<telecom value=\""+ei.getPhone()+"\"  use=\"H\" /> ");
		}else{
			bufferXml.append("							<telecom value=\""+ei.getPhone()+"\" use=\"H\" /> ");
		}
		
		String SexCode="";
		if("男".equals(this.custom.getSEX())){
			SexCode="1";
			
		}else if("女".equals(this.custom.getSEX())){
			SexCode="2";
		}else{
			SexCode="0";
		}
		
		bufferXml.append("							<!--性别--> ");
		bufferXml.append("							<administrativeGenderCode code=\""+SexCode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""+this.custom.getSEX()+"\"/> ");
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
		if("".equals(this.custom.getNEXT_OF_KIN_PHONE()) || this.custom.getNEXT_OF_KIN_PHONE() == null){
			bufferXml.append("										<telecom value=\"\" use=\"WP\"/> ");
		}else{
			bufferXml.append("										<telecom value=\""+this.custom.getNEXT_OF_KIN_PHONE()+"\" use=\"WP\"/> ");
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
		bufferXml.append("						<id root=\"2.16.156.10011.0.3.2\" extension=\""+doctorid+"\"/> ");
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
	 * @param patientid 
	 * @param ei 
	 * @return
	 */
	private String updatePersonal(String url , String logname, String patientid, ExamInfoUserDTO ei) {
		
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("<PRPA_IN201314UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN201314UV02.xsd\" xmlns=\"urn:hl7-org:v3\"> ");
		bufferXml.append("	<id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/> ");
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
		bufferXml.append("						<id root=\"2.16.156.10011.0.2.2\" extension=\""+patientid+"\"/> ");
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
		if("".equals(this.custom.getNEXT_OF_KIN_PHONE()) || this.custom.getNEXT_OF_KIN_PHONE() == null){
			bufferXml.append("							<telecom value=\""+ei.getPhone()+"\" use=\"H\"/> ");
		}else{
			bufferXml.append("							<telecom value=\""+ei.getPhone()+"\" use=\"H\"/> ");
		}
		
		String SexCode="";
		if("男".equals(this.custom.getSEX())){
			SexCode="1";
			
		}else if("女".equals(this.custom.getSEX())){
			SexCode="2";
		}else{
			SexCode="0";
		}
		
		bufferXml.append("							<!--性别--> ");
		bufferXml.append("							<administrativeGenderCode code=\""+SexCode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""+this.custom.getSEX()+"\"/> ");
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
		if("".equals(this.custom.getNEXT_OF_KIN_PHONE()) || this.custom.getNEXT_OF_KIN_PHONE() == null){
			bufferXml.append("										<telecom value=\"\" use=\"WP\"/> ");
		}else{
			bufferXml.append("										<telecom value=\""+this.custom.getNEXT_OF_KIN_PHONE()+"\" use=\"WP\"/> ");
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
		bufferXml.append("						<id root=\"2.16.156.10011.0.3.2\" extension=\""+doctorid+"\"/> ");
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
	
	
	
	
	/**
	 * 查询个人基本信息是否已经注册
	 * @param patiendId 病人ID
	 * @param logname  
	 * @param uuidjz 
	 * @param charging_summary_num 
	 * @return
	 */
	
	//门诊就诊登记服务 拼接xml
	public String AmbulatoryEncounterStarted(String url , String logname,String patientid, String uuidjz, String jzh){
		StringBuffer bufferXml = new StringBuffer();
		bufferXml.append("<PRPA_IN401001UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/PRPA_IN401001UV02.xsd\">   ");
		bufferXml.append("	<id root=\"2.16.156.10011.0\" extension=\""+uuidjz+"\"/>                                                                                                                           ");
		bufferXml.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                                                                                                     ");
		bufferXml.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201301UV02\"/>                                                                                                                              ");
		bufferXml.append("	<processingCode code=\"P\"/>                                                                                                                                                                                 ");
		bufferXml.append("	<processingModeCode code=\"R\"/>                                                                                                                                                                             ");
		bufferXml.append("	<acceptAckCode code=\"AL\"/>                                                                                                                                                                                 ");
		bufferXml.append("	<receiver typeCode=\"RCV\">                                                                                                                                                                                  ");
		bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                                                                                                                                   ");
		bufferXml.append("			<id root=\"2.16.156.10011.0.1.1\" extension=\"HIP\"/>                                                                                                                               ");
		bufferXml.append("		</device>                                                                                                                                                                                                ");
		bufferXml.append("	</receiver>                                                                                                                                                                                                  ");
		bufferXml.append("	<sender typeCode=\"SND\">                                                                                                                                                                                    ");
		bufferXml.append("		<device classCode=\"CER\" determinerCode=\"INSTANCE\">                                                                                                                                                   ");
		bufferXml.append("			<id root=\"2.16.156.10011.0.1.2\" extension=\"HJW\"/>                                                                                                                               ");
		bufferXml.append("		</device>                                                                                                                                                                                                ");
		bufferXml.append("	</sender>                                                                                                                                                                                                    ");
		bufferXml.append("	<controlActProcess classCode=\"CACT\" moodCode=\"APT\">                                                                                                                                                      ");
		bufferXml.append("		<subject typeCode=\"SUBJ\">                                                                                                                                                                              ");
		bufferXml.append("			<!--挂号登记事件信息 -->                                                                                                                                                                             ");
		bufferXml.append("			<encounterEvent classCode=\"ENC\" moodCode=\"EVN\">                                                                                                                                                  ");
		bufferXml.append("				<!--门（急）诊号数据元：HDSD00.02.040	DE01.00.010.00 就诊流水号-->                                                                                                                             ");
		bufferXml.append("				<id root=\"2.16.156.10011.0.5.1\" extension=\""+jzh+"\"/>                                                                                                                                   ");
		bufferXml.append("				<!--就诊类别代码（患者类别代码）数据元: HDSD00.02.024	DE02.01.060.00   值域：1.门诊 2.急诊 3.住院 9.其他-->                                                                                    ");
		bufferXml.append("				<code code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"9\" displayName=\"其他\"/>                                                                                ");
		bufferXml.append("				<statusCode code=\"active\"/>                                                                                                                                                                    ");
		bufferXml.append("				<!--就诊日期时间数据元：HDSD00.02.036	DE06.00.062.00 -->                                                                                                                                       ");
		bufferXml.append("				<effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                                                                                              ");
		bufferXml.append("				<!--就诊原因数据元：HDSD00.02.037	DE05.10.053.00-->                                                                                                                                            ");
		bufferXml.append("				<reasonCode>                                                                                                                                                                                     ");
		bufferXml.append("					<originalText>体检</originalText>                                                                                                                                              ");
		bufferXml.append("				</reasonCode>                                                                                                                                                                                    ");
		bufferXml.append("				<!--患者-->                                                                                                                                                                                      ");
		bufferXml.append("				<subject typeCode=\"SBJ\" contextControlCode=\"OP\">                                                                                                                                             ");
		bufferXml.append("					<patient classCode=\"PAT\">                                                                                                                                                                  ");
		bufferXml.append("						<!--平台注册的患者ID -->                                                                                                                                                                 ");
		bufferXml.append("						<id code=\""+patientid+"\"/>                                                                                                                                                                    ");
		bufferXml.append("						<!--患者基本信息-->                                                                                                                                                                      ");
		bufferXml.append("						<patientPerson>                                                                                                                                                                          ");
		bufferXml.append("							<!--姓名数据元：HDSD00.02.027	DE02.01.039.00 -->                                                                                                                                   ");
		bufferXml.append("							<name use=\"L\">"+this.custom.getNAME()+"</name>                                                                                                                                                        ");
		bufferXml.append("						</patientPerson>                                                                                                                                                                         ");
		bufferXml.append("					</patient>                                                                                                                                                                                   ");
		bufferXml.append("				</subject>                                                                                                                                                                                       ");
		bufferXml.append("				<!--接诊医生-->                                                                                                                                                                                  ");
		bufferXml.append("				<consultant typeCode=\"CON\">                                                                                                                                                                    ");
		bufferXml.append("					<assignedPerson classCode=\"ASSIGNED\">                                                                                                                                                      ");
		bufferXml.append("						<!--医生的职工号-->                                                                                                                                                                      ");
		bufferXml.append("						<id root=\"2.16.156.10011.0.3.2\" extension=\""+doctorid+"\"/>                                                                                                                                    ");
		bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                                           ");
		bufferXml.append("							<!--责任医师姓名数据元：HDSD00.02.059	DE02.01.039.00	-->                                                                                                                          ");
		bufferXml.append("							<name>"+doctorname+"</name>                                                                                                                                                                  ");
		bufferXml.append("						</assignedPerson>                                                                                                                                                                        ");
		bufferXml.append("					</assignedPerson>                                                                                                                                                                            ");
		bufferXml.append("				</consultant>                                                                                                                                                                                    ");
		bufferXml.append("				<!--服务机构-->                                                                                                                                                                                  ");
		bufferXml.append("				<location typeCode=\"ORG\">                                                                                                                                                                      ");
		bufferXml.append("					<time/>                                                                                                                                                                                      ");
		bufferXml.append("					<serviceDeliveryLocation classCode=\"SDLOC\">                                                                                                                                                ");
		bufferXml.append("						<statusCode code=\"active\"/>                                                                                                                                                            ");
		bufferXml.append("						<location classCode=\"PLC\" determinerCode=\"INSTANCE\">                                                                                                                                 ");
		bufferXml.append("							<!--科室代码数据元：HDSD00.02.055	DE08.10.052.00	-->                                                                                                                              ");
		bufferXml.append("							<id root=\"2.16.156.10011.0.4.2\" extension=\""+kddepid+"\"/>                                                                                                                                ");
		bufferXml.append("							<!--科室名称数据元：HDSD00.02.054	DE08.10.026.00	-->                                                                                                                              ");
		bufferXml.append("							<name>"+kddepname+"</name>                                                                                                                                                                  ");
		bufferXml.append("						</location>                                                                                                                                                                              ");
		bufferXml.append("						<!--服务机构-->                                                                                                                                                                          ");
		bufferXml.append("						<serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                                                              ");
		bufferXml.append("							<!--医疗机构代码-->                                                                                                                                                                  ");
		bufferXml.append("							<id root=\"2.16.156.10011.1.5\" extension=\"44000115-4\"/>                                                                                                                           ");
		bufferXml.append("						</serviceProviderOrganization>                                                                                                                                                           ");
		bufferXml.append("					</serviceDeliveryLocation>                                                                                                                                                                   ");
		bufferXml.append("				</location>                                                                                                                                                                                      ");
		bufferXml.append("			</encounterEvent>                                                                                                                                                                                    ");
		bufferXml.append("		</subject>                                                                                                                                                                                               ");
		bufferXml.append("	</controlActProcess>                                                                                                                                                                                         ");
		bufferXml.append("</PRPA_IN401001UV02>                                                                                                                                                                                           ");
		
		return bufferXml.toString();
	}
	
	
	//插入zl_req_patInfo表
	private void insert_zl_req_patInfo(long examid,String exam_num,String jzh,String patientid, String logname){
		long exam_id=examid;
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			
			String insertsql = "insert into zl_req_patInfo ( exam_info_id,zl_pat_id,exam_num,"
					+ "zl_mzh,zl_tjh,flag) values('" + exam_id + "','" + patientid
					+ "','" + exam_num + "','" +jzh + "','" + patientid
					+ "','0')";
			TranLogTxt.liswriteEror_to_txt(logname, "人员新增插入中间表res==insertsql:" + insertsql + "\r\n");
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
	//通过exam_num 查询exam_info的id
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
