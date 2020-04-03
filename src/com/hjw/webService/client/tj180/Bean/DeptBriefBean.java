package com.hjw.webService.client.tj180.Bean;

public class DeptBriefBean {
	private String deptId="";//	所属科室
	private String brief="";//	科室小结
	private String ifAbnormity="";//	是否异常	正常：0，异常：1
	private String examineDoctor="";//	检查医生	登录用户名如：WYY
	private String examineDate="";//	检查时间	Yyyy-mm-dd hh24:mi:ss
	private String editor="";//	编辑人 登录用户名如：WYY
	private String editDate="";//	编辑时间 	Yyyy-mm-dd hh24:mi:ss
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getIfAbnormity() {
		return ifAbnormity;
	}
	public void setIfAbnormity(String ifAbnormity) {
		this.ifAbnormity = ifAbnormity;
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
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditDate() {
		return editDate;
	}
	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	
}
