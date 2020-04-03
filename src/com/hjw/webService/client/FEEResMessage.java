package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ShanxiXXG.FEEResMessageXXG;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.dashiqiao.FEEResMessageDSQ;
import com.hjw.webService.client.fangzheng.FEEResMessageZL2;
import com.hjw.webService.client.hokai.FEEResMessageHK;
import com.hjw.webService.client.hokai305.FEEResMessageHK305;
import com.hjw.webService.client.huojianwa.FEEResMessageHjw;
import com.hjw.webService.client.liubaxian.FEEResMessageLBX;
import com.hjw.webService.client.xintong.FEEResMessageQH;
import com.hjw.webService.client.zhangyeshi.FEEResMessageZYS;
import com.hjw.webService.client.zhonglian.FEEResMessageZL;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * his获取缴费结果
 * 
 * @author yangm
 *
 */
public class FEEResMessage {
	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultHeader getMessage(String exam_num) {
		ResultHeader rb = new ResultHeader();
		String logName = "feeres";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("HIS_GET");
		String web_url = wcd.getConfig_url().trim();
		String userType = wcd.getConfig_method().trim();
		System.out.println("userType--0--" + userType);
		if ("2".equals(userType)) {// 中联
			FEEResMessageZL lis = new FEEResMessageZL();
			rb = lis.getMessage(web_url, exam_num, logName);
		} else if ("8".equals(userType)) {// 中联获取缴费信息2
			FEEResMessageZL2 lis = new FEEResMessageZL2();
			rb = lis.getMessage(web_url, exam_num, logName);
		}else if ("9".equals(userType)) {//火箭蛙
			FEEResMessageHjw lis = new FEEResMessageHjw();
			rb = lis.getMessage(web_url, exam_num, logName);
		}else if ("17".equals(userType)) {//张掖市 -- 坐标
			FEEResMessageZYS lis = new FEEResMessageZYS();
			rb = lis.getMessage(web_url, exam_num, logName);
		}else if ("31".equals(userType)) {//留坝县
			FEEResMessageLBX lis = new FEEResMessageLBX();
			rb = lis.getMessage(web_url, exam_num, logName);
		} else {
			rb.setTypeCode("AE");
			rb.setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}
	
	public String getMessageServer(String body, String logName) {
		String res = "";
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------" + DateTimeUtil.getDate()
				+ "---------------------------------");
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac
				.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("HIS_GET");
		String userType = wcd.getConfig_method().trim();
		TranLogTxt.liswriteEror_to_txt(logName, "userType--0--" + userType);
		
		if ("21".equals(userType)) {//和佳-常德二院
			FEEResMessageHK lis = new FEEResMessageHK();
			res = lis.getMessage(body, logName);
		} else if ("21.1".equals(userType)) {//和佳-305
			FEEResMessageHK305 lis = new FEEResMessageHK305();
			res = lis.getMessage(body, logName);
		}else if ("18".equals(userType)) {//青海-- 信通
			FEEResMessageQH lis = new FEEResMessageQH();
			res = lis.getMessage(body, logName);
		} else if ("27".equals(userType)) {//青海-- 信通
			FEEResMessageDSQ lis = new FEEResMessageDSQ();
			res = lis.getMessage(body, logName);
		} else if ("33".equals(userType)) {//山西心血管
			FEEResMessageXXG lis = new FEEResMessageXXG();
			res = lis.getMessage(body, logName);
		} 
		return res;
	}
}
