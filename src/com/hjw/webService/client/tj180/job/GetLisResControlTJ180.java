package com.hjw.webService.client.tj180.job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.tj180.LISResMessageTj180;
import com.hjw.webService.client.tj180.LISResOtherMessageTj180;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetLisResControlTJ180 {
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public ResultHeader getMessage(String url, int days, String logName) {
		String datetime = DateTimeUtil.DateDiff2(days);
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select DISTINCT a.exam_num "
					+ "from exam_info a,examinfo_charging_item b,charging_item c,department_dep d "
					+ "where a.id=b.examinfo_id and a.is_Active='Y' and b.isActive='Y' and b.exam_status='N' "
					+ "and b.charge_item_id=c.id and d.dep_category='131'  and b.pay_status <>'M' and c.item_category = '普通类型' and c.dep_id=d.id and a.status <> 'Z' "
					+ "and CONVERT(varchar(50),a.join_date,23)>= '" + datetime + "' order by a.exam_num ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			List<String> exam_nums=new ArrayList<String>();
			while (rs1.next()) {
				exam_nums.add(rs1.getString("exam_num"));
			}
			rs1.close();
			for(String exam_num:exam_nums){
				LISResMessageTj180 prm = new LISResMessageTj180();
				prm.getMessage(url, logName, exam_num);
			}
			rh.setTypeCode("AA");
			rh.setText("");
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logName, "res: :  操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rh;
	}
	
	/**
	 * 医院专用，批量处理以前的lis结果
	 * @param url
	 * @param days
	 * @param logName
	 * @return
	 */
	public ResultHeader getMessage_other(String url, int days, String logName) {
		String datetime = DateTimeUtil.DateDiff2(days);
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select DISTINCT a.exam_num "
					+ "from exam_info a,examinfo_charging_item b,charging_item c,department_dep d "
					+ "where a.id=b.examinfo_id and a.is_Active='Y' and b.isActive='Y' and b.exam_status='N' "
					+ "and b.charge_item_id=c.id and d.dep_category='131' and b.pay_status <>'M' and c.item_category = '普通类型' and c.dep_id=d.id and a.status <> 'Z' "
					+ "and CONVERT(varchar(50),a.join_date,23)<= '" + datetime + "' order by a.exam_num ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			List<String> exam_nums=new ArrayList<String>();
			while (rs1.next()) {
				exam_nums.add(rs1.getString("exam_num"));
			}
			rs1.close();
			for(String exam_num:exam_nums){
				LISResOtherMessageTj180 prm = new LISResOtherMessageTj180();
				prm.getOtherMessage(url, logName, exam_num);
			}
			rh.setTypeCode("AA");
			rh.setText("");
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logName, "res: :  操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return rh;
	}
}
