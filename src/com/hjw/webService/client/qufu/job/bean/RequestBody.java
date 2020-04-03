package com.hjw.webService.client.qufu.job.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "requestBody")  
@XmlType(propOrder = {})
public class RequestBody {

	@XmlElement
	private String PAYLOADTYPE = "";//文书报告荷载类型,参考4.1.3节(非空)

	@XmlElement
	private String PATIENTTYPE = "3";//患者类别编码 参考4.1.4节(非空):3-体检

	@XmlElement
	private String PATIENTID = "";//病人id

	@XmlElement
	private String VISITFLOWID = "";//病人流水id

	@XmlElement
	private String VISITFLOWDOMAINID = "";//病人流水域id

	@XmlElement
	private String EXTEND_CONDITION = "";//扩展条件,参考4.1.5节

	public String getPAYLOADTYPE() {
		return PAYLOADTYPE;
	}

	public void setPAYLOADTYPE(String pAYLOADTYPE) {
		PAYLOADTYPE = pAYLOADTYPE;
	}

	public String getPATIENTTYPE() {
		return PATIENTTYPE;
	}

	public void setPATIENTTYPE(String pATIENTTYPE) {
		PATIENTTYPE = pATIENTTYPE;
	}

	public String getPATIENTID() {
		return PATIENTID;
	}

	public void setPATIENTID(String pATIENTID) {
		PATIENTID = pATIENTID;
	}

	public String getVISITFLOWID() {
		return VISITFLOWID;
	}

	public void setVISITFLOWID(String vISITFLOWID) {
		VISITFLOWID = vISITFLOWID;
	}

	public String getVISITFLOWDOMAINID() {
		return VISITFLOWDOMAINID;
	}

	public void setVISITFLOWDOMAINID(String vISITFLOWDOMAINID) {
		VISITFLOWDOMAINID = vISITFLOWDOMAINID;
	}

	public String getEXTEND_CONDITION() {
		return EXTEND_CONDITION;
	}

	public void setEXTEND_CONDITION(String eXTEND_CONDITION) {
		EXTEND_CONDITION = eXTEND_CONDITION;
	}
	
	
}
