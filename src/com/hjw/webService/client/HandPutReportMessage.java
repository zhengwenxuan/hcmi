package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.TCI.PutReportControlZ2;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class HandPutReportMessage {
	
	/**
	 * 体检报告上传
	 * 
	 * @return
	 */
	public ResultHeader handGetMessage(String exam_num,long userid) {
		ResultHeader rb = new ResultHeader();
		String logName = "handPutReport";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("HAND_REPORT_SEND");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		System.out.println("userType--0--" + userType);
		if ("1".equals(userType)) {// 浙二院
			PutReportControlZ2 lis = new PutReportControlZ2();
			rb = lis.getMessage(web_url, exam_num, userid, logName);
		} else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
