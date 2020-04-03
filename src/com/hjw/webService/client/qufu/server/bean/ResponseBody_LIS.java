package com.hjw.webService.client.qufu.server.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})
public class ResponseBody_LIS {

	@XmlElement
	private List<ExamRequest_LIS> ExamRequest = new ArrayList<>();

	public List<ExamRequest_LIS> getExamRequest() {
		return ExamRequest;
	}

	public void setExamRequest(List<ExamRequest_LIS> examRequest) {
		ExamRequest = examRequest;
	}

}
