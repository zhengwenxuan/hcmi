package com.hjw.webService.client.tj180;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.StringUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueAddBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.tj180.Bean.QueueBodyBean;
import com.hjw.webService.client.tj180.Bean.QueueResBodyBean;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj   
     * @Description:  增加排队
     * @author: yangm     
     * @date:   2016年10月7日 下午2:50:56   
     * @version V2.0.0.0
 */
public class QueueAddSendMessageTJ180 {
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	/**
	 * 
	 * @Title: lisSend @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public QueueResBody getMessage(String url, QueueAddBean eu, String logname) {
		QueueResBody rb = new QueueResBody();
		try {
			JSONObject json = JSONObject.fromObject(eu);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url);
			QueueBodyBean qbb = new QueueBodyBean();
			qbb = getQueueBodyBean(eu.getExam_id(), logname);

			json = JSONObject.fromObject(qbb);// 将java对象转换为json对象
			str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);

			String result = HttpUtil.doPost(url, qbb, "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result + "\r\n");
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				JSONObject jsonobject = JSONObject.fromObject(result);
				QueueResBodyBean resdah = new QueueResBodyBean();
				resdah = (QueueResBodyBean) JSONObject.toBean(jsonobject, QueueResBodyBean.class);
				if ("200".equals(resdah.getStatus())) {
					boolean inserflag = insertqueueLog(qbb.getExam_num(), resdah.getQueueId(), logname);
					if (inserflag) {
						rb.setRestext("");
						rb.setRescode("AA");
						rb.setIdnumber("");
					} else {
						rb.setRestext("排队成功，但写本地日志表exam_queue_log失败，详情请看日志文件");
						rb.setRescode("AE");
						rb.setIdnumber("");
					}
				} else {
					rb.setRestext(resdah.getErrorinfo());
					rb.setRescode("AE");
					rb.setIdnumber("");
				}
			} else {
				rb.setRestext("");
				rb.setRescode("AE");
				rb.setIdnumber("");
			}
		} catch (Exception ex) {
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
			rb.setIdnumber("");
		}

		JSONObject json = JSONObject.fromObject(rb);// 将java对象转换为json对象
		String str = json.toString();// 将json对象转换为字符串
		TranLogTxt.liswriteEror_to_txt(logname, "re1:" + str);
		return rb;
	}
	
	private QueueBodyBean getQueueBodyBean(long exam_id,String logname){
		Connection tjtmpconnect = null;
		QueueBodyBean qb= new QueueBodyBean();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		//String vipcustom = configService.getCenterconfigByKey("IS_VIP_CUSTOME_TYPE").getConfig_value().trim();//vip对于的体检类型
		String vipchargitem=configService.getCenterconfigByKey("IS_VIP_CHARGITEM").getConfig_value().trim();//vip对于的收费项目
		//String vipdept=configService.getCenterconfigByKey("IS_VIP_DEPT").getConfig_value().trim();//vip对于的科室
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.id,b.arch_num,a.exam_num,b.sex,a.company_id,a.company,a.exam_type,a.customer_type from exam_info a,customer_info b "
					+ "where a.customer_id=b.id "
					+ "and a.is_Active='Y' "
					+ "and a.freeze=0 and a.status<>'Z' "
					+ "and a.id='"+exam_id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb1);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs.next()) {
				qb.setCustomerPatientId(rs.getString("arch_num"));
				qb.setCustomerSex(rs.getString("sex"));
				qb.setOrganizationId(rs.getString("company_id"));
				qb.setOrganizationName("company");
				qb.setReserveId(rs.getString("exam_num"));
				String examtype=rs.getString("exam_type");
				qb.setExam_id(exam_id);
				qb.setExam_num(rs.getString("exam_num"));
				String QUEUE_COMPANY_G = configService.getCenterconfigByKey("QUEUE_COMPANY_G").getConfig_value().trim();//团体按照个人发送排队申请
				if(StringUtil.checkString(QUEUE_COMPANY_G, ",", rs.getString("company_id"))){
					qb.setReserveType("P");
				}else if("T".equals(examtype)){
					qb.setReserveType("O");
				}else if("G".equals(examtype)){
					qb.setReserveType("P");
				}
				qb.setRegeditDateTime(DateTimeUtil.getDateTimes());
				//qb.setIsVipFood(getcheckStr(vipcustom,rs.getString("customer_type"))+"");
				
				double amount=0;
				String sb2 = "select ec.id,c.id as charge_item_id,c.dep_id,ec.pay_status,ec.amount,d.dep_num  "
						+ "from exam_info e,charging_item c"
						+ " left join department_dep d on d.id=c.dep_id,"
						+ " examinfo_charging_item ec "
						+ "where ec.examinfo_id = e.id and ec.charge_item_id = c.id "
						+ "and ec.pay_status <> 'M'  "
						+ "and ec.isActive = 'Y' "
						+ "and ec.change_item <> 'C' and e.is_Active='Y' "
						+ "and e.id ='"+exam_id+"'";
				TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb2);
				ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb2);
				while (rs1.next()) {
					if(!"M".equals(rs1.getString("pay_status"))){
						amount=amount+rs1.getDouble("amount");
					}
					/*if("0".equals(qb.getIsVipFood())){
						qb.setIsVipFood(getcheckStr(vipdept,rs1.getString("dep_id"))+"");
					}*/
					if("0".equals(qb.getIsVipFood())){
						qb.setIsVipFood(getcheckStr(vipchargitem,rs1.getString("charge_item_id"))+"");
					}					
				}
				
				qb.setHasCc(getDeptNumflag(rs.getLong("id"),"'US','XGNS'",logname));
				BigDecimal bd = new BigDecimal(0);
				bd = new BigDecimal(amount);
				amount = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();			
				rs1.close();				
				qb.setReserveTotalCost(amount+"");
				qb.setIsVipFood(getCheckvip(qb.getExam_num(),qb.getIsVipFood(),amount,logname));
			} 
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return qb;
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
	public String getCheckvip(String exam_num,String vipflag,double amt,String logname){
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
	}
	
	
	private String getDeptNumflag(long exam_id,String dept_num,String logname){
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
	}
	/**
	 * 
	 * @param strs
	 * @param str
	 * @return
	 */
	private int getcheckStr(String strs,String str){
		int f=0;  // 有：1，没有：0
		String[] strings=strs.split(",");
		for(int i=0;i<strings.length;i++){
			if(str.equals(strings[i])){
				f=1;
				break;
			}
		}
		return f;
	}
}
