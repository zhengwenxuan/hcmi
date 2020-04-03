package com.hjw.webService.client.body;

import com.hjw.webService.client.Bean.CustomResBean;
import com.hjw.webService.client.Bean.FeeControlActProcess;
import com.hjw.webService.client.body.ResultHeader;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {}) 
public class FeeResultBody{
	@XmlElement  
  private ResultHeader ResultHeader=new ResultHeader();
  private List<String> itemCodeList = new ArrayList<String>();
	@XmlElement  
  private FeeControlActProcess ControlActProcess= new FeeControlActProcess();

	@XmlElement  
	private List<CustomResBean> custom=new ArrayList<CustomResBean>();		

	public List<String> getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(List<String> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	public List<CustomResBean> getCustom() {
		return custom;
	}

	public void setCustom(List<CustomResBean> custom) {
		this.custom = custom;
	}
	
	public ResultHeader getResultHeader() {
		return ResultHeader;
	}

	public void setResultHeader(ResultHeader resultHeader) {
		ResultHeader = resultHeader;
	}

	public FeeControlActProcess getControlActProcess() {
		return ControlActProcess;
	}

	public void setControlActProcess(FeeControlActProcess controlActProcess) {
		ControlActProcess = controlActProcess;
	}
	
	
  
}
