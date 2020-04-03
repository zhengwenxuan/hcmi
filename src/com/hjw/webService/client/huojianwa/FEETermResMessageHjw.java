package com.hjw.webService.client.huojianwa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.synjones.framework.persistence.JdbcQueryManager;

public class FEETermResMessageHjw {
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public ResultHeader getMessage(String url,String acc_num, String logName) {
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.id,a.amount2,a.amount1,a.receiv_status from charging_summary_group a where account_num='"+acc_num+"' and is_active='Y' ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs.next()) {
				long summary_id = rs.getLong("id");
				boolean hisstatus = getHisStatus(url,acc_num,logName);
				if(hisstatus){
					String updatesql="update charging_summary_group set receiv_status='1' where id ='"+summary_id+"'";
					TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
					tjtmpconnect.createStatement().execute(updatesql);
					updatesql="update charging_summary_single set charging_status='Y' where id='"+summary_id+"'";
					TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
					tjtmpconnect.createStatement().execute(updatesql);
					updateHisStatus(url,acc_num,logName);
				}
			}
			rs.close();
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
	
	private boolean getHisStatus(String url,String reqno,String logname){
		Connection connect = null;
		boolean hisstatus=false;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];	
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql="select req_no from Pat_Charge_List where patID='" + reqno + "' and req_no='"+reqno+"' and chargeFlag='1'";
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+reqno+":"+sql);
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				hisstatus=true;			
			}
			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+reqno+":"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return hisstatus;
	}
	
	private boolean updateHisStatus(String url,String reqno,String logname){
		Connection connect = null;
		boolean hisstatus=false;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];	
			String table = url.split("&")[3];
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + url);
			//ExamInfoUserDTO eu = new ExamInfoUserDTO();
			//eu=this.getExamInfoForNum(exam_num, logname);
			connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			String sql="update Pat_Charge_List set readFlag=1  where patID='" + reqno + "' and req_no='"+reqno+"' and chargeFlag='1'";
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+reqno+":"+sql);
			connect.createStatement().execute(sql);
			hisstatus=true;			
		} catch (Exception ex) {
			ex.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+reqno+":"+com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return hisstatus;
	}
}
