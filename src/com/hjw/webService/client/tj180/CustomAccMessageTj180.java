package com.hjw.webService.client.tj180;

import java.util.HashMap;
import java.util.Map;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.CustAccBody;
import com.hjw.webService.client.Bean.CustAccResBean;
import com.hjw.webService.client.Bean.CustAccResBody;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class CustomAccMessageTj180{
	  
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public CustAccResBody getMessage(String url,CustAccBody custom,String logname) {		
		CustAccResBody resdah=new CustAccResBody();		
		try {			
			JSONObject json = JSONObject.fromObject(custom);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			String result = HttpUtil.doPost(url,custom,"utf-8");
			//String result="{\"errorInfo\":\"\",\"status\":\"200\",\"patNum\":1,\"customerInfo\":[{\"customerName\":\"丁满足\",\"occupation\":null,\"customerCardNo\":\"0007101827\",\"phone\":\"13506062789\",\"customerOrganization\":\"网络预约—01\",\"customerIdentityNo\":\"350582196812260510\",\"customerChargeType\":\"自费\",\"customerSex\":\"1\",\"customerBirthPlace\":\"350582\",\"customerBirthday\":\"1968-12-26\",\"customerMarriedAge\":\"22\",\"unitInContract\":\"43\",\"customerWebbed\":\"已婚\",\"address\":\"福建省泉州市晋江区(县)\",\"customerPatientId\":\"4800009\",\"customerIdentity\":\"地方\",\"birthPlaceName\":\"福建晋江市\",\"customerNation\":\"汉族\",\"customerSSid\":null}]}";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
            if((result!=null)&&(result.trim().length()>0)){	                    
	                    result = result.trim();
	    				JSONObject jsonobject = JSONObject.fromObject(result);
	    				Map classMap = new HashMap();
						classMap.put("customerInfo", CustAccResBean.class);	 
						resdah = (CustAccResBody)JSONObject.toBean(jsonobject,CustAccResBody.class,classMap);
						if("200".equals(resdah.getStatus())){
							resdah.setStatus("AA");
						}else{
							resdah.setStatus("AE");
						}
	                }else{
	                	resdah.setStatus("AE");
	                	resdah.setErrorInfo(url  +" 返回无返回");
	                }

		} catch (Exception ex) {
			ex.printStackTrace();
			resdah.setStatus("AE");
        	resdah.setErrorInfo("");
		}
		return resdah;
	}
}
