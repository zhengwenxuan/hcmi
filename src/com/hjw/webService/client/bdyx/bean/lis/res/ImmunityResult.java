package com.hjw.webService.client.bdyx.bean.lis.res;

public class ImmunityResult {

	private String displayOrder = "";//显示序号
	private String itemCode = "";//检验结果项编码
	private String itemNameCn = "";//检验结果项名称
	private String labResult = "";//检测结果名称
	private String reportMemo = "";//备注
	private String references = "";//参考信息
	
	public String getDisplayOrder() {
		return displayOrder;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemNameCn() {
		return itemNameCn;
	}
	public String getLabResult() {
		return labResult;
	}
	public String getReportMemo() {
		return reportMemo;
	}
	public String getReferences() {
		return references;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemNameCn(String itemNameCn) {
		this.itemNameCn = itemNameCn;
	}
	public void setLabResult(String labResult) {
		this.labResult = labResult;
	}
	public void setReportMemo(String reportMemo) {
		this.reportMemo = reportMemo;
	}
	public void setReferences(String references) {
		this.references = references;
	}
}
