package com.hjw.webService.client.jsjg.bean.in;

import com.google.gson.Gson;

public class MsgBody_Report {

	private InParam_Report InParam;

	public InParam_Report getInParam() {
		return InParam;
	}

	public void setInParam(InParam_Report InParam) {
		this.InParam = InParam;
	}
	
	public static void main(String[] args) {
		String jsonStr = "{\"InParam\": {\"Report\": [{\"ReportNo\": \"A000033064\",\"HospitalCode\": \"医院代码\",\"RptunitID\": \"报告单元ID\",\"ApplyOrderNo\": \"819604578\",\"BarCode\": \"条码号\",\"PatientNo\": \"1234160611\",\"VisitType\": \"I\",\"VisitNo\": \"S001236\",\"SampleDesc\": \"血清\",\"SampleNo\": \"1234567\",\"ReportClass\": \"01\",\"ReportClassName\": \"血细胞分析\",\"ItemCode\": \"检验项目代码\",\"ItemName\": \"检验项目名称\",\"TestItemId\": \"123\",\"EnglishName\": \"One\",\"ChineseName\": \"123\",\"ApplyDatetime\": \"申请时间\",\"ApplyDoctorNo\": \"申请医生代码\",\"ApplyDoctorName\": \"申请医生名称\",\"DrawDocCode\": \"L10023\",\"DrawDocName\": \"李若彤\",\"SampleTime\": \"2016-03-13 09:00:00\",\"ReciveDoc\": \"L00168\",\"ReciveDocName\": \"黄蓉\",\"ReciveTime\": \"2016-03-13 09:10:00\",\"ReportDoc\": \"L00178\",\"ReportDocName\": \"欧阳修\",\"ReportTime\": \"2016-03-13 09:10:00\",\"ExamDept\": \"1011\",\"ExamDeptName\": \"检验科\",\"VerifiDoc\": \"L00178\",\"VerifiDocName\": \"欧阳修\",\"VerifiTime\": \"2016-03-13 09:10:00\",\"VerifiTime2\": \"\",\"VerifiDoc2\": \"\",\"VerifiDocName2\": \"\",\"LastUpdateTime\": \"\",\"BeforeReportTime\": \"\",\"LastPrintTime\": \"\",\"PrintNum\": \"\",\"ReleaseTime\": \"\",\"ReleasePerson\": \"\",\"ChargeFlag\": \"\",\"ApplyReason\": \"\",\"IsUpdate\": \"\",\"IsCheckUpdate\": \"\",\"DeleteReason\": \"\",\"UpTime\": \"\",\"UpInfo\": \"\",\"OutPrintNum\": \"\",\"Status\": \"3\",\"Results\": {\"Result\": [{\"SortNo\": \"1\",\"ItemCode\": \"C100.0015\",\"ItemName\": \"细菌培养+药敏\",\"ItemResult\": \"三种以上细菌生长，可以污染\",\"FloraProportion\": \"大量，假菌丝\",\"MedicineAllergys\": {\"MedicineAllergy\": [{\"SortNo\": \"1\",\"MedicineCode\": \"TET\",\"MedicineName\": \"四环素\",\"MedicineAllergyResult\": \"R\",\"MIC\": \"≤4\"},{\"SortNo\": \"2\",\"MedicineCode\": \"CLR\",\"MedicineName\": \"克拉霉素\",\"MedicineAllergyResult\": \"S\",\"MIC\": \"＞4\"}]}}]}}]}}";
		MsgBody_Report msgBody = new Gson().fromJson(jsonStr, MsgBody_Report.class);
		System.out.println(msgBody.getInParam().getReport().get(0).getResults().getResult().get(0).getItemResult());
	}
}
