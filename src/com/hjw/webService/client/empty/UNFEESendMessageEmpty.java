package com.hjw.webService.client.empty;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.FeeReqControlActProcess;
import com.hjw.webService.client.Bean.ReqNo;
import com.hjw.webService.client.body.FeeReqBody;
import com.hjw.webService.client.body.UnFeeMessage;

public class UNFEESendMessageEmpty{
	private UnFeeMessage feeMessage;
	
	public UNFEESendMessageEmpty(UnFeeMessage feeMessage){
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
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getEXAM_NUM() + ":" + xml);
		return rb;
	}
}
