package com.hjw.webService.client.hzzbmz;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.RandomUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.DAHCustomerBean;
import com.hjw.webService.client.body.DAHResBody;
import com.hjw.webService.client.hzzbmz.Bean.CustDAHBean;
import com.hjw.webService.client.hzzbmz.Bean.DAHResBean;
import com.hjw.webService.client.hzzbmz.client.HisServiceDelegate;
import com.hjw.webService.client.hzzbmz.client.HisService_Service;
import com.hjw.webService.client.hzzbmz.client.HisService_ServiceLocator;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;

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
public class DAHSendMessageHZZBMZ{
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public DAHResBody getMessage(String url,DAHCustomerBean eu,String logname) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		DAHResBody rb = new DAHResBody();
		try {
			CustDAHBean cu= new CustDAHBean();
			String mzdm="TJ"+RandomUtil.getRandomFileName();
			cu.setMzhm(mzdm);
			cu.setBrxm(eu.getName());
		    cu.setBrxb(eu.getSexcode());
			cu.setCsny(DateTimeUtil.shortFmt5(DateTimeUtil.parse2(eu.getBrid())));
			cu.setSfzh(eu.getId_num());
			cu.setJdr("管理员");
			cu.setJdsj(DateTimeUtil.getDateTimes());
			JSONObject json = JSONObject.fromObject(cu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("DAH_SEND");
			String web_url = wcd.getConfig_url().trim();
			HisService_Service hs = new HisService_ServiceLocator(web_url);
			HisServiceDelegate hsd=hs.getHisService();
			//str = str.replaceAll("\"", "");
			Object o= hsd.invoke("adapter.hisService","SYNCMSBRDA", str);			
			String res = (String)o;
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + res);
			DAHResBean da= new DAHResBean();
			JSONObject jsonobject = JSONObject.fromObject(res);
			da = (DAHResBean) JSONObject.toBean(jsonobject, DAHResBean.class);
			if ((da!=null)&&(da.getSuccess()!=null)&&("0".equals(da.getSuccess()))){
				rb.setRescode("ok");
				rb.setIdnumber(da.getBrid());
			}else{
				rb.setRescode("error");
				rb.setIdnumber("");
				rb.setRestext(da.getMessage());
			}

		} catch (Exception ex) {
			com.hjw.interfaces.util.StringUtil.formatException(ex);
			rb.setRescode("error");
			rb.setIdnumber("");
		}
		return rb;
	}

	
}
