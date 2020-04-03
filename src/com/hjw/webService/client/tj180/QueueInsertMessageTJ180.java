package com.hjw.webService.client.tj180;


import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueInsertBean;
import com.hjw.webService.client.body.QueueResInsertBody;
import com.hjw.webService.client.tj180.Bean.QueueInsertBean180;

import net.sf.json.JSONObject;



/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  增加排队
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class QueueInsertMessageTJ180 {
	
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResInsertBody getMessage(String url, QueueInsertBean eu, String logname) {
		QueueResInsertBody rb = new QueueResInsertBody();
		try {
			JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			QueueInsertBean180 qbb = new QueueInsertBean180();
			qbb.setCURRENT_NO(eu.getCurrentNo());
			qbb.setIP_ADDRESS(eu.getIpAddress());
			qbb.setNEXT_NO(eu.getNextNo());
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url + "\r\n");
			String result = HttpUtil.doPost(url,qbb, "utf-8");
			//String result="true";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				if(result.toLowerCase().indexOf("true")>=0)
				rb.setRestext("");
				rb.setRescode("AA");
			} else {
				rb.setRestext("查询失败");
				rb.setRescode("AE");
			}
		} catch (Exception ex) {
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");			
		}

		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}
	
	
}
