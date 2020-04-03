package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ArmyPatInfoBody;
import com.hjw.webService.client.tj180.ArmyGetMessageTj180;
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
public class ArmyGetMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ArmyPatInfoBody armySend(long batchid) {
		String logName="armyexaminfo";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("ARMY_EXAMINFO_GET");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//档案号获取接口类型1、浙二院；2湖州浙北明州医院
		System.out.println("userType--0--"+dahtype);
		ArmyPatInfoBody rb = new ArmyPatInfoBody();
		if("6".equals(dahtype)){//180医院
			ArmyGetMessageTj180 dahmess= new ArmyGetMessageTj180();
			rb = dahmess.getMessage(web_url,batchid,logName);
		}else{
			rb.setStatus("AE");
			rb.setErrorInfo("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
