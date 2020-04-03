package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.longkou.job.ExamSendReportReceiveMessageLK;
import com.hjw.webService.client.tj180.job.ExamSendReportReceiveMessage180;
import com.hjw.webService.client.yichang.AutoSendExamSummaryYC;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 统计每天体检人数
 * @author dq
 *
 */
public class AutoSendReportMessage {
	
	public void getMessage(){
		String logName = "autoSendReportReceive";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = webserviceConfigurationService.getWebServiceConfig("SEND_REPORT_RECEIVE");
		TranLogTxt.liswriteEror_to_txt(logName, "=============");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		String days = wcd.getConfig_value();//同步前几天的数据
		TranLogTxt.liswriteEror_to_txt(logName, "-------"+web_url+"===="+days+"======"+userType);
		int lday=Integer.valueOf(days);
		System.out.println("userType--0--" + userType);
		if ("6".equals(userType)) {// 180同步体检报告发放数据
			ExamSendReportReceiveMessage180 examCount = new ExamSendReportReceiveMessage180();
			examCount.getMessage(web_url,lday, logName);
		}else if ("7".equals(userType)) {//龙口市人民医院
			ExamSendReportReceiveMessageLK examCount = new ExamSendReportReceiveMessageLK();
			examCount.getMessage(web_url,lday, logName);
		}else if ("14".equals(userType)) {//宜昌
			AutoSendExamSummaryYC examCount = new AutoSendExamSummaryYC();
			examCount.getMessage(web_url,lday, logName);
		}
	}
}
