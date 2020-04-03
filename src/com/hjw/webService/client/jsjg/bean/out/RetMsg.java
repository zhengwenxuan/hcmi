package com.hjw.webService.client.jsjg.bean.out;

import com.google.gson.Gson;

public class RetMsg {

	private OutParam OutParam = new OutParam();

	public OutParam getOutParam() {
		return OutParam;
	}

	public void setOutParam(OutParam outParam) {
		OutParam = outParam;
	}
	
	public static void main(String[] args) {
		RegInfo regInfo = new RegInfo();
		ApplyOrderInfo applyOrderInfo1 = new ApplyOrderInfo();
		applyOrderInfo1.getItems().getItem().add(new ItemJSJG());
		applyOrderInfo1.getItems().getItem().add(new ItemJSJG());
		ApplyOrderInfo applyOrderInfo2 = new ApplyOrderInfo();
		applyOrderInfo2.getItems().getItem().add(new ItemJSJG());
		applyOrderInfo2.getItems().getItem().add(new ItemJSJG());
		
		ApplyOrderInfos applyOrderInfos = new ApplyOrderInfos();
		applyOrderInfos.getApplyOrderInfo().add(applyOrderInfo1);
		applyOrderInfos.getApplyOrderInfo().add(applyOrderInfo2);
		
		RetMsg RetMsg = new RetMsg();
		RetMsg.getOutParam().setRegInfo(regInfo);
		RetMsg.getOutParam().setApplyOrderInfos(applyOrderInfos);
		System.out.println(new Gson().toJson(RetMsg, RetMsg.class));
	}
}
