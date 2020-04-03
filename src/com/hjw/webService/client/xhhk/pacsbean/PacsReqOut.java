package com.hjw.webService.client.xhhk.pacsbean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class PacsReqOut {

	private String ADM_ID = "";//体检号
	private String HISID = "";//体检号
	private String PATNAME = "";//姓名
	private String SEX = "";//性别
	private String PAT_AGE = "";//年龄
	private String BIRTHDATE = "";//生日
	private String TELEPHONE = "";//联系电话
	private List<ItemsApplyPacsXHHK> Items = new ArrayList<>();
	
	public static void main(String[] args) {
		PacsReqOut pacsReq = new PacsReqOut();
		List<ItemsApplyPacsXHHK> Items = new ArrayList<>();
		Items.add(new ItemsApplyPacsXHHK());
		Items.add(new ItemsApplyPacsXHHK());
		pacsReq.setItems(Items);
		String json = new Gson().toJson(pacsReq, PacsReqOut.class);
		System.out.println(json);
	}

	public String getADM_ID() {
		return ADM_ID;
	}

	public String getHISID() {
		return HISID;
	}

	public String getPATNAME() {
		return PATNAME;
	}

	public String getSEX() {
		return SEX;
	}

	public String getPAT_AGE() {
		return PAT_AGE;
	}

	public String getBIRTHDATE() {
		return BIRTHDATE;
	}

	public String getTELEPHONE() {
		return TELEPHONE;
	}

	public void setADM_ID(String aDM_ID) {
		ADM_ID = aDM_ID;
	}

	public void setHISID(String hISID) {
		HISID = hISID;
	}

	public void setPATNAME(String pATNAME) {
		PATNAME = pATNAME;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public void setPAT_AGE(String pAT_AGE) {
		PAT_AGE = pAT_AGE;
	}

	public void setBIRTHDATE(String bIRTHDATE) {
		BIRTHDATE = bIRTHDATE;
	}

	public void setTELEPHONE(String tELEPHONE) {
		TELEPHONE = tELEPHONE;
	}

	public List<ItemsApplyPacsXHHK> getItems() {
		return Items;
	}

	public void setItems(List<ItemsApplyPacsXHHK> items) {
		Items = items;
	}
}
