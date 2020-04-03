package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class LisGetResBean{
	private String status = "";
	private String errorinfo = "";
	private List<LisGetResItemBean> labResultInfo = new ArrayList<LisGetResItemBean>();
   
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

	public List<LisGetResItemBean> getLabResultInfo() {
		return labResultInfo;
	}

	public void setLabResultInfo(List<LisGetResItemBean> labResultInfo) {
		this.labResultInfo = labResultInfo;
	}
}
