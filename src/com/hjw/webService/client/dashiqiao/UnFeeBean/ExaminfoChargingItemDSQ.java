package com.hjw.webService.client.dashiqiao.UnFeeBean;

public class ExaminfoChargingItemDSQ implements java.io.Serializable{

	private static final long serialVersionUID = -97502163798576023L;
	
	private long id;

	private long examinfo_id;

	private long charge_item_id;

	private String exam_indicator;
	private String exam_indicators;
	
	private String item_name;
	
	private String item_code;
	
	private double item_amount;

	private double discount;

	private double amount;

	private String isActive;

	private String final_exam_date;

	private String pay_status;//结算状态 
	private String pay_statuss;

	private String exam_status;//检查状态
	private String exam_statuss;

	private long is_new_added;

	private String exam_date;

	private long creater;

	private String create_time;

	private long updater;

	private String update_time;

	private long check_status;

	private long exam_doctor_id;

	private String exam_doctor_name;

	private String add_status;

	private double calculation_amount;

	private String is_application;//是否发送申请
	private String is_applications;

	private String change_item;

	private double team_pay;

	private double personal_pay;

	private String team_pay_status;//付费状态
    private String team_pay_statuss;
	private String item_type="";
	private long dep_id;
	private String dep_name;
	private String dep_category;
	private String his_req_status;
	private String his_req_statuss;
	
	private String sample_status;
	private String sample_statuss;
	private long sample_id;
	private int itemnum=1;
	private String his_num;
	private int calculation_rate;
	private long item_discount;
	
	private String exam_num;
	private String patient_id;
	private String item_abbreviation;
	
	
	
    
    public String getItem_abbreviation() {
		return item_abbreviation;
	}

	public void setItem_abbreviation(String item_abbreviation) {
		this.item_abbreviation = item_abbreviation;
	}

	public long getItem_discount() {
		return item_discount;
	}

	public void setItem_discount(long item_discount) {
		this.item_discount = item_discount;
	}
	
	public int getCalculation_rate() {
		return calculation_rate;
	}

	public void setCalculation_rate(int calculation_rate) {
		this.calculation_rate = calculation_rate;
	}

