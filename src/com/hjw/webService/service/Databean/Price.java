package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
     * @Title:  圈存查询一体机系统3.0   
     * @Package com.hjw.webService.service.Databean   
     * @Description:   价表   
     * @author: yangm     
     * @date:   2016年10月31日 上午11:14:22   
     * @version V3.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})  
public class Price {
	private String action="";//	操作类型，取值范围（insert、delete、update）
	private String itemVersion="";//	数据版本号
	private String item_class="";//	项目分类
	private String item_code="";//	项目代码
	private String item_name="";//	项目名称
	private String item_spec="";//	项目规格
	private String units="";//	单位
	private double price;//	价格
	private String prefer_price="";//	优惠价格
	private String foreigner_price="";//	外宾价格
	private String performed_by="";//	执行科室
	private String fee_type_mask="";//	费别屏蔽标志
	private String class_on_inp_rcpt="";//	对应的住院收据费用分类
	private String class_on_outp_rcpt="";//	对应的门诊收据费用分类
	private String class_on_reckoning="";//	对应的核算项目分类
	private String subj_code="";//	对应的会计科目
	private String class_on_mr="";//	对应的病案首页费用分类
	private String memo="";//	备注
	private String start_date="";//	起用日期
	private String stop_date="";//	停用日期
	private String operator="";//	操作员
	private String enter_date="";//	录入日期及时间
	private String high_price="";//	最高价格
	private String material_code="";//	物价编码
	private String control_flag="";//	控制标志
	private String input_code="";//	拼音码
	private String input_code_wb="";//	五笔码
	private String changed_memo="";//	价格变更原因包括调价和停用等都可以录入保存原因
	private String package_spec="";//	包装规格
	private String firm_id="";//	厂家标识
	private String license_id="";//	许可证编号
	private String charge_according="";//	收费依据
	private String mr_bill_class="";//	对应的2011版病案首页费用分类代码
	private String group_flag="";//	是否组合
	private String zyfrzfbz="";//	在押犯人自费标志，1 = 不参与优惠
	private String high_quality_supplies_sign="";//	高质耗材标志 0或空: 非高质耗材  1: 高质耗材
	private String xmdj="";//	1甲类，2乙类，3自费，4工伤，5生育，6甲类(仅限门诊)，7乙类(仅限门诊)，8甲类审批，9丙类
	private String focorner="";//	院内四角码
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
	public String getItem_class() {
		return item_class;
	}
	public void setItem_class(String item_class) {
		this.item_class = item_class;
	}
	public String getItem_code() {
		return item_code;
	}
	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_spec() {
		return item_spec;
	}
	public void setItem_spec(String item_spec) {
		this.item_spec = item_spec;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPrefer_price() {
		return prefer_price;
	}
	public void setPrefer_price(String prefer_price) {
		this.prefer_price = prefer_price;
	}
	public String getForeigner_price() {
		return foreigner_price;
	}
	public void setForeigner_price(String foreigner_price) {
		this.foreigner_price = foreigner_price;
	}
	public String getPerformed_by() {
		return performed_by;
	}
	public void setPerformed_by(String performed_by) {
		this.performed_by = performed_by;
	}
	public String getFee_type_mask() {
		return fee_type_mask;
	}
	public void setFee_type_mask(String fee_type_mask) {
		this.fee_type_mask = fee_type_mask;
	}
	public String getClass_on_inp_rcpt() {
		return class_on_inp_rcpt;
	}
	public void setClass_on_inp_rcpt(String class_on_inp_rcpt) {
		this.class_on_inp_rcpt = class_on_inp_rcpt;
	}
	public String getClass_on_outp_rcpt() {
		return class_on_outp_rcpt;
	}
	public void setClass_on_outp_rcpt(String class_on_outp_rcpt) {
		this.class_on_outp_rcpt = class_on_outp_rcpt;
	}
	public String getClass_on_reckoning() {
		return class_on_reckoning;
	}
	public void setClass_on_reckoning(String class_on_reckoning) {
		this.class_on_reckoning = class_on_reckoning;
	}
	public String getSubj_code() {
		return subj_code;
	}
	public void setSubj_code(String subj_code) {
		this.subj_code = subj_code;
	}
	public String getClass_on_mr() {
		return class_on_mr;
	}
	public void setClass_on_mr(String class_on_mr) {
		this.class_on_mr = class_on_mr;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getStop_date() {
		return stop_date;
	}
	public void setStop_date(String stop_date) {
		this.stop_date = stop_date;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getEnter_date() {
		return enter_date;
	}
	public void setEnter_date(String enter_date) {
		this.enter_date = enter_date;
	}
	public String getHigh_price() {
		return high_price;
	}
	public void setHigh_price(String high_price) {
		this.high_price = high_price;
	}
	public String getMaterial_code() {
		return material_code;
	}
	public void setMaterial_code(String material_code) {
		this.material_code = material_code;
	}
	public String getControl_flag() {
		return control_flag;
	}
	public void setControl_flag(String control_flag) {
		this.control_flag = control_flag;
	}
	public String getInput_code() {
		return input_code;
	}
	public void setInput_code(String input_code) {
		this.input_code = input_code;
	}
	public String getInput_code_wb() {
		return input_code_wb;
	}
	public void setInput_code_wb(String input_code_wb) {
		this.input_code_wb = input_code_wb;
	}
	public String getChanged_memo() {
		return changed_memo;
	}
	public void setChanged_memo(String changed_memo) {
		this.changed_memo = changed_memo;
	}
	public String getPackage_spec() {
		return package_spec;
	}
	public void setPackage_spec(String package_spec) {
		this.package_spec = package_spec;
	}
	public String getFirm_id() {
		return firm_id;
	}
	public void setFirm_id(String firm_id) {
		this.firm_id = firm_id;
	}
	public String getLicense_id() {
		return license_id;
	}
	public void setLicense_id(String license_id) {
		this.license_id = license_id;
	}
	public String getCharge_according() {
		return charge_according;
	}
	public void setCharge_according(String charge_according) {
		this.charge_according = charge_according;
	}
	public String getMr_bill_class() {
		return mr_bill_class;
	}
	public void setMr_bill_class(String mr_bill_class) {
		this.mr_bill_class = mr_bill_class;
	}
	public String getGroup_flag() {
		return group_flag;
	}
	public void setGroup_flag(String group_flag) {
		this.group_flag = group_flag;
	}
	public String getZyfrzfbz() {
		return zyfrzfbz;
	}
	public void setZyfrzfbz(String zyfrzfbz) {
		this.zyfrzfbz = zyfrzfbz;
	}
	public String getHigh_quality_supplies_sign() {
		return high_quality_supplies_sign;
	}
	public void setHigh_quality_supplies_sign(String high_quality_supplies_sign) {
		this.high_quality_supplies_sign = high_quality_supplies_sign;
	}
	public String getXmdj() {
		return xmdj;
	}
	public void setXmdj(String xmdj) {
		this.xmdj = xmdj;
	}
	public String getFocorner() {
		return focorner;
	}
	public void setFocorner(String focorner) {
		this.focorner = focorner;
	}

}
