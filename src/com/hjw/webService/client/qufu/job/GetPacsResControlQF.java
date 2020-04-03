package com.hjw.webService.client.qufu.job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.service.ConfigService;
import com.hjw.util.DateTimeUtil;

import com.hjw.webService.client.Bean.ThridInterfaceLog;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultPacsBody;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetPacsResControlQF {
   private static JdbcQueryManager jdbcQueryManager;
   private static ConfigService configService;
   private ThridInterfaceLog til;
   
   static{
   		init();
   	}

	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}

	public ResultHeader getMessage(String url, int days) {
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setMessage_name("PACS_READ");
    	til.setMessage_type("webservice");
    	til.setSender("PEIS");
    	til.setReceiver("PF");
    	til.setFlag(2);
    	til.setXtgnb_id("2");//程序自动，设置为2-登记台首页
    	til.setMessage_inout(0);
		configService.insert_log(til);
		
		String datetime = DateTimeUtil.DateDiff2(days);
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select DISTINCT ei.exam_num, ps.pacs_req_code "
					+ " from exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd,pacs_summary ps,pacs_detail pd "
					+ " where ei.id=eci.examinfo_id and ei.is_Active='Y' and eci.isActive='Y' "
					+ " and eci.exam_status='C' "
					+ " and eci.charge_item_id=ci.id and dd.dep_category='21' and eci.pay_status <>'M' and ci.item_category = '普通类型' and ci.dep_id=dd.id "
					+ " and ei.status <> 'Z' "
					+ " and ps.examinfo_num = ei.exam_num"
					+ " and ps.id = pd.summary_id and pd.chargingItem_num = ci.item_code "
					+ " and dd.dep_num not in ('EL','BLK') "
					+ " and CONVERT(varchar(50),ei.join_date,23)>= '" + datetime + "' order by ei.exam_num ";
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), sb1);
			List<String> exam_nums=new ArrayList<String>();
			List<String> req_codes=new ArrayList<String>();
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs1.next()) {
				exam_nums.add(rs1.getString("exam_num"));
				req_codes.add(rs1.getString("pacs_req_code"));
			}
			rs1.close();
			int count = 0;
			for(int i=0; i<exam_nums.size(); i++){
				String exam_num = exam_nums.get(i);
				String req_code = req_codes.get(i);
				PACSResMessageQF prm = new PACSResMessageQF();
				ResultPacsBody rb = prm.getMessage(url, exam_num, req_code);
				if("AA".equals(rb.getResultHeader().getTypeCode())) {
					count++;
				}
			}
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:本次共执行"+exam_nums.size()+"条，其中成功"+count+"条");
			rh.setTypeCode("AA");
			rh.setText("");
		} catch (SQLException ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		if("AA".equals(rh.getTypeCode())) {
			til.setFlag(0);
		} else if("AE".equals(rh.getTypeCode())) {
			til.setFlag(2);
		}
		til.setSys_respones(rh.getText());
		configService.update_log(til);
		return rh;
	}
	
	/**
	 * 心电入库
	 * @param url
	 * @param days
	 * @return
	 */
	public ResultHeader getMessageXD(String url, int days) {
		til = new ThridInterfaceLog();
    	til.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    	til.setMessage_name("XD_READ");
    	til.setMessage_type("webservice");
    	til.setSender("PEIS");
    	til.setReceiver("PF");
    	til.setFlag(2);
    	til.setXtgnb_id("2");//程序自动，设置为2-登记台首页
    	til.setMessage_inout(0);
		configService.insert_log(til);
		
		String datetime = DateTimeUtil.DateDiff2(days);
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select DISTINCT ei.exam_num, ps.pacs_req_code "
					+ " from exam_info ei,examinfo_charging_item eci,charging_item ci,department_dep dd,pacs_summary ps,pacs_detail pd "
					+ " where ei.id=eci.examinfo_id and ei.is_Active='Y' and eci.isActive='Y' "
					+ " and eci.exam_status='C' "
					+ " and eci.charge_item_id=ci.id and dd.dep_category='21' and eci.pay_status <>'M' and ci.item_category = '普通类型' and ci.dep_id=dd.id "
					+ " and ei.status <> 'Z' "
					+ " and ps.examinfo_num = ei.exam_num "
					+ " and ps.id = pd.summary_id and pd.chargingItem_num = ci.item_code "
					+ " and dd.dep_num='EL' "
					+ " and CONVERT(varchar(50),ei.join_date,23)>= '" + datetime + "' order by ei.exam_num ";
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			List<String> exam_nums=new ArrayList<String>();
			List<String> req_codes=new ArrayList<String>();
			while (rs1.next()) {
				exam_nums.add(rs1.getString("exam_num"));
				req_codes.add(rs1.getString("pacs_req_code"));
			}
			rs1.close();
			int count = 0;
			for(int i=0; i<exam_nums.size(); i++) {
				String exam_num = exam_nums.get(i);
				String req_code = req_codes.get(i);
				XdResMessageQF prm = new XdResMessageQF();
				ResultPacsBody rb = prm.getMessage(url, exam_num, req_code);
				if("AA".equals(rb.getResultHeader().getTypeCode())) {
					count++;
				}
			}
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:本次共执行"+exam_nums.size()+"条，其中成功"+count+"条");
			rh.setTypeCode("AA");
			rh.setText("");
		} catch (SQLException ex) {
			configService.insert_message_log(til.getId(), til.getMessage_seq_code(), "res:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
			rh.setTypeCode("AE");
			rh.setText(com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		
		if("AA".equals(rh.getTypeCode())) {
			til.setFlag(0);
		} else if("AE".equals(rh.getTypeCode())) {
			til.setFlag(2);
		}
		til.setSys_respones(rh.getText());
		configService.update_log(til);
		return rh;
	}
}
