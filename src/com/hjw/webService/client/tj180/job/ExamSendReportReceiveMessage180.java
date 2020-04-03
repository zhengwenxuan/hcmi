package com.hjw.webService.client.tj180.job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.SendReportReceive;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class ExamSendReportReceiveMessage180 {
	private static JdbcQueryManager jdbcQueryManager;
	static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	public void getMessage(String url,int days, String logname) {
		String sdatetime = DateTimeUtil.DateDiff2(days+1);
		String edatetime = DateTimeUtil.DateDiff2(days);
		Connection peis_connect = null;
		List<SendReportReceive> list = new ArrayList<SendReportReceive>();
		try {
			peis_connect = jdbcQueryManager.getConnection();
				String sql = "select d.exam_num,(case when r.receive_type is null then '0' else r.receive_type end) as receive_type1,"
						+ "uu.chi_name as printer,p.Report_Print_Date,r.receive_name,xx.chi_name as sender,d.e1date "
						+ " from exam_flow_config d  "
						+ "left join report_receive r on r.exam_num = d.exam_num "
						+ "left join user_usr xx on xx.id=d.e1creater "
						+ ",exam_info p "
						+ "left join user_usr uu on uu.id=p.Report_Print_UserId "
						+ "where d.exam_num=p.exam_num and d.e1 = 1 "
						+ " and CONVERT(varchar(50),d.e0date,23) >= '"+sdatetime+"'  "
						+ "and CONVERT(varchar(50),d.e0date,23) <= '"+edatetime+"' ";
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + sql+"\r\n");
				ResultSet rs = peis_connect.createStatement().executeQuery(sql);		
				while (rs.next()) {
					SendReportReceive s= new SendReportReceive();
					s.setReserveId(rs.getString("exam_num"));
					String receive_type1=rs.getString("receive_type1");
					if("0".equals(receive_type1)){ //1-本人；2-他人代领；3-邮寄
						s.setGetType("1");//"未邮寄,未自取");
					}else if("1".equals(receive_type1)){
						s.setGetType("3");//已邮寄");
					}else if("2".equals(receive_type1)){
						s.setGetType("1");//"已自取");
					}else{
						s.setGetType("1");//"已自取");
					}
					s.setRptOperator(rs.getString("printer"));
					s.setRptPrintDate(rs.getString("rptPrintDate"));
					s.setRptGetOperator(rs.getString("receive_name"));
					s.setSender(rs.getString("sender"));
					s.setSendDateTime(rs.getString("e1date"));
					list.add(s);
				}
				rs.close();
				
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:" +com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			try {			
				if (peis_connect != null) {
					peis_connect.close();
				}
				
			} catch (Exception sqle4) {
				sqle4.printStackTrace();
			}
		}
		for(SendReportReceive s:list){
			JSONObject json = JSONObject.fromObject(list);// 将java对象转换为json对象
			String str = json.toString();// 将json对象转换为字符串
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + str);
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + url.trim());
			String result = HttpUtil.doPost(url.trim(),s,"utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "res:" + result+"\r\n");
		}
	}
}