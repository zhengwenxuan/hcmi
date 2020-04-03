package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.CustAccBody;
import com.hjw.webService.client.Bean.CustAccResBody;
import com.hjw.webService.client.hokai305.CustomAccMessageHK305;
import com.hjw.webService.client.tj180.CustomAccMessageTj180;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 从第三方获取人员身份信息
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CUSTOMACCMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public CustAccResBody customSend(CustAccBody custom) {
		String logname="reqCustAcc";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		CustAccResBody rb = new CustAccResBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("CUSTOMACC_SEND");
		String web_url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method().trim();//从第三方获取人员信息 1、tj180
		System.out.println("userType--0--"+userType);
		
		if ("1".equals(userType)) {//tj180
			CustomAccMessageTj180 cs= new CustomAccMessageTj180();
			rb=cs.getMessage(web_url,custom, logname);
		}else if ("21.1".equals(userType)){
			CustomAccMessageHK305 cs= new CustomAccMessageHK305();
			rb=cs.getMessage(web_url,custom, logname);
		}else{
			rb.setStatus("AE");
			rb.setErrorInfo("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
}
