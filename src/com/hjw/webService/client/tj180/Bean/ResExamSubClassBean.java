package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class ResExamSubClassBean{
	private String status = "";
	private String errorinfo = "";
	private List<ExamSubClassInfo> examSubClassInfo = new ArrayList<ExamSubClassInfo>();

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

	public List<ExamSubClassInfo> getExamSubClassInfo() {
		return examSubClassInfo;
	}

	public void setExamSubClassInfo(List<ExamSubClassInfo> examSubClassInfo) {
		this.examSubClassInfo = examSubClassInfo;
	}

	
}
