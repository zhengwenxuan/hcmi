package com.hjw.webService.client.bdyx.bean.lis.res;

public class ReviewDoctorInfo {

	private String reviewDate = "";//审核日期
	private String reviewerRole = "";//审核医生角色
	private String reviewerId = "";//审核医生编码
	private String reviewerName = "";//审核医生名称
	public String getReviewDate() {
		return reviewDate;
	}
	public String getReviewerRole() {
		return reviewerRole;
	}
	public String getReviewerId() {
		return reviewerId;
	}
	public String getReviewerName() {
		return reviewerName;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public void setReviewerRole(String reviewerRole) {
		this.reviewerRole = reviewerRole;
	}
	public void setReviewerId(String reviewerId) {
		this.reviewerId = reviewerId;
	}
	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}
}
