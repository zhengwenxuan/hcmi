package com.hjw.webService.client.jsjg.bean.in;

import com.google.gson.Gson;

public class MsgBody_RequestFrom {

	private InParam_RequestFrom InParam;

	public InParam_RequestFrom getInParam() {
		return InParam;
	}

	public void setInParam(InParam_RequestFrom InParam) {
		this.InParam = InParam;
	}
	
	public static void main(String[] args) {
		String jsonStr = "{\"InParam\": {\"PatientCode\": \" S001234\",\"PatientCodeType\": \"ZYH\"}}";
//		JSONObject jsonobject = JSONObject.fromObject(jsonStr.trim());
//		MsgBody msgBody = (MsgBody)JSONObject.toBean(jsonobject, MsgBody.class);
//		System.out.println(msgBody.getInParam().getPatientCode());
		
		MsgBody_RequestFrom msgBody = new Gson().fromJson(jsonStr, MsgBody_RequestFrom.class);
		System.out.println(msgBody.getInParam().getPatientCode());
	}
}
