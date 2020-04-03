package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.hokai305.FEEReqMessageHK305;

public class FEEReqMessage {

	
	public String FeeSendString(String xmlstr,String userType,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		String rb="";
		if ("21.1".equals(userType)) {//和佳-305
			FEEReqMessageHK305 Feereq = new FEEReqMessageHK305();
			rb = Feereq.getFeereqmeassgae(xmlstr, logname);
		}else {
			rb=("AE");
		}
		return rb;
	}
}
