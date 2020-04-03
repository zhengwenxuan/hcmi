package com.hjw.webService.client.liubaxian;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetHisResControlLBX {
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public ResultHeader getMessage(String url,int days, String logName) {
		String datetime = DateTimeUtil.DateDiff2(days);
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.exam_id,b.exam_num,a.req_num,a.id from charging_summary_single a,exam_info b where a.exam_id=b.id "
					+ "and b.is_Active='Y' "
					+ "and a.charging_status='R' "
					+ "and CONVERT(varchar(50),a.create_time,23)>= '"+datetime+"' order by b.exam_num ";
			TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + sb1);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs.next()) {
				//String exam_num = rs.getString("exam_num");
				long exam_id=rs.getLong("exam_id");
				long summary_id = rs.getLong("id");
				String exam_num=rs.getString("exam_num");
				String req_num = rs.getString("req_num");
				boolean hisstatus = getHisStatus(url,exam_num,req_num,logName);
				if(hisstatus){
					String updatesql="update examinfo_charging_item set pay_status='Y' where id in"
							+ "( select b.id from charging_detail_single a,examinfo_charging_item b "
							+ "where a.summary_id='"+summary_id+"' and b.charge_item_id=a.charging_item_id and b.isActive = 'Y' and b.examinfo_id='"+exam_id+"')";
					TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
					tjtmpconnect.createStatement().execute(updatesql);
					updatesql="update charging_summary_single set charging_status='Y' where id='"+summary_id+"'";
					TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
					tjtmpconnect.createStatement().execute(updatesql);
					updateHisStatus(url,exam_num,req_num,logName);
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
	
	private boolean getHisStatus(String url,String exam_num,String reqno,String logname){
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
			String sql="select req_no from Pat_Charge_List where patID='" + exam_num + "' and req_no='"+reqno+"' and chargeFlag='1'";
			TranLogTxt.liswriteEror_to_txt(logname,"res:"+reqno+":"+sql);
			ResultSet rs = connect.createStatement().executeQuery(sql);
			if (rs.next()) {
				hisstatus=true;			
			}
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
	
	private boolean updateHisStatus(String url,String exam_num,String reqno,String logname){
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
			String sql="update Pat_Charge_List set readFlag=1  where patID='" + exam_num + "' and req_no='"+reqno+"' and chargeFlag='1'";
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
