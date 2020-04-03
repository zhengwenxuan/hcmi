package com.hjw.webService.client.bjxy.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.synjones.framework.persistence.JdbcQueryManager;

public class QuerySqlData {
	private static WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
	private static JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	
	
	public static String runSql(String sql){
		System.out.println(sql);
		Connection connection = null;
		try {
		    connection = jdbcQueryManager.getConnection();
			int rs = connection.createStatement().executeUpdate(sql);
			return "1";
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		}finally{
			try {
				if(connection!=null){
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	public static String getYiZhuHao(String yizhuhao){
		int i=Integer.valueOf(yizhuhao);
		String str = String.format("%012d", i);
		return str;
	}

	public static String getShiFouCunZai(String exam_num,String apply_id,String lognmae){
		String sql=" select * from exam_info e "
				+ " left join sample_exam_detail s on e.id=s.exam_info_id "
				+ " where e.exam_num='"+exam_num+"' and s.sample_barcode='"+apply_id+"'";
		TranLogTxt.liswriteEror_to_txt(lognmae,sql);
		Connection connection = null;
		Statement statement = null;
		String flag="0";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				flag="1";
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return flag;
	}
	public static List<String> getQueryID(String sql,String lognmae){
		TranLogTxt.liswriteEror_to_txt(lognmae,sql);
		Connection connection = null;
		Statement statement = null;
		List<String> addresss=new ArrayList<String>();
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				addresss.add(rs.getString("id"));
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
	public static String[] getSample(String item_code,String logName){
		/*String sql=" select s.check_id as updater,s.check_date as update_time,c.name as name,c.unit_sn as unit_sn,m.name as username from sample_exam_detail s "
				+ " left join xy_a_employee_mi m on s.updater=m.emp_sn "
				+ " left join xy_zd_unit_code c on m.dept_sn=c.unit_sn where s.sample_barcode='"+item_code+"'";*/
		String sql="select  u.work_num as updater,s.create_time as update_time,c.name as name,c.unit_sn as unit_sn,"
				+ "m.name as username from sample_exam_detail s "
				+ "left join user_usr u on s.creater=u.id "
				+ "left join xy_a_employee_mi m on u.work_num=m.emp_sn "
				+ "left join xy_zd_unit_code c on m.dept_sn = c.unit_sn "
				+ " where s.sample_barcode='"+item_code+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String[] addresss=new String[5];
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			String as = "";
			while (rs.next()) {
				addresss[0]=rs.getString("updater");
				addresss[1]=rs.getString("update_time");
				addresss[2]=rs.getString("name");
				addresss[3]=rs.getString("unit_sn");
				addresss[4]=rs.getString("username");
				as=rs.getString("updater")+rs.getString("update_time")+rs.getString("name")+rs.getString("unit_sn")+rs.getString("username");
			}
			TranLogTxt.liswriteEror_to_txt(logName,as);
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
	
	public static String getExecUnit(String item_code,String class_code,String logName){
		String sql=" select b.exec_unit as exec_unit from xy_jc_zd_exam_sub_type b"
				+ " where b.type_code='"+class_code+"' and b.sub_code='"+item_code+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String addresss="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				addresss=rs.getString("exec_unit");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
	
	public static String getPacsClassStrs(String item_code,String logName){
		String sql=" select b.name as name from xy_jc_zd_exam_type b"
				+ " where b.code='"+item_code+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String addresss="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				addresss=rs.getString("name");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
	
	public static String[] getClassStrs(String item_code,String logName){
		String sql=" select a.itemclass_code as itemclass_code,a.itemclass_name as itemclass_name,c.samp_type as samp_type from xy_zd_jy_itemclass  a "
				+ " left join xy_zd_jy_item_itemclass b on a.itemclass_code=b.itemclass_code "
				+ " left join xy_zd_jy_itemclass_samptype c on b.itemclass_code=c.itemclass_code "
				+ " where b.item_code='"+item_code+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String[] addresss=new String[3];
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				addresss[0]=rs.getString("itemclass_code");
				addresss[1]=rs.getString("itemclass_name");
				addresss[2]=rs.getString("samp_type");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
	
	public static String[] getSampleTypeStrs(String item_code,String logName){
		String sql=" select samp_type,samp_name from xy_third_sample_type "
				+ " where apply_item='"+item_code+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String[] addresss=new String[2];
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				addresss[0]=rs.getString("samp_type");
				addresss[1]=rs.getString("samp_name");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
	public static String getOrderNo(String charging_id,String sample_barcode,String logName){
		String sql=" select s.id as id from examResult_chargingItem s "
				+ " left join sample_exam_detail se on s.exam_id=se.id"
				+ " where s.charging_id='"+charging_id+"' and s.result_type='sample' and s.isActive='Y' and se.sample_barcode='"+sample_barcode+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String id="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				id=rs.getString("id");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return id;
	}
	
	public static String getUser_name(String id,String logName){
		String sql="select s.name as username from xy_a_employee_mi s where s.emp_sn='"+id+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String username="";
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				username=rs.getString("username");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return username;
	}
	
	public static String[] getAddresss(String exam_num,String logName){
		String sql="select address,phone from exam_info where exam_num='"+exam_num+"'";
		TranLogTxt.liswriteEror_to_txt(logName,sql);
		Connection connection = null;
		Statement statement = null;
		String[] addresss=new String[2];
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				addresss[0]=rs.getString("address");
				addresss[1]=rs.getString("phone");
			}
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return addresss;
	}
}
