package com.hjw.webService.client.jinyu.lisbean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Data_Row")  
@XmlType(propOrder = {})
public class PatientInfoJY{
	
	@XmlElement
	private String Lis_Barcode = "";//医院条码
	@XmlElement
	private String pat_id = "";//病人ID
	@XmlElement
	private String pat_no = "";//门诊/住院号
	@XmlElement
	private String pat_name = "";//病人姓名
	@XmlElement
	private String pat_bedno = "";//床号
	@XmlElement
	private String blood_time = "";//采样时间
	@XmlElement
	private String pat_sex = "";//性别
	@XmlElement
	private String pat_birthday = "";//出生日期
	@XmlElement
	private long pat_age;//年龄
	@XmlElement
	private String pat_ageunit = "岁";//年龄单位
	@XmlElement
	private String pat_tel = "";//病人电话
	@XmlElement
	private String dept_name = "体检中心";//送检科室
	@XmlElement
	private String doctor_name = "";//送检医生
	@XmlElement
	private String doctor_tel = "";//医生电话
	@XmlElement
	private String clinical_diag = "体检";//医嘱
	@XmlElement
	private String samp_name = "";//标本类型
	@XmlElement
	private String pat_from = "";//标本来源
	@XmlElement
	private List<LisItems> LisItems = new ArrayList<>();//标本来源
	
	public String getLis_Barcode() {
		return Lis_Barcode;
	}
	public void setLis_Barcode(String lis_Barcode) {
		Lis_Barcode = lis_Barcode;
	}
	public String getPat_id() {
		return pat_id;
	}
	public void setPat_id(String pat_id) {
		this.pat_id = pat_id;
	}
	public String getPat_no() {
		return pat_no;
	}
	public void setPat_no(String pat_no) {
		this.pat_no = pat_no;
	}
	public String getPat_name() {
		return pat_name;
	}
	public void setPat_name(String pat_name) {
		this.pat_name = pat_name;
	}
	public String getPat_bedno() {
		return pat_bedno;
	}
	public void setPat_bedno(String pat_bedno) {
		this.pat_bedno = pat_bedno;
	}
	public String getBlood_time() {
		return blood_time;
	}
	public void setBlood_time(String blood_time) {
		this.blood_time = blood_time;
	}
	public String getPat_sex() {
		return pat_sex;
	}
	public void setPat_sex(String pat_sex) {
		this.pat_sex = pat_sex;
	}
	public String getPat_birthday() {
		return pat_birthday;
	}
	public void setPat_birthday(String pat_birthday) {
		this.pat_birthday = pat_birthday;
	}
	public long getPat_age() {
		return pat_age;
	}
	public void setPat_age(long pat_age) {
		this.pat_age = pat_age;
	}
	public String getPat_ageunit() {
		return pat_ageunit;
	}
	public void setPat_ageunit(String pat_ageunit) {
		this.pat_ageunit = pat_ageunit;
	}
	public String getPat_tel() {
		return pat_tel;
	}
	public void setPat_tel(String pat_tel) {
		this.pat_tel = pat_tel;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDoctor_name() {
		return doctor_name;
	}
	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}
	public String getDoctor_tel() {
		return doctor_tel;
	}
	public void setDoctor_tel(String doctor_tel) {
		this.doctor_tel = doctor_tel;
	}
	public String getClinical_diag() {
		return clinical_diag;
	}
	public void setClinical_diag(String clinical_diag) {
		this.clinical_diag = clinical_diag;
	}
	public String getSamp_name() {
		return samp_name;
	}
	public void setSamp_name(String samp_name) {
		this.samp_name = samp_name;
	}
	public String getPat_from() {
		return pat_from;
	}
	public void setPat_from(String pat_from) {
		this.pat_from = pat_from;
	}
	public List<LisItems> getLisItems() {
		return LisItems;
	}
	public void setLisItems(List<LisItems> lisItems) {
		LisItems = lisItems;
	}
}
