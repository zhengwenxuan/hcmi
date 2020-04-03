package com.hjw.webService.job.bean;

public class ExaminfoDiseaseDTO implements java.io.Serializable{

	private static final long serialVersionUID = -97502163798576023L;
	private long id;
	private long exam_info_id;
	private long disease_id;
	private String disease_name="";
	private long disease_index;
	private String isActive="";
	private long creater;
	private String create_time="";
	private long updater;
	private String update_time="";
	private String disease_type="";
	private String icd_10="";
	private String final_doc_num="";
	private String suggest="";
	private String disease_class="";
    private String work_num="";
    private String dep_num="";
    private String disease_num;
    private String disease_key;
    
	public String getDisease_num() {
		return disease_num;
	}

	public void setDisease_num(String disease_num) {
		this.disease_num = disease_num;
	}

	public String getDisease_key() {
		return disease_key;
	}

	public void setDisease_key(String disease_key) {
		this.disease_key = disease_key;
	}

	public String getWork_num() {
		return work_num;
	}

	public void setWork_num(String work_num) {
		this.work_num = work_num;
	}

	public String getDep_num() {
		return dep_num;
	}

	public void setDep_num(String dep_num) {
		this.dep_num = dep_num;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExam_info_id() {
		return exam_info_id;
	}

	public void setExam_info_id(long exam_info_id) {
		this.exam_info_id = exam_info_id;
	}

	public long getDisease_id() {
		return disease_id;
	}

	public void setDisease_id(long disease_id) {
		this.disease_id = disease_id;
	}

	public String getDisease_name() {
		return disease_name;
	}

	public void setDisease_name(String disease_name) {
		this.disease_name = disease_name;
	}

	public long getDisease_index() {
		return disease_index;
	}

	public void setDisease_index(long disease_index) {
		this.disease_index = disease_index;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public long getCreater() {
		return creater;
	}

	public void setCreater(long creater) {
		this.creater = creater;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public long getUpdater() {
		return updater;
	}

	public void setUpdater(long updater) {
		this.updater = updater;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getDisease_type() {
		return disease_type;
	}

	public void setDisease_type(String disease_type) {
		this.disease_type = disease_type;
	}

	public String getIcd_10() {
		return icd_10;
	}

	public void setIcd_10(String icd_10) {
		this.icd_10 = icd_10;
	}

	public String getFinal_doc_num() {
		return final_doc_num;
	}

	public void setFinal_doc_num(String final_doc_num) {
		this.final_doc_num = final_doc_num;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public String getDisease_class() {
		return disease_class;
	}

	public void setDisease_class(String disease_class) {
		this.disease_class = disease_class;
	}
}
