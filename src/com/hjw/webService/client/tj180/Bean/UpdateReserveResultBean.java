package com.hjw.webService.client.tj180.Bean;

public class UpdateReserveResultBean {
	private String projectId="";//	结果项目编码
	private String projectName="";//		结果项目名称
	private String deptId="";//		所属科室
	private String result="";//		结果值
	private String hint="";//		提示 如：偏高、偏低
	private String ifAbnormity="";//		是否异常	正常：0，异常：1
	private String unionProjectId="";//		组合项目编码
	private String examineDoctor="";//		检查医生	中文名如：吴XX
	private String examineDate="";//		检查时间	Yyyy-mm-dd hh24:mi:ss
	private String units="";//		单位
	private String printContext="";//		范围值 如：0.1-0.3
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getIfAbnormity() {
		return ifAbnormity;
	}
	public void setIfAbnormity(String ifAbnormity) {
		this.ifAbnormity = ifAbnormity;
	}
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public String getExamineDoctor() {
		return examineDoctor;
	}
	public void setExamineDoctor(String examineDoctor) {
		this.examineDoctor = examineDoctor;
	}
	public String getExamineDate() {
		return examineDate;
	}
	public void setExamineDate(String examineDate) {
		this.examineDate = examineDate;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getPrintContext() {
		return printContext;
	}
	public void setPrintContext(String printContext) {
		this.printContext = printContext;
	}	
}
