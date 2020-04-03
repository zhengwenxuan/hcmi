package com.hjw.webService.client.body;

public class FeeResultWxAndZfbBody {

	private String resultCode;//处理结果代码： 0：支付成功，其他状态为失败
	
	private String resultDesc;//处理结果描述
	private String orderId;//移动服务平台订单号（resultCode为0时有值）
	private String tradeNo;//第三方支付的交易流水号
	private String payTime;//交易时间，格式：YYYY-MM-DD HI24:MI:SS
	private String payAmout;//实际支付金额(单位：分)
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayAmout() {
		return payAmout;
	}
	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}
	
	
	
}
