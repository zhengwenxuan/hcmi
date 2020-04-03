package com.hjw.webService.client.hghis;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.DateUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.hghis.SMS.SmsSendInfo;
import com.hjw.wst.DTO.BatchDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.mascloud.model.MoModel;
import com.mascloud.model.StatusReportModel;
import com.mascloud.sdkclient.Client;
import com.mascloud.util.JsonUtil;
import com.synjones.framework.persistence.JdbcQueryManager;


public class AutoSendSMSClient {
	
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
	

	public MSGResBody getSMScustome(String url, int days, String logname){
		MSGResBody 	msg = new MSGResBody(); 
		String baodaosql = " select * from sms_send_info where is_active='Y' and is_send_fly='N' and send_type='D' and sms_type='1' and sms_status='0' ";
		List<SmsSendInfo> baodaolist = jdbcQueryManager.getList(baodaosql, SmsSendInfo.class);
		for (int i = 0; i < baodaolist.size(); i++) {
			ExamInfoUserDTO ei = configService.getExamInfoForNum( baodaolist.get(i).getExam_num());
			 msg = getMessage(url, ei, logname, "D");//报道发送短信
			 if(msg.getRescode().equals("AA")){
				 
				 TranLogTxt.liswriteEror_to_txt(logname, "res:报道发送短信成功： " +baodaolist.get(i).getExam_num() );
			 }
		}
		
		String inputsql = " select * from sms_send_info where is_active='Y' and is_send_fly='N' and send_type='P' and sms_type='1' and sms_status='0'  ";
		List<SmsSendInfo> inputlist = jdbcQueryManager.getList(inputsql, SmsSendInfo.class);
		for (int i = 0; i < inputlist.size(); i++) {
			ExamInfoUserDTO ei = configService.getExamInfoForNum(inputlist.get(i).getExam_num());
			 msg = getMessage(url, ei, logname, "P");//打印发送短信
			 if(msg.getRescode().equals("AA")){
				 
				 TranLogTxt.liswriteEror_to_txt(logname, "res:打印发送短信成功： " +inputlist.get(i).getExam_num() );
			 }
		}
		
		
		return msg;
	}
	
