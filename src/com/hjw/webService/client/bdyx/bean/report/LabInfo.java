package com.hjw.webService.client.bdyx.bean.report;

import java.util.ArrayList;
import java.util.List;

public class LabInfo {

	private String itemCode = "";//检验项目编码
	private String itemName = "";//检验项目名称
	private String briefSummary = "";//小结
	private String labDocCode = "";//检验医生编码
	private String labDocName = "";//检验医生名称
	private List<LabSubItemInfo_ST> labSubItemInfo_ST = new ArrayList<>();//0..n 检验子项信息
	private List<LabSubItemInfo_SC> labSubItemInfo_SC = new ArrayList<>();//0..n 检验子项信息
	private List<LabSubItemInfo_IVL_SC> labSubItemInfo_IVL_SC = new ArrayList<>();//0..n 检验子项信息
	
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getBriefSummary() {
		return briefSummary;
	}
	public String getLabDocCode() {
		return labDocCode;
	}
	public String getLabDocName() {
		return labDocName;
	}
	public List<LabSubItemInfo_ST> getLabSubItemInfo_ST() {
		return labSubItemInfo_ST;
	}
	public List<LabSubItemInfo_SC> getLabSubItemInfo_SC() {
		return labSubItemInfo_SC;
	}
	public List<LabSubItemInfo_IVL_SC> getLabSubItemInfo_IVL_SC() {
		return labSubItemInfo_IVL_SC;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setBriefSummary(String briefSummary) {
		this.briefSummary = briefSummary;
	}
	public void setLabDocCode(String labDocCode) {
		this.labDocCode = labDocCode;
	}
	public void setLabDocName(String labDocName) {
		this.labDocName = labDocName;
	}
	public void setLabSubItemInfo_ST(List<LabSubItemInfo_ST> labSubItemInfo_ST) {
		this.labSubItemInfo_ST = labSubItemInfo_ST;
	}
	public void setLabSubItemInfo_SC(List<LabSubItemInfo_SC> labSubItemInfo_SC) {
		this.labSubItemInfo_SC = labSubItemInfo_SC;
	}
	public void setLabSubItemInfo_IVL_SC(List<LabSubItemInfo_IVL_SC> labSubItemInfo_IVL_SC) {
		this.labSubItemInfo_IVL_SC = labSubItemInfo_IVL_SC;
	}
}
