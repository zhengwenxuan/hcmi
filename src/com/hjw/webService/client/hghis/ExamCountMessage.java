package com.hjw.webService.client.hghis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.synjones.framework.persistence.JdbcQueryManager;

public class ExamCountMessage {
	private static JdbcQueryManager jdbcQueryManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public void getMessage(String url, String logname) {
		Connection orecal_connect = null;
		Connection peis_connect = null;
		Statement statement = null;
		try {
			String dburl = url.split("&")[0];
			String user = url.split("&")[1];
			String passwd = url.split("&")[2];
			orecal_connect = OracleDatabaseSource.getConnection(dburl, user, passwd);
			peis_connect = jdbcQueryManager.getConnection();
			boolean flags = true;
			peis_connect.setAutoCommit(false);
			
			try {
//				String old_sql = "select count(e.id) exam_count,convert(varchar(50),e.create_time,23) create_time "
//						+ "from exam_info e where e.is_Active = 'Y' and convert(varchar(50),e.create_time,23) >='2018-01-01' "
//						+ "group by convert(varchar(50),e.create_time,23) order by convert(varchar(50),e.create_time,23)";
				String sql = "select count(e.id) exam_count,convert(varchar(50),e.create_time,23) create_time "
						+ "from exam_info e where e.is_Active = 'Y' and convert(varchar(50),e.create_time,23) ='"+DateTimeUtil.getDate2()+"' "
						+ "group by convert(varchar(50),e.create_time,23)";
				ResultSet rs = peis_connect.createStatement().executeQuery(sql);
				statement = orecal_connect.createStatement();
				// 处理结果
				while (rs.next()) {
					String select_sql = "select * from TJ_DJRS where 日期 = to_date('"+rs.getString("create_time")+"','yyyy-mm-dd')";
					ResultSet rs1 = statement.executeQuery(select_sql);
					int countold=0;
					if (rs1.next()) {
						countold ++;
					}
					rs1.close();
					if(countold==0){
						String insert_sql = "insert into TJ_DJRS(日期,人数) values(to_date('"+rs.getString("create_time")+"','yyyy-mm-dd'),'"+rs.getInt("exam_count")+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:插入sql--" + insert_sql);
						statement.executeUpdate(insert_sql);
					}else{
						String update_sql = "update TJ_DJRS set 人数='"+rs.getInt("exam_count")+"' where 日期=to_date('"+rs.getString("create_time")+"','yyyy-mm-dd')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:修改sql--" + update_sql);
						statement.executeUpdate(update_sql);
					}
				}
				rs.close();
				TranLogTxt.liswriteEror_to_txt(logname, "res:体检人数同步成功!");
				String zjsql = "select count(e.id) exam_count,convert(varchar(50),e.final_date,23) create_time "
						+ "from exam_info e where e.is_Active = 'Y' and convert(varchar(50),e.final_date,23) ='"+DateTimeUtil.getDate2()+"' "
						+ "group by convert(varchar(50),e.final_date,23)";
				rs = peis_connect.createStatement().executeQuery(zjsql);
				statement = orecal_connect.createStatement();
				// 处理结果
				while (rs.next()) {
					String select_sql = "select * from TJ_ZJRS where 日期 = to_date('"+rs.getString("create_time")+"','yyyy-mm-dd')";
					ResultSet rs1 = statement.executeQuery(select_sql);
					int countold=0;
					if (rs1.next()) {
						countold ++;
					}
					rs1.close();
					if(countold==0){
						String insert_sql = "insert into TJ_ZJRS(日期,人数) values(to_date('"+rs.getString("create_time")+"','yyyy-mm-dd'),'"+rs.getInt("exam_count")+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:插入sql--" + insert_sql);
						statement.executeUpdate(insert_sql);
					}else{
						String update_sql = "update TJ_ZJRS set 人数='"+rs.getInt("exam_count")+"' where 日期=to_date('"+rs.getString("create_time")+"','yyyy-mm-dd')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:修改sql--" + update_sql);
						statement.executeUpdate(update_sql);
					}
				}
				rs.close();
				TranLogTxt.liswriteEror_to_txt(logname, "res:每天总检状态的人数同步成功!");
				
				String jcsql = "select count(e.id) exam_count,convert(varchar(50),e.join_date,23) create_time "
						+ "from exam_info e where e.is_Active = 'Y' and convert(varchar(50),e.join_date,23) ='"+DateTimeUtil.getDate2()+"' "
						+ "group by convert(varchar(50),e.join_date,23)";
				rs = peis_connect.createStatement().executeQuery(jcsql);
				statement = orecal_connect.createStatement();
				// 处理结果
				while (rs.next()) {
					String select_sql = "select * from TJ_JCRS where 日期 = to_date('"+rs.getString("create_time")+"','yyyy-mm-dd')";
					ResultSet rs1 = statement.executeQuery(select_sql);
					int countold=0;
					if (rs1.next()) {
						countold ++;
					}
					rs1.close();
					if(countold==0){
						String insert_sql = "insert into TJ_JCRS(日期,人数) values(to_date('"+rs.getString("create_time")+"','yyyy-mm-dd'),'"+rs.getInt("exam_count")+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:插入sql--" + insert_sql);
						statement.executeUpdate(insert_sql);
					}else{
						String update_sql = "update TJ_JCRS set 人数='"+rs.getInt("exam_count")+"' where 日期=to_date('"+rs.getString("create_time")+"','yyyy-mm-dd')";
						TranLogTxt.liswriteEror_to_txt(logname, "res:修改sql--" + update_sql);
						statement.executeUpdate(update_sql);
					}
				}
				rs.close();
				TranLogTxt.liswriteEror_to_txt(logname, "res:每天检查状态的人数同步成功!");
			} catch (Exception e) {
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				flags = false;
			}
			if (!flags) {
				peis_connect.rollback();
			} else {		
				peis_connect.commit();
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (statement != null){
					statement.close();
				}
				if (orecal_connect != null) {
					OracleDatabaseSource.close(orecal_connect);
				}
				if (peis_connect != null){
					peis_connect.close();
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
}