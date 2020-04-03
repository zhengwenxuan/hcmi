package com.hjw.webService.client.hokai305;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqPacsItemDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponent;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.MenuDTO;
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
public class PACSDELSendMessageHK305 {
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
	public PACSDELSendMessageHK305(PacsMessageBody lismessage){
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
		 ZlReqPacsItemDTO zlpacs = updatezl_req_item(lismessage.getCustom().getExam_num(), comps.getReq_no(),
				"", logname);
		if (zlpacs.getId() > 0) {
			StringBuffer sb0 = new StringBuffer("");
		
			sb0.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ");
			sb0.append("	<!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                               ");
			sb0.append("	<id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                              ");
			sb0.append("	<!-- 消息创建时间(1..1) -->                                                                                           ");
			sb0.append("	<creationTime value=\"" + lismessage.getCreationTime_value() + "\"/>                                                                              ");
			sb0.append("	<!-- 服务编码，S0041代表检查申请新增(1..1)-->                                                                         ");
			sb0.append("	<interactionId extension=\"S0041\"/>                                                                                  ");
			sb0.append("	<!-- 接受者(1..1) -->                                                                                                 ");
			sb0.append("	<receiver code=\"SYS001\"/>                                                                                     ");
			sb0.append("	<!-- 发送者(1..1) -->                                                                                                 ");
			sb0.append("	<sender code=\"SYS009\"/>                                                                                       ");
			sb0.append("	<!-- 封装的消息内容 -->                                                                                               ");
			sb0.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                               ");
			sb0.append("		<code value=\"delete\"/>                                                                                          ");
			sb0.append("		<subject typeCode=\"SUBJ\">                                                                                       ");
			sb0.append("			<observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                       ");
			sb0.append("				<!-- 检查申请单编号 必须项已使用 -->                                                                      ");
			sb0.append("				<id>                                                                                                      ");
			sb0.append("					<item extension=\""+zlpacs.getReq_id()+"\" root=\"2.16.156.10011.1.24\"/>                                         ");
			sb0.append("				</id>                                                                                                     ");
			sb0.append("				<!--检查号类别编码/类别名称/检查号(0..1)-->                                                               ");
			sb0.append("				<examId typeCode=\"\" type=\"\" value=\"\"/>                                                              ");
			sb0.append("				<!--申请单描述(0..1)-->                                                                                   ");
			sb0.append("				<text value=\"\"/>                                                                              ");
			sb0.append("				<!--申请单状态，见申请单状态字典(1..1)-->                                                                 ");
			sb0.append("				<statusCode code=\"1\" value=\"开立\"/>                                                                   ");
			sb0.append("				<!--申请时间(0..1)-->                                                                                     ");
			sb0.append("				<effectiveTime value=\"" + lismessage.getCreationTime_value() + "\"/>                                                                 ");
			sb0.append("				<!--优先级别(0..1)-->                                                                                     ");
			sb0.append("				<priority code=\"\" displayName=\"\"/>                                                               ");
			sb0.append("				<!--体征(0..1)-->                                                                                         ");
			sb0.append("				<sign value=\"正常\"/>                                                                                    ");
			sb0.append("				<!--执行科室(0..1) -->                                                                                    ");
			sb0.append("				<location code=\"\" displayName=\"\"/>                                                   ");
			sb0.append("				<!--检查类型编码/名称(0..1) -->                                                                           ");
			sb0.append("				<typeCode code=\"\" displayName=\"US\"/>                                                               ");
			sb0.append("				<!--检查子类编码/名称(0..1) -->                                                                           ");
			sb0.append("				<subTypeCode code=\"\" displayName=\"US\"/>                                                            ");
			sb0.append("				<!--临床症状(0..1)-->                                                                                     ");
			sb0.append("				<clinicSymptom value=\"正常\"/>                                                                           ");
			sb0.append("				<!--申请备注(0..1) -->                                                                                    ");
			sb0.append("				<memo value=\"\"/>                                                                                 ");
			sb0.append("				<!--注意事项(0..1) -->                                                                                    ");
			sb0.append("				<annotationText value=\"\"/>                                                                       ");
			sb0.append("				<!--开单医生/送检医生(1..1) -->                                                                           ");
			sb0.append("				<author typeCode=\"AUT\">                                                                                 ");
			sb0.append("					<!--开单者签名编码/名称-CA(0..1)-->                                                                   ");
			sb0.append("					<signatureCode code=\"S\" value=\"\"/>                                                          ");
			sb0.append("					<assignedEntity classCode=\"ASSIGNED\">                                                               ");
			sb0.append("						<!--开立者 ID(0..1)-->                                                                            ");
			sb0.append("						<id extension=\"\" root=\"2.16.156.10011.1.4\"/>                                ");
			sb0.append("						<!--开立者姓名(0..1)-->                                                                           ");
			sb0.append("						<name value=\"\"/>                                                                          ");
			sb0.append("						<!-- 申请科室信息(0..1) -->                                                                       ");
			sb0.append("						<representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                           ");
			sb0.append("							<!--医疗卫生机构（科室） ID(0..1)-->                                                          ");
			sb0.append("							<id extension=\"\" root=\"2.16.156.10011.1.26\"/>                                   ");
			sb0.append("							<!--开立科室(0..1)-->                                                                         ");
			sb0.append("							<name value=\"\"/>                                                                    ");
			sb0.append("						</representedOrganization>                                                                        ");
			sb0.append("					</assignedEntity>                                                                                     ");
			sb0.append("				</author>                                                                                                 ");
			sb0.append("				<!--检查部位编码/名称(0..1) -->                                                                           ");
			sb0.append("				<targetSiteCode code=\"\" value=\"\"/>                                                        ");
			sb0.append("				<!-- 多个检查项目循环component2 -->                                                                       ");
			int  count =1;
			for(PacsComponent pcs : comps.getPacsComponent()) {
			sb0.append("				<component2>                                                                                              ");
			sb0.append("					<observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                               ");
			sb0.append("						<id>                                                                                              ");
			sb0.append("							<!--检查项目序号ID-->                                                                         ");
			sb0.append("							<item extension=\""+ count++ +"\"/>                                                                       ");
			sb0.append("						</id>                                                                                             ");
			sb0.append("						<!--检查项目编码 必须项已使用 -->                                                                 ");
			sb0.append("						<code code=\""+pcs.getPacs_num()+"\" displayName=\""+pcs.getItemName()+"\"/>                                                    ");
			sb0.append("						<!--价格(0..1)-->                                                                                 ");
			sb0.append("						<price value=\""+pcs.getItemprice()+"\"/>                                                                          ");
			sb0.append("					</observationRequest>                                                                                 ");
			sb0.append("				</component2>                                                                                             ");
			}
			sb0.append("				<!--就诊信息(0..1) -->                                                                                    ");
			sb0.append("				<componentOf1 contextConductionInd=\"false\" typeCode=\"COMP\">                                           ");
			sb0.append("					<encounter classCode=\"ENC\" moodCode=\"EVN\">                                                        ");
			sb0.append("						<id>                                                                                              ");
			sb0.append("							<!-- 就诊次数(0..1) -->                                                                       ");
			sb0.append("							<item extension=\"\" root=\"1.2.156.112635.1.2.1.7\"/>                                       ");
			sb0.append("							<!-- 就诊流水号(1..1) -->                                                                     ");
			sb0.append("							<item extension=\"\" root=\"1.2.156.112635.1.2.1.6\"/>                                  ");
			sb0.append("						</id>                                                                                             ");
			sb0.append("						<!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->                                     ");
			sb0.append("						<code code=\"3\" displayName=\"体检\"/>                                                           ");
			sb0.append("						<!--费用类别 (0..1)-->                                                                            ");
			sb0.append("						<chargeCode code=\"1\" displayName=\"自费\"/>                                                     ");
			//String jzcs=DateTimeUtil.getDateTimes().substring(2, 4)+sjh.getOthers().substring(1,sjh.getOthers().length());
			sb0.append("						<!--就诊日期时间 (1..1)-->                                                                        ");
			sb0.append("						<effectiveTime value=\""+DateTimeUtil.getDateTimes().substring(2, 4)+"\"/>                                                         ");
			sb0.append("						<!--病人(0..1) -->                                                                                ");
			sb0.append("						<patient classCode=\"PAT\">                                                                       ");
			sb0.append("							<id>                                                                                          ");
			sb0.append("								<!--急诊号标识(0..1) -->                                                                  ");
			sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.10\"/>                              ");
			sb0.append("								<!--门诊号标识(0..1) -->                                                                  ");
			sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.11\"/>                              ");
			sb0.append("								<!--住院号标识(0..1)-->                                                                   ");
			sb0.append("								<item extension=\"\" root=\"2.16.156.10011.1.12\"/>                      ");
			sb0.append("								<!--患者 ID 标识(0..1)-->                                                                 ");
			sb0.append("								<item extension=\"" + lismessage.getCustom().getPersonid() + "\" root=\"2.16.156.10011.0.2.2\"/>                     ");
			sb0.append("							</id>                                                                                         ");
			sb0.append("							<!--患者当前就诊状态，见就诊状态字典(0..1)-->                                                 ");
			sb0.append("							<statusCode code=\"1\" value=\"挂号\"/>                                                       ");
			sb0.append("							<!--个人信息 必须项已使用(0..1) -->                                                           ");
			sb0.append("							<patientPerson classCode=\"PSN\">                                                             ");
			sb0.append("								<!-- 身份证号/医保卡号(0..1) -->                                                          ");
			sb0.append("								<id>                                                                                      ");
			sb0.append("									<!-- 身份证号(0..1) -->                                                               ");
			sb0.append("									<item extension=\""+lismessage.getCustom().getPersonidnum()+"\" root=\"2.16.156.10011.1.3\"/>                  ");
			sb0.append("									<!-- 医保卡号(0..1) -->                                                               ");
			sb0.append("									<item extension=\"\" root=\"2.16.156.10011.1.15\"/>                    ");
			sb0.append("								</id>                                                                                     ");
			sb0.append("								<!--患者姓名(0..1)-->                                                                     ");
			sb0.append("								<name value=\""+lismessage.getCustom().getName()+"\"/>                                                                    ");
			sb0.append("								<!--性别(0..1)-->                                                                         ");
			sb0.append("								<administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" displayName=\"男性\"/>                               ");
			sb0.append("								<!--出生日期(0..1)-->                                                                     ");
			sb0.append("								<birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\"/>                                                           ");
			sb0.append("								<!--年龄(0..1)-->                                                                         ");
			sb0.append("								<age units=\"岁\" value=\"" + lismessage.getCustom().getOld() + "\"/>                                                          ");
			sb0.append("								<!-- 家庭电话，电子邮件等联系方式                                                         ");
			sb0.append("                                    @use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->                          ");
			sb0.append("								<!-- 患者电话或电子邮件(1..*) -->                                                         ");
			sb0.append("								<telecom use=\"H\" value=\""+lismessage.getCustom().getContact_tel()+"\"/>                                             ");
			sb0.append("								<telecom use=\"PUB\" value=\""+lismessage.getCustom().getContact_tel()+"\"/>                                           ");
			sb0.append("								<telecom use=\"EMA\" value=\"\"/>                                      ");
			sb0.append("								<!--病人来源编码/名称(0..1)-->                                                            ");
			sb0.append("								<patientSource code=\"1\" displayName=\"战士\"/>                                          ");
			sb0.append("								<!--外来医疗单位名称(0..1)-->                                                             ");
			sb0.append("								<facility code=\"1\" displayName=\"战士\"/>                                               ");
			sb0.append("								<!--身份(0..1)-->                                                                         ");
			sb0.append("								<age code=\"1\" displayName=\"战士\"/>                                                    ");
			sb0.append("							</patientPerson>                                                                              ");
			sb0.append("						</patient>                                                                                        ");
			sb0.append("						<!--住院位置-住院有此节点，其他可无此节点(0..1)-->                                                ");
			sb0.append("						<location typeCode=\"LOC\">                                                                       ");
			sb0.append("							<!--@root类别， @extension:病床号 @displayName:病床名称-->                                    ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.22\"/>                    ");
			sb0.append("							<!--@root类别， @extension:病房编码 @displayName:病房名称-->                                  ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.21\"/>                ");
			sb0.append("							<!--@root类别， @extension:科室编码 @displayName:科室名称-->                                  ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.26\"/>               ");
			sb0.append("							<!--@root类别， @extension:病区编码 @displayName:病区名称-->                                  ");
			sb0.append("							<item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.27\"/>             ");
			sb0.append("						</location>                                                                                       ");
			sb0.append("						<!--诊断(检查申请原因)(0..*) -->                                                                  ");
			sb0.append("						<pertinentInformation1 typeCode=\"PERT\">                                                         ");
			sb0.append("							<observationDx classCode=\"OBS\" moodCode=\"EVN\">                                            ");
			sb0.append("								<!--诊断类别编码/名称(0..1) -->                                                           ");
			sb0.append("								<code code=\"\" displayName=\"\"/>                                               ");
			sb0.append("								<!--诊断代码及描述 (0..1)-->                                                              ");
			sb0.append("								<value code=\"\" displayName=\"\"/>                                                  ");
			sb0.append("								<!--建议描述 (0..1)-->                                                                    ");
			sb0.append("								<suggestionText/>                                                                         ");
			sb0.append("								<!--诊断时间(0..1) -->                                                                    ");
			sb0.append("								<effectiveTime value=\"\"/>                                                 ");
			sb0.append("								<!--诊断医生工号/姓名 (0..1)-->                                                           ");
			sb0.append("								<author code=\"\" displayName=\"\"/>                                            ");
			sb0.append("							</observationDx>                                                                              ");
			sb0.append("						</pertinentInformation1>                                                                          ");
			sb0.append("					</encounter>                                                                                          ");
			sb0.append("				</componentOf1>                                                                                           ");
			sb0.append("			</observationRequest>                                                                                         ");
			sb0.append("		</subject>                                                                                                        ");
			sb0.append("	</controlActProcess>                                                                                                  ");
			sb0.append("</POOR_IN200901UV>                                                                                                        ");
		

			
			xml=sb0.toString() ;

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
	public ZlReqPacsItemDTO updatezl_req_item(String exam_num,String req_id,String ciid,String logname){
		ExamInfoUserDTO ei= new ExamInfoUserDTO();
		ZlReqPacsItemDTO zpcs = new ZlReqPacsItemDTO();
		ei=this.getExamInfoForNum(exam_num);
		Connection tjtmpconnect = null;
		long lisid=0;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select id,req_id from zl_req_pacs_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_ids='"+ciid+"' and pacs_req_code='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				lisid=rs1.getLong("id");
				zpcs.setId(Integer.parseInt(lisid+""));
				zpcs.setReq_id(rs1.getString("req_id"));
				
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
		return zpcs;
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
