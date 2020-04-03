package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.GetLisPacsResControlBDYX;
import com.hjw.webService.client.bdyx.useCode.GetLisPacsResControlBDYX_UseCode;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.job.fangzheng.GetLisPacsResControlFZ;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoGetLisPacsResMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage() {
		ResultHeader rb= new ResultHeader();
		String logName="resLisPacs";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("LISPACS_GET");
		String web_url = wcd.getConfig_url().trim();
		String userType=wcd.getConfig_method().trim();
		System.out.println("userType--0--"+userType);
		if ("8".equals(userType)) {//方正
			GetLisPacsResControlFZ lis= new GetLisPacsResControlFZ();
			rb=lis.getMessage();
		}else if ("26".equals(userType)) {//武威肿瘤医院-北大医信
			GetLisPacsResControlBDYX lis= new GetLisPacsResControlBDYX();
			rb=lis.getMessage(web_url, logName);
		}else if ("26.1".equals(userType)) {//武威肿瘤医院-北大医信-使用编码代替id
			GetLisPacsResControlBDYX_UseCode lis= new GetLisPacsResControlBDYX_UseCode();
			rb=lis.getMessage(web_url, logName);
		}else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
}
