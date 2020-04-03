package com.hjw.webService.client.zhangyeshi;

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
import com.synjones.framework.persistence.JdbcQueryManager;

public class FEEResMessageZYS {
	
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
				
				String danJuId = FEEResMessageZYS.getFeeId(req_num, logName); 
				
				//http://127.0.0.1:3336/getinfo.html
				//url = url + "?&BillID="+danJuId+"&InterfaceID=8";
				
				String param = "?&BillID="+danJuId+"&InterfaceID=8";
				
				TranLogTxt.liswriteEror_to_txt(logName, "发送路径URL===： " + url+param);
				
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("BillID", danJuId);
				para.put("InterfaceID", 8);
				
				String result = HttpUtil.doPost(url, para, "UTF-8");
				
				if ((result != null) && (result.trim().length() > 0)) {
					result = result.trim();
					//解析XML
					ZYSResolveXML ssl = new ZYSResolveXML();
					Map<String, String> mapXML = ssl.resolveXML(result, true);
					
					if("1".equals(mapXML.get("resultCode"))){
						String updatesql="update examinfo_charging_item set pay_status='Y' where id in"
								+ "( select b.id from charging_detail_single a,examinfo_charging_item b "
								+ "where a.summary_id='"+summary_id+"' and b.id=a.charging_item_id and b.examinfo_id='"+exam_id+"')";
						TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
						tjtmpconnect.createStatement().execute(updatesql);
						updatesql="update charging_summary_single set charging_status='Y' where id='"+summary_id+"'";
						TranLogTxt.liswriteEror_to_txt(logName, "res: :操作语句： " + updatesql);
						tjtmpconnect.createStatement().execute(updatesql);
						rh.setTypeCode("AA");
						rh.setText("更改状态成功");
					}else if("0".equals(mapXML.get("resultCode"))) { 
						rh.setTypeCode("AE");
						rh.setText(mapXML.get("resultMsg"));
					}else {
						rh.setTypeCode("AE");
						rh.setText("不识别的错误代码"+mapXML.get("resultMsg"));
					}
					
				} else {
					rh.setTypeCode("AE");
					rh.setText("返回结果错误："+result);
				}
				
				
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
	
	
	
	/**
	 * 查询返回结果的申请单号
	 * @param req_no
	 * @param logname
	 * @return
	 */
	public static String getFeeId(String req_no, String logname) {
		Connection tjtmpconnect = null;
		String req_id = "";
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String sb1 = "select zl_tjh from zl_req_patInfo where zl_mzh ='"+req_no+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "收费单据:操作语句： " + sb1);
			ResultSet rs1 = tjtmpconnect.createStatement().executeQuery(sb1);
			if (rs1.next()) {
				req_id = rs1.getString("zl_tjh");
			}
			rs1.close();
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "查询收费单据:操作失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return req_id;
	}

}
