package com.hjw.webService.client.yichang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.yichang.bean.ReportConclusionS;
import com.hjw.webService.client.yichang.bean.cdr.client.ret.DataSourceBGSC;
import com.hjw.webService.client.yichang.gencode.SERVDelegate;
import com.hjw.webService.client.yichang.gencode.SERVService;
import com.hjw.webService.client.yichang.gencode.SERVServiceLocator;
import com.hjw.webService.job.bean.CommonExamDetailDTO;
import com.hjw.webService.job.bean.ExamResultDetailDTO;
import com.hjw.webService.job.bean.ExamSummaryDTO;
import com.hjw.webService.job.bean.ExaminfoDiseaseDTO;
import com.hjw.webService.job.bean.ViewExamDetailDTO;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.UserInfoDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;



public class AutoSendExamSummaryYC {

	private static JdbcQueryManager jdbcQueryManager;
	private static JdbcPersistenceManager jdbcPersistenceManager;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		jdbcPersistenceManager = (JdbcPersistenceManager) wac.getBean("jdbcPersistenceManager");
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	/**
	 * 发送体检报告
	 * @param url
	 * @param days
	 * @param logname
	 * @return
	 */
	public ResultBody getMessage(String url, int days, String logname){
		ResultBody rb = new ResultBody();
		String datetime1 = DateTimeUtil.DateDiff2(days);
		
		Calendar deadline = Calendar.getInstance();
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
		//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
		deadline.set(2019, Calendar.SEPTEMBER, 5, 0, 0, 0);
		int viewDateStrlong = Integer.valueOf(df.format(deadline.getTime()));
		int dateTimeslong = Integer.valueOf(DateTimeUtil.getDate().substring(0, 8));
		
		if(!(viewDateStrlong >= dateTimeslong)){
			 System.err.println(viewDateStrlong);
			 System.err.println(dateTimeslong);
			 rb.getResultHeader().setTypeCode("AE");
			 rb.getResultHeader().setText("到期");
			 TranLogTxt.liswriteEror_to_txt(logname, "到期时间:" + viewDateStrlong + "\r\n");
			 return rb;
		 }
		
		List<ExaminfoChargingItemDTO> ecilist = getExamcharitem(datetime1,logname);
		String doctorlist = configService.getCenterconfigByKey("JIANKANG_DOCTOR_NAME").getConfig_value().trim();//健康师列表
		String[] splitList = doctorlist.split(",");
		
		
		for (int i = 0; i < ecilist.size(); i++) {
			System.err.println(i+"============");
			ExamSummaryDTO esd = getExamSummaryAndExamInfoID(ecilist.get(i).getExaminfo_id(),logname);
			
			
			if(esd.getApprove_status().equals("A")){//未审核的不允许上传
			
			TranLogTxt.liswriteEror_to_txt(logname,"体检ID=="+esd.getExam_info_id()+"审核状态=="+esd.getApprove_status());
			
			
			StringBuilder sb = new StringBuilder();
			ExamInfoUserDTO eu = configService.getExamInfoForExam_id(esd.getExam_info_id());
			UserInfoDTO user = getUser(esd.getExam_doctor_id(),logname);
			List<ExaminfoDiseaseDTO> ExaminfoDiseaseList =	getExamInfoDisease(esd.getExam_info_id(),logname);
			
			StringBuilder jianyi= new StringBuilder();
			int count=1;
			for (int j = 0; j < ExaminfoDiseaseList.size(); j++) {
				jianyi.append(count+".:"+ExaminfoDiseaseList.get(i).getSuggest());
				count++;
			}
			
			sb.append("<PRPA_IN000001UV03>                                                                                                                                 ");
			sb.append("	<!-- UUID,交互实例唯一码-->                                                                                                                        ");
			sb.append("	<id root=\"2.999.1.96\" extension=\""+eu.getExam_num()+"\" />                                                                      ");
			sb.append("	<creationTime value=\""+ DateTimeUtil.getDateTime().replace(" ", "T")+"+08:00"+"\" />                                                                                                          ");
			sb.append("	<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN000001UV03\" />                                                                   ");
			sb.append("	<receiver typeCode=\"RCV\">                                                                                                                        ");
			sb.append("		<!-- 可以填写电话信息或者URL-->                                                                                                                ");
			sb.append("		<telecom value=\"\" />                                                                                                                         ");
			sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                                                                         ");
			sb.append("			<id root=\"2.999.1.97\" extension=\"HIP-HSB\" />                                                                                           ");
			sb.append("			<telecom value=\"\" />                                                                                                             ");
			sb.append("			<softwareName code=\"HIP-HSB\" displayName=\"医疗服务总线\" codeSystem=\"2.999.2.3.2.84\" codeSystemName=\"医院信息平台系统域代码表\" />   ");
			sb.append("		</device>                                                                                                                                      ");
			sb.append("	</receiver>                                                                                                                                        ");
			sb.append("	<sender typeCode=\"SND\">                                                                                                                          ");
			sb.append("		<telecom value=\"\" />                                                                                                                         ");
			sb.append("		<device classCode=\"DEV\" determinerCode=\"INSTANCE\">                                                                                         ");
			sb.append("			<id root=\"2.999.1.98\" extension=\"LCFW-TJGL\" />                                                                                         ");
			sb.append("			<telecom value=\"设备编号\" />                                                                                                             ");
			sb.append("			<softwareName code=\"LCFW-TJGL\" displayName=\"体检管理系统\" codeSystem=\"2.999.2.3.2.84\" codeSystemName=\"医院信息平台系统域代码表\" /> ");
			sb.append("		</device>                                                                                                                                      ");
			sb.append("	</sender>                                                                                                                                          ");
			sb.append("	<controlActProcess classCode=\"CACT\" moodCode=\"APT\">                                                                                            ");
			sb.append("		<subject typeCode=\"SUBJ\">                                                                                                                    ");
			sb.append("			<request>                                                                                                                                  ");
			sb.append("			<UpReportCollectionData>                                                                                                                   ");
			sb.append("			<_report> ");
			sb.append("	 <HealthConsultant>");
			for (int b = 0; b < splitList.length; b++) {
				sb.append("<string>"+splitList[b]+"</string>");
			}
			sb.append("	</HealthConsultant>");
			sb.append(" <LibraryId>a74e58c3c6ae4fa2ba9e16bf96793f45</LibraryId>                            ");
			sb.append(" <GradeID>8d2e036a45594f989671be51253d902a</GradeID> ");
			sb.append(" <OrganID>201908001</OrganID>                        ");
			sb.append(" <CardNo>"+eu.getId_num()+"</CardNo>                 ");
			sb.append(" <ReportNo>"+eu.getExam_num()+"</ReportNo>                        ");
			sb.append(" <ReportDate>"+esd.getCreate_time().replace(" ", "T")+"+08:00"+"</ReportDate>                 ");
			sb.append(" <ReportState>1</ReportState>                        ");
			sb.append(" <UserName>"+eu.getUser_name()+"</UserName>           ");
			sb.append(" <UserNo>"+eu.getArch_num()+"</UserNo>               ");
			sb.append(" <Gender>2</Gender>                                  ");
			sb.append(" <Birthday>"+eu.getBirthday().replace(" ", "T")+"+08:00"+"</Birthday>           ");
			sb.append(" <Telephone>"+eu.getPhone()+"</Telephone>            ");
			sb.append(" <WorkUnit>"+eu.getCompany()+"</WorkUnit>            ");
			sb.append(" <LoginName>"+eu.getArch_num()+"</LoginName>         ");
			sb.append(" <LoginPwd>123456</LoginPwd>                         ");
			sb.append(" <Summarize>"+esd.getFinal_exam_result()+"</Summarize>     ");
			sb.append(" <Advice>"+jianyi.toString()+"</Advice>     ");
			sb.append(" <ZJDoctor>"+user.getChi_Name()+"</ZJDoctor>         ");
			sb.append(" <ZJDate>"+esd.getCreate_time().replace(" ", "T")+"+08:00"+"</ZJDate>            ");
			sb.append(" <ReportAbnormalS>									 ");
			
			
			for (ExaminfoDiseaseDTO examinfoDiseaseDTO : ExaminfoDiseaseList) {
				//体检异常对象
				sb.append(" <TempReportAbnormal> 								 ");
				sb.append(" <ReportNo>"+eu.getExam_num()+"</ReportNo>						 ");
				sb.append(" <AbnormalName>"+examinfoDiseaseDTO.getDisease_name()+"</AbnormalName>	");
				sb.append(" </TempReportAbnormal>								 ");
			}
			sb.append(" </ReportAbnormalS>    								 ");
			sb.append(" <ReportConclusionS>									 ");
			
			List<ReportConclusionS> reportConclusionSList = getReportConclusionS(esd.getExam_info_id(), logname);
			for (ReportConclusionS reportConclusionS : reportConclusionSList) {
				//体检小结对象  暂时未上传科室小结 ( xml实例里面没有) 如果需要上传小结 需要关联exam_dep_result表
				sb.append(" <TempReportConclusion>								");
				sb.append("	<ReportNo>"+eu.getExam_num()+"</ReportNo>						");
				sb.append("	<SectionName>"+reportConclusionS.getSectionName()+"</SectionName>		");
				sb.append("	<CheckUser>"+reportConclusionS.getCheckUser()+"</CheckUser>				");
				sb.append("	<CheckTime>"+reportConclusionS.getCheckTime().replace(" ", "T")+"+08:00"+"</CheckTime>			");
				sb.append(" </TempReportConclusion>								");
			}
			
			sb.append(" </ReportConclusionS>									");
			
			sb.append(" <ReportItemS>");
			
			List<ExamResultDetailDTO> examResultDetail = getExamResultDetail(esd.getExam_info_id(), logname);
			for (ExamResultDetailDTO examResultDetailDTO : examResultDetail) {
				//体检小项对象(检验科室)
				sb.append("<TempReportItem>									");
				sb.append("	<ReportNo>"+eu.getExam_num()+"</ReportNo>					");
				sb.append("	<SectionName>"+examResultDetailDTO.getCharging_item_name()+"</SectionName>			");
				sb.append("	<ItemName>"+examResultDetailDTO.getItem_name()+"</ItemName>						");
				sb.append("	<ItemValue>"+examResultDetailDTO.getExam_result()+"</ItemValue>						");
				sb.append("	<ItemTag>1</ItemTag>							");
				sb.append("	<Units>"+examResultDetailDTO.getItem_unit()+"</Units>								");
				sb.append("	<Reference>"+examResultDetailDTO.getRef_min()+"-"+examResultDetailDTO.getRef_max()+"</Reference>					");
				sb.append("</TempReportItem>								");
			}
			
			List<CommonExamDetailDTO> CommonExamDetailList =  getCommonExamDetail(esd.getExam_info_id(), logname);
			for (CommonExamDetailDTO commonExamDetailDTO : CommonExamDetailList) {
				//体检小项对象(普通科室)
				sb.append("<TempReportItem>									");
				sb.append("	<ReportNo>"+eu.getExam_num()+"</ReportNo>					");
				sb.append("	<SectionName>"+commonExamDetailDTO.getCharging_item_name()+"</SectionName>			");
				sb.append("	<ItemName>"+commonExamDetailDTO.getItem_name()+"</ItemName>						");
				sb.append("	<ItemValue>"+commonExamDetailDTO.getExam_result()+"</ItemValue>						");
				sb.append("	<ItemTag>1</ItemTag>							");
				sb.append("	<Units>"+commonExamDetailDTO.getItem_unit()+"</Units>								");
				sb.append("	<Reference>"+commonExamDetailDTO.getRef_Mmin()+"-"+commonExamDetailDTO.getRef_Mmax()+"</Reference>					");
				sb.append("</TempReportItem>								");
			}
			  List<ViewExamDetailDTO> ViewExamDetailDTOList = getViewExamDetail(esd.getExam_info_id(), logname);
			  for (ViewExamDetailDTO viewExamDetailDTO : ViewExamDetailDTOList) {
				  //体检小项对象(影像科室)
				  sb.append("<TempReportItem>									");
					sb.append("	<ReportNo>"+eu.getExam_num()+"</ReportNo>					");
					
					sb.append("	<SectionName>"+viewExamDetailDTO.getDep_name()+"</SectionName>			");
					sb.append("	<ItemName>"+viewExamDetailDTO.getCharging_item_name()+"</ItemName>						");
					sb.append("	<ItemValue>描述:"+viewExamDetailDTO.getExam_desc()+"结论:"+viewExamDetailDTO.getExam_result()+"</ItemValue>						");
					sb.append("	<ItemTag>1</ItemTag>							");
					sb.append("	<Units></Units>								");
					sb.append("	<Reference></Reference>					");
					sb.append("</TempReportItem>		");
				
			}
			
			  	sb.append(" </ReportItemS>");

				sb.append(" <IsRepeat>1</IsRepeat>								");
				sb.append(" <IsUpdate>1</IsUpdate>								");
				sb.append(" <IsUpdateReport>1</IsUpdateReport>					");	
				
				sb.append("</_report>											");
				sb.append("</UpReportCollectionData>							");
				sb.append("</request>											");
				sb.append("</subject>											");
				sb.append("</controlActProcess>									");
				sb.append("</PRPA_IN000001UV03>									");
				
				try {
					TranLogTxt.liswriteEror_to_txt(logname,"autoSendExamSummary入参:"+sb.toString());
					SERVService servService = new SERVServiceLocator(url);
					SERVDelegate servDelegate = servService.getSERVPort();
					String resRequestForm = servDelegate.HIPMessageServer("UpReportCollectionData", sb.toString());
					TranLogTxt.liswriteEror_to_txt(logname,"autoSendExamSummary返回:"+resRequestForm);
					
					if(resRequestForm != null && resRequestForm.length()>5){
						DataSourceBGSC resData = new DataSourceBGSC(resRequestForm, true);
						if("1".equals(resData.getUpReportCollectionDataResult())) {
							rb.getResultHeader().setTypeCode("AA");
							rb.getResultHeader().setText("上传报告成功");
							System.err.println(eu.getExam_num());
							//测试阶段 暂时不修改上传状态,实际启动需修改
							updateExamSummary(esd.getId(),logname);
						} else if("3".equals(resData.getUpReportCollectionDataResult())) {
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("报告上传失败");
						}else{
							rb.getResultHeader().setTypeCode("AE");
							rb.getResultHeader().setText("报告上传异常");
						}
					}else{
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("上传报告返回值过短");
					}
					
				} catch (Exception e) {
					//e.printStackTrace();
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("上传报告报错");
				}
				TranLogTxt.liswriteEror_to_txt(logname,"体检ID=="+esd.getExam_info_id()+"体检号=="+eu.getExam_num()+"成功或失败==:"+rb.getResultHeader().getTypeCode()+"描述==:"+rb.getResultHeader().getText());
		}else{
			TranLogTxt.liswriteEror_to_txt(logname,"体检ID=="+esd.getExam_info_id()+"审核状态=="+esd.getApprove_status());
			
		}
		}
		return rb;
		
		
	}

