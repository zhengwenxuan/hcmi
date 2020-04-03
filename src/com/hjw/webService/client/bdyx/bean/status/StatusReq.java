package com.hjw.webService.client.bdyx.bean.status;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class StatusReq {

	private String newUpFlag = "";//消息交互类型
	private String sender = "";//发送者
	private String patientDomain = "";//域ID
	private String patientLid = "";//患者ID
	private String visitTypeCode = "";//就诊类别编码
	private String visitTypeName = "";//就诊类别名称
	private String visitTimes = "";//就诊次数
	private String visitOrdNo = "";//就诊流水号
	private String visitNo = "";//就诊号
	private String operationDate = "";//操作时间
	private String operationCode = "";//操作人编码
	private String operationName = "";//操作人姓名
	private String deptCode = "";//病人科室编码
	private String deptName = "";//病人科室名称
	private String orgCode = "";//医疗机构代码
	private String orgName = "";//医疗机构名称
	private String execDept = "";//执行科室编码
	private String execDeptName = "";//执行科室名称
	private List<OrderStatusInf> orderStatusInf = new ArrayList<>();//1..n医嘱信息  可循环
	
	public String getNewUpFlag() {
		return newUpFlag;
	}
	public String getSender() {
		return sender;
	}
	public String getPatientDomain() {
		return patientDomain;
	}
	public String getPatientLid() {
		return patientLid;
	}
	public String getVisitTypeCode() {
		return visitTypeCode;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public String getOperationDate() {
		return operationDate;
	}
	public String getOperationCode() {
		return operationCode;
	}
	public String getOperationName() {
		return operationName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public String getExecDept() {
		return execDept;
	}
	public String getExecDeptName() {
		return execDeptName;
	}
	public void setNewUpFlag(String newUpFlag) {
		this.newUpFlag = newUpFlag;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public void setPatientDomain(String patientDomain) {
		this.patientDomain = patientDomain;
	}
	public void setPatientLid(String patientLid) {
		this.patientLid = patientLid;
	}
	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}
	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setExecDept(String execDept) {
		this.execDept = execDept;
	}
	public void setExecDeptName(String execDeptName) {
		this.execDeptName = execDeptName;
	}
	public List<OrderStatusInf> getOrderStatusInf() {
		return orderStatusInf;
	}
	public void setOrderStatusInf(List<OrderStatusInf> orderStatusInf) {
		this.orderStatusInf = orderStatusInf;
	}
	
	public static void main(String[] args) {
		String str = "";
		StatusReq statusReq = new Gson().fromJson(str, StatusReq.class);
		System.out.println(statusReq.getDeptName());
		System.out.println(statusReq.getOrderStatusInf().get(0).getOrderTypeName());
	}
}
