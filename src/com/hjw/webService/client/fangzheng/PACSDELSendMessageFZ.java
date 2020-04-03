package com.hjw.webService.client.fangzheng;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.PacsComponents;
import com.hjw.webService.client.body.PacsMessageBody;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.webService.client.fangzheng.MQ.mqSendSample;
import com.hjw.wst.DTO.ExamInfoUserDTO;
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
public class PACSDELSendMessageFZ {
	private PacsMessageBody lismessage;
    private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public PACSDELSendMessageFZ(PacsMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultPacsBody getMessage(String url, String logname) {
		ResultPacsBody rb = new ResultPacsBody();
		mqSendSample mqSendSample =null;
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			String ip = url.split("&")[0];
			int port = Integer.valueOf(url.split("&")[1]);
			mqSendSample = new mqSendSample();
			// int port = 5000;
			mqSendSample.initEnvironment(ip, port);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (PacsComponents comps : lismessage.getComponents()) {
				try {
					String xml = lisSendMessage(comps,logname);
					if(xml.trim().length()>0){
						String stype=getOrderExecId(comps.getPacsComponent().get(0).getItemCode(),logname);
					TranLogTxt.liswriteEror_to_txt(logname, "req:" + lismessage.getMessageid() + ":" + xml);
					String messages = mqSendSample.msgSend(xml, "BS005","0401",stype);
					TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid() + ":" + messages);
					ApplyNOBean an = new ApplyNOBean();
					an.setApplyNO(comps.getReq_no());
					anList.add(an);
					}
				} catch (Exception ex) {
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("pacs解析返回值错误");
				}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}finally{
			if(mqSendSample!=null){
				try{
				mqSendSample.disconnectQM();
				}catch(Exception ex){
					
				}
			}			
		}
		return rb;
	}

