package com.hjw.webService.client.zigong;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.MSGSendBean;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.tj180.Bean.MSGReqBean;
import com.hjw.wst.DTO.CrmSmsSendDTO;
import com.hjw.wst.DTO.CustomerInfoDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class ZiGongSendMessage {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public MSGResBody getMessage(String url, MSGSendBean eu, String logname) {
		MSGResBody 	msg = new MSGResBody(); 
		/*try {
			String sql = "select a.id,a.template_id,a.sms_note,a.sms_phone,a.sms_date,a.send_user,a.arch_num,a.sms_status,a.sms_amount,"
					+ "a.sms_batch,a.sms_type,a.user_id,a.sms_time,a.user_type,u.chi_name,u.log_name from crm_sms_send a "
					+ "  left join user_usr u on u.id=a.send_user "
					+ "  where a.sms_batch='"+eu.getBatchCode()+"' and a.sms_status=0 and sms_date<='"+DateUtil.getDateTime()+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: select crm_sms_send=====" + sql);
			List<CrmSmsSendDTO> crmsendlist = jdbcQueryManager.getList(sql, CrmSmsSendDTO.class);
			
			if(crmsendlist!=null){
				for (int i = 0; i < crmsendlist.size(); i++) {
					
					CustomerInfoDTO ci =  getcustomerinfo(crmsendlist.get(i).getArch_num(),logname);
					
					String Sms_note = crmsendlist.get(i).getSms_note().replace("{name}", ci.getUser_name());
					// String  sms_tiem="";
					// int sms_type = (int) jsonObject.get("sms_type");//预约发送时间
					 String sms_type = crmsendlist.get(i).getSms_type();
		            if(sms_type!="1"){
		            	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		                 
		                 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		                  sms_tiem = formatter.format(sdf.parse(crmsendlist.get(i).getSms_time()));
		            }
					try {
						
						ZGCrmSmsBean crmSmsBean = new ZGCrmSmsBean();
						crmSmsBean.setLoginName("");//从短信发送服务 xml配置获取
						crmSmsBean.setPwd("");
						crmSmsBean.setFeeType("2");
						crmSmsBean.setMobile(crmsendlist.get(i).getSms_phone());
						crmSmsBean.setContent(Sms_note);
						crmSmsBean.setSignName("");
						crmSmsBean.setTimingDate("");
						crmSmsBean.setExtCode("");
						String json = new Gson().toJson(crmSmsBean, ZGCrmSmsBean.class);
						
						String doPost_Str = HttpUtil.doPost_Str(url, json, "utf-8");
						
						System.err.println(doPost_Str);
						boolean contains = doPost_Str.contains("ok");
						if(contains){
							String crmsql = " update crm_sms_send set sms_status=1 where id='"+crmsendlist.get(i).getId()+"'  ";
							TranLogTxt.liswriteEror_to_txt(logname, "res: update crm_sms_send=====" + crmsql);
							int executeUpdate = jdbcQueryManager.getConnection().createStatement().executeUpdate(crmsql);
							
							if(executeUpdate>0){
								msg.setRescode("AA");
								msg.setRestext("短信发送成功"+crmsendlist.get(i).getSms_phone()+"=="+crmsendlist.get(i).getArch_num());
							}
							
						}
					} catch (Exception e) {
						msg.setRescode("AE");
						msg.setRestext("短信发送失败"+crmsendlist.get(i).getSms_phone()+"=="+crmsendlist.get(i).getArch_num());
						e.printStackTrace();
					}
				}
			}else{
				msg.setRescode("AE");
				msg.setRestext("没有需要发送的短信");
			}
			
		}catch(Exception ex){
			msg.setRescode("AE");
			msg.setRescode("短信发送失败");
			ex.printStackTrace();	
		}*/
		
		
		//立即发送 也是 进入短信池  此处只做提示  实际发送短信业务 还是job定时任务 每隔几分钟  读取短信池中数据
		msg.setRescode("AA");
		return msg;
	}
	private CustomerInfoDTO getcustomerinfo(String arch_num,String logname) {
		
			CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO();
			String sql = " select * from customer_info where is_Active='Y' and arch_num='"+arch_num+"'  ";
			TranLogTxt.liswriteEror_to_txt(logname, "res: select customer_info=====" + sql);
			List<CustomerInfoDTO> list = jdbcQueryManager.getList(sql, CustomerInfoDTO.class);
			
			if(list!=null){
				customerInfoDTO = list.get(0);
			}
		
		
		return customerInfoDTO;
		

}
	
}
