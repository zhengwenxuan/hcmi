package com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.DateTimeUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "FindOrd")  
@XmlType(propOrder = {})
public class FindOrd {

	@XmlElement
	private String Name = "";//黄明贵</Name
	@XmlElement
	private String Gender = "";//男</Gender
	@XmlElement
	private String Birthday = "";//1962/8/15  0:00:00</Birthday
	@XmlElement
	private String Age = "";//  56岁</Age
	@XmlElement
	private String PatientCode = "";//ZY020000493039</PatientCode
	@XmlElement
	private String InpatientCode = "";//ZY020000493039</InpatientCode
	@XmlElement
	private String OutpatientCode = "";//</OutpatientCode
	@XmlElement
	private String CheckupCode = "";//</CheckupCode
	@XmlElement
	private String Height = "";//</Height
	@XmlElement
	private String Weigth = "";//</Weigth
	@XmlElement
	private String Marry = "";//</Marry
	@XmlElement
	private String Location = "";//2020@</Location
	@XmlElement
	private String Address = "";//</Address
	@XmlElement
	private String Telephone = "";//18071916007</Telephone
	@XmlElement
	private String ApplyTime = DateTimeUtil.getDateTime();
	@XmlElement
	private String ApplyDoctor = "吴之余";//吴高波</ApplyDoctor
	@XmlElement
	private String ApplyDepartment = "体检中心";//2020@心血管内科</ApplyDepartment
	@XmlElement
	private String Barcode = "";//17441201</Barcode
	@XmlElement
	private String ExamCode = "";//17441201</ExamCode
	@XmlElement
	private String OrderItemCode = "";//F00000073607</OrderItemCode
	@XmlElement
	private String OrderItemName = "";//(复)床旁心电图</OrderItemName
	@XmlElement
	private String ItemPrice = "";//45</ItemPrice
	@XmlElement
	private String ExecDeptCode = "0191";//0191</ExecDeptCode
	@XmlElement
	private String ExecDeptName = "心电图室";//心电图室</ExecDeptName
	@XmlElement
	private String ClinicDiag = "";//</ClinicDiag
	@XmlElement
	private String ChargeFlag = "";//B@已记账</ChargeFlag
	@XmlElement
	private String PatientSource = "体检";//住院</PatientSource
	@XmlElement
	private String BedNo = "";//+9</BedNo
	@XmlElement
	private String Zone = "";//0445@心血管内科一病区</Zone
	@XmlElement
	private String OrderStatus = "核实";//核实</OrderStatus
	@XmlElement
	private String Nation = "";//HZ-汉族</Nation = "";
	public String getName() {
		return Name;
	}
	public String getGender() {
		return Gender;
	}
	public String getBirthday() {
		return Birthday;
	}
	public String getAge() {
		return Age;
	}
	public String getPatientCode() {
		return PatientCode;
	}
	public String getInpatientCode() {
		return InpatientCode;
	}
	public String getOutpatientCode() {
		return OutpatientCode;
	}
	public String getCheckupCode() {
		return CheckupCode;
	}
	public String getHeight() {
		return Height;
	}
	public String getWeigth() {
		return Weigth;
	}
	public String getMarry() {
		return Marry;
	}
	public String getLocation() {
		return Location;
	}
	public String getAddress() {
		return Address;
	}
	public String getTelephone() {
		return Telephone;
	}
	public String getApplyTime() {
		return ApplyTime;
	}
	public String getApplyDoctor() {
		return ApplyDoctor;
	}
	public String getApplyDepartment() {
		return ApplyDepartment;
	}
	public String getBarcode() {
		return Barcode;
	}
	public String getExamCode() {
		return ExamCode;
	}
	public String getOrderItemCode() {
		return OrderItemCode;
	}
	public String getOrderItemName() {
		return OrderItemName;
	}
	public String getItemPrice() {
		return ItemPrice;
	}
	public String getExecDeptCode() {
		return ExecDeptCode;
	}
	public String getExecDeptName() {
		return ExecDeptName;
	}
	public String getClinicDiag() {
		return ClinicDiag;
	}
	public String getChargeFlag() {
		return ChargeFlag;
	}
	public String getPatientSource() {
		return PatientSource;
	}
	public String getBedNo() {
		return BedNo;
	}
	public String getZone() {
		return Zone;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public String getNation() {
		return Nation;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
	}
	public void setAge(String age) {
		Age = age;
	}
	public void setPatientCode(String patientCode) {
		PatientCode = patientCode;
	}
	public void setInpatientCode(String inpatientCode) {
		InpatientCode = inpatientCode;
	}
	public void setOutpatientCode(String outpatientCode) {
		OutpatientCode = outpatientCode;
	}
	public void setCheckupCode(String checkupCode) {
		CheckupCode = checkupCode;
	}
	public void setHeight(String height) {
		Height = height;
	}
	public void setWeigth(String weigth) {
		Weigth = weigth;
	}
	public void setMarry(String marry) {
		Marry = marry;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public void setApplyTime(String applyTime) {
		ApplyTime = applyTime;
	}
	public void setApplyDoctor(String applyDoctor) {
		ApplyDoctor = applyDoctor;
	}
	public void setApplyDepartment(String applyDepartment) {
		ApplyDepartment = applyDepartment;
	}
	public void setBarcode(String barcode) {
		Barcode = barcode;
	}
	public void setExamCode(String examCode) {
		ExamCode = examCode;
	}
	public void setOrderItemCode(String orderItemCode) {
		OrderItemCode = orderItemCode;
	}
	public void setOrderItemName(String orderItemName) {
		OrderItemName = orderItemName;
	}
	public void setItemPrice(String itemPrice) {
		ItemPrice = itemPrice;
	}
	public void setExecDeptCode(String execDeptCode) {
		ExecDeptCode = execDeptCode;
	}
	public void setExecDeptName(String execDeptName) {
		ExecDeptName = execDeptName;
	}
	public void setClinicDiag(String clinicDiag) {
		ClinicDiag = clinicDiag;
	}
	public void setChargeFlag(String chargeFlag) {
		ChargeFlag = chargeFlag;
	}
	public void setPatientSource(String patientSource) {
		PatientSource = patientSource;
	}
	public void setBedNo(String bedNo) {
		BedNo = bedNo;
	}
	public void setZone(String zone) {
		Zone = zone;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public void setNation(String nation) {
		Nation = nation;
	}
	
	public static void main(String[] args) {
		String xml = JaxbUtil.convertToXmlWithOutHead(new FindOrd(), true);
		System.out.println(xml);
	}
}
