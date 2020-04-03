package com.hjw.webService.client.bdyx.useCode;

import java.util.List;

import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.LisResult;
import com.hjw.webService.client.bdyx.bean.lis.res.ImmunityResult;
import com.hjw.webService.client.bdyx.bean.lis.res.LisResultBDYX;
import com.hjw.webService.client.bdyx.bean.lis.res.ReportLisBDYX;
import com.hjw.webService.client.bdyx.bean.lis.res.ReportDoctorInfo;
import com.hjw.webService.client.bdyx.bean.lis.res.ReviewDoctorInfo;
import com.hjw.webService.client.body.ResultHeader;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetLisMicrobeResControlBDYX_UseCode {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
    public ResultHeader getMessage(String body, String logname){
    	ResultHeader rb = new ResultHeader();
    	LisResultBDYX lisResultBDYX = new Gson().fromJson(body, LisResultBDYX.class);
//    	logname = logname+"-"+lisResultBDYX.getVisitOrdNo();
    	TranLogTxt.liswriteEror_to_txt(logname, "body:"+body);
    	LisResult lisResult = new LisResult();
    	
    	String reporterName = "";
    	String reportDate = "";
    	List<ReportDoctorInfo> reportDoctorList = lisResultBDYX.getReportDoctorInfo();
    	if(reportDoctorList.isEmpty()) {
    		TranLogTxt.liswriteEror_to_txt(logname,"报告医生为空");
    		lisResult.setRead_flag(3);
    		lisResult.setNote("报告医生为空");
    	} else {
    		reporterName = reportDoctorList.get(0).getReporterName();
    		reportDate = reportDoctorList.get(0).getReportDate();
    		if(!StringUtil.isEmpty(reportDate) && reportDate.length()==14) {
    			reportDate = reportDate.substring(0, 4)+"-"+reportDate.substring(4, 6)+"-"+reportDate.substring(6, 8)+" "+reportDate.substring(8, 10)+":"+reportDate.substring(10, 12)+":"+reportDate.substring(12, 14);
    		}
    	}
    	String reviewName = "";
    	List<ReviewDoctorInfo> reviewDoctorList = lisResultBDYX.getReviewDoctorInfo();
    	if(reviewDoctorList.isEmpty()) {
    		TranLogTxt.liswriteEror_to_txt(logname,"审核医生为空");
    		lisResult.setRead_flag(3);
    		lisResult.setNote("审核医生为空");
    	} else {
    		reviewName = reviewDoctorList.get(0).getReviewerName();
    	}
		lisResult.setTil_id(logname);
		lisResult.setExam_num(lisResultBDYX.getVisitOrdNo());
		lisResult.setSample_barcode(lisResultBDYX.getSample().getSampleNo());
		lisResult.setDoctor(reporterName);
		lisResult.setExam_date(reportDate);
		lisResult.setSh_doctor(reviewName);
		boolean flagss = true;
		int seq_code = 0;
		
		TranLogTxt.liswriteEror_to_txt(logname,"lisResultBDYX.getReport().size():"+lisResultBDYX.getReport().size());
		for (ReportLisBDYX report : lisResultBDYX.getReport()) {
			lisResult.setLis_item_code(report.getLabItemCode());
			lisResult.setLis_item_name(report.getLabItemName());
			TranLogTxt.liswriteEror_to_txt(logname,"report.getImmunityResult().size():"+report.getImmunityResult().size());
			for (ImmunityResult immunityResult : report.getImmunityResult()) {
				lisResult.setSeq_code(seq_code++);
				lisResult.setReport_item_code(immunityResult.getItemCode());
				lisResult.setReport_item_name(immunityResult.getItemNameCn());
				lisResult.setItem_result(immunityResult.getLabResult());
				lisResult.setRef(immunityResult.getReferences());
				
				boolean succ = this.configService.insert_lis_result(lisResult);
				if (!succ) {
					flagss = false;
				}
			}
		}
		if (flagss) {
			TranLogTxt.liswriteEror_to_txt(logname,"lis信息 入库成功");
			rb.setTypeCode("AA");
			rb.setText("lis信息 入库成功");
		} else {
			TranLogTxt.liswriteEror_to_txt(logname,"lis信息 入库错误");
			rb.setTypeCode("AE");
			rb.setText("lis信息 入库错误");
		}
		String xml = JaxbUtil.convertToXmlWithOutHead(rb, true);		
		TranLogTxt.liswriteEror_to_txt(logname,"res:"+xml);
    	return rb;    	
    }
    
}
