package com.hjw.webService.client.xintong;

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

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.xintong.client.HIPMessageServiceService;
import com.hjw.webService.client.xintong.client.HIPMessageServiceServiceLocator;
import com.hjw.webService.client.xintong.client.IHIPMessageService;
import com.hjw.webService.client.xintong.lis.ResLisMessageQH;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class LISDELSendMessageQH {
	
	private LisMessageBody lismessage;
	private static ConfigService configService;
    private static JdbcQueryManager jdbcQueryManager;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageQH(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	
	public ResultLisBody getMessage(String url, String logname) {
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
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}
	
	
	
	
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
			bufferXml.append("			<id root=\"1.2.840.114350.1.13.999.234\"   extension=\"HJW\" />  ");
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
			bufferXml.append("                      <id root=\"2.16.156.10011.1.10\" extension=\"E10000000\"/>");
			bufferXml.append("                      <!--住院号标识--> ");
			bufferXml.append("                      <id root=\"2.16.156.10011.1.12\" extension=\"HA201102113366666\"/>  ");            
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
			bufferXml.append("									<id root=\"2.16.156.10011.1.26\" extension=\""+lismessage.getDoctor().getDept_code()+"\"/>   ");
			bufferXml.append("									<!--申请单开立科室--> ");
			bufferXml.append("									<name>"+lismessage.getDoctor().getDept_name()+"</name> ");
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
			
			bufferXml.append("									<administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" codeSystem=\"2.16.156.10011.2.3.3.4\" displayName=\""+lismessage.getCustom().getSexname()+"\" codeSystemName=\"生理性别代码表（GB/T 2261.1）\"/>  ");
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
			
			List<LisComponents> components = lismessage.getComponents();
			
			for (int i = 0; i < components.size(); i++) {
				String extXml = extMessage(logname,components.get(i).getReq_no());
				TranLogTxt.liswriteEror_to_txt(logname, "拼接LIS - extXml信息==" + extXml + "\r\n");
				String result = dams.HIPMessageServer2016Ext("XT-G0021", "", extXml);
				//诊疗项目收费登记
				
				TranLogTxt.liswriteEror_to_txt(logname, "LIS返回结果==" + result + "\r\n");
				
				if ((result != null) && (result.trim().length() > 0)) {
					//解析申请单结果
					TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销11==" +"======解析申请单撤销=======" + "\r\n");
					
					 rhone = delresmessage(logname,result);
					 
					TranLogTxt.liswriteEror_to_txt(logname, "LIS撤销返回结果code44==111" + rhone.getTypeCode() + "\r\n");
					TranLogTxt.liswriteEror_to_txt(logname, "LIS撤销返回结果code44==111" + rhone.getText() + "\r\n");
					if(rhone.getTypeCode().equals("1")){
						rhone.setTypeCode("AA");
						TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销55==111" +rhone.getTypeCode() + "\r\n");
						TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销55==111" +rhone.getText() + "\r\n");
						
					}else{
						rhone.setTypeCode("AE");
						TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销55==222" +rhone.getTypeCode() + "\r\n");
						TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销55==222" +rhone.getText() + "\r\n");
						
					}
					
				}
				
			}
			
			
			
			
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
	
		return rhone;
		
	}


//解析lis撤销申请  响应信息的xml
public ResultHeader delresmessage(String logname, String result) throws Exception {
	
	TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销22==" + "解析申请单撤销22 "+ "\r\n");
	ResultHeader rh= new ResultHeader();
	rh.setTypeCode("AE");
	try{
	InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
	//Map<String, String> xmlMap = new HashMap<>();
	//xmlMap.put("abc", "urn:hl7-org:v3");
	SAXReader sax = new SAXReader();
	//sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
	Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
	
	
	
	rh.setTypeCode(document.selectSingleNode("RESPONSE/RETURN_CODE").getText());// 获取消息成功失败 节点;
	rh.setText(document.selectSingleNode("RESPONSE/RETURN_MSG").getText());//获取消息  描述节点
	rh.setSourceMsgId(document.selectSingleNode("RESPONSE/REQ_MSGID").getText());//获取请求消息id 节点
	
	TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销22==111" + rh.getTypeCode()+ "\r\n");
	TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销22==222" + rh.getText()+ "\r\n");
	
	}catch(Exception ex){
		rh.setTypeCode("AE");
		rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
	}
   
	TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销33---111==" +rh.getTypeCode() + "\r\n");
	TranLogTxt.liswriteEror_to_txt(logname, "解析申请单撤销33---222==" +rh.getText() + "\r\n");
    return rh;
}


private String extMessage(String logname,String req_no) {
	StringBuffer bufferXml = new StringBuffer();
	
	/*bufferXml.append("<REQUEST>   ");
	bufferXml.append("<TOKENID></TOKENID> ");
	bufferXml.append("<SENDER></SENDER>   ");
	bufferXml.append("<REQ_MSGID>"+UUID.randomUUID().toString().replaceAll("-", "")+"</REQ_MSGID> ");
	bufferXml.append("    <CLINIC_DESC>临床描述</CLINIC_DESC>   ");
	bufferXml.append("    <!--诊断列表--> ");
	bufferXml.append("    <DIAG_LIST> ");
	bufferXml.append("      <!--诊断列表,1.*--> ");
	bufferXml.append("      <DIAG_INFO>   ");
	bufferXml.append("        <SEQ_NO>001</SEQ_NO>");
	bufferXml.append("        <DIAG_CODE>TJ001</DIAG_CODE>");
	bufferXml.append("        <DIAG_NMAE>体检</DIAG_NMAE>   ");
	bufferXml.append("        <DIAG_FNMAE>体检信息</DIAG_FNMAE>   ");
	bufferXml.append("      </DIAG_INFO>  ");
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
			bufferXml.append("                <IS_DST>2</IS_DST>");
			bufferXml.append("             </LAB_INFO>");
		
		}
	}
	
	bufferXml.append("          </LAB_LIST>   ");
	bufferXml.append("       </SPEC_INFO> ");
	bufferXml.append("    </SPEC_LIST>");
	bufferXml.append(" </REQUEST> ");*/

	String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
	String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生名称
	String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//开单科室id
	String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//开单科室名称
	
		bufferXml.append("<REQUEST>                                                    ");
		bufferXml.append("	<!--接入认证token  未开启认证则传空 值 -->                 ");
		bufferXml.append("	<TOKENID></TOKENID>        ");
		bufferXml.append("	<!-- 发送系统代码 -->                                      ");
		bufferXml.append("	<SENDER>HJW</SENDER>                                       ");
		bufferXml.append("	<!--请求消息ID  -->                                        ");
		bufferXml.append("	<REQ_MSGID>"+UUID.randomUUID().toString().replaceAll("-", "")+"</REQ_MSGID>    ");
		bufferXml.append("	<REQ_PARAMS>                                               ");
		bufferXml.append("		<!-- 需要更新的业务编号 -->                            ");
		bufferXml.append("		<ACTION>AddActRequest</ACTION>                         ");
		bufferXml.append("		<!-- 具体发往的系统名称 -->                            ");
		bufferXml.append("		<SYSTEM>LIS</SYSTEM>                                   ");
		bufferXml.append("		<!--具体业务号:唯一标识号,申请单号,就诊登记号等  -->   ");
		bufferXml.append("		<BUSINESS_NO>"+req_no+"</BUSINESS_NO>                 ");
		bufferXml.append("		<!--业务状态 execute:执行 active:有效,cancel:撤销-->   ");
		bufferXml.append("		<BUSINESS_STATUS>cancel</BUSINESS_STATUS>             ");
		bufferXml.append("		<!-- 执行科室ID -->                                    ");
		bufferXml.append("		<EXEC_DEPT_ID>"+kddepid+"</EXEC_DEPT_ID>                      ");
		bufferXml.append("		<!--执行科室名称  -->                                  ");
		bufferXml.append("		<EXEC_DEPT_NAME>"+kddepname+"</EXEC_DEPT_NAME>              ");
		bufferXml.append("		<!-- 执行医生ID -->                                    ");
		bufferXml.append("		<EXEC_EMP_ID>"+doctorid+"</EXEC_EMP_ID>                   ");
		bufferXml.append("		<!-- 执行医生姓名 -->                                  ");
		bufferXml.append("		<EXEC_EMP_NAME>"+doctorname+"</EXEC_EMP_NAME>                    ");
		bufferXml.append("	</REQ_PARAMS>                                              ");
		bufferXml.append("</REQUEST>                                                   ");

	
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
				"  sample_demo sd   where  sam_demo_id=sd.id and exam_num='"+LisCode+"' ";
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

}
