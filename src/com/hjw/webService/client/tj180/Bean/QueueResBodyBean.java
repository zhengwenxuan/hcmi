package com.hjw.webService.client.tj180.Bean;

public class QueueResBodyBean {
	private String status="500";//	返回状态码	正常：200，参数异常400，未知错误: 500
	private String reserveId="";//	体检预约号
	private String queueId="";//	排队号
	private String errorinfo="";//	错误信息提示 	正常：””，异常：中文提示
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
	public String getQueueId() {
		return queueId;
	}
	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

	
}
