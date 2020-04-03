package com.hjw.webService.client.bdyx.bean.report;

import java.util.ArrayList;
import java.util.List;

public class PhysicalExamInfo {

	private String itemCode = "";//项目编码
	private String itemName = "";//项目名称
	private String briefSummary = "";//小结
	private String examDate = "";//检查日期
	private String examDocId = "";//检查医生编码
	private String examDocName = "";//检查医生名称
	
	private List<PhyExamInfo_ST> phyExamInfo_ST = new ArrayList<>();//检查细项信息（ST）//文本型
	private List<PhyExamInfo_SC> phyExamInfo_SC = new ArrayList<>();//检查细项信息（PQ）//数值型
	private List<PhyExamInfo_IVL_SC> phyExamInfo_IVL_SC = new ArrayList<>();//检查细项信息（IVL_SC）
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getBriefSummary() {
		return briefSummary;
	}
	public String getExamDate() {
		return examDate;
	}
	public String getExamDocId() {
		return examDocId;
	}
	public String getExamDocName() {
		return examDocName;
	}
	public List<PhyExamInfo_ST> getPhyExamInfo_ST() {
		return phyExamInfo_ST;
	}
	public List<PhyExamInfo_SC> getPhyExamInfo_SC() {
		return phyExamInfo_SC;
	}
	public List<PhyExamInfo_IVL_SC> getPhyExamInfo_IVL_SC() {
		return phyExamInfo_IVL_SC;
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
	public void setExamDate(String examDate) {
		this.examDate = examDate;
	}
	public void setExamDocId(String examDocId) {
		this.examDocId = examDocId;
	}
	public void setExamDocName(String examDocName) {
		this.examDocName = examDocName;
	}
	public void setPhyExamInfo_ST(List<PhyExamInfo_ST> phyExamInfo_ST) {
		this.phyExamInfo_ST = phyExamInfo_ST;
	}
	public void setPhyExamInfo_SC(List<PhyExamInfo_SC> phyExamInfo_SC) {
		this.phyExamInfo_SC = phyExamInfo_SC;
	}
	public void setPhyExamInfo_IVL_SC(List<PhyExamInfo_IVL_SC> phyExamInfo_IVL_SC) {
		this.phyExamInfo_IVL_SC = phyExamInfo_IVL_SC;
	}
}