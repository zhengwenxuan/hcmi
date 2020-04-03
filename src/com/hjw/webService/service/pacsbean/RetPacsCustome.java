package com.hjw.webService.service.pacsbean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.service.bean.RetCustome;

public class RetPacsCustome extends RetCustome {
	private String pacs_summary_id="";// 申请单号
	private String dept_code="";// 科室编码对应检查类型编码
	private String dept_name="";//检查类型名称
	private String effectiveTime="";//报告时间	
	private String npositive="";//阴性-阳性	

	private List<RetPacsItem> list = new ArrayList<RetPacsItem>();// 收费项目		
		
	public String getNpositive() {
		return npositive;
	}

	public void setNpositive(String npositive) {
		this.npositive = npositive;
	}
	
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

	public String getPacs_summary_id() {
		return pacs_summary_id;
	}

	public void setPacs_summary_id(String pacs_summary_id) {
		this.pacs_summary_id = pacs_summary_id;
	}

	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}

	public List<RetPacsItem> getList() {
		return list;
	}

	public void setList(List<RetPacsItem> list) {
		this.list = list;
	}

}