	private String lisSendMessage(PacsComponents comps, String logname) {
		String xml="";
		
		long lisid = updatezl_req_item(lismessage.getCustom().getExam_num(), comps.getReq_no(),
				comps.getPacsComponent().get(0).getItemCode(), logname);
		if (lisid > 0) {
			StringBuffer sb = new StringBuffer("");
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xsi:schemaLocation=\"urn:hl7-org:v3 ../../Schemas/POOR_IN200901UV11.xsd\">\n");
		sb.append("<!-- 消息ID -->\n");
		sb.append("<id extension=\"BS005\" />\n");
		sb.append("<!-- 消息创建时间 -->\n");
		sb.append("<creationTime value=\"" + lismessage.getCreationTime_value() + "\" />\n");
		sb.append("<!-- 交互ID -->\n");
		sb.append("<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"POOR_IN200901UV11\" />\n");
		sb.append("<!-- 消息用途: P(Production); D(Debugging); T(Training) -->\n");
		sb.append("<processingCode code=\"P\" />\n");
		sb.append("<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) -->\n");
		sb.append("<processingModeCode code=\"T\" />");
		sb.append("<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->\n");
		sb.append("<acceptAckCode code=\"NE\" />\n");
		sb.append("<!-- 接受者 -->\n");
		sb.append("<receiver typeCode=\"RCV\">\n");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("<!-- 接受者ID -->\n");
		sb.append("<id>\n");
		sb.append("<item root=\"1.2.156.112685.1.1.19\" extension=\"\"/>\n");
		sb.append("</id>\n");
		sb.append("</device>\n");
		sb.append("</receiver>\n");
		sb.append("<!-- 发送者 -->\n");
		sb.append("<sender typeCode=\"SND\">\n");
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
		sb.append("<!-- 发送者ID -->\n");
		sb.append("<id>\n");
		sb.append("<item root=\"1.2.156.112685.1.1.19\" extension=\"S040\"/>\n");
		sb.append("</id>\n");
		sb.append("</device>\n");
		sb.append("</sender>\n");
		sb.append("<!-- 封装的消息内容(按Excel填写) -->\n");
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
		sb.append("<!-- 触发事件(triggerEventID - 2.16.840.1.113883.1.18) @code: stop表示停止, cancel表示撤销-->\n");
		sb.append("<code code=\"cancel\"></code>\n");
		sb.append("<subject typeCode=\"SUBJ\" contextConductionInd=\"false\">\n");
		sb.append("<placerGroup>\n");
		sb.append("<subject typeCode=\"SBJ\">\n");
		sb.append("<patient classCode=\"PAT\">\n");
		sb.append("<id>\n");
		sb.append("<!-- 域ID -->\n");
		sb.append("<item root=\"1.2.156.112685.1.2.1.2\" extension=\"01\" />\n");
		sb.append("<!-- 患者ID -->\n");
		sb.append("<item root=\"1.2.156.112685.1.2.1.3\" extension=\"" + lismessage.getCustom().getPersonid()
				+ "\" />\n");
		ExamInfoUserDTO sjh = getHISDJH(lismessage.getCustom().getExam_num());
		sb.append("</id>\n");
		sb.append("<!-- 申请科室信息 -->\n");
		sb.append("<providerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append("<!--申请科室编码 必须项已使用 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"0176\" root=\"1.2.156.112685.1.1.1\" />\n");
		sb.append("</id>\n");
		sb.append("<!--申请科室名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item>\n");
		sb.append("<part value=\"体检中心\" />\n");
		sb.append("</item>\n");
		sb.append("</name>\n");
		sb.append("<asOrganizationPartOf classCode=\"PART\">\n");
		sb.append("<wholeOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">\n");
		sb.append("<!--医疗机构代码 -->\n");
		sb.append("<id>\n");
		sb.append("<item extension=\"46014326-4\"/>\n");// 46014326-4
		sb.append("</id>\n");
		sb.append("<!--医疗机构名称 -->\n");
		sb.append("<name xsi:type=\"BAG_EN\">\n");
		sb.append("<item><part value=\"包头市中心医院\" /></item>\n");// 包头市中心医院
		sb.append("</name>\n");
		sb.append("</wholeOrganization>\n");
		sb.append("</asOrganizationPartOf>\n");
		sb.append("</providerOrganization>\n");
		sb.append("</patient>\n");
		sb.append("</subject>\n");
			
				StringBuffer sb0 = new StringBuffer();
				sb0.append("<!--1..n 一个检验消息中可以由多个申请单。component2对应一个申请单，有多个申请单时，重复component2 -->\n");
				sb0.append("<component2 typeCode=\"COMP\">\n");
				sb0.append("<!-- 项目序号(可选项) -->\n");
				sb0.append("<sequenceNumber value=\"\" />\n");
				sb0.append("<substanceAdministrationRequest classCode=\"SBADM\" moodCode=\"RQO\">\n");
				sb0.append("<!-- 医嘱号 必须项已使用 -->\n");
				sb0.append("<id extension=\"" + lisid + "\" identifierName=\"定位执行关联的医嘱\" />\n");
				sb0.append("<!-- 医嘱类型 必须项目已使用 -->\n");
				sb0.append("<code code=\"D\" codeSystem=\"1.2.156.112685.1.1.27\">\n");
				sb0.append("<!-- 医嘱类型名称 -->\n");
				sb0.append("<displayName value=\"检查\" />\n");
				sb0.append("</code>\n");
				sb0.append("<specimen typeCode=\"SPC\">\n");
				sb0.append("<specimen classCode=\"SPEC\">\n");
				sb0.append("<!--标本号/条码号 必须项已使用 -->\n");
				sb0.append("<id extension=\"" + comps.getReq_no() + "\" />\n");//---------------??确认
				sb0.append("<!--标本角色代码（patient,group,blind) 必须项目未使用 -->\n");
				sb0.append("<code />\n");
				sb0.append("</specimen>\n");
				sb0.append("</specimen>\n");
				sb0.append("<performer typeCode=\"PRF\">\n");
				//sb0.append("<assignedEntity classCode=\"ASSIGNED\">\n");
				sb0.append("<!-- 撤销或停止时间 -->\n");
				sb0.append("<time>\n");
				sb0.append("<any value=\"" + DateTimeUtil.getDateTimes() + "\"></any>\n");
				sb0.append("</time>\n");
				sb0.append("<assignedEntity classCode=\"ASSIGNED\">\n");
				sb0.append("<!-- 撤销或停止人编码(assignedEntity - 2.16.840.1.113883.11.11595) -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"" + lismessage.getDoctor().getDoctorCode()
						+ "\" root=\"1.2.156.112685.1.1.2\"></item>\n");
				sb0.append("</id>\n");
				sb0.append("<assignedPerson determinerCode=\"INSTANCE\"	classCode=\"PSN\">\n");
				sb0.append("<!-- 撤销人姓名 -->\n");
				sb0.append("<name xsi:type=\"LIST_EN\">\n");
				sb0.append("<item>\n");
				sb0.append("<part value=\"" + lismessage.getDoctor().getDoctorName() + "\"/>\n");
				sb0.append("</item>\n");
				sb0.append("</name>\n");
				sb0.append("</assignedPerson>\n");
				sb0.append("</assignedEntity>\n");
				sb0.append("</performer>\n");
				sb0.append("<!-- 执行科室 -->\n");
				sb0.append("<location typeCode=\"LOC\">\n");
				sb0.append("<serviceDeliveryLocation classCode=\"SDLOC\">\n");
				sb0.append("<location classCode=\"PLC\" determinerCode=\"INSTANCE\">\n");
				sb0.append("<!-- 执行科室编码 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"" + comps.getPacsComponent().get(0).getServiceDeliveryLocation_code()
						+ "\" root=\"1.2.156.112685.1.1.1\"></item>\n");
				sb0.append("</id>\n");
				sb0.append("<!--执行科室名称 -->\n");
				sb0.append("<name xsi:type=\"BAG_EN\">\n");
				sb0.append("<item>\n");
				sb0.append("<part value=\"" + comps.getPacsComponent().get(0).getServiceDeliveryLocation_name() + "\" />\n");
				sb0.append("</item>\n");
				sb0.append("</name>\n");
				sb0.append("</location>\n");
				sb0.append("</serviceDeliveryLocation>\n");
				sb0.append("</location>\n");

				sb0.append("<!-- 医嘱撤消原因 -->\n");
				sb0.append("<reason contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<code>\n");
				//sb0.append("<displayName value=\"医疗付费方式\"/>\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\"  value=\"\"/>\n");
				sb0.append("</observation>\n");
				sb0.append("</reason>\n");

				sb0.append("<!-- 互斥信息 -->\n");
				sb0.append("<reason contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"EVN\">\n");
				sb0.append("<!-- 互斥医嘱号 -->\n");
				sb0.append("<id>\n");
				sb0.append("<item extension=\"\" />\n");
				sb0.append("</id>\n");
				sb0.append("<!-- 医嘱类别编码/嘱类别名称 - 针剂药品, 材料类, 治疗类, 片剂药品, 化验类 -->\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.27\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("</observation>\n");
				sb0.append("</reason>\n");

				sb0.append("<!-- 先诊疗后付费类型  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"CD\" code=\"\">\n");
				sb0.append("<displayName value=\"\"/>\n");
				sb0.append("</value>\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");

				sb0.append("<!-- 收费状态标识  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\"	contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"收费状态标识\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"0\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");

				sb0.append("<!-- HIS执行状态  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"0\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");

				sb0.append("<!-- 业务操作时间  -->\n");
				sb0.append("<pertinentInformation typeCode=\"PERT\" contextConductionInd=\"false\">\n");
				sb0.append("<observation classCode=\"OBS\" moodCode=\"INT\">\n");
				sb0.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.120\">\n");
				sb0.append("<displayName value=\"\" />\n");
				sb0.append("</code>\n");
				sb0.append("<value xsi:type=\"ST\" value=\"\" />\n");
				sb0.append("</observation>\n");
				sb0.append("</pertinentInformation>\n");
				sb0.append("</substanceAdministrationRequest>\n");
				sb0.append("</component2>\n");

				sb0.append(" <!--就诊 -->\n");
				sb0.append("<componentOf1 contextConductionInd=\"false\" xsi:nil=\"false\" typeCode=\"COMP\">\n");
				sb0.append(" <!--就诊 -->\n");
				sb0.append("<encounter classCode=\"ENC\" moodCode=\"EVN\">\n");
				sb0.append("<id >\n");
				sb0.append(" <!-- 就诊次数 -->\n");
				String dates = DateTimeUtil.getDateTimes();
				String jzcs = dates.substring(2, 4) + sjh.getOthers().substring(1, sjh.getOthers().length());
				sb0.append("<item extension=\"" + jzcs + "\" root=\"1.2.156.112685.1.2.1.7\"/>\n");
				sb0.append("<!-- 就诊流水号 -->\n");
				String lsh = lismessage.getCustom().getPersonid()
						+ sjh.getOthers().substring(1, sjh.getOthers().length());
				sb0.append("<item extension=\"" + lsh + "\" root=\"1.2.156.112685.1.2.1.6\"/>\n");
				sb0.append("</id>\n");
				sb0.append("<!--病人类型编码 -->\n");
				sb0.append("<code codeSystem=\"1.2.156.112685.1.1.80\" code=\"0401\" >\n");
				sb0.append("<!-- 就诊类别名称 -->\n");
				sb0.append("<displayName value=\"干保体检\" />\n");
				sb0.append("</code>\n");
				sb0.append(" <!--必须项未使用 -->\n");
				sb0.append("<statusCode />\n");
				sb0.append(" <!--病人 必须项未使用 -->\n");
				sb0.append("<subject typeCode=\"SBJ\">\n");
				sb0.append("<patient classCode=\"PAT\" />\n");
				sb0.append("</subject>\n");

				sb0.append("</encounter>\n");
				sb0.append("</componentOf1>\n");
				sb0.append("</placerGroup>\n");
				sb0.append("</subject>\n");
				sb0.append("</controlActProcess>\n");
				sb0.append("</POOR_IN200901UV>\n");
				xml=sb.toString() + sb0.toString();
			}
		return xml;
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
