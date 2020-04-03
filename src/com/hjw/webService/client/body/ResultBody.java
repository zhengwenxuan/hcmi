package com.hjw.webService.client.body;

import com.hjw.webService.client.Bean.ControlActProcess;
import com.hjw.webService.client.body.ResultHeader;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {}) 
public class ResultBody{
	@XmlElement  
  private ResultHeader ResultHeader= new ResultHeader();
	
	@XmlElement  
  private ControlActProcess ControlActProcess=new ControlActProcess();

	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}

	public ControlActProcess getControlActProcess() {
		return ControlActProcess;
	}

	public void setControlActProcess(ControlActProcess controlActProcess) {
		ControlActProcess = controlActProcess;
	}
	
	
  
}
