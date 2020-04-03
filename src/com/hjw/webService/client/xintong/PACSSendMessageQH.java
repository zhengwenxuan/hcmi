package com.hjw.webService.client.xintong;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 01检查、02检验、03输血、04 病理、05手术
 * 
 * @author Administrator
 *
 */
public class PACSSendMessageQH {

	private PacsMessageBody pacsmessage;
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public PACSSendMessageQH(PacsMessageBody pacsmessage) {
		this.pacsmessage = pacsmessage;
	}

	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		JSONObject json = JSONObject.fromObject(pacsmessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "传入参数pacs===:" + str);

		try {
			List<ApplyNOBean> anList = new ArrayList<ApplyNOBean>();

			TranLogTxt.liswriteEror_to_txt(logname,
					"==pacsmessage.getComponents().size()===:" + pacsmessage.getComponents().size());

			for (PacsComponents comps : pacsmessage.getComponents()) {
				ResultHeader rhone = new ResultHeader();
				TranLogTxt.liswriteEror_to_txt(logname, "参数URL===:" + url + "参数comps===:" + comps);
				rhone = this.pacsQHSendMessage(url, comps, logname);
				TranLogTxt.liswriteEror_to_txt(logname, "rhone.getTypeCode()===:" + rhone.getTypeCode());
				if ("AA".equals(rhone.getTypeCode())) {
					rb.getResultHeader().setTypeCode("AA");
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
				} else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(rhone.getText());
				}
			}
			// rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}

