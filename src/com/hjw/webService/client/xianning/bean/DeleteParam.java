package com.hjw.webService.client.xianning.bean;

public class DeleteParam {

	private String EXAM_NO = "";//申请单号

	private String EXAM_CODE = "";//体检单号

	public String getEXAM_NO() {
		return EXAM_NO;
	}

	public String getEXAM_CODE() {
		return EXAM_CODE;
	}

	public void setEXAM_NO(String eXAM_NO) {
		EXAM_NO = eXAM_NO;
	}

	public void setEXAM_CODE(String eXAM_CODE) {
		EXAM_CODE = eXAM_CODE;
	}
	
	@Override
	public String toString() {
		return "?EXAM_NO="+EXAM_NO+"&EXAM_CODE="+EXAM_CODE+"";
	}
	
	public static void main(String[] args) {
//		String jsonString = JSONSerializer.toJSON(new DeleteParam()).toString();
//		System.out.println(jsonString);
		DeleteParam deleteParam = new DeleteParam();
		deleteParam.setEXAM_NO("123");
		deleteParam.setEXAM_CODE("456");
		System.out.println(deleteParam);
	}
}
