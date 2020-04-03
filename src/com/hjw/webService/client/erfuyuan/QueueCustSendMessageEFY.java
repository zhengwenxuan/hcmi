package com.hjw.webService.client.erfuyuan;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.google.gson.Gson;
import com.hjw.interfaces.util.HttpUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.QueueCustomerBean;
import com.hjw.webService.client.body.QueueResBody;
import com.hjw.webService.client.erfuyuan.bean.Request_qmModifyItems;
import com.hjw.webService.client.erfuyuan.bean.Response_qmModifyItems;
import com.synjones.framework.persistence.JdbcQueryManager;

import net.sf.json.JSONObject;

public class QueueCustSendMessageEFY {
	
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    }
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	/**
	 * 客户项目动态新增删除功能
	 */
	public QueueResBody getMessage(String url2,QueueCustomerBean eu,String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "url:" + url2);
		QueueResBody rb = new QueueResBody();
		try {
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + JSONObject.fromObject(eu));
			Request_qmModifyItems request = get_qmModifyItems_Data(eu.getExam_id(), logname);
			if(StringUtil.isEmpty(request.getClientId())) {
				rb.setRestext("不调用排队接口");
				rb.setRescode("AA");
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
				return rb;
			}
			TranLogTxt.liswriteEror_to_txt(logname, "request:" + JSONObject.fromObject(request));
			String result = HttpUtil.doGet(url2, request.covertToMap(), "utf-8");
			TranLogTxt.liswriteEror_to_txt(logname, "result:" + result);
			Response_qmModifyItems response = new Gson().fromJson(result, Response_qmModifyItems.class);
			if ("0".equals(response.getStatus())) {
				rb.setRestext("成功");
				rb.setRescode("AA");
			}else if ("12001".equals(response.getStatus())) { 
				rb.setRestext("体检者ID不正确或不存在");
				rb.setRescode("AE");
			}else if ("12002".equals(response.getStatus())) { 
				rb.setRestext("返回失败");
				rb.setRescode("AE");
			}else {
				rb.setRestext("不识别的错误代码："+response.getStatus());
				rb.setRescode("AE");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setRestext(com.hjw.interfaces.util.StringUtil.formatException(ex));
			rb.setRescode("AE");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "res:" + JSONObject.fromObject(rb));
		return rb;
	}
	
	private Request_qmModifyItems get_qmModifyItems_Data(long exam_id, String logname) {
		Connection tjtmpconnect = null;
		Request_qmModifyItems request= new Request_qmModifyItems();
		try {
			tjtmpconnect = jdbcQueryManager.getConnection();
			String add_sql = " SELECT ei.exam_num, ci.item_name "
					+ " FROM exam_info ei "
					+ " LEFT JOIN examinfo_charging_item eci ON ei.id = eci.examinfo_id "
					+ " LEFT JOIN charging_item ci ON eci.charge_item_id = ci.id "
					+ " WHERE ( eci.pay_status IN ('Y', 'R') OR ( ei.is_after_pay = 'Y' AND eci.pay_status IN ('Y', 'R', 'N')))"
					+ " AND eci.isActive = 'Y' "
					+ " AND ci.item_category != '耗材类型' "
					+ " AND ei.id = "+exam_id;
			TranLogTxt.liswriteEror_to_txt(logname, "add_sql： " +add_sql);
			ResultSet add_rs = tjtmpconnect.createStatement().executeQuery(add_sql);
			String itemsAdd = "";
			while (add_rs.next()) {
				if(StringUtil.isEmpty(request.getClientId())) {
					request.setClientId(add_rs.getString("exam_num"));
				}
				itemsAdd += (add_rs.getString("item_name") + ";");
			}
			add_rs.close();
			
			if(itemsAdd.length() > 1) {
				itemsAdd = itemsAdd.substring(0, itemsAdd.length()-1);
				request.setItemsAdd(itemsAdd);
			}
			String remove_sql = " SELECT ei.exam_num, ci.item_name "
					+ " FROM exam_info ei "
					+ " LEFT JOIN examinfo_charging_item eci ON ei.id = eci.examinfo_id "
					+ " LEFT JOIN charging_item ci ON eci.charge_item_id = ci.id "
					+ " WHERE eci.pay_status IN ('N', 'R', 'M')"
					+ " AND eci.isActive = 'N' "
					+ " AND ci.item_category != '耗材类型' "
					+ " AND ei.id = "+exam_id;
			TranLogTxt.liswriteEror_to_txt(logname, "remove_sql： " +remove_sql);
			ResultSet remove_rs = tjtmpconnect.createStatement().executeQuery(remove_sql);
			String itemsRemove = "";
			while (remove_rs.next()) {
				if(StringUtil.isEmpty(request.getClientId())) {
					request.setClientId(remove_rs.getString("exam_num"));
				}
				itemsRemove += (remove_rs.getString("item_name") + ";");
			}
			remove_rs.close();
			if(itemsRemove.length() > 1) {
				itemsRemove = itemsRemove.substring(0, itemsRemove.length()-1);
				request.setItemsRemove(itemsRemove);
			}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "Exception:" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return request;
	}
}
