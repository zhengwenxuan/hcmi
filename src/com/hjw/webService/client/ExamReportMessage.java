package com.hjw.webService.client;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.zhaotong.ExamReportDeptDetailMessageZT;
import com.hjw.webService.client.zhaotong.ExamReportDetailMessageZT;
import com.hjw.webService.client.zhaotong.ExamReportMessageZT;

public class ExamReportMessage {
	
	public String getReport(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, DateTimeUtil.getDateTime()+"---总检报告查询---"+userType+"---------------------------");
		String rb="";
		if ("30".equals(userType)) {    //昭通
			ExamReportMessageZT erm= new ExamReportMessageZT();
			rb = erm.getReport(xmlstr, logName);
		}else {
			rb=("AE");
			TranLogTxt.liswriteEror_to_txt(logName, "---总检报告查询---接口无对应厂家,请检查webservice_configuration表config_method字段---------------------------");
		}
		return rb;
	}
	
	public String getReportDetail(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, DateTimeUtil.getDateTime()+"---总检报告明细查询---"+userType+"---------------------------");
		String rb="";
		if ("30".equals(userType)) {    //昭通
			ExamReportDetailMessageZT erm= new ExamReportDetailMessageZT();
			rb = erm.getReportDetail(xmlstr, logName);
		}else {
			rb=("AE");
			TranLogTxt.liswriteEror_to_txt(logName, "---总检报告查询---接口无对应厂家,请检查webservice_configuration表config_method字段---------------------------");
		}
		return rb;
	}
	
	public String getReportDeptDetail(String xmlstr,String userType,String logName) {
		TranLogTxt.liswriteEror_to_txt(logName, DateTimeUtil.getDateTime()+"---总检报告分科明细查询---"+userType+"---------------------------");
		String rb="";
		if ("30".equals(userType)) {    //昭通
			ExamReportDeptDetailMessageZT erm= new ExamReportDeptDetailMessageZT();
			rb = erm.getReportDeptDetail(xmlstr, logName);
		}else {
			rb=("AE");
			TranLogTxt.liswriteEror_to_txt(logName, "---总检报告查询---接口无对应厂家,请检查webservice_configuration表config_method字段---------------------------");
		}
		return rb;
	}
	
}
