package com.hjw.webService.client.bdyx.bean.pacs.req;

public class PatientContact {

	private String contactTelephone = "";//联系人电话
	private String contactName = "";//联系人姓名
	
	public String getContactTelephone() {
		return contactTelephone;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
}
