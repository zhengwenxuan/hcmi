package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.YbCustomMessage;
import com.hjw.webService.client.body.YbCustomResultBody;
import com.hjw.webService.client.dbgj.YBCustomSendMessageTJPT;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.20	医保卡查询服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class YBCustomSendMessage {

	private YbCustomMessage custom;
	
	public YBCustomSendMessage(YbCustomMessage custom){
		this.custom=custom;
	}


	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public YbCustomResultBody customSend(String url,String userType, boolean debug) {
		String logname="reqYbCust";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		YbCustomResultBody rb = new YbCustomResultBody();
		if ("1".equals(userType)) {
			YBCustomSendMessageTJPT pms = new YBCustomSendMessageTJPT(custom);
			rb = pms.getMessage(url, logname);
		}else{
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("接口无对应厂家,请检查webservice_configuration表config_method字段");
		}
		return rb;
	}

}
