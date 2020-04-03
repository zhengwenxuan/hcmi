package com.hjw.webService.client.hokai305;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.webService.client.tj180.Bean.QueueResBodyBean;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class QueueAddSendMessageHK305 {

	
	private static JdbcQueryManager jdbcQueryManager;
	private static CustomerInfoService customerInfoService;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		customerInfoService = (CustomerInfoService) wac.getBean("customerInfoService");
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	
	public QueueResBody getMessage(String url, QueueAddBean eu, String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			
			//拼接xml 数据
			String xml  = getQueueBodyBean(eu.getExam_id(), logname);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
			
			String result = HttpUtil.doPost_Xml(url, xml, "utf-8");
			
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				rb = ResContralBeanHK.Queue(result);
				JSONObject jsonobject = JSONObject.fromObject(result);
				QueueResBodyBean resdah = new QueueResBodyBean();
				resdah = (QueueResBodyBean) JSONObject.toBean(jsonobject, QueueResBodyBean.class);
				if ("AA".equals(rb.getRescode())){
						rb.setRestext("排队成功!!");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext());
						ExamInfoDTO examInfoForexamId = customerInfoService.getExamInfoForexamId(eu.getExam_id());
						boolean inserflag = insertqueueLog(examInfoForexamId.getExam_num(), resdah.getQueueId(), logname);
						if (inserflag) {
							rb.setRescode("AA");
							rb.setRestext("排队成功，写本地日志表exam_queue_log成功，详情请看日志文件");
							rb.setIdnumber("");
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext());
						} else {
							rb.setRestext("排队成功，但写本地日志表exam_queue_log失败，详情请看日志文件");
							rb.setRescode("AE");
							rb.setIdnumber("");
							TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext());
						}
				} else {
					rb.setRestext("排队失败!!");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext() + "\r\n");
				}
						
			}
			
		}catch (Exception ex) {
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
			rb.setIdnumber("");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext());
		}
		return rb;
		
	}
	
	private String getQueueBodyBean(long exam_id,String logname){
		
		ExamInfoUserDTO eu = getExamInfoAndExamid(exam_id);
		List<ExaminfoChargingItemDTO> chargItemList = getChargItemAndExamid(exam_id);
		
		String czyxm= configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//操作员
		String czyxmid= configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//操作员id
		String zxksmc= configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//执行科室名称
		String zxksid= configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//执行科室id
		
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" <POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">   ");
		sb.append("   <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                   ");
		sb.append("   <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                                  ");
		sb.append("   <!-- 消息创建时间(1..1) -->                                                                                               ");
		sb.append("   <creationTime value=\""+DateUtil.getDateTimes()+"\"/>                                                                                  ");
		sb.append("   <!-- 服务编码，S0086代表体检叫号申请接口(1..1)-->                                                                         ");
		sb.append("   <interactionId extension=\"S0086\"/>                                                                                      ");
		sb.append("   <!-- 接受者(1..1) -->                                                                                                     ");
		sb.append("   <receiver code=\"SYS002\"/>                                                                                         ");
		sb.append("   <!-- 发送者(1..1) -->                                                                                                     ");
		sb.append("   <sender code=\"SYS009\"/>                                                                                           ");
		sb.append("   <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                                   ");
		sb.append("     <!-- 消息交互类型 @code: 新增/更新 :new 撤销:delete -->                                                                 ");
		sb.append("     <code value=\"new\"/>                                                                                                   ");
		sb.append("     <subject typeCode=\"SUBJ\">                                                                                             ");
		sb.append("       <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                               ");
		sb.append("         <!-- 申请单号(1..1) -->                                                                                             ");
		sb.append("         <id>                                                                                                                ");
		sb.append("           <item extension=\"0923848747\" root=\"2.16.156.10011.1.24\"/>                                                     ");
		sb.append("         </id>                                                                                                               ");
		sb.append("         <!--患者-->                                                                                                         ");
		sb.append("         <patient classCode=\"PAT\">                                                                                         ");
		sb.append("           <!--患者身份证号 (0..1)-->                                                                                        ");
		sb.append("           <id>                                                                                                              ");
		sb.append("             <item extension=\""+eu.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>                                            ");
		sb.append("           </id>                                                                                                             ");
		sb.append("           <!--姓名 (1..1)-->                                                                                                ");
		sb.append("           <name value=\""+eu.getU_name()+"\"/>                                                                                          ");
		sb.append("           <!--性别 (0..1)-->                                                                                                ");
		sb.append("           <administrativeGenderCode code=\"1\" displayName=\""+eu.getSex()+"\"/>                                                       ");
		sb.append("           <!--出生日期(0..1)-->                                                                                             ");
		sb.append("           <birthTime value=\""+eu.getBirthday()+"\"/>                                                                                   ");
		sb.append("           <!--年龄 (0..1)-->                                                                                                ");
		sb.append("           <age units=\"岁\" value=\""+eu.getAge()+"\"/>                                                                                  ");
		sb.append("           <!--优先级别(0..1)-->                                                                                             ");
		sb.append("           <priorityCode value=\"5\"/>                                                                                       ");
		sb.append("           <!--身份代码,见身份字典(0..1)-->                                                                                  ");
		sb.append("           <identityCode code=\"\" displayName=\"\"/>                                                                  ");
		sb.append("         </patient>                                                                                                          ");
		sb.append("         <!--申请时间(0..1)-->                                                                                               ");
		sb.append("         <effectiveTime value=\""+DateUtil.getDateTime()+"\"/>                                                                           ");
		sb.append("         <!--体检部门(0..1) -->                                                                                              ");
		sb.append("         <location code=\""+zxksid+"\" displayName=\""+zxksmc+"\"/>                                                                    ");
		sb.append("         <!--体检信息(0..1)-->                                                                                               ");
		sb.append("         <encounter classCode=\"ENC\" moodCode=\"EVN\">                                                                      ");
		sb.append("           <id>                                                                                                              ");
		sb.append("             <!--体检号标识(0..1)-->                                                                                         ");
		sb.append("             <item extension=\"\" root=\"2.16.156.10011.1.13\"/>                                            ");
		sb.append("             <!--患者 ID 标识(0..1)-->                                                                                       ");
		sb.append("             <item extension=\""+eu.getPatient_id()+"\" root=\"2.16.156.10011.0.2.2\"/>                                           ");
		sb.append("           </id>                                                                                                             ");
		sb.append("           <!--体检项目信息(1..1) -->                                                                                        ");
		sb.append("           <items>                                                                                                           ");
		
		for (int i = 0; i < chargItemList.size(); i++) {
			
			sb.append("             <item>                                                                                                          ");
			sb.append("               <!-- 项目序号(1..1) -->                                                                                       ");
			sb.append("               <itemNo>"+i+"</itemNo>                                                                                            ");
			sb.append("               <!-- 项目编码 名称，对应收费项目目录的编码(1..1)-->                                                           ");
			sb.append("               <itemCode code=\"\" displayName=\""+chargItemList.get(i).getHis_num()+"\"/>                                                                        ");
			sb.append("               <!--执行科室(0..1) -->                                                                                        ");
			sb.append("               <location code=\""+zxksid+"\" displayName=\""+zxksmc+"\"/>                                                              ");
			sb.append("             </item>          ");
			
		}
		
		
		sb.append("           </items>                                                                                                          ");
		sb.append("         </encounter>                                                                                                        ");
		sb.append("       </observationRequest>                                                                                                 ");
		sb.append("     </subject>                                                                                                              ");
		sb.append("     <!--操作者信息(0..1)-->                                                                                                 ");
		sb.append("     <author code=\""+czyxmid+"\" displayName=\""+czyxm+"\"/>                                                                          ");
		sb.append("   </controlActProcess>                                                                                                      ");
		sb.append(" </POOR_IN200901UV>                                                                                                          ");
		
		
		return sb.toString();
		
	}

	//查询体检人员 的收费项目
	private List<ExaminfoChargingItemDTO> getChargItemAndExamid(long exam_id) {
		
		ArrayList<ExaminfoChargingItemDTO> list = new ArrayList<ExaminfoChargingItemDTO>();
		
		String sb2 = "select ec.id,c.id as charge_item_id,c.dep_id,ec.pay_status,ec.amount,d.dep_num,c.his_num "
				+ "from exam_info e,charging_item c"
				+ " left join department_dep d on d.id=c.dep_id,"
				+ " examinfo_charging_item ec "
				+ "where ec.examinfo_id = e.id and ec.charge_item_id = c.id "
				+ "and ec.pay_status <> 'M'  "
				+ "and ec.isActive = 'Y' "
				+ "and ec.change_item <> 'C' and e.is_Active='Y' "
				+ "and e.id ='"+exam_id+"'";
			//PageSupport mapcharging = this.jdbcQueryManager.getList(sb2, 1, 10000, ExaminfoChargingItemDTO.class);
			
			List<ExaminfoChargingItemDTO> charitemlist = this.jdbcQueryManager.getList(sb2,ExaminfoChargingItemDTO.class);
			
			
			return charitemlist;
	}

	//查询体检人员 人员信息
	private ExamInfoUserDTO getExamInfoAndExamid(long exam_id) {
		String sb1 = "select a.patient_id,a.id,b.arch_num,a.exam_num,b.sex,a.company_id,a.company,a.exam_type,a.customer_type,b.id_num,b.birthday "
				+ " from exam_info a,customer_info b "
				+ " where a.customer_id=b.id "
				+ " and a.is_Active='Y' "
				+ " and a.freeze=0 and a.status<>'Z' "
				+ " and a.id='"+exam_id+"'";
		
		PageSupport mapexaminfo = this.jdbcQueryManager.getList(sb1, 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if((mapexaminfo!=null)&&(mapexaminfo.getList().size()>0)){
			eu= (ExamInfoUserDTO)mapexaminfo.getList().get(0);	
		}
		
		return eu;
	}

	private boolean insertqueueLog(String exam_num,String queueNo,String logname){
		Connection tjtmpconnect = null;
		boolean tjvip=false;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "insert exam_queue_log (exam_num,queue_no,dept_num,queue_day,queue_date) values('"
			+exam_num+"','"+queueNo+"','','"+DateTimeUtil.getDate()+"','"+DateTimeUtil.getDateTime()+"') ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb1);				
			tjtmpconnect.createStatement().execute(sb1);
			tjvip=true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return tjvip;
	}
	
	/**
	 * 
	 * @param exam_num
	 * @param vipflag
	 * @param amt
	 * @param logname
	 * @return
	 */
	/*public String getCheckvip(String exam_num,String vipflag,double amt,String logname){
		Connection tjtmpconnect = null;
		int tjvip=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sb1 = "SELECT id,vip_code,vip_name,amt_lower,amt_upper,isActive "
						+ "FROM config_exam_vip where "+amt+">=amt_lower and "+amt+"<=amt_upper  order by req_no";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb1);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				if (rs1.next()) {
					if("TJVIP".equals(rs1.getString("vip_code"))){
						tjvip=1; 
					}										
				}
				rs1.close();
			int vipflagnew=0;
			if(("0".equals(vipflag))&&(tjvip==1)){
				vipflagnew=0;
			}else if(("1".equals(vipflag))&&(tjvip==0)){
				vipflagnew=2;
			}else if(("1".equals(vipflag))&&(tjvip==1)){
				vipflagnew=3;
			}else{
				vipflagnew=0;
			}
			String ustr="update exam_info set vipflag='"+vipflagnew+"' where exam_num='"+exam_num+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +ustr);
			tjtmpconnect.createStatement().execute(ustr);
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return vipflag;
	}*/
	
	
	/*private String getDeptNumflag(long exam_id,String dept_num,String logname){
		Connection tjtmpconnect = null;
		String flag="0";
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sb1 = "select * from examinfo_charging_item a,charging_item b,department_dep c "
						+ "where a.isActive='Y' and a.examinfo_id='"+exam_id+"' "
						+ "and a.exam_status='N' and a.charge_item_id=b.id and b.dep_id=c.id and c.dep_num in ("+dept_num+") ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +sb1);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
				if (rs1.next()) {
					flag="1";									
				}
				rs1.close();
			
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :  操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return flag;
	}*/
	/**
	 * 
	 * @param strs
	 * @param str
	 * @return
	 */
	/*private int getcheckStr(String strs,String str){
		int f=0;  // 有：1，没有：0
		String[] strings=strs.split(",");
		for(int i=0;i<strings.length;i++){
			if(str.equals(strings[i])){
				f=1;
				break;
			}
		}
		return f;
	}*/
}