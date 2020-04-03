package com.hjw.webService.client.tj180.Bean;

public class HisLabSampleResBean {

	private String UNIONPROJECTID;//项目编码
	private String SAMPLENO;//标本序号
	private String BARCODE;//条码号
	private String SAMPLEDATETIME;//打条码时间
	private String OPERATOR;//采集人
	private String TESTNO;//检验流水号
	public String getUNIONPROJECTID() {
		return UNIONPROJECTID;
	}
	public void setUNIONPROJECTID(String uNIONPROJECTID) {
		UNIONPROJECTID = uNIONPROJECTID;
	}
	public String getSAMPLENO() {
		return SAMPLENO;
	}
	public void setSAMPLENO(String sAMPLENO) {
		SAMPLENO = sAMPLENO;
	}
	public String getBARCODE() {
		return BARCODE;
	}
	public void setBARCODE(String bARCODE) {
		BARCODE = bARCODE;
	}
	public String getSAMPLEDATETIME() {
		return SAMPLEDATETIME;
	}
	public void setSAMPLEDATETIME(String sAMPLEDATETIME) {
		SAMPLEDATETIME = sAMPLEDATETIME;
	}
	public String getOPERATOR() {
		return OPERATOR;
	}
	public void setOPERATOR(String oPERATOR) {
		OPERATOR = oPERATOR;
	}
	public String getTESTNO() {
		return TESTNO;
	}
	public void setTESTNO(String tESTNO) {
		TESTNO = tESTNO;
	}
}
