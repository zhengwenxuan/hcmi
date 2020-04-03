package com.hjw.webService.client.bdyx.bean.pacs.res;

import java.util.ArrayList;
import java.util.List;

public class ExamItem {
	private String itemCode = "";//检查项目编码	
	private String itemName = "";//检查项目名称	
	private String reportMemo = "";//检查备注	
	private String imagingFinding = "";//客观所见内容	
	private String imagingConclusion = "";//主观提示内容	
	private String examinationMethod = "";//检查方法编码	
	private String examinationMethodName = "";//检查方法名称	
	private String targetSiteCode = "";//检查部位编码	
	private String targetSiteName = "";//检查部位名称	
	private List<ExaminingDoctor> examiningDoctors = new ArrayList<>();//1..n 检查医生信息
	private String manufacturerName = "";//仪器型号
	private String routeCode = "";//给药方式编码
	private String routeName = "";//给药方式名称
	private String reagentCode = "";//试剂编码
	private String reagentName = "";//试剂名称
	private String reagentDosage = "";//试剂用量
	private String reagentDosageUnit = "";//试剂用量单位
	private String recordStartTime = "";//记录开始时间
	private String recordTotalTime = "";//记录总时间文本
	private String recordTotalTimeUnit = "";//记录总时间单位
	private List<ImageText> imageText = new ArrayList<>();//0..n 图像信息	
	
	public String getItemCode() {
		return itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public String getReportMemo() {
		return reportMemo;
	}
	public String getImagingFinding() {
		return imagingFinding;
	}
	public String getImagingConclusion() {
		return imagingConclusion;
	}
	public String getExaminationMethod() {
		return examinationMethod;
	}
	public String getExaminationMethodName() {
		return examinationMethodName;
	}
	public String getTargetSiteCode() {
		return targetSiteCode;
	}
	public String getTargetSiteName() {
		return targetSiteName;
	}
	public List<ExaminingDoctor> getExaminingDoctors() {
		return examiningDoctors;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public String getRouteCode() {
		return routeCode;
	}
	public String getRouteName() {
		return routeName;
	}
	public String getReagentCode() {
		return reagentCode;
	}
	public String getReagentName() {
		return reagentName;
	}
	public String getReagentDosage() {
		return reagentDosage;
	}
	public String getReagentDosageUnit() {
		return reagentDosageUnit;
	}
	public String getRecordStartTime() {
		return recordStartTime;
	}
	public String getRecordTotalTime() {
		return recordTotalTime;
	}
	public String getRecordTotalTimeUnit() {
		return recordTotalTimeUnit;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public void setReportMemo(String reportMemo) {
		this.reportMemo = reportMemo;
	}
	public void setImagingFinding(String imagingFinding) {
		this.imagingFinding = imagingFinding;
	}
	public void setImagingConclusion(String imagingConclusion) {
		this.imagingConclusion = imagingConclusion;
	}
	public void setExaminationMethod(String examinationMethod) {
		this.examinationMethod = examinationMethod;
	}
	public void setExaminationMethodName(String examinationMethodName) {
		this.examinationMethodName = examinationMethodName;
	}
	public void setTargetSiteCode(String targetSiteCode) {
		this.targetSiteCode = targetSiteCode;
	}
	public void setTargetSiteName(String targetSiteName) {
		this.targetSiteName = targetSiteName;
	}
	public void setExaminingDoctors(List<ExaminingDoctor> examiningDoctors) {
		this.examiningDoctors = examiningDoctors;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public void setReagentCode(String reagentCode) {
		this.reagentCode = reagentCode;
	}
	public void setReagentName(String reagentName) {
		this.reagentName = reagentName;
	}
	public void setReagentDosage(String reagentDosage) {
		this.reagentDosage = reagentDosage;
	}
	public void setReagentDosageUnit(String reagentDosageUnit) {
		this.reagentDosageUnit = reagentDosageUnit;
	}
	public void setRecordStartTime(String recordStartTime) {
		this.recordStartTime = recordStartTime;
	}
	public void setRecordTotalTime(String recordTotalTime) {
		this.recordTotalTime = recordTotalTime;
	}
	public void setRecordTotalTimeUnit(String recordTotalTimeUnit) {
		this.recordTotalTimeUnit = recordTotalTimeUnit;
	}
	public List<ImageText> getImageText() {
		return imageText;
	}
	public void setImageText(List<ImageText> imageText) {
		this.imageText = imageText;
	}
}
