package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tiantan.insertHRPDataMessageTT;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoInsertHRPMessage {
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoInsertHRP";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("AUTO_INSERT_HRP");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		int lday=Integer.valueOf(wcd.getConfig_value());
		System.out.println("userType--0--" + userType);
		if ("10".equals(userType)) {//北京天坛医院 HRP
			insertHRPDataMessageTT lis= new insertHRPDataMessageTT();
			rb=lis.getMessage(web_url, lday, logName);
		}  else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
