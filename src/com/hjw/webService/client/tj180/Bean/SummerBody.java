package com.hjw.webService.client.tj180.Bean;

public class SummerBody {
	private String reserveId = "";// 体检预约号
	private String examineDoctor = "";// 总检医生 登录用户名如：WYY
	private String examineDate = "";// 总检时间 Yyyy-mm-dd hh24:mi:ss
	private String diagnosisDesc = "";// 综述内容
	private String adviceDesc = "";// 建议内容
	private String lastDiagnosis = "";// 最终诊断
	private String editor = "";// 审核医生 登录用户名如：WYY
	private String editDate = "";// 审核时间 Yyyy-mm-dd hh24:mi:ss
	private String joinDate="";//报道日期	

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getReserveId() {
		return reserveId;
	}

	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
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

	public String getDiagnosisDesc() {
		return diagnosisDesc;
	}

	public void setDiagnosisDesc(String diagnosisDesc) {
		this.diagnosisDesc = diagnosisDesc;
	}

	public String getAdviceDesc() {
		return adviceDesc;
	}

	public void setAdviceDesc(String adviceDesc) {
		this.adviceDesc = adviceDesc;
	}

	public String getLastDiagnosis() {
		return lastDiagnosis;
	}

	public void setLastDiagnosis(String lastDiagnosis) {
		this.lastDiagnosis = lastDiagnosis;
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
