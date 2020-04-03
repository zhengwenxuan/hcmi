package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.HealthFileResBody;
import com.hjw.webService.client.tj180.HealthResMessageTj180;
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
public class HealthResMessage{

	/**
	 * 
	 * @param exam_num 体检编号
	 * @param userType 0 表示体检系统有健康管理报告时候不去健康管理系统再获取
	 *                 1表示不管体检系统有还是没有健康管理报告，都从健康管理系统去得
	 * @return
	 */
	public HealthFileResBody getHealThMessage(String exam_num,int userType) {
		String logName="resHealth";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("HEALTH_READ");
				
		HealthFileResBody rb=new HealthFileResBody();
		if ("6".equals(wcd.getConfig_method())) {//tj180
			HealthResMessageTj180 lis= new HealthResMessageTj180();
			rb = lis.getMessage(wcd.getConfig_url(), exam_num, userType, logName);
		}else {
			rb.setRescode("AE");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
