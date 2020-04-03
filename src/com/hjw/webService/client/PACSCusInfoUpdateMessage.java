package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.webService.client.Carestream.PACSCusInfoUpdateMessageRK;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;


/**
 * PACS修改同步档案信息
 * @author Administrator
 *
 */
public class PACSCusInfoUpdateMessage {
	private final String mesType="PACS_CUSINFO_UP";
	public ResultPacsBody pacsSend(ExamInfoUserDTO eu) {
		String logname = "reqPacsCusIonf";
		ResultPacsBody rb = new ResultPacsBody();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();		
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig(mesType);
		String url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method();
		
		if ("7".equals(userType)) { //全景锐科
			String convalue=wcd.getConfig_value();
			PACSCusInfoUpdateMessageRK pms = new PACSCusInfoUpdateMessageRK();
			rb = pms.getMessage(url,convalue, logname, eu);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb; 
	}
}
