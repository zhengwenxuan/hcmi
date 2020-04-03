package com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Item")  
@XmlType(propOrder = {})
public class ItemYC {

	@XmlElement
	private String	hospCode	 = "";//	机构HIS编码
	@XmlElement
	private String	testItemCode	 = "";//	检测项目HIS编码	是
	@XmlElement
	private String	testItemName	 = "";//	检测项目名称	是
	@XmlElement
	private String	checkDoctorCode	 = "";//	审核医师HIS编码
	@XmlElement
	private String	checkDoctorName	 = "";//	审核医师姓名
	@XmlElement
	private String	orderNO	 = "";//	医嘱号
	@XmlElement
	private String	itemCode	 = "";//	医嘱项目编码	是
	@XmlElement
	private String	itemName	 = "";//	医嘱项目名称	是
	@XmlElement
	private String	itemPrice	 = "";//	项目价格
	@XmlElement
	private String	examinationSiteCode	 = "";//	检查部位编码	是
	@XmlElement
	private String	examinationSiteName	 = "";//	检查部位名称	是
	@XmlElement
	private String	sampleType	 = "";//	样本类型编码	是
	@XmlElement
	private String	specimenTypeName	 = "";//	样本类型名称	是
	@XmlElement
	private String	pacsTestMethodCode	 = "";//	检查方法编码	是
	@XmlElement
	private String	pacsTestMethodName	 = "无";//	检查方法名称	是
	
	public String getHospCode() {
		return hospCode;
	}
	public String getTestItemCode() {
		return testItemCode;
	}
	public String getTestItemName() {
		return testItemName;
	}
	public String getCheckDoctorCode() {
		return checkDoctorCode;
	}
	public String getCheckDoctorName() {
		return checkDoctorName;
	}
	public String getOrderNO() {
		return orderNO;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public String getExaminationSiteCode() {
		return examinationSiteCode;
	}
	public String getExaminationSiteName() {
		return examinationSiteName;
	}
	public String getSampleType() {
		return sampleType;
	}
	public String getSpecimenTypeName() {
		return specimenTypeName;
	}
	public String getPacsTestMethodCode() {
		return pacsTestMethodCode;
	}
	public String getPacsTestMethodName() {
		return pacsTestMethodName;
	}
	public void setHospCode(String hospCode) {
		this.hospCode = hospCode;
	}
	public void setTestItemCode(String testItemCode) {
		this.testItemCode = testItemCode;
	}
	public void setTestItemName(String testItemName) {
		this.testItemName = testItemName;
	}
	public void setCheckDoctorCode(String checkDoctorCode) {
		this.checkDoctorCode = checkDoctorCode;
	}
	public void setCheckDoctorName(String checkDoctorName) {
		this.checkDoctorName = checkDoctorName;
	}
	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public void setExaminationSiteCode(String examinationSiteCode) {
		this.examinationSiteCode = examinationSiteCode;
	}
	public void setExaminationSiteName(String examinationSiteName) {
		this.examinationSiteName = examinationSiteName;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public void setSpecimenTypeName(String specimenTypeName) {
		this.specimenTypeName = specimenTypeName;
	}
	public void setPacsTestMethodCode(String pacsTestMethodCode) {
		this.pacsTestMethodCode = pacsTestMethodCode;
	}
	public void setPacsTestMethodName(String pacsTestMethodName) {
		this.pacsTestMethodName = pacsTestMethodName;
	}
}
