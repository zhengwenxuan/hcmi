package com.hjw.webService.client.dashiqiao.FeeResBean;

public class ResFeeStatusBeanDSQ {
	private String code="AE";   //
	private String persionid="";//门诊号
	private String reqno="";//收费申请号
	private String status="";//收费状态
	private String messageId="";
	private String text="";
	private String yizhuId="";
	private String patienttypeCode="";
	
	private String exam_num;//体检号
	private String his_num;//诊疗编码
	private String item_code;//体检系统收费编码
	private String charging_item_id;//体检系统收费项目id
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPersionid() {
		return persionid;
	}
	public void setPersionid(String persionid) {
		this.persionid = persionid;
	}
	public String getReqno() {
		return reqno;
	}
	public void setReqno(String reqno) {
		this.reqno = reqno;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getYizhuId() {
		return yizhuId;
	}
	public void setYizhuId(String yizhuId) {
		this.yizhuId = yizhuId;
	}
	public String getPatienttypeCode() {
		return patienttypeCode;
	}
	public void setPatienttypeCode(String patienttypeCode) {
		this.patienttypeCode = patienttypeCode;
	}
	
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public void setCharging_item_id(String charging_item_id) {
		this.charging_item_id = charging_item_id;
	}
	public String getHis_num() {
		return his_num;
	}
	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getCharging_item_id() {
		return charging_item_id;
	}
	
}
