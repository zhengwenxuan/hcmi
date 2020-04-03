package com.hjw.webService.client.bdyx.bean.pacs.res;

public class DiagnosisInfo {

	private String diagnosesTypeCode = "";//诊断类别编码
	private String diagnosesTypeName = "";//诊断类别名称
	private String icdCode = "";//临床诊断编码(疾病编码)
	private String icdName = "";//临床诊断(疾病名称)
	
	public String getDiagnosesTypeCode() {
		return diagnosesTypeCode;
	}
	public String getDiagnosesTypeName() {
		return diagnosesTypeName;
	}
	public String getIcdCode() {
		return icdCode;
	}
	public String getIcdName() {
		return icdName;
	}
	public void setDiagnosesTypeCode(String diagnosesTypeCode) {
		this.diagnosesTypeCode = diagnosesTypeCode;
	}
	public void setDiagnosesTypeName(String diagnosesTypeName) {
		this.diagnosesTypeName = diagnosesTypeName;
	}
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}
	public void setIcdName(String icdName) {
		this.icdName = icdName;
	}
}
