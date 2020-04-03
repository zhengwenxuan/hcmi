package com.hjw.webService.client.tj180.Bean;

public class ExamTDeleteResBean{
	private String status = "";
	private String errorinfo = "";
    private String orgReserveId="";//	团队体检预约号  

	public String getOrgReserveId() {
		return orgReserveId;
	}

	public void setOrgReserveId(String orgReserveId) {
		this.orgReserveId = orgReserveId;
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
