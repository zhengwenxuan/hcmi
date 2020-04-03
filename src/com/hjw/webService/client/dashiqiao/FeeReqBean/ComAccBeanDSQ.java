package com.hjw.webService.client.dashiqiao.FeeReqBean;

public class ComAccBeanDSQ {

	private String COMPANY_BATCH_NO="";//批次
	private String COMPANY_NAME="";//单位名称
	private String ACC_NUM="";//结算号
	private String ACCOUNT_NUM="";//体检申请单号
	private String TOTAL_GROSS1="";  //计算金额
	private String TOTAL_GROSS2="";	//实际金额
	private String CLINIC_NO="";//发票号 字段 大石桥 实际用于 绑定就诊号
	
	
	public String getACCOUNT_NUM() {
		return ACCOUNT_NUM;
	}
	public void setACCOUNT_NUM(String aCCOUNT_NUM) {
		ACCOUNT_NUM = aCCOUNT_NUM;
	}
	public String getCOMPANY_BATCH_NO() {
		return COMPANY_BATCH_NO;
	}
	public void setCOMPANY_BATCH_NO(String cOMPANY_BATCH_NO) {
		COMPANY_BATCH_NO = cOMPANY_BATCH_NO;
	}
	public String getCOMPANY_NAME() {
		return COMPANY_NAME;
	}
	public void setCOMPANY_NAME(String cOMPANY_NAME) {
		COMPANY_NAME = cOMPANY_NAME;
	}
	public String getACC_NUM() {
		return ACC_NUM;
	}
	public void setACC_NUM(String aCC_NUM) {
		ACC_NUM = aCC_NUM;
	}
	public String getTOTAL_GROSS1() {
		return TOTAL_GROSS1;
	}
	public void setTOTAL_GROSS1(String tOTAL_GROSS1) {
		TOTAL_GROSS1 = tOTAL_GROSS1;
	}
	public String getTOTAL_GROSS2() {
		return TOTAL_GROSS2;
	}
	public void setTOTAL_GROSS2(String tOTAL_GROSS2) {
		TOTAL_GROSS2 = tOTAL_GROSS2;
	}
	public String getCLINIC_NO() {
		return CLINIC_NO;
	}
	public void setCLINIC_NO(String cLINIC_NO) {
		CLINIC_NO = cLINIC_NO;
	}
			
	
	
}
