package com.hjw.webService.client.tj180;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.tj180.Bean.CustModifyPSWBody;
import com.hjw.webService.client.tj180.Bean.CustModifyPSWResBody;

import net.sf.json.JSONObject;

public class CUSTOMERModifyPSWMessageTj180 {

	public CustModifyPSWResBody getMessage(String url,CustModifyPSWBody body,String logname){
		CustModifyPSWResBody res = new CustModifyPSWResBody();
		try {			
			JSONObject json = JSONObject.fromObject(body);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
			String result = HttpUtil.doPost(url,body,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
            if((result!=null)&&(result.trim().length()>0)){	                    
	                    result = result.trim();
	    				JSONObject jsonobject = JSONObject.fromObject(result);
						res = (CustModifyPSWResBody)JSONObject.toBean(jsonobject,CustModifyPSWResBody.class);
						if("200".equals(res.getStatus())){
							res.setStatus("AA");
						}else{
							res.setStatus("AE");
						}
	                }else{
	                	res.setStatus("AE");
	                	res.setErrorinfo(url  +" 返回无返回");
	                }

		} catch (Exception ex) {
			ex.printStackTrace();
			res.setStatus("AE");
			res.setErrorinfo(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return res;
	}
}
