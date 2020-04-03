package com.hjw.webService.client.empty;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ReqId;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;

public class FEESendMessageEmpty {

	private FeeMessage feeMessage;

	public FEESendMessageEmpty(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}

	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = JaxbUtil.convertToXml(this.feeMessage, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		
		ReqId req= new ReqId();
		req.setReq_id(feeMessage.getREQ_NO());
		rb.getResultHeader().setTypeCode("AA");
		rb.getResultHeader().setText("");
		rb.getControlActProcess().getList().add(req);
		xml = JaxbUtil.convertToXml(rb, true);
		
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		return rb;
	}
}
