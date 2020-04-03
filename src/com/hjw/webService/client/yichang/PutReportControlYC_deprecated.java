package com.hjw.webService.client.yichang;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.DTO.FileDTO;
import com.hjw.interfaces.FTPServer.PdfFtpHelper;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.yichang.bean.cdr.client.generalReport.RegistGeneralReport;
import com.hjw.webService.client.yichang.bean.cdr.client.header.PRPA_IN000001UV03;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceCDR;
import com.hjw.webService.client.yichang.gencode.SERVDelegate;
import com.hjw.webService.client.yichang.gencode.SERVService;
import com.hjw.webService.client.yichang.gencode.SERVServiceLocator;
import com.hjw.webService.job.bean.ExamSummaryDTO;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ReportPdfDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PutReportControlYC_deprecated {
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
	}

	public ResultHeader getMessage(String url, int days, String logName) {
		String datetime = DateTimeUtil.DateDiff2(days);
		ResultHeader rh = new ResultHeader();

		String sb1 = "SELECT top 10 a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest,"
				+ "a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time,"
				+ "a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time,"
				+ "x.chi_name as examdocname,y.chi_name as checkdocname,c.exam_num,c.pdf_file  " 
				+ " FROM exam_summary a "
				+ " left join user_usr x on x.id=a.exam_doctor_id "
				+ " left join user_usr y on y.id=a.check_doc "
				+ " ,exam_info b, report_pdf c "
				+ "where a.approve_status in ('A','B') and a.exam_info_id=b.id and b.exam_num = c.exam_num and c.is_finished = 2 "// and read_status='0'
				+ " and (CONVERT(varchar(50),c.report_date,23)>= '" + datetime+ "') "
				+ " order by c.report_date ";
		TranLogTxt.liswriteEror_to_txt(logName, "操作语句： " + sb1);
		List<ExamSummaryDTO> userList = jdbcQueryManager.getList(sb1, ExamSummaryDTO.class);
		for (ExamSummaryDTO es : userList) {
			sendreportone(url,es,logName);
		}
		rh.setTypeCode("AA");
		rh.setText("");
		return rh;
	}
	
	private ResultHeader sendreportone(String url,ExamSummaryDTO es,String logname){
		ResultHeader rh = new ResultHeader();
		//注册通用报告
		try {
			ExamInfoUserDTO ei = this.getExamInfoForNum(es.getExam_info_id(), logname);
			RegistGeneralReport generalReport = getGeneralReport(es, ei, logname);
			PRPA_IN000001UV03 reqGeneralReport = new PRPA_IN000001UV03(ei.getExam_num());
			reqGeneralReport.getControlActProcess().getSubject().getRequest().setRegistGeneralReport(generalReport);
			
			String xml = JaxbUtil.convertToXml(reqGeneralReport, true);
			SERVService servService = new SERVServiceLocator(url);
			SERVDelegate servDelegate = servService.getSERVPort();
			reqGeneralReport.getControlActProcess().getSubject().getRequest().getRegistGeneralReport().getRegistGeneralReportReq().setPdfBase64("");
			TranLogTxt.liswriteEror_to_txt(logname,"reqGeneralReport:"+JaxbUtil.convertToXml(reqGeneralReport, true));
			String resGeneralReport = servDelegate.HIPMessageServer("registGeneralReport", xml);
			TranLogTxt.liswriteEror_to_txt(logname,"resGeneralReport:"+resGeneralReport);	
			DataSourceCDR resData = new DataSourceCDR(resGeneralReport, true);
			if("AA".equals(resData.getStatus())) {
				//read_status 字段微信已用，考虑在report_pdf加个标识
				String sql = "update report_pdf set is_finished='9' where exam_num ='" + es.getExam_num() + "' ";
				this.jdbcPersistenceManager.executeSql(sql);
				
				TranLogTxt.liswriteEror_to_txt(logname,es.getExam_num()+"-"+"注册报告成功");
				rh.setTypeCode("AA");
				rh.setText(es.getExam_num()+"-"+"注册报告成功");
			} else {
				TranLogTxt.liswriteEror_to_txt(logname,"AE:"+resData.getMessage());
				rh.setTypeCode("AE");
				rh.setText(resData.getMessage());
			}
		} catch (Exception ex){
		    rh.setTypeCode("AE");
			rh.setText("注册报告-调用webservice错误Exception："+com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname,"注册报告-调用webservice错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rh;
	}
	
	private RegistGeneralReport getGeneralReport(ExamSummaryDTO es, ExamInfoUserDTO ei,String logname) {
		
//		String base64Str = "";
//		try {
//			File file = new File("D://report/"+ei.getExam_num()+".pdf");
//			FileInputStream inputFile = new FileInputStream(file);
//			byte[] buffer = new byte[(int) file.length()];
//			inputFile.read(buffer);
//			inputFile.close();
//			base64Str = Base64.base64Encode(buffer);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		ReportPdfDTO pdf=new ReportPdfDTO();
		pdf = this.reportPDF(ei.getExam_num(), logname);
		String filename = "";
		String base64Str = "";
		if(pdf.getExam_num().length()>0){
			PdfFtpHelper pdfh = null;
			try{
				pdfh=new PdfFtpHelper();
				TranLogTxt.liswriteEror_to_txt(logname, "="+pdf.getFtp_server()+"="+pdf.getFtp_port()+"="+pdf.getFtp_user()+"="+pdf.getFtp_password()+"=");
				boolean pdfconn=  pdfh.login(pdf.getFtp_server(), pdf.getFtp_port(), pdf.getFtp_user(), pdf.getFtp_password());
				if(pdfconn){
					FileDTO fd= pdfh.downloadFile(pdf.getReport_path(), pdf.getPdf_file(),false);
					if (fd.isFlag()) {
						filename = ei.getExam_num() + "体检报告.pdf";
						base64Str = Base64.base64Encode(fd.getBytes());
					}				  
				}
			}catch(Exception ex){
				  ex.printStackTrace();
			}finally{
				if(pdfh!=null){
					pdfh.close(); 
				}
			}
		}
		
		String sexcode = "";
		if("男".equals(ei.getSex()) || "男性".equals(ei.getSex())) {
			sexcode = "1";
			ei.setSex("男性");
		} else if("女".equals(ei.getSex()) || "女性".equals(ei.getSex())) {
			sexcode = "2";
			ei.setSex("女性");
		} else {
			sexcode = "0";
			ei.setSex("未知的性别");
		}
		
		String birthday = "";
		if(!StringUtil.isEmpty(ei.getBirthday())) {
			birthday = ei.getBirthday().replace(" ", "T");
		}
		
		String reportContent = ei.getSet_name();;
		if(StringUtil.isEmpty(reportContent)) {
			reportContent = "";
			List<ChargingItemDTO> chargingItemList = this.getChargingItemForId(ei.getId(), logname);
			for(ChargingItemDTO chargingItem : chargingItemList) {
				reportContent += (chargingItem.getItem_name() + ",");
			}
			reportContent = reportContent.substring(0, reportContent.length()-1);
		}
		
		UserInfoDTO exam_doctor = getUser(es.getExam_doctor_id(), logname);
//		UserInfoDTO check_doc = getUser(es.getCheck_doc(), logname);
		UserInfoDTO check_doc = exam_doctor;
		
		RegistGeneralReport generalReport = new RegistGeneralReport();
//		generalReport.getRegistGeneralReportReq().setReportNO();
//		generalReport.getRegistGeneralReportReq().setApplyType();
		generalReport.getRegistGeneralReportReq().setDiagnosisNo(ei.getArch_num());
		generalReport.getRegistGeneralReportReq().setClinicFlowNo(ei.getExam_num());
		generalReport.getRegistGeneralReportReq().setLocalId(pdf.getPdf_file());
//		generalReport.getRegistGeneralReportReq().setPatientSource();
//		generalReport.getRegistGeneralReportReq().setRoomName();
//		generalReport.getRegistGeneralReportReq().setBedNO();
		generalReport.getRegistGeneralReportReq().setPatientName(ei.getUser_name());
		generalReport.getRegistGeneralReportReq().setSexCode(sexcode);
		generalReport.getRegistGeneralReportReq().setSexName(ei.getSex());
		generalReport.getRegistGeneralReportReq().setBirthDate(birthday);
		generalReport.getRegistGeneralReportReq().setAge(ei.getAge()+"岁");
//		generalReport.getRegistGeneralReportReq().setApplyNO();
//		generalReport.getRegistGeneralReportReq().setReportTitle();
		generalReport.getRegistGeneralReportReq().setReportContent(reportContent);
//		generalReport.getRegistGeneralReportReq().setOpenTime();
//		generalReport.getRegistGeneralReportReq().setOrgCode();
//		generalReport.getRegistGeneralReportReq().setOrgName();
		generalReport.getRegistGeneralReportReq().setReportWriterDortor(exam_doctor.getChi_Name());
		generalReport.getRegistGeneralReportReq().setReportWriterDortorCode(exam_doctor.getWork_num());
		generalReport.getRegistGeneralReportReq().setReportAuditDoctor(check_doc.getChi_Name());
		generalReport.getRegistGeneralReportReq().setReportAuditDoctorCode(check_doc.getWork_num());
//		generalReport.getRegistGeneralReportReq().setReviewDoctorDeptCode();
//		generalReport.getRegistGeneralReportReq().setReviewDoctorDeptName();
		generalReport.getRegistGeneralReportReq().setReportDate(pdf.getReport_date().replace(" ", "T"));
//		generalReport.getRegistGeneralReportReq().getExaminationFileList().add()
//		generalReport.getRegistGeneralReportReq().setXmlDocument();
		generalReport.getRegistGeneralReportReq().setPdfBase64(base64Str);
		
//		generalReport.getRegistGeneralReportReq().setApplicationDoctorName();
//		generalReport.getRegistGeneralReportReq().setApplicationDoctorNo();
//		generalReport.getRegistGeneralReportReq().setEmergencyStatus();
//		generalReport.getRegistGeneralReportReq().setExaminationPurpose();
//		generalReport.getRegistGeneralReportReq().setReportView();
//		generalReport.getRegistGeneralReportReq().setDiagnosePrompt();
//		generalReport.getRegistGeneralReportReq().setSimpleTime();
//		generalReport.getRegistGeneralReportReq().setCodeExpand();
	return generalReport;
}

	public ExamInfoUserDTO getExamInfoForNum(long id,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,"
				+ "c.register_date,c.join_date,c.exam_times,a.arch_num"
				+ ",a.user_name,a.id_num,a.sex,a.birthday,a.nation,a.phone ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.id = '" + id + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "getExamInfoForNum： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	private UserInfoDTO getUser(long id, String logname) throws ServiceException {
		String sql = "select u.work_num,u.chi_name from user_usr u where  u.id = "+id+" and u.is_active = 'Y'";
		TranLogTxt.liswriteEror_to_txt(logname, "sql： " +sql);
		List<UserInfoDTO> list = this.jdbcQueryManager.getList(sql, UserInfoDTO.class);
		return list.get(0);
	}
	
	private ReportPdfDTO reportPDF(String exam_num, String logname) throws ServiceException {
		String sql = "SELECT top 1 exam_num,arch_num,report_year,pdf_file,report_path,ftp_server,ftp_port,ftp_user,ftp_password,"
				+ "is_finished,report_date,create_time FROM report_pdf where exam_num='"+exam_num+"' order by report_date desc";
		TranLogTxt.liswriteEror_to_txt(logname, "reportPDF： " +sql);
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ReportPdfDTO.class);
		ReportPdfDTO eu = new ReportPdfDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ReportPdfDTO)map.getList().get(0);			
		}
		return eu;
	}
	
	private List<ChargingItemDTO> getChargingItemForId(long exam_id, String logname)throws ServiceException {
		String sql = "select a.id,a.item_code,a.item_name from Charging_Item a,Examinfo_Charging_Item g where a.id=g.charge_item_id and g.examinfo_id=" + exam_id + " and g.exam_status<>'G' and g.pay_status<>'M' and g.isActive='Y' ";
		List<ChargingItemDTO> list= new ArrayList<ChargingItemDTO>();
		TranLogTxt.liswriteEror_to_txt(logname, "getChargingItemForId： " +sql);
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 1000, ChargingItemDTO.class);
		
		if((map!=null)&&(map.getList()!=null)){
			list=map.getList();
		}
		return list;
	}
}
