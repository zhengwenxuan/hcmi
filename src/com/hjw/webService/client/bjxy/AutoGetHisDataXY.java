package com.hjw.webService.client.bjxy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.interfaces.DBServer.SqlServerDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.synjones.framework.persistence.JdbcQueryManager;

public class AutoGetHisDataXY {
	private static JdbcQueryManager jqm;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public ResultHeader getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "西苑-北大医信 自动同步his数据开始");
		ResultHeader rb = new ResultHeader();
		Connection his_connect = null;
		
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			
			his_connect = SqlServerDatabaseSource.getConnection(url.split("&")[0], url.split("&")[1], url.split("&")[2]);
			String sql = "SELECT [item_class],[class_name],[clinic_code],[clinic_name],[clinic_py],[amount],[charge_code],[charge_name],[charge_py],[charge_price],[charge_unit] FROM [view_zd_charge_item_HJW] "
					+ " union "
					+ " SELECT [item_class],[class_name],[clinic_code],[clinic_name],[clinic_py],[amount],[charge_code],[charge_name],[charge_py],[charge_price],[charge_unit] FROM [view_zd_charge_item_HJW_TJ]";
			TranLogTxt.liswriteEror_to_txt(logname, "查询his视图:" +sql);
			ResultSet his_rs = his_connect.createStatement().executeQuery(sql);
			
			TranLogTxt.liswriteEror_to_txt(logname, "同步his基础数据===start===");
			insert_his_data(logname, his_rs);
			his_rs.close();
			TranLogTxt.liswriteEror_to_txt(logname, "同步his基础数据===end===");
			
			TranLogTxt.liswriteEror_to_txt(logname, "更新charging_item价格信息===start===");
			updateHIsPriceSynchro(logname);
			TranLogTxt.liswriteEror_to_txt(logname, "更新charging_item价格信息===end===");
			
			rb.setTypeCode("AA");
			rb.setText("数据同步成功!");
			
		} catch (Throwable e) {
			rb.setTypeCode("AE");
			rb.setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
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
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}

	private void insert_his_data(String logname, ResultSet his_rs) throws SQLException {
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
	while(his_rs.next()) {
		String item_class = his_rs.getString("item_class").trim();
		String class_name = his_rs.getString("class_name").trim();
		String clinic_py = his_rs.getString("clinic_py").trim();
		String clinic_code = his_rs.getString("clinic_code").trim();
		String clinic_name = his_rs.getString("clinic_name").trim();
		int amount = his_rs.getInt("amount");
		String charge_code = his_rs.getString("charge_code").trim();
		String charge_name = his_rs.getString("charge_name").trim();
		String charge_py = his_rs.getString("charge_py").trim();
		double charge_price = his_rs.getDouble("charge_price");
		String charge_unit = his_rs.getString("charge_unit").trim();
		
		try {
			connect = jqm.getConnection();
	
			String select_clinic_sql = "select * from his_clinic_item where item_code = '"+clinic_code+"' and item_class = '"+item_class+"'";
			ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
			if(!rs.next()) {
				String insert_clinic_sql = "insert into his_clinic_item([item_class],[item_code],[item_name],[input_code]"
						+ ",[expand1],[expand2],[expand3],[item_status] ,[create_date],[update_date]) "
						+ " values ('"+item_class+"','"+clinic_code+"','"+clinic_name+"','"+clinic_py+"'"
						+ ",'','"+class_name+"','','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
						TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
				connect.createStatement().executeUpdate(insert_clinic_sql);
				clinicNum++;
			}
			
			String insert_clinic_price_sql = "insert into his_clinic_item_v_price_list([clinic_item_class],[clinic_item_code],[charge_item_no]"
					+ ",[charge_item_class],[charge_item_code],[charge_item_spec],[amount],[units],[backbill_rule],[create_date],[update_date])"
					+ " values('"+item_class+"','"+clinic_code+"',''"
					+ ",'','"+charge_code+"','','"+amount+"','"+charge_unit+"','','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
					TranLogTxt.liswriteEror_to_txt(logname, "插入HIS价表和诊疗项目关系sql--"+insert_clinic_price_sql);
			connect.createStatement().executeUpdate(insert_clinic_price_sql);
			clinic_price_listNum++;
		
			
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
	}
	
	private boolean updateHIsPriceSynchro(String logname) {
/////////////////// copy to AutoGetHisDataXY.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
		String sql = "";
		ResultSet charging_item_rs = null;
		ResultSet clinic_item_rs = null;
		ResultSet price_list_rs = null;
		
		Connection connection = null;
		boolean fal = false;
		try {
			connection = jqm.getConnection();
			sql = " SELECT  c.id,c.his_num,c.item_class,c.amount,c.hiscodeClass FROM   charging_item c  where  isActive='Y' and  his_num <> '' ";
			TranLogTxt.liswriteEror_to_txt(logname,"查询收费项目sql: " + sql);
			charging_item_rs = connection.createStatement().executeQuery(sql);
			while (charging_item_rs.next()) {
				String hiscodeClass = charging_item_rs.getString("hiscodeClass");
				if("1".equals(hiscodeClass)) {
					//his_num对应价表
					sql = "	SELECT p.price FROM his_price_list p   where  "
							+ "	getdate()>=p.start_date and GETDATE()<=p.stop_date  and  p.item_code= '"
							+ charging_item_rs.getString("his_num") + "'  and  " + " p.item_class ='" + charging_item_rs.getString("item_class")
							+ "'";
					TranLogTxt.liswriteEror_to_txt(logname,"查询价表sql: " + sql);
					price_list_rs = connection.createStatement().executeQuery(sql);
					if(price_list_rs.next()) {
						double amount = price_list_rs.getDouble("price");
						if (amount != charging_item_rs.getDouble("amount")) {
							sql = " update charging_item set  amount = " + amount + "  where  id = '" + charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname,"按照价表价格更新收费项目sql: " + sql);
							connection.createStatement().executeUpdate(sql);
							
//							try {
//								String upsql = "update examinfo_charging_item set item_amount="
//										+ amount + ",amount=" + amount
//										+ "*itemnum,personal_pay=" + amount
//										+ " *itemnum where id "
//										+ "in(select eci.id from examinfo_charging_item eci,exam_info ei where ei.id=eci.examinfo_id "
//										+ " and ei.status='Y' " + " and eci.isActive='Y' "
//										+ " and ei.exam_type='G' " + " and eci.exam_indicator='G' "
//										+ " and eci.charge_item_id=" + charging_item_rs.getLong("id") + " " + ")";
//								connection.createStatement().execute(upsql);
//								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
//								
//								upsql = " update examinfo_charging_item set item_amount="
//										+ amount + ",amount=" + amount
//										+ "*itemnum,personal_pay=" + amount
//										+ "*itemnum where id "
//										+ "in(select eci.id  from examinfo_charging_item eci,exam_info ei,batch b where ei.id=eci.examinfo_id "
//										+ "and ei.status='Y' " + "and eci.isActive='Y' "
//										+ "and ei.exam_type='T' " + "and b.is_Active='Y' "
//										+ "and b.id=ei.batch_id " + "and b.overflag=0 "
//										+ "and eci.exam_indicator='G' " + "and eci.charge_item_id="
//										+ charging_item_rs.getLong("id") + " " + ")";
//								connection.createStatement().execute(upsql);
//								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
//								
//								upsql = "update examinfo_charging_item set item_amount="
//										+ amount + ",amount="
//										+ amount + "*itemnum,team_pay="
//										+ amount + "*itemnum where id "
//										+ "in(select eci.id from examinfo_charging_item eci,exam_info ei,batch b where ei.id=eci.examinfo_id  "
//										+ "and ei.status='Y'  " + "and eci.isActive='Y'  "
//										+ "and ei.exam_type='T'  " + "and b.is_Active='Y'  "
//										+ "and b.id=ei.batch_id  " + "and b.overflag=0  "
//										+ "and eci.exam_indicator='T'  " + "and eci.charge_item_id="
//										+ charging_item_rs.getLong("id") + " " + ")";
//								connection.createStatement().execute(upsql);
//								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
//							}catch(Exception ex){
//								
//							}
							
							connection.createStatement()
							.execute("update set_charging_item set amount=" + amount
							+ "*itemnum,discount=10,item_amount=" + amount
							+ "  where charging_item_id='" + charging_item_rs.getLong("id") + "' ");
							String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
									+ charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
							ResultSet rss = connection.createStatement().executeQuery(setsql);
							if (rss.next()) {
								String upsql = "update exam_set set set_discount=10,"
										+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "'),set_amount=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "') " + "where id='" + rss.getString("exam_set_id") + "'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
								connection.createStatement().execute(upsql);										
							}
							rss.close();
						}
					}
					price_list_rs.close();
				} else {
					sql = "	SELECT [dbo].[fun_GetPriceByHisClinicItemCode](item_code,item_class) as price FROM his_clinic_item where	item_code = '" + charging_item_rs.getString("his_num") 
						+ "'and " + " item_class ='" + charging_item_rs.getString("item_class") + "'";
					TranLogTxt.liswriteEror_to_txt(logname,"查询诊疗表sql: " + sql);
					clinic_item_rs = connection.createStatement().executeQuery(sql);
					if (clinic_item_rs.next()) {//his_num对应诊疗项目
						double amount = clinic_item_rs.getDouble("price");
						if (amount != charging_item_rs.getDouble("amount")) {
							sql = " update charging_item set  amount = " + amount + "  where  id = '" + charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname,"按照诊疗价格更新收费项目sql: " + sql);
							connection.createStatement().executeUpdate(sql);
							
//							try {
//								String upsql = "update examinfo_charging_item set item_amount="
//										+ amount + ",amount=" + amount
//										+ "*itemnum,personal_pay=" + amount
//										+ " *itemnum where id "
//										+ "in(select eci.id from examinfo_charging_item eci,exam_info ei where ei.id=eci.examinfo_id "
//										+ " and ei.status='Y' " + " and eci.isActive='Y' "
//										+ " and ei.exam_type='G' " + " and eci.exam_indicator='G' "
//										+ " and eci.charge_item_id=" + charging_item_rs.getLong("id") + " " + ")";
//								connection.createStatement().execute(upsql);
//								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
//								
//								upsql = " update examinfo_charging_item set item_amount="
//										+ amount + ",amount=" + amount
//										+ "*itemnum,personal_pay=" + amount
//										+ "*itemnum where id "
//										+ "in(select eci.id  from examinfo_charging_item eci,exam_info ei,batch b where ei.id=eci.examinfo_id "
//										+ "and ei.status='Y' " + "and eci.isActive='Y' "
//										+ "and ei.exam_type='T' " + "and b.is_Active='Y' "
//										+ "and b.id=ei.batch_id " + "and b.overflag=0 "
//										+ "and eci.exam_indicator='G' " + "and eci.charge_item_id="
//										+ charging_item_rs.getLong("id") + " " + ")";
//								connection.createStatement().execute(upsql);
//								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
//								
//								upsql = "update examinfo_charging_item set item_amount="
//										+ amount + ",amount="
//										+ amount + "*itemnum,team_pay="
//										+ amount + "*itemnum where id "
//										+ "in(select eci.id from examinfo_charging_item eci,exam_info ei,batch b where ei.id=eci.examinfo_id  "
//										+ "and ei.status='Y'  " + "and eci.isActive='Y'  "
//										+ "and ei.exam_type='T'  " + "and b.is_Active='Y'  "
//										+ "and b.id=ei.batch_id  " + "and b.overflag=0  "
//										+ "and eci.exam_indicator='T'  " + "and eci.charge_item_id="
//										+ charging_item_rs.getLong("id") + " " + ")";
//								connection.createStatement().execute(upsql);
//								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
//							}catch(Exception ex){
//								
//							}
							
							connection.createStatement()
							.execute("update set_charging_item set amount=" + amount
							+ "*itemnum,discount=10,item_amount=" + amount
							+ "  where charging_item_id='" + charging_item_rs.getLong("id") + "' ");
							String setsql = "select exam_set_id from set_charging_item where charging_item_id='"
									+ charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + setsql);
							ResultSet rss = connection.createStatement().executeQuery(setsql);
							if (rss.next()) {
								String upsql = "update exam_set set set_discount=10,"
										+ "price=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "'),set_amount=(select SUM(a.amount) from set_charging_item  a " + "where a.exam_set_id='"
										+ rss.getString("exam_set_id") + "') " + "where id='" + rss.getString("exam_set_id") + "'";
								TranLogTxt.liswriteEror_to_txt(logname, "res: :操作语句： " + upsql);
								connection.createStatement().execute(upsql);										
							}
							rss.close();
						}
					}
					clinic_item_rs.close();
				}
			}
			charging_item_rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			fal = true;
		} finally {
			try {
				if (price_list_rs != null) {
					price_list_rs.close();
				}
				if (clinic_item_rs != null) {
					clinic_item_rs.close();
				}
				if (charging_item_rs != null) {
					charging_item_rs.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return fal;
/////////////////// copy to AutoGetHisDataXY.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
		}
}
