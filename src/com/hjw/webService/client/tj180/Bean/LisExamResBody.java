package com.hjw.webService.client.tj180.Bean;

import java.util.List;

public class LisExamResBody {
	private String status=""; //返回状态码   正常：200，参数异常400，未知错误: 500
	private String reserveId="";//体检预约号
	private String errorInfo;
	private List<LisLabSampleResBean> sampleInfo;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public List<LisLabSampleResBean> getSampleInfo() {
		return sampleInfo;
	}
	public void setSampleInfo(List<LisLabSampleResBean> sampleInfo) {
		this.sampleInfo = sampleInfo;
	}
	
}
