package com.hjw.webService.client.jsjg.bean.in;

public class InParam_Status {

	private String VisitType = "";//就诊类型	例如门诊，住院等
	private String PatientNo = "";//病人号
	private String PatientName = "";//病人姓名
	private String ApplyID = "";//医嘱ID
	private String BarCode = "";//条码号
	private String Exec = "";//执行人
	private String ExecTime = "";//执行时间
	private String ExecStatus = "";//执行状态	（1：生成条码，2:采样确认，3、标本接收，4、报告审核，5、作废条码，6、取消签收，7、取消审核）
	
	public String getVisitType() {
		return VisitType;
	}
	public String getPatientNo() {
		return PatientNo;
	}
	public String getPatientName() {
		return PatientName;
	}
	public String getApplyID() {
		return ApplyID;
	}
	public String getBarCode() {
		return BarCode;
	}
	public String getExec() {
		return Exec;
	}
	public String getExecTime() {
		return ExecTime;
	}
	public String getExecStatus() {
		return ExecStatus;
	}
	public void setVisitType(String visitType) {
		VisitType = visitType;
	}
	public void setPatientNo(String patientNo) {
		PatientNo = patientNo;
	}
	public void setPatientName(String patientName) {
		PatientName = patientName;
	}
	public void setApplyID(String applyID) {
		ApplyID = applyID;
	}
	public void setBarCode(String barCode) {
		BarCode = barCode;
	}
	public void setExec(String exec) {
		Exec = exec;
	}
	public void setExecTime(String execTime) {
		ExecTime = execTime;
	}
	public void setExecStatus(String execStatus) {
		ExecStatus = execStatus;
	}

}
