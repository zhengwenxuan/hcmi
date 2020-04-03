package com.hjw.webService.client;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.FeeResultWxAndZfbBody;
import com.hjw.webService.client.body.FeeWxAndZfbMessage;
import com.hjw.webService.client.dashiqiao.FEESendMessageDSQ;
import com.hjw.webService.client.empty.FEESendMessageEmpty;
import com.hjw.webService.client.zhaotong.UNFEESendWxAndZfbMessageZT;


public class UNFeeSendWxAndZfbMessage {

	private FeeWxAndZfbMessage feeMessage;

	public UNFeeSendWxAndZfbMessage(FeeWxAndZfbMessage feeMessage){
		this.feeMessage=feeMessage;
	}
	
	public FeeResultWxAndZfbBody UNfeeSendWxAndZfb(String url,String userType, boolean debug) {
		
		String logname="UNreqFeeWXAndZfb";
		TranLogTxt.liswriteEror_to_txt(logname, "-------------------------------------------------"+DateTimeUtil.getDateTime()+"-"+feeMessage.getReq_nums()+"--------------------------------");
		FeeResultWxAndZfbBody rb = new FeeResultWxAndZfbBody();
		System.out.println("--------------"+userType);
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		//String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID", feeMessage.getCenter_num()).getConfig_value().trim();//开单医生id
		
		
		
		if ("30".equals(userType)) {//微信支付宝 和金蝶第三方退费
			UNFEESendWxAndZfbMessageZT fm = new UNFEESendWxAndZfbMessageZT(feeMessage);//
			rb =fm.getMessage(url, logname);
			
		}else{
			rb.setResultCode("AE");
			rb.setResultDesc("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
		
	}
	
}
