package com.hjw.webService.client.hokai305.bean;

public class LisReqCustomerMessage305 {

	
	private String req_no="";//申请单号
	private String zy_id;//住院号
	private String jz_id;//急诊号
	private String mz_id;//门诊号
	private String barcode_id;//条码id
	private String id_no;//身份证号
	private String yihao_id;//医保卡号
	private String PATIENT_ID;//	患者ID
	private String jzk_id;//就诊卡号
	private String EXAM_NUM;//	体检号
	private String code="AE";//
	private String examtype; //体检类型  个人 团体
	private String comname;//单位名称
	private String comid;//单位id
	private String message;
	
	
	
	public String getExamtype() {
		return examtype;
	}
	public void setExamtype(String examtype) {
		this.examtype = examtype;
	}
	public String getComname() {
		return comname;
	}
	public void setComname(String comname) {
		this.comname = comname;
	}
	public String getComid() {
		return comid;
	}
	public void setComid(String comid) {
		this.comid = comid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReq_no() {
		return req_no;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public String getZy_id() {
		return zy_id;
	}
	public void setZy_id(String zy_id) {
		this.zy_id = zy_id;
	}
	public String getJz_id() {
		return jz_id;
	}
	public void setJz_id(String jz_id) {
		this.jz_id = jz_id;
	}
	public String getMz_id() {
		return mz_id;
	}
	public void setMz_id(String mz_id) {
		this.mz_id = mz_id;
	}
	public String getBarcode_id() {
		return barcode_id;
	}
	public void setBarcode_id(String barcode_id) {
		this.barcode_id = barcode_id;
	}
	public String getId_no() {
		return id_no;
	}
	public void setId_no(String id_no) {
		this.id_no = id_no;
	}
	public String getYihao_id() {
		return yihao_id;
	}
	public void setYihao_id(String yihao_id) {
		this.yihao_id = yihao_id;
	}
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getJzk_id() {
		return jzk_id;
	}
	public void setJzk_id(String jzk_id) {
		this.jzk_id = jzk_id;
	}
	public String getEXAM_NUM() {
		return EXAM_NUM;
	}
	public void setEXAM_NUM(String eXAM_NUM) {
		EXAM_NUM = eXAM_NUM;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
