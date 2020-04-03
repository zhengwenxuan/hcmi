package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class LisBodyReqBean {
	private String reserveId="";//	体检预约号
	private String reserveType="";//	体检预约类型P：需要收费；O：不需要收费
	private String customerPatientId="";//	客户HISID号
	private String orderDept="27";//	开单科室 默认：27
	private String orderDoctor="";//开单人
	private String itemsNum="1";//	检验项目数
	
	
	public String getOrderDoctor() {
		return orderDoctor;
	}
	public void setOrderDoctor(String orderDoctor) {
		this.orderDoctor = orderDoctor;
	}
	private List<LisReqBean> itemsInfo=new ArrayList<LisReqBean>();//项目情况
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getReserveType() {
		return reserveType;
	}
	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
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
	public List<LisReqBean> getItemsInfo() {
		return itemsInfo;
	}
	public void setItemsInfo(List<LisReqBean> itemsInfo) {
		this.itemsInfo = itemsInfo;
	}

	
}
