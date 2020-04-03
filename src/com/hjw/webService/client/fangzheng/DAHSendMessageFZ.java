package com.hjw.webService.client.fangzheng;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.DAHCustomerBean;
import com.hjw.webService.client.body.DAHResBody;
import com.hjw.webService.client.fangzheng.client.CreatePersonService;
import com.hjw.webService.client.fangzheng.client.CreatePersonServiceLocator;
import com.hjw.webService.client.fangzheng.client.ICreatePersonService;
import com.synjones.framework.persistence.JdbcQueryManager;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DAHSendMessageFZ{
	  private static JdbcQueryManager jdbcQueryManager;
	  private static ConfigService configService;
	    static{
	    	init();
	    	}
		public static void init(){
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
			configService = (ConfigService) wac.getBean("configService");
		}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public DAHResBody getMessage(String url, DAHCustomerBean eu, String dah, String logname) {
		String reqString = this.getSendMessage(eu);
		TranLogTxt.liswriteEror_to_txt(logname, "req: 操作语句： " + reqString);
		DAHResBody rb = new DAHResBody();
		try {
			CreatePersonService cps = new CreatePersonServiceLocator(url);
			ICreatePersonService is = cps.getCreatePersonServicePort();
			String result = is.createPerson(reqString);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if ((result != null) && (result.trim().length() > 0)) {
				rb.setRescode("ok");
				rb.setIdnumber(eu.getPersionId());
			} else {
				rb.setRescode("error");
				rb.setRestext(url + " 返回无返回");
			}
		} catch (Exception ex) {
			com.hjw.interfaces.util.StringUtil.formatException(ex);
			rb.setRescode("error");
			rb.setIdnumber("");
		}
		return rb;
	}

	private String getPersonId(String xmlmessage) throws Exception {
		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
		Map<String, String> xmlMap = new HashMap<>();
		xmlMap.put("abc", "urn:hl7-org:v3");
		SAXReader sax = new SAXReader();
		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		String persionId=document.selectSingleNode("abc:PRPA_IN201306UV02/controlActProcess/subject/registrationEvent/subject1/patient/id/item[@root='1.2.156.112606.1.2.1.3']/@extension").getText();// 获取根节点;
		return persionId;
	}
	
	/**
	 * 
	 * @param exam_num
	 * @param logname
	 * @return
	 */
	public String getchargtypename(String chargingtypeid,String logname){
		Connection tjtmpconnect = null;
		String chargtypename= "自费";		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.data_name as chargname from data_dictionary a where a.id='"+chargingtypeid+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				chargtypename=(rs1.getString("chargname"));
			}
           rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return chargtypename;
	} 	
	
	public String getcustomtypename(String customtypeid,String logname){
		Connection tjtmpconnect = null;
		String customtypename= "地方";
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select c.type_name as custname from customer_type c where c.id='"+customtypeid+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				customtypename=(rs1.getString("custname"));
			}
           rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +com.hjw.interfaces.util.StringUtil.formatException(ex));			
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return customtypename;
	} 	
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public String getNation(String nationid,String logname){
		Connection tjtmpconnect = null;
		String nationname="";
		if((nationid==null)||(nationid.trim().length()<=0)){
			nationname="汉族";
		}
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select data_name from data_dictionary where id='"+nationid+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				nationname=rs1.getString("data_name");
			}else{
				nationname="汉族";
			}
            rs1.close();
		} catch (SQLException ex) {
			nationname="汉族";
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
	
	private String getSendMessage(DAHCustomerBean eu) {
		StringBuffer sb = new StringBuffer("");
        
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<PRPA_IN201305UV02 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../../schemas/PRPA_IN201305UV02.xsd\" xmlns=\"urn:hl7-org:v3\" ITSVersion=\"XML_1.0\">");
			//<!-- 消息ID -->
		sb.append("<id root=\"\"/>");
			//<!-- 消息创建时间 -->
		sb.append("<creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>");
			//<!-- 交互ID(HL7交互类型代码系统) -->
		sb.append("<interactionId root=\"2.16.840.1.113883.1.6\" extension=\"PRPA_IN201305UV02\"/>");
			//<!-- 消息发送系统的状态: P(Production); D(Debugging); T(Training) -->
		sb.append("<processingCode code=\"P\"/>");
			//<!-- 消息处理模式: A(Archive); I(Initial load); R(Restore from archive); T(Current processing) -->
		sb.append("<processingModeCode code=\"R\"/>");
			//<!-- 消息应答: AL(Always); ER(Error/reject only); NE(Never) -->
		sb.append("<acceptAckCode/>");
			//<!-- 接受者 -->
		sb.append("<receiver typeCode=\"RCV\">");
			//<!-- 接收设备/应用 -->
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
			//<!-- 接收者ID(接收设备/应用ID) -->
		sb.append("<id>");
		sb.append("<item root=\"1\" extension=\"\" />");//接受机构
		sb.append("</id>");
		sb.append("</device>");
		sb.append("</receiver>");		
		    //<!-- 发送者 -->
		sb.append("<sender typeCode=\"SND\">");
			//<!-- 发送设备/应用 -->
		sb.append("<device classCode=\"DEV\" determinerCode=\"INSTANCE\">");
			//<!-- 发送者ID(发送设备/应用ID) -->
		sb.append("<id>");
		sb.append("<item root=\"2\" extension=\"\" />");//发送机构
		sb.append("</id>");
		sb.append("</device>");
		sb.append("</sender>");	

			//<!-- 封装的消息内容 - Trigger Event Control Act wrapper -->
		sb.append("<controlActProcess classCode=\"CACT\" moodCode=\"EVN\">");
		sb.append("<subject typeCode=\"SUBJ\">");
		sb.append("<registrationRequest classCode=\"REG\" moodCode=\"RQO\">");
		sb.append("<statusCode/>");
		sb.append("<subject1 typeCode=\"SBJ\">");
		sb.append("<patient classCode=\"PAT\">");
		sb.append("<id>");
		sb.append("<item extension=\"E106598941\" root=\"1.2.156.112685.1.2.1.1\" identifierName=\"患者EMPI标识\"/>");//患者EMPI标识
		sb.append("<item extension=\"201809890\" root=\"1.2.156.112685.1.2.1.13\" identifierName=\"住院号\"/>");//住院号
		sb.append("<item extension=\"4429856\" root=\"1.2.156.112685.1.2.1.12\" identifierName=\"门诊号\"/>");//门诊号
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.11\" identifierName=\"医保号\"/>");//医保号
		sb.append("<item extension=\"01\" root=\"1.2.156.112685.1.2.1.2\" identifierName=\"域ID(门诊、住院)\"/>");//域ID(门诊、住院)
		sb.append("<item extension=\"4429856\" root=\"1.2.156.112685.1.2.1.3\" identifierName=\"患者ID\"/>");//患者ID
		sb.append("</id>");
		
		sb.append("<addr xsi:type=\"BAG_AD\">");
		
			//<!--通用联系地址（如果只有一个地址:保留type="AL"项目，删除type="CTY"项目；如果有两个地址:参考如下形式） -->
			//<!-- 现住地址 -->
		sb.append("<item use=\"TMP\">");
		sb.append("<part type=\"CTY\" value=\"\" code=\"\" />");
		sb.append("<part type=\"AL\" value=\""+eu.getAddress()+"\"/>");
			//<!--邮政编码 -->
		sb.append("<part type=\"ZIP\" value=\"\" />");//"邮政编码
		sb.append("</item>");//
			//<!-- 家庭住址 -->
		sb.append("<item use=\"H\">");
		sb.append("<part type=\"CTY\" value=\"\" code=\"\" />");
		sb.append("<part type=\"AL\" value=\"\" />");
			//<!--邮政编码 -->
		sb.append("<part type=\"ZIP\" value=\"\" />");//邮政编码
		sb.append("</item>");
			//<!-- 户籍地址 -->
		sb.append("<item use=\"PUB\">");
		sb.append("<part type=\"CTY\" value=\"\" code=\"\" />");
		sb.append("<part type=\"AL\" value=\""+eu.getAddress()+"\" />");
			//<!--邮政编码 -->
		sb.append("<part type=\"ZIP\" value=\"\" />");
		sb.append("</item>");		
		sb.append("</addr>");
				
		/*<!-- 家庭电话，电子邮件等联系方式
		@use: 联系方式类型。PUB为联系电话，H为家庭电话
		value: 前缀为区分电话还是电子邮件地址。电话无前缀，电子邮件前缀为mailto
		电话默认值为空，电子邮件默认值为mailto:default-->*/
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		sb.append("<item use=\"PUB\" value=\""+eu.getPhone()+"\"/>");
		sb.append("<item use=\"H\" value=\"\"/>");
		sb.append("<item use=\"PUB\" value=\"\"/>");
		sb.append("</telecom>");
		sb.append("<statusCode code=\"active\"/>");
		sb.append("<patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">");
			/*<!-- 身份证号及各种证件号
			@extension: 证件号码
			@root: 证件OID
			@controlInformationExtension: 证件名称 -->*/
		sb.append("<id>");
		sb.append("<item extension=\""+eu.getId_num()+"\" root=\"1.2.156.112685.1.2.1.9\" controlInformationExtension=\"身份证号\"/>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.14\" controlInformationExtension=\"居民户口簿\"/>");//
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.15\" controlInformationExtension=\"护照\"/>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.16\" controlInformationExtension=\"军官证\"/>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.17\" controlInformationExtension=\"驾驶证\"/>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.18\" controlInformationExtension=\"港澳居民来往内地通行证\"/>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.19\" controlInformationExtension=\"台湾居民来往内地通行证\"/>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.2.1.20\" controlInformationExtension=\"其他法定有效证件\"/>");
		sb.append("</id>");
		//<!--姓名 必须项已使用-->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\""+eu.getName()+"\"/>");
		sb.append("</item>");
		sb.append("</name>");
		//<!-- 性别代码 -->
		sb.append("<administrativeGenderCode code=\""+eu.getSexcode()+"\" codeSystem=\"1.2.156.112685.1.1.3\" codeSystemName=\"性别\"/>");
		//<!-- 出生日期 -->
				Date bridd = DateTimeUtil.parse0(eu.getBrid());
				String brids = DateTimeUtil.shortFmt4(bridd);
		sb.append("<birthTime value=\""+brids+"\"/>");
		//<!-- 婚姻状况类别代码 -->
		sb.append("<maritalStatusCode code=\"\" codeSystem=\"1.2.156.112685.1.1.4\" codeSystemName=\"婚姻状况\"/>");
		//<!-- 文化程度代码 -->
		sb.append("<educationLevelCode code=\"\" codeSystem=\"1.2.156.112685.1.1.8\" codeSystemName=\"文化程度\"/>");
		//<!--民族代码 -->
		sb.append("<ethnicGroupCode>");
		sb.append("<item code=\"\" codeSystem=\"1.2.156.112685.1.1.5\" codeSystemName=\"民族\"/>");
		sb.append("</ethnicGroupCode>");
		//<!--雇佣关系 -->
		sb.append("<asEmployee classCode=\"EMP\">");
			//<!-- 工作单位地址 -->
		sb.append("<addr xsi:type=\"BAG_AD\">");
				//<!--通用联系地址（如果只有一个地址:保留type="AL"项目，删除type="CTY"项目；如果有两个地址:参考如下形式）
				//@use: WP代表工作地址 -->
		sb.append("<item use=\"WP\">");
		sb.append("<part type=\"CTY\" value=\"\" code=\"\" />");
		sb.append("<part type=\"AL\" value=\"\" />");
		//<!--邮政编码 -->
		sb.append("<part type=\"ZIP\" value=\"\" />");
		sb.append("</item>");
		sb.append("</addr>");
			//<!-- 工作单位联系方式 -->
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		sb.append("<item use=\"WP\" value=\"\"/>");
		sb.append("</telecom>");
			//<!--职业编码系统名称/职业代码 -->
		sb.append("<occupationCode code=\"\" codeSystem=\"1.2.156.112685.1.1.7\" codeSystemName=\"\"/>");
			//<!-- 必须项未使用 -->
			//<!--工作单位 -->
		sb.append("<employerOrganization determinerCode=\"INSTANCE\" classCode=\"ORG\">");
				//<!--工作单位名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"\"/>");
		sb.append("</item>");
		sb.append("</name>");
				//<!-- 必须项未使用 -->
		sb.append("<contactParty classCode=\"CON\" xsi:nil=\"true\"/>");
		sb.append("</employerOrganization>");
		sb.append("</asEmployee>");
		sb.append("<asCitizen classCode=\"CIT\">");
		//<!--所属国家 必须项已使用-->
		sb.append("<politicalNation classCode=\"NAT\" determinerCode=\"INSTANCE\">");
		sb.append("<code code=\"\" codeSystem=\"1.2.156.112685.1.1.6\" codeSystemName=\"国籍\"/>");
			//<!-- 国家名称 -->
		sb.append("<name>");
		sb.append("<part value=\"\"/>");
		sb.append("</name>");
		sb.append("</politicalNation>");
		sb.append("</asCitizen>");
	     //<!-- 联系人 -->
		sb.append("<contactParty classCode=\"CON\">");
		//<!-- 与患者关系 -->
		sb.append("<id>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.1.18\" identifierName=\"与患者关系\"/>");
		sb.append("</id>");
		//<!-- 联系人地址 -->
		sb.append("<addr xsi:type=\"BAG_AD\">");
		//	<!--通用联系地址（如果只有一个地址:保留type="AL"项目，删除type="CTY"项目；如果有两个地址:参考如下形式） -->
		sb.append("<item use=\"H\">");
		sb.append("<part type=\"CTY\" value=\"\" code=\"\" />");
		sb.append("<part type=\"AL\" value=\"\" />");
				//<!--邮政编码 -->
		sb.append("<part type=\"ZIP\" value=\"\" />");
		sb.append("</item>");
		sb.append("</addr>");
		//<!-- 联系人电话或电子邮件 -->
		sb.append("<telecom xsi:type=\"BAG_TEL\">");
		sb.append("<item use=\"H\" value=\"\"/>");
		sb.append("</telecom>");
		sb.append("<contactPerson>");
			//<!-- 联系人姓名 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item><part value=\"\" /></item>");
		sb.append("</name>");
		sb.append("</contactPerson>");
		sb.append("</contactParty>");
	     //<!-- 出生地 -->
		sb.append("<birthPlace classCode=\"\">");
		sb.append("<addr use=\"\">");
		sb.append("<part type=\"AL\" value=\"\" />");
			//<!--邮政编码 -->
		sb.append("<part type=\"ZIP\" value=\"\" />");
		sb.append("</addr>");
		sb.append("</birthPlace>");
		sb.append("</patientPerson>");
        //<!-- 医疗机构信息  -->
		sb.append("<providerOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
	    //<!--医疗机构编码-->
		sb.append("<id>");
		sb.append("<item extension=\"\"/>");
		sb.append("</id>");
	    //<!--医疗机构名称 -->
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item>");
		sb.append("<part value=\"\" />");
		sb.append("</item>");
		sb.append("</name>");
		sb.append("<contactParty classCode=\"CON\"/>");
		sb.append("</providerOrganization>");
		sb.append("</patient>");
		sb.append("</subject1>");
        //<!--作者-->
		sb.append("<author typeCode=\"AUT\">");
		sb.append("<assignedEntity classCode=\"ASSIGNED\">");
		sb.append("<id>");
		sb.append("<item extension=\"\" root=\"1.2.156.112685.1.1.2\" identifierName=\"用户名\" />");
		sb.append("</id>");
		sb.append("<assignedPerson determinerCode=\"INSTANCE\" classCode=\"PSN\">");
		sb.append("<name xsi:type=\"BAG_EN\">");
		sb.append("<item><part value=\"\"/></item>");
		sb.append("</name>");
		sb.append("</assignedPerson>");
		sb.append("</assignedEntity>");
		sb.append("</author>");
		sb.append("</registrationRequest>");
		sb.append("</subject>");
		sb.append("</controlActProcess>");
		sb.append("</PRPA_IN201305UV02>");
		
		return sb.toString();
	}
}
