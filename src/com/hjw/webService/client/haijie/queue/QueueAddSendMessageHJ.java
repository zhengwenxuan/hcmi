package com.hjw.webService.client.haijie.queue;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;

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
public class QueueAddSendMessageHJ {

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResBody getMessage(String url,String tokenurl,QueueAddBean eu,String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +  eu.getExam_id());
			String[] tokenurls= tokenurl.split("&");
			QueueHttpUtil qh= new QueueHttpUtil();
			qh.getToken(tokenurls[0], tokenurls[1], tokenurls[2],logname);
		    Thread.sleep(100);	
			String res = qh.addToQueue(url+"/"+eu.getExam_id(),tokenurls[1],"utf-8");	
			TranLogTxt.liswriteEror_to_txt(logname, "res:-res" +  res);
			rb.setRestext("");
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
