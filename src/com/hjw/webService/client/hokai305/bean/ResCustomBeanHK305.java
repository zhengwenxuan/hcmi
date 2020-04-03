package com.hjw.webService.client.hokai305.bean;

import java.util.ArrayList;

public class ResCustomBeanHK305 {
	private String code="AE";
	private String examinfo_id="";
	private String persionid="";
	private String codetext="";
	private Boolean faly=false;
	
	private ArrayList<QueryCustomer> customerList = new ArrayList<QueryCustomer>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExaminfo_id() {
		return examinfo_id;
	}

	public void setExaminfo_id(String examinfo_id) {
		this.examinfo_id = examinfo_id;
	}

	public String getPersionid() {
		return persionid;
	}

	public void setPersionid(String persionid) {
		this.persionid = persionid;
	}

	public String getCodetext() {
		return codetext;
	}

	public void setCodetext(String codetext) {
		this.codetext = codetext;
	}

	public Boolean getFaly() {
		return faly;
	}

	public void setFaly(Boolean faly) {
		this.faly = faly;
	}

	public ArrayList<QueryCustomer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(ArrayList<QueryCustomer> customerList) {
		this.customerList = customerList;
	}
	
	
	
	
}
