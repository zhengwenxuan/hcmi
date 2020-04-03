package com.hjw.webService.client.xhhk.lisbean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CreateApply {

	private String ApplyNo = "";//申请单号 提供给医生、患者的申请单编号
	private String HospitalCode = "HJWTJ";//送检医院 HIS(本院) JMJKFWGC (居民健康服务广场)
	private String HospitalName = "火箭蛙体检";//送检医院名称 HIS、居民健康服务广场
	private String PatNo = "";//门诊号/住院号
	private int PatType = 4;//病人类型 1 门诊、2 住院、3 急诊、4 体检、5 互联网
	private String IdCard = "";//身份证号
	private String PatName = "";//病人姓名
	private int Sex;//性别1 男、2 女
	private String Age = "";//年龄 单位：岁
	private String Birthday = "";//出生日期 1990-02-28
	private String Telephone = "";//手机号
	private String Address = "";//地址
	private String DiagnosisCode = "";//诊断编码
	private String DiagnosisName = "";//诊断名称
	private String DeptCode = "";//科室编码
	private String DeptName = "";//科室名称
	private String DoctCode = "";//医生编码
	private String DoctName = "";//医生名称
	private String TimeStamp = "";//Datetime申请时间 2018-06-23 10:11:12
	private String sample_barcode ="";//条码号
	private String exam_num = "";//体检号
	private String charging_id = "";//体检收费项目代码
	private String FeeType = "";//病人费别
	private String WardCode = "";//病区编码
	private String WardName = "";//病区名称
	private String BedNo = "";//病人床号
	private String SampleNo = "";//样本编码
	private String SampleType = "";//样本类型
	
	
	
	private List<ItemsApplyLisXHHK> Items = new ArrayList<>();
	
	public static void main(String[] args) {
		CreateApply createApply = new CreateApply();
		createApply.getItems().add(new ItemsApplyLisXHHK());
		createApply.getItems().add(new ItemsApplyLisXHHK());
		createApply.getItems().add(new ItemsApplyLisXHHK());
		String json = new Gson().toJson(createApply, CreateApply.class);
		System.out.println(json);
	}

	public String getApplyNo() {
		return ApplyNo;
	}

	public String getHospitalCode() {
		return HospitalCode;
	}

	public String getHospitalName() {
		return HospitalName;
	}

	public String getPatNo() {
		return PatNo;
	}

	public int getPatType() {
		return PatType;
	}

	public String getIdCard() {
		return IdCard;
	}

	public String getPatName() {
		return PatName;
	}

	public int getSex() {
		return Sex;
	}

	public String getAge() {
		return Age;
	}

	public String getBirthday() {
		return Birthday;
	}

	public String getTelephone() {
		return Telephone;
	}

	public String getAddress() {
		return Address;
	}

	public String getDiagnosisCode() {
		return DiagnosisCode;
	}

	public String getDiagnosisName() {
		return DiagnosisName;
	}

	public String getDeptCode() {
		return DeptCode;
	}

	public String getDeptName() {
		return DeptName;
	}

	public String getDoctCode() {
		return DoctCode;
	}

	public String getDoctName() {
		return DoctName;
	}

	public String getTimeStamp() {
		return TimeStamp;
	}

	public List<ItemsApplyLisXHHK> getItems() {
		return Items;
	}

	public void setApplyNo(String applyNo) {
		ApplyNo = applyNo;
	}

	public void setHospitalCode(String hospitalCode) {
		HospitalCode = hospitalCode;
	}

	public void setHospitalName(String hospitalName) {
		HospitalName = hospitalName;
	}

	public void setPatNo(String patNo) {
		PatNo = patNo;
	}

	public void setPatType(int patType) {
		PatType = patType;
	}

	public void setIdCard(String idCard) {
		IdCard = idCard;
	}

	public void setPatName(String patName) {
		PatName = patName;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public void setAge(String age) {
		Age = age;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public void setDiagnosisCode(String diagnosisCode) {
		DiagnosisCode = diagnosisCode;
	}

	public void setDiagnosisName(String diagnosisName) {
		DiagnosisName = diagnosisName;
	}

	public void setDeptCode(String deptCode) {
		DeptCode = deptCode;
	}

	public void setDeptName(String deptName) {
		DeptName = deptName;
	}

	public void setDoctCode(String doctCode) {
		DoctCode = doctCode;
	}

	public void setDoctName(String doctName) {
		DoctName = doctName;
	}

	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}

	public void setItems(List<ItemsApplyLisXHHK> items) {
		Items = items;
	}

	public String getSample_barcode() {
		return sample_barcode;
	}

	public void setSample_barcode(String sample_barcode) {
		this.sample_barcode = sample_barcode;
	}

	public String getExam_num() {
		return exam_num;
	}

	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}

	public String getCharging_id() {
		return charging_id;
	}

	public void setCharging_id(String charging_id) {
		this.charging_id = charging_id;
	}

	public String getFeeType() {
		return FeeType;
	}

	public void setFeeType(String feeType) {
		FeeType = feeType;
	}

	public String getWardCode() {
		return WardCode;
	}

	public void setWardCode(String wardCode) {
		WardCode = wardCode;
	}

	public String getWardName() {
		return WardName;
	}

	public void setWardName(String wardName) {
		WardName = wardName;
	}

	public String getBedNo() {
		return BedNo;
	}

	public void setBedNo(String bedNo) {
		BedNo = bedNo;
	}

	public String getSampleNo() {
		return SampleNo;
	}

	public void setSampleNo(String sampleNo) {
		SampleNo = sampleNo;
	}

	public String getSampleType() {
		return SampleType;
	}

	public void setSampleType(String sampleType) {
		SampleType = sampleType;
	}
	
}
