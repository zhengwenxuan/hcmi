package com.hjw.webService.client.longkou.bean;

public class ExamDetailBean implements java.io.Serializable {
	 private static final long serialVersionUID = -97502163798576023L;

	 private String id="";
	 private String exam_num="";
	 private long exam_info_id;
	 private long charging_id;
	 private long exam_item_id;
	 private String exam_result="";
	 private String exam_date="";
	 private String exam_doctor_name="";
	 private String item_num="";
	 private String item_name="";
	 private String remark="";
	 private String health_level="";
	 private String item_unit="";
	 private double ref_Fmax;
	 private double ref_Fmin;	 
	 private String ycbz="1";//异常表示	 
	 
	public String getYcbz() {
		return ycbz;
	}
	public void setYcbz(String ycbz) {
		this.ycbz = ycbz;
	}
	public String getHealth_level() {
		return health_level;
	}
	public void setHealth_level(String health_level) {
		this.health_level = health_level;
	}
	public String getItem_unit() {
		return item_unit;
	}
	public void setItem_unit(String item_unit) {
		this.item_unit = item_unit;
	}
	public double getRef_Fmax() {
		return ref_Fmax;
	}
	public void setRef_Fmax(double ref_Fmax) {
		this.ref_Fmax = ref_Fmax;
	}
	public double getRef_Fmin() {
		return ref_Fmin;
	}
	public void setRef_Fmin(double ref_Fmin) {
		this.ref_Fmin = ref_Fmin;
	}
	public String getItem_num() {
		return item_num;
	}
	public void setItem_num(String item_num) {
		this.item_num = item_num;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public long getExam_info_id() {
		return exam_info_id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public long getCharging_id() {
		return charging_id;
	}
	public void setCharging_id(long charging_id) {
		this.charging_id = charging_id;
	}
	public long getExam_item_id() {
		return exam_item_id;
	}
	public void setExam_item_id(long exam_item_id) {
		this.exam_item_id = exam_item_id;
	}
	public String getExam_result() {
		return exam_result;
	}
	public void setExam_result(String exam_result) {
		this.exam_result = exam_result;
	}
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
	public String getExam_doctor_name() {
		return exam_doctor_name;
	}
	public void setExam_doctor_name(String exam_doctor_name) {
		this.exam_doctor_name = exam_doctor_name;
	}
	
	}