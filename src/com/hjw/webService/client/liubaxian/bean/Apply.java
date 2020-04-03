package com.hjw.webService.client.liubaxian.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Apply")  
@XmlType(propOrder = {})
public class Apply {

	@XmlElement
	private String ReqNo = "";//申请单号
	@XmlElement
	private String ExamNum = "";//体检号
	@XmlElement
	private String PatName = "";//姓名
	@XmlElement
	private String Sex = "";//男/女/未知
	@XmlElement
	private String Birthday = "";//生日 yyyy-MM-dd
	@XmlElement
	private String Age = "";//年龄
	@XmlElement
	private String StudyType = "";//检查类别：US(超声)/MR(核磁)CT(CT 检查)/XX(射线检查)
	@XmlElement
	private String ReqDoctor = "";//申请医生
	@XmlElement
	private String ReqDate = "";//申请时间 yyyymmdd hh:mm
	@XmlElement
	private String ReqDept = "";//申请科室
	@XmlElement
	private String PerformedBy = "";//执行科室
	@XmlElement
	private ItemList_Apply ItemList = new ItemList_Apply();//
	
	public String getReqNo() {
		return ReqNo;
	}
	public String getExamNum() {
		return ExamNum;
	}
	public String getPatName() {
		return PatName;
	}
	public String getSex() {
		return Sex;
	}
	public String getBirthday() {
		return Birthday;
	}
	public String getAge() {
		return Age;
	}
	public String getStudyType() {
		return StudyType;
	}
	public String getReqDoctor() {
		return ReqDoctor;
	}
	public String getReqDate() {
		return ReqDate;
	}
	public String getReqDept() {
		return ReqDept;
	}
	public String getPerformedBy() {
		return PerformedBy;
	}
	public ItemList_Apply getItemList() {
		return ItemList;
	}
	public void setReqNo(String reqNo) {
		ReqNo = reqNo;
	}
	public void setExamNum(String examNum) {
		ExamNum = examNum;
	}
	public void setPatName(String patName) {
		PatName = patName;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	public void setAge(String age) {
		Age = age;
	}
	public void setStudyType(String studyType) {
		StudyType = studyType;
	}
	public void setReqDoctor(String reqDoctor) {
		ReqDoctor = reqDoctor;
	}
	public void setReqDate(String reqDate) {
		ReqDate = reqDate;
	}
	public void setReqDept(String reqDept) {
		ReqDept = reqDept;
	}
	public void setPerformedBy(String performedBy) {
		PerformedBy = performedBy;
	}
	public void setItemList(ItemList_Apply itemList) {
		ItemList = itemList;
	}
	
}
