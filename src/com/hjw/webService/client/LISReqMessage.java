package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai305.LISReqMessage305;


/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISReqMessage {

	public String lisSendString(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("21.1".equals(userType)) {//和佳-305
			LISReqMessage305 lisreq = new LISReqMessage305();
			rb = lisreq.getMessage(xmlstr, logName);
		}/* else if ("21".equals(userType)) {//和佳-常德二院
		LisResMessageHK lis= new LisResMessageHK();
			rb = lis.getMessage(xmlstr, logName);
		} else if ("21.1".equals(userType)) {//
			LisResMessageHK305 lis= new LisResMessageHK305();
			rb = lis.getMessage(xmlstr, logName);
		}*/else {
			rb=("AE");
		}
		return rb;
	}
	
}
