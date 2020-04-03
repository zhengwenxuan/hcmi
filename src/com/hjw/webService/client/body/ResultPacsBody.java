package com.hjw.webService.client.body;

import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.body.ResultHeader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {}) 
public class ResultPacsBody{
	@XmlElement  
  private ResultHeader ResultHeader= new ResultHeader();
	
	@XmlElement  
  private ControlActPacsProcess ControlActProcess=new ControlActPacsProcess();

	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}

	public ControlActPacsProcess getControlActProcess() {
		return ControlActProcess;
	}

	public void setControlActProcess(ControlActPacsProcess controlActProcess) {
		ControlActProcess = controlActProcess;
	}
  
}
