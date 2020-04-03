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

import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LisSendMessageHK{
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

	public LisSendMessageHK(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
		JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		try {			
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
				ResultHeader rhone= new ResultHeader();
				rhone=this.lisSendMessage(url, comps, logname);
				if("AA".equals(rhone.getTypeCode())){
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
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

	private ResultHeader lisSendMessage(String url,LisComponents comps,String logname) {
		
		ResultHeader rhone= new ResultHeader();
		try {
			StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
		sb.append("    <!--消息ID-->\n");
		sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\" root=\"2.16.156.10011.0\"/>\n");
		sb.append("    <!--消息发送时间-->\n");
		sb.append("    <creationTime value=\"" + lismessage.getCreationTime_value() + "\"/>\n");
		sb.append("    <interactionId extension=\"S0081\" root=\"2.16.840.1.113883.1.6\"/>\n");
		sb.append("    <processingCode code=\"P\"/>\n");
		sb.append("    <processingModeCode/>\n");
		sb.append("    <acceptAckCode code=\"AL\"/>\n");
		sb.append("    <receiver typeCode=\"RCV\">\n");
		sb.append("        <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("            <id>\n");
		sb.append("                <item extension=\"SYS001\" root=\"2.16.156.10011.0.1.1\"/>\n");
		sb.append("            </id>\n");
		sb.append("        </device>\n");
		sb.append("    </receiver>\n");
		sb.append("    <sender typeCode=\"SND\">\n");
		sb.append("        <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("            <id>\n");
		sb.append("                <item extension=\"SYS009\" root=\"2.16.156.10011.0.1.2\"/>\n");
		sb.append("            </id>\n");
		sb.append("        </device>\n");
		sb.append("    </sender>\n");
		sb.append("    <controlActProcess classCode=\"ACTN\" moodCode=\"EVN\">\n");
		sb.append("        <subject typeCode=\"SUBJ\">\n");
		sb.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">\n");
		sb.append("				<!--唯一标志(0..1)-->\n");
		sb.append("				<soleId  value = \""+comps.getReq_no()+"\"/>\n");
		sb.append("                <id>\n");
		sb.append("                    <!--电子申请单编号(1..*)-->\n");
		sb.append("                    <item extension=\""+comps.getReq_no()+"\" root=\"2.16.156.10011.1.24\"/>\n");
		sb.append("                </id>\n");
		sb.append("                <code/>\n");
		sb.append("                <!--申请单描述(0..1)-->\n");
		sb.append("                <text value=\"申请单描述\"/>\n");
		sb.append("                <!--申请单状态(1..1)-->\n");
		sb.append("                <statusCode code=\"active\"/>\n");
		sb.append("                <!--申请单有效日期时间(0..1)-->\n");
		sb.append("                <effectiveTime xsi:type=\"IVL_TS\">\n");
		sb.append("	                    <low value=\""+DateTimeUtil.getDateTimes()+"\" />"); 
		sb.append("	                    <high value=\""+DateTimeUtil.DateAdd(30)+"\" />   "); 
		sb.append("                </effectiveTime>\n");
		sb.append("                <!--优先级别(0..1)-->\n");
		sb.append("                <priorityCode code=\"N\">\n");
		sb.append("                    <displayName value=\"常规\"/>\n");
		sb.append("                </priorityCode>\n");
		sb.append("                <!--标本-->\n");
		sb.append("                <specimen>\n");
		sb.append("                    <specimen classCode=\"SPEC\">\n");
		sb.append("                        <!--标本ID/或者称条码ID-->\n");
		sb.append("                        <id extension=\""+comps.getReq_no()+"\" root=\"2.16.156.10011.1.14\"/>\n");
		sb.append("                        <!--标本类别代码-->\n");
		sb.append("                        <code code=\"1\">\n");
		sb.append("                            <displayName value=\"标本类别名称\"/>\n");
		sb.append("                        </code>\n");
		sb.append("                    </specimen>\n");
		sb.append("                </specimen>\n");
		sb.append("                <!--开单医生/送检医生 -->\n");
		sb.append("                <author typeCode=\"AUT\">\n");
		sb.append("                    <!-- 开单时间 -->\n");
		sb.append("                    <time value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("                    <!--申请单开立者签名-->\n");
		sb.append("                    <signatureText value=\"" + lismessage.getDoctor().getDoctorName() + "\"/>\n");
		sb.append("                    <assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append("                        <!--开单医生编码 -->\n");
		sb.append("                        <id>\n");
		sb.append("                            <item extension=\"" + lismessage.getDoctor().getDoctorCode() + "\" root=\"2.16.156.10011.1.4\"/>\n");
		sb.append("                        </id>\n");
		sb.append("                        <!--开单医生姓名 -->\n");
		sb.append("                        <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">\n");
		sb.append("                            <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                <item>\n");
		sb.append("                                    <part value=\"" + lismessage.getDoctor().getDoctorName() + "\"/>\n");
		sb.append("                                </item>\n");
		sb.append("                            </name>\n");
		sb.append("                        </assignedPerson>\n");
		sb.append("                        <!-- 申请科室信息 -->\n");
		sb.append("                        <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
		sb.append("                            <!--申请科室编码 必须项已使用 -->\n");
		sb.append("                            <id>\n");
		sb.append("                                <item extension=\"40\" root=\"2.16.156.10011.1.26\"/>\n");
		sb.append("                            </id>\n");
		sb.append("                            <!--申请科室名称 -->\n");
		sb.append("                            <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                <item>\n");
		sb.append("                                    <part value=\"健康管理中心\"/>\n");
		sb.append("                                </item>\n");
		sb.append("                            </name>\n");
		sb.append("                        </representedOrganization>\n");
		sb.append("                    </assignedEntity>\n");
		sb.append("                </author>\n");
		sb.append("                <!--审核者 （检验的采集人）-->\n");
		sb.append("                <verifier typeCode=\"VRF\">\n");
		sb.append("                    <!--审核日期时间 -->\n");
		sb.append("                    <time value=\"\"/>\n");
		sb.append("                    <assignedEntity classCode=\"ASSIGNED\">\n");
		sb.append("                        <!--审核者编码 -->\n");
		sb.append("                        <id>\n");
		sb.append("                            <item extension=\"\" root=\"2.16.156.10011.1.4\"/>\n");
		sb.append("                        </id>\n");
		sb.append("                        <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">\n");
		sb.append("                            <!--审核者姓名 -->\n");
		sb.append("                            <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                <item>\n");
		sb.append("                                    <part value=\"\"/>\n");
		sb.append("                                </item>\n");
		sb.append("                            </name>\n");
		sb.append("                        </assignedPerson>\n");
		sb.append("                    </assignedEntity>\n");
		sb.append("                </verifier>\n");
		sb.append("                <!-- 多个检验项目循环component2 -->\n");
		for (LisComponent comp : comps.getItemList()) {	
			long chargingItemid = Long.parseLong(comp.getChargingItemid());
			ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(chargingItemid);
			int lisid=updatezl_req_item(lismessage.getCustom().getExam_num(),comps.getReq_no(),comp.getChargingItemid(),logname);
		if(lisid>0){
		sb.append("                <component2>\n");
		sb.append("                    <observationRequest classCode=\"OBS\" moodCode=\"RQO\">\n");
		sb.append("                        <!-- 价格 默认单位：元-->\n");
		sb.append("						<bill>\n");
		sb.append("							<!-- 单据号 -->\n");
		sb.append("							<id value=\"\"/>\n");
		sb.append("							<!--  单据性质 1、收费（就是收取现金的类型） 2、记账（医院记账）-->\n");
		sb.append("							<code value = \"\"/>\n");
		sb.append("							<!-- 金额 默认单位：元-->\n");
		sb.append("							<price value=\""+chargingItem.getAmount()+"\"/>\n");
		sb.append("							<!-- 是否收费 Y/N-->\n");
		sb.append("							<isCharge value=\"Y\"/>\n");
		sb.append("						</bill>\n");
		sb.append("						<!--是否加急 -->\n");
		sb.append("						<isUrgent value=\"\"/>\n");
		sb.append("						\n");
		sb.append("                        <id>\n");
		sb.append("                            <!--医嘱ID-->\n");
		sb.append("                            <item extension=\""+lisid+"\" root=\"2.16.156.10011.1.28\"/>\n");
		sb.append("                        </id>\n");
		sb.append("                        <!--检验项目编码 必须项已使用 -->\n");
		sb.append("                        <code code=\""+chargingItem.getHis_num()+"\">\n");
		sb.append("                            <!--检验项目名称 -->\n");
		sb.append("                            <displayName value=\""+comp.getItemName()+"\"/>\n");
		sb.append("                        </code>\n");
		sb.append("                        <!-- 必须项未使用 -->\n");
		sb.append("                        <statusCode/>\n");
		sb.append("                        <methodCode>\n");
		sb.append("                            <!--检验方法编码 -->\n");
		sb.append("                            <item code=\"\">\n");
		sb.append("                                <!--检验方法名 -->\n");
		sb.append("                                <displayName value=\"检验方法描述\"/>\n");
		sb.append("                            </item>\n");
		sb.append("                        </methodCode>\n");
		sb.append("                        <!--执行科室 -->\n");
		sb.append("                        <location typeCode=\"LOC\">\n");
		sb.append("                            <!--执行时间 -->\n");
		sb.append("                            <time>\n");
		sb.append("                                <any value=\""+DateTimeUtil.getDateTimes()+"\"/>\n");
		sb.append("                            </time>\n");
		sb.append("                            <serviceDeliveryLocation classCode=\"SDLOC\">\n");
		sb.append("                                <serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
		sb.append("                                    <!--执行科室编码 -->\n");
		sb.append("                                    <id>\n");
		sb.append("                                        <item extension=\""+comp.getServiceDeliveryLocation_code()+"\" root=\"2.16.156.10011.1.26\"/>\n");
		sb.append("                                    </id>\n");
		sb.append("                                    <!-- 执行科室名称 -->\n");
		sb.append("                                    <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                        <item>\n");
		sb.append("                                            <part value=\""+comp.getServiceDeliveryLocation_name()+"\"/>\n");
		sb.append("                                        </item>\n");
		sb.append("                                    </name>\n");
		sb.append("                                </serviceProviderOrganization>\n");
		sb.append("                            </serviceDeliveryLocation>\n");
		sb.append("                        </location>\n");
		sb.append("                    </observationRequest>\n");
		sb.append("                </component2>\n");
		}
		}
		sb.append("                <subjectOf6 contextConductionInd=\"false\">\n");
		sb.append("                    <!-- 必须项 未使用 default=false -->\n");
		sb.append("                    <seperatableInd value=\"false\"/>\n");
		sb.append("                    <!--申请注意事项 -->\n");
		sb.append("                    <annotation>\n");
		sb.append("                        <text value=\"\"/>\n");
		sb.append("                        <statusCode code=\"completed\"/>\n");
		sb.append("                        <author>\n");
		sb.append("                            <assignedEntity classCode=\"ASSIGNED\"/>\n");
		sb.append("                        </author>\n");
		sb.append("                    </annotation>\n");
		sb.append("                </subjectOf6>\n");
		sb.append("                <!--就诊 -->\n");
		sb.append("                <componentOf1 contextConductionInd=\"false\" typeCode=\"COMP\">\n");
		sb.append("                    <!--就诊 -->\n");
		sb.append("                    <encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
		sb.append("                        <id>\n");
		sb.append("                            <!--门急诊号-->\n");
		sb.append("                            <item extension=\"\" root=\"2.16.156.10011.1.11\"/>\n");
		sb.append("                            <!--住院号-->\n");
		sb.append("                            <item extension=\"\" root=\"2.16.156.10011.1.12\"/>\n");
		sb.append("                        </id>\n");
		sb.append("                        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->\n");
		sb.append("                        <code code=\"3\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码 表\">\n");
		sb.append("                            <displayName value=\"体检\"/>\n");
		sb.append("                        </code>\n");
		sb.append("                        <!--必须项未使用 -->\n");
		sb.append("                        <statusCode/>\n");
		sb.append("                        <!--病人 必须项未使用 -->\n");
		sb.append("                        <subject typeCode=\"SBJ\">\n");
		sb.append("                            <patient classCode=\"PAT\">\n");
		sb.append("                                <id>\n");
		sb.append("                                    <!-- 患者ID -->\n");
		sb.append("                                    <item extension=\"" + lismessage.getCustom().getPersonid() + "\" root=\"2.16.156.10011.0.2.2\"/>\n");
		sb.append("                                </id>\n");
		sb.append("								<!--体检号码-->\n");
		sb.append("                                <peId value = \"" + lismessage.getCustom().getExam_num() + "\"/>\n");
		sb.append("								<!-- 门诊次数/住院次数 -->\n");
		ExamInfoUserDTO sjh=getHISDJH(lismessage.getCustom().getExam_num());
		String dates = DateTimeUtil.getDateTimes();	
		System.err.println(sjh.getOthers()+"=============");
		String jzcs=dates.substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
		System.err.println(jzcs);
		sb.append("								<patientCount value=\""+jzcs+"\" displayName=\"门诊次数\"/>\n");
		sb.append("                                <!--个人信息 必须项已使用 -->\n");
		sb.append("                                <patientPerson classCode=\"PSN\">\n");
		sb.append("                                    <!-- 身份证号/医保卡号 -->\n");
		sb.append("                                    <id>\n");
		sb.append("                                        <!-- 身份证号 -->\n");
		sb.append("                                        <item extension=\"" + lismessage.getCustom().getPersonidnum()
				+ "\" root=\"2.16.156.10011.1.3\"/>\n");
		sb.append("                                        <!-- 医保卡号 -->\n");
		sb.append("                                        <item extension=\"\" root=\"2.16.156.10011.1.15\"/>\n");
		sb.append("                                    </id>\n");
		sb.append("                                    <!--姓名 -->\n");
		sb.append("                                    <name xsi:type=\"DSET_EN\">\n");
		sb.append("                                        <item>\n");
		sb.append("                                            <part value=\"" + lismessage.getCustom().getName() + "\"/>\n");
		sb.append("                                        </item>\n");
		sb.append("                                    </name>\n");
		sb.append("                                    <!-- 联系电话 -->\n");
		sb.append("                                    <telecom xsi:type=\"BAG_TEL\">\n");
		sb.append("                                        <!-- 联系电话 -->\n");
		sb.append("                                        <item value=\"" + lismessage.getCustom().getTel() + "\"/>\n");
		sb.append("                                    </telecom>\n");
		sb.append("                                    <!--性别代码 -->\n");
		sb.append("                                    <administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" codeSystem=\"2.16.156.10011.2.3.3.4\"/>\n");
		sb.append("                                    <!--出生日期 -->\n");
		sb.append("                                    <birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\">\n");
		sb.append("                                        <!--年龄 -->\n");
		sb.append("                                        <originalText value=\"" + lismessage.getCustom().getOld() + "\" displayName=\"岁\"/>\n");
		sb.append("                                    </birthTime>\n");
		sb.append("                                    <!--医疗付款方式 -->\n");
		sb.append("									<chargeType value=\"\"/>\n");
		sb.append("                                    <!--住址 -->\n");
		sb.append("                                    <addr xsi:type=\"BAG_AD\">\n");
		sb.append("                                        <item use=\"H\">\n");
		sb.append("                                            <part type=\"AL\" value=\"" + lismessage.getCustom().getAddress() + "\"/>\n");
		sb.append("                                        </item>\n");
		sb.append("                                    </addr>\n");
		sb.append("                                </patientPerson>\n");
		sb.append("                            </patient>\n");
		sb.append("                        </subject>\n");
		sb.append("                        <!--住院位置-->\n");
		sb.append("                        <location typeCode=\"LOC\">\n");
		sb.append("                            <time/>\n");
		sb.append("                            <serviceDeliveryLocation classCode=\"SDLOC\">\n");
		sb.append("                                <location classCode=\"PLC\" determinerCode=\"INSTANCE\">\n");
		sb.append("                                    <!--DE01.00.026.00 病床号-->\n");
		sb.append("                                    <id>\n");
		sb.append("                                        <item extension=\"\"/>\n");
		sb.append("                                    </id>\n");
		sb.append("                                    <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                        <item use=\"IDE\">\n");
		sb.append("                                            <part value=\"\"/>\n");
		sb.append("                                        </item>\n");
		sb.append("                                    </name>\n");
		sb.append("                                    <asLocatedEntityPartOf classCode=\"LOCE\">\n");
		sb.append("                                        <location classCode=\"PLC\" determinerCode=\"INSTANCE\">\n");
		sb.append("                                            <!--DE01.00.019.00 病房号-->\n");
		sb.append("                                            <id>\n");
		sb.append("                                                <item extension=\"\"/>\n");
		sb.append("                                            </id>\n");
		sb.append("                                            <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                                <item use=\"IDE\">\n");
		sb.append("                                                    <part value=\"\"/>\n");
		sb.append("                                                </item>\n");
		sb.append("                                            </name>\n");
		sb.append("                                        </location>\n");
		sb.append("                                    </asLocatedEntityPartOf>\n");
		sb.append("                                </location>\n");
		sb.append("                                <serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
		sb.append("                                    <!--DE08.10.026.00 科室名称-->\n");
		sb.append("                                    <id>\n");
		sb.append("                                        <item extension=\"\"/>\n");
		sb.append("                                    </id>\n");
		sb.append("                                    <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                        <item use=\"IDE\">\n");
		sb.append("                                            <part value=\"\"/>\n");
		sb.append("                                        </item>\n");
		sb.append("                                    </name>\n");
		sb.append("                                    <asOrganizationPartOf classCode=\"PART\">\n");
		sb.append("                                        <!-- DE08.10.054.00 病区名称 -->\n");
		sb.append("                                        <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
		sb.append("                                            <id>\n");
		sb.append("                                                <item extension=\"\"/>\n");
		sb.append("                                            </id>\n");
		sb.append("                                            <name xsi:type=\"BAG_EN\">\n");
		sb.append("                                                <item use=\"IDE\">\n");
		sb.append("                                                    <part value=\"\"/>\n");
		sb.append("                                                </item>\n");
		sb.append("                                            </name>\n");
		sb.append("                                        </wholeOrganization>\n");
		sb.append("                                    </asOrganizationPartOf>\n");
		sb.append("                                </serviceProviderOrganization>\n");
		sb.append("                            </serviceDeliveryLocation>\n");
		sb.append("                        </location>\n");
		sb.append("                        <!--诊断(检查申请原因) -->\n");
		sb.append("                        <pertinentInformation1 typeCode=\"PERT\">\n");
		sb.append("                            <observationDx classCode=\"OBS\" moodCode=\"EVN\">\n");
		sb.append("                                <!--诊断类别编码 必须项已使用 -->\n");
		sb.append("                                <code code=\"\" codeSystem=\"1.2.156.112635.1.1.29\">\n");
		sb.append("                                    <!--诊断类别名称 -->\n");
		sb.append("                                    <displayName value=\"\"/>\n");
		sb.append("                                </code>\n");
		sb.append("                                <!-- 必须项未使用 -->\n");
		sb.append("                                <statusCode code=\"active\"/>\n");
		sb.append("                                <!--诊断日期 -->\n");
		sb.append("                                <effectiveTime>\n");
		sb.append("                                    <any value=\"\"/>\n");
		sb.append("                                </effectiveTime>\n");
		sb.append("                                <!-- 疾病编码 必须项已使用 -->\n");
		sb.append("                                <value code=\"\" codeSystem=\"2.16.156.10011.2.3.3.11\">\n");
		sb.append("                                    <!-- 疾病名称 -->\n");
		sb.append("                                    <displayName value=\"\"/>\n");
		sb.append("                                </value>\n");
		sb.append("                            </observationDx>\n");
		sb.append("                        </pertinentInformation1>\n");
		sb.append("                    </encounter>\n");
		sb.append("                </componentOf1>\n");
		sb.append("            </observationRequest>\n");
		sb.append("        </subject>\n");
		sb.append("    </controlActProcess>\n");
		sb.append("</POOR_IN200901UV>");
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
			String result = HttpUtil.doPost_Xml(url,sb.toString(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();				
				rhone = ResContralBeanHK.getRes(result);				
			}
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
	
		return rhone;
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

}
