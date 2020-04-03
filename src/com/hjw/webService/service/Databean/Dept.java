package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
     * @Title:  
     * @Package com.hjw.webService.service.Databean   
     * @Description:    科室  
     * @author: yangm     
     * @date:   2016年10月31日 上午11:14:59   
     * @version V3.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})  
public class Dept {
	private String action;//	操作类型，取值范围（插入更新标志  1插入 2更新 0 删除）
	private String itemVersion;//	数据版本号
	private String serial_no;//	序号
	private String dept_code;//	科室代码
	private String dept_name;//	科室名称
	private String dept_alias;//	科室别名或全称
	private String clinic_attr;//	临床科室属性
	private String outp_or_inp;//	门诊住院科室标志
	private String internal_or_sergery;//	内外科标志
	private String input_code;//	拼音码
	private String position;//	位置
	private String input_code_wb;//	五笔码
	private String dispensing_cumulate;//	摆药累积属性
	private String virtual_cabinet;//	虚拟药柜
	private String order_code;//	号
	private String branch_code;//	科室所属分院编码
	private String big_dept_code;//	科室所属大科室代码，用于跨分院统计大科数据时使用
	private String create_user;//	创建人
	private String create_date;//	创建时间
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getItemVersion() {
		return itemVersion;
	}
	public void setItemVersion(String itemVersion) {
		this.itemVersion = itemVersion;
	}
	public String getSerial_no() {
		return serial_no;
	}
	public void setSerial_no(String serial_no) {
		this.serial_no = serial_no;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getDept_alias() {
		return dept_alias;
	}
	public void setDept_alias(String dept_alias) {
		this.dept_alias = dept_alias;
	}
	public String getClinic_attr() {
		return clinic_attr;
	}
	public void setClinic_attr(String clinic_attr) {
		this.clinic_attr = clinic_attr;
	}
	public String getOutp_or_inp() {
		return outp_or_inp;
	}
	public void setOutp_or_inp(String outp_or_inp) {
		this.outp_or_inp = outp_or_inp;
	}
	public String getInternal_or_sergery() {
		return internal_or_sergery;
	}
	public void setInternal_or_sergery(String internal_or_sergery) {
		this.internal_or_sergery = internal_or_sergery;
	}
	public String getInput_code() {
		return input_code;
	}
	public void setInput_code(String input_code) {
		this.input_code = input_code;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getInput_code_wb() {
		return input_code_wb;
	}
	public void setInput_code_wb(String input_code_wb) {
		this.input_code_wb = input_code_wb;
	}
	public String getDispensing_cumulate() {
		return dispensing_cumulate;
	}
	public void setDispensing_cumulate(String dispensing_cumulate) {
		this.dispensing_cumulate = dispensing_cumulate;
	}
	public String getVirtual_cabinet() {
		return virtual_cabinet;
	}
	public void setVirtual_cabinet(String virtual_cabinet) {
		this.virtual_cabinet = virtual_cabinet;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getBranch_code() {
		return branch_code;
	}
	public void setBranch_code(String branch_code) {
		this.branch_code = branch_code;
	}
	public String getBig_dept_code() {
		return big_dept_code;
	}
	public void setBig_dept_code(String big_dept_code) {
		this.big_dept_code = big_dept_code;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	

}
