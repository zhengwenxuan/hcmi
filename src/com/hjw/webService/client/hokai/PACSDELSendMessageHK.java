package com.hjw.webService.client.hokai;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.service.BatchService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description: 2.7	检查申请撤销信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class PACSDELSendMessageHK {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    private static BatchService batchService; 
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		batchService = (BatchService) wac.getBean("batchService");
	
	}
	public PACSDELSendMessageHK(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (PacsComponents comps : lismessage.getComponents()) {
				ResultHeader rhone = lisSendMessage(url, comps, logname);
				if ("AA".equals(rhone.getTypeCode())) {
					ApplyNOBean an = new ApplyNOBean();
					/*String getCreateID = batchService.GetCreateID("pacs_req_num");
					
					StringBuffer sb = new StringBuffer();
					sb.append("update pacs_summary set pacs_req_code='"+getCreateID+"' where pacs_req_code = '" + comps.getReq_no() + "' ");
					Connection connection=null;
					connection = this.jdbcQueryManager.getConnection();
					connection.createStatement().executeUpdate(sb.toString());*/
					
					//an.setApplyNO(getCreateID);
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
					
					
					
					
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		return rb;
	}

	private ResultHeader lisSendMessage(String url,PacsComponents comps, String logname) {
		String xml="";
		ResultHeader rhone= new ResultHeader();
		rhone.setTypeCode("AE");
		try{
		long lisid = updatezl_req_item(lismessage.getCustom().getExam_num(), comps.getReq_no(),
				"", logname);
		if (lisid > 0) {
			StringBuffer sb = new StringBuffer("");
		
		sb.append("<POOR_IN200902UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");  
		sb.append("    <!-- 消息流水号 -->  ");  
		sb.append("    <id extension=\"" + UUID.randomUUID().toString().toLowerCase() + "\" root=\"2.16.156.10011.0\"/>  ");  
		sb.append("    <!-- 消息创建时间 -->");  
		sb.append("    <creationTime value=\"" + lismessage.getCreationTime_value() + "\"/> ");  
		sb.append("    <!-- 消息的服务标识-->   ");  
		sb.append("    <interactionId extension=\"S0074\" root=\"2.16.840.1.113883.1.6\"/>");  
		sb.append("    <!--处理代码，标识此消息是否是产品、训练、调试系统的一部分。 D：调试； P：产品； T：训练 -->   ");  
		sb.append("    <processingCode code=\"P\"/>   ");  
		sb.append("    <!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Currentprocessing) -->");  
		sb.append("    <processingModeCode/>");  
		sb.append("    <!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->");  
		sb.append("    <acceptAckCode code=\"AL\"/>   ");  
		sb.append("    <!-- 接受者 -->  ");  
		sb.append("    <receiver typeCode=\"RCV\">");  
		sb.append("        <device classCode=\"DEV\" determinerCode=\"INSTANCE\">   ");  
		sb.append("            <!-- 接受者ID -->");  
		sb.append("            <id> ");  
		sb.append("                <item extension=\"SYS001\" root=\"2.16.156.10011.0.1.1\"/>   ");  
		sb.append("            </id>");  
		sb.append("        </device>");  
		sb.append("    </receiver>  ");  
		sb.append("    <!-- 发送者 -->  ");  
		sb.append("    <sender typeCode=\"SND\">  ");  
		sb.append("        <device classCode=\"DEV\" determinerCode=\"INSTANCE\">   ");  
		sb.append("            <!-- 发送者ID -->");  
		sb.append("            <id> ");  
		sb.append("                <item extension=\"SYS009\" root=\"2.16.156.10011.0.1.2\"/>   ");  
		sb.append("            </id>");  
		sb.append("        </device>");  
		sb.append("    </sender>");  
		sb.append("    <!-- 封装的消息内容(按Excel填写) -->   ");  
		sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">");  
		sb.append("        <!-- 消息交互类型 @code: 新增 :new 修改:update -->   ");  
		sb.append("        <code code=\"new\" codeSystem=\"2.16.840.1.113883.1.6\"/>");  
		sb.append("        <subject typeCode=\"SUBJ\" xsi:nil=\"false\">  ");  
		sb.append("            <placerGroup>");  
		sb.append("                <!-- 1..n可循环 检查状态信息 -->   ");  
		sb.append("                <component2> ");  
		sb.append("                    <observationRequest classCode=\"OBS\"> ");  
		sb.append("                        <!-- 必须项已使用 -->");  
		sb.append("                        <id> ");  
		sb.append("                            <!-- 申请单号 -->");  
		sb.append("                            <item extension=\""+comps.getReq_no()+"\" root=\"2.16.156.10011.1.24\"/>");  
		sb.append("                        </id>");  
		sb.append("						<actOrderid>");  
		sb.append("                            <!--医嘱ID-->  ");  
		sb.append("                            <item root=\"2.16.156.10011.1.28\" extension=\"" + lisid + "\" />");  
		sb.append("                        </actOrderid>  ");  
		sb.append("                        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->  ");  
		sb.append("						<patientType> ");  
		sb.append("							<patienttypeCode code=\"3\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码表\" displayName=\"体检\"/> ");  
		sb.append("						</patientType>");  
		sb.append("                        <!-- 操作人 -->");  
		sb.append("                        <performer typeCode=\"PRF\"> ");  
		sb.append("                            <time> ");  
		sb.append("                                <!-- 操作日期 -->  ");  
		sb.append("                                <low value=\"" + lismessage.getCreationTime_value() + "\"/>  ");  
		sb.append("                            </time>");  
		sb.append("                            <assignedEntity classCode=\"ASSIGNED\">");  
		sb.append("                                <!-- 操作人编码 -->");  
		sb.append("                                <id>   ");  
		sb.append("                                    <item extension=\"" + lismessage.getDoctor().getDoctorCode()+ "\" root=\"2.16.156.10011.1.4\"/>");  
		sb.append("                                </id>  ");  
		sb.append("                                <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\"> ");  
		sb.append("                                    <!-- 操作人姓名 必须项已使用 --> ");  
		sb.append("                                    <name xsi:type=\"BAG_EN\"> ");  
		sb.append("                                        <item> ");  
		sb.append("                                            <part value=\"" + lismessage.getDoctor().getDoctorName() + "\"/>   ");  
		sb.append("                                        </item>");  
		sb.append("                                    </name>");  
		sb.append("                                </assignedPerson>  ");  
		sb.append("                            </assignedEntity>");  
		sb.append("                        </performer>   ");  
		sb.append("                        <!--执行科室 -->   ");  
		sb.append("                        <location typeCode=\"LOC\" xsi:nil=\"false\">");  
		sb.append("                            <!--就诊机构/科室 -->  ");  
		sb.append("                            <serviceDeliveryLocation classCode=\"SDLOC\">  ");  
		sb.append("                                <serviceProviderOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">   ");  
		sb.append("                                    <!--执行科室编码 --> ");  
		sb.append("                                    <id>   ");  
		sb.append("                                        <item extension=\"" + comps.getPacsComponent().get(0).getServiceDeliveryLocation_code()+ "\" root=\"2.16.156.10011.1.26\"/> ");  
		sb.append("                                    </id>  ");  
		sb.append("                                    <!--执行科室名称 --> ");  
		sb.append("                                    <name xsi:type=\"BAG_EN\"> ");  
		sb.append("                                        <item> ");  
		sb.append("                                            <part value=\"" + comps.getPacsComponent().get(0).getServiceDeliveryLocation_name() + "\"/>");  
		sb.append("                                        </item>");  
		sb.append("                                    </name>");  
		sb.append("                                </serviceProviderOrganization>   ");  
		sb.append("                            </serviceDeliveryLocation> ");  
		sb.append("                        </location>");  
		sb.append("                        <!-- 检查状态 -->  ");  
		sb.append("                        <!-- 新登记(01)，已检查(02)，已报告(03)，已审核(04), 已撤销(05) -->");  
		sb.append("                        <component1 contextConductionInd=\"true\"> ");  
		sb.append("                            <processStep classCode=\"PROC\">   ");  
		sb.append("                                <code code=\"05\" codeSystem=\"1.2.156.112635.1.1.93\">");  
		sb.append("                                    <!--检查状态名称 --> ");  
		sb.append("                                    <displayName value=\"已撤销\"/>");  
		sb.append("                                </code>");  
		sb.append("                            </processStep> ");  
		sb.append("                        </component1>  ");  
		sb.append("                    </observationRequest>  ");  
		sb.append("                </component2>");  
		sb.append("            </placerGroup>   ");  
		sb.append("        </subject>   ");  
		sb.append("    </controlActProcess> ");  
		sb.append("</POOR_IN200902UV>");                                                                                                              
		xml=sb.toString() ;

		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml + "\r\n");
		String result = HttpUtil.doPost_Xml(url,xml, "utf-8");
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					rhone = ResContralBeanHK.getRes(result);

				}
		
			}
		}catch(Exception ex){
			
		}
		
		return rhone;
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	private ExamInfoUserDTO getExamInfoForNum(String exam_num) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.exam_times ");
		sb.append(" from exam_info c ");
		sb.append(" where c.is_Active='Y' ");		
		sb.append(" and c.exam_num = '" + exam_num + "' ");	
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
	public double getChargingAmt(String id) throws ServiceException {
		Connection tjtmpconnect = null;
		double lisitemid =0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select amount from charging_item a where id='"+id+"'";
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				lisitemid = rs1.getDouble("amount");
			}
			rs1.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisitemid;
	} 
	
	/**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public long updatezl_req_item(String exam_num,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		long lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select id from zl_req_pacs_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_ids='"+ciid+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				lisid=rs1.getLong("id");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return lisid;
	}
	
	/**
	 * 获取医嘱执行分类编码
	 * @param url
	 * @param view_num
	 * @return
	 */
	public String getOrderExecId(String cicode,String logname) throws ServiceException {
		Connection tjtmpconnect = null;
		String eu="";
		PreparedStatement preparedStatement = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.remark from charging_item a where a.item_code='"+cicode+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				eu = rs1.getString("remark");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		if(eu==null){
			eu="0000";
		}else if(eu.trim().length()<=0){
			eu="0000";
		}else if("超声".equals(eu.trim())){
			eu="0101";
		}else if("CT".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("DR".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("MRI".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("内窥镜".equals(eu.trim().toUpperCase())){
			eu="0105";
		}else if("数字胃肠".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("病理".equals(eu.trim().toUpperCase())){
			eu="0104";
		}else if("PET".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("X线".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("乳腺".equals(eu.trim().toUpperCase())){
			eu="0103";
		}else if("心电".equals(eu.trim().toUpperCase())){
			eu="0106";
		}else{
			eu="0000";
		}
		return eu;
	} 
}
