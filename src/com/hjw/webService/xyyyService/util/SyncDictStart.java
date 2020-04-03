package com.hjw.webService.xyyyService.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.synjones.framework.persistence.JdbcQueryManager;

public class SyncDictStart {
	//同步xy_zd_charge_item
	public static void xy_zd_charge_item(){
		List<String> strList=new ArrayList<String>();
		strList=xy_zd_charge_itemgetStrs();
		if(strList!=null&&strList.size()>0){
			for(String exam_num:strList){
				List<String[]> strsList=new ArrayList<String[]>();
				strsList=xy_zd_charge_itemLisStrs(exam_num);
				if(strsList!=null&&strsList.size()>0){
					String sum=getSumLisOne(strsList.get(0)[1]);
					String sql="update charging_item set amount='"+sum+"' where exam_num='"+strsList.get(0)[1]+"'";
					runSql(sql);
					for(int i=0;i<strsList.size();i++){
							String sums=getSumLisTwo(strsList.get(i)[0]);
							String sqla="update charging_item set amount='"+sums+"' where exam_num='"+strsList.get(i)[0]+"'";
							String res=runSql(sqla);
							if(res.equals("1")){
								String ssql="update xy_zd_charge_item set action_flag='1' where code ='"+exam_num+"'";
								runSql(ssql);
							}else if(res.equals("0")){
								String ssql="update xy_zd_charge_item set action_flag='2' where code ='"+exam_num+"'";
								runSql(ssql);
							}
					}
				}else{
					List<String> pacsList=new ArrayList<String>();
					pacsList=xy_zd_charge_itemPacsStrs(exam_num);
					for(int i=0;i<pacsList.size();i++){
							String sum=getSumPacs(pacsList.get(i));
							String sql="update charging_item set amount='"+sum+"' where view_num='"+pacsList.get(i)+"'";
							String res=runSql(sql);
							if(res.equals("1")){
								String ssql="update xy_zd_charge_item set action_flag='1' where code ='"+exam_num+"'";
								runSql(ssql);
							}else if(res.equals("0")){
								String ssql="update xy_zd_charge_item set action_flag='2' where code ='"+exam_num+"'";
								runSql(ssql);
							}
					}
				}
			}
		}
	}
	//同步xy_zd_jy_apply_item
	public static void xy_zd_jy_apply_item(){
		List<String[]> strList=new ArrayList<String[]>();
		strList=xy_zd_jy_apply_itemStrs();
		for(int i=0;i<strList.size();i++){
			if(strList.get(i)[1].equals("1")){
				String sum=getSumLisOne(strList.get(i)[0]);
				String sql="update charging_item set amount='"+sum+"' where exam_num='"+strList.get(i)[0]+"'";
				String res=runSql(sql);
				if(res.equals("1")){
					String ssql="update xy_zd_jy_apply_item set action_flag='1' where code ='"+strList.get(i)[0]+"'";
					runSql(ssql);
				}else if(res.equals("0")){
					String ssql="update xy_zd_jy_apply_item set action_flag='2' where code ='"+strList.get(i)[0]+"'";
					runSql(ssql);
				}
			}else if(strList.get(i)[1].equals("2")){
				String sum=getSumLisTwo(strList.get(i)[0]);
				String sql="update charging_item set amount='"+sum+"' where exam_num='"+strList.get(i)[0]+"'";
				String res=runSql(sql);
				if(res.equals("1")){
					String ssql="update xy_zd_jy_apply_item set action_flag='1' where code ='"+strList.get(i)[0]+"'";
					runSql(ssql);	
				}else if(res.equals("0")){
					String ssql="update xy_zd_jy_apply_item set action_flag='2' where code ='"+strList.get(i)[0]+"'";
					runSql(ssql);
				}
			}
		}
	}
	//同步xy_zd_jy_apply_detail
	public static void xy_zd_jy_apply_detail(){
		List<String[]> strList=new ArrayList<String[]>();
		strList=xy_zd_jy_apply_detailStrs();
		for(int i=0;i<strList.size();i++){
			if(strList.get(i)[1].equals("1")){
				String sum=getSumLisOne(strList.get(i)[0]);
				String sql="update charging_item set amount='"+sum+"' where exam_num='"+strList.get(i)[0]+"'";
				String res=runSql(sql);
				if(res.equals("1")){
					String ssql="update xy_zd_jy_apply_detail set action_flag='1' where apply_item ='"+strList.get(i)[0]+"' and detail_item='"+strList.get(i)[2]+"'";
					runSql(ssql);	
				}else if(res.equals("0")){
					String ssql="update xy_zd_jy_apply_detail set action_flag='2' where apply_item ='"+strList.get(i)[0]+"' and detail_item='"+strList.get(i)[2]+"'";
					runSql(ssql);	
				}
			}else if(strList.get(i)[1].equals("2")){
				String sum=getSumLisTwo(strList.get(i)[0]);
				String sql="update charging_item set amount='"+sum+"' where exam_num='"+strList.get(i)[0]+"'";
				String res=runSql(sql);
				if(res.equals("1")){
					String ssql="update xy_zd_jy_apply_detail set action_flag='1' where apply_item ='"+strList.get(i)[0]+"' and detail_item='"+strList.get(i)[2]+"'";
					runSql(ssql);
				}else if(res.equals("0")){
					String ssql="update xy_zd_jy_apply_detail set action_flag='2' where apply_item ='"+strList.get(i)[0]+"' and detail_item='"+strList.get(i)[2]+"'";
					runSql(ssql);
				}
			}
		}
	}
	//同步xy_yz_order_charge
	public static void xy_yz_order_charge(){
		List<String[]> strList=new ArrayList<String[]>();
		strList=xy_yz_order_chargeStrs();
		for(int i=0;i<strList.size();i++){
			String sum=getSumPacs(strList.get(i)[0]);
			String sql="update charging_item set amount='"+sum+"' where view_num='"+strList.get(i)[0]+"'";
			String res=runSql(sql);
			if(res.equals("1")){
				String ssql="update xy_yz_order_charge set action_flag='1' where order_code ='"+strList.get(i)[1]+"' and charge_code='"+strList.get(i)[2]+"'";
				runSql(ssql);	
			}else if(res.equals("0")){
				String ssql="update xy_yz_order_charge set action_flag='2' where order_code ='"+strList.get(i)[1]+"' and charge_code='"+strList.get(i)[2]+"'";
				runSql(ssql);	
			}
		}
	}
	//同步xy_yz_order_item
	public static void xy_yz_order_item(){
		List<String[]> strList=new ArrayList<String[]>();
		strList=xy_yz_order_itemStrs();
		for(int i=0;i<strList.size();i++){
			String sum=getSumPacs(strList.get(i)[0]);
			String sql="update charging_item set amount='"+sum+"' where view_num='"+strList.get(i)[0]+"'";
			String res=runSql(sql);
			if(res.equals("1")){
				String ssql="update xy_yz_order_item set action_flag='1' where order_code ='"+strList.get(i)[1]+"' ";
				runSql(ssql);	
			}else if(res.equals("0")){
				String ssql="update xy_yz_order_item set action_flag='2' where order_code ='"+strList.get(i)[1]+"' ";
				runSql(ssql);	
			}
		}
	}
	public static void xy_jc_zd_exam_sub_type(){
		List<String> strList=new ArrayList<String>();
		strList=xy_jc_zd_exam_sub_typeStrs();
		for(int i=0;i<strList.size();i++){
			String sum=getSumPacs(strList.get(i));
			String sql="update charging_item set amount='"+sum+"' where view_num='"+strList.get(i)+"'";
			runSql(sql);
			String ssql="update xy_jc_zd_exam_sub_type set action_flag='1' where type_code+sub_code ='"+strList.get(i)+"' ";
			runSql(ssql);
		
		}
	}
	private static List<String> xy_jc_zd_exam_sub_typeStrs(){
		List<String> strsList=new ArrayList<String>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select zs.type_code+zs.sub_code as app from  xy_jc_zd_exam_sub_type zs  "
				+ " where zs.action_date>='"+DateTimeUtil.shortFmt3(new Date())+"' and zs.action_flag='0'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String strs="";
				strs=rs.getString("app");
				strsList.add(strs);
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
		return strsList;
	}
	private static List<String[]> xy_yz_order_itemStrs(){
		List<String[]> strsList=new ArrayList<String[]>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select zs.type_code+zs.sub_code as app,zoi.order_code as order_code from xy_yz_order_item zoi  join xy_jc_zd_exam_sub_type zs on zoi.order_code=zs.yz_order_code "
				+ " where zoi.action_date>='"+DateTimeUtil.shortFmt3(new Date())+"' and zoi.action_flag='0'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String[] strs=new String[2];
				strs[0]=rs.getString("app");
				strs[0]=rs.getString("order_code");
				strsList.add(strs);
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
		return strsList;
	}
	private static List<String[]> xy_yz_order_chargeStrs(){
		List<String[]> strsList=new ArrayList<String[]>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select zs.type_code+zs.sub_code as app,zoc.order_code as order_code,zoc.charge_code as charge_code from xy_yz_order_charge zoc join xy_jc_zd_exam_sub_type zs on zoc.order_code=zs.yz_order_code "
				+ " where zoc.action_date>='"+DateTimeUtil.shortFmt3(new Date())+"' and zoc.action_flag='0'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String[] strs=new String[3];
				strs[0]=rs.getString("app");
				strs[1]=rs.getString("order_code");
				strs[2]=rs.getString("charge_code");
				strsList.add(strs);
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
		return strsList;
	}
	private static List<String[]> xy_zd_jy_apply_detailStrs(){
		List<String[]> strsList=new ArrayList<String[]>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select  zi.apply_item as app,zi.is_item as isitem,zd.detail_item as detail from xy_zd_jy_apply_detail zd join xy_zd_jy_apply_item zi on zd.apply_item=zi.apply_item "
				+ " where zd.action_date>='"+DateTimeUtil.shortFmt3(new Date())+"' and zd.action_flag='0'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String[] strs=new String[3];
				strs[0]=rs.getString("app");
				strs[1]=rs.getString("isitem");
				strs[2]=rs.getString("detail");
				strsList.add(strs);
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
		return strsList;
	}
	private static List<String[]> xy_zd_jy_apply_itemStrs(){
		List<String[]> strsList=new ArrayList<String[]>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select apply_item as app,is_item as isitem from xy_zd_jy_apply_item "
				+ " where action_date>='"+DateTimeUtil.shortFmt3(new Date())+"' and action_flag='0'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String[] strs=new String[2];
				strs[0]=rs.getString("app");
				strs[1]=rs.getString("isitem");
				strsList.add(strs);
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
		return strsList;
	}
	private static List<String> xy_zd_charge_itemgetStrs(){
		List<String> strList=new ArrayList<String>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select code as code  from xy_zd_charge_item "
				+ " where action_date>='"+DateTimeUtil.shortFmt3(new Date())+"' and action_flag='0'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String str=rs.getString("code");
				strList.add(str);
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
		return strList;
	}
	//查询lis项目
	private static List<String[]> xy_zd_charge_itemLisStrs(String exam_num){
		List<String[]> strList=new ArrayList<String[]>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="  select zad.apply_item as app,zad.detail_item as det,zja.is_item as isitem "
				+ " from xy_zd_charge_item zci  "
				+ "  join xy_zd_jy_apply_item zja on zci.code=zja.his_mz_code  "
				+ "  join xy_zd_jy_apply_detail zad on zad.detail_item=zja.apply_item  where code = '"+exam_num+"'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String[] str=new String[3];
				str[0]=rs.getString("app");
				str[1]=rs.getString("det");
				str[2]=rs.getString("isitem");
				strList.add(str);
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
		return strList;
	}
	//查询pacs项目
	private static List<String> xy_zd_charge_itemPacsStrs(String exam_num){
		List<String> strList=new ArrayList<String>();
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="  select zje.type_code+zje.sub_code as code from xy_zd_charge_item zci"
				+ " join xy_yz_order_charge zoc on zci.code=zoc.charge_code "
				+ " join xy_yz_order_item zoi on zoc.order_code=zoi.order_code "
				+ " join xy_jc_zd_exam_sub_type zje on zje.yz_order_code=zoi.order_code where zci.code='"+exam_num+"'";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				String str="";
				str=rs.getString("code");
				strList.add(str);
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
		return strList;
	}
	//查询lis组合项目价格
	private static String getSumLisTwo(String exam_num){
		String str="";
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select sum(charge_price) as he "
				+ "from xy_zd_charge_item where code in"
				+ " (select his_mz_code from xy_zd_jy_apply_item where apply_item in"
				+ "(select zd.detail_item from xy_zd_jy_apply_item z,xy_zd_jy_apply_detail zd  "
				+ "where z.apply_item=zd.apply_item and z.is_item=2 and  z.apply_item in "
				+ "(select exam_num from charging_item where exam_num='"+exam_num+"'))) ";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				str=rs.getString("he");
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
		return str;
	}
	//查询pacs项目价格
	private static String getSumPacs(String exam_num){
		String str="";
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql="select SUM(z.charge_price*amount) as he  "
				+ "from xy_yz_order_charge y "
				+ "inner join xy_zd_charge_item z on y.charge_code=z.code "
				+ "inner join xy_yz_order_item yz on yz.order_code=y.order_code"
				+ " where y.order_code= "
				+ "(select order_code from xy_yz_order_item  where  order_code= "
				+ "(select yz_order_code from xy_jc_zd_exam_sub_type  where type_code+sub_code='"+exam_num+"')) ";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				str=rs.getString("he");
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
		return str;
	}
	//查询lis单个项目价格
	private static String getSumLisOne(String exam_num){
		String str="";
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		String sql=" select sum(charge_price) as he "
				+ "from xy_zd_charge_item where code in "
				+ " (select his_mz_code from xy_zd_jy_apply_item where apply_item in"
				+ "(select zd.detail_item from xy_zd_jy_apply_detail zd  where   zd.detail_item in"
				+ "(select exam_num from charging_item where exam_num='"+exam_num+"'))) ";
		Connection connection = null;
		Statement statement = null;
		try {
			// 读取记录数
			connection = jdbcQueryManager.getConnection();
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				str=rs.getString("he");
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
		return str;
	}
	
	private static String runSql(String sql){
		WebApplicationContext wac =ContextLoader.getCurrentWebApplicationContext();
		JdbcQueryManager jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
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
}
