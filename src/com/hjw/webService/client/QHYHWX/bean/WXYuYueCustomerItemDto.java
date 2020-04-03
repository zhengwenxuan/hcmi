package com.hjw.webService.client.QHYHWX.bean;

public class WXYuYueCustomerItemDto {

	
	private String identityCard;//身份证号
	private String patPhoto;//身份证照片
	private String patName;//病人姓名
	private String sex;//病人性别
	private String age;//病人年龄
	private String packageName;//预约套餐（多个套餐按；隔开）
	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getPatPhoto() {
		return patPhoto;
	}
	public void setPatPhoto(String patPhoto) {
		this.patPhoto = patPhoto;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
	
	
	
}
