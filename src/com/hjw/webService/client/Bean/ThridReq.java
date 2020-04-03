package com.hjw.webService.client.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ThridReq {

	private int id;
	private long exam_info_id;
	private String exam_num="";
	private String arch_num="";
	private String req_no="";
	private int readFlag;
	private String read_time="";
	private int creater;
	private String create_time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	private String databody="";
	private String notices="";
	private int errornum;
	private String ser_config_key="";
	
	public int getId() {
		return id;
	}
	public long getExam_info_id() {
		return exam_info_id;
	}
	public String getExam_num() {
		return exam_num;
	}
	public String getArch_num() {
		return arch_num;
	}
	public String getReq_no() {
		return req_no;
	}
	public int getReadFlag() {
		return readFlag;
	}
	public String getRead_time() {
		return read_time;
	}
	public int getCreater() {
		return creater;
	}
	public String getCreate_time() {
		return create_time;
	}
	public String getDatabody() {
		return databody;
	}
	public String getNotices() {
		return notices;
	}
	public int getErrornum() {
		return errornum;
	}
	public String getSer_config_key() {
		return ser_config_key;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public void setArch_num(String arch_num) {
		this.arch_num = arch_num;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}
	public void setRead_time(String read_time) {
		this.read_time = read_time;
	}
	public void setCreater(int creater) {
		this.creater = creater;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public void setDatabody(String databody) {
		this.databody = databody;
	}
	public void setNotices(String notices) {
		this.notices = notices;
	}
	public void setErrornum(int errornum) {
		this.errornum = errornum;
	}
	public void setSer_config_key(String ser_config_key) {
		this.ser_config_key = ser_config_key;
	}
}
