package com.hjw.webService.client;


import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.ExamPicSendMessageTJ180;
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
public class ExamPicMessage {
    
	public ExamPicMessage(){
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader send(String exam_num) {
		final String mesType="EXAM_PIC_GET";
		String logname = "exam_pic_get";
		ResultHeader rb = new ResultHeader();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		String url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method();	
		
		if ("1".equals(userType)) {//tj180
			ExamPicSendMessageTJ180 pms = new ExamPicSendMessageTJ180();
			rb = pms.getMessage(url,exam_num, logname);
		}else{
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	}
	
	public ResultHeader send_arch_num(String arch_num) {
		final String mesType="EXAM_PIC_GET";
		String logname = "exam_pic_get";
		ResultHeader rb = new ResultHeader();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		String url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method();	
		
		if ("1".equals(userType)) {//tj180
			ExamPicSendMessageTJ180 pms = new ExamPicSendMessageTJ180();
			rb = pms.getMessageArchNum(url,arch_num, logname);
		}else{
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	}
}
