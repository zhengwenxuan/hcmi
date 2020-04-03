package com.hjw.webService.client.bdyx.bean.pacs.req;

public class Diagnose {

	private String diagnosesTypeCode = "";//诊断类别编码
	private String diagnosesTypeName = "";//诊断类别名称
	private String diagnosisDate = "";//诊断日期
	private String icdCode = "";//疾病编码
	private String diagnosis = "";//疾病名称
	
	public String getDiagnosesTypeCode() {
		return diagnosesTypeCode;
	}
	public String getDiagnosesTypeName() {
		return diagnosesTypeName;
	}
	public String getDiagnosisDate() {
		return diagnosisDate;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosesTypeCode(String diagnosesTypeCode) {
		this.diagnosesTypeCode = diagnosesTypeCode;
	}
	public void setDiagnosesTypeName(String diagnosesTypeName) {
		this.diagnosesTypeName = diagnosesTypeName;
	}
	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
}
