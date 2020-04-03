package com.hjw.webService.client;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.acmeway.TSNResultsSendMessageJNYY;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class TSNResultsSendMessage {
    private final String mesType="TSN_SEND";
	public TSNResultsSendMessage(){
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String logname, String exam_num, String url, String userType, boolean debug) {
		ResultPacsBody rb = new ResultPacsBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		url = wcd.getConfig_url().trim();
		userType=wcd.getConfig_method();
		
		TranLogTxt.liswriteEror_to_txt(logname, "=== 传入参数 userType===:" + userType);
		
		if ("24".equals(userType)) {//24	奥美之路体适能-江南医院
			TSNResultsSendMessageJNYY pms = new TSNResultsSendMessageJNYY();
			rb = pms.getMessage(exam_num, url, logname, debug);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	}
	
}
