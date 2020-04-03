package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.CUSTOMERModifyPSWMessageTj180;
import com.hjw.webService.client.tj180.Bean.CustModifyPSWBody;
import com.hjw.webService.client.tj180.Bean.CustModifyPSWResBody;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

//初始化人员密码
public class CUSTOMERModifyPSWMessage {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public CustModifyPSWResBody customSend(CustModifyPSWBody custom) {
		String logname="reqCustModifyPSW";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		CustModifyPSWResBody rb = new CustModifyPSWResBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("CUSTOMACC_MODIFY_PSW");
		String web_url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method().trim();//从第三方获取人员信息 1、tj180
		System.out.println("userType--0--"+userType);
		
		if ("1".equals(userType)) {//tj180
			CUSTOMERModifyPSWMessageTj180 cs= new CUSTOMERModifyPSWMessageTj180();
			rb=cs.getMessage(web_url,custom, logname);
		}else{
			rb.setStatus("AE");
			rb.setErrorinfo("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
