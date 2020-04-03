package com.hjw.webService.client.Bean;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description:  医生和科室
     * @author: yangm     
     * @date:   2016年10月7日 上午11:36:01   
     * @version V2.0.0.0
 */
public class Doctor {
	
	private String time = "201205061000";// 开单时间
	private String doctorCode="";// 医生编码
	private String doctorName="";// 医生姓名
	private String dept_code="";// 申请科室编码
	private String dept_name="";// 申请科室名称

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

}
