package com.hjw.webService.client.tj180.job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.DateUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.MSGSendBean;
import com.hjw.webService.client.body.MSGResBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.MSGSendMessageTJ180;
import com.synjones.framework.persistence.JdbcQueryManager;

public class PostMsgControlTJ180 {
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init(); 
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public MSGResBody getMessage(String url, int days, String logName) {
		Connection tjtmpconnect = null;
		MSGResBody rh= new MSGResBody();
		List<String> exam_nums=new ArrayList<String>();
		try { 
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String dates = DateUtil.DateDiff2(days)+"  23:59:59";
			String sb1 = "  SELECT ss.sms_batch "
					+ "FROM  exam_info   ei,customer_info  cs,crm_sms_send ss "
					+ "WHERE   ei.id=ss.user_id   AND  cs.id=ei.customer_id "
					+ "and ei.exam_num not in (select exam_num  from  report_receive) "
					+ "AND  ss.sms_status='0' and ss.sms_type=1 "
					+ "AND  ss.sms_date<= '"+dates+"'  "
					+ "order by ss.sms_date desc  ";
					//+ "AND  ss.sms_date>= '2018-09-01 00:00:00' AND  ss.sms_date< '2018-10-05 23:59:59'  "
					//+ "and CONVERT(varchar(50),a.join_date,23)>= '" + datetime + "' order by a.exam_num ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			
			while (rs1.next()) {
				exam_nums.add(rs1.getString("sms_batch"));
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logName, "res: :  操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rh.setRescode("AE");
			rh.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			return rh;
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		for(String exam_num:exam_nums){
			MSGSendBean msb= new MSGSendBean();
			msb.setBatchCode(exam_num);				
			MSGSendMessageTJ180 prm = new MSGSendMessageTJ180();
			prm.getMessage(url, msb,logName);
		}
		rh.setRescode("AA");
		rh.setRestext("");
		return rh;
	}
}
