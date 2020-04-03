package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class PacsGetResBean{
	private String status = "";
	private String errorinfo = "";
	private List<PacsGetResItemBean> examResultInfo = new ArrayList<PacsGetResItemBean>();
   
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

	public List<PacsGetResItemBean> getExamResultInfo() {
		return examResultInfo;
	}

	public void setExamResultInfo(List<PacsGetResItemBean> examResultInfo) {
		this.examResultInfo = examResultInfo;
	}

	
}
