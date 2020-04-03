package com.hjw.webService.job.fangzheng;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.Base64;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.fangzheng.MQ.mqSendSample;
import com.hjw.webService.client.tj180.Bean.UpdateDiagnosisBean;
import com.hjw.webService.client.tj180.Bean.UpdateDiagnosisBody;
import com.hjw.webService.job.bean.CommonExamDetailDTO;
import com.hjw.webService.job.bean.ExamResultDetailDTO;
import com.hjw.webService.job.bean.ExamSummaryDTO;
import com.hjw.webService.job.bean.ExaminfoDiseaseDTO;
import com.hjw.webService.job.bean.ViewExamDetailDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.UserDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

public class PutReportControlTJFZ {
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
		String datetime = DateTimeUtil.DateDiff2(days);
		ResultHeader rh = new ResultHeader();

		String sb1 = "SELECT a.id,a.exam_doctor_id,a.exam_info_id,a.final_exam_result,a.result_Y,a.result_D,a.suggest,"
				+ "a.center_num,a.check_doc,a.check_time,a.approve_status,a.creater,a.create_time,a.updater,a.update_time,"
				+ "a.acceptance_check,a.acceptance_doctor,a.acceptance_date,a.read_status,a.read_time,"
				+ "x.chi_name as examdocname,y.chi_name as checkdocname,b.exam_num" + "  FROM exam_summary a "
				+ "  left join user_usr x on x.id=a.exam_doctor_id"
				+ "  left join user_usr y on y.id=a.check_doc ,exam_info b "
				+ "where a.approve_status='B' and read_status='0' and a.exam_info_id=b.id "
				+ " and (CONVERT(varchar(50),a.create_time,23)>= '" + datetime
				+ "' or CONVERT(varchar(50),a.update_time,23)>= '" + datetime + "')";
		TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
		List<ExamSummaryDTO> userList = this.jdbcQueryManager.getList(sb1, ExamSummaryDTO.class);