		public MSGResBody getMessage(String url, ExamInfoUserDTO ei, String logname,String Sendtype){
				
			MSGResBody 	msg = new MSGResBody(); 
			ei = configService.getExamInfoForNum(ei.getExam_num());
			if(ei != null ){
				Connection conn = null;
    			Statement statement = null;	
				if(ei.getPhone()!=null && ei.getPhone().length()>0){
					//企业名称
					String ecname = configService.getCenterconfigByKey("SEND_SMS_LOGIN_COM_NAME").getConfig_value().trim();
					//用户名
					String userAccount = configService.getCenterconfigByKey("SEND_SMS_LOGIN_NAME").getConfig_value().trim();
					//密码
					String passWord = configService.getCenterconfigByKey("SEND_SMS_LOGIN_PAWD").getConfig_value().trim();
					//签名
					String sing = configService.getCenterconfigByKey("SEND_SMS_LOGIN_SIGN").getConfig_value().trim();
					//模板id
					String modeid_DY = configService.getCenterconfigByKey("SEND_SMS_LOGIN_MODEID_DY").getConfig_value().trim();
					String modeid_BD = configService.getCenterconfigByKey("SEND_SMS_LOGIN_MODEID_BD").getConfig_value().trim();
					
					try {
						//登录校验
						Client client = Client.getInstance();
						//boolean isLoggedin = client.login("http://112.35.4.197:15000", "hgtjzx", "123456", "黄冈市中心医院");
						System.err.println(ecname+"=企业名=");
						System.err.println(userAccount+"=用户名=");
						System.err.println(passWord+"=密码=");
						System.err.println(sing+"=签名=");
						System.err.println(modeid_BD+"=模板id=");
						
						boolean isLoggedin = client.login( url, userAccount, passWord, ecname );
							
						//boolean isLoggedin = client.login( "http://112.35.4.197:15000", "hgtjzx", "123456", "黄冈市中心医院" );
						if( isLoggedin ) {
							// 普通短信
							//int rt = client.sendDSMS( new String[]{"18639649877","15565579321","13426286242"}, "短信内容测试总检", "", 1, "WiyLzkyeA", null, true );
							//System.out.println("rt========="+ rt );
							
							//int rtm = client.sendTSMS(new String[] { "18639649877" }, "7d3ad1b1baf947ac97e6f5775e7b50ba", new String[] { "参数一" }, "123", 0, "itIvQwAuR", null);
							int rtm=8888;
							if(Sendtype.equals("D")){
								//rtm = client.sendDSMS( new String[]{ ei.getPhone() }, smsContent, "123", 1, sing, null, true );
								 rtm = client.sendTSMS(new String[] {ei.getPhone()}, modeid_BD, new String[] { ei.getUser_name() }, "", 1, sing, null);
							}else{
								//rtm = client.sendDSMS( new String[]{ mobiles }, smsContent, "123", 1, sing, null, true );
								 rtm = client.sendTSMS(new String[] {ei.getPhone()}, modeid_DY, new String[] { ei.getUser_name() }, "", 1, sing, null);
							}
							 TranLogTxt.liswriteEror_to_txt(logname, "res:发送短信短信平台返回标识：=== " +rtm +"手机号=="+ei.getPhone()+"姓名==="+ei.getUser_name());
						    

					        List<StatusReportModel> statusReportlist = client.getReport();
					        System.out.println("getReport : " + JsonUtil.toJsonString(statusReportlist));
					        


					        List<MoModel> deliverList = client.getMO();
					        System.out.println("getMO : " + JsonUtil.toJsonString(deliverList));
					        msg.setRescode("AA");
					        msg.setRestext("短信发送成功");
					        
					        
					        //插入短信 日志表
					        UUID uuid = UUID.randomUUID();
					    	String uid = uuid.toString().replace("-", "");
					        try {
					        	
					        	if(rtm==1){
					        			
					    			conn = jdbcQueryManager.getConnection();
						        	statement = conn.createStatement();
					        		conn.setAutoCommit(false);
					        		String  insertSMS="";
					        		if(Sendtype.equals("D")){
					        			
					        			 insertSMS = " insert into crm_sms_send (id,arch_num,template_id,send_user,sms_note,sms_phone,sms_batch,user_id,sms_date,sms_time,sms_status,user_type,sms_type) "
					        					+ " values('"+uid+"','"+ei.getArch_num()+"','"+modeid_BD+"','14','详情根据模板id,查询短信平台配置','"+ei.getPhone()+"','"+uid+"','14','"+DateUtil.getDateTime()+"','"+DateUtil.getDateTime()+"'"
					        					+ " ,'1','1','1') ";
					        		}else{
					        			 insertSMS = " insert into crm_sms_send (id,arch_num,template_id,send_user,sms_note,sms_phone,sms_batch,user_id,sms_date,sms_time,sms_status,user_type,sms_type) "
					        					+ " values('"+uid+"','"+ei.getArch_num()+"','"+modeid_DY+"','14','详情根据模板id,查询短信平台配置','"+ei.getPhone()+"','"+uid+"','14','"+DateUtil.getDateTime()+"','"+DateUtil.getDateTime()+"'"
					        					+ " ,'1','1','1') ";
					        		}
					        		
					        		 TranLogTxt.liswriteEror_to_txt(logname, "res:发送短信成功插入短信日志表：=== " +insertSMS );
						        	
									 
					        		 int updatecrmSms = statement.executeUpdate(insertSMS);
									 
									 
									 
									 if(updatecrmSms>0){
										
										 String updateSmsInfo = "update sms_send_info set is_send_fly='Y',sms_status='1' where exam_num='"+ei.getExam_num()+"' ";
										 TranLogTxt.liswriteEror_to_txt(logname, "res:发送短信成功修改短信中间表：=== " +updatecrmSms );
										 int UpdatesmsInfo = statement.executeUpdate(updateSmsInfo);
										
										 if(UpdatesmsInfo>0){
											 conn.commit();
										 }
									 }
					        	}
					        	
								
							} catch (SQLException e) {
								conn.rollback();
								e.printStackTrace();
							}finally {
								try {
									if (statement != null) {
										statement.close();
									}
									if (conn != null) {
										conn.setAutoCommit(true);
										conn.close();
									}
								} catch (SQLException sqle4) {
									sqle4.printStackTrace();
								}
							}
					        
					        
						} else {
							msg.setRescode("AE");
					        msg.setRestext("短信验证失败");
						}
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
					

					
				}
				
			}
			
			
			// 获取上行短信——结束
			return msg;
			
		}
}
