package com.hjw.webService.service.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ResultHeader")  
@XmlType(propOrder = {})
public class ResultEquipmentHeader {

	@XmlElement  
	private String type_code;//AA:处理成功；AE：处理失败
	
	@XmlElement  
	private String text;//处理结果说明
	
	@XmlElement  
	private String user_name; //姓名
	
	@XmlElement  
	private String birthdate; //生日  YYYY-MM-DD
	
	@XmlElement  
	private String sex;       //性别  男、女
	
	@XmlElement  
	private String age;       //年龄
	
	@XmlElement  
	private String id_num;    //身份证号
	
	@XmlElement  
	private String height;    //身高
	
	@XmlElement  
	private String weight;    //体重
	
	@XmlElement  
	private String bmi;       //体重指数

	public String getType_code() {
		return type_code;
	}

	public void setType_code(String type_code) {
		this.type_code = type_code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getBmi() {
		return bmi;
	}

	public void setBmi(String bmi) {
		this.bmi = bmi;
	}
}
