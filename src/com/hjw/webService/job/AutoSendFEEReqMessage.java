package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.SendFeeReqMessageHK;
import com.hjw.webService.client.hokai305.SendFeeReqMessageHK305;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
/**
 *  
 *  自动发送FEE申请
 * @author yangm
 *
 */

public class AutoSendFEEReqMessage {
	
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoReqFee";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("AUTO_FEE_SEND");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		int lday=Integer.valueOf(wcd.getConfig_value());
		System.out.println("userType--0--" + userType);
		if ("21".equals(userType)) {//和佳-常德二院
			SendFeeReqMessageHK lis= new SendFeeReqMessageHK();
			rb=lis.getMessage(web_url, lday, logName);
		} else if ("21.1".equals(userType)) {//和佳-305
			SendFeeReqMessageHK305 lis= new SendFeeReqMessageHK305();
			rb=lis.getMessage(web_url, lday, logName);
		} else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
