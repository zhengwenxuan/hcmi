package com.hjw.webService.client.hokai305;

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

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.DTO.ZlReqPatinfoDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
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
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class LisSendMessageHK305{
private LisMessageBody lismessage;
private static JdbcQueryManager jdbcQueryManager;
private static CustomerInfoService customerInfoService;
private static ConfigService configService;
private static WebserviceConfigurationService webserviceConfigurationService;
static {
	init();
}

public static void init() {
	WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	configService = (ConfigService) wac.getBean("configService");
	webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
}

	public LisSendMessageHK305(LisMessageBody lismessage){
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
		ExamInfoUserDTO eu = configService.getExamInfoForNum(lismessage.getCustom().getExam_num());
		//1，调用his获取申请单号
		TranLogTxt.liswriteEror_to_txt(logname, "1，调用his获取申请单号-开始");
		ResultHeader rhone = new ResultHeader();
		String req_no = "";
		try {
			StringBuffer sb = new StringBuffer("");
			sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
			sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                        ");
			sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                         ");
			sb.append("    <!-- 消息创建时间(1..1) -->                                                                                    ");
			sb.append("    <creationTime value=\""+lismessage.getCreationTime_value()+"\"/>                                                                         ");
			sb.append("    <!-- 服务编码，S0087代表申请单号生成接口(1..1)-->                                                              ");
			sb.append("    <interactionId extension=\"S0087\"/>                                                                             ");
			sb.append("    <!-- 接受者(1..1) -->                                                                                          ");
			sb.append("    <receiver code=\"SYS002\"/>                                                                                ");
			sb.append("    <!-- 发送者(1..1) -->                                                                                          ");
			sb.append("    <sender code=\"SYS009\"/>                                                                                  ");
			sb.append("    <!-- 封装的消息内容 -->                                                                                        ");
			sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                            ");
			sb.append("        <!-- 消息交互类型 @code: 新增/更新 :new 撤销:delete -->                                                    ");
			sb.append("        <code value=\"new\"/>                                                                                        ");
			sb.append("        <subject typeCode=\"SUBJ\">                                                                                  ");
			sb.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                    ");
			sb.append("                <!--需要生成的申请单号类型：1:检验，2:检查(0..1)-->                                                ");
			sb.append("                <typeCode code=\"1\" value=\"检验\"/>                                                                  ");
			sb.append("            </observationRequest>                                                                                  ");
			sb.append("        </subject>                                                                                                 ");
			sb.append("        <!--操作者信息(0..1)-->                                                                                    ");
			sb.append("        <author code=\"\" displayName=\"\"/>                                                                 ");
			sb.append("    </controlActProcess>                                                                                           ");
			sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->  ");
			sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>   ");
			sb.append("    <!-- 消息创建时间(1..1) -->");
			sb.append("    <creationTime value=\""+lismessage.getCreationTime_value()+"\"/>   ");
			sb.append("    <!-- 服务编码，S0087代表申请单号生成接口(1..1)-->");
			sb.append("    <interactionId extension=\"S0087\"/> ");
			sb.append("    <!-- 接受者(1..1) -->  ");
			sb.append("    <receiver code=\"SYS002\"/>");
			sb.append("    <!-- 发送者(1..1) -->  ");
			sb.append("    <sender code=\"SYS009\"/>  ");
			sb.append("    <!-- 封装的消息内容 -->");
			sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">  ");
			sb.append("        <!-- 消息交互类型 @code: 新增/更新 :new 撤销:delete -->");
			sb.append("        <code value=\"new\"/>");
			sb.append("        <subject typeCode=\"SUBJ\">  ");
			sb.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
			sb.append("                <!--需要生成的申请单号类型：1:检验，2:检查(0..1)-->");
			sb.append("                <typeCode code=\"1\" value=\"检验\"/>");
			sb.append("            </observationRequest>  ");
			sb.append("        </subject> ");
			sb.append("        <!--操作者信息(0..1)-->");
			sb.append("        <author code=\"\" displayName=\"\"/> ");
			sb.append("    </controlActProcess>   ");
			sb.append("</POOR_IN200901UV>");
			
			WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
		    wcf = webserviceConfigurationService.getWebServiceConfig("GET_REQNO");
		    String get_req_no_url = wcf.getConfig_url().trim();
		    
		    TranLogTxt.liswriteEror_to_txt(logname, "get_req_no_url:" + get_req_no_url);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString());
			String result = HttpUtil.doPost_Xml(get_req_no_url, sb.toString(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				ResultHeader rh = ResContralBeanHK.getResGET_REQNO(result);
				if("AA".equals(rh.getTypeCode())) {
					req_no = rh.getSourceMsgId();//申请单号
				} else {
					TranLogTxt.liswriteEror_to_txt(logname, "获取申请单号返回错误:" + rh.getText());
					rhone.setTypeCode("AE");
					rhone.setText("获取申请单号返回错误:" + rh.getText());
				}
			} else {
				TranLogTxt.liswriteEror_to_txt(logname, "获取申请单号无返回");
				rhone.setTypeCode("AE");
				rhone.setText("获取申请单号无返回");
			}
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if("AE".equals(rhone.getTypeCode())) {
			return rhone;
		}
		
		//2，检验申请单新增
		TranLogTxt.liswriteEror_to_txt(logname, "2，检验申请单新增-开始");
		try {
			StringBuffer sb = new StringBuffer("");
			sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
			sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->");
			sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>");
			sb.append("    <!-- 消息创建时间(1..1) -->");
			sb.append("    <creationTime value=\"" + lismessage.getCreationTime_value() + "\"/>");
			sb.append("    <!-- 服务编码，S0038代表检验申请新增(1..1)-->");
			sb.append("    <interactionId extension=\"S0038\"/>");
			sb.append("    <!-- 接受者(1..1) -->");
			sb.append("    <receiver code=\"SYS003\"/>");
			sb.append("    <!-- 发送者(1..1) -->");
			sb.append("    <sender code=\"SYS009\"/>");
			sb.append("    <controlActProcess classCode=\"ACTN\" moodCode=\"EVN\">");
			sb.append("        <!-- 状态代码（create、update、delete）(1..1) -->");
			sb.append("        <code value=\"create\"/>");
			sb.append("        <subject typeCode=\"SUBJ\">");
			sb.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
			sb.append("                <id>");
			sb.append("                    <!--电子申请单编号(1..1)-->");
			sb.append("                    <item extension=\""+req_no+"\" root=\"2.16.156.10011.1.24\"/>");
			sb.append("                </id>");
			sb.append("                <!--申请单描述(0..1)-->");
			sb.append("                <text value=\"申请单描述\"/>");
			sb.append("                <!--申请单状态，见申请单状态字典(1..1)-->");
			sb.append("                <statusCode code=\"1\" value=\"开立\"/>");
			sb.append("                <!--申请时间(0..1)-->");
			sb.append("                <effectiveTime value=\""+lismessage.getCreationTime_value()+"\"/>");
			sb.append("                <!--优先级别(0..1)-->");
			sb.append("                <priority code=\"0\" displayName=\"常规\"/>");
			sb.append("                <!--费用类别 (0..1)-->");
			sb.append("                <chargeCode code=\"1\" displayName=\"自费\"/>");
			sb.append("                <!--注意事项(0..1) -->");
			sb.append("                <annotationText value=\"注意XXX\"/>");
			sb.append("                <!--开单医生/送检医生(1..1) -->");
			sb.append("                <author typeCode=\"AUT\">");
			sb.append("                    <!--开单时间(0..1)-->");
			sb.append("                    <time value=\""+DateTimeUtil.getDateTimes()+"\"/>");
			sb.append("                    <!--开单者签名编码/名称-CA(0..1)-->");
			sb.append("                    <signatureCode code=\"S\" value=\""+lismessage.getDoctor().getDoctorName()+"\"/>");
			sb.append("                    <assignedEntity classCode=\"ASSIGNED\">");
			sb.append("                        <!--开立者 ID(0..1)-->");
			sb.append("                        <id extension=\""+lismessage.getDoctor().getDoctorCode()+"\" root=\"2.16.156.10011.1.4\"/>");
			sb.append("                        <!--开立者姓名(0..1)-->");
			sb.append("                        <name value=\"梁雪梅\"/>");
			sb.append("                        <!-- 申请科室信息(0..1) -->");
			sb.append("                        <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
			sb.append("                            <!--医疗卫生机构（科室） ID(0..1)-->");
			sb.append("                            <id extension=\"4115\" root=\"2.16.156.10011.1.26\"/>");
			sb.append("                            <!--开立科室(0..1)-->");
			sb.append("                            <name value=\"体检中心\"/>");
			sb.append("                        </representedOrganization>");
			sb.append("                    </assignedEntity>");
			sb.append("                </author>");
			sb.append("                <!-- 标本信息(0..1) -->");
			sb.append("                <specimen>");
			sb.append("                    <!--标本ID/或者条码ID(0..1)-->");
			sb.append("                    <id extension=\""+comps.getReq_no()+"\"/>");
			sb.append("                    <!--标本类别代码(0..1)-->");
			sb.append("                    <code code=\"\" displayName=\"\"/>");
			sb.append("                    <!--标本描述(0..1)-->");
			sb.append("                    <text value=\"描述\"/>");
			sb.append("                </specimen>");
			sb.append("                <!-- 容器类型编码/名称(0..1) -->");
			sb.append("                <participant code=\"容器类型编码\" displayName=\"容器类型名称\"/>");
			sb.append("                <!-- 多个检验项目循环component2(1..1) -->");
			
			int count =1;
			for (LisComponent comp : comps.getItemList()) {	
				long chargingItemid = Long.parseLong(comp.getChargingItemid());
				ChargingItemDTO chargingItem = customerInfoService.getChargingItemForId(chargingItemid);
				int lisid=updatezl_req_item(lismessage.getCustom().getExam_num(),req_no,comps.getReq_no(),comp.getChargingItemid(),logname);
				if(lisid>0){
					
					sb.append("                <component2>");
					sb.append("                    <observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
					sb.append("                        <id>");
					sb.append("                            <!--项目ID(0..1)  comp.getChargingItemid()-->");
					sb.append("                            <item extension=\""+ count++ +"\" root=\"2.16.156.10011.1.28\"/>");
					sb.append("                        </id>");
					sb.append("                        <!--检验项目编码/名称 (1..1)-->");
					sb.append("                        <code code=\""+comp.getHis_num()+"\" displayName=\""+comp.getItemName()+"\"/>");
					sb.append("                        <!--正常时为active，否则为disable(0..1)-->");
					sb.append("                        <statusCode code=\"active\"/>");
					sb.append("                        <!--检验方法编码/名称(0..1) -->");
					sb.append("                        <methodCode code=\"\" displayName=\"检验方法描述\"/>");
					sb.append("                        <!--执行科室(0..1) -->");
					sb.append("                        <location code=\""+comp.getServiceDeliveryLocation_code()+"\" displayName=\""+comp.getServiceDeliveryLocation_name()+"\"/>");
					sb.append("                        <!--价格(0..1)-->");
					sb.append("                        <price value=\""+chargingItem.getAmount()+"\"/>");
					sb.append("                    </observationRequest>");
					sb.append("                </component2>");
			}
			}
			sb.append("                <!--就诊信息(0..1) -->");
			sb.append("                <componentOf1 contextConductionInd=\"false\" typeCode=\"COMP\">");
			sb.append("                    <encounter classCode=\"ENC\" moodCode=\"EVN\">");
			sb.append("                        <id>");
			sb.append("                            <!-- 就诊次数(0..1) -->");
			sb.append("                            <item extension=\"\" root=\"1.2.156.112635.1.2.1.7\"/>");
			sb.append("                            <!-- 就诊流水号(1..1) -->");
			sb.append("                            <item extension=\"\" root=\"1.2.156.112635.1.2.1.6\"/>");
			sb.append("                        </id>");
			sb.append("                        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->");
			sb.append("                        <code code=\"1\" displayName=\"门诊\"/>");
			sb.append("                        <!--费用类别 (0..1)-->");
			sb.append("                        <chargeCode code=\"1\" displayName=\"自费\"/>");
			sb.append("                        <!--就诊日期时间 (1..1)-->");
			sb.append("                        <effectiveTime value=\""+lismessage.getCreationTime_value()+"\"/>");
			sb.append("                        <!--病人(0..1) -->");
			sb.append("                        <patient classCode=\"PAT\">");
			sb.append("                            <id>");
			sb.append("                                <!--急诊号标识(0..1) -->");
			sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.10\"/>");
			sb.append("                                <!--门诊号标识(0..1) -->");
			sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.11\"/>");
			sb.append("                                <!--住院号标识(0..1)-->");
			sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.12\"/>");
			sb.append("                                <!--患者 ID 标识(0..1)-->");
			sb.append("                                <item extension=\""+lismessage.getCustom().getPersonid()+"\" root=\"2.16.156.10011.0.2.2\"/>");
			sb.append("                            </id>");
			sb.append("                            <!--患者当前就诊状态，见就诊状态字典(0..1)-->");
			sb.append("                            <statusCode code=\"1\" value=\"挂号\"/>");
			sb.append("                            <!--个人信息 必须项已使用(0..1) -->");
			sb.append("                            <patientPerson classCode=\"PSN\">");
			sb.append("                                <!-- 身份证号/医保卡号(0..1) -->");
			sb.append("                                <id>");
			sb.append("                                    <!-- 身份证号(0..1) -->");
			sb.append("                                    <item extension=\""+eu.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>");
			sb.append("                                    <!-- 医保卡号(0..1) -->");
			sb.append("                                    <item extension=\"\" root=\"2.16.156.10011.1.15\"/>");
			sb.append("                                </id>");
			sb.append("                                <!--患者姓名(0..1)-->");
			sb.append("                                <name value=\""+eu.getUser_name()+"\"/>");
			sb.append("                                <!--性别(0..1)-->");
			sb.append("                                <administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" displayName=\""+eu.getSex()+"\"/>");
			sb.append("                                <!--出生日期(0..1)-->");
			sb.append("                                <birthTime value=\""+lismessage.getCustom().getBirthtime()+"\"/>");
			sb.append("                                <!--年龄(0..1)-->");
			sb.append("                                <age units=\"岁\" value=\""+lismessage.getCustom().getOld()+"\"/>");
			sb.append("                                <!-- 家庭电话，电子邮件等联系方式");
			sb.append("                                    @use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->");
			sb.append("                                <!-- 患者电话或电子邮件(1..*) -->");
			sb.append("                                <telecom use=\"H\" value=\""+eu.getPhone()+"\"/>");
			sb.append("                                <telecom use=\"PUB\" value=\""+eu.getPhone()+"\"/>");
			sb.append("                                <telecom use=\"EMA\" value=\""+eu.getEmail()+"\"/>");
			sb.append("                            </patientPerson>");
			sb.append("                        </patient>");
			sb.append("                        <!--住院位置-住院有此节点，其他可无此节点(0..1)-->");
			sb.append("                        <location typeCode=\"LOC\">");
			sb.append("                            <!--@root类别， @extension:病床号 @displayName:病床名称-->");
			sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.22\"/>");
			sb.append("                            <!--@root类别， @extension:病房编码 @displayName:病房名称-->");
			sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.21\"/>");
			sb.append("                            <!--@root类别， @extension:科室编码 @displayName:科室名称-->");
			sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.26\"/>");
			sb.append("                            <!--@root类别， @extension:病区编码 @displayName:病区名称-->");
			sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.27\"/>");
			sb.append("                        </location>");
			sb.append("                        <!--诊断(检查申请原因)(0..*) -->");
			sb.append("                        <pertinentInformation1 typeCode=\"PERT\">");
			sb.append("                            <observationDx classCode=\"OBS\" moodCode=\"EVN\">");
			sb.append("                                <!--诊断类别编码/名称(0..1) -->");
			sb.append("                                <code code=\"\" displayName=\"\"/>");
			sb.append("                                <!--诊断代码及描述 (0..1)-->");
			sb.append("                                <value code=\"1\" displayName=\"\"/>");
			sb.append("                                <!--建议描述 (0..1)-->");
			sb.append("                                <suggestionText/>");
			sb.append("                                <!--诊断时间(0..1) -->");
			sb.append("                                <effectiveTime value=\"\"/>");
			sb.append("                                <!--诊断医生工号/姓名 (0..1)-->");
			sb.append("                                <author code=\"\" displayName=\"\"/>");
			sb.append("                            </observationDx>");
			sb.append("                        </pertinentInformation1>");
			sb.append("                    </encounter>");
			sb.append("                </componentOf1>");
			sb.append("            </observationRequest>");
			sb.append("        </subject>");
			sb.append("    </controlActProcess>");
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
		if("AE".equals(rhone.getTypeCode())) {
			return rhone;
		}
		
		//3，发送采样状态并绑定条码号
		TranLogTxt.liswriteEror_to_txt(logname, "3，发送采样状态并绑定条码号-开始");
		try {
			StringBuffer sb = new StringBuffer("");
			sb.append("<POLB_IN224000UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">   ");
			sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                   ");
			sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                                  ");
			sb.append("    <!-- 消息创建时间(1..1) -->                                                                                               ");
			sb.append("    <creationTime value=\""+lismessage.getCreationTime_value()+"\"/>                                                                                  ");
			sb.append("    <!-- 服务编码，S0063代表检验状态信息更新(1..1)-->                                                                         ");
			sb.append("    <interactionId extension=\"S0063\"/>                                                                                      ");
			sb.append("    <!-- 接受者(1..1) -->                                                                                                     ");
			sb.append("    <receiver code=\"SYS003\"/>                                                                                         ");
			sb.append("    <!-- 发送者(1..1) -->                                                                                                     ");
			sb.append("    <sender code=\"SYS009\"/>                                                                                           ");
			sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                                   ");
			sb.append("        <subject typeCode=\"SUBJ\">                                                                                           ");
			sb.append("            <observationReport classCode=\"GROUPER\" moodCode=\"EVN\">                                                        ");
			sb.append("                <id>                                                                                                          ");
			sb.append("                    <!--电子申请单编号(1..1)-->                                                                               ");
			sb.append("                    <item extension=\""+req_no+"\" root=\"2.16.156.10011.1.24\"/>                                             ");
			sb.append("                </id>                                                                                                         ");
			sb.append("                <!-- 操作日期(0..1) -->                                                                                       ");
			sb.append("                <time value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                              ");
			sb.append("                <!--操作者签名编码/名称-CA(0..1)-->                                                                           ");
			sb.append("                <signatureCode code=\"\" value=\"\"/>                                                                  ");
			sb.append("                <!-- 操作人编码/名称 (0..1) -->                                                                               ");
			sb.append("                <performer code=\"\" displayName=\"\"/>                                                              ");
			sb.append("                <!--执行科室 -->                                                                                              ");
			sb.append("                <location code=\"\" displayName=\"\">                                                        ");
			sb.append("                    <window code=\"\" displayName=\"\"/>                                                      ");
			sb.append("                </location>                                                                                                   ");
			sb.append("                <!--申请单状态(0..1)-->                                                                                       ");
			sb.append("                <statusCode code=\"5\" value=\"采集\">                                                             ");
			sb.append("                    <!--撤销或拒收时的原因描述(0..1)-->                                                                       ");
			sb.append("                    <originalText value=\"撤销或拒收原因描述\"/>                                                              ");
			sb.append("                </statusCode>                                                                                                 ");
			sb.append("                <!-- 标本信息-有标本状态时有此节点(0..1) -->                                                                  ");
			sb.append("                <specimen classCode=\"SPEC\">                                                                                 ");
			sb.append("                    <!--标本ID/或者条码ID(0..1)  comps.getReq_no()-->                                                                            ");
			sb.append("                    <id extension=\""+comps.getReq_no()+"\"/>                                                                            ");
			sb.append("                    <!--标本类别代码(0..1)-->                                                                                 ");
			sb.append("                    <code code=\"1\" displayName=\"名称\"/>                                                           ");
			sb.append("                    <!--标本描述(0..1)-->                                                                                     ");
			sb.append("                    <text value=\"描述\"/>                                                                                    ");
			sb.append("                </specimen>                                                                                                   ");
			sb.append("                <!--记录对象(0..1)-->                                                                                         ");
			sb.append("                <recordTarget contextControlCode=\"OP\" typeCode=\"RCT\">                                                     ");
			sb.append("                    <patient classCode=\"PAT\">                                                                               ");
			sb.append("                        <!--PatientID(0..1)-->                                                                                ");
			sb.append("                        <id>                                                                                                  ");
			sb.append("                            <item extension=\""+lismessage.getCustom().getPersonid()+"\" root=\"2.16.156.10011.0.2.2\"/>                                           ");
			sb.append("                        </id>                                                                                                 ");
			sb.append("                        <!--患者姓名(0..1)-->                                                                                 ");
			sb.append("                        <name value=\""+eu.getUser_name()+"\"/>                                                                                ");
			sb.append("                        <!--性别(0..1)-->                                                                                     ");
			sb.append("                        <administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" displayName=\""+eu.getSex()+"\"/>                                           ");
			sb.append("                    </patient>                                                                                                ");
			sb.append("                </recordTarget>                                                                                               ");
			sb.append("            </observationReport>                                                                                              ");
			sb.append("        </subject>                                                                                                            ");
			sb.append("    </controlActProcess>                                                                                                      ");
			sb.append("</POLB_IN224000UV01>                                                                                                          ");
			
			
			WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
		    wcf = webserviceConfigurationService.getWebServiceConfig("ExamStarts_IsSample");
		    String get_req_no_url = wcf.getConfig_url().trim();
		    
		    TranLogTxt.liswriteEror_to_txt(logname, "get_req_no_url:" + get_req_no_url);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString());
			String result = HttpUtil.doPost_Xml(get_req_no_url, sb.toString(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);
			
			
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();				
				rhone = ResContralBeanHK.getRes(result);				
			}
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		
		ZlReqItemDTO zri = new ZlReqItemDTO();
		zri.setExam_info_id(eu.getId());
		zri.setZl_pat_id(lismessage.getCustom().getPersonid());
		zri.setReq_id(req_no);
		zri.setLis_req_code(comps.getReq_no());
		//configService.insert_zl_req_item(zri, logname);
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
	 * @param req_no 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_item(String exam_num, String req_no,String req_id,String ciid,String logname){
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
					+"'  and charging_item_id='"+ciid+"' and req_id='"+req_no+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_item(exam_info_id,charging_item_id,zl_pat_id,lis_item_id,req_id,lis_req_code,createdate) values('" 
				+ ei.getId() + "','" +ciid + "','" +zlp.getZl_pat_id() + "','"+2+"','"+req_no+"','"+req_id+"','"+DateTimeUtil.getDateTime()+"')";
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
