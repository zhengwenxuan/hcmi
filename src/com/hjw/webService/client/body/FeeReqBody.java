package com.hjw.webService.client.body;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.webService.client.Bean.FeeReqControlActProcess;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class FeeReqBody {
	@XmlElement  
	  private ResultHeader ResultHeader=new ResultHeader();
	  private List<String> itemCodeList = new ArrayList<String>();
		
		@XmlElement  
	  private FeeReqControlActProcess ControlActProcess = new FeeReqControlActProcess();

		public List<String> getItemCodeList() {
			return itemCodeList;
		}

		public void setItemCodeList(List<String> itemCodeList) {
			this.itemCodeList = itemCodeList;
		}

		public ResultHeader getResultHeader() {
			return ResultHeader;
		}

		public void setResultHeader(ResultHeader resultHeader) {
			ResultHeader = resultHeader;
		}

		public FeeReqControlActProcess getControlActProcess() {
			return ControlActProcess;
		}

		public void setControlActProcess(FeeReqControlActProcess controlActProcess) {
			ControlActProcess = controlActProcess;
		}
		
	
}
