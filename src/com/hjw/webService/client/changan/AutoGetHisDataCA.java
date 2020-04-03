package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.HisClinicItemPriceDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.synjones.framework.persistence.JdbcQueryManager;

public class AutoGetHisDataCA {
	private static JdbcQueryManager jqm;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public ResultHeader getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "天健-长安医院 自动同步his数据开始");
		ResultHeader rb = new ResultHeader();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			TranLogTxt.liswriteEror_to_txt(logname, "同步his基础数据===start===");
			Connection his_connect = null;
			try {
				//1，连接his数据库
				String his_dburl = url.split("&")[0];
				String his_user = url.split("&")[1];
				String his_passwd = url.split("&")[2];
				his_connect = OracleDatabaseSource.getConnection(his_dburl, his_user, his_passwd);
				
				//2，查询诊疗
				String select_CLINIC_sql = "select * from CLINIC_ITEM_DICT";
				ResultSet CLINIC_rs = his_connect.createStatement().executeQuery(select_CLINIC_sql);
				List<HisClinicItem> clinicList = new ArrayList<>(30000);
				while(CLINIC_rs.next()) {
					HisClinicItem clinicItem = new HisClinicItem();
//					clinicItem.setItem_class(rs0.getString("ITEM_CLASS"));
					clinicItem.setItem_code(CLINIC_rs.getString("ITEM_CODE"));
					clinicItem.setItem_name(CLINIC_rs.getString("ITEM_NAME"));
					clinicItem.setInput_code(CLINIC_rs.getString("INPUT_CODE"));
					clinicList.add(clinicItem);
				}
				CLINIC_rs.close();
				
				//3，查询价表
				String select_PRICE_LIST_sql = "select * from PRICE_LIST";
				ResultSet PRICE_rs = his_connect.createStatement().executeQuery(select_PRICE_LIST_sql);
				List<HisPriceList> priceList = new ArrayList<>(30000);
				while(PRICE_rs.next()) {
					HisPriceList price = new HisPriceList();
//					price.setItem_class(PRICE_rs.getString("ITEM_CLASS"));
					price.setItem_code(PRICE_rs.getString("ITEM_CODE"));
					price.setItem_name(PRICE_rs.getString("ITEM_NAME"));
					price.setItem_spec(PRICE_rs.getString("ITEM_SPEC"));
					price.setUnits(PRICE_rs.getString("UNITS"));
					price.setPrice(PRICE_rs.getDouble("PRICE"));
					price.setPrefer_price(PRICE_rs.getDouble("PREFER_PRICE"));
					price.setPerformed_by(PRICE_rs.getString("PERFORMED_BY"));
					price.setInput_code(PRICE_rs.getString("INPUT_CODE"));
					price.setClass_on_inp_rcpt(PRICE_rs.getString("CLASS_ON_INP_RCPT"));
					price.setClass_on_outp_rcpt(PRICE_rs.getString("CLASS_ON_OUTP_RCPT"));
					price.setClass_on_reckoning(PRICE_rs.getString("CLASS_ON_RECKONING"));
					price.setSubj_code(PRICE_rs.getString("SUBJ_CODE"));
					price.setMemo(PRICE_rs.getString("MEMO"));
					price.setStart_date(PRICE_rs.getDate("START_DATE"));
					
					
					price.setStop_date(PRICE_rs.getDate("STOP_DATE"));
					priceList.add(price);
				}
				PRICE_rs.close();
				
				//3，查询价表
				String select_CLINIC_PRICE_LIST_sql = "select * from PRICE_LIST";
				ResultSet CLINIC_PRICE_rs = his_connect.createStatement().executeQuery(select_CLINIC_PRICE_LIST_sql);
				List<HisClinicItemPriceDTO> clinicPriceList = new ArrayList<>(30000);
				while(CLINIC_PRICE_rs.next()) {
					HisClinicItemPriceDTO clinicPrice = new HisClinicItemPriceDTO();
					clinicPrice.setClinic_item_class(CLINIC_PRICE_rs.getString("CLINIC_ITEM_CLASS"));
					clinicPrice.setClinic_item_code(CLINIC_PRICE_rs.getString("CLINIC_ITEM_CODE"));
					clinicPrice.setCharge_item_no(CLINIC_PRICE_rs.getInt("CHARGE_ITEM_NO"));
					clinicPrice.setCharge_item_classs(CLINIC_PRICE_rs.getString("CHARGE_ITEM_CLASS"));
					clinicPrice.setCharge_item_code(CLINIC_PRICE_rs.getString("CHARGE_ITEM_CODE"));
					clinicPrice.setCharge_item_spec(CLINIC_PRICE_rs.getString("CHARGE_ITEM_SPEC"));
					clinicPrice.setAmount(CLINIC_PRICE_rs.getInt("AMOUNT"));
					clinicPrice.setUnits(CLINIC_PRICE_rs.getString("UNITS"));
					clinicPriceList.add(clinicPrice);
				}
				CLINIC_PRICE_rs.close();
				
				System.out.println(clinicList.size());
				System.out.println(priceList.size());
				System.out.println(clinicPriceList.size());
				configService.delete_his_data(logname);
				configService.insertClinicList_direct(logname, clinicList);
				configService.insertPriceList_direct(logname, priceList);
				configService.insertClinicPriceList_direct(logname, clinicPriceList);
			} catch (Throwable ex) {
				TranLogTxt.liswriteEror_to_txt(logname, "res:向his库写入数据错误：" + com.hjw.interfaces.util.StringUtil.formatException(ex));
				try{
					his_connect.close();
				}catch(Exception et){}
				rb.setTypeCode("AE");
				rb.setText("连接his库失败");
			} finally {
				try {
					if (his_connect != null) {
						his_connect.close();
					}
				} catch (Exception e) {}
			}
			TranLogTxt.liswriteEror_to_txt(logname, "同步his基础数据===end===");
			
			TranLogTxt.liswriteEror_to_txt(logname, "更新charging_item价格信息===start===");
			updateHIsPriceSynchro(logname);
			TranLogTxt.liswriteEror_to_txt(logname, "更新charging_item价格信息===end===");
			
			rb.setTypeCode("AA");
			rb.setText("数据同步成功!");
		
		} catch (Throwable e) {
			rb.setTypeCode("AE");
			rb.setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}

	private boolean updateHIsPriceSynchro(String logname) {
		/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
		String sql = "";
		ResultSet charging_item_rs = null;
		ResultSet clinic_item_rs = null;
		ResultSet price_list_rs = null;
		
		Connection connection = null;
		boolean fal = false;
		try {
			connection = this.jqm.getConnection();
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
						}
					}
					price_list_rs.close();
				} else {
					sql = "	SELECT price FROM his_clinic_item where	item_code = '" + charging_item_rs.getString("his_num") 
						+ "'and " + " item_class ='" + charging_item_rs.getString("item_class") + "'";
					TranLogTxt.liswriteEror_to_txt(logname,"查询诊疗表sql: " + sql);
					clinic_item_rs = connection.createStatement().executeQuery(sql);
					if (clinic_item_rs.next()) {//his_num对应诊疗项目
						double amount = clinic_item_rs.getDouble("price");
						if (amount != charging_item_rs.getDouble("amount")) {
							sql = " update charging_item set  amount = " + amount + "  where  id = '" + charging_item_rs.getLong("id") + "'";
							TranLogTxt.liswriteEror_to_txt(logname,"按照诊疗价格更新收费项目sql: " + sql);
							connection.createStatement().executeUpdate(sql);
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
		/////////////////// copy to AutoGetHisDataNH.java_updateHIsPriceSynchro() start //////////////////////////////////////////////////////
		}
}
