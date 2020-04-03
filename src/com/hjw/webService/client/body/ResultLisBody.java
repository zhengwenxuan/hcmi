package com.hjw.webService.client.body;

import com.hjw.webService.client.Bean.ControlActLisProcess;
import com.hjw.webService.client.Bean.ControlActPacsProcess;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.Bean.LisGetResItemBean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {}) 
public class ResultLisBody{
	@XmlElement  
  private ResultHeader ResultHeader= new ResultHeader();
	
	@XmlElement  
  private ControlActLisProcess ControlActProcess=new ControlActLisProcess();

  private List<LisGetResItemBean> lisResultList;
	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}

	public ControlActLisProcess getControlActProcess() {
		return ControlActProcess;
	}

	public void setControlActProcess(ControlActLisProcess controlActProcess) {
		ControlActProcess = controlActProcess;
	}

	public List<LisGetResItemBean> getLisResultList() {
		return lisResultList;
	}

	public void setLisResultList(List<LisGetResItemBean> lisResultList) {
		this.lisResultList = lisResultList;
	}
}
