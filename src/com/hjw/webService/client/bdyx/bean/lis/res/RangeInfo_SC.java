package com.hjw.webService.client.bdyx.bean.lis.res;

public class RangeInfo_SC {

	private String referRangesCode = "";//范围类型编码
	private String referRangesName = "";//范围类型名称
	private String lowValue = "";//范围低值
	private String highValue = "";//范围高值
	private String refRanUnit = "";//范围值单位
	
	public String getReferRangesCode() {
		return referRangesCode;
	}
	public String getReferRangesName() {
		return referRangesName;
	}
	public String getLowValue() {
		return lowValue;
	}
	public String getHighValue() {
		return highValue;
	}
	public String getRefRanUnit() {
		return refRanUnit;
	}
	public void setReferRangesCode(String referRangesCode) {
		this.referRangesCode = referRangesCode;
	}
	public void setReferRangesName(String referRangesName) {
		this.referRangesName = referRangesName;
	}
	public void setLowValue(String lowValue) {
		this.lowValue = lowValue;
	}
	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}
	public void setRefRanUnit(String refRanUnit) {
		this.refRanUnit = refRanUnit;
	}
}
