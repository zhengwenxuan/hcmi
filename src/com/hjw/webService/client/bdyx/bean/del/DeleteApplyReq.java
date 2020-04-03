package com.hjw.webService.client.bdyx.bean.del;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;

public class DeleteApplyReq {

	private static String HOSPITAL_ID = "";
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		HOSPITAL_ID = configService.getCenterconfigByKey("HOSPITAL_ID").getConfig_value();
	}
	private String triggerEvent = "cancel";//消息交互类型
	private String patientDomain = "02";//域ID
	private String patientLid = "";//患者ID
	private String visitTimes = "1";//就诊次数
	private String visitType = "0401";//就诊类别编码
	private String visitTypeName = "体检";//就诊类别名称
	private String visitOrdNo = "";//就诊流水号
	private String deptCode = "";//病人科室编码
	private String deptName = "";//病人科室名称
	private String orgCode = HOSPITAL_ID;//医疗机构代码
	private String orgName = "武威医学科学院";//医疗机构名称
	private List<Order> order = new ArrayList<>();//1..n 医嘱信息（可循环）
	
	public String getTriggerEvent() {
		return triggerEvent;
	}
	public String getPatientDomain() {
		return patientDomain;
	}
	public String getPatientLid() {
		return patientLid;
	}
	public String getVisitTimes() {
		return visitTimes;
	}
	public String getVisitType() {
		return visitType;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public String getVisitOrdNo() {
		return visitOrdNo;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public List<Order> getOrder() {
		return order;
	}
	public void setTriggerEvent(String triggerEvent) {
		this.triggerEvent = triggerEvent;
	}
	public void setPatientDomain(String patientDomain) {
		this.patientDomain = patientDomain;
	}
	public void setPatientLid(String patientLid) {
		this.patientLid = patientLid;
	}
	public void setVisitTimes(String visitTimes) {
		this.visitTimes = visitTimes;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public void setVisitOrdNo(String visitOrdNo) {
		this.visitOrdNo = visitOrdNo;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
}
