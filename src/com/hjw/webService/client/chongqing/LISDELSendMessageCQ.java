package com.hjw.webService.client.chongqing;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapLocator;
import com.hjw.webService.client.chongqing.soap.ZlHisSoapPortType;
import com.hjw.webService.client.chongqing.util.ChongQingSetHL7;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import ca.uhn.hl7v2.model.v251.message.ORL_O22;
import ca.uhn.hl7v2.model.v251.segment.MSA;
import ca.uhn.hl7v2.model.v251.segment.MSH;
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
public class LISDELSendMessageCQ {
	private LisMessageBody lismessage;
	private static ConfigService configService;
    private static JdbcQueryManager jdbcQueryManager;
    static {
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		configService = (ConfigService) wac.getBean("configService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	public LISDELSendMessageCQ(LisMessageBody lismessage){
		this.lismessage=lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname, boolean debug) {
		ResultLisBody rb = new ResultLisBody();
		try {
			JSONObject json = JSONObject.fromObject(lismessage);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			List<ApplyNOBean> anList=new ArrayList<ApplyNOBean>();
			for (LisComponents comps : lismessage.getComponents()) {
					
					ResultHeader rhone = lisSendMessage(url,comps,logname);
					if("AA".equals(rhone.getTypeCode())){
						ApplyNOBean an = new ApplyNOBean();
						an.setApplyNO(comps.getReq_no());
						anList.add(an);
					}
			}
			rb.getResultHeader().setTypeCode("AA");
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}

	private ResultHeader lisSendMessage(String url,LisComponents comps, String logname) {
		ResultHeader rhone= new ResultHeader();
		try {
			ExamInfoUserDTO eu = configService.getExamInfoForBarcode(comps.getReq_no());
			String LisSendReq = ChongQingSetHL7.DelLis(comps,eu,logname);// 撤销lis申请
			TranLogTxt.liswriteEror_to_txt(logname, "Lis发送c撤销申请入参:" + LisSendReq + "\r\n");

			URL zlurl = new URL(url);
			ZlHisSoapLocator soapLocator = new ZlHisSoapLocator();
			ZlHisSoapPortType portType = soapLocator.getzlHisSoapHttpSoap11Endpoint(zlurl);
			String res = portType.zlWS_HL7(LisSendReq);
			TranLogTxt.liswriteEror_to_txt(logname, "LIs发送申请返回:" + res + "\r\n");
			rhone= ChongQingSetHL7.getResCodeCQ(res, logname);
			/*
			ExamInfoUserDTO eu = configService.getExamInfoForBarcode(comps.getReq_no());
			List<ZlReqItemDTO> req_item_list = configService.select_zl_req_item(eu.getId(), comps.getReq_no(), logname);
			for(ZlReqItemDTO req_item : req_item_list) {
				StringBuffer sb = new StringBuffer("");
				sb.append("<POLB_IN224000UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
				sb.append("    <!--消息流水号-->\n");
				sb.append("    <id extension=\"" + UUID.randomUUID().toString().toLowerCase() + "\" root=\"2.16.156.10011.0\"/>\n");
				sb.append("    <!--消息创建时间 -->\n");
				sb.append("    <creationTime value=\"" + lismessage.getCreationTime_value() + "\"/>\n");
				sb.append("    <interactionId extension=\"S0073\" root=\"2.16.840.1.113883.1.6\"/>\n");
				sb.append("    <processingCode code=\"P\"/>\n");
				sb.append("    <processingModeCode/>\n");
				sb.append("    <acceptAckCode code=\"AL\"/>\n");
				sb.append("    <receiver typeCode=\"RCV\">\n");
				sb.append("        <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
				sb.append("            <id>\n");
				sb.append("                <item extension=\"SYS001\" root=\"2.16.156.10011.0.1.1\"/>\n");
				sb.append("            </id>\n");
				sb.append("        </device>\n");
				sb.append("    </receiver>\n");
				sb.append("    <sender typeCode=\"SND\">\n");
				sb.append("        <device classCode=\"DEV\" determinerCode=\"INSTANCE\">\n");
				sb.append("            <id>\n");
				sb.append("                <item extension=\"SYS009\" root=\"2.16.156.10011.0.1.2\"/>\n");
				sb.append("            </id>\n");
				sb.append("        </device>\n");
				sb.append("    </sender>\n");
				sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">\n");
				sb.append("        <subject contextConductionInd=\"false\" typeCode=\"SUBJ\">\n");
				sb.append("            <observationReport classCode=\"GROUPER\" moodCode=\"EVN\">\n");
				sb.append("                <id>\n");
				sb.append("                    <!--电子申请单编号(1..1)-->\n");
				sb.append("                    <item extension=\""+comps.getReq_no()+"\" root=\"2.16.156.10011.1.24\"/>\n");
				sb.append("                </id>\n");
				sb.append("				<actOrderid>\n");
				sb.append("                    <!--医嘱ID-->\n");
				sb.append("                    <item root=\"2.16.156.10011.1.28\" extension=\""+req_item.getId()+"\" />\n");
				sb.append("                </actOrderid>\n");
				sb.append("                <code/>\n");
				sb.append("                <!--申请单描述-->\n");
				sb.append("                <text value=\"申请单描述\"/>\n");
				sb.append("                <!--申请单状态-->\n");
				sb.append("                <statusCode code=\"disable\"/>\n");
				sb.append("                <!--优先级别-->\n");
				sb.append("                <priorityCode/>\n");
				sb.append("                <specimen contextControlCode=\"OP\" typeCode=\"SPC\">\n");
				sb.append("                    <specimen classCode=\"SPEC\">\n");
				sb.append("                        <!--标本ID/或者条码ID(1..1)-->\n");
				sb.append("                        <id extension=\""+comps.getReq_no()+"\" root=\"2.16.156.10011.1.14\"/>\n");
				sb.append("                        <!--标本类别代码(1..1)-->\n");
				sb.append("                        <code code=\"1\">\n");
				sb.append("                            <displayName value=\"标本类别名称\"/>\n");
				sb.append("                        </code>\n");
				sb.append("                        <subjectOf1 typeCode=\"SBJ\">\n");
				sb.append("                            <specimenProcessStep classCode=\"PROC\" moodCode=\"EVN\">\n");
				sb.append("                                <!--检验操作(1..1)-->\n");
				sb.append("                                <verifier typeCode=\"AUTHEN\">\n");
				sb.append("                                    <!--操作日期时间(1..1)-->\n");
				sb.append("                                    <time value=\""+DateTimeUtil.getDateTimes()+"\" xsi:type=\"TS\"/>\n");
				sb.append("<!--操作代码和名称：分配条码(备管)(code=10)，采集(code=20), 报告结果删除(code=260)(1..1)，标本作废(code=270)-->\n");
				sb.append("                                    <modeCode code=\"code代码\">\n");
				sb.append("                                        <displayName value=\"送检\"/>\n");
				sb.append("                                        <originalText value=\"描述\"/>\n");
				sb.append("                                    </modeCode>\n");
				sb.append("                                    <!--操作者(0..1)-->\n");
				sb.append("                                    <assignedEntity classCode=\"ASSIGNED\">\n");
				sb.append("                                        <!--操作者代码(0..1)-->\n");
				sb.append("                                        <id>\n");
				sb.append("                                            <item extension=\"@员工号\" root=\"2.16.156.10011.1.4\"/>\n");
				sb.append("                                        </id>\n");
				sb.append("                                        <assignedPerson classCode=\"" + lismessage.getDoctor().getDoctorCode()
						+ "\" determinerCode=\"INSTANCE\">\n");
				sb.append("                                            <!--操作者姓名(0..1)-->\n");
				sb.append("                                            <name xsi:type=\"BAG_EN\">\n");
				sb.append("                                                <item>\n");
				sb.append("                                                    <part value=\"" + lismessage.getDoctor().getDoctorName()
						+ "\"/>\n");
				sb.append("                                                </item>\n");
				sb.append("                                            </name>\n");
				sb.append("                                        </assignedPerson>\n");
				sb.append("                                        <!--操作科室(0..1)-->\n");
				sb.append("                                        <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">\n");
				sb.append("                                            <id>\n");
				sb.append("                                                <item extension=\"科室ID\" root=\"2.16.156.10011.1.26\"/>\n");
				sb.append("                                            </id>\n");
				sb.append("                                            <name xsi:type=\"BAG_EN\">\n");
				sb.append("                                                <item>\n");
				sb.append("                                                    <part value=\"健康管理中心\"/>\n");
				sb.append("                                                </item>\n");
				sb.append("                                            </name>\n");
				sb.append("                                        </representedOrganization>\n");
				sb.append("                                    </assignedEntity>\n");
				sb.append("                                </verifier>\n");
				sb.append("                            </specimenProcessStep>\n");
				sb.append("                        </subjectOf1>\n");
				sb.append("                    </specimen>\n");
				sb.append("                </specimen>\n");
				sb.append("                <!--记录对象(0..1)-->\n");
				sb.append("                <recordTarget contextControlCode=\"OP\" typeCode=\"RCT\">\n");
				sb.append("                    <patient classCode=\"PAT\">\n");
				sb.append("                        <!--PatientID(0..1)-->\n");
				sb.append("                        <id>\n");
				sb.append("                            <item extension=\"\" root=\"2.16.156.10011.0.2.2\"/>\n");
				sb.append("                        </id>\n");
				sb.append("                        <statusCode code=\"disable\"/>\n");
				sb.append("                        <patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">\n");
				sb.append("                            <id>\n");
				sb.append("                                <!--身份证号(0..1)-->\n");
				sb.append("                                <item extension=\"" + lismessage.getCustom().getPersonidnum()
					+ "\" root=\"2.16.156.10011.1.3\"/>\n");
				sb.append("                                <!-- 医保卡号(0..1) -->\n");
				sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.15\"/>\n");
				sb.append("                            </id>\n");
				sb.append("                            <!--患者姓名(0..1)-->\n");
				sb.append("                            <name xsi:type=\"LIST_EN\">\n");
				sb.append("                                <item>\n");
				sb.append("                                    <part value=\"" + lismessage.getCustom().getName() + "\"/>\n");
				sb.append("                                </item>\n");
				sb.append("                            </name>\n");
				sb.append("							<!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->\n");
				sb.append("							<patientType>\n");
				sb.append("								<patienttypeCode code=\"4\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码表\" displayName=\"体检\"/>\n");
				sb.append("							</patientType>\n");
				sb.append("                            <!--性别(0..1)-->\n");
				sb.append("                            <administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" codeSystem=\"2.16.156.10011.2.3.3.4\" codeSystemName=\"\">\n");
				sb.append("                                <displayName value=\""+lismessage.getCustom().getSexname()+"性\"/>\n");
				sb.append("                            </administrativeGenderCode>\n");
				sb.append("                            <!--出生日期(0..1)-->\n");
				sb.append("                            <birthTime value=\"" + lismessage.getCustom().getBirthtime() + "\"/>\n");
				sb.append("                        </patientPerson>\n");
				sb.append("                    </patient>\n");
				sb.append("                </recordTarget>\n");
				sb.append("            </observationReport>\n");
				sb.append("        </subject>\n");
				sb.append("    </controlActProcess>\n");
				sb.append("</POLB_IN224000UV01>\n");
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
				String result = HttpUtil.doPost_Xml(url,sb.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();				
					rhone = ResContralBeanHK.getRes(result);				
				}
			}
		*/}catch(Exception ex){
			rhone.setTypeCode("AE");
			rhone.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
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
			String sb1 = "select id from zl_req_item where  exam_info_id='"+ei.getId()
					+"'  and charging_item_id='"+ciid+"' and req_id='"+req_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ":操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				lisid=rs1.getLong("id");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + lismessage.getMessageid()+ ": zl_req_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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

}
