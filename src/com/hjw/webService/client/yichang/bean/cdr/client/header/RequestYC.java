package com.hjw.webService.client.yichang.bean.cdr.client.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.yichang.bean.cdr.client.createDoc.RegistPatientCreateDocInfo;
import com.hjw.webService.client.yichang.bean.cdr.client.generalReport.RegistGeneralReport;
import com.hjw.webService.client.yichang.bean.cdr.client.hipPushRequestFormToXD.HIPMessageServer;
import com.hjw.webService.client.yichang.bean.cdr.client.registration.RegistPatientRegistrationInformation;
import com.hjw.webService.client.yichang.bean.cdr.client.requestForm.RegistRequestForm;
import com.hjw.webService.client.yichang.bean.cdr.client.update.RegistUpdatePatienInfo;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "request")  
@XmlType(propOrder = {})
public class RequestYC {
	
	@XmlElement
	private RegistPatientRegistrationInformation registPatientRegistrationInformation = null;//
	
	@XmlElement
	private RegistPatientCreateDocInfo registPatientCreateDocInfo = null;//
	
	@XmlElement
	private RegistUpdatePatienInfo registUpdatePatienInfo = null;//
	
	@XmlElement
	private RegistGeneralReport registGeneralReport = null;//
	
	@XmlElement
	private RegistRequestForm registRequestForm = null;//
	
	@XmlElement
	private HIPMessageServer HIPMessageServer = null;//

	public RegistPatientRegistrationInformation getRegistPatientRegistrationInformation() {
		return registPatientRegistrationInformation;
	}

	public void setRegistPatientRegistrationInformation(
			RegistPatientRegistrationInformation registPatientRegistrationInformation) {
		this.registPatientRegistrationInformation = registPatientRegistrationInformation;
	}

	public RegistPatientCreateDocInfo getRegistPatientCreateDocInfo() {
		return registPatientCreateDocInfo;
	}

	public void setRegistPatientCreateDocInfo(RegistPatientCreateDocInfo registPatientCreateDocInfo) {
		this.registPatientCreateDocInfo = registPatientCreateDocInfo;
	}

	public RegistUpdatePatienInfo getRegistUpdatePatienInfo() {
		return registUpdatePatienInfo;
	}

	public void setRegistUpdatePatienInfo(RegistUpdatePatienInfo registUpdatePatienInfo) {
		this.registUpdatePatienInfo = registUpdatePatienInfo;
	}

	public RegistGeneralReport getRegistGeneralReport() {
		return registGeneralReport;
	}

	public void setRegistGeneralReport(RegistGeneralReport registGeneralReport) {
		this.registGeneralReport = registGeneralReport;
	}

	public RegistRequestForm getRegistRequestForm() {
		return registRequestForm;
	}

	public void setRegistRequestForm(RegistRequestForm registRequestForm) {
		this.registRequestForm = registRequestForm;
	}

	public HIPMessageServer getHIPMessageServer() {
		return HIPMessageServer;
	}

	public void setHIPMessageServer(HIPMessageServer hIPMessageServer) {
		HIPMessageServer = hIPMessageServer;
	}
}
