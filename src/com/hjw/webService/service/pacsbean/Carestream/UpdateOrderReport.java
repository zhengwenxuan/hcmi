package com.hjw.webService.service.pacsbean.Carestream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "xmlMessage")  
@XmlType(propOrder = {})  
public class UpdateOrderReport{
	@XmlElement  
	private String RemoteAccNo="";//	1-1	申请单编号	
	@XmlElement 
	private String AccNo="";//	1-1	检查编号	
	@XmlElement 
	private String SubmitDoc="";//	1-1	提交医生	
	@XmlElement 
	private String SubmitDT="";//	1-1	提交时间	YYYY-MM-DD HH:mm:ss
	@XmlElement 
	private String ApproveDoc="";//	1-1	审核医生	
	@XmlElement 
	private String ApproveDT="";//	1-1	审核时间	YYYY-MM-DD HH:mm:ss
	@XmlElement 
	private String IsPositive="";//	1-1	阴阳性 	1–阳性0-阴性
	@XmlElement 
	private String Description="";//	1-1	报告所见	
	@XmlElement 
	private String Comment="";//	1-1	报告结论	
	@XmlElement 
	private String ImageUrl="";//	1-N	仅提供DX的jpg 其他的检查通过关键影像提供，没有标记就不提供	HTTP \FTP
	public String getRemoteAccNo() {
		return RemoteAccNo;
	}
	public void setRemoteAccNo(String remoteAccNo) {
		RemoteAccNo = remoteAccNo;
	}
	public String getAccNo() {
		return AccNo;
	}
	public void setAccNo(String accNo) {
		AccNo = accNo;
	}
	public String getSubmitDoc() {
		return SubmitDoc;
	}
	public void setSubmitDoc(String submitDoc) {
		SubmitDoc = submitDoc;
	}
	public String getSubmitDT() {
		return SubmitDT;
	}
	public void setSubmitDT(String submitDT) {
		SubmitDT = submitDT;
	}
	public String getApproveDoc() {
		return ApproveDoc;
	}
	public void setApproveDoc(String approveDoc) {
		ApproveDoc = approveDoc;
	}
	public String getApproveDT() {
		return ApproveDT;
	}
	public void setApproveDT(String approveDT) {
		ApproveDT = approveDT;
	}
	public String getIsPositive() {
		return IsPositive;
	}
	public void setIsPositive(String isPositive) {
		IsPositive = isPositive;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

}
