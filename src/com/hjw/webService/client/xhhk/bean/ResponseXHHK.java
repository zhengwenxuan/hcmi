package com.hjw.webService.client.xhhk.bean;

import java.util.List;

import com.hjw.webService.client.xhhk.lisbean.ApplyItems;

public class ResponseXHHK {

	private int Code;//接口状态 0 成功 -1 失败
	private String Msg = "";//错误信息
	private String ApplyId = "";//申请单流水号 数据库自动生成的条目编号
	private List<ApplyItems> ApplyItems ;//子申请单流水号列表
	private long Timestamp = System.currentTimeMillis();//时间戳
	
	public int getCode() {
		return Code;
	}
	public String getMsg() {
		return Msg;
	}
	public String getApplyId() {
		return ApplyId;
	}
	
	public long getTimestamp() {
		return Timestamp;
	}
	public void setCode(int code) {
		Code = code;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	public void setApplyId(String applyId) {
		ApplyId = applyId;
	}
	
	public void setTimestamp(long timestamp) {
		Timestamp = timestamp;
	}
	public List<ApplyItems> getApplyItems() {
		return ApplyItems;
	}
	public void setApplyItems(List<ApplyItems> applyItems) {
		ApplyItems = applyItems;
	}
	
}
