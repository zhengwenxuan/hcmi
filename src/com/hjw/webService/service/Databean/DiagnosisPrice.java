package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
     * @Title:  临床诊疗项目与价表项目对照表  
     * @Package com.hjw.webService.service.Databean   
     * @Description:    TODO(用一句话描述该文件做什么)   
     * @author: yangm     
     * @date:   2016年10月31日 上午11:13:39   
     * @version V3.0.0.0
 */

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})  
public class DiagnosisPrice {
	private String action="";//	操作类型，取值范围（插入更新标志  1插入 2更新 0 删除）
	private String itemVersion="";//	数据版本号
	private String clinic_item_class="";//	临床诊疗项目类别
	private String clinic_item_code="";//	临床诊疗项目代码
	private int charge_item_no;//	对应价表项目序号
	private String charge_item_class="";//	对应价表项目分类
	private String charge_item_code="";//	对应价表项目代码
	private String charge_item_spec="";//	对应价表项目规格
	private long amount;//	对应价表项目数量
	private String units="";//	对应价表项目单位
	private String backbill_rule="";//	对应的计价规则

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
	public String getClinic_item_class() {
		return clinic_item_class;
	}
	public void setClinic_item_class(String clinic_item_class) {
		this.clinic_item_class = clinic_item_class;
	}
	public String getClinic_item_code() {
		return clinic_item_code;
	}
	public void setClinic_item_code(String clinic_item_code) {
		this.clinic_item_code = clinic_item_code;
	}
	public int getCharge_item_no() {
		return charge_item_no;
	}
	public void setCharge_item_no(int charge_item_no) {
		this.charge_item_no = charge_item_no;
	}
	public String getCharge_item_class() {
		return charge_item_class;
	}
	public void setCharge_item_class(String charge_item_class) {
		this.charge_item_class = charge_item_class;
	}
	public String getCharge_item_code() {
		return charge_item_code;
	}
	public void setCharge_item_code(String charge_item_code) {
		this.charge_item_code = charge_item_code;
	}
	public String getCharge_item_spec() {
		return charge_item_spec;
	}
	public void setCharge_item_spec(String charge_item_spec) {
		this.charge_item_spec = charge_item_spec;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getBackbill_rule() {
		return backbill_rule;
	}
	public void setBackbill_rule(String backbill_rule) {
		this.backbill_rule = backbill_rule;
	}

}
