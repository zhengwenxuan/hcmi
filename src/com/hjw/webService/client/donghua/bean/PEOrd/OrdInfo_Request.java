package com.hjw.webService.client.donghua.bean.PEOrd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "OrdInfo")  
@XmlType(propOrder = {})
public class OrdInfo_Request {

	@XmlElement
	private String OrdID = "";//医嘱唯一ID 体检系统医嘱唯一号
	@XmlElement
	private String OrdCode = "";//His医嘱项目代码 跟HIS同步的医嘱项目代码
	@XmlElement
	private String DeptCode = "";//开单科室代码 东华His中的代码
	@XmlElement
	private String RecDeptCode = "";//接收科室代码 东华His中的代码
	@XmlElement
	private String UserCode = "";//录入者 录入医嘱人的工号
	@XmlElement
	private String Flag = "";//检查/检验区分标识 RIS (检查医嘱)/LIS (检验医嘱)
	
	public String getOrdID() {
		return OrdID;
	}
	public String getOrdCode() {
		return OrdCode;
	}
	public String getDeptCode() {
		return DeptCode;
	}
	public String getRecDeptCode() {
		return RecDeptCode;
	}
	public String getUserCode() {
		return UserCode;
	}
	public String getFlag() {
		return Flag;
	}
	public void setOrdID(String ordID) {
		OrdID = ordID;
	}
	public void setOrdCode(String ordCode) {
		OrdCode = ordCode;
	}
	public void setDeptCode(String deptCode) {
		DeptCode = deptCode;
	}
	public void setRecDeptCode(String recDeptCode) {
		RecDeptCode = recDeptCode;
	}
	public void setUserCode(String userCode) {
		UserCode = userCode;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
}
