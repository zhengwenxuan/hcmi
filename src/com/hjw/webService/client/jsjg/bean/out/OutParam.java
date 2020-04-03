package com.hjw.webService.client.jsjg.bean.out;

public class OutParam {

	private RegInfo RegInfo;//就诊信息
	private ApplyOrderInfos ApplyOrderInfos;//申请单集合
	private OutResult OutResult = new OutResult();//返回状态集合
	
	public OutResult getOutResult() {
		return OutResult;
	}
	public void setOutResult(OutResult outResult) {
		OutResult = outResult;
	}
	public RegInfo getRegInfo() {
		return RegInfo;
	}
	public ApplyOrderInfos getApplyOrderInfos() {
		return ApplyOrderInfos;
	}
	public void setRegInfo(RegInfo regInfo) {
		RegInfo = regInfo;
	}
	public void setApplyOrderInfos(ApplyOrderInfos applyOrderInfos) {
		ApplyOrderInfos = applyOrderInfos;
	}
	
}