	public String getHis_num() {
		return his_num;
	}

	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}

	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}

	public int getItemnum() {
		return itemnum;
	}

	public void setItemnum(int itemnum) {
		this.itemnum = itemnum;
	}

	public String getExam_indicators() {
		return exam_indicators;
	}

	public void setExam_indicators(String exam_indicators) {
		this.exam_indicators = exam_indicators;
	}

	public String getPay_statuss() {
		return pay_statuss;
	}

	public void setPay_statuss(String pay_statuss) {
		this.pay_statuss = pay_statuss;
	}

	public String getExam_statuss() {
		return exam_statuss;
	}

	public void setExam_statuss(String exam_statuss) {
		this.exam_statuss = exam_statuss;
	}

	public String getIs_applications() {
		return is_applications;
	}

	public void setIs_applications(String is_applications) {
		this.is_applications = is_applications;
	}

	public String getTeam_pay_statuss() {
		return team_pay_statuss;
	}

	public void setTeam_pay_statuss(String team_pay_statuss) {
		this.team_pay_statuss = team_pay_statuss;
		
	}

	public long getDep_id() {
		return dep_id;
	}

	public void setDep_id(long dep_id) {
		this.dep_id = dep_id;
	}

	public String getDep_name() {
		return dep_name;
	}

	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExaminfo_id() {
		return examinfo_id;
	}

	public void setExaminfo_id(long examinfo_id) {
		this.examinfo_id = examinfo_id;
	}

	public long getCharge_item_id() {
		return charge_item_id;
	}

	public void setCharge_item_id(long charge_item_id) {
		this.charge_item_id = charge_item_id;
	}

	public String getExam_indicator() {
		return exam_indicator;
	}

	public void setExam_indicator(String exam_indicator) {
		this.exam_indicator = exam_indicator;
		//G个人付费，T团体付费，GT混合，M免费
		if(exam_indicator==null){
			this.setExam_indicators("未知");
		}else if("G".equals(exam_indicator)){
			this.setExam_indicators("个人付费");
		}else if("T".equals(exam_indicator)){
			this.setExam_indicators("团体付费");
		}else if("M".equals(exam_indicator)){
			this.setExam_indicators("免费");
		}else if("GT".equals(exam_indicator)){
			this.setExam_indicators("混合付费");
		}else if("TG".equals(exam_indicator)){
			this.setExam_indicators("混合付费");
		}else {
			this.setExam_indicators("未知");
		}
	}

	public double getItem_amount() {
		return item_amount;
	}

	public void setItem_amount(double item_amount) {
		this.item_amount = item_amount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getFinal_exam_date() {
		return final_exam_date;
	}

	public void setFinal_exam_date(String final_exam_date) {
		this.final_exam_date = final_exam_date;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
		//个人（N未付费，Y 已付费）团体（R 预付费）
		if(pay_status==null){
			this.setPay_statuss("未知");
		}else if("Y".equals(pay_status)){
			this.setPay_statuss("已付费");
		}else if("R".equals(pay_status)){
			this.setPay_statuss("预付费");
		}else if("N".equals(pay_status)){
			this.setPay_statuss("未付费");
		}else {
			this.setPay_statuss("未知");
		}
	}

	public String getExam_status() {
		return exam_status;
	}

	public void setExam_status(String exam_status) {
		this.exam_status = exam_status;
		//N未检查 Y已检查 G弃检 C已登记，D延期
		if(exam_status==null){
			this.setExam_statuss("未知");
		}else if("Y".equals(exam_status)){
			this.setExam_statuss("已检查");
		}else if("G".equals(exam_status)){
			this.setExam_statuss("弃检");
		}else if("C".equals(exam_status)){
			this.setExam_statuss("已登记");
		}else if("D".equals(exam_status)){
			this.setExam_statuss("延期");
		}else if("N".equals(exam_status)){
			this.setExam_statuss("未检查");
		}else {
			this.setExam_statuss("未知");
		}
	}

	public long getIs_new_added() {
		return is_new_added;
	}

	public void setIs_new_added(long is_new_added) {
		this.is_new_added = is_new_added;
	}

	public String getExam_date() {
		return exam_date;
	}

	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}

	public long getCreater() {
		return creater;
	}

	public void setCreater(long creater) {
		this.creater = creater;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public long getUpdater() {
		return updater;
	}

	public void setUpdater(long updater) {
		this.updater = updater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public long getCheck_status() {
		return check_status;
	}

	public void setCheck_status(long check_status) {
		this.check_status = check_status;
	}

	public long getExam_doctor_id() {
		return exam_doctor_id;
	}

	public void setExam_doctor_id(long exam_doctor_id) {
		this.exam_doctor_id = exam_doctor_id;
	}

	public String getExam_doctor_name() {
		return exam_doctor_name;
	}

	public void setExam_doctor_name(String exam_doctor_name) {
		this.exam_doctor_name = exam_doctor_name;
	}

	public String getAdd_status() {
		return add_status;
	}

	public void setAdd_status(String add_status) {
		this.add_status = add_status;
	}

	public double getCalculation_amount() {
		return calculation_amount;
	}

	public void setCalculation_amount(double calculation_amount) {
		this.calculation_amount = calculation_amount;
	}

	public String getIs_application() {
		return is_application;
	}

	public void setIs_application(String is_application) {
		this.is_application = is_application;
		//N Y
		if(is_application==null){
			this.setIs_applications("未知");
		}else if("N".equals(is_application)){
			this.setIs_applications("未发");
		}else if("Y".equals(is_application)){
			this.setIs_applications("已发");
		}else{
			this.setIs_applications("未知");
		}
	}

	public String getChange_item() {
		return change_item;
	}

	public void setChange_item(String change_item) {
		this.change_item = change_item;
	}

	public double getTeam_pay() {
		return team_pay;
	}

	public void setTeam_pay(double team_pay) {
		this.team_pay = team_pay;
	}

	public double getPersonal_pay() {
		return personal_pay;
	}

	public void setPersonal_pay(double personal_pay) {
		this.personal_pay = personal_pay;
	}

	public String getTeam_pay_status() {
		return team_pay_status;
	}

	public void setTeam_pay_status(String team_pay_status) {
		this.team_pay_status = team_pay_status;
	}

	public String getDep_category() {
		return dep_category;
	}

	public void setDep_category(String dep_category) {
		this.dep_category = dep_category;
	}

	public String getSample_status() {
		return sample_status;
	}

	public void setSample_status(String sample_status) {
		this.sample_status = sample_status;
		if("W".equals(sample_status)){
			this.setSample_statuss("未采样");
		}else if("Y".equals(sample_status)){
			this.setSample_statuss("已采样");
		}else if("E".equals(sample_status)){
			this.setSample_statuss("已检查");
		}else if("H".equals(sample_status)){
			this.setSample_statuss("已核收");
		}
	}

	public String getSample_statuss() {
		return sample_statuss;
	}

	public void setSample_statuss(String sample_statuss) {
		this.sample_statuss = sample_statuss;
	}

	public long getSample_id() {
		return sample_id;
	}

	public void setSample_id(long sample_id) {
		this.sample_id = sample_id;
	}

	public String getHis_req_status() {
		return his_req_status;
	}

	public void setHis_req_status(String his_req_status) {
		this.his_req_status = his_req_status;
		if("Y".equals(his_req_status)){
			this.his_req_statuss = "已发";
		}else{
			this.his_req_statuss = "未发";
		}
	}

	public String getHis_req_statuss() {
		return his_req_statuss;
	}

	public void setHis_req_statuss(String his_req_statuss) {
		this.his_req_statuss = his_req_statuss;
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
	
	
}

