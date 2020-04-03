package com.hjw.DTO;

import com.hjw.util.DateTimeUtil;

public class HisReqResult {

	private String req_no = "";// -- 申请单号
	private String message_id = "";//--消息唯一ID(外部系统用于标识消息)
	private String exam_num = "";//-- 体检编号
	private int item_nums;//-- 收费项目个数
	private int trans_flag = 2;//-- 发送状态:1成功，2失败
	private String create_time = DateTimeUtil.getDateTime();//-- 插入时间
	private String note = "";//-- 备注（失败原因）
	private String message_request = "";//-- 消息请求原文
	private String message_response = "";//--消息应答原文
	
	public String getReq_no() {
		return req_no;
	}
	public String getMessage_id() {
		return message_id;
	}
	public String getExam_num() {
		return exam_num;
	}
	public int getItem_nums() {
		return item_nums;
	}
	public int getTrans_flag() {
		return trans_flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public String getNote() {
		return note;
	}
	public String getMessage_request() {
		return message_request;
	}
	public String getMessage_response() {
		return message_response;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public void setItem_nums(int item_nums) {
		this.item_nums = item_nums;
	}
	public void setTrans_flag(int trans_flag) {
		this.trans_flag = trans_flag;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setMessage_request(String message_request) {
		this.message_request = message_request;
	}
	public void setMessage_response(String message_response) {
		this.message_response = message_response;
	}
}
