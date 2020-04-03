package com.hjw.webService.client.tj180.BaseData.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResExamClassBean{
	private String status = "";
	private String errorinfo = "";
	private List<ExamClassInfo> examClassInfo = new ArrayList<ExamClassInfo>();

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

	public List<ExamClassInfo> getExamClassInfo() {
		return examClassInfo;
	}

	public void setExamClassInfo(List<ExamClassInfo> examClassInfo) {
		this.examClassInfo = examClassInfo;
	}	

}
