package com.hjw.webService.client.bdyx;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.bdyx.bean.MessageGet;
import com.hjw.webService.client.bdyx.bean.ResponseGet;
import com.hjw.webService.client.body.ResultHeader;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetLisPacsResControlBDYX {
	private static ConfigService configService;
	private static JdbcQueryManager jdbcQueryManager;
	private static String SEND_SYS_ID;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		SEND_SYS_ID = configService.getCenterconfigByKey("SEND_SYS_ID").getConfig_value();
	}
    public ResultHeader getMessage(String url, String logname){
    	TranLogTxt.liswriteEror_to_txt(logname, "url:"+url);
    	ResultHeader rb = new ResultHeader();
    	Map<String, Object> req = new HashMap<>();
		req.put("syscode", SEND_SYS_ID);//体检系统id
		req.put("serviceId", "");//serviceId传值就是拉取固定类型的消息
		req.put("isJson", "true");
		TranLogTxt.liswriteEror_to_txt(logname, "req:"+req);
		String res = HttpUtil.doGet(url, req, "utf-8");
//		TranLogTxt.liswriteEror_to_txt(logname, "res:"+res);
		ResponseGet responseGet = new Gson().fromJson(res, ResponseGet.class);

		if(responseGet.getData().getMessages().size() > 0) {
			for(MessageGet message : responseGet.getData().getMessages()) {
//				TranLogTxt.liswriteEror_to_txt(logname, "body:"+message.getBody());
				if("BS004".equals(message.getHead(1))) {//BS004医嘱执行状态信息服务
					rb = new UpdateStatusBDYX().getMessage(message.getBody(), logname);
				} else if("BS319".equals(message.getHead(1))) {//BS319检验报告信息服务
					rb = new GetLisResControlBDYX().getMessage(message.getBody(), logname);
				} else if("BS320".equals(message.getHead(1))) {//BS320检查报告信息服务
					rb = new GetPacsResControlBDYX().getMessage(message.getBody(), logname);
				} else if("BS354".equals(message.getHead(1))) {//BS354微生物检验报告信息服务
					rb = new GetLisMicrobeResControlBDYX().getMessage(message.getBody(), logname);
				} else if("BS366".equals(message.getHead(1))) {//BS366病理检查报告信息服务
					rb = new GetPacsResControlBDYX().getMessage(message.getBody(), logname);
				} else if("BS368".equals(message.getHead(1))) {//BS368胃镜检查报告信息服务
					rb = new GetPacsResControlBDYX().getMessage(message.getBody(), logname);
				} else if("BS369".equals(message.getHead(1))) {//BS369心电图检查报告信息服务
					rb = new GetPacsResControlBDYX().getMessage(message.getBody(), logname);
				} else {
					TranLogTxt.liswriteEror_to_txt(logname, "不识别的service_id："+message.getHead(1));
				}
			}
		} else {
			TranLogTxt.liswriteEror_to_txt(logname, "消息列表中没有消息");
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(rb, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"res:"+xml);
    	return rb;    	
    }
    
}
