package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.Bean.QueueInsertBean;
import com.hjw.webService.client.Bean.QueueSearchBean;
import com.hjw.webService.client.Bean.QueueUpdateBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.body.QueueResInsertBody;
import com.hjw.webService.client.body.QueueResSearchBody;
import com.hjw.webService.client.body.QueueResUpdateBody;
import com.hjw.webService.client.haijie.queue.QueueAddSendMessageHJ;
import com.hjw.webService.client.tj180.QueueAddSendMessageTJ180;
import com.hjw.webService.client.tj180.QueueInsertMessageTJ180;
import com.hjw.webService.client.tj180.QueueSearchListMessageTJ180;
import com.hjw.webService.client.tj180.QueueUpdateMessageTJ180;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 排队叫号
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class QueueUpdateListMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResUpdateBody Send(QueueUpdateBean eu) {
		String logName="queueUpdateList";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("QUEUEUPDATE_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//档案号获取接口类型1、浙二院；2湖州浙北明州医院
		System.out.println("userType--0--"+dahtype);
		QueueResUpdateBody rb = new QueueResUpdateBody();
		if ("2".equals(dahtype)) {//180排队
			QueueUpdateMessageTJ180 lis= new QueueUpdateMessageTJ180();
			rb = lis.getMessage(web_url,eu,logName);
		}else{
			rb.setRescode("AE");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
