package com.hjw.webService.service.lisbean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.service.bean.RetCustome;

public class RetLisStatusCustome extends RetCustome {

	private List<String> sample_barcode=new ArrayList<String>();// 申请单号// 条码号
	
	private String status;//状态
	
	private String effectiveTime;//报告时间	

	public List<String> getSample_barcode() {
		return sample_barcode;
	}

	public void setSample_barcode(List<String> sample_barcode) {
		this.sample_barcode = sample_barcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}


}
