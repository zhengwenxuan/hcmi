package com.hjw.webService.client.tj180.Bean;

public class QueueBodyBean {
	private String reserveId="";//	体检预约号
	private String exam_num="";// 体检编号
	private long exam_id;//体检编号id
	private String customerPatientId="";//	客户HISID号
	private String reserveType="O";//	预约类型 	个人：P，团队：O
	private String customerSex="";//	客户性别 中文：男/女
	private String reserveTotalCost;//	体检预约总金额
	private String isVipFood="0";//	是否开了贵宾早餐 	有：1，没有：0
	private String organizationId="";//	所属团体编码
	private String organizationName="";//	所属团体名称
	private String regeditDateTime="0";//	取号时间
	private String hasCc="0";//	是否有彩超项目	有：1，没有：0
	
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public long getExam_id() {
		return exam_id;
	}
	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getCustomerPatientId() {
		return customerPatientId;
	}
	public void setCustomerPatientId(String customerPatientId) {
		this.customerPatientId = customerPatientId;
	}
	public String getReserveType() {
		return reserveType;
	}
	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
	}
	public String getCustomerSex() {
		return customerSex;
	}
	public void setCustomerSex(String customerSex) {
		this.customerSex = customerSex;
	}
	public String getReserveTotalCost() {
		return reserveTotalCost;
	}
	public void setReserveTotalCost(String reserveTotalCost) {
		this.reserveTotalCost = reserveTotalCost;
	}
	public String getIsVipFood() {
		return isVipFood;
	}
	public void setIsVipFood(String isVipFood) {
		this.isVipFood = isVipFood;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getRegeditDateTime() {
		return regeditDateTime;
	}
	public void setRegeditDateTime(String regeditDateTime) {
		this.regeditDateTime = regeditDateTime;
	}
	public String getHasCc() {
		return hasCc;
	}
	public void setHasCc(String hasCc) {
		this.hasCc = hasCc;
	}
}
