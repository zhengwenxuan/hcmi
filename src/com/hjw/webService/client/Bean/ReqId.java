package com.hjw.webService.client.Bean;

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
public class ReqId {

	@XmlElement  
  private String req_id;//申请单号
  private String third_req_id;//第三方申请号（门诊号）
  
  public String getThird_req_id() {
	return third_req_id;
}

public void setThird_req_id(String third_req_id) {
	this.third_req_id = third_req_id;
}

private List<FeeRes> feeitem=new ArrayList<FeeRes>();
	public String getReq_id() {
		return req_id;
	}

	public void setReq_id(String req_id) {
		this.req_id = req_id;
	}

	public List<FeeRes> getFeeitem() {
		return feeitem;
	}

	public void setFeeitem(List<FeeRes> feeitem) {
		this.feeitem = feeitem;
	}

	
}
