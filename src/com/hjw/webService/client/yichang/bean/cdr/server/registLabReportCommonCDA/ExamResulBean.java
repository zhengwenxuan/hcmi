package com.hjw.webService.client.yichang.bean.cdr.server.registLabReportCommonCDA;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "examResultList")  
@XmlType(propOrder = {})
public class ExamResulBean {

	@XmlElement
	private String	detailExamProjectCode	 = "";//	明细测试项目编码	是
	@XmlElement
	private String	detailExamProjectName	 = "";//	明细测试项目中文名	是
	@XmlElement
	private String	orderItemCode	 = "";//	医嘱项目编码	
	@XmlElement
	private String	orderItemName	 = "";//	医嘱项目名称	
	@XmlElement
	private String	examDeviceCode	 = "";//	测试设备编码	
	@XmlElement
	private String	examMethod	 = "";//	检验方法代码	
	@XmlElement
	private String	sco	 = "";//
	@XmlElement
	private String	odValue	 = "";//	OD值	
	@XmlElement
	private String	cutOff	 = "";//
	@XmlElement
	private String	result	 = "";//	结果	
	@XmlElement
	private String	resultDate	 = "";//	结果时间	
	@XmlElement
	private String	unit	 = "";//	单位	
	@XmlElement
	private String	referenceRange	 = "";//	参考范围	
	@XmlElement
	private String	abnormalFlag	 = "";//	异常标志代码	
	@XmlElement
	private String	examApplyNo	 = "";//	检验申请单号	
	@XmlElement
	private String	examResult	 = "";//	检验结果（定量、定性）	
	@XmlElement
	private String	criticalValuesReference	 = "";//	危急参考值	
	@XmlElement
	private String	criticalValuesFlag	 = "";//	危急值标志	是
	@XmlElement
	private String	reviewFlag	 = "";//	复查标志	
	@XmlElement
	private String	printOrder	 = "";//	打印顺序	
	
	public String getDetailExamProjectCode() {
		return detailExamProjectCode;
	}
	public String getDetailExamProjectName() {
		return detailExamProjectName;
	}
	public String getOrderItemCode() {
		return orderItemCode;
	}
	public String getOrderItemName() {
		return orderItemName;
	}
	public String getExamDeviceCode() {
		return examDeviceCode;
	}
	public String getExamMethod() {
		return examMethod;
	}
	public String getSco() {
		return sco;
	}
	public String getOdValue() {
		return odValue;
	}
	public String getCutOff() {
		return cutOff;
	}
	public String getResult() {
		return result;
	}
	public String getResultDate() {
		return resultDate;
	}
	public String getUnit() {
		return unit;
	}
	public String getReferenceRange() {
		return referenceRange;
	}
	public String getAbnormalFlag() {
		return abnormalFlag;
	}
	public String getExamApplyNo() {
		return examApplyNo;
	}
	public String getExamResult() {
		return examResult;
	}
	public String getCriticalValuesReference() {
		return criticalValuesReference;
	}
	public String getCriticalValuesFlag() {
		return criticalValuesFlag;
	}
	public String getReviewFlag() {
		return reviewFlag;
	}
	public String getPrintOrder() {
		return printOrder;
	}
	public void setDetailExamProjectCode(String detailExamProjectCode) {
		this.detailExamProjectCode = detailExamProjectCode;
	}
	public void setDetailExamProjectName(String detailExamProjectName) {
		this.detailExamProjectName = detailExamProjectName;
	}
	public void setOrderItemCode(String orderItemCode) {
		this.orderItemCode = orderItemCode;
	}
	public void setOrderItemName(String orderItemName) {
		this.orderItemName = orderItemName;
	}
	public void setExamDeviceCode(String examDeviceCode) {
		this.examDeviceCode = examDeviceCode;
	}
	public void setExamMethod(String examMethod) {
		this.examMethod = examMethod;
	}
	public void setSco(String sco) {
		this.sco = sco;
	}
	public void setOdValue(String odValue) {
		this.odValue = odValue;
	}
	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setResultDate(String resultDate) {
		this.resultDate = resultDate;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setReferenceRange(String referenceRange) {
		this.referenceRange = referenceRange;
	}
	public void setAbnormalFlag(String abnormalFlag) {
		this.abnormalFlag = abnormalFlag;
	}
	public void setExamApplyNo(String examApplyNo) {
		this.examApplyNo = examApplyNo;
	}
	public void setExamResult(String examResult) {
		this.examResult = examResult;
	}
	public void setCriticalValuesReference(String criticalValuesReference) {
		this.criticalValuesReference = criticalValuesReference;
	}
	public void setCriticalValuesFlag(String criticalValuesFlag) {
		this.criticalValuesFlag = criticalValuesFlag;
	}
	public void setReviewFlag(String reviewFlag) {
		this.reviewFlag = reviewFlag;
	}
	public void setPrintOrder(String printOrder) {
		this.printOrder = printOrder;
	}
}
