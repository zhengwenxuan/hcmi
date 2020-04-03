package com.hjw.webService.client.dashiqiao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.webService.client.dashiqiao.HisDataBean.HisClinicItemDSQ;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.service.ChargingItemService;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageDSQ {

	private static JdbcQueryManager jqm;
	private static ConfigService configService;
	private static ChargingItemService chargingItemService;
	private List<HisClinicItem> clinicList = new ArrayList<HisClinicItem>();
	/*private List<HisClinicItemPriceDTO> clinicPriceList = new ArrayList<HisClinicItemPriceDTO>();
	private List<HisPriceList> priceList = new ArrayList<HisPriceList>();*/
	
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		chargingItemService = (ChargingItemService) wac.getBean("chargingItemService");
	}
	
	public String getMessage(String messages, String logname) {
		ResultHeader rb = new ResultHeader();
		rb.setTypeCode("AE");
		HisClinicItemDSQ clinic = new Gson().fromJson(messages, HisClinicItemDSQ.class);
		
		insert_his_data(logname, clinic);
		
		boolean fly = updateHIsPriceSynchro(logname);
		
		TranLogTxt.liswriteEror_to_txt(logname, "同步体检收费项目价表数据===end===,"+fly+"");
		if(fly==true){
			rb.setTypeCode("AA");
			rb.setText("收费价表同步成功!!");
		}
		String res = new Gson().toJson(rb, ResultHeader.class);
		return res;
	}

	//更新体检收费项目表 价格
		private boolean updateHIsPriceSynchro(String logname) {
			/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
			String sql = "";
			ResultSet charging_item_rs = null;
			ResultSet clinic_item_rs = null;
			
			Connection connection = null;
			boolean fal = false;
			try {
				connection = this.jqm.getConnection();
				sql = " SELECT  c.id,c.his_num,c.item_class,c.amount,c.hiscodeClass,item_pinyin FROM   charging_item c  where  isActive='Y' and  his_num <> '' ";
				TranLogTxt.liswriteEror_to_txt(logname,"查询收费项目sql: " + sql);
				//查询所有  有效的 his_num不为空的 收费项目
				charging_item_rs = connection.createStatement().executeQuery(sql);
				while (charging_item_rs.next()) {
					
					//String hiscodeClass = charging_item_rs.getString("hiscodeClass");
					
						sql = "	SELECT price,expand1,expand2 FROM his_clinic_item where	item_code = '" + charging_item_rs.getString("his_num") +"' and expand1='"+charging_item_rs.getString("item_pinyin")+"'";
							
						/*if(charging_item_rs.getString("item_class").equals("") && charging_item_rs.getString("item_class") !=null){
							sql	+= " and  item_class ='" + charging_item_rs.getString("item_class") + "'";
						}*/
						TranLogTxt.liswriteEror_to_txt(logname,"查询诊疗表sql: " + sql);
						
						//查询所有 item_code==his_num 不为空的 诊疗项目
						clinic_item_rs = connection.createStatement().executeQuery(sql);
						
						
						while (clinic_item_rs.next()) {//his_num对应诊疗项目
							double amount = clinic_item_rs.getDouble("price");
							String item_pinyin = clinic_item_rs.getString("expand1");
							TranLogTxt.liswriteEror_to_txt(logname,"诊疗项目价格: " + amount);
							TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格: " + charging_item_rs.getDouble("amount"));
							TranLogTxt.liswriteEror_to_txt(logname,"收费项目价格是否和诊疗价格相等: " + (amount != charging_item_rs.getDouble("amount"))+"");
							if (amount != charging_item_rs.getDouble("amount")) {
								sql = " update charging_item set  amount = " + amount + ",item_pinyin='"+item_pinyin+"'  where  id = '" + charging_item_rs.getLong("id") + "'";
								
								TranLogTxt.liswriteEror_to_txt(logname,"按照诊疗价格更新收费项目sql: " + sql);
								connection.createStatement().executeUpdate(sql);
								fal = true;
								
							}
						}
						
						
				}
				charging_item_rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				fal = false;
			} finally {
				try {
					
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
					fal = false;
				}
			}
			return fal;
			/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
			}

	private void insert_his_data(String logname, HisClinicItemDSQ clinic) {
		Connection connect = null;
		int errorNum = 0;
		int clinicNum = 0;
		try {
			
			connect = jqm.getConnection();
			
			String del_clinic_sql = "delete his_clinic_item where item_code = '"+clinic.getITEM_CODE()+"' and expand1='"+clinic.getITEM_ABBREVIATION()+"' ";
			connect.createStatement().executeUpdate(del_clinic_sql);
			TranLogTxt.liswriteEror_to_txt(logname, "删除系统旧诊疗项目数据列表成功!");
			
			
			
			
			String select_clinic_sql = "select * from his_clinic_item where item_code = '"+clinic.getITEM_CODE()+"' and expand1='"+clinic.getITEM_ABBREVIATION()+"' ";
			TranLogTxt.liswriteEror_to_txt(logname, "查询收费项目"+select_clinic_sql);
			ResultSet rs = connect.createStatement().executeQuery(select_clinic_sql);
			if (!rs.next()) {
				String insert_clinic_sql = "insert into his_clinic_item([price],[item_class],[item_code],[item_name],[input_code]"
						+ ",[expand1],[expand2],[expand3] ,[create_date],[update_date]) "
						+ " values ('"+clinic.getITEM_PRICE()+"','','"+clinic.getITEM_CODE()+"','"+clinic.getITEM_NAME()+"',''"
						+ ",'"+clinic.getITEM_ABBREVIATION()+"','"+clinic.getITEM_PRICE()+"','"+clinic.getEXEC_DEPT_CODE()+"','"+DateTimeUtil.getDateTime()+"','"+DateTimeUtil.getDateTime()+"')";
				
			TranLogTxt.liswriteEror_to_txt(logname, "插入HIS诊疗项目sql--"+insert_clinic_sql);
			connect.createStatement().executeUpdate(insert_clinic_sql);
				
			}else{
				String update_clinic_sql = " update his_clinic_item set price='"+clinic.getITEM_PRICE()+"',expand3='"+clinic.getEXEC_DEPT_CODE()+"'where item_code = '"+clinic.getITEM_CODE()+"' and expand1='"+clinic.getITEM_ABBREVIATION()+"' ";
				connect.createStatement().executeUpdate(update_clinic_sql);
				TranLogTxt.liswriteEror_to_txt(logname, "更新HIS诊疗项目sql--"+update_clinic_sql);
			}
			
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
		TranLogTxt.liswriteEror_to_txt(logname, "数据同步结束，成功插入his_clinic_item表"+clinicNum+"条，问题数据"+errorNum+"条");
	}
}
