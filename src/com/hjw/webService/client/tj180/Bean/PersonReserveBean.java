package com.hjw.webService.client.tj180.Bean;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: 消息体 
     * @author: yangm     
     * @date:   2016年10月7日 上午11:08:27   
     * @version V2.0.0.0
 */
public class PersonReserveBean {
	private String reserveId="";//	体检预约号
	private String customerPatientId="";//	客户HISID号
	private String reserveType="O";//	体检预约类型 	P：个人；O：团体
	private String organizationId="";//	所属团队编码
	private String organizationName="";//	所属团队名称
	private String orgReserveId="";//	团体预约号
	private String groupId="";//	分组编码
	private String groupName="";//	分组名称
	private String totalCost="";//	总费用
	private String incomeFlag="";//	收费状态 未收费：0，团队记账：4
	private String  enabledFlag="";//	体检状态	未体检：9，体检中：，体检完：
	private String examineDate="";//	体检日期 Yyyy-mm-dd hh:mm:ss
	private String orderedByDoctor="";//	开单医生
	private String editor="";//	开单操作员登录用户名(需建与HIS一致的登录用户)	大写拼音字头：如WYY
	private String editDate="";//	开单日期 Yyyy-mm-dd hh:mm:ss
	private String recommend="";//	介绍人 	中文名
	private String recommendTTM="";//	TTM介绍人 	中文名
	private String memo="";//	预约备注
	private String examineFlag="3";//	体检标志 默认：3，显示表格：7
    
	public String getExamineFlag() {
		return examineFlag;
	}
	public void setExamineFlag(String examineFlag) {
		this.examineFlag = examineFlag;
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
	public String getOrgReserveId() {
		return orgReserveId;
	}
	public void setOrgReserveId(String orgReserveId) {
		this.orgReserveId = orgReserveId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}
	public String getIncomeFlag() {
		return incomeFlag;
	}
	public void setIncomeFlag(String incomeFlag) {
		this.incomeFlag = incomeFlag;
	}
	public String getEnabledFlag() {
		return enabledFlag;
	}
	public void setEnabledFlag(String enabledFlag) {
		this.enabledFlag = enabledFlag;
	}
	public String getExamineDate() {
		return examineDate;
	}
	public void setExamineDate(String examineDate) {
		this.examineDate = examineDate;
	}
	public String getOrderedByDoctor() {
		return orderedByDoctor;
	}
	public void setOrderedByDoctor(String orderedByDoctor) {
		this.orderedByDoctor = orderedByDoctor;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditDate() {
		return editDate;
	}
	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getRecommendTTM() {
		return recommendTTM;
	}
	public void setRecommendTTM(String recommendTTM) {
		this.recommendTTM = recommendTTM;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
