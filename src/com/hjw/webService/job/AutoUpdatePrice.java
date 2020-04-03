package com.hjw.webService.job;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.model.ChargingItemModel;
import com.hjw.wst.service.ChargingItemService;

public class AutoUpdatePrice {
	private static ChargingItemService chargingItemService;
	private static String logname = "updatePrice";

	public ResultHeader getMessage() {
		TranLogTxt.liswriteEror_to_txt(logname, "自动更新价格开始");
		ResultHeader rb = new ResultHeader();
		
		try {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			chargingItemService = (ChargingItemService) wac.getBean("chargingItemService");
			
			ChargingItemModel model = new ChargingItemModel();
 			chargingItemService.updateHIsPriceSynchro(model);
			rb.setTypeCode("AA");
			rb.setText("数据同步成功!");
			
		} catch (Throwable e) {
			rb.setTypeCode("AE");
			rb.setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}
}
