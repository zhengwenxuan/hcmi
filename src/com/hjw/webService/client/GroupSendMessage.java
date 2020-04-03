package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.GroupUpdateSendMessageTJ180;
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
public class GroupSendMessage {

	private long groupOrBatch_id;

	public GroupSendMessage(long groupOrBatch_id){
		this.groupOrBatch_id=groupOrBatch_id;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader lisSend() {
		String logName="reqGroup";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("GROUP_SEND");
		String url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method();	
		ResultHeader rb= new ResultHeader();
		if ("6".equals(userType)) {//180医院
			System.out.println("userType--1--"+userType);
			GroupUpdateSendMessageTJ180 lis= new GroupUpdateSendMessageTJ180();
			rb = lis.getMessage(url,this.groupOrBatch_id,logName);
		}else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	/**
	 * 
	 * @Title: 按照批次发送 @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader sendByBatch() {
		String logName="reqBatchForGroup";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("GROUP_SEND");
		String url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method();	
		ResultHeader rb= new ResultHeader();
		if ("6".equals(userType)) {//180医院
			System.out.println("userType--1--"+userType);
			GroupUpdateSendMessageTJ180 lis= new GroupUpdateSendMessageTJ180();
			rb = lis.getMessageByBatch(url,this.groupOrBatch_id,logName);
		}else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
