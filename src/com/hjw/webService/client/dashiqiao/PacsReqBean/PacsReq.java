package com.hjw.webService.client.dashiqiao.PacsReqBean;

public class PacsReq {

	private String exam_num;// 体检号
	private String patient_id;// 患者id 对应平台就诊号
	private long chargingitemid;// 体检系统收费项目id
	private String his_num;// 诊疗码
	private String item_abbreviation;
	private String item_code;// 收费项目码
	private long eci_id;//
	private String doctorname;
	private String item_name;// 收费项目名称
	private double amount;// 收费项目价格
	private double item_amount;//
	private String pay_status;//
	private String hiscodeClass;// 诊疗类别
	private String pacs_req_code;// 体检系统检查申请号
	private String dep_num;// 检查科室名称
	private String clinic_no;
	private String exam_type;
	private String exam_indicator;
	
	
	

	public String getExam_indicator() {
		return exam_indicator;
	}

	public void setExam_indicator(String exam_indicator) {
		this.exam_indicator = exam_indicator;
	}
	

	
	

	public String getExam_type() {
		return exam_type;
	}

	public void setExam_type(String exam_type) {
		this.exam_type = exam_type;
	}

	public String getClinic_no() {
		return clinic_no;
	}

	public void setClinic_no(String clinic_no) {
		this.clinic_no = clinic_no;
	}
	
	

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public String getItem_abbreviation() {
		return item_abbreviation;
	}

	public void setItem_abbreviation(String item_abbreviation) {
		this.item_abbreviation = item_abbreviation;
	}

	public String getExam_num() {
		return exam_num;
	}

	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}

	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	public long getChargingitemid() {
		return chargingitemid;
	}

	public void setChargingitemid(long chargingitemid) {
		this.chargingitemid = chargingitemid;
	}

	public String getHis_num() {
		return his_num;
	}

	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public long getEci_id() {
		return eci_id;
	}

	public void setEci_id(long eci_id) {
		this.eci_id = eci_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getItem_amount() {
		return item_amount;
	}

	public void setItem_amount(double item_amount) {
		this.item_amount = item_amount;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getHiscodeClass() {
		return hiscodeClass;
	}

	public void setHiscodeClass(String hiscodeClass) {
		this.hiscodeClass = hiscodeClass;
	}

	public String getPacs_req_code() {
		return pacs_req_code;
	}

	public void setPacs_req_code(String pacs_req_code) {
		this.pacs_req_code = pacs_req_code;
	}

	public String getDep_num() {
		return dep_num;
	}

	public void setDep_num(String dep_num) {
		this.dep_num = dep_num;
	}

}
