package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResDeptBean{
	private String status = "";
	private String errorinfo = "";
	private List<DeptInfo> deptInfo = new ArrayList<DeptInfo>();

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

	public List<DeptInfo> getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(List<DeptInfo> deptInfo) {
		this.deptInfo = deptInfo;
	}

}
