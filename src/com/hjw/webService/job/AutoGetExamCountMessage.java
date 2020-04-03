package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hghis.ExamCountMessage;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 统计每天体检人数
 * @author dq
 *
 */
public class AutoGetExamCountMessage {
	
	public void getMessage(){
		String logName = "autoExamCount";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");

		WebserviceConfigurationDTO wcd = webserviceConfigurationService.getWebServiceConfig("HIS_EXAM_COUNT");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		System.out.println("userType--0--" + userType);
		if ("1".equals(userType)) {// 黄冈统计体检人数
			ExamCountMessage examCount = new ExamCountMessage();
			examCount.getMessage(web_url, logName);
		}
	}
}
