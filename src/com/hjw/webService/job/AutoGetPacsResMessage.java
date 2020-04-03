package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.qufu.job.GetPacsResControlQF;
import com.hjw.webService.client.tj180.job.GetPacsResControlTJ180;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoGetPacsResMessage {
	
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoGetPacsRes";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");

		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_LIS_RES = configService.getCenterconfigByKey("AUTO_PACS_RES").getConfig_value().trim();// 是否自动获取PACS
		String AUTO_LIS_RES_DAY = configService.getCenterconfigByKey("AUTO_PACS_RES_DAY").getConfig_value().trim();// 自动获取PACS结果天数，从检查之日算起
        int lday=Integer.valueOf(AUTO_LIS_RES_DAY);
		if ("Y".equals(AUTO_LIS_RES)) {
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("PACS_READ");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("6".equals(userType)) {//对应tj180
				GetPacsResControlTJ180 lis = new GetPacsResControlTJ180();
				rb=lis.getMessage(web_url, lday,false,logName);
			} else if ("11".equals(userType)) {//曲阜
				GetPacsResControlQF lis = new GetPacsResControlQF();
				rb=lis.getMessage(web_url, lday);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
	
	/**
	 * 病理入库
	 * @return
	 */
	public ResultHeader getMessageBL() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoGetPacsBLRes";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");

		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_LIS_RES = configService.getCenterconfigByKey("AUTO_PACS_RES").getConfig_value().trim();// 是否自动获取PACS
		String AUTO_LISBL_RES_DAY = configService.getCenterconfigByKey("AUTO_PACSBL_RES_DAY").getConfig_value().trim();// 自动获取PACS结果天数，从检查之日算起
        int lday=Integer.valueOf(AUTO_LISBL_RES_DAY);
		if ("Y".equals(AUTO_LIS_RES)) {
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("PACS_READ");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("6".equals(userType)) {//对应tj180
				GetPacsResControlTJ180 lis = new GetPacsResControlTJ180();
				rb=lis.getMessageBL(web_url, lday,false,logName);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
	
	/**
	 *彩超入库
	 * @return
	 */
	public ResultHeader getMessageUS() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoGetPacsUSRes";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");

		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_LIS_RES = configService.getCenterconfigByKey("AUTO_PACS_RES").getConfig_value().trim();// 是否自动获取PACS
		String AUTO_LISBL_RES_DAY = configService.getCenterconfigByKey("AUTO_PACSBL_RES_DAY").getConfig_value().trim();// 自动获取PACS 彩超结果天数，从检查之日算起
        int lday=Integer.valueOf(AUTO_LISBL_RES_DAY);
		if ("Y".equals(AUTO_LIS_RES)) {
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("PACS_READ");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("6".equals(userType)) {//对应tj180
				GetPacsResControlTJ180 lis = new GetPacsResControlTJ180();
				rb=lis.getMessageUS(web_url, lday,false,logName);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
	
	/**
	 * 心电入库
	 * @return
	 */
	public ResultHeader getMessageXD() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoGetPacsXDRes";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
		+ "---------------------------------");
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_PACS_RES = configService.getCenterconfigByKey("AUTO_PACS_RES").getConfig_value().trim();// 是否自动获取PACS
		String AUTO_PACSXD_RES_DAY = configService.getCenterconfigByKey("AUTO_PACSXD_RES_DAY").getConfig_value().trim();// 自动获取心电结果天数，从检查之日算起
		int lday=Integer.valueOf(AUTO_PACSXD_RES_DAY);
		if ("Y".equals(AUTO_PACS_RES)) {
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("XD_READ");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("11".equals(userType)) {//曲阜
				GetPacsResControlQF lis = new GetPacsResControlQF();
				rb=lis.getMessageXD(web_url, lday);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
}
