package com.hjw.webService.client.bdyx.bean.pacs.req;

import com.hjw.util.DateTimeUtil;

public class ExaminationOrderDto {

	private String orderLid = "";//医嘱号
	private String itemCode = "";//检查项目编码
	private String itemName = "";//检查项目名称
	private String orderDescribe = "";//医嘱描述
	private String orderStartTime = DateTimeUtil.getDateTimes();//医嘱开始时间
	private String orderEndTime = "";//医嘱停止时间
	private String executionFrequency = "ONCE";//医嘱执行频率编码
	private String execFreqName = "ONCE";//医嘱执行频率名称
	private String examMethodCode = "";//检查方法编码
	private String examMethodName = "";//检查方法名
	private String itemClassCode = "";//检查类型编码
	private String itemClassName = "";//检查类型名称
	private String regionCode = "";//检查部位编码
	private String regionName = "";//检查部位名称
	private String skinTestFlag = "";//是否皮试
	private String urgentFlag = "";//是否加急
	private String medViewFlag = "";//是否药观
	private String paymentType = "";//先诊疗后付费类型编码
	private String paymentTypeName = "";//先诊疗后付费类型名称
	private String chargeStatusCode = "";//收费状态标识
	private String HISExecutionStatus = "";//HIS执行状态
	private String handleTime = "";//业务操作时间
	private String orderTimeType = "0";//医嘱时间类型编码
	private String orderTimeTypeName = "临时";//医嘱时间类型名称
	private String clinicalPathwayCode = "";//临床路径项目编号
	private String clinicalPathwayNumber = "";//临床路径项目序号
	
	public String getOrderLid() {
		return orderLid;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getOrderDescribe() {
		return orderDescribe;
	}
	public String getOrderStartTime() {
		return orderStartTime;
	}
	public String getOrderEndTime() {
		return orderEndTime;
	}
	public String getExecutionFrequency() {
		return executionFrequency;
	}
	public String getExecFreqName() {
		return execFreqName;
	}
	public String getExamMethodCode() {
		return examMethodCode;
	}
	public String getExamMethodName() {
		return examMethodName;
	}
	public String getItemClassCode() {
		return itemClassCode;
	}
	public String getItemClassName() {
		return itemClassName;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public String getRegionName() {
		return regionName;
	}
	public String getSkinTestFlag() {
		return skinTestFlag;
	}
	public String getUrgentFlag() {
		return urgentFlag;
	}
	public String getMedViewFlag() {
		return medViewFlag;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public String getPaymentTypeName() {
		return paymentTypeName;
	}
	public String getChargeStatusCode() {
		return chargeStatusCode;
	}
	public String getHISExecutionStatus() {
		return HISExecutionStatus;
	}
	public String getHandleTime() {
		return handleTime;
	}
	public String getOrderTimeType() {
		return orderTimeType;
	}
	public String getOrderTimeTypeName() {
		return orderTimeTypeName;
	}
	public String getClinicalPathwayCode() {
		return clinicalPathwayCode;
	}
	public String getClinicalPathwayNumber() {
		return clinicalPathwayNumber;
	}
	public void setOrderLid(String orderLid) {
		this.orderLid = orderLid;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setOrderDescribe(String orderDescribe) {
		this.orderDescribe = orderDescribe;
	}
	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}
	public void setExecutionFrequency(String executionFrequency) {
		this.executionFrequency = executionFrequency;
	}
	public void setExecFreqName(String execFreqName) {
		this.execFreqName = execFreqName;
	}
	public void setExamMethodCode(String examMethodCode) {
		this.examMethodCode = examMethodCode;
	}
	public void setExamMethodName(String examMethodName) {
		this.examMethodName = examMethodName;
	}
	public void setItemClassCode(String itemClassCode) {
		this.itemClassCode = itemClassCode;
	}
	public void setItemClassName(String itemClassName) {
		this.itemClassName = itemClassName;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public void setSkinTestFlag(String skinTestFlag) {
		this.skinTestFlag = skinTestFlag;
	}
	public void setUrgentFlag(String urgentFlag) {
		this.urgentFlag = urgentFlag;
	}
	public void setMedViewFlag(String medViewFlag) {
		this.medViewFlag = medViewFlag;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public void setPaymentTypeName(String paymentTypeName) {
		this.paymentTypeName = paymentTypeName;
	}
	public void setChargeStatusCode(String chargeStatusCode) {
		this.chargeStatusCode = chargeStatusCode;
	}
	public void setHISExecutionStatus(String hISExecutionStatus) {
		HISExecutionStatus = hISExecutionStatus;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	public void setOrderTimeType(String orderTimeType) {
		this.orderTimeType = orderTimeType;
	}
	public void setOrderTimeTypeName(String orderTimeTypeName) {
		this.orderTimeTypeName = orderTimeTypeName;
	}
	public void setClinicalPathwayCode(String clinicalPathwayCode) {
		this.clinicalPathwayCode = clinicalPathwayCode;
	}
	public void setClinicalPathwayNumber(String clinicalPathwayNumber) {
		this.clinicalPathwayNumber = clinicalPathwayNumber;
	}
}
