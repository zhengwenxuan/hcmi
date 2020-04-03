package com.hjw.webService.client.tj180.Bean;

public class LisPacsResItemBean{
	private String unionProjectId="";//	已收费体检项目编码    
	private String errorinfo="";//	生成检验流水号
	private boolean status=false;//	
	private String testNo="";//	生成检验流水号
	private String examineType="";//	检查类别 检验：1，检查：2，
	
	public String getTestNo() {
		return testNo;
	}
	public void setTestNo(String testNo) {
		this.testNo = testNo;
	}
	public String getExamineType() {
		return examineType;
	}
	public void setExamineType(String examineType) {
		this.examineType = examineType;
	}
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
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
	
}
