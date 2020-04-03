package com.hjw.webService.client.donghua.bean.PEOrd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "OrdInfo")  
@XmlType(propOrder = {})
public class OrdInfo_Response {

	@XmlElement
	private String OrdID = "";//医嘱唯一ID 体检系统医嘱唯一号
	@XmlElement
	private String OrdRowID = "";//医嘱RowID 东华医嘱RowID
	
	public String getOrdID() {
		return OrdID;
	}
	public String getOrdRowID() {
		return OrdRowID;
	}
	public void setOrdID(String ordID) {
		OrdID = ordID;
	}
	public void setOrdRowID(String ordRowID) {
		OrdRowID = ordRowID;
	}
}
