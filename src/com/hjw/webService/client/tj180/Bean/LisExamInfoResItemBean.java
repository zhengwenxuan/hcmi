package com.hjw.webService.client.tj180.Bean;

public class LisExamInfoResItemBean {
	private long exam_id;
	private String exam_num="";
	private long eci_id;
	private String his_num="";
	private long sam_demo_id;
	private String samplebarcode="";
	
	public String getSamplebarcode() {
		return samplebarcode;
	}
	public void setSamplebarcode(String samplebarcode) {
		this.samplebarcode = samplebarcode;
	}
	public long getSam_demo_id() {
		return sam_demo_id;
	}
	public void setSam_demo_id(long sam_demo_id) {
		this.sam_demo_id = sam_demo_id;
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
	public long getEci_id() {
		return eci_id;
	}
	public void setEci_id(long eci_id) {
		this.eci_id = eci_id;
	}
	public String getHis_num() {
		return his_num;
	}
	public void setHis_num(String his_num) {
		this.his_num = his_num;
	}
}
