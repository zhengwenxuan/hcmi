package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ArmyPatInfoBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.haoyitong.WenJuanGetMessageHYT;
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
public class WenJuanGetMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader wenjuanGet(long examinfo_id) {
		String logName="wenjuanGet";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("WENJUAN_GET");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();
		System.out.println("userType--0--"+dahtype);
		ResultHeader rh = new ResultHeader();
		if("22".equals(dahtype)){//好医通
			WenJuanGetMessageHYT dahmess= new WenJuanGetMessageHYT();
			rh = dahmess.getMessage(web_url,examinfo_id,logName);
		}else{
			rh.setTypeCode("AE");
			rh.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rh;
	}

}
