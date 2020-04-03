package com.hjw.webService.client.bdyx.bean.del;

import com.hjw.util.DateTimeUtil;

public class Order {

	private String orderLid = "";//医嘱号
	private String orderType = "";//医嘱的类型编码
	private String orderTypeName = "";//医嘱的类型名称
	private String sampleCode = "";//标本号
	private String cancelOrStopTime = DateTimeUtil.getDateTimes();//撤销或停止时间
	private String cancelOrStopPerson = "01170";//撤销或停止人编码
	private String cancelOrStopPersonName = "樊军";//撤销或停止人姓名
	private String execDept = "";//医嘱执行科室编码
	private String execDeptName = "";//医嘱执行科室名称
	private String cancelReason = "";//医嘱撤消原因
	private String mutexesOrderLid = "";//互斥医嘱号
	private String mutexesOrderType = "";//互斥医嘱类别编码
	private String mutexesOrderTypeName = "";//互斥医嘱类别名称
	private String paymentTypeCode = "";//先诊疗后付费类型编码
	private String paymentTypeName = "";//先诊疗后付费类型名称
	private String payFlag = "";//收费状态标识
	private String hisStatus = "";//HIS执行状态
	private String createDate = "";//业务操作时间
	
	public String getOrderLid() {
		return orderLid;
	}
	public String getOrderType() {
		return orderType;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public String getSampleCode() {
		return sampleCode;
	}
	public String getCancelOrStopTime() {
		return cancelOrStopTime;
	}
	public String getCancelOrStopPerson() {
		return cancelOrStopPerson;
	}
	public String getCancelOrStopPersonName() {
		return cancelOrStopPersonName;
	}
	public String getExecDept() {
		return execDept;
	}
	public String getExecDeptName() {
		return execDeptName;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public String getMutexesOrderLid() {
		return mutexesOrderLid;
	}
	public String getMutexesOrderType() {
		return mutexesOrderType;
	}
	public String getMutexesOrderTypeName() {
		return mutexesOrderTypeName;
	}
	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}
	public String getPaymentTypeName() {
		return paymentTypeName;
	}
	public String getPayFlag() {
		return payFlag;
	}
	public String getHisStatus() {
		return hisStatus;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setOrderLid(String orderLid) {
		this.orderLid = orderLid;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public void setSampleCode(String sampleCode) {
		this.sampleCode = sampleCode;
	}
	public void setCancelOrStopTime(String cancelOrStopTime) {
		this.cancelOrStopTime = cancelOrStopTime;
	}
	public void setCancelOrStopPerson(String cancelOrStopPerson) {
		this.cancelOrStopPerson = cancelOrStopPerson;
	}
	public void setCancelOrStopPersonName(String cancelOrStopPersonName) {
		this.cancelOrStopPersonName = cancelOrStopPersonName;
	}
	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}
	public void setExecDeptName(String execDeptName) {
		this.execDeptName = execDeptName;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public void setMutexesOrderLid(String mutexesOrderLid) {
		this.mutexesOrderLid = mutexesOrderLid;
	}
	public void setMutexesOrderType(String mutexesOrderType) {
		this.mutexesOrderType = mutexesOrderType;
	}
	public void setMutexesOrderTypeName(String mutexesOrderTypeName) {
		this.mutexesOrderTypeName = mutexesOrderTypeName;
	}
	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public void setHisStatus(String hisStatus) {
		this.hisStatus = hisStatus;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
