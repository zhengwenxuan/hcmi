package com.hjw.webService.service.Databean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
     * @Title:   
     * @Package com.hjw.webService.service.Databean   
     * @Description:  临床诊疗项目字典  
     * @author: yangm     
     * @date:   2016年10月31日 上午11:14:09   
     * @version V3.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "List")  
@XmlType(propOrder = {})  
public class DiagnosisItem {
	private String action="";//	操作类型，取值范围（插入更新标志  1插入 2更新 0 删除）
	private String itemVersion="";//	数据版本号
	private String item_class="";//	项目分类
	private String item_code="";//	项目代码
	private String item_name="";//	项目名称
	private String input_code="";//	输入码
	private String input_code_wb="";//	五笔码
	private String expand1="";//	检验标本
	private String expand2="";//	检验类别
	private String expand3="";//	检验科室
	private String expand4="";//	扩展码4
	private String expand5="";//	扩展码5
	private String item_status="";//	项目状态
	private String memo="";//	备注
	private String disp_class="";//	细化分类名称
	private String branch_code="";//	分院代码
	private double price;//诊疗项目价格

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
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
	public String getExpand1() {
		return expand1;
	}
	public void setExpand1(String expand1) {
		this.expand1 = expand1;
	}
	public String getExpand2() {
		return expand2;
	}
	public void setExpand2(String expand2) {
		this.expand2 = expand2;
	}
	public String getExpand3() {
		return expand3;
	}
	public void setExpand3(String expand3) {
		this.expand3 = expand3;
	}
	public String getExpand4() {
		return expand4;
	}
	public void setExpand4(String expand4) {
		this.expand4 = expand4;
	}
	public String getExpand5() {
		return expand5;
	}
	public void setExpand5(String expand5) {
		this.expand5 = expand5;
	}
	public String getItem_status() {
		return item_status;
	}
	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDisp_class() {
		return disp_class;
	}
	public void setDisp_class(String disp_class) {
		this.disp_class = disp_class;
	}
	public String getBranch_code() {
		return branch_code;
	}
	public void setBranch_code(String branch_code) {
		this.branch_code = branch_code;
	}

}
