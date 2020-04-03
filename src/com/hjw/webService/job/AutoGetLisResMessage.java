package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.qufu.job.GetLisResControlQF;
import com.hjw.webService.client.tj180.job.GetLisResControlTJ180;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoGetLisResMessage {
	
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoGetLisRes";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_LIS_RES = configService.getCenterconfigByKey("AUTO_LIS_RES").getConfig_value().trim();// 是否自动获取lis
		String AUTO_LIS_RES_DAY = configService.getCenterconfigByKey("AUTO_LIS_RES_DAY").getConfig_value().trim();// 自动获取lis结果天数，从检查之日算起
		int lday = Integer.valueOf(AUTO_LIS_RES_DAY);
		if ("Y".equals(AUTO_LIS_RES)) {
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = webserviceConfigurationService.getWebServiceConfig("LIS_READ");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("6".equals(userType)) {// 对应tj180
				GetLisResControlTJ180 lis = new GetLisResControlTJ180();
				rb = lis.getMessage(web_url, lday, logName);
			} else if ("11".equals(userType)) {//曲阜
				GetLisResControlQF lis = new GetLisResControlQF();
				rb = lis.getMessage(web_url, lday);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
	
	/**
	 * 医院专用，批量处理以前的lis结果
	 * @return
	 */
	public ResultHeader getMessageOther() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoGetLisOtherRes";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_LIS_RES = configService.getCenterconfigByKey("AUTO_LIS_RES").getConfig_value().trim();// 是否自动获取lis
		String AUTO_LIS_RES_DAY = configService.getCenterconfigByKey("AUTO_LIS_RES_DAY").getConfig_value().trim();// 自动获取lis结果天数，从检查之日算起
		int lday = Integer.valueOf(AUTO_LIS_RES_DAY);
		if ("Y".equals(AUTO_LIS_RES)) {
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("LIS_READ");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("6".equals(userType)) {// 对应tj180
				GetLisResControlTJ180 lis = new GetLisResControlTJ180();
				rb = lis.getMessage_other(web_url, lday, logName);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
}
