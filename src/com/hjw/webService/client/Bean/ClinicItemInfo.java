package com.hjw.webService.client.Bean;

public class ClinicItemInfo {
	private String clinicClass="";//	诊疗项目类别
	private String clinicCode="";//	诊疗项目代码
	private String clinicName="";//	诊疗项目名称
	private double price;//	价格
	private String inputCode="";//	拼音输入码
	
	public String getClinicClass() {
		return clinicClass;
	}
	public void setClinicClass(String clinicClass) {
		this.clinicClass = clinicClass;
	}
	public String getClinicCode() {
		return clinicCode;
	}
	public void setClinicCode(String clinicCode) {
		this.clinicCode = clinicCode;
	}
	public String getClinicName() {
		return clinicName;
	}
	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getInputCode() {
		return inputCode;
	}
	public void setInputCode(String inputCode) {
		this.inputCode = inputCode;
	}

}
