package com.hjw.webService.client.xhhk.pacsbean;

import com.google.gson.Gson;

public class PacsReqIn {

	private String HISID = "";

	public String getHISID() {
		return HISID;
	}

	public void setHISID(String hISID) {
		HISID = hISID;
	}
	
	public static void main(String[] args) {
		String json = "{\"HISID\":\"80201697\"}";
		PacsReqIn reqIn = new Gson().fromJson(json, PacsReqIn.class);
		System.out.println(reqIn.getHISID());
	}
}
