package com.hjw.webService.client.body;


public class QueueResInsertBody {
	private String rescode = "";//AA表示成功 否则表示失败
	private String restext = "";

	public String getRescode() {
		return rescode;
	}

	public void setRescode(String rescode) {
		this.rescode = rescode;
	}

	public String getRestext() {
		return restext;
	}

	public void setRestext(String restext) {
		this.restext = restext;
	}

}
