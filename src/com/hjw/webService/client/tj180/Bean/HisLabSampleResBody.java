package com.hjw.webService.client.tj180.Bean;

import java.util.List;

public class HisLabSampleResBody {

	private String status="";//返回状态码 正常：200，参数异常400，未知错误: 500
	private String reserveId="";//体检预约号
	private String breserveId="";//
	private String errorinfo;//错误信息提示 正常：””，异常：中文提示
	private List<HisLabSampleResBean> sampleInfo;
	
	public String getBreserveId() {
		return breserveId;
	}
	public void setBreserveId(String breserveId) {
		this.breserveId = breserveId;
	}
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
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public List<HisLabSampleResBean> getSampleInfo() {
		return sampleInfo;
	}
	public void setSampleInfo(List<HisLabSampleResBean> sampleInfo) {
		this.sampleInfo = sampleInfo;
	}
}
