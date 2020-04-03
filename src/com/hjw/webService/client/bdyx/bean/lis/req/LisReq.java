package com.hjw.webService.client.bdyx.bean.lis.req;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;

public class LisReq {

	private String requestNo = "";//检验申请单编号
	private String orderType = "3";//医嘱类型
	private String orderTypeName = "化验类";//医嘱类型名称
	private String requestDate = DateTimeUtil.getDateTimes();//检验申请日期
	private String isSecret = "";//是否隐私
	private List<LabOrderDto> labOrderDto = new ArrayList<>();//1..n 医嘱项目信息
	private String rportRemarksTypeCode = "";//报告备注类型
	private String reportDesc = "";//报告备注
	private String medicalObs = "";//药观编码
	private String medicalObsName = "";//药观名称
	
	public String getRequestNo() {
		return requestNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public String getIsSecret() {
		return isSecret;
	}
	public List<LabOrderDto> getLabOrderDto() {
		return labOrderDto;
	}
	public String getRportRemarksTypeCode() {
		return rportRemarksTypeCode;
	}
	public String getReportDesc() {
		return reportDesc;
	}
	public String getMedicalObs() {
		return medicalObs;
	}
	public String getMedicalObsName() {
		return medicalObsName;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public void setIsSecret(String isSecret) {
		this.isSecret = isSecret;
	}
	public void setLabOrderDto(List<LabOrderDto> labOrderDto) {
		this.labOrderDto = labOrderDto;
	}
	public void setRportRemarksTypeCode(String rportRemarksTypeCode) {
		this.rportRemarksTypeCode = rportRemarksTypeCode;
	}
	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	public void setMedicalObs(String medicalObs) {
		this.medicalObs = medicalObs;
	}
	public void setMedicalObsName(String medicalObsName) {
		this.medicalObsName = medicalObsName;
	}
}
