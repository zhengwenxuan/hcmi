package com.hjw.webService.client.hokai305;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.webService.client.hokai.bean.ResCustomBeanHK;
import com.hjw.webService.client.hokai.bean.TeamAccBeanHK;
import com.hjw.webService.client.hokai.bean.TeamItemBeanHK;
import com.hjw.webService.client.huojianwa.bean.ComAccBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.CompanyService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Description: 发送团体缴费申请
 * @version V2.0.0.0
 */
public class FEETermSendMessageHK_bak305 {
	private String accnum = "";
	private String personid = "";
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static CompanyService companyService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();

		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		companyService = (CompanyService) wac.getBean("companyService");
	}

	public FEETermSendMessageHK_bak305(String personid, String accnum) {
		this.accnum = accnum;
		this.personid = personid;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public FeeResultBody getMessage(String url, String logname) {
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + this.accnum + ":" + xml);
		try {
			getString(url, logname);
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("");
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception： " + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息解析返回值错误");
		}

		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + accnum + ":" + xml);
		return rb;
	}

	private void getString(String url, String logname) {
		@SuppressWarnings("unused")
		// String charging_summary_num =
		// GetNumContral.getInstance().getParamNum("rcpt_num");
		ComAccBean cb = new ComAccBean();
		cb = this.getAcc_nums(this.accnum, logname);// 团体信息
		WebserviceConfigurationDTO wcf = webserviceConfigurationService.getWebServiceConfig("CUST_APPLICATION");
		ResCustomBeanHK rcb = searchPatid(wcf.getConfig_value().trim(),cb, this.accnum, logname);
		if ("AA".equals(rcb.getCode())) {
			if (rcb.getFaly() == true) {// 没有 controlActProcess 节点 没有档案信息
										// 需要调用新增接口
				setSearchString(wcf.getConfig_url().trim(), this.accnum, cb,rcb.getPersionid(),logname);
				insert_search(cb, this.accnum, rcb.getPersionid(), logname);
			} else {
				insert_search(cb, this.accnum, rcb.getPersionid(), logname);
			}

		
		List<TeamAccBeanHK> tblist = new ArrayList<TeamAccBeanHK>();
		tblist = this.getAccnumList(this.accnum, logname);
		for (TeamAccBeanHK tb : tblist) {
			ResultHeader rh = new ResultHeader();

			String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();// 开单医生id
			String doctorname = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();// 开单医生名称
			String kddepid = configService.getCenterconfigByKey("IS_LIS_DEPT_ID").getConfig_value().trim();// 开单科室名称
			String kddepname = configService.getCenterconfigByKey("IS_LIS_DEPT_NAME").getConfig_value().trim();// 开单科室名称
			String SQKS = companyService.getDatadis("SQKS").get(0).getRemark();
			String exam_num = tb.getExam_num();

			try {
				StringBuffer sb = new StringBuffer();
				sb.append(
						"<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  ");
				sb.append(
						"  <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                  ");
				sb.append("  <id extension=\"" + UUID.randomUUID().toString().toLowerCase()
						+ "\"/>                                                                 ");
				sb.append(
						"  <!-- 消息创建时间(1..1) -->                                                                                              ");
				sb.append("  <creationTime value=\"" + DateTimeUtil.getDateTimes()
						+ "\"/>                                                                                 ");
				sb.append(
						"  <!-- 服务编码，S0085代表收费申请(1..1)-->                                                                                ");
				sb.append(
						"  <interactionId extension=\"S0084\"/>                                                                                     ");
				sb.append(
						"  <!-- 接受者(1..1) -->                                                                                                    ");
				sb.append(
						"  <receiver code=\"SYS002\"/>                                                                                        ");
				sb.append(
						"  <!-- 发送者(1..1) -->                                                                                                    ");
				sb.append(
						"  <sender code=\"SYS009\"/>                                                                                          ");
				sb.append(
						"  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                                  ");
				sb.append(
						"    <!-- 消息交互类型 @code: 新增/更新 :new 撤销:delete -->                                                                ");
				sb.append(
						"    <code value=\"new\"/>                                                                                                  ");
				sb.append(
						"    <subject typeCode=\"SUBJ\">                                                                                            ");
				sb.append(
						"      <observationRequest classCode=\"OBS\" moodCode=\"RQO\">                                                              ");
				sb.append(
						"        <!-- 申请单号(1..1) -->                                                                                            ");
				sb.append(
						"        <id>                                                                                                               ");
				sb.append("          <item extension=\"" + this.accnum + "\" root=\"2.16.156.10011.1.24\"/>                                                    ");
				sb.append(
						"        </id>                                                                                                              ");
				sb.append(
						"        <!--申请时间(0..1)-->                                                                                              ");
				sb.append("        <effectiveTime value=\"" + DateTimeUtil.getDateTimes()
						+ "\"/>                                                                          ");
				sb.append(
						"        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->                                                      ");
				sb.append(
						"        <code code=\"3\" displayName=\"体检\"/>                                                                            ");
				sb.append(
						"        <!--开立者(1..1)-->                                                                                                ");
				sb.append(
						"        <author typeCode=\"AUT\">                                                                                          ");
				sb.append(
						"          <!--开单时间(0..1)-->                                                                                            ");
				sb.append("          <time value=\"" + DateTimeUtil.getDateTimes()
						+ "\"/>                                                                                 ");
				sb.append(
						"          <assignedEntity classCode=\"ASSIGNED\">                                                                          ");
				sb.append(
						"            <!--开立者 ID(0..1)-->                                                                                         ");
				sb.append("            <id extension=\"" + doctorid
						+ "\" root=\"2.16.156.10011.1.4\"/>                                             ");
				sb.append(
						"            <!--开立者姓名(0..1)-->                                                                                        ");
				sb.append("            <name value=\"" + doctorname
						+ "\"/>                                                                                       ");
				sb.append(
						"            <!-- 申请科室信息(0..1) -->                                                                                    ");
				sb.append(
						"            <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                        ");
				sb.append(
						"              <!--医疗卫生机构（科室） ID(0..1)-->                                                                         ");
				sb.append("              <id extension=\"" + kddepid
						+ "\" root=\"2.16.156.10011.1.26\"/>                                                  ");
				sb.append(
						"              <!--开立科室(0..1)-->                                                                                        ");
				sb.append("              <name value=\"" + kddepname
						+ "\"/>                                                                                     ");
				sb.append(
						"            </representedOrganization>                                                                                     ");
				sb.append(
						"          </assignedEntity>                                                                                                ");
				sb.append(
						"        </author>                                                                                                          ");
				sb.append(
						"        <!--执行科室(0..1) -->                                                                                             ");
				sb.append("        <location code=\"" + kddepid + "\" displayName=\"" + kddepname
						+ "\"/>                                                                   ");
				sb.append(
						"        <!--申请备注(0..1) -->                                                                                             ");
				sb.append(
						"        <memo value=\"注意XXX\"/>                                                                                          ");
				sb.append(
						"        <!--体检信息(0..1)-->                                                                                              ");
				sb.append(
						"        <encounter classCode=\"ENC\" moodCode=\"EVN\">                                                                     ");
				sb.append(
						"          <id>                                                                                                             ");
				sb.append(
						"            <!--体检号标识(0..1)-->                                                                                        ");
				sb.append(
						"            <item extension=\"\" root=\"2.16.156.10011.1.13\"/>                                           ");
				sb.append(
						"            <!--患者 ID 标识(0..1)-->                                                                                      ");
				sb.append(
						"            <item extension=\"" + rcb.getPersionid() + "\" root=\"2.16.156.10011.0.2.2\"/>                                          ");
				sb.append(
						"          </id>                                                                                                            ");
				sb.append(
						"          <!--PERSONAL=个检;GROUP=团检(0..1)-->                                                                            ");
				sb.append(
						"          <code code=\"GROUP\" displayName=\"团检\"/>                                                                   ");
				sb.append(
						"          <!--合同单位，团检时有此节点(0..1)-->                                                                            ");
				sb.append("          <unitInContract code=\""+cb.getCom_num()+"\" displayName=\"" + cb.getCom_name()
						+ "\"/>                                                                     ");
				sb.append(
						"          <!-- 申请类型编码，名称，用于区分此申请是收费还是算绩效，1:收费，2:绩效(1..1) -->                                ");
				sb.append(
						"          <typeCode code=\"1\" displayName=\"收费\"/>                                                                           ");
				sb.append(
						"          <!-- 体检批次,包含年份信息(1..1) -->                                                                             ");
				sb.append("          <peTimes value=\"" + cb.getBatch_num()
						+ "\"/>                                                                                            ");
				sb.append(
						"          <!--收费明细信息,团检时申请状态是绩效时用此节点(1..1) -->                                                        ");
				sb.append(
						"          <chargeInfos>                                                                                                    ");
				sb.append(
						"            <!--待收费信息(1..*) -->                                                                                       ");
				sb.append(
						"            <chargeInfo>                                                                                                   ");
				sb.append(
						"              <!-- 项目序号(1..1) -->                                                                                      ");
				sb.append(
						"              <itemNo>1</itemNo>                                                                                           ");
				sb.append(
						"              <!-- 项目编码 名称，对应收费项目目录的编码(1..1)-->                                                          ");
				sb.append(
						"              <item code=\"\" displayName=\"\"/>                                                                           ");
				sb.append(
						"              <!-- 实收金额(0..1)-->                                                                                       ");
				sb.append(
						"              <costs value=\"\"/>                                                                                          ");
				sb.append(
						"              <!-- 应收金额(0..1) -->                                                                                      ");
				sb.append(
						"              <charge value=\"\"/>                                                                                         ");
				sb.append(
						"              <!-- 数量以及单位(0..1)-->                                                                                   ");
				sb.append(
						"              <count units=\"\" value=\"1\"/>                                                                            ");
				sb.append(
						"            </chargeInfo>                                                                                                  ");
				sb.append(
						"          </chargeInfos>                                                                                                   ");
				sb.append(
						"          <!--收费汇总信息，状态为收费时用此节点(0..1) -->                                                                 ");
				sb.append(
						"          <counts>                                                                                                          ");

				sb.append(
						"          <count>                                                                                                          ");
				sb.append(
						"            <!-- 项目编码/名称(1..1) -->                                                                                   ");
				sb.append(
						"            <item class=\"D\" code=\"CTF001\" displayName=\"查体费\"/>                         ");
				sb.append("            <units value=\"次\"/>                            ");
				sb.append("            <spec   value=\"\\\"/>              ");
				sb.append("            <amount value=\"" + 1 + "\"/>                            ");
				sb.append(
						"            <!-- 人数，团检时有此节点(0..1) -->                                                                            ");
				sb.append(
						"            <number value=\"\"/>                                                                                           ");
				sb.append(
						"            <!-- 实收总金额(0..1) -->                                                                                      ");
				sb.append("            <costs value=\"" + cb.getAmount2() + "\"/>                             ");
				sb.append(
						"            <!-- 应收总金额(0..1) -->                                                                                      ");
				sb.append("            <charge value=\"" + cb.getAmount1() + "\"/>                            ");
				sb.append(
						"            <!-- 项目序号(1..1) -->                                                                                      ");
				sb.append("            <itemNo>" + 1 + "</itemNo>                           ");
				sb.append(
						"            <!-- 医嘱序号(1..1) -->                                                                                     ");
				sb.append("            <orderNo  value=\"" + 1 + "\"/>                            ");
				sb.append(
						"            <!-- 医嘱类别(1..1) -->                                                                                     ");
				sb.append("            <orderClass  value=\"D\"/>                            ");
				sb.append(
						"          </count>                                                                                                         ");
				sb.append(
						"          </counts>                                                                                                         ");
				sb.append(
						"        </encounter>                                                                                                       ");
				sb.append(
						"      </observationRequest>                                                                                                ");
				sb.append(
						"    </subject>                                                                                                             ");
				sb.append(
						"    <!--操作者信息(0..1)-->                                                                                                ");
				sb.append("    <author code=\"" + doctorid + "\" displayName=\"" + doctorname
						+ "\"/>                                                         ");
				sb.append(
						"  </controlActProcess>                                                                                                     ");
				sb.append(
						"</POOR_IN200901UV>    																										");

				List<TeamItemBeanHK> tilist = new ArrayList<TeamItemBeanHK>();
				tilist = this.getAccItemList(tb, logname);

				TranLogTxt.liswriteEror_to_txt(logname, "团体结账申请入参:" + sb.toString() + "\r\n");
				TranLogTxt.liswriteEror_to_txt(logname, "团体结账申请发送地址:" + url + "\r\n");
				String result = HttpUtil.doPost_Xml(url, sb.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "团体结账申请返回:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					rh = ResContralBeanHK.getRes(result);
				} else {
					rh.setTypeCode("AE");
					rh.setText("接口无返回");
				}
			} catch (Exception ex) {
				rh.setTypeCode("AE");
				rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
			}
		}
		}
	}

	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public ExamInfoUserDTO getExamInfoForNum(String exam_num, String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.age,c.exam_num,a.user_name,c.age,a.id_num,a.birthday,a.sex,c.status,c.exam_type"
				+ ",c.register_date,c.join_date,c.phone,c.exam_times,a.arch_num,c.patient_id ");
		sb.append(" from exam_info c,customer_info a ");
		sb.append(" where a.id=c.customer_id and c.is_Active='Y' ");
		sb.append(" and c.exam_num = '" + exam_num + "' ");
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
		PageSupport map = this.jdbcQueryManager.getList(sb.toString(), 1, 10000, ExamInfoUserDTO.class);
		ExamInfoUserDTO eu = new ExamInfoUserDTO();
		if ((map != null) && (map.getList().size() > 0)) {
			eu = (ExamInfoUserDTO) map.getList().get(0);
		}
		return eu;
	}

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public ComAccBean getAcc_nums(String account_num, String logname) {
		Connection tjtmpconnect = null;
		ComAccBean accnums = new ComAccBean();
		List<String> list = new ArrayList<String>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select f.batch_num,f.com_name,b.acc_num,f.com_num,a.amount1,a.amount2 from charging_summary_group a "
					+ "left join (select c.id,c.batch_num,d.com_name,d.com_num from batch c,company_info d where c.company_id=d.id) f on f.id=a.batch_id"
					+ ",team_invoice_account b  " + "where a.account_num=b.account_num and a.account_num='"
					+ account_num + "'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				accnums.setAcc_num(rs1.getString("acc_num"));
				accnums.setBatch_num(rs1.getString("batch_num"));
				accnums.setCom_name(rs1.getString("com_name"));
				accnums.setAmount1(rs1.getString("amount1"));
				accnums.setAmount2(rs1.getString("amount2"));
				accnums.setCom_num(rs1.getString("com_num"));
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}

		return accnums;
	}

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public List<TeamAccBeanHK> getAccnumList(String account_num, String logname) {
		Connection tjtmpconnect = null;
		List<TeamAccBeanHK> tblist = new ArrayList<TeamAccBeanHK>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select distinct exam_num,acc_num from team_account_exam_list where acc_num in("
					+ "select distinct acc_num from team_invoice_account where account_num='" + account_num + "')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				TeamAccBeanHK tb = new TeamAccBeanHK();
				tb.setAcc_num(rs1.getString("acc_num"));
				tb.setExam_num(rs1.getString("exam_num"));
				tblist.add(tb);
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}

		return tblist;
	}

	/**
	 * 
	 * @param exam_info_id
	 * @param chargitem_id
	 * @param lis_req_no
	 * @param logname
	 * @return
	 */
	public List<TeamItemBeanHK> getAccItemList(TeamAccBeanHK tb, String logname) {
		Connection tjtmpconnect = null;
		List<TeamItemBeanHK> tblist = new ArrayList<TeamItemBeanHK>();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select ta.price,ta.acc_charge,ci.his_num,ci.item_name from team_account_item_list ta,"
					+ "charging_item ci where ta.acc_num='" + tb.getAcc_num() + "' and ta.exam_num='" + tb.getExam_num()
					+ "' " + "and ci.id=ta.charging_item_id";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				TeamItemBeanHK tbi = new TeamItemBeanHK();
				tbi.setAcc_charge(rs1.getDouble("acc_charge"));
				tbi.setHis_num(rs1.getString("his_num"));
				tbi.setItem_name(rs1.getString("item_name"));
				tbi.setPrice(rs1.getDouble("price"));
				tblist.add(tbi);
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}

		return tblist;
	}

	/**
	 * 新增
	 * @param cb 
	 * @param logname 
	 * 
	 * @return
	 */
	private void setSearchString(String url, String acc_num, ComAccBean cb, String patid, String logname) {
		StringBuffer sb0 = new StringBuffer();
		sb0.append(
				"<PRPA_IN201311UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb0.append("	<!-- 消息ID -->  ");
		sb0.append("	<id extension=\"" + UUID.randomUUID().toString().toLowerCase() + "\"/>  ");
		sb0.append("	<!-- 消息创建时间 -->   ");
		sb0.append("	<creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/>");
		sb0.append("	<!-- 交互ID(HL7交互类型代码系统) 用来区分消息类型，S0001代表患者注册     -->  ");
		sb0.append("	<interactionId extension=\"S0001\"/> ");

		sb0.append("	<!-- 接收者 --> ");
		sb0.append("  <receiver  code=\"SYS001\"/> ");
		sb0.append("	<!-- 发送者 --> ");
		sb0.append("  <sender    code=\"SYS009\"/> ");
		sb0.append("	<!-- 封装的消息内容 - Trigger Event Control Act wrapper --> ");
		sb0.append("	<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">   ");
		sb0.append("		<code  value=\"create\"/> ");
		sb0.append("		<subject typeCode=\"SUBJ\">   ");
		sb0.append("			<registrationRequest classCode=\"REG\" moodCode=\"RQO\">");
		sb0.append("				<subject1 typeCode=\"SBJ\"> ");
		sb0.append("					<patient classCode=\"PAT\"> ");
		sb0.append("	<!-- 注册，更新时为active，作废时为disable  --> ");
		sb0.append("	<statusCode code=\"active\"/>   ");
		sb0.append("	<!-- 患者信息来源，1.门诊 2.住院 3.体检(1..1) -->  ");
		sb0.append("	<source  code=\"3\" displayName=\"体检中心\"/>   ");
		sb0.append("	<!-- 必须项已使用 --> ");
		sb0.append("	<!-- 各种标识符 患者在医院中的ID(1..*)-->   ");
		sb0.append("	<id>   ");
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.1\"/>");// 患者EMPI标识
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.13\"/>");// 住院号
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.12\"/>");// 就诊卡号
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.11\"/>");// 医保号
		sb0.append("		<item extension=\"\" identifierName=\"\" root=\"1.2.156.112635.1.2.1.2\"/>");// 域ID(门诊、住院)
		sb0.append("		<item extension=\"" + patid+ "\" identifierName=\"\" root=\"2.16.156.10011.0.2.2\"/>");// 患者ID
		sb0.append("	</id> ");
		sb0.append("	<!-- 患者建卡日期(0..1) -->   ");
		sb0.append("	<effectiveDate value=\"\"/> ");
		sb0.append("	<!-- 上次就诊日期(0..1) -->  ");
		sb0.append("	<lastVisitDate value=\"\"/> ");
		sb0.append("	<!-- 保密等级(2:内部级，3:秘密级，4:机密级，5:绝密级)(0..1) -->");
		sb0.append("	<confidentialityCode code=\"2\" displayName=\"内部级\"/> ");
		sb0.append("	<!-- 费别,见费别字典(0..1) -->");
		sb0.append("	<chargeType code=\"13\" displayName=\"收费\"/> ");
		sb0.append("	<!-- 患者基本信息(1..1) --> ");
		sb0.append("	<patientPerson>");
		sb0.append("		<!-- 身份证号及各种证件号(身份证号必须有)(1..*)");
		sb0.append("		@extension: 证件号码 ");
		sb0.append("	@root: 证件OID ");
		sb0.append("	@controlInformationExtension: 证件名称 --> ");
		sb0.append("	<id>   ");
		sb0.append("		<item extension=\"\" root=\"2.16.156.10011.1.3\" controlInformationExtension=\"居民身份证\"/>");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.14\" controlInformationExtension=\"居民户口簿\"/>   ");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.15\" controlInformationExtension=\"护照\"/> ");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.16\" controlInformationExtension=\"军官证\"/> ");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.17\" controlInformationExtension=\"驾驶证\"/> ");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.18\" controlInformationExtension=\"港澳居民来往内地通行证\"/> ");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.19\" controlInformationExtension=\"台湾居民来往内地通行证\"/> ");
		sb0.append(
				"		<item extension=\"\" root=\"2.16.156.10011.1.1.2.2.1.20\" controlInformationExtension=\"其他法定有效证件\"/> ");
		sb0.append("	</id> ");
		sb0.append("	<!--姓名 (1..1)-->");
		sb0.append("	<name value=\""+cb.getCom_name()+"\"/>   ");
		sb0.append("	<!-- 家庭电话，电子邮件等联系方式 ");
		sb0.append("	@use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->  ");
		sb0.append("	<!-- 患者电话或电子邮件(1..*) --> ");
		sb0.append("	<telecom use=\"H\" value=\"\"/>   ");
		sb0.append("	<telecom use=\"PUB\" value=\"\"/> ");
		sb0.append("	<telecom use=\"EMA\" value=\"\"/>  ");
		sb0.append("							<!--性别(1..1)-->");
		sb0.append("<administrativeGenderCode code=\"0\" displayName=\"未知\"/>");
		sb0.append("	<!--婚姻状况(0..1)-->");
		sb0.append("	<maritalStatusCode code=\"\"  displayName=\"\"/>  ");
		sb0.append("	<!-- 文化程度代码(0..1) -->  ");
		sb0.append("	<educationLevelCode code=\"\" displayName=\"\"/>   ");
		sb0.append("	<!--民族代码(1..*) -->   ");
		sb0.append("	<ethnicGroupCode code=\"\" displayName=\"汉族\"/>   ");
		sb0.append("	<!--国籍代码,见国家及地区字典(0..1) -->   ");
		sb0.append("	<citizenCode code=\"53\"   displayName=\"中国\"/> ");
		sb0.append("	<!--身份代码,见身份字典(0..1) -->   ");
		sb0.append("	<identityCode code=\"I\" displayName=\"其他\"/> ");
		sb0.append("	<!--出生日期(1..1)-->");
		sb0.append("	<birthTime value=\"\"/>");
		sb0.append("	<!-- 出生地(0..1)  -->   ");
		sb0.append("	<addr use=\"PUB\">   ");
		sb0.append("		<item> ");
		sb0.append("			<!--非结构化地址（完整地址描述） --> ");
		sb0.append("			<part type=\"SAL\" value=\"\"/>");
		sb0.append("			<!--地址-省（自治区、直辖市） -->");
		sb0.append("			<part type=\"STA\" value=\"\"/>   ");
		sb0.append("			<!--地址-市（地区） --> ");
		sb0.append("			<part type=\"CTY\" value=\"\"/>   ");
		sb0.append("			<!--地址-县（区） -->   ");
		sb0.append("			<part type=\"CNT\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-乡（镇、街道办事处） -->  ");
		sb0.append("			<part type=\"STB\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-村（街、路、弄等） -->");
		sb0.append("			<part type=\"STR\" value=\"\"/> ");
		sb0.append("			<!-- 地址-门牌号码 -->  ");
		sb0.append("			<part type=\"BNR\" value=\"\"/> ");
		sb0.append("			<!-- 邮政编码--> ");
		sb0.append("			<part type=\"ZIP\" value=\"\"/>");
		sb0.append("		</item>  ");
		sb0.append("	</addr>   ");
		sb0.append("	<!--职业类别代码(0..*)--> ");
		sb0.append("	<asEmployee classCode=\"EMP\">");
		sb0.append("		<!--职业编码系统名称/职业代码(0..*)-->");
		sb0.append("		<occupationCode code=\"\" displayName=\"\" />");
		sb0.append("		<employerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> ");
		sb0.append("			<!--机构识别号(0..*)-->");
		sb0.append("			<id>1289348683278175</id>  ");
		sb0.append("			<!--工作单位名称(0..*)-->  ");
		sb0.append("			<name value=\"\"/>  ");
		sb0.append("			<!-- 工作单位联系方式(0..*) -->");
		sb0.append("			<telecom use=\"WP\" value=\"\"/> ");
		sb0.append("			<!-- 工作单位地址(0..*) -->");
		sb0.append("			<addr use=\"WP\">  ");
		sb0.append("					<!--非结构化地址（完整地址描述） -->");
		sb0.append("	<part type=\"SAL\" value=\"\"/> ");
		sb0.append("	<!--地址-省（自治区、直辖市） -->   ");
		sb0.append("	<part type=\"STA\" value=\"\"/>   ");
		sb0.append("	<!--地址-市（地区） --> ");
		sb0.append("	<part type=\"CTY\" value=\"\"/>   ");
		sb0.append("	<!--地址-县（区） -->   ");
		sb0.append("	<part type=\"CNT\" value=\"\"/>   ");
		sb0.append("	<!-- 地址-乡（镇、街道办事处） -->  ");
		sb0.append("	<part type=\"STB\" value=\"\"/>   ");
		sb0.append("	<!-- 地址-村（街、路、弄等） -->");
		sb0.append("	<part type=\"STR\" value=\"\"/> ");
		sb0.append("	<!-- 地址-门牌号码 -->  ");
		sb0.append("	<part type=\"BNR\" value=\"\"/>");
		sb0.append("	<!-- 邮政编码-->");
		sb0.append("					<part type=\"ZIP\" value=\"\"/>   ");
		sb0.append("			</addr>   ");
		sb0.append("		</employerOrganization> ");
		sb0.append("	</asEmployee>   ");
		sb0.append("	<!--联系人(0..*)--> ");
		sb0.append("	<personalRelationship>  ");
		sb0.append("		<!-- 与患者关系 -->");
		sb0.append("		<code code=\"01\" displayName=\"\"/>  ");
		sb0.append("		<!--联系人基本信息(0..1)--> ");
		sb0.append("		<contactPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">   ");
		sb0.append("			<!-- 联系人姓名(0..*) -->  ");
		sb0.append("	<name value=\"LIST_EN\"/>  ");
		sb0.append("	<!-- 联系人电话或电子邮件(0..*) -->  ");
		sb0.append("	<telecom use=\"H\" value=\"\"/>");
		sb0.append("	<!-- 联系人地址(0..*) -->  ");
		sb0.append("	<addr use=\"WP\">");
		sb0.append("		<item> ");
		sb0.append("			<!--非结构化地址（完整地址描述） -->  ");
		sb0.append("			<part type=\"SAL\" value=\"\"/> ");
		sb0.append("	<!--地址-省（自治区、直辖市） --> ");
		sb0.append("	<part type=\"STA\" value=\"\"/> ");
		sb0.append("	<!--地址-市（地区） -->   ");
		sb0.append("	<part type=\"CTY\" value=\"\"/> ");
		sb0.append("	<!--地址-县（区） --> ");
		sb0.append("	<part type=\"CNT\" value=\"\"/> ");
		sb0.append("	<!-- 地址-乡（镇、街道办事处） -->");
		sb0.append("	<part type=\"STB\" value=\"\"/> ");
		sb0.append("			<!-- 地址-村（街、路、弄等） -->  ");
		sb0.append("			<part type=\"STR\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-门牌号码 -->");
		sb0.append("			<part type=\"BNR\" value=\"\"/>  ");
		sb0.append("			<!-- 邮政编码-->");
		sb0.append("			<part type=\"ZIP\" value=\"\"/> ");
		sb0.append("		</item> ");
		sb0.append("							</addr>   ");
		sb0.append("			</contactPerson>");
		sb0.append("		</personalRelationship>   ");
		sb0.append("	</patientPerson> ");
		sb0.append("	<!-- 建卡机构、医疗机构信息(1..1)  -->   ");
		sb0.append("			<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">   ");
		sb0.append("				<!--医疗机构编码-->");
		sb0.append("				<id extension=\"46600083-8\" root=\"2.16.156.10011.1.5\"/> ");
		sb0.append("	<!--医疗机构名称 --> ");
		sb0.append("	<name value=\"LIST_EN\"/>");
		sb0.append("	<!--医疗机构联系信息 -->   ");
		sb0.append("	<contactParty classCode=\"CON\"> ");
		sb0.append("		<!-- 联系人电话或电子邮件 -->  ");
		sb0.append("		<telecom use=\"H\" value=\"\"/>");
		sb0.append("		<!-- 联系人地址 -->");
		sb0.append("		<addr use=\"WP\"> ");
		sb0.append("		<item>");
		sb0.append("			<!--非结构化地址（完整地址描述） -->");
		sb0.append("			<part type=\"SAL\" value=\"\"/> ");
		sb0.append("			<!--地址-省（自治区、直辖市） -->   ");
		sb0.append("			<part type=\"STA\" value=\"\"/> ");
		sb0.append("			<!--地址-市（地区） -->   ");
		sb0.append("			<part type=\"CTY\" value=\"\"/> ");
		sb0.append("			<!--地址-县（区） -->");
		sb0.append("			<part type=\"CNT\" value=\"\"/>");
		sb0.append("			<!-- 地址-乡（镇、街道办事处） -->  ");
		sb0.append("			<part type=\"STB\" value=\"\"/>   ");
		sb0.append("			<!-- 地址-村（街、路、弄等） -->");
		sb0.append("			<part type=\"STR\" value=\"\"/> ");
		sb0.append("			<!-- 地址-门牌号码 -->  ");
		sb0.append("			<part type=\"BNR\" value=\"\"/>");
		sb0.append("			<!-- 邮政编码-->  ");
		sb0.append("			<part type=\"ZIP\" value=\"\"/>   ");
		sb0.append("		</item>   ");
		sb0.append("	</addr> ");
		sb0.append("				</contactParty>");
		sb0.append("			</providerOrganization>");

		sb0.append("<!-- 描述和患者相关的一些Observation信息，比如血型，过敏信息，身高，体重，年龄等 (0..*) -->");
		sb0.append("<subjectOf typeCode=\"OBS\">   ");
		sb0.append("	<!-- Observation详细信息(1..1) -->");
		sb0.append("	<observation classCode=\"OBS\" moodCode=\"EVN\"> ");
		sb0.append("		<!--observation类型编码(1..1)--> ");
		sb0.append("		<code code=\"01\" displayName=\"血型\"/>   ");
		sb0.append("					<!--observation的值(0..1)--> ");
		sb0.append("					<value code=\"\" displayName=\"\"/> ");
		sb0.append("					<!--该Observation的详细描述(0..1)-->   ");
		sb0.append("					<text/>");
		sb0.append("				</observation> ");
		sb0.append("			</subjectOf>   ");
		sb0.append("		</patient>");
		sb0.append("	</subject1>");
		sb0.append("				<!--操作者信息(0..1)-->");
		sb0.append("				<author code=\"\" displayName=\"\"/>");
		sb0.append("			</registrationRequest> ");
		sb0.append("		</subject>   ");
		sb0.append("	</controlActProcess> ");
		sb0.append("</PRPA_IN201311UV02>");
		TranLogTxt.liswriteEror_to_txt(logname, "新增结账挂号入参:" + sb0.toString());
		TranLogTxt.liswriteEror_to_txt(logname, "新增结账挂号入参Url:" + url);
		System.err.println(sb0.toString());
		String result = HttpUtil.doPost_Str(url, sb0.toString(), "utf-8");

		TranLogTxt.liswriteEror_to_txt(logname, "新增结账挂号返回:" + result + "\r\n");
	}

	/**
	 * 查询
	 * @param url 
	 * 
	 * @param cb
	 * @return
	 */
	private ResCustomBeanHK searchPatid(String url, ComAccBean cb, String acc_num, String logname) {
		ResCustomBeanHK rb1 = new ResCustomBeanHK();
		try {
			StringBuffer sb0 = new StringBuffer();
			sb0.append(
					"<PRPA_IN201305UV02 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
			sb0.append("  <id extension=\"" + UUID.randomUUID().toString().toLowerCase() + "\"/> ");
			sb0.append("  <creationTime value=\"" + DateTimeUtil.getDateTimes() + "\"/> ");
			sb0.append("  <interactionId  extension=\"S0004\"/>");
			sb0.append("  <receiver  code=\"SYS001\"/>");
			sb0.append("  <sender  code=\"SYS009\"/>");
			sb0.append("  <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">  ");
			sb0.append("  <queryByParameter> ");
			sb0.append("  <id>");
			sb0.append("  <item extension=\"" + cb.getAcc_num() + "\" root=\"2.16.156.10011.1.3\"/> ");
			sb0.append("  </id>   ");

			sb0.append("  <name value=\"" + cb.getCom_name() + "\" /> ");
			sb0.append("  <administrativeGenderCode code=\"" + "1" + "\" displayName=\"未知\"/> ");
			sb0.append("  <birthTime value=\"\" /> ");
			sb0.append("  <identityCode code=\"I\" displayName=\"其他\"/> ");

			sb0.append("  </queryByParameter>  ");
			sb0.append("  </controlActProcess>   ");
			sb0.append("</PRPA_IN201305UV02>	 ");
			TranLogTxt.liswriteEror_to_txt(logname, "查询结账的患者id入参:" + sb0.toString());
			TranLogTxt.liswriteEror_to_txt(logname, "查询结账的患者id_url:" + url);
			String result = HttpUtil.doPost_Str(url, sb0.toString(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "查询结账的患者id返回:" + result + "\r\n");
			if (result.trim().length() > 0) {
				try {
					InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
					Map<String, String> xmlMap = new HashMap<>();
					xmlMap.put("abc", "urn:hl7-org:v3");
					SAXReader sax = new SAXReader();

					sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
					Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
					rb1.setCode(
							document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode").getText());// 获取根节点;
					rb1.setFaly(document.selectNodes("abc:MCCI_IN000002UV01/abc:controlActProcess").size() == 0);

					String patid = document
							.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension").getText();

					// 如果"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension"
					// 为空 则去人员信息中拿患者id
					System.err.println("判断是否有人员信息节点:" + rb1.getFaly());
					System.err.println("判断是否为空串:" + patid.equals(""));
					if (patid.equals("")) {
						TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入1111");

						Document document2 = document.selectSingleNode("abc:MCCI_IN000002UV01/abc:controlActProcess")
								.getDocument();

						TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入2222");
						Element rootElement = document2.getRootElement();

						TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入3333:" + rootElement.getName());

						Element element = rootElement.element("controlActProcess");
						List<Element> subject1List = element.elements("subject");

						TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入4444");

						for (int i = 0; i < subject1List.size(); i++) {
							TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入5555");
							String name = subject1List.get(0).getName();
							TranLogTxt.liswriteEror_to_txt(logname,
									subject1List.get(0)
											.selectSingleNode(
													"abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension")
											.getText());
							TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入6666");

							rb1.setPersionid(subject1List.get(0)
									.selectSingleNode(
											"abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension")
									.getText());

							TranLogTxt.liswriteEror_to_txt(logname, "患者id为空进入6666");
							TranLogTxt.liswriteEror_to_txt(logname,
									"获取人员信息节点的患者id:" + subject1List.get(0)
											.selectSingleNode(
													"abc:registrationRequest/abc:subject1/abc:patient/abc:id/abc:item[@root='2.16.156.10011.0.2.2']/@extension")
											.getText() + "\r\n");
						}

						TranLogTxt.liswriteEror_to_txt(logname, "下面节点患者id:" + rb1.getPersionid() + "\r\n");
					} else {
						// 如果"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension"
						// 不为空 则直接拿此节点的 患者id
						rb1.setPersionid(
								document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/abc:id/@extension")
										.getText());
						TranLogTxt.liswriteEror_to_txt(logname, "上面节点患者id:" + rb1.getPersionid() + "\r\n");
					}

				} catch (Exception ex) {
					try {
						InputStream is = new ByteArrayInputStream(result.getBytes("utf-8"));
						Map<String, String> xmlMap = new HashMap<>();
						xmlMap.put("abc", "urn:hl7-org:v3");
						SAXReader sax = new SAXReader();
						sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
						Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
						rb1.setCode(document.selectSingleNode("abc:MCCI_IN000002UV01/abc:acknowledgement/@typeCode")
								.getText());// 获取根节点;
						rb1.setCodetext(document
								.selectSingleNode(
										"abc:MCCI_IN000002UV01/abc:acknowledgement/abc:acknowledgementDetail/abc:text/@value")
								.getText());
					} catch (Exception ext) {
						rb1.setCode("AE");
						rb1.setCodetext(ext.toString());
					}
				}
			} else {
				rb1.setCode("AE");
				rb1.setCodetext("");
			}
		} catch (Exception ex) {
			rb1.setCode("AE");
		}
		return rb1;
	}

	private void insert_search(ComAccBean cb, String accnum, String persionid, String logname) {
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();

			String insertsql = "insert into zl_req_term_his_item values ('" + cb.getBatch_num() + "','" + accnum
					+ "','','" + DateTimeUtil.getDateTime() + "','1','" + persionid + "','" + cb.getCom_name() + "')";
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + insertsql + "\r\n");
			tjtmpconnect.createStatement().executeUpdate(insertsql);
		} catch (Exception ex) {

		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
}
