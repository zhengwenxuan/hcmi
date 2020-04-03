package com.hjw.webService.client.xianning.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONSerializer;

public class RequestParam {

	private PATIENTINFO PATIENTINFO = new PATIENTINFO();
	
	private List<EXAMINFO> EXAMINFO = new ArrayList<>();

	public PATIENTINFO getPATIENTINFO() {
		return PATIENTINFO;
	}

	public List<EXAMINFO> getEXAMINFO() {
		return EXAMINFO;
	}

	public void setPATIENTINFO(PATIENTINFO pATIENTINFO) {
		PATIENTINFO = pATIENTINFO;
	}

	public void setEXAMINFO(List<EXAMINFO> eXAMINFO) {
		EXAMINFO = eXAMINFO;
	}

	public static void main(String[] args) {
		RequestParam request = new RequestParam();
		request.getEXAMINFO().add(new EXAMINFO());
		request.getEXAMINFO().add(new EXAMINFO());
		String jsonString = JSONSerializer.toJSON(request).toString();
		System.out.println(jsonString);
	}
}