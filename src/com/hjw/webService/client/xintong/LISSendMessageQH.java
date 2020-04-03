package com.hjw.webService.client.xintong;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class LISSendMessageQH {
	
	private LisMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	static {
		init();
	}
	
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}

	public LISSendMessageQH(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "lis===:" + str);
		try {			
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
				ResultHeader rhone= new ResultHeader();
				TranLogTxt.liswriteEror_to_txt(logname, "====参数URL===:" + url+"===参数comps===:" +comps);
				rhone=this.lisQHSendMessage(url, comps, logname);
				TranLogTxt.liswriteEror_to_txt(logname, "rhone.getTypeCode()===:" + rhone.getTypeCode());
				if("AA".equals(rhone.getTypeCode())){
					rb.getResultHeader().setTypeCode("AA");
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
				}else {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(rhone.getText());
				}
			}
			//rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}
	
	
	
	/**
	 *  拼接 检验LIS xml信息
	 * @param comps
	 * @param logname
	 * @return
	 */
	private ResultHeader lisQHSendMessage(String url,LisComponents comps,String logname) {
		
		ResultHeader rhone= new ResultHeader();
		try {
			StringBuffer bufferXml = new StringBuffer();
			
			bufferXml.append(" <POOR_IN200901UV xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ITSVersion=\"XML_1.0\" xsi:schemaLocation=\"urn:hl7-org:v3 ../multicacheschemas/POOR_IN200901UV.xsd\">  ");
			bufferXml.append(" <id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toUpperCase()+"\"/>");
			bufferXml.append("	<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>  ");
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
			bufferXml.append("			<id root=\"1.2.840.114350.1.13.999.234\"  extension=\"HJW\"/>  ");
			bufferXml.append("		</device> ");
			bufferXml.append("	</sender> ");
			bufferXml.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\"> ");
			bufferXml.append("		<code code=\"POOR_TE200901UV\" codeSystem=\"2.16.840.1.113883.1.6\"/>   ");
			bufferXml.append("		<subject typeCode=\"SUBJ\"> ");
			bufferXml.append("			<observationRequest classCode=\"OBS\" moodCode=\"RQO\">   ");
			bufferXml.append("				<!--电子申请单编号-->   ");
			bufferXml.append("				<id root=\"2.16.156.10011.1.24\" extension=\""+comps.getReq_no()+"\"/>   ");
			bufferXml.append("				<!--申请单类型--> ");
			bufferXml.append("				<code/>");
			bufferXml.append("				<!--申请单项目内容-->   ");
			bufferXml.append("				<text>02</text> ");
			bufferXml.append("				<!--申请单状态--> ");
			bufferXml.append("				<statusCode code=\"active\"/> ");
			bufferXml.append("				<!--申请单有效期间-->   ");
			bufferXml.append("				<effectiveTime xsi:type=\"IVL_TS\"> ");
			bufferXml.append("					<!--申请单计划开始日期时间--> ");
			bufferXml.append("					<low value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
			bufferXml.append("					<!--申请单计划结束日期时间--> ");
			bufferXml.append("					<high value=\""+DateTimeUtil.DateAdd(30)+"000000\"/>");
			bufferXml.append("				</effectiveTime>");
			bufferXml.append("				<!--优先（紧急）度-->   ");
			bufferXml.append("				<priorityCode code=\"R\" displayName=\"routine\" codeSystem=\"2.16.840.1.113883.1.11.16866\" codeSystemName=\"ActPriority\"/> ");
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
			bufferXml.append("								<additive3 classCode=\"MAT\" determinerCode=\"INSTANCE\">   ");
			bufferXml.append("									<!--标本固定液名称--> ");
			bufferXml.append("									<desc xsi:type=\"ST\"></desc> ");
			bufferXml.append("								</additive3>");
			bufferXml.append("							</additive> ");
			bufferXml.append("						</specimenNatural>  ");
			bufferXml.append("						<productOf typeCode=\"PRD\">");
			bufferXml.append("							<!--标本采样日期时间-->   ");
			bufferXml.append("							<time value=\""+DateTimeUtil.getDateTimes()+"\"/>");
			bufferXml.append("							<!--标本接收--> ");
			bufferXml.append("							<specimenProcessStep classCode=\"ACSN\" moodCode=\"EVN\">   ");
			bufferXml.append("								<!--接收标本日期时间--> ");
			bufferXml.append("								<effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/> ");
			bufferXml.append("							</specimenProcessStep>");
			bufferXml.append("						</productOf>");
			bufferXml.append("					</specimen> ");
			bufferXml.append("				</specimen> ");
			bufferXml.append("				<!--记录对象--> ");
			bufferXml.append("				<recordTarget typeCode=\"RCT\" contextControlCode=\"OP\">   ");
			bufferXml.append("					<patient classCode=\"PAT\"> ");
			
			/*bufferXml.append("						<!--门（急）诊号标识 -->   ");
			bufferXml.append("                      <id root=\"2.16.156.10011.1.10\" extension=\"E10000000\"/>");
			bufferXml.append("                      <!--住院号标识--> ");
			bufferXml.append("                      <id root=\"2.16.156.10011.1.12\" extension=\"HA201102113366666\"/>  ");            
			*/
			bufferXml.append("                      <!--个人信息--> ");
			bufferXml.append("                      <id root=\"2.16.156.10011.0.2.2\" extension=\""+lismessage.getCustom().getExam_num()+"\"/>  ");            
			
			bufferXml.append("					</patient>  ");
			bufferXml.append("				</recordTarget> ");
			bufferXml.append("				<!--申请单开立者--> ");
			bufferXml.append("				<author typeCode=\"AUT\" contextControlCode=\"OP\">   ");
			bufferXml.append("					<!--申请单开立日期时间--> ");
			bufferXml.append("					<time value=\""+DateTimeUtil.getDateTimes()+"\"/>");
			bufferXml.append("					<signatureCode code=\"S\"/> ");
			bufferXml.append("					<!--申请单开立者签名--> ");
			bufferXml.append("					<signatureText>"+lismessage.getDoctor().getDoctorName()+"</signatureText> ");
			bufferXml.append("					<assignedEntity classCode=\"ASSIGNED\"> ");
			bufferXml.append("						<!--医务人员ID-->   ");
			bufferXml.append("						<id root=\"2.16.156.10011.1.4\" extension=\""+lismessage.getDoctor().getDoctorCode()+"\"/>  ");
			bufferXml.append("						<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">  ");
			bufferXml.append("							<!--申请单开立者-->   ");
			bufferXml.append("							<name>"+lismessage.getDoctor().getDoctorName()+"</name>   ");
			bufferXml.append("						</assignedPerson>   ");
			bufferXml.append("						<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">   ");
			bufferXml.append("							<!--医疗卫生机构（科室）ID--> ");
			bufferXml.append("							<id root=\"2.16.156.10011.1.26\" extension=\""+lismessage.getDoctor().getDept_code()+"\"/> ");
			bufferXml.append("							<!--申请单开立科室--> ");
			bufferXml.append("							<name>"+lismessage.getDoctor().getDept_name()+"</name> ");
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
			bufferXml.append("						<id root=\"2.16.156.10011.1.4\" extension=\"120109197706015518\"/>  ");
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
			bufferXml.append("				<reason typeCode=\"RSON\" contextControlCode=\"OP\" contextConductionInd=\"true\">  ");
			bufferXml.append("					<!--现病史--> ");
			bufferXml.append("					<observation classCode=\"OBS\" moodCode=\"EVN\">  ");
			bufferXml.append("						<code code=\"DE04.01.119.00\" displayName=\"主诉\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>");
			bufferXml.append("						<value xsi:type=\"ST\"></value> ");
			bufferXml.append("					</observation>");
			bufferXml.append("				</reason>   ");
			bufferXml.append("				<reason typeCode=\"RSON\" contextControlCode=\"OP\" contextConductionInd=\"true\">  ");
			bufferXml.append("					<!--症状描述--> ");
			bufferXml.append("					<observation classCode=\"OBS\" moodCode=\"EVN\">  ");
			bufferXml.append("						<code code=\"DE04.01.117.00\" displayName=\"症状描述\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>");
			bufferXml.append("						<value xsi:type=\"ST\"></value> ");
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
			bufferXml.append("								<id root=\"2.16.156.10011.1.4\" extension=\""+lismessage.getDoctor().getDoctorCode()+"\"/>");
			bufferXml.append("								<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">");
			bufferXml.append("									<!--申请单开立者--> ");
			bufferXml.append("									<name>"+lismessage.getDoctor().getDoctorName()+"</name> ");
			bufferXml.append("								</assignedPerson> ");
			bufferXml.append("								<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
			bufferXml.append("									<!--医疗卫生机构（科室）ID--> ");
			bufferXml.append("									<id root=\"2.16.156.10011.1.26\" extension=\"LIS20160815\"/>   ");
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
			bufferXml.append("								<id root=\"2.16.156.10011.1.10\" extension=\"E10000000\"/>  ");
			bufferXml.append("								<!--住院号标识--> ");
			bufferXml.append("								<id root=\"2.16.156.10011.1.12\" extension=\"HA201102113366666\"/>");
			bufferXml.append("								<!--患者就医联系电话--> ");
			bufferXml.append("								<telecom/>  ");
			bufferXml.append("								<!--患者角色状态-->   ");
			bufferXml.append("								<statusCode code=\"active\"/>   ");
			bufferXml.append("								<patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");
			bufferXml.append("									<!--患者身份证号--> ");
			if(!"".equals(lismessage.getCustom().getPersonidnum()) && lismessage.getCustom().getPersonidnum()!=null) {
				bufferXml.append("									<id root=\"2.16.156.10011.1.3\" extension=\""+ lismessage.getCustom().getPersonidnum()+"\"/>");
			}else {
				bufferXml.append("									<id root=\"2.16.156.10011.1.3\" />");
			}
			bufferXml.append("									<name use=\"L\">" + lismessage.getCustom().getName() + "</name>   ");
			bufferXml.append("									<!--性别-->																														");
			
			TranLogTxt.liswriteEror_to_txt(logname, "性别代码==" + lismessage.getCustom().getSexcode() + "性别=="+lismessage.getCustom().getSexname());
			
			String SexCode="";
			if("男".equals(lismessage.getCustom().getSexname())){
				SexCode="1";
				
			}else if("女".equals(lismessage.getCustom().getSexname())){
				SexCode="2";
			}else{
				SexCode="0";
			}
			
			bufferXml.append("									<administrativeGenderCode code=\""+SexCode+"\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""+lismessage.getCustom().getSexname()+"性"+"\" codeSystemName=\"生理性别代码表（GB/T 2261.1）\"/>  ");
			bufferXml.append("									<!--出生日期--> ");
			bufferXml.append("									<birthTime value=\""+lismessage.getCustom().getBirthtime()+"\"/> ");
			bufferXml.append("									<asOtherIDs classCode=\"ROL\">");
			bufferXml.append("										<!--健康档案编号--> ");
			if(!"".equals(lismessage.getCustom().getArch_num()) && lismessage.getCustom().getArch_num()!=null) {
				bufferXml.append("										<id root=\"2.16.156.10011.1.2\" extension=\""+lismessage.getCustom().getArch_num()+"\"/>   ");
			}else {
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
			bufferXml.append("</POOR_IN200901UV>     																																		");
			
			TranLogTxt.liswriteEror_to_txt(logname, "拼接LIS - XML信息==" + bufferXml.toString() + "\r\n");
			//发送请求信息
			HIPMessageServiceService dam = new HIPMessageServiceServiceLocator(url);
			IHIPMessageService dams = dam.getHIPMessageServicePort();
			String gettokens = Gettoken.Gettokens(url,logname);
			String extXml = extMessage(logname,gettokens);
			
			TranLogTxt.liswriteEror_to_txt(logname, "拼接LIS - extXml信息==" + extXml + "\r\n");
			//诊疗项目收费登记
			String result = dams.HIPMessageServer2016Ext("AddActRequest", bufferXml.toString(), extXml);
			
			TranLogTxt.liswriteEror_to_txt(logname, "LIS返回结果==" + result + "\r\n");
			
			if ((result != null) && (result.trim().length() > 0)) {
				TranLogTxt.liswriteEror_to_txt(logname, "解析返回结果==" + QHResolveXML.getXmlResult(result,"MCCI_IN000002UV01","typeCode")+"==="+QHResolveXML.getXmlResult(result,"MCCI_IN000002UV01","text"));
				//解析申请单结果
				rhone.setTypeCode(QHResolveXML.getXmlResult(result,"MCCI_IN000002UV01","typeCode"));
				rhone.setText(QHResolveXML.getXmlResult(result,"MCCI_IN000002UV01","text"));
			}
			
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
	
		return rhone;
		
	}
	
	/**
	 * LIS第三参数信息
	 * @param gettokens 
	 * @return
	 */
	private String extMessage(String logname, String gettokens) {
		StringBuffer bufferXml = new StringBuffer();
		
		bufferXml.append("<REQUEST>   ");
		bufferXml.append("<TOKENID>"+gettokens+"</TOKENID> ");
		bufferXml.append("<SENDER></SENDER>   ");
		bufferXml.append("<REQ_MSGID>"+UUID.randomUUID().toString().replaceAll("-", "")+"</REQ_MSGID> ");
		bufferXml.append("    <CLINIC_DESC>临床描述</CLINIC_DESC>   ");
		bufferXml.append("    <!--诊断列表--> ");
		bufferXml.append("    <DIAG_LIST> ");
		bufferXml.append("      <!--诊断列表,1.*--> ");
		
		String customerType = LISSendMessageQH.queryCustomerTypeQH(lismessage.getCustom().getExam_num());
		if("健康体检".equals(customerType)){
			bufferXml.append("      <DIAG_INFO>   ");
			bufferXml.append("        <SEQ_NO>1</SEQ_NO>");
			bufferXml.append("        <DIAG_CODE>Z10.100</DIAG_CODE>");
			bufferXml.append("        <DIAG_NMAE>健康体检</DIAG_NMAE>   ");
			bufferXml.append("        <DIAG_FNMAE>公共机构居民的常规一般性健康查体</DIAG_FNMAE>   ");
			bufferXml.append("      </DIAG_INFO>  ");
		}else{
			bufferXml.append("      <DIAG_INFO>   ");
			bufferXml.append("        <SEQ_NO>1</SEQ_NO>");
			bufferXml.append("        <DIAG_CODE>Z10.800</DIAG_CODE>");
			bufferXml.append("        <DIAG_NMAE>"+customerType+"</DIAG_NMAE>   ");
			bufferXml.append("        <DIAG_FNMAE>常规一般性健康查体，其他限定人群的</DIAG_FNMAE>   ");
			bufferXml.append("      </DIAG_INFO>  ");
		}
		
		bufferXml.append("    </DIAG_LIST>");
		
		bufferXml.append("    <!--标本列表--> ");
		bufferXml.append("    <SPEC_LIST> ");
		bufferXml.append("       <!--标本信息,1.*-->");
		
		List<LisComponents> components = lismessage.getComponents();
		for (int i = 0; i < components.size(); i++) {
			LisComponents liscoms = new LisComponents();
			liscoms = components.get(i);
			bufferXml.append("       <SPEC_INFO>  ");
			String lisCode = getSpecimenItem(liscoms.getItemList().get(i).getHis_num());
			
			String[] lisCodeResult = lisCode.split("_");
			if(lisCode.length()>3) {
				TranLogTxt.liswriteEror_to_txt(logname, "===lisCode==" + lisCode +"===lisCode 1=="+lisCodeResult[1]+"===lisCode 0=="+lisCodeResult[0]+"\r\n");
				bufferXml.append("          <LAB_CLASS_CODE>"+lisCodeResult[1]+"</LAB_CLASS_CODE> ");
				bufferXml.append("          <LAB_CLASS_NAME>"+lisCodeResult[0]+"</LAB_CLASS_NAME> ");
			}else {
				bufferXml.append("          <LAB_CLASS_CODE>1</LAB_CLASS_CODE> ");
				bufferXml.append("          <LAB_CLASS_NAME>检验项目</LAB_CLASS_NAME> ");
			}
			bufferXml.append("          <SPEC_TYPE_CODE>"+liscoms.getItemList().get(0).getSpecimenNatural()+"</SPEC_TYPE_CODE> ");
			bufferXml.append("          <SPEC_TYPE_NAME>"+liscoms.getItemList().get(0).getSpecimenNaturalname()+"</SPEC_TYPE_NAME>   ");
			bufferXml.append("          <SPEC_NO>1</SPEC_NO> ");
			bufferXml.append("          <!--检验项目列表--> ");
			bufferXml.append("          <LAB_LIST>");

			List<LisComponent> component = liscoms.getItemList();
			for (int j = 0; j < component.size(); j++) {
				int lisid=updatezl_req_item(lismessage.getCustom().getExam_num(),liscoms.getReq_no(),component.get(j).getChargingItemid(),logname);
				TranLogTxt.liswriteEror_to_txt(logname, "插入zl_req_item表==" + lisid + "\r\n");
				if(lisid>0){
				bufferXml.append("             <!--检验项目信息,1.*-->");
				bufferXml.append("             <LAB_INFO> ");
				bufferXml.append("                <SEQ_NO>"+j+"</SEQ_NO>");
				bufferXml.append("                <APPL_NO>"+liscoms.getReq_no()+"</APPL_NO> ");
				bufferXml.append("                <LAB_ITEM_CODE>"+component.get(j).getItemCode()+"</LAB_ITEM_CODE> ");
				bufferXml.append("                <LAB_ITEM_NAME>"+component.get(j).getItemName()+"</LAB_ITEM_NAME>");
				bufferXml.append("                <LAB_METH_CODE></LAB_METH_CODE>   ");
				bufferXml.append("                <LAB_METH_NAME></LAB_METH_NAME>");
				bufferXml.append("                <EXEC_DEPT_CODE>"+component.get(j).getServiceDeliveryLocation_code()+"</EXEC_DEPT_CODE>");
				bufferXml.append("                <EXEC_DEPT_NAME>"+component.get(j).getServiceDeliveryLocation_name()+"</EXEC_DEPT_NAME> ");
				bufferXml.append("                <PRICE>"+component.get(j).getItemprice()+"</PRICE>");
				bufferXml.append("                <IS_DST>2</IS_DST>");
				bufferXml.append("             </LAB_INFO>");
				}
			}
		}
		
		bufferXml.append("          </LAB_LIST>   ");
		bufferXml.append("       </SPEC_INFO> ");
		bufferXml.append("    </SPEC_LIST>");
		bufferXml.append(" </REQUEST> ");
	
		return bufferXml.toString();
	}
	
	
	
	
	/**
	 * 
	 * @param id
	 * @return 标本类型
	 * @throws ServiceException
	 */
	private String getSpecimenItem(String LisCode) throws ServiceException {
		Connection tjtmpconnect = null;
		String lisitem = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select  sd.demo_name,ci.item_code from charging_item  ci ," + 
					"  sample_demo sd   where  sam_demo_id=sd.id and his_num='"+LisCode+"' ";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitem = rs1.getString("demo_name")+"_"+rs1.getString("item_code");
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
	
	
	/**
	 * 
	 * @param exam_num
	 * @return 根据体检号 查询人员体检类型
	 * @throws ServiceException
	 */
	public static String queryCustomerTypeQH(String exam_num) throws ServiceException {
		Connection tjtmpconnect = null;
		String data_name = "";
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String sb1 = " select data_name from data_dictionary where id = (select customer_type from exam_info where exam_num = '"+exam_num+"') ";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				data_name = rs1.getString("data_name");
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
		return data_name;
	} 
	
	
	public int updatezl_req_item(String exam_num,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		ZlReqPatinfoDTO zlp = new ZlReqPatinfoDTO();
		zlp=getzl_patinfoFromNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_id='"+ciid+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,zl_pat_id,lis_item_id,req_id,lis_req_code,createdate) values('" 
				+ ei.getId() + "','" +ciid + "','" +zlp.getZl_pat_id() + "','"+2+"','"+req_id+"','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +insertsql);				
				preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.executeUpdate();
				ResultSet rs = null;
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next())
					
					lisid = rs.getInt(1);
				
				rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}
	
	//根据体检号查询zl_reqpatinfo表信息
		private ZlReqPatinfoDTO getzl_patinfoFromNum(String exam_num){
			StringBuffer sb = new StringBuffer();
			sb.append("select * from zl_req_patInfo where exam_num= '" + exam_num + "'");
			PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1,10000,ZlReqPatinfoDTO.class);
			ZlReqPatinfoDTO zlreq = new ZlReqPatinfoDTO();
			
			if((map!=null)&&(map.getList().size()>0)){
				zlreq= (ZlReqPatinfoDTO)map.getList().get(0);			
			}
			return zlreq;
			
		}
	
	private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
}
