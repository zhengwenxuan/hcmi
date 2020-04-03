package com.hjw.webService.client.hghis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.util.StringUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageHG {

	private static JdbcQueryManager jdbcQueryManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public ResultHisBody getMessage(String url, String logname) {
		ResultHisBody rb = new ResultHisBody();
		
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
				TranLogTxt.liswriteEror_to_txt(logname, "req:开始同步执行科室数据!(xt_department)");
				String del_dep_sql = "delete his_dict_dept";
				peis_connect.createStatement().executeUpdate(del_dep_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:删除系统旧执行科室数据列表成功!");
				
				String select_dep_sql= "select * from xt_department";
				statement = orecal_connect.createStatement();
				ResultSet rs = statement.executeQuery(select_dep_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:查询HIS执行科室数据列表成功!");
				// 处理结果
				while (rs.next()) {
					String inset_dep_sql = "insert into his_dict_dept(dept_code,dept_name,dept_class,input_code)"
							+ "values('"+rs.getInt("DepartmentNo")+"','"+rs.getString("DepartmentName")+"','','"+rs.getString("PY")+"')";
					peis_connect.createStatement().executeUpdate(inset_dep_sql);
				}
				TranLogTxt.liswriteEror_to_txt(logname, "req:插入新的系统执行科室数据列表成功!");
				
				TranLogTxt.liswriteEror_to_txt(logname, "req:开始同步诊疗项目数据!(xt_groupname)");
				
				String del_clinic_sql = "delete his_clinic_item";
				peis_connect.createStatement().executeUpdate(del_clinic_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:删除系统旧诊疗项目数据列表成功!");
				
				String select_clinic_sql = "select * from xt_groupname";
				rs = statement.executeQuery(select_clinic_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:查询HIS诊疗项目数据列表成功!");
				// 处理结果
				while (rs.next()) {
					String insert_clinic_sql = "insert into his_clinic_item(item_class,item_code,item_name,input_code,item_status,expand1) "
							+ "values ('A','"+rs.getString("groupid")+"','"+rs.getString("groupname").replace("'", "''")+"','','"+rs.getString("STOP")+"','"+rs.getString("DEPLIST")+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "req:插入HIS诊疗项目sql--"+insert_clinic_sql);
					peis_connect.createStatement().executeUpdate(insert_clinic_sql);
				}
				TranLogTxt.liswriteEror_to_txt(logname, "req:插入新的系统诊疗项目数据列表成功!");
				
				TranLogTxt.liswriteEror_to_txt(logname, "req:开始同步诊疗项目和价表关系数据!(xt_groupdetail)");
				
				String del_clinic_price_sql = "delete his_clinic_item_v_price_list";
				peis_connect.createStatement().executeUpdate(del_clinic_price_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:删除系统旧诊疗项目和价表关系数据列表成功!");
				
				String select_clinic_price_sql = "select * from xt_groupdetail";
				rs = statement.executeQuery(select_clinic_price_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:查询HIS诊疗项目和价表关系数据列表成功!");
				// 处理结果
				while (rs.next()) {
					if(StringUtil.isInt(rs.getString("TIMES"))){
						String insert_clinic_price_sql = "insert into his_clinic_item_v_price_list(clinic_item_class,clinic_item_code,"
								+ "charge_item_no,charge_item_class,charge_item_code,charge_item_spec,amount,units)"
								+ " values('A','"+rs.getString("groupid")+"','"+rs.getString("TIMES")+"','A','"+rs.getString("code")+"','/','1','')";
						peis_connect.createStatement().executeUpdate(insert_clinic_price_sql);
					}
				}
				TranLogTxt.liswriteEror_to_txt(logname, "req:插入新的诊疗项目和价表关系数据列表成功!");
				
				TranLogTxt.liswriteEror_to_txt(logname, "req:开始同步价表数据!(xt_ItemName)");
				
				String del_price_sql = "delete his_price_list";
				peis_connect.createStatement().executeUpdate(del_price_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:删除系统旧价表数据列表成功!");
				
				String select_price_sql = "select * from xt_ItemName";
				rs = statement.executeQuery(select_price_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "req:查询HIS价表数据列表成功!");
				// 处理结果
				while (rs.next()) {
					String insert_price_sql = "  insert into his_price_list(item_class,item_code,item_name,item_spec,units,price,"
							+ "prefer_price,input_code,start_date,stop_date,subj_code) "
							+ "values('A','"+rs.getInt("Code")+"','"+rs.getString("Name").replace("'", "''")+"','/','"+rs.getString("Unit")+"',"
							+ "'"+rs.getString("Price")+"','"+rs.getString("Price")+"','',"
							+ "'2000-01-01 00:00:00.000','9999-12-31 23:59:59.000','"+rs.getInt("ExpensesCode")+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "req:插入HIS价表sql--"+insert_price_sql);
					peis_connect.createStatement().executeUpdate(insert_price_sql);
				}
				TranLogTxt.liswriteEror_to_txt(logname, "req:插入新的价表数据列表成功!");
				rs.close();
			} catch (Exception e) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
				TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
				flags = false;
			}
			if (!flags) {
				peis_connect.rollback();
			} else {		
				peis_connect.commit();
				rb.getResultHeader().setTypeCode("AA");
				rb.getResultHeader().setText("数据同步成功!");
			}
		} catch (Exception e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("链接his数据库错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
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
		return rb;
	}
}
