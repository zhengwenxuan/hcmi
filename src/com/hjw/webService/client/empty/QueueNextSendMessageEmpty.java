package com.hjw.webService.client.empty;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueNextBean;
import com.hjw.webService.client.body.QueueResBody;

import net.sf.json.JSONObject;

public class QueueNextSendMessageEmpty {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResBody getMessage(String url,String tokenurl,QueueNextBean eu,String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			rb.setRestext("空实现直接返回成功");
			rb.setRescode("AA");
			rb.setIdnumber("");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
			rb.setIdnumber("");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}

}
