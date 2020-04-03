package com.hjw.webService.client.hokai305;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.DateUtil;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.Timeutils;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueCustomerBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.webService.client.tj180.Bean.QueueResBodyBean;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.ExaminfoChargingItemDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.domain.ExamInfo;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class QueueCustSendMessageEFY305 {
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
	
	public QueueResBody getMessage(String url, QueueCustomerBean eu, String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			
			//拼接xml 数据
			String xml  = getQueueBodyBean(eu.getExam_id(), logname);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
			
			String result = HttpUtil.doPost_Xml(url, xml, "utf-8");
			
			if ((result != null) && (result.trim().length() > 0)) {
				/*result = result.trim();
				rb = ResContralBeanHK.Queue(result);
				JSONObject jsonobject = JSONObject.fromObject(result);
				QueueResBodyBean resdah = new QueueResBodyBean();
				resdah = (QueueResBodyBean) JSONObject.toBean(jsonobject, QueueResBodyBean.class);*/
				ResultHeader rh = ResContralBeanHK.getRes(result);
				
				if ("AA".equals(rh.getTypeCode())){
						rb.setRestext("排队成功!!");
						TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext());
						ExamInfoDTO examInfoForexamId = customerInfoService.getExamInfoForexamId(eu.getExam_id());
						boolean inserflag = insertqueueLog(examInfoForexamId.getExam_num(), rh.getTypeCode(), logname);
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
					rb.setRescode("AE");
					rb.setRestext("排队失败!!");
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext() + "\r\n");
				}
						
			}
			
		}catch (Exception ex) {
			rb.setRestext("排队失败!!");
			rb.setRescode("AE");
			rb.setIdnumber("");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + rb.getRestext());
		}
		return rb;
		
	}
	
	private String getQueueBodyBean(long exam_id,String logname){
		HttpServletRequest request = ServletActionContext.getRequest();
		String ipconfig = request.getRemoteAddr();
		System.err.println("i++++++p:"+ipconfig+"======");
		ExamInfoUserDTO eu = getExamInfoAndExamid(exam_id);
		String sys_ip = this.customerInfoService.getCenterconfigByKey("GET_SYS_IP").getConfig_value().trim();
		String[] split = sys_ip.split(",");
		String locationcode="";
		String displayName="";
		
			if(split[0].equals(ipconfig)){
				locationcode="1";
				displayName="普通一";
			}else if(split[1].equals(ipconfig)){
				locationcode="2";
				displayName="普通二";
			}else if(split[2].equals(ipconfig)){
				locationcode="3";
				displayName="vip";
			}
		
				
		ExamInfo ex = getexaminfo(exam_id);
		
		
		List<ExaminfoChargingItemDTO> chargItemList = getChargItemAndExamid(exam_id);
		//ZlReqPacsItemDTO zpidto = getreqnoforpacsreqcode(exam_id);
		
		String czyxm= configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//操作员
		String czyxmid= configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//操作员id
		String zxksmc= configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();//执行科室名称
		String zxksid= configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();//执行科室id
		
		
		
	//	ResultHeader rhone = getHisReqNO(logname);
		StringBuilder sb = new StringBuilder();
	//	if(rhone.getTypeCode().equals("AA")){
			
			
			
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
			sb.append("           <item extension=\""+ex.getExam_num()+"\" root=\"2.16.156.10011.1.24\"/>                                                     ");
			sb.append("         </id>                                                                                                               ");
			sb.append("         <!--患者-->                                                                                                         ");
			sb.append("         <patient classCode=\"PAT\">                                                                                         ");
			sb.append("           <!--患者身份证号 (0..1)-->                                                                                        ");
			sb.append("           <id>                                                                                                              ");
			sb.append("             <item extension=\""+eu.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>                                            ");
			sb.append("           </id>                                                                                                             ");
			sb.append("           <!--姓名 (1..1)-->                                                                                                ");
			sb.append("           <name value=\""+eu.getUser_name()+"\"/>                                                                                          ");
			sb.append("           <!--性别 (0..1)-->                                                                                                ");
			sb.append("           <administrativeGenderCode code=\"1\" displayName=\""+eu.getSex()+"\"/>                                                       ");
			sb.append("           <!--出生日期(0..1)-->                                                                                             ");
			sb.append("           <birthTime value=\""+(eu.getBirthday().trim().replaceAll("-", "")).replace(":", "")+"\"/>                                                                                   ");
			sb.append("           <!--年龄 (0..1)-->                                                                                                ");
			sb.append("           <age units=\"岁\" value=\""+eu.getAge()+"\"/>                                                                                  ");
			sb.append("           <!--优先级别(0..1)-->                                                                                             ");
			sb.append("           <priorityCode value=\"5\"/>                                                                                       ");
			sb.append("           <!--身份代码,见身份字典(0..1)-->                                                                                  ");
			sb.append("           <identityCode code=\"\" displayName=\"\"/>                                                                  ");
			sb.append("         </patient>                                                                                                          ");
			sb.append("         <!--申请时间(0..1)-->                                                                                               ");
			sb.append("         <effectiveTime value=\""+DateUtil.getDateTimes()+"\"/>                                                                           ");
			sb.append("         <!--体检部门(0..1) -->                                                                                              ");
			sb.append("         <location code=\""+locationcode+"\" displayName=\""+displayName+"\"/>                                                                    ");
			sb.append("         <!--体检信息(0..1)-->                                                                                               ");
			sb.append("         <encounter classCode=\"ENC\" moodCode=\"EVN\">                                                                      ");
			sb.append("           <id>                                                                                                              ");
			sb.append("             <!--体检号标识(0..1)-->                                                                                         ");
			sb.append("             <item extension=\""+ex.getExam_num()+"\" root=\"2.16.156.10011.1.13\"/>                                            ");
			sb.append("             <!--患者 ID 标识(0..1)-->                                                                                       ");
			sb.append("             <item extension=\""+eu.getPatient_id()+"\" root=\"2.16.156.10011.0.2.2\"/>                                           ");
			sb.append("           </id>                                                                                                             ");
			sb.append("           <!--体检项目信息(1..1) -->                                                                                        ");
			sb.append("           <items>                                                                                                           ");
			
			for (int i = 0; i < chargItemList.size(); i++) {
				
				sb.append("             <item>                                                                                                          ");
				sb.append("               <!-- 项目序号(1..1) -->                                                                                       ");
				sb.append("               <itemNo>"+10+"</itemNo>                                                                                            ");
				sb.append("               <!-- 项目编码 名称，对应收费项目目录的编码(1..1)-->                                                           ");
				sb.append("               <itemCode code=\""+chargItemList.get(i).getHis_num()+"\" displayName=\""+chargItemList.get(i).getItem_name()+"\"/>                                                                        ");
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
			
	//	}
	//	return sb.toString();
			
		}
		
		
		
		
		


	//调用 his获取排队申请单号
	private ResultHeader getHisReqNO(String logname) {
		//1，调用his获取申请单号
		TranLogTxt.liswriteEror_to_txt(logname, "1，调用his获取排队申请单号-开始");
		ResultHeader rhone = new ResultHeader();
		String req_no = "";
		try {
			StringBuffer sb = new StringBuffer("");
			sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
			sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                        ");
			sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                         ");
			sb.append("    <!-- 消息创建时间(1..1) -->                                                                                    ");
			sb.append("    <creationTime value=\""+Timeutils.getNowDate()+"\"/>                                                                         ");
			sb.append("    <!-- 服务编码，S0087代表申请单号生成接口(1..1)-->                                                              ");
			sb.append("    <interactionId extension=\"S0087\"/>                                                                             ");
			sb.append("    <!-- 接受者(1..1) -->                                                                                          ");
			sb.append("    <receiver code=\"SYS002\"/>                                                                                ");
			sb.append("    <!-- 发送者(1..1) -->                                                                                          ");
			sb.append("    <sender code=\"SYS009\"/>                                                                                  ");
			sb.append("    <!-- 封装的消息内容 -->                                                                                        ");
			sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                            ");
			sb.append("        <!-- 消息交互类型 @code: 新增/更新 :new 撤销:delete -->                                                    ");
			sb.append("        <code value=\"new\"/>                                                                                        ");
			sb.append("        <subject typeCode=\"SUBJ\">                                                                                  ");
			sb.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                    ");
			sb.append("                <!--需要生成的申请单号类型：1:检验，2:检查(0..1)-->                                                ");
			sb.append("                <typeCode code=\"2\" value=\"检查\"/>                                                                  ");
			sb.append("            </observationRequest>                                                                                  ");
			sb.append("        </subject>                                                                                                 ");
			sb.append("        <!--操作者信息(0..1)-->                                                                                    ");
			sb.append("        <author code=\"\" displayName=\"\"/>                                                                 ");
			sb.append("    </controlActProcess>                                                                                           ");
			sb.append("</POOR_IN200901UV>");
			
			WebserviceConfigurationDTO wcf = new WebserviceConfigurationDTO();
		    wcf = webserviceConfigurationService.getWebServiceConfig("GET_REQNO");
		    String get_req_no_url = wcf.getConfig_url().trim();
		    
		    TranLogTxt.liswriteEror_to_txt(logname, "get_req_no_url:" + get_req_no_url);
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString());
			String result = HttpUtil.doPost_Xml(get_req_no_url, sb.toString(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result);
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				ResultHeader rh = ResContralBeanHK.getResGET_REQNO(result);
				if("AA".equals(rh.getTypeCode())) {
					rhone.setTypeCode("AA");
					rhone.setSourceMsgId(rh.getSourceMsgId());
					rhone.setText("获取申请单号成功:" + rh.getText());
					req_no = rh.getSourceMsgId();//申请单号
					
					
					
					
				} else {
					TranLogTxt.liswriteEror_to_txt(logname, "获取申请单号返回错误:" + rh.getText());
					rhone.setTypeCode("AE");
					rhone.setText("获取申请单号返回错误:" + rh.getText());
				}
			} else {
				TranLogTxt.liswriteEror_to_txt(logname, "获取排队申请单号无返回");
				rhone.setTypeCode("AE");
				rhone.setText("获取排队申请单号无返回");
			}
		}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		if("AE".equals(rhone.getTypeCode())) {
			return rhone;
		}
		return rhone;
	}

	//查询体检人员 的收费项目
	private List<ExaminfoChargingItemDTO> getChargItemAndExamid(long exam_id) {
		
		ArrayList<ExaminfoChargingItemDTO> list = new ArrayList<ExaminfoChargingItemDTO>();
		
		String sb2 = "select ec.id,c.id as charge_item_id,c.dep_id,ec.pay_status,ec.amount,d.dep_num,c.his_num,c.item_name "
				+ "from exam_info e,charging_item c"
				+ " left join department_dep d on d.id=c.dep_id,"
				+ " examinfo_charging_item ec "
				+ "where ec.examinfo_id = e.id and ec.charge_item_id = c.id "
				+ "and ec.pay_status <> 'M' and d.dep_num='TJUS'"
				+ "and ec.isActive = 'Y' "
				+ "and ec.change_item <> 'C' and e.is_Active='Y' "
				+ "and e.id ='"+exam_id+"'";
			//PageSupport mapcharging = this.jdbcQueryManager.getList(sb2, 1, 10000, ExaminfoChargingItemDTO.class);
			
			List<ExaminfoChargingItemDTO> charitemlist = this.jdbcQueryManager.getList(sb2,ExaminfoChargingItemDTO.class);
			
			
			return charitemlist;
	}

	//查询体检人员 人员信息
	private ExamInfoUserDTO getExamInfoAndExamid(long exam_id) {
		String sb1 = "select a.patient_id,a.id,b.arch_num,a.exam_num,a.age,b.sex,b.user_name,a.company_id,a.company,a.exam_type,a.customer_type,b.id_num,b.birthday "
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
	
	
	private ExamInfo getexaminfo(long exam_id) {
		String sb1 = " select  * from exam_info where id='"+exam_id+"'";
		
		PageSupport examinfo = this.jdbcQueryManager.getList(sb1, 1, 10000, ExamInfo.class);
		ExamInfo ex = new ExamInfo();
		if((examinfo!=null)&&(examinfo.getList().size()>0)){
			ex= (ExamInfo)examinfo.getList().get(0);	
		}
		
		return ex;
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
	
	
	
	/*private ZlReqPacsItemDTO getreqnoforpacsreqcode(long exam_id) {
		String sql = "select * from zl_req_pacs_item where exam_info_id='"+exam_id+"'";
		PageSupport map = this.jdbcQueryManager.getList(sql, 1, 10000, ZlReqPacsItemDTO.class);
		ZlReqPacsItemDTO eu = new ZlReqPacsItemDTO();
		if((map!=null)&&(map.getList().size()>0)){
			eu= (ZlReqPacsItemDTO)map.getList().get(0);			
		}
		return eu;
	}*/

}
