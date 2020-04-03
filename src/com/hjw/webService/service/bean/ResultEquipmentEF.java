package com.hjw.webService.service.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "xmlMessage")  
@XmlType(propOrder = {}) 
public class ResultEquipmentEF {
	@XmlElement 
	private String exam_num="";//	 体检编号	
	@XmlElement 
	private String exam_doc="";//	 检查医生	
	@XmlElement 
	private String exam_date="";//	 检查时间    YYYY-MM-DD HH:mm:ss
	@XmlElement 
	private String approve_doc="";// 审核医生	
	@XmlElement 
	private String approve_date="";// 审核时间	YYYY-MM-DD HH:mm:ss
	@XmlElement 
	private String exam_desc="";// 检查描述 报告所见	
	@XmlElement 
	private String exam_result="";// 检查结果 报告结论	
	@XmlElement 
	private String image="";//	报告图片   Base64图片文件
	
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getExam_doc() {
		return exam_doc;
	}
	public void setExam_doc(String exam_doc) {
		this.exam_doc = exam_doc;
	}
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
	public String getApprove_doc() {
		return approve_doc;
	}
	public void setApprove_doc(String approve_doc) {
		this.approve_doc = approve_doc;
	}
	public String getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(String approve_date) {
		this.approve_date = approve_date;
	}
	public String getExam_desc() {
		return exam_desc;
	}
	public void setExam_desc(String exam_desc) {
		this.exam_desc = exam_desc;
	}
	public String getExam_result() {
		return exam_result;
	}
	public void setExam_result(String exam_result) {
		this.exam_result = exam_result;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
