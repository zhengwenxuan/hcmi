package com.hjw.webService.client.empty;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueCustomerBean;
import com.hjw.webService.client.body.QueueResBody;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class QueueCustSendMessageEmpty {
	
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public QueueResBody getMessage(String url,QueueCustomerBean eu,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
		QueueResBody rb = new QueueResBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + JSONObject.fromObject(eu));
			rb.setRescode("AA");
			rb.setRestext("接口空实现，直接返回成功");
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
}
