package com.hjw.webService.client.xhhk.lisbean;

import com.google.gson.Gson;

public class SetApplyStatus {

	private String ApplyNo ="";//申请单号
	private String ItemCode ="";//检验项目编号
	private int ApplyStatus;//申请单状态:	1 -新建、2-撤销、3-确认、4-签收、5-报告、6-复核（已确认）、6-打印
	
	public String getApplyNo() {
		return ApplyNo;
	}
	public String getItemCode() {
		return ItemCode;
	}
	public int getApplyStatus() {
		return ApplyStatus;
	}
	public void setApplyNo(String applyNo) {
		ApplyNo = applyNo;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public void setApplyStatus(int applyStatus) {
		ApplyStatus = applyStatus;
	}
	
	public static void main(String[] args) {
		String str = ""
		+"{"
		+"\"ApplyNo\":\"5c6d091ee082922974525f87\","
		+"\"ItemCode\":\"5c6d091ee082922974525f88\","
		+"\"ApplyStatus\":2"
		+"}";
		
		SetApplyStatus applyStatus = new Gson().fromJson(str, SetApplyStatus.class);
		System.out.println(applyStatus.getApplyNo());
	}
}
