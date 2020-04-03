package com.hjw.webService.client.donghua.bean.PEStop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "PEStopOrdInfo")  
@XmlType(propOrder = {})
public class PEStopOrdInfo {

	@XmlElement
	private String PEOrdRowID = "";//
	@XmlElement
	private String RowIDCode = "";//
	@XmlElement
	private String RowIDContent = "";//
	
	public String getPEOrdRowID() {
		return PEOrdRowID;
	}
	public String getRowIDCode() {
		return RowIDCode;
	}
	public String getRowIDContent() {
		return RowIDContent;
	}
	public void setPEOrdRowID(String pEOrdRowID) {
		PEOrdRowID = pEOrdRowID;
	}
	public void setRowIDCode(String rowIDCode) {
		RowIDCode = rowIDCode;
	}
	public void setRowIDContent(String rowIDContent) {
		RowIDContent = rowIDContent;
	}
}
