package com.hjw.webService.client.qufu.job.bean;

public class RetCustomeQF {
	private String custome_id="";//患者id	
	private String coustom_jzh="";//就诊号	
	private String exam_num="";
	private String pacs_checkno="";//PACS报告ID	
	private String reg_doc="";//记录医生姓名
	private String check_doc="";//检查医生姓名
	private String check_date="";//检查时间
	private String report_doc="";//报告医生
	private String report_date="";//报告时间
	private String audit_doc="";//审核医师
	private String audit_date="";//审核时间		
	
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getReg_doc() {
		return reg_doc;
	}
	public void setReg_doc(String reg_doc) {
		this.reg_doc = reg_doc;
	}
	public String getCheck_doc() {
		return check_doc;
	}
	public void setCheck_doc(String check_doc) {
		this.check_doc = check_doc;
	}
	public String getCheck_date() {
		return check_date;
	}
	public void setCheck_date(String check_date) {
		this.check_date = check_date;
	}
	public String getReport_doc() {
		return report_doc;
	}
	public void setReport_doc(String report_doc) {
		this.report_doc = report_doc;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getAudit_doc() {
		return audit_doc;
	}
	public void setAudit_doc(String audit_doc) {
		this.audit_doc = audit_doc;
	}
	public String getAudit_date() {
		return audit_date;
	}
	public void setAudit_date(String audit_date) {
		this.audit_date = audit_date;
	}
	public String getCustome_id() {
		return custome_id;
	}
	public void setCustome_id(String custome_id) {
		this.custome_id = custome_id;
	}
	public String getCoustom_jzh() {
		return coustom_jzh;
	}
	public void setCoustom_jzh(String coustom_jzh) {
		this.coustom_jzh = coustom_jzh;
	}
	public String getPacs_checkno() {
		return pacs_checkno;
	}
	public void setPacs_checkno(String pacs_checkno) {
		this.pacs_checkno = pacs_checkno;
	}

}
