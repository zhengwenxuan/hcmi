package com.hjw.webService.client.tj180.Bean;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: 消息体 
     * @author: yangm     
     * @date:   2016年10月7日 上午11:08:27   
     * @version V2.0.0.0
 */
public class ExamPicSetBean {
	private String patientId="";//	档案号
	private String photo="";
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	
	
}
