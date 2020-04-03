package com.hjw.webService.client.qiyang;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.synjones.framework.persistence.JdbcQueryManager;


public class test {

	
	



	public static void main(String[] args) {
		String his_req_no="";
		System.err.println(1111);
		
		 try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 Connection conn =  null;
		try {
			System.err.println(2222);
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=qy", "sa", "HUOjianwa010");
			System.err.println(3333);
			String sql = "select distinct zrhi.his_req_no from charging_summary_single css,charging_detail_single cds,zl_req_his_item zrhi "
			 		+ " where cds.summary_id=css.id and cds.charging_item_id=zrhi.charging_item_code  and zrhi.flay='0' and css.is_active='Y' "
			 		+ " and css.req_num='' and css.exam_id='' and zrhi.exam_num=''";
			
			//String sql = "select his_req_no from zl_req_his_item where exam_num='1905150001'";
			   Statement statement = conn.createStatement();
			   System.err.println(4444);
				
			   ResultSet rs = statement.executeQuery(sql);
				System.err.println(sql);
			   System.err.println(5555);

				while (rs.next()) {
					System.err.println(sql+"==================6666-----4");
					
					his_req_no = rs.getString("his_req_no");
					
					System.err.println(sql+"==================7777-----5");
					
				}
				rs.close();
				conn.close();
				statement.close();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.err.println(his_req_no);
		 
		 
	}
}
