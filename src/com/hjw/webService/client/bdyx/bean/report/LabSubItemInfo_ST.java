package com.hjw.webService.client.bdyx.bean.report;

public class LabSubItemInfo_ST {

	private String displayOrder = "";//检验子项序号（ST）
	private String labSubItemCode = "";//检验子项编码（ST）
	private String labSubItemName = "";//检验子项全称（ST）
	private String labSubTextValue = "";//检验文本结果（ST）
	private String unusualDesc = "";//异常描述（ST）
	private String referenceRange = "";//参考范围（ST）
	
	public String getDisplayOrder() {
		return displayOrder;
	}
	public String getLabSubItemCode() {
		return labSubItemCode;
	}
	public String getLabSubItemName() {
		return labSubItemName;
	}
	public String getLabSubTextValue() {
		return labSubTextValue;
	}
	public String getUnusualDesc() {
		return unusualDesc;
	}
	public String getReferenceRange() {
		return referenceRange;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public void setLabSubItemCode(String labSubItemCode) {
		this.labSubItemCode = labSubItemCode;
	}
	public void setLabSubItemName(String labSubItemName) {
		this.labSubItemName = labSubItemName;
	}
	public void setLabSubTextValue(String labSubTextValue) {
		this.labSubTextValue = labSubTextValue;
	}
	public void setUnusualDesc(String unusualDesc) {
		this.unusualDesc = unusualDesc;
	}
	public void setReferenceRange(String referenceRange) {
		this.referenceRange = referenceRange;
	}
}
