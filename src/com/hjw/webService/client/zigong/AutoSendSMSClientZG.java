package com.hjw.webService.client.zigong;
import java.net.URLEncoder;
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
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.wst.DTO.CrmSmsBaseTemplateDTO;
import com.hjw.wst.DTO.CrmSmsSendDTO;
import com.hjw.wst.DTO.CustomerInfoDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.synjones.framework.persistence.JdbcQueryManager;


public class AutoSendSMSClientZG {
	
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
	}
	

	public MSGResBody getSMScustome(String url, int days, String logname){
		MSGResBody 	msg = new MSGResBody(); 
		String crmsql = "  select  * from crm_sms_send where sms_status=0 and sms_date<= '"+DateUtil.getDateTime()+"'";
		TranLogTxt.liswriteEror_to_txt(logname, "res: select crm_sms_send=====" + crmsql);
		List<CrmSmsSendDTO> crmsendlist = jdbcQueryManager.getList(crmsql, CrmSmsSendDTO.class);
		
		if(crmsendlist !=null && crmsendlist.size()>0){
		for (int i = 0; i < crmsendlist.size(); i++) {
			
			CustomerInfoDTO ci = customerInfoService.getCustomerinfoByArchNum(crmsendlist.get(i).getArch_num());
			CrmSmsBaseTemplateDTO crmSmsSend = getCrmSmsSend(crmsendlist.get(i).getTemplate_id(),logname);
			
			
			String Sms_note = crmSmsSend.getSms_note().replace("{name}", ci.getUser_name());
			System.err.println("短信池内短信内容=="+crmSmsSend.getSms_note());
			System.err.println("需要替换的名称=="+ci.getUser_name());
			System.err.println("替换名称后短信内容=="+Sms_note);
			
			try {
				//String Contentencode = URLEncoder.encode(Sms_note, "UTF-8");
				
				
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
				
				System.err.println("短信平台返回"+doPost_Str);
				boolean contains = doPost_Str.contains("OK");
				System.err.println("是否发送到短信平台  审核通过  或者 发送成功"+contains);
				if(contains){
					String sql = " update crm_sms_send set sms_status=1 where id='"+crmsendlist.get(i).getId()+"'  ";
					TranLogTxt.liswriteEror_to_txt(logname, "res: update crm_sms_send=====" + sql);
					int executeUpdate = jdbcQueryManager.getConnection().createStatement().executeUpdate(sql);
					
					if(executeUpdate>0){
						msg.setRescode("AA");
						msg.setRestext("短信发送成功"+crmsendlist.get(i).getSms_phone()+"=="+crmsendlist.get(i).getArch_num());
					}
					
				}else{
					msg.setRescode("AE");
					msg.setRestext("短信发送失败");
				}
			} catch (Exception e) {
				msg.setRescode("AE");
				msg.setRestext("短信发送失败"+crmsendlist.get(i).getSms_phone()+"=="+crmsendlist.get(i).getArch_num());
				e.printStackTrace();
			}
		}
	}else{
		msg.setRescode("AE");
		msg.setRestext("不存在需要发送的短信");
	}
		
		return msg;
	}


	private  CrmSmsBaseTemplateDTO getCrmSmsSend(String template_id, String logname) {
		String sql = " select * from crm_sms_base_template where id='"+template_id+"' ";
		CrmSmsBaseTemplateDTO templateDTO = new CrmSmsBaseTemplateDTO();
		TranLogTxt.liswriteEror_to_txt(logname, "res: select crm_sms_base_template=====" + sql);
		List<CrmSmsBaseTemplateDTO> list = jdbcQueryManager.getList(sql, CrmSmsBaseTemplateDTO.class);
		
		if(list!=null){
			templateDTO= list.get(0);
		}
		
		return templateDTO;
	}
	
		
}
