package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.JzkCustom;
import com.hjw.webService.client.body.YbCustomMessage;
import com.hjw.webService.client.dashiqiao.JZKCustomSendMessageDSQ;
import com.hjw.webService.client.dbgj.JZKCustomSendMessageTJPT;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.21	就诊卡查询服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class JZKCustomSendMessage {

	private YbCustomMessage custom;
	
	public JZKCustomSendMessage(YbCustomMessage custom){
		this.custom=custom;
	}  

	public YbCustomMessage getCustom() {
		return custom;
	}

	public void setCustom(YbCustomMessage custom) {
		this.custom = custom;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public JzkCustom customSend(String url,String userType, boolean debug) {
		String logname="reqJzkCust";
		TranLogTxt.liswriteEror_to_txt(logname, "------------------------------------------"+DateTimeUtil.getDateTime()+"---------------------------------");
		JzkCustom rb = new JzkCustom();		
		if ("1".equals(userType)) {
			JZKCustomSendMessageTJPT jzk= new JZKCustomSendMessageTJPT(custom);
			jzk.getMessage(url, logname);
		}else if("27".equals(userType)){//大石桥
			JZKCustomSendMessageDSQ jzk= new JZKCustomSendMessageDSQ(custom);
			rb = jzk.getMessage(url, logname);
		} else {
			JzkCustom jzk=new JzkCustom();
			jzk.setRESULT("9");
		}
		return rb;
	}

}
