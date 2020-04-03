package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.erfuyuan.QueueAddSendMessageEFY;
import com.hjw.webService.client.haijie.queue.QueueAddSendMessageHJ;
import com.hjw.webService.client.haijie.queue.QueueAddSendMessageHJNew;
import com.hjw.webService.client.haijie.queue.QueueAddSendMessageHJNew2;
import com.hjw.webService.client.hokai305.QueueAddSendMessageHK305;

import com.hjw.webService.client.tiaoding.QueueAddSendMessageTD;

import com.hjw.webService.client.tj180.QueueAddSendMessageTJ180;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 3.	将客户加入排队
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class QueueAddToSendMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResBody Send(QueueAddBean eu) {
		String logName="QueueAdd";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("QUEUEADD_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//档案号获取接口类型1、浙二院；2湖州浙北明州医院
		System.out.println("userType-0-"+dahtype);
		TranLogTxt.liswriteEror_to_txt(logName, "========dahtype=========="+dahtype+"=====");
		QueueResBody rb = new QueueResBody();
		if ("1".equals(dahtype)) {//海捷排队
			String tokenurl = webserviceConfigurationService.getWebServiceConfig("QUEUETOKEN_SEND").getConfig_url().trim();
			QueueAddSendMessageHJ lis= new QueueAddSendMessageHJ();
			rb = lis.getMessage(web_url,tokenurl,eu,logName);
		}else if ("1.1".equals(dahtype)) {//海捷排队-升级版
			String tokenurl = webserviceConfigurationService.getWebServiceConfig("QUEUETOKEN_SEND").getConfig_url().trim();
			QueueAddSendMessageHJNew lis= new QueueAddSendMessageHJNew();
			rb = lis.getMessage(web_url,tokenurl,eu,logName);
		}else if ("1.2".equals(dahtype)) {//海捷排队-调用2个路径-黑龙江
			String tokenurl = webserviceConfigurationService.getWebServiceConfig("QUEUETOKEN_SEND").getConfig_url().trim();
			QueueAddSendMessageHJNew2 lis= new QueueAddSendMessageHJNew2();
			rb = lis.getMessage(web_url.split("\\^")[0],tokenurl.split("\\^")[0],eu,logName);
			rb = lis.getMessage(web_url.split("\\^")[1],tokenurl.split("\\^")[1],eu,logName);
		}else if ("2".equals(dahtype)) {//180排队
			QueueAddSendMessageTJ180 lis= new QueueAddSendMessageTJ180();
			rb = lis.getMessage(web_url,eu,logName);
		}else if ("13".equals(dahtype.trim())) {//二附院排队
			QueueAddSendMessageEFY lis= new QueueAddSendMessageEFY();
			rb = lis.getMessage(web_url,eu,logName);

		}else if("21.1".equals(dahtype.trim())){
			QueueAddSendMessageHK305 lis= new QueueAddSendMessageHK305();
			rb = lis.getMessage(web_url,eu,logName);

		}else if ("25".equals(dahtype.trim())) {//调鼎排队-新版（长治二院使用）
			QueueAddSendMessageTD lis= new QueueAddSendMessageTD();
			rb = lis.getMessage(web_url,eu,logName);

		}else{
			rb.setRescode("AE");
			rb.setIdnumber("");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
