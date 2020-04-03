package com.hjw.webService.client.bdyx.bean.pacs.res;

public class ReviewDoctor {

	private String reviewDate = "";//审核日期
	private String reviewDoctor = "";//审核医生编码
	private String reviewDoctorName = "";//审核医生名称
	
	public String getReviewDate() {
		return reviewDate;
	}
	public String getReviewDoctor() {
		return reviewDoctor;
	}
	public String getReviewDoctorName() {
		return reviewDoctorName;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public void setReviewDoctor(String reviewDoctor) {
		this.reviewDoctor = reviewDoctor;
	}
	public void setReviewDoctorName(String reviewDoctorName) {
		this.reviewDoctorName = reviewDoctorName;
	}
}
