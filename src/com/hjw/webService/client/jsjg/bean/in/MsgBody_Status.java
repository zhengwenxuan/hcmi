package com.hjw.webService.client.jsjg.bean.in;

import com.google.gson.Gson;

public class MsgBody_Status {

	private InParam_Status InParam;

	public InParam_Status getInParam() {
		return InParam;
	}

	public void setInParam(InParam_Status InParam) {
		this.InParam = InParam;
	}
	
	public static void main(String[] args) {
		String jsonStr = "{\"InParam\": {\"VisitType\":\"\",\"PatientNo\":\"\",\"PatientName\":\"\",\"ApplyID\":\"\",\"BarCode\":\"123456789\",\"Exec\":\"\",\"ExecTime\":\"\",\"ExecStatus\":\"\"}}";
//		JSONObject jsonobject = JSONObject.fromObject(jsonStr.trim());
//		MsgBody msgBody = (MsgBody)JSONObject.toBean(jsonobject, MsgBody.class);
//		System.out.println(msgBody.getInParam().getPatientCode());
		
		MsgBody_Status msgBody = new Gson().fromJson(jsonStr, MsgBody_Status.class);
		System.out.println(msgBody.getInParam().getBarCode());
	}
}