		for (ExamSummaryDTO es : userList) {
			try {
				ExamInfoUserDTO ei = new ExamInfoUserDTO();
				ei = this.getExamInfoForNum(es.getExam_info_id(), logName);

				StringBuffer sb = new StringBuffer("");
				sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\">\n");
		sb.append("	<!--地域代码  \"CN\" 代表中国-->\n");
		sb.append("    <realmCode code=\"CN\"/>\n");
		sb.append("	<typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_MT000040\"/>\n");
		sb.append("	<templateId root=\"2.16.156.10011.2.1.1.16\"/>\n");
		sb.append("	<!-- 表单编号 -->\n");
		sb.append("	<id root=\"2.16.156.10011.1.1.2\" extension=\""+es.getId()+"\"/>\n");
		sb.append("    <!--文档类别代码-->\n");
		sb.append("    <code code=\"HSDC00.01\" codeSystem=\"2.16.156.10011.2.4\" codeSystemName=\"卫生信息共享文档规范编码体系\"/>\n");
		sb.append("    <title>成人健康体检</title>\n");
		sb.append("    <!--文档生效时间 [1..1]-->\n");
		sb.append("    <effectiveTime value=\""+es.getCreate_time().substring(0,10).replaceAll("-", "")+"\"/>\n");
		sb.append("    <!--文档密级  默认值 [1..1] -->\n");
		sb.append("    <confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" displayName=\"正常访问保密级别\"/>\n");
		sb.append("    <!--文档语言 [1..1]  默认值 code=\"zh-CN\" 含义：中文字符编码-->\n");
		sb.append("    <languageCode code=\"zh-CN\"/>\n");
		sb.append("	<!--服务ID-->\n");
		sb.append("	<setId extension=\"BS347\"/>\n");
		sb.append("	<!-- 文档的操作版本:0表示新增, 1表示修改，-1表示删除 -->\n");
		sb.append("	<versionNumber value=\"0\"/>\n");
		sb.append("	<recordTarget typeCode=\"RCT\" contextControlCode=\"OP\">\n");
		sb.append("		<patientRole classCode=\"PAT\">\n");
		sb.append("			<!-- 域ID -->\n");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.2\" extension=\"01\" />\n"); 
		sb.append("			<!-- 患者ID -->\n");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.3\" extension=\""+ei.getPatient_id()+"\" />\n"); 
		sb.append("			<!-- 就诊号 -->\n");
		sb.append("            <id root=\"1.2.156.112685.1.2.1.12\" extension=\""+ei.getClinic_no()+"\" />\n");
		sb.append("			<!--健康档案标识号 （长度17） -->\n");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.32\" extension=\""+es.getId()+"\"/>\n");
		sb.append("			<addr use=\"H\">\n");
		sb.append("				<houseNumber>"+ei.getAddress()+"</houseNumber>\n");
		sb.append("				<streetName></streetName>\n");
		sb.append("				<township></township>\n");
		sb.append("				<county></county>\n");
		sb.append("				<city></city>\n");
		sb.append("				<state></state>\n");
		sb.append("				<postalCode></postalCode>\n");
		sb.append("			</addr>\n");
		sb.append("			<telecom value=\""+ei.getPhone()+"\"/>\n");
		sb.append("			<patient classCode=\"PSN\" determinerCode=\"INSTANCE\">\n");
		sb.append("				<name>"+ei.getUser_name()+"</name>\n");
		String sexcode="1";
		if("男".equals(ei.getSex())){
			sexcode="1";
		}else if("女".equals(ei.getSex())){
			sexcode="2";
		}
		sb.append("				<administrativeGenderCode code=\""+sexcode+"\" displayName=\""+ei.getSex()+"性\" codeSystem=\"1.2.156.112685.1.1.3\" codeSystemName=\"生理性别代码表（GB/T 2261.1）\"/>\n");
		sb.append("			</patient>\n");
		sb.append("		</patientRole>\n");
		sb.append("	</recordTarget>\n");
		sb.append("	<author typeCode=\"AUT\" contextControlCode=\"OP\">\n");
		sb.append("		<time xsi:type=\"TS\" value=\""+es.getCreate_time().substring(0,10).replaceAll("-", "")+"\"/>\n");
		sb.append("		<assignedAuthor classCode=\"ASSIGNED\">\n");
		UserDTO uname=getUserWorkNum(es.getExam_doctor_id(),logName);
		sb.append("			<id root=\"1.2.156.112685.1.1.2\" extension=\""+uname.getWork_num()+"\"/>\n");
		sb.append("			<assignedPerson>\n");
		sb.append("				<name>"+uname.getUsername()+"</name>\n");
		sb.append("			</assignedPerson>\n");
		sb.append("			<representedOrganization>\n");
		sb.append("				<id root=\"1.2.156.112685\"  extension=\"\"/>\n");
		sb.append("				<name></name>\n");
		sb.append("				<addr></addr>\n");			
		sb.append("			</representedOrganization>\n");
		sb.append("		</assignedAuthor>\n");
		sb.append("	</author>\n");
		sb.append("	<custodian typeCode=\"CST\">\n");
		sb.append("		<assignedCustodian classCode=\"ASSIGNED\">\n");
		sb.append("			<representedCustodianOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
		sb.append("				<id root=\"1.2.156.112685\" extension=\"46014326-4\"/>\n");
		sb.append("				<name>包头市中心医院</name>\n");
		sb.append("				<telecom value=\"\"/>\n");
		sb.append("				<addr></addr>\n");
		sb.append("			</representedCustodianOrganization>\n");
		sb.append("		</assignedCustodian>\n");
		sb.append("	</custodian>\n");
		sb.append("	<!-- 电子签章信息 -->\n");
		sb.append("	<legalAuthenticator>\n");
		sb.append("		<time />\n");
		sb.append("		<signatureCode code=\"S\" />\n");
		sb.append("		<assignedEntity>\n");
		sb.append("			<id extension=\"123\" />\n");
		sb.append("		</assignedEntity>\n");
		sb.append("	</legalAuthenticator>\n");
		sb.append("	<!-- 责任医生 -->\n");
		sb.append("	<authenticator>\n");
		sb.append("	    <!-- 体检时间 -->\n");
		sb.append("		<time value=\""+es.getCreate_time().substring(0,10).replaceAll("-", "")+"\"/>\n");
		sb.append("		<signatureCode/>\n");
		sb.append("		<assignedEntity>\n");
		uname=getUserWorkNum(es.getCheck_doc(),logName);
		sb.append("			<id root=\"1.2.156.112672.1.1.2\" extension=\""+uname.getWork_num()+"\"/>\n");
		sb.append("			<code displayName=\"责任医师\"/>\n");
		sb.append("			<assignedPerson>\n");
		sb.append("				<name>"+uname.getUsername()+"</name>\n");
		sb.append("			</assignedPerson>\n");
		sb.append("		</assignedEntity>\n");
		sb.append("	</authenticator>\n");
		sb.append("	<relatedDocument typeCode=\"RPLC\">\n");
		sb.append("		<parentDocument classCode=\"DOCCLIN\" moodCode=\"EVN\">\n");
		sb.append("			<id root=\"2.16.156.10011.1.1.2\" extension=\""+es.getId()+"\"/>\n");
		sb.append("			<setId/>\n");
		sb.append("			<versionNumber value=\"1\"/>\n");
		sb.append("		</parentDocument>\n");
		sb.append("	</relatedDocument>\n");
		sb.append("	<componentOf>\n");
		sb.append("		<encompassingEncounter>\n");
		ExamInfoUserDTO sjh=getHISDJH(ei.getExam_num());
		String dates = DateTimeUtil.getDateTimes();		
		String jzcs=dates.substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
		sb.append("			<!-- 就诊次数 -->\n");
		sb.append("			<id root=\"1.2.156.112685.1.2.1.7\" extension=\""+jzcs+"\"/>\n");
		sb.append("		    <!-- 就诊流水号 -->\n");
		String lsh=ei.getPatient_id()+sjh.getOthers().substring(1,sjh.getOthers().length());
		sb.append("			<id root=\"1.2.156.112685.1.2.1.6\" extension=\""+lsh+"\"/>\n");			
		sb.append("            <!-- 就诊类别编码/就诊类别名称 -->\n");
		sb.append("			<code code=\"0401\" codeSystem=\"1.2.156.112685.1.1.80\" displayName=\"干保体检\" />\n");	
		sb.append("			<effectiveTime/>\n");
		sb.append("		</encompassingEncounter>\n");
		sb.append("	</componentOf>\n");
		sb.append("	<component>\n");
		sb.append("		<structuredBody>\n");

        sb.append("			<!--");
        sb.append("******************************************************");
        sb.append("整个文档PDF文件");
        sb.append("******************************************************");
        sb.append("-->\n");	
        sb.append("			<component>\n");
        sb.append("				<section>\n");
        sb.append("				    <code code=\"20001\" codeSystem=\"2.16.840.1.113883.6.96\"  />\n");
        sb.append("				    <entry>\n");
        sb.append("						<observationMedia classCode=\"OBS\" moodCode=\"EVN\">\n");
        String URL=configService.getCenterconfigByKey("REPORT_SELF_URL").getConfig_value().trim()+"/showreportpdf.action?examnum="+ei.getExam_num();
        URL=Base64.base64Encode(URL.getBytes("utf-8"));
        URL=URL.replaceAll("[\\s*\t\n\r]", "");  
        sb.append("							<value xsi:type=\"ED\" mediaType=\"url\">"+URL+"</value>\n");
        sb.append("						</observationMedia>\n");
        sb.append("				    </entry>\n");	
        sb.append("				</section>\n");
        sb.append("			</component>\n");
        
        
        sb.append("			<!--症状章节-->\n");
        //总检
        UpdateDiagnosisBody udb = new UpdateDiagnosisBody();
        udb.setReserveId(es.getExam_num());
        List<UpdateDiagnosisBean> diagnosisInfo = new ArrayList<UpdateDiagnosisBean>();
        String zjjlStr = "select a.id,a.exam_info_id,c.disease_num,c.disease_name,a.disease_name as disease_key,CONVERT(varchar(50),a.create_time,20) as create_time,"
        		+ "a.update_time,a.final_doc_num,a.suggest,b.work_num,c.disease_num from examinfo_disease a "
        		+ " left join user_usr b on b.id=a.creater "
        		+ " left join disease_knowloedge_lib c on c.id=a.disease_id where exam_info_id='"
        		+ es.getExam_info_id() + "' and a.isActive='Y' order by a.disease_index";
        TranLogTxt.liswriteEror_to_txt(logName, "res: :总检语句： " + zjjlStr);
        List<ExaminfoDiseaseDTO> zjjlList = this.jdbcQueryManager.getList(zjjlStr, ExaminfoDiseaseDTO.class);
        if ((zjjlList != null) && (zjjlList.size() > 0)) {
        	sb.append("			<component>\n");
            sb.append("				<section>\n");
            sb.append("					<code code=\"11450-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"PROBLEM LIST\"/>\n");
            sb.append("					<text/>\n");
            sb.append("					<entry>\n");
            sb.append("						<organizer classCode=\"CLUSTER\" moodCode=\"EVN\">\n");
            sb.append("							<statusCode/>\n");
        
        	for (ExaminfoDiseaseDTO dd : zjjlList) {
        		
        	
                sb.append("							<component>\n");
                sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
                sb.append("									<code code=\""+dd.getId()+"\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"症状名称\"/>\n");
                sb.append("									<value xsi:type=\"ST\">"+dd.getDisease_name()+"</value>\n");
                sb.append("								</observation>\n");
                sb.append("							</component>\n");
                if((dd.getDisease_num()!=null)&&(dd.getDisease_num().trim().length()>0)){    
                      sb.append("							<component>\n");
                      sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
                      sb.append("									<code code=\"DE04.01.116.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"症状代码\"/>\n");
                      sb.append("									<value  xsi:type=\"CD\" code=\""+dd.getDisease_num()+"\" displayName=\""+dd.getDisease_name()+"\" codeSystem=\"1.2.156.112685.1.1.30\" codeSystemName=\"ICD-10\"></value>\n");
                      sb.append("								</observation>\n");
                      sb.append("							</component>\n");
                 }
                    	
            }
        	 sb.append("						</organizer>\n");
             sb.append("					</entry>\n");
             sb.append("				</section>\n");
             sb.append("			</component>\n");                
        }
             sb.append("			<!--生命体征章节-->\n");
             StringBuffer sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj01","					<code code=\"8716-3\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"VITAL SIGNS\"/>\n",logName);
        	 sb.append(sbitem.toString());
        	 
			 sb.append("			<!--口腔、咽喉和牙齿章节-->\n");
			 sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj02","					<code code=\"10201-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"EMOUTH and THROAT and TEETH\"/>\n",logName);
             sb.append(sbitem.toString());
             
             sb.append("			<!--眼章节-->\n");
             sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj03","					<code code=\"10197-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Physical findings of Eye\"/>\n",logName);
             sb.append(sbitem.toString());
             
             sb.append("            <!--耳章节-->\n");
             sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj04","					<code code=\"10195-6\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"EAR\"/>\n",logName);
             sb.append(sbitem.toString());
             
             sb.append("			<!--腹部章节-->\n");
             sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj05","					<code code=\"10191-5\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"ABDOMEN\"/>\n",logName);
             sb.append(sbitem.toString());
             
             sb.append("			<!--功能检查章节-->\n");
             sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj06","					<code code=\"46006-3\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Physical functioning and structural problems\"/>\n",logName);
             sb.append(sbitem.toString());
             
             sb.append("			<!--心脏章节-->\n");
             sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj07","					<code code=\"10200-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"HEART\"/>\n",logName);
             sb.append(sbitem.toString());
             
             sb.append("			<!--血管章节-->\n");
             sbitem = new StringBuffer("");
             sbitem=getKsjc(es.getExam_info_id(),"zj08","					<code code=\"10208-7\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"VESSELS\"/>\n",logName);
             sb.append(sbitem.toString());
             
     		sb.append("			<!--呼吸系统章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj09","					<code code=\"11412-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"RESPIRATORY SYSTEM\"/>\n",logName);
            sb.append(sbitem.toString());
            
     		sb.append("			<!--皮肤章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj10","					<code code=\"29302-7\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"INTEGUMENTARY SYSTEM\"/>\n",logName);
            sb.append(sbitem.toString());
            
     		sb.append("			<!--淋巴系统章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj11","					<code code=\"11447-0\" displayName=\"HEMATOLOGIC+LYMPHATIC+IMMUNOLOGIC SYSTEM\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>\n",logName);
            sb.append(sbitem.toString());
            
     		sb.append("			<!--四肢章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj12","					<code code=\"10196-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"EXTREMITIES\"/>\n",logName);
            sb.append(sbitem.toString());
            
     		sb.append("			<!--直肠章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj13","					<code code=\"10205-3\" displayName=\"RECTUM\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>\n",logName);
            sb.append(sbitem.toString());
            
     		sb.append("			<!--乳腺章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj14","					<code code=\"10193-1\" displayName=\" Breasts\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>\n",logName);
            sb.append(sbitem.toString());
            
     		sb.append("			<!--生殖器章节-->\n");
     		sbitem = new StringBuffer("");
            sbitem=getKsjc(es.getExam_info_id(),"zj15","					<code code=\"11400-9\" codeSystem=\"2.16.840.1.113883.6.1\" displayName=\"GENITALIA\" codeSystemName=\"LOINC\"/>\n",logName);
            sb.append(sbitem.toString());
     		
     		sb.append("           <!--实验室检查章节-->\n");
     		StringBuffer jykstr = new StringBuffer();
			jykstr.append("select n.id,n.exam_info_id,n.exam_item_id,n.exam_category,n.exam_item_category,");
			jykstr.append("n.exam_doctor,n.exam_result,n.ref_value,");
			jykstr.append(
					"n.ref_indicator,n.center_num,n.approver,n.approve_date,n.creater,CONVERT(varchar(50),n.exam_date,20) as exam_date,");
			jykstr.append(
					"n.updater,n.update_time,n.item_unit,n.ref_value,a.dep_id,dep.dep_name,x.item_num,x.item_name");
			jykstr.append(
					",x.seq_code,x.item_category,a.item_name as charging_item_name,b.charging_item_id,a.item_code as charging_item_code,dep.dep_num");
			jykstr.append(" from exam_result_detail n,");
			jykstr.append(
					"charging_item a,examinfo_charging_item g,charging_item_exam_item b,examination_item x,department_dep dep");
			jykstr.append(
					" where b.charging_item_id=a.id and a.isActive='Y' and g.isActive='Y' and x.is_Active='Y' ");
			jykstr.append(
					" and x.is_Active='Y' and dep.isActive='Y' and a.id=g.charge_item_id and x.id=b.exam_item_id ");
			jykstr.append(
					" and dep.id=a.dep_id and g.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
			jykstr.append(" and n.exam_info_id='" + es.getExam_info_id() + "' and x.is_print='Y'");
			TranLogTxt.liswriteEror_to_txt(logName, "res: :lis检查： " + jykstr.toString());
			List<ExamResultDetailDTO> jyksList = this.jdbcQueryManager.getList(jykstr.toString(),
					ExamResultDetailDTO.class);
            if(jyksList!=null||jyksList.size()>0){
            	Map<String,Object> hisresltMap =new HashMap<String,Object>();
			for (ExamResultDetailDTO dd : jyksList){				
				hisresltMap.put(dd.getCharging_item_id()+"&&"+dd.getId(), dd);				
			}
			Map<String, List<Object>> map = new HashMap<String, List<Object>>();  
			map=this.mapCombine(hisresltMap);
			
				sb.append("            <component>\n");
	     		sb.append("                <section>\n");
	     		sb.append("                    <code code=\"30954-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"STUDIES SUMMARY\"/>\n");
	     		sb.append("                    <text/>\n");
			for (String cii : map.keySet()) {
				 List<Object> plrList = new ArrayList<Object>();	
				 plrList = map.get(cii);
				 sb.append("                    <entry typeCode=\"COMP\">\n");
		     		sb.append("                        <!-- 血常规 -->\n");
		     		sb.append("                        <organizer classCode=\"BATTERY\" moodCode=\"EVN\">\n");
		     		sb.append("                            <statusCode/>\n");
		     	for(Object ob:plrList){
		     		ExamResultDetailDTO dd =(ExamResultDetailDTO)ob;
		     		sb.append("                            <component typeCode=\"COMP\">\n");
		     		sb.append("                                <!-- "+dd.getItem_name()+" -->\n");
		     		if("数字型".equals(dd.getItem_category().trim())){
		     			sb.append("                                <observation classCode=\"OBS\" moodCode=\"EVN\">\n");
			     		sb.append("                                    <code code=\""+dd.getItem_num()+"\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>\n");
			     		sb.append("                                    <value xsi:type=\"PQ\" value=\""+dd.getExam_result().replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"\" unit=\""+dd.getItem_unit()+"\"/>\n");
			     		sb.append("                                </observation>\n");
		     		}else{
		     			sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
		         		sb.append("									<code code=\"DE04.50.050.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" />\n");
		         		sb.append("									<value xsi:type=\"CD\" code=\""+dd.getItem_num()+"\"  displayName=\""+dd.getExam_result().replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"\" codeSystem=\"2.16.156.10011.2.3.1.95\" codeSystemName=\"尿实验室定性检测结果代码表\"/>\n");
		         		sb.append("								</observation>\n");
		     		}		     		
		     		sb.append("                            </component>\n");
		     	}
	     		sb.append("                        </organizer>\n");
	     		sb.append("                    </entry>\n");
			}
			sb.append("                </section>\n");
     		sb.append("            </component>\n");
            }            
            
            sb.append("			<!-- 辅助检查章节 -->\n");
         // 影像科室检查结果明细表
            StringBuffer jckstr = new StringBuffer();
			jckstr.append(
					"select n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.report_picture_path,n.exam_desc,"
							+ "n.exam_result,n.exam_date,n.center_num,n.approver,n.approve_date,n.creater,n.create_time,"
							+ "n.updater,n.update_time,n.pacs_id,m.dep_id,m.dep_name,m.charging_item_name,m.charging_item_id,"
							+ "x.item_num,x.item_name,m.charging_item_code"
							+ " from view_exam_detail n left join ("
							+ "select dep_id,b.item_name as charging_item_name,b.id as charging_item_id,b.item_code as charging_item_code,a.id,"
							+ "dep.dep_name from pacs_summary a,pacs_detail g,charging_item b,department_dep dep"
							+ "   where a.id=g.summary_id and g.chargingItem_num=b.item_code and dep.id=b.dep_id) m "
							+ "on m.id=n.pacs_id ,examination_item x where n.exam_info_id='" + es.getExam_info_id() + "' and x.id=n.exam_item_id");
			TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " + jckstr.toString());
			List<ViewExamDetailDTO> jcksList = this.jdbcQueryManager.getList(jckstr.toString(), ViewExamDetailDTO.class);
			//TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.1);
			if(jcksList!=null&&jcksList.size()>0){
				sb.append("            <component>\n");
	     		sb.append("                <section>\n");
	     		sb.append("                    <code displayName=\"辅助检查\"/>\n");
	     		//TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.2);
			for (ViewExamDetailDTO dd : jcksList) {
				//TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.3);
				sb.append("                    <entry>\n");
	     		sb.append("                        <!-- "+dd.getItem_name()+" -->\n");
	     		sb.append("                        <observation classCode=\"OBS\" moodCode=\"EVN\">\n");
	     		//sb.append("                            <code code=\"DE04.30.046.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"胸部X线检查异常标志\"/>\n");
	     		//sb.append("                            <value xsi:type=\"BL\" value=\"true\"/>\n");
	     		sb.append("                            <entryRelationship typeCode=\"COMP\">\n");
	     		sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
	     		sb.append("									<code code=\""+dd.getItem_name()+"\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\""+dd.getExam_result().replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"\"></code>\n");
	     		sb.append("									<value xsi:type=\"ST\">"+dd.getExam_desc()+"</value>\n");
	     		sb.append("								</observation>\n");
	     		sb.append("							</entryRelationship>\n");
	     		sb.append("                        </observation>\n");
	     		sb.append("                    </entry>\n");	
	     		//TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.4);
			}
			sb.append("                </section>\n");
     		sb.append("            </component>\n");
     		//TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.5);
			}
			//TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.6);
     		sb.append("		</structuredBody>\n");
     		sb.append("	</component>\n");
     		sb.append("</ClinicalDocument>\n");
     		
     		TranLogTxt.liswriteEror_to_txt(logName, "req:" + sb.toString());
     		TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +1.7);
     		String ip = url.split("&")[0];
			int port = Integer.valueOf(url.split("&")[1]);
			TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句：准备发送申请 " +1.8);
			mqSendSample mqSendSample = new mqSendSample();
			mqSendSample.initEnvironment(ip, port);
			TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： ----"+ip+"---"+port+"--");			
			TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句：开始发送申请 " +1.9);
			String messages = mqSendSample.msgSend(sb.toString(), "BS347","0401","0");
			TranLogTxt.liswriteEror_to_txt(logName, "res:" + messages);
			TranLogTxt.liswriteEror_to_txt(logName, "res: :影像科室检查语句： " +2.0);
     		String sql = "update exam_summary set read_status='1',read_time='" + DateTimeUtil.getDateTime()
						+ "' where id ='" + es.getId() + "' ";
     		TranLogTxt.liswriteEror_to_txt(logName, "res: :语句： " +sql);
			this.jdbcPersistenceManager.executeSql(sql);
        } catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		}
		rh.setTypeCode("AA");
		rh.setText("");
		return rh;
		}
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(long id,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type,c.address,a.user_name,c.age,a.birthday,a.sex,"
				+ "c.register_date,c.join_date,c.exam_times,a.arch_num,c.patient_id,c.clinic_no,c.visit_no,c.mc_no ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");		
		sb.append(" and c.id = '" + id + "' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb.toString());
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);			
		}
		return eu;
	} 

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public UserDTO getUserWorkNum(long userid,String logname){
		Connection tjtmpconnect = null;
		UserDTO nationname=new UserDTO();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select work_num,chi_name from user_usr where id='"+userid+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				nationname.setWork_num(rs1.getString("work_num"));
				nationname.setUsername(rs1.getString("chi_name"));
			}
            rs1.close();
		} catch (SQLException ex) {
			
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return nationname;
	} 	
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getHISDJH(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT zl_djh as others,zl_tjh as visit_no,zl_mzh as clinic_no FROM zl_req_patInfo where exam_num = '" + exam_num + "' ");	
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ExamInfoUserDTO)map.getList().get(0);	
		}
		return eu;
	} 
	
	public long getExamLib(long exam_item_id,String exam_result,String logname){
		Connection tjtmpconnect = null;
		long ids=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.id from item_result_lib a where a.exam_item_id='"+exam_item_id+"' and a.exam_result=ltrim(rtrim('"+exam_result+"'))";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				ids=rs1.getLong("id");
			}
            rs1.close();
		} catch (SQLException ex) {
			
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return ids;
	}
	
	private StringBuffer getKsjc(long exam_info_id,String typsse,String title,String logName){
		StringBuffer sb = new StringBuffer("");
    	// 普通科室小结-生命体征
		StringBuffer ksxjstr = new StringBuffer();
		ksxjstr.append(" select n.id,n.exam_info_id,n.exam_item_id,n.exam_doctor,n.center_num,n.health_level,");
		ksxjstr.append(
				"n.exam_result,n.creater,CONVERT(varchar(50),n.exam_date,20) as exam_date,n.updater,n.update_time,a.dep_id,"
				+ "b.charging_item_id,a.item_name as charging_item_name,dep.dep_name,x.item_num,x.id as examination_item_id, ");
		ksxjstr.append(
				"x.item_name,x.seq_code,x.ref_Mmax,x.ref_Mmin,x.ref_Fmin,x.ref_Fmax,a.item_code as charging_item_code");
		ksxjstr.append(",x.item_unit,x.item_category,dep.dep_num");
		ksxjstr.append(" from common_exam_detail n");
		ksxjstr.append(
				",charging_item a,charging_item_exam_item b,examination_item x,department_dep dep,examinfo_charging_item y");
		ksxjstr.append(
				" where b.charging_item_id=a.id and x.id=b.exam_item_id and a.isActive='Y' and x.is_Active='Y' ");
		ksxjstr.append(" and dep.isActive='Y' and y.isActive='Y' and dep.id=a.dep_id ");
		ksxjstr.append(
				" and y.charge_item_id=a.id and y.examinfo_id=n.exam_info_id and b.exam_item_id=n.exam_item_id");
		ksxjstr.append(" and n.exam_info_id='" + exam_info_id + "' and x.is_print='Y' and x.remark='"+typsse+"' ");
		TranLogTxt.liswriteEror_to_txt(logName, "res: :普通科室小结-生命体征： " + ksxjstr.toString());
		List<CommonExamDetailDTO> ksmxList = this.jdbcQueryManager.getList(ksxjstr.toString(),
				CommonExamDetailDTO.class);
		 if ((ksmxList != null) && (ksmxList.size() > 0)) {
			 sb.append("			<component>\n");
		     sb.append("				<section>\n");
		     sb.append(title);
		     sb.append("					<text/>\n");
		
		for (CommonExamDetailDTO dd : ksmxList) {
			if("数字型".equals(dd.getItem_category().trim())){
				sb.append("					<entry>\n");
         		sb.append("						<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
         		sb.append("							<code code=\""+dd.getItem_num()+"\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\""+dd.getItem_name()+"\"/>\n");
         		sb.append("							<value xsi:type=\"PQ\" value=\""+dd.getExam_result().replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"\" unit=\""+dd.getItem_unit()+"\"/>\n");
         		sb.append("						</observation>\n");
         		sb.append("					</entry>\n");
			}else{
				long elid=getExamLib(dd.getExamination_item_id(),dd.getExam_result(),logName);
				if(elid>0){
					sb.append("					<entry>\n");
		     		sb.append("						<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
		     		sb.append("							<code code=\"DE04.10.209.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"心脏杂音标志\"/>\n");
		     		if ("Z".equals(dd.getHealth_level())) {
		     			sb.append("							<value xsi:type=\"BL\" value=\"true\"/>\n");
					} else if ("Y".equals(dd.getHealth_level())) {
						sb.append("							<value xsi:type=\"BL\" value=\"false\"/>\n");
					} else if ("W".equals(dd.getHealth_level())) {
						sb.append("							<value xsi:type=\"BL\" value=\"false\"/>\n");
					}			     		
		     		sb.append("							<entryRelationship typeCode=\"COMP\">\n");
		     		sb.append("								<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
		     		sb.append("									<code code=\""+dd.getItem_num()+"\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\""+dd.getItem_name()+"\"/>\n");
		     		sb.append("									<value xsi:type=\"ST\">"+dd.getExam_result().replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"</value>\n");
		     		sb.append("								</observation>\n");
		     		sb.append("							</entryRelationship>\n");
		     		sb.append("						</observation>\n");
		     		sb.append("					</entry>\n");
				}else{
					sb.append("					<entry>\n");
			        sb.append("						<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
			        sb.append("							<code code=\""+dd.getItem_num()+"\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\""+dd.getItem_name()+"\"/>\n");
			        sb.append("							<value xsi:type=\"ST\" value=\""+dd.getExam_result().replaceAll("<", "&lt;").replaceAll(">", "&gt;")+"\" />\n");
			        sb.append("						</observation>\n");
			        sb.append("					</entry>\n");
				}
			}
			
		} 
		sb.append("				</section>\n");
        sb.append("			</component>\n");
		 }
		 return sb;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private Map mapCombine(Map<String,Object> list) {  
		 //System.out.println(2.1);
	        Map<String, List<Object>> map = new HashMap<String, List<Object>>();  
	        for (String key : list.keySet()) { 
	        	//System.out.println(2.2);
	        	 String md[]=key.split("&&");
	        	 //System.out.println(2.3);
	        	 String keyone=md[0];
	                if (!map.containsKey(keyone)) { 
	                	//System.out.println(2.4);
	                    List<Object> newList = new ArrayList<Object>();  
	                    newList.add(list.get(key));  
	                    map.put(keyone, newList);  
	                    //System.out.println(2.5);
	                } else {  
	                    map.get(keyone).add(list.get(key));  
	                    //System.out.println(2.6);
	                }  
	            }  
	        //System.out.println(2.7);
	        return map;  
	    }  
	
}
