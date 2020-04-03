package com.hjw.webService.client.xhhk.lisbean;

public class ItemsResultXHHK {

	private String itemcode = ""; //检验项目编号
	private String ResultNo = "";//结果单元编号
	private String ResultName = "";//项目名称
	private String ResultCode = "";//项目代码
	private String ResultValue = "";//检验结果
	private String Unit = "";//结果单位
	private String ResultFlag = "";//结果标志ＨＬN
	private String NormalRange = "";//指标范围
	private String LowerLimit = "";//范围下限
	private String UpperLimit = "";//范围上限
	
	
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getResultNo() {
		return ResultNo;
	}
	public String getResultName() {
		return ResultName;
	}
	public String getResultCode() {
		return ResultCode;
	}
	public String getResultValue() {
		return ResultValue;
	}
	public String getUnit() {
		return Unit;
	}
	public String getResultFlag() {
		return ResultFlag;
	}
	public String getNormalRange() {
		return NormalRange;
	}
	public String getLowerLimit() {
		return LowerLimit;
	}
	public String getUpperLimit() {
		return UpperLimit;
	}
	public void setResultNo(String resultNo) {
		ResultNo = resultNo;
	}
	public void setResultName(String resultName) {
		ResultName = resultName;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public void setResultValue(String resultValue) {
		ResultValue = resultValue;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public void setResultFlag(String resultFlag) {
		ResultFlag = resultFlag;
	}
	public void setNormalRange(String normalRange) {
		NormalRange = normalRange;
	}
	public void setLowerLimit(String lowerLimit) {
		LowerLimit = lowerLimit;
	}
	public void setUpperLimit(String upperLimit) {
		UpperLimit = upperLimit;
	}
}
