package com.hjw.webService.client.Bean;

public class SendReportReceive {
	private String reserveId="";//	体检预约号	String	是
	private String getType="1";//	领取方式 	1-本人；2-他人代领；3-邮寄	String	是
	private String rptOperator="";//	报告打印人	String	是
	private String rptPrintDate="";//	报告打印时间 Yyyy-mm-dd hh24:mi:ss	String	是
	private String rptGetOperator="";//	报告领取人 没有记录可传””	String	否
	private String sender="";//	报告发放人	String	是
	private String sendDateTime="";//	报告发放时间 	Yyyy-mm-dd hh24:mi:ss	String	是
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getGetType() {
		return getType;
	}
	public void setGetType(String getType) {
		this.getType = getType;
	}
	public String getRptOperator() {
		return rptOperator;
	}
	public void setRptOperator(String rptOperator) {
		this.rptOperator = rptOperator;
	}
	public String getRptPrintDate() {
		return rptPrintDate;
	}
	public void setRptPrintDate(String rptPrintDate) {
		this.rptPrintDate = rptPrintDate;
	}
	public String getRptGetOperator() {
		return rptGetOperator;
	}
	public void setRptGetOperator(String rptGetOperator) {
		this.rptGetOperator = rptGetOperator;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSendDateTime() {
		return sendDateTime;
	}
	public void setSendDateTime(String sendDateTime) {
		this.sendDateTime = sendDateTime;
	}

}
