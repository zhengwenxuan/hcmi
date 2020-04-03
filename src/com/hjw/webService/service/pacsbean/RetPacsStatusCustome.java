package com.hjw.webService.service.pacsbean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.service.bean.RetCustome;

public class RetPacsStatusCustome extends RetCustome {
	private List<String> pacs_summary_id=new ArrayList<String>();// 申请单号
	
	private String effectiveTime="";//报告时间	
	private String status="";//状态	
	
	public List<String> getPacs_summary_id() {
		return pacs_summary_id;
	}
	public void setPacs_summary_id(List<String> pacs_summary_id) {
		this.pacs_summary_id = pacs_summary_id;
	}
	public String getEffectiveTime() {
		return effectiveTime;
	}
	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	

}
