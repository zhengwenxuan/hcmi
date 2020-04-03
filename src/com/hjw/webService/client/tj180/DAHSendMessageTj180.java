package com.hjw.webService.client.tj180;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.DAHCustomerBean;
import com.hjw.webService.client.body.DAHResBody;
import com.hjw.webService.client.tj180.Bean.CustDAHBean;
import com.hjw.webService.client.tj180.Bean.DAHBean;
import com.hjw.webService.client.tj180.Bean.ResDAHBean;
import com.hjw.wst.DTO.ExamInfoUserDTO;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  2.6	检查申请信息服务
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class DAHSendMessageTj180{
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
	public DAHResBody getMessage(String url,DAHCustomerBean eu,String dah,String logname) {
		JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		WebserviceConfigurationService webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
		DAHResBody rb = new DAHResBody();
		String result="";
		try {
			CustDAHBean cu= new CustDAHBean();	
			
			if((eu.getName()!=null)&&(eu.getName().trim().length()>0)){
				eu.setName(StringUtil.subTextString(eu.getName().trim(),8));
			}else{
				eu.setName("");
			}			
			
			if((eu.getAddress()!=null)&&(eu.getAddress().trim().length()>0)){
				eu.setAddress(StringUtil.subTextString(eu.getAddress().trim(),38));
			}else if((eu.getAddress()!=null)&&(eu.getAddress().trim().length()==0)){
				eu.setAddress("");
			}			
			cu.setAddress(eu.getAddress());
			
			if((eu.getBrid()!=null)&&(eu.getBrid().trim().length()>10)){
				cu.setCustomerBirthday(eu.getBrid().substring(0, 10));
			}else{
				cu.setCustomerBirthday(eu.getBrid());
			}
			
			if(eu.getId_num().trim().length()==18){
				cu.setCustomerBirthPlace(eu.getId_num().trim().substring(0, 6));
			}
			cu.setCustomerIdentityNo(eu.getId_num());
			cu.setCustomerName(eu.getName());
			cu.setCustomerSex(eu.getSex());
			cu.setOperator(getUserWorkNum(eu.getUserId(),logname));
			cu.setCustomerNation(getNation(eu.getNation(),logname));
			cu.setPhone(eu.getPhone());
			cu.setCustomerChargeType(getchargtypename(eu.getCustomerChargeType(),logname));
			cu.setUnitInContract(eu.getUnitInContract());
			cu.setCustomerIdentity(getcustomtypename(eu.getCustomerIdentity(),logname));
			cu.setCustomerPatientId(dah);
			cu.setOrganizationId(eu.getOrganizationId());
			cu.setCustomerSSid(eu.getCustomerSSid());			
			cu.setVocation(eu.getVocation());
			cu.setEducation(eu.getEducation());
			cu.setMarriedAge(eu.getMarriedAge());
			cu.setInfoSource("0");
			cu.setSubOrganization(eu.getSubOrganization());
			cu.setGroupName(eu.getGroupName());
			if(eu.getWebbed()==null||eu.getWebbed().trim().length()<=0){
				cu=this.getCustom(dah, cu, logname);
			} else {
				if (eu.getWebbed().indexOf("已") >= 0) {
					cu.setWebbed("1");
				} else if (eu.getWebbed().indexOf("未") >= 0) {
					cu.setWebbed("0");
				} else {
					cu.setWebbed("2");
				}
			}			
			
			json = JSONObject.fromObject(cu);// 将java对象转换为json对象
			str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("DAH_SEND");
			String web_url = wcd.getConfig_url().trim();
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + web_url);
			result = HttpUtil.doPost(web_url,cu,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				JSONObject jsonobject = JSONObject.fromObject(result);
				Map classMap = new HashMap();
				classMap.put("customerInfo", DAHBean.class);
				ResDAHBean resdah = new ResDAHBean();
				resdah = (ResDAHBean) JSONObject.toBean(jsonobject, ResDAHBean.class, classMap);				
				if ("200".equals(resdah.getStatus())) {
					rb.setRescode("ok");
					rb.setIdnumber(resdah.getCustomerInfo().get(0).getCustomerPatientId());
				}else{
					rb.setRescode("error");
					rb.setRestext(resdah.getErrorinfo());
				}
			}else{
	                rb.setRescode("error");
	                rb.setRestext(url  +" 返回无返回");
	                }

		} catch (Exception ex) {
			//ex.printStackTrace();
			rb.setRescode("error");
			rb.setIdnumber("");
			rb.setRestext(result);
		}
		json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
		return rb;
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

	/**
	 * 
	 * @param exam_num
	 * @param logname
	 * @return
	 */
	public CustDAHBean getCustom(String arch_num,CustDAHBean cu,String logname){
		if(arch_num!=null&&arch_num.trim().length()>0){
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,a.id as customer_id,dd.data_name as nation,a.idtype,a.user_name,a.arch_num,a.id_num,c.patient_id,"
				+ "a.sex,c.age,c.exam_num,c.is_marriage,c.exam_type,"
				+ "c.position,c._level,c.group_id,c.email,c.chargingType,c.remarke,"
				+ "c.others,c.status,c.phone,c.customer_type,c.customer_type_id,c.customerType,a.address");
		sb.append(",c.group_id,c.group_index,a.birthday,c._level,c.status,c.register_date"
				+ ",c.join_date,c.exam_times,c.exam_num,c.past_medical_history,"
				+ "c.picture_Path,c.is_after_pay,c.batch_id,n.batch_name,c.is_Active,"
				+ "c.employeeID,c.patient_id,c.mc_no,c.visit_date,c.visit_no,c.clinic_no,c.exam_indicator,c.batch_id,"
				+ "c.company_id,m.group_name,c.freeze,c.company,c.budget_amount,c.introducer,c.marriage_age ");
		sb.append(" from customer_info a ");
		sb.append(" left join data_dictionary dd on convert(varchar(20),dd.id)=a.nation");
		sb.append(" ,exam_info c ");
		sb.append(" left join batch n on n.id=c.batch_id ");
		sb.append(" left join group_info m on m.id=c.group_id ");
		sb.append(" where c.customer_id=a.id ");
		sb.append(" and c.is_Active='Y' ");
		sb.append(" and a.is_Active='Y' ");
		sb.append(" and a.arch_num='" + arch_num.trim() + "' order by c.create_time desc");
		TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb.toString());
		List<ExamInfoUserDTO> list = this.jdbcQueryManager.getList(sb.toString(), ExamInfoUserDTO.class);
		if((list!=null)&&(list.size()>0)){
			ExamInfoUserDTO ei= new ExamInfoUserDTO();
			ei=(ExamInfoUserDTO)list.get(0);			
			if(ei.getIs_marriage().indexOf("已")>=0){
				cu.setWebbed("1");
				cu.setMarriedAge(ei.getMarriage_age()+"");
			}else if(ei.getIs_marriage().indexOf("未")>=0){
				cu.setWebbed("0");
				cu.setMarriedAge("0");
			}else {
				cu.setWebbed("2");
				cu.setMarriedAge("0");
			}
		}else{
			cu.setWebbed("2");
			cu.setMarriedAge("0");
		}
		}else{
			cu.setWebbed("2");
			cu.setMarriedAge("0");
		}
		return cu;
	} 	
	
	/**
	 * 
	 * @param exam_num
	 * @param logname
	 * @return
	 */
	public String getComNum(String exam_num,String logname){
		Connection tjtmpconnect = null;
		String chargtypename= "";		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select b.com_num from exam_info a  left join company_info b on b.id=a.company_id "
					+ "where exam_num='' a.id='"+exam_num+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				chargtypename=(rs1.getString("com_num"));
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
	
	/**
	 * 
	 * @param url
	 * @param view_num
	 * @return
	 */
	public String getUserWorkNum(long userid,String logname){
		Connection tjtmpconnect = null;
		String nationname= configService.getCenterconfigByKey("IS_DAH_OPERATOR_ID").getConfig_value().trim();//档案号对应的操作员
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select work_num from user_usr where id='"+userid+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " +sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				String work_num=rs1.getString("work_num");
				if((work_num!=null)&&(work_num.trim().length()>0)){
				nationname=work_num;
				}
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
	
}
