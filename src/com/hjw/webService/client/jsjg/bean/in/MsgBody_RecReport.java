package com.hjw.webService.client.jsjg.bean.in;

import com.google.gson.Gson;

public class MsgBody_RecReport {

	private InParam_RecReport InParam;

	public InParam_RecReport getInParam() {
		return InParam;
	}

	public void setInParam(InParam_RecReport InParam) {
		this.InParam = InParam;
	}
	
	public static void main(String[] args) {
		String jsonStr = "{\"InParam\":{\"ReportNo\":\"12054264\",\"OperatorCode\":\"0110\",\"OperatorName\":\"王五\",\"OperatorTime\":\"2016-03-1309:10:00\"}}";
		MsgBody_RecReport msgBody = new Gson().fromJson(jsonStr, MsgBody_RecReport.class);
		System.out.println(msgBody.getInParam().getReportNo());
	}
}
