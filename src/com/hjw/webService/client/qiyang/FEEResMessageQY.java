package com.hjw.webService.client.qiyang;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.zhangyeshi.FEEResMessageZYS;
import com.hjw.webService.client.zhangyeshi.ZYSResolveXML;
import com.synjones.framework.persistence.JdbcQueryManager;

public class FEEResMessageQY {
	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
	}
	
	/**
	 * 获取单据收费状态
	 * @param url
	 * @param exam_num
	 * @param logName
	 * @return
	 */
	public ResultHeader getMessage(String url,String exam_num, String logName) {
		Connection tjtmpconnect = null;
		ResultHeader rh= new ResultHeader();
		
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "select a.exam_id,b.exam_num,a.req_num,a.id from charging_summary_single a,exam_info b where a.exam_id=b.id "
					+ "and b.is_Active='Y' "
					+ "and a.charging_status='R' "
					+ "and b.exam_num= '"+exam_num+"' ";
			TranLogTxt.liswriteEror_to_txt(logName, "查询 操作语句： " + sb1);
			ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb1);
			while (rs.next()) {
				long exam_id=rs.getLong("exam_id");
				long summary_id = rs.getLong("id");
				String req_num = rs.getString("req_num");
				
				
				
				
				TranLogTxt.liswriteEror_to_txt(logName, "传入参数===exam_id=== " + exam_id + "===summary_id=== "+ summary_id+ "==req_num=="+ req_num);
				
				
				
				
			}
			//关闭资源
			rs.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logName, "请求res:  操作失败===" + com.hjw.interfaces.util.StringUtil.formatException(ex));
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
		
		TranLogTxt.liswriteEror_to_txt(logName,"===getResultHeader().getTypeCode()==="+rh.getTypeCode());
		
		return rh;
	}
}
