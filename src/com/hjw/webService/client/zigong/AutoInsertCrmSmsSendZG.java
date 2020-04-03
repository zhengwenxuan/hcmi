package com.hjw.webService.client.zigong;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.crm.DTO.CompanyInfoDTO;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.wst.DTO.CrmSmsBaseTemplateDTO;
import com.hjw.wst.DTO.DataDictionaryDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.domain.CompanyInfo;
import com.hjw.wst.domain.DataDictionary;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class AutoInsertCrmSmsSendZG {

	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	@SuppressWarnings("deprecation")
	public MSGResBody getMessage(String url, int days, String logname) {

		MSGResBody msg = new MSGResBody();
		try {
			String sql = " select remark,data_code_children,id from data_dictionary where  data_code='DXMBLX' and isActive='Y'  ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: select data_dictionary=====" + sql);
			List<DataDictionary> list = this.jdbcQueryManager.getList(sql, DataDictionary.class);

			if (list != null) {
				for (DataDictionary dataDictionaryDTO : list) {
					String data_code_children = dataDictionaryDTO.getData_code_children();

					if (data_code_children.equals("") || data_code_children.length() <= 0 || data_code_children == null) {
							
						data_code_children = "error";
					} else {
						if (data_code_children.equals("SRTX")) {// 生日提醒
//=================================================================生日提醒=============================================================================
							TranLogTxt.liswriteEror_to_txt(logname, "res: SRTX select examinfouserd=====" + dataDictionaryDTO.getRemark());
							List<ExamInfoUserDTO> list2 = jdbcQueryManager.getList(dataDictionaryDTO.getRemark(), ExamInfoUserDTO.class);
							

							for (ExamInfoUserDTO examInfoUserDTO : list2) {

								//String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
								// String fssj="";//短信发送时间 //生日当天 10点钟
										
								// 生日 和当前的日期 一致 插入表
								//if (examInfoUserDTO.getId_num().equals(format)) {
								//System.err.println("生日时间"+examInfoUserDTO.getId_num()+"当前时间"+format);
								//System.err.println("体检号:"+examInfoUserDTO.getExam_num());
								//	ExamInfoUserDTO examInfoForNum = configService.getExamInfoForNum(examInfoUserDTO.getExam_num());
								LocalDate now = LocalDate.now();
								//now.plusDays(1)
								String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now.plusDays(1));
								String rq =" 10:00:00.000";
								String smssendtime = format+rq;
								System.err.println("用户id===="+examInfoUserDTO.getId());	
								msg = inserCrmSmsSend(msg, dataDictionaryDTO, examInfoUserDTO, smssendtime,logname);

								//}
							}

						}else if (data_code_children.equals("TTNJTX")) {// 团体年检提醒
//=========================================================================团体年检提醒=============================================================================
							TranLogTxt.liswriteEror_to_txt(logname, "res: TTNJTX select examinfouserd=====" + dataDictionaryDTO.getRemark());
							List<CompanyInfo> list5 = jdbcQueryManager.getList(dataDictionaryDTO.getRemark(), CompanyInfo.class);

							for (CompanyInfo com : list5) {
								
								//Date parse = DateUtil.parse(com.getCreate_Time());
								Calendar calendar = Calendar.getInstance();//日历对象
								calendar.setTime(com.getCreate_Time());//设置当前日期
								calendar.add(Calendar.DATE, -10);
								
								// 团体单位 创建时间  提前10天 的10点钟
								if (new Date().after(calendar.getTime())) {
									LocalDate now = LocalDate.now();
									String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now.plusDays(1));
									String rq =" 10:00:00.000";
									String smssendtime = format+rq;
								//	msg = inserCrmSmsSend(msg, dataDictionaryDTO, com, smssendtime,logname);

								}
							}
						
						}else if (data_code_children.equals("TTBDTX")) {// 团体备单提醒
//=========================================================================团体备单提醒=============================================================================
							TranLogTxt.liswriteEror_to_txt(logname, "res: TTBDTX select examinfouserd=====" + dataDictionaryDTO.getRemark());
							List<ExamInfoUserDTO> list4 = jdbcQueryManager.getList(dataDictionaryDTO.getRemark(), ExamInfoUserDTO.class);
							//10点发送
							String smssendtime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now().plusHours(10));
							for (ExamInfoUserDTO examInfoUserDTO : list4) {
								msg = inserCrmSmsSend(msg, dataDictionaryDTO, examInfoUserDTO, smssendtime,logname);
							}
						
						}else if (data_code_children.equals("GRNJTX")) {// 个人年检提醒
//========================================================================个人年检提醒=============================================================================
							TranLogTxt.liswriteEror_to_txt(logname, "res: GRNJTX select examinfouserd=====" + dataDictionaryDTO.getRemark());
							List<ExamInfoUserDTO> list6 = jdbcQueryManager.getList(dataDictionaryDTO.getRemark(), ExamInfoUserDTO.class);

							
							for (ExamInfoUserDTO examInfoUserDTO : list6) {
								
								Date parse = DateUtil.parse(examInfoUserDTO.getFinal_date());
								Calendar calendar = Calendar.getInstance();//日历对象
								calendar.setTime(parse);//设置当前日期
								calendar.add(Calendar.DATE, -10);
								
								// 个人总检时间  提前10天 的10点钟
								if (new Date().after(calendar.getTime())) {
									String smssendtime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now().plusDays(10).plusHours(10));

									msg = inserCrmSmsSend(msg, dataDictionaryDTO, examInfoUserDTO, smssendtime,logname);

								}
							}

						}else if (data_code_children.equals("LQBGTX")) {// 领取报告提醒
//=========================================================================领取报告提醒=============================================================================
							TranLogTxt.liswriteEror_to_txt(logname, "res: LQBGTX select examinfouserd=====" + dataDictionaryDTO.getRemark());
							
							List<ExamInfoUserDTO> list7 = jdbcQueryManager.getList(dataDictionaryDTO.getRemark(), ExamInfoUserDTO.class);
							//10点发送
							String smssendtime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now().plusHours(10));
							for (ExamInfoUserDTO examInfoUserDTO : list7) {
								msg = inserCrmSmsSend(msg, dataDictionaryDTO, examInfoUserDTO, smssendtime,logname);
							}
						
						}else {
							msg.setRescode("AE");
						}
					}

				}
			}
		} catch (Exception e) {
			msg.setRescode("AE");
			e.printStackTrace();
		}
		return msg;

	}

	//团体 年检提醒
	public MSGResBody inserCrmSmsSend(MSGResBody msg, DataDictionary dataDictionaryDTO,CompanyInfo com, String smssendtime,String logname) {
			
		Connection connection;
		String sql1="";
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		CrmSmsBaseTemplateDTO crmSmsBaseTemplate = getCrmSmsBaseTemplate(dataDictionaryDTO.getId(), logname);
		
		try {
			connection = jdbcQueryManager.getConnection();
			sql1 = " insert into crm_sms_send (id,arch_num,template_id,send_user,sms_note,sms_phone,sms_batch,user_id,sms_date,sms_time,sms_status,user_type,sms_type) "
					+ " values('" + uuid + "','" + com.getCom_Num()+ "','" + crmSmsBaseTemplate.getId()
					+ "','1','" + crmSmsBaseTemplate.getSms_note() + "','" + com.getCom_phone() + "','" + uuid
					+ "','1','" + smssendtime + "','' " + " ,'0','1','1') ";
			int executeUpdate = connection.createStatement().executeUpdate(sql1);

			if (executeUpdate > 0) {
				msg.setRescode("AE");
				msg.setRestext("短信发送成功,插入短信记录表成功!单位com_num:" + com.getCom_Num() + "===单位联系人电话:"+ com.getCom_phone());
						
			} else {
				msg.setRescode("AE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;
	}
	
	
	//个人  
	public MSGResBody inserCrmSmsSend(MSGResBody msg, DataDictionary dataDictionaryDTO,ExamInfoUserDTO examInfoUserDTO, String smssendtime,String logname) {
		
		Connection connection;
		String sql1="";
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		CrmSmsBaseTemplateDTO crmSmsBaseTemplate = getCrmSmsBaseTemplate(dataDictionaryDTO.getId(), logname);
		
		try {
			connection = jdbcQueryManager.getConnection();
			sql1 = " insert into crm_sms_send (id,arch_num,template_id,send_user,sms_note,sms_phone,sms_batch,user_id,sms_date,sms_time,sms_status,user_type,sms_type) "
					+ " values('" + uuid + "','" + examInfoUserDTO.getArch_num() + "','" + crmSmsBaseTemplate.getId()
					+ "','1','" + crmSmsBaseTemplate.getSms_note() + "','" + examInfoUserDTO.getPhone() + "','" + uuid
					+ "','"+examInfoUserDTO.getId()+"','" + smssendtime + "','' " + " ,'0','1','1') ";
			int executeUpdate = connection.createStatement().executeUpdate(sql1);

			if (executeUpdate > 0) {
				msg.setRescode("AE");
				msg.setRestext("短信发送成功,插入短信记录表成功!档案号:" + examInfoUserDTO.getArch_num() + "===电话:"+ examInfoUserDTO.getPhone()+"===体检id=="+examInfoUserDTO.getId());
						
			} else {
				msg.setRescode("AE");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return msg;
	}

	public CrmSmsBaseTemplateDTO getCrmSmsBaseTemplate(long id, String logname) {

		// CrmSmsBaseTemplateDTO crmSmsBaseTemplateDTO = new
		// CrmSmsBaseTemplateDTO();
		String sql = " select  * from crm_sms_base_template where sms_category=" + id + "  ";
		TranLogTxt.liswriteEror_to_txt(logname, "res查询短信模板表： " + sql);
		List<CrmSmsBaseTemplateDTO> list = jdbcQueryManager.getList(sql, CrmSmsBaseTemplateDTO.class);
		if (list == null || list.isEmpty()) {
			return new CrmSmsBaseTemplateDTO();
		}

		return list.get(0);
	}
	
}
