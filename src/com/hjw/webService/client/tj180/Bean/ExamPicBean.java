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
public class ExamPicBean {
	private String arch_num="";
	private long exam_id;
	private String exam_num="";
	private String picture_path="";
	private long picture_resource;//图片来源 0 表示主系统，1表示第三方系统
	public String getArch_num() {
		return arch_num;
	}
	public void setArch_num(String arch_num) {
		this.arch_num = arch_num;
	}
	public long getExam_id() {
		return exam_id;
	}
	public void setExam_id(long exam_id) {
		this.exam_id = exam_id;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getPicture_path() {
		return picture_path;
	}
	public void setPicture_path(String picture_path) {
		this.picture_path = picture_path;
	}
	public long getPicture_resource() {
		return picture_resource;
	}
	public void setPicture_resource(long picture_resource) {
		this.picture_resource = picture_resource;
	}

	
}
