package com.hjw.webService.client.changan.Bean;

public class MSGSureTempalteResBean {

	private int status= -1; //返回码
	private String desc= ""; //结果描述
	private String smsId=""; //唯一短信标识，有智语平台生成(32位)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSmsId() {
		return smsId;
	}
	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}
	
}
