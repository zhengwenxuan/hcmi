package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.PacsResStatusMessageHK;
import com.hjw.webService.client.huojianwa.FEETermResMessageHjw;
import com.hjw.webService.client.zhonglian.FEEResMessageZL;
import com.hjw.webService.client.zhonglian.FEETermResMessageZL;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * his获取缴费结果
 * 
 * @author yangm
 *
 */
public class FEETermResMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage(String acc_num) {
		ResultHeader rb = new ResultHeader();
		String logName = "feeres";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("HIS_GET");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		System.out.println("userType--0--" + userType);
		if ("2".equals(userType)) {// 中联
			FEETermResMessageZL lis = new FEETermResMessageZL();
			rb = lis.getMessage(web_url, acc_num, logName);
		} else if ("9".equals(userType)) {// 火箭蛙
			FEETermResMessageHjw lis = new FEETermResMessageHjw();
			rb = lis.getMessage(web_url, acc_num, logName);
		} else{
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
