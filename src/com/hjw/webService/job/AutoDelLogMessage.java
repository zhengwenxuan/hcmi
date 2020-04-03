package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;

/**
 *  自动删除日志文件
 */
public class AutoDelLogMessage {
	
	private String logName = "autoDelLog";
	
	public ResultHeader getMessage() {
		ResultHeader rh = new ResultHeader();
		try {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			ConfigService configService = (ConfigService) wac.getBean("configService");
			String IS_LOG_DEL_DAYS = configService.getCenterconfigByKey("IS_LOG_DEL_DAYS")
					.getConfig_value().trim();
			TranLogTxt.lisdelEror_to_txt(Integer.valueOf(IS_LOG_DEL_DAYS));
		}catch(Exception ex){
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logName,"错误提示："+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rh;
	}
}
