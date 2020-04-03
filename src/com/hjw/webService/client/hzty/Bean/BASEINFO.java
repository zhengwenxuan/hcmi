package com.hjw.webService.client.hzty.Bean;

public class BASEINFO {
	private String CAOZUOYDM = "";// 操作员代码 必传 由医院提供
	private String CAOZUOYXM = "";// 操作员姓名 必传
	private String CAOZUORQ = "";// 操作日期 必传
	private String XITONGBS = "";// 系统标识
	private String FENYUANDM = "";// 分院代码 必传 由医院提供
	private String JIGOUDM = "";// 机构代码 必传 发送机构， 由医院提供
	private String JIGOUMC = "";// 机构名称
	private String JIGOUYZM = "";// 机构验证码 必传 由医院提供，验证有效性(传入 时，需 MD5加密码)
	private String JIESHOUJGDM = "";// 接收机构代码
	private String ZHONGDUANJBH = "";// 终端机编号
	private String ZHONGDUANLSH = "";// 终端流水号
	private String IPADDRESS = "";//

	public String getCAOZUOYDM() {
		return CAOZUOYDM;
	}

	public void setCAOZUOYDM(String cAOZUOYDM) {
		CAOZUOYDM = cAOZUOYDM;
	}

	public String getCAOZUOYXM() {
		return CAOZUOYXM;
	}

	public void setCAOZUOYXM(String cAOZUOYXM) {
		CAOZUOYXM = cAOZUOYXM;
	}

	public String getCAOZUORQ() {
		return CAOZUORQ;
	}

	public void setCAOZUORQ(String cAOZUORQ) {
		CAOZUORQ = cAOZUORQ;
	}

	public String getXITONGBS() {
		return XITONGBS;
	}

	public void setXITONGBS(String xITONGBS) {
		XITONGBS = xITONGBS;
	}

	public String getFENYUANDM() {
		return FENYUANDM;
	}

	public void setFENYUANDM(String fENYUANDM) {
		FENYUANDM = fENYUANDM;
	}

	public String getJIGOUDM() {
		return JIGOUDM;
	}

	public void setJIGOUDM(String jIGOUDM) {
		JIGOUDM = jIGOUDM;
	}

	public String getJIGOUMC() {
		return JIGOUMC;
	}

	public void setJIGOUMC(String jIGOUMC) {
		JIGOUMC = jIGOUMC;
	}

	public String getJIGOUYZM() {
		return JIGOUYZM;
	}

	public void setJIGOUYZM(String jIGOUYZM) {
		JIGOUYZM = jIGOUYZM;
	}

	public String getJIESHOUJGDM() {
		return JIESHOUJGDM;
	}

	public void setJIESHOUJGDM(String jIESHOUJGDM) {
		JIESHOUJGDM = jIESHOUJGDM;
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

	public String getIPADDRESS() {
		return IPADDRESS;
	}

	public void setIPADDRESS(String iPADDRESS) {
		IPADDRESS = iPADDRESS;
	}

}
