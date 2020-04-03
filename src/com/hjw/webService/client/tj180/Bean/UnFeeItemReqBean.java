package com.hjw.webService.client.tj180.Bean;

public class UnFeeItemReqBean{
	private long exam_id;
	private String charging_status="";
	private long charging_item_id;
	private double amount1;
	private String item_code="";
	
	private String his_num="";
	private String hiscodeClass="";//1：价表项目，2：诊疗项目
	private String hisitemClass="";//his项目类型
	private String hisitemCode="";//his项目编码
	private String hisitemSpec="";//	收费项目规格 codeClass=2时，””
	private String hisunits="";//	收费项目单位	codeClass=2时，””

	public String getHisitemClass() {
		return hisitemClass;
	}
	public void setHisitemClass(String hisitemClass) {
		this.hisitemClass = hisitemClass;
	}
	public String getHisitemCode() {
		return hisitemCode;
	}
	public void setHisitemCode(String hisitemCode) {
		this.hisitemCode = hisitemCode;
	}
	public String getHisitemSpec() {
		return hisitemSpec;
	}
	public void setHisitemSpec(String hisitemSpec) {
		this.hisitemSpec = hisitemSpec;
	}
	public String getHisunits() {
		return hisunits;
	}
	public void setHisunits(String hisunits) {
		this.hisunits = hisunits;
	}
	public long getExam_id() {
		return exam_id;
	}
	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}
	public String getCharging_status() {
		return charging_status;
	}
	public void setCharging_status(String charging_status) {
		this.charging_status = charging_status;
	}
	public long getCharging_item_id() {
		return charging_item_id;
	}
	public void setCharging_item_id(long charging_item_id) {
		this.charging_item_id = charging_item_id;
	}
	public double getAmount1() {
		return amount1;
	}
	public void setAmount1(double amount1) {
		this.amount1 = amount1;
	}
	public String getHis_num() {
		return his_num;
	}
	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}
	public String getHiscodeClass() {
		return hiscodeClass;
	}
	public void setHiscodeClass(String hiscodeClass) {
		this.hiscodeClass = hiscodeClass;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	
}
