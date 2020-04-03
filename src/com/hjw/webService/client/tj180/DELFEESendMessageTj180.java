package com.hjw.webService.client.tj180;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.DelFeeMessage;
import com.hjw.webService.client.body.FeeReqBody;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.19	项目减项  天健 平台对接-东北国际
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DELFEESendMessageTj180 {

	private DelFeeMessage feeMessage;
	
	public DELFEESendMessageTj180(DelFeeMessage feeMessage){
		this.feeMessage=feeMessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeReqBody getMessage(String url, String logname) {
		FeeReqBody rb = new FeeReqBody();
		String xml = "";
		this.feeMessage.setMSG_TYPE("TJ606");
		xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		List<ReqNo> List = new ArrayList<ReqNo>();
		ReqNo rn = new ReqNo();
		rn.setREQ_NO(this.feeMessage.getREQ_NO());
		List.add(rn);
		rb.getControlActProcess().setList(List);
		rb.getResultHeader().setTypeCode("AA");
		rb.getResultHeader().setText("操作成功");
		return rb;
	}

}
