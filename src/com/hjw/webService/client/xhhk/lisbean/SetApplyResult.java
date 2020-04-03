package com.hjw.webService.client.xhhk.lisbean;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class SetApplyResult {

	private String ItemCode = "";//检验项目编号
	private String ApplyNo = "";//体检申请单流水号
	private String ReportNo = "";//报告单号
	private String ReportDoct = "";//检验医生
	private String ReportTime = "";//检验时间
	private String ReviewDoct = "";//审核医生
	private String ReviewTime = "";//审核时间
	
	private List<ItemsResultXHHK> Items = new ArrayList<>();//

	public String getItemCode() {
		return ItemCode;
	}

	public String getReportNo() {
		return ReportNo;
	}

	public String getReportDoct() {
		return ReportDoct;
	}

	public String getReportTime() {
		return ReportTime;
	}

	public String getReviewDoct() {
		return ReviewDoct;
	}

	public String getReviewTime() {
		return ReviewTime;
	}

	public List<ItemsResultXHHK> getItems() {
		return Items;
	}

	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}

	public void setReportNo(String reportNo) {
		ReportNo = reportNo;
	}

	public void setReportDoct(String reportDoct) {
		ReportDoct = reportDoct;
	}

	public void setReportTime(String reportTime) {
		ReportTime = reportTime;
	}

	public void setReviewDoct(String reviewDoct) {
		ReviewDoct = reviewDoct;
	}

	public void setReviewTime(String reviewTime) {
		ReviewTime = reviewTime;
	}

	public void setItems(List<ItemsResultXHHK> items) {
		Items = items;
	}
	
	public String getApplyNo() {
		return ApplyNo;
	}

	public void setApplyNo(String applyNo) {
		ApplyNo = applyNo;
	}

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer("");
		sb.append("{ ");
		sb.append("	\"ItemCode\": \"5c6d091ee082922974525f89\",");
		sb.append("	\"ReportNo\": \"77898278453\", ");
		sb.append("	\"ReportDoct\": \"大大\",  ");
		sb.append("	\"ReportTime\": \"2019-01-01 12:00:00\", ");
		sb.append("	\"ReviewDoct\": \"小小\",  ");
		sb.append("	\"ReviewTime\": \"2019-01-02 13:00:00\", ");
		sb.append("	\"Items\": [{  ");
		sb.append("");
		sb.append("		\"ResultNo\": \"3214412\", ");
		sb.append("		\"ResultName\": \"报告名\",");
		sb.append("		\"ResultCode\": \"78896877687\", ");
		sb.append("		\"ResultValue\": \"value132445431\", ");
		sb.append("		\"Unit\": \"unit\",");
		sb.append("		\"ResultFlag\": \"flag1\", ");
		sb.append("		\"NormalRange\": \"range1\", ");
		sb.append("		\"LowerLimit\": \"low\",   ");
		sb.append("		\"UpperLimit\": \"up\" ");
		sb.append("");
		sb.append("	}, { ");
		sb.append("");
		sb.append("		\"ResultNo\": \"3214413\", ");
		sb.append("		\"ResultName\": \"报告名\",");
		sb.append("		\"ResultCode\": \"78896877687\", ");
		sb.append("		\"ResultValue\": \"value132445431\", ");
		sb.append("		\"Unit\": \"unit\",");
		sb.append("		\"ResultFlag\": \"flag1\", ");
		sb.append("		\"NormalRange\": \"range1\", ");
		sb.append("		\"LowerLimit\": \"low\",   ");
		sb.append("		\"UpperLimit\": \"up\" ");
		sb.append("");
		sb.append("	}]   ");
		sb.append("} ");
		SetApplyResult result = new Gson().fromJson(sb.toString(), SetApplyResult.class);
		System.out.println(result.getItemCode());
		System.out.println(result.getItems().get(0).getResultName());
	}
}
