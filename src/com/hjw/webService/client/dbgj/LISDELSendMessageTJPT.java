package com.hjw.webService.client.dbgj;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
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
public class LISDELSendMessageTJPT {

	private LisMessageBody lismessage;
	
	public LISDELSendMessageTJPT(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String logName) {
		ResultLisBody rb = new ResultLisBody();
		String xml = "";
		try {
			xml = lisSendMessage();
			TranLogTxt.liswriteEror_to_txt(logName,"req:"+lismessage.getMessageid()+":"+xml);
			try {
				DefaultAcceptMessageService dam = new DefaultAcceptMessageServiceLocator(url);
				DefaultAcceptMessage dams = dam.getDefaultAcceptMessagePort();
				String messages = dams.acceptMessage(xml);
				TranLogTxt.liswriteEror_to_txt(logName,"res:"+lismessage.getMessageid()+":"+messages);
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
		sb.append("<code code=\"delete\"></code>");
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
		sb.append("<administrativeGenderCode code=\""
				+ lismessage.getCustom().getSexcode() + "\" codeSystem=\"1.2.156.112606.1.1.3\" />");
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
		
		

		if ((lismessage.getComponents() != null) && (lismessage.getComponents().size()>0))
			for (LisComponents com : lismessage.getComponents()) {
				//<!--1..n 一个检验消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2 -->
				sb.append("<component2 typeCode=\"COMP\">");
				//<!--申请单信息Begin -->
				sb.append("<observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
						//<!--检验申请单编号 必须项已使用 -->
				sb.append("<id>");
				sb.append("<item extension=\""+com.getReq_no()+"\" />");
				sb.append("</id>");
						//<!-- 医嘱类型 必须项目已使用 -->
				sb.append("<code code=\"\" codeSystem=\"1.2.156.112606.1.1.27\">");
							//<!-- 医嘱类型名称 -->
				sb.append("<displayName value=\"\"/>");
				sb.append("</code>");
				
				sb.append("</observationRequest>");
				sb.append("</component2>");
				//<!--申请单信息End -->

			}
		

		sb.append("</placerGroup>");
		sb.append("</subject>");
		sb.append("</controlActProcess>");
		sb.append("</POOR_IN200901UV>");
		return sb.toString();
	}

}
