package com.hjw.webService.client.zhonglian.bean;

public class ZLReqPatInfoBean {
	private long exam_info_id;
	private String exam_num="";
	private String zl_mzh="";
	private String zl_tjh="";
	private String zl_pat_id="";
	private int flag;
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getExam_info_id() {
		return exam_info_id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getZl_mzh() {
		return zl_mzh;
	}
	public void setZl_mzh(String zl_mzh) {
		this.zl_mzh = zl_mzh;
	}
	public String getZl_tjh() {
		return zl_tjh;
	}
	public void setZl_tjh(String zl_tjh) {
		this.zl_tjh = zl_tjh;
	}
	public String getZl_pat_id() {
		return zl_pat_id;
	}
	public void setZl_pat_id(String zl_pat_id) {
		this.zl_pat_id = zl_pat_id;
	}
	
}
