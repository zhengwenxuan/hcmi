package com.hjw.webService.client.jinyu.lisbean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hjw.webService.service.bean.RetCustome;
import com.hjw.webService.service.pacsbean.RetPacsItem;

public class RetLisCustomeJY extends RetCustome {

	private String sample_barcode;// 申请单号// 条码号
	
	private String dept_code;//执行科室	
	
	private String dept_name;//执行科室名称
	
	private String effectiveTime;//报告时间	

	Map<String, RetLisChargeItemJY> rtlischarge_map=new HashMap<String, RetLisChargeItemJY>();// 收费项目
	
	private List<RetPacsItem> list = new ArrayList<RetPacsItem>();// 收费项目

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(String effectiveTime) {
		this.effectiveTime = effectiveTime;
	}
	
	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}

	public String getSample_barcode() {
		return sample_barcode;
	}

	public void setSample_barcode(String sample_barcode) {
		this.sample_barcode = sample_barcode;
	}

	public Map<String, RetLisChargeItemJY> getRtlischarge_map() {
		return rtlischarge_map;
	}

	public void setRtlischarge_map(Map<String, RetLisChargeItemJY> rtlischarge_map) {
		this.rtlischarge_map = rtlischarge_map;
	}

	public List<RetPacsItem> getList() {
		return list;
	}

	public void setList(List<RetPacsItem> list) {
		this.list = list;
	}
}
