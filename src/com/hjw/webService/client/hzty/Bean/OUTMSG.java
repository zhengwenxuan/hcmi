package com.hjw.webService.client.hzty.Bean;

public class OUTMSG {
	private String ERRNO="";// 错误编号   
	private String ERRMSG="";// 错误信息   
	private String ZHONGDUANJBH="";// 终端机编号   
	private String ZHONGDUANLSH="";// 	终端流水号   
	private String ERRMSG_YW="";// 错误信息_英文 
	public String getERRNO() {
		return ERRNO;
	}
	public void setERRNO(String eRRNO) {
		ERRNO = eRRNO;
	}
	public String getERRMSG() {
		return ERRMSG;
	}
	public void setERRMSG(String eRRMSG) {
		ERRMSG = eRRMSG;
	}
	public String getZHONGDUANJBH() {
		return ZHONGDUANJBH;
	}
	public void setZHONGDUANJBH(String zHONGDUANJBH) {
		ZHONGDUANJBH = zHONGDUANJBH;
	}
	public String getZHONGDUANLSH() {
		return ZHONGDUANLSH;
	}
	public void setZHONGDUANLSH(String zHONGDUANLSH) {
		ZHONGDUANLSH = zHONGDUANLSH;
	}
	public String getERRMSG_YW() {
		return ERRMSG_YW;
	}
	public void setERRMSG_YW(String eRRMSG_YW) {
		ERRMSG_YW = eRRMSG_YW;
	}
	
	
}