	/**
	 * 修改上传状态
	 * @param id
	 * @param logname
	 */
	private void updateExamSummary(long id, String logname) {
	
	String sql = " update exam_summary set read_status='1',read_time='" + DateTimeUtil.getDateTime()+ "' where id ='" + id + "' ";
	TranLogTxt.liswriteEror_to_txt(logname, "exam_summary表的id==="+id+"根据exam_summary的id修改上传状态:" + sql + "\r\n");
	
	this.jdbcPersistenceManager.executeSql(sql);
		
	}

/**
 * 查询结论表
 * @param exam_info_id
 * @param logname
 * @return
 */
	private ExamSummaryDTO getExamSummaryAndExamInfoID(long exam_info_id, String logname) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select top 1 id,final_exam_result,exam_info_id,exam_doctor_id,exam_doctor_id,check_time,acceptance_check,acceptance_date,create_time,read_status,approve_status from exam_summary where exam_info_id='"+exam_info_id+"' and read_status='0'  order by create_time  ");
		List<ExamSummaryDTO> examSummaryList = AutoSendExamSummaryYC.jdbcQueryManager.getList(sb.toString(),ExamSummaryDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束通过体检号查询报告:" + sb.toString() + "\r\n");
		
		return examSummaryList.get(0);
		
	}

