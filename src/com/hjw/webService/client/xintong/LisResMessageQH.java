package com.hjw.webService.client.xintong;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.xintong.lis.ResLisMessageQH;
import com.hjw.webService.client.xintong.lis.RetLisChargeItemQH;
import com.hjw.webService.client.xintong.lis.RetLisCustomeQH;
import com.hjw.webService.client.xintong.lis.RetLisItemQH;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class LisResMessageQH {
	
	private static ConfigService configService;
    private ThridInterfaceLog til;
    
    private static JdbcQueryManager jdbcQueryManager;

    static{
   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	
	/**
	 * 接受订阅报告信息  LIS解析报告  
	 * @param url 
	 * @param logname
	 */
	 public String getMessage(String xmlMsg,String logName) {
		 
		//用本地测试
		//xmlMsg = reportXML();
		
		String result = QHResolveXML.getNodeAttVal(xmlMsg, "abc:CUST_OUT00004/abc:controlActProcess/abc:subject/abc:message_contents","val");
		
		String result2 = result.replaceAll("&lt;", "<");
		String resultXML = result2.replaceAll("&gt;", ">");
		
		//获取病人ID
		String exam_num =  QHResolveXML.getNodeAttVal(resultXML.trim(), "abc:ProvideAndRegisterDocumentSetRequest/abc:SourcePatientID","val");
		
		if("".equals(exam_num) || exam_num==null){
			
			return "入库失败,病人ID为空";
		}
		
		TranLogTxt.liswriteEror_to_txt(logName, "----结果exam_num---"+exam_num);
		
		//解析XML结果
		String resultBase64 = QHResolveXML.getNodeAttVal(resultXML, "abc:ProvideAndRegisterDocumentSetRequest/abc:Document/abc:Content","val");
		
		//解密 xml信息
		TranLogTxt.liswriteEror_to_txt(logName, "----解密xml报告---"+getFromBase64(resultBase64.trim()));
		
		String req_code = QHResolveXML.lisRportInfoNode(getFromBase64(resultBase64.trim()),"SQDH");
		
		TranLogTxt.liswriteEror_to_txt(logName, "----结果req_code---"+req_code);
		
		ResultLisBody rb = insertLisMessage(logName,getFromBase64(resultBase64.trim()), exam_num, req_code);
		
		if("AA".equals(rb.getResultHeader().getTypeCode()) && rb.getResultHeader().getTypeCode()!= null){
			return "LIS数据入库成功";
		}else{
			return "LIS数据入库失败";
		}
		
	}
	
	
	/**
	 * 
	 * @Title: accetpMessageLis @Description: Lis
	 *         结果返回处理 @param: @return @return: String @throws
	 */
	public ResultLisBody insertLisMessage(String logName,String xml,String exam_num, String req_code) {
		
		String message_id = "TJ"+DateTimeUtil.getDateTimes();
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setReq_no(req_code);
    	til.setExam_no(exam_num);
    	til.setMessage_id(message_id);
    	til.setMessage_name("LIS_READ");
    	til.setMessage_type("webservice");
    	til.setSender("PEIS");
    	til.setReceiver("PF");
    	til.setFlag(2);
    	til.setXtgnb_id("2");//程序自动，设置为2-登记台首页
    	til.setMessage_inout(0);
		configService.insert_log(til);
		
		ResultLisBody rb = new ResultLisBody();
		
		configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "req:" + exam_num +"-"+req_code);
		try {
			
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"responseBody:"+xml);
			til.setMessage_response(xml);
			
			if(xml.length() > 20) {
				ResLisMessageQH rpm = new ResLisMessageQH(xml, true);
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"返回信息解析成功！");
				RetLisCustomeQH rc = rpm.rc;
				if ((rc == null) || (rc.getRetLisChargeItem() == null)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("lis信息解析为空");
				} else {
					LisResult lisResult = new LisResult();
					lisResult.setTil_id(til.getId());
					lisResult.setExam_num(exam_num);
					lisResult.setSample_barcode(req_code);
					lisResult.setDoctor(rc.getCheck_doc());
					lisResult.setExam_date(rc.getCheck_date());
					lisResult.setSh_doctor(rc.getAudit_doc());
					boolean flagss = true;
					int seq_code = 0;
					RetLisChargeItemQH rlcharg = rc.getRetLisChargeItem();
					lisResult.setLis_item_code(rlcharg.getChargingItem_num());
					lisResult.setLis_item_name(rlcharg.getChargingItem_name());
					lisResult.setItem_result(rlcharg.getItem_result()); //检查结果
					if(rlcharg.getListRetLisItem().size()>0){
						for (RetLisItemQH rlis : rlcharg.getListRetLisItem()) {
							lisResult.setSeq_code(seq_code++);
							lisResult.setReport_item_code(rlis.getItem_id());
							lisResult.setReport_item_name(rlis.getItem_name());
							lisResult.setItem_result(rlis.getValues());
							lisResult.setRef(rlis.getValue_fw());
							lisResult.setItem_unit(rlis.getValues_dw());
							lisResult.setFlag(rlis.getValue_ycbz());
							//火箭蛙		H-高		L-低		N-正常			P-阳性			C-危急	HH-偏高报警	LL-偏低报警
							if ("M".equals(lisResult.getFlag())) {
								lisResult.setFlag("N");
							} else if ("Q".equals(lisResult.getFlag())) {
								lisResult.setFlag("P");
							}
							
							boolean succ = this.configService.insert_lis_result(lisResult);
							if (succ) {
								rb.getResultHeader().setTypeCode("AA");
								rb.getResultHeader().setText("交易成功");
							} else {
								rb.getResultHeader().setTypeCode("AE");
								rb.getResultHeader().setText("pacs 入库失败");
								flagss = false;
							}
						}
					}
					
					if (flagss) {
						configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis信息 入库成功");
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("lis信息 入库成功");
						til.setFlag(0);
					} else {
						configService.insert_message_log(til.getId(), til.getMessage_seq_code(),"lis信息 入库错误");
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("lis信息 入库错误");
					}
				}
			} else {
				configService.insert_message_log(til.getId(), til.getMessage_seq_code(),xml.toString());
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("xml信息返回错误");
			}
			rb.getResultHeader().setTypeCode("AA");
		} catch (Exception ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "lis调用webservice错误:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			ex.printStackTrace();
			til.setFlag(1);
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("lis调用webservice错误");
		}
		
		configService.update_log(til);
		return rb;
	}
	
	
	// 加密
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = new BASE64Encoder().encode(b);
		}
		return s;
	}

	// 解密
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	/**
	 * 模拟接收XML  LIS报告
	 * @return
	 */
	private static String reportXML(){
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>  ");
		buffer.append("<CUST_OUT00004 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/CUST_OUT00004.xsd\">  ");
		buffer.append("  <id extension=\"e02aa38f0007c3f22530\" root=\"040CD76A-ED0E-400B-9FD3-60387BCDE0EB\"/> ");
		buffer.append("  <creationTime value=\"20190212132517\"/>                                             ");
		buffer.append("  <interactionId extension=\"CUST_OUT00004\" root=\"2.16.840.1.113883.1.6\"/>            ");
		buffer.append("  <processingCode code=\"D\"/>                                                         ");
		buffer.append("  <processingModeCode/>                                                              ");
		buffer.append("  <acceptAckCode code=\"AL\"/>                                                         ");
		buffer.append("  <receiver typeCode=\"RCV\">                                                          ");
		buffer.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\"/>                              ");
		buffer.append("  </receiver>                                                                        ");
		buffer.append("  <sender typeCode=\"SND\">                                                            ");
		buffer.append("    <device classCode=\"DEV\" determinerCode=\"INSTANCE\"/>                              ");
		buffer.append("  </sender>                                                                          ");
		buffer.append("  <acknowledgement typeCode=\"AA\">                                                    ");
		buffer.append("    <targetMessage>                                                                  ");
		buffer.append("      <id extension=\"1da0f9e0-1154-1cdc-svbe-1603d6866807\"/>                         ");
		buffer.append("    </targetMessage>                                                                 ");
		buffer.append("    <acknowledgementDetail typeCode=\"E\">                                             ");
		buffer.append("      <!--处理结果说明 -->                                                           ");
		buffer.append("      <text>                                                                         ");
		buffer.append("        <description value=\"\"/>                                                      ");
		buffer.append("      </text>                                                                        ");
		buffer.append("    </acknowledgementDetail>                                                         ");
		buffer.append("  </acknowledgement>                                                                 ");
		buffer.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                ");
		buffer.append("    <subject>                                                                        ");
		buffer.append("      <dispatch_id>e02aa38f0006c3f22530</dispatch_id>                                ");
		buffer.append("      <sender>LIS</sender>                                                          ");
		buffer.append("      <message_id>1da0f9e0-1154-1cdc-svbe-1603d6866807</message_id>                  ");
		buffer.append("      <action>PatientRegistryFindCandidatesQuery</action>                            ");
		buffer.append("      <create_time>2019-25-12 13:25:17</create_time>                                 ");
		buffer.append("      <response_uri>HIP.PACS.QUEUE</response_uri>                                    ");
		buffer.append("      <message_contents>                                                             ");
		//message里面的内容 
		
		buffer.append(" &lt;ProvideAndRegisterDocumentSetRequest xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:hl7-org:v3\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/ProvideAndRegisterDocumentSetRequest.xsd\"&gt; ");
		buffer.append("	&lt;ID root=\"请求消息OID\" extension=\"C193FE3B-E71F-4D81-8B8B-9462F35E8D38\"/&gt;                                                                   ");
		buffer.append("	&lt;SourcePatientID&gt;1904090004&lt;/SourcePatientID&gt;                                                                                            ");
		buffer.append("	&lt;SourcePatientName&gt;刘永2&lt;/SourcePatientName&gt;                                                                                          ");
		buffer.append("	&lt;HealthCardId&gt;6222022320001571462&lt;/HealthCardId&gt;                                                                                      ");
		buffer.append("	&lt;IdentityId&gt;120109197706015519&lt;/IdentityId&gt;                                                                                           ");
		buffer.append("	&lt;Organization id=\"77788899922\"&gt;                                                                                                             ");
		buffer.append("		&lt;Name&gt;XX.XX&lt;/Name&gt;                                                                                                                ");
		buffer.append("		&lt;TelephoneNumber areaCode=\"028\" number=\"4448888\"/&gt;                                                                                      ");
		buffer.append("		&lt;EmailAddress address=\"XXX@yy.com.cn\"/&gt;                                                                                                 ");
		buffer.append("		&lt;Address city=\"成都\" country=\"中国\" postalCode=\"55667788\" stateOrProvince=\"四川\" street=\"青阳街\" streetNumber=\"1234\"/&gt;                  ");
		buffer.append("	&lt;/Organization&gt;                                                                                                                             ");
		buffer.append("	&lt;RegistryPackage&gt;                                                                                                                           ");
		buffer.append("		&lt;SubmissionSet targetObject=\"Document.1\" availabilityStatus=\"Submitted\"&gt;                                                                ");
		buffer.append("			&lt;SubmissionTime&gt;2012-12-14T10:11:22Z&lt;/SubmissionTime&gt;                                                                         ");
		buffer.append("			&lt;UniqueId&gt;1.3.6.1.4.1.21367.2005.3.9999.33&lt;/UniqueId&gt;                                                                         ");
		buffer.append("			&lt;SourceId&gt;3670984664&lt;/SourceId&gt;                                                                                               ");
		buffer.append("			&lt;Comments&gt;皮肤过敏，治疗药物为盐酸布替萘芬乳膏。&lt;/Comments&gt;                                                                   ");
		buffer.append("			&lt;Title&gt;会诊记录&lt;/Title&gt;                                                                                                       ");
		buffer.append("			&lt;CreateTime&gt;2012-12-13T11:32:15Z&lt;/CreateTime&gt;                                                                                 ");
		buffer.append("			&lt;ServerOrganization&gt;YYY.YY&lt;/ServerOrganization&gt;                                                                               ");
		buffer.append("			&lt;EpisodeID&gt;1111&lt;/EpisodeID&gt;                                                                                                   ");
		buffer.append("			&lt;InTime&gt;2012-12-13T11:32:15Z&lt;/InTime&gt;                                                                                         ");
		buffer.append("			&lt;OutTime&gt;2012-12-13T12:32:15Z&lt;/OutTime&gt;                                                                                       ");
		buffer.append("			&lt;AdmissionDepart&gt;皮肤科&lt;/AdmissionDepart&gt;                                                                                     ");
		buffer.append("			&lt;AdmissionDoctor&gt;李医生&lt;/AdmissionDoctor&gt;                                                                                     ");
		buffer.append("			&lt;AdmissionType&gt;门诊&lt;/AdmissionType&gt;                                                                                           ");
		buffer.append("			&lt;DiagnosisResult&gt;皮肤过敏&lt;/DiagnosisResult&gt;                                                                                   ");
		buffer.append("			&lt;Author&gt;                                                                                                                            ");
		buffer.append("				&lt;AuthorName&gt;刘善&lt;/AuthorName&gt;                                                                                             ");
		buffer.append("				&lt;AuthorInstitution&gt;XX.XX&lt;/AuthorInstitution&gt;                                                                              ");
		buffer.append("				&lt;AuthorSpecialty&gt;皮肤&lt;/AuthorSpecialty&gt;                                                                                   ");
		buffer.append("				&lt;AuthorRole&gt;三级专家&lt;/AuthorRole&gt;                                                                                         ");
		buffer.append("			&lt;/Author&gt;                                                                                                                           ");
		buffer.append("		&lt;/SubmissionSet&gt;                                                                                                                        ");
		buffer.append("	&lt;/RegistryPackage&gt;                                                                                                                          ");
		buffer.append("	&lt;Document id=\"Document.1\" mimeType=\"text/xml\" parentDocumentRelationship=\"APND\" parentDocumentId=\"AAC97D6B-75BC-4E6E-8174-EFC09C722A09\"&gt;    ");
		buffer.append("		&lt;Content&gt;                                                                            ");
		//此xml base64加密
		buffer.append(" "+lisReportMsgBase64()+" ");
		
		buffer.append("     &lt;/Content&gt;                                 ");
		buffer.append("	&lt;/Document&gt;                                                                                                                                  ");
		buffer.append(" &lt;/ProvideAndRegisterDocumentSetRequest&gt;                                                                                                      ");
		
		buffer.append("</message_contents>                                                                                       ");
		buffer.append("    </subject>                                                                                            ");
		buffer.append("  </controlActProcess>                                                                                    ");
		buffer.append("</CUST_OUT00004>                                                                                          ");
		                                                                                                                         
		return buffer.toString();
	}
	
	
	
	/**
	 * 
	 * @return
	 */
		private static String lisReportMsgBase64(){
			
			StringBuffer buffer = new StringBuffer();
			
			buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>  ");
			
			buffer.append("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:mif=\"urn:hl7-org:v3/mif\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ..\\sdschemas\\SDA.xsd\">   "); 
			buffer.append("  <!--******************************************************** CDA Header********************************************************-->                          ");
			buffer.append("  <realmCode code=\"CN\"/>                                                                                                                                      ");
			buffer.append("  <typeId extension=\"POCD_MT000040\" root=\"2.16.840.1.113883.1.3\"/>                                                                                            ");
			buffer.append("  <templateId root=\"2.16.156.10011.2.1.1.27\"/>                                                                                                                ");
			buffer.append("  <!-- 文档流水号 -->                                                                                                                                         ");
			buffer.append("  <id extension=\"RN001\" root=\"2.16.156.10011.1.1\"/>                                                                                                           ");
			buffer.append("  <code code=\"C0007\" codeSystem=\"2.16.156.10011.2.4\" codeSystemName=\"卫生信息共享文档规范编码体系\"/>                                                          ");
			buffer.append("  <title>检验记录</title>                                                                                                                                     ");
			buffer.append("  <!-- 文档机器生成时间 -->                                                                                                                                   ");
			buffer.append("  <effectiveTime value=\"20190409194431\"/>                                                                                                                     ");
			buffer.append("  <confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" displayName=\"正常访问保密级别\"/>                         ");
			buffer.append("  <languageCode code=\"zh-CN\"/>                                                                                                                                ");
			buffer.append("  <setId/>                                                                                                                                                    ");
			buffer.append("  <versionNumber/>                                                                                                                                            ");
			buffer.append("  <!--文档记录对象（患者）-->                                                                                                                                 ");
			buffer.append("  <recordTarget contextControlCode=\"OP\" typeCode=\"RCT\">                                                                                                       ");
			buffer.append("    <patientRole classCode=\"PAT\">                                                                                                                             ");
			buffer.append("      <!--门（急）诊号标识 -->                                                                                                                                ");
			buffer.append("      <id extension=\"E10000000\" root=\"2.16.156.10011.1.11\"/>                                                                                                  ");
			buffer.append("      <!--住院号标识-->                                                                                                                                       ");
			buffer.append("      <id extension=\"E10000000\" root=\"2.16.156.10011.1.12\"/>                                                                                                  ");
			buffer.append("      <!--检验报告单号标识-->                                                                                                                                 ");
			buffer.append("      <id extension=\"LIS20190401006674111\" root=\"2.16.156.10011.1.33\"/>                                                                                       ");
			buffer.append("      <!--电子申请单编号-->                                                                                                                                   ");
			buffer.append("      <id extension=\"8190409004\" root=\"2.16.156.10011.1.24\"/>                                                                                                 ");
			buffer.append("      <!-- 检验标本编号标识 -->                                                                                                                               ");
			buffer.append("      <id extension=\"1\" root=\"2.16.156.10011.1.14\"/>                                                                                                          ");
			buffer.append("      <!-- 患者类别代码 -->                                                                                                                                   ");
			buffer.append("      <patientType>                                                                                                                                           ");
			buffer.append("        <patienttypeCode code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码表\" displayName=\"门诊\"/>                                  ");
			buffer.append("      </patientType>                                                                                                                                          ");
			buffer.append("      <telecom value=\"null\"/>                                                                                                                                 ");
			buffer.append("      <patient classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                     ");
			buffer.append("        <!--患者身份证号标识-->                                                                                                                               ");
			buffer.append("        <id extension=\"null\" root=\"2.16.156.10011.1.3\"/>                                                                                                      ");
			buffer.append("        <name>李真</name>                                                                                                                                     ");
			buffer.append("        <administrativeGenderCode code=\"2\" codeSystem=\"2.16.156.10011.2.3.3.4\" codeSystemName=\"生理性别代码表(GB/T 2261.1)\" displayName=\"女性\"/>              ");
			buffer.append("        <!-- 年龄 -->                                                                                                                                         ");
			buffer.append("        <age unit=\"岁\" value=\"23\"/>                                                                                                                           ");
			buffer.append("      </patient>                                                                                                                                              ");
			buffer.append("    </patientRole>                                                                                                                                            ");
			buffer.append("  </recordTarget>                                                                                                                                             ");
			buffer.append("  <!-- 检验报告医师（文档创作者） -->                                                                                                                         ");
			buffer.append("  <author contextControlCode=\"OP\" typeCode=\"AUT\">                                                                                                             ");
			buffer.append("    <!-- 检验报告日期 -->                                                                                                                                     ");
			buffer.append("    <time value=\"20190401\"/>                                                                                                                                  ");
			buffer.append("    <assignedAuthor classCode=\"ASSIGNED\">                                                                                                                     ");
			buffer.append("      <id extension=\"sa\" root=\"2.16.156.10011.1.7\"/>                                                                                                          ");
			buffer.append("      <!-- 医师姓名 -->                                                                                                                                       ");
			buffer.append("      <assignedPerson>                                                                                                                                        ");
			buffer.append("        <name>系统管理员</name>                                                                                                                               ");
			buffer.append("      </assignedPerson>                                                                                                                                       ");
			buffer.append("    </assignedAuthor>                                                                                                                                         ");
			buffer.append("  </author>                                                                                                                                                   ");
			buffer.append("  <!-- 保管机构 -->                                                                                                                                           ");
			buffer.append("  <custodian typeCode=\"CST\">                                                                                                                                  ");
			buffer.append("    <assignedCustodian classCode=\"ASSIGNED\">                                                                                                                  ");
			buffer.append("      <representedCustodianOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                            ");
			buffer.append("        <id extension=\"LIS20181102101429867\" root=\"2.16.156.10011.1.5\"/>                                                                                      ");
			buffer.append("        <name>测试医院一</name>                                                                                                                               ");
			buffer.append("      </representedCustodianOrganization>                                                                                                                     ");
			buffer.append("    </assignedCustodian>                                                                                                                                      ");
			buffer.append("  </custodian>                                                                                                                                                ");
			buffer.append("  <!-- 审核医师签名 -->                                                                                                                                       ");
			buffer.append("  <authenticator>                                                                                                                                             ");
			buffer.append("    <time/>                                                                                                                                                   ");
			buffer.append("    <signatureCode/>                                                                                                                                          ");
			buffer.append("    <assignedEntity>                                                                                                                                          ");
			buffer.append("      <id extension=\"sa\" root=\"2.16.156.10011.1.4\"/>                                                                                                          ");
			buffer.append("      <code displayName=\"检验技师\"/>                                                                                                                          ");
			buffer.append("      <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                              ");
			buffer.append("        <name>系统管理员</name>                                                                                                                               ");
			buffer.append("      </assignedPerson>                                                                                                                                       ");
			buffer.append("    </assignedEntity>                                                                                                                                         ");
			buffer.append("  </authenticator>                                                                                                                                            ");
			buffer.append("  <!-- 检验医师签名 -->                                                                                                                                       ");
			buffer.append("  <authenticator>                                                                                                                                             ");
			buffer.append("    <time/>                                                                                                                                                   ");
			buffer.append("    <signatureCode/>                                                                                                                                          ");
			buffer.append("    <assignedEntity>                                                                                                                                          ");
			buffer.append("      <id extension=\"sa\" root=\"2.16.156.10011.1.4\"/>                                                                                                          ");
			buffer.append("      <code displayName=\"检查验医师\"/>                                                                                                                        ");
			buffer.append("      <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                              ");
			buffer.append("        <name>系统管理员</name>                                                                                                                               ");
			buffer.append("      </assignedPerson>                                                                                                                                       ");
			buffer.append("    </assignedEntity>                                                                                                                                         ");
			buffer.append("  </authenticator>                                                                                                                                            ");
			buffer.append("  <!-- 检验申请机构及科室 -->                                                                                                                                 ");
			buffer.append("  <participant typeCode=\"PRF\">                                                                                                                                ");
			buffer.append("    <time/>                                                                                                                                                   ");
			buffer.append("    <associatedEntity classCode=\"ASSIGNED\">                                                                                                                   ");
			buffer.append("      <scopingOrganization>                                                                                                                                   ");
			buffer.append("        <id root=\"2.16.156.10011.1.26\"/>                                                                                                                      ");
			buffer.append("        <name>测试(GCP)</name>                                                                                                                                ");
			buffer.append("        <asOrganizationPartOf>                                                                                                                                ");
			buffer.append("          <wholeOrganization>                                                                                                                                 ");
			buffer.append("            <id extension=\"LIS20181102101429867\" root=\"2.16.156.10011.1.5\"/>                                                                                  ");
			buffer.append("            <name>测试医院一</name>                                                                                                                           ");
			buffer.append("          </wholeOrganization>                                                                                                                                ");
			buffer.append("        </asOrganizationPartOf>                                                                                                                               ");
			buffer.append("      </scopingOrganization>                                                                                                                                  ");
			buffer.append("    </associatedEntity>                                                                                                                                       ");
			buffer.append("  </participant>                                                                                                                                              ");
			buffer.append("  <relatedDocument typeCode=\"RPLC\">                                                                                                                           ");
			buffer.append("    <parentDocument>                                                                                                                                          ");
			buffer.append("      <id/>                                                                                                                                                   ");
			buffer.append("      <setId/>                                                                                                                                                ");
			buffer.append("      <versionNumber/>                                                                                                                                        ");
			buffer.append("    </parentDocument>                                                                                                                                         ");
			buffer.append("  </relatedDocument>                                                                                                                                          ");
			buffer.append("  <!-- 病床号、病房、病区、科室和医院的关联 -->                                                                                                               ");
			buffer.append("  <componentOf>                                                                                                                                               ");
			buffer.append("    <encompassingEncounter>                                                                                                                                   ");
			buffer.append("      <effectiveTime/>                                                                                                                                        ");
			buffer.append("      <location>                                                                                                                                              ");
			buffer.append("        <healthCareFacility>                                                                                                                                  ");
			buffer.append("          <serviceProviderOrganization>                                                                                                                       ");
			buffer.append("            <asOrganizationPartOf classCode=\"PART\">                                                                                                           ");
			buffer.append("              <!-- DE01.00.026.00	病床号 -->                                                                                                               ");
			buffer.append("              <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                                   ");
			buffer.append("                <id extension=\"22\" root=\"2.16.156.10011.1.22\"/>                                                                                               ");
			buffer.append("                <!-- DE01.00.019.00	病房号 -->                                                                                                               ");
			buffer.append("                <asOrganizationPartOf classCode=\"PART\">                                                                                                       ");
			buffer.append("                  <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                               ");
			buffer.append("                    <id root=\"2.16.156.10011.1.21\"/>                                                                                                          ");
			buffer.append("                    <!-- DE08.10.026.00	科室名称 -->                                                                                                         ");
			buffer.append("                    <asOrganizationPartOf classCode=\"PART\">                                                                                                   ");
			buffer.append("                      <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                           ");
			buffer.append("                        <id root=\"2.16.156.10011.1.26\"/>                                                                                                      ");
			buffer.append("                        <name>测试(GCP)</name>                                                                                                                ");
			buffer.append("                        <!-- DE08.10.054.00	病区名称 -->                                                                                                     ");
			buffer.append("                        <asOrganizationPartOf classCode=\"PART\">                                                                                               ");
			buffer.append("                          <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                       ");
			buffer.append("                            <id root=\"2.16.156.10011.1.27\"/>                                                                                                  ");
			buffer.append("                            <name/>                                                                                                                           ");
			buffer.append("                            <!--XXX医院 -->                                                                                                                   ");
			buffer.append("                            <asOrganizationPartOf classCode=\"PART\">                                                                                           ");
			buffer.append("                              <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                   ");
			buffer.append("                                <id extension=\"LIS20181102101429867\" root=\"2.16.156.10011.1.5\"/>                                                              ");
			buffer.append("                                <name>测试医院一</name>                                                                                                       ");
			buffer.append("                              </wholeOrganization>                                                                                                            ");
			buffer.append("                            </asOrganizationPartOf>                                                                                                           ");
			buffer.append("                          </wholeOrganization>                                                                                                                ");
			buffer.append("                        </asOrganizationPartOf>                                                                                                               ");
			buffer.append("                      </wholeOrganization>                                                                                                                    ");
			buffer.append("                    </asOrganizationPartOf>                                                                                                                   ");
			buffer.append("                  </wholeOrganization>                                                                                                                        ");
			buffer.append("                </asOrganizationPartOf>                                                                                                                       ");
			buffer.append("              </wholeOrganization>                                                                                                                            ");
			buffer.append("            </asOrganizationPartOf>                                                                                                                           ");
			buffer.append("          </serviceProviderOrganization>                                                                                                                      ");
			buffer.append("        </healthCareFacility>                                                                                                                                 ");
			buffer.append("      </location>                                                                                                                                             ");
			buffer.append("    </encompassingEncounter>                                                                                                                                  ");
			buffer.append("  </componentOf>                                                                                                                                              ");
			buffer.append("  <!--***************************************************文档体Body*******************************************************-->                                 ");
			buffer.append("  <component>                                                                                                                                                 ");
			buffer.append("    <structuredBody>                                                                                                                                          ");
			buffer.append("      <!-- 诊断记录章节-->                                                                                                                                    ");
			buffer.append("      <component>                                                                                                                                             ");
			buffer.append("        <section>                                                                                                                                             ");
			buffer.append("          <code code=\"29548-5\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Diagnosis\"/>                                            ");
			buffer.append("          <text/>                                                                                                                                             ");
			buffer.append("          <!--条目：诊断-->                                                                                                                                   ");
			buffer.append("          <entry>                                                                                                                                             ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                      ");
			buffer.append("              <code code=\"DE05.01.024.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"诊断代码\"/>                      ");
			buffer.append("              <!--诊断日期-->                                                                                                                                 ");
			buffer.append("              <effectiveTime value=\"20190401\"/>                                                                                                               ");
			buffer.append("              <value code=\"M10.903\" codeSystem=\"2.16.156.10011.2.3.3.11\" codeSystemName=\"ICD-10\" displayName=\"痛风结节\" xsi:type=\"CD\"/>                       ");
			buffer.append("              <performer>                                                                                                                                     ");
			buffer.append("                <assignedEntity>                                                                                                                              ");
			buffer.append("                  <id/>                                                                                                                                       ");
			buffer.append("                  <representedOrganization>                                                                                                                   ");
			buffer.append("                    <name>测试医院一</name>                                                                                                                   ");
			buffer.append("                  </representedOrganization>                                                                                                                  ");
			buffer.append("                </assignedEntity>                                                                                                                             ");
			buffer.append("              </performer>                                                                                                                                    ");
			buffer.append("            </observation>                                                                                                                                    ");
			buffer.append("          </entry>                                                                                                                                            ");
			buffer.append("        </section>                                                                                                                                            ");
			buffer.append("      </component>                                                                                                                                            ");
			buffer.append("      <!--实验室检查章节-->                                                                                                                                   ");
			buffer.append("      <component>                                                                                                                                             ");
			buffer.append("        <section>                                                                                                                                             ");
			buffer.append("          <code code=\"30954-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"STUDIES SUMMARY\"/>                                      ");
			buffer.append("          <text/>                                                                                                                                             ");
			buffer.append("          <entry>                                                                                                                                             ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                      ");
			buffer.append("              <code code=\"DE02.10.027.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验方法名称\"/>                  ");
			buffer.append("              <value xsi:type=\"ST\">患者接受医学检查项目所采用的检验方法名称</value>                                                                           ");
			buffer.append("            </observation>                                                                                                                                    ");
			buffer.append("          </entry>                                                                                                                                            ");
			buffer.append("          <entry>                                                                                                                                             ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                      ");
			buffer.append("              <code code=\"DE04.30.018.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"生化类\"/>                        ");
			buffer.append("              <value xsi:type=\"ST\">患者检验项目所属的类别详细描述</value>                                                                                     ");
			buffer.append("            </observation>                                                                                                                                    ");
			buffer.append("          </entry>                                                                                                                                            ");
			
			buffer.append("          <entry>                                                                                                                                             ");
			buffer.append("            <organizer classCode=\"CLUSTER\" moodCode=\"EVN\">                                                                                                    ");
			buffer.append("              <statusCode/>                                                                                                                                   ");
			buffer.append("              <component>                                                                                                                                     ");
			buffer.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
			buffer.append("                  <code code=\"DE04.30.019.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验项目代码\"/>              ");
			buffer.append("                  <!-- 检验时间 -->                                                                                                                           ");
			buffer.append("                  <effectiveTime value=\"20190401\"/>                                                                                                           ");
			buffer.append("                  <value xsi:type=\"ST\">1054| LDL-C| 低密度载脂蛋白| 1| 10| 1-10| H</value>                                                                                                      ");
			buffer.append("                  <entryRelationship typeCode=\"COMP\">                                                                                                         ");
			buffer.append("                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                              ");
			buffer.append("                      <code code=\"DE04.50.134.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本类别\"/>              ");
			buffer.append("                      <!-- DE04.50.137.00 标本采样日期时间	接收标本日期时间 -->                                                                             ");
			buffer.append("                      <effectiveTime>                                                                                                                         ");
			buffer.append("                        <low value=\"20190401\"/>                                                                                                               ");
			buffer.append("                        <high value=\"20190401\"/>                                                                                                              ");
			buffer.append("                      </effectiveTime>                                                                                                                        ");
			buffer.append("                      <value xsi:type=\"ST\">血清</value>                                                                                                       ");
			buffer.append("                    </observation>                                                                                                                            ");
			buffer.append("                  </entryRelationship>                                                                                                                        ");
			buffer.append("                  <entryRelationship typeCode=\"COMP\">                                                                                                         ");
			buffer.append("                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                              ");
			buffer.append("                      <code code=\"DE04.50.135.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本状态\"/>              ");
			buffer.append("                      <value xsi:type=\"ST\">已 审 提交</value>                                                                                                 ");
			buffer.append("                    </observation>                                                                                                                            ");
			buffer.append("                  </entryRelationship>                                                                                                                        ");
			buffer.append("                </observation>                                                                                                                                ");
			buffer.append("              </component>                                                                                                                                    ");
			buffer.append("              <component>                                                                                                                                     ");
			buffer.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
			buffer.append("                  <code code=\"DE04.30.017.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验结果代码\"/>              ");
			buffer.append("                  <value code=\"1\" codeSystem=\"2.16.156.10011.2.3.2.38\" codeSystemName=\"检查/检验结果代码表\" displayName=\"异常\" xsi:type=\"CD\"/>                ");
			buffer.append("                </observation>                                                                                                                                ");
			buffer.append("              </component>                                                                                                                                    ");
			buffer.append("              <component>                                                                                                                                     ");
			buffer.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
			buffer.append("                  <code code=\"DE04.30.015.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验定量结果\"/>              ");
			buffer.append("                  <value value=\"1\" xsi:type=\"REAL\"/>                                                                                                          ");
			buffer.append("                  <entryRelationship typeCode=\"COMP\">                                                                                                         ");
			buffer.append("                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                              ");
			buffer.append("                      <code code=\"DE04.30.016.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查定量结果计量单位\"/>  ");
			buffer.append("                      <value xsi:type=\"ST\">摩尔</value>                                                                                                                  ");
			buffer.append("                    </observation>                                                                                                                            ");
			buffer.append("                  </entryRelationship>                                                                                                                        ");
			buffer.append("                </observation>                                                                                                                                ");
			buffer.append("              </component>                                                                                                                                    ");
			buffer.append("            </organizer>                                                                                                                                      ");
			buffer.append("          </entry>                                                                                                                                            ");
			
			buffer.append("          <entry>                                                                                                                                             ");
			buffer.append("            <organizer classCode=\"CLUSTER\" moodCode=\"EVN\">                                                                                                    ");
			buffer.append("              <statusCode/>                                                                                                                                   ");
			buffer.append("              <component>                                                                                                                                     ");
			buffer.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
			buffer.append("                  <code code=\"DE04.30.019.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验项目代码\"/>              ");
			buffer.append("                  <!-- 检验时间 -->                                                                                                                           ");
			buffer.append("                  <effectiveTime value=\"20190401\"/>                                                                                                           ");
			buffer.append("                  <value xsi:type=\"ST\">1010| AST| 谷草转氨酶| 3| 8| 3-8| L</value>                                                                                                      ");
			buffer.append("                  <entryRelationship typeCode=\"COMP\">                                                                                                         ");
			buffer.append("                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                              ");
			buffer.append("                      <code code=\"DE04.50.134.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本类别\"/>              ");
			buffer.append("                      <!-- DE04.50.137.00 标本采样日期时间	接收标本日期时间 -->                                                                             ");
			buffer.append("                      <effectiveTime>                                                                                                                         ");
			buffer.append("                        <low value=\"20190401\"/>                                                                                                               ");
			buffer.append("                        <high value=\"20190401\"/>                                                                                                              ");
			buffer.append("                      </effectiveTime>                                                                                                                        ");
			buffer.append("                      <value xsi:type=\"ST\">血清</value>                                                                                                       ");
			buffer.append("                    </observation>                                                                                                                            ");
			buffer.append("                  </entryRelationship>                                                                                                                        ");
			buffer.append("                  <entryRelationship typeCode=\"COMP\">                                                                                                         ");
			buffer.append("                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                              ");
			buffer.append("                      <code code=\"DE04.50.135.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本状态\"/>              ");
			buffer.append("                      <value xsi:type=\"ST\">已 审 提交</value>                                                                                                 ");
			buffer.append("                    </observation>                                                                                                                            ");
			buffer.append("                  </entryRelationship>                                                                                                                        ");
			buffer.append("                </observation>                                                                                                                                ");
			buffer.append("              </component>                                                                                                                                    ");
			buffer.append("              <component>                                                                                                                                     ");
			buffer.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
			buffer.append("                  <code code=\"DE04.30.017.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验结果代码\"/>              ");
			buffer.append("                  <value code=\"1\" codeSystem=\"2.16.156.10011.2.3.2.38\" codeSystemName=\"检查/检验结果代码表\" displayName=\"异常\" xsi:type=\"CD\"/>                ");
			buffer.append("                </observation>                                                                                                                                ");
			buffer.append("              </component>                                                                                                                                    ");
			buffer.append("              <component>                                                                                                                                     ");
			buffer.append("                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
			buffer.append("                  <code code=\"DE04.30.015.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验定量结果\"/>              ");
			buffer.append("                  <value value=\"2\" xsi:type=\"REAL\"/>                                                                                                          ");
			buffer.append("                  <entryRelationship typeCode=\"COMP\">                                                                                                         ");
			buffer.append("                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                              ");
			buffer.append("                      <code code=\"DE04.30.016.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查定量结果计量单位\"/>  ");
			buffer.append("                      <value xsi:type=\"ST\">毫升</value>                                                                                                                  ");
			buffer.append("                    </observation>                                                                                                                            ");
			buffer.append("                  </entryRelationship>                                                                                                                        ");
			buffer.append("                </observation>                                                                                                                                ");
			buffer.append("              </component>                                                                                                                                    ");
			buffer.append("            </organizer>                                                                                                                                      ");
			buffer.append("          </entry>                                                                                                                                            ");
			
			buffer.append("        </section>                                                                                                                                           ");
			buffer.append("      </component>                                                                                                                                           ");
			buffer.append("      <!-- 检验报告章节 -->                                                                                                                                  ");
			buffer.append("      <component>                                                                                                                                            ");
			buffer.append("        <section>                                                                                                                                            ");
			buffer.append("          <code displayName=\"检验报告\"/>                                                                                                                     ");
			buffer.append("          <text/>                                                                                                                                            ");
			buffer.append("          <entry>                                                                                                                                            ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                     ");
			buffer.append("              <code code=\"DE04.50.130.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告结果\"/>                 ");
			buffer.append("              <value xsi:type=\"ST\">血脂六项,肝功全项| CJYB0140,CJY00011</value>                                                                                                      ");
			buffer.append("            </observation>                                                                                                                                   ");
			buffer.append("          </entry>                                                                                                                                           ");
			buffer.append("          <entry>                                                                                                                                            ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                     ");
			buffer.append("              <code code=\"DE08.10.026.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告科室\"/>                 ");
			buffer.append("              <value xsi:type=\"ST\">测试(GCP)</value>                                                                                                         ");
			buffer.append("            </observation>                                                                                                                                   ");
			buffer.append("          </entry>                                                                                                                                           ");
			buffer.append("          <entry>                                                                                                                                            ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                     ");
			buffer.append("              <code code=\"DE08.10.013.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告机构名称\"/>             ");
			buffer.append("              <value xsi:type=\"ST\">测试医院一</value>                                                                                                        ");
			buffer.append("            </observation>                                                                                                                                   ");
			buffer.append("          </entry>                                                                                                                                           ");
			buffer.append("          <entry>                                                                                                                                            ");
			buffer.append("            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                     ");
			buffer.append("              <code code=\"DE06.00.179.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告备注\"/>                 ");
			buffer.append("              <value xsi:type=\"ST\">检验报告备注信息</value>                                                                                                  ");
			buffer.append("            </observation>                                                                                                                                   ");
			buffer.append("          </entry>                                                                                                                                           ");
			buffer.append("        </section>                                                                                                                                           ");
			buffer.append("      </component>                                                                                                                                           ");
			buffer.append("    </structuredBody>                                                                                                                                     ");
			buffer.append("  </component>                                                                                                                                            ");
			buffer.append("</ClinicalDocument>                                                                                                                                       ");
			                                                                                                                                                                         
			return getBase64(buffer.toString().trim());
		}                   
	
	
	/*public static void main(String[] args) {
		
		String xmlMsg = reportXML();
		
		String result = QHResolveXML.getNodeAttVal(xmlMsg, "abc:CUST_OUT00004/abc:controlActProcess/abc:subject/abc:message_contents","val");
		
		String result2 = result.replaceAll("&lt;", "<");
		String resultXML = result2.replaceAll("&gt;", ">");
		
		//获取病人ID
		String reqNo =  QHResolveXML.getNodeAttVal(resultXML.trim(), "abc:ProvideAndRegisterDocumentSetRequest/abc:SourcePatientID","val");
		
		//System.out.println(reqNo);
		
		//解析XML结果
		String resultBase64 = QHResolveXML.getNodeAttVal(resultXML, "abc:ProvideAndRegisterDocumentSetRequest/abc:Document/abc:Content","val");
		
		System.out.println(resultBase64);
		
		
		//解密 xml信息
		
		
		String xml = getFromBase64(lisReportMsgBase64());
		System.out.println(xml.trim());
	}*/
		
                                                                                                                                                                            
}                                                                                                                                                                           
