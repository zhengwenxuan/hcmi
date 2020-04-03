package com.hjw.webService.client.xianning.bean;

public class EXAMINFO {

	private String EXAM_CLASS = "";//检查类型
	private String EXAM_ITEM = "";//检查项目
	private String EXAM_ITEM_CODE = "";//检查项目代码
	private String EXAM_PARTS = "";//部位名称
	
	public String getEXAM_CLASS() {
		return EXAM_CLASS;
	}
	public String getEXAM_ITEM() {
		return EXAM_ITEM;
	}
	public String getEXAM_ITEM_CODE() {
		return EXAM_ITEM_CODE;
	}
	public String getEXAM_PARTS() {
		return EXAM_PARTS;
	}
	public void setEXAM_CLASS(String eXAM_CLASS) {
		EXAM_CLASS = eXAM_CLASS;
	}
	public void setEXAM_ITEM(String eXAM_ITEM) {
		EXAM_ITEM = eXAM_ITEM;
	}
	public void setEXAM_ITEM_CODE(String eXAM_ITEM_CODE) {
		EXAM_ITEM_CODE = eXAM_ITEM_CODE;
	}
	public void setEXAM_PARTS(String eXAM_PARTS) {
		EXAM_PARTS = eXAM_PARTS;
	}
}
