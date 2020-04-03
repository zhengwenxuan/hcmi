package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueCustomerBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.empty.QueueCustSendMessageEmpty;
import com.hjw.webService.client.erfuyuan.QueueCustSendMessageEFY;
import com.hjw.webService.client.haijie.queue.QueueCustSendMessageHJ;
import com.hjw.webService.client.haijie.queue.QueueCustSendMessageHJNew;
import com.hjw.webService.client.haijie.queue.QueueCustSendMessageHJNew2;
import com.hjw.webService.client.hokai305.QueueCustSendMessageEFY305;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 预约排队借口
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class QueueCustomSendMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResBody Send(QueueCustomerBean eu) {
		String logName="QueueCustom";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("QUEUECUSOME_SEND");
		String web_url = wcd.getConfig_url().trim();
		String dahtype=wcd.getConfig_method().trim();//档案号获取接口类型1、浙二院；2湖州浙北明州医院
		System.out.println("userType--0--"+dahtype);
		QueueResBody rb = new QueueResBody();
		if ("0".equals(dahtype)) {//空实现，直接返回成功
			QueueCustSendMessageEmpty lis= new QueueCustSendMessageEmpty();
			rb = lis.getMessage(web_url,eu,logName);
		} else if ("1".equals(dahtype)) {//海捷排队
			String tokenurl = webserviceConfigurationService.getWebServiceConfig("QUEUETOKEN_SEND").getConfig_url().trim();
			System.out.println(tokenurl);
			QueueCustSendMessageHJ lis= new QueueCustSendMessageHJ();
			rb = lis.getMessage(web_url,tokenurl,eu,logName);
		} else if ("1.1".equals(dahtype)) {//海捷排队-升级版
			String tokenurl = webserviceConfigurationService.getWebServiceConfig("QUEUETOKEN_SEND").getConfig_url().trim();
			System.out.println(tokenurl);
			QueueCustSendMessageHJNew lis= new QueueCustSendMessageHJNew();
			rb = lis.getMessage(web_url,tokenurl,eu,logName);
		} else if ("1.2".equals(dahtype)) {//海捷排队-调用2个路径-黑龙江
			String tokenurl = webserviceConfigurationService.getWebServiceConfig("QUEUETOKEN_SEND").getConfig_url().trim();
			System.out.println(tokenurl);
			QueueCustSendMessageHJNew2 lis= new QueueCustSendMessageHJNew2();
			rb = lis.getMessage(web_url.split("\\^")[0],tokenurl.split("\\^")[0],eu,logName);
			rb = lis.getMessage(web_url.split("\\^")[1],tokenurl.split("\\^")[1],eu,logName);
		} else if ("13".equals(dahtype)) {//二附院排队
			QueueCustSendMessageEFY lis= new QueueCustSendMessageEFY();
			rb = lis.getMessage(web_url,eu,logName);
		}else if("21.1".equals(dahtype.trim())){//305排队叫号
			QueueCustSendMessageEFY305 lis= new QueueCustSendMessageEFY305();
			rb = lis.getMessage(web_url,eu,logName);

		}else{
			rb.setRescode("AE");
			rb.setIdnumber("");
			rb.setRestext("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
