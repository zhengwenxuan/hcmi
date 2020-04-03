package com.hjw.webService.client.body;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.Bean.YbControlActProcess;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class YbCustomResultBody {
	@XmlElement  
	  private ResultHeader ResultHeader=new ResultHeader();
		
		@XmlElement  
	  private YbControlActProcess ControlActProcess= new YbControlActProcess();

		public ResultHeader getResultHeader() {
			return ResultHeader;
		}

		public void setResultHeader(ResultHeader resultHeader) {
			ResultHeader = resultHeader;
		}

		public YbControlActProcess getControlActProcess() {
			return ControlActProcess;
		}

		public void setControlActProcess(YbControlActProcess controlActProcess) {
			ControlActProcess = controlActProcess;
		}
		
		
}
