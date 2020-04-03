package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai305.bean.LisReqCustomerMessage305;
import com.hjw.webService.client.hokai305.bean.LisSendDTOHK305;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PacsReqMessage305 {
	private static JdbcQueryManager jdbcQueryManager;
	   private static ConfigService configService;
	   static {
	   	init();
	   	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
			configService = (ConfigService) wac.getBean("configService");
		}

		/**
		 * @param strbody
		 * @param logname
		 * @return
		 */
		public String getMessage(String strbody,String logname) {
			
			TranLogTxt.liswriteEror_to_txt(logname, "====================================lisByExam_num====================================");
			//ResultHeader resHeader = new ResultHeader();
			//LisMessageBody lisMessage = new LisMessageBody();
			StringBuilder sbxml = new StringBuilder();
			LisReqCustomerMessage305 lisreq = getreqNo(strbody);
			if(!lisreq.getPATIENT_ID().equals("")){
				ExamInfoUserDTO eu = getExamInfoForBarcode("",lisreq.getPATIENT_ID());
				if ((eu == null) || (eu.getId() <= 0)) {
					lisreq.setCode("AE");
					lisreq.setMessage("查无此人!!");
					
					
					sbxml = new StringBuilder();
					sbxml.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ");
					sbxml.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                 ");
					sbxml.append("    <id extension=\"22a0f9e0-4454-11dc-a6be-3603d6866807\"/>                                                                ");
					sbxml.append("    <!-- 消息创建时间(1..1) -->                                                                                             ");
					sbxml.append("    <creationTime value=\"20111129220000\"/>                                                                                ");
					sbxml.append("    <!-- 服务编码，S0040代表检验申请查询(1..1)-->                                                                           ");
					sbxml.append("    <interactionId extension=\"S0040\"/>                                                                                    ");
					sbxml.append("    <!-- 接受者(1..1) -->                                                                                                   ");
					sbxml.append("    <receiver code=\"SYS002\"/>                                                                                       ");
					sbxml.append("    <!-- 发送者(1..1) -->                                                                                                   ");
					sbxml.append("    <sender code=\"SYS009\"/>                                                                                         ");
					sbxml.append("    <!--typeCode为处理结果， AA表示成功 AE表示失败-->                                                                       ");
					sbxml.append("    <acknowledgement typeCode=\""+lisreq.getCode()+"\">                                                                                       ");
					sbxml.append("        <targetMessage>                                                                                                     ");
					sbxml.append("            <id extension=\"1ee83ff1-08ab-4fe7-b573-ea777e9bad51\"/>                                                        ");
					sbxml.append("        </targetMessage>                                                                                                    ");
					sbxml.append("        <acknowledgementDetail>                                                                                             ");
					sbxml.append("            <text value=\""+lisreq.getMessage()+"\"/>                                                                                  ");
					sbxml.append("        </acknowledgementDetail>                                                                                            ");
					sbxml.append("    </acknowledgement>                                                                                                      ");
					sbxml.append("</MCCI_IN000002UV01>		");
				return sbxml.toString();
				} else if ("Z".equals(eu.getStatus())) {
					lisreq.setCode("AE");
					lisreq.setMessage("体检者已经总检!!");
				
				} else {
					StringBuilder sb = new StringBuilder();
					 sbxml = new StringBuilder();
					 sb.append("select p.pacs_req_code,c.view_num,c.item_code,ec.item_amount,hd.dept_code,hd.dept_name,c.his_num,c.item_name,"
					    		+ "dd.dep_num,ec.id,ec.pay_status,ec.amount,c.hiscodeClass,c.id as itemId  from examinfo_charging_item ec,"
					    		+ "pacs_summary p,pacs_detail d,department_dep dd,charging_item c left join his_dict_dept hd "
					    		+ "on c.perform_dept = hd.dept_code where ec.charge_item_id = c.id and p.id = d.summary_id and "
					    		+ "d.chargingItem_num = c.item_code and c.dep_id = dd.id and ec.isActive = 'Y' and c.interface_flag = '2' "
					    		+ "and ec.change_item != 'C' and ec.pay_status != 'M' and ec.exam_status in ('N','D') "
					    		+ "and ec.examinfo_id = " + eu.getId() + " and p.examinfo_num = '" + eu.getExam_num() + "'");
//					sb.append(" and dd.dep_inter_num in ('"+requestBody.getEXECDEPTID().replaceAll(",", "','")+"') ");
					
					/*boolean isBufa = false;
					if(!isBufa){
						sb.append(" and eci.is_application = 'N'");
					}*/
				/*	
					if("Y".equals(this.configService.getCenterconfigByKey("IS_LIS_BANGDING").getConfig_value().trim())){
						sb.append(" and (sd.BarCode_Class = 0 or (sd.BarCode_Class = 1 and sed.is_binding = 1))");
					}*/
					
					//configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "查项目sql:"+sb.toString());
					List<LisSendDTOHK305> sendList = this.jdbcQueryManager.getList(sb.toString(), LisSendDTOHK305.class);
					
					String noPayItems = "";
					if("N".equals(eu.getIs_after_pay())){
						String IS_HIS_LIS_CHECK = this.configService.getCenterconfigByKey("IS_HIS_LIS_CHECK").getConfig_value().trim();
						for(int i=0;i<sendList.size();i++){
							if(("N".equals(sendList.get(i).getPay_status()))&&("Y".equals(IS_HIS_LIS_CHECK))){
								if(i == sendList.size() - 1){
									noPayItems += sendList.get(i).getItem_name();
								}else{
									noPayItems += sendList.get(i).getItem_name()+",";
								}
								sendList.remove(i);
							}
						}
					}
					
					if(sendList.size() == 0 && "".equals(noPayItems)){
						//configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "条码号【"+requestBody.getEXAM_NO()+"】没有需要发送申请的检验科室项目!");
						lisreq.setCode("AE");
						lisreq.setMessage("没有需要发送的项目!!");
					}else if(sendList.size() == 0 && (!"".equals(noPayItems))){
						//configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "条码号【"+requestBody.getEXAM_NO()+"】项目("+noPayItems+")未付费!");
						lisreq.setCode("AE");
						lisreq.setMessage("此项目未付费!!");
					}else{
						lisreq.setCode("AA");
						lisreq.setMessage("查询成功!!");
						String IS_LIS_PACS_DOCTOR_ID = configService.getCenterconfigByKey("IS_LIS_PACS_DOCTOR_ID").getConfig_value();
						//ResponseBody_LIS responseBody = new ResponseBody_LIS();
						sbxml.append("<MCCI_IN000002UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ");
						sbxml.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                 ");
						sbxml.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                                ");
						sbxml.append("    <!-- 消息创建时间(1..1) -->                                                                                             ");
						sbxml.append("    <creationTime value=\"\"/>                                                                                ");
						sbxml.append("    <!-- 服务编码，S0040代表检验申请查询(1..1)-->                                                                           ");
						sbxml.append("    <interactionId extension=\"S0040\"/>                                                                                    ");
						sbxml.append("    <!-- 接受者(1..1) -->                                                                                                   ");
						sbxml.append("    <receiver code=\"SYS003\"/>                                                                                       ");
						sbxml.append("    <!-- 发送者(1..1) -->                                                                                                   ");
						sbxml.append("    <sender code=\"SYS009\"/>                                                                                         ");
						sbxml.append("    <!--typeCode为处理结果， AA表示成功 AE表示失败-->                                                                       ");
						sbxml.append("    <acknowledgement typeCode=\""+lisreq.getCode()+"\">                                                                                       ");
						sbxml.append("        <targetMessage>                                                                                                     ");
						sbxml.append("            <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                        ");
						sbxml.append("        </targetMessage>                                                                                                    ");
						sbxml.append("        <acknowledgementDetail>                                                                                             ");
						sbxml.append("            <text value=\""+lisreq.getMessage()+"\"/>                                                                                  ");
						sbxml.append("        </acknowledgementDetail>                                                                                            ");
						sbxml.append("    </acknowledgement>                                                                                                      ");
						sbxml.append("    <controlActProcess classCode=\"ACTN\" moodCode=\"EVN\">                                                                 ");
						sbxml.append("        <!--查询成功且数量大于0时有此节点，否则无此节点-->                                                                  ");
						for(LisSendDTOHK305 lissend : sendList){
							sbxml.append("        <subject typeCode=\"SUBJ\">                                                                                         ");
							sbxml.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                         ");
							sbxml.append("                <id>                                                                                                        ");
							sbxml.append("                    <!--电子申请单编号(1..1)-->                                                                             ");
							sbxml.append("                    <item extension=\""+lissend.getReq_id()+"\" root=\"2.16.156.10011.1.24\"/>                                          ");
							sbxml.append("                </id>                                                                                                       ");
							sbxml.append("                <!--申请单描述(0..1)-->                                                                                     ");
							sbxml.append("                <text value=\"申请单描述\"/>                                                                                ");
							sbxml.append("                <!--申请单状态，见申请单状态字典(1..1)-->                                                                   ");
							sbxml.append("                <statusCode code=\"1\" value=\"开立\"/>                                                                     ");
							sbxml.append("                <!--申请时间(0..1)-->                                                                                       ");
							sbxml.append("                <effectiveTime value=\"20180508121212\"/>                                                                   ");
							sbxml.append("                <!--优先级别(0..1)-->                                                                                       ");
							sbxml.append("                <priority code=\"N\" displayName=\"常规\"/>                                                                 ");
							sbxml.append("                <!--费用类别 (0..1)-->                                                                                      ");
							sbxml.append("                <chargeCode code=\"1\" displayName=\"自费\"/>                                                               ");
							sbxml.append("                <!--注意事项(0..1) -->                                                                                      ");
							sbxml.append("                <annotationText value=\"注意XXX\"/>                                                                         ");
							sbxml.append("                <!--开单医生/送检医生(1..1) -->                                                                             ");
							sbxml.append("                <author typeCode=\"AUT\">                                                                                   ");
							sbxml.append("                    <!--开单时间(0..1)-->                                                                                   ");
							sbxml.append("                    <time value=\"20180508121212\"/>                                                                        ");
							sbxml.append("                    <!--开单者签名编码/名称-CA(0..1)-->                                                                     ");
							sbxml.append("                    <signatureCode code=\"S\" value=\"李医生\"/>                                                            ");
							sbxml.append("                    <assignedEntity classCode=\"ASSIGNED\">                                                                 ");
							sbxml.append("                        <!--开立者 ID(0..1)-->                                                                              ");
							sbxml.append("                        <id extension=\"120109197706015518\" root=\"2.16.156.10011.1.4\"/>                                  ");
							sbxml.append("                        <!--开立者姓名(0..1)-->                                                                             ");
							sbxml.append("                        <name value=\"李医生\"/>                                                                            ");
							sbxml.append("                        <!-- 申请科室信息(0..1) -->                                                                         ");
							sbxml.append("                        <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                             ");
							sbxml.append("                            <!--医疗卫生机构（科室） ID(0..1)-->                                                            ");
							sbxml.append("                            <id extension=\"1234567890\" root=\"2.16.156.10011.1.26\"/>                                     ");
							sbxml.append("                            <!--开立科室(0..1)-->                                                                           ");
							sbxml.append("                            <name value=\"呼吸内科\"/>                                                                      ");
							sbxml.append("                        </representedOrganization>                                                                          ");
							sbxml.append("                    </assignedEntity>                                                                                       ");
							sbxml.append("                </author>                                                                                                   ");
							sbxml.append("                <!-- 标本信息(0..1) -->                                                                                     ");
							sbxml.append("                <specimen>                                                                                                  ");
							sbxml.append("                    <!--标本ID/或者条码ID(0..1)-->                                                                          ");
							sbxml.append("                    <id extension=\"\"/>                                                                          ");
							sbxml.append("                    <!--标本类别代码(0..1)-->                                                                               ");
							sbxml.append("                    <code code=\"1\" displayName=\"标本类别名称\"/>                                                         ");
							sbxml.append("                    <!--标本描述(0..1)-->                                                                                   ");
							sbxml.append("                    <text value=\"描述\"/>                                                                                  ");
							sbxml.append("                </specimen>                                                                                                 ");
							sbxml.append("                <!-- 容器类型编码/名称(0..1) -->                                                                            ");
							sbxml.append("                <participant code=\"容器类型编码\" displayName=\"容器类型名称\"/>                                           ");
							
							sbxml.append("                <!-- 多个检验项目循环component2(1..1) -->                                                                   ");
							sbxml.append("                <component2>                                                                                                ");
							sbxml.append("                    <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                 ");
							sbxml.append("                        <id>                                                                                                ");
							sbxml.append("                            <!--项目ID(0..1)-->                                                                             ");
							sbxml.append("                            <item extension=\""+lissend.getHis_num()+"\" root=\"2.16.156.10011.1.28\"/>                                          ");
							sbxml.append("                        </id>                                                                                               ");
							sbxml.append("                        <!--检验项目编码/名称 (1..1)-->                                                                     ");
							sbxml.append("                        <code code=\"92\" displayName=\""+lissend.getItem_name()+"\"/>                                                          ");
							sbxml.append("                        <!--正常时为active，否则为disable(0..1)-->                                                          ");
							sbxml.append("                        <statusCode code=\"active\"/>                                                                       ");
							sbxml.append("                        <!--检验方法编码/名称(0..1) -->                                                                     ");
							sbxml.append("                        <methodCode code=\"94\" displayName=\"检验方法描述\"/>                                              ");
							sbxml.append("                        <!--执行科室(0..1) -->                                                                              ");
							sbxml.append("                        <location code=\"091977060\" displayName=\"检验科\"/>                                             ");
							sbxml.append("                        <!--价格(0..1)-->                                                                                   ");
							sbxml.append("                        <price value=\""+lissend.getAmount()+"\"/>                                                                                 ");
							sbxml.append("                    </observationRequest>                                                                                   ");
							sbxml.append("                </component2>                                                                                               ");
							sbxml.append("                <!--就诊信息(0..1) -->                                                                                      ");
							sbxml.append("                <componentOf1 contextConductionInd=\"false\" typeCode=\"COMP\">                                             ");
							sbxml.append("                    <encounter classCode=\"ENC\" moodCode=\"EVN\">                                                          ");
							sbxml.append("                        <id>                                                                                                ");
							sbxml.append("                            <!-- 就诊次数(0..1) -->                                                                         ");
							sbxml.append("                            <item extension=\"2\" root=\"1.2.156.112635.1.2.1.7\"/>                                         ");
							sbxml.append("                            <!-- 就诊流水号(1..1) -->                                                                       ");
							sbxml.append("                            <item extension=\"\" root=\"1.2.156.112635.1.2.1.6\"/>                                    ");
							sbxml.append("                        </id>                                                                                               ");
							sbxml.append("                        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->                                       ");
							sbxml.append("                        <code code=\"1\" displayName=\"门诊\"/>                                                             ");
							sbxml.append("                        <!--费用类别 (0..1)-->                                                                              ");
							sbxml.append("                        <chargeCode code=\"1\" displayName=\"自费\"/>                                                       ");
							sbxml.append("                        <!--就诊日期时间 (1..1)-->                                                                          ");
							sbxml.append("                        <effectiveTime value=\"20120506100000\"/>                                                           ");
							sbxml.append("                        <!--病人(0..1) -->                                                                                  ");
							sbxml.append("                        <patient classCode=\"PAT\">                                                                         ");
							sbxml.append("                            <id>                                                                                            ");
							sbxml.append("                                <!--急诊号标识(0..1) -->                                                                    ");
							sbxml.append("                                <item extension=\"\" root=\"2.16.156.10011.1.10\"/>                                ");
							sbxml.append("                                <!--门诊号标识(0..1) -->                                                                    ");
							sbxml.append("                                <item extension=\"\" root=\"2.16.156.10011.1.11\"/>                                ");
							sbxml.append("                                <!--住院号标识(0..1)-->                                                                     ");
							sbxml.append("                                <item extension=\"\" root=\"2.16.156.10011.1.12\"/>                        ");
							sbxml.append("                                <!--患者 ID 标识(0..1)-->                                                                   ");
							sbxml.append("                                <item extension=\""+eu.getPatient_id()+"\" root=\"2.16.156.10011.0.2.2\"/>                       ");
							sbxml.append("                            </id>                                                                                           ");
							sbxml.append("                            <!--患者当前就诊状态，见就诊状态字典(0..1)-->                                                   ");
							sbxml.append("                            <statusCode code=\"1\" value=\"挂号\"/>                                                         ");
							sbxml.append("                            <!--个人信息 必须项已使用(0..1) -->                                                             ");
							sbxml.append("                            <patientPerson classCode=\"PSN\">                                                               ");
							sbxml.append("                                <!-- 身份证号/医保卡号(0..1) -->                                                            ");
							sbxml.append("                                <id>                                                                                        ");
							sbxml.append("                                    <!-- 身份证号(0..1) -->                                                                 ");
							sbxml.append("                                    <item extension=\""+eu.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>                    ");
							sbxml.append("                                    <!-- 医保卡号(0..1) -->                                                                 ");
							sbxml.append("                                    <item extension=\"\" root=\"2.16.156.10011.1.15\"/>                      ");
							sbxml.append("                                </id>                                                                                       ");
							sbxml.append("                                <!--患者姓名(0..1)-->                                                                       ");
							sbxml.append("                                <name value=\""+eu.getUser_name()+"\"/>                                                                      ");
							sbxml.append("                                <!--性别(0..1)-->                                                                           ");
							sbxml.append("                                <administrativeGenderCode code=\"1\" displayName=\""+eu.getSex()+"\"/>                                 ");
							sbxml.append("                                <!--出生日期(0..1)-->                                                                       ");
							sbxml.append("                                <birthTime value=\""+eu.getBirthday()+"\"/>                                                             ");
							sbxml.append("                                <!--年龄(0..1)-->                                                                           ");
							sbxml.append("                                <age units=\"岁\" value=\""+eu.getAge()+"\"/>                                                            ");
							sbxml.append("                                <!-- 家庭电话，电子邮件等联系方式                                                           ");
							sbxml.append("                                    @use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->                            ");
							sbxml.append("                                <!-- 患者电话或电子邮件(1..*) -->                                                           ");
							sbxml.append("                                <telecom use=\"H\" value=\""+eu.getPhone()+"\"/>                                               ");
							sbxml.append("                                <telecom use=\"PUB\" value=\""+eu.getPhone()+"\"/>                                             ");
							sbxml.append("                                <telecom use=\"EMA\" value=\"\"/>                                        ");
							sbxml.append("                            </patientPerson>                                                                                ");
							sbxml.append("                        </patient>                                                                                          ");
							sbxml.append("                        <!--住院位置-住院有此节点，其他可无此节点(0..1)-->                                                  ");
							sbxml.append("                        <location typeCode=\"LOC\">                                                                         ");
							sbxml.append("                            <!--@root类别， @extension:病床号 @displayName:病床名称-->                                      ");
							sbxml.append("                            <item displayName=\"\" extension=\"001\" root=\"2.16.156.10011.1.22\"/>                      ");
							sbxml.append("                            <!--@root类别， @extension:病房编码 @displayName:病房名称-->                                    ");
							sbxml.append("                            <item displayName=\"\" extension=\"001\" root=\"2.16.156.10011.1.21\"/>                  ");
							sbxml.append("                            <!--@root类别， @extension:科室编码 @displayName:科室名称-->                                    ");
							sbxml.append("                            <item displayName=\"\" extension=\"001\" root=\"2.16.156.10011.1.26\"/>                 ");
							sbxml.append("                            <!--@root类别， @extension:病区编码 @displayName:病区名称-->                                    ");
							sbxml.append("                            <item displayName=\"\" extension=\"001\" root=\"2.16.156.10011.1.27\"/>               ");
							sbxml.append("                        </location>                                                                                         ");
							sbxml.append("                        <!--诊断(检查申请原因)(0..*) -->                                                                    ");
							sbxml.append("                        <pertinentInformation1 typeCode=\"PERT\">                                                           ");
							sbxml.append("                            <observationDx classCode=\"OBS\" moodCode=\"EVN\">                                              ");
							sbxml.append("                                <!--诊断类别编码/名称(0..1) -->                                                             ");
							sbxml.append("                                <code code=\"\" displayName=\"\"/>                                                 ");
							sbxml.append("                                <!--诊断代码及描述 (0..1)-->                                                                ");
							sbxml.append("                                <value code=\"\" displayName=\"\"/>                                                    ");
							sbxml.append("                                <!--建议描述 (0..1)-->                                                                      ");
							sbxml.append("                                <suggestionText/>                                                                           ");
							sbxml.append("                                <!--诊断时间(0..1) -->                                                                      ");
							sbxml.append("                                <effectiveTime value=\"20120506100000\"/>                                                   ");
							sbxml.append("                                <!--诊断医生工号/姓名 (0..1)-->                                                             ");
							sbxml.append("                                <author code=\"2010\" displayName=\"刘永好\"/>                                              ");
							sbxml.append("                            </observationDx>                                                                                ");
							sbxml.append("                        </pertinentInformation1>                                                                            ");
							sbxml.append("                    </encounter>                                                                                            ");
							sbxml.append("                </componentOf1>                                                                                             ");
							sbxml.append("            </observationRequest>                                                                                           ");
							sbxml.append("        </subject>                                                                                                          ");	
						}
						
						sbxml.append("        <queryAck>                                                                                                          ");
						sbxml.append("            <!--查询时消息ID(0..1)-->                                                                                       ");
						sbxml.append("            <queryId extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                       ");
						sbxml.append("            <!--查询响应代码，AE 系统错误、 NF 未找到数据、 OK 找到数据、 QE 查询参数错误(0..1)-->                          ");
						sbxml.append("            <queryResponseCode code=\"OK\"/>                                                                                ");
						sbxml.append("            <!--查询结果总数量(0..1)-->                                                                                     ");
						sbxml.append("            <resultTotalQuantity value=\""+sendList.size()+"\"/>                                                                              ");
						sbxml.append("        </queryAck>                                                                                                         ");
						sbxml.append("    </controlActProcess>                                                                                                    ");
					
						sbxml.append("</MCCI_IN000002UV01>		");
					}
					
					return sbxml.toString();
			}
				
			}else{
				lisreq.setCode("AE");
				lisreq.setMessage("体检号 患者id 身份证号为空!!!");
			}
			return sbxml.toString();
		}
		
		
		
		private ExamInfoUserDTO getExamInfoForBarcode(String req_no,String patient_ID) {
				
			String sql = " select top 1 ci.user_name,ei.patient_id,ci.id_num,ci.sex,ci.birthday,ci.phone, "
					+ " ei.id,ei.age,ei.exam_num,ei.status,ei.exam_type,ei.register_date,ei.join_date,ei.exam_times,ei.is_marriage,ei.is_after_pay,ei.address, "
					+ " com.com_num as remark1 "
					+ " from  customer_info ci, exam_info ei "
					+ " left join company_info com on com.id = ei.company_id "
					+ " where ei.exam_num='" + patient_ID + "' and ei.customer_id = ci.id and ei.is_Active='Y' ";
			//查询  需要新增 体检号 身份证号
			PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ExamInfoUserDTO.class);
			ExamInfoUserDTO eu = new ExamInfoUserDTO();
			if((map!=null)&&(map.getList().size()>0)){
				eu= (ExamInfoUserDTO)map.getList().get(0);			
			}
			if("男".equals(eu.getSex())) {
				eu.setSex("1");
			} else if("女".equals(eu.getSex())) {
				eu.setSex("0");
			} else {
				eu.setSex("%");
			}
			if(eu.getBirthday() != null && eu.getBirthday().length()>=10) {
				eu.setBirthday(eu.getBirthday().substring(0, 4) + eu.getBirthday().substring(5, 7) + eu.getBirthday().substring(8, 10));
			}
			return eu;
		}

		//获取入参值
		private LisReqCustomerMessage305 getreqNo(String xmlstr){
			//ResLisStatusBeanHK reqno= new ResLisStatusBeanHK();
			LisReqCustomerMessage305 lisreq = new LisReqCustomerMessage305();
			try{
				InputStream is = new ByteArrayInputStream(xmlstr.getBytes("utf-8"));
				Map<String, String> xmlMap = new HashMap<>();
				xmlMap.put("abc", "urn:hl7-org:v3");
				SAXReader sax = new SAXReader();
				sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
				Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
				
				//	String patient_id = document.selectSingleNode("abc:RCMR_IN000002UV02/abc:controlActProcess/abc:subject/abc:clinicalDocument/abc:recordTarget/abc:patient/abc:id/item[@root='2.16.156.10011.0.2.2']/@extension").getText();// 获取根节点
				//申请单号
			//	lisreq.setReq_no(document.selectSingleNode("abc:QUMT_IN020030UV01/abc:controlActProcess/abc:queryByParameter/abc:queryByParameterPayload/abc:id/item[@root='2.16.156.10011.1.24']/@extension").getText());
				//身份证号
			//	lisreq.setId_no(document.selectSingleNode("abc:QUMT_IN020030UV01/abc:controlActProcess/abc:queryByParameter/abc:queryByParameterPayload/abc:id/item[@root='2.16.156.10011.1.3']/@extension").getText());
				//患者id
				lisreq.setPATIENT_ID(document.selectSingleNode("abc:QUMT_IN020030UV01/abc:controlActProcess/abc:queryByParameter/abc:queryByParameterPayload/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension").getText());
				//条码号
			//	lisreq.setBarcode_id(document.selectSingleNode("abc:QUMT_IN020030UV01/abc:controlActProcess/abc:queryByParameter/abc:queryByParameterPayload/abc:id/item[@root='2.16.156.10011.1.14']/@extension").getText());
				lisreq.setCode("AA");
			}catch(Exception ex){
				lisreq.setCode("AE");
				lisreq.setMessage("xml解析失败"+com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
			return lisreq;
			
		}
		
		
		
}
