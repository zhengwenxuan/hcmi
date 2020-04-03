package com.hjw.webService.client.hokai305;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.ZlReqItemDTO;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.hokai.bean.ResContralBeanHK;
import com.hjw.wst.DTO.ChargingItemDTO;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
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
public class LISDELSendMessageHK305 {
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
	public LISDELSendMessageHK305(LisMessageBody lismessage){
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
			
			HashSet<String> hashset = new HashSet<>();
			
			for (LisComponents comps : lismessage.getComponents()) {
					
					//查询lis申请单状态
					boolean flag =  getlisstatus(url,comps,logname);
					if(flag){
						ResultHeader rhone = lisSendMessage(url,comps,logname);
						if("AA".equals(rhone.getTypeCode())){
							hashset.add("AA");
							ApplyNOBean an = new ApplyNOBean();
							an.setApplyNO(comps.getReq_no());
							anList.add(an);
						}
					}else{
						 rb.getResultHeader().setTypeCode("AE");
						 rb.getResultHeader().setText("LIS状态不允许撤销!!");
					}
					
			}
			Iterator<String> it = hashset.iterator();
			if(hashset.size()==1){
				if(it.next().equals("AA")){
					
					rb.getResultHeader().setTypeCode("AA");
				}else{
					rb.getResultHeader().setTypeCode("AE");
				}
			}
			
			rb.getControlActProcess().setList(anList);
		} catch (Exception ex){
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		return rb;
	}

