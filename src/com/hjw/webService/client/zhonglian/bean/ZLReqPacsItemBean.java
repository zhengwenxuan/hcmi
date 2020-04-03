package com.hjw.webService.client.zhonglian.bean;

public class ZLReqPacsItemBean {
	private long exam_info_id;
	private String pacs_req_code="";
	private String charging_item_ids="";
	private String zl_pat_id="";
	private String zl_pacs_id="";
	private String req_id="";
	public long getExam_info_id() {
		return exam_info_id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public String getPacs_req_code() {
		return pacs_req_code;
	}
	public void setPacs_req_code(String pacs_req_code) {
		this.pacs_req_code = pacs_req_code;
	}
	public String getCharging_item_ids() {
		return charging_item_ids;
	}
	public void setCharging_item_ids(String charging_item_ids) {
		this.charging_item_ids = charging_item_ids;
	}
	public String getZl_pat_id() {
		return zl_pat_id;
	}
	public void setZl_pat_id(String zl_pat_id) {
		this.zl_pat_id = zl_pat_id;
	}
	public String getZl_pacs_id() {
		return zl_pacs_id;
	}
	public void setZl_pacs_id(String zl_pacs_id) {
		this.zl_pacs_id = zl_pacs_id;
	}
	public String getReq_id() {
		return req_id;
	}
	public void setReq_id(String req_id) {
		this.req_id = req_id;
	}

}
