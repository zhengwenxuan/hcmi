package com.hjw.webService.client.bdyx.bean.report;

public class LabSubItemInfo_SC {

	private String displayOrder = "";//检验子项序号（PQ）
	private String labSubItemCode = "";//检验子项编码（PQ）
	private String labSubItemName = "";//检验子项全称（PQ）
	private String labSubValue = "";//检验定量结果（PQ）
	private String labSubQueValueUnit = "";//检验结果单位（PQ）
	private String unusualDesc = "";//异常描述（PQ）
	private String referenceRange = "";//参考范围（PQ）
	private String referenceRangeUnit = "";//参考范围单位（PQ）
	
	public String getDisplayOrder() {
		return displayOrder;
	}
	public String getLabSubItemCode() {
		return labSubItemCode;
	}
	public String getLabSubItemName() {
		return labSubItemName;
	}
	public String getLabSubValue() {
		return labSubValue;
	}
	public String getLabSubQueValueUnit() {
		return labSubQueValueUnit;
	}
	public String getUnusualDesc() {
		return unusualDesc;
	}
	public String getReferenceRange() {
		return referenceRange;
	}
	public String getReferenceRangeUnit() {
		return referenceRangeUnit;
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
	public void setLabSubValue(String labSubValue) {
		this.labSubValue = labSubValue;
	}
	public void setLabSubQueValueUnit(String labSubQueValueUnit) {
		this.labSubQueValueUnit = labSubQueValueUnit;
	}
	public void setUnusualDesc(String unusualDesc) {
		this.unusualDesc = unusualDesc;
	}
	public void setReferenceRange(String referenceRange) {
		this.referenceRange = referenceRange;
	}
	public void setReferenceRangeUnit(String referenceRangeUnit) {
		this.referenceRangeUnit = referenceRangeUnit;
	}
}
