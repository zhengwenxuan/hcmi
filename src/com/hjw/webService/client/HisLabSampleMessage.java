package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tj180.HisLabSampleMessageTj180;
import com.hjw.webService.client.tj180.Bean.HisLabSampleBody;
import com.hjw.webService.client.tj180.Bean.HisLabSampleResBody;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class HisLabSampleMessage {

	public HisLabSampleResBody sampleSend(HisLabSampleBody body){//查看采样采样信息
		String logName= "hisLabSample";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("HIS_SAMPLE_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//查看检查结果  1 体检180
		
		HisLabSampleResBody rb = new HisLabSampleResBody();
		if ("1".equals(dahtype)) {//体检180
			HisLabSampleMessageTj180 mes = new HisLabSampleMessageTj180();
			rb = mes.getMessage(web_url, body, logName);
		}else{
			rb.setStatus("AE");
			rb.setErrorinfo("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	public ResultLisBody lisResultSend(String testno){
		String logName= "hisLabSampleResult";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("LIS_READ");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//查看检查结果  1 体检180
		
		ResultLisBody rb = new ResultLisBody();
		ResultHeader rh = new ResultHeader();
		if ("6".equals(dahtype)) {//体检180
			HisLabSampleMessageTj180 mes = new HisLabSampleMessageTj180();
			rb = mes.getLisMessage(web_url, testno, logName);
		}else{
			rh.setTypeCode("AE");
			rh.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