	/**
	 * 查询影像科室明细
	 * @param exam_info_id
	 * @param logname
	 * @return
	 */
	private List<ViewExamDetailDTO> getViewExamDetail(long exam_info_id, String logname) {

				String sql = " select  pd.dep_name,pd.chargingItem_name as charging_item_name,ved.exam_desc,ved.exam_result from pacs_detail pd ,pacs_summary  ps,"
						+ " (select v.pacs_id,v.exam_desc,v.exam_result from view_exam_detail v where exam_info_id='"+exam_info_id+"') as ved  "
						+ " where ved.pacs_id=ps.id and pd.summary_id=ps.id ";
				List<ViewExamDetailDTO> list = this.jdbcQueryManager.getList(sql, ViewExamDetailDTO.class);
				TranLogTxt.liswriteEror_to_txt(logname, "结束查询影像结果明细表： " +sql);
				/*if(list == null || list.isEmpty()) {
					return (List<ViewExamDetailDTO>) new ViewExamDetailDTO();
				}*/
				return list;
		
	}

	/**
	 * 查询普通科室明细
	 * @param exam_info_id
	 * @param logname
	 * @return
	 */
	private List<CommonExamDetailDTO> getCommonExamDetail(long exam_info_id, String logname) {
		
		String sql = " select  ci.item_name as charging_item_name,ced.charging_item_id,ei.item_name,ced.exam_item_id,ced.exam_result,ei.item_unit,ei.ref_Mmax,ei.ref_Mmin,ei.ref_Fmax,ei.ref_Fmin "
				+ " from common_exam_detail ced, charging_item ci ,examination_item ei "
				+ " where ei.id=ced.exam_item_id and ci.id=ced.charging_item_id and  exam_info_id='"+exam_info_id+"'  ";
		List<CommonExamDetailDTO> list = this.jdbcQueryManager.getList(sql, CommonExamDetailDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束查询普通科室明细表： " +sql);
		/*if(list == null || list.isEmpty()) {
			return (List<CommonExamDetailDTO>) new CommonExamDetailDTO();
		}*/
		return list;
	}

	/**
	 * 查询检验明细表
	 * @param exam_info_id
	 * @param logname
	 * @return
	 */
	private List<ExamResultDetailDTO> getExamResultDetail(long exam_info_id, String logname) {
		String sql = " select ci.item_name as charging_item_name,erd.charging_item_id,ei.item_name,erd.exam_item_id,erd.ref_indicator,erd.exam_result,erd.item_unit,erd.ref_max,erd.ref_min  "
				+ " from exam_result_detail erd,charging_item ci ,examination_item ei  "
				+ " where ei.id=erd.exam_item_id and ci.id=erd.charging_item_id and  exam_info_id='"+exam_info_id+"'  ";
		List<ExamResultDetailDTO> list = this.jdbcQueryManager.getList(sql, ExamResultDetailDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束查询exam_result_detail检验结果明细： " +sql);
		/*if(list == null || list.isEmpty()) {
			return (List<ExamResultDetailDTO>) new ExamResultDetailDTO();
		}*/
		return list;
		
	}
	/**
	 * 查询体检人与阳性发现表
	 * @param exam_info_id
	 * @param logname
	 * @return
	 */
	private List<ExaminfoDiseaseDTO> getExamInfoDisease(long exam_info_id, String logname) {
		
				String sql = " select  disease_id,disease_name,suggest from examinfo_disease where exam_info_id='"+exam_info_id+"' order by create_time  ";
				List<ExaminfoDiseaseDTO> list = this.jdbcQueryManager.getList(sql, ExaminfoDiseaseDTO.class);
				TranLogTxt.liswriteEror_to_txt(logname, "结束查询体检人人员与阳性关系表： " +sql);
				/*if(list == null || list.isEmpty()) {
					return (List<ExaminfoDiseaseDTO>) new ExaminfoDiseaseDTO();
				}*/
				return list;
	}


	private List<ReportConclusionS> getReportConclusionS(long exam_info_id, String logname) {
		ChargingItemDTO chargingItem = getChargingItem(logname);
		String sql = " select dd.dep_name as SectionName,eci.exam_doctor_name as CheckUser,eci.exam_date as CheckTime  "
				+ " from department_dep dd ,charging_item ci,(select  charge_item_id,exam_doctor_name,exam_date from examinfo_charging_item where examinfo_id='"+exam_info_id+"') as eci  "
				+ " where eci.charge_item_id=ci.id and ci.dep_id=dd.id and ci.id !='"+chargingItem.getId()+"' ";//检后健康管理项目 没有检查医生
		List<ReportConclusionS> list = this.jdbcQueryManager.getList(sql, ReportConclusionS.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束查询检查科室及检查医生： " +sql);
		/*if(list == null || list.isEmpty()) {
			return (List<ReportConclusionS>) new ReportConclusionS();
		}*/
		return list;
		

	}


	

	/**
	 * 查询包含检后管理项目的人员
	 * @param datetime
	 * @param logname
	 * @return
	 */
	private List<ExaminfoChargingItemDTO> getExamcharitem(String datetime, String logname) {
		StringBuffer sb = new StringBuffer();
		ChargingItemDTO chargingItem = getChargingItem(logname);
		sb.append("  select eci.examinfo_id from examinfo_charging_item eci  where eci.isActive='Y'   and eci.charge_item_id='"+chargingItem.getId()+"'  and eci.create_time>'"+datetime+"' ");

		List<ExaminfoChargingItemDTO> eciList = this.jdbcQueryManager.getList(sb.toString(),ExaminfoChargingItemDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束包含C0000990项目的人:" + sb.toString() + "\r\n");
		
		return eciList;
	}
	
	/**
	 * 查询人员名称
	 * @param user_id
	 * @param logname
	 * @return
	 */
	public UserInfoDTO getUser(long user_id,String logname) {
		String sql = "select u.id,u.chi_name,u.log_name,u.pwd_encrypted from user_usr u where u.id = '"+user_id+"' and u.is_active = 'Y'";
		List<UserInfoDTO> list = this.jdbcQueryManager.getList(sql, UserInfoDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束查询user_use表： " +sql);
		/*if(list == null || list.isEmpty()) {
			return new UserInfoDTO();
		}*/
		return list.get(0);
	}
	/**
	 * 查询检后管理 项目信息
	 * @param logname
	 * @return
	 */
	public ChargingItemDTO getChargingItem(String  logname){
		
		String sql = " select  * from charging_item where item_code='C0000990' and isActive='Y' ";
		List<ChargingItemDTO> list = this.jdbcQueryManager.getList(sql, ChargingItemDTO.class);
		TranLogTxt.liswriteEror_to_txt(logname, "结束查询收费项目表item_code为C0000990的项目： " +sql);
		/*if(list == null || list.isEmpty()) {
			return new UserInfoDTO();
		}*/
		return list.get(0);
	}
	
}
