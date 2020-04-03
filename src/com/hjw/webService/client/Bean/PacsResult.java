package com.hjw.webService.client.Bean;

import com.hjw.util.DateTimeUtil;

public class PacsResult  implements java.io.Serializable{

	private static final long serialVersionUID = 4783399711232352000L;

	private long id;//	ID号	自增型
	private String req_no="";//	申请单号	
	private String pacs_checkno="";//	PACS报告ID	
	private String exam_num="";//	体检号	
	private String til_id="";//	第三方通讯日志主表id	thrid_interface_log表id简称
	private String item_name="";//	项目名称	多个项目名称以英文逗号隔开
	private String pacs_item_code="";//	PACS检查项目代码	多个项目代码以英文逗号隔开
	private String study_type="";//	检查类别	US(超声)MR(核磁)CT(CT检查)XX(射线检查)
	private String study_body_part="";//	检查部位	多个部位以英文逗号隔开
	private String clinic_diagnose="";//	检查结论	
	private String clinic_symptom="";//	检查描述	
	private String clinic_advice="";//	医师意见	
	private String is_abnormal="";//	阳性状态	N-正常 Y-阳性报告 C-危急
	private String report_img_path="";//	报告图片路径	多路径以^隔开，如：20180512\report_1.jpg^20180512\report_2.jpg
	private String img_path="";//	检查图片(报告内图，俗称小图)	多路径以^隔开，如：20180512\1.jpg^20180512\2.jpg
	private int study_state=5;//	检查状态	0：默认；1：登记取消；2：已登记；3：已经检查完4：已经报告完5：已经审核完；只有状态为0或1时体检系统才可以退费,此处固定为5
	private String reg_doc="";//	记录医生姓名	
	private String check_doc="";//	检查医生姓名	
	private String check_date="";//	检查时间	
	private String report_doc="";//	报告医生	
	private String report_date="";//	报告时间	
	private String audit_doc="";//	审核医师	
	private String audit_date="";//	审核时间	
	private String note="";//	备注	备注( 可记录处理失败信息等)
	private int status;//	报告读取标志	默认为0,体检系统读取后改为1,无匹配项目为2，其他错误为3
	private int is_tran_image=1;//	是否取图	默认为1,0 - 不取,1 - 取
	private int is_report_image=1;//	是否为报告图	0 - 图片不包含文字报告	1 - 图片为整个报告	默认为1
	private String create_time = DateTimeUtil.getDateTime();//插入时间
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getReq_no() {
		return req_no;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public String getPacs_checkno() {
		return pacs_checkno;
	}
	public void setPacs_checkno(String pacs_checkno) {
		this.pacs_checkno = pacs_checkno;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getTil_id() {
		return til_id;
	}
	public void setTil_id(String til_id) {
		this.til_id = til_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getPacs_item_code() {
		return pacs_item_code;
	}
	public void setPacs_item_code(String pacs_item_code) {
		this.pacs_item_code = pacs_item_code;
	}
	public String getStudy_type() {
		return study_type;
	}
	public void setStudy_type(String study_type) {
		this.study_type = study_type;
	}
	public String getStudy_body_part() {
		return study_body_part;
	}
	public void setStudy_body_part(String study_body_part) {
		this.study_body_part = study_body_part;
	}
	public String getClinic_diagnose() {
		return clinic_diagnose;
	}
	public void setClinic_diagnose(String clinic_diagnose) {
		this.clinic_diagnose = clinic_diagnose;
	}
	public String getClinic_symptom() {
		return clinic_symptom;
	}
	public void setClinic_symptom(String clinic_symptom) {
		this.clinic_symptom = clinic_symptom;
	}
	public String getClinic_advice() {
		return clinic_advice;
	}
	public void setClinic_advice(String clinic_advice) {
		this.clinic_advice = clinic_advice;
	}
	public String getIs_abnormal() {
		return is_abnormal;
	}
	public void setIs_abnormal(String is_abnormal) {
		this.is_abnormal = is_abnormal;
	}
	public String getReport_img_path() {
		return report_img_path;
	}
	public void setReport_img_path(String report_img_path) {
		this.report_img_path = report_img_path;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public int getStudy_state() {
		return study_state;
	}
	public void setStudy_state(int study_state) {
		this.study_state = study_state;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getIs_tran_image() {
		return is_tran_image;
	}
	public void setIs_tran_image(int is_tran_image) {
		this.is_tran_image = is_tran_image;
	}
	public int getIs_report_image() {
		return is_report_image;
	}
	public void setIs_report_image(int is_report_image) {
		this.is_report_image = is_report_image;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
}
