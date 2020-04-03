package com.hjw.webService.client.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThridInterfaceLog  implements java.io.Serializable{

	private static final long serialVersionUID = 4783399711232352000L;

	private String id="";
	
	private String req_no="";//申请号
	
	private String exam_no="";//体检编号
	
	private String message_id="";//消息唯一ID(外部系统用于标识消息)
	
	private String message_name="";//接口名称
	
	private String message_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());//消息时间
	
	private String message_type="";//消息类型( WebService/MQ 等 )
	
	private String sender="";//发送者( PEIS/HIS/PACS/LIS )
	
	private String receiver="";//接收者( PEIS/HIS/PACS/LIS )
	
	private String message_request="";//消息请求原文
	
	private String message_response="";//消息应答原文
	
	private int flag;//消息处理状态，0-正常 1-拒绝 2-错误
	
	private String sys_request="";//主系统请求数据原文
	
	private String sys_respones="";//返回主系统数据原文
	
	private String xtgnb_id="0";//主系统调用模块编号
	
	private int message_inout;//调用方式 0 出 1 进
	
	private int message_seq_code = 0;//	顺序号
	
	public int getMessage_seq_code() {
		message_seq_code++;
		return message_seq_code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReq_no() {
		return req_no;
	}

	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}

	public String getExam_no() {
		return exam_no;
	}

	public void setExam_no(String exam_no) {
		this.exam_no = exam_no;
	}

	public String getMessage_id() {
		return message_id;
	}

	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}

	public String getMessage_name() {
		return message_name;
	}

	public void setMessage_name(String message_name) {
		this.message_name = message_name;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage_request() {
		return message_request;
	}

	public void setMessage_request(String message_request) {
		this.message_request = message_request;
	}

	public String getMessage_response() {
		return message_response;
	}

	public void setMessage_response(String message_response) {
		this.message_response = message_response;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getSys_request() {
		return sys_request;
	}

	public void setSys_request(String sys_request) {
		this.sys_request = sys_request;
	}

	public String getSys_respones() {
		return sys_respones;
	}

	public void setSys_respones(String sys_respones) {
		this.sys_respones = sys_respones;
	}

	public String getXtgnb_id() {
		return xtgnb_id;
	}

	public void setXtgnb_id(String xtgnb_id) {
		this.xtgnb_id = xtgnb_id;
	}

	public int getMessage_inout() {
		return message_inout;
	}

	public void setMessage_inout(int message_inout) {
		this.message_inout = message_inout;
	}

	public String getMessage_date() {
		return message_date;
	}

//	public void setMessage_date(String message_date) {
//		this.message_date = message_date;
//	}
	
}
