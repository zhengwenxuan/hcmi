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
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.fangzheng.MQ.mqSendSample;
import com.hjw.wst.DTO.ExamInfoUserDTO;
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
public class LisSendMessageFZ{
private LisMessageBody lismessage;
private static JdbcQueryManager jdbcQueryManager;
static {
	init();
}

public static void init() {
	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
}

	public LisSendMessageFZ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
		mqSendSample mqSendSample =null;
		try {			
			String ip = url.split("&")[0];
			int port = Integer.valueOf(url.split("&")[1]);
			mqSendSample = new mqSendSample();
			// int port = 5000;
			mqSendSample.initEnvironment(ip, port);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
				try {
					List<String> slist = new ArrayList<String>();
					slist = lisSendMessage(comps,logname);
					for (String xml : slist) {
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
						String messages = mqSendSample.msgSend(xml, "BS006","0401","0");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					}
					
					long datetime=Long.valueOf(DateTimeUtil.getDateTimes());	
					List<String> stauts=new ArrayList<String>();
					stauts=this.getStatus(comps, "100.001", "医嘱已下达",datetime+"", logname);
					for (String xml : stauts) {
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
						String messages = mqSendSample.msgSend(xml, "BS004","0401","0");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					}
					
					datetime=Long.valueOf(DateTimeUtil.getDateTimes())+1;	
					stauts=this.getStatus(comps, "110.001", "医嘱已确认",datetime+"", logname);
					for (String xml : stauts) {
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
						String messages = mqSendSample.msgSend(xml, "BS004","0401","0");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					}
					
					datetime=Long.valueOf(DateTimeUtil.getDateTimes())+2;	
					stauts=this.getStatus(comps, "120.001", "标本已采集",datetime+"", logname);
					for (String xml : stauts) {
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
						String messages = mqSendSample.msgSend(xml, "BS004","0401","0");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					}
					
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("pacs解析返回值错误");
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
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

	private List<String> lisSendMessage(LisComponents comps,String logname) {
		List<String> slist=new ArrayList<String>();
		StringBuffer sb = new StringBuffer("");
       
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xsi:schemaLocation=\"urn:hl7-org:v3 ../../Schemas/POOR_IN200901UV20.xsd\">\n");
		sb.append("<!-- 消息ID -->\n");
		sb.append("<id extension=\"BS006\" />\n");
		sb.append("<!-- 消息创建时间 -->\n");
		sb.append("<creationTime value=\"" + lismessage.getCreationTime_value() + "\" />\n");
		sb.append("<!-- 交互ID -->\n");
		sb.append("<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"POOR_IN200901UV21\" />\n");
		sb.append("<!-- 消息用途: P(Production); D(Debugging); T(Training) -->\n");
		sb.append("<processingCode code=\"P\" />\n");
		sb.append("<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) -->\n");
		sb.append("<processingModeCode code=\"R\" />");
		sb.append("<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->\n");
		sb.append("<acceptAckCode code=\"NE\" />\n");
		sb.append("<!-- 接受者 -->\n");
		sb.append("<receiver typeCode=\"RCV\">\n");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("<!-- 接受者ID -->\n");
		sb.append("<id>\n");
		sb.append("<item root=\"1.2.156.112685.1.1.19\" extension=\"\"/>\n");
		sb.append("</id>\n");
		sb.append("</device>\n");
		sb.append("</receiver>\n");
		sb.append("<!-- 发送者 -->\n");
		sb.append("<sender typeCode=\"SND\">\n");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("<!-- 发送者ID -->\n");
		sb.append("<id>\n");
		sb.append("<item root=\"1.2.156.112685.1.1.19\" extension=\"S040\"/>\n");
		sb.append("</id>\n");
		sb.append("</device>\n");
		sb.append("</sender>\n");
		sb.append("<!-- 封装的消息内容(按Excel填写) -->\n");
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
		sb.append("<!-- 消息交互类型 @code: 新增 :new 删除：delete-->\n");
		sb.append("<code code=\"new\"></code>\n");
		sb.append("<subject typeCode=\"SUBJ\" xsi:nil=\"false\">\n");
		sb.append("<placerGroup classCode=\"GROUPER\" moodCode=\"RQO\">\n");
		sb.append("<subject typeCode=\"SBJ\">\n");
		sb.append("<patient classCode=\"PAT\">\n");
		sb.append("<id>\n");
		sb.append("<!-- 域ID -->\n");
		sb.append("<item root=\"1.2.156.112685.1.2.1.2\" extension=\"01\" />\n");
		sb.append("<!-- 患者ID -->\n");
		sb.append(
				"<item root=\"1.2.156.112685.1.2.1.3\" extension=\"" + lismessage.getCustom().getPersonid() + "\" />\n");
		sb.append("<!-- 就诊号 -->\n");
		ExamInfoUserDTO sjh=getHISDJH(lismessage.getCustom().getExam_num());
		sb.append(
				"<item root=\"1.2.156.112685.1.2.1.12\" extension=\"" + sjh.getClinic_no()+ "\" />\n");
		sb.append("<!-- 收据单号 -->\n");		
		sb.append("<item root=\"1.2.156.112685.1.2.1.42\" extension=\""+sjh.getOthers()+"\" />\n");
		sb.append("</id>\n");
		sb.append("<!-- 病区编码/病区名 床号 -->\n");
		sb.append("<addr xsi:type=\"BAG_AD\">\n");
		sb.append("<item use=\"TMP\">\n");
		sb.append(
				"<part type=\"BNR\" value=\"\" code=\"\" codeSystem=\"1.2.156.112685.1.1.33\"/>\n");
		sb.append("<part type=\"CAR\" value=\"\" />\n");
		sb.append("</item>\n");
		sb.append("</addr>\n");
		sb.append("<!--个人信息 必须项已使用 -->\n");
		sb.append("<patientPerson classCode=\"PSN\">\n");
		sb.append("<!-- 身份证号/医保卡号 -->\n");
		sb.append("<id>\n");
		sb.append("<!-- 身份证号 -->\n");
		sb.append("<item extension=\"" + lismessage.getCustom().getPersonidnum()
				+ "\" root=\"1.2.156.112685.1.2.1.9\" />\n");
		sb.append("</id>\n");
		sb.append("<!--姓名 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getCustom().getName() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("<!-- 联系电话 -->\n");
		sb.append("<telecom xsi:type=\"BAG_TEL\">\n");
		sb.append("<!-- 联系电话 -->\n");
		sb.append("<item use=\"EC\" value=\"" + lismessage.getCustom().getTel() + "\"></item>\n");
		sb.append("<!--移动电话 -->\n");
		sb.append("<item use=\"MC\" value=\"" + lismessage.getCustom().getTel() + "\"></item>\n");
		sb.append("</telecom>\n");
		sb.append("<!--性别代码 -->\n");
		sb.append("<administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" codeSystem=\"1.2.156.112685.1.1.3\" />\n");
		sb.append("<!--出生日期 -->\n");
		sb.append("<birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\">\n");
		sb.append("<!--年龄 -->\n");
		sb.append("<originalText value=\"" + lismessage.getCustom().getOld() + "\" />\n");
		sb.append("</birthTime>\n");
		sb.append("<!--住址 -->\n");
		sb.append("<addr xsi:type=\"BAG_AD\">\n");
		sb.append("<item use=\"H\">\n");
		sb.append("<part type=\"AL\" value=\"" + lismessage.getCustom().getAddress() + "\" />\n");
		sb.append("<!--邮政编码 -->\n");
		sb.append("<part type=\"ZIP\" value=\"\" />\n");
		sb.append("</item>\n");
		sb.append("</addr>\n");		
		sb.append("<!--雇佣关系 -->\n");
		sb.append("<asEmployee classCode=\"EMP\">\n");
		sb.append("<!--工作单位 -->\n");
		sb.append("<employerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append("<!-- 工作单位代码 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"\"></item>\n");
		sb.append("</id>\n");
		sb.append("<!--工作单位名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getCustom().getComname() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("<contactParty classCode=\"CON\" />\n");
		sb.append("</employerOrganization>\n");
		sb.append("</asEmployee>\n");
		sb.append("</patientPerson>\n");
		sb.append("<providerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append("<!-- 必须项 未使用 -->\n");
		sb.append("<id><item extension=\"\" root=\"1.2.156.112685.1.1.1\"/></id>\n");
		sb.append("<!--申请医院 保留可不填 -->\n");
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
		sb.append("<!--开医嘱者/送检医生 -->\n");
		sb.append("<author typeCode=\"AUT\">\n");
		sb.append("<!-- 开单时间 -->\n");
		sb.append("<time value=\"" + lismessage.getDoctor().getTime() + "\"></time>\n");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append("<!--开单医生编码 -->\n");
		sb.append("<id>\n");
		sb.append(
				"<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\"></item>\n");
		sb.append("</id>\n");
		sb.append("<!--开单医生姓名 -->\n");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</assignedPerson>\n");
		sb.append("<!-- 申请科室信息 -->\n");
		sb.append("<representedOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append("<!--申请科室编码 必须项已使用 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"0176\" root=\"1.2.156.112685.1.1.1\" />\n");
		sb.append("</id>\n");
		sb.append("<!--申请科室名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item use=\"ABC\">\n");
		sb.append("<part value=\"体检中心\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</representedOrganization>\n");
		sb.append("</assignedEntity>\n");
		sb.append("</author>\n");
		sb.append("<!-- 录入人 -->\n");
		sb.append("<transcriber typeCode=\"TRANS\">\n");
		sb.append("<time>\n");
		sb.append("<!-- 录入日期 开始时间 -->\n");
		sb.append("<low value=\""+DateTimeUtil.getDateTimes()+"\"></low>\n");
		sb.append("<!-- 录入日期 结束时间 -->\n");
		sb.append("<high value=\""+DateTimeUtil.getDateTimes()+"\"></high>\n");
		sb.append("</time>\n");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append("<!-- 录入人 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\"></item>\n");
		sb.append("</id>\n");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">\n");
		sb.append("<!-- 录入人姓名 必须项已使用 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item use=\"ABC\">\n");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\"/>\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</assignedPerson>\n");
		sb.append("</assignedEntity>\n");
		sb.append("</transcriber>\n");
		
		sb.append("<!-- 确认人 -->\n");
		sb.append("<verifier typeCode=\"VRF\" xsi:nil=\"false\">\n");
		sb.append("<!--确认时间 -->\n");
		sb.append("<time value=\"" + lismessage.getDoctor().getTime() + "\"></time>\n");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append("<!--确认人编码 -->\n");
		sb.append("<id>\n");
		sb.append(
				"<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\" />\n");
		sb.append("</id>\n");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\" xsi:nil=\"false\">\n");
		sb.append("<!--确认人姓名 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("</assignedPerson>\n");
		sb.append("</assignedEntity>\n");
		sb.append("</verifier>\n");
		
					for (LisComponent comp : comps.getItemList()) {	
						int lisid=updatezl_req_item(lismessage.getCustom().getExam_num(),comps.getReq_no(),comp.getChargingItemid(),logname);
					if(lisid>0){
						StringBuffer sb0=new StringBuffer(); 
				sb0.append("<!--1..n 一个检验消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2 -->\n");
				sb0.append("<component2 typeCode=\"COMP\">\n");
				sb0.append("<!--申请单信息Begin -->\n");
				sb0.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">\n");
				sb0.append("<!--检验申请单编号 必须项已使用 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\""+comps.getReq_no()+"\" root=\"1.2.156.112685.1.2.1.21\"/>\n");
				sb0.append("</id>\n");
				//sb0.append("<costs value=\""+comps.getCosts()+"\" />");
				//sb0.append("<RELEVANT_CLINIC_DIAG value=\"\"/>");
				sb0.append("<!-- 医嘱类型 必须项目已使用 -->\n");
				sb0.append("<code code=\"C\" codeSystem=\"1.2.156.112685.1.1.27\">\n");
				sb0.append("<!-- 医嘱类型名称 -->\n");
				sb0.append("<displayName value=\"检验\" />\n");
				sb0.append("</code>\n");
				sb0.append("<!-- 检验申请单状态 必须项未使用 -->\n");
				sb0.append("<statusCode></statusCode>\n");
				sb0.append("<!-- 检验申请日期 -->\n");
				sb0.append("<effectiveTime xsi:type=\"IVL_TS\">\n");
				sb0.append("<any value=\""+DateTimeUtil.getDateTimes()+"\"></any>\n");
				sb0.append("</effectiveTime>\n");
				sb0.append("<!-- 是否私隐 -->\n");
				sb0.append("<confidentialityCode>\n");
				sb0.append("<item code=\"\" codeSystem=\"1.2.156.112685.1.1.84\"></item>\n");
				sb0.append("</confidentialityCode>\n");
				
				
				sb0.append("<!--1..n 一个申请单可以包含多个医嘱，每个医嘱对应一个component2,多个医嘱重复component2 -->\n");
				sb0.append("<component2 typeCode=\"COMP\">\n");
				sb0.append("<!--医嘱项目信息Begin -->\n");
				sb0.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">\n");
				sb0.append("<!-- 医嘱号 必须项已使用 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\""+lisid+"\"></item>\n");
				sb0.append("</id>\n");
				sb0.append("<!-- 检验项目编码 必须项使用 -->\n");
				sb0.append("<code code=\""+comp.getItemCode()+"\" codeSystem=\"1.2.156.112685.1.1.46\">\n");
				sb0.append("<displayName value=\""+comp.getItemName()+"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<!--项目价格--> \n");
				sb0.append("<statusCode/>\n");
				sb0.append("<effectiveTime xsi:type=\"QSC_TS\" validTimeLow=\""+DateTimeUtil.getDateTimes()+"\" validTimeHigh=\""+DateTimeUtil.getDateTimes()+"\">\n");
				sb0.append("<!-- 医嘱执行频率编码 -->\n");
				sb0.append("<code code=\"-01\" codeSystem=\"1.2.156.112685.1.1.28\">\n");
				sb0.append("<!--医嘱执行频率名称 -->\n");
				sb0.append("<displayName value=\"一次性\" />\n");
				sb0.append("</code>\n");
				sb0.append("</effectiveTime>\n");
				sb0.append("<!-- 检验项目优先级别 -->\n");
				sb0.append("<priorityCode code=\"\" />\n");
				sb0.append("<!-- 选择血糖或者胰岛素等项目时用 -->\n");
				sb0.append("<methodCode>\n");
				sb0.append("<!-- 检验描述编码 -->\n");
				sb0.append("<item code=\""+lisid+"\" codeSystem=\"1.2.156.112685.1.1.116\">\n");
					sb0.append("<!-- 检验描述名称 -->\n");
				sb0.append("<displayName value=\"\"></displayName>\n");
				sb0.append("</item>\n");
				sb0.append("</methodCode>\n");	
								sb0.append("<!-- 采集部位 -->\n");
				sb0.append("<targetSiteCode>\n");
				sb0.append("<item code=\""+comp.getTargetSiteCode()+"\" />\n");
				sb0.append("</targetSiteCode>\n");
				sb0.append("<specimen typeCode=\"SPC\">\n");
				sb0.append("<specimen classCode=\"SPEC\">\n");
										sb0.append("<!--标本号/条码号 必须项已使用 -->\n");
				sb0.append("<id extension=\""+comp.getExtension()+"\" />\n");
										sb0.append("<!--标本角色代码（patient,group,blind) 必须项目未使用 -->\n");
				sb0.append("<code />\n");
				
				sb0.append("<specimenNatural determinerCode=\"INSTANCE\" classCode=\"ENT\">\n");				
				//ExamInfoUserDTO sdemo=new ExamInfoUserDTO();
				//sdemo = getSamDemo(comp.getChargingItemid(),logname);
				/*if((sdemo!=null)&&(sdemo.getExam_num().trim().length()>0)){
					sb0.append("<!--标本类型 血清/血浆/尿 标本类别代码 -->\n");
					sb0.append("<code code=\""+sdemo.getExam_num()+"\" codeSystem=\"1.2.156.112685.1.1.45\">\n");
					sb0.append("<!--标本类型名称 -->\n");
					sb0.append("<displayName value=\""+sdemo.getArch_num()+"\" />\n");
					sb0.append("</code>\n");
				} else {*/
					sb0.append("<!--标本类型 血清/血浆/尿 标本类别代码 -->\n");
					sb0.append(
							"<code code=\"" + comp.getSpecimenNatural() + "\" codeSystem=\"1.2.156.112685.1.1.45\">\n");
					sb0.append("<!--标本类型名称 -->\n");
					sb0.append("<displayName value=\"" + comp.getSpecimenNaturalname() + "\" />\n");
					sb0.append("</code>\n");
				//}
				sb0.append("</specimenNatural>\n");
				sb0.append("<subjectOf1 typeCode=\"SBJ\" contextControlCode=\"OP\">\n");
				sb0.append("<specimenProcessStep moodCode=\"EVN\" classCode=\"SPECCOLLECT\">\n");
												sb0.append("<!-- 采集日期 -->\n");
				sb0.append("<effectiveTime xsi:type=\"IVL_TS\">\n");
				sb0.append("<any value=\""+comp.getItemtime()+"\"></any>\n");
				sb0.append("</effectiveTime>\n");
				sb0.append("<subject typeCode=\"SBJ\" contextControlCode=\"OP\">\n");
				sb0.append("<specimenInContainer classCode=\"CONT\">\n");
				sb0.append("<container determinerCode=\"INSTANCE\" classCode=\"CONT\">\n");
															sb0.append("<!--测试项目容器ID -->\n");
				sb0.append("<code code=\"\"></code>\n");
															sb0.append("<!-- 测试项目容器 -->\n");
				sb0.append("<desc value=\"\" />\n");
				sb0.append("</container>\n");
				sb0.append("</specimenInContainer>\n");
				sb0.append("</subject>\n");
				sb0.append("<performer typeCode=\"PRF\">\n");
				sb0.append("<assignedEntity classCode=\"ASSIGNED\">\n");
														sb0.append("<!-- 采集人Id -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"1.2.156.112685.1.1.2\"></item>\n");
				sb0.append("</id>\n");
				sb0.append("<assignedPerson determinerCode=\"INSTANCE\"	classCode=\"PSN\">\n");
															sb0.append("<!-- 采集人姓名 -->\n");
				sb0.append("<name xsi:type=\"BAG_EN\">\n");
				sb0.append("<item>\n");
				sb0.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\"/>\n");
				sb0.append("</item>\n");
				sb0.append("</name>\n");
															sb0.append("<!-- 采集地点 -->\n");
				sb0.append("<asLocatedEntity classCode=\"LOCE\">\n");
				sb0.append("<addr xsi:type=\"BAG_AD\">\n");
				sb0.append("<item use=\"WP\">\n");
				sb0.append("<part type=\"BNR\" value=\"\" />\n");
				sb0.append("</item>\n");
				sb0.append("</addr>\n");
				sb0.append("</asLocatedEntity>\n");
				sb0.append("</assignedPerson>\n");
				sb0.append("</assignedEntity>\n");
				sb0.append("</performer>\n");
				sb0.append("</specimenProcessStep>\n");
				sb0.append("</subjectOf1>\n");
				sb0.append("</specimen>\n");
				sb0.append("</specimen>\n");
								sb0.append("<!-- 执行科室 -->\n");
				sb0.append("<location typeCode=\"LOC\">\n");
				sb0.append("<serviceDeliveryLocation classCode=\"SDLOC\">\n");
				sb0.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
											sb0.append("<!-- 执行科室编码 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\""+comp.getServiceDeliveryLocation_code()+"\" root=\"1.2.156.112685.1.1.1\"></item>\n");
				sb0.append("</id>\n");
											sb0.append("<!--执行科室名称 -->\n");
				sb0.append("<name xsi:type=\"BAG_EN\">\n");
				sb0.append("<item>\n");
				sb0.append("<part value=\""+comp.getServiceDeliveryLocation_name()+"\" />\n");
				sb0.append("</item>\n");
				sb0.append("</name>\n");
				sb0.append("</serviceProviderOrganization>\n");
				sb0.append("</serviceDeliveryLocation>\n");
				sb0.append("</location>\n");
				sb0.append("<!-- 一些相关信息 @code: 区分项目 -->\n");
				sb0.append("<component2>\n");
				sb0.append("<!-- 是否标记 -->\n");
				sb0.append("<placerGroup>\n");
				sb0.append("<!-- 是否皮试 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"01\" codeSystem=\"1.2.156.112685.1.1.84\">\n");
				sb0.append("<displayName value=\"皮试\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<!-- 是否值(是: true, 否: false) -->\n");
				sb0.append("<value xsi:type=\"BL\" value=\"\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("<!-- 是否加急 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.84\" >\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"BL\" value=\"\" />\n");				
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				
				sb0.append("<!-- 是否药观 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\"	contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.84\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"BL\" value=\"false\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
					
				sb0.append("<!-- 先诊疗后付费类型  -->\n");
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
				sb0.append("<displayName value=\"收费状态标识\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"0\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
                sb0.append("<!-- HIS执行状态  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
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
				sb0.append("<!-- 医疗付费方式编码/名称 -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"医疗付费方式\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"CD\"  code=\"\" codeSystem=\"1.2.156.112685.1.1.86\">\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</value>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");

				sb0.append("</placerGroup>\n");
				sb0.append("</component2>\n");
								
				sb0.append("<subjectOf1 typeCode=\"SUBJ\">\n");
				sb0.append("<valuedItem moodCode=\"DEF\" classCode=\"INVE\">\n");
				sb0.append("<!--测试项目价格 -->\n");
				sb0.append("<unitPriceAmt>\n");
				
				sb0.append("<numerator xsi:type=\"MO\" value=\""+getChargingAmt(comp.getChargingItemid())+"\" currency=\"RMB\" />\n");
				sb0.append("</unitPriceAmt>\n");
				sb0.append("<component typeCode=\"COMP\">\n");
				sb0.append("<valuedUnitItem moodCode=\"DEF\" classCode=\"INVE\">\n");
												sb0.append("<!-- 必须项未使用 -->\n");
				sb0.append("<unitQuantity />\n");
												sb0.append("<!--耗材价格 -->\n");
				sb0.append("<unitPriceAmt>\n");
				sb0.append("<numerator xsi:type=\"MO\" value=\"\" currency=\"RMB\" />\n");
				sb0.append("</unitPriceAmt>\n");
				sb0.append("</valuedUnitItem>\n");
				sb0.append("</component>\n");
				sb0.append("</valuedItem>\n");
				sb0.append("</subjectOf1>\n");

								sb0.append("<!--标本要求 -->\n");
				sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\"\n typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 default=false -->\n");
				sb0.append("<seperatableInd value=\"false\" />\n");
				sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.11\"></code>\n");
										sb0.append("<!--标本要求 必须项已使用 -->\n");
				sb0.append("<text value=\"\" />\n");
										sb0.append("<!-- 必须项未使用 -->\n");
				sb0.append("<statusCode />\n");
										sb0.append("<!--必须项 未使用 -->\n");
				sb0.append("<author>\n");
				sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
				sb0.append("</author>\n");
				sb0.append("</annotation>\n");
				sb0.append("</subjectOf6>\n");
				sb0.append("</observationRequest>\n");
				sb0.append("</component2>\n");	
						sb0.append("<!--医嘱项目信息End -->\n");
						
						sb0.append("<!--报告备注 -->\n");
						sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<seperatableInd/>\n");
						sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.11\"></code>\n");
										sb0.append("<!--报告备注 必须项已使用 -->\n");
						sb0.append("<text value=\"\" />\n");
										sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode code=\"completed\" />\n");
										sb0.append("<!--必须项 未使用 -->\n");
						sb0.append("<author>\n");
						sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
						sb0.append("</author>\n");
						sb0.append("</annotation>\n");
						sb0.append("</subjectOf6>\n");
								sb0.append("<!--备注字段1 药观编码－打印在报告单上，药理机构要求 -->\n");
						sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 default=false -->\n");
						sb0.append("<seperatableInd value=\"false\" />\n");
						sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.12\"></code>\n");
										sb0.append("<!--药观编码 必须项已使用 -->\n");
						sb0.append("<text value=\"\" />\n");
										sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode />\n");
										sb0.append("<!--必须项 未使用 -->\n");
						sb0.append("<author>\n");
						sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
						sb0.append("</author>\n");
						sb0.append("</annotation>\n");
						sb0.append("</subjectOf6>\n");
								sb0.append("<!--备注字段2 药观名称－打印在报告单上，药理机构要求 -->\n");
						sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 default=false -->\n");
						sb0.append("<seperatableInd value=\"false\" /> \n");
						sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.13\"></code>\n");
										sb0.append("<!--药观名称 必须项已使用 -->\n");
						sb0.append("<text value=\"\" />\n");
										sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode />\n");
										sb0.append("<!--必须项 未使用 -->\n");
						sb0.append("<author>\n");
						sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
						sb0.append("</author>\n");
						sb0.append("</annotation>\n");
						sb0.append("</subjectOf6>\n");
								sb0.append("<!--备注字段3 其它HIS要求储存但未确定信息1 -->\n");
						sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 default=false -->\n");
						sb0.append("<seperatableInd value=\"false\" />\n");
						sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.14\"></code>\n");
										sb0.append("<!--备注字段3 必须项已使用 -->\n");
						sb0.append("<text value=\"\" />\n");
										sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode />\n");
										sb0.append("<!--必须项 未使用 -->\n");
						sb0.append("<author>\n");
						sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
						sb0.append("</author>\n");
						sb0.append("</annotation>\n");
						sb0.append("</subjectOf6>\n");
								sb0.append("<!--备注字段4 其它HIS要求储存但未确定信息2 -->\n");
						sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 default=false -->\n");
						sb0.append("<seperatableInd value=\"false\" />\n");
						sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.15\"></code>\n");
										sb0.append("<!--备注字段4 必须项已使用 -->\n");
						sb0.append("<text value=\"\" />\n");
										sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode />\n");
										sb0.append("<!--必须项 未使用 -->\n");
						sb0.append("<author>\n");
						sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
						sb0.append("</author>\n");
						sb0.append("</annotation>\n");
						sb0.append("</subjectOf6>\n");
								sb0.append("<!--备注字段5 其它HIS要求储存但未确定信息3 -->\n");
						sb0.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">\n");
									sb0.append("<!-- 必须项 未使用 default=false -->\n");
						sb0.append("<seperatableInd value=\"false\" />\n");
						sb0.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">\n");
										sb0.append("<!-- 备注类型 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.2.2.16\"></code>\n");
										sb0.append("<!--备注字段5 必须项已使用 -->\n");
						sb0.append("<text value=\""+sjh.getVisit_no()+"\" />\n");//--挂号id
										sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode />\n");
										sb0.append("<!--必须项 未使用 -->\n");
						sb0.append("<author>\n");
						sb0.append("<assignedEntity classCode=\"ASSIGNED\" />\n");
						sb0.append("</author>\n");
						sb0.append("</annotation>\n");
						sb0.append("</subjectOf6>\n");
						
						sb0.append("</observationRequest>\n");
						sb0.append("</component2>\n");
						
						sb0.append("<!--申请单信息End -->\n");
						sb0.append(" <!--就诊 -->\n");
						sb0.append("<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"COMP\">\n");
						sb0.append(" <!--就诊 -->\n");
						sb0.append("<encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
						sb0.append("<id >\n");
						sb0.append(" <!-- 就诊次数 -->\n");
						String dates = DateTimeUtil.getDateTimes();		
						String jzcs=dates.substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
						sb0.append("<item extension=\""+jzcs+"\" root=\"1.2.156.112685.1.2.1.7\"/>\n");
						sb0.append("<!-- 就诊流水号 -->\n");
						String lsh=lismessage.getCustom().getPersonid()+sjh.getOthers().substring(1,sjh.getOthers().length());
						sb0.append("<item extension=\""+lsh+"\" root=\"1.2.156.112685.1.2.1.6\"/>\n");  
						sb0.append("</id>\n");
						sb0.append("<!--病人类型编码 -->\n");
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
						sb0.append("<!--就诊机构/科室 -->\n");
						sb0.append("<location typeCode=\"LOC\" xsi:nil=\"false\">\n");
						sb0.append("<!--必须项未使用 -->\n");
						sb0.append("<time />\n");
						sb0.append("<!--就诊机构/科室 -->\n");
						sb0.append("<serviceDeliveryLocation classCode=\"SDLOC\">\n");
						sb0.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
						sb0.append("<!--就诊院区编码 -->\n");
						sb0.append("<id>\n");
						sb0.append("<item extension=\"46014326-4\"></item>\n");
						sb0.append("</id>\n");
						sb0.append("<!--就诊院区名称 -->\n");
						sb0.append("<name xsi:type=\"BAG_EN\">\n");
						sb0.append("<item>\n");
						sb0.append("<part value=\"包头市中心医院\"></part>\n");
						sb0.append("</item>\n");
						sb0.append("</name>\n");
						sb0.append("</serviceProviderOrganization>\n");
						sb0.append("</serviceDeliveryLocation>\n");
						sb0.append("</location>\n");
						sb0.append("<!-- 诊断(原因) -->\n");
						sb0.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">\n");
						sb0.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">\n");
						sb0.append("<!-- 诊断类别 必须项已使用 -->\n");
						sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.29\">\n");
						sb0.append("<!--诊断类别名称 -->\n");
						sb0.append("<displayName value=\"\" />\n");
						sb0.append("</code>\n");
						sb0.append("<!-- 必须项 未使用 -->\n");
						sb0.append("<statusCode />\n");
						sb0.append("<!-- 疾病代码 必须项已使用 -->\n");
						sb0.append("<value code=\"\" codeSystem=\"1.2.156.112685.1.1.30\">\n");
						sb0.append("<!-- 疾病名称 -->\n");
						sb0.append("<displayName value=\"\" />\n");
						sb0.append("</value>\n");
						sb0.append("</observationDx>\n");
						sb0.append("</pertinentInformation1>\n");
						sb0.append("</encounter>\n");
						sb0.append("</componentOf1>\n");
						sb0.append("</placerGroup>\n");
						sb0.append("</subject>\n");
						sb0.append("</controlActProcess>\n");
						sb0.append("</POOR_IN200901UV>\n");
						slist.add(sb.toString()+sb0.toString())	;	
					}
			}		
		return slist;
	}
	
	
	private List<String> getStatus(LisComponents comps,String status,String statusname,String datetime,String logname){
		List<String> stauts=new ArrayList<String>();
		for (LisComponent comp : comps.getItemList()) {	
			int lisid=getzl_req_Lis_item(lismessage.getCustom().getExam_num(),comps.getReq_no(),comp.getChargingItemid(),logname);
		if(lisid>0){
		StringBuffer sb004=new StringBuffer();
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
		sb004.append("						<any value=\"" + datetime + "\"></any>\n");
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
		sb004.append("								<item extension=\""+comp.getServiceDeliveryLocation_code()+"\" root=\"1.21.156.112685.1.1.1\" />\n");
		sb004.append("							</id>\n");
		sb004.append("							<!--执行科室名称 -->\n");
		sb004.append("							<name xsi:type=\"BAG_EN\">\n");
		sb004.append("								<item>\n");
		sb004.append("									<part value=\"检验室\" />\n");
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
		sb004.append("							<item extension=\""+comps.getReq_no()+"\" root=\"1.2.156.112685.1.2.1.21\"/>\n");
		sb004.append("							<!-- 报告号 -->\n");
		sb004.append("							<item extension=\"\" root=\"1.2.156.112685.1.2.1.24\"/>\n");
		sb004.append("							<!-- StudyInstanceUID -->\n");
		sb004.append("							<item extension=\"\" root=\"1.2.156.112685.1.2.1.30\"/>\n");
		sb004.append("						</id>\n");
		sb004.append("						<!-- 医嘱类别编码/医嘱类别名称 - 针剂药品, 材料类, 治疗类, 片剂药品, 化验类 -->\n");
		sb004.append("						<code code=\"C\" codeSystem=\"1.2.156.112685.1.1.27\">\n");
		sb004.append("							<displayName value=\"检验\" />\n");
		sb004.append("						</code>\n");
		sb004.append("						<!-- 必须项未使用 -->\n");
		sb004.append("						<statusCode />\n");
		sb004.append("						<!-- 必须项未使用 -->\n");
		sb004.append("						<effectiveTime xsi:type=\"IVL_TS\" />\n");
		sb004.append("						<!-- 标本信息 -->\n");
		sb004.append("						<specimen typeCode=\"SPC\">\n");
		sb004.append("							<specimen classCode=\"SPEC\">\n");
		sb004.append("								<!--标本条码号 必须项已使用 -->\n");
		sb004.append("								<id extension=\""+comp.getExtension()+"\" />\n");
		sb004.append("								<!--必须项目未使用 -->\n");
		sb004.append("								<code />\n");
		sb004.append("								<subjectOf1 typeCode=\"SBJ\" contextControlCode=\"OP\">\n");
		sb004.append("									<specimenProcessStep moodCode=\"EVN\" classCode=\"SPECCOLLECT\">\n");
		sb004.append("										<!-- 采集日期 -->\n");
		sb004.append("										<effectiveTime xsi:type=\"IVL_TS\">\n");
		sb004.append("											<any value=\""+comp.getItemtime()+"\"></any>\n");
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
		stauts.add(sb004.toString());
		}		
		}
		return stauts;
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
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getSamDemo(String chargid,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select b.demo_num as exam_num,b.demo_name as arch_num from charging_item a,sample_demo b where a.sam_demo_id=b.id and a.id='"+chargid+"'");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public double getChargingAmt(String id) throws ServiceException {
		Connection tjtmpconnect = null;
		double lisitemid =0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select amount from charging_item a where id='"+id+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitemid = rs1.getDouble("amount");
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
		return lisitemid;
	} 
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_item(String exam_num,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_id='"+ciid+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,req_id,createdate) values('" 
				+ ei.getId() + "','" +ciid + "','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
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
	public int getzl_req_Lis_item(String exam_num,String req_id,String cicode,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
							
				String sb1 = "select a.id from zl_req_item a where a.exam_info_id='"+ei.getId()+"' and a.charging_item_id='"
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
