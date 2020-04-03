package com.hjw.webService.client.body;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.tj180.Bean.QueueResSearchBean;

public class QueueResSearchBody {
	private String rescode = "";//AA表示成功 否则表示失败
	private List<QueueResSearchBean> queryList = new ArrayList<QueueResSearchBean>();
	private String restext = "";

	public String getRescode() {
		return rescode;
	}

	public void setRescode(String rescode) {
		this.rescode = rescode;
	}

	

	public List<QueueResSearchBean> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<QueueResSearchBean> queryList) {
		this.queryList = queryList;
	}

	public String getRestext() {
		return restext;
	}

	public void setRestext(String restext) {
		this.restext = restext;
	}

}
