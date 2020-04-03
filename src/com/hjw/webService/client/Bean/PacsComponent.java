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
public class PacsComponent {
	private String itemName="";// 申请单详细内容  取体检收费项目名称
	private String itemDate = "20120506";// 检查申请日期
	private String itemCode="";//收费项目编码-对应自己编码
	private long   itemId;//收费项目id
	private double itemprice;//收费项目单价
	private double itemamount;//收费项目折扣后金额
	private String pacs_num="";//收费项目字典表的pacs_num
	private String exam_class="";//如填写 ct等  //科室代码
	private String itemtime = "201206060900";// 执行时间	
	private String serviceDeliveryLocation_code="";// 执行科室编码
	private String serviceDeliveryLocation_name="";// 执行科室名称	
    private String his_num="";//his关联码
	private String code_class="";//1：价表项目，2：诊疗项目
	
	public String getCode_class() {
		return code_class;
	}

	public void setCode_class(String code_class) {
		this.code_class = code_class;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getHis_num() {
		return his_num;
	}

	public void setHis_num(String his_num) {
		this.his_num = his_num;
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

	public String getPacs_num() {
		return pacs_num;
	}

	public void setPacs_num(String pacs_num) {
		this.pacs_num = pacs_num;
	}

	public String getExam_class() {
		return exam_class;
	}

	public void setExam_class(String exam_class) {
		this.exam_class = exam_class;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDate() {
		return itemDate;
	}

	public void setItemDate(String itemDate) {
		this.itemDate = itemDate;
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

}
