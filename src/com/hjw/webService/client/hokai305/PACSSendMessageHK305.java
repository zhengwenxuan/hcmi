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
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.domain.ChargingItem;
import com.hjw.wst.domain.Customer_Type;
import com.hjw.wst.domain.DataDictionary;
import com.hjw.wst.service.BatchService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PACSSendMessageHK305{
	private PacsMessageBody lismessage;
	private static ConfigService configService;
	private static BatchService batchService;
	private static JdbcQueryManager jdbcQueryManager;
	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		batchService = (BatchService) wac.getBean("batchService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	public PACSSendMessageHK305(PacsMessageBody lismessage){
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
							
							ResultHeader rhone = pacsSendMessage(url,pcs,logname,eu);
							if("AA".equals(rhone.getTypeCode())){
								ApplyNOBean ab= new ApplyNOBean();
								ab.setApplyNO(pcs.getReq_no());
								list.add(ab);
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
		TranLogTxt.liswriteEror_to_txt(logname, "ret:111" + lismessage.getMessageid() + ":" + xml);
		return rb;
	}
	
	
	private ResultHeader pacsSendMessage(String url,PacsComponents compts,String logname, ExamInfoUserDTO eu) {
		List<PacsComponent> pacsComponent = compts.getPacsComponent();
		String itemCode="";
		for (int i = 0; i < pacsComponent.size(); i++) {
			 itemCode = pacsComponent.get(0).getItemCode();
		}
		ChargingItemDTO chargitem = getChargitem(itemCode);
		
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
					sb.append("                <typeCode code=\"2\" value=\"检查\"/>                                                                  ");
					sb.append("            </observationRequest>                                                                                  ");
					sb.append("        </subject>                                                                                                 ");
					sb.append("        <!--操作者信息(0..1)-->                                                                                    ");
					sb.append("        <author code=\"\" displayName=\"\"/>                                                                 ");
					sb.append("    </controlActProcess>                                                                                           ");
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
		
		String xml="";
		String itemNames = "";
		for(PacsComponent pcs : compts.getPacsComponent()) {
			itemNames += (pcs.getItemName() + ";");
		}
		if(itemNames.length()>0) {
			itemNames.substring(0, itemNames.length()-1);
		}
		
		int lisid=updatezl_req_pacs_item(lismessage.getCustom().getExam_num(),req_no,compts.getReq_no(),"",logname);
		if(lisid>0){
	    try {StringBuffer sb0=new StringBuffer();	                                                                                                                 
			sb0.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ");
			sb0.append("	<!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                               ");
			sb0.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                              ");
			sb0.append("	<!-- 消息创建时间(1..1) -->                                                                                           ");
			sb0.append("	<creationTime value=\"" + lismessage.getCreationTime_value() + "\"/>                                                                              ");
			sb0.append("	<!-- 服务编码，S0041代表检查申请新增(1..1)-->                                                                         ");
			sb0.append("	<interactionId extension=\"S0041\"/>                                                                                  ");
			sb0.append("	<!-- 接受者(1..1) -->                                                                                                 ");
			sb0.append("	<receiver code=\"SYS001\"/>                                                                                     ");
			sb0.append("	<!-- 发送者(1..1) -->                                                                                                 ");
			sb0.append("	<sender code=\"SYS009\"/>                                                                                       ");
			sb0.append("	<!-- 封装的消息内容 -->                                                                                               ");
			sb0.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                               ");
			sb0.append("		<code value=\"create\"/>                                                                                          ");
			sb0.append("		<subject typeCode=\"SUBJ\">                                                                                       ");
			sb0.append("			<observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                       ");
			sb0.append("				<!-- 检查申请单编号 必须项已使用 -->                                                                      ");
			sb0.append("				<id>                                                                                                      ");
			sb0.append("					<item extension=\""+req_no+"\" root=\"2.16.156.10011.1.24\"/>                                         ");
			sb0.append("				</id>                                                                                                     ");
			sb0.append("				<!--检查号类别编码/类别名称/检查号(0..1)-->                                                               ");
			sb0.append("				<examId typeCode=\"\" type=\"\" value=\"\"/>                                                              ");
			sb0.append("				<!--申请单描述(0..1)-->                                                                                   ");
			sb0.append("				<text value=\""+itemNames+"\"/>                                                                              ");
			sb0.append("				<!--申请单状态，见申请单状态字典(1..1)-->                                                                 ");
			sb0.append("				<statusCode code=\"1\" value=\"开立\"/>                                                                   ");
			sb0.append("				<!--申请时间(0..1)-->                                                                                     ");
			sb0.append("				<effectiveTime value=\"" + lismessage.getCreationTime_value() + "\"/>                                                                 ");
			sb0.append("				<!--优先级别(0..1)-->                                                                                     ");
			sb0.append("				<priority code=\"0\" displayName=\"\"/>                                                               ");
			sb0.append("				<!--体征(0..1)-->                                                                                         ");
			sb0.append("				<sign value=\"正常\"/>                                                                                    ");
			sb0.append("				<!--执行科室(0..1) -->                                                                                    ");
			sb0.append("				<location code=\""+chargitem.getPerform_dept()+"\" displayName=\"\"/>                                                   ");
			//String itemCode2 = compts.getPacsComponent().get(0).getItemCode();
			//ChargingItem charitemExecType = getchargingitemExecType(itemCode, logname);
			if(chargitem.getRemark().equals("") || chargitem.getRemark() ==null){
				sb0.append("				<!--检查类型编码/名称(0..1) -->                                                                           ");
				sb0.append("				<typeCode code=\"\" displayName=\"\"/>                                                               ");
			}else{
				sb0.append("				<!--检查类型编码/名称(0..1) -->                                                                           ");
				sb0.append("				<typeCode code=\"\" displayName=\""+chargitem.getRemark()+"\"/>                                                               ");
			}
			
			
			if(chargitem.getItem_note().equals("") || chargitem.getItem_note()==null){
				sb0.append("				<!--检查子类编码/名称(0..1) -->                                                                           ");
				sb0.append("				<subTypeCode code=\"\" displayName=\"\"/>                                                            ");
			}else{
				sb0.append("				<!--检查子类编码/名称(0..1) -->                                                                           ");
				sb0.append("				<subTypeCode code=\"\" displayName=\""+chargitem.getItem_note()+"\"/>                                                            ");
			}
			
			
			sb0.append("				<!--临床症状(0..1)-->                                                                                     ");
			sb0.append("				<clinicSymptom value=\"正常\"/>                                                                           ");
			sb0.append("				<!--申请备注(0..1) -->                                                                                    ");
			sb0.append("				<memo value=\"\"/>                                                                                 ");
			sb0.append("				<!--注意事项(0..1) -->                                                                                    ");
			sb0.append("				<annotationText value=\"\"/>                                                                       ");
			sb0.append("				<!--开单医生/送检医生(1..1) -->                                                                           ");
			sb0.append("				<author typeCode=\"AUT\">                                                                                 ");
			sb0.append("					<!--开单者签名编码/名称-CA(0..1)-->                                                                   ");
			sb0.append("					<signatureCode code=\"S\" value=\"\"/>                                                          ");
			sb0.append("					<assignedEntity classCode=\"ASSIGNED\">                                                               ");
			sb0.append("						<!--开立者 ID(0..1)-->                                                                            ");
			sb0.append("						<id extension=\"\" root=\"2.16.156.10011.1.4\"/>                                ");
			sb0.append("						<!--开立者姓名(0..1)-->                                                                           ");
			sb0.append("						<name value=\"梁雪梅\"/>                                                                          ");
			sb0.append("						<!-- 申请科室信息(0..1) -->                                                                       ");
			sb0.append("						<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                           ");
			sb0.append("							<!--医疗卫生机构（科室） ID(0..1)-->                                                          ");
			sb0.append("							<id extension=\"4115\" root=\"2.16.156.10011.1.26\"/>                                   ");
			
			
			if(eu.getCustomer_type_id()==6){
				sb0.append("							<!--开立科室(0..1)-->                                                                         ");
				sb0.append("							<name value=\"体检三部\"/>                                                                    ");
			}else{
				sb0.append("							<!--开立科室(0..1)-->                                                                         ");
				sb0.append("							<name value=\"体检中心\"/>                                                                    ");
			}
			
			
			sb0.append("						</representedOrganization>                                                                        ");
			sb0.append("					</assignedEntity>                                                                                     ");
			sb0.append("				</author>                                                                                                 ");
			sb0.append("				<!--检查部位编码/名称(0..1) -->                                                                           ");
			sb0.append("				<targetSiteCode code=\"\" value=\"\"/>                                                        ");
			sb0.append("				<!-- 多个检查项目循环component2 -->                                                                       ");
			for(PacsComponent pcs : compts.getPacsComponent()) {
				int  count =1;
			sb0.append("				<component2>                                                                                              ");
			sb0.append("					<observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                               ");
			sb0.append("						<id>                                                                                              ");
			sb0.append("							<!--检查项目序号ID-->                                                                         ");
			sb0.append("							<item extension=\""+ count +"\"/>                                                                       ");
			sb0.append("						</id>                                                                                             ");
			sb0.append("						<!--检查项目编码 必须项已使用 -->                                                                 ");
			sb0.append("						<code code=\""+pcs.getPacs_num()+"\" displayName=\""+pcs.getItemName()+"\"/>                                                    ");
			sb0.append("						<!--价格(0..1)-->                                                                                 ");
			sb0.append("						<price value=\""+compts.getCosts()+"\"/>                                                                          ");
			sb0.append("					</observationRequest>                                                                                 ");
			sb0.append("				</component2>                                                                                             ");
			}
			sb0.append("				<!--就诊信息(0..1) -->                                                                                    ");
			sb0.append("				<componentOf1 contextConductionInd=\"false\" typeCode=\"COMP\">                                           ");
			sb0.append("					<encounter classCode=\"ENC\" moodCode=\"EVN\">                                                        ");
			sb0.append("						<id>                                                                                              ");
			sb0.append("							<!-- 就诊次数(0..1) -->                                                                       ");
			sb0.append("							<item extension=\"\" root=\"1.2.156.112635.1.2.1.7\"/>                                       ");
			sb0.append("							<!-- 就诊流水号(1..1) -->                                                                     ");
			sb0.append("							<item extension=\"\" root=\"1.2.156.112635.1.2.1.6\"/>                                  ");
			sb0.append("						</id>                                                                                             ");
			sb0.append("						<!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->                                     ");
			sb0.append("						<code code=\"3\" displayName=\"体检\"/>                                                           ");
			sb0.append("						<!--费用类别 (0..1)-->                                                                            ");
			sb0.append("						<chargeCode code=\"1\" displayName=\"自费\"/>                                                     ");
			//String jzcs=DateTimeUtil.getDateTimes().substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
			sb0.append("						<!--就诊日期时间 (1..1)-->                                                                        ");
			sb0.append("						<effectiveTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                         ");
			sb0.append("						<!--病人(0..1) -->                                                                                ");
			sb0.append("						<patient classCode=\"PAT\">                                                                       ");
			sb0.append("							<id>                                                                                          ");
			sb0.append("								<!--急诊号标识(0..1) -->                                                                  ");
			sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.10\"/>                              ");
			sb0.append("								<!--门诊号标识(0..1) -->                                                                  ");
			sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.11\"/>                              ");
			sb0.append("								<!--住院号标识(0..1)-->                                                                   ");
			sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.12\"/>                      ");
			sb0.append("								<!--患者 ID 标识(0..1)-->                                                                 ");
			sb0.append("								<item extension=\"" + lismessage.getCustom().getPersonid() + "\" root=\"2.16.156.10011.0.2.2\"/>                     ");
			sb0.append("							</id>                                                                                         ");
			sb0.append("							<!--患者当前就诊状态，见就诊状态字典(0..1)-->                                                 ");
			sb0.append("							<statusCode code=\"1\" value=\"挂号\"/>                                                       ");
			sb0.append("							<!--个人信息 必须项已使用(0..1) -->                                                           ");
			sb0.append("							<patientPerson classCode=\"PSN\">                                                             ");
			sb0.append("								<!-- 身份证号/医保卡号(0..1) -->                                                          ");
			sb0.append("								<id>                                                                                      ");
			sb0.append("									<!-- 身份证号(0..1) -->                                                               ");
			sb0.append("									<item extension=\""+lismessage.getCustom().getPersonidnum()+"\" root=\"2.16.156.10011.1.3\"/>                  ");
			sb0.append("									<!-- 医保卡号(0..1) -->                                                               ");
			sb0.append("									<item extension=\"\" root=\"2.16.156.10011.1.15\"/>                    ");
			sb0.append("								</id>                                                                                     ");
			sb0.append("								<!--患者姓名(0..1)-->                                                                     ");
			sb0.append("								<name value=\""+lismessage.getCustom().getName()+"\"/>                                                                    ");
			sb0.append("								<!--患者姓名全拼(0..1)-->                                                                     ");
			
			String pingYin = getPingYin(lismessage.getCustom().getName());
			
			sb0.append("								<namePhonetic value=\""+pingYin+"\"/>                                                                    ");
			sb0.append("								<!--性别(0..1)-->                                                                         ");
			sb0.append("								<administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" displayName=\""+eu.getSex()+"\"/>                               ");
			sb0.append("								<!--出生日期(0..1)-->                                                                     ");
			sb0.append("								<birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\"/>                                                           ");
			sb0.append("								<!--年龄(0..1)-->                                                                         ");
			sb0.append("								<age units=\"岁\" value=\"" + lismessage.getCustom().getOld() + "\"/>                                                          ");
			sb0.append("								<!-- 家庭电话，电子邮件等联系方式                                                         ");
			sb0.append("                                    @use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->                          ");
			sb0.append("								<!-- 患者电话或电子邮件(1..*) -->                                                         ");
			sb0.append("								<telecom use=\"H\" value=\""+lismessage.getCustom().getContact_tel()+"\"/>                                             ");
			sb0.append("								<telecom use=\"PUB\" value=\""+lismessage.getCustom().getContact_tel()+"\"/>                                           ");
			sb0.append("								<telecom use=\"EMA\" value=\"\"/>                                      ");
			sb0.append("								<!--病人来源编码/名称(0..1)-->                                                            ");
			sb0.append("								<patientSource code=\"1\" displayName=\"体检\"/>                                          ");
			sb0.append("								<!--外来医疗单位名称(0..1)-->                                                             ");
			sb0.append("								<facility code=\"1\" displayName=\"体检\"/>                                               ");
			sb0.append("								<!--身份(0..1)-->                                                                         ");
			sb0.append("								<age code=\"1\" displayName=\"体检\"/>                                                    ");
			sb0.append("							</patientPerson>                                                                              ");
			sb0.append("						</patient>                                                                                        ");
			sb0.append("						<!--住院位置-住院有此节点，其他可无此节点(0..1)-->                                                ");
			sb0.append("						<location typeCode=\"LOC\">                                                                       ");
			sb0.append("							<!--@root类别， @extension:病床号 @displayName:病床名称-->                                    ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.22\"/>                    ");
			sb0.append("							<!--@root类别， @extension:病房编码 @displayName:病房名称-->                                  ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.21\"/>                ");
			sb0.append("							<!--@root类别， @extension:科室编码 @displayName:科室名称-->                                  ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.26\"/>               ");
			sb0.append("							<!--@root类别， @extension:病区编码 @displayName:病区名称-->                                  ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.27\"/>             ");
			sb0.append("						</location>                                                                                       ");
			sb0.append("						<!--诊断(检查申请原因)(0..*) -->                                                                  ");
			sb0.append("						<pertinentInformation1 typeCode=\"PERT\">                                                         ");
			sb0.append("							<observationDx classCode=\"OBS\" moodCode=\"EVN\">                                            ");
			sb0.append("								<!--诊断类别编码/名称(0..1) -->                                                           ");
			sb0.append("								<code code=\"\" displayName=\"\"/>                                               ");
			sb0.append("								<!--诊断代码及描述 (0..1)-->                                                              ");
			sb0.append("								<value code=\"\" displayName=\"\"/>                                                  ");
			sb0.append("								<!--建议描述 (0..1)-->                                                                    ");
			sb0.append("								<suggestionText/>                                                                         ");
			sb0.append("								<!--诊断时间(0..1) -->                                                                    ");
			sb0.append("								<effectiveTime value=\"\"/>                                                 ");
			sb0.append("								<!--诊断医生工号/姓名 (0..1)-->                                                           ");
			sb0.append("								<author code=\"\" displayName=\"\"/>                                            ");
			sb0.append("							</observationDx>                                                                              ");
			sb0.append("						</pertinentInformation1>                                                                          ");
			sb0.append("					</encounter>                                                                                          ");
			sb0.append("				</componentOf1>                                                                                           ");
			sb0.append("			</observationRequest>                                                                                         ");
			sb0.append("		</subject>                                                                                                        ");
			sb0.append("	</controlActProcess>                                                                                                  ");
			sb0.append("</POOR_IN200901UV>                                                                                                        ");
		

			xml=sb0.toString();
			//System.err.println(xml);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb0.toString() + "\r\n");
			String result = HttpUtil.doPost_Xml(url,sb0.toString(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();				
				rhone = ResContralBeanHK.getResGET_pacsshenqing(result);				
			}
		
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if("AE".equals(rhone.getTypeCode())) {
			return rhone;
		}
		}
		return rhone;
	}
	
		
	
	
	/**
	 * 
	 * @param logname2 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public int updatezl_req_pacs_item(String exam_num,String req_no, String req_id,String cicode,String logname){
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
					+"'  and charging_item_ids='"+cicode+"' and req_id='"+req_no+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			
				String insertsql = "insert into zl_req_pacs_item(exam_info_id,pacs_req_code,charging_item_ids,zl_pat_id,req_id,createdate) values('" 
				+ ei.getId() + "','" + req_id + "','" +cicode + "','" +zlp.getZl_pat_id() + "','"+req_no+"','"+DateTimeUtil.getDateTime()+"')";
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
	
	/*public ChargingItem getchargingitemExecType(String cicode,String logname){
		
		String sb1 = "select top 1 a.remark,a.item_note from charging_item a where a.item_code='"+cicode+"' and isActive='Y' ";
		List<ChargingItem> list = this.jdbcQueryManager.getList(sb1, ChargingItem.class);
		ChargingItem chargingItem = new ChargingItem();
		if((list!=null)&&(list.size()>0)){
			chargingItem = list.get(0);
		}
		return chargingItem;
	}*/
	
	
	/*public Customer_Type getcustomerType(long l ,String  logname){
		
		Customer_Type Customer_Type = new Customer_Type();
		String sb1 = " select  * from customer_type where id='"+l+"' ";
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + "查询customer_type操作语句： " +sb1);
		List<Customer_Type> list = this.jdbcQueryManager.getList(sb1, Customer_Type.class);
		if((list!=null)&&(list.size()>0)){
			Customer_Type = list.get(0);
		}
		
		return Customer_Type;
	}*/
	
	
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
			eu.setValue("");
			eu.setText("");
		}else if(eu.getText().trim().length()<=0){
			eu.setValue("OT");
		}else if("超声".equals(eu.getText().trim())){
			eu.setValue("超声");
		}else if("CT".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("CT");
		}else if("DR".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("DR");
		}else if("MR".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("MR");
		}else if("胃肠镜".equals(eu.getText().trim().toUpperCase())){
			eu.setValue("胃肠镜");
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
			eu.setValue("");
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
	
	public ChargingItemDTO getChargitem(String item_code) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT perform_dept,remark,item_note FROM charging_item where item_code = '" + item_code + "' and  isActive='Y' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ChargingItemDTO.class);
		ChargingItemDTO ci = new ChargingItemDTO();
		if((map!=null)&&(map.getList().size()>0)){
			ci= (ChargingItemDTO)map.getList().get(0);	
		}
		return ci;
	} 
	
	public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
 
        char[] input = inputString.trim().toCharArray();
        String output = "";
 
        try {
            for (int i = 0; i < input.length; i++) {
                if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else
                    output += java.lang.Character.toString(input[i]);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

}
