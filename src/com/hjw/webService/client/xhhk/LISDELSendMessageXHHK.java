package com.hjw.webService.client.xhhk;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.xhhk.bean.ResponseXHHK;
import com.hjw.webService.client.xhhk.lisbean.DeleteApply;
import com.hjw.webService.client.xhhk.lisbean.HttpClientUtil;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class LISDELSendMessageXHHK {

	private LisMessageBody lismessage;
	private static ConfigService configService;
    private static JdbcQueryManager jdbcQueryManager;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public LISDELSendMessageXHHK(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url,String logname) {
		ResultLisBody rb = new ResultLisBody();
	
		try {
			ControlActLisProcess ca = new ControlActLisProcess();
			List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
			List<LisComponents> components = lismessage.getComponents();
			DeleteApply deleteApply = new DeleteApply();
			for (LisComponents lisComponents : components) {
				deleteApply.setApplyNo(lisComponents.getReq_no());
				ApplyNOBean an = new ApplyNOBean();
			    an.setApplyNO(lisComponents.getReq_no());
			    list.add(an);
				  
				  
				  String jsonStr = new Gson().toJson(deleteApply, DeleteApply.class);
				  TranLogTxt.liswriteEror_to_txt(logname, "入参:" + jsonStr);
				  //String result = HttpUtil.doPost_Str(url, jsonStr, "utf-8");
				  String result = HttpClientUtil.doPostSSLHttps(url, jsonStr, "utf-8");
				  TranLogTxt.liswriteEror_to_txt(logname, "返回:" + result);
				  ResponseXHHK response = new Gson().fromJson(result, ResponseXHHK.class);
				  
					if(0 == response.getCode()){
						ca.setList(list);
						rb.setControlActProcess(ca);
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("lis调用成功");
					}else{
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText(":"+ response.getMsg());
					}
			}
			
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis json格式文件错误");
		}
		return rb;
	}

}

