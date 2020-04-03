package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class DiagnosisPriceDataMessage {
	@XmlElement 
	private AuthHeader AuthHeader= new AuthHeader();
	@XmlElement 
	private ControlActDiagnosisPriceProcess ControlActProcess= new ControlActDiagnosisPriceProcess();
	public AuthHeader getAuthHeader() {
		return AuthHeader;
	}
	public void setAuthHeader(AuthHeader authHeader) {
		AuthHeader = authHeader;
	}
	public ControlActDiagnosisPriceProcess getControlActProcess() {
		return ControlActProcess;
	}
	public void setControlActProcess(ControlActDiagnosisPriceProcess controlActProcess) {
		ControlActProcess = controlActProcess;
	}	

}
