package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.nanfeng.PutReportControlNF;
import com.hjw.webService.client.tj180.PutReportControlTJ180;
import com.hjw.webService.job.fangzheng.PutReportControlTJFZ;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
/**
 *  
 *  发送报告
 * @author yangm
 *
 */

public class AutoPutReportMessage {
	/**
	 * 
	 * @Title: lisSend @Desc	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoPutReport";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
					.getBean("webserviceConfigurationService");
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("REPORT_SEND");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			int lday=Integer.valueOf(wcd.getConfig_value());
			System.out.println("userType--0--" + userType);
			if ("6".equals(userType)) {// 对应tj180
				PutReportControlTJ180 lis = new PutReportControlTJ180();
				rb = lis.getMessage(web_url, lday, logName);
			} else if ("8".equals(userType)) {//方正
				PutReportControlTJFZ lis= new PutReportControlTJFZ();
				rb=lis.getMessage(web_url, lday, logName);
			} else if ("28".equals(userType)) {//南丰
				PutReportControlNF lis= new PutReportControlNF();
				rb=lis.getMessage(web_url, lday, logName);
			} else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		return rb;
	}
}
