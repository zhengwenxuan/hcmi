package com.hjw.webService.client.yichang.bean.cdr.server.hipReqStatusWriteBack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "statusReq")  
@XmlType(propOrder = {})
public class StatusReq {

	@XmlElement
	private String applyNo = "";//申请单编号
	@XmlElement
	private String medicalType = "";//诊疗类型
	@XmlElement
	private String confirmCancelStatus = "";//确认/取消状态
	
	public String getApplyNo() {
		return applyNo;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public String getConfirmCancelStatus() {
		return confirmCancelStatus;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public void setConfirmCancelStatus(String confirmCancelStatus) {
		this.confirmCancelStatus = confirmCancelStatus;
	}

	public static void main(String[] args) throws Exception {
		String xml = ""
				+"<statusReq>"
					+"<applyNo>T197180068</applyNo><medicalType>04</medicalType><confirmCancelStatus>02</confirmCancelStatus>"
				+"</statusReq>"
				+ "";
		StatusReq request = JaxbUtil.converyToJavaBean(xml, StatusReq.class);
		System.out.println(request.getApplyNo());
	}
}
