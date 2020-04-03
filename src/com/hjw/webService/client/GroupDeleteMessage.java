package com.hjw.webService.client;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.webService.client.body.ExamDeleteMessageBody;
import com.hjw.webService.client.body.ExamTDeleteMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.ExamDelSendMessageTJ180;
import com.hjw.webService.client.tj180.ExamTDelSendMessageTJ180;
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
public class GroupDeleteMessage {
	private ExamTDeleteMessageBody lismessage;	
    private final String mesType="EXAMT_DELETE";
	public GroupDeleteMessage(ExamTDeleteMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader send() {
		String logname = mesType;
		ResultHeader rb = new ResultHeader();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();	
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		String url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method();	
		
		if ("1".equals(userType)) {//tj180
			ExamTDelSendMessageTJ180 pms = new ExamTDelSendMessageTJ180(lismessage);
			rb = pms.getMessage(url, logname);
		}else{
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	}
}
