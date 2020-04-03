package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.changan.GetHisResControlCA;
import com.hjw.webService.client.liubaxian.FEEResMessageLBX;
import com.hjw.webService.client.liubaxian.GetHisResControlLBX;
import com.hjw.webService.job.fangzheng.GetHisResControlFZ;
import com.hjw.webService.job.huojianwa.GetHisResControlHjw;
import com.hjw.webService.job.zhonglian.GetHisResControlZL;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoGetHisResMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autohisres";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_LIS_RES = configService.getCenterconfigByKey("AUTO_HIS_RES").getConfig_value().trim();// 是否自动获取his
		if ("Y".equals(AUTO_LIS_RES)) {
			String AUTO_LIS_RES_DAY = configService.getCenterconfigByKey("AUTO_HIS_RES_DAY").getConfig_value().trim();// 自动获取lis结果天数，从检查之日算起
			int lday = Integer.valueOf(AUTO_LIS_RES_DAY);
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("HIS_GET");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			if ("1.1".equals(userType)) {//长安医院 天健
				GetHisResControlCA lis = new GetHisResControlCA();
				rb = lis.getMessage(web_url,lday, logName);
			} else if ("2".equals(userType)) {// 中联
				GetHisResControlZL lis = new GetHisResControlZL();
				rb = lis.getMessage(web_url,lday, logName);
			} else if ("8".equals(userType)) {// 中联
				GetHisResControlFZ lis = new GetHisResControlFZ();
				rb = lis.getMessage(web_url,lday, logName);
			}else if ("9".equals(userType)) {// 火箭蛙
				GetHisResControlHjw lis = new GetHisResControlHjw();
				rb = lis.getMessage(web_url,lday, logName);
			}else if ("31".equals(userType)) {// 火箭蛙
				GetHisResControlLBX lis = new GetHisResControlLBX();
				rb = lis.getMessage(web_url,lday, logName);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
}
