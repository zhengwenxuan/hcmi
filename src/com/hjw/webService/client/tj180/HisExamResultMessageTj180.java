package com.hjw.webService.client.tj180;

import java.util.HashMap;
import java.util.Map;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.HisExamBody;
import com.hjw.webService.client.tj180.Bean.HisExamResBean;
import com.hjw.webService.client.tj180.Bean.HisExamResBody;

import net.sf.json.JSONObject;

public class HisExamResultMessageTj180 {

	public HisExamResBody getMessage(String url,HisExamBody body,String logname){
		HisExamResBody resBody = new HisExamResBody();
		try {
			JSONObject json = JSONObject.fromObject(body);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
			String result = HttpUtil.doPost(url,body,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
			if((result!=null)&&(result.trim().length()>0)){
				JSONObject jsonobject = JSONObject.fromObject(result.trim());
				Map classMap = new HashMap();
				classMap.put("examResultInfo",HisExamResBean.class);
				resBody = (HisExamResBody) JSONObject.toBean(jsonobject,HisExamResBody.class,classMap);
				if("200".equals(resBody.getStatus())){
					resBody.setStatus("AA");
				}else{
					resBody.setStatus("AE");
				}
			}else{
				resBody.setStatus("AE");
				resBody.setErrorInfo(url  +" 返回无返回");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			resBody.setStatus("AE");
			resBody.setErrorInfo(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return resBody;
	}
}
