package com.hjw.webService.client.jsjg.bean.in;

public class InParam_RequestFrom {

	private String PatientCode = "";//住院号
	
	private String PatientCodeType = "";//患者编号类型

	public String getPatientCode() {
		return PatientCode;
	}

	public String getPatientCodeType() {
		return PatientCodeType;
	}

	public void setPatientCode(String patientCode) {
		PatientCode = patientCode;
	}

	public void setPatientCodeType(String patientCodeType) {
		PatientCodeType = patientCodeType;
	}

}
