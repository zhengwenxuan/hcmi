package com.hjw.webService.client.donghua.bean.PatAdmInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class Request_PatAdm {

	@XmlElement
	private String RegNo = "";//体检登记号/体检档案号
	@XmlElement
	private String AdmNo = "";//体检就诊号/体检流水号
	@XmlElement
	private String Name = "";//病人姓名
	@XmlElement
	private String Sex = "";//病人性别代码 M：男，F:女
	@XmlElement
	private String Birth = "";//出生日期 YY-MM-DD
	@XmlElement
	private String Phone = "";//电话
	@XmlElement
	private String Address = "";//地址
	@XmlElement
	private String AdmDate = "";//就诊日期
	@XmlElement
	private String AdmTime = "";//就诊时间
	@XmlElement
	private String IdentityNo = "";//身份证号
	
	public static void main(String[] args) throws Exception {
		Request_PatAdm request = new Request_PatAdm();
		String xml = JaxbUtil.convertToXmlWithOutHead(request, true);
		System.out.println(xml);
	}
	
	public String getRegNo() {
		return RegNo;
	}
	public String getAdmNo() {
		return AdmNo;
	}
	public String getName() {
		return Name;
	}
	public String getSex() {
		return Sex;
	}
	public String getBirth() {
		return Birth;
	}
	public String getPhone() {
		return Phone;
	}
	public String getAddress() {
		return Address;
	}
	public String getAdmDate() {
		return AdmDate;
	}
	public String getAdmTime() {
		return AdmTime;
	}
	public String getIdentityNo() {
		return IdentityNo;
	}
	public void setRegNo(String regNo) {
		RegNo = regNo;
	}
	public void setAdmNo(String admNo) {
		AdmNo = admNo;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public void setBirth(String birth) {
		Birth = birth;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public void setAdmDate(String admDate) {
		AdmDate = admDate;
	}
	public void setAdmTime(String admTime) {
		AdmTime = admTime;
	}
	public void setIdentityNo(String identityNo) {
		IdentityNo = identityNo;
	}
}
