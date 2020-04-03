package com.hjw.webService.client.Bean;

import com.hjw.util.DateTimeUtil;

public class LisResult  implements java.io.Serializable{

	private static final long serialVersionUID = 4783399711232352000L;

	private long id;//	ID号	自增型
	private String exam_num="";//	体检号
	private String til_id="";//	第三方通讯日志表id	thrid_interface_log表id简称
	private String sample_barcode="";//	条码号
	private String lis_item_code="";//	LIS组合项目代码
	private String lis_item_name="";//	LIS组合项目名称
	private String report_item_code="";//	LIS报告细项代码
	private String report_item_name="";//	LIS报告细项名称
	private String exam_date="";//	检查时间
	private String item_result="";//	项目结果
	private String item_unit="";//	项目单位
	private String flag="";//	高低标志	H-高 L-低N-正常HH-偏高报警LL-偏低报警C-危急
	private String ref="";//	参考范围
	private int seq_code;//	顺序号
	private String doctor="";//	检查医生
	private String sh_doctor="";//	审核医生
	private String note="";//	备注	备注( 可记录处理失败信息等)
	private int read_flag;//	读取标志	默认值0，体检系统读取后变为1，无匹配项目为2，其他错误为3
	private String create_time = DateTimeUtil.getDateTime();//插入时间
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getSample_barcode() {
		return sample_barcode;
	}
	public void setSample_barcode(String sample_barcode) {
		this.sample_barcode = sample_barcode;
	}
	public String getLis_item_code() {
		return lis_item_code;
	}
	public void setLis_item_code(String lis_item_code) {
		this.lis_item_code = lis_item_code;
	}
	public String getLis_item_name() {
		return lis_item_name;
	}
	public void setLis_item_name(String lis_item_name) {
		this.lis_item_name = lis_item_name;
	}
	public String getReport_item_code() {
		return report_item_code;
	}
	public void setReport_item_code(String report_item_code) {
		this.report_item_code = report_item_code;
	}
	public String getReport_item_name() {
		return report_item_name;
	}
	public void setReport_item_name(String report_item_name) {
		this.report_item_name = report_item_name;
	}
	public String getExam_date() {
		return exam_date;
	}
	public void setExam_date(String exam_date) {
		this.exam_date = exam_date;
	}
	public String getItem_result() {
		return item_result;
	}
	public void setItem_result(String item_result) {
		this.item_result = item_result;
	}
	public String getItem_unit() {
		return item_unit;
	}
	public void setItem_unit(String item_unit) {
		this.item_unit = item_unit;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public int getSeq_code() {
		return seq_code;
	}
	public void setSeq_code(int seq_code) {
		this.seq_code = seq_code;
	}
	public String getDoctor() {
		return doctor;
	}
	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}
	public String getSh_doctor() {
		return sh_doctor;
	}
	public void setSh_doctor(String sh_doctor) {
		this.sh_doctor = sh_doctor;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getRead_flag() {
		return read_flag;
	}
	public void setRead_flag(int read_flag) {
		this.read_flag = read_flag;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
