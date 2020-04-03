package com.hjw.webService.service.bean;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class SgtZInMsg {
	
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
	 
	 @JsonIgnore 
	public String getPatientID() {
		return PatientID;
	}
	 
	 @JsonIgnore 
	public void setPatientID(String patientID) {
		PatientID = patientID;
	}
	 
	 @JsonIgnore 
	public String getScanID() {
		return ScanID;
	}
	 
	 @JsonIgnore 
	public void setScanID(String scanID) {
		ScanID = scanID;
	}
	 
	 @JsonIgnore 
	public String getName() {
		return Name;
	}
	 
	 @JsonIgnore 
	public void setName(String name) {
		Name = name;
	}
	 
	 @JsonIgnore 
	public String getGender() {
		return Gender;
	}
	 
	 @JsonIgnore 
	public void setGender(String gender) {
		Gender = gender;
	}
	 
	 @JsonIgnore 
	public String getBirthday() {
		return Birthday;
	}
	 
	 @JsonIgnore 
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}	 
	 
	public static void main(String[] args)throws Exception {
		SgtZInMsg s = new SgtZInMsg();
		s.setBirthday("1212125");
		ObjectMapper mapper = new ObjectMapper();   
        String json=mapper.writeValueAsString(s); 
        System.out.println(json);
	}
}
