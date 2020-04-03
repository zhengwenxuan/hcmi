package com.hjw.DTO;

public class ZlReqHisItemDTO {
	private long id;
	private String exam_num="";
	private String req_no="";
	private String charging_item_code="";
	private String price_list_code="";
	private String his_num="";
	private String his_req_no="";
	private String tmp1="";
	private String tmp2="";
	
	public long getId() {
		return id;
	}
	public String getExam_num() {
		return exam_num;
	}
	public String getReq_no() {
		return req_no;
	}
	public String getHis_num() {
		return his_num;
	}
	public String getHis_req_no() {
		return his_req_no;
	}
	public String getTmp1() {
		return tmp1;
	}
	public String getTmp2() {
		return tmp2;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}
	public void setHis_req_no(String his_req_no) {
		this.his_req_no = his_req_no;
	}
	public void setTmp1(String tmp1) {
		this.tmp1 = tmp1;
	}
	public void setTmp2(String tmp2) {
		this.tmp2 = tmp2;
	}
	public String getCharging_item_code() {
		return charging_item_code;
	}
	public String getPrice_list_code() {
		return price_list_code;
	}
	public void setCharging_item_code(String charging_item_code) {
		this.charging_item_code = charging_item_code;
	}
	public void setPrice_list_code(String price_list_code) {
		this.price_list_code = price_list_code;
	}
}
