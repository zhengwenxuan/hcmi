package com.hjw.webService.client.fangzheng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.fangzheng.MQ.mqSendSample;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.MenuDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSSendMessageFZ{
	private PacsMessageBody lismessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageFZ(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname, boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		mqSendSample mqSendSample = null;
		try {
			mqSendSample = new mqSendSample();
			String ip = url.split("&")[0];
			int port = Integer.valueOf(url.split("&")[1]);
			mqSendSample.initEnvironment(ip, port);
			List<ApplyNOBean> anList = new ArrayList<ApplyNOBean>();
			for (PacsComponents comps : lismessage.getComponents()) {
				try {
					String xml = lisSendMessage(comps,logname);
					if(xml.trim().length()>0){
					String stype=getOrderExecId(comps.getPacsComponent().get(0).getItemCode(),logname);
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
					String messages = mqSendSample.msgSend(xml, "BS002","0401",stype);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					
					long datetime=Long.valueOf(DateTimeUtil.getDateTimes());					
					String strstatus = getStatus(comps,"100.001", "医嘱已下达",datetime+"", logname);
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + strstatus);
					messages = mqSendSample.msgSend(strstatus, "BS004","0401",stype);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					
					datetime=Long.valueOf(DateTimeUtil.getDateTimes())+1;	
					strstatus = getStatus(comps,"110.001", "医嘱已确认",datetime+"", logname);
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + strstatus);
					messages = mqSendSample.msgSend(strstatus, "BS004","0401",stype);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
					}
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("pacs解析返回值错误");
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}finally{
			if(mqSendSample!=null){
				try{
				mqSendSample.disconnectQM();
				}catch(Exception ex){
					
				}
			}			
		}
		return rb;
	}
			

	private String lisSendMessage(PacsComponents compts,String logname) {
		String xml="";
		int lisid=updatezl_req_pacs_item(lismessage.getCustom().getExam_num(),compts.getReq_no(),compts.getPacsComponent().get(0).getItemCode(),logname);
		if(lisid>0){
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xsi:schemaLocation=\"urn:hl7-org:v3 ../../Schemas/POOR_IN200901UV20.xsd\">\n");
		sb.append("<!-- 消息ID -->\n");
		sb.append("<id extension=\"BS002\" />\n");
		sb.append(" <!-- 消息创建时间 -->\n");
		sb.append("<creationTime value=\"" + lismessage.getCreationTime_value() + "\" />\n");
		sb.append(" <!-- 交互ID -->\n");
		sb.append("<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"POOR_IN200901UV20\" />\n");
		sb.append(" <!-- 消息用途: P(Productisb.append(); D(Debugging); T(Training) --> \n");
		sb.append("<processingCode code=\"P\" />\n");
		sb.append(" <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive);--> \n");
		sb.append(" T(Current processing) -->\n");
		sb.append("<processingModeCode code=\"T\" />\n");
		sb.append(" <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->\n");
		sb.append("<acceptAckCode code=\"NE\" />\n");
		sb.append(" <!-- 接受者 -->\n");
		sb.append("<receiver typeCode=\"RCV\">\n");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append(" <!-- 接受者ID -->\n");
		sb.append("<id>\n");
		sb.append("<item root=\"1.2.156.112685.1.1.19\" extension=\"\"/>\n");
		sb.append("</id>\n");
		sb.append("</device>\n");
		sb.append("</receiver>\n");
		sb.append(" <!-- 发送者 -->\n");
		sb.append("<sender typeCode=\"SND\">\n");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append(" <!-- 发送者ID -->\n");
		sb.append("<id>\n");
		sb.append("<item root=\"1.2.156.112685.1.1.19\" extension=\"S040\"/>\n");
		sb.append("</id>\n");
		sb.append("</device>\n");
		sb.append("</sender>\n");
		sb.append(" <!-- 封装的消息内容(按Excel填写) -->\n");
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
		sb.append(" <!-- 消息交互类型 @code: 新增 :new 删除：delete-->\n");
		sb.append("<code code=\"new\"></code>\n");
		sb.append("<subject typeCode=\"SUBJ\" xsi:nil=\"false\">\n");
		sb.append("<placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">\n");
		sb.append("<subject typeCode=\"SBJ\">\n");
		sb.append("<patient classCode=\"PAT\">\n");
		sb.append("<id>\n");
		sb.append(" <!-- 域ID -->\n");
		sb.append("<item root=\"1.2.156.112685.1.2.1.2\" extension=\"01\" />\n");
		sb.append(" <!-- 患者ID -->\n");
		sb.append(
				"<item root=\"1.2.156.112685.1.2.1.3\" extension=\"" + lismessage.getCustom().getPersonid() + "\" />\n");
		sb.append(" <!-- 就诊号 -->\n");
		ExamInfoUserDTO sjh=getHISDJH(lismessage.getCustom().getExam_num());
		sb.append(
				"<item root=\"1.2.156.112685.1.2.1.12\" extension=\""+sjh.getClinic_no()+"\" />\n");
		sb.append("</id>\n");
		sb.append(" <!-- 病区编码/病区名 床号 -->\n");
		sb.append("<addr xsi:type=\"BAG_AD\">\n");
		sb.append("<item use=\"TMP\">\n");
		sb.append(
				"<part type=\"BNR\" value=\"\" code=\"\" codeSystem=\"1.2.156.112685.1.1.33\" />\n");
		sb.append("<part type=\"CAR\" value=\"\" />\n");
		sb.append("</item>\n");
		sb.append("</addr>\n");
		sb.append(" <!--个人信息 必须项已使用 -->\n");
		sb.append("<patientPerson classCode=\"PSN\">\n");
		sb.append(" <!-- 身份证号/医保卡号 -->\n");
		sb.append("<id>\n");
		sb.append(" <!-- 身份证号 -->\n");
		sb.append("<item extension=\"" + lismessage.getCustom().getPersonidnum()+ "\" root=\"1.2.156.112685.1.2.1.9\" />\n");
		sb.append("<!-- 医保卡号 -->\n");
		sb.append("<item extension=\""+lismessage.getCustom().getPersioncode()+"\" root=\"1.2.156.112685.1.2.1.11\" />\n");
		sb.append("</id>\n");
		sb.append(" <!--姓名 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getCustom().getName() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append(" <!-- 联系电话 -->\n");
		sb.append("<telecom xsi:type=\"BAG_TEL\">\n");
		sb.append(" <!-- 联系电话 -->\n");
		sb.append("<item value=\"" + lismessage.getCustom().getTel() + "\"></item>\n");
		sb.append("</telecom>\n");
		sb.append(" <!--性别代码 -->\n");
		sb.append("<administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" codeSystem=\"1.2.156.112685.1.1.3\" />\n");	
		sb.append(" <!--出生日期 -->\n");
		sb.append("<birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\">\n");
		sb.append(" <!--年龄 -->\n");
		sb.append("<originalText value=\"" + lismessage.getCustom().getOld() + "\" />\n");
		sb.append("</birthTime>\n");
		sb.append(" <!--住址 -->\n");
		sb.append("<addr xsi:type=\"BAG_AD\">\n");
		sb.append("<item use=\"H\">\n");
		sb.append("<part type=\"AL\" value=\"" + lismessage.getCustom().getAddress() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</addr>\n");
		sb.append(" <!--婚姻状况类别编码 -->\n");
		sb.append("<maritalStatusCode code=\""
				+ lismessage.getCustom().getMeritalcode() + "\" codeSystem=\"1.2.156.112685.1.1.4\"/>\n");
		sb.append(" <!--民族编码 -->\n");
		sb.append("<ethnicGroupCode>\n");
		sb.append("<item code=\""
				+ lismessage.getCustom().getEthnicGroupCode() + "\" codeSystem=\"1.2.156.112685.1.1.5\"/>\n");
		sb.append("</ethnicGroupCode>\n");
		sb.append(" <!--雇佣关系 -->\n");
		sb.append("<asEmployee classCode=\"EMP\">\n");
		sb.append(" <!--职业编码 -->\n");
		sb.append("<occupationCode code=\"\" codeSystem=\"1.2.156.112685.1.1.7\">\n");
		sb.append(" <!--职业 -->\n");
		sb.append("<displayName value=\"\"></displayName>\n");
		sb.append("</occupationCode>\n");
		sb.append(" <!--工作单位 -->\n");
		sb.append("<employerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append(" <!--工作单位名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getCustom().getComname() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("<contactParty classCode=\"CON\" xsi:nil=\"true\" />\n");
		sb.append("</employerOrganization>\n");
		sb.append("</asEmployee>\n");
		sb.append(" <!--公民身份 -->\n");
		sb.append("<asCitizen>\n");
		sb.append(" <!--所属国家 -->\n");
		sb.append("<politicalNation>\n");
		sb.append(" <!--国籍编码 -->\n");
		sb.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.6\">\n");
		sb.append(" <!--国家 -->\n");
		sb.append("<displayName value=\"\"></displayName>\n");
		sb.append("</code>\n");
		sb.append("</politicalNation>\n");
		sb.append("</asCitizen>\n");
		sb.append(" <!--联系人 -->\n");
		sb.append("<contactParty classCode=\"CON\">\n");
		sb.append(" <!--联系人电话-->\n");
		sb.append("<telecom xsi:type=\"BAG_TEL\">\n");
		sb.append(
				"<item use=\"MC\" value=\"" + lismessage.getCustom().getContact_tel() + "\" capabilities=\"voice\" />\n");
		sb.append("</telecom>\n");
		sb.append(" <!--联系人姓名/亲属 -->\n");
		sb.append("<contactPerson>\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item use=\"IDE\">\n");
		sb.append("<part value=\"" + lismessage.getCustom().getContact_tel() + " \" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</contactPerson>\n");
		sb.append("</contactParty>\n");
		sb.append("</patientPerson>\n");
		sb.append("<providerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append(" <!-- 必须项 未使用 -->\n");
		sb.append("<id><item extension=\"\" root=\"1.2.156.112685.1.1.1\"/></id>\n");
		sb.append(" <!--申请医院 保留可不填 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"包头市中心医院\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("<asOrganizationPartOf classCode=\"PART\">\n");
		sb.append("<wholeOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append("<!--医疗机构代码 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"46014326-4\"/>\n");//46014326-4
		sb.append("</id>\n");
		sb.append("<!--医疗机构名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item><part value=\"包头市中心医院\" /></item>\n");//包头市中心医院
		sb.append("</name>\n");
		sb.append("</wholeOrganization>\n");
		sb.append("</asOrganizationPartOf>\n");
		sb.append("</providerOrganization>\n");
		sb.append("</patient>\n");
		sb.append("</subject>\n");
		sb.append(" <!--开医嘱者/送检医生 -->\n");
		sb.append("<author typeCode=\"AUT\">\n");
		sb.append(" <!-- 开单时间 -->\n");
		sb.append("<time value=\"" + lismessage.getDoctor().getTime() + "\"></time>\n");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append(" <!--开单医生编码 -->\n");
		sb.append("<id>\n");
		sb.append(
				"<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\" />\n");
		sb.append("</id>\n");
		sb.append(" <!--开单医生姓名 -->\n");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</assignedPerson>\n");
		sb.append(" <!-- 申请科室信息 -->\n");
		sb.append("<representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append(" <!--申请科室编码 必须项已使用 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"" + lismessage.getDoctor().getDept_code() + "\" root=\"1.2.156.112685.1.1.1\" />\n");
		sb.append("</id>\n");
		sb.append(" <!--申请科室名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getDoctor().getDept_name() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</representedOrganization>\n");
		sb.append("</assignedEntity>\n");
		sb.append("</author>\n");
		sb.append(" <!-- 确认人 -->\n");
		sb.append("<verifier typeCode=\"VRF\">\n");
		sb.append(" <!--确认时间 -->\n");
		sb.append("<time value=\"" + lismessage.getDoctor().getTime() + "\"></time>\n");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append(" <!--确认人编码 -->\n");
		sb.append("<id>\n");
		sb.append(
				"<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\" />\n");
		sb.append("</id>\n");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">\n");
		sb.append(" <!--确认人姓名 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</assignedPerson>\n");
		sb.append("</assignedEntity>\n");
		sb.append("</verifier>\n");
				StringBuffer sb0=new StringBuffer(); 
				sb0.append(" <!--1..n(可循环)一个检查消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2-->\n");
				sb0.append("<component2>\n");
				sb0.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">\n");
				sb0.append(" <!-- 检查申请单编号 必须项已使用 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"" + compts.getReq_no() + "\" />\n");
				sb0.append("</id>\n");
				sb0.append(" <!-- 医嘱类型 -->\n");
				sb0.append("<code code=\"D\" codeSystem=\"1.2.156.112685.1.1.27\">\n");
				sb0.append(" <!-- 医嘱类型名称 -->\n");
				sb0.append("<displayName value=\"检查\" />\n");
				sb0.append("</code>\n");
				/*sb0.append("<exam_class  value=\""+compts.getPacsComponent().get(0).getExam_class()+"\"/>\n");
				sb0.append("<costs value=\""+compts.getCosts()+"\" />\n");
				sb0.append("<--子类-->\n"); 
				sb0.append("<sub_exam_class value=\"\" />\n");*/
				sb0.append(" <!-- 申请单详细内容 -->\n");
				sb0.append("<text value=\"" + compts.getPacsComponent().get(0).getItemName() + "\"/>\n");
				sb0.append(" <!-- 检查状态 必须项未使用 -->\n");
				sb0.append("<statusCode></statusCode>\n");
				sb0.append(" <!-- 检查申请日期 -->\n");
				sb0.append("<effectiveTime xsi:type=\"IVL_TS\">\n");
				sb0.append("<any value=\"" + DateTimeUtil.getDateTimes() + "\" />\n");
				sb0.append("</effectiveTime>\n");
				sb0.append(" <!--标本 -->\n");
				sb0.append("<specimen typeCode=\"SPC\">\n");
				sb0.append("<specimen classCode=\"SPEC\">\n");
				sb0.append(" <!--标本号-号码 必须项未使用 -->\n");
				sb0.append("<id extension=\"\"/>\n");
				sb0.append(" <!--标本类别编码 必须项已使用 -->\n");
				
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.45\"/>\n");//---------------------------------------------
				sb0.append("<subjectOf1 typeCode=\"SBJ\">\n");
				sb0.append("<specimenCollectionProcess moodCode=\"EVN\" classCode=\"SPECCOLLECT\">\n");
				sb0.append(" <!--必须项未使用 -->\n");
				sb0.append("<code></code>\n");
				sb0.append(" <!-- 标本要求 -->\n");
				sb0.append("<text value=\"\"></text>\n");
				sb0.append("</specimenCollectionProcess>\n");
				sb0.append("</subjectOf1>\n");
				sb0.append("</specimen>\n");
				sb0.append("</specimen>\n");
				sb0.append(" <!--执行科室 -->\n");
				sb0.append("<location typeCode=\"LOC\">\n");
				sb0.append(" <!-- 执行时间 -->\n");
				sb0.append("<time>\n");
				sb0.append("<any value=\"" + compts.getPacsComponent().get(0).getItemtime() + "\"></any>\n");
				sb0.append("</time>\n");
				sb0.append("<serviceDeliveryLocation classCode=\"SDLOC\">\n");
				sb0.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
				sb0.append(" <!--执行科室编码 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"" + compts.getPacsComponent().get(0).getServiceDeliveryLocation_code()
						+ "\" root=\"1.2.156.112685.1.1.1\" />\n");
				sb0.append("</id>\n");
				sb0.append(" <!-- 执行科室名称 -->\n");
				sb0.append("<name xsi:type=\"BAG_EN\">\n");
				sb0.append("<item>\n");
				sb0.append("<part value=\"" + compts.getPacsComponent().get(0).getServiceDeliveryLocation_name() + " \" />\n");
				sb0.append("</item>\n");
				sb0.append("</name>\n");
				sb0.append("</serviceProviderOrganization>\n");
				sb0.append("</serviceDeliveryLocation>\n");
				sb0.append("</location>\n");
				sb0.append(" <!--1..n 一个申请单可以包含多个医嘱，每个医嘱对应一个component2,多个医嘱重复component2 -->\n");

				sb0.append("<component2>\n");
				sb0.append("<observationRequest classCode=\"OBS\">\n");
				sb0.append(" <!-- 医嘱号 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"" +lisid + "\" />\n");//------------------------------------
				sb0.append("</id>\n");
				sb0.append(" <!--检查项目编码 必须项已使用 -->\n");
				sb0.append(
						"<code code=\""+compts.getPacsComponent().get(0).getPacs_num()+"\" codeSystem=\"1.2.156.112685.1.1.88/1.2.156.112685.1.1.110\" >\n");
				sb0.append(" <!--检查项目名称 -->\n");
				sb0.append("<displayName value=\""+compts.getPacsComponent().get(0).getItemName()+"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<!-- 医嘱描述 -->\n");
				sb0.append("<text value=\"\"/>\n");//医嘱描述
				sb0.append(" <!-- 必须项未使用 -->\n");
				sb0.append("<statusCode />\n");
				sb0.append(" <!-- 必须项未使用 -->\n");
				sb0.append("<effectiveTime  xsi:type=\"QSC_TS\" validTimeLow=\""+DateTimeUtil.getDateTimes()+"\" validTimeHigh=\""+DateTimeUtil.getDateTimes()+"\">\n");//-------------------------------------
				sb0.append("<!-- 医嘱执行频率编码 -->\n");
				sb0.append("<code code=\"-01\" codeSystem=\"1.2.156.112685.1.1.28\">\n");
				sb0.append("<!--医嘱执行频率名称 -->\n");
				sb0.append("<displayName value=\"一次性\" />\n");
				sb0.append("</code>\n");
				sb0.append("</effectiveTime>\n");
				sb0.append(" <!--检查方法编码 -->\n");
				sb0.append("<methodCode>\n");
				sb0.append("<item code=\" \" codeSystem=\"1.2.156.112685.1.1.43\" codeSystemName=\"\">\n");
				sb0.append(" <!--检查方法名 -->\n");
				sb0.append("<displayName value=\"\"></displayName>\n");
				sb0.append("</item>\n");
				MenuDTO eu= new MenuDTO();
				eu=getOrderExecType(compts.getPacsComponent().get(0).getItemCode(),logname);
				sb0.append(" <!-- 检查类型编码 -->\n");				   
				  sb0.append("<item code=\""+eu.getValue()+"\" codeSystem=\"1.2.156.112685.1.1.129\">\n");
				  sb0.append("<!-- 检查类型名称   --> \n");
				  sb0.append("<displayName value=\""+eu.getText()+"\" /> \n");
				  sb0.append("</item>\n");
				sb0.append("</methodCode>\n");
				sb0.append(" <!--检查部位编码 -->\n");
				sb0.append("<targetSiteCode>\n");
				sb0.append("<item code=\"\" codeSystem=\"1.2.156.112685.1.1.42\" codeSystemName=\"\">\n");
				sb0.append(" <!--检查部位名称 -->\n");
				sb0.append("<displayName value=\"\"></displayName>\n");
				sb0.append("</item>\n");
				sb0.append("</targetSiteCode>\n");
				sb0.append("<component2>\n");
				sb0.append("<!-- 是否标记 -->\n");
				sb0.append("<placerGroup>\n");
				sb0.append("<!-- 是否皮试 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\"	contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"01\" codeSystem=\"1.2.156.112685.1.1.84\">\n");
				sb0.append("<displayName value=\"皮试\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"BL\" value=\"true\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!-- 是否加急 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\"	contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"03\" codeSystem=\"1.2.156.112685.1.1.84\">\n");
				sb0.append("<displayName value=\"加急\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"BL\" value=\"true\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!-- 是否药观 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\"	contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"04\" codeSystem=\"1.2.156.112685.1.1.84\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"BL\" value=\"false\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
					
				sb0.append("<!--  先诊疗后付费类型  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"CD\" code=\"\">\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</value>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!-- 收费状态标识  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\"	contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"0\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
                sb0.append("<!-- HIS执行状态  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"110.001\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"医嘱已确认\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"0\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");										
				sb0.append("<!-- 业务操作时间  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!--医嘱时间类型-->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\"/>\n");
				sb0.append("<!--医嘱时间类型编码 名称-->\n");
				sb0.append("<value xsi:type=\"CD\" code=\"01\" codeSystem=\"1.2.156.112685.1.1.82\">\n");
				sb0.append("<displayName value=\"临时医嘱\"></displayName>\n");
				sb0.append("</value>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");	
				sb0.append("<!-- 临床路径项目编号 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code code=\"\"  codeSystem=\"1.2.156.112685.1.1.120\" >\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"\"/>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
                sb0.append(" <!-- 临床路径项目序号 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"\"/>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!-- 体重 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"\"/>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!-- 身高 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"\"/>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");					
				sb0.append("</placerGroup>\n");
				sb0.append("</component2>\n");	
				
				sb0.append("</observationRequest>\n");
				sb0.append("</component2>\n");
				
				sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\">\n");
				sb0.append(" <!-- 必须项 未使用 default=false -->\n");
				sb0.append("<seperatableInd value=\"false\" />\n");
				sb0.append(" <!--申请注意事项 -->\n");
				sb0.append("<annotation>\n");
				sb0.append("<text value=\"\"></text>\n");
				sb0.append("<statusCode code=\"completed\" />\n");
				sb0.append("<author>\n");
				sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
				sb0.append("</author>\n");
				sb0.append("</annotation>\n");
				sb0.append("</subjectOf6>\n");
				sb0.append("</observationRequest>\n");
				sb0.append("</component2>\n");
				
				sb0.append(" <!--就诊 -->\n");
				sb0.append("<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"\">\n");
				sb0.append(" <!--就诊 -->\n");
				sb0.append("<encounter classCode=\"\" moodCode=\"\">\n");
				sb0.append("<id >\n");
				sb0.append(" <!-- 就诊次数 -->\n");
				String dates = DateTimeUtil.getDateTimes();		
				String jzcs=dates.substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
				sb0.append("<item extension=\""+jzcs+"\" root=\"1.2.156.112685.1.2.1.7\"/>\n");
				sb0.append("<!-- 就诊流水号 -->\n");
				String lsh=lismessage.getCustom().getPersonid()+sjh.getOthers().substring(1,sjh.getOthers().length());
				sb0.append("<item extension=\""+lsh+"\" root=\"1.2.156.112685.1.2.1.6\"/>\n");  
				sb0.append("</id>\n");
				sb0.append(" <!--病人类型编码 -->\n");
				sb0.append("<code codeSystem=\"1.2.156.112685.1.1.80\" code=\"0401\" >\n");
				sb0.append("<!-- 就诊类别名称 -->\n");
				sb0.append("<displayName value=\"干保体检\" />\n");
				sb0.append("</code>\n");
				sb0.append(" <!--必须项未使用 -->\n");
				sb0.append("<statusCode  />\n");
				sb0.append(" <!--病人 必须项未使用 -->\n");
				sb0.append("<subject typeCode=\"SBJ\">\n");
				sb0.append("<patient classCode=\"PAT\" />\n");
				sb0.append("</subject>\n");
				sb0.append(" <!--诊断(检查申请原因) -->\n");
				sb0.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">\n");
				sb0.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append(" <!--诊断类别编码 必须项已使用 -->\n");
				sb0.append("<code code=\"0401\" codeSystem=\"1.2.156.112685.1.1.29\" >\n");
				sb0.append(" <!--诊断类别名称 -->\n");
				sb0.append("<displayName value=\"干保体检\" />\n");		
				sb0.append("</code>\n");
				sb0.append(" <!-- 必须项未使用 -->\n");
				sb0.append("<statusCode code=\"\" />\n");
				sb0.append(" <!--诊断日期 -->\n");
				sb0.append("<effectiveTime>\n");
				sb0.append("<any value=\"\"></any>\n");
				sb0.append("</effectiveTime>\n");
				sb0.append(" <!-- 疾病编码 必须项已使用 -->\n");
				sb0.append("<value code=\"\" codeSystem=\"1.2.156.112685.1.1.30\">\n");
				sb0.append(" <!-- 疾病名称 -->\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</value>\n");
				sb0.append("</observationDx>\n");
				sb0.append("</pertinentInformation1>\n");

				
				sb0.append("<!-- 0..n(可循环)一个检查消息中可以由多个既往病史pertinentInformation1对应，有多个既往病史时，	  重复pertinentInformation1 -->\n"); 
				sb0.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">\n");
				sb0.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<!--既往史编码 -->\n"); 
				sb0.append("<code code=\"A18.028+M01.1*\" codeSystem=\"1.2.156.112685.1.2.2.5\" />" ); //
				sb0.append("<!-- 必须项未使用 -->\n"); 
				sb0.append("<statusCode />\n"); 
				sb0.append("<!--既往史疾病编码-->\n");
				sb0.append("<value code=\"\" codeSystem=\"1.2.156.112685.1.1.30\">"); 
				sb0.append("<!--既往史 疾病名称 -->\n");
				sb0.append("<displayName value=\" \" />"); 
				sb0.append("</value>\n"); 
				sb0.append("</observationDx>\n");
				sb0.append("</pertinentInformation1>\n");
				 
				
				 sb0.append("<!-- 主诉 -->\n");
				 sb0.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">\n");
				 sb0.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">\n");
				 sb0.append("<code codeSystem=\"1.2.156.112685.1.2.3.2\"/>\n");
				sb0.append("<!-- 必须项未使用 -->\n");
				sb0.append("<statusCode />\n");
				sb0.append("<value>\n");
								sb0.append("<!-- 主诉 -->\n");
								sb0.append("<displayName value=\"\" />\n");
							sb0.append("</value>\n");
						sb0.append("</observationDx>\n");
					sb0.append("</pertinentInformation1>\n");
					
					sb0.append("<!--过敏信息（可循环），有多个过敏信息时，重复pertinentInformation1-->\n");
					sb0.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">\n");
						sb0.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">\n");
							sb0.append("<!--过敏信息编码 -->\n");
							sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\" />\n");
							sb0.append("<!--必须项未使用 -->\n");
							sb0.append("<statusCode />\n");
							sb0.append("<!--过敏时间-->\n");
							sb0.append("<effectiveTime>\n");
								sb0.append("<any value=\"\"/>\n");
							sb0.append("</effectiveTime>\n");
							sb0.append("<!--过敏原编码/过敏原名称-->\n");
							sb0.append("<value xsi:type=\"CD\" code=\"\" codeSystem=\"1.2.156.112685.1.1.13\" >\n");
								sb0.append("<displayName value=\"\" />\n");
								sb0.append("<!--过敏信息文本描述-->\n");
								sb0.append("<originalText value=\"\" />\n");
							sb0.append("</value>\n");
						sb0.append("</observationDx>\n");
					sb0.append("</pertinentInformation1>\n");

				sb0.append("</encounter>\n");
				sb0.append("</componentOf1>\n");
				sb0.append("</placerGroup>\n");
				sb0.append("</subject>\n");
				sb0.append("</controlActProcess>\n");
				sb0.append("</POOR_IN200901UV>\n");
				xml=(sb.toString()+sb0.toString());
		}
		return xml; 
	}
	

	private String getStatus(PacsComponents compts,String status,String statusname,String datetime,String logname){
		StringBuffer sb004=new StringBuffer();
		int lisid=getzl_req_pacs_item(lismessage.getCustom().getExam_num(),compts.getReq_no(),compts.getPacsComponent().get(0).getItemCode(),logname);
		if(lisid>0){
		
		sb004.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb004.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3  ../../Schemas/POOR_IN200901UV23.xsd\">\n");
		sb004.append("	<!-- 消息ID -->\n");
		sb004.append("	<id extension=\"BS004\" />\n");
		sb004.append("	<!-- 消息创建时间 -->\n");
		sb004.append("	<creationTime value=\"" + datetime + "\" />\n");
		sb004.append("	<!-- 交互ID -->\n");
		sb004.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"POOR_IN200901UV23\" />\n");
		sb004.append("	<!--消息用途: P(Production); D(Debugging); T(Training) -->\n");
		sb004.append("	<processingCode code=\"P\" />\n");
		sb004.append("	<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) -->\n");
		sb004.append("	<processingModeCode code=\"R\" />\n");
		sb004.append("	<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->\n");
		sb004.append("	<acceptAckCode code=\"NE\" />\n");
		sb004.append("	<!-- 接受者 -->\n");
		sb004.append("	<receiver typeCode=\"RCV\">\n");
		sb004.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb004.append("			<!-- 接受者ID -->\n");
		sb004.append("			<id>\n");
		sb004.append("				<item root=\"1.2.156.112685.1.1.19\" extension=\"\"/>\n");
		sb004.append("			</id>\n");
		sb004.append("		</device>\n");
		sb004.append("	</receiver>\n");
		sb004.append("	<!-- 发送者 -->\n");
		sb004.append("	<sender typeCode=\"SND\">\n");
		sb004.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb004.append("			<!-- 发送者ID -->\n");
		sb004.append("			<id>\n");
		sb004.append("				<item root=\"1.2.156.112685.1.1.19\" extension=\"S040\"/>\n");
		sb004.append("			</id>\n");
		sb004.append("		</device>\n");
		sb004.append("	</sender>\n");
		sb004.append("	<!-- 封装的消息内容(按Excel填写) -->\n");
		sb004.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
		sb004.append("		<!-- 消息交互类型 @code: 新增 :new 修改:update -->\n");
		sb004.append("		<code code=\"update\"></code>\n");
		sb004.append("		<subject typeCode=\"SUBJ\" xsi:nil=\"false\">\n");
		sb004.append("			<placerGroup>\n");
		sb004.append("				<!-- 必须项未使用 -->\n");
		sb004.append("				<code></code>\n");
		sb004.append("				<!-- 检验申请单状态 必须项未使用 -->\n");
		sb004.append("				<statusCode code=\"active\"></statusCode>\n");
		sb004.append("				<!-- 患者信息 -->\n");
		sb004.append("				<subject typeCode=\"SBJ\">\n");
		sb004.append("					<patient classCode=\"PAT\">\n");
		sb004.append("						<id>\n");
		sb004.append("							<!--域ID -->\n");
		sb004.append("							<item root=\"1.2.156.112685.1.2.1.2\" extension=\"01\" />\n");
		sb004.append("							<!-- 患者ID -->\n");
		sb004.append("							<item root=\"1.2.156.112685.1.2.1.3\" extension=\"" + lismessage.getCustom().getPersonid() + "\" />\n");
		sb004.append("							<!-- 就诊号 -->\n");
		ExamInfoUserDTO sjh=getHISDJH(lismessage.getCustom().getExam_num());
		sb004.append("            				<item root=\"1.2.156.112685.1.2.1.12\" extension=\"" + sjh.getClinic_no()+ "\" />\n");
		sb004.append("						</id>\n");
		sb004.append("						<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
		sb004.append("							<!--病人科室编码-->\n");
		sb004.append("							<id>\n");
		sb004.append("								<item extension=\"0176\" root=\"1.2.156.112685.1.1.1\"/>\n");
		sb004.append("							</id>\n");
		sb004.append("							<!--病人科室名称 -->\n");
		sb004.append("							<name xsi:type=\"BAG_EN\">\n");
		sb004.append("								<item>\n");
		sb004.append("									<part value=\"体检中心\" />\n");
		sb004.append("								</item>\n");
		sb004.append("							</name>\n");
		sb004.append("							<asOrganizationPartOf classCode=\"PART\">\n");
		sb004.append("								<wholeOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb004.append("									<!--医疗机构代码 -->\n");
		sb004.append("									<id>\n");
		sb004.append("										<item extension=\"46014326-4\"/>\n");
		sb004.append("									</id>\n");
		sb004.append("									<!--医疗机构名称 -->\n");
		sb004.append("									<name xsi:type=\"BAG_EN\">\n");
		sb004.append("										<item><part value=\"包头市中心医院\" /></item>\n");
		sb004.append("									</name>\n");
		sb004.append("								</wholeOrganization>\n");
		sb004.append("							</asOrganizationPartOf>\n");
		sb004.append("						</providerOrganization>\n");							
		sb004.append("					</patient>\n");
		sb004.append("				</subject>\n");
		sb004.append("				<!-- 操作人 -->\n");
		sb004.append("				<performer typeCode=\"PRF\">\n");
		sb004.append("					<time>\n");
		sb004.append("						<!-- 操作日期 -->\n");
		sb004.append("						<any value=\"" + datetime+ "\"></any>\n");
		sb004.append("					</time>\n");
		sb004.append("					<assignedEntity classCode=\"ASSIGNED\">\n");
		sb004.append("						<!-- 操作人编码 -->\n");
		sb004.append("						<id>\n");
		sb004.append("							<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\"></item>\n");
		sb004.append("						</id>\n");
		sb004.append("						<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">\n");
		sb004.append("							<!-- 操作人姓名 必须项已使用 -->\n");
		sb004.append("							<name xsi:type=\"BAG_EN\">\n");
		sb004.append("								<item>\n");
		sb004.append("									<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />\n");
		sb004.append("								</item>\n");
		sb004.append("							</name>\n");
		sb004.append("						</assignedPerson>\n");
		sb004.append("					</assignedEntity>\n");
		sb004.append("				</performer>\n");
		sb004.append("				<!--执行科室 -->\n");
		sb004.append("				<location typeCode=\"LOC\" xsi:nil=\"false\">\n");
		sb004.append("					<!--必须项未使用 -->\n");
		sb004.append("					<time />\n");
		sb004.append("					<!--就诊机构/科室 -->\n");
		sb004.append("					<serviceDeliveryLocation classCode=\"SDLOC\">\n");
		sb004.append("						<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb004.append("							<!--执行科室编码 -->\n");
		sb004.append("							<id>\n");
		sb004.append("								<item extension=\"" + compts.getPacsComponent().get(0).getServiceDeliveryLocation_code()
						+ "\" root=\"1.21.156.112685.1.1.1\" />\n");
		sb004.append("							</id>\n");
		sb004.append("							<!--执行科室名称 -->\n");
		sb004.append("							<name xsi:type=\"BAG_EN\">\n");
		sb004.append("								<item>\n");
		sb004.append("									<part value=\"" + compts.getPacsComponent().get(0).getServiceDeliveryLocation_name() + "\" />\n");
		sb004.append("								</item>\n");
		sb004.append("							</name>\n");
		sb004.append("						</serviceProviderOrganization>\n");
		sb004.append("					</serviceDeliveryLocation>\n");
		sb004.append("				</location>\n");						
		sb004.append("				<!-- 1..n可循环  医嘱状态信息 -->\n");
		
		sb004.append("				<component2>\n");
		sb004.append("					<!--医嘱序号-->\n");
		sb004.append("					<sequenceNumber value=\"1\"/>\n");
		sb004.append("					<observationRequest classCode=\"OBS\">\n");
		sb004.append("						<!-- 必须项已使用 -->\n");
		sb004.append("						<id>\n");
		sb004.append("							<!-- 医嘱号 -->\n");
		sb004.append("							<item extension=\""+lisid+"\" root=\"1.2.156.112685.1.2.1.22\"/>\n");
		sb004.append("							<!-- 申请单号 -->\n");
		sb004.append("							<item extension=\""+compts.getReq_no()+"\" root=\"1.2.156.112685.1.2.1.21\"/>\n");
		sb004.append("							<!-- 报告号 -->\n");
		sb004.append("							<item extension=\"\" root=\"1.2.156.112685.1.2.1.24\"/>\n");
		sb004.append("							<!-- StudyInstanceUID -->\n");
		sb004.append("							<item extension=\"\" root=\"1.2.156.112685.1.2.1.30\"/>\n");
		sb004.append("						</id>\n");
		sb004.append("						<!-- 医嘱类别编码/医嘱类别名称 - 针剂药品, 材料类, 治疗类, 片剂药品, 化验类 -->\n");
		sb004.append("						<code code=\"D\" codeSystem=\"1.2.156.112685.1.1.27\">\n");
		sb004.append("							<displayName value=\"检查\" />\n");
		sb004.append("						</code>\n");
		sb004.append("						<!-- 必须项未使用 -->\n");
		sb004.append("						<statusCode />\n");
		sb004.append("						<!-- 必须项未使用 -->\n");
		sb004.append("						<effectiveTime xsi:type=\"IVL_TS\" />\n");
		sb004.append("						<!-- 标本信息 -->\n");
		sb004.append("						<specimen typeCode=\"SPC\">\n");
		sb004.append("							<specimen classCode=\"SPEC\">\n");
		sb004.append("								<!--标本条码号 必须项已使用 -->\n");
		sb004.append("								<id extension=\"" + compts.getReq_no() + "\" />\n");
		sb004.append("								<!--必须项目未使用 -->\n");
		sb004.append("								<code />\n");
		sb004.append("								<subjectOf1 typeCode=\"SBJ\" contextControlCode=\"OP\">\n");
		sb004.append("									<specimenProcessStep moodCode=\"EVN\" classCode=\"SPECCOLLECT\">\n");
		sb004.append("										<!-- 采集日期 -->\n");
		sb004.append("										<effectiveTime xsi:type=\"IVL_TS\">\n");
		sb004.append("											<any value=\"" + datetime+ "\"></any>\n");
		sb004.append("										</effectiveTime>\n");
		sb004.append("										<performer typeCode=\"PRF\">\n");
		sb004.append("											<assignedEntity classCode=\"ASSIGNED\">\n");
		sb004.append("												<!-- 采集人Id -->\n");
		sb004.append("												<id>\n");
		sb004.append("													<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\"></item>\n");
		sb004.append("												</id>\n");
		sb004.append("												<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">\n");
		sb004.append("													<!-- 采集人姓名 -->\n");
		sb004.append("													<name xsi:type=\"BAG_EN\">\n");
		sb004.append("														<item>\n");
		sb004.append("															<part value=\"" + lismessage.getDoctor().getDoctorName() + " \" />\n");
		sb004.append("														</item>\n");
		sb004.append("													</name>\n");
		sb004.append("												</assignedPerson>\n");
		sb004.append("											</assignedEntity>\n");
		sb004.append("										</performer>\n");
		sb004.append("									</specimenProcessStep>\n");
		sb004.append("								</subjectOf1>\n");
		sb004.append("							</specimen>\n");
		sb004.append("						</specimen>\n");
		sb004.append("						<!-- 原因 -->\n");
		sb004.append("						<reason contextConductionInd=\"true\">\n");
		sb004.append("							<observation moodCode=\"EVN\" classCode=\"OBS\">\n");
		sb004.append("								<!-- 必须项 未使用-->\n");
		sb004.append("								<code></code>\n");
		sb004.append("								<value xsi:type=\"ST\" value=\"医嘱撤消原因\"/>\n");
		sb004.append("							</observation>\n");
		sb004.append("						</reason>\n");							
		sb004.append("						<!-- 医嘱执行状态 -->\n");
		sb004.append("						<component1 contextConductionInd=\"true\">\n");
		sb004.append("							<processStep classCode=\"PROC\">\n");
		sb004.append("								<code code=\""+status+"\" codeSystem=\"1.2.156.112685.1.1.93\">\n");
		sb004.append("									<!--医嘱执行状态名称 -->\n");
		sb004.append("									<displayName value=\""+statusname+"\" />\n");
		sb004.append("								</code>\n");
		sb004.append("							</processStep>\n");
		sb004.append("						</component1>\n");
		sb004.append("					</observationRequest>\n");
		sb004.append("				</component2>\n");
		sb004.append("				<!--就诊 -->\n");
		sb004.append("				<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"COMP\">\n");
		sb004.append("					<!--就诊 -->\n");
		sb004.append("					<encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
		sb004.append("						<id>\n");
		sb004.append("							<!-- 就诊次数 必须项已使用 -->\n");
		String dates = DateTimeUtil.getDateTimes();		
		String jzcs=dates.substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
		sb004.append("                         <item extension=\""+jzcs+"\" root=\"1.2.156.112685.1.2.1.7\"/>\n");
		sb004.append("                         <!-- 就诊流水号 -->\n");
		String lsh=lismessage.getCustom().getPersonid()+sjh.getOthers().substring(1,sjh.getOthers().length());
		sb004.append("                         <item extension=\""+lsh+"\" root=\"1.2.156.112685.1.2.1.6\"/>\n");  
		sb004.append("						</id>\n");
		sb004.append("						<!--就诊类别编码-->\n");
		sb004.append("						<code codeSystem=\"1.2.156.112685.1.1.80\" code=\"0401\">\n");
		sb004.append("							<!-- 就诊类别名称 -->\n");
		sb004.append("							<displayName value=\"干保体检\" />\n");
		sb004.append("						</code>\n");
		sb004.append("						<!--必须项未使用 -->\n");
		sb004.append("						<statusCode code=\"Active\" />\n");
		sb004.append("						<!--病人 必须项未使用 -->\n");
		sb004.append("						<subject typeCode=\"SBJ\">\n");
		sb004.append("							<patient classCode=\"PAT\" />\n");
		sb004.append("						</subject>\n");
		sb004.append("					</encounter>\n");
		sb004.append("				</componentOf1>\n");
		sb004.append("			</placerGroup>\n");
		sb004.append("		</subject>\n");
		sb004.append("	</controlActProcess>\n");
		sb004.append("</POOR_IN200901UV>\n");
		}		
		return sb004.toString();
	}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getHISDJH(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT zl_djh as others,zl_tjh as visit_no,zl_mzh as clinic_no FROM zl_req_patInfo where exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);	
		}
		return eu;
	} 
	
	/**
	 * 获取医嘱执行分类编码
	 * @param url
	 * @param view_num
	 * @return
	 */
	public String getOrderExecId(String cicode,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		String eu="";
		PreparedStatement preparedStatement = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.remark from charging_item a where a.item_code='"+cicode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				eu = rs1.getString("remark");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		if(eu==null){
			eu="0000";
		}else if(eu.trim().length()<=0){
			eu="0000";
		}else if("超声".equals(eu.trim())){
			eu="0101";
		}else if("CT".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("DR".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("MRI".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("内窥镜".equals(eu.trim().toUpperCase())){
			eu="0105";
		}else if("数字胃肠".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("病理".equals(eu.trim().toUpperCase())){
			eu="0104";
		}else if("PET".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("X线".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("乳腺".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("心电".equals(eu.trim().toUpperCase())){
			eu="0106";
		}else{
			eu="0000";
		}
		return eu;
	} 
	
	/**
	 * 标本类型编码
	 * @param url
	 * @param view_num
	 * @return
	 */
	public MenuDTO getOrderExecType(String cicode,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		MenuDTO eu= new MenuDTO();
		PreparedStatement preparedStatement = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.remark from charging_item a where a.item_code='"+cicode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				eu.setText(rs1.getString("remark"));
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		if((eu==null)||(eu.getText()==null)){
			eu.setValue("OT");
			eu.setText("");
		}else if(eu.getText().trim().length()<=0){
			eu.setValue("OT");
		}else if("超声".equals(eu.getText().trim())){
			eu.setValue("US");
		}else if("CT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("CT");
		}else if("DR".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DX");
		}else if("MRI".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("MRI");
		}else if("内窥镜".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("ES");
		}else if("ECT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("NM");
		}else if("彩色多普勒".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("CD");
		}else if("双多普勒".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DD");
		}else if("数字胃肠".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("RF");
		}else if("SPECT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("ST");
		}else if("雷射表面扫描".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("LS");
		}else if("病理".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DG");
		}else if("PET".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("PT");
		}else if("X线".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("XA");
		}else if("乳腺".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("MG");
		}else if("心电图".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("ECG");
		}else if("放疗图像".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("RTIMAGE");
		}else{
			eu.setValue("OT");
		}
		return eu;
	} 
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
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
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_pacs_item(String exam_num,String req_id,String cicode,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_pacs_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_ids='"+cicode+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,charging_item_ids,req_id,createdate) values('" 
				+ ei.getId() + "','" +cicode + "','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +insertsql);				
				preparedStatement = tjtmpconnect.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.executeUpdate();
				ResultSet rs = null;
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next())
					lisid = rs.getInt(1);
				
				rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int getzl_req_pacs_item(String exam_num,String req_id,String cicode,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
							
				String sb1 = "select a.id from zl_req_pacs_item a where a.exam_info_id='"+ei.getId()+"' and a.charging_item_ids='"
				+cicode+"' and a.req_id='"+req_id+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
				tjtmpconnect.createStatement().execute(sb1);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				if (rs1.next()) {
					lisid=rs1.getInt("id");
				}
				rs1.close();
				
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
 }
