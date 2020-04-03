package com.hjw.interfaces.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.Bean.ReqMessage;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.body.ResultBody;
import com.hjw.webService.client.dashiqiao.ResBean.ResHdMeessage;
import com.hjw.wst.service.CommService;
import com.hjw.wst.service.CustomerInfoService;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.hjw.wst.service.examInfoService;
import com.synjones.framework.persistence.JdbcPersistenceManager;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.persistence.PersistenceManager;
import com.synjones.framework.persistence.QueryManager;

public class LockCenterDateUtil {

	
	
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	private static CommService commService;   //examInfoService
	private static Calendar checkDay;
	
	static{
    	init();
    }
	
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		commService = (CommService) wac.getBean("commService");  //
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");
	}
	//month 为国外月份  实际设置 例如11月 此参数要传10  实际月份减少1
	public static ResHdMeessage SetEaminatioinCenterDate(int year,int month,int date ,String logname) {
		ResHdMeessage rb = new ResHdMeessage();
		rb.setStatus("AA");
		
		String xml = "";
		try {
			/////////////////////////////日期限制及体检系统通知功能-开始/////////////////////////////
			Calendar deadline = Calendar.getInstance();
			SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
			//JANUARY一月	FEBRUARY二月		MARCH三月		APRIL四月		MAY五月			JUNE六月
			//JULY七月		AUGUST八月		SEPTEMBER九月	OCTOBER十月		NOVEMBER十一月	DECEMBER十二月
			deadline.set(year, month, date, 0, 0, 0);
			String viewDateStr = df.format(deadline.getTime());
			System.err.println(viewDateStr+"日期11");
			if(new Date().after(deadline.getTime())) {
				rb.setStatus("AE");
				rb.setMessage("error-接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
				TranLogTxt.liswriteEror_to_txt(logname,"接口已过期，请联系火箭蛙，截止日期："+viewDateStr);
				return rb;
			}
			
			if(checkDay == null) {
				checkDay = Calendar.getInstance(); checkDay.add(Calendar.DATE, -1);
			}
			Calendar today = Calendar.getInstance(); today.set(Calendar.HOUR, 0); today.set(Calendar.MINUTE, 0); today.set(Calendar.SECOND, 0);
			if(today.after(checkDay)) {//每天仅检查一遍
				checkDay = today;
				Connection connection = null;
				try {
					//每次先将旧的通知信息打到日志文件
					connection = jdbcQueryManager.getConnection();
					String sql = " select notices from examinatioin_center ";
					ResultSet rs = connection.createStatement().executeQuery(sql);
					String notices="";
					while (rs.next()) {
						notices = rs.getString("notices");
					}
					TranLogTxt.liswriteEror_to_txt(logname, "原来的notices是:"+notices);
					//判断系统到期时间，提前10天提醒客户
					Calendar alertDate = deadline;
					alertDate.add(Calendar.DATE, -10);
					if(new Date().after(alertDate.getTime())) {
						String noticesNew = "系统到期时间为:"+viewDateStr+"，请尽快联系火箭蛙销售人员!!";
						String updatesql = " update examinatioin_center set notices='"+noticesNew+"' ";
						System.err.println(viewDateStr+"日期时间");
						connection.createStatement().executeUpdate(updatesql);
						rb.setStatus("AA");
						rb.setMessage(noticesNew);
						TranLogTxt.liswriteEror_to_txt(logname, updatesql);
					} else {
						String noticesNew = "系统到期时间为:"+viewDateStr+"，请尽快联系火箭蛙销售人员!!";
						System.err.println(viewDateStr+"日期时间");
						String updatesql = " update examinatioin_center set notices='' ";
						connection.createStatement().executeUpdate(updatesql);
					    rb.setStatus("AA");
						rb.setMessage(noticesNew);
						TranLogTxt.liswriteEror_to_txt(logname, updatesql);
					}
				} catch(Exception ex) {
					 rb.setStatus("AE");
					 rb.setMessage(com.hjw.interfaces.util.StringUtil.formatException(ex));
					ex.printStackTrace();
				} finally {
					connection.close();
				}
			}
			/////////////////////////////日期限制及体检系统通知功能-结束/////////////////////////////
			
			
		} catch (Exception ex){
		    rb.setStatus("AE");
			 rb.setMessage(com.hjw.interfaces.util.StringUtil.formatException(ex));
		}
		return rb;
	}
}
