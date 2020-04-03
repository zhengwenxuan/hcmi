package com.hjw.webService.client.tj180.Bean;

public class LisResItemBean {
	private String reserveId="";//	体检预约号
	private String unionProjectId="";//	已申请体检项目编码
	private String testNo="";//	生成检验流水号
	private String errorInfo="";//
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public String getTestNo() {
		return testNo;
	}
	public void setTestNo(String testNo) {
		this.testNo = testNo;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
