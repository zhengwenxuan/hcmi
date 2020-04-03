package com.hjw.webService.client.hghis.SMS;

public class SmsSendInfo {

	private String exam_num;//体检号
	private String send_type;//D 报道时,P 打印时
	private String create_date;//
	private String update_date;//
	private String is_send_fly;//短信是否发送
	private String is_active;//是否有效
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getIs_send_fly() {
		return is_send_fly;
	}
	public void setIs_send_fly(String is_send_fly) {
		this.is_send_fly = is_send_fly;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}
	
	
}
