package com.hjw.DTO;
/**
 * 就诊卡
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.wst.DTO   
     * @Description:  
     * @author: yangm     
     * @date:   2018年1月3日 下午3:11:09   
     * @version V2.0.0.0
 */
public class MzghBaseInfoDTO {
	private long  OUTPATIENTNO;
	private String NAME;//姓名
	private int SEX;//性别
	private	String BIRTH;//出生日期
	private int AGE;//年龄
	private String ADDRESS;//地址
	private String LXFS;//电话
	private String IDCARDINFO;//身份证
	
	
	private String Typecode;
	private String Text;
	private String MessageID;
	
	
	
	
	public String getTypecode() {
		return Typecode;
	}
	public void setTypecode(String typecode) {
		Typecode = typecode;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public String getMessageID() {
		return MessageID;
	}
	public void setMessageID(String messageID) {
		MessageID = messageID;
	}
	public long getOUTPATIENTNO() {
		return OUTPATIENTNO;
	}
	public void setOUTPATIENTNO(long oUTPATIENTNO) {
		OUTPATIENTNO = oUTPATIENTNO;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getBIRTH() {
		return BIRTH;
	}
	public void setBIRTH(String bIRTH) {
		BIRTH = bIRTH;
	}
	
	public int getAGE() {
		return AGE;
	}
	public void setAGE(int aGE) {
		AGE = aGE;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getLXFS() {
		return LXFS;
	}
	public void setLXFS(String lXFS) {
		LXFS = lXFS;
	}
	public String getIDCARDINFO() {
		return IDCARDINFO;
	}
	public void setIDCARDINFO(String iDCARDINFO) {
		IDCARDINFO = iDCARDINFO;
	}
	
	public int getSEX() {
		return SEX;
	}
	public void setSEX(int sEX) {
		SEX = sEX;
	}
	@Override
	public String toString() {
		return "MzghBaseInfoDTO [OUTPATIENTNO=" + OUTPATIENTNO + ", NAME="
				+ NAME + ", SEX=" + SEX + ", BIRTH=" + BIRTH + ", AGE=" + AGE
				+ ", ADDRESS=" + ADDRESS + ", LXFS=" + LXFS + ", IDCARDINFO="
				+ IDCARDINFO + "]";
	}
	
}
