package com.hjw.webService.service.bean;

import org.codehaus.jackson.annotate.JsonProperty;

public class SgtZRequestInMsg {
	@JsonProperty("DeviceID")
	private String DeviceID="";
	
	@JsonProperty("PatientID")
	 private String PatientID=""; //病人ID 
	
	@JsonProperty("ScanID")
	 private String ScanID="";//体检编号, //扫描的条码号 
	
	@JsonProperty("Name")
	 private String Name="";//:"张三", //病人姓名 
	
	@JsonProperty("Gender")
	 private String Gender="";//女”， //病人性别
	
	@JsonProperty("Birthday")
	 private String Birthday="";//2017/01/01” //病人出生年
	
	@JsonProperty("Height")
	 private double Height;//身高 
	
	@JsonProperty("HeightUnit")
	 private String HeightUnit="cm";//”:”cm”, //身高单位 
	
	@JsonProperty("Weight")
	 private double Weight;//”:”52”, //体重 
	
	@JsonProperty("WeightUnit")
	 private String WeightUnit="kg";//, //体重单位 
	
	@JsonProperty("CheckDate")
	 private String CheckDate;//”:”2017/01/01”, //测量时间
	
	@JsonProperty("Temperatrue")
	 private String Temperatrue="";// //温度 
	
	@JsonProperty("BMI")
	 private double BMI;//BMI
	 
	public String getPatientID() {
		return PatientID;
	}
	public void setPatientID(String patientID) {
		PatientID = patientID;
	}
	public String getScanID() {
		return ScanID;
	}
	public void setScanID(String scanID) {
		ScanID = scanID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getBirthday() {
		return Birthday;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	public double getHeight() {
		return Height;
	}
	public void setHeight(double height) {
		Height = height;
	}
	public String getHeightUnit() {
		return HeightUnit;
	}
	public void setHeightUnit(String heightUnit) {
		HeightUnit = heightUnit;
	}
	public double getWeight() {
		return Weight;
	}
	public void setWeight(double weight) {
		Weight = weight;
	}
	public String getWeightUnit() {
		return WeightUnit;
	}
	public void setWeightUnit(String weightUnit) {
		WeightUnit = weightUnit;
	}
	public String getCheckDate() {
		return CheckDate;
	}
	public void setCheckDate(String checkDate) {
		CheckDate = checkDate;
	}
	public String getTemperatrue() {
		return Temperatrue;
	}
	public void setTemperatrue(String temperatrue) {
		Temperatrue = temperatrue;
	}
	public double getBMI() {
		return BMI;
	}
	public void setBMI(double bMI) {
		BMI = bMI;
	}
	
	 
}
