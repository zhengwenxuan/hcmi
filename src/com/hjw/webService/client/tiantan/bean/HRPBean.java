package com.hjw.webService.client.tiantan.bean;

import java.util.ArrayList;
import java.util.List;

public class HRPBean {

	private String senderID;
	private List<HRPData> datas = new ArrayList<HRPData>();
	
	public String getSenderID() {
		return senderID;
	}
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}
	public List<HRPData> getDatas() {
		return datas;
	}
	public void setDatas(List<HRPData> datas) {
		this.datas = datas;
	}
	
	
	
}
