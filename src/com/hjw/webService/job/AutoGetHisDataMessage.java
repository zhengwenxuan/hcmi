package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bjxy.AutoGetHisDataXY;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.webService.client.changan.AutoGetHisDataCA;
import com.hjw.webService.client.nanhua.AutoGetHisDataNH;
import com.hjw.webService.client.xintong.AutoGetClinicItemMessageQH;
import com.hjw.webService.client.xintong.ManualGetClinicItemMessageQH;
import com.hjw.webService.client.zhonglian.BaseData.GetClinicItemMessageZl;
import com.hjw.webService.client.zhonglian.BaseData.GetPriceItemMessageZL;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

public class AutoGetHisDataMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage() {
		ResultHeader rb = new ResultHeader();
		String logName = "autoHisData" + DateTimeUtil.getDateTimes();
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		
		ConfigService configService = (ConfigService) wac.getBean("configService");
		String AUTO_HIS_DATA = configService.getCenterconfigByKey("AUTO_HIS_DATA").getConfig_value().trim();// 是否自动获取his基础数据
		TranLogTxt.liswriteEror_to_txt(logName, "AUTO_HIS_DATA：" + AUTO_HIS_DATA);
		if ("Y".equals(AUTO_HIS_DATA)) {
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("HIS_GET_DATA");
			String web_url = wcd.getConfig_url().trim();
			String userType = wcd.getConfig_method().trim();
			System.out.println("userType--0--" + userType);
			TranLogTxt.liswriteEror_to_txt(logName, "userType：" + userType);
			if ("1.1".equals(userType)) {//天健-长安医院
				AutoGetHisDataCA lis = new AutoGetHisDataCA();
				rb = lis.getMessage(web_url, logName);
			} else if ("4".equals(userType)) {//西苑-北大医信
				AutoGetHisDataXY lis = new AutoGetHisDataXY();
				rb = lis.getMessage(web_url, logName);
			} else if ("10".equals(userType)) {//南华-创星
				AutoGetHisDataNH lis = new AutoGetHisDataNH();
				rb = lis.getMessage(web_url, logName);
			} else if ("11".equals(userType)) {//中联his 获取诊疗
				GetClinicItemMessageZl lis = new GetClinicItemMessageZl();
				lis.getClinicItem(web_url,"utf-8",logName);
				
				GetPriceItemMessageZL lisp = new GetPriceItemMessageZL();
				lisp.getPriceItem(web_url,"utf-8",logName);
			} else if ("18".equals(userType)) {//青海his 获取诊疗
				ManualGetClinicItemMessageQH lis = new ManualGetClinicItemMessageQH();
				ResultHisBody by = lis.getMessage(web_url, logName);
				rb.setTypeCode(by.getResultHeader().getTypeCode());
				rb.setText(by.getResultHeader().getText());
				
			}else {
				rb.setTypeCode("AE");
				rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
			}
		}
		return rb;
	}
}
