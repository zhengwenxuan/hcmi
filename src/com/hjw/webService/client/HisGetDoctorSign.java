package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.GetHisDoctorSignTj180;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.domain.WebUserInfo;
import com.hjw.wst.service.WebserviceConfigurationService;

public class HisGetDoctorSign {

	public String getDoctorSign(WebUserInfo user){
		String resmessage="";
		String logName= "hisGetDoctorSign";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("GET_HIS_SIGN");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//查看检查结果  1 体检180
		
		if ("1".equals(dahtype)) {//体检180
			GetHisDoctorSignTj180 mes = new GetHisDoctorSignTj180();
			resmessage = mes.getMessage(web_url, user, logName);
		}else{
			resmessage = "接口无对应厂家,请检查webservice_configuration表config_method字段";
		}
		return resmessage;
	}
}
