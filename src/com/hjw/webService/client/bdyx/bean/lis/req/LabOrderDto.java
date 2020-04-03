package com.hjw.webService.client.bdyx.bean.lis.req;

import com.hjw.util.DateTimeUtil;

public class LabOrderDto {
	private String orderLid = "";//医嘱号
	private String itemCode = "";//检验项目编码
	private String itemName = "";//检验项目名称
	private String level = "";//检验项目优先级别
	private String orderStartTime = DateTimeUtil.getDateTimes();//医嘱开始时间
	private String orderEndTime = "";//医嘱停止时间
	private String orderExecFreqCode = "ONCE";//医嘱执行频率编码
	private String orderExecFreqName = "ONCE";//医嘱执行频率名称
	private String labDescriptionCode = "";//检验描述编码
	private String labDescriptionName = "";//检验描述名称
	private String position = "";//采集部位
	private String specimenid = "";//标本条码号
	private String sampleType = "";//标本类型编码
	private String sampleTypeName = "";//标本类型名称
	private String itemContainerID = "";//测试项目容器类型
	private String itemContainerName = "";//测试项目容器名称
	private String deliveryId = "";//执行科室编码
	private String deliveryName = "检验科";//执行科室名称
	private String skinTestFlag = "";//是否皮试
	private String urgentFlag = "";//是否加急
	private String medViewFlag = "";//是否药观
	private String paymentType = "";//先诊疗后付费类型编码
	private String paymentTypeName = "";//先诊疗后付费类型名称
	private String payFlag = "";//收费状态标识
	private String hisStatus = "";//HIS执行状态
	private String createDate = "";//业务操作时间
	private String orderTimeTypeCode = "0";//医嘱时间类型编码
	private String orderTimeTypeName = "临时";//医嘱时间类型名称
	private String clinicalPathwayCode = "";//临床路径项目编号
	private String clinicalPathwayNumber = "";//临床路径项目序号
	private String itemPrice = "";//测试项目价格
	private String materialPrice = "";//耗材价格 
	private String remarksType = "";//备注类型
	private String sampleRequirement = "";//标本要求
	
	public String getOrderLid() {
		return orderLid;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getLevel() {
		return level;
	}
	public String getOrderStartTime() {
		return orderStartTime;
	}
	public String getOrderEndTime() {
		return orderEndTime;
	}
	public String getOrderExecFreqCode() {
		return orderExecFreqCode;
	}
	public String getOrderExecFreqName() {
		return orderExecFreqName;
	}
	public String getLabDescriptionCode() {
		return labDescriptionCode;
	}
	public String getLabDescriptionName() {
		return labDescriptionName;
	}
	public String getPosition() {
		return position;
	}
	public String getSpecimenid() {
		return specimenid;
	}
	public String getSampleType() {
		return sampleType;
	}
	public String getSampleTypeName() {
		return sampleTypeName;
	}
	public String getItemContainerID() {
		return itemContainerID;
	}
	public String getItemContainerName() {
		return itemContainerName;
	}
	public String getDeliveryId() {
		return deliveryId;
	}
	public String getDeliveryName() {
		return deliveryName;
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
	public String getPayFlag() {
		return payFlag;
	}
	public String getHisStatus() {
		return hisStatus;
	}
	public String getCreateDate() {
		return createDate;
	}
	public String getOrderTimeTypeCode() {
		return orderTimeTypeCode;
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
	public String getItemPrice() {
		return itemPrice;
	}
	public String getMaterialPrice() {
		return materialPrice;
	}
	public String getRemarksType() {
		return remarksType;
	}
	public String getSampleRequirement() {
		return sampleRequirement;
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
	public void setLevel(String level) {
		this.level = level;
	}
	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}
	public void setOrderExecFreqCode(String orderExecFreqCode) {
		this.orderExecFreqCode = orderExecFreqCode;
	}
	public void setOrderExecFreqName(String orderExecFreqName) {
		this.orderExecFreqName = orderExecFreqName;
	}
	public void setLabDescriptionCode(String labDescriptionCode) {
		this.labDescriptionCode = labDescriptionCode;
	}
	public void setLabDescriptionName(String labDescriptionName) {
		this.labDescriptionName = labDescriptionName;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public void setSpecimenid(String specimenid) {
		this.specimenid = specimenid;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public void setSampleTypeName(String sampleTypeName) {
		this.sampleTypeName = sampleTypeName;
	}
	public void setItemContainerID(String itemContainerID) {
		this.itemContainerID = itemContainerID;
	}
	public void setItemContainerName(String itemContainerName) {
		this.itemContainerName = itemContainerName;
	}
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
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
	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}
	public void setHisStatus(String hisStatus) {
		this.hisStatus = hisStatus;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public void setOrderTimeTypeCode(String orderTimeTypeCode) {
		this.orderTimeTypeCode = orderTimeTypeCode;
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
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public void setMaterialPrice(String materialPrice) {
		this.materialPrice = materialPrice;
	}
	public void setRemarksType(String remarksType) {
		this.remarksType = remarksType;
	}
	public void setSampleRequirement(String sampleRequirement) {
		this.sampleRequirement = sampleRequirement;
	}
}
