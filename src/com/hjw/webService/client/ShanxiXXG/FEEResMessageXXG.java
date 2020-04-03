package com.hjw.webService.client.ShanxiXXG;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.ShanxiXXG.bean.FeeResXXG;
import com.hjw.webService.client.ShanxiXXG.bean.ResBodyXXG;
import com.hjw.webService.client.dashiqiao.ResCusBean.CustomerExam;
import com.hjw.wst.DTO.ChargingSummarySingleDTO;
import com.hjw.wst.service.WebserviceConfigurationService;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class FEEResMessageXXG {

	private static JdbcQueryManager jdbcQueryManager;
	private static ConfigService configService;
	private static WebserviceConfigurationService webserviceConfigurationService;
	
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
		configService = (ConfigService) wac.getBean("configService");
		webserviceConfigurationService = (WebserviceConfigurationService) wac.getBean("webserviceConfigurationService");

	}
	
	public String getMessage(String strbody, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + strbody);
		Connection tjtmpconnect = null;
		String resmsg = "";
		String rescode = "0";
		
		if("".equals(strbody)||strbody==null) {
			resmsg = "error-入参不能为空！";
		}
		FeeResXXG feeres = new Gson().fromJson(strbody, FeeResXXG.class);
		if("".equals(feeres.getCharge_no())||feeres.getCharge_no()==null) {
			resmsg = "error-申请单号不能为空！";
		}
		if("".equals(feeres.getStatus())||feeres.getStatus()==null) {
			resmsg = "error-收费状态不能为空！";
		}else if(!"1".equals(feeres.getStatus())){
			resmsg = "error-收费状态不在范围内！";
		}
		
		try {
			
			ChargingSummarySingleDTO css = getReqNoStatus(feeres.getCharge_no(),logname);
			if(css!=null) {
				
				tjtmpconnect = this.jdbcQueryManager.getConnection();
				String sql = "select css.req_num,css.is_active,eci.id as eci_id,eci.charge_item_id,eci.examinfo_id,eci.pay_status from charging_summary_single css " + 
						"left join charging_detail_single cds on cds.summary_id = css.id,examinfo_charging_item eci " + 
						"where css.exam_id = eci.examinfo_id and cds.charging_item_id = eci.charge_item_id " + 
						"and css.req_num = '"+feeres.getCharge_no()+"' and eci.isActive = 'Y' ";
				TranLogTxt.liswriteEror_to_txt(logname, "sql 查项目信息： " + sql);
				ResultSet rs = tjtmpconnect.createStatement().executeQuery(sql);
				
				while(rs.next()) {
					long examinfo_id=rs.getLong("examinfo_id");
					long charge_item_id = rs.getLong("charge_item_id");
					long eci_id = rs.getLong("eci_id");
					String pay_status = rs.getString("pay_status");
					
					if("N".equals(pay_status)) {
						String updsql = "update examinfo_charging_item set pay_status = 'Y' where id = "+eci_id+" and examinfo_id = '"+examinfo_id+"' and charge_item_id = '"+charge_item_id+"'";
						
						TranLogTxt.liswriteEror_to_txt(logname, "sql 改收费状态： " + updsql);
						tjtmpconnect.createStatement().execute(updsql);
						
						rescode = "1";
						resmsg = "收费状态通知成功！";
					}
				}
				
			}else {
				resmsg = "error-无效的申请单号！";
			}
			
		} catch (Exception e) {
			rescode = "-1000";
			resmsg = "error-系统异常！";
			TranLogTxt.liswriteEror_to_txt(logname,"发送收费申请-调用错误Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
		} finally {
			if(tjtmpconnect != null) {
				try {
					tjtmpconnect.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		String resBody = getResBody(rescode,resmsg);
		TranLogTxt.liswriteEror_to_txt(logname,"res :"+resBody);
		return resBody;
	}
	
	public ChargingSummarySingleDTO getReqNoStatus(String req_num,String logname) {
		String sql = "select req_num from charging_summary_single where req_num = '"+req_num+"' and is_active = 'Y'";
		ChargingSummarySingleDTO css = new ChargingSummarySingleDTO();
		TranLogTxt.liswriteEror_to_txt(logname,"sql 查申请单号:"+sql);
		List<ChargingSummarySingleDTO> listCSS = this.jdbcQueryManager.getList(sql, ChargingSummarySingleDTO.class);
		if(listCSS.size()>0) {
			css = listCSS.get(0);
		}else {
			return null;
		}
		return css;
	}
	
	public String getResBody(String status,String msg) {
		ResBodyXXG resbody = new ResBodyXXG();
		resbody.setCode(status);
		resbody.setMsg(msg);
		String body = JSONObject.fromObject(resbody).toString();
		return body;
	}
	
}
