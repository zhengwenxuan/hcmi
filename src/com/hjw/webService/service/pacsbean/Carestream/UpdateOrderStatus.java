package com.hjw.webService.service.pacsbean.Carestream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "xmlMessage")  
@XmlType(propOrder = {})  
public class UpdateOrderStatus {
	@XmlElement  
	private String HospID="";//	1-1	医院代码	
	@XmlElement  
	private String RemoteAccNo="";//	1-1	申请单编号	
	@XmlElement  
	private String AccNo="";//	1-1	检查编号	
	@XmlElement  
	private String Status="";//	1-1	状态说明	10-登记完成
	                                      //14-检查完成
	                                      //32-报告审核
	                                      //0-科室撤销医嘱
	public String getHospID() {
		return HospID;
	}
	public void setHospID(String hospID) {
		HospID = hospID;
	}
	public String getRemoteAccNo() {
		return RemoteAccNo;
	}
	public void setRemoteAccNo(String remoteAccNo) {
		RemoteAccNo = remoteAccNo;
	}
	public String getAccNo() {
		return AccNo;
	}
	public void setAccNo(String accNo) {
		AccNo = accNo;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	

}
