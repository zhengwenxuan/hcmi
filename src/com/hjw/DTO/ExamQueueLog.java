package com.hjw.DTO;

import com.hjw.util.DateTimeUtil;

public class ExamQueueLog {

	private String exam_num = "";
	private String queue_no = "";
	private String dept_num = "";
	private String queue_day = DateTimeUtil.getDate();
	private String queue_date = DateTimeUtil.getDateTime();
	
	public String getExam_num() {
		return exam_num;
	}
	public String getQueue_no() {
		return queue_no;
	}
	public String getDept_num() {
		return dept_num;
	}
	public String getQueue_day() {
		return queue_day;
	}
	public String getQueue_date() {
		return queue_date;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public void setQueue_no(String queue_no) {
		this.queue_no = queue_no;
	}
	public void setDept_num(String dept_num) {
		this.dept_num = dept_num;
	}
	public void setQueue_day(String queue_day) {
		this.queue_day = queue_day;
	}
	public void setQueue_date(String queue_date) {
		this.queue_date = queue_date;
	}
}
