package com.hjw.webService.client.bdyx.bean.lis.res;

import java.util.ArrayList;
import java.util.List;

public class SubItemInfo_SC {

	private String displayOrder = "";//显示序号
	private String itemCode = "";//检验子项编码
	private String itemNameEn = "";//检验子项简称
	private String itemNameCn = "";//检验子项全称
	private String itemValueSC = "";//检验结果
	private String itemUnitSC = "";//检验结果单位
	private String reportMemo = "";//文字参考范围
	private List<RangeInfo_SC> rangeInfo_SC = new ArrayList<>();//1..n 范围信息
	
	private String normalFlag = "";//高低值判断编码
	private String normalFlagName = "";//高低值判断内容
	private String warnFlag = "";//危险值判断编码
	private String warnFlagName = "";//危险值判断内容
	
	public String getDisplayOrder() {
		return displayOrder;
	}
	public String getItemCode() {
		return itemCode;
	}
	public String getItemNameEn() {
		return itemNameEn;
	}
	public String getItemNameCn() {
		return itemNameCn;
	}
	public String getItemValueSC() {
		return itemValueSC;
	}
	public String getItemUnitSC() {
		return itemUnitSC;
	}
	public String getReportMemo() {
		return reportMemo;
	}
	public List<RangeInfo_SC> getRangeInfo_SC() {
		return rangeInfo_SC;
	}
	public String getNormalFlag() {
		return normalFlag;
	}
	public String getNormalFlagName() {
		return normalFlagName;
	}
	public String getWarnFlag() {
		return warnFlag;
	}
	public String getWarnFlagName() {
		return warnFlagName;
	}
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemNameEn(String itemNameEn) {
		this.itemNameEn = itemNameEn;
	}
	public void setItemNameCn(String itemNameCn) {
		this.itemNameCn = itemNameCn;
	}
	public void setItemValueSC(String itemValueSC) {
		this.itemValueSC = itemValueSC;
	}
	public void setItemUnitSC(String itemUnitPQ) {
		this.itemUnitSC = itemUnitSC;
	}
	public void setReportMemo(String reportMemo) {
		this.reportMemo = reportMemo;
	}
	public void setRangeInfo_SC(List<RangeInfo_SC> rangeInfo_SC) {
		this.rangeInfo_SC = rangeInfo_SC;
	}
	public void setNormalFlag(String normalFlag) {
		this.normalFlag = normalFlag;
	}
	public void setNormalFlagName(String normalFlagName) {
		this.normalFlagName = normalFlagName;
	}
	public void setWarnFlag(String warnFlag) {
		this.warnFlag = warnFlag;
	}
	public void setWarnFlagName(String warnFlagName) {
		this.warnFlagName = warnFlagName;
	}
}
