package com.hjw.webService.client.hokai;

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

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.MenuDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PACSSendMessageHK{
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static BatchService batchService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		batchService = (BatchService) wac.getBean("batchService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSSendMessageHK(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url,String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname,"lismessage:"+new Gson().toJson(lismessage, PacsMessageBody.class));
			TranLogTxt.liswriteEror_to_txt(logname,"url:"+url);
			String exam_num = lismessage.getCustom().getExam_num();
			if (StringUtil.isEmpty(exam_num)) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("体检编号为空");
			} else {
				ExamInfoUserDTO eu=this.configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
				if ((eu==null)||(eu.getId() <= 0)) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("体检编号为空");
				} else {
					ControlActPacsProcess ca = new ControlActPacsProcess();
					List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
					
					for (PacsComponents pcs : lismessage.getComponents()) {
						try {
						String xml = pacsSendMessage(pcs,logname);
						TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml + "\r\n");
						String result = HttpUtil.doPost_Xml(url,xml, "utf-8");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
						if ((result != null) && (result.trim().length() > 0)) {
							result = result.trim();
							ResultHeader rhone= new ResultHeader();
							rhone = ResContralBeanHK.getRes(result);
							if("AA".equals(rhone.getTypeCode())){
								ApplyNOBean ab= new ApplyNOBean();
								ab.setApplyNO(pcs.getReq_no());
								list.add(ab);
							}
						}
						}catch(Exception ex){
							
						}
					}
					if(list.size() > 0) {
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("pacs调用成功");
					}

			}
			}
		} catch (Exception ex){
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "ret:" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
	
	private String pacsSendMessage(PacsComponents compts,String logname) {
		String xml="";
		String itemNames = "";
		for(PacsComponent pcs : compts.getPacsComponent()) {
			itemNames += (pcs.getItemName() + ";");
		}
		if(itemNames.length()>0) {
			itemNames.substring(0, itemNames.length()-1);
		}
		
		int lisid=updatezl_req_pacs_item(lismessage.getCustom().getExam_num(),compts.getReq_no(),"",logname);
		if(lisid>0){
			StringBuffer sb0=new StringBuffer();	                                                                                                                 
			sb0.append("<POOR_IN200901UV   ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"); 
			sb0.append("<!-- 消息ID (1..1)-->"); 
			sb0.append("<id root=\"2.16.156.10011.0\" extension=\""+UUID.randomUUID().toString().toLowerCase()+"\" />"); 
			sb0.append("	    <!-- 消息创建时间 (1..1)-->"); 
			sb0.append("<creationTime value=\"" + lismessage.getCreationTime_value() + "\" />"); 
			sb0.append("	    <!-- 消息的服务标识 (1..1)--> "); 
			sb0.append("	    <interactionId root=\"2.16.840.1.113883.1.6\" extension=\"S0082\" />"); 
			sb0.append("	    <!--处理代码，标识此消息是否是产品、训练、调试系统的一部分。 D：调试； P：产品； T：训练(1..1) -->  "); 
			sb0.append("	    <processingCode code=\"P\" />   "); 
			sb0.append("	    <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) (1..1)-->"); 
			sb0.append("	    <processingModeCode />"); 
			sb0.append("	    <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) (1..1)--> "); 
			sb0.append("	    <acceptAckCode code=\"AL\" />   "); 
			sb0.append("	    <!-- 接受者 (1..1)--> "); 
			sb0.append("	    <receiver typeCode=\"RCV\"> "); 
			sb0.append("	        <device "); 
			sb0.append("	            classCode=\"DEV\" "); 
			sb0.append("	            determinerCode=\"INSTANCE\">"); 
			sb0.append("	            <!-- 接受者ID --> "); 
			sb0.append("	            <id>"); 
			sb0.append("	                <item   "); 
			sb0.append("	                    root=\"2.16.156.10011.0.1.1\"  extension=\"SYS001\" />  "); 
			sb0.append("	            </id> "); 
			sb0.append("	        </device> "); 
			sb0.append("	    </receiver> "); 
			sb0.append("	    <!-- 发送者 (1..1)--> "); 
			sb0.append("	    <sender typeCode=\"SND\"> "); 
			sb0.append("	        <device "); 
			sb0.append("	            classCode=\"DEV\" "); 
			sb0.append("	            determinerCode=\"INSTANCE\">"); 
			sb0.append("	            <!-- 发送者ID --> "); 
			sb0.append("	            <id>"); 
			sb0.append("	                <item "); 
			sb0.append("	                    root=\"2.16.156.10011.0.1.2\" "); 
			sb0.append("	                    extension=\"SYS009\" /> "); 
			sb0.append("	            </id> "); 
			sb0.append("	        </device> "); 
			sb0.append("	    </sender>   "); 
			sb0.append("	    <!-- 封装的消息内容 --> "); 
			sb0.append("	    <controlActProcess"); 
			sb0.append("	        classCode=\"CACT\"  "); 
			sb0.append("	        moodCode=\"EVN\">   "); 
			sb0.append("	        <code  code=\"POOR_TE200901UV\"   "); 
			sb0.append("	            codeSystem=\"2.16.840.1.113883.1.6\" /> "); 
			sb0.append("	        <subject typeCode=\"SUBJ\"> "); 
			sb0.append("	            <observationRequest    classCode=\"OBS\"   moodCode=\"RQO\"> "); 
			sb0.append("	<!--唯一标志(0..1)--> "); 
			sb0.append("					<soleId  value = \""+lisid+"\"/> "); 
			sb0.append("	                <!-- 检查申请单编号 必须项已使用 -->"); 
			sb0.append("	                <id>  "); 
			sb0.append("	                    <item root=\"2.16.156.10011.1.24\"   extension=\""+compts.getReq_no()+"\" />   "); 
			sb0.append("	                </id> "); 
			sb0.append("	                <code />  "); 
			sb0.append("	                <!-- 申请单详细内容 --> "); 
			sb0.append("	                <text value=\""+itemNames+"\" />   "); 
			sb0.append("	                <!-- 必须项未使用 --> "); 
			sb0.append("	                <statusCode />"); 
			sb0.append("	                <!--检查申请有效日期时间 -->"); 
			sb0.append("	                <effectiveTime xsi:type=\"IVL_TS\"> "); 
			sb0.append("	                    <low value=\""+DateTimeUtil.getDateTimes()+"\" />"); 
			sb0.append("	                    <high value=\""+DateTimeUtil.DateAdd(30)+"\" />   "); 
			sb0.append("	                </effectiveTime>  "); 
			sb0.append("	                <!--优先（紧急）度--> "); 
			sb0.append("	                <priorityCode code=\"N\"> "); 
			sb0.append("	                    <displayName value=\"常规\" />"); 
			sb0.append("	                </priorityCode>   "); 
			sb0.append("	                <!--开单医生/送检医生 -->   "); 
			sb0.append("	                <author typeCode=\"AUT\"> "); 
			sb0.append("	                    <!-- 开单时间 --> "); 
			sb0.append("	                    <time value=\"" + lismessage.getDoctor().getTime() + "\" /> "); 
			sb0.append("	                    <!--申请单开立者签名--> "); 
			sb0.append("	                    <signatureText value=\"" + lismessage.getDoctor().getDoctorName() + "\" />"); 
			sb0.append("	                    <assignedEntity classCode=\"ASSIGNED\">   "); 
			sb0.append("	                        <!--开单医生编码 -->"); 
			sb0.append("	                        <id>"); 
			sb0.append("	                            <item "); 
			sb0.append("	                                extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"2.16.156.10011.1.4\" /> "); 
			sb0.append("	                        </id> "); 
			sb0.append("	                        <!--开单医生姓名 -->"); 
			sb0.append("	                        <assignedPerson  determinerCode=\"INSTANCE\" classCode=\"PSN\">"); 
			sb0.append("	                            <name xsi:type=\"BAG_EN\">"); 
			sb0.append("	                                <item>"); 
			sb0.append("	                                    <part value=\"" + lismessage.getDoctor().getDoctorName() + "\" />   "); 
			sb0.append("	                                </item> "); 
			sb0.append("	                            </name>   "); 
			sb0.append("	                        </assignedPerson>   "); 
			sb0.append("	                        <!-- 申请科室信息 -->   "); 
			sb0.append("	                        <representedOrganization  determinerCode=\"INSTANCE\"  classCode=\"ORG\"> "); 
			sb0.append("	                            <!--申请科室编码 必须项已使用 --> "); 
			sb0.append("	                            <id>  "); 
			sb0.append("	                                <item  extension=\"" + lismessage.getDoctor().getDept_code() + "\"   root=\"2.16.156.10011.1.26\" /> "); 
			sb0.append("	                            </id> "); 
			sb0.append("	                            <!--申请科室名称 -->"); 
			sb0.append("	                            <name xsi:type=\"BAG_EN\">"); 
			sb0.append("	                                <item>"); 
			sb0.append("	                                    <part value=\"" + lismessage.getDoctor().getDept_name() + "\"  />   "); 
			sb0.append("	                                </item> "); 
			sb0.append("	                            </name>   "); 
			sb0.append("	                        </representedOrganization>"); 
			sb0.append("	                    </assignedEntity> "); 
			sb0.append("	                </author> "); 
			sb0.append("	                <!--审核者--> "); 
			sb0.append("	                <verifier typeCode=\"VRF\">   "); 
			sb0.append("	                    <!--审核日期时间 -->"); 
			sb0.append("	                    <time value=\"\" /> "); 
			sb0.append("	                    <assignedEntity classCode=\"ASSIGNED\">   "); 
			sb0.append("	                        <!--审核者编码 -->  "); 
			sb0.append("	                        <id>"); 
			sb0.append("	                            <item   extension=\"\"   root=\"2.16.156.10011.1.4\" />"); 
			sb0.append("	                        </id> "); 
			sb0.append("	                        <assignedPerson   determinerCode=\"INSTANCE\"    classCode=\"PSN\">   "); 
			sb0.append("	                            <!--审核者姓名 -->  "); 
			sb0.append("	                            <name xsi:type=\"DSET_EN\">   "); 
			sb0.append("	                                <item>"); 
			sb0.append("	                                    <part value=\"\" />   "); 
			sb0.append("	                                </item> "); 
			sb0.append("	                            </name>   "); 
			sb0.append("	                        </assignedPerson>   "); 
			sb0.append("	                    </assignedEntity> "); 
			sb0.append("	                </verifier> "); 
			sb0.append("	                <!-- 多个检查项目循环component2 --> "); 
			for(PacsComponent pcs : compts.getPacsComponent()) {
				sb0.append("	                <component2>"); 
				sb0.append("	                    <observationRequest classCode=\"OBS\" moodCode=\"RQO\"> "); 
				sb0.append("	                        <id>"); 
				sb0.append("	                            <!--医嘱ID-->   "); 
				sb0.append("	                            <item root=\"2.16.156.10011.1.28\"   extension=\""+lisid+"\" />"); 
				sb0.append("	                        </id> "); 
				sb0.append("	                        <!-- 单据属性-->"); 
				sb0.append("	                        <bill>"); 
				sb0.append("	                            <!-- 单据号 --> "); 
				sb0.append("	                            <id value=\"\"/>  "); 
				sb0.append("	                            <!--  单据性质 -->  "); 
				sb0.append("	                            <code value = \"\"/>  "); 
				sb0.append("	                            <!-- 金额 默认单位：元-->   "); 
				sb0.append("	                            <price value=\""+compts.getCosts()+"\"/> "); 
				sb0.append("	                        </bill>   "); 
				sb0.append("	                        <!--检查项目编码 必须项已使用 -->   "); 
				sb0.append("	                        <code code=\""+pcs.getPacs_num()+"\">"); 
				sb0.append("	                            <!--检查项目名称 -->"); 
				sb0.append("	                            <displayName value=\""+pcs.getItemName()+"\" />"); 
				sb0.append("	                        </code>   "); 
				sb0.append("	                        <!-- 必须项未使用 -->   "); 
				sb0.append("	                        <statusCode />"); 
				sb0.append("	                        <methodCode>  "); 
				sb0.append("	                            <!--检查方式编码 -->"); 
				sb0.append("	                            <item code=\"\" codeSystem=\"2.16.156.10011.2.3.2.47\" codeSystemName=\"检查方式代码表\">  "); 
				sb0.append("	                                <!--检查方式名称 -->"); 
				sb0.append("	                                <displayName value=\"\" />"); 
				sb0.append("	                            </item>   "); 
				sb0.append("	                            <!--检查类型编码 -->"); 
				MenuDTO eu= new MenuDTO();
				eu=getOrderExecType(pcs.getItemCode(),logname);
				sb0.append("	                            <item code=\""+eu.getValue()+"\">   "); 
				sb0.append("	                                <!--检查类型名称 -->"); 
				sb0.append("	                                <displayName value=\""+eu.getText()+"\" />  "); 
				sb0.append("	                            </item>   "); 
				sb0.append("	                        </methodCode> "); 
				sb0.append("	                        <!--检查部位编码 -->"); 
				sb0.append("	                        <targetSiteCode>"); 
				sb0.append("	                            <item code=\"\">  "); 
				sb0.append("	                                <!--检查部位名称 -->"); 
				sb0.append("	                                <displayName value=\"\" />  "); 
				sb0.append("	                            </item>   "); 
				sb0.append("	                        </targetSiteCode>   "); 
				sb0.append("	                        <!--执行科室 -->"); 
				sb0.append("	                        <location typeCode=\"LOC\"> "); 
				sb0.append("	                            <!-- 执行时间 -->   "); 
				sb0.append("	                            <time>"); 
				sb0.append("	                                <any value=\"\" />"); 
				sb0.append("	                            </time>   "); 
				sb0.append("	                            <serviceDeliveryLocation   classCode=\"SDLOC\"> "); 
				sb0.append("	                                <serviceProviderOrganization   determinerCode=\"INSTANCE\"  classCode=\"ORG\">"); 
				sb0.append("	                                    <!--执行科室编码 -->"); 
				sb0.append("	                                    <id>"); 
				sb0.append("	                                        <item   "); 
				sb0.append("	                                            extension=\"" + pcs.getServiceDeliveryLocation_code()
						+ "\"  root=\"2.16.156.10011.1.26\" /> "); 
				sb0.append("	                                    </id>   "); 
				sb0.append("	                                    <!-- 执行科室名称 -->   "); 
				sb0.append("	                                    <name xsi:type=\"DSET_EN\"> "); 
				sb0.append("	                                        <item>  "); 
				sb0.append("	                                            <part value=\"" + pcs.getServiceDeliveryLocation_name()
						+ "\" />  "); 
				sb0.append("	                                        </item> "); 
				sb0.append("	                                    </name> "); 
				sb0.append("	                                </serviceProviderOrganization>"); 
				sb0.append("	                            </serviceDeliveryLocation>  "); 
				sb0.append("	                        </location>   "); 
				sb0.append("	                    </observationRequest>   "); 
				sb0.append("	                </component2> "); 
			}
			sb0.append("	                <subjectOf6 contextConductionInd=\"false\">   "); 
			sb0.append("	                    <!-- 必须项 未使用 default=false -->"); 
			sb0.append("	                    <seperatableInd value=\"false\" />"); 
			sb0.append("	                    <!--申请注意事项 -->"); 
			sb0.append("	                    <annotation>  "); 
			sb0.append("	                        <text value=\"\" />"); 
			sb0.append("	                        <statusCode code=\"completed\" /> "); 
			sb0.append("	                        <author>  "); 
			sb0.append("	                            <assignedEntity classCode=\"ASSIGNED\" />   "); 
			sb0.append("	                        </author> "); 
			sb0.append("	                    </annotation> "); 
			sb0.append("	                </subjectOf6> "); 
			sb0.append("	                <!--就诊 -->"); 
			sb0.append("	                <componentOf1  contextConductionInd=\"false\"  xsi:nil=\"false\"   typeCode=\"COMP\">"); 
			sb0.append("	                    <!--就诊 -->  "); 
			sb0.append("	                    <encounter  classCode=\"ENC\"  moodCode=\"EVN\">  "); 
			sb0.append("	                        <id>"); 
			sb0.append("	                            <!--门（急）诊号标识 -->"); 
			ExamInfoUserDTO sjh=getHISDJH(lismessage.getCustom().getExam_num());
			sb0.append("	                            <item  root=\"2.16.156.10011.1.10\"   extension=\""+sjh.getClinic_no()+"\" />"); 
			sb0.append("	                            <!--住院号标识-->   "); 
			sb0.append("	                            <item root=\"2.16.156.10011.1.12\"    extension=\"\" /> "); 
			sb0.append("	                        </id> "); 
			sb0.append("	                        <!--就诊类别编码1.门诊 2.住院 3.体检 4.急诊 9.其他--> "); 
			sb0.append("	                        <code   codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码表\" code=\"03\"> "); 
			sb0.append("	                            <!-- 就诊类别名称 --> "); 
			sb0.append("	                            <displayName value=\"体检\" />"); 
			sb0.append("	                        </code>   "); 
			sb0.append("	                        <!--必须项未使用 -->"); 
			sb0.append("	                        <statusCode />"); 
			sb0.append("	                        <!--病人 必须项未使用 --> "); 
			sb0.append("	                        <subject typeCode=\"SBJ\">"); 
			sb0.append("	                            <patient classCode=\"PAT\">   "); 
			sb0.append("	                                <id>  "); 
			sb0.append("	                                    <!-- 患者ID-->"); 
			sb0.append("	                                    <item  root=\"2.16.156.10011.0.2.2\"  extension=\"" + lismessage.getCustom().getPersonid() + "\" />   "); 
			sb0.append("	                                </id> "); 
			sb0.append("	                                <!--体检号码--> "); 
			sb0.append("	                                <PEId value = \"" + lismessage.getCustom().getExam_num() + "\"/>  "); 
			sb0.append("	                                <!-- 门诊次数/住院次数 -->  "); 
			String dates = DateTimeUtil.getDateTimes();		
			String jzcs=dates.substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
			sb0.append("	                                <patientCount value=\""+jzcs+"\" displayName=\"门诊次数\" />   "); 
			sb0.append("	                                <!--个人信息 必须项已使用 --> "); 
			sb0.append("	                                <patientPerson classCode=\"PSN\">   "); 
			sb0.append("	                                    <!-- 身份证号/医保卡号 -->"); 
			sb0.append("	                                    <id>"); 
			sb0.append("	                                        <!-- 身份证号 -->   "); 
			sb0.append("	                                        <item extension=\""+lismessage.getCustom().getPersonidnum()+"\"  root=\"2.16.156.10011.1.3\" /> "); 
			sb0.append("	                                        <!-- 医保卡号 -->   "); 
			sb0.append("	                                        <item extension=\""+lismessage.getCustom().getPersioncode()+"\"   root=\"2.16.156.10011.1.15\" />  "); 
			sb0.append("	                                    </id>   "); 
			sb0.append("	                                    <!--姓名 -->"); 
			sb0.append("	                                    <name xsi:type=\"DSET_EN\"> "); 
			sb0.append("	                                        <item>  "); 
			sb0.append("	                                            <part value=\""+lismessage.getCustom().getName()+"\" /> "); 
			sb0.append("	                                        </item> "); 
			sb0.append("	                                    </name> "); 
			sb0.append("	                                    <!-- 联系电话 -->   "); 
			sb0.append("	                                    <telecom xsi:type=\"BAG_TEL\">  "); 
			sb0.append("	                                        <!-- 联系电话 -->   "); 
			sb0.append("	                                        <item value=\""+lismessage.getCustom().getContact_tel()+"\" />"); 
			sb0.append("	                                    </telecom>  "); 
			sb0.append("	                                    <!--性别代码 -->"); 
			sb0.append("	                                    <administrativeGenderCode   code=\""+lismessage.getCustom().getSexcode()+"\"    codeSystem=\"2.16.156.10011.2.3.3.4\" />   "); 
			   
			sb0.append("	                                    <!--出生日期 -->"); 
			sb0.append("	                                    <birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\">"); 
			sb0.append("	                                        <!--年龄 -->"); 
			sb0.append("	                                        <originalText value=\"" + lismessage.getCustom().getOld() + "\" displayName=\"岁\"/> "); 
			sb0.append("	                                    </birthTime>"); 
			sb0.append("	                                    <!--住址 -->"); 
			sb0.append("	                                    <addr xsi:type=\"BAG_AD\">"); 
			sb0.append("	                                        <item use=\"H\">  "); 
			sb0.append("	                                            <part type=\"AL\" value=\"" + lismessage.getCustom().getAddress() + "\" />"); 
			sb0.append("	                                        </item> "); 
			sb0.append("	                                    </addr> "); 
			sb0.append("	                                </patientPerson>"); 
			sb0.append("	                            </patient>"); 
			sb0.append("	                        </subject>"); 
			sb0.append("	                        <!--住院位置--> "); 
			sb0.append("	                        <location typeCode=\"LOC\"> "); 
			sb0.append("	                            <time />  "); 
			sb0.append("	                            <serviceDeliveryLocation"); 
			sb0.append("	                                classCode=\"SDLOC\">"); 
			sb0.append("	                                <location classCode=\"PLC\"  determinerCode=\"INSTANCE\">"); 
			sb0.append("	                                    <!--DE01.00.026.00 病床号-->  "); 
			sb0.append("	                                    <id>"); 
			sb0.append("	                                        <item extension=\"001\" />  "); 
			sb0.append("	                                    </id>   "); 
			sb0.append("	                                    <name xsi:type=\"BAG_EN\">"); 
			sb0.append("	                                        <item use=\"IDE\">"); 
			sb0.append("	                                            <part value=\"\" />  "); 
			sb0.append("	                                        </item> "); 
			sb0.append("	                                    </name> "); 
			sb0.append("	                                    <asLocatedEntityPartOf  "); 
			sb0.append("	                                        classCode=\"LOCE\">   "); 
			sb0.append("	                                        <location "); 
			sb0.append("	                                            classCode=\"PLC\" "); 
			sb0.append("	                                            determinerCode=\"INSTANCE\">"); 
			sb0.append("	                                            <!--DE01.00.019.00 病房号-->"); 
			sb0.append("	                                            <id>"); 
			sb0.append("	                                                <item extension=\"\" />"); 
			sb0.append("	                                            </id> "); 
			sb0.append("	                                            <name xsi:type=\"BAG_EN\">  "); 
			sb0.append("	                                                <item use=\"IDE\">  "); 
			sb0.append("	                                                    <part value=\"\" />"); 
			sb0.append("	                                                </item> "); 
			sb0.append("	                                            </name> "); 
			sb0.append("	                                        </location> "); 
			sb0.append("	                                    </asLocatedEntityPartOf>"); 
			sb0.append("	                                </location> "); 
			sb0.append("	                                <serviceProviderOrganization  classCode=\"ORG\" determinerCode=\"INSTANCE\">  "); 
			sb0.append("	                                    <!--DE08.10.026.00 科室名称-->"); 
			sb0.append("	                                    <id>"); 
			sb0.append("	                                        <item extension=\"\" />  "); 
			sb0.append("	                                    </id>   "); 
			sb0.append("	                                    <name xsi:type=\"BAG_EN\">"); 
			sb0.append("	                                        <item use=\"IDE\">"); 
			sb0.append("	                                            <part value=\"\" /> "); 
			sb0.append("	                                        </item> "); 
			sb0.append("	                                    </name> "); 
			sb0.append("	                                    <asOrganizationPartOf   "); 
			sb0.append("	                                        classCode=\"PART\">   "); 
			sb0.append("	                                        <!-- DE08.10.054.00 病区名称 -->"); 
			sb0.append("	                                        <wholeOrganization  classCode=\"ORG\" determinerCode=\"INSTANCE\">  "); 
			sb0.append("	                                            <id>"); 
			sb0.append("	                                                <item extension=\"\" />"); 
			sb0.append("	                                            </id> "); 
			sb0.append("	                                            <name xsi:type=\"BAG_EN\">  "); 
			sb0.append("	                                                <item use=\"IDE\">  "); 
			sb0.append("	                                                    <part value=\"\" />   "); 
			sb0.append("	                                                </item> "); 
			sb0.append("	                                            </name> "); 
			sb0.append("	                                        </wholeOrganization>"); 
			sb0.append("	                                    </asOrganizationPartOf> "); 
			sb0.append("	                                </serviceProviderOrganization>"); 
			sb0.append("	                            </serviceDeliveryLocation>  "); 
			sb0.append("	                        </location>   "); 
			sb0.append("	                        <!--诊断(检查申请原因) -->"); 
			sb0.append("	                        <pertinentInformation1 typeCode=\"PERT\" xsi:nil=\"false\"> "); 
			sb0.append("	                            <observationDx classCode=\"OBS\"  moodCode=\"EVN\">"); 
			sb0.append("	                                <!--诊断类别编码 必须项已使用 --> "); 
			sb0.append("	                                <code code=\"\"   codeSystem=\"1.2.156.112635.1.1.29\"> "); 
			sb0.append("	                                    <!--诊断类别名称 -->"); 
			sb0.append("	                                    <displayName value=\"\" />  "); 
			sb0.append("	                                </code> "); 
			sb0.append("	                                <!-- 必须项未使用 -->   "); 
			sb0.append("	                                <statusCode code=\"active\" />"); 
			sb0.append("	                                <!-- 病人主诉-->"); 
			sb0.append("	                                <patientComp value=\"\"/> "); 
			sb0.append("	                                <!--诊断日期 -->"); 
			sb0.append("	                                <effectiveTime> "); 
			sb0.append("	                                    <any value=\"\" />"); 
			sb0.append("	                                </effectiveTime>"); 
			sb0.append("	                                <!-- 疾病编码 必须项已使用 -->"); 
			sb0.append("	                                <value code=\"\" codeSystem=\"2.16.156.10011.2.3.3.11\"> "); 
			sb0.append("	                                    <!-- 疾病名称 -->   "); 
			sb0.append("	                                    <displayName value=\"\" />   "); 
			sb0.append("	                                </value>"); 
			sb0.append("	                            </observationDx>"); 
			sb0.append("	                        </pertinentInformation1>"); 
			sb0.append("	                    </encounter>  "); 
			sb0.append("	                </componentOf1>   "); 
			sb0.append("	            </observationRequest> "); 
			sb0.append("	        </subject>"); 
			sb0.append("	    </controlActProcess>  "); 
			sb0.append("	</POOR_IN200901UV>"); 

			xml=sb0.toString();
			System.err.println(xml);
		}
		return xml; 
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
		ZlReqPatinfoDTO zlp = new ZlReqPatinfoDTO();
		zlp=getzl_patinfoFromNum(exam_num);
		Connection tjtmpconnect = null;
		PreparedStatement preparedStatement = null;
		int lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_pacs_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_ids='"+cicode+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,charging_item_ids,zl_pat_id,req_id,createdate) values('" 
				+ ei.getId() + "','" +cicode + "','" +zlp.getZl_pat_id() + "','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
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
			eu.setValue("DR");
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
}
