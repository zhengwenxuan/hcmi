package com.hjw.webService.client.dbgj;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessage;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageService;
import com.hjw.webService.client.dbgj.client.DefaultAcceptMessageServiceLocator;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISSendMessageTJPT {

	private LisMessageBody lismessage;
	
	public LISSendMessageTJPT(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
		String xml = "";
		try {
			xml = lisSendMessage();
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+lismessage.getMessageid()+":"+xml);
			try {
				DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
				DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
				String messages = dams.acceptMessage(xml);
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+lismessage.getMessageid()+":"+messages);
				try {
					rb = JaxbUtil.converyToJavaBean(messages, ResultLisBody.class);
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("lis解析返回值错误");
				}
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("lis调用webservice错误");
			}

		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}

	private String lisSendMessage() {
		StringBuffer sb = new StringBuffer("");
        if((this.lismessage.getCustom().getPersonid().length()<=0)||(this.lismessage.getCustom().getPersonno().length()<=0)){
        	this.lismessage.getCustom().setPersonid(this.lismessage.getCustom().getExam_num());
        	this.lismessage.getCustom().setPersonno(this.lismessage.getCustom().getExam_num());
        }
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
				"<item root=\"1.2.156.112606.1.2.1.3\" extension=\"" + lismessage.getCustom().getPersonid() + "\" />");
		// <!-- 就诊号 -->
		sb.append(
				"<item root=\"1.2.156.112606.1.2.1.12\" extension=\"" + lismessage.getCustom().getPersonno() + "\" />");
		sb.append(
				"<item root=\"exam_num\" extension=\""+lismessage.getCustom().getExam_num()+"\" />");
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
		sb.append("<item use=\"MC\" value=\"" + lismessage.getCustom().getTel() + "\"></item>");
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
		
		// <!--雇佣关系 -->
		sb.append("<asEmployee classCode=\"EMP\">");
		// <!--工作单位 -->
		sb.append("<employerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
		// <!--工作单位名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getCustom().getComname() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("<contactParty classCode=\"CON\" xsi:nil=\"true\" />");
		sb.append("</employerOrganization>");
		sb.append("</asEmployee>");
		sb.append("</patientPerson>");
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
		//<!-- 录入人 -->
		sb.append("<transcriber typeCode=\"TRANS\">");
		sb.append("<time>");
				//<!-- 录入日期 开始时间 -->
		sb.append("<low value=\"\"></low>");
				//<!-- 录入日期 结束时间 -->
		sb.append("<high value=\"\"></high>");
		sb.append("</time>");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
				//<!-- 录入人 -->
		sb.append("<id>");
		sb.append("<item extension=\"\" root=\"1.2.156.112606.1.1.2\"></item>");
		sb.append("</id>");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
					//<!-- 录入人姓名 必须项已使用 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item use=\"ABC\">");
		sb.append("<part value=\"\"/>");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</transcriber>");
		
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
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		// <!--确认人姓名 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</verifier>");

		if ((lismessage.getComponents() != null) && (lismessage.getComponents().size() > 0))
			for (LisComponents comps : lismessage.getComponents()) {

				//<!--1..n 一个检验消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2 -->
				sb.append("<component2 typeCode=\"COMP\">");
				//<!--申请单信息Begin -->
				sb.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
						//<!--检验申请单编号 必须项已使用 -->
				sb.append("<id>");
				sb.append("<item extension=\""+comps.getReq_no()+"\" />");
				sb.append("</id>");
				sb.append("<costs value=\""+comps.getCosts()+"\" />");
				sb.append("<RELEVANT_CLINIC_DIAG value=\"\"/>");
						//<!-- 医嘱类型 必须项目已使用 -->
				sb.append("<code code=\""+comps.getYzCode()+"\" codeSystem=\"1.2.156.112606.1.1.27\">");
							//<!-- 医嘱类型名称 -->
				sb.append("<displayName value=\""+comps.getYzName()+"\" />");
				sb.append("</code>");
						//<!-- 检验申请单状态 必须项未使用 -->
				sb.append("<statusCode />");
						//<!-- 检验申请日期 -->
				sb.append("<effectiveTime xsi:type=\"IVL_TS\">");
				sb.append("<any value=\""+comps.getDatetime()+"\"></any>");
				sb.append("</effectiveTime>");
						//<!-- 是否私隐 -->
				sb.append("<confidentialityCode>");
				sb.append("<item code=\"\" codeSystem=\"1.2.156.112606.1.1.84\"></item>");
				sb.append("</confidentialityCode>");
						
				if ((comps.getItemList() != null) && (comps.getItemList().size() > 0)){
					for (LisComponent comp : comps.getItemList()) {	
						
						//<!--1..n 一个申请单可以包含多个医嘱，每个医嘱对应一个component2,多个医嘱重复component2 -->
				sb.append("<component2 typeCode=\"COMP\">");
								//<!--医嘱项目信息Begin -->
				sb.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
								//<!-- 医嘱号 必须项已使用 -->
				sb.append("<id>");
				sb.append("<item extension=\"\"></item>");
				sb.append("</id>");
								//<!-- 检验项目编码 必须项使用 -->
				sb.append("<code code=\""+comp.getItemCode()+"\" codeSystem=\"1.2.156.112606.1.1.46/1.2.156.112606.1.1.110\"  codeSystemName=\" OID检验项目/医嘱字典\">");
				sb.append("<displayName value=\""+comp.getItemName()+"\" />");
				sb.append("</code>");
				//<--项目价格--> 空
				sb.append("<charges values=\"\" />");
				sb.append("<effectiveTime xsi:type=\"QSC_TS\" validTimeLow=\"\" validTimeHigh=\"\">");
									//<!-- 医嘱执行频率编码 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.1.28\">");
										//<!--医嘱执行频率名称 -->
				sb.append("<displayName value=\"\" />");
				sb.append("</code>");
				sb.append("</effectiveTime>");
								//<!-- 检验项目优先级别 -->
				sb.append("<priorityCode code=\"\" />");
								//<!-- 采集部位 -->
				sb.append("<targetSiteCode>");
				sb.append("<item code=\""+comp.getTargetSiteCode()+"\" />");
				sb.append("</targetSiteCode>");
				sb.append("<specimen typeCode=\"SPC\">");
				sb.append("<specimen classCode=\"SPEC\">");
										//<!--标本号/条码号 必须项已使用 -->
				sb.append("<id extension=\""+comp.getExtension()+"\" />");
										//<!--标本角色代码（patient,group,blind) 必须项目未使用 -->
				sb.append("<code />");
				sb.append("<specimenNatural determinerCode=\"INSTANCE\" classCode=\"ENT\">");
											//<!--标本类型 血清/血浆/尿 标本类别代码 -->
				sb.append("<code code=\""+comp.getSpecimenNatural()+"\" codeSystem=\"1.2.156.112606.1.1.45\">");
												//<!--标本类型名称 -->
				sb.append("<displayName value=\""+comp.getSpecimenNaturalname()+"\" />");
				sb.append("</code>");
				sb.append("</specimenNatural>");
				sb.append("<subjectOf1 typeCode=\"SBJ\" contextControlCode=\"OP\">");
				sb.append("<specimenProcessStep moodCode=\"EVN\" classCode=\"SPECCOLLECT\">");
												//<!-- 采集日期 -->
				sb.append("<effectiveTime xsi:type=\"IVL_TS\">");
				sb.append("<any value=\""+comp.getItemtime()+"\"></any>");
				sb.append("</effectiveTime>");
				sb.append("<subject typeCode=\"SBJ\" contextControlCode=\"OP\">");
				sb.append("<specimenInContainer classCode=\"CONT\">");
				sb.append("<container determinerCode=\"INSTANCE\" classCode=\"CONT\">");
															//<!--测试项目容器ID -->
				sb.append("<code code=\"\"></code>");
															//<!-- 测试项目容器 -->
				sb.append("<desc value=\"\" />");
				sb.append("</container>");
				sb.append("</specimenInContainer>");
				sb.append("</subject>");
				sb.append("<performer typeCode=\"PRF\">");
				sb.append("<assignedEntity classCode=\"ASSIGNED\">");
														//<!-- 采集人Id -->
				sb.append("<id>");
				sb.append("<item extension=\"\" root=\"1.2.156.112606.1.1.2\"></item>");
				sb.append("</id>");
				sb.append("<assignedPerson determinerCode=\"INSTANCE\"	classCode=\"PSN\">");
															//<!-- 采集人姓名 -->
				sb.append("<name xsi:type=\"BAG_EN\">");
				sb.append("<item>");
				sb.append("<part value=\"\"/>");
				sb.append("</item>");
				sb.append("</name>");
															//<!-- 采集地点 -->
				sb.append("<asLocatedEntity classCode=\"LOCE\">");
				sb.append("<addr xsi:type=\"BAG_AD\">");
				sb.append("<item use=\"WP\">");
				sb.append("<part type=\"BNR\" value=\"\" />");
				sb.append("</item>");
				sb.append("</addr>");
				sb.append("</asLocatedEntity>");
				sb.append("</assignedPerson>");
				sb.append("</assignedEntity>");
				sb.append("</performer>");
				sb.append("</specimenProcessStep>");
				sb.append("</subjectOf1>");
				sb.append("</specimen>");
				sb.append("</specimen>");
								//<!-- 执行科室 -->
				sb.append("<location typeCode=\"LOC\">");
				sb.append("<serviceDeliveryLocation classCode=\"SDLOC\">");
				sb.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
											//<!-- 执行科室编码 -->
				sb.append("<id>");
				sb.append("<item extension=\""+comp.getServiceDeliveryLocation_code()+"\" root=\"1.2.156.112606.1.1.1\"></item>");
				sb.append("</id>");
											//<!--执行科室名称 -->
				sb.append("<name xsi:type=\"BAG_EN\">");
				sb.append("<item>");
				sb.append("<part value=\""+comp.getServiceDeliveryLocation_name()+"\" />");
				sb.append("</item>");
				sb.append("</name>");
				sb.append("</serviceProviderOrganization>");
				sb.append("</serviceDeliveryLocation>");
				sb.append("</location>");
								//<!-- 一些相关信息 @code: 区分项目 -->
				sb.append("<component2>");
									//<!-- 是否标记 -->
				sb.append("<placerGroup>");
										//<!-- 是否皮试 -->
				sb.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">");
				sb.append("<observation classCode=\"OBS\" moodCode=\"INT\">");
				sb.append("<code code=\"01\" codeSystem=\"1.2.156.112606.1.1.84\" />");
												//<!-- 是否值(是: true, 否: false) -->
				sb.append("<value xsi:type=\"BL\" value=\"\" />");
				sb.append("</observation>");
				sb.append("</pertinentInformation>");
										//<!-- 是否加急 -->
				sb.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">");
				sb.append("<observation classCode=\"OBS\" moodCode=\"INT\">");
				sb.append("<code code=\"03\" codeSystem=\"1.2.156.112606.1.1.84\" />");
												//<!-- 是否值(是: true, 否: false) -->
				sb.append("<value xsi:type=\"BL\" value=\"\" />");
				sb.append("</observation>");
				sb.append("</pertinentInformation>");
										//<!-- 是否药观 -->
				sb.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">");
				sb.append("<observation classCode=\"OBS\" moodCode=\"INT\">");
				sb.append("<code code=\"04\" codeSystem=\"1.2.156.112606.1.1.84\" />");
												//<!-- 是否值(是: true, 否: false) -->
				sb.append("<value xsi:type=\"BL\" value=\"\" />");
				sb.append("</observation>");
				sb.append("</pertinentInformation>");
				sb.append("</placerGroup>");
				sb.append("</component2>");
								
				sb.append("<subjectOf1 typeCode=\"SUBJ\">");
				sb.append("<valuedItem moodCode=\"DEF\" classCode=\"INVE\">");
										//<!--测试项目价格 -->
				sb.append("<unitPriceAmt>");
				sb.append("<numerator xsi:type=\"MO\" value=\"\" currency=\"RMB\" />");
				sb.append("</unitPriceAmt>");
				sb.append("<component typeCode=\"COMP\">");
				sb.append("<valuedUnitItem moodCode=\"DEF\" classCode=\"INVE\">");
												//<!-- 必须项未使用 -->
				sb.append("<unitQuantity />");
												//<!--耗材价格 -->
				sb.append("<unitPriceAmt>");
				sb.append("<numerator xsi:type=\"MO\" value=\"\" currency=\"RMB\" />");
				sb.append("</unitPriceAmt>");
				sb.append("</valuedUnitItem>");
				sb.append("</component>");
				sb.append("</valuedItem>");
				sb.append("</subjectOf1>");

								//<!--标本要求 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
									//<!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"\" />");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
										//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.11\"></code>");
										//<!--标本要求 必须项已使用 -->
				sb.append("<text value=\"\" />");
										//<!-- 必须项未使用 -->
				sb.append("<statusCode />");
										//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
				sb.append("</observationRequest>");
				sb.append("</component2>");	
						//<!--医嘱项目信息End -->
			}
			}
		//<!--报告备注 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
							//<!-- 必须项 未使用 -->
				sb.append("<seperatableInd />");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
								//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.11\"></code>");
								//<!--报告备注 必须项已使用 -->
				sb.append("<text value=\"\" />");
								//<!-- 必须项 未使用 -->
				sb.append("<statusCode />");
								//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
						//<!--备注字段1 药观编码－打印在报告单上，药理机构要求 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
							//<!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"\" />");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
								//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.12\"></code>");
								//<!--药观编码 必须项已使用 -->
				sb.append("<text value=\"\" />");
								//<!-- 必须项 未使用 -->
				sb.append("<statusCode />");
								//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
						//<!--备注字段2 药观名称－打印在报告单上，药理机构要求 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
							//<!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"\" /> ");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
								//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.13\"></code>");
								//<!--药观名称 必须项已使用 -->
				sb.append("<text value=\"\" />");
								//<!-- 必须项 未使用 -->
				sb.append("<statusCode />");
								//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
						//<!--备注字段3 其它HIS要求储存但未确定信息1 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
							//<!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"\" />");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
								//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.14\"></code>");
								//<!--备注字段3 必须项已使用 -->
				sb.append("<text value=\"\" />");
								//<!-- 必须项 未使用 -->
				sb.append("<statusCode />");
								//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
						//<!--备注字段4 其它HIS要求储存但未确定信息2 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
							//<!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"\" />");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
								//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.15\"></code>");
								//<!--备注字段4 必须项已使用 -->
				sb.append("<text value=\"\" />");
								//<!-- 必须项 未使用 -->
				sb.append("<statusCode />");
								//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
						//<!--备注字段5 其它HIS要求储存但未确定信息3 -->
				sb.append("<subjectOf6 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"SUBJ\">");
							//<!-- 必须项 未使用 default=false -->
				sb.append("<seperatableInd value=\"\" />");
				sb.append("<annotation classCode=\"ACT\" moodCode=\"EVN\">");
								//<!-- 备注类型 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.2.2.16\"></code>");
								//<!--备注字段5 必须项已使用 -->
				sb.append("<text value=\"\" />");
								//<!-- 必须项 未使用 -->
				sb.append("<statusCode />");
								//<!--必须项 未使用 -->
				sb.append("<author>");
				sb.append("<assignedEntity classCode=\"ASSIGNED\" />");
				sb.append("</author>");
				sb.append("</annotation>");
				sb.append("</subjectOf6>");
				sb.append("</observationRequest>");
				sb.append("</component2>");
				//<!--申请单信息End -->

			}
		// <!--就诊 -->
		sb.append("<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"COMP\">");
		// <!--就诊 -->
		sb.append("<encounter classCode=\"ENC\" moodCode=\"EVN\">");
		// <!-- 就诊次数 -->
		sb.append("<id>");
		sb.append("<item extension=\"1\" root=\"1.2.156.112606.1.2.1.7\"/>");
		sb.append("</id>");
		// <!--病人类型编码 -->
		sb.append("<code codeSystem=\"1.2.156.112606.1.1.80\" codeSystemName=\"\" 	code=\"\" />");
		// <!--必须项未使用 -->
		sb.append("<statusCode  />");
		// <!--病人 必须项未使用 -->
		sb.append("<subject typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\" />");
		sb.append("</subject>");
		//<!--就诊机构/科室 -->
		sb.append("<location typeCode=\"LOC\" xsi:nil=\"false\">");
			//<!--必须项未使用 -->
		sb.append("<time />");
			//<!--就诊机构/科室 -->
		sb.append("<serviceDeliveryLocation classCode=\"SDLOC\">");
		sb.append("<serviceProviderOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
					//<!--就诊院区编码 -->
		sb.append("<id>");
		sb.append("<item extension=\"\"></item>");
		sb.append("</id>");
					//<!--就诊院区名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"\"></part>");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("</serviceProviderOrganization>");
		sb.append("</serviceDeliveryLocation>");
		sb.append("</location>");
		//<!-- 诊断(原因) -->
		sb.append("<pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\">");
		sb.append("<observationDx classCode=\"OBS\" moodCode=\"EVN\">");
				//<!-- 诊断类别 必须项已使用 -->
		sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.1.29\" />");
				//<!-- 必须项 未使用 -->
		sb.append("<statusCode />");
				//<!-- 疾病代码 必须项已使用 -->
		sb.append("<value code=\"\" codeSystem=\"1.2.156.112606.1.1.30\">");
					//<!-- 疾病名称 -->
		sb.append("<displayName value=\"\" />");
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

}
