package com.hjw.webService.client.changan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.DTO.HisClinicItemPriceDTO;
import com.hjw.interfaces.DBServer.OracleDatabaseSource;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHisBody;
import com.hjw.wst.domain.HisClinicItem;
import com.hjw.wst.domain.HisPriceList;
import com.synjones.framework.persistence.JdbcQueryManager;

public class HISDataSynchronizingMessageCA {

	private static JdbcQueryManager jqm;
	private static ConfigService configService;
	
	static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jqm = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	
	public ResultHisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "天健-长安医院 自动同步his数据开始");
		ResultHisBody rb = new ResultHisBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "url:" + url);
			TranLogTxt.liswriteEror_to_txt(logname, "同步his基础数据===start===");
			Connection his_connect = null;
			try {
				TranLogTxt.liswriteEror_to_txt(logname, "1，连接his数据库");
				String his_dburl = url.split("&")[0];
				String his_user = url.split("&")[1];
				String his_passwd = url.split("&")[2];
				his_connect = OracleDatabaseSource.getConnection(his_dburl, his_user, his_passwd);
				
				TranLogTxt.liswriteEror_to_txt(logname, "2，查询诊疗");
				String select_CLINIC_sql = "select * from CLINIC_ITEM_DICT";
				ResultSet CLINIC_rs = his_connect.createStatement().executeQuery(select_CLINIC_sql);
				List<HisClinicItem> clinicList = new ArrayList<>(30000);
				while(CLINIC_rs.next()) {
					HisClinicItem clinicItem = new HisClinicItem();
					clinicItem.setItem_class(CLINIC_rs.getString("ITEM_CLASS"));
					clinicItem.setItem_code(CLINIC_rs.getString("ITEM_CODE"));
					clinicItem.setItem_name(CLINIC_rs.getString("ITEM_NAME"));
					clinicItem.setInput_code(CLINIC_rs.getString("INPUT_CODE"));
					clinicList.add(clinicItem);
				}
				CLINIC_rs.close();
				
				TranLogTxt.liswriteEror_to_txt(logname, "3，查询价表");
				String select_PRICE_LIST_sql = "select * from PRICE_LIST";
				ResultSet PRICE_rs = his_connect.createStatement().executeQuery(select_PRICE_LIST_sql);
				List<HisPriceList> priceList = new ArrayList<>(30000);
				while(PRICE_rs.next()) {
					HisPriceList price = new HisPriceList();
					price.setItem_class(PRICE_rs.getString("ITEM_CLASS"));
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
					price.setStart_date(PRICE_rs.getTimestamp("START_DATE"));
					price.setStop_date(PRICE_rs.getTimestamp("STOP_DATE"));
					priceList.add(price);
				}
				PRICE_rs.close();
				
				TranLogTxt.liswriteEror_to_txt(logname, "4，查询价表诊疗关系");
				String select_CLINIC_PRICE_LIST_sql = "select * from CLINIC_VS_CHARGE";
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
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("连接his库失败");
			} finally {
				try {
					if (his_connect != null) {
						his_connect.close();
					}
				} catch (Exception e) {}
			}
			TranLogTxt.liswriteEror_to_txt(logname, "同步his基础数据===end===");
			
			rb.getResultHeader().setTypeCode("AA");
			rb.getResultHeader().setText("数据同步成功!");
		
		} catch (Throwable e) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("错误" + com.hjw.interfaces.util.StringUtil.formatException(e));
		}
		String xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + xml);
		return rb;
	}
}
