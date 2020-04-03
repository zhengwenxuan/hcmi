package com.hjw.webService.client.tj180.Bean;

public class QueueResSearchBean {
	private String SERIAL_NO="";
	private String REGEDIT_DATE_TIME="";
	private String CUSTOMER_ID="";
	private String CARD_NO="";
	private String RESERVE_ID="";
	private String PATIENT_ID="";
	private String CUSTOMER_NAME="";
	private String SEX="";
	private String WEDDED="";
	private String MOBILE="";
	private String QUEUE_NAME="";
	private String QUEUE_IDENTIFY="";
	private String STATUS="";
	private String EXAMED_ROOM="";
	private String RE_EXAM_DATETIME="";
	private String MEMO="";
	private String QUEUE_TYPE="";
	private String CHARGE_TYPE="";
	private String RESERVE_TYPE="";
	private String ORG_RESERVE_ID="";
	private String ORGANIZATION_ID="";
	private String ORGANIZATION_NAME="";
	private String AHEAD_DATE_TIME="";
	private String AHEAD_OPERATOR="";
	private String AHEAD_INDICATOR="";
	private String sexname="";
	private String statuss="";	
	
	public String getSexname() {
		return sexname;
	}
	public void setSexname(String sexname) {
		this.sexname = sexname;
	}
	public String getStatuss() {
		return statuss;
	}
	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}
	public String getSERIAL_NO() {
		return SERIAL_NO;
	}
	public void setSERIAL_NO(String sERIAL_NO) {
		SERIAL_NO = sERIAL_NO;
	}
	public String getREGEDIT_DATE_TIME() {
		return REGEDIT_DATE_TIME;
	}
	public void setREGEDIT_DATE_TIME(String rEGEDIT_DATE_TIME) {
		REGEDIT_DATE_TIME = rEGEDIT_DATE_TIME;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}
	public String getCARD_NO() {
		return CARD_NO;
	}
	public void setCARD_NO(String cARD_NO) {
		CARD_NO = cARD_NO;
	}
	public String getRESERVE_ID() {
		return RESERVE_ID;
	}
	public void setRESERVE_ID(String rESERVE_ID) {
		RESERVE_ID = rESERVE_ID;
	}
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getCUSTOMER_NAME() {
		return CUSTOMER_NAME;
	}
	public void setCUSTOMER_NAME(String cUSTOMER_NAME) {
		CUSTOMER_NAME = cUSTOMER_NAME;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
		if("1".equals(SEX)){
			this.setSexname("男");
		}else if("2".equals(SEX)){
			this.setSexname("女");
		}else{
			this.setSexname("未知");
		}
	}
	public String getWEDDED() {
		return WEDDED;
	}
	public void setWEDDED(String wEDDED) {
		WEDDED = wEDDED;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getQUEUE_NAME() {
		return QUEUE_NAME;
	}
	public void setQUEUE_NAME(String qUEUE_NAME) {
		QUEUE_NAME = qUEUE_NAME;
	}
	public String getQUEUE_IDENTIFY() {
		return QUEUE_IDENTIFY;
	}
	public void setQUEUE_IDENTIFY(String qUEUE_IDENTIFY) {
		QUEUE_IDENTIFY = qUEUE_IDENTIFY;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
		if("1".equals(STATUS)){
			this.setStatuss("已检");
		}else if("0".equals(STATUS)){
			this.setStatuss("未检");
		}else{
			this.setStatuss("未知");
		}
	}
	public String getEXAMED_ROOM() {
		return EXAMED_ROOM;
	}
	public void setEXAMED_ROOM(String eXAMED_ROOM) {
		EXAMED_ROOM = eXAMED_ROOM;
	}
	public String getRE_EXAM_DATETIME() {
		return RE_EXAM_DATETIME;
	}
	public void setRE_EXAM_DATETIME(String rE_EXAM_DATETIME) {
		RE_EXAM_DATETIME = rE_EXAM_DATETIME;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	public String getQUEUE_TYPE() {
		return QUEUE_TYPE;
	}
	public void setQUEUE_TYPE(String qUEUE_TYPE) {
		QUEUE_TYPE = qUEUE_TYPE;
	}
	public String getCHARGE_TYPE() {
		return CHARGE_TYPE;
	}
	public void setCHARGE_TYPE(String cHARGE_TYPE) {
		CHARGE_TYPE = cHARGE_TYPE;
	}
	public String getRESERVE_TYPE() {
		return RESERVE_TYPE;
	}
	public void setRESERVE_TYPE(String rESERVE_TYPE) {
		RESERVE_TYPE = rESERVE_TYPE;
	}
	public String getORG_RESERVE_ID() {
		return ORG_RESERVE_ID;
	}
	public void setORG_RESERVE_ID(String oRG_RESERVE_ID) {
		ORG_RESERVE_ID = oRG_RESERVE_ID;
	}
	public String getORGANIZATION_ID() {
		return ORGANIZATION_ID;
	}
	public void setORGANIZATION_ID(String oRGANIZATION_ID) {
		ORGANIZATION_ID = oRGANIZATION_ID;
	}
	public String getORGANIZATION_NAME() {
		return ORGANIZATION_NAME;
	}
	public void setORGANIZATION_NAME(String oRGANIZATION_NAME) {
		ORGANIZATION_NAME = oRGANIZATION_NAME;
	}
	public String getAHEAD_DATE_TIME() {
		return AHEAD_DATE_TIME;
	}
	public void setAHEAD_DATE_TIME(String aHEAD_DATE_TIME) {
		AHEAD_DATE_TIME = aHEAD_DATE_TIME;
	}
	public String getAHEAD_OPERATOR() {
		return AHEAD_OPERATOR;
	}
	public void setAHEAD_OPERATOR(String aHEAD_OPERATOR) {
		AHEAD_OPERATOR = aHEAD_OPERATOR;
	}
	public String getAHEAD_INDICATOR() {
		return AHEAD_INDICATOR;
	}
	public void setAHEAD_INDICATOR(String aHEAD_INDICATOR) {
		AHEAD_INDICATOR = aHEAD_INDICATOR;
	}

}
