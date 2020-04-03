package com.hjw.webService.client.empty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageEmpty {

	private DelFeeMessage feeMessage;

	public DELFEESendMessageEmpty(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
		String xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		
		rb.getResultHeader().setTypeCode("AA");
		rb.getResultHeader().setText("减项成功！");
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}
	
}
