package com.hjw.webService.client.tianchang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Description: 2.17	收费退费
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class UNFEESendMessageTC{
	private UnFeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public UNFEESendMessageTC(UnFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getEXAM_NUM() + ":" + xml);
		List<ReqNo> okList = new ArrayList<ReqNo>();
		for (String reqNo : this.feeMessage.getREQ_NOS().getREQ_NO()) {
			ReqNo req = new ReqNo();
			req.setREQ_NO(reqNo);
			okList.add(req);
		}
		FeeReqControlActProcess controlActProcess = new FeeReqControlActProcess();
		controlActProcess.setList(okList);
		rb.setControlActProcess(controlActProcess);
		rb.getResultHeader().setTypeCode("AA");
		rb.getResultHeader().setText("退费已经成功");
		return rb;
	}
	
}
