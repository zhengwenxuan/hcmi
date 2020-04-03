package com.hjw.webService.client.jsjg.bean.in;

import java.util.ArrayList;
import java.util.List;

public class InParam_Report {

	private List<ReportJSJG> Report = new ArrayList<>();//单个报告单

	public List<ReportJSJG> getReport() {
		return Report;
	}

	public void setReport(List<ReportJSJG> report) {
		Report = report;
	}
	
}
