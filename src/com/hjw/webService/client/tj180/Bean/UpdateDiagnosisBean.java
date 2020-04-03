package com.hjw.webService.client.tj180.Bean;

public class UpdateDiagnosisBean {
	private String diagnosisId="";//	诊断编码
	private String diagnosisName="";//	诊断词条名称
	private String diagnosisKey="";//	诊断词条关键字
	private String diagnosisDesc="";//	诊断描述
	private String oftenFlag="";//	是否常用 	默认常用：1，不常用：0
	private String countFlag="";//	是否纳入统计 	默认纳入：1，不纳入：0
	private String adviceDesc="";//	建议内容
	private String deptId="";//	所属职能科室
	private String editor="";//	编辑人	登录用户名如：WYY
	private String editDate="";//	编辑时间 	Yyyy-mm-dd hh24:mi:ss
	
	public String getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(String diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public String getDiagnosisName() {
		return diagnosisName;
	}
	public void setDiagnosisName(String diagnosisName) {
		this.diagnosisName = diagnosisName;
	}
	public String getDiagnosisKey() {
		return diagnosisKey;
	}
	public void setDiagnosisKey(String diagnosisKey) {
		this.diagnosisKey = diagnosisKey;
	}
	public String getDiagnosisDesc() {
		return diagnosisDesc;
	}
	public void setDiagnosisDesc(String diagnosisDesc) {
		this.diagnosisDesc = diagnosisDesc;
	}
	public String getOftenFlag() {
		return oftenFlag;
	}
	public void setOftenFlag(String oftenFlag) {
		this.oftenFlag = oftenFlag;
	}
	public String getCountFlag() {
		return countFlag;
	}
	public void setCountFlag(String countFlag) {
		this.countFlag = countFlag;
	}
	public String getAdviceDesc() {
		return adviceDesc;
	}
	public void setAdviceDesc(String adviceDesc) {
		this.adviceDesc = adviceDesc;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
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
