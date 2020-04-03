package com.hjw.DTO;

public class ZlReqItemDTO {
	private long id;
	private long exam_info_id;
	private String charging_item_id="";
	private String zl_pat_id="";
	private String lis_item_id="";
	private String req_id="";
	private String lis_req_code="";
	public long getExam_info_id() {
		return exam_info_id;
	}
	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}
	public String getCharging_item_id() {
		return charging_item_id;
	}
	public void setCharging_item_id(String charging_item_id) {
		this.charging_item_id = charging_item_id;
	}
	public String getZl_pat_id() {
		return zl_pat_id;
	}
	public void setZl_pat_id(String zl_pat_id) {
		this.zl_pat_id = zl_pat_id;
	}
	public String getLis_item_id() {
		return lis_item_id;
	}
	public void setLis_item_id(String lis_item_id) {
		this.lis_item_id = lis_item_id;
	}
	public String getReq_id() {
		return req_id;
	}
	public void setReq_id(String req_id) {
		this.req_id = req_id;
	}
	public String getLis_req_code() {
		return lis_req_code;
	}
	public void setLis_req_code(String lis_req_code) {
		this.lis_req_code = lis_req_code;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
