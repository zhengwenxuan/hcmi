package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class UnHisResBean{
	private String status = "";
	private String errorinfo = "";
	private List<HisResItemBean> refundItemsInfo = new ArrayList<HisResItemBean>();
    private String refundtemsNum="";    

	public List<HisResItemBean> getRefundItemsInfo() {
		return refundItemsInfo;
	}

	public void setRefundItemsInfo(List<HisResItemBean> refundItemsInfo) {
		this.refundItemsInfo = refundItemsInfo;
	}

	public String getRefundtemsNum() {
		return refundtemsNum;
	}

	public void setRefundtemsNum(String refundtemsNum) {
		this.refundtemsNum = refundtemsNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorinfo() {
		return errorinfo;
	}

	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

}
