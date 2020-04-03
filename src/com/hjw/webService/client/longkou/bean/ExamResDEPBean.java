package com.hjw.webService.client.longkou.bean;

public class ExamResDEPBean implements java.io.Serializable {
	 private static final long serialVersionUID = -97502163798576023L;

		private long id;
		private String dep_num="";
		private String dep_name="";
        private String remark="";
        private String exam_doctor="";
        private String exam_result_summary="";
        private String create_time="";
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getDep_num() {
			return dep_num;
		}
		public void setDep_num(String dep_num) {
			this.dep_num = dep_num;
		}
		public String getDep_name() {
			return dep_name;
		}
		public void setDep_name(String dep_name) {
			this.dep_name = dep_name;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getExam_doctor() {
			return exam_doctor;
		}
		public void setExam_doctor(String exam_doctor) {
			this.exam_doctor = exam_doctor;
		}
		public String getExam_result_summary() {
			return exam_result_summary;
		}
		public void setExam_result_summary(String exam_result_summary) {
			this.exam_result_summary = exam_result_summary;
		}
		public String getCreate_time() {
			return create_time;
		}
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
        
        
        
	}