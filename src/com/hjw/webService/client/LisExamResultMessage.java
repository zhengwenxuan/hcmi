package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.LisExamResultMessageTj180;
import com.hjw.webService.client.tj180.Bean.LisExamBody;
import com.hjw.webService.client.tj180.Bean.LisExamResBody;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class LisExamResultMessage {
 
	public LisExamResBody resultSend(LisExamBody body){
		String logName= "lisExamResult";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("LIS_EXAMRESULT_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//查看检查结果  1 体检180
		
		LisExamResBody rb = new LisExamResBody();
		if ("1".equals(dahtype)) {//体检180
			LisExamResultMessageTj180 mes = new LisExamResultMessageTj180();
			rb = mes.getMessage(web_url, body, logName);
		}else{
			rb.setStatus("AE");
			rb.setErrorInfo("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
