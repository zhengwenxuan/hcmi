package com.hjw.webService.client.QHYHWX.bean;

import java.util.ArrayList;
import java.util.List;

public class WeiXinSetDTO {

	private String statsu;  //0 1
	private String errorMsg;//成功 失败
	private List<WeiXinPackgesDTO> packages = new ArrayList<>();
	public String getStatsu() {
		return statsu;
	}
	public void setStatsu(String statsu) {
		this.statsu = statsu;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public List<WeiXinPackgesDTO> getPackages() {
		return packages;
	}
	public void setPackages(List<WeiXinPackgesDTO> packages) {
		this.packages = packages;
	}
	
	
	
	
	
	
}
