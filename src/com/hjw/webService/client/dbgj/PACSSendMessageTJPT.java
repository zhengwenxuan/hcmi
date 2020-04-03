package com.hjw.webService.client.dbgj;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageServiceLocator;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSSendMessageTJPT{
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}
	public PACSSendMessageTJPT(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	
	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String logname,boolean debug) {
		String xml = "";
		ResultPacsBody rb = new ResultPacsBody();
		if (!debug) {
			xml = lisSendMessage_test();
		} else {
			xml = lisSendMessage();
		}		
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+lismessage.getMessageid()+":"+xml);
			try {
				DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
				DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
				String messages = dams.acceptMessage(xml);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+lismessage.getMessageid()+":"+messages);
				try {
					rb = JaxbUtil.converyToJavaBean(messages, ResultPacsBody.class);
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("pacs解析返回值错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("pacs调用webservice错误");
			}

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}
		return rb;
	}
			

	private String lisSendMessage() {
		StringBuffer sb = new StringBuffer("");

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xsi:schemaLocation=\"urn:hl7-org:v3 ../../Schemas/POOR_IN200901UV20.xsd\">");
		// <!-- 消息ID -->
		sb.append("<id extension=\"" + lismessage.getId_extension() + "\" />");
		// <!-- 消息创建时间 -->
		sb.append("<creationTime value=\"" + lismessage.getCreationTime_value() + "\" />");
		// <!-- 交互ID -->
		sb.append("<interactionId root=\"\" extension=\""+lismessage.getMessageid()+"\" />");
		// <!-- 消息用途: P(Production); D(Debugging); T(Training) -->
		sb.append("<processingCode code=\"" + lismessage.getProcessingCode_code() + "\" />");
		// <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive);
		// T(Current processing) -->
		sb.append("<processingModeCode code=\"" + lismessage.getProcessingMode_Code() + "\" />");
		// <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->
		sb.append("<acceptAckCode code=\"" + lismessage.getAcceptAckCode_code() + "\" />");
		// <!-- 接受者 -->
		sb.append("<receiver typeCode=\"RCV\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		// <!-- 接受者ID -->
		sb.append("<id />");
		sb.append("</device>");
		sb.append("</receiver>");
		// <!-- 发送者 -->
		sb.append("<sender typeCode=\"SND\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		// <!-- 发送者ID -->
		sb.append("<id />");
		sb.append("</device>");
		sb.append("</sender>");
		// <!-- 封装的消息内容(按Excel填写) -->
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">");
		// <!-- 消息交互类型 @code: 新增 :new 删除：delete-->
		sb.append("<code code=\"new\"></code>");
		sb.append("<subject typeCode=\"SUBJ\" xsi:nil=\"false\">");
		sb.append("<placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">");
		sb.append("<subject typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\">");
		sb.append("<id>");
		// <!-- 域ID -->
		sb.append("<item root=\"1.2.156.112606.1.2.1.2\" extension=\"01\" />");
		// <!-- 患者ID -->
		sb.append(
				"<item root=\"1.2.156.112606.1.2.1.3\" extension=\"" + lismessage.getCustom().getExam_num() + "\" />");
		// <!-- 就诊号 -->
		sb.append(
				"<item root=\"1.2.156.112606.1.2.1.12\" extension=\""+lismessage.getCustom().getPersonno()+"\" />");
		sb.append(
				"<item root=\"exam_num\" extension=\""+lismessage.getCustom().getExam_num()+"\" />");
		sb.append(
				"<item root=\"exam_vip\" extension=\""+lismessage.getCustom().getVipflag()+"\" />");
		sb.append(
				"<item root=\"file_no\" extension=\""+lismessage.getCustom().getArch_num()+"\" />");
		sb.append("</id>");
		// <!-- 病区编码/病区名 床号 -->
		sb.append("<addr xsi:type=\"BAG_AD\">");
		sb.append("<item use=\"TMP\">");
		sb.append(
				"<part type=\"BNR\" value=\"\" code=\"\" codeSystem=\"1.2.156.112606.1.1.33\" codeSystemVersion=\"\" />");
		sb.append("<part type=\"CAR\" value=\"\" />");
		sb.append("</item>");
		sb.append("</addr>");
		// <!--个人信息 必须项已使用 -->");
		sb.append("<patientPerson classCode=\"PSN\">");
		// <!-- 身份证号/医保卡号 -->
		sb.append("<id>");
		// <!-- 身份证号 -->
		sb.append("<item extension=\"" + lismessage.getCustom().getPersonidnum()
				+ "\" root=\"1.2.156.112606.1.2.1.9\" />");
		//<!-- 医保卡号 -->
		sb.append("<item extension=\""+lismessage.getCustom().getPersioncode()+"\" root=\"1.2.156.112606.1.2.1.11\" />");
				//<!-- 就诊卡号 -->
		sb.append("<item extension=\""+lismessage.getCustom().getMc_no()+"\" root=\"jiuzhenkahao\" />");
		sb.append("</id>");
		// <!--姓名 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getCustom().getName() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		// <!-- 联系电话 -->
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		// <!-- 联系电话 -->
		sb.append("<item value=\"" + lismessage.getCustom().getTel() + "\"></item>");
		sb.append("</telecom>");
		// <!--性别代码 -->
		sb.append("<administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" codeSystem=\"1.2.156.112606.1.1.3\" codeSystemName=\""+lismessage.getCustom().getSexname()+"\"/>");	
		// <!--出生日期 -->
		sb.append("<birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\">");
		// <!--年龄 -->
		sb.append("<originalText value=\"" + lismessage.getCustom().getOld() + "\" />");
		sb.append("</birthTime>");
		// <!--住址 -->
		sb.append("<addr xsi:type=\"BAG_AD\">");
		sb.append("<item use=\"H\">");
		sb.append("<part type=\"AL\" value=\"" + lismessage.getCustom().getAddress() + "\" />");
		sb.append("</item>");
		sb.append("</addr>");
		// <!--婚姻状况类别编码 -->
		sb.append("<maritalStatusCode code=\""
				+ lismessage.getCustom().getMeritalcode() + "\" codeSystem=\"1.2.156.112606.1.1.4\" codeSystemName=\"\" />");
		// <!--民族编码 -->
		sb.append("<ethnicGroupCode>");
		sb.append("<item code=\""
				+ lismessage.getCustom().getEthnicGroupCode() + "\" codeSystem=\"1.2.156.112606.1.1.5\" codeSystemName=\"\" />");
		sb.append("</ethnicGroupCode>");
		// <!--雇佣关系 -->
		sb.append("<asEmployee classCode=\"EMP\">");
		// <!--职业编码 -->
		sb.append("<occupationCode code=\"\" codeSystem=\"1.2.156.112606.1.1.7\" codeSystemName=\"\">");
		// <!--职业 -->
		sb.append("<displayName value=\"\"></displayName>");
		sb.append("</occupationCode>");
		String comnum=getComNum(this.lismessage.getCustom().getExam_num());
		
		// <!--工作单位 -->
		sb.append("<employerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		sb.append("<unit_code value=\""+comnum+"\" />");
		// <!--工作单位名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getCustom().getComname() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("<contactParty classCode=\"CON\" xsi:nil=\"true\" />");
		sb.append("</employerOrganization>");
		sb.append("</asEmployee>");
		// <!--公民身份 -->
		sb.append("<asCitizen>");
		// <!--所属国家 -->
		sb.append("<politicalNation>");
		// <!--国籍编码 -->
		sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.1.6\" codeSystemName=\"\">");
		// <!--国家 -->
		sb.append("<displayName value=\"\"></displayName>");
		sb.append("</code>");
		sb.append("</politicalNation>");
		sb.append("</asCitizen>");
		// <!--联系人 -->
		sb.append("<contactParty classCode=\"CON\">");
		// <!--联系人电话-->
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		sb.append(
				"<item use=\"MC\" value=\"" + lismessage.getCustom().getContact_tel() + "\" capabilities=\"voice\" />");
		sb.append("</telecom>");
		// <!--联系人姓名/亲属 -->
		sb.append("<contactPerson>");
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item use=\"IDE\">");
		sb.append("<part value=\"" + lismessage.getCustom().getContact_tel() + " \" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</contactPerson>");
		sb.append("</contactParty>");
		sb.append("</patientPerson>");
		sb.append("<providerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		// <!-- 必须项 未使用 -->
		sb.append("<id></id>");
		// <!--申请医院 保留可不填 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getPart_name() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</providerOrganization>");
		sb.append("</patient>");
		sb.append("</subject>");
		// <!--开医嘱者/送检医生 -->
		sb.append("<author typeCode=\"AUT\">");
		// <!-- 开单时间 -->
		sb.append("<time value=\"" + lismessage.getDoctor().getTime() + "\"></time>");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		// <!--开单医生编码 -->
		sb.append("<id>");
		sb.append(
				"<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112606.1.1.2\" />");
		sb.append("</id>");
		// <!--开单医生姓名 -->
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		// <!-- 申请科室信息 -->
		sb.append("<representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		// <!--申请科室编码 必须项已使用 -->
		sb.append("<id>");
		sb.append("<item extension=\"" + lismessage.getDoctor().getDept_code() + "\" root=\"1.2.156.112606.1.1.1\" />");
		sb.append("</id>");
		// <!--申请科室名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getDoctor().getDept_name() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</representedOrganization>");
		sb.append("</assignedEntity>");
		sb.append("</author>");
		// <!-- 确认人 -->
		sb.append("<verifier typeCode=\"VRF\">");
		// <!--确认时间 -->
		sb.append("<time value=\"" + lismessage.getDoctor().getTime() + "\"></time>");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		// <!--确认人编码 -->
		sb.append("<id>");
		sb.append(
				"<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112606.1.1.2\" />");
		sb.append("</id>");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\"");
		sb.append(" classCode=\"PSN\">");
		// <!--确认人姓名 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</verifier>");

		if ((lismessage.getComponents() != null) && (lismessage.getComponents().size()>0))
			for (PacsComponents compts : lismessage.getComponents()) {
				// <!--
				// 1..n(可循环)一个检查消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2
				// -->
				sb.append("<component2>");
				sb.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
				// <!-- 检查申请单编号 必须项已使用 -->
				sb.append("<id>");
				sb.append("<item extension=\"" + compts.getReq_no() + "\" />");
				sb.append("</id>");
				// <!-- 医嘱类型 -->
				sb.append("<code code=\"" + compts.getYzCode()+ "\" codeSystem=\"1.2.156.112606.1.1.27\">");
				// <!-- 医嘱类型名称 -->
				sb.append("<displayName value=\"" + compts.getYzName() + "\" />");
				sb.append("</code>");
				sb.append("<exam_class  value=\""+compts.getPacsComponent().get(0).getExam_class()+"\"/>");
				sb.append("<costs value=\""+compts.getCosts()+"\" />");
				//<--子类--> 空
				sb.append("<sub_exam_class value=\"\" />");
				// <!-- 申请单详细内容 -->
				sb.append("<text value=\"" + compts.getPacsComponent().get(0).getItemName() + "\"/>");
				// <!-- 检查状态 必须项未使用 -->
				sb.append("<statusCode></statusCode>");
				// <!-- 检查申请日期 -->
				sb.append("<effectiveTime xsi:type=\"IVL_TS\">");
				sb.append("<any value=\"" + compts.getDatetime() + "\" />");
				sb.append("</effectiveTime>");
				// <!--标本 -->
				sb.append("<specimen typeCode=\"SPC\">");
				sb.append("<specimen classCode=\"SPEC\">");
				// <!--标本号-号码 必须项未使用 -->
				sb.append("<id extension=\"\"/>");
				// <!--标本类别编码 必须项已使用 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.1.45\"/>");
				sb.append("<subjectOf1 typeCode=\"\">");
				sb.append("<specimenCollectionProcess moodCode=\"EVN\" classCode=\"SPECCOLLECT\">");
				// <!--必须项未使用 -->
				sb.append("<code></code>");
				// <!-- 标本要求 -->
				sb.append("<text value=\"\"></text>");
				sb.append("</specimenCollectionProcess>");
				sb.append("</subjectOf1>");
				sb.append("</specimen>");
				sb.append("</specimen>");
				// <!--执行科室 -->
				sb.append("<location typeCode=\"LOC\">");
				// <!-- 执行时间 -->
				sb.append("<time>");
				sb.append("<any value=\"" + compts.getPacsComponent().get(0).getItemtime() + "\"></any>");
				sb.append("</time>");
				sb.append("<serviceDeliveryLocation classCode=\"SDLOC\">");
				sb.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
				// <!--执行科室编码 -->
				sb.append("<id>");
				sb.append("<item extension=\"" + compts.getPacsComponent().get(0).getServiceDeliveryLocation_code()
						+ "\" root=\"1.2.156.112606.1.1.1\" />");
				sb.append("</id>");
				// <!-- 执行科室名称 -->
				sb.append("<name xsi:type=\"BAG_EN\">");
				sb.append("<item>");
				sb.append("<part value=\"" + compts.getPacsComponent().get(0).getServiceDeliveryLocation_name() + " \" />");
				sb.append("</item>");
				sb.append("</name>");
				sb.append("</serviceProviderOrganization>");
				sb.append("</serviceDeliveryLocation>");
				sb.append("</location>");
				// <!--1..n 一个申请单可以包含多个医嘱，每个医嘱对应一个component2,多个医嘱重复component2
				// -->

				sb.append("<component2>");
				sb.append("<observationRequest classCode=\"OBS\">");
				// <!-- 医嘱号 -->
				sb.append("<id>");
				sb.append("<item extension=\"\" />");
				sb.append("</id>");
				// <!--检查项目编码 必须项已使用 -->
				sb.append(
						"<code code=\""+compts.getPacsComponent().get(0).getPacs_num()+"\" codeSystem=\"1.2.156.112606.1.1.88/1.2.156.112606.1.1.110\" codeSystemName=\"\">");
				// <!--检查项目名称 -->
				sb.append("<displayName value=\""+compts.getPacsComponent().get(0).getItemName()+"\" />");
				sb.append("</code>");
				// <!-- 必须项未使用 -->
				sb.append("<statusCode />");
				// <!-- 必须项未使用 -->
				sb.append("<effectiveTime xsi:type=\"IVL_TS\" />");
				// <!--检查方法编码 -->
				sb.append("<methodCode>");
				sb.append("<item code=\" \" codeSystem=\"1.2.156.112606.1.1.43\" codeSystemName=\"\">");
				// <!--检查方法名 -->
				sb.append("<displayName value=\"\"></displayName>");
				sb.append("</item>");
				sb.append("</methodCode>");
				// <!--检查部位编码 -->
				sb.append("<targetSiteCode>");
				sb.append("<item code=\"\" codeSystem=\"1.2.156.112606.1.1.42\" codeSystemName=\"\">");
				// <!--检查部位名称 -->
				sb.append("<displayName value=\"\"></displayName>");
				sb.append("</item>");
				sb.append("</targetSiteCode>");
				sb.append("</observationRequest>");
				sb.append("</component2>");

				
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\">");
				// <!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"false\" />");
				// <!--申请注意事项 -->
				sb.append("<annotation>");
				sb.append("<text value=\"\"></text>");
				sb.append("<statusCode code=\"completed\" />");
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
				sb.append("</observationRequest>");
				sb.append("</component2>");
			}
		// <!--就诊 -->
		sb.append("<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"\">");
		// <!--就诊 -->
		sb.append("<encounter classCode=\"\" moodCode=\"\">");
		// <!-- 就诊次数 -->
		sb.append("<id>");
		sb.append("<item extension=\"\" root=\"1.2.156.112606.1.2.1.7\"/>");
		sb.append("</id>");
		// <!--病人类型编码 -->
		sb.append("<code codeSystem=\"1.2.156.112606.1.1.80\" codeSystemName=\"\" 	code=\"3\" />");
		// <!--必须项未使用 -->
		sb.append("<statusCode  />");
		// <!--病人 必须项未使用 -->
		sb.append("<subject typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\" />");
		sb.append("</subject>");
		// <!--诊断(检查申请原因) -->
		sb.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">");
		sb.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">");
		// <!--诊断类别编码 必须项已使用 -->
		sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.1.29\" codeSystemName=\" \">");
		// <!--诊断类别名称 -->
		sb.append("<displayName value=\"\" />");
		//<!--临床症状 -->
		sb.append("<CLIN_SYMP value=\"\"/>");
		//<!--体征 -->
		sb.append("<PHYS_SIGN value=\"\" />");
		sb.append("</code>");
		// <!-- 必须项未使用 -->
		sb.append("<statusCode code=\"active\" />");
		// <!--诊断日期 -->
		sb.append("<effectiveTime>");
		sb.append("<any value=\"\"></any>");
		sb.append("</effectiveTime>");
		// <!-- 疾病编码 必须项已使用 -->
		sb.append("<value code=\"\" codeSystem=\"1.2.156.112606.1.1.30\">");
		// <!-- 疾病名称 -->
		sb.append("<displayName value=\"\" />");
		sb.append("</value>");
		sb.append("</observationDx>");
		sb.append("</pertinentInformation1>");

		/*
		 * //<!-- 0..n(可循环)一个检查消息中可以由多个既往病史pertinentInformation1对应，有多个既往病史时，
		 * 重复pertinentInformation1 --> sb.append(
		 * "<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">");
		 * sb.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">");
		 * //<!--既往史编码 --> sb.append(
		 * "<code code=\"A18.028+M01.1*\" codeSystem=\"1.2.156.112606.1.2.2.5\" codeSystemName=\"东北国际医院OID既往史\"/>"
		 * ); //<!-- 必须项未使用 --> sb.append("<statusCode />"); //<!--既往史疾病编码-->
		 * sb.append(
		 * "<value code=\"A18.029+M01.1* \" codeSystem=\"1.2.156.112606.1.1.30\">"
		 * ); //<!--既往史 疾病名称 --> sb.append("<displayName value=\"膝结核性滑膜炎 \" />"
		 * ); sb.append("</value>"); sb.append("</observationDx>");
		 * sb.append("</pertinentInformation1>");
		 */

		sb.append("</encounter>");
		sb.append("</componentOf1>");
		sb.append("</placerGroup>");
		sb.append("</subject>");
		sb.append("</controlActProcess>");
		sb.append("</POOR_IN200901UV>");
		return sb.toString();
	}

	public String lisSendMessage_test() {
		StringBuffer sb = new StringBuffer("");

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xsi:schemaLocation=\"urn:hl7-org:v3");
		sb.append(" ../../Schemas/POOR_IN200901UV20.xsd\">");
		// <!-- 消息ID -->
		sb.append("<id extension=\"TI001\" />");
		// <!-- 消息创建时间 -->
		sb.append("<creationTime value=\"20120106110000\" />");
		// <!-- 交互ID -->
		sb.append("<interactionId root=\"\" extension=\"POOR_IN200901UV20\" />");
		// <!-- 消息用途: P(Production); D(Debugging); T(Training) -->
		sb.append("<processingCode code=\"P\" />");
		// <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive);
		// T(Current processing) -->
		sb.append("<processingModeCode code=\"T\" />");
		// <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->
		sb.append("<acceptAckCode code=\"NE\" />");
		// <!-- 接受者 -->
		sb.append("<receiver typeCode=\"RCV\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		// <!-- 接受者ID -->
		sb.append("<id />");
		sb.append("</device>");
		sb.append("</receiver>");
		// <!-- 发送者 -->
		sb.append("<sender typeCode=\"SND\">");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
		// <!-- 发送者ID -->
		sb.append("<id />");
		sb.append("</device>");
		sb.append("</sender>");
		// <!-- 封装的消息内容(按Excel填写) -->
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">");
		// <!-- 消息交互类型 @code: 新增 :new 删除：delete-->
		sb.append("<code code=\"new\"></code>");
		sb.append("<subject typeCode=\"SUBJ\" xsi:nil=\"false\">");
		sb.append("<placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">");
		sb.append("<subject typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\">");
		sb.append("<id>");
		// <!-- 域ID -->
		sb.append("<item root=\"1.2.156.112606.1.2.1.2\" extension=\"01\" />");
		// <!-- 患者ID -->
		sb.append("<item root=\"1.2.156.112606.1.2.1.3\" extension=\"09102312\" />");
		// <!-- 就诊号 -->
		sb.append("<item root=\"1.2.156.112606.1.2.1.12\" extension=\"\" />");
		sb.append(
				"<item root=\"exam_num\" extension=\"T1100011111\" />");
		sb.append("</id>");
		// <!-- 病区编码/病区名 床号 -->
		sb.append("<addr xsi:type=\"BAG_AD\">");
		sb.append("<item use=\"TMP\">");
		sb.append(
				"<part type=\"BNR\" value=\"9A血液科\" code=\"09808\" codeSystem=\"1.2.156.112606.1.1.33\" codeSystemVersion=\"东北国际医院OID病区\" />");
		sb.append("<part type=\"CAR\" value=\"06\" />");
		sb.append("</item>");
		sb.append("</addr>");
		// <!--个人信息 必须项已使用 -->");
		sb.append("<patientPerson classCode=\"PSN\">");
		// <!-- 身份证号/医保卡号 -->
		sb.append("<id>");
		// <!-- 身份证号 -->
		sb.append("<item extension=\"110938197803030456\" root=\"1.2.156.112606.1.2.1.9\" />");
		// <!-- 医保卡号 -->
		sb.append("<item extension=\"191284777494877\" root=\"1.2.156.112606.1.2.1.11\" />");
		sb.append("</id>");
		// <!--姓名 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"张三\" />");
		sb.append("</item>");
		sb.append("</name>");
		// <!-- 联系电话 -->
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		// <!-- 联系电话 -->
		sb.append("<item value=\"15801020489\"></item>");
		sb.append("</telecom>");
		// <!--性别代码 -->
		sb.append(
				"<administrativeGenderCode code=\"男\" codeSystem=\"1.2.156.112606.1.1.3\" codeSystemName=\"东北国际医院OID性别代码\"/>");
		// <!--出生日期 -->
		sb.append("<birthTime value=\"19870202\">");
		// <!--年龄 -->
		sb.append("<originalText value=\"25\" />");
		sb.append("</birthTime>");
		// <!--住址 -->
		sb.append("<addr xsi:type=\"BAG_AD\">");
		sb.append("<item use=\"H\">");
		sb.append("<part type=\"AL\" value=\"沈阳铁西区\" />");
		sb.append("</item>");
		sb.append("</addr>");
		// <!--婚姻状况类别编码 -->
		sb.append(
				"<maritalStatusCode code=\"10\" codeSystem=\"1.2.156.112606.1.1.4\" codeSystemName=\"东北国际医院OID婚姻状况\" />");
		// <!--民族编码 -->
		sb.append("<ethnicGroupCode>");
		sb.append("<item code=\"HA\" codeSystem=\"1.2.156.112606.1.1.5\" codeSystemName=\"东北国际医院OID民族代码\" />");
		sb.append("</ethnicGroupCode>");
		// <!--雇佣关系 -->
		sb.append("<asEmployee classCode=\"EMP\">");
		// <!--职业编码 -->
		sb.append("<occupationCode code=\"6-24\" codeSystem=\"1.2.156.112606.1.1.7\" codeSystemName=\"东北国际医院OID职业\">");
		// <!--职业 -->
		sb.append("<displayName value=\"重有色金属冶炼人员\"></displayName>");
		sb.append("</occupationCode>");
		// <!--工作单位 -->
		sb.append("<employerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		// <!--工作单位名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"中钢\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("<contactParty classCode=\"CON\" xsi:nil=\"true\" />");
		sb.append("</employerOrganization>");
		sb.append("</asEmployee>");
		// <!--公民身份 -->
		sb.append("<asCitizen>");
		// <!--所属国家 -->
		sb.append("<politicalNation>");
		// <!--国籍编码 -->
		sb.append("<code code=\"156\" codeSystem=\"1.2.156.112606.1.1.6\" codeSystemName=\"东北国际医院OID国藉\">");
		// <!--国家 -->
		sb.append("<displayName value=\"中国\"></displayName>");
		sb.append("</code>");
		sb.append("</politicalNation>");
		sb.append("</asCitizen>");
		// <!--联系人 -->
		sb.append("<contactParty classCode=\"CON\">");
		// <!--联系人电话-->
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		sb.append("<item use=\"MC\" value=\"13899095656\" capabilities=\"voice\" />");
		sb.append("</telecom>");
		// <!--联系人姓名/亲属 -->
		sb.append("<contactPerson>");
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item use=\"IDE\">");
		sb.append("<part value=\"李四 \" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</contactPerson>");
		sb.append("</contactParty>");
		sb.append("</patientPerson>");
		sb.append("<providerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		// <!-- 必须项 未使用 -->
		sb.append("<id></id>");
		// <!--申请医院 保留可不填 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"东北国际医院\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</providerOrganization>");
		sb.append("</patient>");
		sb.append("</subject>");
		// <!--开医嘱者/送检医生 -->
		sb.append("<author typeCode=\"AUT\">");
		// <!-- 开单时间 -->
		sb.append("<time value=\"201205061000\"></time>");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		// <!--开单医生编码 -->
		sb.append("<id>");
		sb.append("<item extension=\"09882374\" root=\"1.2.156.112606.1.1.2\" />");
		sb.append("</id>");
		// <!--开单医生姓名 -->
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"李武\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		// <!-- 申请科室信息 -->
		sb.append("<representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		// <!--申请科室编码 必须项已使用 -->
		sb.append("<id>");
		sb.append("<item extension=\"023984\" root=\"1.2.156.112606.1.1.1\" />");
		sb.append("</id>");
		// <!--申请科室名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"骨科\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</representedOrganization>");
		sb.append("</assignedEntity>");
		sb.append("</author>");
		// <!-- 确认人 -->
		sb.append("<verifier typeCode=\"VRF\">");
		// <!--确认时间 -->
		sb.append("<time value=\"201205061000\"></time>");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		// <!--确认人编码 -->
		sb.append("<id>");
		sb.append("<item extension=\"9023884\" root=\"1.2.156.112606.1.1.2\" />");
		sb.append("</id>");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\"");
		sb.append(" classCode=\"PSN\">");
		// <!--确认人姓名 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"李二\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</verifier>");

		for (int i = 0; i < 2; i++) {
			// <!--
			// 1..n(可循环)一个检查消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2
			// -->
			sb.append("<component2>");
			sb.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
			// <!-- 检查申请单编号 必须项已使用 -->
			sb.append("<id>");
			sb.append("<item extension=\"0923848747\" />");
			sb.append("</id>");
			// <!-- 医嘱类型 -->
			sb.append("<code code=\"2\" codeSystem=\"1.2.156.112606.1.1.27\">");
			// <!-- 医嘱类型名称 -->
			sb.append("<displayName value=\"检查类\" />");
			sb.append("</code>");
			sb.append("<exam_class  value=\"CT\"/>");
			sb.append("<costs value=\"100.00\" />");
			//<--子类--> 空
			sb.append("<sub_exam_class value=\"\" />");
			// <!-- 申请单详细内容 -->
			sb.append("<text value=\"腿骨检查\"/>");
			// <!-- 检查状态 必须项未使用 -->
			sb.append("<statusCode></statusCode>");
			// <!-- 检查申请日期 -->
			sb.append("<effectiveTime xsi:type=\"IVL_TS\">");
			sb.append("<any value=\"20120506\" />");
			sb.append("</effectiveTime>");
			// <!--标本 -->
			sb.append("<specimen typeCode=\"SPC\">");
			sb.append("<specimen classCode=\"SPEC\">");
			// <!--标本号-号码 必须项未使用 -->
			sb.append("<id extension=\"01238487\"/>");
			// <!--标本类别编码 必须项已使用 -->
			sb.append("<code code=\"04\" codeSystem=\"1.2.156.112606.1.1.45\"/>");
			sb.append("<subjectOf1 typeCode=\"SBJ\">");
			sb.append("<specimenCollectionProcess moodCode=\"EVN\" classCode=\"SPECCOLLECT\">");
			// <!--必须项未使用 -->
			sb.append("<code></code>");
			// <!-- 标本要求 -->
			sb.append("<text value=\"胆囊切片\"></text>");
			sb.append("</specimenCollectionProcess>");
			sb.append("</subjectOf1>");
			sb.append("</specimen>");
			sb.append("</specimen>");
			// <!--执行科室 -->
			sb.append("<location typeCode=\"LOC\">");
			// <!-- 执行时间 -->
			sb.append("<time>");
			sb.append("<any value=\"201206060900\"></any>");
			sb.append("</time>");
			sb.append("<serviceDeliveryLocation classCode=\"SDLOC\">");
			sb.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
			// <!--执行科室编码 -->
			sb.append("<id>");
			sb.append("<item extension=\"0128384\" root=\"1.2.156.112606.1.1.1\" />");
			sb.append("</id>");
			// <!-- 执行科室名称 -->
			sb.append("<name xsi:type=\"BAG_EN\">");
			sb.append("<item>");
			sb.append("<part value=\"检验科 \" />");
			sb.append("</item>");
			sb.append("</name>");
			sb.append("</serviceProviderOrganization>");
			sb.append("</serviceDeliveryLocation>");
			sb.append("</location>");
			// <!--1..n 一个申请单可以包含多个医嘱，每个医嘱对应一个component2,多个医嘱重复component2 -->
			sb.append("<component2>");
			sb.append("<observationRequest classCode=\"OBS\">");
			// <!-- 医嘱号 -->
			sb.append("<id>");
			sb.append("<item extension=\"293847547\" />");
			sb.append("</id>");
			// <!--检查项目编码 必须项已使用 -->
			sb.append(
					"<code code=\"92\" codeSystem=\"1.2.156.112606.1.1.88/1.2.156.112606.1.1.110\" codeSystemName=\"东北国际医院OID检查项目/医嘱字典\">");
			// <!--检查项目名称 -->
			sb.append("<displayName value=\"全身骨显像\" />");
			sb.append("</code>");
			// <!-- 必须项未使用 -->
			sb.append("<statusCode />");
			// <!-- 必须项未使用 -->
			sb.append("<effectiveTime xsi:type=\"IVL_TS\" />");
			// <!--检查方法编码 -->
			sb.append("<methodCode>");
			sb.append("<item code=\"94 \" codeSystem=\"1.2.156.112606.1.1.43\" codeSystemName=\"东北国际医院OID检查方法\">");
			// <!--检查方法名 -->
			sb.append("<displayName value=\"膝关节三维CT扫描\"></displayName>");
			sb.append("</item>");
			sb.append("</methodCode>");
			// <!--检查部位编码 -->
			sb.append("<targetSiteCode>");
			sb.append("<item code=\"1106\" codeSystem=\"1.2.156.112606.1.1.42\" codeSystemName=\"东北国际医院OID检查部位\">");
			// <!--检查部位名称 -->
			sb.append("<displayName value=\"右膝关节\"></displayName>");
			sb.append("</item>");
			sb.append("</targetSiteCode>");
			sb.append("</observationRequest>");
			sb.append("</component2>");
			sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\">");
			// <!-- 必须项 未使用 default=false -->
			sb.append("<seperatableInd value=\"false\" />");
			// <!--申请注意事项 -->
			sb.append("<annotation>");
			sb.append("<text value=\"注意XXX\"></text>");
			sb.append("<statusCode code=\"completed\" />");
			sb.append("<author>");
			sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
			sb.append("</author>");
			sb.append("</annotation>");
			sb.append("</subjectOf6>");
			sb.append("</observationRequest>");
			sb.append("</component2>");
		}
		// <!--就诊 -->
		sb.append("<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"COMP\">");
		// <!--就诊 -->
		sb.append("<encounter classCode=\"ENC\" moodCode=\"EVN\">");
		// <!-- 就诊次数 -->
		sb.append("<id>");
		sb.append("<item extension=\"2\" root=\"1.2.156.112606.1.2.1.7\"/>");
		sb.append("</id>");
		// <!--病人类型编码 -->
		sb.append("<code codeSystem=\"1.2.156.112606.1.1.80\" codeSystemName=\"\" 	code=\"3\" />");
		// <!--必须项未使用 -->
		sb.append("<statusCode  />");
		// <!--病人 必须项未使用 -->
		sb.append("<subject typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\" />");
		sb.append("</subject>");
		// <!--诊断(检查申请原因) -->
		sb.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">");
		sb.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">");
		// <!--诊断类别编码 必须项已使用 -->
		sb.append("<code code=\"7\" codeSystem=\"1.2.156.112606.1.1.29\" codeSystemName=\"东北国际医院OID诊断类别 \">");
		// <!--诊断类别名称 -->
		sb.append("<displayName value=\"放射诊断\" />");
		//<!--临床症状 -->
		sb.append("<CLIN_SYMP value=\"放射诊断\"/>");
		//<!--体征 -->
		sb.append("<PHYS_SIGN value=\"放射诊断\" />");
		sb.append("</code>");
		// <!-- 必须项未使用 -->
		sb.append("<statusCode code=\"active\" />");
		// <!--诊断日期 -->
		sb.append("<effectiveTime>");
		sb.append("<any value=\"20120506\"></any>");
		sb.append("</effectiveTime>");
		// <!-- 疾病编码 必须项已使用 -->
		sb.append("<value code=\"A18.029+M01.1* \" codeSystem=\"1.2.156.112606.1.1.30\">");
		// <!-- 疾病名称 -->
		sb.append("<displayName value=\"膝结核性滑膜炎 \" />");
		sb.append("</value>");
		sb.append("</observationDx>");
		sb.append("</pertinentInformation1>");

		// <!--
		// 0..n(可循环)一个检查消息中可以由多个既往病史pertinentInformation1对应，有多个既往病史时，重复pertinentInformation1
		// -->
		sb.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">");
		sb.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">");
		// <!--既往史编码 -->
		sb.append(
				"<code code=\"A18.028+M01.1*\" codeSystem=\"1.2.156.112606.1.2.2.5\" codeSystemName=\"东北国际医院OID既往史\"/>");
		// <!-- 必须项未使用 -->
		sb.append("<statusCode />");
		// <!--既往史疾病编码-->
		sb.append("<value code=\"A18.029+M01.1* \" codeSystem=\"1.2.156.112606.1.1.30\">");
		// <!--既往史 疾病名称 -->
		sb.append("<displayName value=\"膝结核性滑膜炎 \" />");
		sb.append("</value>");
		sb.append("</observationDx>");
		sb.append("</pertinentInformation1>");

		sb.append("</encounter>");
		sb.append("</componentOf1>");
		sb.append("</placerGroup>");
		sb.append("</subject>");
		sb.append("</controlActProcess>");
		sb.append("</POOR_IN200901UV>");
		return sb.toString();
	}
	
	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public String getComNum(String exam_num) {
		Connection tjtmpconnect = null;
		String logname="getcomnum";
		String req_id = "";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ci.com_name,ci.com_num from company_info ci,exam_info  ei where ei.company_id=ci.id "
					+ "and ei.exam_num='"+exam_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				req_id = rs1.getString("com_num");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_id;
	}
}
