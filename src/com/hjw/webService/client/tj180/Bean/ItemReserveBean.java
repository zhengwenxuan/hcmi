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
public class ItemReserveBean {
	private String deptId="";//	体检职能科室代码
	private String deptName="";//	体检职能科室名称
	private String unionProjectId="";//	体检项目编码
	private String unionProjectName="";//	体检项目名称 中文名
	private String price="";//	项目价格 	免费项目也要传，价格为0
	private String examNo="";//	对应的检查号或检验号
	private String examineType="";//	检查类别 	检验：1，检查：2，
	private String note="";//	检查科室位置
	private String memo="";//	检查注意事项
	private String deptNo="";//	体检职能科室排序号
	private String unionProjectNo="";//	组合项目排序号
	private String fitSex="0";//	适合性别 	不限：0，适合男：1，适合女：2
	private String printOrNot="0";//	是否打印申请单 
	private String sendOrNot="0";//	是否发送申请
	private String fitWebbed="2";//	适合婚否 	不限：2，适合已婚：1，适合未婚：0
	private String itemClass="";//	收费项目类型
	private String itemCode="";//	收费项目编码
	private String itemSpec="";//	收费项目规格 codeClass=2时，””
	private String units="";//	收费项目单位 	codeClass=2时，””
	private String codeClass="";//	收费项目编码类别 1：价表项目，2：诊疗项目
	private String chargeDept="";//	执行科室代码
	private String examClass="";//	检查类别
	private String examSubClass="";//	检查子类别
	private String editor="";//	录入操作员登录用户名(需建与HIS一致的登录用户) 大写拼音字头：如WYY
	private String editDate="";//	录入时间 	Yyyy-mm-dd hh:mm:ss
	
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getUnionProjectNo() {
		return unionProjectNo;
	}
	public void setUnionProjectNo(String unionProjectNo) {
		this.unionProjectNo = unionProjectNo;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public String getUnionProjectName() {
		return unionProjectName;
	}
	public void setUnionProjectName(String unionProjectName) {
		this.unionProjectName = unionProjectName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getExamNo() {
		return examNo;
	}
	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}
	public String getExamineType() {
		return examineType;
	}
	public void setExamineType(String examineType) {
		this.examineType = examineType;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getFitSex() {
		return fitSex;
	}
	public void setFitSex(String fitSex) {
		this.fitSex = fitSex;
	}
	public String getPrintOrNot() {
		return printOrNot;
	}
	public void setPrintOrNot(String printOrNot) {
		this.printOrNot = printOrNot;
	}
	public String getSendOrNot() {
		return sendOrNot;
	}
	public void setSendOrNot(String sendOrNot) {
		this.sendOrNot = sendOrNot;
	}
	public String getFitWebbed() {
		return fitWebbed;
	}
	public void setFitWebbed(String fitWebbed) {
		this.fitWebbed = fitWebbed;
	}
	public String getItemClass() {
		return itemClass;
	}
	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemSpec() {
		return itemSpec;
	}
	public void setItemSpec(String itemSpec) {
		this.itemSpec = itemSpec;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getCodeClass() {
		return codeClass;
	}
	public void setCodeClass(String codeClass) {
		this.codeClass = codeClass;
	}
	public String getChargeDept() {
		return chargeDept;
	}
	public void setChargeDept(String chargeDept) {
		this.chargeDept = chargeDept;
	}
	public String getExamClass() {
		return examClass;
	}
	public void setExamClass(String examClass) {
		this.examClass = examClass;
	}
	public String getExamSubClass() {
		return examSubClass;
	}
	public void setExamSubClass(String examSubClass) {
		this.examSubClass = examSubClass;
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
	
}
