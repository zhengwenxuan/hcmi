package com.hjw.webService.client;

import com.hjw.webService.client.shanxijiaotongyiyuan.SSJTYYSendMessage;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.MSGSendBean;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.changan.MSGSureTempalteSend;
import com.hjw.webService.client.hghis.AutoSendSMSClient;
import com.hjw.webService.client.tj180.MSGSendMessageTJ180;
import com.hjw.webService.client.zigong.ZiGongSendMessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 发送短信接口
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class MSGSendMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public MSGResBody Send(MSGSendBean eu) {
		String logName="msgSend";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("MSGMESSAGE_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();
		System.out.println("userType--0--"+dahtype);
		MSGResBody rb = new MSGResBody();
		if ("6".equals(dahtype)) {//180短信
			MSGSendMessageTJ180 lis= new MSGSendMessageTJ180();
			rb = lis.getMessage(web_url,eu,logName);
		}else if("7".equals(dahtype)) {//长安医院短信指定模板发送
			MSGSureTempalteSend lis= new MSGSureTempalteSend();
			rb = lis.getMessage(wcd,eu,logName);
		}else if("8".equals(dahtype)){//陕西省交通医院
			SSJTYYSendMessage lis = new SSJTYYSendMessage();
			rb = lis.getMessage(web_url,eu,logName);
		}else if("31".equals(dahtype)){//自贡市人民医院
			ZiGongSendMessage lis = new ZiGongSendMessage();
			rb = lis.getMessage(web_url,eu,logName);
		}else{
			rb.setRescode("AE");
			rb.setIdnumber("");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
}
