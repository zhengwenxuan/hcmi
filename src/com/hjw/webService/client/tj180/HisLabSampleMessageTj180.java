package com.hjw.webService.client.tj180;

import java.util.HashMap;
import java.util.Map;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.tj180.Bean.HisLabSampleBody;
import com.hjw.webService.client.tj180.Bean.HisLabSampleResBean;
import com.hjw.webService.client.tj180.Bean.HisLabSampleResBody;
import com.hjw.webService.client.tj180.Bean.LisGetReqBean;
import com.hjw.webService.client.tj180.Bean.LisGetResBean;
import com.hjw.webService.client.tj180.Bean.LisGetResItemBean;

import net.sf.json.JSONObject;

public class HisLabSampleMessageTj180 {

	public HisLabSampleResBody getMessage(String url,HisLabSampleBody body,String logname){
		HisLabSampleResBody resBody = new HisLabSampleResBody();
		try {
			JSONObject json = JSONObject.fromObject(body);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
			String result = HttpUtil.doPost(url,body,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
			if((result!=null)&&(result.trim().length()>0)){
				JSONObject jsonobject = JSONObject.fromObject(result.trim());
				Map classMap = new HashMap();
				classMap.put("sampleInfo",HisLabSampleResBean.class);
				resBody = (HisLabSampleResBody) JSONObject.toBean(jsonobject,HisLabSampleResBody.class,classMap);
				if("200".equals(resBody.getStatus())){					
					if(body.getBreserveId().indexOf("B")==0){						
						resBody.setBreserveId(resBody.getReserveId());
						resBody.setReserveId(body.getBreserveId());
					}
					resBody.setStatus("AA");
				}else{
					resBody.setStatus("AE");
				}
			}else{
				resBody.setStatus("AE");
				resBody.setErrorinfo(url  +" 返回无返回");
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			resBody.setStatus("AE");
			resBody.setErrorinfo(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return resBody;
	}
	
	public ResultLisBody getLisMessage(String url,String testno,String logname){
		ResultLisBody rb = new ResultLisBody();
		ResultHeader rh = new ResultHeader();
		try {
			LisGetReqBean p = new LisGetReqBean();
			p.setTestNo(testno);
			JSONObject json = JSONObject.fromObject(p);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			String result = HttpUtil.doPost(url,p,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
            if((result!=null)&&(result.trim().length()>0)){	   
				result = result.trim();
				JSONObject jsonobject = JSONObject.fromObject(result);
				Map classMap = new HashMap();
				classMap.put("labResultInfo", LisGetResItemBean.class);
				LisGetResBean resdah = new LisGetResBean();
				resdah = (LisGetResBean) JSONObject.toBean(jsonobject, LisGetResBean.class, classMap);
				if ("200".equals(resdah.getStatus())) {
					rh.setTypeCode("AA");
					rb.setLisResultList(resdah.getLabResultInfo());
				}else{
					rh.setTypeCode("AE");
					rh.setText(resdah.getErrorinfo());
				}
			}else{
				rh.setTypeCode("AE");
				rh.setText(url  +" 返回无返回");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		rb.setResultHeader(rh);
		return rb;
	}
}
