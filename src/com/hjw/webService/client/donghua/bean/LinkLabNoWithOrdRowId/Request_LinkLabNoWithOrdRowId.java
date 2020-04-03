package com.hjw.webService.client.donghua.bean.LinkLabNoWithOrdRowId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class Request_LinkLabNoWithOrdRowId {
	
	@XmlElement
	private String LabNo = "";//检验号 体检系统产生
	@XmlElement
	private String OrdRowIds = "";//医嘱号列表 该试管关联的医嘱号，格式：RowID1^RowID2 传东华HIS产生的医嘱号
	
	public static void main(String[] args) throws Exception {
		Request_LinkLabNoWithOrdRowId request = new Request_LinkLabNoWithOrdRowId();
		String xml = JaxbUtil.convertToXmlWithOutHead(request, true);
		System.out.println(xml);
	}

	public String getLabNo() {
		return LabNo;
	}

	public String getOrdRowIds() {
		return OrdRowIds;
	}

	public void setLabNo(String labNo) {
		LabNo = labNo;
	}

	public void setOrdRowIds(String ordRowIds) {
		OrdRowIds = ordRowIds;
	}
}
