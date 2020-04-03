package com.hjw.webService.client.jsjg.bean.out;

import java.util.ArrayList;
import java.util.List;

public class ApplyOrderInfos {

	private List<ApplyOrderInfo> ApplyOrderInfo = new ArrayList<>();//单个申请单

	public List<ApplyOrderInfo> getApplyOrderInfo() {
		return ApplyOrderInfo;
	}

	public void setApplyOrderInfo(List<ApplyOrderInfo> applyOrderInfo) {
		ApplyOrderInfo = applyOrderInfo;
	}
}
