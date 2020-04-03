package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

public class PacsResBean{	
	private String status = ""; //返回状态码	正常：200，参数异常400，未知错误: 500
	private String errorinfo = "";
	private List<PacsResItemBean> examItemsInfo = new ArrayList<PacsResItemBean>();
    private String examItemsNum="";
    
	public List<PacsResItemBean> getExamItemsInfo() {
		return examItemsInfo;
	}

	public void setExamItemsInfo(List<PacsResItemBean> examItemsInfo) {
		this.examItemsInfo = examItemsInfo;
	}

	public String getExamItemsNum() {
		return examItemsNum;
	}

	public void setExamItemsNum(String examItemsNum) {
		this.examItemsNum = examItemsNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorinfo() {
		return errorinfo;
	}

	public void setErrorinfo(String errorinfo) {
		this.errorinfo = errorinfo;
	}

}
