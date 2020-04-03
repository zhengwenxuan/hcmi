package com.hjw.webService.client.jsjg.bean.out;

public class ApplyOrderInfo {

	private String ApplyOrderNo = "";//申请单号
	private String ApplyDeptCode = "";//申请科室代码
	private String ApplyDeptName = "";//申请科室名称
	private String DocCode = "";//开单医生工号
	private String DocName = "";//开单医生姓名
	private String ApplyTime = "";//申请时间	格式：yyyy-MM-dd HH:mm:ss
	private String ApplyOrderType = "";//申请单类型
	private String Diagnosis = "";//临床诊断
	private String DiagnosisCode = "";//诊断代码
	private String DiagnosisName = "";//诊断名称
	private String EspecialCondition = "";//特殊信息
	private String InspectedInfo = "";//已检信息
	private String Infection = "";//传染信息
	private Items Items = new Items();//项目集合
	
	public String getApplyOrderNo() {
		return ApplyOrderNo;
	}
	public String getApplyDeptCode() {
		return ApplyDeptCode;
	}
	public String getApplyDeptName() {
		return ApplyDeptName;
	}
	public String getDocCode() {
		return DocCode;
	}
	public String getDocName() {
		return DocName;
	}
	public String getApplyTime() {
		return ApplyTime;
	}
	public String getApplyOrderType() {
		return ApplyOrderType;
	}
	public String getDiagnosis() {
		return Diagnosis;
	}
	public String getDiagnosisCode() {
		return DiagnosisCode;
	}
	public String getDiagnosisName() {
		return DiagnosisName;
	}
	public String getEspecialCondition() {
		return EspecialCondition;
	}
	public String getInspectedInfo() {
		return InspectedInfo;
	}
	public String getInfection() {
		return Infection;
	}
	public Items getItems() {
		return Items;
	}
	public void setApplyOrderNo(String applyOrderNo) {
		ApplyOrderNo = applyOrderNo;
	}
	public void setApplyDeptCode(String applyDeptCode) {
		ApplyDeptCode = applyDeptCode;
	}
	public void setApplyDeptName(String applyDeptName) {
		ApplyDeptName = applyDeptName;
	}
	public void setDocCode(String docCode) {
		DocCode = docCode;
	}
	public void setDocName(String docName) {
		DocName = docName;
	}
	public void setApplyTime(String applyTime) {
		ApplyTime = applyTime;
	}
	public void setApplyOrderType(String applyOrderType) {
		ApplyOrderType = applyOrderType;
	}
	public void setDiagnosis(String diagnosis) {
		Diagnosis = diagnosis;
	}
	public void setDiagnosisCode(String diagnosisCode) {
		DiagnosisCode = diagnosisCode;
	}
	public void setDiagnosisName(String diagnosisName) {
		DiagnosisName = diagnosisName;
	}
	public void setEspecialCondition(String especialCondition) {
		EspecialCondition = especialCondition;
	}
	public void setInspectedInfo(String inspectedInfo) {
		InspectedInfo = inspectedInfo;
	}
	public void setInfection(String infection) {
		Infection = infection;
	}
	public void setItems(Items items) {
		Items = items;
	}
	
}
