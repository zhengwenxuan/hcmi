package com.hjw.webService.client.TCI.bean;

import java.util.ArrayList;
import java.util.List;

public class TransmitResData {
	private String appcode;
	private String databuffer;
	private List<TransmitResData2> resultlist = new ArrayList<TransmitResData2>();
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public String getDatabuffer() {
		return databuffer;
	}
	public void setDatabuffer(String databuffer) {
		this.databuffer = databuffer;
	}
	public List<TransmitResData2> getResultlist() {
		return resultlist;
	}
	public void setResultlist(List<TransmitResData2> resultlist) {
		this.resultlist = resultlist;
	}
}
