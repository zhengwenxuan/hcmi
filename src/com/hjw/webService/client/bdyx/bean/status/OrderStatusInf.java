package com.hjw.webService.client.bdyx.bean.status;

public class OrderStatusInf {

	private String orderSeq = "";//医嘱序号
	private String orderLid = "";//医嘱号
	private String requestNo = "";//申请单号
	private String reportNo = "";//报告号
	private String studyInstanceUID = "";//StudyInstanceUID
	private String orderType = "";//医嘱类型编码
	private String orderTypeName = "";//医嘱类型名称
	private String sampleCode = "";//标本条码号
	private String gatherTime = "";//采集时间
	private String gatherPersonCode = "";//采集人Id
	private String gatherPersonName = "";//采集人姓名
	private String reason = "";//原因
	private String orderStatus = "";//医嘱执行状态 
	private String orderStatusName = "";//医嘱执行状态名称
	
	public String getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}
	public String getOrderLid() {
		return orderLid;
	}
	public void setOrderLid(String orderLid) {
		this.orderLid = orderLid;
	}
	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getStudyInstanceUID() {
		return studyInstanceUID;
	}
	public void setStudyInstanceUID(String studyInstanceUID) {
		this.studyInstanceUID = studyInstanceUID;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public String getSampleCode() {
		return sampleCode;
	}
	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}
	public String getGatherTime() {
		return gatherTime;
	}
	public void setGatherTime(String gatherTime) {
		this.gatherTime = gatherTime;
	}
	public String getGatherPersonCode() {
		return gatherPersonCode;
	}
	public void setGatherPersonCode(String gatherPersonCode) {
		this.gatherPersonCode = gatherPersonCode;
	}
	public String getGatherPersonName() {
		return gatherPersonName;
	}
	public void setGatherPersonName(String gatherPersonName) {
		this.gatherPersonName = gatherPersonName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderStatusName() {
		return orderStatusName;
	}
	public void setOrderStatusName(String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}
}
