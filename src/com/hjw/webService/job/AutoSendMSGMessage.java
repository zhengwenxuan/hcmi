package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.hghis.AutoSendSMSClient;
import com.hjw.webService.client.tj180.job.PostMsgControlTJ180;
import com.hjw.webService.client.zigong.AutoSendSMSClientZG;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoSendMSGMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public MSGResBody getMessage() {
		String logName="automsgSend";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("MSGMESSAGE_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();
		System.out.println("userType--0--"+dahtype);
		int lday = Integer.valueOf(wcd.getConfig_value());
		MSGResBody rb = new MSGResBody();
		if ("6".equals(dahtype)) {//180短信
			PostMsgControlTJ180 lis= new PostMsgControlTJ180();
			rb = lis.getMessage(web_url,lday,logName);
		}else if("5".equals(dahtype)){//黄冈短信
			AutoSendSMSClient lis = new AutoSendSMSClient();
			rb = lis.getSMScustome(web_url,lday,logName);
					
		}else if("31".equals(dahtype)){//自贡短信
			AutoSendSMSClientZG lis = new AutoSendSMSClientZG();
			rb = lis.getSMScustome(web_url,lday,logName);
					
		}else{
			rb.setRescode("AE");
			rb.setIdnumber("");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
