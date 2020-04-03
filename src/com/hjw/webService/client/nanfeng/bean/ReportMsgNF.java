package com.hjw.webService.client.nanfeng.bean;

import java.util.ArrayList;
import java.util.List;


public class ReportMsgNF {

	
	private String OrdID = "";//医嘱号
	
	private String LabNo = "";//条码号
	
	private String exam_num = "";//体检号
	
	private String ReportID = "";//LIS报告ID
	
	private String ReportUrl = "";//报告地址
	
	private String RecUser = "";//接收者名称
	
	private String RecUserCode = "";//接收者代码
	
	private String RecDate = "";//接收日期 YYYY-MM-DD
	
	private String RecTime = "";//接收时间 HH:MM:SS
	
	private String EntryUser = "";//录入者名称
	
	private String EntryUserCode = "";//录入者代码
	
	private String EntryDate = "";//录入日期 YYYY-MM-DD
	
	private String EntryTime = "";//录入时间 HH:MM:SS
	
	private String AuthUser = "";//审核者名称
	
	private String AuthUserCode = "";//审核者代码
	
	private String AuthDate = "";//审核日期 YYYY-MM-DD
	
	private String AuthTime = "";//审核时间 HH:MM:SS
	
	private String Notes = "";//报告备注
	
	private String ImageFile  = "";//图像的路径（多幅图使用@分割）检验图像的路径
	
	private String MainWarnDesc = "";//危急值说明 W@危急
	
	private List<ItemResultMsgNF> resMsg = new ArrayList<ItemResultMsgNF>();
	
//	private ResultMsgs ResultMsgs = new ResultMsgs();//结果信息（list）
	
	public String getOrdID() {
		return OrdID;
	}
	public String getLabNo() {
		return LabNo;
	}
	public String getReportID() {
		return ReportID;
	}
	public String getReportUrl() {
		return ReportUrl;
	}
	public String getRecUser() {
		return RecUser;
	}
	public String getRecUserCode() {
		return RecUserCode;
	}
	public String getRecDate() {
		return RecDate;
	}
	public String getRecTime() {
		return RecTime;
	}
	public String getEntryUser() {
		return EntryUser;
	}
	public String getEntryUserCode() {
		return EntryUserCode;
	}
	public String getEntryDate() {
		return EntryDate;
	}
	public String getEntryTime() {
		return EntryTime;
	}
	public String getAuthUser() {
		return AuthUser;
	}
	public String getAuthUserCode() {
		return AuthUserCode;
	}
	public String getAuthDate() {
		return AuthDate;
	}
	public String getAuthTime() {
		return AuthTime;
	}
	public String getNotes() {
		return Notes;
	}
	public String getImageFile() {
		return ImageFile;
	}
	public String getMainWarnDesc() {
		return MainWarnDesc;
	}
	public void setOrdID(String ordID) {
		OrdID = ordID;
	}
	public void setLabNo(String labNo) {
		LabNo = labNo;
	}
	public void setReportID(String reportID) {
		ReportID = reportID;
	}
	public void setReportUrl(String reportUrl) {
		ReportUrl = reportUrl;
	}
	public void setRecUser(String recUser) {
		RecUser = recUser;
	}
	public void setRecUserCode(String recUserCode) {
		RecUserCode = recUserCode;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public void setRecTime(String recTime) {
		RecTime = recTime;
	}
	public void setEntryUser(String entryUser) {
		EntryUser = entryUser;
	}
	public void setEntryUserCode(String entryUserCode) {
		EntryUserCode = entryUserCode;
	}
	public void setEntryDate(String entryDate) {
		EntryDate = entryDate;
	}
	public void setEntryTime(String entryTime) {
		EntryTime = entryTime;
	}
	public void setAuthUser(String authUser) {
		AuthUser = authUser;
	}
	public void setAuthUserCode(String authUserCode) {
		AuthUserCode = authUserCode;
	}
	public void setAuthDate(String authDate) {
		AuthDate = authDate;
	}
	public void setAuthTime(String authTime) {
		AuthTime = authTime;
	}
	public void setNotes(String notes) {
		Notes = notes;
	}
	public void setImageFile(String imageFile) {
		ImageFile = imageFile;
	}
	public void setMainWarnDesc(String mainWarnDesc) {
		MainWarnDesc = mainWarnDesc;
	}
	public List<ItemResultMsgNF> getResMsg() {
		return resMsg;
	}
	public void setResMsg(List<ItemResultMsgNF> resMsg) {
		this.resMsg = resMsg;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	
}
