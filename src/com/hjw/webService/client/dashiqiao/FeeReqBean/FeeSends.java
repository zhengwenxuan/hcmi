package com.hjw.webService.client.dashiqiao.FeeReqBean;

import com.hjw.webService.client.dashiqiao.LisReqBean.LisReqSends;
import com.hjw.webService.client.dashiqiao.PacsReqBean.PacsReqSends;

public class FeeSends {
	  
	private FeeMessageDSQ FeeMessage =  new FeeMessageDSQ();
	private LisReqSends LisReqSends =  new LisReqSends();
	private PacsReqSends PacsReqSends =  new PacsReqSends();
	
	public FeeMessageDSQ getFeeMessage() {
		return FeeMessage;
	}
	public void setFeeMessage(FeeMessageDSQ feeMessage) {
		FeeMessage = feeMessage;
	}
	public LisReqSends getLisReqSends() {
		return LisReqSends;
	}
	public void setLisReqSends(LisReqSends lisReqSends) {
		LisReqSends = lisReqSends;
	}
	public PacsReqSends getPacsReqSends() {
		return PacsReqSends;
	}
	public void setPacsReqSends(PacsReqSends pacsReqSends) {
		PacsReqSends = pacsReqSends;
	}
	
	
	
	  
	  
}
