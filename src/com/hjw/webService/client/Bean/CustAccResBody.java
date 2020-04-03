package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

public class CustAccResBody {
	private String status="AE";//	返回状态码 正常：200，参数异常400，	未知错误: 500
	private int patNum;//	客户人数
	private String errorInfo="";//	错误信息提示 	正常：””，异常：中文提示
	private List<CustAccResBean>customerInfo=new ArrayList<CustAccResBean>();
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPatNum() {
		return patNum;
	}
	public void setPatNum(int patNum) {
		this.patNum = patNum;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public List<CustAccResBean> getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(List<CustAccResBean> customerInfo) {
		this.customerInfo = customerInfo;
	} 
	
	
}
