package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.yichang.SendPacsReqMessageYC;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
/**
 *  
 *  自动发送PACS申请
 */

public class AutoSendPacsReqMessage {
	
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoReqPacs";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("AUTO_PACS_SEND");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		int lday=Integer.valueOf(wcd.getConfig_value());
		System.out.println("userType--0--" + userType);
		if ("14".equals(userType)) {//宜昌
			SendPacsReqMessageYC lis= new SendPacsReqMessageYC();
			rb=lis.getMessage(web_url, lday, logName);
		}else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
