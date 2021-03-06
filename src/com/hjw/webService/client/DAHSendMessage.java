package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.DAHCustomerBean;
import com.hjw.webService.client.body.DAHResBody;
import com.hjw.webService.client.dbgj.DAHSendMessageZJEY;
import com.hjw.webService.client.fangzheng.DAHSendMessageFZ;
import com.hjw.webService.client.hzzbmz.DAHSendMessageHZZBMZ;
import com.hjw.webService.client.tj180.DAHSendMessageTj180;
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
public class DAHSendMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public DAHResBody dahSend(DAHCustomerBean eu,String dah) {
		String logName="dahLis";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("DAH_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//档案号获取接口类型1、浙二院；2湖州浙北明州医院
		System.out.println("userType--0--"+dahtype);
		DAHResBody rb = new DAHResBody();
		if ("1".equals(dahtype)) {//浙二院
			DAHSendMessageZJEY lis= new DAHSendMessageZJEY();
			rb = lis.getMessage(web_url,eu,dah, logName);
		}else if ("2".equals(dahtype)) {//湖州浙北明州医院
			DAHSendMessageHZZBMZ lis= new DAHSendMessageHZZBMZ();
			rb = lis.getMessage(web_url,eu,logName);
		}else if ("3".equals(dahtype)) {//180医院获取病案号
			DAHSendMessageTj180 lis= new DAHSendMessageTj180();
			rb = lis.getMessage(web_url,eu,dah,logName);
		}else if("8".equals(dahtype)){//方正平台
			DAHSendMessageFZ dahmess= new DAHSendMessageFZ();
			rb = dahmess.getMessage(web_url,eu,dah,logName);
		}else{
			rb.setRescode("error");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