		return rb;
	}

	/**
	 * 拼接pacs 检查拼接XML
	 * 
	 * @param compts
	 * @param logname
	 * @return
	 */
	private ResultHeader pacsQHSendMessage(String url, PacsComponents compts, String logname) {

		ResultHeader rhone = new ResultHeader();
		try {
			StringBuffer bufferXml = new StringBuffer();

			bufferXml.append(
					" <POOR_IN200901UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/POOR_IN200901UV.xsd\">  ");
			bufferXml.append(" <id root=\"2.16.156.10011.0\" extension=\"" + UUID.randomUUID().toString().toUpperCase()
					+ "\"/>");
			bufferXml.append("	<creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/>  ");
			bufferXml.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"POOR_IN200901UV\"/> ");
			bufferXml.append("	<processingCode code=\"P\"/>  ");
			bufferXml.append("	<processingModeCode code=\"I\"/>");
			bufferXml.append("	<acceptAckCode code=\"AL\"/>  ");
			bufferXml.append("	<receiver typeCode=\"RCV\">   ");
			bufferXml.append("		<telecom/>");
			bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
			bufferXml.append("			<id root=\"1.2.840.114350.1.13.999.567\"  extension=\"HIP\"/>  ");
			bufferXml.append("		</device> ");
			bufferXml.append("	</receiver>   ");
			bufferXml.append("	<sender typeCode=\"SND\"> ");
			bufferXml.append("		<telecom/>");
			bufferXml.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
			bufferXml.append("			<id root=\"1.2.840.114350.1.13.999.234\"  extension=\"HJW\" />  ");
			bufferXml.append("		</device> ");
			bufferXml.append("	</sender> ");
			bufferXml.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\"> ");
			bufferXml.append("		<code code=\"POOR_TE200901UV\" codeSystem=\"2.16.840.1.113883.1.6\"/>   ");
			bufferXml.append("		<subject typeCode=\"SUBJ\"> ");
			bufferXml.append("			<observationRequest classCode=\"OBS\" moodCode=\"RQO\">   ");
			bufferXml.append("				<!--电子申请单编号-->   ");
			bufferXml.append(
					"				<id root=\"2.16.156.10011.1.24\" extension=\"" + compts.getReq_no() + "\"/>   ");
			bufferXml.append("				<!--申请单类型--> ");
			bufferXml.append("				<code/>");
			bufferXml.append("				<!--申请单项目内容-->   ");
			if ("BL".equals(compts.getPacsComponent().get(0).getExam_class())) {
				bufferXml.append("				<text>04</text> ");
			} else {
				bufferXml.append("				<text>01</text> ");
			}
			bufferXml.append("				<!--申请单状态--> ");
			bufferXml.append("				<statusCode code=\"active\"/> ");
			bufferXml.append("				<!--申请单有效期间-->   ");
			bufferXml.append("				<effectiveTime xsi:type=\"IVL_TS\"> ");
			bufferXml.append("					<!--申请单计划开始日期时间--> ");
			bufferXml.append("					<low value=\"" + DateTimeUtil.getDateTimes() + "\"/> ");
			bufferXml.append("					<!--申请单计划结束日期时间--> ");
			bufferXml.append("					<high value=\"" + DateTimeUtil.DateAdd(30) + "000000\"/>");
			bufferXml.append("				</effectiveTime>");
			bufferXml.append("				<!--优先（紧急）度-->   ");
			bufferXml.append(
					"				<priorityCode code=\"R\" displayName=\"routine\" codeSystem=\"2.16.840.1.113883.1.11.16866\" codeSystemName=\"ActPriority\"/> ");
			bufferXml.append("				<!--标本信息--> ");
			bufferXml.append("				<specimen typeCode=\"SPC\" contextControlCode=\"OP\"> ");
			bufferXml.append("					<specimen classCode=\"SPEC\">   ");
			bufferXml.append("						<!--标本号-->   ");
			bufferXml.append("						<id root=\"2.16.156.10011.1.14\" extension=\"1\"/>   ");
			bufferXml.append("						<!--标本类别--> ");
			bufferXml.append("						<code/> ");
			bufferXml.append("						<specimenNatural classCode=\"ENT\" determinerCode=\"INSTANCE\"> ");
			bufferXml.append("							<code/> ");
			bufferXml.append("							<!--标本状态--> ");
			bufferXml.append("							<desc>对受检标本状态的描述</desc> ");
			bufferXml.append("							<additive classCode=\"ADTV\"> ");
			bufferXml.append(
					"								<additive3 classCode=\"MAT\" determinerCode=\"INSTANCE\">   ");
			bufferXml.append("									<!--标本固定液名称--> ");
			bufferXml.append("									<desc xsi:type=\"ST\"></desc> ");
			bufferXml.append("								</additive3>");
			bufferXml.append("							</additive> ");
			bufferXml.append("						</specimenNatural>  ");
			bufferXml.append("						<productOf typeCode=\"PRD\">");
			bufferXml.append("							<!--标本采样日期时间-->   ");
			bufferXml.append("							<time value=\"20130303020404\"/>");
			bufferXml.append("							<!--标本接收--> ");
			bufferXml.append("							<specimenProcessStep classCode=\"ACSN\" moodCode=\"EVN\">   ");
			bufferXml.append("								<!--接收标本日期时间--> ");
			bufferXml.append("								<effectiveTime value=\"20130303040404\"/> ");
			bufferXml.append("							</specimenProcessStep>");
			bufferXml.append("						</productOf>");
			bufferXml.append("					</specimen> ");
			bufferXml.append("				</specimen> ");
			bufferXml.append("				<!--记录对象--> ");
			bufferXml.append("				<recordTarget typeCode=\"RCT\" contextControlCode=\"OP\">   ");
			bufferXml.append("					<patient classCode=\"PAT\"> ");
			bufferXml.append("						<!--门（急）诊号标识 -->   ");
			bufferXml.append("                      <id root=\"2.16.156.10011.1.10\" extension=\""
					+ pacsmessage.getCustom().getExam_num() + "\"/>  ");
			bufferXml.append("                      <!--住院号标识--> ");
			bufferXml.append("                      <id root=\"2.16.156.10011.1.12\"  />  ");

			bufferXml.append("                      <!--个人信息--> ");
			bufferXml.append("                      <id root=\"2.16.156.10011.0.2.2\" extension=\""
					+ pacsmessage.getCustom().getExam_num() + "\"/>  ");

			bufferXml.append("					</patient>  ");
			bufferXml.append("				</recordTarget> ");
			bufferXml.append("				<!--申请单开立者--> ");
			bufferXml.append("				<author typeCode=\"AUT\" contextControlCode=\"OP\">   ");
			bufferXml.append("					<!--申请单开立日期时间--> ");
			bufferXml.append("					<time value=\"" + DateTimeUtil.getDateTimes() + "\"/>");
			bufferXml.append("					<signatureCode code=\"S\"/> ");
			bufferXml.append("					<!--申请单开立者签名--> ");
			bufferXml.append("					<signatureText>" + pacsmessage.getDoctor().getDoctorName()
					+ "</signatureText> ");
			bufferXml.append("					<assignedEntity classCode=\"ASSIGNED\"> ");
			bufferXml.append("						<!--医务人员ID-->   ");
			bufferXml.append("						<id root=\"2.16.156.10011.1.4\" extension=\""
					+ pacsmessage.getDoctor().getDoctorCode() + "\"/>  ");
			bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">  ");
			bufferXml.append("							<!--申请单开立者-->   ");
			bufferXml.append(
					"							<name>" + pacsmessage.getDoctor().getDoctorName() + "</name>   ");
			bufferXml.append("						</assignedPerson>   ");
			bufferXml.append(
					"						<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">   ");
			bufferXml.append("							<!--医疗卫生机构（科室）ID--> "); //
			bufferXml.append("							<id root=\"2.16.156.10011.1.26\" extension=\""
					+ pacsmessage.getDoctor().getDept_code() + "\"/> ");
			bufferXml.append("							<!--申请单开立科室--> ");
			bufferXml.append("							<name>" + pacsmessage.getDoctor().getDept_name() + "</name> "); //
			bufferXml.append("						</representedOrganization>");
			bufferXml.append("					</assignedEntity>   ");
			bufferXml.append("				</author>   ");
			bufferXml.append("				<!--申请单审核者--> ");
			bufferXml.append("				<verifier typeCode=\"VRF\" contextControlCode=\"OP\"> ");
			bufferXml.append("					<!--申请单审核日期时间--> ");
			bufferXml.append("					<time value=\"20120202030303\"/>");
			bufferXml.append("					<signatureCode code=\"S\"/> ");
			bufferXml.append("					<!--申请单审核者签名--> ");
			bufferXml.append("					<signatureText>王医生</signatureText> ");
			bufferXml.append("					<assignedEntity classCode=\"ASSIGNED\"> ");
			bufferXml.append("						<!--医务人员ID-->   ");
			bufferXml.append(
					"						<id root=\"2.16.156.10011.1.4\" extension=\"120109197706015518\"/>  ");
			bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">  ");
			bufferXml.append("							<!--申请单审核者-->   ");
			bufferXml.append("							<name></name>   ");
			bufferXml.append("						</assignedPerson>   ");
			bufferXml.append("					</assignedEntity>   ");
			bufferXml.append("				</verifier> ");
			bufferXml.append("				<!--目的--> ");
			bufferXml.append("				<goal typeCode=\"OBJC\">  ");
			bufferXml.append("					<observationEventCriterion>   ");
			bufferXml.append("						<text></text>   ");
			bufferXml.append("					</observationEventCriterion>  ");
			bufferXml.append("				</goal> ");
			bufferXml.append("				<!--原因--> ");
			bufferXml.append(
					"				<reason typeCode=\"RSON\" contextControlCode=\"OP\" contextConductionInd=\"true\">  ");
			bufferXml.append("					<!--现病史--> ");
			bufferXml.append("					<observation classCode=\"OBS\" moodCode=\"EVN\">  ");
			bufferXml.append(
					"						<code code=\"DE04.01.119.00\" displayName=\"主诉\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>");

			String customerType = LISSendMessageQH.queryCustomerTypeQH(pacsmessage.getCustom().getExam_num());
			if ("健康体检".equals(customerType)) {
				bufferXml.append("						<value xsi:type=\"ST\">公共机构居民的常规一般性健康查体</value> ");
			} else {
				bufferXml.append("						<value xsi:type=\"ST\">常规一般性健康查体，其他限定人群的</value> ");

			}
			bufferXml.append("					</observation>");
			bufferXml.append("				</reason>   ");
			bufferXml.append(
					"				<reason typeCode=\"RSON\" contextControlCode=\"OP\" contextConductionInd=\"true\">  ");
			bufferXml.append("					<!--症状描述--> ");
			bufferXml.append("					<observation classCode=\"OBS\" moodCode=\"EVN\">  ");
			bufferXml.append(
					"						<code code=\"DE04.01.117.00\" displayName=\"症状描述\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>");
			if ("健康体检".equals(customerType)) {
				bufferXml.append("						<value xsi:type=\"ST\">公共机构居民的常规一般性健康查体</value> ");
			} else {
				bufferXml.append("						<value xsi:type=\"ST\">常规一般性健康查体，其他限定人群的</value> ");
			}
			bufferXml.append("					</observation>");
			bufferXml.append("				</reason>   ");
			bufferXml.append("				<!--申请单备注信息-->   ");
			bufferXml.append("				<subjectOf6 typeCode=\"SUBJ\" contextConductionInd=\"false\">   ");
			bufferXml.append("					<seperatableInd value=\"false\"/>   ");
			bufferXml.append("					<annotation>");
			bufferXml.append("						<!--申请单备注信息--> ");
			bufferXml.append("						<text>对下达申请单的补充说明和注意事项提示</text>   ");
			bufferXml.append("						<statusCode code=\"completed\"/>");
			bufferXml.append("						<author>");
			bufferXml.append("							<assignedEntity classCode=\"ASSIGNED\"> ");
			bufferXml.append("								<!--医务人员ID--> ");
			bufferXml.append("								<id root=\"2.16.156.10011.1.4\" extension=\""
					+ pacsmessage.getDoctor().getDoctorCode() + "\"/>");
			bufferXml.append(
					"								<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">");
			bufferXml.append("									<!--申请单开立者--> ");
			bufferXml.append(
					"									<name>" + pacsmessage.getDoctor().getDoctorName() + "</name> ");
			bufferXml.append("								</assignedPerson> ");
			bufferXml.append(
					"								<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
			bufferXml.append("									<!--医疗卫生机构（科室）ID--> ");
			bufferXml.append(
					"									<id root=\"2.16.156.10011.1.26\" extension=\"RSS20140108000000001\"/>   ");
			bufferXml.append("									<!--申请单开立科室--> ");
			bufferXml.append("									<name>青海省人民医院</name> ");
			bufferXml.append("								</representedOrganization>");
			bufferXml.append("							</assignedEntity> ");
			bufferXml.append("						</author> ");
			bufferXml.append("					</annotation> ");
			bufferXml.append("				</subjectOf6>   ");
			bufferXml.append("				<!--就医信息--> ");
			bufferXml.append("				<componentOf1 contextConductionInd=\"false\">   ");
			bufferXml.append("					<encounter classCode=\"ENC\" moodCode=\"EVN\">");
			bufferXml.append("						<id/>   ");
			bufferXml.append("						<statusCode code=\"active\"/>   ");
			bufferXml.append("						<subject typeCode=\"SBJ\">  ");
			bufferXml.append("							<patient classCode=\"PAT\"> ");
			bufferXml.append("								<!--门（急）诊号标识 -->");
			bufferXml.append(
					"								<id root=\"2.16.156.10011.1.10\" extension=\""+pacsmessage.getCustom().getExam_num()+"\"/>  ");
			bufferXml.append("								<!--住院号标识--> ");
			bufferXml.append(
					"								<id root=\"2.16.156.10011.1.12\" extension=\"HA201102113366666\"/>");
			bufferXml.append("								<!--患者就医联系电话--> ");
			bufferXml.append("								<telecom/>  ");
			bufferXml.append("								<!--患者角色状态-->   ");
			bufferXml.append("								<statusCode code=\"active\"/>   ");
			bufferXml.append(
					"								<patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
			bufferXml.append("									<!--患者身份证号--> ");
			if (!"".equals(pacsmessage.getCustom().getPersonidnum())
					&& pacsmessage.getCustom().getPersonidnum() != null) {
				bufferXml.append("									<id root=\"2.16.156.10011.1.3\" extension=\""
						+ pacsmessage.getCustom().getPersonidnum() + "\"/>");
			} else {
				bufferXml.append("									<id root=\"2.16.156.10011.1.3\" />");
			}
			bufferXml.append("									<name use=\"L\">" + pacsmessage.getCustom().getName()
					+ "</name>   ");
			bufferXml.append(
					"									<!--性别-->																														");

			TranLogTxt.liswriteEror_to_txt(logname,
					"性别代码==" + pacsmessage.getCustom().getSexcode() + "性别==" + pacsmessage.getCustom().getSexname());

			
			String xingbie="M";
			if(pacsmessage.getCustom().getSexcode().equals("1")){
				xingbie="M";
			}else if(pacsmessage.getCustom().getSexcode().equals("2")){
				xingbie="F";
			}else{
				xingbie="0";
			}
			System.err.println("pacs性别==="+xingbie);
			bufferXml.append("									<administrativeGenderCode code=\""
					+ xingbie + "\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""
					+ pacsmessage.getCustom().getSexname() + "\" codeSystemName=\"生理性别代码表（GB/T 2261.1）\"/>  ");
			bufferXml.append("									<!--出生日期--> ");
			bufferXml.append("									<birthTime value=\""
					+ pacsmessage.getCustom().getBirthtime() + "\"/> ");
			bufferXml.append("									<asOtherIDs classCode=\"ROL\">");
			bufferXml.append("										<!--健康档案编号--> ");
			if (!"".equals(pacsmessage.getCustom().getArch_num()) && pacsmessage.getCustom().getArch_num() != null) {
				bufferXml.append("										<id root=\"2.16.156.10011.1.2\" extension=\""
						+ pacsmessage.getCustom().getArch_num() + "\"/>   ");
			} else {
				bufferXml.append("										<id root=\"2.16.156.10011.1.2\" />   ");
			}
			bufferXml.append("										<!--健康卡号--> ");
			bufferXml.append("										<id root=\"2.16.156.10011.1.19\"/> ");
			bufferXml.append("									</asOtherIDs>   ");
			bufferXml.append("								</patientPerson>");
			bufferXml.append("							</patient>");
			bufferXml.append("						</subject>");
			bufferXml.append("					</encounter>");
			bufferXml.append("				</componentOf1> ");
			bufferXml.append("			</observationRequest> ");
			bufferXml.append("		</subject>  ");
			bufferXml.append("	</controlActProcess>  ");
			bufferXml.append(
					"</POOR_IN200901UV>     																																		");

			TranLogTxt.liswriteEror_to_txt(logname, "拼接 PACS- XML信息==" + bufferXml.toString() + "\r\n");
			// 发送请求信息
			HIPMessageServiceService dam = new HIPMessageServiceServiceLocator(url);
			IHIPMessageService dams = dam.getHIPMessageServicePort();
			String gettokens = Gettoken.Gettokens(url,logname);
			String extXml = "";

			TranLogTxt.liswriteEror_to_txt(logname, "类别== compts.getPacsComponent().get(0).getExam_class()=="
					+ compts.getPacsComponent().get(0).getExam_class() + "\r\n");

			if ("BL".equals(compts.getPacsComponent().get(0).getExam_class())) {
				extXml = pacsExtMessageBL(compts, logname,gettokens);
			} else {
				extXml = pacsExtMessageJC(compts, logname,gettokens);
			}

			TranLogTxt.liswriteEror_to_txt(logname, "拼接PACS - extXml信息==" + extXml + "\r\n");
			// 诊疗项目收费登记
			String result = dams.HIPMessageServer2016Ext("AddActRequest", bufferXml.toString(), extXml);

			TranLogTxt.liswriteEror_to_txt(logname, "PACS返回结果==" + result + "\r\n");

			if ((result != null) && (result.trim().length() > 0)) {
				TranLogTxt.liswriteEror_to_txt(logname,
						"解析返回结果==" + QHResolveXML.getXmlResult(result, "MCCI_IN000002UV01", "typeCode") + "==="
								+ QHResolveXML.getXmlResult(result, "MCCI_IN000002UV01", "text"));
				// 解析申请单结果
				rhone.setTypeCode(QHResolveXML.getXmlResult(result, "MCCI_IN000002UV01", "typeCode"));
				rhone.setText(QHResolveXML.getXmlResult(result, "MCCI_IN000002UV01", "text"));
			}

		} catch (Exception ex) {
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}

		return rhone;

	}

	/**
	 * PACS 检查的
	 * @param gettokens 
	 * 
	 * @return
	 */
	private String pacsExtMessageJC(PacsComponents compts, String logname, String gettokens) {
		StringBuffer bufferXml = new StringBuffer();

		bufferXml.append("<REQUEST> ");
		bufferXml.append("  <!--用户在平台统一登录令牌--> ");
		bufferXml.append("<TOKENID>"+gettokens+"</TOKENID> ");
		bufferXml.append("  <SENDER/>   ");
		bufferXml.append("  <!--消息发送方在平台上的注册标识--> ");
		bufferXml.append("<REQ_MSGID>"+UUID.randomUUID().toString().replaceAll("-", "")+"</REQ_MSGID> ");
		bufferXml.append("  <!--请求消息ID,由消息发送方生成单次请求唯一的标识，建议用uuid 32位--> ");
		bufferXml.append("  <CLINIC_DESC>临床描述</CLINIC_DESC> ");

		bufferXml.append("  <!--诊断列表--> ");
		bufferXml.append("  <DIAG_LIST> ");
		bufferXml.append("    <!--诊断信息,1.*-->   ");

		String customerType = LISSendMessageQH.queryCustomerTypeQH(pacsmessage.getCustom().getExam_num());
		if ("健康体检".equals(customerType)) {
			bufferXml.append("      <DIAG_INFO>   ");
			bufferXml.append("        <SEQ_NO>1</SEQ_NO>");
			bufferXml.append("        <DIAG_CODE>Z10.100</DIAG_CODE>");
			bufferXml.append("        <DIAG_NMAE>健康体检</DIAG_NMAE>   ");
			bufferXml.append("        <DIAG_FNMAE>公共机构居民的常规一般性健康查体</DIAG_FNMAE>   ");
			bufferXml.append("      </DIAG_INFO>  ");

			bufferXml.append("  </DIAG_LIST>");
			bufferXml.append("  <!--检查禁忌--> ");
			bufferXml.append("  <EXM_TABOO_LIST>");
			bufferXml.append("    <EXM_TABOO_INFO>  ");
			bufferXml.append("      <SEQ_NO>1</SEQ_NO>  ");
			bufferXml.append("      <EXM_TABOO_CODE>Z10.100</EXM_TABOO_CODE>   ");
			bufferXml.append("      <EXM_TABOO_NAME>公共机构居民的常规一般性健康查体</EXM_TABOO_NAME> ");
			bufferXml.append("    </EXM_TABOO_INFO> ");
			bufferXml.append("  </EXM_TABOO_LIST>   ");
		} else {
			bufferXml.append("      <DIAG_INFO>   ");
			bufferXml.append("        <SEQ_NO>1</SEQ_NO>");
			bufferXml.append("        <DIAG_CODE>Z10.800</DIAG_CODE>");
			bufferXml.append("        <DIAG_NMAE>" + customerType + "</DIAG_NMAE>   ");
			bufferXml.append("        <DIAG_FNMAE>常规一般性健康查体，其他限定人群的</DIAG_FNMAE>   ");
			bufferXml.append("      </DIAG_INFO>  ");

			bufferXml.append("  </DIAG_LIST>");
			bufferXml.append("  <!--检查禁忌--> ");
			bufferXml.append("  <EXM_TABOO_LIST>");
			bufferXml.append("    <EXM_TABOO_INFO>  ");
			bufferXml.append("      <SEQ_NO>1</SEQ_NO>  ");
			bufferXml.append("      <EXM_TABOO_CODE>Z10.800</EXM_TABOO_CODE>   ");
			bufferXml.append("      <EXM_TABOO_NAME>常规一般性健康查体，其他限定人群的</EXM_TABOO_NAME> ");
			bufferXml.append("    </EXM_TABOO_INFO> ");
			bufferXml.append("  </EXM_TABOO_LIST>   ");
		}

		bufferXml.append("  <!--检查项目列表--> ");
		bufferXml.append("  <EXM_LIST>  ");

		for (PacsComponent pcs : compts.getPacsComponent()) {

			bufferXml.append("    <EXM_INFO>");
			bufferXml.append("      <SEQ_NO>1</SEQ_NO>  ");

			// String lisCode = getSpecimenItemPacs(pcs.getHis_num(),logname);
			String lisCode = getDeptType(compts.getPacsComponent().get(0).getExam_class());
			String[] lisCodeResult = lisCode.split("_");

			if (lisCode.length() > 3) {
				bufferXml.append("      <EXM_CLASS_CODE>" + lisCodeResult[0] + "</EXM_CLASS_CODE>");
				bufferXml.append("      <EXM_CLASS_NAME>" + lisCodeResult[1] + "</EXM_CLASS_NAME>   ");
			} else {
				bufferXml.append("      <EXM_CLASS_CODE>1</EXM_CLASS_CODE>");
				bufferXml.append("      <EXM_CLASS_NAME>检查项目</EXM_CLASS_NAME>   ");
			}

			bufferXml.append("      <EXM_SUBCLASS_CODE>001</EXM_SUBCLASS_CODE>");
			bufferXml.append("      <EXM_SUBCLASS_NAME>体检</EXM_SUBCLASS_NAME>   ");

			bufferXml.append("      <EXM_METH_CODE></EXM_METH_CODE>  ");
			bufferXml.append("      <EXM_METH_NAME></EXM_METH_NAME>   ");
			bufferXml.append("      <EXM_ITEM_CODE>" + pcs.getPacs_num() + "</EXM_ITEM_CODE>  ");
			bufferXml.append("      <EXM_ITEM_NAME>" + pcs.getItemName() + "</EXM_ITEM_NAME>   ");
			bufferXml.append("      <EXM_SITE_CODE></EXM_SITE_CODE>");
			bufferXml.append("      <EXM_SITE_NAME></EXM_SITE_NAME>   ");
			bufferXml.append("      <EXM_SITE_FNAME></EXM_SITE_FNAME> ");
			bufferXml.append("      <EXEC_DEPT_CODE>" + pcs.getServiceDeliveryLocation_code() + "</EXEC_DEPT_CODE>  ");
			bufferXml.append("      <EXEC_DEPT_NAME>" + pcs.getServiceDeliveryLocation_name() + "</EXEC_DEPT_NAME> ");
			bufferXml.append("    </EXM_INFO> ");
		}

		bufferXml.append("  </EXM_LIST> ");
		bufferXml.append("</REQUEST>");

		return bufferXml.toString();

	}

	/**
	 * 拼接pacs 病理 拼接XML
	 * 
	 * @param compts
	 * @param logname
	 * @param gettokens 
	 * @return
	 */
	private String pacsExtMessageBL(PacsComponents compts, String logname, String gettokens) {

		StringBuffer bufferXml = new StringBuffer();

		bufferXml.append(" <RESPONSE>   ");
		bufferXml.append(" <REQ_MSGID>"+gettokens+"</REQ_MSGID>  ");
		bufferXml.append(" <RESP_MSGID></RESP_MSGID>");
		bufferXml.append(" <CLINIC_DESC>临床描述</CLINIC_DESC>");
		bufferXml.append(" <APPL_ID>HA201102113366777</APPL_ID>   ");

		bufferXml.append("   <!--诊断列表-->");
		bufferXml.append("   <DIAG_LIST>");
		bufferXml.append("     <!--诊断信息,1.*-->  ");
		bufferXml.append("     <DIAG_INFO>  ");
		bufferXml.append("       <SEQ_NO>001</SEQ_NO> ");
		bufferXml.append("       <DIAG_CODE>TJ001</DIAG_CODE>");
		bufferXml.append("       <DIAG_NMAE>体检</DIAG_NMAE>");
		bufferXml.append("       <DIAG_FNMAE>体检信息</DIAG_FNMAE>  ");
		bufferXml.append("     </DIAG_INFO> ");
		bufferXml.append("   </DIAG_LIST>   ");

		bufferXml.append("   <INFECT_COND></INFECT_COND>  ");
		bufferXml.append("   <SPECIAL_REQ></SPECIAL_REQ>  ");
		bufferXml.append("   <SURGERY_VIS></SURGERY_VIS>  ");
		bufferXml.append("   <!--病理检查史-->");
		bufferXml.append("   <PATHOL_HST>   ");
		bufferXml.append("     <HST_DO_FLAG></HST_DO_FLAG>");
		bufferXml.append("     <HST_PATHOL_ORG></HST_PATHOL_ORG>   ");
		bufferXml.append("     <HST_PATHOL_NO></HST_PATHOL_NO> ");
		bufferXml.append("     <HST_PATHOL_DIAG></HST_PATHOL_DIAG>");
		bufferXml.append("     <HST_PATHOL_DT></HST_PATHOL_DT>");
		bufferXml.append("     <HST_XLP_NO></HST_XLP_NO> ");
		bufferXml.append("   </PATHOL_HST>  ");

		bufferXml.append("   <!--送检组织信息-->");
		bufferXml.append("   <SPEC_NAT_INFO>");
		bufferXml.append("     <SPEC_NAT_NAME></SPEC_NAT_NAME>");
		bufferXml.append("     <SPEC_NAT_QTY></SPEC_NAT_QTY> ");
		bufferXml.append("     <SPEC_NAT_UNIT></SPEC_NAT_UNIT>  ");
		bufferXml.append("   </SPEC_NAT_INFO> ");

		bufferXml.append("   <!--标本列表-->");
		bufferXml.append("   <SPEC_LIST>");
		bufferXml.append("     <!--标本信息,1.*-->  ");
		bufferXml.append("     <SPEC_INFO>  ");
		bufferXml.append("       <SEQ_NO>1</SEQ_NO> ");
		bufferXml.append("       <SPEC_NO>18111600005</SPEC_NO>   ");
		bufferXml.append("       <SPEC_PLACE>左肝方叶</SPEC_PLACE>");
		bufferXml.append("       <SPEC_NAME>左肝方叶</SPEC_NAME>  ");
		bufferXml.append("       <QTY>3</QTY> ");
		bufferXml.append("       <QTY_UNIT>块</QTY_UNIT>");
		bufferXml.append("       <CLCT_DT>201811161122000</CLCT_DT> ");
		bufferXml.append("       <FIX_DT>201811161122000</FIX_DT> ");
		bufferXml.append("     </SPEC_INFO> ");
		bufferXml.append("   </SPEC_LIST>   ");

		bufferXml.append("   <!--肿瘤信息-->");
		bufferXml.append("   <TUMOR_INFO>   ");
		bufferXml.append("     <TUMOR_FLAG></TUMOR_FLAG>  ");
		bufferXml.append("     <BODY_PARTS></BODY_PARTS>  ");
		bufferXml.append("     <SIZE></SIZE>");
		bufferXml.append("     <SHAPE></SHAPE>  ");
		bufferXml.append("     <MOB></MOB>");
		bufferXml.append("     <GROWTH></GROWTH>  ");
		bufferXml.append("     <FIRM></FIRM>");
		bufferXml.append("     <MET></MET>  ");
		bufferXml.append("     <RT></RT>");
		bufferXml.append("   </TUMOR_INFO>  ");

		bufferXml.append("   <!--妇科标本信息 -->   ");
		bufferXml.append("   <OG_SPEC_INFO> ");
		bufferXml.append("     <OG_FLAG></OG_FLAG>  ");
		bufferXml.append("     <LMP_DT></LMP_DT>");
		bufferXml.append("     <MENSES_VOL></MENSES_VOL>");
		bufferXml.append("     <PD_DU></PD_DU>");
		bufferXml.append("     <TREAT_HST></TREAT_HST>  ");
		bufferXml.append("     <TREAT_DT></TREAT_DT>  ");
		bufferXml.append("     <TREAT_DOSE></TREAT_DOSE>");
		bufferXml.append("     <DC_DT></DC_DT>  ");
		bufferXml.append("     <UPR_DESC></UPR_DESC>  ");
		bufferXml.append("     <LPA_DESC></LPA_DESC>");
		bufferXml.append("   </OG_SPEC_INFO>");

		bufferXml.append("   <!--检查项目列表-->");
		bufferXml.append("   <EXM_LIST> ");
		bufferXml.append("     <!--检查项目信息,1.*-->  ");

		for (PacsComponent pcs : compts.getPacsComponent()) {

			bufferXml.append("     <EXM_INFO>   ");
			bufferXml.append("       <SEQ_NO>1</SEQ_NO> ");
			// String lisCode = getSpecimenItemPacs(pcs.getHis_num(),logname);
			String lisCode = getDeptType(compts.getPacsComponent().get(0).getExam_class());
			String[] lisCodeResult = lisCode.split("_");

			if (lisCode.length() > 1) {
				bufferXml.append("       <EXM_CLASS_CODE>" + lisCodeResult[0] + "</EXM_CLASS_CODE> ");
				bufferXml.append("       <EXM_CLASS_NAME>" + lisCodeResult[1] + "</EXM_CLASS_NAME>");
			} else {
				bufferXml.append("       <EXM_CLASS_CODE>1</EXM_CLASS_CODE> ");
				bufferXml.append("       <EXM_CLASS_NAME>病理项目</EXM_CLASS_NAME>");
			}
			bufferXml.append("       <EXM_SUBCLASS_CODE>001</EXM_SUBCLASS_CODE>   ");
			bufferXml.append("       <EXM_SUBCLASS_NAME>体检</EXM_SUBCLASS_NAME>");

			bufferXml.append("       <EXM_METH_CODE></EXM_METH_CODE> ");
			bufferXml.append("       <EXM_METH_NAME></EXM_METH_NAME>");
			bufferXml.append("       <EXM_ITEM_CODE>" + pcs.getItemCode() + "</EXM_ITEM_CODE> ");
			bufferXml.append("       <EXM_ITEM_NAME>" + pcs.getItemName() + "</EXM_ITEM_NAME>");
			bufferXml.append("       <EXM_SITE_CODE></EXM_SITE_CODE> ");
			bufferXml.append("       <EXM_SITE_NAME></EXM_SITE_NAME>");
			bufferXml.append("       <EXM_SITE_FNAME></EXM_SITE_FNAME>");
			bufferXml.append("       <EXEC_DEPT_CODE>" + pcs.getServiceDeliveryLocation_code() + "</EXEC_DEPT_CODE> ");
			bufferXml.append("       <EXEC_DEPT_NAME>" + pcs.getServiceDeliveryLocation_name() + "</EXEC_DEPT_NAME>");
			bufferXml.append("       <PRICE>"+pcs.getItemprice()+"</PRICE>");
			bufferXml.append("     </EXM_INFO>  ");

		}

		bufferXml.append("   </EXM_LIST>");
		bufferXml.append("</RESPONSE>   ");

		return bufferXml.toString();

	}

	/**
	 * 
	 * @param id
	 * @return 标本类型
	 * @throws ServiceException
	 */
	private String getSpecimenItemPacs(String LisCode, String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		String lisitem = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select  sd.demo_name,ci.item_code from charging_item  ci ,"
					+ "  sample_demo sd   where  sam_demo_id=sd.id and his_num='" + LisCode + "' ";

			TranLogTxt.liswriteEror_to_txt(logname, "=== sql  ==" + sb1 + " \r\n");

			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitem = rs1.getString("demo_name") + "_" + rs1.getString("item_code");
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisitem;
	}

	/*
	 * 核医学 NM
	 * 
	 * 体检放射 FS
	 * 
	 * 介入超声 US Ｂ超 US 彩超 US
	 * 
	 * 阴道镜 YDJ 内窥镜 NJ 内镜 NJ 胃镜 NJ
	 * 
	 * CT CT 放射 FS 磁共振 MR 病理 BL 介入治疗 XA
	 * 
	 */

	/**
	 * 
	 * @param exam_class
	 * @return
	 */
	private static String getDeptType(String exam_class) {
		String deptType = "";
		if ("CT".equals(exam_class)) {
			deptType = "CT_CT";
		} else if ("DX".equals(exam_class)) {
			deptType = "FS_放射";
		} else if ("US".equals(exam_class) || "XGNS".equals(exam_class)) {
			deptType = "US_彩超";
		} else if ("NJ".equals(exam_class)) {
			deptType = "NJ_胃镜";
		} else if ("MR".equals(exam_class)) {
			deptType = "MR_磁共振";
		} else if ("ECG".equals(exam_class)) {
			deptType = "ECG_心电图";
		} else if ("BL".equals(exam_class)) {
			deptType = "BL_病理";
		}

		return deptType;
	}

}
