package com.hjw.webService.client.donghua.bean.lis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "OrgResultMsgs")  
@XmlType(propOrder = {})
public class OrgResultMsgs {

	@XmlElement
	private List<OrgResultMsg> OrgResultMsg = new ArrayList<>();

	public List<OrgResultMsg> getOrgResultMsg() {
		return OrgResultMsg;
	}

	public void setOrgResultMsg(List<OrgResultMsg> orgResultMsg) {
		OrgResultMsg = orgResultMsg;
	}
}
