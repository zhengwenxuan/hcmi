package com.hjw.webService.client.bdyx.bean.lis.res;

import java.util.ArrayList;
import java.util.List;

public class ReportLisBDYX {

	private String labItemCode = "";//检验项编码
	private String labItemName = "";//检验项名称
	
	//一般检验
	private List<SubItemInfo_SC> subItemInfo_SC = new ArrayList<>();//1..n 检验子项信息
	
	//微生物
	private String warnFlag = "";//危机编码
	private String warnFlagName = "";//危机名称
	private String descriptionResult = "";//检验项结果描述
	private List<ImmunityResult> immunityResult = new ArrayList<>();//0..n 免疫检验结果项信息
	private String comment = "";//报告评语
	
	public String getLabItemCode() {
		return labItemCode;
	}
	public String getLabItemName() {
		return labItemName;
	}
	public void setLabItemCode(String labItemCode) {
		this.labItemCode = labItemCode;
	}
	public void setLabItemName(String labItemName) {
		this.labItemName = labItemName;
	}
	public List<SubItemInfo_SC> getSubItemInfo_SC() {
		return subItemInfo_SC;
	}
	public void setSubItemInfo_SC(List<SubItemInfo_SC> subItemInfo_SC) {
		this.subItemInfo_SC = subItemInfo_SC;
	}
	public String getWarnFlag() {
		return warnFlag;
	}
	public String getWarnFlagName() {
		return warnFlagName;
	}
	public String getDescriptionResult() {
		return descriptionResult;
	}
	public List<ImmunityResult> getImmunityResult() {
		return immunityResult;
	}
	public String getComment() {
		return comment;
	}
	public void setWarnFlag(String warnFlag) {
		this.warnFlag = warnFlag;
	}
	public void setWarnFlagName(String warnFlagName) {
		this.warnFlagName = warnFlagName;
	}
	public void setDescriptionResult(String descriptionResult) {
		this.descriptionResult = descriptionResult;
	}
	public void setImmunityResult(List<ImmunityResult> immunityResult) {
		this.immunityResult = immunityResult;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
