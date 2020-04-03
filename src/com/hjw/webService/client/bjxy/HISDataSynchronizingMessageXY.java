package com.hjw.webService.client.bjxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hsqldb.lib.StringUtil;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageXY {

	private static JdbcQueryManager jqm;
	static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public ResultHisBody getMessage(String url, String logname) {
		ResultHisBody rb = new ResultHisBody();
		Connection his_connect = null;
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			his_connect = SqlServerDatabaseSource.getConnection(url.split("&")[0], url.split("&")[1], url.split("&")[2]);
			String sql = "SELECT [item_class],[class_name],[clinic_code],[clinic_name],[clinic_py],[amount],[charge_code],[charge_name],[charge_py],[charge_price],[charge_unit] FROM [view_zd_charge_item_HJW] where deleted_flag = 0 "
					+ " union "
					+ " SELECT '' as [item_class], '' as [class_name],[clinic_code],[clinic_name],[clinic_py],[amount],[charge_code],[charge_name],[charge_py],[charge_price],[charge_unit] FROM [view_zd_charge_item_HJW_TJ] where deleted_flag = 0 "
					+ " union "
					+ " SELECT [item_class],[class_name],[clinic_code],[clinic_name],[clinic_py],[amount],[charge_code],[charge_name],[charge_py],[charge_price],[charge_unit] FROM [view_zd_charge_item_HJW_HC] where deleted_flag = 0 ";
			TranLogTxt.liswriteEror_to_txt(logname, "查询his视图:" +sql);
			ResultSet his_rs = his_connect.createStatement().executeQuery(sql);

/////////////////// copy to AutoGetHisDataNH.java_insert_his_data() start //////////////////////////////////////////////////////
		Connection connect = null;
		try {
			connect = jqm.getConnection();
			
			String del_clinic_sql = "delete his_clinic_item";
			connect.createStatement().executeUpdate(del_clinic_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目数据列表成功!");
			
			String del_clinic_price_sql = "delete his_clinic_item_v_price_list";
			connect.createStatement().executeUpdate(del_clinic_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目和价表关系数据列表成功!");
			
			String del_price_sql = "delete his_price_list";
			connect.createStatement().executeUpdate(del_price_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧价表数据列表成功!");
		} catch (SQLException e) {
			e.printStackTrace();
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {
				if (connect != null){
					connect.close();
				}
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		int errorNum = 0;
		int clinicNum = 0;
		int clinic_price_listNum = 0;
		int priceNum = 0;
		int index = 0;
		while(his_rs.next()) {
			index++;
			String item_class = his_rs.getString("item_class").trim();
			String class_name = his_rs.getString("class_name");
			if(class_name == null) {
				class_name = "";
			} else {
				class_name = class_name.trim();
			}
			String clinic_py = his_rs.getString("clinic_py");
			if(clinic_py == null) {
				clinic_py = "";
			} else {
				clinic_py = clinic_py.trim();
			}
			String clinic_code = his_rs.getString("clinic_code").trim();
			String clinic_name = his_rs.getString("clinic_name");
			if(clinic_name == null) {
				clinic_name = "";
			} else {
				clinic_name = clinic_name.trim();
			}
			int amount = his_rs.getInt("amount");
			String charge_code = his_rs.getString("charge_code").trim();
			String charge_name = his_rs.getString("charge_name");
			if(charge_name == null) {
				charge_name = "";
			} else {
				charge_name = charge_name.trim();
			}
			String charge_py = his_rs.getString("charge_py");
			if(charge_py == null) {
				charge_py = "";
			} else {
				charge_py = charge_py.trim();
			}
			double charge_price = his_rs.getDouble("charge_price");
			String charge_unit = his_rs.getString("charge_unit");
			if(charge_unit == null) {
				charge_unit = "";
			} else {
				charge_unit = charge_unit.trim();
			}
			
			try {
				connect = jqm.getConnection();
				TranLogTxt.liswriteEror_to_txt(logname, clinic_name+"---"+index);

				String select_clinic_sql = "select * from his_clinic_item where item_code = '"+clinic_code+"' and item_class = '"+item_class+"'";
				ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
				if(!rs.next() && !StringUtil.isEmpty(clinic_code)) {
					String insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
							+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date]) "
							+ " values ('"+item_class+"','"+clinic_code+"','"+clinic_name+"','"+clinic_py+"'"
							+ ",'','"+class_name+"','','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
							TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
					connect.createStatement().executeUpdate(insert_clinic_sql);
					clinicNum++;
				}
				
				if(!StringUtil.isEmpty(clinic_code)) {
					String insert_clinic_price_sql = "insert into his_clinic_item_v_price_list([clinic_item_class],[clinic_item_code],[charge_item_no]"
							+ ",[charge_item_class],[charge_item_code],[charge_item_spec],[amount],[units],[backbill_rule],[create_date],[update_date])"
							+ " values('"+item_class+"','"+clinic_code+"',''"
							+ ",'','"+charge_code+"','','"+amount+"','"+charge_unit+"','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系sql--"+insert_clinic_price_sql);
					connect.createStatement().executeUpdate(insert_clinic_price_sql);
					clinic_price_listNum++;
				}
				
				String select_clinic_price_sql = "select * from his_price_list where item_code = '"+charge_code+"'";
				rs = connect.createStatement().executeQuery(select_clinic_price_sql);
				if(!rs.next()) {
					String insert_price_sql = "insert into his_price_list([item_class],[item_code],[item_name],[item_spec],[units],[price]"
							+ ",[prefer_price],[performed_by],[input_code],[class_on_inp_rcpt],[class_on_outp_rcpt],[class_on_reckoning]"
							+ ",[subj_code],[memo],[start_date],[stop_date],[create_date],[update_date],[is_active]) "
							+ "values('','"+charge_code+"','"+charge_name+"','','"+charge_unit+"','"+charge_price+"'"
							+ ",'"+charge_price+"','','"+charge_py+"','','',''"
							+ ",'','','"+DateTimeUtil.getDateTime()+"','9999-12-31 23:59:59.000','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"','Y')";
							TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表sql--"+insert_price_sql);
					connect.createStatement().executeUpdate(insert_price_sql);
					priceNum++;
				}
			} catch (Exception e) {
				errorNum++;
				e.printStackTrace();
				TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
			} finally {
				try {
					if (connect != null) {
						connect.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+clinicNum+"条，"
				+ "his_clinic_item_v_price_list表"+ clinic_price_listNum+ "条，his_price_list表"+priceNum+"条，问题数据"+errorNum+"条");
		
/////////////////// copy to AutoGetHisDataNH.java_insert_his_data() end //////////////////////////////////////////////////////
			his_rs.close();
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("数据同步成功!");
		} catch (Throwable e) {
			e.printStackTrace();
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
			TranLogTxt.liswriteEror_to_txt(logname, "错误:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			if(his_connect != null) {
				try {
					his_connect.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rb;
	}
}
