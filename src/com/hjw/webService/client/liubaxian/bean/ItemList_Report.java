package com.hjw.webService.client.liubaxian.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "ItemList")  
@XmlType(propOrder = {})
public class ItemList_Report {

	@XmlElement
	private String ItemName = "";//项目名称
	@XmlElement
	private String PacsItemCode = "";//PACS 检查项目代码
	@XmlElement
	private String StudyType = "";//检查类别：US(超声)/MR(核磁)/CT(CT 检查)/XX(射线检查)
	@XmlElement
	private String StudyBodyPart = "";//检查部位
	
	public String getItemName() {
		return ItemName;
	}
	public String getPacsItemCode() {
		return PacsItemCode;
	}
	public String getStudyType() {
		return StudyType;
	}
	public String getStudyBodyPart() {
		return StudyBodyPart;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public void setPacsItemCode(String pacsItemCode) {
		PacsItemCode = pacsItemCode;
	}
	public void setStudyType(String studyType) {
		StudyType = studyType;
	}
	public void setStudyBodyPart(String studyBodyPart) {
		StudyBodyPart = studyBodyPart;
	}
}