	private boolean getlisstatus(String url, LisComponents comps, String logname) {
		boolean falg=false;
		ExamInfoUserDTO eu = configService.getExamInfoForBarcode305(comps.getReq_no());
		List<ZlReqItemDTO> req_item_list = configService.select_zl_req_item(eu.getId(), comps.getReq_no(), logname);
		StringBuffer sb = new StringBuffer();
		
		sb.append("<QUMT_IN020030UV01 ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ");
		sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->                                                                                 ");
		sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>                                                                ");
		sb.append("    <!-- 消息创建时间(1..1) -->                                                                                             ");
		sb.append("    <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>                                                                                ");
		sb.append("    <!-- 服务编码，S0064代表检验状态查询(1..1)-->                                                                           ");
		sb.append("    <interactionId extension=\"S0064\"/>                                                                                    ");
		sb.append("    <!-- 接受者(1..1) -->                                                                                                   ");
		sb.append("    <receiver code=\"SYS003\"/>                                                                                       ");
		sb.append("    <!-- 发送者(1..1) -->                                                                                                   ");
		sb.append("    <sender code=\"SYS009\"/>                                                                                         ");
		sb.append("    <controlActProcess classCode=\"CACT\" moodCode=\"EVN\">                                                                 ");
		sb.append("        <queryByParameter>                                                                                                  ");
		sb.append("            <!--申请单状态，见检验状态字典(0..1)-->                                                                         ");
		sb.append("            <statusCode code=\"\" value=\"\"/>                                                                         ");
		sb.append("            <id>                                                                                                            ");
		sb.append("                <!--电子申请单编号(0..1)-->                                                                                 ");
		sb.append("                <item extension=\""+req_item_list.get(0).getReq_id()+"\" root=\"2.16.156.10011.1.24\"/>                                              ");
		sb.append("            </id>                                                                                                           ");
		sb.append("        </queryByParameter>                                                                                                 ");
		sb.append("    </controlActProcess>                                                                                                    ");
		sb.append("</QUMT_IN020030UV01>                                                                                                        ");
		
		TranLogTxt.liswriteEror_to_txt(logname, "查询lis状态req:" + sb.toString() + "\r\n");
		
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
		wcd = webserviceConfigurationService.getWebServiceConfig("CUST_APPLICATION");
		
		String lisStatusurl = wcd.getConfig_url().trim();
		String result = HttpUtil.doPost_Xml(lisStatusurl,sb.toString(), "utf-8");
		
		TranLogTxt.liswriteEror_to_txt(logname, "查询lis状态返回:" + result + "\r\n");
		
		ResultHeader rhone = ResContralBeanHK.getLisReqStatus305(result);	
		if(rhone.getTypeCode().equals("AA")){
			TranLogTxt.liswriteEror_to_txt(logname, "查询lis状态返回状态:" + rhone.getText() + "\r\n");
			TranLogTxt.liswriteEror_to_txt(logname, "查询lis状态返回判断:" + rhone.getText().equals("8") + "\r\n");
			if(rhone.getText().equals("8")){
				falg=false;
			}else if(rhone.getText().equals("9")){
				falg=false;
			}else if(rhone.getText().equals("0")){
				falg=false;
			}else{
				falg=true;
			}
		}
		TranLogTxt.liswriteEror_to_txt(logname, "查询lis状态falg:" + falg + "\r\n");
		return falg;
	}
	private ResultHeader lisSendMessage(String url,LisComponents comps, String logname) {
		ResultHeader rhone= new ResultHeader();
		try {
			ApplyNOBean an = new ApplyNOBean();
			ExamInfoUserDTO eu = configService.getExamInfoForBarcode305(comps.getReq_no());
			List<ZlReqItemDTO> req_item_list = configService.select_zl_req_item(eu.getId(), comps.getReq_no(), logname);
			for(ZlReqItemDTO req_item : req_item_list) {
				StringBuffer sb = new StringBuffer("");
				
				sb.append("<POOR_IN200901UV ITSVersion=\"XML_1.0\" xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
				sb.append("    <!-- 消息ID(系统生成唯一UUID)(1..1) -->");
				sb.append("    <id extension=\""+UUID.randomUUID().toString().toLowerCase()+"\"/>");
				sb.append("    <!-- 消息创建时间(1..1) -->");
				sb.append("    <creationTime value=\""+DateTimeUtil.getDateTimes()+"\"/>");
				sb.append("    <!-- 服务编码，S0038代表检验申请新增(1..1)-->");
				sb.append("    <interactionId extension=\"S0038\"/>");
				sb.append("    <!-- 接受者(1..1) -->");
				sb.append("    <receiver code=\"SYS003\"/>");
				sb.append("    <!-- 发送者(1..1) -->");
				sb.append("    <sender code=\"SYS009\"/>");
				sb.append("    <controlActProcess classCode=\"ACTN\" moodCode=\"EVN\">");
				sb.append("        <!-- 状态代码（create、update、delete）(1..1) -->");
				sb.append("        <code value=\"delete\"/>");
				sb.append("        <subject typeCode=\"SUBJ\">");
				sb.append("            <observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
				sb.append("                <id>");
				sb.append("                    <!--电子申请单编号(1..1)-->");
				sb.append("                    <item extension=\""+req_item.getReq_id()+"\" root=\"2.16.156.10011.1.24\"/>");
				sb.append("                </id>");
				sb.append("                <!--申请单描述(0..1)-->");
				sb.append("                <text value=\"申请单描述\"/>");
				sb.append("                <!--申请单状态，见申请单状态字典(1..1)-->");
				sb.append("                <statusCode code=\"0\" value=\"开立\"/>");
				sb.append("                <!--申请时间(0..1)-->");
				sb.append("                <effectiveTime value=\""+lismessage.getCreationTime_value()+"\"/>");
				sb.append("                <!--优先级别(0..1)-->");
				sb.append("                <priority code=\"1\" displayName=\"常规\"/>");
				sb.append("                <!--费用类别 (0..1)-->");
				sb.append("                <chargeCode code=\"1\" displayName=\"自费\"/>");
				sb.append("                <!--注意事项(0..1) -->");
				sb.append("                <annotationText value=\"注意XXX\"/>");
				sb.append("                <!--开单医生/送检医生(1..1) -->");
				sb.append("                <author typeCode=\"AUT\">");
				sb.append("                    <!--开单时间(0..1)-->");
				sb.append("                    <time value=\""+DateTimeUtil.getDateTimes()+"\"/>");
				sb.append("                    <!--开单者签名编码/名称-CA(0..1)-->");
				sb.append("                    <signatureCode code=\"S\" value=\""+lismessage.getDoctor().getDoctorName()+"\"/>");
				sb.append("                    <assignedEntity classCode=\"ASSIGNED\">");
				sb.append("                        <!--开立者 ID(0..1)-->");
				sb.append("                        <id extension=\""+lismessage.getDoctor().getDoctorCode()+"\" root=\"2.16.156.10011.1.4\"/>");
				sb.append("                        <!--开立者姓名(0..1)-->");
				sb.append("                        <name value=\""+lismessage.getDoctor().getDoctorName()+"\"/>");
				sb.append("                        <!-- 申请科室信息(0..1) -->");
				sb.append("                        <representedOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">");
				sb.append("                            <!--医疗卫生机构（科室） ID(0..1)-->");
				sb.append("                            <id extension=\"\" root=\"2.16.156.10011.1.26\"/>");
				sb.append("                            <!--开立科室(0..1)-->");
				sb.append("                            <name value=\"体检\"/>");
				sb.append("                        </representedOrganization>");
				sb.append("                    </assignedEntity>");
				sb.append("                </author>");
				sb.append("                <!-- 标本信息(0..1) -->");
				sb.append("                <specimen>");
				sb.append("                    <!--标本ID/或者条码ID(0..1)-->");
				sb.append("                    <id extension=\""+req_item.getLis_req_code()+"\"/>");
				sb.append("                    <!--标本类别代码(0..1)-->");
				sb.append("                    <code code=\"\" displayName=\"\"/>");
				sb.append("                    <!--标本描述(0..1)-->");
				sb.append("                    <text value=\"描述\"/>");
				sb.append("                </specimen>");
				sb.append("                <!-- 容器类型编码/名称(0..1) -->");
				sb.append("                <participant code=\"容器类型编码\" displayName=\"容器类型名称\"/>");
				sb.append("                <!-- 多个检验项目循环component2(1..1) -->");
				int count=1;
				for (LisComponent comp : comps.getItemList()) {	
					sb.append("                <component2>");
					sb.append("                    <observationRequest classCode=\"OBS\" moodCode=\"RQO\">");
					sb.append("                        <id>");
					sb.append("                            <!--项目ID(0..1)  comp.getChargingItemid()-->");
					sb.append("                            <item extension=\""+ count++ +"\" root=\"2.16.156.10011.1.28\"/>");
					sb.append("                        </id>");
					sb.append("                        <!--检验项目编码/名称 (1..1)-->");
					sb.append("                        <code code=\""+comps.getItemList().get(0).getHis_num()+"\" displayName=\""+comps.getItemList().get(0).getItemName()+"\"/>");
					sb.append("                        <!--正常时为active，否则为disable(0..1)-->");
					sb.append("                        <statusCode code=\"active\"/>");
					sb.append("                        <!--检验方法编码/名称(0..1) -->");
					sb.append("                        <methodCode code=\"\" displayName=\"检验方法描述\"/>");
					sb.append("                        <!--执行科室(0..1) -->");
					sb.append("                        <location code=\""+comps.getItemList().get(0).getServiceDeliveryLocation_code()+"\" displayName=\""+comps.getItemList().get(0).getServiceDeliveryLocation_name()+"\"/>");
					sb.append("                        <!--价格(0..1)-->");
					sb.append("                        <price value=\"\"/>");
					sb.append("                    </observationRequest>");
					sb.append("                </component2>");
			
			
				}
				
				sb.append("                <!--就诊信息(0..1) -->");
				sb.append("                <componentOf1 contextConductionInd=\"false\" typeCode=\"COMP\">");
				sb.append("                    <encounter classCode=\"ENC\" moodCode=\"EVN\">");
				sb.append("                        <id>");
				sb.append("                            <!-- 就诊次数(0..1) -->");
				sb.append("                            <item extension=\"\" root=\"1.2.156.112635.1.2.1.7\"/>");
				sb.append("                            <!-- 就诊流水号(1..1) -->");
				sb.append("                            <item extension=\"\" root=\"1.2.156.112635.1.2.1.6\"/>");
				sb.append("                        </id>");
				sb.append("                        <!--就诊类别代码 1.门诊 2.住院 3.体检 4.急诊 9.其他 (1..1)-->");
				sb.append("                        <code code=\"1\" displayName=\"门诊\"/>");
				sb.append("                        <!--费用类别 (0..1)-->");
				sb.append("                        <chargeCode code=\"1\" displayName=\"自费\"/>");
				sb.append("                        <!--就诊日期时间 (1..1)-->");
				sb.append("                        <effectiveTime value=\""+lismessage.getCreationTime_value()+"\"/>");
				sb.append("                        <!--病人(0..1) -->");
				sb.append("                        <patient classCode=\"PAT\">");
				sb.append("                            <id>");
				sb.append("                                <!--急诊号标识(0..1) -->");
				sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.10\"/>");
				sb.append("                                <!--门诊号标识(0..1) -->");
				sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.11\"/>");
				sb.append("                                <!--住院号标识(0..1)-->");
				sb.append("                                <item extension=\"\" root=\"2.16.156.10011.1.12\"/>");
				sb.append("                                <!--患者 ID 标识(0..1)-->");
				sb.append("                                <item extension=\""+lismessage.getCustom().getPersonid()+"\" root=\"2.16.156.10011.0.2.2\"/>");
				sb.append("                            </id>");
				sb.append("                            <!--患者当前就诊状态，见就诊状态字典(0..1)-->");
				sb.append("                            <statusCode code=\"1\" value=\"挂号\"/>");
				sb.append("                            <!--个人信息 必须项已使用(0..1) -->");
				sb.append("                            <patientPerson classCode=\"PSN\">");
				sb.append("                                <!-- 身份证号/医保卡号(0..1) -->");
				sb.append("                                <id>");
				sb.append("                                    <!-- 身份证号(0..1) -->");
				sb.append("                                    <item extension=\""+eu.getId_num()+"\" root=\"2.16.156.10011.1.3\"/>");
				sb.append("                                    <!-- 医保卡号(0..1) -->");
				sb.append("                                    <item extension=\"\" root=\"2.16.156.10011.1.15\"/>");
				sb.append("                                </id>");
				sb.append("                                <!--患者姓名(0..1)-->");
				sb.append("                                <name value=\""+eu.getUser_name()+"\"/>");
				sb.append("                                <!--性别(0..1)-->");
				sb.append("                                <administrativeGenderCode code=\""+lismessage.getCustom().getSexcode()+"\" displayName=\""+eu.getSex()+"性\"/>");
				sb.append("                                <!--出生日期(0..1)-->");
				sb.append("                                <birthTime value=\""+lismessage.getCustom().getBirthtime()+"\"/>");
				sb.append("                                <!--年龄(0..1)-->");
				sb.append("                                <age units=\"岁\" value=\""+lismessage.getCustom().getOld()+"\"/>");
				sb.append("                                <!-- 家庭电话，电子邮件等联系方式");
				sb.append("                                    @use: 联系方式类型。PUB为联系电话，H为家庭电话,EMA为邮箱 -->");
				sb.append("                                <!-- 患者电话或电子邮件(1..*) -->");
				sb.append("                                <telecom use=\"H\" value=\""+eu.getPhone()+"\"/>");
				sb.append("                                <telecom use=\"PUB\" value=\""+eu.getPhone()+"\"/>");
				sb.append("                                <telecom use=\"EMA\" value=\""+eu.getEmail()+"\"/>");
				sb.append("                            </patientPerson>");
				sb.append("                        </patient>");
				sb.append("                        <!--住院位置-住院有此节点，其他可无此节点(0..1)-->");
				sb.append("                        <location typeCode=\"LOC\">");
				sb.append("                            <!--@root类别， @extension:病床号 @displayName:病床名称-->");
				sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.22\"/>");
				sb.append("                            <!--@root类别， @extension:病房编码 @displayName:病房名称-->");
				sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.21\"/>");
				sb.append("                            <!--@root类别， @extension:科室编码 @displayName:科室名称-->");
				sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.26\"/>");
				sb.append("                            <!--@root类别， @extension:病区编码 @displayName:病区名称-->");
				sb.append("                            <item displayName=\"\" extension=\"\" root=\"2.16.156.10011.1.27\"/>");
				sb.append("                        </location>");
				sb.append("                        <!--诊断(检查申请原因)(0..*) -->");
				sb.append("                        <pertinentInformation1 typeCode=\"PERT\">");
				sb.append("                            <observationDx classCode=\"OBS\" moodCode=\"EVN\">");
				sb.append("                                <!--诊断类别编码/名称(0..1) -->");
				sb.append("                                <code code=\"\" displayName=\"\"/>");
				sb.append("                                <!--诊断代码及描述 (0..1)-->");
				sb.append("                                <value code=\"1\" displayName=\"\"/>");
				sb.append("                                <!--建议描述 (0..1)-->");
				sb.append("                                <suggestionText/>");
				sb.append("                                <!--诊断时间(0..1) -->");
				sb.append("                                <effectiveTime value=\"\"/>");
				sb.append("                                <!--诊断医生工号/姓名 (0..1)-->");
				sb.append("                                <author code=\"\" displayName=\"\"/>");
				sb.append("                            </observationDx>");
				sb.append("                        </pertinentInformation1>");
				sb.append("                    </encounter>");
				sb.append("                </componentOf1>");
				sb.append("            </observationRequest>");
				sb.append("        </subject>");
				sb.append("    </controlActProcess>");
				sb.append("</POOR_IN200901UV>");
				
				TranLogTxt.liswriteEror_to_txt(logname, "req:" + sb.toString() + "\r\n");
				String result = HttpUtil.doPost_Xml(url,sb.toString(), "utf-8");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();				
					rhone = ResContralBeanHK.getRes(result);				
				}
			}
		}catch(Exception ex){
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
