package com.hjw.webService.client.tj180.Bean;

public class HisRessendItemBean{
	private boolean status = false;// true表示入库成功  false表示不成功
	private String unionProjectId="";//	已收费体检项目编码    
	private String errorinfo;//错误原因
	
	public String getErrorinfo() {
		return errorinfo;
	}
	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}

}
