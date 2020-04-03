package com.hjw.webService.client.bdyx.bean.pacs.req;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;

public class ExaminationApplication {

	private String requestNo = "";//检查申请单编号
	private String orderType = "2";//医嘱类型
	private String orderTypeName = "检查类";//医嘱类型名称
	private String requestDetails = "";//申请单详细内容
	private String requestDate = DateTimeUtil.getDateTimes();//检查申请日期
	private String sampleNo = "";//标本号
	private String sampleType = "";//标本类别编码
	private String sampleRequirement = "";//标本要求
	private String executeTime = "";//执行时间
	private String executiveDeptName = "";//执行科室名称
	private String executiveDept = "";//执行科室编码
	private String examNotice = "";//申请注意事项
	private List<ExaminationOrderDto> examinationOrderDtos = new ArrayList<>();//医嘱项目信息
	
	public String getRequestNo() {
		return requestNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public String getOrderTypeName() {
		return orderTypeName;
	}
	public String getRequestDetails() {
		return requestDetails;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public String getSampleNo() {
		return sampleNo;
	}
	public String getSampleType() {
		return sampleType;
	}
	public String getSampleRequirement() {
		return sampleRequirement;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public String getExecutiveDeptName() {
		return executiveDeptName;
	}
	public String getExecutiveDept() {
		return executiveDept;
	}
	public List<ExaminationOrderDto> getExaminationOrderDtos() {
		return examinationOrderDtos;
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
	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public void setSampleRequirement(String sampleRequirement) {
		this.sampleRequirement = sampleRequirement;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public void setExecutiveDeptName(String executiveDeptName) {
		this.executiveDeptName = executiveDeptName;
	}
	public void setExecutiveDept(String executiveDept) {
		this.executiveDept = executiveDept;
	}
	public void setExaminationOrderDtos(List<ExaminationOrderDto> examinationOrderDtos) {
		this.examinationOrderDtos = examinationOrderDtos;
	}
	public String getExamNotice() {
		return examNotice;
	}
	public void setExamNotice(String examNotice) {
		this.examNotice = examNotice;
	}
}
