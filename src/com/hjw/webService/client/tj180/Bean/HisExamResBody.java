package com.hjw.webService.client.tj180.Bean;

import java.util.List;

public class HisExamResBody {
	private String status; //返回状态码   正常：200，参数异常400，未知错误: 500
	private String patientId;//客户HISID号
	private String errorInfo;
	private List<HisExamResBean> examResultInfo;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public List<HisExamResBean> getExamResultInfo() {
		return examResultInfo;
	}
	public void setExamResultInfo(List<HisExamResBean> examResultInfo) {
		this.examResultInfo = examResultInfo;
	}
}
