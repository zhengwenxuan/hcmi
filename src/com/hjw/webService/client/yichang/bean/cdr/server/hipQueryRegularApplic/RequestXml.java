package com.hjw.webService.client.yichang.bean.cdr.server.hipQueryRegularApplic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "requestXml")  
@XmlType(propOrder = {})
public class RequestXml {

	@XmlElement
	private String	 patientSourceCode = "";//patientSourceCode
	@XmlElement
	private String	 applyType = "";//applyType>
	@XmlElement
	private String	 applyCode = "";//applyCode>
	@XmlElement
	private String	 visitType = "";//visitSqNo>
	@XmlElement
	private String	 visitNo = "";//visitSqNo>
	@XmlElement
	private String	 visitSqNo = "";//visitSqNo>
	@XmlElement
	private String	 patientName = "";//patientName>
	@XmlElement
	private String	 patientPhoneNumber = "";//patientPhoneNumber>
	@XmlElement
	private String	 applicationStartTime = "";//applicationStartTime>
	@XmlElement
	private String	 applicationEndTime = "";//applicationEndTime>
	@XmlElement
	private String	 codeExpand1 = "";//codeExpand1>
	@XmlElement
	private String	 codeExpand2 = "";//codeExpand2>
	@XmlElement
	private String	 codeExpand3 = "";//codeExpand3>
	@XmlElement
	private String	 codeExpand4 = "";//codeExpand4>
	@XmlElement
	private String	 codeExpand5 = "";//codeExpand5>
	
	public String getPatientSourceCode() {
		return patientSourceCode;
	}
	public String getApplyType() {
		return applyType;
	}
	public String getApplyCode() {
		return applyCode;
	}
	public String getVisitSqNo() {
		return visitSqNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getPatientPhoneNumber() {
		return patientPhoneNumber;
	}
	public String getApplicationStartTime() {
		return applicationStartTime;
	}
	public String getApplicationEndTime() {
		return applicationEndTime;
	}
	public String getCodeExpand1() {
		return codeExpand1;
	}
	public String getCodeExpand2() {
		return codeExpand2;
	}
	public String getCodeExpand3() {
		return codeExpand3;
	}
	public String getCodeExpand4() {
		return codeExpand4;
	}
	public String getCodeExpand5() {
		return codeExpand5;
	}
	public void setPatientSourceCode(String patientSourceCode) {
		this.patientSourceCode = patientSourceCode;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public void setApplyCode(String applyCode) {
		this.applyCode = applyCode;
	}
	public void setVisitSqNo(String visitSqNo) {
		this.visitSqNo = visitSqNo;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setPatientPhoneNumber(String patientPhoneNumber) {
		this.patientPhoneNumber = patientPhoneNumber;
	}
	public void setApplicationStartTime(String applicationStartTime) {
		this.applicationStartTime = applicationStartTime;
	}
	public void setApplicationEndTime(String applicationEndTime) {
		this.applicationEndTime = applicationEndTime;
	}
	public void setCodeExpand1(String codeExpand1) {
		this.codeExpand1 = codeExpand1;
	}
	public void setCodeExpand2(String codeExpand2) {
		this.codeExpand2 = codeExpand2;
	}
	public void setCodeExpand3(String codeExpand3) {
		this.codeExpand3 = codeExpand3;
	}
	public void setCodeExpand4(String codeExpand4) {
		this.codeExpand4 = codeExpand4;
	}
	public void setCodeExpand5(String codeExpand5) {
		this.codeExpand5 = codeExpand5;
	}
	
	public String getVisitNo() {
		return visitNo;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public static void main(String[] args) throws Exception {
		String xml = ""
				+"<requestXml>                                     "
				+"	<patientSourceCode>?</patientSourceCode>      "
				+"	<applyType>?</applyType>                      "
				+"	<applyCode>?</applyCode>                      "
				+"	<visitSqNo>?</visitSqNo>                      "
				+"	<patientName>?</patientName>                  "
				+"	<patientPhoneNumber>?</patientPhoneNumber>    "
				+"	<applicationStartTime>?</applicationStartTime>"
				+"	<applicationEndTime>?</applicationEndTime>    "
				+"	<codeExpand1>?</codeExpand1>                  "
				+"	<codeExpand2>?</codeExpand2>                  "
				+"	<codeExpand3>?</codeExpand3>                  "
				+"	<codeExpand4>?</codeExpand4>                  "
				+"	<codeExpand5>?</codeExpand5>                  "
				+"</requestXml>                                    "
				+ "";
		RequestXml request = JaxbUtil.converyToJavaBean(xml, RequestXml.class);
		System.out.println(request.getApplicationEndTime());
	}
}
