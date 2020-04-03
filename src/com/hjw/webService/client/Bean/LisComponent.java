package com.hjw.webService.client.Bean;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.Bean.dbgj
 * @Description: 申请单
 * @author: yangm
 * @date: 2016年10月7日 上午11:39:54
 * @version V2.0.0.0
 */
public class LisComponent {
	private String chargingItemid="";//收费项目id
	private String itemName="";// 检验项目名称
	private String itemCode="";// 检验项目编码 必须项使用 对应 第三方检测项目编码
	private String itemtime = "201206060900";// 采集日期
	private double itemprice;//收费项目单价
	private double itemamount;//收费项目折扣后金额
	private String targetSiteCode="";//采集部位
	private String extension="";//标本号/条码号 必须项已使用 放条码
	private String specimenNatural="";//标本类型 血清/血浆/尿 标本类别代码
	private String specimenNaturalname="";//标本类型名称
	private String serviceDeliveryLocation_code="";// 执行科室编码
	private String serviceDeliveryLocation_name="";// 执行科室名称	
	private String his_num="";//his关联码
    private String code_class="";//1：价表项目，2：诊疗项目
	private String xtItemCode =""; //系统收费项目编码
    
	public String getCode_class() {
		return code_class;
	}

	public void setCode_class(String code_class) {
		this.code_class = code_class;
	}
	public String getHis_num() {
		return his_num;
	}

	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}

	public String getChargingItemid() {
		return chargingItemid;
	}

	public void setChargingItemid(String chargingItemid) {
		this.chargingItemid = chargingItemid;
	}

	public double getItemprice() {
		return itemprice;
	}

	public void setItemprice(double itemprice) {
		this.itemprice = itemprice;
	}

	public double getItemamount() {
		return itemamount;
	}

	public void setItemamount(double itemamount) {
		this.itemamount = itemamount;
	}

	public String getTargetSiteCode() {
		return targetSiteCode;
	}

	public void setTargetSiteCode(String targetSiteCode) {
		this.targetSiteCode = targetSiteCode;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getSpecimenNatural() {
		return specimenNatural;
	}

	public void setSpecimenNatural(String specimenNatural) {
		this.specimenNatural = specimenNatural;
	}

	public String getSpecimenNaturalname() {
		return specimenNaturalname;
	}

	public void setSpecimenNaturalname(String specimenNaturalname) {
		this.specimenNaturalname = specimenNaturalname;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemtime() {
		return itemtime;
	}

	public void setItemtime(String itemtime) {
		this.itemtime = itemtime;
	}

	public String getServiceDeliveryLocation_code() {
		return serviceDeliveryLocation_code;
	}

	public void setServiceDeliveryLocation_code(String serviceDeliveryLocation_code) {
		this.serviceDeliveryLocation_code = serviceDeliveryLocation_code;
	}

	public String getServiceDeliveryLocation_name() {
		return serviceDeliveryLocation_name;
	}

	public void setServiceDeliveryLocation_name(String serviceDeliveryLocation_name) {
		this.serviceDeliveryLocation_name = serviceDeliveryLocation_name;
	}

	public String getXtItemCode() {
		return xtItemCode;
	}

	public void setXtItemCode(String xtItemCode) {
		this.xtItemCode = xtItemCode;
	}

}
