package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class MSGReqBean{
	private String MSG_CONTENT="";//	短信内容
	private String DB_USER="";//	发送人登录用户名 	如：WYY
	private String USER_NAME="";//	发送人用户名对应姓名 	如：吴雅云
	private String DEPT_NAME="健康体检中心";//	发送科室 	如：健康体检中心（可写死）
	private String SRC_PHONE="JKGL";//	发送电话号码源 	如：JKGL（可写死）
	private List<String> PHONE_LIST=new ArrayList<String>();//	发送的手机组 	如：[‘13245678901’,’78952456212’,…]
	private String sms_tiem;//预约发送时间
	private int sms_type;//是否立即发送     1立即发送  0预约发送
	
	public int getSms_type() {
		return sms_type;
	}
	public void setSms_type(int sms_type) {
		this.sms_type = sms_type;
	}
	public String getMSG_CONTENT() {
		return MSG_CONTENT;
	}
	public void setMSG_CONTENT(String mSG_CONTENT) {
		MSG_CONTENT = mSG_CONTENT;
	}

	public String getSms_tiem() {
		return sms_tiem;
	}

	public void setSms_tiem(String sms_tiem) {
		this.sms_tiem = sms_tiem;
	}

	public String getDB_USER() {
		return DB_USER;
	}
	public void setDB_USER(String dB_USER) {
		DB_USER = dB_USER;
	}
	public String getUSER_NAME() {
		return USER_NAME;
	}
	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}
	public String getDEPT_NAME() {
		return DEPT_NAME;
	}
	public void setDEPT_NAME(String dEPT_NAME) {
		DEPT_NAME = dEPT_NAME;
	}
	public String getSRC_PHONE() {
		return SRC_PHONE;
	}
	public void setSRC_PHONE(String sRC_PHONE) {
		SRC_PHONE = sRC_PHONE;
	}
	public List<String> getPHONE_LIST() {
		return PHONE_LIST;
	}
	public void setPHONE_LIST(List<String> pHONE_LIST) {
		PHONE_LIST = pHONE_LIST;
	}
}
