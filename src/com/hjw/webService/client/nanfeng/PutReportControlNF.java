package com.hjw.webService.client.nanfeng;

import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.FTPServer.PdfFtpHelper;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.nanfeng.gencode.WSInterface;
import com.hjw.webService.client.nanfeng.gencode.WSInterfaceLocator;
import com.hjw.webService.client.nanfeng.gencode.WSInterfacePortType;
import com.hjw.webService.client.nanfeng.util.NanfenHL7;
import com.hjw.webService.job.bean.ExamSummaryDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ReportPdfDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PutReportControlNF {
	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public ResultHeader getMessage(String url, int days, String logName) {
//		String datetime = DateTimeUtil.DateDiff2(days);
		ResultHeader rh = new ResultHeader();

		String sb1 =" SELECT top 10 a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest, "
				+" a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time, "
				+" a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time, "
				+" x.chi_name as examdocname,y.chi_name as checkdocname,c.exam_num,c.pdf_file "
				+" FROM exam_summary a "
				+" left join user_usr x on x.id=a.exam_doctor_id "
				+" left join user_usr y on y.id=a.check_doc "
				+" ,exam_info b, report_pdf c "
				+" where a.approve_status in ('A','B') and a.exam_info_id=b.id and b.exam_num = c.exam_num and c.is_finished = 2 "// and read_status='0'
				//+"and (CONVERT(varchar(50),c.report_date,23)>= '"+ datetime+"')"
				+" order by c.report_date desc";
		TranLogTxt.liswriteEror_to_txt(logName,"res: :操作语句："+ sb1);
		List<ExamSummaryDTO> userList = this.jdbcQueryManager.getList(sb1, ExamSummaryDTO.class);
		for (ExamSummaryDTO es : userList) {
			sendreportone(url,es,logName);
		}
		rh.setTypeCode("AA");
		rh.setText("");
		return rh;
	}
	
	public ResultHeader sendMessage(String url, String exam_num, String logName) {
		ResultHeader rh = new ResultHeader();
		if (exam_num != null && exam_num.trim().length() > 0) {
			String sb1 ="SELECT a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest, "
					+" a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time, "
					+" a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time, "
					+" x.chi_name as examdocname,y.chi_name as checkdocname,b.exam_num,b.join_date"+"FROM exam_summary a "
					+" left join user_usr x on x.id=a.exam_doctor_id "
					+" left join user_usr y on y.id=a.check_doc ,exam_info b "
					+" where a.approve_status='A' and a.exam_info_id=b.id"+"and b.exam_num='"+ exam_num +"'";
			TranLogTxt.liswriteEror_to_txt(logName,"res: :操作语句："+ sb1);
			List<ExamSummaryDTO> userList = this.jdbcQueryManager.getList(sb1, ExamSummaryDTO.class);
			if (userList != null && userList.size() == 1) {
				rh = sendreportone(url, userList.get(0), logName);
			} else {
				rh.setTypeCode("AE");
				rh.setText("不满足上传条件，可能未审核");
			}
		} else {
			rh.setTypeCode("AE");
			rh.setText("无效体检编号");
		}
		return rh;
	}

	private ResultHeader sendreportone(String url,ExamSummaryDTO es,String logName){
	     ResultHeader rh = new ResultHeader();		
			try {
				ExamInfoUserDTO ei = this.configService.getExamInfoForExam_id(es.getExam_info_id());
				ReportPdfDTO pdf=new ReportPdfDTO();
				pdf = this.reportPDF(ei.getExam_num(), logName);
				String filename ="";
				if(pdf.getExam_num().length()>0){
					PdfFtpHelper pdfh = null;
					try{
						pdfh=new PdfFtpHelper();
						TranLogTxt.liswriteEror_to_txt(logName,"="+pdf.getFtp_server()+"="+pdf.getFtp_port()+"="+pdf.getFtp_user()+"="+pdf.getFtp_password()+"=");
						boolean pdfconn=  pdfh.login(pdf.getFtp_server(), pdf.getFtp_port(), pdf.getFtp_user(), pdf.getFtp_password());
						if(pdfconn){
							String reqStr =""
									+"<root>"
									+"<records>"
									+"<record>"
									+"<orgCode>49271091-3</orgCode>"
									+"<sourceSystem>PEIS</sourceSystem>"
									+"<docClass>M</docClass>"
									+"<docType>M0001</docType>"
									+"<docNo>"+es.getExam_info_id()+"</docNo>"
									+"<docTitle>"+pdf.getPdf_file()+"</docTitle>"
									+"<applyNo>"+ei.getExam_num()+"</applyNo>"
									+"<patientId>"+ei.getExam_num()+"</patientId>"
									+"<visitNo>"+ei.getExam_num()+"</visitNo>"
									+"<visitType>3</visitType>"
									+"<visitDept>0118</visitDept>"
									+"<patientAge>"+ei.getAge()+"</patientAge>"
									+"<patientSex>"+ei.getSex()+"</patientSex>"
									+"<bedNo></bedNo>"
									+"<recordTime>"+DateTimeUtil.getDateTime()+"</recordTime>"
									+"<docPath>"+pdf.getReport_path()+"/"+ pdf.getPdf_file()+"</docPath>"
									+"<createOperator>admin</createOperator>"
									+"<createOperatorId>1</createOperatorId>"
									+"<modifyTime></modifyTime>"
									+"<modifyOperator></modifyOperator>"
									+"<modifyOperatorId></modifyOperatorId>"
									+"<validFlag>1</validFlag>"
									+"</record>"
									+"</records>"
									+"</root>";
							
							WSInterface wsInterface = new WSInterfaceLocator(url);
							TranLogTxt.liswriteEror_to_txt(logName,"url:"+url);
							WSInterfacePortType wsInterfacePortType = wsInterface.getWSInterfaceHttpSoap11Endpoint();
							
							StringBuilder msgHeader = new StringBuilder();
							msgHeader.append("<root>");
							msgHeader.append("<serverName>SendPdfInfo</serverName>");
							msgHeader.append("<format>xml</format>");
							msgHeader.append("<callOperator></callOperator>");
							msgHeader.append("<certificate>xuMp+IMHvlYA3s34dkHQEWi8PL7CgdC9</certificate>");
//							msgHeader.append("<msgNo>"+GetGUID.getGUID()+"</msgNo>");
//							msgHeader.append("<sendTime>"+reqTime+"</sendTime>");
//							msgHeader.append("<sendCount>0</sendCount>");
							msgHeader.append("</root>");
							TranLogTxt.liswriteEror_to_txt(logName,"msgHeader:"+ msgHeader.toString());
							TranLogTxt.liswriteEror_to_txt(logName,"reqStr:"+ reqStr);
							String res = wsInterfacePortType.callInterface(msgHeader.toString(), reqStr);
							TranLogTxt.liswriteEror_to_txt(logName,"res:"+ res);
							String res_code = NanfenHL7.getResCode(res);
							if (res_code.contains("error")) {
								rh.setTypeCode("AE");
								rh.setText("报告上传不完整");
								TranLogTxt.liswriteEror_to_txt(logName,"LIS返回错误:"+ res_code);
							}else{
								String sql ="update report_pdf set is_finished='9' where exam_num ='"+ es.getExam_num() +"'";
								this.jdbcPersistenceManager.executeSql(sql);
								rh.setTypeCode("AA");
								rh.setText("");
							}
						}
					}catch(Throwable ex){
						  ex.printStackTrace();
						  TranLogTxt.liswriteEror_to_txt(logName, com.hjw.interfaces.util.StringUtil.formatException(ex));
						  rh.setTypeCode("AE");
						  rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
					}finally{
						if(pdfh!=null){
							pdfh.close(); 
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				TranLogTxt.liswriteEror_to_txt(logName,"res: :操作语句："+ com.hjw.interfaces.util.StringUtil.formatException(ex));
				rh.setTypeCode("AE");
				rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		return rh;
	}

	private UserInfoDTO getUser(long id, String logName) throws ServiceException {
		String sql ="select u.work_num,u.chi_name from user_usr u where  u.id ="+id+"and u.is_active = 'Y'";
		TranLogTxt.liswriteEror_to_txt(logName,"sql："+sql);
		List<UserInfoDTO> list = this.jdbcQueryManager.getList(sql, UserInfoDTO.class);
		return list.get(0);
	}
	
	private ReportPdfDTO reportPDF(String exam_num, String logname) throws ServiceException {
		String sql ="SELECT top 1 exam_num,arch_num,report_year,pdf_file,report_path,ftp_server,ftp_port,ftp_user,ftp_password,"
				+"is_finished,report_date,create_time FROM report_pdf where exam_num='"+exam_num+"' order by report_date desc";
		TranLogTxt.liswriteEror_to_txt(logname,"reportPDF："+sql);
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ReportPdfDTO.class);
		ReportPdfDTO eu = new ReportPdfDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ReportPdfDTO)map.getList().get(0);			
		}
		return eu;
	}
}
