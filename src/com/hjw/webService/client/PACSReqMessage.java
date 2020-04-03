package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai305.PacsReqMessage305;
import com.hjw.webService.client.xhhk.PacsReqMessageXHHK;
import com.hjw.webService.client.xhhk.PacsXDReqMessageXHHK;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSReqMessage {

	public String pacsSendString(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("19".equals(userType)) {//银川-星华惠康
			PacsReqMessageXHHK lis= new PacsReqMessageXHHK();
			rb = lis.getMessage(xmlstr, logName);
		}else if("21.1".equals(userType)){
			PacsReqMessage305 lis= new PacsReqMessage305();
			rb = lis.getMessage(xmlstr, logName);
		}else {
			rb="接口无对应厂家,请联系体检工程师检查webservice_configuration表config_method字段";
		}
		return rb;
	}
	
	public String pacsXDSendString(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("19".equals(userType)) {//银川-星华惠康
			PacsXDReqMessageXHHK lis= new PacsXDReqMessageXHHK();
			rb = lis.getMessage(xmlstr, logName);
		} else {
			rb="接口无对应厂家,请联系体检工程师检查webservice_configuration表config_method字段";
		}
		return rb;
	}
	
}
