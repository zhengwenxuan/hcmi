package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class HisBodyReqBean {
	private String reserveId="";//	体检预约号
	private String customerPatientId="";//	客户HISID号
	private String orderDept="27";//	开单科室 默认：27
	private String itemsNum="";//	开单项目数
	private List<HisReqBean> itemsInfo=new ArrayList<HisReqBean>();
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getCustomerPatientId() {
		return customerPatientId;
	}
	public void setCustomerPatientId(String customerPatientId) {
		this.customerPatientId = customerPatientId;
	}
	public String getOrderDept() {
		return orderDept;
	}
	public void setOrderDept(String orderDept) {
		this.orderDept = orderDept;
	}
	public String getItemsNum() {
		return itemsNum;
	}
	public void setItemsNum(String itemsNum) {
		this.itemsNum = itemsNum;
	}
	public List<HisReqBean> getItemsInfo() {
		return itemsInfo;
	}
	public void setItemsInfo(List<HisReqBean> itemsInfo) {
		this.itemsInfo = itemsInfo;
	}

	
}
